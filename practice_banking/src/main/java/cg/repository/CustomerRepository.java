package com.cg.repository;

import com.cg.model.Customer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Repository
@Transactional
public class CustomerRepository implements ICustomerRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Customer> findAll() {
        String sql = "select c from Customer c WHERE c.deleted = false";
        TypedQuery<Customer> query = em.createQuery(sql, Customer.class);
        return query.getResultList();
    }

    @Override
    public Customer findById(Long id) {
        String sql = "select c from Customer c where  c.id=:id and c.deleted = false";
        TypedQuery<Customer> query = em.createQuery(sql, Customer.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Customer> findRecipients(Long id) {
        String sql = "select c from Customer c WHERE c.id<>:id and c.deleted = false";
        TypedQuery<Customer> query = em.createQuery(sql, Customer.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public void save(Customer customer) {
        if (customer != null) {
            if (customer.getBalance() == null) {
                customer.setBalance(BigDecimal.ZERO);
            } else {
                customer.setBalance(customer.getBalance());
            }
            em.merge(customer);
        } else {
            em.persist(customer);
        }
    }

    @Override
    public void remove(Long id) {
        Customer customer = findById(id);
        if (customer != null) {
            em.remove(customer);
        }
    }

}
