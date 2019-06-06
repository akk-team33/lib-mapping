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


public final class FieldMapper<T>
{

  private final Map<String, Field> fieldMap;

  private FieldMapper(final Map<String, Field> fieldMap)
  {
    this.fieldMap = fieldMap;
  }

  public static Builder builder()
  {
    return new Builder();
  }

  public final Map<String, Object> map(final T subject)
  {
    return new AbstractMap<String, Object>()
    {
      private Set<Entry<String, Object>> backing = new EntrySet(subject);

      @Override
      public Set<Entry<String, Object>> entrySet()
      {
        return backing;
      }
    };
  }

  public SRC<T> mapFrom(final Map<?, ?> source)
  {
    return new SRC<>(source);
  }

  public final class SRC<T>
  {

    private final Map<?, ?> source;

    private SRC(final Map<?, ?> source)
    {
      this.source = source;
      throw new UnsupportedOperationException("not yet implemented");
    }
  }

  public static final class Stage
  {

    private final Fields.Mapper mapper;

    private Stage(final Fields.Mapper mapper)
    {
      this.mapper = mapper;
    }

    public <T> FieldMapper<T> apply(final Class<T> subjectClass)
    {
      return new FieldMapper<T>(mapper.map(subjectClass));
    }
  }

  public static final class Builder
  {

    private final Fields.Mapping mapping = Fields.mapping();

    private Builder(){}

    public final Stage prepare()
    {
      return new Stage(mapping.prepare());
    }

    public final <T> FieldMapper<T> build(final Class<T> subjectClass)
    {
      return prepare().apply(subjectClass);
    }

    public final Builder setToFieldStream(final Function<Class<?>, Stream<Field>> toFieldStream)
    {
      mapping.setToFieldStream(toFieldStream);
      return this;
    }

    public final Builder setToName(final Function<Field, String> toName)
    {
      mapping.setToName(toName);
      return this;
    }

    public final Builder setToNaming(final Function<Class<?>, Function<Field, String>> toNaming)
    {
      mapping.setToNaming(toNaming);
      return this;
    }
  }

  private class EntrySet extends AbstractSet<Map.Entry<String, Object>>
  {

    private final Object subject;

    private EntrySet(final Object subject)
    {
      this.subject = subject;
    }

    @Override
    public Iterator<Map.Entry<String, Object>> iterator()
    {
      return new EntryIterator(fieldMap.entrySet().iterator());
    }

    @Override
    public int size()
    {
      return fieldMap.size();
    }

    private class EntryIterator implements Iterator<Map.Entry<String, Object>>
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
      public Map.Entry<String, Object> next()
      {
        return new Entry(backing.next());
      }
    }


    private class Entry implements Map.Entry<String, Object>
    {

      private final Map.Entry<String, Field> backing;

      private Entry(final Map.Entry<String, Field> backing)
      {
        this.backing = backing;
      }

      @Override
      public String getKey()
      {
        return backing.getKey();
      }

      @Override
      public Object getValue()
      {
        try
        {
          return backing.getValue().get(subject);
        }
        catch (IllegalAccessException e)
        {
          throw new IllegalStateException(e.getMessage(), e);
        }
      }

      @Override
      public Object setValue(final Object value)
      {
        throw new UnsupportedOperationException("this entry is immutable");
      }

      @Override
      public int hashCode()
      {
        throw new UnsupportedOperationException("not yet implemented");
      }

      @Override
      public boolean equals(final Object obj)
      {
        throw new UnsupportedOperationException("not yet implemented");
      }

      @Override
      public String toString()
      {
        throw new UnsupportedOperationException("not yet implemented");
      }
    }
  }
}
