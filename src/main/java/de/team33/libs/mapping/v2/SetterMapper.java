package de.team33.libs.mapping.v2;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableMap;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;


/**
 * <p>A tool for creating {@link Map}s that represent the properties of a given instance of a particular
 * type.</p>
 * <p>To get an Instance use {@link SetterMapper#simple(Class)} or
 * {@link SetterMapper.Stage#apply(Class)}</p>
 *
 * @see #stage(Function)
 */
public final class SetterMapper<T> {

    private static final String ACCESS_FAILED = "Cannot apply method <%s> of <%s> with parameter <%s>";

    private final Map<String, Method> setterMap;

    private SetterMapper(final Map<String, Method> setterMap) {
      this.setterMap = unmodifiableMap(new TreeMap<>(setterMap));
    }

    /**
     * Returns a {@linkplain Stage preliminary stage} of a {@link SetterMapper}.
     *
     * @param mapping A method that can, in a certain way, generate a {@link Map} from a {@link Class} that can map the
     *                names of all relevant fields to the {@link Field}s themselves.
     */
    public static Stage stage(final Function<Class<?>, Map<String, Method>> mapping) {
        return new Stage(mapping);
    }

    /**
     * <p>Returns a {@link SetterMapper} that can represent all <em>significant</em> fields of an instance of the given
     * {@link Class} as a {@link Map}.</p>
     *
     * <p><em>Significant</em> in this context are all fields straightly declared by the given class,
     * which are non-static and non-transient.</p>
     */
    public static <T> SetterMapper<T> simple(final Class<T> type) {
        return stage(Methods.Mapping.PUBLIC_SETTERS).apply(type);
    }

    /**
     * Remaps an origin {@link Map} to a target instance of the associated {@link Class} and returns that instance.
     */
    public final T remap(final Map<?, ?> origin, final T target) {
        setterMap.forEach((name, setter) -> {
            if (origin.containsKey(name)) {
                final Object value = origin.get(name);
                try {
                    setter.invoke(target, value);
                } catch (IllegalAccessException | InvocationTargetException | RuntimeException e) {
                    throw new IllegalStateException(format(ACCESS_FAILED, setter, target, value), e);
                }
            }
        });
        return target;
    }

    /**
     * Represents a preliminary stage of a {@link SetterMapper}.
     */
    public static final class Stage {

        private final Function<Class<?>, Map<String, Method>> mapping;

        private Stage(final Function<Class<?>, Map<String, Method>> mapping) {
            this.mapping = mapping;
        }

        /**
         * <p>Returns a {@link SetterMapper} that can represent fields of an instance of the given {@link Class} as a
         * {@link Map}.</p>
         */
        public final <T> SetterMapper<T> apply(final Class<T> type) {
            return new SetterMapper<>(mapping.apply(type));
        }
    }
}
