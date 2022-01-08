package uk.co.kleindelao.demo.henrys.basket;

import java.util.Iterator;
import java.util.Set;

public record Catalogue(Set<ShoppingItem> availableItems) implements Iterable<ShoppingItem> {
    @Override
    public Iterator<ShoppingItem> iterator() {
        return availableItems().iterator();
    }
}
