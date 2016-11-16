/**
 *
 * 理论上来说，应该是在service级别来切换数据源，因为一个完整的业务应该是在service层，所以在设计切面进行动态切换数据源的时候，应该拦截service层的方法
 *
 * 当然，本程序旨在示例，因此拦截jdbc层面的方法来切换数据源
 *
 * @author Arvin
 * @time 2016/11/16 11:42
 */
package com.github.cianfree.dynamic.datasource.aspect;