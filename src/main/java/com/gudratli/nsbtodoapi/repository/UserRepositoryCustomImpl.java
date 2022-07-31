package com.gudratli.nsbtodoapi.repository;

import com.gudratli.nsbtodoapi.entity.Role;
import com.gudratli.nsbtodoapi.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom
{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public User changePassword (Integer id, String password)
    {
        String query = "update User u set u.password = :password where u.id = :id";

        Query resultQuery = entityManager.createQuery(query);

        resultQuery.setParameter("id", id);
        resultQuery.setParameter("password", password);

        resultQuery.executeUpdate();

        query = "select u from User u where u.id = :id";
        resultQuery = entityManager.createQuery(query, User.class);
        resultQuery.setParameter("id", id);

        return (User) resultQuery.getSingleResult();
    }

    @Override
    @Transactional
    public User changeStatus (Integer id, Boolean status)
    {
        String query = "update User u set u.status = :status where u.id = :id";

        Query resultQuery = entityManager.createQuery(query);

        resultQuery.setParameter("id", id);
        resultQuery.setParameter("status", status);

        resultQuery.executeUpdate();

        query = "select u from User u where u.id = :id";
        resultQuery = entityManager.createQuery(query, User.class);
        resultQuery.setParameter("id", id);

        return (User) resultQuery.getSingleResult();
    }

    @Override
    @Transactional
    public User changeBanned (Integer id, Boolean banned)
    {
        String query = "update User u set u.banned = :banned where u.id = :id";

        Query resultQuery = entityManager.createQuery(query);

        resultQuery.setParameter("id", id);
        resultQuery.setParameter("banned", banned);

        resultQuery.executeUpdate();

        query = "select u from User u where u.id = :id";
        resultQuery = entityManager.createQuery(query, User.class);
        resultQuery.setParameter("id", id);

        return (User) resultQuery.getSingleResult();
    }

    @Override
    @Transactional
    public User changeRole (Integer id, Role role)
    {
        String query = "update User u set u.role = :role where u.id = :id";

        Query resultQuery = entityManager.createQuery(query);

        resultQuery.setParameter("id", id);
        resultQuery.setParameter("role", role);

        resultQuery.executeUpdate();

        query = "select u from User u where u.id = :id";
        resultQuery = entityManager.createQuery(query, User.class);
        resultQuery.setParameter("id", id);

        return (User) resultQuery.getSingleResult();
    }
}
