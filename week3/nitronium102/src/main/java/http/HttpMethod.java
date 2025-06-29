package http;

public enum HttpMethod {
    GET("GET"),
    POST("POST");

    private final String value;

    HttpMethod(String value) {
        this.value = value;
    }

    public boolean isPost() {
        return this == POST;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
