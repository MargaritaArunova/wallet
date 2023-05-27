package com.wallet.repository.category;

import com.wallet.model.entity.Category;
import com.wallet.model.type.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c where (c.personId = ?1 or c.personId = -1) and c.type = ?2")
    List<Category> findCategoriesByPersonIdAndType(Long personId, TransactionType categoryType);

    Optional<Category> findByIdAndType(Long id, TransactionType type);
}
