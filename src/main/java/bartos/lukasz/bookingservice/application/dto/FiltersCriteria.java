package bartos.lukasz.bookingservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class FiltersCriteria {
    private String arrivalDate;
    private String departureDate;
    private Integer roomCapacity;
    private String priceRange;
    private List<String> selectedEquipments;
}
