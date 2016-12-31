# FileDownloader OkHttp3 Connection

A connection implemented with the OkHttp3 for FileDownloader.

[![Download][bintray_svg]][bintray_url]
![][file_downloader_svg]

## Usage

The simplest way to enable the connection with the okHttp3 for FileDownloader:

```java
// Init the FileDownloader with the OkHttp3Connection.Creator.
FileDownloader.init(context, new DownloadMgrInitialParams.InitCustomMaker()
                .connectionCreator(new OkHttp3Connection.Creator()));
```

Alternatively, If you want to customize the `OkHttp3Client`, you can provide the `OkHttpClient.Builder` when instance the `OkHttp3Connection.Creator`:

```java
// Enable the okHttp3 connection with the customized okHttp client builder.
final OkHttpClient.Builder builder = new OkHttpClient.Builder();
builder.connectTimeout(20_000, TimeUnit.SECONDS); // customize the value of the connect timeout.

// Init the FileDownloader with the OkHttp3Connection.Creator.
FileDownloader.init(context, new DownloadMgrInitialParams.InitCustomMaker()
        .connectionCreator(new OkHttp3Connection.Creator(builder)));
```

## Installation

Adding the following dependency to your `build.gradle` file:

```groovy
dependencies {
    compile 'cn.dreamtobe.filedownloader:filedownloader-okhttp3-connection:1.0.0'
}
```

## License

```
Copyright (C) 2016 Jacksgong(blog.dreamtobe.cn)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[file_downloader_svg]: https://img.shields.io/badge/Android-FileDownloader-orange.svg
[bintray_svg]: https://api.bintray.com/packages/jacksgong/maven/filedownloader-okhttp3-connection/images/download.svg
[bintray_url]: https://bintray.com/jacksgong/maven/filedownloader-okhttp3-connection/_latestVersion
