package uk.co.kleindelao.demo.henrys.basket;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;
import static java.util.Map.entry;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.BDDAssertions.then;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ShoppingProcessTest {
  @Nested
  class SoupBreadDiscount {
    @Nested
    class WithinDateRange {
      static Stream<Arguments> shouldApplyDiscountOnBread() {
        return Stream.of(Arguments.of(2, 1, "1.70"), Arguments.of(0, 1, "0.80"),
            Arguments.of(5, 3, "4.85"));
      }

      @ParameterizedTest
      @MethodSource
      void shouldApplyDiscountOnBread(final int numberOfSoups, final int numberOfBreads,
                                      final String expectedTotalValue) {
        // Given
        final var shoppingProcess = new ShoppingProcess(LocalDate.now());
        shoppingProcess.addItems(0, numberOfSoups);
        shoppingProcess.addItems(1, numberOfBreads);

        // When
        final var actualTotal = shoppingProcess.getTotalPrice();

        // Then
        then(actualTotal).isEqualTo(new BigDecimal(expectedTotalValue));
      }
    }

    @Nested
    class OutsideDateRange {
      @Test
      void shouldNotApplyBeforeDateRange() {
        // Given
        final var shoppingProcess = new ShoppingProcess(LocalDate.now()
                                                                 .minusDays(2));
        shoppingProcess.addItems(0, 2);
        shoppingProcess.addItems(1, 1);

        // When
        final var actualTotal = shoppingProcess.getTotalPrice();

        // Then
        then(actualTotal).isEqualTo(new BigDecimal("2.10"));
      }

      @Test
      void shouldNotApplyAfterDateRange() {
        // Given
        final var shoppingProcess = new ShoppingProcess(LocalDate.now()
                                                                 .plusDays(7));
        shoppingProcess.addItems(0, 2);
        shoppingProcess.addItems(1, 1);

        // When
        final var actualTotal = shoppingProcess.getTotalPrice();

        // Then
        then(actualTotal).isEqualTo(new BigDecimal("2.10"));
      }
    }
  }

  @Nested
  class AppleDiscount {
    @Nested
    class WithinDateRange {
      @Test
      void shouldApplyWhenInAFewDays() {
        // Given
        final var shoppingProcess = new ShoppingProcess(LocalDate.now()
                                                                 .plusDays(5));
        shoppingProcess.addItems(3, 5);

        // When
        final var actualTotal = shoppingProcess.getTotalPrice();

        // Then
        then(actualTotal).isEqualTo(new BigDecimal("0.45"));
      }

      @Test
      void shouldApplyWhenInRangeDuringShortMonth() {
        // Given
        final var fixedClockBeforeJanuaryEnd =
            Clock.fixed(LocalDate.of(2022, 1, 30)
                                 .atStartOfDay(ZoneId.systemDefault())
                                 .toInstant(), ZoneId.systemDefault());
        final var shoppingProcess = new ShoppingProcess(fixedClockBeforeJanuaryEnd,
            LocalDate.of(2022, 2, 4));
        shoppingProcess.addItems(3, 5);

        // When
        final var actualTotal = shoppingProcess.getTotalPrice();

        // Then
        then(actualTotal).isEqualTo(new BigDecimal("0.45"));
      }

      @Test
      void shouldApplyOnLastDayOfMonthFollowingStartDate() {
        // Given
        final var fixedClockBeforeMonthEnd =
            Clock.fixed(LocalDate.of(2022, 7, 30)
                                 .atStartOfDay(ZoneId.systemDefault())
                                 .toInstant(), ZoneId.systemDefault());
        final var shoppingProcess = new ShoppingProcess(fixedClockBeforeMonthEnd,
            LocalDate.of(2022, 9, 30));
        shoppingProcess.addItems(3, 5);

        // When
        final var actualTotal = shoppingProcess.getTotalPrice();

        // Then
        then(actualTotal).isEqualTo(new BigDecimal("0.45"));
      }
    }

    @Nested
    class OutsideDateRange {
      @Test
      void shouldNotApplyWhenBeforeDateRange() {
        // Given
        final ShoppingProcess shoppingProcess = new ShoppingProcess(LocalDate.now());
        shoppingProcess.addItems(3, 5);

        // When
        final var actualTotal = shoppingProcess.getTotalPrice();

        // Then
        then(actualTotal).isEqualTo(new BigDecimal("0.50"));
      }

      @Test
      void shouldNotApplyAfterDateRange() {
        // Given
        final ShoppingProcess shoppingProcess = new ShoppingProcess(LocalDate.now()
                                                                             .plusMonths(3));
        shoppingProcess.addItems(3, 5);

        // When
        final var actualTotal = shoppingProcess.getTotalPrice();

        // Then
        then(actualTotal).isEqualTo(new BigDecimal("0.50"));
      }
    }
  }

  @Nested
  class DateIndependentTest {
    private final ShoppingProcess shoppingProcess = new ShoppingProcess(LocalDate.now());

    @Nested
    class CatalogueTests {
      @Test
      void shouldHaveACatalogue() {
        then(shoppingProcess.getCatalogue()).isNotNull();
      }

      @Test
      void shouldHaveNonEmptyCatalogue() {
        then(shoppingProcess.getCatalogue()).isNotEmpty();
      }

      @Test
      void shouldContainSoupBreadMilkApples() {
        then(
            shoppingProcess.getCatalogue()).anySatisfy(
                                               item -> itemMatches(item, "soup", "tin", "0.65"))
                                           .anySatisfy(
                                               item -> itemMatches(item, "bread", "loaf", "0.8"))
                                           .anySatisfy(
                                               item -> itemMatches(item, "milk", "bottle", "1.3"))
                                           .anySatisfy(
                                               item -> itemMatches(item, "apples", "single",
                                                   "0.1"));
      }

      private void itemMatches(final CatalogueItem item, final String expectedName,
                               final String expectedUnit,
                               final String expectedCostValue) {
        then(item.name()).isEqualTo(expectedName);
        then(item.unit()).isEqualTo(expectedUnit);
        then(item.cost()).isEqualTo(new BigDecimal(expectedCostValue));
      }
    }

    @Nested
    class BasketTests {
      @Test
      void shouldAddItemsByIndex() {
        // Given
        final var numberOfItems = nextInt(1, 10);

        // When
        shoppingProcess.addItems(1, numberOfItems);

        // Then
        then(shoppingProcess.getBasket()
                            .getContent()).containsOnly(
            entry(new CatalogueItem("bread", "loaf", new BigDecimal("0.8")), numberOfItems));
      }
    }

    @Nested
    class Total {
      @Test
      void shouldAddUpToZeroForEmptyBasket() {
        // When
        final var total = shoppingProcess.getTotalPrice();

        // Then
        then(total).isEqualTo(ZERO.setScale(2, HALF_UP));
      }

      @Test
      void shouldAddIndividualPricesForUndiscountedItems() {
        // Given
        final var numberOfBreads = nextInt(1, 10);
        final var numberOfMilks = nextInt(1, 10);
        shoppingProcess.addItems(1, numberOfBreads);
        shoppingProcess.addItems(2, numberOfMilks);

        // When
        final var total = shoppingProcess.getTotalPrice();

        // Then
        then(total).hasScaleOf(2)
                   .isEqualTo(new BigDecimal("0.8").multiply(BigDecimal.valueOf(numberOfBreads))
                                                   .add(new BigDecimal("1.3").multiply(
                                                       BigDecimal.valueOf(numberOfMilks)))
                                                   .setScale(2, HALF_UP));
      }
    }
  }
}
