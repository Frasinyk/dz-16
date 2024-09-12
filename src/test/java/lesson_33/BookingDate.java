package lesson_33;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class BookingDate {
    private Date checkin;
    private Date checkout;
//
// LocalDate checkin = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//valueOf(startData);
}
