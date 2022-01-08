package uk.co.kleindelao.demo.henrys.basket;

import com.google.common.annotations.VisibleForTesting;
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
}
