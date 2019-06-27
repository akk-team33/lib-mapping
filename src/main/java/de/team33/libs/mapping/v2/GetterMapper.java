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
 * <p>To get an Instance use {@link GetterMapper#simple(Class)} or
 * {@link GetterMapper.Stage#apply(Class)}</p>
 *
 * @see #stage(Function)
 */
public final class GetterMapper<T> {

    private static final String ACCESS_FAILED = "Cannot apply method <%s> of <%s> with parameter <%s>";

    private final Map<String, Method> getterMap;

    private GetterMapper(final Map<String, Method> getterMap) {
      this.getterMap = unmodifiableMap(new TreeMap<>(getterMap));
    }

    /**
     * Returns a {@linkplain Stage preliminary stage} of a {@link GetterMapper}.
     *
     * @param mapping A method that can, in a certain way, generate a {@link Map} from a {@link Class} that can map the
     *                names of all relevant fields to the {@link Field}s themselves.
     */
    public static Stage stage(final Function<Class<?>, Map<String, Method>> mapping) {
        return new Stage(mapping);
    }

    /**
     * <p>Returns a {@link GetterMapper} that can represent all <em>significant</em> fields of an instance of the given
     * {@link Class} as a {@link Map}.</p>
     *
     * <p><em>Significant</em> in this context are all fields straightly declared by the given class,
     * which are non-static and non-transient.</p>
     */
    public static <T> GetterMapper<T> simple(final Class<T> type) {
        return stage(Methods.Mapping.PUBLIC_GETTERS).apply(type);
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
     * Represents a preliminary stage of a {@link GetterMapper}.
     */
    public static final class Stage {

        private final Function<Class<?>, Map<String, Method>> mapping;

        private Stage(final Function<Class<?>, Map<String, Method>> mapping) {
            this.mapping = mapping;
        }

        /**
         * <p>Returns a {@link GetterMapper} that can represent fields of an instance of the given {@link Class} as a
         * {@link Map}.</p>
         */
        public final <T> GetterMapper<T> apply(final Class<T> type) {
            return new GetterMapper<>(mapping.apply(type));
        }
    }
}
