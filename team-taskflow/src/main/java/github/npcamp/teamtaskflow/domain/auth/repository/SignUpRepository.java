package github.npcamp.teamtaskflow.domain.auth.repository;

import github.npcamp.teamtaskflow.domain.common.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SignUpRepository extends JpaRepository<User, Long> {

    Optional<Object> findByUserName(@NotBlank(message = "userName 필수값") String userName);
}
