package de.team33.test.mapping.shared;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PlainType {

    private int privateInt;
    private String privateString;
    private Date privateDate;

    @Override
    public final int hashCode() {
        return toList(this).hashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        return this == obj || (obj instanceof PlainType && toList(this).equals(toList((PlainType) obj)));
    }

    @Override
    public final String toString() {
        return toList(this).toString();
    }

    private static List<Object> toList(final PlainType subject) {
        return Arrays.asList(subject.privateInt, subject.privateString, subject.privateDate);
    }

    public final int getPrivateInt() {
        return privateInt;
    }

    public final PlainType setPrivateInt(final int privateInt) {
        this.privateInt = privateInt;
        return this;
    }

    public final String getPrivateString() {
        return privateString;
    }

    public final PlainType setPrivateString(final String privateString) {
        this.privateString = privateString;
        return this;
    }

    public final Date getPrivateDate() {
        return (Date) privateDate.clone();
    }

    public final PlainType setPrivateDate(final Date privateDate) {
        this.privateDate = (Date) privateDate.clone();
        return this;
    }
}
