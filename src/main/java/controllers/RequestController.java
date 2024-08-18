package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import entityDTO.RequestDTO;
import mappers.RequestMapper;
import models.RequestModel;
import org.mapstruct.factory.Mappers;
import service.RequestService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/requests/*")
public class RequestController extends HttpServlet {

    private RequestService requestService;
    private RequestMapper requestMapper;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        this.requestService = new RequestService(new repository.RequestRepository());
        this.requestMapper = Mappers.getMapper(RequestMapper.class);
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<RequestDTO> requests = requestService.getAllRequest()
                    .stream()
                    .map(requestMapper::toDTO)
                    .collect(Collectors.toList());
            response.getWriter().write(convertToJson(requests));
        } else {
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length == 2) {
                Integer id = Integer.parseInt(pathParts[1]);
                RequestModel requestModel = requestService.getOrderById(id);
                if (requestModel != null) {
                    RequestDTO requestDTO = requestMapper.toDTO(requestModel);
                    response.getWriter().write(convertToJson(requestDTO));
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Request not found");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request path");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDTO requestDTO = parseRequestBody(request, RequestDTO.class);
        RequestModel requestModel = requestMapper.toModel(requestDTO);
        requestService.addRequest(requestModel);
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.getWriter().write(convertToJson(requestMapper.toDTO(requestModel)));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request path");
            return;
        }

        String[] pathParts = pathInfo.split("/");
        if (pathParts.length == 2) {
            Integer id = Integer.parseInt(pathParts[1]);
            RequestDTO requestDTO = parseRequestBody(request, RequestDTO.class);
            RequestModel requestModel = requestMapper.toModel(requestDTO).toBuilder().id(id).build();

            if (requestService.updateRequest(requestModel)) {
                response.getWriter().write(convertToJson(requestMapper.toDTO(requestModel)));
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Request not found");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request path");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request path");
            return;
        }

        String[] pathParts = pathInfo.split("/");
        if (pathParts.length == 2) {
            Integer id = Integer.parseInt(pathParts[1]);
            if (requestService.deleteRequestById(id)) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Request not found");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request path");
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
