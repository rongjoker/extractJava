## jvm相关

markword其中有4位是保存gc的年龄,所以最大为15就会进入老年代

<br>
java -XX:+PrintCommandLineFlags -version
可以查看gc器

<br>
Volatile 使用场景:

禁止指令重排序

一写多读：有一个数据，只由一个线程更新，其他线程都来读取。