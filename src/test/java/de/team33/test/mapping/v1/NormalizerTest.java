package de.team33.test.mapping.v1;

import static org.junit.Assert.*;

import de.team33.libs.mapping.v1.Normalizer;
import org.junit.Test;


public class NormalizerTest
{

  private final Normalizer normalizer = new Normalizer();

  @Test
  public void normalNull()
  {
    assertNull(normalizer.normal(null));
  }

  @Test
  public void normalPrimitive()
  {
    assertEquals("278", normalizer.normal(278).toString());
  }
}
