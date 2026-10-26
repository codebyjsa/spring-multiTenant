package com.multitenant.sample.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordHashGenerator implements CommandLineRunner {

    private final PasswordEncoder encoder;

    public PasswordHashGenerator(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        System.out.println("TEST HASH: "
                + encoder.encode("ChangeMe123!"));
    }
}