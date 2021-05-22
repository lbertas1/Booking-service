package bartos.lukasz.bookingservice.infrastructure.controllers;

import bartos.lukasz.bookingservice.application.annotations.CoveredControllerAdvice;
import bartos.lukasz.bookingservice.application.service.dataServices.OpinionService;
import bartos.lukasz.bookingservice.domain.reservation.opinion.dto.OpinionRequestDto;
import bartos.lukasz.bookingservice.domain.reservation.opinion.dto.OpinionResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/opinions")
@RequiredArgsConstructor
@CoveredControllerAdvice
public class OpinionController {

    private final OpinionService opinionService;

    @PostMapping
    ResponseEntity<OpinionResponseDto> addOpinions(@RequestBody OpinionRequestDto opinionRequestDto) {
        return ResponseEntity.ok(opinionService.saveOpinion(opinionRequestDto));
    }

//    @PutMapping
//    ResponseEntity<OpinionResponseDto> updateOpinion(@RequestBody OpinionRequestDto opinionRequestDto) {
//        return ResponseEntity.ok(opinionService.updateOpinion(opinionRequestDto));
//    }

    @DeleteMapping("/{reservationId}")
    ResponseEntity<OpinionResponseDto> removeOpinion(@PathVariable Long reservationId) {
        return ResponseEntity.ok(opinionService.removeOpinion(reservationId));
    }
}
