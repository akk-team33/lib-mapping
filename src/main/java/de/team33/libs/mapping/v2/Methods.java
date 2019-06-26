package de.team33.libs.mapping.v2;

import static java.util.stream.Collectors.toMap;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import de.team33.libs.reflect.v4.Fields;


class Methods
{

  static Stream<Method> publicStreamOf(Class<?> type) {
    throw new UnsupportedOperationException("not yet implemented");
  }

  interface Naming extends Function<Method, String> {

    Naming SIMPLE = method -> method.getName();
  }

  interface Streaming extends Function<Class<?>, Stream<Method>> {

    Streaming PUBLIC_GETTERS = type -> publicStreamOf(type);

    Streaming PUBLIC_SETTERS = type -> publicStreamOf(type);
  }

  @FunctionalInterface
  interface Mapping extends Function<Class<?>, Map<String, Method>> {

    Mapping PUBLIC_GETTERS = type -> Streaming.PUBLIC_GETTERS.apply(type)
                                                             .peek(field -> field.setAccessible(true))
                                                             .collect(toMap(Naming.SIMPLE, field -> field));
    Mapping PUBLIC_SETTERS = type -> Streaming.PUBLIC_SETTERS.apply(type)
                                                             .peek(field -> field.setAccessible(true))
                                                             .collect(toMap(Naming.SIMPLE, field -> field));
  }
}
