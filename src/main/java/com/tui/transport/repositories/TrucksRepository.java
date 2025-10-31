package com.tui.transport.repositories;

import com.tui.transport.models.Truck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrucksRepository extends JpaRepository<Truck, Long> {
}
