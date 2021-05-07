package com.example.hibernatetest.jpa.repository;

import com.example.hibernatetest.jpa.entity.Human;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

@Repository
public interface HumanRepository extends CrudRepository<Human, Long> {

    Human findOneById(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "from Human w where w.id = :id")
    Human findByIdLocked(@Param("id") Long id);
}
