package wura.project.mini_banking_app.mapper;

import wura.project.mini_banking_app.dto.RegisterDto;
import wura.project.mini_banking_app.dto.RegisterResponseDto;
import wura.project.mini_banking_app.entity.User;

public class UserMapper {
    public static User mapToUser(RegisterDto registerDto) {

        return User.builder()
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .email(registerDto.getEmail())
                .build();
    }

    public static RegisterResponseDto mapToRegisterResponse(User user) {

        return RegisterResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .accountNumber(
                        user.getAccount().getAccountNumber()
                )
                .balance(
                        user.getAccount().getBalance()
                )
                .build();
    }
}
