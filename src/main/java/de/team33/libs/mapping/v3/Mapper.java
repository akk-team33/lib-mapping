package de.team33.libs.mapping.v3;

import java.util.Map;

public interface Mapper<T> {

    Map<String, Object> map(T origin);

    T remap(Map<String, Object> origin, T target);
}
