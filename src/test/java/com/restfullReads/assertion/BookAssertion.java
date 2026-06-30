package com.restfullReads.assertion;

import org.testng.Assert;

import java.util.List;
import java.util.stream.Collectors;

public class BookAssertion {

    public static void assertValueGreaterThanOrEqualsTo(List<Double> values, double compareWith) {
        validateInput(values);

        boolean allMatched = values.stream().allMatch(item -> item >= compareWith);

        List<Double> failedValues = values.stream()
                .filter(item -> item < compareWith)
                .collect(Collectors.toList());

        Assert.assertTrue(allMatched,
                String.format("Expected all values >= %s, but found: %s", compareWith, failedValues));
    }

    public static void assertValueLessThanOrEqualsTo(List<Double> values, double compareTo) {
        validateInput(values);

        boolean allMatched = values.stream().allMatch(val -> val <= compareTo);

        List<Double> failedValues = values.stream()
                .filter(item -> item > compareTo)
                .collect(Collectors.toList());

        Assert.assertTrue(allMatched,
                String.format("Expected all values <= %s, but found: %s", compareTo, failedValues));
    }

    public static void assertValuesLessThan(List<Double> values, double compareTo) {
        validateInput(values);

        boolean allMatched = values.stream().allMatch(val -> val < compareTo);

        List<Double> failedValues = values.stream()
                .filter(item -> item >= compareTo)
                .collect(Collectors.toList());

        Assert.assertTrue(allMatched,
                String.format("Expected all values < %s, but found: %s", compareTo, failedValues));
    }

    public static void assertValuesGreaterThan(List<Double> values, double compareWith) {
        validateInput(values);

        boolean allMatched = values.stream().allMatch(val -> val > compareWith);

        List<Double> failedValues = values.stream()
                .filter(item -> item <= compareWith)
                .collect(Collectors.toList());

        Assert.assertTrue(allMatched,
                String.format("Expected all values > %s, but found: %s", compareWith, failedValues));
    }


    private static void validateInput(List<Double> values) {
        Assert.assertNotNull(values, "Values list must not be null");
        Assert.assertFalse(values.isEmpty(), "Values list must not be empty");
    }
}