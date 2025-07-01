package myuserservice.hateoas;

import myuserservice.dto.UserDto;
import myuserservice.controller.UserController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserDto, EntityModel<UserDto>> {

    @Override
    public EntityModel<UserDto> toModel(UserDto user) {
        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).deleteUser(user.getId())).withRel("delete"),
                linkTo(methodOn(UserController.class).updateUser(user.getId(), user)).withRel("update"),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users"));
    }

}
