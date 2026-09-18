package wura.project.mini_banking_app.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DepositResponseDto {
    private String accountNumber;
    private BigDecimal previousBalance;
    private BigDecimal depositedAmount;
    private BigDecimal newBalance;
    private String transactionReference;
    private String message;

}
