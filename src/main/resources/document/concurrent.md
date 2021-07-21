## java并发编程相关


### AQS


### CAS

java可以利用Syschronized、ReentrantLock、AtomicInteger类实现线程安全，AtomicInteger封装了一个【volatile int value】属性，它可以对这个属性进行许多原子性操作，这些原子性操作大多是基于cas原理，而在cas中，AtomicInteger使用的是一个叫Unsafe的类中的方法，Unsafe可以提供一些底层操作，也就是CPU特定的指令集，进而避免了并发问题（Unsafe是一个很危险的类，它可以做一些和内存相关的操作）






### volatile && synchronized


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


https://www.jianshu.com/p/a4f84658a7e8

参考 ReentrantLock ，为什么只有state是volatile修饰，但能保证可见性

r大的博客
https://www.iteye.com/blog/user/rednaxelafx
