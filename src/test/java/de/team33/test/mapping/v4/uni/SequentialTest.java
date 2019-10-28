package de.team33.test.mapping.v4.uni;

import com.google.common.collect.ImmutableMap;
import de.team33.libs.mapping.v4.Mapper;
import de.team33.libs.mapping.v4.uni.Sequential;
import de.team33.test.mapping.shared.PlainType;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class SequentialTest {

    private static final Function<PlainType, Map<String, Object>> MAPPER =
            new Sequential.Builder<PlainType, Map<String, Object>>(TreeMap::new)
                    .add(PlainType::getPrivateInt, (map, value) -> map.put("int", value))
                    .add(PlainType::getPrivateString, (map, value) -> map.put("string", value))
                    .add(PlainType::getPrivateDate, (map, value) -> map.put("date", value))
                    .build();
    private static final Function<Map<String, Object>, PlainType> REMAPPER =
            new Sequential.Builder<Map<String, Object>, PlainType>(PlainType::new)
                    .add(map -> (Integer) map.get("int"), PlainType::setPrivateInt)
                    .add(map -> (String) map.get("string"), PlainType::setPrivateString)
                    .add(map -> (Date) map.get("date"), PlainType::setPrivateDate)
                    .build();

    private final Random random = new Random();

    @Test
    public final void map() {
        final PlainType source = new PlainType().setPrivateInt(random.nextInt())
                                                .setPrivateString(new BigInteger(random.nextInt(64) + 1, random).toString(36))
                                                .setPrivateDate(new Date(random.nextLong()));
        final Map<String, Object> result = MAPPER.apply(source);
        assertEquals(source, REMAPPER.apply(result));
    }

    @Test
    public final void remap() {
        final Map<String, Object> source = ImmutableMap.<String, Object>builder()
                .put("int", random.nextInt())
                .put("string", new BigInteger(random.nextInt(64) + 1, random).toString(36))
                .put("date", new Date(random.nextLong()))
                .build();
        final PlainType result = REMAPPER.apply(source);
        assertEquals(source, MAPPER.apply(result));
    }
}
