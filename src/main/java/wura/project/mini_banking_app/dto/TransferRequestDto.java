package wura.project.mini_banking_app.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequestDto {
    private String recipientAccountNumber;
    private BigDecimal amount;
    private String description;

}
