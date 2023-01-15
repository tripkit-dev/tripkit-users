package tripkit.userservice.vo.request;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class LoginRequest {
    @Email
    @NotBlank(message = "Email cannot be blank")
    @Size(min = 2, message = "Email not be less than two characters")
    private String email;
    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be equal or grater than 8 characters and less than 16 characters")
    private String password;
}
