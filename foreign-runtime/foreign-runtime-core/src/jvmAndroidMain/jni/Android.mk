LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE    := foreign-kotlin-jni
LOCAL_SRC_FILES := $(LOCAL_PATH)/../../jvmCommonMain/c/jni.c
include $(BUILD_SHARED_LIBRARY)
