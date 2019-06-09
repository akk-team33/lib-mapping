package de.team33.test.mapping.v1;

import com.google.common.collect.ImmutableMap;
import de.team33.libs.mapping.v1.FieldMapper;
import de.team33.libs.reflect.v3.Fields;
import de.team33.test.mapping.shared.SubType;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class FieldMapperTest {

    private static final Fields.Mapper FIELDS_MAPPER_0 = Fields.mapping().prepare();
    private static final Fields.Mapper FIELDS_MAPPER_1 = Fields.mapping()
            .setToFieldStream(Fields.Streaming.FLAT)
            .setToName(Fields.Naming.SIMPLE)
            .prepare();
    private static final FieldMapper.Factory FACTORY_0 = FieldMapper.factory(FIELDS_MAPPER_0::map);
    private static final FieldMapper.Factory FACTORY_1 = FieldMapper.factory(FIELDS_MAPPER_1::map);

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
                // {.dateValue=null, .intValue=11, .stringValue=abc, dateValue=Sun Jun 09 17:45:26 CEST 2019, intValue=-5, stringValue=def}
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
        final FieldMapper<Subject> mapper = FACTORY_1.apply(Subject.class);
        final Subject subject = new Subject(278, 3.141592654, "a string");
        final Map<?, ?> expected = ImmutableMap.builder()
                .put("intValue", subject.intValue)
                .put("doubleValue", subject.doubleValue)
                .put("stringValue", subject.stringValue)
                .put("dateValue", subject.dateValue)
                .build();
        assertEquals(expected, mapper.map(subject));
    }

    private static class SuperSubject {

        private final int intValue;

        private final double doubleValue;

        private final String stringValue;

        private SuperSubject(final int intValue, final double doubleValue, final String stringValue) {
            this.intValue = intValue;
            this.doubleValue = doubleValue;
            this.stringValue = stringValue;
        }

        private SuperSubject() {
            this.intValue = 0;
            this.doubleValue = 0;
            this.stringValue = null;
        }

        static List<Object> toList(final SuperSubject subject) {
            return Arrays.asList(subject.intValue, subject.doubleValue, subject.stringValue);
        }

        public int getIntValue() {
            return intValue;
        }

        public double getDoubleValue() {
            return doubleValue;
        }

        public String getStringValue() {
            return stringValue;
        }
    }


    private static class Subject extends SuperSubject {

        private final int intValue;

        private final double doubleValue;

        private final String stringValue;

        private final Date dateValue;

        private Subject(final int intValue, final double doubleValue, final String stringValue) {
            super(intValue - 1, doubleValue * 10, "super " + stringValue);
            this.intValue = intValue;
            this.doubleValue = doubleValue;
            this.stringValue = stringValue;
            this.dateValue = new Date();
        }

        private Subject() {
            this.intValue = 0;
            this.doubleValue = 0.0;
            this.stringValue = null;
            this.dateValue = null;
        }

        static List<Object> toList(final Subject subject) {
            return Arrays.asList(SuperSubject.toList(subject),
                    subject.intValue,
                    subject.doubleValue,
                    subject.stringValue,
                    subject.dateValue);
        }

        @Override
        public int hashCode() {
            return toList(this).hashCode();
        }

        @Override
        public boolean equals(final Object obj) {
            return (this == obj) || ((obj instanceof Subject) && toList(this).equals(toList((Subject) obj)));
        }

        @Override
        public String toString() {
            return toList(this).toString();
        }
    }
}
