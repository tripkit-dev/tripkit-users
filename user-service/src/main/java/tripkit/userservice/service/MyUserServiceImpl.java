package tripkit.userservice.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tripkit.userservice.domain.MyUser;
import tripkit.userservice.dto.MyUserDto;
import tripkit.userservice.repository.MyUserRepository;
import tripkit.userservice.util.Mapper;

import java.util.Collections;


@Service
@AllArgsConstructor
public class MyUserServiceImpl implements MyUserService{

    private final MyUserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public MyUserDto getUserDetailsByEmail(String email) {
        MyUser savedUser = getMyUserByEmail(email);
        return Mapper.toObject(savedUser, MyUserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       MyUser savedUser = getMyUserByEmail(email);
        return new User(savedUser.getEmail(), savedUser.getEncryptedPassword(),
                true, true, true, true,
                Collections.emptyList()
        );
    }

    private MyUser getMyUserByEmail(String email){
        //TODO NULL일 때 처리해주기
        return userRepository.findByEmail(email);
    }

}
