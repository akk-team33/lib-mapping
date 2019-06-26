package de.team33.test.mapping.v2;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import de.team33.test.mapping.shared.FlatType;
import org.junit.Test;


public class PropertyMapperTest
{

    @Test
    public void map() {
        final FlatType expected = new FlatType(278, "a string", new Date());
        final Map<String, Object> stage = FlatType.MAPPER.map(expected);
        final FlatType result = FlatType.MAPPER.remap(stage, new FlatType(0, null, null));
        assertEquals(expected, result);
    }

    @Test
    public void remap() {
        final Map<?, ?> expected = ImmutableMap.builder()
                .put(".intValue", 11)
                .put(".stringValue", "abc")
                .put(".dateValue", new Date(0))
                .put("intValue", -5)
                .put("stringValue", "def")
                .put("dateValue", new Date())
                .build();
        final FlatType stage = FlatType.MAPPER.remap(expected, new FlatType(0, null, null));
        final Map<String, Object> result = FlatType.MAPPER.map(stage);
        assertEquals(expected, result);
    }
}
