package myuserservice.mapper;

import org.junit.jupiter.api.Test;
import myuserservice.dto.UserDto;
import myuserservice.entity.User;


import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    @Test
    void toDto_shouldReturnNullWhenUserIsNull() {
        UserDto result = UserMapper.toDto(null);
        assertThat(result).isNull();
    }

    @Test
    void toEntity_shouldReturnNullWhenDtoIsNull() {
        User result = UserMapper.toEntity(null);
        assertThat(result).isNull();
    }

    @Test
    void toDto_shouldMapFieldsCorrectly() {
        User user = User.builder()
                .id(1L)
                .name("Alice")
                .email("alice@example.com")
                .age(30)
                .build();

        UserDto dto = UserMapper.toDto(user);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Alice");
        assertThat(dto.getEmail()).isEqualTo("alice@example.com");
        assertThat(dto.getAge()).isEqualTo(30);
    }


    @Test
    void toEntity_shouldMapFieldsCorrectly() {
        UserDto dto = UserDto.builder()
                .id(1L)
                .name("Alice")
                .email("alice@example.com")
                .age(30)
                .build();

        User user = UserMapper.toEntity(dto);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("Alice");
        assertThat(user.getEmail()).isEqualTo("alice@example.com");
        assertThat(user.getAge()).isEqualTo(30);
    }
}
