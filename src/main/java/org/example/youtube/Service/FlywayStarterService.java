package org.example.youtube.Service;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class FlywayStarterService implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("flyway starter service");

        Flyway.configure().baselineOnMigrate(true).dataSource(dataSource).load().migrate();

    }
}
