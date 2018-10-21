## Java对象头
synchronized用的锁是存在Java对象头里的，如果对象是数组类型，则虚拟机用3个字宽(Word)存储对象头，如果是非数组类型，则用2字宽存储对象头。在32位虚拟机中
1字宽等于4字节，即32bit

Java对象头长度

长度|内容|说明
---|---|---
32/64bit|Mark Word|存储对象的hashCode或锁信息
32/64bit|Class Mwtadata Address|存储到对象类型数据的指针
32/64bit|Array length|数组的长度

Mark Word的状态变化

## 锁的四种状态
+ 无锁状态
+ 偏向锁

+ 轻量级锁
+ 重量级锁
