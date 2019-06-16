package de.team33.test.mapping.shared;

import de.team33.libs.mapping.v1.FieldMapper;
import de.team33.libs.reflect.v4.Fields;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class SubType extends SuperType {

    public static final FieldMapper<SubType> MAPPER = FieldMapper
            .stage(Fields.Mapping.SIGNIFICANT_DEEP)
            .apply(SubType.class);

    public final int intValue;
    public final String stringValue;
    public final Date dateValue;

    public SubType(final int intValue, final String stringValue, final Date dateValue) {
        super(
                intValue + 278,
                stringValue + " (super)",
                new Date(null == dateValue ? 0L : dateValue.getTime() - 1000000));
        this.intValue = intValue;
        this.stringValue = stringValue;
        this.dateValue = dateValue;
    }

    private static List<Object> toList(final SubType subject) {
        return Arrays.asList(subject.intValue, subject.stringValue, subject.dateValue, SuperType.toList(subject));
    }

    @Override
    public int hashCode() {
        return toList(this).hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof SubType) && toList(this).equals(toList((SubType) obj)));
    }

    @Override
    public String toString() {
        return toList(this).toString();
    }
}
