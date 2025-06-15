package myuserservice.service;

import myuserservice.dto.UserDto;
import myuserservice.entity.User;
import myuserservice.mapper.UserMapper;
import myuserservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private  static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toEntity(userDto);
        user.setId(null);
        User saved = userRepository.save(user);
        log.info("Пользователь добавлен: {}", saved);
        return UserMapper.toDto(saved);
    }

    @Override
    public UserDto getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id " + id + " не найден"));
        log.info("Пользователь найден: {}", user);
        return UserMapper.toDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        log.info("Найдено пользователей: {}", users.size());
        return users.stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id " + id + " не найден"));
        log.info("Обновление пользователя с id {}: {}", id, userDto);
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAge(userDto.getAge());

        User updated = userRepository.save(user);
        log.info("Пользователь обновлён: {}", updated);
        return UserMapper.toDto(updated);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Пользователь с id " + id + " не найден");
        }
        userRepository.deleteById(id);
        log.info("Пользователь с id {} удалён", id);
    }
}