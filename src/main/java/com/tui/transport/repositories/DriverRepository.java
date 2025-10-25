package com.tui.transport.repositories;

import com.tui.transport.models.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long>{
    Optional<Driver> findByPhoneNumber(String phoneNumber);

    Optional<Driver> findByEmail(String email);
}
