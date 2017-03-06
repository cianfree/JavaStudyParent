package com.github.cianfree.test;

import org.hibernate.Session;

/**
 * 支持事务的
 *
 * @author Arvin
 * @time 2017/2/14 16:43
 */
public abstract class HTInvoker {

    public HTInvoker(Session session) {
        try {
            // 开启事务
            session.beginTransaction();
            invoke(session);
        } catch (Exception e) {
            // 事务回滚
            System.out.println("事务回滚！");
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public abstract void invoke(Session session);

}
