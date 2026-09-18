package wura.project.mini_banking_app.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LoginResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    private String accountNumber;
    private BigDecimal balance;

    private String token;
    private long expiresIn;
    private String message;
}
