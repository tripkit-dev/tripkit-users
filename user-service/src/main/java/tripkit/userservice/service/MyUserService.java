package tripkit.userservice.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import tripkit.userservice.dto.MyUserDto;

import java.util.List;

public interface MyUserService extends UserDetailsService {
    void createUser(MyUserDto userDTO);
    MyUserDto getUserByUserId(String userId);
    MyUserDto getUserDetailsByEmail(String email);
    List<MyUserDto> getAllUser();
}
