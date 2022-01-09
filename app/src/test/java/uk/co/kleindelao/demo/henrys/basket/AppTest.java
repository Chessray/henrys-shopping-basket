package uk.co.kleindelao.demo.henrys.basket;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;
import static com.github.stefanbirkner.systemlambda.SystemLambda.withTextFromSystemIn;
import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;
import static org.assertj.core.api.BDDAssertions.then;

import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AppTest {
  @Test
  void shouldPrintGreeting() throws Exception {
    // Given
    final var app = new App();

    // When
    final var output = tapSystemOut(app::printGreeting);

    // Then
    then(output).hasLineCount(2);
  }

  @Test
  void shouldPrintInstructions() throws Exception {
    // Given
    final var app = new App();

    // When
    final var output = tapSystemOut(app::printInstructions);

    // Then
    then(output).hasLineCount(3);
  }

  static class VarargsAggregator implements ArgumentsAggregator {
    @Override
    public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws
        ArgumentsAggregationException {
      return accessor.toList()
                     .stream()
                     .skip(context.getIndex())
                     .map(String::valueOf)
                     .toArray(String[]::new);
    }
  }

  static Stream<Arguments> shouldComputeExpectedTotal() {
    return Stream.of(Arguments.of("3.15", BASIC_ISO_DATE.format(LocalDate.now()), "a", "0",
            "3", "a", "1", "2", "q"),
        Arguments.of("1.90", BASIC_ISO_DATE.format(LocalDate.now()), "a", "3", "6", "a", "2", "1",
            "q"),
        Arguments.of("1.84", BASIC_ISO_DATE.format(LocalDate.now()
                                                            .plusDays(5)), "a", "3", "6", "a", "2",
            "1", "q"),
        Arguments.of("1.97", BASIC_ISO_DATE.format(LocalDate.now()
                                                            .plusDays(5)), "a", "3", "3", "a", "0",
            "2", "a", "1", "1", "q"));
  }

  @ParameterizedTest
  @MethodSource
  void shouldComputeExpectedTotal(final String expectedTotalValue,
                                  final @AggregateWith(VarargsAggregator.class)
                                      String... input) throws Exception {
    // When
    final var output =
        tapSystemOut(
            () ->
                withTextFromSystemIn(input)
                    .execute(() -> new App().launch()));

    // Then
    final var outputLines = output.lines()
                                  .toList();
    then(outputLines.get(outputLines.size() - 4)).endsWith(expectedTotalValue);
  }
}