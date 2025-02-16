package uk.co.kleindelao.demo.henrys.basket;

import java.math.BigDecimal;

public record CatalogueItem(String name, String unit, BigDecimal cost) implements BasketItem {
}
