# 装配Bean
## 声明Bean
### 使用注解的
+ @Component
+ @service
+ @controller
+ @Named  (几乎不用)(来源于Java依赖注入规范)

默认将ID设置为类名的第一个字母变为小写，如果想设置不同ID，使用@Component("XXXX")

需要开启组件扫描
1. 在java中开启
```
@Configuration
@ComponentScan //开启组件扫描
public class SpringConfig(){
}
```
@ComponentScan默认会扫描与配置类相同的包以及子包，设置其他包使用@ComponentScan(basePackages="XXXX"),如果是多个包则使用
@ComponentScan(basePackages={"XXXX"，"XXXX"})。

除了将包设置为简单的String类型之外，还可以将其指定为包中所包含的类或接口。@ComponentScan(basePackageClasses={XXXX.class，XXXX.class})
2. 通过XML组件启用组件扫描
```
<context:component-scan base-package="包名" />
```
## 构造器注入和Setter方法注入
### 构造器注入
+ \<constructor-arg\>元素
```
<bean id="user" class="fun.liutong.controller.UserController">
    <constructor-arg ref="userService" />
</bean>
```
如果构造器带参数,使用value属性，通过该属性表明给定的值要字面量的形式注入到构造器中。
```
<bean id="user" class="fun.liutong.controller.UserController">
    <constructor-arg value="name" />
    <constructor-arg value="age />
</bean>
```
+ 使用Spring 3.0所引入的c-命名空间
```
<bean id="user" class="fun.liutong.controller.UserController" 
        c:name-ref="userService"/>
```
c: 命名空间前缀  name 构造器参数  
## 装配Bean
### 在XML中进行显式装配
在基于XML的Spring配置中声明一个Bean，使用\<bean\>标签。
```
<bean id="user" class="fun.liutong.pojo.User" />
```

### 在java中进行显示装配
添加@Configuration注解表明这个类是一个配置类。
```
@Configuration
@ComponentScan //开启组件扫描
public class SpringConfig(){
    @Bean(name="自定义Bean ID")
    public User user(){
        return new User();
    }
}
```
### 隐式的Bean发现机制和自动装配
+ @Autowired
+ @Inject   (来源于Java依赖注入规范)
自动装配就是让Spring自动满足Bean依赖的方法，在满足依赖过程中，会在Spring应用上下文中寻找匹配某个Bean需求的其他Bean,可以使用Spring的@Autowired注解。

如果没有匹配到Bean，Spring会抛出一个异常，可以将@Autowired的required属性设置为false：@Autowired(required=false)。

## 控制Bean的创建和销毁
