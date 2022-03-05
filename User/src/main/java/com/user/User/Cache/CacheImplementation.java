package com.user.User.Cache;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
@Component
public class CacheImplementation implements Cache
{
    private Jedis jedis;
    CacheImplementation()
    {
        this.jedis=new Jedis();
    }

    public Jedis getJedis() {
        return jedis;
    }
}

