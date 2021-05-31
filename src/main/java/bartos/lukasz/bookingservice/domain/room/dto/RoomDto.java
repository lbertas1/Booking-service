package bartos.lukasz.bookingservice.domain.room.dto;

import bartos.lukasz.bookingservice.application.enums.Equipments;
import bartos.lukasz.bookingservice.domain.room.Room;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@ApiModel(
        value = "Room",
        description = "Object representing a hotel room."
)
public class RoomDto {

    private Long id;
    private Integer roomNumber;
    private Integer roomCapacity;
    @ToString.Exclude
    private String description;
    private BigDecimal priceForNight;
    private Boolean isBusy;
    private Set<Equipments> equipments;

    public Room toRoom() {
        return Room.builder()
                .id(id)
                .priceForNight(priceForNight)
                .description(description)
                .roomCapacity(roomCapacity)
                .roomNumber(roomNumber)
                .isBusy(isBusy)
                .equipment(equipments
                        .stream()
                        .map(Equipments::name)
                        .collect(Collectors.toSet()))
                .build();
    }
}
