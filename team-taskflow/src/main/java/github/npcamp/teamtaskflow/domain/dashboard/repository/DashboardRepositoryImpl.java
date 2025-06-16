package github.npcamp.teamtaskflow.domain.dashboard.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;


//JPQL 사용
@Repository
public class DashboardRepositoryImpl implements DashboardRepository{

    @PersistenceContext
    private EntityManager em;

    @Override
    public long countAllTasks() {
        return em.createQuery("SELECT COUNT(t) FROM Task t WHERE t.isDeleted=false",Long.class).getSingleResult();
    }
}
