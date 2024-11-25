package com.vsii.coursemanagement.repositories;

import com.vsii.coursemanagement.entities.RSA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RSARepository extends JpaRepository<RSA, Long> {
}
