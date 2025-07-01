package myuserservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import org.springframework.hateoas.server.core.Relation;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Relation(collectionRelation = "users")
public class UserDto {

    private Long id;

    @NotNull(message = "Name is required")
    @Size(min = 2, max = 100)
    private String name;

    @NotNull(message = "Email is required")
    @Size(min = 5, max = 100)
    @Email(message = "Invalid email format")
    private String email;
    @JsonProperty("age")
    private Integer age;
}
