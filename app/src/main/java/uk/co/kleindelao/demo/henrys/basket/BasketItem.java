package uk.co.kleindelao.demo.henrys.basket;

import java.math.BigDecimal;

public sealed interface BasketItem permits CatalogueItem, DiscountItem {
  String name();
  BigDecimal cost();
}
