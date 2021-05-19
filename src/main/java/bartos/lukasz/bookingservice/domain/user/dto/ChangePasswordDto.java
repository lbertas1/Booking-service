package bartos.lukasz.bookingservice.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ChangePasswordDto {
    private Long userId;
    private String oldPassword;
    private String newPassword;
}
