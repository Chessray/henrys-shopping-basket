package uk.co.kleindelao.demo.henrys.basket;

import static java.util.stream.Collectors.joining;

import java.util.HashMap;
import java.util.Map;

public class ShoppingBasket {
  private final Map<ShoppingItem, Integer> content;
  private final HenrysEnglish henrysEnglish;

  public ShoppingBasket() {
    this.content = new HashMap<>();
    henrysEnglish = new HenrysEnglish();
  }

  Map<ShoppingItem, Integer> getContent() {
    return content;
  }

  public void addItems(final int numberOfItems, final ShoppingItem item) {
    content.computeIfPresent(item, (itemKey, currentCount) -> currentCount + numberOfItems);
    content.putIfAbsent(item, numberOfItems);
  }

  public int getNumberOfItems(final ShoppingItem item) {
    return content.getOrDefault(item, 0);
  }

  @Override
  public String toString() {
    return "ShoppingBasket:\n" + content.entrySet()
                                        .stream()
                                        .map(this::toOutputItem)
                                        .collect(joining("\n"));
  }

  private String toOutputItem(final Map.Entry<ShoppingItem, Integer> shoppingEntry) {
    return shoppingEntry.getValue() + " " +
        henrysEnglish.getPlural(shoppingEntry.getKey()
                                             .unit(), shoppingEntry.getValue()) + " of " +
        shoppingEntry.getKey()
                     .name();
  }
}
