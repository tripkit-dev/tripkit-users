package tripkit.userservice.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import tripkit.userservice.dto.MyUserDto;

public interface MyUserService extends UserDetailsService {
    MyUserDto getUserDetailsByEmail(String email);
}
