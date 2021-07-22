## spring相关


### 循环依赖


三级缓存解决循环依赖:
```


/ 一级缓存，缓存正常的bean实例
/** Cache of singleton objects: bean name to bean instance. */
private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

// 二级缓存，缓存还未进行依赖注入和初始化方法调用的bean实例
/** Cache of early singleton objects: bean name to bean instance. */
private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

// 三级缓存，缓存bean实例的ObjectFactory
/** Cache of singleton factories: bean name to ObjectFactory. */
private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);

public Object getSingleton(String beanName) {
    return getSingleton(beanName, true);
}

protected Object getSingleton(String beanName, boolean allowEarlyReference) {
    // 先尝试中一级缓存获取
    Object singletonObject = this.singletonObjects.get(beanName);
    // 获取不到，并且当前需要获取的bean正在创建中
    // 第一次容器初始化触发getBean(A)的时候，这个isSingletonCurrentlyInCreation判断一定为false
    // 这个时候就会去走创建bean的流程，创建bean之前会先把这个bean标记为正在创建
    // 然后A实例化之后，依赖注入B，触发B的实例化，B再注入A的时候，会再次触发getBean(A)
    // 此时isSingletonCurrentlyInCreation就会返回true了

    // 当前需要获取的bean正在创建中时，代表出现了循环依赖（或者一前一后并发获取这个bean）
    // 这个时候才需要去看二、三级缓存
    if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
        // 加锁了
        synchronized (this.singletonObjects) {
            // 从二级缓存获取
            singletonObject = this.earlySingletonObjects.get(beanName);
            if (singletonObject == null && allowEarlyReference) {
                // 二级缓存也没有，并且允许获取早期引用的话 - allowEarlyReference传进来是true
                ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
                // 从三级缓存获取ObjectFactory
                if (singletonFactory != null) {
                    // 通过ObjectFactory获取bean实例
                    singletonObject = singletonFactory.getObject();
                    // 放入二级缓存
                    this.earlySingletonObjects.put(beanName, singletonObject);
                    // 从三级缓存删除
                    // 也就是说对于一个单例bean，ObjectFactory#getObject只会调用到一次
                    // 获取到早期bean实例之后，就把这个bean实例从三级缓存升级到二级缓存了
                    this.singletonFactories.remove(beanName);
                }
            }
        }
    }
    // 不管从哪里获取到的bean实例，都会返回
    return singletonObject;
}

```




### cglib 实现动态代理

利用asm进行字节码增强，修改字节码，产生子类,接管了之前的方法

### Spring中七种事务传播行为(Propagation)
默认值为 Propagation.REQUIRED 如果当前存在事务，则加入该事务，如果当前不存在事务，则创建一个新的事务。