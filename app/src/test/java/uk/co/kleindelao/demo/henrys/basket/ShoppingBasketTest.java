package uk.co.kleindelao.demo.henrys.basket;

import static java.math.BigDecimal.ONE;
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
}