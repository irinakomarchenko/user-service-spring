package myuserservice.integration;

import myuserservice.dto.UserDto;
import myuserservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveUser() {
        var user = myuserservice.entity.User.builder()
                .name("Zoe")
                .email("zoe@example.com")
                .age(30)
                .build();

        var saved = userRepository.save(user);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("zoe@example.com");
    }
}