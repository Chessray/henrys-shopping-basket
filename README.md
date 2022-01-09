# Henry's Grocery Shopping Basket

A quick demo for a small grocery store which offers two discounts within a certain timeframe.

## Build & Run

The project uses the Gradle wrapper. The only requirement is a JDK (minimum 17).

Build the project with `./gradlew build`.

Start the application with `./gradlew run`.

## Usage

The command line interaction guides you through building the shopping basket. Currently, you can only build one basket
per run and only add new entries to this one.

### Adding a shopping item to the basket

1. Enter `a` or `A` in order to trigger the process of adding.
2. Enter the index number of the good you wish to buy as displayed.
3. Enter the number of units you wish to buy.
4. Repeat until you have collected all the items you wish to buy. Your final price will already be shown as "Current
   total".
