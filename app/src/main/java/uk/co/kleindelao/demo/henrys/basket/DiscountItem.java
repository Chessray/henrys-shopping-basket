package uk.co.kleindelao.demo.henrys.basket;

import java.math.BigDecimal;

public record DiscountItem(String name, BigDecimal cost) implements BasketItem {
}
