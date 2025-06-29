package http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpRequestTest {

    private final String USER_NAME = "minji";
    private final String USER_PASSWORD = "password";
    private final String USER_ID = "nitronium";

    @Test
    @DisplayName("GET 요청 전체 파싱")
    void parseGetRequestInputStream() {
        String request =
                "GET /user/create?userId=" + USER_ID +
                        "&passsword=" + USER_PASSWORD +
                        "&name=" + USER_NAME +
                        " HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Accept: */*\r\n\r\n";

        InputStream in = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));
        HttpRequest httpRequest = new HttpRequest(in);

        assertEquals("GET", httpRequest.getMethod());
        assertEquals("/user/create", httpRequest.getPath());
        assertEquals("localhost:8080", httpRequest.getHeader("Host"));
        assertEquals("keep-alive", httpRequest.getHeader("Connection"));

        assertEquals(USER_ID, httpRequest.getParameter("userId"));
        assertEquals(USER_PASSWORD, httpRequest.getParameter("passsword"));
        assertEquals(USER_NAME, httpRequest.getParameter("name"));
    }

    @Test
    @DisplayName("POST 요청 전체 파싱")
    void parsePostRequestInputStream() {
        String body = "userId=" + USER_ID +
                "&passsword=" + USER_PASSWORD +
                "&name=" + USER_NAME;
        String request =
                "POST /user/create HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Content-Type: application/x-www-form-urlencoded\r\n" +
                        "Content-Length: " + body.length() + "\r\n\r\n" +
                        body;

        InputStream in = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));
        HttpRequest httpRequest = new HttpRequest(in);

        assertEquals("POST", httpRequest.getMethod());
        assertEquals("/user/create", httpRequest.getPath());
        assertEquals("localhost:8080", httpRequest.getHeader("Host"));
        assertEquals("keep-alive", httpRequest.getHeader("Connection"));

        assertEquals(USER_ID, httpRequest.getParameter("userId"));
        assertEquals(USER_PASSWORD, httpRequest.getParameter("passsword"));
        assertEquals(USER_NAME, httpRequest.getParameter("name"));
    }
}
