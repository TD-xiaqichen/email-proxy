package com.td.dao.mapper;

import com.td.model.Emailexd;
import org.hibernate.internal.SessionImpl;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EmailMapper {

    @Resource
    private EntityManagerFactory entityManagerFactory;

    public Emailexd getMessageById(String messageId){
        System.out.println(messageId);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Emailexd> query = entityManager.createQuery("select s from Emailexd s where s.id=?1", Emailexd.class);
        query.setParameter(1,messageId);
        List<Emailexd> resultList = query.getResultList();
        safelyClose(entityManager);
        return resultList.get(0);
    }

    public List<Emailexd> findList(){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String hql = "select s from Emailexd s order by s.sentDate desc";
        TypedQuery<Emailexd> query = entityManager.createQuery(hql, Emailexd.class);
        List<Emailexd> resultList = query.getResultList();
        safelyClose(entityManager);
        return resultList;
    }

    public void saveBatch(List<Emailexd> emailexdList){
        List<Emailexd> emailexds = emailexdList.stream().filter(a -> {
            return isExist(a.getMessageId()) == false;
        }).collect(Collectors.toList());
        saveEmail(emailexds);
    }

    private boolean isExist(String messageId){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        Connection connection = entityManager.unwrap(Connection.class);
        Connection connection = entityManager.unwrap(SessionImpl.class).connection();
        String sql = "select * from t_email where message_id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1,messageId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else {
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
              if(connection!=null)  connection.close();
              safelyClose(entityManager);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
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
