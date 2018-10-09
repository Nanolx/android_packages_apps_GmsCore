/*
 * Copyright (C) 2013-2017 microG Project Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.gms.common.security;

import android.content.Context;
import android.util.Log;
import org.conscrypt.OpenSSLProvider;
import java.lang.reflect.Field;
import java.security.Security;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;

public class ProviderInstallerImpl {
    public static void insertProvider(Context context) {
        int insertProviderAt = Security.insertProviderAt(new OpenSSLProvider("GmsCore_OpenSSL"), 1);
	try {
            if (insertProviderAt == 1) {
                SSLContext instance = SSLContext.getInstance("Default");
                Field declaredField = SSLSocketFactory.class.getDeclaredField("defaultSocketFactory");
                declaredField.setAccessible(true);
                declaredField.set(null, instance.getSocketFactory());
                declaredField = SSLServerSocketFactory.class.getDeclaredField("defaultServerSocketFactory");
                declaredField.setAccessible(true);
                declaredField.set(null, instance.getServerSocketFactory());
                Security.setProperty("ssl.SocketFactory.provider", "org.conscrypt.OpenSSLSocketFactoryImpl");
                Security.setProperty("ssl.ServerSocketFactory.provider", "org.conscrypt.OpenSSLServerSocketFactoryImpl");
                instance = SSLContext.getInstance("Default");
                SSLContext.setDefault(instance);
                HttpsURLConnection.setDefaultSSLSocketFactory(instance.getSocketFactory());
                //HttpsURLConnection.setDefaultHostnameVerifier(new oat());
                //TODO: Set the default HostnameVerifier
                Log.i("ProviderInstaller", "Installed default security provider GmsCore_OpenSSL");
            } else {
                Log.e("ProviderInstaller", "Failed to install security provider GmsCore_OpenSSL, result: " + insertProviderAt);
                throw new SecurityException();
            }
        } catch (Exception e) {
            Log.d("ProviderInstaller", "Failed to insert provider");
        }
    }
}
