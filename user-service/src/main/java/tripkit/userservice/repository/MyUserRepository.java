package tripkit.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tripkit.userservice.domain.MyUser;

import java.util.List;
import java.util.Optional;

public interface MyUserRepository extends JpaRepository<MyUser,Long> {
    //로그인과 JWT를 검증 시 사용자 조회를 위한 메서드를 추가
    Optional<MyUser> findByUserId(String userId);
    Optional<MyUser> findByEmail(String email);
    List<MyUser> findAll();
}
