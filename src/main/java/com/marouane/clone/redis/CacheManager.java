package com.marouane.clone.redis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheManager {

    private final Map<String, String> cache = new ConcurrentHashMap<>();

    private static volatile CacheManager instance;

    private CacheManager() {

    }

    public static CacheManager getInstance() {

        if (instance == null) {
            synchronized (CacheManager.class) {

                if (instance == null) {
                    instance = new CacheManager();
                }
            }
        }
        return instance;
    }

    public void put(String key, String value) {
        cache.put(key, value);
    }

    public String get(String key) {
        return cache.get(key);
    }
}
