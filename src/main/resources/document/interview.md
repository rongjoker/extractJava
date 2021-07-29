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

解法：利用 事务同步管理器 `TransactionalEventListener` 解决
org.springframework.transaction.support.TransactionSynchronizationManager

示例：
```
@Transactional(rollbackFor = Exception.class)
    public Boolean inviteUser(..) {
        userService.add(..);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                integralService.addIntegration(..,20)
            }
        });


```

注解方式:
```
@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void addIntegration(..){
        integralService.addIntegration(..,20)
    }

```

核心源码:

```

@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronization transactionSynchronization = createTransactionSynchronization(event);
			TransactionSynchronizationManager.registerSynchronization(transactionSynchronization);
		}
		else if (this.annotation.fallbackExecution()) {
			if (this.annotation.phase() == TransactionPhase.AFTER_ROLLBACK && logger.isWarnEnabled()) {
				logger.warn("Processing " + event + " as a fallback execution on AFTER_ROLLBACK phase");
			}
			processEvent(event);
		}
		else {
			// No transactional event execution at all
			if (logger.isDebugEnabled()) {
				logger.debug("No transaction is active - skipping " + event);
			}
		}
	}

```
本质上还是使用了TransactionSynchronizationManager，只是对他再一次进行封装





mysql 主键索引和唯一索引


update set xx=dd where id =1


for update仅适用于InnoDB，且必须在事务块(BEGIN/COMMIT)中才能生效。在进行事务操作时，通过“for update”语句，MySQL会对查询结果集中每行数据都添加排他锁，其他线程对该记录的更新与删除操作都会阻塞。排他锁包含行锁、表锁。

```
begin;

select * from goods where id <> 1 for update;

commit;

```
类似这样会产生表锁

inset id = 5

### 设计模式

JDK源码对应的设计模式 迭代器:迭代器模式 runnable 命令模式 

适配器模式 （可通过创建方法识别采用不同抽象/接口类型的实例，并返回自己/另一个抽象/接口类型的实现，其装饰/覆盖给定实例）
 java.util.Arrays#asList()
 
 装饰器模式 （通过创作方法识别采用相同抽象/接口类型的实例，添加额外的行为）
 所有子类java.io.InputStream，OutputStream，Reader并Writer有一个构造函数取相同类型的实例。
 
 责任链模式 （通过行为方法识别（间接地）在队列中的相同抽象/接口类型的另一个实现中调用相同的方法）
javax.servlet.Filter#doFilter()

观察者模式（或发布/订阅） （可以通过行为方法识别，根据自己的状态调用另一个抽象/接口类型的实例上的方法）
所有实现java.util.EventListener（因此实际上各地的Swing）


###  单例模式 是否线程安全


spring bean 是否线程安全

servlet 是否线程安全
由于servlet在Tomcat中是以单例模式存在的，所有的线程共享实例变量。多个线程对共享资源的访问就造成了线程不安全问题

线程池参数


@Async 实现

分布式锁的集中方案，除了redis和zk还有其他吗？


ddd的理解和实践
hibernate 更类似黑盒

### 防止sql注入:
有些场景，并不能使用预编译方式（或者你仅仅是不知道或者懒）。像一些代码重构，把表名/列名/排序等字段，动态传入的时候，不可避免的就需要SQL拼接的方式，SQL注入依然有搞头。

SELECT * FROM order WHERE  name like concat(‘%’,#{name}, ‘%’) //正确的写法


队列的应用

cas && cap

##  面试之重点:
### 数据库问题(锁是关键)
### 分布式问题


ConcurrentHashMap的并发扩容ConcurrentHashMap 在扩容时会计算出一个步长（stride），最小值是16，然后给当前扩容线程分配“一个步长”的节点数，例如16个，让该线程去对这16个节点进行扩容操作（将节点从老表移动到新表）。如果在扩容结束前又来一个线程，则也会给该线程分配一个步长的节点数让该线程去扩容。依次类推，以达到多线程并发扩容的效果。


AQS中有多个线程并发添加到队列中，需要做并发控制吗这个是需要的，源码中是通过 CAS 来进行并发控制。在 addWaiter(Node mode) 方法中，如果并发添加节点，则只会有一个线程成功，另一个会失败，从而走到 enq(node) 方法中去进行重试。说实话，虽然看过源码，但是真的很难记得这么多细节，因为 Java 面试要准备的东西实在太多了，所以这边可以利用一个小技巧。小技巧：首先并发控制肯定是要做的，这个不难推测；接着，如果注意观察的话，不难发现在 JDK 源码中做并发控制的方式大多是 CAS，所以当你不知道怎么做并发控制的时候，可以直接蒙个 CAS，很有可能就被你蒙对了。


### 多个AOP的顺序怎么定
通过 Ordered 和 PriorityOrdered 接口进行排序。
思路：这个问题其实我也也没准备过，但是我知道在 Spring 中有专门定义了 Ordered 和 PriorityOrdered 接口来实现排序功能，例如：对 BeanFactoryPostProcessor 的排序就是使用的这两个接口，所以这边我是直接猜测的用的这两个接口，结果还真是，所以说平时多积累还是挺重要的。


### synchronize底层维护了几个列表存放被阻塞的线程

这题是紧接着上一题的，很明显面试官想看看我是不是真的对 synchronize 底层原理有所了解。synchronize 底层使用了3个双向链表来存放被阻塞的线程，3个双向链表分别是：_cxq（Contention queue）、_EntryList（EntryList）、_WaitSet（WaitSet）。当线程获取锁失败进入阻塞后，首先会被加入到 _cxq 链表，_cxq 链表的节点会被进一步转移到 _EntryList 链表。当持有锁的线程释放锁后，_EntryList 链表头结点的线程会被唤醒，该线程称为 OnDeck 线程，然后该线程会尝试抢占锁。而当我们调用 wait() 时，线程会被放入 _WaitSet，直到调用了 notify()/notifyAll() 后，线程才被重新放入 _cxq 或 _EntryList，默认放入 _cxq 链表头部。


### 多线程按顺序输出ABC若干次
考察多线程基础，Semaphore、Lock + Condition、synchronize 都有解法。

### 算法
力扣原题：21、165、206
输出一个二叉树的宽度,二叉树的层序遍历（力扣102）的简单变形，记录下每层的节点个数，取最大值即可。
字符串的全排列 剑指 Offer 38 题
最长上升子序列 力扣 300 题