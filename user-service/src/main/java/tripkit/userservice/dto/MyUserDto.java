package tripkit.userservice.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MyUserDto {
    private String email;
    private String password;
    private String name;
    private String userId;
    private LocalDateTime createdAt;
    private String encryptedPassword;
    //private List<OrderResponse> orders = new ArrayList<>();
}
