package de.team33.test.mapping.v1;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import de.team33.libs.mapping.v1.FieldMapper;
import de.team33.libs.reflect.v3.Fields;
import org.junit.Test;


public class FieldMapperTest
{

  @Test
  public void map()
  {
    final FieldMapper<Subject> mapper = FieldMapper.builder(Subject.class).build();
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
  public void mapAlt()
  {
    final FieldMapper<Subject> mapper = FieldMapper.builder(Subject.class)
                                                   .setToFieldStream(Fields.Streaming.FLAT)
                                                   .setToName(Fields.Naming.SIMPLE)
                                                   .build();
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

    private Subject(final int intValue,
                    final double doubleValue,
                    final String stringValue)
    {
      super(intValue - 1, doubleValue * 10, "super " + stringValue);
      this.intValue = intValue;
      this.doubleValue = doubleValue;
      this.stringValue = stringValue;
      this.dateValue = new Date();
    }
  }
}