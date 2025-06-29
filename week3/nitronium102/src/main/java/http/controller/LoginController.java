package http.controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import model.User;

import java.util.Map;

@Slf4j
public class LoginController extends AbstractController {
    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {
        Map<String, String> params = request.getParameterMap();
        User user = DataBase.findUserById(params.get("userId"));
        if (user != null && user.getPassword().equals(params.get("password"))) {
            response.addHeader("Set-Cookie", "logined=true");
            response.sendRedirect("/index.html");
        } else {
            response.sendRedirect("/user/login_failed.html");
        }
    }
}
