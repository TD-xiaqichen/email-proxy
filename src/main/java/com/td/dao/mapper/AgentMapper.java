package com.td.dao.mapper;

import com.td.dao.AgentRepository;
import com.td.model.Agent;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

@Repository
public class AgentMapper implements AgentRepository {

    @Resource
    private EntityManagerFactory entityManagerFactory;

    public void saveAgent(Agent agent){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try{
            transaction.begin();
            entityManager.persist(agent);
            transaction.commit();
        }catch (Exception e){
            transaction.rollback();
        }finally {
            safelyClose(entityManager);

        }

    }

    private void safelyClose(EntityManager entityManager) {

        if (entityManager == null) {
            return;
        }

        if (entityManager.isOpen()) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            entityManager.close();
        }

    }

}
