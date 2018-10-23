![](http://r.photo.store.qq.com/psb?/V12X9A3m33wlIQ/aXHU8V1jKmb.9ccuHJZ3xEkz.gJd*v5MpAS*9dRWLD4!/r/dGcBAAAAAAAA)
# Iterator接口
Iterator接口，这是一个用于遍历集合中元素的接口，主要包含hashNext(),next(),remove()三种方法。它的一个子接口LinkedIterator在它的基础上又添加了三种
方法，分别是add(),previous(),hasPrevious()。也就是说如果是先Iterator接口，那么在遍历集合中元素的时候，只能往后遍历，被遍历后的元素不会在遍历到，
通常无序集合实现的都是这个接口，比如HashSet，HashMap；而那些元素有序的集合，实现的一般都是LinkedIterator接口，实现这个接口的集合可以双向遍历，
既可以通过next()访问下一个元素，又可以通过previous()访问前一个元素，比如ArrayList。
# List
>List是元素有序并且可以重复的集合。
>List的主要实现：ArrayList, LinkedList, Vector。
## 1.常用方法
![](http://r.photo.store.qq.com/psb?/V12X9A3m33wlIQ/FXnrxrrpSvqxn3elzqtJbQYy7qZxe3LfkcTbJPRLlmc!/r/dDYBAAAAAAAA)
## 2.ArrayList、LinkedList、Vector 的区别

类型|ArrayList|LinkedList|Vector
  ---|---|---|---
底层实现|数组|双向循环链表|数组
同步性及效率|不同步，非线程安全，效率高|不同步，非线程安全，效率高|同步，线程安全，效率低
特点|查询快,增删慢|查询慢,增删快|查询快,增删慢
默认容量|10|/|10
扩容机制|int newCapacity = oldCapacity + (oldCapacity >> 1); //1.5 倍|/|2 倍

# Set
>Set集合中的对象不按特定的方式排序(存入和取出的顺序不一定一致)，并且没有重复对象。

>Set的主要实现类：HashSet, TreeSet。
## 1.常用方法
![](http://r.photo.store.qq.com/psb?/V12X9A3m33wlIQ/Fc0DNWUs1cv28XByICRULEyIqNaZ6Bv5H0529TQozZc!/r/dFIBAAAAAAAA)
## 2.区别

类型|HashSet|TreeSet|LinkedHashSet
---|---|---|---
底层实现|HashMap|红黑树|LinkedHashMap
重复性|不允许重复|不允许重复|不允许重复
有/无序|无序|有序，支持两种排序方式，自然排序和定制排序，其中自然排序为默认的排序方式。|有序，以元素插入的顺序来维护集合的链接表
时间复杂度|add()，remove()，contains()方法的时间复杂度是O(1)|add()，remove()，contains()方法的时间复杂度是O(logn)|LinkedHashSet在迭代访问Set中的全部元素时，性能比HashSet好，但是插入时性能稍微逊色于HashSet，时间复杂度是 O(1)。
同步性|不同步，线程不安全|不同步，线程不安全|不同步，线程不安全
null值|允许null值|不支持null值，会抛出 java.lang.NullPointerException 异常。因为TreeSet应用 compareTo()方法于各个元素来比较他们，当比较null值时会抛出 NullPointerException异常。|允许null值
比较|equals()|compareTo()|equals()
## 3.HashSet如何检查重复
当你把对象加入HashSet时，HashSet会先计算对象的hashcode值来判断对象加入的位置，同时也会与其他加入的对象的hashcode值作比较，如果没有相符的hashcode，HashSet会假设对象没有重复出现。但是如果发现有相同hashcode值的对象，这时会调用equals（）方法来检查hashcode相等的对象是否真的相同。如果两者相同，HashSet就不会让加入操作成功。

hashCode（）与equals（）的相关规定：

如果两个对象相等，则hashcode一定也是相同的

两个对象相等,对两个equals方法返回true

两个对象有相同的hashcode值，它们也不一定是相等的

综上，equals方法被覆盖过，则hashCode方法也必须被覆盖

hashCode()的默认行为是对堆上的对象产生独特值。如果没有重写hashCode()，则该class的两个对象无论如何都不会相等（即使这两个对象指向相同的数据）。
# Map
>Map 是一种把键对象和值对象映射的集合，它的每一个元素都包含一对键对象和值对象。 Map没有继承于Collection接口从Map集合中检索元素时，只要给出键对象，就会返回对应的值对象。

>Map 的常用实现类：HashMap、TreeMap、HashTable、LinkedHashMap、ConcurrentHashMap
## 1.常用方法
![](http://r.photo.store.qq.com/psb?/V12X9A3m33wlIQ/LAQxKtHpfiLrGN0jGGeiHM4MKbJSbBahxu8qk91AKOI!/r/dPMAAAAAAAAA)
## 2.区别

类型|HashMap|HashTable
---|---|---
底层实现|数组+链表|数组+链表
同步性|线程不同步|同步
null值|允许 key 和 Vale 是 null，但是只允许一个 key 为 null,且这个元素存放在哈希表 0 角标位置|不允许key、value 是 null
hash|使用hash(Object key)扰动函数对 key 的 hashCode 进行扰动后作为 hash 值|直接使用 key 的 hashCode() 返回值作为 hash 值
容量|容量为 2^4 且容量一定是 2^n|默认容量是11,不一定是 2^n
扩容|两倍，且哈希桶的下标使用 &运算代替了取模|2倍+1，取哈希桶下标是直接用模运算
