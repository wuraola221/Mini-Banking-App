package wura.project.mini_banking_app.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class WithdrawalRequestDto {
    public BigDecimal amount;
}
