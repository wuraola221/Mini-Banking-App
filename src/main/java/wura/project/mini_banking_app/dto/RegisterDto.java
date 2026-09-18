package wura.project.mini_banking_app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
//    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
