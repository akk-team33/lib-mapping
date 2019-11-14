package de.team33.test.mapping.v9;

import com.google.common.collect.ImmutableMap;
import de.team33.libs.mapping.v9.PropertyMapping;
import de.team33.test.mapping.shared.PlainType;

import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import java.util.Random;

public class PropertyMappingTest extends CodecTestBase<PlainType, Map<String, Object>> {

    private Random random = new Random();

    public PropertyMappingTest() {
        super(new PropertyMapping<>(PlainType::new)
                      .add("a", PlainType::getPrivateInt, PlainType::setPrivateInt)
                      .add("b", PlainType::getPrivateString, PlainType::setPrivateString)
                      .add("c", PlainType::getPrivateDate, PlainType::setPrivateDate)
                      .codec(), 100);
    }

    private String nextString() {
        return new BigInteger(128, random).toString(Character.MAX_RADIX);
    }

    private Date nextDate() {
        return new Date(random.nextLong());
    }

    @Override
    protected final PlainType anyOrigin() {
        return new PlainType()
                .setPrivateInt(random.nextInt())
                .setPrivateString(nextString())
                .setPrivateDate(nextDate());
    }

    @Override
    protected final Map<String, Object> anyResult() {
        return ImmutableMap.<String, Object>builder()
                .put("a", random.nextInt())
                .put("b", nextString())
                .put("c", nextDate())
                .build();
    }
}