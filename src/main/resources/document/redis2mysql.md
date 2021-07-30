## redis&mysql



### redis




redis有没有用过，常用的数据结构以及在业务中使用的场景，redis的hash怎么实现的，rehash过程讲一下和JavaHashMap的rehash有什么区别？redis cluster有没有了解过，怎么做到高可用的？redis的持久化机制，为啥不能用redis做专门的持久化数据库存储？

<br>
不同的是，Redis的字典只能是字符串，另外他们rehash的方式不一样，因为Java的HashMap的字典很大时，rehash是个耗时的操作，需要一次全部rehash。Redis为了追求高性能，不能堵塞服务，所以采用了渐进式rehash策略。
<br>
渐进式rehash会在rehash的同时，保留新旧两个hash结构，如下图所示，查询时会同时查询两个hash结构，然后在后续的定时任务以及hash操作指令中，循环渐进地将旧hash的内容一点点地迁到新的hash结构中。当搬迁完成了，就会使用新的hash结构取而代之。当hash移除最后一个元素后，该数据结构自动删除，内存被回收。
<br>

不提供"关系"，需要自己维护"关系"，非常非常非常麻烦，可与第二点联系；
不支持事务(ACID)，MULTI/EXEC/WATCH不算；
异步持久化，丢数据，改同步性能就没有了；
异步复制，丢数据，WAIT性能就没了，还是会丢；


<br>

没有银弹，特别是在存储领域，我们总是在空间时间上取舍，在CAP上取舍，redis正是内存数据库，以此换取查询速度，在可靠性，事务性，以及空间利用率上有所取舍！

### 雪崩 && 穿透 && 击穿

缓存穿透:数据库没有,缓存没有

Spring-boot最新默认cache即支持空对象,为空则保存org.springframework.cache.support.NullValue

缓存击穿:数据库有,缓存没有(并发情况下恶劣影响)

缓存雪崩:大量缓存同时失效,db面临同时大量请求涌入

登录 redis-cli 命令行，输入monitor，即可进入到 redis 监控模式。

### redis 持久化
RDB(Redis DataBase) 开启子进程，创建RDB
AOF(Append only file) 保存数据库中的键值对，AOF保存写命令的log来还原数据，更新频率更高

### mongodb

[十大优势](https://xie.infoq.cn/article/180d98535bfa0c3e71aff1662)

schema-less or schema-free
<br>

mongodb 对数据的压缩支持 snappy、zlib 算法，针对存储层中的数据分类又可以分为以下几种：
<br>
普通 collection 数据：对用户写入 collection 的数据是否需要压缩，支持 snappy、zlib 算法，4.2 增加zstd支持。
<br>
journal 数据：对 journal 日志数据是否压缩，支持 snappy、zlib 算法，4.2 增加zstd支持。
<br>
index 索引数据：对索引数据是否进行压缩，默认只支持前缀压缩，这样相同前缀的索引数据，共同的前缀只会存储一次，这样即可减少内存和磁盘消耗。
 <br>
 mongodb 默认的 snappy 压缩算法压缩比约为 2.2-3.5 倍
 <br>
 zlib 压缩算法压缩比约为 4.5-7.5 倍(本次迁移采用 zlib 高压缩算法)
 <br>
 mongodb-4.2 版本开始已经支持分布式事务功能
 <br>
 当前 mongodb 已经为我司提供了数万亿级数据库存储服务，从业务场景和业务接入情况来看，当前至少有 90%以上业务场景可以 mongodb 和 mysql 相互替代，这一部分业务使用 mongodb 也非常合适，业务切换到 mongodb 后也得到了一致性的认可，主要为业务解决了如下痛点：
 
 分库分表痛点：业务最大的痛，mongodb 可以理解为无限大的表，彻底解决该痛点。
 
 机房多活痛点：传统 mysql 双向同步具有物理服务器成本高(多副本情况下，又多了一倍)、人力成本高(你得找几个人来开发和运维双向同步系统)、数据一致性很难保证等。mongodb 分片及复制集架构可以天然支持机房多活，包括南北双机房，三机房，甚至更多国内国外机房多活都可解决。
 
 成本高，mysql 官方版本默认没有压缩等功能，同样得数据，默认配置，mongodb 磁盘相比 mysql 就可以节省 70%左右。
 
 分布式弹性扩缩容：有了该功能，除了可以解决分库分表痛点外，对业务容量评估也减轻了很大的工作量。例如如果 mysql，你不知道后续数据量就近多大，一般一次性就申请很多套，造成了资源浪费。
 
 mongodb 分布式事务支持，突破了单机事务的限制。
  <br>
  mongodb存储引擎wiredtiger默认高压缩、高性能、细粒度锁。单个复制集即可存储数十亿数据。  同样的数据，默认mongodb磁盘占用是Es的六分之一。
<br>

### mysql

mysql索引原理和失效的场景以及mvcc以及常用优化
mysql-innodb索引底层采用b+树的数据结构，并且只在叶子节点下保存具体数据，这种设计可以让b+树每个节点都可以尽可能多的存储索引数据，结合缓存优化，可以尽可能少地访问硬盘，并能保证访问每条数据的速度大致相同。mysql的索引遵循最左匹配原则，如果查询条件不是按照最左顺序匹配，联合索引会失效。此外，前导模糊查询(例如 like %李)、负向条件索引(建议用in。负向条件有：!=、<>、not in、not exists、not like 等)、索引列上进行运算或使用函数等情况都会让索引失效。<br>


### 最大的成就
加入目前的团队时，公司面临互联网转型，项目还在草创阶段。带领服务端同事们将销售管理自动化项目完成从无到有的搭建、基于spring-cloud的微服务化、基于aws的devops进行重构升级，一直到目前稳定迭代并衍生出智慧选店的新项目。
<br>


### 遇到的最大的问题
项目基于spring-cloud实现云服务后，公司与aws合作，所有的服务迁移到aws，并全部依赖aws的各项服务。取消了微服务注册中心，改为依赖api-gateway网关进行熔断和降级，用sqs替换rabbit-mq，文件管理服务器迁移到aws s3，项目部署采用devops的流水线，等等。由于aws的资源相关资料较少，官方文档的可读性一般，也不可能遇到问题就依赖aws的技术支持，所以等于是一个坑一个坑的踩。
<br>


### 读过什么论文
 做密码机工作的时候读过北邮的一篇关于加密算法的文章。非对称加密。<br>
 常见的对称加密算法有DES、3DES、AES、Blowfish、IDEA、RC5、RC6.<br>
 要想使用非对称加密算法，首先要有一对key，一个被称为private key私钥，一个成为public key公钥，然后可以把你的public key分发给想给你传密文的用户，然后用户使用该public key加密过得密文，只有使用你的private key才能解密，也就是说，只要你自己保存好你的private key，就能确保，别人想给你发的密文不被破解，所以你不用担心别人的密钥被盗<br>
 常见的非对称加密算法有RSA、DSA.<br>
 由于进行的都是大数计算，使得 RSA 最快的情况也比 DES 慢上好几倍，无论是软件还是硬件实现。速度一直是 RSA 的缺陷。一般来说只用于少量数据加密。RSA 的速度是对应同样安全级别的对称密码算法的1/1000左右。
 比起 DES 和其它对称算法来说，RSA 要慢得多。实际上一般使用一种对称算法来加密信息，然后用 RSA 来加密比较短的公钥，然后将用 RSA 加密的公钥和用对称算法加密的消息发送给接收方。
 这样一来对随机数的要求就更高了，尤其对产生对称密码的要求非常高，否则的话可以越过 RSA 来直接攻击对称密码。


### jvm

操作系统层面，进程运行有5个状态：运行态、就绪态、阻塞态、创建态、结束态。jvm的线程调用的是内核线程。


### spring && es&&devops

spring的核心是ioc和aop以及实现了servlet协议和http路由管理。ioc是将所有的bean进行统一创建和分配管理，不需要开发中编写大量的创建、初始化和配置，专心于具体的功能开发即可，从面向对象的角度来说，ioc提高了项目的抽象程度，隐藏依赖的bean的实现细节，方便封装。aop为面向切面编程，方便在已经开发好的代码里追加功能，比如日志记录、数据校验等等。<br>
es 包含检索和排序两块核心，检索基于倒排索引；排序采用tf * idf算法，词频（term frequency，TF）指的是某一个给定的词语在该文件中出现的频率;某一特定词语的IDF，可以由总文件数目除以包含该词语之文件的数目.这个算法的核心要义就是一个词在一个文档里出现的频率很高，而在所有文档里出现的频率较低，那么检索这个词的时候，这个文档排名就会靠前。<br>
devops本质是提高了增量开发的可行性，在以迭代推进的敏捷开发模式中尤其有效。

### 索引下推

Index Condition Pushdown（索引下推） MySQL 5.6引入了索引下推优化，默认开启，使用SET optimizer_switch = ‘index_condition_pushdown=off’;可以将其关闭。官方文档中给的例子和解释如下： people表中（zipcode，lastname，firstname）构成一个索引SELECT * FROM people WHERE zipcode=‘95054’ AND lastname LIKE ‘%etrunia%’ AND address LIKE ‘%Main Street%’;如果没有使用索引下推技术，则MySQL会通过zipcode='95054’从存储引擎中查询对应的数据，返回到MySQL服务端，然后MySQL服务端基于lastname LIKE '%etrunia%'和address LIKE '%Main Street%'来判断数据是否符合条件。 如果使用了索引下推技术，则MYSQL首先会返回符合zipcode='95054’的索引，然后根据lastname LIKE '%etrunia%'和address LIKE '%Main Street%'来判断索引是否符合条件。如果符合条件，则根据该索引来定位对应的数据，如果不符合，则直接reject掉。 有了索引下推优化，可以在有like条件查询的情况下，减少回表次数。

最关键的一点都没提到：
组合索引满足最左匹配，但是遇到非等值判断时匹配停止。
name like '陈%' 不是等值匹配，所以 age = 20 这里就用不上 (name,age) 组合索引了。如果没有索引下推，组合索引只能用到 name，age 的判定就需要回表才能做了。5.6之后有了索引下推，age = 20 可以直接在组合索引里判定。

### MVCC Multi-Version Concurrency Control，即多版本并发控制

MVCC的意思用简单的话讲就是对数据库的任何修改的提交都不会直接覆盖之前的数据，而是产生一个新的版本与老版本共存，使得读取时可以完全不加锁。这样读某一个数据时，事务可以根据隔离级别选择要读取哪个版本的数据。过程中完全不需要加锁。
<br>
Read Committed - 一个事务读取数据时总是读这个数据最近一次被commit的版本

会导致【不可重复读Non-repeatable read】：两次读的某条数据不一致

<br>
Repeatable Read - 一个事务读取数据时总是读取当前事务开始之前最后一次被commit的版本（所以底层实现时需要比较当前事务和数据被commit的版本号）。
会导致【幻读Phantom Read】：当用户读取某范围数据行时，另一事务在此范围内插入新行，当用户再次读取此范围数据行时，读取到新的幻影行。
比如A更新了大于11的数据，同时B提交了一条18的新数据；A再次查询发现有一条大于11的没有被更新
可见，幻读就是没有读到的记录，以为不存在，但其实是可以更新成功的，并且，更新成功后，再次读取，就出现了
当前读和快照度 的区别

Serializable：最高的隔离级别，它通过强制事务排序，使之不可能相互冲突，从而解决幻读问题


<br>
repeatable read 解决幻读
MVCC会给每行元组加一些辅助字段，记录创建版本号和删除版本号。
而每一个事务在启动的时候，都有一个唯一的递增的版本号。每开启一个新事务，事务的版本号就会递增。

快照读(Snapshort Read / Consistent Read)之间通过MVCC实现。
当前读(Current Read / Locking Read)之间由Next-Key Lock(同时添加间隙锁与行锁称为Next-key lock)实现。
快照读与当前读之间仍然有幻读。

在快照读的时候，每次读取都会产生一个（readview）可读视图


[MVCC](https://www.zhihu.com/question/279538775/answer/407458020)


### acid
<br>isolation 隔离由 锁 实现
<br> consistency 一致性 由 undo log来保证,帮助事务回滚+MVCC
<br> atomicity durable 由 redo log来实现 每次都将重做日志缓存 写入重做日志文件，再调用一次fsync操作,顺序写入
<br> Durability（持久性）：事务处理结束后，对数据的修改就是永久的，即便系统故障也不会丢失。
<br> 额外还有一个binlog，跟redo log很像，但是redo log是在innodb引擎层面产生，而binlog是在数据库上层，任何修改都会产生binlog，binlog是一种逻辑日志，记录的是sql语句；redo log是物理格式日志，记录的是对于每一页的修改。
<br>binlog只要由事务提交完成就会进行写入；redo log是并发的，不是在事务提交时候写入，记录的顺序并不是事务开始的顺序。
<br> 事务执行失败或者接收到rollback命令进行回滚，可以利用undo log回滚到修改之前的样子。undo log也是 逻辑日志。
<br> mvcc也是用undo log实现，用户读取一行记录，如果该记录已经被其他事务占用，当前事务可以通过undo读取之前的行版本信息，实现非锁定读取
<br> undo log 也会产生redo log，因为undo log也需要持久性的保护


### mysql window function 

```
SELECT
	`姓名`,
	`班级`,
	`人气`,
	rank() over w1 AS rak
FROM
	`民工漫班级` window w1 AS ( PARTITION BY `班级` ORDER BY `人气` DESC );


```

### MRR

MySQL 从磁盘读取页的数据后，会把数据放到数据缓冲池，下次如果还用到这个页，就不需要去磁盘读取，直接从内存读。
索引本身就是为了减少磁盘 IO，加快查询，而 MRR，则是把索引减少磁盘 IO 的作用，进一步放大

### 红包雨案例

这个红包特效和金额直接交给前端搞就好，后台主要做防盗刷（后台训练一个人类点击模型，前端把点击时间、点击坐标发送过来，最后一次请求就把钱发下去就行了），最后再做一下风控（设备、手机号、实名信息等）

