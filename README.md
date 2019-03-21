<img src="http://i.imgur.com/hXY4lcC.png" height="42px" alt="microG" /> Services Core (GmsCore) [NanoDroid]
=======

This is the [NanoDroid](https://gitlab.com/Nanolx/NanoDroid) fork of:

microG GmsCore is a FLOSS (Free/Libre Open Source Software) framework to allow applications designed for Google Play Services to run on systems, where Play Services is not available.

### Changes

What's different compared to the original microG GmsCore?

* currently spoofed Play Services version: 15.1.80

* built as priv-app, set multiArch
* fix build failing due missing com.vividsolutions:jts
* changed version code generation to ease frequent beta builds
  * append "(NanoDroid)" to version visible in UI
* fix build issues [by FreeMobileOS](https://github.com/FreeMobileOS/android_packages_apps_GmsCore/commit/95e839146d4f65a3ee2455a14f138514f2683124)
* Cast API work by armills [GmsCore](https://github.com/armills/android_packages_apps_GmsCore/tree/cast-mvp)
* set package of intent prior to broadcasting [by voidstarstar](https://github.com/voidstarstar/android_packages_apps_GmsCore/commit/6c1a479bb10229512183351133f1df43c4297236)
* built with [ThibG's fork of microG DroidGuard Helper](https://github.com/ThibG/android_packages_apps_RemoteDroidGuard)
* fix for [microG issue #560 by ale5000](https://github.com/ale5000-git/android_packages_apps_GmsCore/commits/patch-1)
* temporarily put applications into whitelist when high-priority GCM messages are received, by [voidstartar, ale5000 and ccaapton](https://github.com/microg/android_packages_apps_GmsCore/pull/732)
* [GoogleCloudMessaging getMessageType should never throw](https://github.com/yegortimoshenko/android_external_GmsLib/commit/fed94a84494a2a0ce1c15b465140e1ca3b0e591b) by yegortimoshenko
* improved russian translation by vavun [UnifiedNlp](https://github.com/Vavun/android_packages_apps_UnifiedNlp/commit/455e63b3ebaa6f8d595c4c40b39bac260b5a2e92), GmsCore [1](https://github.com/Vavun/android_packages_apps_GmsCore/commit/bb0e40e6390d230d2fd9031e7d3c6645ae178390) / [2](https://github.com/Vavun/android_packages_apps_GmsCore/tree/patch-1), [microGUiTools](https://github.com/Vavun/android_external_MicroGUiTools/commit/c7bb9018dd4f089b825684aa293c9f5646f65d64),
* [Light theme for Settings](https://github.com/microg/android_external_MicroGUiTools/pull/13) by cbviva
* [Fix settings crashes for ASUS devices](https://github.com/microg/android_external_MicroGUiTools/pull/11) by emv412
* add 'burger' menu providing additional information by oF2pks
  * 'last known location': latest knownLocation per providers (with powerRequirement int value)
  * '/system/etc/gps.conf': view gps.conf
  * 'Geolocation Map': World Map with current location (PlacePickerActivity)
  * 'Google Account': shortcut to AccountSettingsActivity (intent)
  * 'System specifics': Omnirom whitelist, <*.location> system packages (qualcomm, mediatek...), framework-res.apk config/overlay/location detection
  * 'Info': useful links for microG/NanoDroid
* add 'Add Google Account' and 'Test location' to microG settings by [vavun, ale5000](https://github.com/microg/android_packages_apps_GmsCore/pull/735)

### Download

The latest build can be downloaded [here](https://nanolx.org/fdroid/repo)

### Please refer to the [wiki](https://github.com/microg/android_packages_apps_GmsCore) for the original source

License
-------
    Copyright 2014-2016 microG Project Team

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
