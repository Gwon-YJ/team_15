package github.npcamp.teamtaskflow.domain.dashboard.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;


//조회만 하기 때문에 JPQL 사용
@Repository
public class DashboardRepositoryImpl implements DashboardRepository{

    @PersistenceContext
    private EntityManager em;

    @Override
    public long countAllTasks() {
        return em.createQuery("SELECT COUNT(*) FROM Task",Long.class).getSingleResult();
    }
}
