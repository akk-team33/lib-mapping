package de.team33.libs.mapping.v2;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;


class Methods
{

  @FunctionalInterface
  interface Mapping extends Function<Class<?>, Map<String, Method>>
  {

    Mapping PUBLIC_GETTERS = type -> ;
  }
}
