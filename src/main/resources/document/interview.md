## 面经


map底层结构
<br>

dict和压缩表 转换
<br>
还有Java怎么解决aba问题的
<br>
你知道spring有哪些扩展点
<br>
数据库的主从复制
<br>
多线程操作数据库，用普通的连接，一定会出现线程安全问题嘛，用锁也不行？我看网上都用连接池
就是按年龄，0~10，10~20，20~30等等，统计人数

<br>
sql  window function 
<br>
对 底层结构  还有上层和底层的映射  如何实现这种
<br>

redis有没有用过，常用的数据结构以及在业务中使用的场景，redis的hash怎么实现的，rehash过程讲一下和JavaHashMap的rehash有什么区别？redis cluster有没有了解过，怎么做到高可用的？redis的持久化机制，为啥不能用redis做专门的持久化数据库存储？

<br>
1.简历上写，精通spring框架，问一下aop的原理，回答我只会用，不知道原理。
<br>
2.简历上写熟悉linux，问几个简单的命令，回答我都是用的时候百度的，记不住。
<br>
3.简历上写精通Java基础，问io，nio的区别，回答什么是nio。
<br>
4.简历上写熟悉redis，问怎么持久化，回答我只会简单的存取。


<br>
发现cpu使用率99.9%，符合死循环的情况。
然后再使用 jps 和 jstack 命令查看一下进程的状态
但是1.8是在链表转换树或者对树进行操作的时候会出现线程安全的问题。
ConurrentHashMap
1.8抛弃分段锁，转为用CAS+synchronized来实现，同样HashEntry改为Node，也加入了红黑树的实现。主要还是看put的流程。

<br>
map底层结构
dict和压缩表 转换
还有Java怎么解决aba问题的
你知道spring有哪些扩展点
数据库的主从复制
多线程操作数据库，用普通的连接，一定会出现线程安全问题嘛，用锁也不行？我看网上都用连接池
就是按年龄，0~10，10~20，20~30等等，统计人数
sql  window function 
对 底层结构  还有上层和底层的映射  如何实现这种

好像直接嵌套查就可以了… select count(*) from ( select age div 10 as age_group from users ) group by age_group


redis有没有用过，常用的数据结构以及在业务中使用的场景，redis的hash怎么实现的，rehash过程讲一下和JavaHashMap的rehash有什么区别？redis cluster有没有了解过，怎么做到高可用的？redis的持久化机制，为啥不能用redis做专门的持久化数据库存储？

<br>
A服务和B服务互相调用，A发起通知后，B调用A的数据，还是之前的（事务没有执行完）
如果解决
<br>
上线后redis服务突然暴增



<br>
cap&&cas

<br>
mysql 
select * where id >6 for update

spring 事务内如果保证事务提交后再执行其他操作(可能是热门题目)

mysql 主键索引和唯一索引


update set xx=dd where id =1

inset id = 5

JDK源码对应的设计模式


单例模式 是否线程安全

spring bean 是否线程安全

servlet 是否线程安全

线程池参数


@Async 实现

分布式锁的集中方案，除了redis和zk还有其他吗？


ddd的理解和实践

cas && cap

##  面试之重点:
### 数据库问题(锁是关键)
### 分布式问题
