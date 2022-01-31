package org.example.activej.modules.common.redis;

import io.activej.promise.Promise;

public interface Redis {

    Promise<String> getString(String key);

    Promise<Void> putString(String key, String value);

    Promise<Void> putString(String key, String value, Integer ttl);

}
