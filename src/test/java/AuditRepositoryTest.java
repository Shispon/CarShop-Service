
import models.AuditModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import repository.AuditRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AuditRepositoryTest {

    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15.2")
            .withDatabaseName("test_db")
            .withUsername("admin")
            .withPassword("12345");

    private Connection connection;
    private AuditRepository auditRepository;

    @BeforeEach
    public void setUp() throws SQLException {
        postgresContainer.start();

        connection = DriverManager.getConnection(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword());

        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS car_shop.audit (" +
                    "id SERIAL PRIMARY KEY, " +
                    "action VARCHAR(255), " +
                    "timestamp TIMESTAMP, " +
                    "user_id INT)");
        }

        auditRepository = new AuditRepository();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Очистка таблицы после тестов
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS car_shop.audit");
        }
        connection.close();

        postgresContainer.stop();
    }

    @Test
    public void testCreate() {
        AuditModel audit = AuditModel.builder()
                .action("Test Action")
                .timestamp(LocalDateTime.now())
                .userId(1)
                .build();

        AuditModel createdAudit = auditRepository.create(audit);
        assertNotNull(createdAudit);
        assertNotNull(createdAudit.getId());
        assertEquals(audit.getAction(), createdAudit.getAction());
        assertEquals(audit.getUserId(), createdAudit.getUserId());
    }

    @Test
    public void testRead() {
        AuditModel audit = AuditModel.builder()
                .action("Test Action")
                .timestamp(LocalDateTime.now())
                .userId(1)
                .build();
        AuditModel createdAudit = auditRepository.create(audit);

        AuditModel readAudit = auditRepository.read(createdAudit.getId());
        assertNotNull(readAudit);
        assertEquals(createdAudit.getId(), readAudit.getId());
    }

    @Test
    public void testUpdate() {
        AuditModel audit = AuditModel.builder()
                .action("Test Action")
                .timestamp(LocalDateTime.now())
                .userId(1)
                .build();
        AuditModel createdAudit = auditRepository.create(audit);

        AuditModel updatedAudit = createdAudit.toBuilder()
                .action("Updated Action")
                .build();
        AuditModel result = auditRepository.update(updatedAudit);
        assertNotNull(result);
        assertEquals("Updated Action", result.getAction());
    }

    @Test
    public void testDelete() {
        AuditModel audit = AuditModel.builder()
                .action("Test Action")
                .timestamp(LocalDateTime.now())
                .userId(1)
                .build();
        AuditModel createdAudit = auditRepository.create(audit);

        boolean isDeleted = auditRepository.delete(createdAudit.getId());
        assertTrue(isDeleted);
        assertNull(auditRepository.read(createdAudit.getId()));
    }

    @Test
    public void testFindAll() {
        AuditModel audit1 = AuditModel.builder()
                .action("Action 1")
                .timestamp(LocalDateTime.now())
                .userId(1)
                .build();
        AuditModel audit2 = AuditModel.builder()
                .action("Action 2")
                .timestamp(LocalDateTime.now())
                .userId(2)
                .build();

        auditRepository.create(audit1);
        auditRepository.create(audit2);

        List<AuditModel> audits = auditRepository.findAll();
        assertFalse(audits.isEmpty());
        assertTrue(audits.stream().anyMatch(a -> "Action 1".equals(a.getAction())));
        assertTrue(audits.stream().anyMatch(a -> "Action 2".equals(a.getAction())));
    }
}
