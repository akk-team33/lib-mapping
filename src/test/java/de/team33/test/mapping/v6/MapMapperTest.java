package de.team33.test.mapping.v6;

import com.google.common.collect.ImmutableMap;
import de.team33.libs.mapping.v6.MapMapper;
import de.team33.test.mapping.shared.PlainType;

import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import java.util.Random;

public class MapMapperTest extends MapperTestBase<PlainType, Map<String, Object>> {

    private final Random random = new Random();

    public MapMapperTest() {
        super(new MapMapper.Builder<>(PlainType::new)
                .add("a", PlainType::getPrivateInt, PlainType::setPrivateInt)
                .add("b", PlainType::getPrivateString, PlainType::setPrivateString)
                .add("c", PlainType::getPrivateDate, PlainType::setPrivateDate)
                .build());
    }

    private String nextString() {
        return new BigInteger(128, random).toString();
    }

    private Date nextDate() {
        return new Date(random.nextLong());
    }

    @Override
    protected final PlainType newOrigin() {
        return new PlainType()
                .setPrivateInt(random.nextInt())
                .setPrivateString(nextString())
                .setPrivateDate(nextDate());
    }

    @Override
    protected final Map<String, Object> newResult() {
        return ImmutableMap.<String, Object>builder()
                .put("a", random.nextInt())
                .put("b", nextString())
                .put("c", nextDate())
                .build();
    }
}
