package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import entityDTO.AuditDTO;
import mappers.AuditMapper;
import models.AuditModel;
import org.mapstruct.factory.Mappers;
import service.AuditService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/audit/*")
public class AuditController extends HttpServlet {

    private AuditService auditService;
    private AuditMapper auditMapper;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        this.auditService = new AuditService(new repository.AuditRepository());
        this.auditMapper = Mappers.getMapper(AuditMapper.class);
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<AuditDTO> auditLogs = auditService.getAllLogs()
                    .stream()
                    .map(auditMapper::toDTO)
                    .collect(Collectors.toList());
            response.setContentType("application/json");
            response.getWriter().write(convertToJson(auditLogs));
        } else {
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length == 2) {
                Integer id = Integer.parseInt(pathParts[1]);
                AuditModel auditModel = auditService.getAllLogs()
                        .stream()
                        .filter(audit -> audit.getId().equals(id))
                        .findFirst()
                        .orElse(null);
                if (auditModel != null) {
                    AuditDTO auditDTO = auditMapper.toDTO(auditModel);
                    response.setContentType("application/json");
                    response.getWriter().write(convertToJson(auditDTO));
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Audit log not found");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request path");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AuditDTO auditDTO = parseRequestBody(request, AuditDTO.class);
        AuditModel auditModel = auditMapper.toModel(auditDTO);
        auditService.addAction(auditModel.getAction(), auditModel.getUsername(), auditModel.getUserId());
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setContentType("application/json");
        response.getWriter().write(convertToJson(auditMapper.toDTO(auditModel)));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "PUT method is not supported for audit logs");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "DELETE method is not supported for audit logs");
    }

    private <T> T parseRequestBody(HttpServletRequest request, Class<T> clazz) throws IOException {
        return objectMapper.readValue(request.getInputStream(), clazz);
    }

    private String convertToJson(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }
}
