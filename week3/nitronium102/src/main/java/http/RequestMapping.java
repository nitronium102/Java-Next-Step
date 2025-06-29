package http;

import http.controller.CreateUserController;
import http.controller.HttpController;
import http.controller.ListUserController;
import http.controller.LoginController;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {
    private static Map<String, HttpController> controllerMap = new HashMap<>();

    static {
        controllerMap.put("/user/create", new CreateUserController());
        controllerMap.put("/user/login", new LoginController());
        controllerMap.put("/user/list", new ListUserController());
    }

    public static HttpController getController(String requestUrl) {
        return controllerMap.get(requestUrl);
    }
}
