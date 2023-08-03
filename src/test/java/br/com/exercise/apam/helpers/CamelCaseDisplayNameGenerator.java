package br.com.exercise.apam.helpers;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;

/**
 * Camelcase display name generator for JUnit5.
 *
 * Add this annotation to your JUnit5 test classes.
 *
 * Eg:
 *
 * <pre>
 *{@literal @}CamelCaseDisplayNameGenerator
 * class SumCalculatorTest {
 *
 *  {@literal @}Test
 *   void givenCorrectOperandsShouldSumThemCorrectly() {
 *     ...
 *   }
 *
 *  {@literal @}Test
 *   void shouldFailToSumWhenOperandsAreMissing() {
 *     ...
 *   }
 * }
 * </pre>
 *
 * And you could expect to get log messages like that:
 * <pre>
 * Sum Calculator Test
 *
 *   ✔ Given Correct Operands Should Sum Them Correctly
 *   ✔ Should Fail To Sum When Operands Are Missing
 *   ...
 * </pre>
 */
@Target(value=TYPE)
@Retention(value=RUNTIME)
@Documented
@Inherited
@DisplayNameGeneration(CamelCaseGenerator.class)
public @interface CamelCaseDisplayNameGenerator {}

/**
 * Replaces camel case classes and method names by capitalized statements to
 * better describe the intents of a Test class.
 */
class CamelCaseGenerator extends DisplayNameGenerator.Standard {

    private static final String CAMELCASE_REGEX = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])";
    private static final Pattern CAMELCASE_PATTERN = Pattern.compile(CAMELCASE_REGEX);
    private static final String NUMBERS_REGEX = "([0-9]+)";
    private static final String FIRST_CAPTURE_REPLACEMENT = " $1";

    @Override
    public String generateDisplayNameForClass(final Class<?> testClass) {
        return replaceCamelCase(super.generateDisplayNameForClass(testClass));
    }

    @Override
    public String generateDisplayNameForNestedClass(final Class<?> nestedClass) {
        return replaceCamelCase(super.generateDisplayNameForNestedClass(nestedClass));
    }

    @Override
    public String generateDisplayNameForMethod(final Class<?> testClass, final Method testMethod) {
        return replaceCamelCase(testMethod.getName());
    }

    String replaceCamelCase(final String name) {
        String output = name.length() > 1
                ? name.substring(0, 1).toUpperCase() + name.substring(1)
                : name.toUpperCase();

        return Stream.of(CAMELCASE_PATTERN.split(output))
                     .map(str -> str.replaceAll("_", " "))
                     .map(str -> str.replaceAll(NUMBERS_REGEX, FIRST_CAPTURE_REPLACEMENT))
                     .collect(Collectors.joining(" "));
    }
}
