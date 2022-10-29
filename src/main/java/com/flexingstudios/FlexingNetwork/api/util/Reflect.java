package com.flexingstudios.FlexingNetwork.api.util;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.reflect.ReflectionFactory;

public class Reflect {
    private static final Map<String, ClassData> cache = new ConcurrentHashMap<>();

    public static <T extends Enum<?>> T addEnum(Class<T> enumType, String name, Object... args) {
        try {
            ClassData<T> data = getClass(enumType);
            Field field = null;
            try {
                field = data.findFinalField("$VALUES");
            } catch (UnableToFindFieldException ex) {
                try {
                    field = data.findFinalField("ENUM$VALUES");
                } catch (UnableToFindFieldException unableToFindFieldException) {}
            }

            if (field == null) {
                int flags = 4120;
                String valueType = "[L" + enumType.getName().replace('.', '/') + ";";
                for (Field f : enumType.getDeclaredFields()) {
                    if ((f.getModifiers() & flags) == flags && f.getType().getName().replace('.', '/').equals(valueType)) {
                        field = f;
                        field.setAccessible(true);
                        ClassData.FIELD_MODIFIERS.set(field, field.getModifiers() & 0xFFFFFFEF);
                        break;
                    }
                }
            }
            if (field == null)
                throw new UnableToFindFieldException(enumType, "$VALUES");
            Enum[] arrayOfEnum = (Enum[])field.get(null);
            List<T> values = new ArrayList<>(Arrays.asList((T[])arrayOfEnum));
            Object[] params = new Object[args.length + 2];
            params[0] = name;
            params[1] = values.size();
            System.arraycopy(args, 0, params, 2, args.length);

            ReflectionFactory rFactory = ReflectionFactory.getReflectionFactory();
            Enum enum_ = (Enum)rFactory.newConstructorAccessor(data.findConstructor(params)).newInstance(params);
            values.add((T)enum_);
            field.set(null, values.toArray((Enum[])Array.newInstance(enumType, 0)));
            setFinal(Class.class, enumType, "enumConstants", null);
            setFinal(Class.class, enumType, "enumConstantDirectory", null);
            return (T)enum_;
        } catch (Exception e) {
            error(e, "addEnum error");

            return null;
        }
    }

    public static <E> E construct(Class<E> clazz, Object... args) {
        try {
            return getClass(clazz).construct(args);
        } catch (Exception e) {
            error(e, "Constructor error");
            return null;
        }
    }

    public static <E> E get(Class<?> clazz, String field) {
        try {
            return (E)getClass(clazz).get(null, field);
        } catch (Exception e) {
            error(e, "Get static field error");
            return null;
        }
    }

    public static <R> R get(Object instance, String field) {
        try {
            return (R) getClass(instance.getClass()).get(instance, field);
        } catch (Exception e) {
            error(e, "Get field error");
            return null;
        }
    }

    public static <T, E> E get(Class<T> clazz, T instance, String field) {
        try {
            return (E)getClass(clazz).get(instance, field);
        } catch (Exception e) {
            error(e, "Get field error");
            return null;
        }
    }

    public static void set(Class<?> clazz, String field, Object value) {
        try {
            getClass(clazz).set(null, field, value);
        } catch (Exception e) {
            error(e, "Set static field error");
        }
    }

    public static void set(Object instance, String field, Object value) {
        try {
            getClass(instance.getClass()).set(instance, field, value);
        } catch (Exception e) {
            error(e, "Set field error");
        }
    }

    public static <T> void set(Class<T> clazz, T instance, String field, Object value) {
        try {
            getClass(clazz).set(instance, field, value);
        } catch (Exception e) {
            error(e, "Set field error");
        }
    }

    public static void setFinal(Class<?> clazz, String field, Object value) {
        try {
            getClass(clazz).setFinal(null, field, value);
        } catch (Exception e) {
            error(e, "Set static final field error");
        }
    }

    public static void setFinal(Object instance, String field, Object value) {
        try {
            getClass(instance.getClass()).setFinal(instance, field, value);
        } catch (Exception e) {
            error(e, "Set final field error");
        }
    }

    public static <T> void setFinal(Class<T> clazz, T instance, String field, Object value) {
        try {
            getClass(clazz).setFinal(instance, field, value);
        } catch (Exception e) {
            error(e, "Set final field error");
        }
    }

    public static <E> E invoke(Class<?> clazz, String method, Object... args) {
        try {
            return (E)getClass(clazz).invoke(null, method, args);
        } catch (Throwable e) {
            error(e, "Invoke static error");
            return null;
        }
    }

    public static <E> E invoke(Object instance, String method, Object... args) {
        try {
            return (E)getClass(instance.getClass()).invoke(instance, method, args);
        } catch (Throwable e) {
            error(e, "Invoke error");
            return null;
        }
    }

    public static <T, E> E invoke(Class<T> clazz, T instance, String method, Object... args) {
        try {
            return (E)getClass(clazz).invoke(instance, method, args);
        } catch (Throwable e) {
            error(e, "Invoke error");
            return null;
        }
    }

    public static <T> boolean isConstructorExist(Class<T> clazz, Class... args) {
        return findConstructor(clazz, args) != null;
    }

    public static <T> boolean isMethodExist(Class<T> clazz, String method, Class... args) {
        return findMethod(clazz, method, args) != null;
    }

    public static <T> boolean isFieldExist(Class<T> clazz, String field) {
        return findField(clazz, field) != null;
    }

    public static <T> Constructor<T> findConstructor(Class<T> clazz, Class... args) {
        try {
            return getClass(clazz).findConstructor0(args);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static <T> Method findMethod(Class<T> clazz, String method, Class... args) {
        try {
            return getClass(clazz).findMethod0(method, args);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static <T> Field findField(Class<T> clazz, String field) {
        try {
            return getClass(clazz).findField(field);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static <T> Field findFinalField(Class<T> clazz, String field) {
        try {
            return getClass(clazz).findFinalField(field);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static Class<?> findClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException ignored) {
            return null;
        }
    }

    public static void setAggressiveMethodsOverloading(Class<?> clazz, boolean flag) {
        ClassData<?> data = getClass(clazz);
        if (data.aggressiveOverloading != flag) {
            data.aggressiveOverloading = flag;
            data.methods.clear();
        }
    }

    public static MethodHandles.Lookup lookup() {
        return get(MethodHandles.Lookup.class, "IMPL_LOOKUP");
    }

    private static <T> ClassData<T> getClass(Class<T> clazz) {
        ClassData<T> data = cache.get(clazz.getName());
        if (data == null)
            cache.put(clazz.getName(), data = new ClassData<>(clazz));

        return data;
    }

    private static void error(Throwable e, String message) {
        Logger.getLogger("Reflect").log(Level.SEVERE, message, e);
    }

    static class ClassData<K> {
        private static Field FIELD_MODIFIERS = null;
        private final Class<K> clazz;

        static {
            try {
                FIELD_MODIFIERS = Field.class.getDeclaredField("modifiers");
                FIELD_MODIFIERS.setAccessible(true);
            } catch (Exception ex) {
                Reflect.error(ex, "Field modifiers field not found");
            }
        }

        private final Map<String, Field> fields = new HashMap<>();
        private final Map<Object, Method> methods = new HashMap<>();
        private final Map<Object, Constructor<K>> constructors = new HashMap<>();
        boolean aggressiveOverloading = false;

        public ClassData(Class<K> clazz) {
            this.clazz = clazz;
        }

        void set(Object instance, String field, Object value) throws Exception {
            findField(field).set(instance, value);
        }

        void setFinal(Object instance, String field, Object value) throws Exception {
            findFinalField(field).set(instance, value);
        }

        Object get(Object instance, String field) throws Exception {
            return findField(field).get(instance);
        }

        Object invoke(Object instance, String method, Object... args) throws Throwable {
            return findMethod(method, args).invoke(instance, args);
        }

        K construct(Object... args) throws Exception {
            return findConstructor(args).newInstance(args);
        }

        Constructor<K> findConstructor(Object... args) {
            return findConstructor0(toTypes(args));
        }

        Constructor<K> findConstructor0(Class... types) {
            Object mapped = new Reflect.ConstructorMapKey(types);
            Constructor<K> con = constructors.get(mapped);
            if (con == null) {
                Constructor[] arrayOfConstructor;
                int i;
                byte b;
                label25: for (arrayOfConstructor = clazz.getDeclaredConstructors(), i = arrayOfConstructor.length, b = 0; b < i;) {
                    Constructor<K> c = arrayOfConstructor[b];
                    Class<?>[] ptypes = c.getParameterTypes();
                    if (ptypes.length != types.length) {
                        b++;
                        continue;
                    }

                    for (int j = 0; j < ptypes.length; j++) {
                        if (types[j] != null && ptypes[j] != types[j] && !ptypes[j].isAssignableFrom(types[j]))
                            continue label25;
                    }
                    con = c;
                    con.setAccessible(true);
                    constructors.put(mapped, con);
                }

                if (con == null)
                    throw new Reflect.UnableToFindConstructorException(clazz, types);
            }

            return con;
        }

        Method findMethod(String name, Object... args) {
            Object mapped;
            Class[] types = null;
            if (aggressiveOverloading) {
                types = toTypes(args);
                mapped = new Reflect.AggressiveMethodMapKey(name, types);
            } else {
                mapped = new Reflect.MethodMapKey(name, args.length);
            }
            Method method = methods.get(mapped);
            if (method == null) {
                if (types == null)
                    types = toTypes(args);
                method = fastFindMethod(name, types);
                if (method == null)
                    throw new Reflect.UnableToFindMethodException(clazz, name, types);
                methods.put(mapped, method);
            }

            return method;
        }

        Method findMethod0(String name, Class... types) {
            Object mapped;
            if (aggressiveOverloading) {
                mapped = new Reflect.AggressiveMethodMapKey(name, types);
            } else {
                mapped = new Reflect.MethodMapKey(name, types.length);
            }
            Method method = methods.get(mapped);
            if (method == null) {
                method = fastFindMethod(name, types);
                if (method == null)
                    throw new Reflect.UnableToFindMethodException(clazz, name, types);
                methods.put(mapped, method);
            }

            return method;
        }

        private Method fastFindMethod(String name, Class... types) {
            Method method = null;
            name = name.intern();
            Class<K> clazz0 = clazz;
            do {
                label30: for (Method m : clazz0.getDeclaredMethods()) {
                    if (name == m.getName()) {
                        Class<?>[] ptypes = m.getParameterTypes();
                        if (ptypes.length == types.length) {
                            for (int i = 0; i < ptypes.length; i++) {
                                if (types[i] != null && ptypes[i] != types[i] && !ptypes[i].isAssignableFrom(types[i]))
                                    continue label30;
                            }
                            method = m;
                            break;
                        }
                    }
                }

                if (method != null) {
                    method.setAccessible(true);
                    break;
                }
                clazz0 = (Class)clazz0.getSuperclass();
            } while (clazz0 != null);

            return method;
        }

        Field findFinalField(String name) throws Exception {
            Field field = findField(name);
            FIELD_MODIFIERS.set(field, field.getModifiers() & 0xFFFFFFEF);

            return field;
        }

        Field findField(String name) {
            Field field = fields.get(name);
            if (field == null) {
                Class<K> clazz0 = clazz;
                while (clazz0 != null) {
                    try {
                        field = clazz0.getDeclaredField(name);
                        field.setAccessible(true);
                        fields.put(name, field);
                        break;
                    } catch (Exception e) {
                        clazz0 = (Class) clazz0.getSuperclass();
                    }
                }
                if (field == null)
                    throw new Reflect.UnableToFindFieldException(clazz, name);
            }

            return field;
        }

        private Class[] toTypes(Object[] objects) {
            if (objects.length == 0)
                return new Class[0];
            Class[] types = new Class[objects.length];
            for (int i = 0; i < objects.length; i++) {
                if (objects[i] == null) {
                    types[i] = null;
                } else {
                    Class<?> type = objects[i].getClass();
                    if (type == Integer.class) {
                        type = int.class;
                    } else if (type == Double.class) {
                        type = double.class;
                    } else if (type == Boolean.class) {
                        type = boolean.class;
                    } else if (type == Float.class) {
                        type = float.class;
                    } else if (type == Long.class) {
                        type = long.class;
                    } else if (type == Character.class) {
                        type = char.class;
                    } else if (type == Byte.class) {
                        type = byte.class;
                    } else if (type == Short.class) {
                        type = short.class;
                    }
                    types[i] = type;
                }
            }

            return types;
        }
    }

    static class ConstructorMapKey {
        Class[] types;

        public ConstructorMapKey(Class[] types) {
            this.types = types;
        }

        public int hashCode() {
            return Arrays.hashCode(types);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Reflect.AggressiveMethodMapKey))
                return false;
            Reflect.AggressiveMethodMapKey other = (Reflect.AggressiveMethodMapKey)obj;
            if (types.length != other.types.length)
                return false;

            for (int i = 0; i < types.length; i++) {
                if (types[i] != other.types[i])
                    return false;
            }

            return true;
        }
    }

    static class MethodMapKey {
        String name;
        int args;

        public MethodMapKey(String name, int args) {
            this.name = name;
            this.args = args;
        }

        public int hashCode() {
            return name.hashCode() + args;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof MethodMapKey))
                return false;
            MethodMapKey other = (MethodMapKey)obj;

            return other.args == args && other.name.equals(name);
        }
    }

    static class AggressiveMethodMapKey {
        Class[] types;
        String name;

        public AggressiveMethodMapKey(String name, Class[] types) {
            this.name = name;
            this.types = types;
        }

        public int hashCode() {
            int hash = name.hashCode();
            hash = 31 * hash + Arrays.hashCode(types);

            return hash;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof AggressiveMethodMapKey))
                return false;
            AggressiveMethodMapKey other = (AggressiveMethodMapKey)obj;
            if (types.length != other.types.length ||
                    !other.name.equals(name))
                return false;

            for (int i = 0; i < types.length; i++) {
                if (types[i] != other.types[i])
                    return false;
            }

            return true;
        }
    }

    private static String classesToString(Class[] classes) {
        int iMax = classes.length - 1;
        if (iMax == -1)
            return "()";

        StringBuilder b = new StringBuilder();
        b.append('(');

        for (int i = 0;; i++) {
            b.append(classes[i].getName());
            if (i == iMax)
                return b.append(')').toString();
            b.append(',');
        }
    }

    private static class UnableToFindFieldException extends RuntimeException {
        private String fieldName;
        private String className;

        public UnableToFindFieldException(Class clazz, String fieldName) {
            this.fieldName = fieldName;
            className = clazz.getName();
        }

        public String getMessage() {
            return toString();
        }

        public String toString() {
            return "Unable to find field '" + fieldName + "' in class '" + className + "'";
        }
    }

    private static class UnableToFindMethodException extends RuntimeException {
        protected String methodName;
        protected String className;
        protected Class[] types;

        public UnableToFindMethodException(Class clazz, String methodName, Class[] types) {
            this.methodName = methodName;
            className = clazz.getName();
            this.types = types;
        }

        public String getMessage() {
            return toString();
        }

        public String toString() {
            return "Unable to find method '" + className + "." + methodName + Reflect.classesToString(types) + "'";
        }
    }

    private static class UnableToFindConstructorException extends UnableToFindMethodException {
        public UnableToFindConstructorException(Class clazz, Class[] types) {
            super(clazz, null, types);
        }

        public String toString() {
            return "Unable to find constructor '" + className + ".<init>" + Reflect.classesToString(types) + "'";
        }
    }
}
