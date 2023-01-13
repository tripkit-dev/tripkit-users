package tripkit.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tripkit.userservice.domain.MyUser;

public interface MyUserRepository extends JpaRepository<MyUser,Long> {
}
