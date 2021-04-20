#include <common.h>
#include <FirebaseESP32.h>
#include <FirebaseFS.h>
#include <Utils.h>
#include <WiFi.h>
#include "time.h"
#include <SPI.h>
#include <Adafruit_BMP280.h>

FirebaseData firebaseData;
FirebaseJson json;
FirebaseJson jsonH;
Adafruit_BMP280 bmp;

#define FIREBASE_HOST "https://agrosense-34344-default-rtdb.europe-west1.firebasedatabase.app/"
#define FIREBASE_AUTH "9muiaCo5PrH1EEAKaFfKaQv4pZnxQh4TaJ3ao39t"
//#define WIFI_SSID "TP-Link_CB81"
//#define WIFI_PASSWORD "14311123"
#define WIFI_SSID "Bogdan"
#define WIFI_PASSWORD "vatradornei"

#define co2in 39
#define lumin 35
#define humin 0
#define relay1 26
#define relay2 25

#define co2Zero 1300
#define COOLDOWN 2
const int light = 0;
const int dark = 4095;
const int wet = 0;
const int dry = 4095;

const char* ntpServer = "pool.ntp.org";
const long  gmtOffset_sec = 3600;
const int   daylightOffset_sec = 3600;
int min_historic_interval = 0;
int max_historic_interval = COOLDOWN;
int starting_hm = 0;

float temp_day_avg;
float press_day_avg;
float co2_day_avg;
float lum_day_avg;
float hum_day_avg;
float temphour[24];
float presshour[24];
float co2hour[24];
float lhour[24];
float humhour[24];

float user_luminosity;
float user_humidity;
float relay1State;
float relay2State;

void resetAvg(void);
float relayCheck(String node_path);
String get_time(String x);

void setup()
{
  pinMode(relay1,OUTPUT);
  pinMode(relay2,OUTPUT);
  pinMode(co2in, INPUT);
  pinMode(lumin, INPUT);
  pinMode(humin, INPUT);

  Serial.begin(115200);
  //connect to WiFi
  Serial.printf("Connecting to %s ", WIFI_SSID);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println(" CONNECTED");

  bool status;
  status = bmp.begin(); 
  if (!status) 
  {
      Serial.println("Could not find a valid BME280 sensor, check wiring!");
      while (1);
  }
   
  configTime(gmtOffset_sec, daylightOffset_sec, ntpServer);
  Serial.println(get_time("full"));
  starting_hm = get_time("hm").toInt();
  Serial.println(starting_hm);
  if (starting_hm)
  {
    min_historic_interval += starting_hm - 3 * COOLDOWN;
    max_historic_interval += starting_hm - 2 * COOLDOWN;
  }
  else
  {
    min_historic_interval = 2354;
    max_historic_interval = 2356;
  }
  Serial.println(min_historic_interval);
  Serial.println(max_historic_interval);

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);

}

void loop()
{
  
  float co2ppm;
  float luminosityPercentage;
  float humidityPercentage;
  float temperature;
  float pressure;

  temperature = bmp.readTemperature();
  pressure = bmp.readPressure() / 100.00F;
  
  //float temperature = random(-20, 100);
  //float pressure = random(200, 2000);

  for(int y=0; y<3; y++) //collecting 10 sambles for co2, lum, humid over ~3 sec
  {
    float nowarr[10];
    float nowraw;
    float nowsum = 0;
    for (int x = 0; x < 10; x++)
    {
      switch (y) {
        case 0:
          nowarr[x] = analogRead(co2in);
          break;
        case 1:
          nowarr[x] = analogRead(lumin);
          break;
        case 2:
          nowarr[x] = analogRead(humin);
          break;
        default:
          break;
      }
      nowsum += nowarr[x];
      
      delay(100);
    }

    nowraw = nowsum / 10;
    
    switch (y) {
        case 0:
          co2ppm = nowraw - co2Zero;
          break;
        case 1:
          luminosityPercentage = map(nowraw, light, dark, 100, 0);
          break;
        case 2:
          humidityPercentage = map(nowraw, wet, dry, 100, 0);
          break;
        default:
          break;
    }
     
  }

  if(relayCheck("/Sensors/Relays/relay1State")==1)
  {
    Serial.println("RELAY1 IF");
    user_luminosity = relayCheck("/Sensors/Relays/relay1Value");
    delay(200);

    if(user_luminosity<=luminosityPercentage)
        {
          Serial.println("LUM IF TRUE (<=)");
          digitalWrite(relay1, HIGH);
        }            
    else 
        {
          Serial.println("LUM IF FALSE (>=)");
          digitalWrite(relay1, LOW);
        }  
  }
  else if(relayCheck("/Sensors/Relays/relay1State")==0)
  {
    Serial.println("RELAY1 ELSE");
    digitalWrite(relay1, LOW);
  }

  if(relayCheck("/Sensors/Relays/relay2State")==1)
  {
    Serial.println("RELAY2 IF");
    user_humidity = relayCheck("/Sensors/Relays/relay2Value");
    delay(200);

    if(user_humidity<=humidityPercentage)
        {
          Serial.println("HUM IF TRUE (<=)");
          digitalWrite(relay2, HIGH);
        }  
    else 
        {
          Serial.println("HUM IF FALSE (>=)");
          digitalWrite(relay2, LOW);
        }
  }
  else if(relayCheck("/Sensors/Relays/relay2State")==1)
  {
    Serial.println("RELAY2 ELSE");
    digitalWrite(relay2, LOW);
  }

  json.set("/DailyMeasurements/Measurements/Timestamp", get_time("full"));
  json.set("/DailyMeasurements/Measurements/Temperature", String(temperature));
  json.set("/DailyMeasurements/Measurements/CO2", String(co2ppm));
  json.set("/DailyMeasurements/Measurements/Pressure", String(pressure));
  json.set("/DailyMeasurements/Measurements/Luminosity", String(luminosityPercentage));
  json.set("/DailyMeasurements/Measurements/Humidity", String(humidityPercentage));
  Firebase.updateNode(firebaseData, "/Sensors", json); 

  Serial.println(get_time("full"));
  Serial.println(firebaseData.jsonString());

  if (get_time("minutes").toInt() == min_historic_interval % 100)
  {
    temphour[get_time("hours").toInt()] = temperature;
    presshour[get_time("hours").toInt()] = pressure;
    co2hour[get_time("hours").toInt()] = co2ppm;
    lhour[get_time("hours").toInt()] = luminosityPercentage;
    humhour[get_time("hours").toInt()] = humidityPercentage;
  }

  //set HistoricMeasurements at [min_historic_interval,max_historic_interval] with measurements_avg over 24hrs
  if (get_time("hm").toInt() > min_historic_interval && get_time("hm").toInt() < max_historic_interval)
  {
    Serial.println("ENTERED HM IF");
    for (int x = 0; x < 24; x++)
    {
      temp_day_avg += temphour[x];
      press_day_avg += presshour[x];
      co2_day_avg += co2hour[x];
      lum_day_avg += lhour[x];
      hum_day_avg += humhour[x];
    }

    temp_day_avg /= 24;
    press_day_avg /= 24;
    co2_day_avg /= 24;
    lum_day_avg /= 24;
    hum_day_avg /= 24;

    int timeStamp = get_time("hms").toInt();
    String historic_index = get_time("ymd");

    jsonH.set("/Temperature", temp_day_avg);
    jsonH.set("/CO2", co2_day_avg);
    jsonH.set("/Pressure", press_day_avg);
    jsonH.set("/Luminosity", lum_day_avg);
    jsonH.set("/Humidity", hum_day_avg);
    jsonH.set("/Timestamp", timeStamp);

    Firebase.setJSON(firebaseData, "/Sensors/HistoricMeasurements/" + historic_index, jsonH);
    Serial.println(firebaseData.jsonString());

    resetAvg();

    delay(1000*COOLDOWN*60);
  }

}

String get_time(String x)
{
  struct tm timeinfo;
  time_t now;
  time(&now);
  char time_output[30];
  if (!getLocalTime(&timeinfo)) {
    //Serial.println("Failed to obtain time");
    return String("Failed time");
  }
  //Serial.println(&timeinfo, "%A, %B %d %Y %H:%M:%S");

  if (x == "full")
  {
    strftime(time_output, 30, "%d-%m-%y %T", localtime(&now));
  }
  if (x == "seconds")
  {
    strftime(time_output, 30, "%S", localtime(&now));
  }
  if (x == "minutes")
  {
    strftime(time_output, 30, "%M", localtime(&now));
  }
  if (x == "hours")
  {
    strftime(time_output, 30, "%H", localtime(&now));
  }
  if (x == "hm")
  {
    strftime(time_output, 30, "%H%M", localtime(&now));
  }
  if (x == "hms")
  {
    strftime(time_output, 30, "%H%M%S", localtime(&now));
  }
  if (x == "ymd")
  {
    strftime(time_output, 30, "%y%m%d", localtime(&now));
  }


  return time_output;
}

void resetAvg(void)
{
  for (int x = 0; x < 24; x++)
  {
    temphour[x] = 0;
    presshour[x] = 0;
    co2hour[x] = 0;
    lhour[x] = 0;
    humhour[x] = 0;
  }

  temp_day_avg = 0;
  press_day_avg = 0;
  co2_day_avg = 0;
  lum_day_avg = 0;
  hum_day_avg = 0;
}

float relayCheck(String node_path)
{
  if(Firebase.getString(firebaseData, node_path))
      {
        return firebaseData.stringData().toFloat();
      }else{
        Serial.print("Error in getString, ");
        Serial.println(firebaseData.errorReason());
        return relayCheck(node_path);
      }
}
