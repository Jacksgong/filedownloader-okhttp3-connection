package cn.dreamtobe.filedownloader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({OkHttp3Connection.class, OkHttp3Connection.Creator.class,
        OkHttpClient.Builder.class, Request.class, Response.class, ResponseBody.class, Headers.class})
public class OkHttp3ConnectionTest {

    private OkHttp3Connection mConnection;

    private final static String URL = "http://blog.dreamtobe.cn";

    @Mock
    private OkHttp3Connection.Creator mCreator;

    @Mock
    private OkHttpClient mClient;

    @Before
    public void setUp() throws Exception {
        mConnection = new OkHttp3Connection(URL, mClient);

        whenNew(OkHttpClient.class).withNoArguments().thenReturn(mClient);
    }

    @Test
    public void createCreator_withBuilder_Assigned() throws IOException {
        OkHttpClient.Builder builder = PowerMockito.mock(OkHttpClient.Builder.class);
        OkHttpClient customizedClient = mock(OkHttpClient.class);

        when(builder.build()).thenReturn(customizedClient);

        OkHttp3Connection.Creator creator = new OkHttp3Connection.Creator(builder);

        assertThat(creator.customize()).isEqualTo(builder);

        OkHttp3Connection connection = (OkHttp3Connection) creator.create(URL);

        assertThat(connection.mClient).isEqualTo(customizedClient);
    }

    @Test
    public void createCreator_customizeWithoutBuilder_newOne() throws IOException {
        OkHttp3Connection.Creator creator = new OkHttp3Connection.Creator();

        assertThat(creator.customize()).isNotNull();
    }

    @Test
    public void createCreator_withoutBuilder_newOne() throws IOException {
        OkHttp3Connection.Creator creator = new OkHttp3Connection.Creator();
        OkHttp3Connection connection = (OkHttp3Connection) creator.create(URL);

        assertThat(connection.mClient).isNotNull();

    }

    @Test(expected = IllegalStateException.class)
    public void getInputStream_responseIsNull_throwException() throws IOException {
        mConnection.getInputStream();
    }

    @Test
    public void getInputStream_executed_getRightInputStream() throws IOException {
        final Call call = mock(Call.class);
        when(mClient.newCall(any(Request.class))).thenReturn(call);

        final Response response = PowerMockito.mock(Response.class);
        when(call.execute()).thenReturn(response);

        final ResponseBody body = PowerMockito.mock(ResponseBody.class);
        when(response.body()).thenReturn(body);

        final InputStream expectedInputStream = mock(InputStream.class);
        when(body.byteStream()).thenReturn(expectedInputStream);

        mConnection.execute();

        final InputStream resultInputStream = mConnection.getInputStream();

        assertThat(resultInputStream).isEqualTo(expectedInputStream);
    }

    @Test
    public void addHeader_getRequestHeaderFiles_meet() throws IOException {
        OkHttp3Connection.Creator creator = new OkHttp3Connection.Creator();
        OkHttp3Connection connection = (OkHttp3Connection) creator.create(URL);

        connection.addHeader("mock", "mock");
        connection.addHeader("mock1", "mock2");
        connection.addHeader("mock1", "mock3");

        Map<String, List<String>> headers = connection.getRequestHeaderFields();

        assertThat(headers.keySet()).hasSize(2)
                .contains("mock", "mock1");

        List<String> allValues = new ArrayList<>();
        Collection<List<String>> valueList = headers.values();
        for (List<String> values : valueList) {
            allValues.addAll(values);
        }
        assertThat(allValues).hasSize(3)
                .contains("mock", "mock2", "mock3");
    }

    @Test
    public void getResponseHeaderFields_noResponse_null() {
        final Map<String, List<String>> nullFields = mConnection.getResponseHeaderFields();
        assertThat(nullFields).isNull();

        final String nullField = mConnection.getResponseHeaderField(anyString());
        assertThat(nullField).isNull();
    }

    @Test
    public void getResponseHeaderFields_responseNotNull_getHeaders() throws IOException {
        final Call call = mock(Call.class);
        when(mClient.newCall(any(Request.class))).thenReturn(call);

        final Response response = PowerMockito.mock(Response.class);
        when(call.execute()).thenReturn(response);

        mConnection.execute();

        final Headers headers = PowerMockito.mock(Headers.class);
        when(response.headers()).thenReturn(headers);

        final Map headerValues = mock(Map.class);
        when(headers.toMultimap()).thenReturn(headerValues);

        final Map fieldsResult = mConnection.getResponseHeaderFields();
        assertThat(fieldsResult).isEqualTo(headerValues);

        final String expected = "mock";
        when(response.header(anyString())).thenReturn(expected);

        final String filedResult = mConnection.getResponseHeaderField(anyString());
        assertThat(filedResult).isEqualTo(expected);
    }

    @Test
    public void execute() throws IOException {
        final Call call = mock(Call.class);
        when(mClient.newCall(any(Request.class))).thenReturn(call);

        final Response response = PowerMockito.mock(Response.class);
        when(call.execute()).thenReturn(response);

        final Request.Builder builder = mock(Request.Builder.class);
        OkHttp3Connection connection = new OkHttp3Connection(builder, mClient);

        final Request request = PowerMockito.mock(Request.class);
        when(builder.build()).thenReturn(request);

        connection.execute();

        verify(mClient.newCall(request).execute());

    }

    @Test(expected = IllegalStateException.class)
    public void getResponseCode_responseIsNull_throwException() throws IOException {
        mConnection.getResponseCode();
    }

    @Test
    public void getResponseCode_responseNotNull_getCode() throws IOException {
        final Call call = mock(Call.class);
        when(mClient.newCall(any(Request.class))).thenReturn(call);

        final Response response = PowerMockito.mock(Response.class);
        when(call.execute()).thenReturn(response);

        final Integer expected = PowerMockito.mock(Integer.class);
        when(response.code()).thenReturn(expected);

        mConnection.execute();

        final Integer result = mConnection.getResponseCode();
        assertThat(result).isEqualTo(expected);

    }
}