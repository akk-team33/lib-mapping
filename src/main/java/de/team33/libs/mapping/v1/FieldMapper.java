package de.team33.libs.mapping.v1;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;


/**
 * A tool for creating {@link Map}s that represent the fields of a given instance of a particular type.
 */
public final class FieldMapper<T> {

    private final Map<String, Field> fieldMap;

    public FieldMapper(final Class<T> subjectClass, final Function<Class<T>, Map<String, Field>> toFieldsMap) {
        this.fieldMap = toFieldsMap.apply(subjectClass);
    }

    public final Map<String, Object> map(final T subject) {
        return new AbstractMap<String, Object>() {
            private Set<Entry<String, Object>> backing = new EntrySet(subject);

            @Override
            public Set<Entry<String, Object>> entrySet() {
                return backing;
            }
        };
    }

    public final Function<Map<?, ?>, T> mapTo(final T subject) {
        return map -> {
            fieldMap.forEach((name, field) -> {
                final Object value = map.get(name);
                try {
                    field.set(subject, value);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(String.format("Cannot set %s to value <%s>", field, value), e);
                }
            });
            return subject;
        };
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
                return new Entry(backing.next());
            }
        }


        private class Entry implements Map.Entry<String, Object> {

            private final Map.Entry<String, Field> backing;

            private Entry(final Map.Entry<String, Field> backing) {
                this.backing = backing;
            }

            @Override
            public String getKey() {
                return backing.getKey();
            }

            @Override
            public Object getValue() {
                try {
                    return backing.getValue().get(subject);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e.getMessage(), e);
                }
            }

            @Override
            public Object setValue(final Object value) {
                throw new UnsupportedOperationException("this entry is immutable");
            }

            @Override
            public int hashCode() {
                throw new UnsupportedOperationException("not yet implemented");
            }

            @Override
            public boolean equals(final Object obj) {
                throw new UnsupportedOperationException("not yet implemented");
            }

            @Override
            public String toString() {
                throw new UnsupportedOperationException("not yet implemented");
            }
        }
    }
}
