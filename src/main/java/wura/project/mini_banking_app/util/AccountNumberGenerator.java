package wura.project.mini_banking_app.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class AccountNumberGenerator {

    private final SecureRandom secureRandom =
            new SecureRandom();

    public String generate() {

        long number =
                1_000_000_000L +
                        secureRandom.nextLong(9_000_000_000L);

        return String.valueOf(number);
    }
}
