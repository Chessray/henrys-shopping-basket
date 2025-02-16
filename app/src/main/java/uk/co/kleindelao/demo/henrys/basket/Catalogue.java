package uk.co.kleindelao.demo.henrys.basket;

import static java.util.stream.Collectors.joining;

import java.util.Iterator;
import java.util.List;

public record Catalogue(List<CatalogueItem> availableItems) implements Iterable<CatalogueItem> {
  @Override
  public Iterator<CatalogueItem> iterator() {
    return availableItems().iterator();
  }

  public CatalogueItem getItem(final int index) {
    return availableItems().get(index);
  }

  @Override
  public String toString() {
    return availableItems().stream()
                           .map(shoppingItem -> availableItems().indexOf(shoppingItem) + ": " +
                               shoppingItem.name())
                           .collect(joining("\n"));
  }
}
