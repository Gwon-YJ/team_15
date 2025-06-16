package github.npcamp.teamtaskflow.domain.auth.repository;

import github.npcamp.teamtaskflow.domain.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.concurrent.TimeUnit;

public interface LogOutRepository extends JpaRepository<User, Long>  {

}
