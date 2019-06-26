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

import de.team33.libs.reflect.v4.Fields;


/**
 * <p>A tool for creating {@link Map}s that represent the properties of a given instance of a particular
 * type.</p>
 * <p>To get an Instance use {@link PropertyMapper#simple(Class)} or
 * {@link PropertyMapper.Stage#apply(Class)}</p>
 *
 * @see #stage(Function)
 */
public final class PropertyMapper<T> {

    private static final String ACCESS_FAILED = "Cannot apply method <%s> of <%s> with parameter <%s>";

    private final Map<String, Method> getterMap;
    private final Map<String, Method> setterMap;

    private PropertyMapper(final Map<String, Method> getterMap, final Map<String, Method> setterMap) {
      this.getterMap = unmodifiableMap(new TreeMap<>(getterMap));
      this.setterMap = unmodifiableMap(new TreeMap<>(setterMap));
    }

    /**
     * Returns a {@linkplain Stage preliminary stage} of a {@link PropertyMapper}.
     *
     * @param mapping A method that can, in a certain way, generate a {@link Map} from a {@link Class} that can map the
     *                names of all relevant fields to the {@link Field}s themselves.
     */
    public static Stage stage(final Function<Class<?>, Map<String, Method>> getterMapping,
                              final Function<Class<?>, Map<String, Method>> setterMapping) {
        return new Stage(getterMapping, setterMapping);
    }

    /**
     * <p>Returns a {@link PropertyMapper} that can represent all <em>significant</em> fields of an instance of the given
     * {@link Class} as a {@link Map}.</p>
     *
     * <p><em>Significant</em> in this context are all fields straightly declared by the given class,
     * which are non-static and non-transient.</p>
     */
    public static <T> PropertyMapper<T> simple(final Class<T> type) {
        return stage(Methods.Mapping.PUBLIC_GETTERS, Methods.Mapping.PUBLIC_SETTERS).apply(type);
    }

    /**
     * <p>Returns a {@link Map} representation for the given subject that corresponds to the template of this
     * FieldMapper.</p>
     * <p>The result {@link Map} directly reflects the affected fields of the subject. If the latter is modified,
     * the {@link Map} will have corresponding modifications, but the {@link Map} as such is unmodifiable.</p>
     */
    public final Map<String, Object> map(final T subject) {
        return new AbstractMap<String, Object>() {
            private Set<Entry<String, Object>> backing = new MethodEntrySet(subject, getterMap.entrySet());

            @Override
            public Set<Entry<String, Object>> entrySet() {
                return backing;
            }
        };
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
     * Copies an origin of the associated {@link Class} to a target instance and returns that target instance.
     */
    public final T copy(final T origin, final T target) {
        return remap(map(origin), target);
    }

    /**
     * Represents a preliminary stage of a {@link PropertyMapper}.
     */
    public static final class Stage {

        private final Function<Class<?>, Map<String, Method>> getterMapping;
        private final Function<Class<?>, Map<String, Method>> setterMapping;

        private Stage(final Function<Class<?>, Map<String, Method>> getterMapping,
                      final Function<Class<?>, Map<String, Method>> setterMapping) {
            this.getterMapping = getterMapping;
            this.setterMapping = setterMapping;
        }

        /**
         * <p>Returns a {@link PropertyMapper} that can represent fields of an instance of the given {@link Class} as a
         * {@link Map}.</p>
         */
        public final <T> PropertyMapper<T> apply(final Class<T> type) {
            return new PropertyMapper<>(getterMapping.apply(type), setterMapping.apply(type));
        }
    }
}
