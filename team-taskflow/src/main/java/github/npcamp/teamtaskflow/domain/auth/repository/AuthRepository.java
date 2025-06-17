package github.npcamp.teamtaskflow.domain.auth.repository;

import github.npcamp.teamtaskflow.domain.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, Long>  {

    Optional<Object> findByUserName(String userName);
}
