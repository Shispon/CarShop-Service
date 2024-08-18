package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import entityDTO.OrderDTO;
import mappers.OrderMapper;
import models.OrderModel;
import org.mapstruct.factory.Mappers;
import service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/orders/*")
public class OrderController extends HttpServlet {

    private OrderService orderService;
    private OrderMapper orderMapper;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        this.orderService = new OrderService(new repository.OrderRepository());
        this.orderMapper = Mappers.getMapper(OrderMapper.class);
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<OrderDTO> orders = orderService.getAllOrders()
                    .stream()
                    .map(orderMapper::toDTO)
                    .collect(Collectors.toList());
            response.getWriter().write(convertToJson(orders));
        } else {
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length == 2) {
                Integer id = Integer.parseInt(pathParts[1]);
                OrderModel order = orderService.getOrderById(id);
                if (order != null) {
                    OrderDTO orderDTO = orderMapper.toDTO(order);
                    response.getWriter().write(convertToJson(orderDTO));
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request path");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrderDTO orderDTO = parseRequestBody(request, OrderDTO.class);
        OrderModel orderModel = orderMapper.toModel(orderDTO);
        orderService.addOrder(orderModel);
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.getWriter().write(convertToJson(orderMapper.toDTO(orderModel)));
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
            OrderDTO orderDTO = parseRequestBody(request, OrderDTO.class);
            OrderModel orderModel = orderMapper.toModel(orderDTO).toBuilder().id(id).build();

            if (orderService.updateOrder(orderModel)) {
                response.getWriter().write(convertToJson(orderMapper.toDTO(orderModel)));
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
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
            if (orderService.deleteOrder(id)) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
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
