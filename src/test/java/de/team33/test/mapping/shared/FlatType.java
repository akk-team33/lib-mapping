package de.team33.test.mapping.shared;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import de.team33.libs.mapping.v2.PropertyMapper;


public class FlatType {

    public static final PropertyMapper<FlatType> MAPPER = PropertyMapper.simple(FlatType.class);

    private boolean boolValue;
    private int intValue;
    private String stringValue;
    private Date dateValue;

    public FlatType(final boolean boolValue, final int intValue, final String stringValue, final Date dateValue) {
        this.boolValue = boolValue;
        this.intValue = intValue;
        this.stringValue = stringValue;
        this.dateValue = dateValue;
    }

    private static List<Object> toList(final FlatType subject) {
        return Arrays.asList(subject.intValue, subject.stringValue, subject.dateValue);
    }

    public boolean isBoolValue() {
        return boolValue;
    }

    public FlatType setBoolValue(final boolean boolValue) {
        this.boolValue = boolValue;
        return this;
    }

    public int getIntValue() {
        return intValue;
    }

    public FlatType setIntValue(int intValue) {
        this.intValue = intValue;
        return this;
    }

    public String getStringValue() {
        return stringValue;
    }

    public FlatType setStringValue(String stringValue) {
        this.stringValue = stringValue;
        return this;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public FlatType setDateValue(Date dateValue) {
        this.dateValue = dateValue;
        return this;
    }

    @Override
    public int hashCode() {
        return toList(this).hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return (this == obj) || ((obj instanceof FlatType) && toList(this).equals(toList((FlatType) obj)));
    }

    @Override
    public String toString() {
        return toList(this).toString();
    }
}
