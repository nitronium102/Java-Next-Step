package http.controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ListUserController extends AbstractController  {
    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
        if (request.isLoggedIn()) {
            response.sendRedirect("/user/login.html");
            return;
        }
        response.forwardBody(generateUserListHtml());

    }

    private String generateUserListHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='table'>");
        DataBase.findAll().forEach(user -> {
            sb.append("<tr>")
                    .append(String.format("<td>%s</td>", user.getUserId()))
                    .append(String.format("<td>%s</td>", user.getName()))
                    .append(String.format("<td>%s</td>", user.getEmail()))
                    .append("</tr>");
        });
        sb.append("</table>");
        return sb.toString();
    }

}
