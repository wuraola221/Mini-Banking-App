package wura.project.mini_banking_app.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransferResponseDto {
    private String senderAccountNumber;
    private String recipientAccountNumber;

    private BigDecimal amount;

    private BigDecimal previousBalance;
    private BigDecimal newBalance;
    private String transferReference;

    private String transactionReference;
    private String message;

    private LocalDateTime timestamp;
}
