package com.flexingstudios.FlexingNetwork.api.util;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class EntityPersistor<T extends Entity> {

    /**
     * Wraps an Entity object with a proxy which will attempt to ensure the Entity object remains valid
     * even if the entity's chunk is unloaded, then loaded again. Helpful if you need a reference to an
     * Entity over a long period of time which must not be broken. Note that any wrapped Entity will not
     * interact with {@link Object#equals(Object)} reflexively. You must call .equals() on the Entity
     * which has been wrapped, not on another Entity comparing it to this one. This could not be avoided,
     * unfortunately, but as long as you are aware of that, it should work fine.
     * Seems to break in 1.8 because of API fuckery. Use {@link EntityPersistor#wrap(Entity)}
     * @param entity The Entity to wrap
     * @param <T> The type of the Entity
     * @return The wrapped Entity
     */
    public static <T extends Entity> T persist(T entity) {
        Class<?> clazz = entity.getClass();
        boolean foundInterface = false;
        for (Class<?> iface : clazz.getInterfaces()) {
            if (Entity.class.isAssignableFrom(iface)) {
                clazz = iface;
                foundInterface = true;
                break;
            }
        }
        if (!foundInterface) {
            throw new IllegalArgumentException("The provided object cannot be wrapped!");
        }
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {

            private T instance = entity;

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (!instance.isValid()) {
                    T replace = (T) Bukkit.getEntity(instance.getUniqueId());
                    if (replace != null) {
                        instance = replace;
                    }
                }
                if (method.getName().equals("equals") && method.getParameters().length == 1 && method.getParameters()[0].getName().equals("Object")) {
                    if (args[0] instanceof Entity) {
                        return ((Entity) args[0]).getUniqueId().equals(instance.getUniqueId());
                    }
                    return false;
                }
                return method.invoke(instance, args);
            }

        });
    }

    /**
     * Wraps an Entity in an EntityPersistor. Calling {@link EntityPersistor#get()} will refresh the reference
     * if it is invalid. Use for 1.8
     * @param entity The Entity to wrap
     * @param <T> The type of the Entity
     * @return An EntityPersistor wrapping the given Entity
     */
    public static <T extends Entity> EntityPersistor<T> wrap(T entity) {
        return new EntityPersistor<T>(entity);
    }

    private T entity;

    private EntityPersistor(T entity) {
        this.entity = entity;
    }

    /**
     * Gets the Entity held in this EntityPersistor. If the reference is invalid, the EntityPersistor will attempt
     * to refresh it.
     * @return The wrapped Entity
     */
    public T get() {
        refresh:
        if (!entity.isValid()) {
            for (Entity entity : this.entity.getLocation().getChunk().getEntities()) {
                if (entity.getUniqueId().equals(this.entity.getUniqueId())) {
                    this.entity = (T) entity;
                    break refresh;
                }
            }
            for (World world : Bukkit.getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (this.entity.getUniqueId().equals(entity.getUniqueId())) {
                        this.entity = (T) entity;
                        break;
                    }
                }
            }
        }
        return entity;
    }

}
