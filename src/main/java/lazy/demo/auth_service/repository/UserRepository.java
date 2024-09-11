package lazy.demo.auth_service.repository;

import lazy.demo.auth_service.dto.resp.UserDetailResp;
import lazy.demo.auth_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameOrEmail(String username, String email);
    Optional<User> findByEmail(String email);
    List<User> findAllByOrderByUserId();

    @Query(name = "User.findUserDetailRespMapping", nativeQuery = true)
    Optional<UserDetailResp> findUserDetailByUsername(String username, String email);
}