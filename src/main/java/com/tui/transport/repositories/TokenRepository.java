package com.tui.transport.repositories;

import com.tui.transport.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, String> {}
