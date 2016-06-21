#20160308
    分布式锁实现, 基于Zookeeper实现的互斥锁，共享锁

#单个互斥锁ZookeeperMutexLock
    互斥锁的实现机制是当一个线程获取锁后，另一个线程无法获取该锁，并报出异常，这里的线程可以在不同的JVM中；
    目前支持重入特性，在同一线程中的不同地方（不同的方法中）可以重复获取同一把锁。
    互斥锁的使用场景是当一个线程需要对分布式系统中的某些资源独占，不希望其它线程对该资源进行操作。
    互斥锁使用范例如下代码所示，如果代码lock.lock()获取锁key1锁，则进入独占资源操作代码，否则进入异常处理代码，最后释放锁lock.release()。
    
        MutexLock lock = null;
    	try {
    	    lock = new ZookeeperMutexLock(ProviderConfig.Builder.builder().zk(zk).root("mutex").resId("user1").build()); 
    		lock.lock();
            // 独占资源操作代码
    	} catch (LockException e) {
            // 异常处理代码
        } finally {
    		try {
    		    if(lock!=null) lock.release();
    		} catch (LockException ignored) {}
        }
        
#组合互斥锁ZookeeperGroupMutexLock
    组合互斥锁，当需要同时锁定某个配置下的所有互斥锁的时候使用
    
        MutexLock lock = null;
        try {
            lock = new ZookeeperGroupMutexLock(//
                    ProviderConfig.Builder.builder().zk(zk).root("mutex").build(),//
                    "key1", "key2", "key3"//
                    ); 
            lock.lock();
            // 独占资源操作代码
        } catch (LockException e) {
            // 异常处理代码
        } finally {
            try {
                if(lock!=null) lock.release();
            } catch (LockException ignored) {}
        }
    
#组合互斥锁ZookeeperCombinationMutexLock
    组合互斥锁，将多个互斥锁进行组合, 所有的资源都获得互斥锁的时候，整个组合才能获的互斥锁，否则只要有一个没有获得锁就无法获得组合锁
    
        MutexLock lock = null;
        try {
            List<ProviderConfig> configs = Arrays.asList(//
                    ProviderConfig.Builder.builder().root("/comb/type1/").resId("user1").zk(zk).build(),//
                    ProviderConfig.Builder.builder().root("/comb/type2/").resId("user1").zk(zk).build(),//
                    ProviderConfig.Builder.builder().root("/comb/type3/").resId("user1").zk(zk).build()//
            );
            lock = new ZookeeperCombinationMutexLock(configs); 
            lock.lock();
            // 独占资源操作代码
        } catch (LockException e) {
            // 异常处理代码
        } finally {
            try {
                if(lock!=null) lock.release();
            } catch (LockException ignored) {}
        }
    
#共享锁ZookeeperSharedLock
    共享锁有读锁和写锁两种状态，共享锁的实现机制是当一个线程获取锁的读状态后，另一个线程也可以获取该锁的读状态，
    但是另一个线程无法获取该锁的写状态，并抛出异常；当一个线程获取锁的写状态后，另一个线程无法获取该锁，并抛出异常。
    这里的线程可以在不同的JVM中；目前支持重入特性，在同一线程中的不同地方（不同的方法中）可以重复获取同一把锁。
    共享锁的使用场景是当一个线程需要对分布式系统中的某些资源进行读取操作时，其它线程也可以读取该资源，但是不能进行写操作；
    当一个线程需要对分布式系统中的某些资源进行写操作时，不希望其它线程对该资源进行操作。
    共享锁使用范例如下代码所示，如果代码lock.read()/lock.write()获取锁key1锁，则进入独占资源操作代码，否则进入异常处理代码，
    最后释放锁lock.release()。
        
        SharedLocklock = null;
    	try {
        	lock = new ZookeeperSharedLock(ProviderConfig.Builder.builder().zk(zk).root("mutex").resId("user1").build()); 
    		lock.read();//lock.write();
            // 独占资源操作代码
    	} catch (LockException e) {
            // 异常处理代码
        } finally {
    		try {
    		    if(lock!=null) lock.release();
    	} catch (LockException ignored) {}











    