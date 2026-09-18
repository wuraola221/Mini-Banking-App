package wura.project.mini_banking_app.dto;

import lombok.Builder;
import lombok.Data;
import wura.project.mini_banking_app.entity.TransactionStatus;
import wura.project.mini_banking_app.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponseDto {
    private String reference;
    private TransactionType type;
    private TransactionStatus status;

    private BigDecimal amount;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;

    private String description;
    private LocalDateTime createdAt;
}
