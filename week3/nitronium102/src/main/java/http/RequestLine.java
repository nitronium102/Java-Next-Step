package http;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Slf4j
public class RequestLine {

    private final HttpMethod method;
    private final String path;
    private  Map<String, String> params = new HashMap<>();

    public RequestLine(String requestLine) {
        Objects.requireNonNull(requestLine, "requestLine must not be null");
        log.info("request line : {}", requestLine);

        String[] tokens = requestLine.split(" ");
        if (tokens.length < 2) {
            throw new IllegalArgumentException("Invalid request line: Not enough tokens");
        }
        method = HttpMethod.valueOf(tokens[0]);

        int index = tokens[1].indexOf("?");
        if (index == -1) {
            path = tokens[1];
        } else {
            path = tokens[1].substring(0, index);
            params = HttpRequestUtils.parseQueryString(tokens[1].substring(index + 1));
        }
    }
}
