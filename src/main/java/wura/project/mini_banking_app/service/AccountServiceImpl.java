package wura.project.mini_banking_app.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wura.project.mini_banking_app.dto.DepositResponseDto;
import wura.project.mini_banking_app.dto.TransactionResponseDto;
import wura.project.mini_banking_app.entity.*;
import wura.project.mini_banking_app.exception.ResourceNotFoundException;
import wura.project.mini_banking_app.repository.TransactionRepository;
import wura.project.mini_banking_app.repository.UserRepository;
import wura.project.mini_banking_app.util.TransactionReferenceGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService{

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionReferenceGenerator transactionReferenceGenerator;

    @Override
    @Transactional
    public DepositResponseDto deposit (String email, BigDecimal amount){

        if (amount == null) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }

        User user = userRepository.findByEmail(email).orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Account account = user.getAccount();

        if (account == null) {
            throw new ResourceNotFoundException("Account not found");
        }
        BigDecimal previousBalance = account.getBalance();
        BigDecimal newBalance = previousBalance.add(amount);
        account.setBalance(newBalance);

        Transaction transaction =
                Transaction.builder()
                        .reference(
                                transactionReferenceGenerator.generate()
                        )
                        .type(TransactionType.DEPOSIT)
                        .status(TransactionStatus.SUCCESSFUL)
                        .amount(amount)
                        .balanceBefore(previousBalance)
                        .balanceAfter(newBalance)
                        .description("Account deposit")
                        .createdAt(LocalDateTime.now())
                        .account(account)
                        .build();

        transactionRepository.save(transaction);

        log.info(
                "Deposit successful. Reference: {}, Amount: {}",
                transaction.getReference(),
                amount
        );

        return DepositResponseDto.builder()
                .accountNumber(account.getAccountNumber())
                .previousBalance(previousBalance)
                .depositedAmount(amount)
                .newBalance(newBalance)
                .transactionReference(transaction.getReference())
                .message("Deposit successful")
                .build();
    }


    @Override
    public List<TransactionResponseDto> getTransactionHistory(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Account account = user.getAccount();

        if (account == null) {
            throw new ResourceNotFoundException("Account not found");
        }

        List<Transaction> transactions = transactionRepository.findByAccountIdOrderByCreatedAtDesc(
                                account.getId());

        return transactions.stream()
                .map(transaction ->
                        TransactionResponseDto.builder()
                                .reference(
                                        transaction.getReference()
                                )
                                .type(
                                        transaction.getType()
                                )
                                .status(
                                        transaction.getStatus()
                                )
                                .amount(
                                        transaction.getAmount()
                                )
                                .balanceBefore(
                                        transaction.getBalanceBefore()
                                )
                                .balanceAfter(
                                        transaction.getBalanceAfter()
                                )
                                .description(
                                        transaction.getDescription()
                                )
                                .createdAt(
                                        transaction.getCreatedAt()
                                )
                                .build()
                )
                .toList();
    }


}
