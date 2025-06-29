package http.controller;

import http.HttpRequest;
import http.HttpResponse;

public interface HttpController {
    void service(HttpRequest request, HttpResponse response);
}
