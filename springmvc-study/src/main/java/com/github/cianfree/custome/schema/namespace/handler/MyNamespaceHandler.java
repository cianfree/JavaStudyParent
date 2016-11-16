package com.github.cianfree.custome.schema.namespace.handler;

import com.github.cianfree.custome.schema.parser.SimpleDateFormatBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author Arvin
 * @time 2016/11/16 14:33
 */
public class MyNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("dateformat", new SimpleDateFormatBeanDefinitionParser());
    }
}
