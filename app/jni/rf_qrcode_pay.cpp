#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <time.h>

#include "rf_qrcode_pay.h"
#include "aes.h"
#include "Base64.h"

static const char *TAG="rf_qrcode_pay";
static const char* key = "Pu8/KhwqHOAermHzQq734g==";
static const char* KEYFREE = "KEYFREE:";

int isValid(const char* qrcode);
int spilt_qrcode(const char* qrcode,unsigned char* ciphertext,unsigned char* randData);
void get_real_key(const unsigned char* randData,unsigned char* real_key);
void get_time(const unsigned char* timeData,time_t* time);
jstring get_hexStr(JNIEnv *env,const char* chars);

JNIEXPORT jstring JNICALL Java_cn_com_reformer_pos_jni_RfQRCodePay_getCard
  (JNIEnv *env, jclass jc, jstring jsqrcode)
  {
       int ret = 0;
	  jstring jret;
       unsigned char real_key[16];
       unsigned char ciphertext[32];
       unsigned char rand[4];
       unsigned char cleartext[32];
       unsigned char ciphertext_unit[16];
       unsigned char cleartext_unit[16];
       int index = 0;
       unsigned char cardNum[16];
       unsigned char timeData[8];
	  time_t timeStart = 0;
	  time_t timeEnd = 0;
       time_t timeCur;
       char buf[81];
	  const char *str_qrcode = env->GetStringUTFChars(jsqrcode, NULL);
	  time(&timeCur);

       if(spilt_qrcode(str_qrcode,ciphertext,rand))
       {
       	   get_real_key(rand,real_key);
       	   for(index=0;index<2;index++)
           {
           	   memcpy(ciphertext_unit,&ciphertext[index*16],16);
           	   AES128_ECB_decrypt(ciphertext_unit,real_key,cleartext_unit);
           	   memcpy(&cleartext[index*16],cleartext_unit,16);
           }
		   const char txt[] = {0x10,0xFD,0x4B,0x3F};
		   jret = get_hexStr(env,txt);
           memcpy(cardNum,&cleartext[0],16);
           memcpy(timeData,&cleartext[16],8);
           get_time(timeData,&timeStart);
           memcpy(timeData,&cleartext[24],8);
           get_time(timeData,&timeEnd);
		   printf("==>timeCur: %d",(unsigned int)timeCur);
		   ret = (unsigned int)timeStart;
//           if((unsigned int)timeCur < timeStart)
//           {
//           	   ret = -2; //invalid local time
//           }else if((unsigned int)timeCur > timeEnd)
//		   {
//			   ret = timeStart; //qrcode time expire
//		   }
       }else{
           ret = -1; //invalid qrcode
       }

//       jobject mVerifyResponse;
//       jclass objRfPayResponse = env->FindClass(env, "RfPayResponse");
//       jmethodID iVerifyResponse = env->GetMethodID(env, objRfPayResponse, "<init>", "()V");
//       mVerifyResponse = env->NewObject(env, objRfPayResponse, iVerifyResponse);
//       jfieldID jfpayCard = env->GetFieldID(env, objRfPayResponse,"payCard","Ljava/lang/String;");
//       jfieldID jfresult = env->GetFieldID(env, objRfPayResponse,"result","I");
//      env->SetIntField(env, mVerifyResponse,jfresult,ret);
//       if(ret == 0){
//           env->SetObjectField(env,mVerifyResponse,jfpayCard, env->NewStringUTF(env, cardNum));
//       }else{
//           env->SetObjectField(env,mVerifyResponse,jfpayCard, env->NewStringUTF(env, ""));
//       }

       return jret;
}

int isValid(const char* qrcode)
{
	if(qrcode == NULL || strlen(qrcode) != 81)
		return -1;
	char bs[9];
	char code[72];
	memcpy(bs,qrcode,8);
	bs[8] = '\0';
	memcpy(code,&qrcode[9],72);
	if(strcmp(KEYFREE,bs) != 0)
		return -1;
	int i = 0;
	for(i=0;i<71;i++){
		if((code[i] >= '0' && code[i] <= '9')
			|| (code[i] >= 'a' && code[i] <= 'f')
			|| (code[i] >= 'A' && code[i] <= 'F')){
			continue;
		}else{
			return -1;
		}
	}

	return 0;
}

int spilt_qrcode(const char* qrcode,unsigned char* ciphertext,unsigned char* randData)
{
	if(0 != isValid(qrcode))
		return -1;
	if(ciphertext == NULL || strlen((char*)ciphertext) < 32 )
		return -1;
	if(randData == NULL || strlen((char*)randData) < 4 )
		return -1;
	char c[64];
	char r[8];
	char temp[2];
	int num;
	int i = 0;
	memcpy(c,&qrcode[9],64);
	memcpy(r,&qrcode[73],8);
	for(i=0 ; i<64; i+=2)
	{
		temp[0] = c[i];
		temp[1] = c[i+1];
		sscanf(temp,"%x",&num);
		ciphertext[i/2] = num & 0xff;
	}
	for(i=0 ; i<8; i+=2)
	{
		temp[0] = r[i];
		temp[1] = r[i+1];
		sscanf(temp,"%x",&num);
		randData[i/2] = num & 0xff;
	}
	return 0;
}

void get_real_key(const unsigned char* randData,unsigned char* real_key)
{
	int index = 0;
	unsigned int len = strlen(key);
	unsigned char* pre_key = base64Decode((char*)key,len);
	memcpy(real_key,pre_key,len);
	len = strlen((char*)real_key);
	int loc=0;
	for(index = 0;index < 4;index++)
	{
		loc = index * 4 + (randData[index] & 0XFF) % 4;
		real_key[loc] = ((real_key[loc] ^ randData[index]) & 0xFF);
	}
}

void get_time(const unsigned char* timeData,time_t* time)
{
	char temp;
	unsigned int num = 0;
	*time = 0;
	for(int i=0 ; i<8; i++) {
		temp = timeData[i];
		sscanf(&temp, "%x", &num);
		*time = ((num & 0xff) << (28 - i * 4)) + *time;
	}
}

jstring get_hexStr(JNIEnv *env,const char* chars)
{
	size_t len = strlen(chars) + 1;
	char* temp = (char*)malloc(len+1);
	int num = 0;
	for (int i = 0; i < (int)len; ++i) {
		num = (int)chars[i];
		sprintf(&temp[i],"%x",num);
	}
	jstring str = env->NewStringUTF(temp);
	delete[] temp;
	return str;
}