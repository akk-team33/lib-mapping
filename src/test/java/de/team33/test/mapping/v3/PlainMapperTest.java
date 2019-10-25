package de.team33.test.mapping.v3;

import com.google.common.collect.ImmutableMap;
import de.team33.libs.mapping.v3.Mapper;
import de.team33.libs.mapping.v3.PlainMapper;
import de.team33.test.mapping.shared.PlainType;
import org.junit.Test;

import java.util.Date;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PlainMapperTest {

    private static final Mapper<PlainType> MAPPER = PlainMapper.<PlainType>builder()
            .add("privateInt", PlainType::getPrivateInt, PlainType::setPrivateInt)
            .add("privateString", PlainType::getPrivateString, PlainType::setPrivateString)
            .add("privateDate", PlainType::getPrivateDate, PlainType::setPrivateDate)
            .build();

    @Test
    public final void map() {
        final PlainType origin = new PlainType().setPrivateInt(278)
                                                .setPrivateString("a string")
                                                .setPrivateDate(new Date());
        final Map<String, Object> stage = MAPPER.map(origin);
        final PlainType result = MAPPER.remap(stage, new PlainType());
        assertEquals(origin, result);
    }

    @Test
    public final void remap() {
        final Map<String, Object> origin = ImmutableMap.<String, Object>builder()
                .put("privateInt", 278)
                .put("privateString", "a string")
                .put("privateDate", new Date())
                .build();
        final PlainType stage = MAPPER.remap(origin, new PlainType());
        final Map<String, Object> result = MAPPER.map(stage);
        assertEquals(origin, result);
    }
}
