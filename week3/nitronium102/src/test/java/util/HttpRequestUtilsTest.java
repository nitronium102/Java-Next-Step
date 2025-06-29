package util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import util.HttpRequestUtils.Pair;

class HttpRequestUtilsTest {

    private final String USER_NAME = "minji";
    private final String USER_PASSWORD = "password";
    private final String USER_ID = "nitronium";

    @Test
    @DisplayName("[4장] 성공_요청 URL 추출")
    void testGetRequestUrl() {
        String requestLine = "GET /index.html HTTP/1.1";
        String url = HttpRequestUtils.getRequestUrl(requestLine);
        assertEquals("/index.html", url);

        assertNull(HttpRequestUtils.getRequestUrl(null));
        assertNull(HttpRequestUtils.getRequestUrl(""));
        assertNull(HttpRequestUtils.getRequestUrl("GET"));
    }

    @Test
    @DisplayName("[4장] 성공_요청 URL 추출_쿼리스트링 포함")
    void parseQueryString() {
        String queryString = "userId=" + USER_ID + "&password=" + USER_PASSWORD + "&name=" + USER_NAME;
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertEquals(USER_ID, parameters.get("userId"));
        assertEquals(USER_PASSWORD, parameters.get("password"));
        assertEquals(USER_NAME, parameters.get("name"));
    }

    @Test
    @DisplayName("[4장] 성공_요청 URL 추출_쿼리스트링 포함_인코딩")
    void parseQueryString_null() {
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(null);
        assertTrue(parameters.isEmpty());

        parameters = HttpRequestUtils.parseQueryString("");
        assertTrue(parameters.isEmpty());

        parameters = HttpRequestUtils.parseQueryString(" ");
        assertTrue(parameters.isEmpty());
    }

    @Test
    void parseQueryString_invalid() {
        String queryString = "userId=" + USER_ID + "&password";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertEquals(USER_ID, parameters.get("userId"));
        assertNull(parameters.get("password"));
    }

    @Test
    void parseCookies() {
        String cookies = "logined=true; JSessionId=1234";
        Map<String, String> parameters = HttpRequestUtils.parseCookies(cookies);
        assertEquals("true", parameters.get("logined"));
        assertEquals("1234", parameters.get("JSessionId"));
        assertNull(parameters.get("session"));
    }

    @Test
    void getKeyValue() {
        Pair pair = HttpRequestUtils.getKeyValue("userId=javajigi", "=");
        assertEquals(new Pair("userId", "javajigi"), pair);
    }

    @Test
    void getKeyValue_invalid() {
        Pair pair = HttpRequestUtils.getKeyValue("userId", "=");
        assertNull(pair);
    }

    @Test
    void parseHeader() {
        String header = "Content-Length: 59";
        Pair pair = HttpRequestUtils.parseHeader(header);
        assertEquals(new Pair("Content-Length", "59"), pair);
    }
}