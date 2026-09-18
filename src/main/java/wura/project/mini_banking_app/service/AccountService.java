package wura.project.mini_banking_app.service;

import wura.project.mini_banking_app.dto.DepositResponseDto;
import wura.project.mini_banking_app.dto.TransactionResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    DepositResponseDto deposit (String email, BigDecimal amount);

    List<TransactionResponseDto> getTransactionHistory(String email);
}
