package uk.co.kleindelao.demo.henrys.basket;

import java.util.Iterator;
import java.util.List;

public record Catalogue(List<ShoppingItem> availableItems) implements Iterable<ShoppingItem> {
    @Override
    public Iterator<ShoppingItem> iterator() {
        return availableItems().iterator();
    }

    public ShoppingItem getItem(final int index) {
        return availableItems().get(index);
    }
}
