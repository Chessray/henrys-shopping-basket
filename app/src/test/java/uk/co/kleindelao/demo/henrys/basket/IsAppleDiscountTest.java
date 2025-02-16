package uk.co.kleindelao.demo.henrys.basket;

import static org.assertj.core.api.BDDAssertions.then;
import static uk.co.kleindelao.demo.henrys.basket.IsAppleDiscount.determineIsAppleDiscount;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class IsAppleDiscountTest {
  @Nested
  class WithinDateRange {
    @Test
    void shouldApplyWhenInAFewDays() {
      // Given
      final var shoppingDate = LocalDate.now()
                                        .plusDays(5);

      // When
      final var actualIsDiscount =
          determineIsAppleDiscount(Clock.systemDefaultZone(), shoppingDate);

      // Then
      then(actualIsDiscount).isTrue();
    }

    @Test
    void shouldApplyWhenInRangeDuringShortMonth() {
      // Given
      final var fixedClockBeforeJanuaryEnd =
          Clock.fixed(LocalDate.of(2022, 1, 30)
                               .atStartOfDay(ZoneId.systemDefault())
                               .toInstant(), ZoneId.systemDefault());
      final var shoppingDate = LocalDate.of(2022, 2, 4);

      // When
      final var actualIsDiscount =
          determineIsAppleDiscount(fixedClockBeforeJanuaryEnd, shoppingDate);

      // Then
      then(actualIsDiscount).isTrue();
    }

    @Test
    void shouldApplyOnLastDayOfMonthFollowingStartDate() {
      // Given
      final var fixedClockBeforeMonthEnd =
          Clock.fixed(LocalDate.of(2022, 7, 30)
                               .atStartOfDay(ZoneId.systemDefault())
                               .toInstant(), ZoneId.systemDefault());
      final var shoppingDate = LocalDate.of(2022, 9, 30);

      // When
      final var actualIsDiscount =
          determineIsAppleDiscount(fixedClockBeforeMonthEnd, shoppingDate);

      // Then
      then(actualIsDiscount).isTrue();
    }
  }

  @Nested
  class OutsideDateRange {
    @Test
    void shouldNotApplyWhenBeforeDateRange() {
      // Given
      final var shoppingDate = LocalDate.now();

      // When
      final var actualIsDiscount =
          determineIsAppleDiscount(Clock.systemDefaultZone(), shoppingDate);

      // Then
      then(actualIsDiscount).isFalse();
    }

    @Test
    void shouldNotApplyAfterDateRange() {
      // Given
      final var shoppingDate = LocalDate.now().plusMonths(3);

      // When
      final var actualIsDiscount =
          determineIsAppleDiscount(Clock.systemDefaultZone(), shoppingDate);

      // Then
      then(actualIsDiscount).isFalse();
    }
  }
}