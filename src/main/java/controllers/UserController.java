package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import entityDTO.UserDTO;
import mappers.UserMapper;
import models.UserModel;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@WebServlet("/api/users/*")
public class UserController extends HttpServlet {
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();

        if (path.equals("/register")) {
            registerUser(request, response);
        } else if (path.equals("/login")) {
            authenticateUser(request, response);
        } else if (path.equals("/logout")) {
            logoutUser(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();

        if (path.equals("/current")) {
            getCurrentUser(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Парсинг JSON из тела запроса и валидация
        UserDTO userDTO = parseRequestBody(request, UserDTO.class);
        UserModel userModel = userMapper.toModel(userDTO);
        userService.registerUser(userModel);

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setContentType("application/json");
        response.getWriter().write(convertToJson(userMapper.toDTO(userModel)));
    }

    private void authenticateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserDTO userDTO = parseRequestBody(request, UserDTO.class);
        boolean isAuthenticated = userService.authenticateUser(userDTO.getUsername(), userDTO.getPassword());

        if (isAuthenticated) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Authenticated");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authentication failed");
        }
    }

    private void logoutUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userService.logout();
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Logged out");
    }

    private void getCurrentUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserModel currentUser = userService.getCurrentUser();

        if (currentUser != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.getWriter().write(convertToJson(userMapper.toDTO(currentUser)));
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // Метод для парсинга JSON из тела запроса
    private <T> T parseRequestBody(HttpServletRequest request, Class<T> clazz) throws IOException {
        return objectMapper.readValue(request.getInputStream(), clazz);
    }

    // Метод для преобразования объекта в JSON
    private String convertToJson(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }
}
