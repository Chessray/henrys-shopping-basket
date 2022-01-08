package uk.co.kleindelao.demo.henrys.basket;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;

import com.google.common.annotations.VisibleForTesting;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ShoppingBasket {
  private final Map<ShoppingItem, Integer> content;

  public ShoppingBasket() {
    this.content = new HashMap<>();
  }

  @VisibleForTesting
  Map<ShoppingItem, Integer> getContent() {
    return content;
  }

  public void addItems(final int numberOfItems, final ShoppingItem item) {
    content.computeIfPresent(item, (itemKey, currentCount) -> currentCount + numberOfItems);
    content.putIfAbsent(item, numberOfItems);
  }

  public BigDecimal getTotalPrice() {
    return content.entrySet()
                  .stream()
                  .map(entry -> entry.getKey()
                                     .cost()
                                     .multiply(BigDecimal.valueOf(entry.getValue())))
                  .reduce(BigDecimal::add)
                  .orElse(ZERO)
                  .setScale(2, HALF_UP);
  }
}
