/*
 * Copyright (C) 2016 Jacksgong(blog.dreamtobe.cn)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.dreamtobe.filedownloader;

import com.liulishuo.filedownloader.connection.FileDownloadConnection;
import com.liulishuo.filedownloader.util.FileDownloadHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * The FileDownloadConnection implemented with the okhttp3.
 */
public class OkHttp3Connection implements FileDownloadConnection {

    private final OkHttpClient mClient;
    private final Request.Builder mRequestBuilder;

    private Request mRequest;
    private Response mResponse;

    public OkHttp3Connection(String url, OkHttpClient client) {
        mRequestBuilder = new Request.Builder().url(url);

        mClient = client;
    }

    @Override
    public void addHeader(String name, String value) {
        mRequestBuilder.addHeader(name, value);
    }

    @Override
    public boolean dispatchAddResumeOffset(String etag, long offset) {
        return false;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (mResponse == null) throw new IllegalStateException("Please invoke #execute first!");

        return mResponse.body().byteStream();
    }

    @Override
    public Map<String, List<String>> getRequestHeaderFields() {
        if (mRequest == null) {
            mRequest = mRequestBuilder.build();
        }

        return mRequest.headers().toMultimap();
    }

    @Override
    public Map<String, List<String>> getResponseHeaderFields() {
        return mResponse == null ? null : mResponse.headers().toMultimap();
    }

    @Override
    public String getResponseHeaderField(String name) {
        return mResponse == null ? null : mRequest.header(name);
    }

    @Override
    public void execute() throws IOException {
        if (mRequest == null) {
            mRequest = mRequestBuilder.build();
        }

        mResponse = mClient.newCall(mRequest).execute();
    }

    @Override
    public int getResponseCode() throws IOException {
        if (mResponse == null) throw new IllegalStateException("Please invoke #execute first!");

        return mResponse.code();
    }

    @Override
    public void ending() {
        mRequest = null;
        mResponse = null;
    }

    /**
     * The creator for the connection implemented with the okhttp3.
     */
    public static class Creator implements FileDownloadHelper.ConnectionCreator {

        private OkHttpClient mClient;

        public Creator() {
        }

        /**
         * Create the Creator with the customized {@code client}.
         *
         * @param client the customized client.
         */
        public Creator(OkHttpClient client) {
            mClient = client;
        }

        @Override
        public FileDownloadConnection create(String url) throws IOException {
            if (mClient == null) {
                synchronized (Creator.class) {
                    if (mClient == null) {
                        mClient = new OkHttpClient();
                    }
                }
            }

            return new OkHttp3Connection(url, mClient);
        }
    }
}