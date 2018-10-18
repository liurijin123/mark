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
属性类
```
public class SpringAction {
    //注入对象springDao
    private String name;
    private int salary;
    private User user;
    
    //此处必须提供含参数的构造函数用于注入相关属性
    public SpringAction(String name,int salary,User user){
        this.name = name;
        this.salary = salary;
        this.user = user;
        System.out.println("构造方法调用属性");
    }
        
        public void save(){
        ...
    }
}
```
+ \<constructor-arg\>元素
```
<!--配置bean,配置后该类由spring管理-->
<bean name="user" class="com.bless.springdemo.vo.User"></bean>

--很久参数索引赋值--
<bean name="springAction" class="com.bless.springdemo.action.SpringAction">
     <constructor-arg index="0" value="刘通" />
     <constructor-arg index="1" value="3500" />
     <constructor-arg index="2" ref="user"/>
</bean>

--根据参数类型赋值--
<bean name="springAction" class="com.bless.springdemo.action.SpringAction">
     <constructor-arg type="Java.lang.String" value="刘通" />
     <constructor-arg type="java.lang.Intager" value="3500" />
     <constructor-arg type="com.bless.springdemo.vo.User" ref="user"/>
</bean>

--根据参数名称赋值--
<bean name="springAction" class="com.bless.springdemo.action.SpringAction">
     <constructor-arg name="name" value="刘通" />
     <constructor-arg name="salary" value="3500" />
     <constructor-arg name="user" ref="user"/>
</bean>

--按照参数顺序直接赋值（value）--
<bean name="springAction" class="com.bless.springdemo.action.SpringAction">
     <constructor-arg value="刘通" />
     <constructor-arg value="3500" />
     <constructor-arg ref="user"/>
</bean>
```
+ 使用Spring 3.0所引入的c-命名空间
```
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:p="http://www.springframework.org/schema/c"       ////使用命名空间时，注意头文件这里要多出这一行
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">

<!--配置bean,配置后该类由spring管理-->
<bean name="user" class="com.bless.springdemo.vo.User"></bean>

<bean name="springAction" class="com.bless.springdemo.action.SpringAction" 
      c:name-value="刘通" 
      c:salary-value="2300"
      c:user-ref="user"/>

--也可以这样使用ref属性--
<bean name="springAction" class="com.bless.springdemo.action.SpringAction" 
      c:_-ref="user"/>    

--当只使用value属性时可以这样--
<bean name="springAction" class="com.bless.springdemo.action.SpringAction" 
      c:_value="刘通" 
      c:_salary="2300"/>
--使用index--      
<bean name="springAction" class="com.bless.springdemo.action.SpringAction" 
      c:_0="刘通" 
      c:_1="2300"/>
</beans>
```
### Setter方法注入
属性类：
```
public class SpringAction {
    //注入对象springDao
    private String name;
    private int salary;
    private User user;
    
    //此处一定要有属性的setter方法
    public void setName(String name) { 
        this.name = name; 
    } 
    
    public void setSalary(Salary salary) { 
        this.salary = salary; 
    } 
    
    public void setUser(User user) { 
        this.user = user; 
    } 
        
        public void save(){
        ...
    }
}
```
+ 使用\<property\>标签
```
<!--配置bean,配置后该类由spring管理-->
<bean name="user" class="com.bless.springdemo.vo.User"></bean>

--很久参数索引赋值--
<bean name="springAction" class="com.bless.springdemo.action.SpringAction">
     <property index="0" value="jane"/>
     <property index="1" value="3500" />
     <property index="2" ref="user"/>
</bean>

--根据参数类型赋值--
<bean name="springAction" class="com.bless.springdemo.action.SpringAction">
     <property type="Java.lang.String" value="刘通" />
     <property type="java.lang.Intager" value="3500" />
     <property type="com.bless.springdemo.vo.User" ref="user"/>
</bean>

--根据参数名称赋值--
<bean name="springAction" class="com.bless.springdemo.action.SpringAction">
     <property name="name" value="刘通" />
     <property name="salary" value="3500" />
     <property name="user" ref="user"/>
</bean>

--按照参数顺序直接赋值（value）--
<bean name="springAction" class="com.bless.springdemo.action.SpringAction">
     <property value="刘通" />
     <property value="3500" />
     <property ref="user"/>
</bean>
```
+ 集合对象注入  
```
<!--配置bean,配置后该类由spring管理-->
<bean name="user" class="com.bless.springdemo.vo.User"></bean>

--注入list参数--
<bean name="springAction" class="com.bless.springdemo.action.SpringAction">
     <property index="0">
        <list>
            <value>小红</value>
            <value>小明</value>
            <value>小刚</value>
        </list>
     </property>
</bean>

--在list中引用bean--
<bean name="springAction" class="com.bless.springdemo.action.SpringAction">
     <property index="0">
        <list>
            <ref bean="user"/>
            <ref bean="student"/>
        </list>
     </property>
</bean>

--注入map参数--
<bean name="springAction" class="com.bless.springdemo.action.SpringAction">
     <property name="map">
        <map>
            <entry key="name" value="小明"/>
            <entry key="name" value="小红"/>
            <entry key="name" value="小刚"/>
        </map>
     </property>
</bean>

--null注入--
<property name="wife"><null/></property>
```
+ p命名空间注入
```
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:p="http://www.springframework.org/schema/p"       ////使用命名空间时，注意头文件这里要多出这一行
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">

<!--配置bean,配置后该类由spring管理-->
<bean name="user" class="com.bless.springdemo.vo.User"></bean>

<bean name="springAction" class="com.bless.springdemo.action.SpringAction" 
      p:name="小明" 
      c:salary="2300"
      c:user-ref="user"/>
      
</beans>
```
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
