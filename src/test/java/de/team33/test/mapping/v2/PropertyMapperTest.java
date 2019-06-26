package de.team33.test.mapping.v2;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import de.team33.libs.mapping.v2.PropertyMapper;
import de.team33.libs.reflect.v4.Fields;
import de.team33.test.mapping.shared.SubType;
import org.junit.Test;


public class PropertyMapperTest
{

    @Test
    public void map() {
        final SubType expected = new SubType(278, "a string", new Date());
        final Map<String, Object> stage = SubType.MAPPER.map(expected);
        final SubType result = SubType.MAPPER.remap(stage, new SubType(0, null, null));
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
        final SubType stage = SubType.MAPPER.remap(expected, new SubType(0, null, null));
        final Map<String, Object> result = SubType.MAPPER.map(stage);
        assertEquals(expected, result);
    }

    @Test
    public void mapFlatSimple() {
        final PropertyMapper<SubType> mapper = PropertyMapper.stage(Methods.Mapping.SIGNIFICANT_FLAT).apply(SubType.class);
        final SubType subject = new SubType(278, "a string", new Date());
        final Map<?, ?> expected = ImmutableMap.builder()
                .put("intValue", subject.intValue)
                .put("stringValue", subject.stringValue)
                .put("dateValue", subject.dateValue)
                .build();
        assertEquals(expected, mapper.map(subject));
    }
}
