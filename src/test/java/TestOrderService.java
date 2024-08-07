import Models.OrderModel;
import Service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class TestOrderService {
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService();
    }

    @Test
    void addOrder_ShouldAddOrderToMap() {
        OrderModel order = new OrderModel("user1", "pending");
        orderService.addOrder(order);
        assertFalse(orderService.getAllOrders().isEmpty());
    }

    @Test
    void getOrderById_ShouldReturnCorrectOrder() {
        OrderModel order = new OrderModel("user1", "pending");
        orderService.addOrder(order);
        OrderModel result = orderService.getOrderById(order.getId());
        assertEquals(order.getId(), result.getId());
    }

    @Test
    void getOrderByStatus_ShouldReturnCorrectOrder() {
        OrderModel order = new OrderModel("user1", "pending");
        orderService.addOrder(order);
        OrderModel result = orderService.getOrderByStatus("pending");
        assertEquals(order.getStatus(), result.getStatus());
    }

    @Test
    void getOrderByClientId_ShouldReturnCorrectOrder() {
        OrderModel order = new OrderModel("user1", "pending");
        orderService.addOrder(order);
        OrderModel result = orderService.getOrderByClientId("user1");
        assertEquals(order.getUserId(), result.getUserId());
    }

    @Test
    void getOrderByDate_ShouldReturnCorrectOrder() {
        OrderModel order = new OrderModel("user1", "pending");
        Date date = order.getDate();
        orderService.addOrder(order);
        OrderModel result = orderService.getOrderByDate(date);
        assertEquals(order.getDate(), result.getDate());
    }

    @Test
    void getOrderByCarId_ShouldReturnCorrectOrder() {
        OrderModel order = new OrderModel("user1", "pending");
        order.setCarId("car1");
        orderService.addOrder(order);
        OrderModel result = orderService.getOrderByCarId("car1");
        assertEquals(order.getCarId(), result.getCarId());
    }

    @Test
    void getAllOrders_ShouldReturnAllOrders() {
        OrderModel order1 = new OrderModel("user1", "pending");
        OrderModel order2 = new OrderModel("user2", "completed");
        orderService.addOrder(order1);
        orderService.addOrder(order2);

        List<OrderModel> result = orderService.getAllOrders();
        assertEquals(2, result.size());
    }

    @Test
    void searchOrders_ShouldReturnCorrectOrders() {
        OrderModel order1 = new OrderModel("user1", "pending");
        OrderModel order2 = new OrderModel("user2", "completed");
        order1.setCarId("car1");
        order2.setCarId("car2");
        orderService.addOrder(order1);
        orderService.addOrder(order2);

        List<OrderModel> result = orderService.searchOrders("user1", "car1");
        assertEquals(1, result.size());
        assertEquals("user1", result.get(0).getUserId());
        assertEquals("car1", result.get(0).getCarId());
    }

    @Test
    void isCarOrdered_ShouldReturnTrueIfCarIsOrdered() {
        OrderModel order = new OrderModel("user1", "pending");
        order.setCarId("car1");
        orderService.addOrder(order);
        assertTrue(orderService.isCarOrdered("car1"));
    }

    @Test
    void isCarOrdered_ShouldReturnFalseIfCarIsNotOrdered() {
        OrderModel order = new OrderModel("user1", "pending");
        order.setCarId("car1");
        orderService.addOrder(order);
        assertFalse(orderService.isCarOrdered("car2"));
    }

    @Test
    void updateOrder_ShouldUpdateOrderInMap() {
        OrderModel order = new OrderModel("user1", "pending");
        orderService.addOrder(order);
        order.setStatus("completed");
        orderService.updateOrder(order);
        OrderModel result = orderService.getOrderById(order.getId());
        assertEquals("completed", result.getStatus());
    }

    @Test
    void deleteOrder_ShouldRemoveOrderFromMap() {
        OrderModel order = new OrderModel("user1", "pending");
        orderService.addOrder(order);
        orderService.deleteOrder(order.getId());
        assertNull(orderService.getOrderById(order.getId()));
    }

}
