package com.github.cianfree.test;

import com.alibaba.fastjson.JSON;
import com.github.cianfree.model.Admin;
import com.github.cianfree.model.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author Arvin
 * @time 2017/2/14 15:58
 */
public class HibernateHelloTest {

    /** 共享sessionFactory */
    protected SessionFactory sessionFactory;

    @Before
    public void setUp() {
        // SessionFactory 注册中心，应用只需要注册一遍
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml") // 标准的classpath路径下寻找hibernate的配置文件，默认是 hibernate.cfg.xml
                .build();

        try {
            // 构建sessionFactory
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    @Test
    public void testStudent() {

        // 开启一个会话
        Session session = sessionFactory.openSession();

        // 开启事务
        session.beginTransaction();

        session.save(new Student(10L, "Arvin", 27));
        session.save(new Student("Cian", 27));

        // 提交事务
        session.getTransaction().commit();
        session.close();
    }

    @Test
    public void testAdmin() {

        new HTInvoker(sessionFactory.openSession()) {
            @Override
            public void invoke(Session session) {
                session.save(new Admin("Arvin", 27));
                session.save(new Admin("Cian", 27));
            }
        };
    }

    @Test
    public void testSelectStudent() {
        new HInvoker(sessionFactory.openSession()) {
            @Override
            public void invoke(Session session) {
                List<Student> studentList = session.createQuery("FROM " + Student.class.getName()).list();
                System.out.println(JSON.toJSONString(studentList));
            }
        };
    }

    @Test
    public void testSelectAdmin() {
        new HInvoker(sessionFactory.openSession()) {
            @Override
            public void invoke(Session session) {
                List<Admin> adminList = session.createQuery("FROM " + Admin.class.getName()).list();
                System.out.println(JSON.toJSONString(adminList));
            }
        };
    }

}
