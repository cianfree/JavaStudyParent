package edu.zhku.ehcache;

import edu.zhku.domain.User;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Arvin on 2016/7/11.
 */
public class EhcacheTest {

    private CacheManager cacheManager;

    private Cache cache;

    @Before
    public void ready() {
        cacheManager = CacheManager.create(getClass().getClassLoader().getResourceAsStream("ehcache.xml"));
        System.out.println(cacheManager);

        cache = cacheManager.getCache("sampleCache1");
        System.out.println(cache);
    }

    @After
    public void destroy() {
        cacheManager.shutdown();
    }

    @Test
    public void testEhcache() {
        // 放一个元素到cache中
        Element element = new Element("1", new User(1, "Arvin", 25));
        cache.put(element);

        // 获取缓存中的对象
        Element elt = cache.get("1");
        Object obj = elt.getObjectValue();
        if (obj instanceof User) {
            System.out.println("存储的是User对象");
        } else {
            System.out.println("存储的不是User对象");
        }
        System.out.println(elt.getObjectValue());
    }
}
