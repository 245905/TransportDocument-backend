package com.tui.transport.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequiredArgsConstructor
public class SqlController {
    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${sql.debug}")
    private boolean sqlDebug;

    //TODO: NOT SAFE, USE ONLY FOR TESTING
    @PostMapping("/sql")
    public ResponseEntity<String> executeSql(@RequestBody String sql) {
        if (!sqlDebug) {
            logger.error("Attempt to execute SQL while disabled: {}", sql);
            return ResponseEntity.status(403).body("SQL execution is disabled");
        }
        try {
            jdbcTemplate.execute(sql);
            logger.warn("Executed SQL: {}", sql);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
