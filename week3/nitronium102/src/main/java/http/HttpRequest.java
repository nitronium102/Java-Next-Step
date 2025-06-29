package http;

import lombok.extern.slf4j.Slf4j;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpRequest {

    private RequestLine requestLine;
    private Map<String, String> headers = new HashMap<>();

    private Map<String, String> parameter = new HashMap<>();

    public HttpRequest(InputStream in) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            parseRequestLine(br);
            parseHeaders(br);
            parseBodyIfNecessary(br);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void parseRequestLine(BufferedReader br) throws Exception {
        String line = br.readLine();
        if (line == null || line.isEmpty()) {
            throw new IllegalArgumentException("Invalid request line: Request line is null or empty");
        }
        requestLine = new RequestLine(line);
        parameter = requestLine.getParams();
    }

    private void parseHeaders(BufferedReader br) throws Exception {
        String line;
        while (!(line = br.readLine()).isEmpty()) {
            log.debug("header : {}", line);
            String[] headerTokens = line.split(": ");
            if (headerTokens.length < 2) {
                throw new IllegalArgumentException("Invalid header: Not enough tokens");
            }
            headers.put(headerTokens[0].trim(), headerTokens[1].trim());
        }
    }

    private void parseBodyIfNecessary(BufferedReader br) throws Exception {
        if (requestLine.getMethod().equals("POST")) {
            String contentLength = headers.get("Content-Length");
            if (contentLength != null) {
                int length = Integer.parseInt(contentLength);
                String body = IOUtils.readData(br, length);
                parameter.putAll(HttpRequestUtils.parseQueryString(body));
            }
        }
    }

    public boolean isLoggedIn() {
        String cookieValue = headers.get("Cookie");
        if (cookieValue == null) {
            return false;
        }
        Map<String, String> cookies = HttpRequestUtils.parseCookies(cookieValue);
        return cookies.get("logined") != null && Boolean.parseBoolean(cookies.get("logined"));
    }

    public HttpMethod getMethod() {
        return requestLine.getMethod();
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public String getParameter(String name) {
        return parameter.get(name);
    }

    public Map<String, String> getParameterMap() {
        return parameter;
    }
}
