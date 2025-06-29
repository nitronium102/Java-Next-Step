package http;

import lombok.extern.slf4j.Slf4j;
import util.ResponseUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class HttpResponse {

    // HTTP 상태 코드 상수
    public static final int STATUS_OK = 200;
    public static final int STATUS_REDIRECT = 302;

    // MIME 타입 상수
    private static final String CONTENT_TYPE_HTML = "text/html;charset=utf-8";
    private static final String CONTENT_TYPE_CSS = "text/css";
    private static final String CONTENT_TYPE_JS = "application/javascript";

    private final DataOutputStream dos;
    private Map<String, String> headers = new HashMap<>();

    public HttpResponse(OutputStream outputStream) {
        dos = new DataOutputStream(outputStream);
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void forward(String url) {
        byte[] body = ResponseUtils.readWebappFile(url);
        setContentTypeByFileExtension(url);
        addHeader("Content-Length", String.valueOf(body.length));
        send200Response(body);
    }

    public void forwardBody(String body) {
        byte[] contents = body.getBytes(StandardCharsets.UTF_8);
        addHeader("Content-Type", CONTENT_TYPE_HTML);
        addHeader("Content-Length", String.valueOf(contents.length));
        send200Response(contents);
    }

    public void sendRedirect(String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 " + STATUS_REDIRECT + " Found\r\n");
            processHeaders();
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error("Error during redirect: {}", e.getMessage());
        }
    }

    private void response200Header(int lengthOfBodyContent) {
        try{
            dos.writeBytes("HTTP/1.1 " + STATUS_OK + " Found\r\n");
            processHeaders();
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error("Error during response 200: {}", e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error("responseBody error: ", e.getMessage());
        }
    }

    private void send200Response(byte[] body) {
        response200Header(body.length);
        responseBody(body);
    }

    private void setContentTypeByFileExtension(String url) {
        if (url.endsWith(".css")) {
            addHeader("Content-Type", CONTENT_TYPE_CSS);
        } else if (url.endsWith(".js")) {
            addHeader("Content-Type", CONTENT_TYPE_JS);
        } else {
            addHeader("Content-Type", CONTENT_TYPE_HTML);
        }
    }

    private void processHeaders() {
        try {
            Set<String> headerKeys = headers.keySet();
            for (String key : headerKeys) {
                String value = headers.get(key);
                dos.writeBytes(key + ": " + value + "\r\n");
            }
        }  catch (IOException e) {
            log.error("processHeaders error: ", e.getMessage());
        }
    }
}
