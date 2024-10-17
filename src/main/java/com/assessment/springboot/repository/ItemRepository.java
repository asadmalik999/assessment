package com.assessment.springboot.repository;

import com.assessment.springboot.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("SELECT i FROM Item i JOIN i.category c WHERE c.name = :categoryName")
    Page<Item> findByCategoryName(String categoryName, Pageable pageable);

}
