package de.team33.libs.mapping.v2;

import java.lang.reflect.Field;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;

class FieldEntrySet extends AbstractSet<Map.Entry<String, Object>> {

    private static final String ACCESS_FAILED = "cannot get field <%s> from subject <%s>";

    private final Object subject;
    private final Set<Map.Entry<String, Field>> entries;

    FieldEntrySet(final Object subject, final Set<Map.Entry<String, Field>> entries) {
        this.subject = subject;
        this.entries = entries;
    }

    @Override
    public final Iterator<Map.Entry<String, Object>> iterator() {
        return new EntryIterator(entries.iterator());
    }

    @Override
    public final int size() {
        return entries.size();
    }

    private Map.Entry<String, Object> newEntry(final Map.Entry<String, Field> entry) {
        final Field field = entry.getValue();
        try {
            return new SimpleImmutableEntry<>(entry.getKey(), field.get(subject));
        } catch (final IllegalAccessException caught) {
            throw new IllegalStateException(format(ACCESS_FAILED, field, subject), caught);
        }
    }

    private class EntryIterator implements Iterator<Map.Entry<String, Object>> {

        private final Iterator<Map.Entry<String, Field>> backing;

        private EntryIterator(final Iterator<Map.Entry<String, Field>> backing) {
            this.backing = backing;
        }

        @Override
        public final boolean hasNext() {
            return backing.hasNext();
        }

        @Override
        public final Map.Entry<String, Object> next() {
            return newEntry(backing.next());
        }
    }
}
