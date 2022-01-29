package org.example.activej.modules.common.redis;

import io.activej.promise.Promise;

public interface Redis {

    Promise<String> getString(String key);

    Promise<Void> putSting(String key, String value);

    Promise<Void> putSting(String key, String value, Integer ttl);

}
