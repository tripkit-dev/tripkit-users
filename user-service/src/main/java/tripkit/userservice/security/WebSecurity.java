package tripkit.userservice.security;

import org.springframework.context.annotation.Configuration;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tripkit.userservice.service.MyUserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private final MyUserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Environment environment;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        //actuator, health-check 부분은 다음 장을 위한 부분으로 무시
        http.authorizeRequests().antMatchers("/actuator/**").permitAll();
        http.authorizeRequests().antMatchers("/health-check/**").permitAll();
        //모든 요청("/**", 0.0.0.0/0)을 바로 위에서 만들었던 AuthenticationFilter를 거쳐서 들어오도록 한다.
        http.authorizeRequests().antMatchers("/**")
                .hasIpAddress("0.0.0.0/0")
                .and()
                .addFilter(getAuthenticationFilter());

        http.headers().frameOptions().disable();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager(), userService, environment);
        authenticationFilter.setAuthenticationManager(authenticationManager());
        return authenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //AuthenticationManagerBuilder에게 사용자가 정의한 userService(UserDetailsService를 구현한 클래스)가 무엇인지 passwordEncoder(BCryptPasswordEncoder와 같이 사용자가 지정한)는 무엇인지 전달한다.
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

}
