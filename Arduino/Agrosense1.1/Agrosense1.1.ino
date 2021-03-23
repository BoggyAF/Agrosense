#include <common.h>
#include <FirebaseESP32.h>
#include <FirebaseFS.h>
#include <Utils.h>

#include <WiFi.h>
#include "time.h"

#define FIREBASE_HOST "https://agrosense-34344-default-rtdb.europe-west1.firebasedatabase.app/"
#define FIREBASE_AUTH "9muiaCo5PrH1EEAKaFfKaQv4pZnxQh4TaJ3ao39t"
#define WIFI_SSID "Barlogul"
#define WIFI_PASSWORD "straziledinbucuresti"

FirebaseData firebaseData;
FirebaseJson json;

const char* ntpServer = "pool.ntp.org";
const long  gmtOffset_sec = 3600;
const int   daylightOffset_sec = 3600;

String get_time()
{
  struct tm timeinfo;
  time_t now;
  time(&now);
  char time_output[30];
  if(!getLocalTime(&timeinfo)){
    //Serial.println("Failed to obtain time");
    return String("Failed time");
  }
  //Serial.println(&timeinfo, "%A, %B %d %Y %H:%M:%S");
  strftime(time_output, 30, "%d-%m-%y %T", localtime(&now)); 
  return String(time_output);
}

void setup()
{
  Serial.begin(115200);
  //connect to WiFi
  Serial.printf("Connecting to %s ", WIFI_SSID);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED) {
      delay(500);
      Serial.print(".");
  }
  Serial.println(" CONNECTED");
  
  //init and get the time
  configTime(gmtOffset_sec, daylightOffset_sec, ntpServer);
  Serial.println(get_time());

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);

}

void loop()
{
  float temperature = random(-20, 100);
  float co2 = random(200, 2000);
  float pressure = random(200, 2000);
  int luminosityPercentage = random(100);
  int humidityPercentage = random(100);
  
  delay(5000);
  Serial.println(get_time());
  json.set("/DailyMeasurements/Measurements/Date", get_time());
  json.set("/DailyMeasurements/Measurements/Temperature", String(temperature));
  json.set("/DailyMeasurements/Measurements/CO2", String(co2));
  json.set("/DailyMeasurements/Measurements/Pressure", String(pressure));
  json.set("/DailyMeasurements/Measurements/Luminosity", String(luminosityPercentage));
  json.set("/DailyMeasurements/Measurements/Humidity", String(humidityPercentage));

  /*
  int i = 4;
  
  json.set("/HistoricMeasurements/"+get_time()+"/Date", "day1_date");
  json.set("/HistoricMeasurements/"+get_time()+"/Temperature", "day1_temp_avg");
  json.set("/HistoricMeasurements/"+get_time()+"/CO2", "day1_co2_avg");
  json.set("/HistoricMeasurements/"+get_time()+"/Pressure", "day1_press_avg");
  json.set("/HistoricMeasurements/"+get_time()+"/Luminosity", "day1_lumin_avg");
  json.set("/HistoricMeasurements/"+get_time()+"/Humidity", "day1_humid_avg");
  
  json.set("/HistoricMeasurements/2/Date", "day2_date");
  json.set("/HistoricMeasurements/2/Temperature", "day2_temp_avg");
  json.set("/HistoricMeasurements/2/CO2", "day2_co2_avg");
  json.set("/HistoricMeasurements/2/Pressure", "day2_press_avg");
  json.set("/HistoricMeasurements/2/Luminosity", "day2_lumin_avg");
  json.set("/HistoricMeasurements/2/Humidity", "day2_humid_avg");
  */
 
  Firebase.updateNode(firebaseData,"/Sensors",json);
  Serial.println(firebaseData.jsonString());
}
