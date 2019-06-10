package de.team33.test.mapping.shared;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class SuperType {

    private final int intValue;
    private final String stringValue;
    private final Date dateValue;

    protected SuperType(final int intValue, final String stringValue, final Date dateValue) {
        this.intValue = intValue;
        this.stringValue = stringValue;
        this.dateValue = dateValue;
    }

    protected static List<Object> toList(final SuperType subject) {
        return Arrays.asList(subject.intValue, subject.stringValue, subject.dateValue);
    }
}
