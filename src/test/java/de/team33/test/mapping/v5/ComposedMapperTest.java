package de.team33.test.mapping.v5;

import com.google.common.collect.ImmutableMap;
import de.team33.libs.mapping.v5.BiMapper;
import de.team33.libs.mapping.v5.ComposedMapper;
import de.team33.test.mapping.shared.PlainType;

import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.*;

public class ComposedMapperTest extends BiMapperTestBase<PlainType, Map<String, Object>> {

    private final Random random = new Random();

    @Override
    protected final BiMapper<PlainType, Map<String, Object>> mapper() {
        return new ComposedMapper<>(this::toRight, this::toLeft);
    }

    private PlainType toLeft(final Map<String, Object> right) {
        return new PlainType()
                .setPrivateInt((Integer) right.get("int"))
                .setPrivateString((String) right.get("string"))
                .setPrivateDate((Date) right.get("date"));
    }

    private Map<String, Object> toRight(final PlainType left) {
        return ImmutableMap.<String, Object>builder()
                .put("int", left.getPrivateInt())
                .put("string", left.getPrivateString())
                .put("date", left.getPrivateDate())
                .build();
    }

    @Override
    protected final PlainType newLeft() {
        return new PlainType().setPrivateInt(random.nextInt())
                              .setPrivateString(new BigInteger(random.nextInt(64) + 1, random).toString(36))
                              .setPrivateDate(new Date(random.nextLong()));
    }

    @Override
    protected final Map<String, Object> newRight() {
        return ImmutableMap.<String, Object>builder()
                .put("int", random.nextInt())
                .put("string", new BigInteger(random.nextInt(64) + 1, random).toString(36))
                .put("date", new Date(random.nextLong()))
                .build();
    }
}
