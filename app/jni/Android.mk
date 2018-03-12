LOCAL_PATH := $(call my-dir)

#include $(CLEAR_VARS)
#LOCAL_MODULE    := rfpayshare
#LOCAL_SRC_FILES := libposoffline.so
#include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
TARGET_PLATFORM := android-3
LOCAL_MODULE    := rfpay
LOCAL_SRC_FILES := aes.cpp \
                   Base64.cpp \
                   rf_qrcode_pay.cpp
LOCAL_LDLIBS    := -llog
LOCAL_ARM_MODE  := arm
#LOCAL_SHARED_LIBRARIES := rfpayshare

include $(BUILD_SHARED_LIBRARY)