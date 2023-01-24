package tripkit.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tripkit.userservice.dto.MyUserDto;
import tripkit.userservice.service.MyUserService;
import tripkit.userservice.vo.request.MyUserSaveRequest;
import tripkit.userservice.vo.response.MyUserResponse;

import java.util.ArrayList;
import java.util.List;

import static tripkit.userservice.util.Mapper.toObject;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class MyUserController {
    private final Environment environment;
    private final MyUserService userService;

//    @GetMapping("/health-check")
//    @Timed(value = "users.status", longTask = true)
//    public String healthCheck() {
//        return String.format("expiration_time: %s, secret: %s, password: %s",
//                environment.getProperty("token.expiration_time"),
//                environment.getProperty("token.secret"),
//                environment.getProperty("spring.datasource.password")
//        );
//    }

    @GetMapping("/welcome")
    public String welcome() {
        return environment.getProperty("greeting.message");
    }

    @PostMapping("/users")
    public ResponseEntity<MyUserResponse> createUser(@RequestBody MyUserSaveRequest request) {
        MyUserDto userDto = toObject(request, MyUserDto.class);
        userService.createUser(userDto);
        MyUserResponse response = toObject(userDto, MyUserResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/users")
    public ResponseEntity<List<MyUserResponse>> getUsers() {
        List<MyUserDto> savedUsers = userService.getAllUser();
        List<MyUserResponse> response = new ArrayList<>();
        savedUsers.forEach(user -> {
            response.add(toObject(user, MyUserResponse.class));
        });
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

//    @GetMapping("/users/{userId}")
//    public ResponseEntity<MyUserResponse> getUser(@PathVariable("userId") String userId) {
//        MyUserDto userDto = userService.getUserByUserId(userId);
//        MyUserResponse response = toObject(userDto, MyUserResponse.class);
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }

}
