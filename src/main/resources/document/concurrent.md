## java并发编程相关
### AQS



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
~~~java
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
