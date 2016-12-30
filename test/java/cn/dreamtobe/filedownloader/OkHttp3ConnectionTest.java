package cn.dreamtobe.filedownloader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import okhttp3.OkHttpClient;

@RunWith(PowerMockRunner.class)
@PrepareForTest(OkHttp3Connection.class)
public class OkHttp3ConnectionTest {

    private OkHttp3Connection mConnection;

    @Mock
    private OkHttp3Connection.Creator mCreator;

    @Mock
    private OkHttpClient mClient;

    @Before
    public void setUp() {
        mConnection = new OkHttp3Connection("", mClient);
        // TODO: 31/12/2016 expectNew(OkHttpClient.class).
    }

    @Test
    public void createCreator_withBuilder_Assigned() {
    }

    @Test
    public void createCreator_withoutBuilder_newOne() throws IOException {
    }

    @Test(expected = IllegalStateException.class)
    public void getInputStream_responseIsNull_throwException() {
    }

    @Test
    public void getInputStream_responseNotNull_getInputStream() {
    }

    @Test
    public void addHeader_getRequestHeaderFiles_meet() {
    }

    @Test
    public void getResponseHeaderFields_noResponse_null() {
    }

    @Test
    public void getResponseHeaderFields_responseNotNull_getHeaders() {
    }

    @Test
    public void execute() {
    }

    @Test(expected = IllegalStateException.class)
    public void getResponseCode_responseIsNull_throwException() {
    }

    @Test
    public void getResponseCode_responseNotNull_getCode() {
    }

    @Test
    public void ending() {
    }
}