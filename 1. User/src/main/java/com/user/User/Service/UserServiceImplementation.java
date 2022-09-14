package com.user.User.Service;
import com.user.User.Cache.CacheImplementation;
import com.user.User.Model.User;
import com.user.User.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {
    Logger logger = LoggerFactory.getLogger(UserServiceImplementation.class);

    @Autowired
    private CacheImplementation cacheClient;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        logger.info("getAllUsers is executed  ");
        return userRepository.findAll();
    }

    public Optional<User> findUserById(int id) {
        try {
            /*  redis -server & jedis - client  */
            logger.info("Request for findUserById for id ={} is executed ", id);
            Jedis jedis = cacheClient.getJedis();
            String key = "users" +id;
            //if HIT -
            //check CACHE - User Found,Return it.
            // hGetAll(key) Returns all fields and values of the hash stored at key
            // Time Complexity is O(1) which is far better than hitting mysql & Scanning all the rows .
            Map<String, String> userMapFromCache = jedis.hgetAll(key);
            if (userMapFromCache.size() != 0) {
                //construct the user object
                // Set Values from Cache to User Object & return it .
                User user = new User();
                user.setId(id);
                user.setEmail(userMapFromCache.get("email"));
                user.setMobile(userMapFromCache.get("mobile"));
                user.setName(userMapFromCache.get("name"));
                user.setKyc_flag(Integer.parseInt(userMapFromCache.get("kyc_flag")));
                logger.info("Data is returned from cache : {}", user.toString());
                return Optional.of(user);
            } else                                                  // Going to be Executed iN First hit
            {
//            if MISS -
//                User not found in Cache , Go to MySql - Database ,
//                Add particular entry to cache , then return it
                Optional<User> user = userRepository.findById(id);
                if (user != null) {
                    jedis.hset(key,"name",user.get().getName());
                    jedis.hset(key,"email",user.get().getEmail());
                    jedis.hset(key,"mobile",user.get().getMobile());
                    jedis.hset(key,"kyc_flag", String.valueOf(user.get().getKyc_flag()));
                }
                logger.info("Data is returned from MySQL : {}", user.toString());
                return user;
            }
        } catch (Exception exception) {
            //new UserNotFoundException()
            System.out.println(exception);
            logger.error("User not found with request id : {}", id);
            return null;
        }

    }
}
