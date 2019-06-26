package de.team33.libs.mapping.v2;

import static java.util.stream.Collectors.toMap;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;


class Methods
{

  static Stream<Method> publicStreamOf(Class<?> type) {
    return Stream.of(type.getMethods());
  }

  private static String getterAsPropertyName(final Method method) {
    final String name = method.getName();
    if (name.startsWith("get"))
      return stripped(name, "get");
    if (name.startsWith("is"))
      return stripped(name, "is");
    throw new UnsupportedOperationException("not yet implemented");
  }

  private static String setterAsPropertyName(final Method method) {
    final String name = method.getName();
    if (name.startsWith("set"))
      return stripped(name, "set");
    throw new UnsupportedOperationException("not yet implemented");
  }

  private static String stripped(final String name, final String prefix) {
    final String tail = name.substring(prefix.length());
    return tail.isEmpty() ? "" : tail.substring(0, 1).toLowerCase() + tail.substring(1);
  }

  @FunctionalInterface
  interface Naming extends Function<Method, String> {

    Naming SIMPLE = Method::getName;
    Naming GETTER_AS_PROPERTY = Methods::getterAsPropertyName;
    Naming SETTER_AS_PROPERTY = Methods::setterAsPropertyName;
  }

  @FunctionalInterface
  interface Filter extends Predicate<Method> {

    Filter GETTERS = method -> 0 == method.getParameterCount()
                               && !Object.class.equals(method.getDeclaringClass())
                               && Stream.of("get", "is").anyMatch(prfx -> method.getName().startsWith(prfx));

    Filter SETTERS = method -> 1 == method.getParameterCount()
                               && !Object.class.equals(method.getDeclaringClass())
                               && method.getName().startsWith("set");
  }

  @FunctionalInterface
  interface Streaming extends Function<Class<?>, Stream<Method>> {

    Streaming PUBLIC_GETTERS = type -> publicStreamOf(type).filter(Filter.GETTERS);

    Streaming PUBLIC_SETTERS = type -> publicStreamOf(type).filter(Filter.SETTERS);
  }

  @FunctionalInterface
  interface Mapping extends Function<Class<?>, Map<String, Method>> {

    Mapping PUBLIC_GETTERS = type -> Streaming.PUBLIC_GETTERS.apply(type)
                                                             .peek(field -> field.setAccessible(true))
                                                             .collect(toMap(Naming.GETTER_AS_PROPERTY, field -> field));
    Mapping PUBLIC_SETTERS = type -> Streaming.PUBLIC_SETTERS.apply(type)
                                                             .peek(field -> field.setAccessible(true))
                                                             .collect(toMap(Naming.SETTER_AS_PROPERTY, field -> field));
  }
}
