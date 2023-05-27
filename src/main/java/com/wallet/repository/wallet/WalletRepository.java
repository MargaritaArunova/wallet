package com.wallet.repository.wallet;

import com.wallet.model.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    List<Wallet> findAllByPersonId(Long personId);

    Optional<Wallet> findByIdAndPersonId(Long id, Long personId);

    void deleteByIdAndPersonId(Long id, Long personId);

    boolean existsWalletByIdAndPersonId(Long id, Long personId);

}
