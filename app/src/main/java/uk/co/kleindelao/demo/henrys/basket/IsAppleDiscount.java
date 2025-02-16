package uk.co.kleindelao.demo.henrys.basket;

import com.google.common.collect.Range;
import java.time.Clock;
import java.time.LocalDate;

public class IsAppleDiscount {
  static boolean determineIsAppleDiscount(final Clock clock, final LocalDate shoppingDate) {
    final var appleDiscountStartDate = LocalDate.now(clock)
                                                .plusDays(3);
    final var appleDiscountEndDate = LocalDate.of(appleDiscountStartDate.getYear(),
                                                  appleDiscountStartDate.getMonthValue() + 2, 1)
                                              .minusDays(1);
    final Range<LocalDate> appleDiscountRange = Range.closed(appleDiscountStartDate,
        appleDiscountEndDate);
    return appleDiscountRange.contains(shoppingDate);
  }
}
