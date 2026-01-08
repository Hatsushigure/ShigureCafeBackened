package cafe.shigure.UserService.dto;

import cafe.shigure.UserService.model.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationDetailsResponse {
    private String username;
    private String nickname;
    private String email;
    private UserStatus status;
    private String auditCode;
    private boolean isExpired;
}
