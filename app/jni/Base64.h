//
// Created by Administrator on 2016-11-22.
//

#ifndef BQDEMO_BASE64_H
#define BQDEMO_BASE64_H


unsigned char* base64Decode(char* in, unsigned int resultSize);
    // returns a newly allocated array - of size "resultSize" - that
    // the caller is responsible for delete[]ing.

char* base64Encode(char const* orig, unsigned origLength);

#endif //BQDEMO_BASE64_H
