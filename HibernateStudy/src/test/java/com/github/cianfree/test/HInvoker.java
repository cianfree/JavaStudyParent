package com.github.cianfree.test;

import org.hibernate.Session;

/**
 * @author Arvin
 * @time 2017/2/14 16:43
 */
public abstract class HInvoker {

    public HInvoker(Session session) {
        try {
            invoke(session);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public abstract void invoke(Session session);

}
