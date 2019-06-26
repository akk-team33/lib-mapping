package de.team33.test.mapping.shared;

import java.util.Date;

import de.team33.libs.mapping.v2.PropertyMapper;


public class FlatType {

    public static final PropertyMapper<FlatType> MAPPER = PropertyMapper.simple(FlatType.class);

    private int intValue;
    private String stringValue;
    private Date dateValue;

    public FlatType(final int intValue, final String stringValue, final Date dateValue) {
        this.intValue = intValue;
        this.stringValue = stringValue;
        this.dateValue = dateValue;
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
}
