package de.team33.libs.mapping.v1;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import de.team33.libs.reflect.v3.Fields;


/**
 * <p>A tool for cloning instances of a particular type.</p>
 *
 * <p>Assumes that the value of an instance results from its field values,
 * ignoring static and transient fields.</p>
 */
public class Cloning<T> {

    private final Supplier<T> supplier;
    private final Set<Field> fields;

    public Cloning(final Class<T> type, final Supplier<T> supplier) {
        this.supplier = supplier;
        this.fields = Collections.unmodifiableSet(
            Fields.deep(type)
                  .filter(Fields.Filter.SIGNIFICANT)
                  .peek(field -> field.setAccessible(true))
                  .collect(Collectors.toSet())
        );
    }

    public final T clone(final T origin) {
        return copy(origin, supplier.get());
    }

    public final T copy(final T origin, final T result) {
        for ( final Field field : fields ) {
          try {
            field.set(result, field.get(origin));
          } catch (IllegalAccessException e) {
            throw new IllegalStateException("could not access field <" + field + ">", e);
          }
        }
        return result;
    }
}
