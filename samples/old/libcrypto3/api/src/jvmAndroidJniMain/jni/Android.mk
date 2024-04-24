LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE    := foreign-crypto-jni
LOCAL_SRC_FILES := $(LOCAL_PATH)/../../jvmJniMain/c/jni.c
LOCAL_C_INCLUDES:= $(LOCAL_PATH)/../../../build/androidHeaders/$(TARGET_ARCH_ABI)/include
LOCAL_LDLIBS := -L$(LOCAL_PATH)/../../../build/androidLibraries/$(TARGET_ARCH_ABI) -lcrypto
include $(BUILD_SHARED_LIBRARY)
