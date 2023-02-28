LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE    := ffi-jni
LOCAL_SRC_FILES := $(LOCAL_PATH)/../../jniMain/c/jni.c
include $(BUILD_SHARED_LIBRARY)
