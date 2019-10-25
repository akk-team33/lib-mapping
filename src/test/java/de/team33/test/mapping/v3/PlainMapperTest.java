package de.team33.test.mapping.v3;

import com.google.common.collect.ImmutableMap;
import de.team33.libs.mapping.v3.Mapper;
import de.team33.libs.mapping.v3.PlainMapper;
import de.team33.test.mapping.shared.PlainType;

import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import java.util.Random;

public class PlainMapperTest extends MapperTestBase<PlainType> {

    private static final Mapper<PlainType> MAPPER = PlainMapper.<PlainType>builder()
            .add("privateInt", PlainType::getPrivateInt, PlainType::setPrivateInt)
            .add("privateString", PlainType::getPrivateString, PlainType::setPrivateString)
            .add("privateDate", PlainType::getPrivateDate, PlainType::setPrivateDate)
            .build();

    private final Random random = new Random();

    @Override
    protected final Mapper<PlainType> mapper() {
        return MAPPER;
    }

    @Override
    protected final PlainType newSubject() {
        return new PlainType().setPrivateInt(random.nextInt())
                              .setPrivateString(new BigInteger(random.nextInt(64) + 1, random).toString(36))
                              .setPrivateDate(new Date(random.nextLong()));
    }

    @Override
    protected final Map<String, Object> newMap() {
        return ImmutableMap.<String, Object>builder()
                .put("privateInt", random.nextInt())
                .put("privateString", new BigInteger(random.nextInt(64) + 1, random).toString(36))
                .put("privateDate", new Date(random.nextLong()))
                .build();
    }
}
