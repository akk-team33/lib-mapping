package de.team33.test.mapping.v1;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.Ignore;
import org.junit.Test;


public class PrimitiveTest
{

  @Test
  public void byteToString()
  {
    for ( int i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; ++i )
    {
      assertEquals(BigInteger.valueOf(i).toString(), Byte.valueOf((byte) i).toString());
    }
  }

  @Test
  public void shortToString()
  {
    for ( int i = Short.MIN_VALUE; i <= Short.MAX_VALUE; ++i )
    {
      assertEquals(BigInteger.valueOf(i).toString(), Short.valueOf((short) i).toString());
    }
  }

  @Test
  public void intToString()
  {
    final Random random = new Random();
    LongStream.concat(
      LongStream.of(Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE),
      LongStream.generate(random::nextInt).limit(100000)
    ).forEach(lng -> assertEquals(BigInteger.valueOf(lng).toString(), Integer.valueOf((int) lng).toString()));
  }

  @Test
  public void longToString()
  {
    final Random random = new Random();
    LongStream.concat(
      LongStream.of(Long.MIN_VALUE, -1, 0, 1, Long.MAX_VALUE),
      LongStream.generate(random::nextLong).limit(100000)
    ).forEach(lng -> assertEquals(BigInteger.valueOf(lng).toString(), Long.valueOf(lng).toString()));
  }

  @Ignore
  @Test
  public void floatToString()
  {
    final Random random = new Random();
    Stream.generate(random::nextFloat).limit(100000)
          .forEach(flt -> assertEquals(BigDecimal.valueOf(flt).toString(), flt.toString()));
  }

  @Ignore
  @Test
  public void doubleToString()
  {
    final Random random = new Random();
    Stream.generate(random::nextDouble).limit(100000)
          .forEach(dbl -> assertEquals(BigDecimal.valueOf(dbl).toString(), dbl.toString()));
  }
}
