package de.team33.test.mapping.v5;

import de.team33.libs.mapping.v5.BiMapper;
import de.team33.libs.mapping.v5.MapMapper;
import de.team33.test.mapping.shared.PlainType;

import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import java.util.Random;

public class MapMapperTest extends BiMapperTestBase<PlainType, Map<String, Object>> {

    private static final BiMapper<PlainType, Map<String, Object>> MAPPER = new MapMapper.Builder<PlainType>()
            .add(PlainType::getPrivateInt, PlainType::setPrivateInt)
            .add(PlainType::getPrivateString, PlainType::setPrivateString)
            .add(PlainType::getPrivateDate, PlainType::setPrivateDate)
            .build();

    private final Random random = new Random();

    @Override
    protected final BiMapper<PlainType, Map<String, Object>> mapper() {
        return MAPPER;
    }

    @Override
    protected final PlainType newLeft() {
        return new PlainType().setPrivateInt(random.nextInt())
                              .setPrivateString(new BigInteger(random.nextInt(64) + 1, random).toString(36))
                              .setPrivateDate(new Date(random.nextLong()));
    }

    @Override
    protected final Map<String, Object> newRight() {
        return MAPPER.toRight(newLeft());
    }
}
