package de.team33.test.mapping.v1;

import com.google.common.collect.ImmutableMap;
import de.team33.libs.mapping.v1.FieldMapper;
import de.team33.libs.reflect.v4.Fields;
import de.team33.test.mapping.shared.SubType;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;


public class FieldMapperTest {

    @Test
    public void map() {
        final SubType expected = new SubType(278, "a string", new Date());
        final Map<String, Object> stage = SubType.MAPPER.map(expected);
        final SubType result = SubType.MAPPER.mapTo(new SubType(0, null, null)).apply(stage);
        assertEquals(expected, result);
    }

    @Test
    public void mapTo() {
        final Map<?, ?> expected = ImmutableMap.builder()
                .put(".intValue", 11)
                .put(".stringValue", "abc")
                .put(".dateValue", new Date(0))
                .put("intValue", -5)
                .put("stringValue", "def")
                .put("dateValue", new Date())
                .build();
        final SubType stage = SubType.MAPPER.mapTo(new SubType(0, null, null)).apply(expected);
        final Map<String, Object> result = SubType.MAPPER.map(stage);
        assertEquals(expected, result);
    }

    @Test
    public void mapFlatSimple() {
        final FieldMapper<SubType> mapper = FieldMapper.factory(Fields.Mapping.SIGNIFICANT_FLAT).apply(SubType.class);
        final SubType subject = new SubType(278, "a string", new Date());
        final Map<?, ?> expected = ImmutableMap.builder()
                .put("intValue", subject.intValue)
                .put("stringValue", subject.stringValue)
                .put("dateValue", subject.dateValue)
                .build();
        assertEquals(expected, mapper.map(subject));
    }
}
