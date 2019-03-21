# Copyright (c) 2015 μg Project Team
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := GmsCore
LOCAL_MODULE_TAGS := optional
LOCAL_PACKAGE_NAME := GmsCore

gmscore_root  := $(LOCAL_PATH)
gmscore_dir   := play-services-core
gmscore_out   := $(OUT_DIR)/target/common/obj/APPS/$(LOCAL_MODULE)_intermediates
gmscore_build := $(gmscore_root)/$(gmscore_dir)/build
gmscore_apk   := build/outputs/apk/release/play-services-core-release-unsigned.apk

.PHONY: preps_gms
preps_gms:
	rm -rf $(gmscore_build); \
	mkdir -p $(ANDROID_BUILD_TOP)/$(gmscore_out); \
	ln -s $(ANDROID_BUILD_TOP)/$(gmscore_out)/ $(ANDROID_BUILD_TOP)/$(gmscore_build)

$(gmscore_root)/$(gmscore_dir)/$(gmscore_apk): preps_gms
	echo "sdk.dir=$(ANDROID_HOME)" > $(gmscore_root)/local.properties; \
	cd $(gmscore_root) && git submodule update --recursive --init; \
	cd $(gmscore_dir) && JAVA_TOOL_OPTIONS="$(JAVA_TOOL_OPTIONS) -Dfile.encoding=UTF8" ../gradlew assembleRelease

LOCAL_CERTIFICATE := platform
LOCAL_PRIVILEGED_MODULE := true
LOCAL_SRC_FILES := $(gmscore_dir)/$(gmscore_apk)
LOCAL_AIDL_INCLUDES := $(LOCAL_PATH)/play-services-core/src/main/aidl
LOCAL_MODULE_CLASS := APPS
LOCAL_MODULE_SUFFIX := $(COMMON_ANDROID_PACKAGE_SUFFIX)
LOCAL_PRIVILEGED_MODULE := true

include $(BUILD_PREBUILT)
