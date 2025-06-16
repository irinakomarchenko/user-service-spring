package myuserservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import myuserservice.dto.UserDto;
import myuserservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }

    @Test
    void testCreateUser() throws Exception {
        UserDto requestDto = UserDto.builder()
                .name("Alice")
                .email("alice@example.com")
                .age(25)
                .build();

        UserDto responseDto = UserDto.builder()
                .id(1L)
                .name("Alice")
                .email("alice@example.com")
                .age(25)
                .build();

        Mockito.when(userService.createUser(any(UserDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.age").value(25));
    }

    @Test
    void testGetAllUsers() throws Exception {
        List<UserDto> users = List.of(
                UserDto.builder().id(1L).name("Alice").email("alice@example.com").age(25).build(),
                UserDto.builder().id(2L).name("Bob").email("bob@example.com").age(30).build()
        );

        Mockito.when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[1].name").value("Bob"));
    }

    @Test
    void testGetUserById() throws Exception {
        UserDto user = UserDto.builder().id(1L).name("Alice").email("alice@example.com").age(25).build();

        Mockito.when(userService.getUser(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void testUpdateUser() throws Exception {
        UserDto updated = UserDto.builder()
                .id(1L)
                .name("Updated")
                .email("alice@example.com")
                .age(28)
                .build();

        Mockito.when(userService.updateUser(eq(1L), any(UserDto.class))).thenReturn(updated);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.age").value(28));
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());

        Mockito.verify(userService).deleteUser(1L);
    }

    @Test
    void testCreateUser_invalidName() throws Exception {
        UserDto invalidDto = UserDto.builder().name("A").email("a@ex.com").age(20).build();

        mockMvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetUser_notFound() throws Exception {
        Mockito.when(userService.getUser(100L)).thenThrow(new EntityNotFoundException("User not found"));

        mockMvc.perform(get("/api/users/100"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }

    @Test
    void testCreateUser_duplicateEmail() throws Exception {
        UserDto user = UserDto.builder().name("Alice").email("alice@example.com").age(25).build();

        Mockito.when(userService.createUser(any(UserDto.class)))
                .thenThrow(new DataIntegrityViolationException("Email already exists"));

        mockMvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email already exists"));
    }
}
