package de.team33.libs.mapping.v1;

import de.team33.libs.reflect.v4.Fields;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;

import static java.util.Collections.unmodifiableMap;


/**
 * <p>A tool for creating {@link Map}s that represent the fields of a given instance of a particular type.</p>
 * <p>To get an Instance use {@link FieldMapper#simple(Class)} or {@link FieldMapper.Stage#apply(Class)}</p>
 *
 * @see #stage(Function)
 */
public final class FieldMapper<T> {

    private final Map<String, Field> fieldMap;

    private FieldMapper(final Map<String, Field> fieldMap) {
        this.fieldMap = unmodifiableMap(new TreeMap<>(fieldMap));
    }

    /**
     * Returns a {@linkplain Stage preliminary stage} of a {@link FieldMapper}.
     *
     * @param mapping A method that can, in a certain way, generate a {@link Map} from a {@link Class} that can map the
     *                names of all relevant fields to the {@link Field}s themselves.
     */
    public static Stage stage(final Function<Class<?>, Map<String, Field>> mapping) {
        return new Stage(mapping);
    }

    /**
     * <p>Returns a {@link FieldMapper} that can represent all <em>significant</em> fields of an instance of the given
     * {@link Class} as a {@link Map}.</p>
     *
     * <p><em>Significant</em> in this context are all fields straightly declared by the given class,
     * which are non-static and non-transient.</p>
     */
    public static <T> FieldMapper<T> simple(final Class<T> type) {
        return stage(Fields.Mapping.SIGNIFICANT_FLAT).apply(type);
    }

    /**
     * <p>Returns a {@link Map} representation for the given subject that corresponds to the template of this
     * FieldMapper.</p>
     * <p>The result {@link Map} directly reflects the affected fields of the subject. If the latter is modified,
     * the {@link Map} will have corresponding modifications, but the {@link Map} as such is unmodifiable.</p>
     */
    public final Map<String, Object> map(final T subject) {
        return new AbstractMap<String, Object>() {
            private Set<Entry<String, Object>> backing = new FieldEntrySet(subject, fieldMap.entrySet());

            @Override
            public Set<Entry<String, Object>> entrySet() {
                return backing;
            }
        };
    }

    /**
     * Returns a {@link Function} that can copy the fields of an origin to a given {@code target} instance of the
     * associated type and return that instance.
     */
    public final Function<T, T> copyTo(final T target) {
        return origin -> mapTo(target).apply(map(origin));
    }

    /**
     * Returns a {@link Function} that can map a {@link Map} to a given {@code target} instance of the associated type
     * and return that instance.
     */
    public final Function<Map<?, ?>, T> mapTo(final T target) {
        return map -> {
            fieldMap.forEach((name, field) -> {
                final Object value = map.get(name);
                try {
                    field.set(target, value);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(String.format("Cannot set %s to value <%s>", field, value), e);
                }
            });
            return target;
        };
    }

    /**
     * Represents a preliminary stage of a {@link FieldMapper}.
     */
    public static final class Stage {

        private final Function<Class<?>, Map<String, Field>> mapping;

        private Stage(final Function<Class<?>, Map<String, Field>> mapping) {
            this.mapping = mapping;
        }

        /**
         * <p>Returns a {@link FieldMapper} that can represent fields of an instance of the given {@link Class} as a
         * {@link Map}.</p>
         */
        public final <T> FieldMapper<T> apply(final Class<T> type) {
            return new FieldMapper<>(mapping.apply(type));
        }
    }
}
