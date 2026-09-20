package wura.project.mini_banking_app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import wura.project.mini_banking_app.dto.*;
import wura.project.mini_banking_app.service.AccountService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/deposit")
    public ResponseEntity<DepositResponseDto> deposit(@RequestBody DepositRequestDto request, Authentication authentication) {

        String email = authentication.getName();

        DepositResponseDto response = accountService.deposit(email, request.getAmount());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionHistory(Authentication authentication) {

        String email = authentication.getName();

        List<TransactionResponseDto> transactions = accountService.getTransactionHistory(email);

        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<WithdrawalResponseDto> withdraw (@RequestBody WithdrawalRequestDto request, Authentication authentication) {
        String email = authentication.getName();
        WithdrawalResponseDto response = accountService.withdraw(email, request.getAmount());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponseDto> transfer(@RequestBody TransferRequestDto request, Authentication authentication) {

        String senderEmail = authentication.getName();

        TransferResponseDto response = accountService.transfer(senderEmail, request);

        return ResponseEntity.ok(response);
    }
}
