package wura.project.mini_banking_app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wura.project.mini_banking_app.dto.WithdrawalResponseDto;
import wura.project.mini_banking_app.entity.*;
import wura.project.mini_banking_app.exception.AmountMustBeGreaterThanZeroException;
import wura.project.mini_banking_app.exception.InsufficientFundsException;
import wura.project.mini_banking_app.repository.AccountRepository;
import wura.project.mini_banking_app.repository.TransactionRepository;
import wura.project.mini_banking_app.repository.UserRepository;
import wura.project.mini_banking_app.util.TransactionReferenceGenerator;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionReferenceGenerator transactionReferenceGenerator;

    @Mock
    private AccountRepository accountRepository;

    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountServiceImpl(
                userRepository,
                transactionRepository,
                transactionReferenceGenerator,
                accountRepository);
    }

    @Test
    void shouldRejectNegativeWithdrawalAmount() {

        String email = "test@example.com";
        BigDecimal amount = new BigDecimal("-500.00");

        assertThatThrownBy(() -> accountService.withdraw(email, amount))
                .isInstanceOf(AmountMustBeGreaterThanZeroException.class)
                .hasMessage("Withdrawal amount must be greater than zero");

        verifyNoInteractions(userRepository, accountRepository, transactionRepository);
    }

    @Test
    void shouldRejectNullWithdrawalAmount() {

        assertThatThrownBy(() -> accountService.withdraw("test@example.com", null))
                              .isInstanceOf(AmountMustBeGreaterThanZeroException.class);

        verifyNoInteractions(userRepository, accountRepository, transactionRepository);
    }

    @Test
    void shouldRejectWithdrawalWhenBalanceIsInsufficient() {

        String email = "test@example.com";

        Account account = Account.builder()
                .balance(new BigDecimal("500.00"))
                .build();

        User user = User.builder()
                .email(email)
                .account(account)
                .build();

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        assertThatThrownBy(() -> accountService.withdraw(email, new BigDecimal("1000.00")))
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessage("Insufficient balance for this withdrawal");

        assertThat(account.getBalance()).isEqualByComparingTo("500.00");

        verify(accountRepository, never()).saveAndFlush(any());

        verify(transactionRepository, never()).save(any());
    }

    @Test
    void shouldWithdrawSuccessfully() {


        String email = "test@example.com";

        Account account = Account.builder()
                .accountNumber("1234567890")
                .balance(new BigDecimal("10000.00"))
                .build();

        User user = User.builder()
                .email(email)
                .account(account)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        when(transactionReferenceGenerator.generate()).thenReturn("TXN-123456");


        WithdrawalResponseDto response = accountService.withdraw(email, new BigDecimal("2000.00"));


        assertThat(response.getPreviousBalance()).isEqualByComparingTo("10000.00");

        assertThat(response.getWithdrawnAmount()).isEqualByComparingTo("2000.00");

        assertThat(response.getNewBalance()).isEqualByComparingTo("8000.00");

        assertThat(response.getTransactionReference()).isEqualTo("TXN-123456");

        assertThat(account.getBalance()).isEqualByComparingTo("8000.00");

        verify(accountRepository).saveAndFlush(account);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);

        verify(transactionRepository).save(transactionCaptor.capture());

        Transaction savedTransaction = transactionCaptor.getValue();

        assertThat(savedTransaction.getType()).isEqualTo(TransactionType.WITHDRAWAL);

        assertThat(savedTransaction.getStatus()).isEqualTo(TransactionStatus.SUCCESSFUL);

        assertThat(savedTransaction.getAmount()).isEqualByComparingTo("2000.00");

        assertThat(savedTransaction.getBalanceBefore()).isEqualByComparingTo("10000.00");

        assertThat(savedTransaction.getBalanceAfter()).isEqualByComparingTo("8000.00");

        assertThat(savedTransaction.getReference()).isEqualTo("TXN-123456");

        assertThat(savedTransaction.getAccount()).isSameAs(account);
    }
}
