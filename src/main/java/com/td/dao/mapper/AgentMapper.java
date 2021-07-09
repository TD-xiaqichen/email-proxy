package com.td.dao.mapper;

import com.td.dao.AgentRepository;
import com.td.model.Agent;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class AgentMapper implements AgentRepository {

    @Resource
    private EntityManagerFactory entityManagerFactory;

    public Agent getAgentById(Long id){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Agent> query = entityManager.createQuery("select s from Agent s where s.id=?1", Agent.class);
        query.setParameter(1,id);
        List<Agent> resultList = query.getResultList();
        safelyClose(entityManager);
        if(resultList!=null && resultList.size()>0){
            return resultList.get(0);
        }
        return null;
    }

    public Agent getAgentByEmail(String email){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Agent> query = entityManager.createQuery("select s from Agent s where s.email=?1", Agent.class);
        query.setParameter(1,email);
        List<Agent> resultList = query.getResultList();
        safelyClose(entityManager);
        if(resultList!=null && resultList.size()>0){
            return resultList.get(0);
        }
        return null;
    }

    public Agent getAgentByAccount(String account){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Agent> query = entityManager.createQuery("select s from Agent s where s.account=?1", Agent.class);
         query.setParameter(1,account);
        List<Agent> resultList = query.getResultList();
        safelyClose(entityManager);
        if(resultList!=null && resultList.size()>0){
            return resultList.get(0);
        }
        return null;
    }

    public List<Agent> agentList(){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Agent> query = entityManager.createQuery("select s from Agent s", Agent.class);
        List<Agent> resultList = query.getResultList();
        safelyClose(entityManager);
        if(resultList!=null && resultList.size()>0){
            return resultList;
        }
        return null;
    }

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
