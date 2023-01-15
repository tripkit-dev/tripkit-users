package tripkit.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tripkit.userservice.domain.MyUser;

public interface MyUserRepository extends JpaRepository<MyUser,Long> {
    //로그인과 JWT를 검증 시 사용자 조회를 위한 메서드를 추가
    MyUser findByEmail(String email);
}
