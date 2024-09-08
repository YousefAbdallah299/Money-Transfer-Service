package com.transfer.repository;

import com.transfer.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @EntityGraph(attributePaths = {"account"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("""
            SELECT t
            FROM Transaction t
            WHERE t.account.id = :accountId
            """)
    Page<Transaction> getUserTransactionsHistoryByAccountId(@Param("accountId") Long accountId, Pageable pageable);
}
