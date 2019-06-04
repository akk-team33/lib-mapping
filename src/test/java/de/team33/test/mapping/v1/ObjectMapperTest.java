package de.team33.test.mapping.v1;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.junit.Test;


public class ObjectMapperTest
{

  private static final TypeReference<Map<String, Object>> MAP_STRING2STRING_TYPE =

    new TypeReference<Map<String, Object>>()
    {

    };

  @Test
  public final void test()
  {
    final ObjectMapper mapper = new ObjectMapper();
    final Subject subject = new Subject(278, 3.141592654, "123456");
    final Map<String, String> result = mapper.convertValue(subject, MAP_STRING2STRING_TYPE);
    final Map<?, ?> expected = ImmutableMap.builder()
                                           .put("intValue", subject.intValue)
                                           .put("doubleValue", subject.doubleValue)
                                           .put("stringValue", subject.stringValue)
                                           .put("dateValue", subject.dateValue)
                                           .build();
    Assert.assertEquals(expected, result);
  }

  private class Subject
  {

    private final int intValue;

    private final double doubleValue;

    private final String stringValue;

    private final Date dateValue;

    private Subject(final int intValue, final double doubleValue, final String stringValue)
    {
      //super(intValue - 1, doubleValue * 10, "super " + stringValue);
      this.intValue = intValue;
      this.doubleValue = doubleValue;
      this.stringValue = stringValue;
      this.dateValue = new Date();
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

    public Date getDateValue()
    {
      return dateValue;
    }
  }
}
