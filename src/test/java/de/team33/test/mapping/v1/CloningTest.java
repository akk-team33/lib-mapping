package de.team33.test.mapping.v1;

import java.util.Date;

import de.team33.libs.mapping.v1.Cloning;
import de.team33.test.mapping.shared.SubType;
import org.junit.Assert;
import org.junit.Test;


public class CloningTest
{

  private static final Cloning<SubType> CLONING = new Cloning<>(
    SubType.class,
    () -> new SubType(0, null, null)
  );

  @Test
  public void testClone()
  {
    final SubType origin = new SubType(278, "a string", new Date());
    final SubType result = CLONING.clone(origin);
    Assert.assertEquals(origin, result);
  }

  @Test
  public void testCopy()
  {
    final SubType origin = new SubType(278, "a string", new Date());
    final SubType result = CLONING.copy(origin, new SubType(0, null, null));
    Assert.assertEquals(origin, result);
  }
}
