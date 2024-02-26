package com.nus.team4.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> stringRedisTemplate;

    /**
     * 设置键值
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        try {
            stringRedisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param key   String
     * @param value String
     * @param time  Long 超时时间
     * @return
     */
    public boolean set(String key, String value, Long time) {
        try {
            if (time >= 0) {
                stringRedisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param key   String
     * @param value String
     * @param time  Long 超时时间
     */
    public void set(String key, String value, Long time, TimeUnit timeUnit) {
        try {
            if (time >= 0) {
                stringRedisTemplate.opsForValue().set(key, value, time, timeUnit);
            } else {
                set(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加set
     *
     * @param key   String
     * @param value String
     */
    public void addSet(String key, Object value) {
        stringRedisTemplate.opsForSet().add(key, value);
    }


    /**
     * 根据时间和单位设置过期时间
     *
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public Boolean expire(String key, Long timeout, TimeUnit unit) {
        return stringRedisTemplate.expire(key, timeout, unit);
    }

    public long getExpire(String key) {
        return stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
    }
    /**
     * 添加set
     *
     * @param key   String
     * @param value String
     * @param time  Long
     */
    public void addSet(String key, String value, Long time) {
        addSet(key, value);
        if (time >= 0) {
            stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }


    /**
     * 检查给定的元素是否在变量中。
     *
     * @param key String
     * @param o   Object
     * @return Boolean
     */
    public Boolean isMember(String key, Object o) {
        return stringRedisTemplate.opsForSet().isMember(key, o);
    }

    /**
     * 获取set中的值
     */
    public Set<Object> members(String key) {
        return stringRedisTemplate.opsForSet().members(key);
    }

    /**
     * 获取set中的值
     * @return
     */
    public Long size(String key) {
        return stringRedisTemplate.opsForSet().size(key);
    }

    /**
     * 删除set中的值
     */
    public void remove(String key, Object... values) {
        stringRedisTemplate.opsForSet().remove(key, values);
    }


    /**
     * 获取指定键值
     *
     * @param key
     * @return
     */
    public String get(String key) {
        Object o = stringRedisTemplate.opsForValue().get(key);
        if (null == o) {
            return null;
        }
        return String.valueOf(o);
    }

    public Object getObject(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 删除某个键
     *
     * @param key
     * @return
     */
    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 推入列表
     */
    public void listLPush(String key, String value) {
        stringRedisTemplate.opsForList().leftPush(key, value);
    }

    public List getList(String key, int start, int end) {
        return (List) stringRedisTemplate.opsForList().range(key, start, end); // 获取列表数据
    }

}