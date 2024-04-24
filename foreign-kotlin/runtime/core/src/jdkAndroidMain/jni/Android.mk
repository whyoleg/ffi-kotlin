LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE    := foreign-runtime-core-jni
LOCAL_SRC_FILES := $(LOCAL_PATH)/../../jdkMain/c/jni.c
include $(BUILD_SHARED_LIBRARY)
