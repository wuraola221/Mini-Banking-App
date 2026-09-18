package wura.project.mini_banking_app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import wura.project.mini_banking_app.dto.DepositRequestDto;
import wura.project.mini_banking_app.dto.DepositResponseDto;
import wura.project.mini_banking_app.dto.TransactionResponseDto;
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
}
