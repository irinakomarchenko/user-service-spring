package myuserservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import myuserservice.dto.UserDto;
import myuserservice.entity.User;
import myuserservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanDb() {
        userRepository.deleteAll();
    }

    @Test
    void testCreateUser_andGetById() throws Exception {
        UserDto request = UserDto.builder()
                .name("Alice")
                .email("alice@example.com")
                .age(25)
                .build();

        String response = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long createdId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/users/" + createdId)
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$._links.delete.href").exists());
    }

    @Test
    void testGetAllUsers() throws Exception {
        userRepository.saveAll(
                java.util.List.of(
                        User.builder().name("Alice").email("alice@example.com").age(25).build(),
                        User.builder().name("Bob").email("bob@example.com").age(30).build()
                )
        );

        mockMvc.perform(get("/api/users").accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.users[0].name").value("Alice"))
                .andExpect(jsonPath("$._embedded.users[1].name").value("Bob"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testUpdateUser() throws Exception {
        User user = userRepository.save(User.builder()
                .name("Alice")
                .email("alice@example.com")
                .age(25)
                .build());

        UserDto updated = UserDto.builder()
                .id(user.getId())
                .name("Updated")
                .email("alice@example.com")
                .age(28)
                .build();

        mockMvc.perform(put("/api/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.age").value(28))
                .andExpect(jsonPath("$._links.update.href").exists());
    }

    @Test
    void testDeleteUser() throws Exception {
        User user = userRepository.save(User.builder()
                .name("Alice")
                .email("alice@example.com")
                .age(25)
                .build());

        mockMvc.perform(delete("/api/users/" + user.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateUser_invalidName() throws Exception {
        UserDto invalidDto = UserDto.builder().name("A").email("a@ex.com").age(20).build();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateUser_duplicateEmail() throws Exception {
        userRepository.save(User.builder()
                .name("Alice")
                .email("alice@example.com")
                .age(25)
                .build());

        UserDto user = UserDto.builder().name("Alice2").email("alice@example.com").age(25).build();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }
}
