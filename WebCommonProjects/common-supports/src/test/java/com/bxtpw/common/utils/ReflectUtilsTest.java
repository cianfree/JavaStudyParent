package com.bxtpw.common.utils;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015/12/18 18:26
 * @since 0.1
 */
public class ReflectUtilsTest {

    /**
     * 没有写修饰符的属性
     */
    String defaultField;

    /**
     * 具有private修饰符的属性
     */
    private String privateField;

    /**
     * protected修饰属性
     */
    protected String protectedField;

    /**
     * public修饰符的属性
     */
    public String publicField;

    /**
     * 默认修饰符属性
     */
    void defaultMethod() {
    }

    /**
     * Private 修饰符方法
     */
    private void privateMethod() {
    }

    /**
     * protected method
     */
    protected void protectedMethod() {
    }

    /**
     * public method
     */
    public void publicMethod() {
    }

    public class SubTest extends ReflectUtilsTest {

    }

    @Test
    public void testFindDeclaredField() throws Exception {
        assertNotNull(ReflectUtils.findDeclaredField(SubTest.class, "defaultField"));
        assertNotNull(ReflectUtils.findDeclaredField(SubTest.class, "privateField"));
        assertNotNull(ReflectUtils.findDeclaredField(SubTest.class, "protectedField"));
        assertNotNull(ReflectUtils.findDeclaredField(SubTest.class, "publicField"));
    }

    @Test
    public void testFindPublicField() throws Exception {
        assertNull(ReflectUtils.findPublicField(SubTest.class, "defaultField"));
        assertNull(ReflectUtils.findPublicField(SubTest.class, "privateField"));
        assertNull(ReflectUtils.findPublicField(SubTest.class, "protectedField"));
        assertNotNull(ReflectUtils.findPublicField(SubTest.class, "publicField"));
    }

    @Test
    public void testFindDeclaredMethod() throws Exception {
        assertNotNull(ReflectUtils.findDeclaredMethod(SubTest.class, "defaultMethod"));
        assertNotNull(ReflectUtils.findDeclaredMethod(SubTest.class, "privateMethod"));
        assertNotNull(ReflectUtils.findDeclaredMethod(SubTest.class, "protectedMethod"));
        assertNotNull(ReflectUtils.findDeclaredMethod(SubTest.class, "publicMethod"));
    }

    @Test
    public void testFindPublicMethod() throws Exception {
        assertNull(ReflectUtils.findPublicMethod(SubTest.class, "defaultMethod"));
        assertNull(ReflectUtils.findPublicMethod(SubTest.class, "privateMethod"));
        assertNull(ReflectUtils.findPublicMethod(SubTest.class, "protectedMethod"));
        assertNotNull(ReflectUtils.findPublicMethod(SubTest.class, "publicMethod"));
    }
}