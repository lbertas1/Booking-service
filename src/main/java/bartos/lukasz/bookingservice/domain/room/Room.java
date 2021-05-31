package bartos.lukasz.bookingservice.domain.room;

import bartos.lukasz.bookingservice.application.enums.Equipments;
import bartos.lukasz.bookingservice.domain.room.dto.RoomDto;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@SuperBuilder
@NoArgsConstructor
@Data
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Integer roomNumber;
    private Integer roomCapacity;
    @Column(columnDefinition = "VARCHAR(1000)")
    private String description;
    private BigDecimal priceForNight;
    private Boolean isBusy;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> equipment;

    public RoomDto toRoomDto() {
        return RoomDto.builder()
                .id(id)
                .priceForNight(priceForNight)
                .description(description)
                .roomCapacity(roomCapacity)
                .roomNumber(roomNumber)
                .isBusy(isBusy)
                .equipments(equipment
                        .stream()
                        .map(Equipments::valueOf)
                        .collect(Collectors.toSet()))
                .build();
    }
}
