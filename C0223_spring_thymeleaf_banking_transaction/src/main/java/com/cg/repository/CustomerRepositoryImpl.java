//package com.cg.repository;
//
//import com.cg.model.Customer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Example;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.repository.query.FluentQuery;
//import org.springframework.stereotype.Repository;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.TypedQuery;
//import java.util.List;
//import java.util.Optional;
//import java.util.function.Function;
//
//@Repository
//public class CustomerRepositoryImpl implements CustomerRepository {
//    @PersistenceContext
//    private EntityManager em;
//
//    @Override
//    public List<Customer> findRecipients(Long id) {
//        String sql = "select c from Customer c WHERE c.id<>:id and c.deleted = false";
//        TypedQuery<Customer> query = em.createQuery(sql, Customer.class);
//        query.setParameter("id", id);
//        return query.getResultList();
//    }
//}
