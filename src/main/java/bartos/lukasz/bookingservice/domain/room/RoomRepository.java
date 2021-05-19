package bartos.lukasz.bookingservice.domain.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Set<Room> findAllByIsBusyEquals(Boolean isBusy);

    Set<Room> findAllByRoomCapacity(int capacity);

    Set<Room> findAllByIdIsNotIn(List<Long> ids);

    Set<Room> findAllByIdIsNotInAndRoomCapacityEquals(List<Long> ids, Integer capacity);

    Set<Room> findAllByPriceForNightIsGreaterThanAndPriceForNightIsLessThan(BigDecimal price1, BigDecimal price2);
}
