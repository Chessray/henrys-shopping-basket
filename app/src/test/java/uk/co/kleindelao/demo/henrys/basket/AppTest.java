/*
 * This Java source file was generated by the Gradle 'init' task.
 */

package uk.co.kleindelao.demo.henrys.basket;

import static org.assertj.core.api.BDDAssertions.then;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class AppTest {
  private final App app = new App();

  @Test
  void shouldHaveACatalogue() {
    then(app.getCatalogue()).isNotNull();
  }

  @Test
  void shouldHaveNonEmptyCatalogue() {
    then(app.getCatalogue()).isNotEmpty();
  }

  @Test
  void shouldContainSoupBreadMilkApples() {
    then(app.getCatalogue()).anySatisfy(item -> itemMatches(item, "soup", "tin", "0.65"))
                            .anySatisfy(item -> itemMatches(item, "bread", "loaf", "0.8"))
                            .anySatisfy(item -> itemMatches(item, "milk", "bottle", "1.3"))
                            .anySatisfy(item -> itemMatches(item, "apples", "single", "0.1"));
  }

  private void itemMatches(ShoppingItem item, final String expectedName, final String expectedUnit,
                           final String expectedCostValue) {
    then(item.name()).isEqualTo(expectedName);
    then(item.unit()).isEqualTo(expectedUnit);
    then(item.cost()).isEqualTo(new BigDecimal(expectedCostValue));
  }
}
