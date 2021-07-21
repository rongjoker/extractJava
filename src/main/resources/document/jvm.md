## jvm相关

markword其中有4位是保存gc的年龄,所以最大为15就会进入老年代

<br>
java -XX:+PrintCommandLineFlags -version
可以查看gc器

<br>
Volatile 使用场景:

禁止指令重排序

一写多读：有一个数据，只由一个线程更新，其他线程都来读取。


<br>
Java双亲委派机制，打破双亲委派

在Launcher指定了Bootstrap ClassLoader的加载文件夹System.getProperty("sun.boot.class.path");这个主要就是JDK指定的\lib\rt包在ExtClassLoader中指定了加载文件夹：String var0 = System.getProperty("java.ext.dirs");这个主要就是JDK指定的\lib\ext包在AppClassLoader中指定了加载文件夹：String var1 = System.getProperty("java.class.path");都是环境变量，自行可配。这个主要指的是开发中的类路径

<br>
通过委派的方式，可以避免类的重复加载，当父加载器已经加载过某一个类时，子加载器就不会再重新加载这个类
<br>
通过双亲委派的方式，还保证了安全性。因为Bootstrap ClassLoader在加载的时候，只会加载JAVA_HOME中的jar包里面的类，如java.lang.Integer，那么这个类是不会被随意替换的，除非有人跑到你的机器上， 破坏你的JDK
<br>
为什么重写loadClass方法可以破坏双亲委派
检查步骤为：<br>
1、先检查类是否已经被加载过 2、若没有加载则调用父加载器的loadClass()方法进行加载 3、若父加载器为空则默认使用启动类加载器作为父加载器。4、如果父类加载失败，抛出ClassNotFoundException异常后，再调用自己的findClass()方法进行加载。
<br>
如果你想定义一个自己的类加载器，并且要遵守双亲委派模型，那么可以继承ClassLoader，并且在findClass中实现你自己的加载逻辑即可。

<br>
打破的场景：
热部署

<br>
为什么JNDI、JDBC等需要破坏双亲委派？

ServiceLoader<Driver> loadedDrivers = ServiceLoader.load(Driver.class);
DriverManager是被根加载器加载的，那么在加载时遇到以上代码，会尝试加载所有Driver的实现类，但是这些实现类基本都是第三方提供的，根据双亲委派原则，第三方的类不能被根加载器加载。

<br>
JDBC中通过引入ThreadContextClassLoader（线程上下文加载器，默认情况下是AppClassLoader）的方式破坏了双亲委派原则。
第一行，获取当前线程的线程上下⽂类加载器 AppClassLoader，⽤于加载 classpath 中的具体实现类。

<br>
为什么TOMCAT要破坏双亲委派？

<br>
不同的应用程序可能会依赖同一个第三方类库的不同版本，但是不同版本的类库中某一个类的全路径名可能是一样的。如多个应用都要依赖hollis.jar，但是A应用需要依赖1.0.0版本，但是B应用需要依赖1.0.1版本。这两个版本中都有一个类是com.hollis.Test.class。如果采用默认的双亲委派类加载机制，那么是无法加载多个相同的类。所以，Tomcat破坏双亲委派原则，提供隔离的机制，为每个web容器单独提供一个WebAppClassLoader加载器。Tomcat的类加载机制：为了实现隔离性，优先加载 Web 应用自己定义的类，所以没有遵照双亲委派的约定，每一个应用自己的类加载器——WebAppClassLoader负责加载本身的目录下的class文件，加载不到时再交给CommonClassLoader加载，这和双亲委派刚好相反。
<br>
简单来说就是tomcat下的应用可以互相隔离，各自可以用不同版本的第三方类库