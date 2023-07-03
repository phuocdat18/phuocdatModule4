package com.cg.repository;

import com.cg.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @PersistenceContext
    EntityManager em = null;


    public default List<Customer> findRecipients(Long id) {
        String sql = "select c from Customer c WHERE c.id<>:id and c.deleted = false";
        TypedQuery<Customer> query = em.createQuery(sql, Customer.class);
        query.setParameter("id", id);
        return query.getResultList();
    }
}