package org.example.currency_exchanger.context;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

    @Getter
    private static final ApplicationContext context = new ApplicationContext();

    private final Map<Class<?>, Object> beans = new HashMap<>();

    private ApplicationContext() {}

    public <T> void register(Class<T> clazz, T implementation) {
        beans.put(clazz, implementation);
    }

    public <T> T get(Class<T> clazz) {
        Object impl = beans.get(clazz);
        if (impl == null) {
            throw new IllegalStateException("No bean registered for " + clazz.getName());
        }
        return clazz.cast(impl);
    }

}
