LOCAL_PATH := $(call my-dir)
 
include $(CLEAR_VARS)
 
# Here we give our module name and source file(s)
LOCAL_MODULE    := piv2
LOCAL_SRC_FILES := piv2.c
 
include $(BUILD_SHARED_LIBRARY)