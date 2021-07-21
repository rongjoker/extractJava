## 分布式相关


### 分布式事务


[简单介绍](https://www.jianshu.com/p/d64045ed9012)


数据库事务的几个特性：原子性(Atomicity )、一致性( Consistency )、隔离性或独立性( Isolation)和持久性(Durabilily)，简称就是ACID

<br>

分布式事务也部分遵循 ACID 规范：

原子性：严格遵循

一致性：事务完成后的一致性严格遵循；事务中的一致性可适当放宽

隔离性：并行事务间不可影响；事务中间结果可见性允许安全放宽

持久性：严格遵循

<br>
TCC 分为 3 个阶段（Try-Confirm-Cancel）<br>
使用过ByteTCC分布式事务框架，可以集成到spring里,分别写三个接口方法

<br>
两阶段提交/XA
<br>
XA 事务由一个或多个资源管理器（RM）、一个事务管理器（TM）和一个应用程序（ApplicationProgram）组成。
如果有任何一个参与者 prepare 失败，那么 TM 会通知所有完成 prepare 的参与者进行回滚。

<br>
Saga 是这一篇数据库论文saga提到的一个方案。其核心思想是将长事务拆分为多个本地短事务.长事务适用，对中间结果不敏感的业务场景适用



