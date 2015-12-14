LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := webcam
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_LDLIBS := \
	-landroid \
	-llog \
	-ljnigraphics \

LOCAL_SRC_FILES := \
	/Users/judychang/Development/CamCulture/OralImaging/webcam/src/main/jni/Android.mk \
	/Users/judychang/Development/CamCulture/OralImaging/webcam/src/main/jni/Application.mk \
	/Users/judychang/Development/CamCulture/OralImaging/webcam/src/main/jni/capture.c \
	/Users/judychang/Development/CamCulture/OralImaging/webcam/src/main/jni/util.c \
	/Users/judychang/Development/CamCulture/OralImaging/webcam/src/main/jni/video_device.c \
	/Users/judychang/Development/CamCulture/OralImaging/webcam/src/main/jni/webcam.c \
	/Users/judychang/Development/CamCulture/OralImaging/webcam/src/main/jni/yuv.c \

LOCAL_C_INCLUDES += /Users/judychang/Development/CamCulture/OralImaging/webcam/src/main/jni
LOCAL_C_INCLUDES += /Users/judychang/Development/CamCulture/OralImaging/webcam/src/debug/jni

include $(BUILD_SHARED_LIBRARY)
