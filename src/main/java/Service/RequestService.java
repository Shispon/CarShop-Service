package Service;

import Models.RequestModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestService {
    private Map<String, RequestModel> requests = new HashMap<>();

    public void addRequest(RequestModel request) {
        requests.put(request.getId(), request);
    }

    public RequestModel getRequest(String id) {
        return requests.get(id);
    }

    public Collection<RequestModel> getAllRequests() {
        return requests.values();
    }

    public void updateRequest(RequestModel request) {
        requests.put(request.getId(), request);
    }

    public void deleteRequest(String id) {
        requests.remove(id);
    }

    public List<RequestModel> searchRequests(String userId, String status) {
        return requests.values().stream()
                .filter(request -> (userId == null || request.getUserId().equalsIgnoreCase(userId)) &&
                        (status == null || request.getStatus().equalsIgnoreCase(status)))
                .collect(Collectors.toList());
    }
}
