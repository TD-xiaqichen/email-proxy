package com.td.dao.mapper;

import com.td.model.Emailexd;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class EmailMapper {

    @Resource
    private EntityManagerFactory entityManagerFactory;


    public List<Emailexd> findList(){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Emailexd> query = entityManager.createQuery("select s from Emailexd s", Emailexd.class);
        List<Emailexd> resultList = query.getResultList();
        safelyClose(entityManager);
        return resultList;
    }

    public void saveEmail(List<Emailexd> emailexdList){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        int j = 0;
        for(Emailexd email:emailexdList){
            entityManager.persist(email);
            j++;
            if(j%50==0 || j==emailexdList.size()){
                try{
                    entityManager.flush();
                }catch (Exception e){
                     e.printStackTrace();
                }finally {
                  entityManager.clear();
                }
            }
        }
        transaction.commit();
        safelyClose(entityManager);
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
