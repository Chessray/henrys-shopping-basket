package uk.co.kleindelao.demo.henrys.basket;

import static uk.co.kleindelao.demo.henrys.basket.ShoppingUnits.LOAF;

import org.atteo.evo.inflector.English;

public class HenrysEnglish extends English {
  @Override
  public String getPlural(final String word) {
    // https://github.com/atteo/evo-inflector/issues/25
    return LOAF.equals(word) ? "loaves" : super.getPlural(word);
  }
}
