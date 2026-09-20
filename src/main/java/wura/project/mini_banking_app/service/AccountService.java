package wura.project.mini_banking_app.service;

import wura.project.mini_banking_app.dto.*;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    DepositResponseDto deposit(String email, BigDecimal amount);

    List<TransactionResponseDto> getTransactionHistory(String email);

    WithdrawalResponseDto withdraw(String email, BigDecimal amount);

    TransferResponseDto transfer(String senderEmail, TransferRequestDto request);
}