package com.bankIsland.user.dao;

import com.bankIsland.user.entity.AccountOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

public interface AccountOwnerRepository extends JpaRepository<AccountOwner, Integer> {

    Optional<AccountOwner> findByEmail(String email);
    Boolean existsByEmail(String email);

    @Override
    @RestResource(exported = false)
    void deleteById(Integer id);

    @Override
    @RestResource(exported = false)
    void delete(AccountOwner accountOwner);

    @Override
    @RestResource(exported = false)
    <S extends AccountOwner> S save(S entity);

    @Override
    @RestResource(exported = false)
    List<AccountOwner> findAll();

    @Override
    @RestResource(exported = false)
    Optional<AccountOwner> findById(Integer id);

    @Override
    @RestResource(exported = false)
    void deleteAll();

}
