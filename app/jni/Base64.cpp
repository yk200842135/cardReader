//
// Created by Administrator on 2016-11-22.
//
#include "base64.h"

#include<stdlib.h>
#include<string.h>

static char base64DecodeTable[256];
char* strDupSize(char const* str);


char* strDupSize(char const* str)
{
if (str == NULL) return NULL;
size_t len = strlen(str) + 1;
char* copy = (char*)malloc(len+1);
return copy;
}



static void initBase64DecodeTable()
{
  int i;
  for (i = 0; i < 256; ++i) base64DecodeTable[i] = (char)0x80;
      // default value: invalid

  for (i = 'A'; i <= 'Z'; ++i) base64DecodeTable[i] = 0 + (i - 'A');
  for (i = 'a'; i <= 'z'; ++i) base64DecodeTable[i] = 26 + (i - 'a');
  for (i = '0'; i <= '9'; ++i) base64DecodeTable[i] = 52 + (i - '0');
  base64DecodeTable[(unsigned char)'+'] = 62;
  base64DecodeTable[(unsigned char)'/'] = 63;
  base64DecodeTable[(unsigned char)'='] = 0;
}

unsigned char* base64Decode(char* in, unsigned int resultSize)
{
 bool trimTrailingZeros = true;
  static bool haveInitedBase64DecodeTable = false;
  if (!haveInitedBase64DecodeTable)
  {
    initBase64DecodeTable();
    haveInitedBase64DecodeTable = true;
  }

  unsigned char* out = (unsigned char*)strDupSize(in); // ensures we have enough space
  int k = 0;
  int const jMax = strlen(in) - 3;
     // in case "in" is not a multiple of 4 bytes (although it should be)
  for (int j = 0; j < jMax; j += 4)
  {
    char inTmp[4], outTmp[4];
    for (int i = 0; i < 4; ++i)
{
      inTmp[i] = in[i+j];
      outTmp[i] = base64DecodeTable[(unsigned char)inTmp[i]];
      if ((outTmp[i]&0x80) != 0) outTmp[i] = 0; // pretend the input was 'A'
    }

    out[k++] = (outTmp[0]<<2) | (outTmp[1]>>4);
    out[k++] = (outTmp[1]<<4) | (outTmp[2]>>2);
    out[k++] = (outTmp[2]<<6) | outTmp[3];
  }

  if (trimTrailingZeros)
  {
    while (k > 0 && out[k-1] == '\0') --k;
  }
  resultSize = k;

  unsigned char* result = new unsigned char[resultSize +1];
  memset(result, 0x0, resultSize + 1);
  memmove(result, out, resultSize);
  delete[] out;

  return result;
}

static const char base64Char[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

char* base64Encode(char const* origSigned, unsigned origLength)
{
  unsigned char const* orig = (unsigned char const*)origSigned; // in case any input bytes have the MSB set
  if (orig == NULL) return NULL;

  unsigned const numOrig24BitValues = origLength/3;
  bool havePadding = origLength > numOrig24BitValues*3;
  bool havePadding2 = origLength == numOrig24BitValues*3 + 2;
  unsigned const numResultBytes = 4*(numOrig24BitValues + havePadding);
  char* result = new char[numResultBytes+1]; // allow for trailing '/0'

  // Map each full group of 3 input bytes into 4 output base-64 characters:
  unsigned i;
  for (i = 0; i < numOrig24BitValues; ++i)
  {
    result[4*i+0] = base64Char[(orig[3*i]>>2)&0x3F];
    result[4*i+1] = base64Char[(((orig[3*i]&0x3)<<4) | (orig[3*i+1]>>4))&0x3F];
    result[4*i+2] = base64Char[((orig[3*i+1]<<2) | (orig[3*i+2]>>6))&0x3F];
    result[4*i+3] = base64Char[orig[3*i+2]&0x3F];
  }

  // Now, take padding into account.  (Note: i == numOrig24BitValues)
  if (havePadding)
  {
    result[4*i+0] = base64Char[(orig[3*i]>>2)&0x3F];
    if (havePadding2)
{
      result[4*i+1] = base64Char[(((orig[3*i]&0x3)<<4) | (orig[3*i+1]>>4))&0x3F];
      result[4*i+2] = base64Char[(orig[3*i+1]<<2)&0x3F];
    }
else
{
      result[4*i+1] = base64Char[((orig[3*i]&0x3)<<4)&0x3F];
      result[4*i+2] = '=';
    }
    result[4*i+3] = '=';
  }

  result[numResultBytes] = 0x0;
  return result;
}
