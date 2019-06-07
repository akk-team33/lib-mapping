package de.team33.test.mapping.v1;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import de.team33.libs.mapping.v1.FieldMapper;
import de.team33.libs.reflect.v3.Fields;
import org.junit.Test;


public class FieldMapperTest
{

  private static final Fields.Mapper FIELDS_MAPPING_0 = Fields.mapping().prepare();

  private static final Fields.Mapper FIELDS_MAPPING_1 = Fields.mapping()
                                                              .setToFieldStream(Fields.Streaming.FLAT)
                                                              .setToName(Fields.Naming.SIMPLE)
                                                              .prepare();

  @Test
  public void mapReMap()
  {
    final FieldMapper<Subject> mapper = new FieldMapper<>(Subject.class, FIELDS_MAPPING_0::map);
    final Subject subject = new Subject(278, 3.141592654, "a string");
    final Map<String, Object> stage = mapper.map(subject);
    final Subject result = mapper.mapTo(new Subject()).apply(stage);
    assertEquals(subject, result);
  }

  @Test
  public void mapReMapReverse()
  {
    final FieldMapper<Subject> mapper = new FieldMapper<>(Subject.class, FIELDS_MAPPING_0::map);
    final Map<?, ?> expected = ImmutableMap.builder()
                                           .put(".intValue", 11)
                                           .put(".doubleValue", Math.sqrt(2))
                                           .put(".stringValue", "abc")
                                           .put("intValue", -5)
                                           .put("doubleValue", Math.PI)
                                           .put("stringValue", "def")
                                           .put("dateValue", new Date())
                                           .build();
    final Subject stage = mapper.mapTo(new Subject()).apply(expected);
    final Map<String, Object> result = mapper.map(stage);
    assertEquals(expected, result);
  }

  @Test
  public void map()
  {
    final FieldMapper<Subject> mapper = new FieldMapper<>(Subject.class, FIELDS_MAPPING_0::map);
    final Subject subject = new Subject(278, 3.141592654, "a string");
    final Map<?, ?> expected = ImmutableMap.builder()
                                           .put(".intValue", subject.getIntValue())
                                           .put(".doubleValue", subject.getDoubleValue())
                                           .put(".stringValue", subject.getStringValue())
                                           .put("intValue", subject.intValue)
                                           .put("doubleValue", subject.doubleValue)
                                           .put("stringValue", subject.stringValue)
                                           .put("dateValue", subject.dateValue)
                                           .build();
    assertEquals(expected, mapper.map(subject));
  }

  @Test
  public void mapFlatSimple()
  {
    final FieldMapper<Subject> mapper = new FieldMapper<>(Subject.class, FIELDS_MAPPING_1::map);
    final Subject subject = new Subject(278, 3.141592654, "a string");
    final Map<?, ?> expected = ImmutableMap.builder()
                                           .put("intValue", subject.intValue)
                                           .put("doubleValue", subject.doubleValue)
                                           .put("stringValue", subject.stringValue)
                                           .put("dateValue", subject.dateValue)
                                           .build();
    assertEquals(expected, mapper.map(subject));
  }

  private static class SuperSubject
  {

    private final int intValue;

    private final double doubleValue;

    private final String stringValue;

    private SuperSubject(final int intValue, final double doubleValue, final String stringValue)
    {
      this.intValue = intValue;
      this.doubleValue = doubleValue;
      this.stringValue = stringValue;
    }

    private SuperSubject()
    {
      this.intValue = 0;
      this.doubleValue = 0;
      this.stringValue = null;
    }

    static List<Object> toList(final SuperSubject subject)
    {
      return Arrays.asList(subject.intValue, subject.doubleValue, subject.stringValue);
    }

    public int getIntValue()
    {
      return intValue;
    }

    public double getDoubleValue()
    {
      return doubleValue;
    }

    public String getStringValue()
    {
      return stringValue;
    }
  }


  private static class Subject extends SuperSubject
  {

    private final int intValue;

    private final double doubleValue;

    private final String stringValue;

    private final Date dateValue;

    private Subject(final int intValue, final double doubleValue, final String stringValue)
    {
      super(intValue - 1, doubleValue * 10, "super " + stringValue);
      this.intValue = intValue;
      this.doubleValue = doubleValue;
      this.stringValue = stringValue;
      this.dateValue = new Date();
    }

    private Subject()
    {
      this.intValue = 0;
      this.doubleValue = 0.0;
      this.stringValue = null;
      this.dateValue = null;
    }

    static List<Object> toList(final Subject subject)
    {
      return Arrays.asList(SuperSubject.toList(subject),
                           subject.intValue,
                           subject.doubleValue,
                           subject.stringValue,
                           subject.dateValue);
    }

    @Override
    public int hashCode()
    {
      return toList(this).hashCode();
    }

    @Override
    public boolean equals(final Object obj)
    {
      return (this == obj) || ((obj instanceof Subject) && toList(this).equals(toList((Subject)obj)));
    }

    @Override
    public String toString()
    {
      return toList(this).toString();
    }
  }
}
