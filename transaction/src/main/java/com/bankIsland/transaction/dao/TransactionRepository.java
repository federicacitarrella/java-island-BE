package com.bankIsland.transaction.dao;

import com.bankIsland.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findAllByAccountOwnerIdFromOrAccountOwnerIdTo(int accountOwnerIdFrom, int accountOwnerIdTo);

    List<Transaction> findAllByAccountNumberFromOrAccountNumberTo(String accountNumberFrom, String accountNumberTo);

    Transaction findByAccountNumberTo(String accountNumberTo);

    @Override
    @RestResource(exported = false)
    void deleteById(Integer id);

    @Override
    @RestResource(exported = false)
    void delete(Transaction transaction);

    @Override
    @RestResource(exported = false)
    <S extends Transaction> S save(S entity);

    @Override
    @RestResource(exported = false)
    List<Transaction> findAll();

    @Override
    @RestResource(exported = false)
    Optional<Transaction> findById(Integer id);

    @Override
    @RestResource(exported = false)
    void deleteAll();

}
