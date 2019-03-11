package de.team33.libs.mapping.v1;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;


public class Normalizer
{

  private static final Set<Class<?>> PRIMITIVES = unmodifiableSet(new HashSet<>(asList(
    Boolean.class, Number.class, Character.class, CharSequence.class, Enum.class
  )));

  private static final Map<Class<?>, Function<Object, String>> SIMPLE_TO_STAGE = Collections.unmodifiableMap(new HashMap<Class<?>, Function<Object, String>>(){{
    put(Float.class, flt -> BigDecimal.valueOf((Float) flt).toString());
    put(Double.class, dbl -> BigDecimal.valueOf((Double) dbl).toString());
    put(Enum.class, enm -> ((Enum) enm).name());
  }});

  private final Map<Class, BiFunction> methods;

  private Normalizer(final Builder builder)
  {
    this.methods = new ConcurrentHashMap<>(builder.map);
  }

  public static Builder builder()
  {
    return new Builder();
  }

  public Normal normal(final Object origin)
  {
    final Class<?> type = (null == origin) ? Void.class : origin.getClass();
    final BiFunction<Normalizer, Object, Normal> method = getMethod(type);
    return method.apply(this, origin);
  }

  private BiFunction<Normalizer, Object, Normal> getMethod(final Class<?> type)
  {
    //noinspection unchecked
    return Optional.ofNullable((BiFunction<Normalizer, Object, Normal>) methods.get(type)).orElseGet(() -> {
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

  public static final class Builder {

    private final Map<Class, BiFunction> map = new HashMap<>();

    public final <T> Builder put(final Class<T> type, BiFunction<Normalizer, T, Normal> method)
    {
      map.put(type, method);
      return this;
    }

    public final Normalizer build()
    {
      return new Normalizer(this);
    }
  }
}
