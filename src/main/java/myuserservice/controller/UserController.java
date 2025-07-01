package myuserservice.controller;


import myuserservice.dto.UserDto;
import myuserservice.service.UserService;
import myuserservice.hateoas.UserModelAssembler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserModelAssembler assembler;

    public UserController(UserService userService, UserModelAssembler assembler) {
        this.userService = userService;
        this.assembler = assembler;
    }

    @PostMapping
    public EntityModel<UserDto> createUser (@Valid @RequestBody UserDto userDto) {
        log.info("POST /api/users — Запрос на создание пользователя: {}", userDto);
        UserDto created = userService.createUser(userDto);
        log.info("Пользователь создан:  id={}, email={}", created.getId(), created.getEmail());
        return assembler.toModel(created) ;
    }

    @GetMapping("/{id}")
    public EntityModel<UserDto> getUserById(@PathVariable Long id) {
        log.info("GET /api/users/{} — Запрос пользователя по id", id);
        UserDto user = userService.getUser(id);
        log.info("Пользователь найден: id={}, email={}", user.getId(), user.getEmail());
        return assembler.toModel(user);
    }

    @GetMapping
    public CollectionModel<EntityModel<UserDto>> getAllUsers() {
        log.info("GET /api/users — Запрос списка всех пользователей");
        List<UserDto> users = userService.getAllUsers();
        log.info("Найдено пользователей: {}", users.size());

        List<EntityModel<UserDto>> userModels = users.stream()
                .map(assembler::toModel)
                .toList();
        return  CollectionModel.of(
                userModels,
                org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
                        .linkTo(org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
                                .methodOn(UserController.class).getAllUsers())
                        .withSelfRel()
        );
    }

    @PutMapping("/{id}")
    public EntityModel<UserDto > updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        log.info("PUT /api/users/{} — Запрос на обновление пользователя: {}", id, userDto);
        UserDto updated = userService.updateUser(id, userDto);
        log.info("Пользователь обновлён: {}", updated);
        return assembler.toModel(updated) ;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("DELETE /api/users/{} — Запрос на удаление пользователя", id);
        userService.deleteUser(id);
        log.info("Пользователь с id={} удалён", id);
        return ResponseEntity.noContent().build();
    }
}

