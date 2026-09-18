package wura.project.mini_banking_app.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RegisterResponseDto {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String accountNumber;
        private BigDecimal balance;

}
