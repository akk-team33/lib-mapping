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
     * Returns a new {@link Stage}.
     */
    public static Stage stage(final Function<Class<?>, Map<String, Field>> mapping) {
        return new Stage(mapping);
    }

    /**
     * <p>Returns a {@link FieldMapper} that can represent all <em>significant</em> fields of an instance as a
     * {@link Map}.</p>
     *
     * <p><em>Significant</em> are all fields of the given class and its superclasses,
     * which are not static and not transient.</p>
     */
    public static <T> FieldMapper<T> simple(final Class<T> type) {
        return stage(Fields.Mapping.SIGNIFICANT_FLAT).apply(type);
    }

    /**
     * Returns a {@link Map} representation for the given subject that corresponds to the template of this FieldMapper.
     */
    public final Map<String, Object> map(final T subject) {
        return new AbstractMap<String, Object>() {
            private Set<Entry<String, Object>> backing = new EntrySet(subject);

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
         * Returns a {@link FieldMapper} for a given {@link Class}.
         */
        public final <T> FieldMapper<T> apply(final Class<T> type) {
            return new FieldMapper<>(mapping.apply(type));
        }
    }

    private class EntrySet extends AbstractSet<Map.Entry<String, Object>> {

        private final Object subject;

        private EntrySet(final Object subject) {
            this.subject = subject;
        }

        @Override
        public Iterator<Map.Entry<String, Object>> iterator() {
            return new EntryIterator(fieldMap.entrySet().iterator());
        }

        @Override
        public int size() {
            return fieldMap.size();
        }

        private Map.Entry<String, Object> newEntry(final Map.Entry<String, Field> entry) {
            try {
                return new AbstractMap.SimpleImmutableEntry<>(entry.getKey(), entry.getValue().get(subject));
            } catch (final IllegalAccessException caught) {
                throw new IllegalStateException(String.format(
                        "cannot get <%s> from subject <%s>",
                        entry.getValue(), subject
                ), caught);
            }
        }

        private class EntryIterator implements Iterator<Map.Entry<String, Object>> {

            private final Iterator<Map.Entry<String, Field>> backing;

            private EntryIterator(final Iterator<Map.Entry<String, Field>> backing) {
                this.backing = backing;
            }

            @Override
            public boolean hasNext() {
                return backing.hasNext();
            }

            @Override
            public Map.Entry<String, Object> next() {
                return newEntry(backing.next());
            }
        }
    }
}
