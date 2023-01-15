package tripkit.userservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tripkit.userservice.dto.MyUserDto;
import tripkit.userservice.service.MyUserService;
import tripkit.userservice.vo.request.LoginRequest;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final MyUserService userService;
    private final Environment environment;
    public AuthenticationFilter(AuthenticationManager authenticationManager, MyUserService userService, Environment environment){
        super(authenticationManager);
        this.userService = userService;
        this.environment = environment;
    }

    /**
     * 로그인 요청이 들어오면 상위 클래스의 getAuthenticationManager()를 호출하여
     * 사용자가 전달한 파라미터를 전달하여 로그인을 시도함
     * @param request from which to extract parameters and perform the authentication
     * @param response the response, which may be needed if the implementation has to do a
     * redirect as part of a multi-stage authentication process (such as OpenID).
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(),LoginRequest.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword(),
                            Collections.emptyList()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 로그인이 성공하면 db에서 사용자의 userId를 조회하여 jwt를 생성하고, response의 헤더에 넣는 역할을 한다
     * @param request
     * @param response
     * @param chain
     * @param authResult the object returned from the <tt>attemptAuthentication</tt>
     * method.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String userName = ((User)(authResult.getPrincipal())).getUsername();
        MyUserDto userDto = userService.getUserDetailsByEmail(userName);

        String token = Jwts.builder()
                .setSubject(userDto.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(Objects.requireNonNull(environment.getProperty("token.expiration_time")))))
                .signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
                .compact();

        response.addHeader("token", token);
        response.addHeader("userId", userDto.getUserId());
    }
}
