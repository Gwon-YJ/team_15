package github.npcamp.teamtaskflow.domain.user.repository;

import github.npcamp.teamtaskflow.domain.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByOrderByUpdatedAtDesc();

    List<User> findByUserNameContainingOrderByUpdatedAtDesc(String userName);
}

