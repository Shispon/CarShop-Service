package service;

import models.OrderModel;
import repository.OrderRepository;

import java.util.*;

public class OrderService {

    private final OrderRepository orderRepository;
    private Integer id;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        id = 0;
    }

    public void addOrder(OrderModel order) {
        OrderModel newOrder = order.toBuilder()
                .id(id++)
                .build();
        orderRepository.create(newOrder);
    }

    public OrderModel getOrderById(Integer id) {
        return orderRepository.read(id);
    }

    public boolean updateOrder(OrderModel order) {
        return orderRepository.update(order) != null;
    }

    public boolean deleteOrder(Integer id) {
        return orderRepository.delete(id);
    }

    public List<OrderModel> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<OrderModel> searchOrdersByUserIdAndCarID(Integer userId, Integer carId) {
        return orderRepository.findOrderByUserIdAndCarId(userId, carId);
    }
}
