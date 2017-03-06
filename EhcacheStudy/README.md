#Ehcache
    学习Ehcache 2.10.x
    
#ehcache.xml
    默认会在classpath下寻找这个文件，如果没有就在classpath下寻找 ehcache-failsafe.xml, 这个在ehcache发布的jar包中有

#Ehcahe的三个存储层
Ehcache has three storage tiers, summarized here:
## Memory store
    Heap memory that holds a copy of the hoest subset of data from the off-heap store. Subject to Java GC.
## Off-heap store
    Limited in size only by available RAM. Not subject to Java GC. Can store serialized data only. Provides overflow capacity to the memory store.
## Disk store
    Backs up in-memory data and provides overflow capacity to the other tiers. Can store serialized data only.

#cache配置参数解析
##name属性配置
    给这个缓存进行配置，名字唯一，必填项
##过期参数配置
###timeToLiveSeconds
    过期时间，单位秒，即缓存的项在缓存中存活的时间，不管有没有命中，当在缓存中达到了这个时间的时候就会被踢出缓存，默认是0，表示永不过期
###timeToIdleSeconds
    未被访问的空闲时间，单位秒，即缓存的项如果在该时间内一直没有被命中（即被访问），就会从缓存中删除，默认是0，表示永不过期
###eternal
    永恒的，不变的，如果该属性设置了true，那么timeToLiveSeconds和timeToIdleSeconds将无效，将永不过期
##本地存储属性
###maxEntriesLocalHeap
    堆内存中最大缓存对象数，0没有限制
###maxBytesLocalHeap
    堆内存中最大缓存字节数，如果设置了这个属性，maxEntriesLocalHeap将不能被利用
###maxEntriesLocalDisk
    磁盘中的最大对象数，默认为0不限制
###maxBytesLocalDisk
    磁盘中最大缓存的字节数，如果设置了这个属性，maxEntriesLocalDisk将无效
##通过副本而不是引用获取缓存对象（Passing Copies Instead of References）
    默认情况下，get()的操作返回的是缓存对象的引用，获取到的对象能够实时修改，如果你禁止get后进行修改，那么可以通过设置，使得get操作返回的是
    对象的副本，这样对原始对象就没有破坏了。
    
    当使用copyOnRead=true或copyOnWrite=true的时候，对象复制的策略就启用了，默认的实现方式是通过序列化机制，当然，你也可以提供自己的序
    列化机制，通过实现 net.sf.ehcache.store.compound.ReadWriteCopyStrategy接口提供自己的复制策略，然后配置：
    ------------------------------------------------------------------------------------------------
        <cache name="sampleCache1"
            ...
            >
            <!-- 配置策略，注意要放在第一个child属性 -->
            <copyStrategy class="edu.zhku.ehcache.strategy.MyCopyAndWriteStrategy"/>
        </cache>
    ------------------------------------------------------------------------------------------------
    需要注意的是，这里的copyStrategy使用的是单例，因此你缓存的对象必须是线程安全的。    
    
###copyOnRead
    在读取缓存的时候，访问的是缓存对象的副本，默认是false
###copyOnWrite
    在写入到缓存的时候，会创建要写入的对象的副本进行缓存，默认是false
## Memory Store Config
    内存存储层配置，内存存储具有以下特点：
    (1) Accepts all data, whether serializable or not
    (2) Fastest storage option
    (3) Thread safe for use by multiple concurrent threads
### memoryStoreEvictionPolicy
    内存缓存过期策略，包含三种策略(默认是LRU， 最近最少使用)：
    (1) Least Recently Used (LRU)—LRU is the default seing. The last-used timestamp is
    updated when an element is put into the cache or an element is retrieved from the
    cache with a get call.
    
    (2) Least Frequently Used (LFU) —For each get call on the element the number of hits is
    updated. When a put call is made for a new element (and assuming that the max
    limit is reached for the memory store) the element with least number of hits, the Less
    Frequently Used element, is evicted.
    
    (3) First In First Out (FIFO) — Elements are evicted in the same order as they come in.
    When a put call is made for a new element (and assuming that the max limit is
    reached for the memory store) the element that was placed first (First-In) in the store
    is the candidate for eviction (First-Out).
    
    



















    
    