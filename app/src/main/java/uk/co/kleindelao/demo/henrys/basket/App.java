package uk.co.kleindelao.demo.henrys.basket;

import static java.lang.Character.toUpperCase;
import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;

import com.google.common.annotations.VisibleForTesting;
import java.time.LocalDate;
import java.util.Scanner;

public class App {
  private final Scanner inputScanner;
  private final HenrysEnglish henrysEnglish;

  public App() {
    inputScanner = new Scanner(System.in);
    henrysEnglish = new HenrysEnglish();
  }

  void launch() {
    printGreeting();
    final var process =
        new ShoppingProcess(LocalDate.parse(inputScanner.nextLine(), BASIC_ISO_DATE));

    var shouldContinue = true;
    while (shouldContinue) {
      printInstructions();
      final var instructionChar = toUpperCase(inputScanner.next().charAt(0));
      switch (instructionChar) {
        case 'A' -> {
          System.out.println("This is the catalogue:");
          System.out.println(process.getCatalogue());
          System.out.println("Please add the item index number");
          final var catalogueIndex = inputScanner.nextInt();
          final var catalogueItem = process.getCatalogueItem(catalogueIndex);
          System.out.println("How many " + plural(catalogueItem) + " of " + catalogueItem.name() +
              " would you like?");
          final var numberOfItems = inputScanner.nextInt();
          process.addItems(catalogueIndex, numberOfItems);
          System.out.println(process.getBasket());
          System.out.println("Current total: " + process.getTotalPrice());
        }
        case 'Q' -> shouldContinue = false;
      }
    }
  }

  @VisibleForTesting
  void printGreeting() {
    System.out.println("Welcome to Henry's Grocery Shopping Basket.");
    System.out.println("Would you please enter the shopping date (format yyyyMMdd)?");
  }

  private String plural(final CatalogueItem catalogueItem) {
    // https://github.com/atteo/evo-inflector/issues/25
    return henrysEnglish.getPlural(catalogueItem.unit());
  }

  @VisibleForTesting
  void printInstructions() {
    System.out.println("Please enter indicated character and hit <Enter>:");
    System.out.println("(A)dd an item to basket");
    System.out.println("(Q)uit");
  }

  public static void main(String[] args) {
    new App().launch();
  }
}
