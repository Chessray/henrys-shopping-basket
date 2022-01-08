package uk.co.kleindelao.demo.henrys.basket;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.util.Map.entry;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.BDDAssertions.then;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;

class ShoppingBasketTest {
  private final ShoppingBasket shoppingBasket = new ShoppingBasket();

  @Test
  void shouldBeInitiallyEmpty() {
    then(shoppingBasket.getContent()).isEmpty();
  }

  @Test
  void shouldAddEntryForNewItem() {
    // Given
    final var item = new ShoppingItem(RandomString.make(), RandomString.make(), ONE);
    final var numberOfItems = nextInt(1, 10);

    // When
    shoppingBasket.addItems(numberOfItems, item);

    // Then
    then(shoppingBasket.getContent()).containsOnly(entry(item, numberOfItems));
  }

  @Test
  void shouldSumUpTwoEntriesForSameItem() {
    // Given
    final var item = new ShoppingItem(RandomString.make(), RandomString.make(), ONE);
    final var initialNumberOfItems = nextInt(1, 10);
    final var addedNumberOfItems = nextInt(1, 10);
    shoppingBasket.addItems(initialNumberOfItems, item);

    // When
    shoppingBasket.addItems(addedNumberOfItems, item);

    // Then
    then(shoppingBasket.getContent()).containsOnly(
        entry(item, initialNumberOfItems + addedNumberOfItems));
  }

  @Test
  void shouldStoreDifferentItems() {
    // Given
    final var item1 = new ShoppingItem(RandomString.make(), RandomString.make(), ONE);
    final var numberOfItems1 = nextInt(1, 10);
    final var item2 = new ShoppingItem(RandomString.make(), RandomString.make(), TEN);
    final var numberOfItems2 = nextInt(1, 10);
    shoppingBasket.addItems(numberOfItems1, item1);

    // When
    shoppingBasket.addItems(numberOfItems2, item2);

    // Then
    then(shoppingBasket.getContent()).containsOnly(entry(item1, numberOfItems1),
        entry(item2, numberOfItems2));
  }
}