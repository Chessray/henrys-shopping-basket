package uk.co.kleindelao.demo.henrys.basket;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class HenrysEnglishTest {
  private final HenrysEnglish english = new HenrysEnglish();

  @Test
  void shouldReturnLoaves() {
    // Given
    final var singular = "loaf";

    // When
    final var plural = english.getPlural(singular);

    // Then
    then(plural).isEqualTo("loaves");
  }
}