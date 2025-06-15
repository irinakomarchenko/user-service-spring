package myuserservice.mapper;

import myuserservice.entity.User;
import myuserservice.dto.UserDto;

public class UserMapper {

    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
            return UserDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .build();
        }

        public static User toEntity(UserDto dto) {
            if (dto == null) {
                return null;
            }
            return User.builder()
                    .id(dto.getId())
                    .name(dto.getName())
                    .email(dto.getEmail())
                    .age(dto.getAge())
                    .build();
        }
}