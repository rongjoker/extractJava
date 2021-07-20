## java并发编程相关


### AQS


### CAS

java可以利用Syschronized、ReentrantLock、AtomicInteger类实现线程安全，AtomicInteger封装了一个【volatile int value】属性，它可以对这个属性进行许多原子性操作，这些原子性操作大多是基于cas原理，而在cas中，AtomicInteger使用的是一个叫Unsafe的类中的方法，Unsafe可以提供一些底层操作，也就是CPU特定的指令集，进而避免了并发问题（Unsafe是一个很危险的类，它可以做一些和内存相关的操作）






### volatile && synchronized
