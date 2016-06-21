package edu.zhku.hessian.auth;

import com.caucho.hessian.client.HessianConnectionFactory;
import com.caucho.hessian.client.HessianProxyFactory;

/**
 * Created by Arvin on 2016/6/7.
 */
public class MyHessianProxyFactory extends HessianProxyFactory {

    public MyHessianProxyFactory() {
        super();
    }

    public MyHessianProxyFactory(ClassLoader loader) {
        super(loader);
    }

    @Override
    protected HessianConnectionFactory createHessianConnectionFactory() {
        String className
                = System.getProperty(HessianConnectionFactory.class.getName());

        HessianConnectionFactory factory = null;

        try {
            if (className != null) {
                ClassLoader loader = Thread.currentThread().getContextClassLoader();

                Class<?> cl = Class.forName(className, false, loader);

                factory = (HessianConnectionFactory) cl.newInstance();

                return factory;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 关键是这里，我们使用自己的ConnectionFactory
        return new MyHessianConnectionFactory();
    }
}
