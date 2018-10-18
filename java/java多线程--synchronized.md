## synchronized的三种应用方式
* 修饰实例方法，作用于当前实例加锁，进入同步代码前要获得当前实例的锁

* 修饰静态方法，作用于当前类对象加锁，进入同步代码前要获得当前类对象的锁

* 修饰代码块，指定加锁对象，对给定对象加锁，进入同步代码库前要获得给定对象的锁。

## synchronized对象锁的同步和异步
同步：synchronized 

同步的概念就是共享，我们要牢牢记住“共享” 这两个字，如果不是共享的资源，就没有必要进行同步。

异步：asynchronized 

异步的概念就是独立，相互之间不受到任何制约。就好像我们学习http即的时候，在页面发起的ajax请求，我们还可以继续浏览或操作页面的内容，二者之间没有任何关系。

```
public class MyObject {

    public synchronized void method1(){
        try {
            System.out.println(Thread.currentThread().getName());
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /** synchronized */
    public void method2(){
            System.out.println(Thread.currentThread().getName());
    }

    public static void main(String[] args) {

        final MyObject mo = new MyObject();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                mo.method1();
            }
        },"t1");

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                mo.method2();
            }
        },"t2");

        t1.start();
        t2.start();

    }

}
```
A线程先持有object对象的Lock锁，B线程如果在这个时候调用对象中的同步 (synchronized)方法则需等待，也就是同步 

A线程先持有0bject对象的Lock锁，B线程可以以异步的方式调用对象中的非 synchronized修饰的方法
