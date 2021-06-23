package bartos.lukasz.bookingservice.application.dto.events;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class EmailData {
    private String recipient;
}
