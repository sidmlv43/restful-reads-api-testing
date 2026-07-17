package com.restfulReads.session;

import com.restfulReads.enums.UserType;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public final class UserPool {

    private static final Map<UserType, BlockingQueue<User>> USER_POOLS = new ConcurrentHashMap<>();

    private UserPool() {

    }

    public static void register(User user) {

        USER_POOLS.computeIfAbsent(
                        user.getUserType(),
                        key -> new LinkedBlockingQueue<>()
                )
                .offer(user);
    }

    public static User acquire(UserType userType) {

        BlockingQueue<User> pool = USER_POOLS.get(userType);

        if (pool == null) {
            throw new RuntimeException(String.format("No pool found for user type: %s", userType));
        }

        try {
            User user = pool.poll(60, TimeUnit.SECONDS);

            if (user == null) {
                throw new RuntimeException(String.format("Could not acquire a %s user within timeout", userType));
            }

            return user;

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new RuntimeException("Interrupted while waiting for user", e);
        }
    }

    public static boolean release(User user) {

        if (user == null) {
            return false;
        }

        BlockingQueue<User> pool = USER_POOLS.get(user.getUserType());

        if (pool == null) {
            return false;
        }

        return pool.offer(user);
    }

    public static int availableUsers(UserType userType) {

        BlockingQueue<User> pool = USER_POOLS.get(userType);

        return pool == null
                ? 0
                : pool.size();
    }

    public static void clear() {
        USER_POOLS.clear();
    }
}