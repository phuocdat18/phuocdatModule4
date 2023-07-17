package com.cg.repository;

import com.cg.model.Customer;
import com.cg.model.dto.customer.CustomerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ICustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT NEW com.cg.model.dto.customer.CustomerDTO (" +
            "c.id, " +
            "c.fullName, " +
            "c.email, " +
            "c.phone, " +
            "c.balance, " +
            "c.locationRegion " +
            ") " +
            "FROM Customer c " +
            "WHERE c.deleted = false")
    List<CustomerDTO> findAllCustomerDTO();
    @Query("SELECT NEW com.cg.model.dto.customer.CustomerDTO (" +
            "c.id, " +
            "c.fullName, " +
            "c.email, " +
            "c.phone, " +
            "c.balance, " +
            "c.locationRegion " +
            ") " +
            "FROM Customer c " +
            "WHERE c.id = :id AND c.deleted = false")
    Optional<CustomerDTO> findCustomerDTOById(@Param("id") Long id);

    Optional<Customer> findByIdAndDeletedFalse(Long id);

    @Query("SELECT NEW com.cg.model.dto.customer.CustomerDTO (" +
            "c.id, " +
            "c.fullName " +
            ") " +
            "FROM Customer c " +
            "WHERE c.id <> :id AND c.deleted = false ")
    List<CustomerDTO> findRecipients(@Param("id") Long senderId);

    @Modifying
    @Transactional
    @Query("UPDATE Customer c " +
            "SET c.deleted = TRUE " +
            "WHERE c.id = :id")
    void suspendCustomer(@Param("id") long id);

    @Modifying
    @Transactional
    @Query("UPDATE Customer c " +
            "SET c.balance = c.balance + :amount " +
            "WHERE c.id = :id")
    void increaseBalance(@Param("id") long id, @Param("amount") BigDecimal amount);

    @Modifying
    @Transactional
    @Query("UPDATE Customer c " +
            "SET c.balance = c.balance - :amount " +
            "WHERE c.id = :id")
    void decreaseBalance(@Param("id") long id, @Param("amount") BigDecimal amount);

    Boolean existsByEmailAndDeletedIsFalse(String email);
    boolean existsByEmailAndIdIsNotAndDeletedIsFalse(String email, Long id);
    Boolean existsByPhoneAndDeletedIsFalse(String phone);
    boolean existsByPhoneAndIdIsNotAndDeletedIsFalse(String phone, Long id);

}
