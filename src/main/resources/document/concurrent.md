## java并发编程相关


### AQS


### CAS

java可以利用Syschronized、ReentrantLock、AtomicInteger类实现线程安全，AtomicInteger封装了一个【volatile int value】属性，它可以对这个属性进行许多原子性操作，这些原子性操作大多是基于cas原理，而在cas中，AtomicInteger使用的是一个叫Unsafe的类中的方法，Unsafe可以提供一些底层操作，也就是CPU特定的指令集，进而避免了并发问题（Unsafe是一个很危险的类，它可以做一些和内存相关的操作）






### volatile && synchronized

CPU有缓存一致性协议：MESI，这不错。但MESI并非是无条件生效的！
java虚拟机在实现volatile关键字的时候，是写入了一条lock 前缀的汇编指令。
lock 前缀的汇编指令会强制写入主存，也可避免前后指令的CPU重排序，并及时让其他核中的相应缓存行失效，从而利用MESI达到符合预期的效果。
非lock前缀的汇编指令在执行写操作的时候，可能是是不生效的。比如前面所说的Store Buffer的存在，lock前缀的指令在功能上可以等价于内存屏障，可以让其立即刷入主存。
是volatile的底层实现，满足了MESI的触发条件，才让变量有了缓存一致性。


## java并发编程相关
### AQS
### ABA问题
ABA问题是指：
1. T1线程读取内存里的数据A到自己的工作线程，此时T1阻塞；
2. T2线程也读取了内存里的数据A到自己的工作线程，先将A数据修改成B并刷回内存，然后再将B修改成A并刷回内存。T2干其他事情去了；
3. T1此时阻塞结束，将A值改为C，并准备刷回内存，发现此时的内存值还为A，没变，则成功将C刷回了内存。这个过程就是ABA问题。

### ABA会导致哪些问题？
1. 就好比张三偷了你的钱去炒股，短短3天就赚了几十万，然后再在你不知道的情况下如数奉还？这个钱还是以前的钱嘛？
2. 李四老婆出轨隔壁老王，然后悔过自新，又回到李四身边，此时还是一样的老婆嘛？




```
private static class Pair<T> {
        final T reference;
        final int stamp;
        private Pair(T reference, int stamp) {
            this.reference = reference;
            this.stamp = stamp;
        }
        static <T> Pair<T> of(T reference, int stamp) {
            return new Pair<T>(reference, stamp);
        }
    }

```



## java并发编程相关


### AQS

线程2会将自己放入AQS中的一个等待队列，因为自己尝试加锁失败了，此时就要将自己放入队列中来等待，等待线程1释放锁之后，自己就可以重新尝试加锁了。
所以大家可以看到，AQS是如此的核心！AQS内部还有一个等待队列，专门放那些加锁失败的线程！
<br>
接着，线程1在执行完自己的业务逻辑代码之后，就会释放锁！他释放锁的过程非常的简单，就是将AQS内的state变量的值递减1，如果state值为0，则彻底释放锁，会将“加锁线程”变量也设置为null！
<br>
接下来，会从等待队列的队头唤醒线程2重新尝试加锁。好！线程2现在就重新尝试加锁，这时还是用CAS操作将state从0变为1，此时就会成功，成功之后代表加锁成功，就会将state设置为1。此外，还要把“加锁线程”设置为线程2自己，同时线程2自己就从等待队列中出队了。
<br>


### CAS

java可以利用Syschronized、ReentrantLock、AtomicInteger类实现线程安全，AtomicInteger封装了一个【private volatile int value;】属性，它可以对这个属性进行许多原子性操作，这些原子性操作大多是基于cas原理，而在cas中，AtomicInteger使用的是一个叫Unsafe的类中的方法，Unsafe可以提供一些底层操作，也就是CPU特定的指令集，进而避免了并发问题（Unsafe是一个很危险的类，它可以做一些和内存相关的操作）

<br>
gas

```
  public final int getAndSet(int newValue) {
        return U.getAndSetInt(this, VALUE, newValue);
    }

```
<br>
cas:

```

    public final boolean compareAndSet(int expectedValue, int newValue) {
        return U.compareAndSetInt(this, VALUE, expectedValue, newValue);
    }

```



### volatile && synchronized

synchronized同步快对同一条线程来说是可重入的，不会出现自己把自己锁死的问题；
Mutex Lock
监视器锁（Monitor）本质是依赖于底层的操作系统的Mutex Lock（互斥锁）来实现的。每个对象都对应于一个可称为" 互斥锁" 的标记，这个标记用来保证在任一时刻，只能有一个线程访问该对象。

互斥锁：用于保护临界区，确保同一时间只有一个线程访问数据。对共享资源的访问，先对互斥量进行加锁，如果互斥量已经上锁，调用线程会阻塞，直到互斥量被解锁。在完成了对共享资源的访问后，要对互斥量进行解锁。
在运行期间，Java对象头Mark Word里存储的数据会随着锁标志位的变化而变化




https://www.jianshu.com/p/a4f84658a7e8

参考 ReentrantLock ，为什么只有state是volatile修饰，但能保证可见性

r大的博客
https://www.iteye.com/blog/user/rednaxelafx


### ThreadLocal 内存泄漏的原因
从上图中可以看出，hreadLocalMap使用ThreadLocal的弱引用作为key，如果一个ThreadLocal不存在外部强引用时，Key(ThreadLocal)势必会被GC回收，这样就会导致ThreadLocalMap中key为null， 而value还存在着强引用，只有thead线程退出以后,value的强引用链条才会断掉。

但如果当前线程再迟迟不结束的话，这些key为null的Entry的value就会一直存在一条强引用链：

Thread Ref -> Thread -> ThreaLocalMap -> Entry -> value
永远无法回收，造成内存泄漏。

由于ThreadLocalMap中的key是ThreadLocal的弱引用，一旦发生GC便会回收ThreadLocal，那么此时的ThreadLocalMap存储的key便是null。如果不通过手动remove()那么ThreadLocalMap的Entry便伴随线程的整个生命周期造成内存泄漏，大致就是一个thread ref -> thread -> threadLocals -> entry -> value的强引用关系。因此Java其实是有对于内存泄漏的一些预防机制的，每次调用ThreadLocal的set()、get()、remove()方法时都会回收key为空的Entry的value。

那么为什么ThreadLocalMap的key要设计成弱引用呢？其实很简单，如果key设计成强引用且没有手动remove()，那么key会和value一样伴随线程的整个生命周期，如果key是弱引用，被GC后至少ThreadLocal被回收了，在下一次的set()、get()、remove()还会回收key为null的Entry的value。

### ThreadPoolExecutor的拒绝策略RejectedExecutionHandler

自定义拒绝策略：
```
class MyRejectedExecutionHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        new Thread(r,"新线程"+new Random().nextInt(10)).start();
    }
}

```

工作内存并不是在内存中分配一块空间给线程，而是cache 和寄存器的一个抽象