# 概念
+ AOP：Aspect Oriented Programming的缩写，通过预编译方式和运行期动态代
理实现程序功能的统一维护的一种技术
+ 主要功能是：日记记录、性能统计、安全控制、事务处理、异常处理等
## AOP专业术语
名称|说明
---|---
切面(Aspect)|一个关注点的模块化，这个关注点可能会横切多个对象
连接点(Joinpoint)|程序执行过程中的某个特定的点
通知(Advice)|在切面的某个特定的连接点上执行的动作
切入点(Pointcut)|匹配连接点断言，在AOP中通知和一个切入点表达式关联
引入(Introduction)|在不修改类代码的前提下，为类添加新的方法和属性
目标对象(Target Object)|被一个或者多个切面通知的对象
AOP代理(AOP Proxy)|Aop框架创建的对象，用来实现切面契约(aspect contract)(包括通知方法执行等功能)
织入(Weaving)|把切面连接到其他的应用程序类型或对象上，并创建一个被通知的对象，分为：编译时织入、类加载时织入、执行时织入
### Advice的类型
名称|说明
---|---
前置通知(Before advice)|在某连接点(join point)之前执行的通知，但不能阻止连接点之前的执行，除非它抛出一个异常
返回后通知(After returning advice)|在某连接点(join point)正常完成后执行的通知
抛出异常后通知(After throwing)|在方法抛出异常退出时执行的通知
后通知(After advice)|当某连接点退出的时候执行的通知(不论是正常返回还是异常退出)
环绕通知(Around Advice)|包围一个连接点(join point)的通知
## AOP实现方式
+ 预编译
AspectJ
+ 运行期动态代理（JDK动态代理、CGLib动态代理）
SpringAOP、JbossAOP
### SpringAop
+ 纯Java实现，无需特殊的编译过程
+ Spring AOP默认使用标准的JavaSE动态代理作为AOP代理，这使得任何接口(或者接口集)都可以被代理
+ Spring AOP中也可以使用CGLIB代理(如果一个业务对象并没有实现一个接口)
# 配置
## 通过XML配置切面aspect
```
<aop:config>
  <aop:aspect id="myAspect" ref="aBean">  //把aBean做为一个切面来声明
  ...
  </aop:aspect>
</aop:config>
<bean id="aBean" class="...">
  ...
</bean>
```
## 通过XML配置切入点pointcut
+ execution(public * *(..))  切入点为执行所有的public方法时
+ execution(* set*(..))  切入点为执行所有set开始的方法时
+ execution(* fun.liutong.service.UserService.*(..))  切入点为执行UserService类中的所有
+ execution(* fun.liutong.service..(..))  切入点为执行fun.liutong.service包下的所有方法时
+ execution(* fun.liutong.service...(..))  切入点为执行fun.liutong.service包及其包下的所有方法时
```
<aop:config>
  <aop:aspect id="myAspect" ref="aBean">  //把aBean做为一个切面来声明
    <aop:pointcut id="businessService"
      expression="execution(* fun.liutong.service..(..))" />  //配置切入点
      ...
  </aop:aspect>
</aop:config>
<bean id="aBean" class="...">
```
## 通过XML配置通知Advice
```
<aop:config>
  <aop:aspect id="myAspect" ref="aBean">  //把aBean做为一个切面来声明
    <aop:pointcut id=myService"
      expression="execution(* fun.liutong.service..(..))" />  //配置切入点
    <aop:before method="before" pointcut-ref="myService" />  //配置前置通知
    <aop:after-returning method="afterRuturnning" pointcut-ref="myService" />  //配置返回后通知通知
    <aop:after-throwing method="afterThrowing" pointcut-ref="myService" />  //配置抛出异常后通知
    <aop:after method="after" pointcut-ref="myService" />  //配置后通知
    <--环绕通知方法的第一个参数必须是ProceedingJoinPoint类型-->
    <aop:around method="around" pointcut-ref="myService" />  //配置环绕通知
    <--通知中进行参数的配置-->
    <aop:around method="around" pointcut-ref="execution(* fun.liutong.service.UserService.getUser(String,int)
        and args(name,age)" />
  </aop:aspect>
</aop:config>
<bean id="aBean" class="...">
```
