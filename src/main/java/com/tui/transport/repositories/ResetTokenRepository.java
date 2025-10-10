package com.tui.transport.repositories;

import com.tui.transport.models.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetTokenRepository extends JpaRepository<ResetToken, String> {
}
