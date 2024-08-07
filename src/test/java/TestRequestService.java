import Models.RequestModel;
import Service.RequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestRequestService {
    private RequestService requestService;

    @BeforeEach
    void setUp() {
        requestService = new RequestService();
    }

    @Test
    void addRequest_ShouldAddRequestToMap() {
        RequestModel request = new RequestModel("car1", "user1", "description1", "pending");
        requestService.addRequest(request);
        assertFalse(requestService.getAllRequests().isEmpty());
    }

    @Test
    void getRequest_ShouldReturnCorrectRequest() {
        RequestModel request = new RequestModel("car1", "user1", "description1", "pending");
        requestService.addRequest(request);
        RequestModel result = requestService.getRequest(request.getId());
        assertEquals(request.getId(), result.getId());
    }

    @Test
    void updateRequest_ShouldUpdateRequestInMap() {
        RequestModel request = new RequestModel("car1", "user1", "description1", "pending");
        requestService.addRequest(request);
        request.setStatus("completed");
        requestService.updateRequest(request);
        RequestModel result = requestService.getRequest(request.getId());
        assertEquals("completed", result.getStatus());
    }

    @Test
    void deleteRequest_ShouldRemoveRequestFromMap() {
        RequestModel request = new RequestModel("car1", "user1", "description1", "pending");
        requestService.addRequest(request);
        requestService.deleteRequest(request.getId());
        assertNull(requestService.getRequest(request.getId()));
    }

    @Test
    void searchRequests_ShouldReturnCorrectRequests() {
        RequestModel request1 = new RequestModel("car1", "user1", "description1", "pending");
        RequestModel request2 = new RequestModel("car2", "user2", "description2", "completed");
        requestService.addRequest(request1);
        requestService.addRequest(request2);

        List<RequestModel> result = requestService.searchRequests("user1", "pending");
        assertEquals(1, result.size());
        assertEquals("user1", result.get(0).getUserId());
        assertEquals("pending", result.get(0).getStatus());
    }
}
