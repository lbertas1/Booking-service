package bartos.lukasz.bookingservice.infrastructure.controllers;

import bartos.lukasz.bookingservice.application.annotations.CoveredControllerAdvice;
import bartos.lukasz.bookingservice.application.dto.FiltersCriteria;
import bartos.lukasz.bookingservice.application.exception.RoomServiceException;
import bartos.lukasz.bookingservice.application.service.dataServices.RoomService;
import bartos.lukasz.bookingservice.domain.room.dto.RoomDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
@CoveredControllerAdvice
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/{id}")
    ResponseEntity<RoomDto> getRoomById(@PathVariable("id") Long id) throws RoomServiceException {
        return ResponseEntity.ok(roomService.get(id));
    }

    @GetMapping("/filtered-rooms")
    ResponseEntity<List<RoomDto>> getRoomsByFilters(
            @RequestHeader MultiValueMap<String, String> headers,
            @RequestParam(required = false) String arrivalDate,
            @RequestParam(required = false) String departureDate,
            @RequestParam(required = false) String roomCapacity,
            @RequestParam(required = false) String selectedEquipments,
            @RequestParam(required = false) String priceRange) {

        return ResponseEntity
                .ok(roomService
                        .getRoomsByFiltersCriteria(FiltersCriteria
                                .builder()
                                .arrivalDate(!arrivalDate.equals("null") ? arrivalDate : null)
                                .departureDate(!departureDate.endsWith("null") ? departureDate : null)
                                .roomCapacity(!roomCapacity.equals("null") ? Integer.valueOf(roomCapacity) : null)
                                .selectedEquipments(!selectedEquipments.equals("") ? new ArrayList<>(Arrays.asList(selectedEquipments.split(","))) : null)
                                .priceRange(!priceRange.equals("null") ? priceRange : null)
                                .build()));
    }
}
