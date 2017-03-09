package controller;

import com.google.gson.Gson;
import db.DBController;
import db.User;

import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Servlet")
public class Servlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "createUser":
                createUser(request, response);
                break;
            case "validateLogin":
                validateLogin(request, response);
                break;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("This is the Ubob Servlet.");
    }

    private void validateLogin(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        Boolean success = AccountController.getInstance().validateLogin(name, password);
        User user = DBController.getInstance().getUserByName(name);

        this.setResponse(success, user, response);
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        Boolean success = AccountController.getInstance().createUser(name, password);
        User user = DBController.getInstance().getUserByName(name);

        this.setResponse(success, user, response);
    }

    private void setResponse(Boolean success, User user, HttpServletResponse response) {
        try {
            PrintWriter out = response.getWriter();

            if (success) {
                Gson gson = new Gson();

                JsonObject addMessage = JsonProvider.provider().createObjectBuilder()
                        .add("success", "true")
                        .add("id", user.getId().toString())
                        .add("username", user.getName())
                        .add("balance", user.getBalance())
                        .add("rented", gson.toJson(user.getBikesRented()))
                        .build();

                out.append(addMessage.toString());
            } else {
                JsonObject addMessage = JsonProvider.provider().createObjectBuilder()
                        .add("success", "fail")
                        .build();

                out.append(addMessage.toString());
            }

        } catch (IOException e) {
            System.err.println("Couldn't build the JSON object from input data.");
            e.printStackTrace();
        }
    }
}
