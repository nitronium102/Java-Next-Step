package http.controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import model.User;

import java.util.Map;

@Slf4j
public class CreateUserController extends AbstractController {
    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {
        Map<String, String> params = request.getParameterMap();
        User user = new User(
                params.get("userId"),
                params.get("password"),
                params.get("name"),
                params.get("email")
        );
        log.debug("user : {}", user);
        DataBase.addUser(user);
        response.sendRedirect("/index.html");
    }
}
