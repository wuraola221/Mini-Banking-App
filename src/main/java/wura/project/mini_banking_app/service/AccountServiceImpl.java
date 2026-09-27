package wura.project.mini_banking_app.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wura.project.mini_banking_app.dto.*;
import wura.project.mini_banking_app.entity.*;
import wura.project.mini_banking_app.exception.AmountMustBeGreaterThanZeroException;
import wura.project.mini_banking_app.exception.InsufficientFundsException;
import wura.project.mini_banking_app.exception.ResourceNotFoundException;
import wura.project.mini_banking_app.repository.AccountRepository;
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
    private final AccountRepository accountRepository;

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
        accountRepository.saveAndFlush(account);

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

    @Override
    @Transactional
    public WithdrawalResponseDto withdraw(String email, BigDecimal amount) {


        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AmountMustBeGreaterThanZeroException("Withdrawal amount must be greater than zero");
        }


        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("User not found"));

        Account account = user.getAccount();

        if (account == null) {
            throw new ResourceNotFoundException(("Account not found"));
        }

        BigDecimal previousBalance = account.getBalance();


        if (previousBalance.compareTo(amount) < 0){
            throw new InsufficientFundsException("Insufficient balance for this withdrawal");
        }

        BigDecimal newBalance = previousBalance.subtract(amount);
        System.out.println("New balance: " + newBalance);

        account.setBalance(newBalance);


        accountRepository.saveAndFlush(account);


        Transaction transaction = Transaction.builder()
                .reference(transactionReferenceGenerator.generate())
                .type(TransactionType.WITHDRAWAL)
                .status(TransactionStatus.SUCCESSFUL)
                .amount(amount)
                .balanceBefore(previousBalance)
                .balanceAfter(newBalance)
                .description("Account withdrawal")
                .createdAt(LocalDateTime.now())
                .account(account)
                .build();

        transactionRepository.save(transaction);



        log.info("Withdrawal successful. Reference: {}, Amount: {}, ", transaction.getReference(), amount);

        return WithdrawalResponseDto.builder()
                .accountNumber(account.getAccountNumber())
                .previousBalance(previousBalance)
                .withdrawnAmount(amount)
                .newBalance(newBalance)
                .transactionReference(transaction.getReference())
                .message("Withdraw successful")
                .build();



    }

    @Override
    @Transactional
    public TransferResponseDto transfer(String senderEmail, TransferRequestDto request) {



        BigDecimal amount = request.getAmount();



        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AmountMustBeGreaterThanZeroException("Transfer amount must be greater than zero");
        }

        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));


        Account senderAccount = sender.getAccount();
        if (senderAccount == null) {
            throw new ResourceNotFoundException("Sender not founnd");
        }


        Account recipientAccount = accountRepository
                .findByAccountNumber(request.getRecipientAccountNumber())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Recipient account not found"));

        if (senderAccount.getId()
                .equals(recipientAccount.getId())) {
            throw new IllegalArgumentException("You cannot transfer money to your own account");
        }

        BigDecimal senderPreviousBalance = senderAccount.getBalance();

        BigDecimal recipientPreviousBalance = recipientAccount.getBalance();

        if (senderPreviousBalance.compareTo(amount) < 0) {

            throw new InsufficientFundsException("Insufficient balance for this transfer");
        }



        BigDecimal senderNewBalance = senderPreviousBalance.subtract(amount);

        BigDecimal recipientNewBalance = recipientPreviousBalance.add(amount);

        senderAccount.setBalance(senderNewBalance);
        recipientAccount.setBalance(recipientNewBalance);


        accountRepository.saveAndFlush(senderAccount);

        accountRepository.saveAndFlush(recipientAccount);

        String transferReference = transactionReferenceGenerator.generateTransferReference();


        String senderTransactionReference = transactionReferenceGenerator.generate();

        String recipientTransactionReference = transactionReferenceGenerator.generate();




        Transaction senderTransaction = Transaction.builder()
                .reference(senderTransactionReference)
                .transferReference(transferReference)
                .type(TransactionType.TRANSFER_OUT)
                .status(TransactionStatus.SUCCESSFUL)
                .amount(amount)
                .balanceBefore(senderPreviousBalance)
                .balanceAfter(senderNewBalance)
                .description(request.getDescription() != null ? request.getDescription() : "Transfer")
                .createdAt(LocalDateTime.now())
                .account(senderAccount)
                .build();




        Transaction recipientTransaction = Transaction.builder()
                .reference(recipientTransactionReference)
                .transferReference(transferReference)
                .type(TransactionType.TRANSFER_IN)
                .status(TransactionStatus.SUCCESSFUL)
                .amount(amount)
                .balanceBefore(recipientPreviousBalance)
                .balanceAfter(recipientNewBalance)
                .description(request.getDescription() != null ? request.getDescription() : "Transfer")
                .createdAt(LocalDateTime.now())
                .account(recipientAccount)
                .build();

            transactionRepository.saveAllAndFlush(List.of(senderTransaction, recipientTransaction));





        return TransferResponseDto.builder()
                .senderAccountNumber(senderAccount.getAccountNumber())
                .recipientAccountNumber(recipientAccount.getAccountNumber())
                .amount(amount)
                .previousBalance(senderPreviousBalance)
                .newBalance(senderNewBalance)
                .transferReference(transferReference)
                .message("Transfer successful")
                .timestamp(LocalDateTime.now())
                .build();

    }


}
