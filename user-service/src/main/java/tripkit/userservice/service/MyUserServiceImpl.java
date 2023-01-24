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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static tripkit.userservice.util.Mapper.toObject;


@Service
@AllArgsConstructor
public class MyUserServiceImpl implements MyUserService{

    private final MyUserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void createUser(MyUserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        MyUser user = toObject(userDto, MyUser.class);
        user.setEncryptedPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
    }

    @Override
    public MyUserDto getUserByUserId(String userId) {
        MyUser savedUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        MyUserDto response = toObject(savedUser, MyUserDto.class);
        return response;
    }

    @Override
    public MyUserDto getUserDetailsByEmail(String email) {
        MyUser savedUser = getMyUserByEmail(email);
        return toObject(savedUser, MyUserDto.class);
    }

    @Override
    public List<MyUserDto> getAllUser() {
        Iterable<MyUser> savedUsers = userRepository.findAll();
        List<MyUserDto> response = new ArrayList<>();
        savedUsers.forEach(user -> {
            response.add(toObject(user, MyUserDto.class));
        });
        return response;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       MyUser savedUser = getMyUserByEmail(email);
        return new User(savedUser.getEmail(), savedUser.getEncryptedPassword(),
                true, true, true, true,
                Collections.emptyList()
        );
    }

    private MyUser getMyUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
