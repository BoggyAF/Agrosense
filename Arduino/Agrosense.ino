#include <WiFi.h>
#include <FirebaseESP32.h>
#include <stdlib.h>
#include <Wire.h>
#include <SPI.h>
#include <Adafruit_BMP280.h>
 
#define co2in A0
#define humin A5
#define lumin A19
#define co2Zero 55

Adafruit_BMP280 bmp; // use I2C interface
Adafruit_Sensor *bmp_temp = bmp.getTemperatureSensor();
Adafruit_Sensor *bmp_pressure = bmp.getPressureSensor();
 
#define FIREBASE_HOST "https://agrosense-34344-default-rtdb.europe-west1.firebasedatabase.app/"
#define FIREBASE_AUTH "9muiaCo5PrH1EEAKaFfKaQv4pZnxQh4TaJ3ao39t"
#define WIFI_SSID "wifi_ssid"
#define WIFI_PASSWORD "wifi_password"
 
FirebaseData firebaseData;
FirebaseJson json;

const int dry = 595; //soil hum dry level
const int wet = 240; //soil hum dry level
const int light = 100; //luminosity high level PLACEHOLDER
const int dark = 0; //luminosity low level PLACEHOLDER

void setup()
{
  //pin setup
  pinMode(co2in,INPUT);
  pinMode(humin,INPUT);
  pinMode(lumin,INPUT);
  //checking sensors connectivity ONLY BMP280 FOR NOW
  if (!bmp.begin()) {
    Serial.println(F("Could not find a valid BMP280 sensor, check wiring!"));
    while (1) delay(10);
  }
  
  //Wire.begin(SDA,SCL) bmp280 wirirng; default sda =4 scl =5
  Serial.begin(115200);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");

  //waiting for wifi connection
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();
 
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);
 
  //Set database read timeout to 1 minute (max 15 minutes)
  Firebase.setReadTimeout(firebaseData, 1000 * 60);
  //tiny, small, medium, large and unlimited.
  //Size and its write timeout e.g. tiny (1s), small (10s), medium (30s) and large (60s).
  Firebase.setwriteSizeLimit(firebaseData, "tiny");
 
  Serial.println("------------------------------------");
  Serial.println("Connected...");


  /* Default settings from datasheet. */
  bmp.setSampling(Adafruit_BMP280::MODE_NORMAL,     /* Operating Mode. */
                  Adafruit_BMP280::SAMPLING_X2,     /* Temp. oversampling */
                  Adafruit_BMP280::SAMPLING_X16,    /* Pressure oversampling */
                  Adafruit_BMP280::FILTER_X16,      /* Filtering. */
                  Adafruit_BMP280::STANDBY_MS_500); /* Standby time. */

  bmp_temp->printSensorDetails();
}
 
void loop()
{
  delay(100);

  //bmp value transfer
  sensors_event_t temp_event, pressure_event;
  bmp_temp->getEvent(&temp_event);
  bmp_pressure->getEvent(&pressure_event);

  //co2 stabilization
  int co2now[10]; //int array for co2 readings
  int co2raw = 0; //int for raw value of co2
  int co2ppm = 0; //int for calculated ppm
  int co2sum = 0; //int for averaging

  for (int x = 0;x<10;x++) //samplpe co2 10x over 2 seconds
  {                   
    co2now[x] = analogRead(co2in);
    delay(200);
  }

  for (int x = 0;x<10;x++)  //add samples together
  {                     
    co2sum=co2sum + co2now[x];  
  }
  
  co2raw = co2sum/10; //divide samples by 10
  co2ppm = co2raw - co2Zero; //get calculated ppm

  //soil humid % conversion
  int humidityValue = analogRead(humin);
  int humidPercentage = map(humidityValue, wet, dry, 100, 0);

  //lumin % conversion
  int luminosityValue = analogRead(lumin);
  int luminPercentage = map(luminosityValue, light, dark, 100, 0);
  
  //value printing
  Serial.print("Temperature (C):");
  Serial.println(temp_event.temperature);

  Serial.print("Pressure (idk):");
  Serial.println(pressure_event.pressure);
  
  Serial.print("CO2 (PPM):");
  Serial.println(co2ppm);
  
  Serial.print("Soil Humidity (%):");
  Serial.println(humidPercentage);

  Serial.print("Luminosity (%):");
  Serial.println(luminPercentage);
  
  Serial.println();
   
  delay(100); 

  //json building
  json.set("/Temperature", temp_event.temperature);
  json.set("/CO2", co2ppm);
  json.set("/Pressure", pressure_event.pressure);
  json.set("/Luminosity", luminPercentage);
  json.set("/Humidity", humidPercentage);
  
  Firebase.updateNode(firebaseData,"/Sensor",json);
 
}
