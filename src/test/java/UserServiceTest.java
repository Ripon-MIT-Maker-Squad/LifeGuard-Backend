//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.util.List;
//
//import com.riponmakers.lifeguard.UserDatabase.User;
//import com.riponmakers.lifeguard.UserDatabase.UserDatabaseConnector;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.testcontainers.containers.PostgreSQLContainer;
//import com.riponmakers.lifeguard.UserDatabase.UserService;
//
//class UserServiceTest {
//    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
//            "postgres:15-alpine"
//    );
//
//    UserService UserService;
//
//    @BeforeAll
//    static void beforeAll() {
//        postgres.start();
//    }
//
//    @AfterAll
//    static void afterAll() {
//        postgres.stop();
//    }
//
//    @BeforeEach
//    void setUp() {
//        UserDatabaseConnector connectionProvider = new UserDatabaseConnector(
//                postgres.getJdbcUrl(),
//                postgres.getUsername(),
//                postgres.getPassword()
//        );
//
//        UserService = new UserService(connectionProvider);
//    }
//
//    @Test
//    void shouldGetUsers() {
//        UserService.createUser(new User("bigbair", 1241412, true, false));
//        UserService.createUser(new User("cluelessParent", 51231, false, false));
//
//
//        List<User> Users = UserService.getAllUsers();
//        assertEquals(2, Users.size());
//    }
//}