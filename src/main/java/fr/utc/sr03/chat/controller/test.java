package fr.utc.sr03.chat.controller;

import redis.clients.jedis.JedisPooled;

public class test {
    public static void main(String[] args){
        JedisPooled jedis = new JedisPooled("localhost", 26379);
        jedis.sadd("planets", "Earth");
    }

}
