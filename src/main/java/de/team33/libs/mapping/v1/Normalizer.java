package de.team33.libs.mapping.v1;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;


public class Normalizer
{

  private static final Set<Class<?>> PRIMITIVES = unmodifiableSet(new HashSet<>(asList(
    Boolean.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class,
    Character.class, CharSequence.class, BigInteger.class, BigDecimal.class
  )));

  private final Map<Class<?>, BiFunction<Normalizer, Object, Normal>> methods = new ConcurrentHashMap<>();

  public Normal normal(final Object origin)
  {
    final Class<?> type = (null == origin) ? Void.class : origin.getClass();
    final BiFunction<Normalizer, Object, Normal> method = getMethod(type);
    return method.apply(this, origin);
  }

  private BiFunction<Normalizer, Object, Normal> getMethod(final Class<?> type)
  {
    return Optional.ofNullable(methods.get(type)).orElseGet(() -> {
      final BiFunction<Normalizer, Object, Normal> result = getDefaultMethod(type);
      methods.put(type, result);
      return result;
    });
  }

  private BiFunction<Normalizer, Object, Normal> getDefaultMethod(final Class<?> type)
  {
    if (Void.class == type)
      return (normalizer, origin) -> null;
    if (isPrimitive(type))
      return Normalizer::newPrimitive;

    throw new UnsupportedOperationException("not yet implemented");
  }

  private boolean isPrimitive(final Class<?> type)
  {
    return PRIMITIVES.stream().anyMatch(primitive -> primitive.isAssignableFrom(type));
  }

  private Normal newPrimitive(final Object origin)
  {
    return new Primitive(origin);
  }
}
