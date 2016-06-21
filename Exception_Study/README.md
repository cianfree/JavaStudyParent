#异常概念
    程序运行中的一些错误

#异常分类
    Throwable
        -- Error    系统错误，虚拟机错误，我们不需要处理
        -- Exception    必须catch的异常或声明抛出
        ------RuntimeException  可以不用代码中声明

#JDK常见异常
<ul>
    <li>IndexOutOfBoundsException/ArrayIndexOutOfBoundsException   数组越界异常</li> 
    <li>ArithmeticException   数学运算异常，2 / 0</li> 
    <li>NullPointerException   空指针异常</li> 
</ul>

#异常捕获
    先catch子类异常再捕获父类异常




