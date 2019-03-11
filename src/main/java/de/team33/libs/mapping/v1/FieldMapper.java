package de.team33.libs.mapping.v1;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import de.team33.libs.reflect.v3.Fields;


public class FieldMapper<T>
{

  public static final Stage STAGE = builder().prepare();

  private final Map<String, Field> fields;
  private final Function<Class<?>, Function<Object, Object>> subMapping;

  private FieldMapper(final Stage stage, final Class<T> subjectClass)
  {
    this.fields = stage.fields.map(subjectClass);
    this.subMapping = stage.subMapping;
  }

  public static Builder builder()
  {
    return new Builder();
  }

  public final Map<Object, Object> map(final T subject)
  {
    return new AbstractMap<Object, Object>()
    {

      @Override
      public final Set<Entry<Object, Object>> entrySet()
      {
        return new EntrySet(subject);
      }
    };
  }

  private class EntrySet extends AbstractSet<Map.Entry<Object, Object>>
  {

    private final Object subject;

    private EntrySet(final Object subject)
    {
      this.subject = subject;
    }

    @Override
    public Iterator<Map.Entry<Object, Object>> iterator()
    {
      return new EntryIterator(fields.entrySet().iterator());
    }

    @Override
    public int size()
    {
      return fields.size();
    }

    private class EntryIterator implements Iterator<Map.Entry<Object, Object>>
    {

      private final Iterator<Map.Entry<String, Field>> backing;

      private EntryIterator(final Iterator<Map.Entry<String, Field>> backing)
      {
        this.backing = backing;
      }

      @Override
      public boolean hasNext()
      {
        return backing.hasNext();
      }

      @Override
      public Map.Entry<Object, Object> next()
      {
        final Map.Entry<String, Field> entry = backing.next();
        final String key = entry.getKey();
        try
        {
          final Object value = entry.getValue().get(subject);
          return new AbstractMap.SimpleImmutableEntry<>(
            subMapping.apply(key.getClass()).apply(key),
            subMapping.apply(value.getClass()).apply(value)
          );
        }
        catch (final IllegalAccessException caught)
        {
          throw new IllegalArgumentException("could not access field <" + entry.getValue() + ">", caught);
        }
      }
    }
  }


  public static class Stage
  {
    private final Fields.Mapper fields;
    private final Function<Class<?>, Function<Object, Object>> subMapping;

    private Stage(final Builder builder)
    {
      this.fields = Fields.mapping()
                          .setToFieldStream(builder.toFieldStream)
                          .setToNaming(builder.toNaming)
                          .prepare();
      this.subMapping = builder.subMapping;
    }

    public final <T> FieldMapper<T> get(final Class<T> subjectClass)
    {
      return new FieldMapper<>(this, subjectClass);
    }
  }


  public static class Builder
  {

    private Function<Class<?>, Stream<Field>> toFieldStream;
    private Function<Class<?>, Function<Field, String>> toNaming;
    private Function<Class<?>, Function<Object, Object>> subMapping;

    private Builder()
    {
      toFieldStream = Fields.Streaming.SIGNIFICANT;
      toNaming = Fields.Naming.ContextSensitive.COMPACT;
      subMapping = ignored -> subject -> subject;
    }

    public final Builder setToFieldStream(final Function<Class<?>, Stream<Field>> toFieldStream)
    {
      this.toFieldStream = toFieldStream;
      return this;
    }

    public final Builder setToNaming(final Function<Class<?>, Function<Field, String>> toNaming)
    {
      this.toNaming = toNaming;
      return this;
    }

    public Builder setToName(final Function<Field, String> toName)
    {
      return setToNaming(ignored -> toName);
    }

    public final Builder setSubMapping(final Function<Class<?>, Function<Object, Object>> subMapping)
    {
      this.subMapping = subMapping;
      return this;
    }

    public final Stage prepare()
    {
      return new Stage(this);
    }

    public final <T> FieldMapper<T> build(final Class<T> subjectClass)
    {
      return prepare().get(subjectClass);
    }
  }
}
