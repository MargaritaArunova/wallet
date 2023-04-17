package com.wallet.repository;

import com.wallet.model.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

    List<Operation> findAllByWalletId(Long walletId);

    Operation findByIdAndWalletId(Long id, Long walletId);

    void deleteAllByWalletId(Long walletId);

    void deleteByIdAndWalletId(Long id, Long walletId);
}
