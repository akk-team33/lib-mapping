package de.team33.test.mapping.shared;

import de.team33.libs.mapping.v3.Mapper;
import de.team33.libs.mapping.v3.PlainMapper;

import java.util.Date;

public class PlainType {

    private int privateInt;
    private String privateString;
    private Date privateDate;

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
