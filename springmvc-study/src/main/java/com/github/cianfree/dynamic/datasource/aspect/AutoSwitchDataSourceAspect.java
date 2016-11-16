package com.github.cianfree.dynamic.datasource.aspect;

import com.github.cianfree.dynamic.datasource.DataSourceSwitch;
import com.github.cianfree.dynamic.datasource.DataSourceType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * 自动切换当前线程数据源切面，根据方法的前缀，如果是以insert， add，update，del，modify，change，remove开头的方法，都会被拦截
 *
 * @author Arvin
 * @time 2016/11/16 11:44
 */
//@Aspect
//@Component
public class AutoSwitchDataSourceAspect {


    public static final String updateMethodRegex = "(?i)(add.*)|(update.*)|(insert.*)|(modify.*)|(change.*)|(remove.*)|(del.*)";

    public AutoSwitchDataSourceAspect() {
        System.out.println("AutoSwitchDataSourceAspect 切面启动了。。。");
    }

    /**
     * 拦截所有的JdbcTemplate的方法
     *
     * @param pjp
     * @return
     */
    //@Around("execution(* org.springframework.jdbc.core.JdbcTemplate.*(..)))")
    public Object interceptAllJdbcTemplateMethod(ProceedingJoinPoint pjp) throws Throwable {

        // 获取目标方法
        Method targetMethod = getTargetMethod(pjp);

        // 检查方法名称
        String methodName = targetMethod.getName();

        if (methodName.matches(updateMethodRegex)) {
            DataSourceSwitch.switchDataSource(DataSourceType.LOCAL);
        } else {
            DataSourceSwitch.switchDataSource(DataSourceType.TEST);
        }

        return pjp.proceed();

    }

    private Method getTargetMethod(ProceedingJoinPoint pjp) {
        Object[] args = pjp.getArgs();
        Class[] argTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null) {
                argTypes[i] = args[i].getClass();
            }
        }
        try {
            return pjp.getTarget().getClass().getMethod(pjp.getSignature().getName(), argTypes);
        } catch (NoSuchMethodException e) {
            Method[] targetMethods = pjp.getTarget().getClass().getMethods();
            for (int i = 0; i < targetMethods.length; i++) {
                if (!targetMethods[i].getName().equals(pjp.getSignature().getName())) {
                    continue;
                }
                if (classIsAssignable(targetMethods[i].getParameterTypes(), argTypes)) {
                    return targetMethods[i];
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Invalid method !");
    }

    public boolean classIsAssignable(Class[] source, Class[] target) {
        if (source.length != target.length) {
            return false;
        }
        for (int i = 0; i < source.length; i++) {
            if (target[i] != null && !checkIsAssignableFrom(source[i], target[i])) {
                return false;
            }
        }
        return true;
    }

    private boolean checkIsAssignableFrom(Class source, Class target) {
        String[] normalTypes = new String[]{"boolean", "byte", "char", "short", "int", "long", "float", "double"};
        String[] encapsulationTypes = new String[]{"java.lang.Boolean", "java.lang.Byte", "java.lang.Character", "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.lang.Float", "java.lang.Double"};
        List<String> normalList = Arrays.asList(normalTypes);
        if (normalList.contains(source.getName())) {
            return encapsulationTypes[normalList.indexOf(source.getName())].equals(target.getName());
        } else {
            return source.isAssignableFrom(target);
        }

    }
}
