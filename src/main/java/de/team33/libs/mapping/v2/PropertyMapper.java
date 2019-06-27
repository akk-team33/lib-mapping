package de.team33.libs.mapping.v2;

import static java.lang.String.format;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;


/**
 * <p>A tool for creating {@link Map}s that represent the properties of a given instance of a particular
 * type.</p>
 * <p>To get an Instance use {@link PropertyMapper#simple(Class)} or
 * {@link PropertyMapper.Stage#apply(Class)}</p>
 *
 * @see #stage(Function)
 */
public final class PropertyMapper<T> {

    private static final String ACCESS_FAILED = "Cannot apply method <%s> of <%s> with parameter <%s>";

    private final GetterMapper<T> getterMapper;
    private final SetterMapper<T> setterMap;

    private PropertyMapper(final GetterMapper<T> getterMapper, final SetterMapper<T> setterMapper) {
      this.getterMapper = getterMapper;
      this.setterMap = setterMapper;
    }

    /**
     * Returns a {@linkplain Stage preliminary stage} of a {@link PropertyMapper}.
     *
     * @param mapping A method that can, in a certain way, generate a {@link Map} from a {@link Class} that can map the
     *                names of all relevant fields to the {@link Field}s themselves.
     */
    public static Stage stage(final Function<Class<?>, Map<String, Method>> getterMapping,
                              final Function<Class<?>, Map<String, Method>> setterMapping) {
        return new Stage(getterMapping, setterMapping);
    }

    /**
     * <p>Returns a {@link PropertyMapper} that can represent all <em>significant</em> fields of an instance of the given
     * {@link Class} as a {@link Map}.</p>
     *
     * <p><em>Significant</em> in this context are all fields straightly declared by the given class,
     * which are non-static and non-transient.</p>
     */
    public static <T> PropertyMapper<T> simple(final Class<T> type) {
        return stage(Methods.Mapping.PUBLIC_GETTERS, Methods.Mapping.PUBLIC_SETTERS).apply(type);
    }

    /**
     * <p>Returns a {@link Map} representation for the given subject that corresponds to the template of this
     * FieldMapper.</p>
     * <p>The result {@link Map} directly reflects the affected fields of the subject. If the latter is modified,
     * the {@link Map} will have corresponding modifications, but the {@link Map} as such is unmodifiable.</p>
     */
    public final Map<String, Object> map(final T subject) {
        return getterMapper.map(subject);
    }

    /**
     * Remaps an origin {@link Map} to a target instance of the associated {@link Class} and returns that instance.
     */
    public final T remap(final Map<?, ?> origin, final T target) {
        return setterMap.remap(origin, target);
    }

    /**
     * Copies an origin of the associated {@link Class} to a target instance and returns that target instance.
     */
    public final T copy(final T origin, final T target) {
        return remap(map(origin), target);
    }

    /**
     * Represents a preliminary stage of a {@link PropertyMapper}.
     */
    public static final class Stage {

        private final GetterMapper.Stage getterStage;
        private final SetterMapper.Stage setterStage;

        private Stage(final Function<Class<?>, Map<String, Method>> getterMapping,
                      final Function<Class<?>, Map<String, Method>> setterMapping) {
            this.getterStage = GetterMapper.stage(getterMapping);
            this.setterStage = SetterMapper.stage(setterMapping);
        }

        /**
         * <p>Returns a {@link PropertyMapper} that can represent fields of an instance of the given {@link Class} as a
         * {@link Map}.</p>
         */
        public final <T> PropertyMapper<T> apply(final Class<T> type) {
            return new PropertyMapper<T>(getterStage.apply(type), setterStage.apply(type));
        }
    }
}
