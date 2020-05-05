#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <ESP8266HTTPClient.h>
#include <ArduinoJson.h>
#include <EEPROM.h>
#include <Ticker.h>
#include "H_project.h"
#include <IRremoteESP8266.h>
#include <IRsend.h>

#define MAGIC_NUMBER 0xAA

int test=5;

const int IRpin = 4;  //GPIO 4对应esp8266 D2脚
IRsend irsend(IRpin);  // 设置该脚发送红外信号
const uint64_t IR_signal_1=0x4DB23BC4; //机顶盒开关机
const uint64_t IR_signal_2=0x44BBA15E; //节目加
const uint64_t IR_signal_3=0x44BB619E; //节目减
const uint64_t IR_signal_4=0x44BB01FE; //音量加
const uint64_t IR_signal_5=0x44BB817E; //音量减
const uint64_t IR_signal_6=0xFDB04F; //电视开关机
const uint64_t IR_signal_u=0x4DB253AC; //上
const uint64_t IR_signal_d=0x4DB24BB4; //下
const uint64_t IR_signal_l=0x4DB29966; //左
const uint64_t IR_signal_r=0x4DB2837C; //右
const uint64_t IR_signal_ok=0x4DB2738C; //ok
const uint64_t IR_signal_b=0x44BBA956; //返回

int state;
WiFiClient espClient;

//声明方法
void initSystem();
void initOneNetMqtt();
void callback(char* topic, byte* payload, unsigned int length);
void saveConfig();
void loadConfig();
bool parseRegisterResponse();

/**
 * 初始化
 */
void setup() {
  irsend.begin();
  initSystem();
  initOneNetMqtt();
}

void loop() {
  ESP.wdtFeed();
  state = connectToOneNetMqtt();
  if(state == ONENET_RECONNECT){
     //重连成功 需要重新注册
     mqttClient.subscribe(TOPIC,1);
     mqttClient.loop();
  }else if(state == ONENET_CONNECTED){
     mqttClient.loop();
  }
  delay(2000);
}

void initSystem(){
    int cnt = 0;
    Serial.begin (115200);
    Serial.println("\r\n\r\nStart ESP8266 MQTT");
    Serial.print("Firmware Version:");
    Serial.println(VER);
    Serial.print("SDK Version:");
    Serial.println(ESP.getSdkVersion());
    wifi_station_set_auto_connect(0);//关闭自动连接
    ESP.wdtEnable(5000);
    WiFi.disconnect();
    delay(100);
    WiFi.begin(ssid, password);
    while (WiFi.status() != WL_CONNECTED) {
          delay(500);
          cnt++;
          Serial.print(".");
          if(cnt>=40){
            cnt = 0;
            //重启系统
            delayRestart(1);
          }
    }
    pinMode(LED_BUILTIN, OUTPUT);
    pinMode(test, OUTPUT);
    

    loadConfig();
    //还没有注册
    if(strcmp(config.deviceid,DEFAULT_ID) == 0){
        int tryAgain = 0;
        while(!registerDeviceToOneNet()){
          Serial.print(".");
          delay(500);
          tryAgain++;
          if(tryAgain == 5){
            //尝试5次
            tryAgain = 0;
            //重启系统
            delayRestart(1);
          }
        }
        if(!parseRegisterResponse()){
            //重启系统
            delayRestart(1);
            while(1);
        }
    }
}

void initOneNetMqtt(){
    mqttClient.setServer(mqttServer,mqttPort);
    mqttClient.setClient(espClient);
    mqttClient.setCallback(callback);

    initOneNet(PRODUCT_ID,API_KEY,config.deviceid);
}

void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
  }
  Serial.println();
  switch((char)payload[0])
  {
  case'0':digitalWrite(test,HIGH);break; //开灯
  case'1':irsend.sendNEC(IR_signal_1);break; //开关机顶盒
  case'2':irsend.sendNEC(IR_signal_2);break; //节目加
  case'3':irsend.sendNEC(IR_signal_3);break; //节目减
  case'4':irsend.sendNEC(IR_signal_4);break; //音量加
  case'5':irsend.sendNEC(IR_signal_5);break; //音量减
  case'6':irsend.sendNEC(IR_signal_6);break; //开关电视
  case'u':irsend.sendNEC(IR_signal_u);break; //上
  case'd':irsend.sendNEC(IR_signal_d);break; //下
  case'l':irsend.sendNEC(IR_signal_l);break; //左
  case'r':irsend.sendNEC(IR_signal_r);break; //右
  case'o':irsend.sendNEC(IR_signal_ok);break; //确认
  case'b':irsend.sendNEC(IR_signal_b);break; //返回
  case'7':digitalWrite(test,LOW);break; //关灯
  }
}

/*
 * 保存参数到EEPROM
*/
void saveConfig()
{
  Serial.println("Save OneNet config!");
  Serial.print("deviceId:");
  Serial.println(config.deviceid);

  EEPROM.begin(150);
  uint8_t *p = (uint8_t*)(&config);
  for (int i = 0; i < sizeof(config); i++)
  {
    EEPROM.write(i, *(p + i));
  }
  EEPROM.commit();
}

/*
 * 从EEPROM加载参数
*/
void loadConfig()
{
  EEPROM.begin(150);
  uint8_t *p = (uint8_t*)(&config);
  for (int i = 0; i < sizeof(config); i++)
  {
    *(p + i) = EEPROM.read(i);
  }
  EEPROM.commit();
  if (config.magic != MAGIC_NUMBER)
  {
    strcpy(config.deviceid, DEFAULT_ID);
    config.magic = MAGIC_NUMBER;
    saveConfig();
    Serial.println("Restore config!");
  }
  Serial.println("-----Read config-----");
  Serial.print("deviceId:");
  Serial.println(config.deviceid);
  Serial.println("-------------------");
}

/**
 * 解析注册返回结果
 */
bool parseRegisterResponse(){
   Serial.println("start parseRegisterResponse");
   StaticJsonBuffer<200> jsonBuffer;
     // StaticJsonBuffer 在栈区分配内存   它也可以被 DynamicJsonBuffer（内存在堆区分配） 代替
     // DynamicJsonBuffer  jsonBuffer;
   JsonObject& root = jsonBuffer.parseObject(response);

     // Test if parsing succeeds.
   if (!root.success()) {
       Serial.println("parseObject() failed");
       return false;
   }

   int errno = root["errno"];
   if(errno !=0){
       Serial.println("register failed!");
       return false;
   }else{
       Serial.println("register sucess!");
       strcpy(config.deviceid, root["data"]["device_id"]);
       saveConfig();
       return true;
   }
}
