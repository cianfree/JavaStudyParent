#算法-排序算法
##排序相关的的基本概念
<p style="font-size: 18px;">
排序：将一组杂乱无章的数据按一定的规律顺次排列起来。
</p>
    数据表( data list): 它是待排序数据对象的有限集合。
    排序码(key):        通常数据对象有多个属性域，即多个数据成员组成,其中有一个属性域可用来区分对象,作为排序依据。该域即为排序码。每个数据表用哪个属性域作为排序码，要视具体的应用需要而定。
<p style="font-size: 18px;">
分类： 
</p>
    内排序： 指在排序期间数据对象全部存放在内存的排序；
    外排序： 指在排序期间全部对象个数太多，不能同时存放在内存，必须根据排序过程的要求，不断在内、外存之间移动的排序。

#排序算法的分析

##排序算法的稳定性

如果在对象序列中有两个对象 r[i] 和 r[j], 它们的排序码 k[i]==k[j]。如果排序前后,对象 r[i]和 r[j]
的相对位置不变，则称排序算法是稳定的；否则排序算法是不稳定的。
##排序算法的评价

###时间开销

排序的时间开销可用算法执行中的数据比较次数与数据移动次数来衡量。
算法运行时间代价的大略估算一般都按平均情况进行估算。对于那些受对象排序码序列初始排列及对象个数影响较大的，需要按最好情况和最坏情况进行估算

###空间开销

算法执行时所需的附加存储。


#END