package uk.co.kleindelao.demo.henrys.basket;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;
import static uk.co.kleindelao.demo.henrys.basket.ShoppingUnits.BOTTLE;
import static uk.co.kleindelao.demo.henrys.basket.ShoppingUnits.LOAF;
import static uk.co.kleindelao.demo.henrys.basket.ShoppingUnits.SINGLE;
import static uk.co.kleindelao.demo.henrys.basket.ShoppingUnits.TIN;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Range;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Map.Entry;

public class ShoppingProcess {
  private static final BigDecimal APPLE_DISCOUNT_MULTIPLICAND = new BigDecimal("0.9");
  private static final BigDecimal SOUP_BREAD_DISCOUNT_DIVISOR = BigDecimal.valueOf(2);

  private final Catalogue catalogue;
  private final ShoppingItem soup;
  private final ShoppingItem bread;
  private final ShoppingItem apples;
  private final ShoppingBasket basket;
  private final boolean isAppleDiscount;
  private final boolean isSoupBreadDiscount;

  public ShoppingProcess(final LocalDate shoppingDate) {
    this(Clock.systemDefaultZone(), shoppingDate);
  }

  @VisibleForTesting
  ShoppingProcess(final Clock clock,
                  final LocalDate shoppingDate) {
    this.isSoupBreadDiscount = Range.closed(LocalDate.now(clock)
                                                     .minusDays(1),
                                        LocalDate.now(clock)
                                                 .plusDays(6))
                                    .contains(shoppingDate);
    this.isAppleDiscount = determineIsAppleDiscount(clock, shoppingDate);
    this.basket = new ShoppingBasket();
    soup = shoppingItem("soup", TIN, "0.65");
    bread = shoppingItem("bread", LOAF, "0.8");
    apples = shoppingItem("apples", SINGLE, "0.1");
    catalogue = new Catalogue(
        List.of(soup, bread,
            shoppingItem("milk", BOTTLE, "1.3"), apples));
  }

  private boolean determineIsAppleDiscount(final Clock clock, final LocalDate shoppingDate) {
    final var appleDiscountStartDate = LocalDate.now(clock)
                                                .plusDays(3);
    final var appleDiscountEndDate = LocalDate.of(appleDiscountStartDate.getYear(),
                                                  appleDiscountStartDate.getMonthValue() + 2, 1)
                                              .minusDays(1);
    final Range<LocalDate> appleDiscountRange = Range.closed(appleDiscountStartDate,
        appleDiscountEndDate);
    return appleDiscountRange.contains(shoppingDate);
  }

  private static ShoppingItem shoppingItem(final String name, final String unit,
                                           final String costValue) {
    return new ShoppingItem(name, unit, new BigDecimal(costValue));
  }

  Catalogue getCatalogue() {
    return catalogue;
  }

  ShoppingBasket getBasket() {
    return basket;
  }

  public ShoppingItem getCatalogueItem(final int index) {
    return catalogue.getItem(index);
  }

  public void addItems(final int catalogueIndex, final int numberOfItems) {
    basket.addItems(numberOfItems, catalogue.getItem(catalogueIndex));
  }

  public BigDecimal getTotalPrice() {
    return basket.getContent()
                 .entrySet()
                 .stream()
                 .map(this::toItemPrice)
                 .reduce(BigDecimal::add)
                 .orElse(ZERO)
                 .setScale(2, HALF_UP);
  }

  private BigDecimal toItemPrice(final Entry<ShoppingItem, Integer> entry) {
    return isSoupBreadDiscount && bread.equals(entry.getKey()) ?
        calculateBreadSumWithSoupDiscount(entry) : calculateItemPrice(entry);
  }

  private BigDecimal calculateItemPrice(final Entry<ShoppingItem, Integer> entry) {
    return entry.getKey()
                .cost()
                .multiply(itemMultiplier(entry));
  }

  private BigDecimal calculateBreadSumWithSoupDiscount(final Entry<ShoppingItem, Integer> entry) {
    final var numberOfBreads = entry.getValue();
    final var numberOfDiscountedBreads = Math.min(basket.getNumberOfItems(soup) / 2,
        numberOfBreads);
    return entry.getKey()
                .cost()
                .multiply(BigDecimal.valueOf(numberOfDiscountedBreads))
                .divide(SOUP_BREAD_DISCOUNT_DIVISOR, HALF_UP)
                .add(entry.getKey()
                          .cost()
                          .multiply(
                              BigDecimal.valueOf(numberOfBreads - numberOfDiscountedBreads)));
  }

  private BigDecimal itemMultiplier(final Entry<ShoppingItem, Integer> entry) {
    var multiplier = BigDecimal.valueOf(entry.getValue());
    if (isAppleDiscount && apples.equals(entry.getKey())) {
      multiplier = multiplier.multiply(APPLE_DISCOUNT_MULTIPLICAND);
    }
    return multiplier;
  }

  @Override
  public String toString() {
    return "ShoppingProcess{" +
        "catalogue=" + catalogue +
        ", isAppleDiscount=" + isAppleDiscount +
        ", isSoupBreadDiscount=" + isSoupBreadDiscount +
        '}';
  }
}
