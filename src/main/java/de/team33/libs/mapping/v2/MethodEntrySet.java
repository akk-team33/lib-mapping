package de.team33.libs.mapping.v2;

import static java.lang.String.format;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


class MethodEntrySet extends AbstractSet<Map.Entry<String, Object>> {

    private static final String ACCESS_FAILED = "cannot invoke method <%s> from subject <%s>";

    private final Object subject;
    private final Set<Map.Entry<String, Method>> entries;

    MethodEntrySet(final Object subject, final Set<Map.Entry<String, Method>> entries) {
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

    private Map.Entry<String, Object> newEntry(final Map.Entry<String, Method> entry) {
        final Method field = entry.getValue();
        try {
            return new SimpleImmutableEntry<>(entry.getKey(), field.invoke(subject));
        } catch (final IllegalAccessException | InvocationTargetException caught) {
            throw new IllegalStateException(format(ACCESS_FAILED, field, subject), caught);
        }
    }

    private class EntryIterator implements Iterator<Map.Entry<String, Object>> {

        private final Iterator<Map.Entry<String, Method>> backing;

        private EntryIterator(final Iterator<Map.Entry<String, Method>> backing) {
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
