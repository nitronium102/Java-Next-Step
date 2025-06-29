package http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestLineTest {

    private static final String INDEX_PATH = "/index.html";
    private static final String USER_CREATE_PATH = "/user/create";
    private static final String QUERY = "?userId=nitronium&password=password&name=minji";
    private static final String HTTP_VERSION = "HTTP/1.1";

    private String buildRequestLine(HttpMethod method, String path, String query, String version) {
        return method.getValue() + " " + path + (query != null ? query : "") + " " + version;
    }

    @Test
    @DisplayName("GET 요청: 메서드와 경로 파싱")
    void parseGetRequestLine() {
        RequestLine line = new RequestLine(buildRequestLine(HttpMethod.GET, INDEX_PATH, null, HTTP_VERSION));
        assertEquals(HttpMethod.GET.getValue(), line.getMethod());
        assertEquals(INDEX_PATH, line.getPath());
        assertTrue(line.getParams().isEmpty());
    }

    @Test
    @DisplayName("POST 요청: 메서드와 경로 파싱")
    void parsePostRequestLine() {
        RequestLine line = new RequestLine(buildRequestLine(HttpMethod.POST, INDEX_PATH, null, HTTP_VERSION));
        assertEquals(HttpMethod.POST.getValue(), line.getMethod());
        assertEquals(INDEX_PATH, line.getPath());
        assertTrue(line.getParams().isEmpty());
    }

    @Test
    @DisplayName("쿼리 파라미터 파싱")
    void parseQueryParams() {
        RequestLine line = new RequestLine(buildRequestLine(HttpMethod.GET, USER_CREATE_PATH, QUERY, HTTP_VERSION));
        assertEquals(HttpMethod.GET.getValue(), line.getMethod());
        assertEquals(USER_CREATE_PATH, line.getPath());

        Map<String, String> params = line.getParams();
        assertEquals(3, params.size());
        assertEquals("nitronium", params.get("userId"));
        assertEquals("password", params.get("password"));
        assertEquals("minji", params.get("name"));
    }
}
