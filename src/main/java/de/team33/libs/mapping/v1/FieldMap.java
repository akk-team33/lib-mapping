package de.team33.libs.mapping.v1;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import de.team33.libs.reflect.v3.Fields;


public class FieldMap extends AbstractMap<String, Object>
{

  private final Map<String, Field> fieldMap;

  public FieldMap(final Class<?> subjectClass)
  {
    fieldMap = Fields.mapping().prepare().map(subjectClass);
  }

  @Override
  public Set<Entry<String, Object>> entrySet()
  {
    return new EntrySet();
  }

  private class EntrySet extends AbstractSet<Entry<String, Object>>
  {

    @Override
    public Iterator<Entry<String, Object>> iterator()
    {
      return new EntryIterator(fieldMap.entrySet().iterator());
    }

    @Override
    public final int size()
    {
      return fieldMap.size();
    }
  }


  private class EntryIterator implements Iterator<Entry<String, Object>>
  {

    private final Iterator<Entry<String, Field>> backing;

    private EntryIterator(final Iterator<Entry<String, Field>> backing)
    {
      this.backing = backing;
    }

    @Override
    public boolean hasNext()
    {
      return backing.hasNext();
    }

    @Override
    public Entry<String, Object> next()
    {
      final Entry<String, Field> next = backing.next();
      try
      {
        return new SimpleImmutableEntry<>(next.getKey(), next.getValue().get(null));
      }
      catch (IllegalAccessException e)
      {
        throw new IllegalStateException(e.getMessage(), e);
      }
    }
  }
}
