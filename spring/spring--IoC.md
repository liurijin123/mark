## 概念
>IoC是一个管理bean的容器，bean的创建，事件，行为，相互之间的依赖都由IoC容器管理。

>控制反转是一种通过描述并通过第三方去产生或获取特定对象的方式。
## IoC容器的设计
Spring IoC容器的设计主要是基于BeanFactory和ApplicationContext两个接口，其中ApplicationContext是BeanFactory的子接口
## 在IoC容器中定义Bean
1.Resource定位：Spring IoC容器根据开发者的配置进行资源定位。
2.BeanDefinition的载入：将Resource定位到的信息，保存到Bean定义(BeanDefinition)中。
3.BeanDefinition的注册：将BeanDefinition的信息发布到Spring IoC容器中。
## Bean的生命周期
+ 实例化bean对象(通过构造方法或者工厂方法)
+ 设置对象属性(setter等)（依赖注入）
+ 如果Bean实现了BeanNameAware接口，工厂调用Bean的setBeanName()方法传递Bean的ID。（和下面的一条均属于检查Aware接口）
+ 如果Bean实现了BeanFactoryAware接口，工厂调用setBeanFactory()方法传入工厂自身
+ 将Bean实例传递给Bean的后置处理器的postProcessBeforeInitialization(Object bean, String beanname)方法
+ 调用Bean的初始化方法
+ 将Bean实例传递给Bean的后置处理器的postProcessAfterInitialization(Object bean, String beanname)方法
+ 使用Bean
+ 容器关闭之前，调用Bean的销毁方法
## 依赖注入
所谓依赖注入，是指程序运行过程中，如果需要调用另一个对象协助时，无须在代码中创建被调用者，而是依赖于外部的注入。Spring的依赖注入对调用者和被调用者几乎没有任何要求，完全支持对POJO之间依赖关系的管理
