package uz.tatu.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * A DTO representing a password change required data - current and new password.
 */

@Data
@EqualsAndHashCode
@ToString
public class PasswordChangeDTO {

    private String currentPassword;
    private String newPassword;


}
