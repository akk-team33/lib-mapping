package de.team33.test.mapping.v4;

import com.google.common.collect.ImmutableMap;
import de.team33.libs.mapping.v4.Mapper;
import de.team33.libs.mapping.v4.PlainMapper;
import de.team33.test.mapping.shared.PlainType;

import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class PlainMapperTest extends MapperTestBase<PlainType, Map<String, Object>> {

    private static final Mapper<PlainType, Map<String, Object>> MAPPER =
            PlainMapper.<PlainType, Map<String, Object>>builder(TreeMap::new)
                    .add(PlainType::getPrivateInt, (map, value) -> map.put("int", value))
                    .add(PlainType::getPrivateString, (map, value) -> map.put("string", value))
                    .add(PlainType::getPrivateDate, (map, value) -> map.put("date", value))
                    .build();
    private static final Mapper<Map<String, Object>, PlainType> REMAPPER =
            PlainMapper.<Map<String, Object>, PlainType>builder(PlainType::new)
                    .add(map -> (Integer) map.get("int"), PlainType::setPrivateInt)
                    .add(map -> (String) map.get("string"), PlainType::setPrivateString)
                    .add(map -> (Date) map.get("date"), PlainType::setPrivateDate)
                    .build();

    private final Random random = new Random();

    @Override
    protected final PlainType newSource() {
        return new PlainType().setPrivateInt(random.nextInt())
                              .setPrivateString(new BigInteger(random.nextInt(64) + 1, random).toString(36))
                              .setPrivateDate(new Date(random.nextLong()));
    }

    @Override
    protected final Map<String, Object> newResult() {
        return ImmutableMap.<String, Object>builder()
                .put("int", random.nextInt())
                .put("string", new BigInteger(random.nextInt(64) + 1, random).toString(36))
                .put("date", new Date(random.nextLong()))
                .build();
    }

    @Override
    protected final Mapper<PlainType, Map<String, Object>> mapper() {
        return MAPPER;
    }

    @Override
    protected final Mapper<Map<String, Object>, PlainType> remapper() {
        return REMAPPER;
    }
}
