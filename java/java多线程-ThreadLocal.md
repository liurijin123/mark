## ThreadLocal是什么
ThreadLocal是一个本地线程副本变量工具类。主要用于将私有线程和该线程存放的副本对象做一个映射，各个线程之间的变量互不干扰，在高并发场景下，可以实现无状态的调用，特别适用于各个线程依赖不通的变量值完成操作的场景。
## 从数据结构入手
### 下图为ThreadLocal的内部结构图
![](https://upload-images.jianshu.io/upload_images/7432604-ad2ff581127ba8cc.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/806/format/webp)
### 从上面的结构图，我们已经窥见ThreadLocal的核心机制：
+ 每个Thread线程内部都有一个Map。
+ Map里面存储线程本地对象（key）和线程的变量副本（value）
+ 但是，Thread内部的Map是由ThreadLocal维护的，由ThreadLocal负责向map获取和设置线程的变量值。
所以对于不同的线程，每次获取副本值时，别的线程并不能获取到当前线程的副本值，形成了副本的隔离，互不干扰。
## 深入解析ThreadLocal
ThreadLocal类提供如下几个核心方法：
```
public T get()
public void set(T value)
public void remove()
```
+ get()方法用于获取当前线程的副本变量值。
+ set()方法用于保存当前线程的副本变量值。
+ initialValue()为当前线程初始副本变量值。
+ remove()方法移除当前前程的副本变量值。
### get()方法
```
/**
 * Returns the value in the current thread's copy of this
 * thread-local variable.  If the variable has no value for the
 * current thread, it is first initialized to the value returned
 * by an invocation of the {@link #initialValue} method.
 *
 * @return the current thread's value of this thread-local
 */
public T get() {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null) {
        ThreadLocalMap.Entry e = map.getEntry(this);
        if (e != null)
            return (T)e.value;
    }
    return setInitialValue();
}

ThreadLocalMap getMap(Thread t) {
    return t.threadLocals;
}

private T setInitialValue() {
    T value = initialValue();
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null)
        map.set(this, value);
    else
        createMap(t, value);
    return value;
}

protected T initialValue() {
    return null;
}
```
步骤：

1.获取当前线程的ThreadLocalMap对象threadLocals

2.从map中获取线程存储的K-V Entry节点。

3.从Entry节点获取存储的Value副本值返回。

4.map为空的话返回初始值null，即线程变量副本为null，在使用时需要注意判断NullPointerException。
### set()方法
```
/**
 * Sets the current thread's copy of this thread-local variable
 * to the specified value.  Most subclasses will have no need to
 * override this method, relying solely on the {@link #initialValue}
 * method to set the values of thread-locals.
 *
 * @param value the value to be stored in the current thread's copy of
 *        this thread-local.
 */
public void set(T value) {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null)
        map.set(this, value);
    else
        createMap(t, value);
}

ThreadLocalMap getMap(Thread t) {
    return t.threadLocals;
}

void createMap(Thread t, T firstValue) {
    t.threadLocals = new ThreadLocalMap(this, firstValue);
}
```
步骤：

1.获取当前线程的成员变量map

2.map非空，则重新将ThreadLocal和新的value副本放入到map中。

3.map空，则对线程的成员变量ThreadLocalMap进行初始化创建，并将ThreadLocal和value副本放入map中。
### remove()方法
```
/**
 * Removes the current thread's value for this thread-local
 * variable.  If this thread-local variable is subsequently
 * {@linkplain #get read} by the current thread, its value will be
 * reinitialized by invoking its {@link #initialValue} method,
 * unless its value is {@linkplain #set set} by the current thread
 * in the interim.  This may result in multiple invocations of the
 * <tt>initialValue</tt> method in the current thread.
 *
 * @since 1.5
 */
public void remove() {
 ThreadLocalMap m = getMap(Thread.currentThread());
 if (m != null)
     m.remove(this);
}

ThreadLocalMap getMap(Thread t) {
    return t.threadLocals;
}
```
remove方法比较简单，不做赘述。





