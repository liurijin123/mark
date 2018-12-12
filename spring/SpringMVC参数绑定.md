## 一、SpringMVC 参数绑定
在 SpringMVC 中，提交请求的数据是通过方法形参来接收的。从客户端请求的 key/value 数据，经过参数绑定，将 key/value 数据绑定到 Controller 的形参上，然后在 Controller 就可以直接使用该形参。
## 二、默认支持的类型
SpringMVC 有支持的默认参数类型，我们直接在形参上给出这些默认类型的声明，就能直接使用了。如下：

   1. HttpServletRequest 对象

   2. HttpServletResponse 对象

   3. HttpSession 对象

   4. Model/ModelMap 对象　

Controller 代码：
```
@RequestMapping("/defaultParameter")
    public ModelAndView defaultParameter(HttpServletRequest request,HttpServletResponse response,
                            HttpSession session,Model model,ModelMap modelMap) throws Exception{
        request.setAttribute("requestParameter", "request类型");
        response.getWriter().write("response");
        session.setAttribute("sessionParameter", "session类型");
        //ModelMap是Model接口的一个实现类，作用是将Model数据填充到request域
        //即使使用Model接口，其内部绑定还是由ModelMap来实现
        model.addAttribute("modelParameter", "model类型");
        modelMap.addAttribute("modelMapParameter", "modelMap类型");
         
        ModelAndView mv = new ModelAndView();
        mv.setViewName("view/success.jsp");
        return mv;
    }
 ```
## 三、基本数据类型的绑定
   1. byte，占用一个字节，取值范围为 -128-127，默认是“\u0000”，表示空
   2. short，占用两个字节，取值范围为 -32768-32767
   3. int，占用四个字节，-2147483648-2147483647
   4. long，占用八个字节，对 long 型变量赋值时必须加上"L"或“l”,否则不认为是 long 型
   5. float，占用四个字节，对 float 型进行赋值的时候必须加上“F”或“f”，如果不加，会产生编译错误，因为系统自动将其定义为 double 型变量。double转换为float类型数据会损失精度。float a = 12.23产生编译错误的，float a = 12是正确的
   6. double，占用八个字节，对 double 型变量赋值的时候最好加上“D”或“d”，但加不加不是硬性规定
   7. char,占用两个字节，在定义字符型变量时，要用单引号括起来
   8. boolean，只有两个值“true”和“false”，默认值为false，不能用0或非0来代替，这点和C语言不同

我们以 int 类型为例：

　　JSP 页面代码：
```
  <form action="basicData" method="post">
    <input name="username" value="10" type="text"/>
    <input type="submit" value="提交">
</form>
```
Controller 代码：
```
@RequestMapping("/basicData")
    public void basicData(int username){
        System.out.println(username);//10
    }
```
表单中input的name值和Controller的参数变量名保持一致，就能完成数据绑定。那么如果不一致使用 @RequestParam 注解来完成，如下：

JSP页面代码不变，<input name="username">保持原样，Controller 代码如下:
```
@RequestMapping("/basicData")
    public void basicData(@RequestParam(value="username") int username){
        System.out.println(username);//10
    }
```
问题：我们这里的参数是基本数据类型，如果从前台页面传递的值为 null 或者 “”的话，那么会出现数据转换的异常，就是必须保证表单传递过来的数据不能为null或”"，所以，在开发过程中，对可能为空的数据，最好将参数数据类型定义成包装类型
## 四、包装数据类型的绑定
```
@RequestMapping("/basicData")
    public void basicData(@RequestParam(value="username") Integer username){
        System.out.println(username);//10
    }
```
## 五、POJO（实体类）类型的绑定
>User.java
```
package com.ys.po;
 
import java.util.Date;
 
public class User {
    private Integer id;
 
    private String username;
 
    private String sex;
 
    private Date birthday;
 
    public Integer getId() {
        return id;
    }
 
    public void setId(Integer id) {
        this.id = id;
    }
 
    public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }
 
    public String getSex() {
        return sex;
    }
 
    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }
 
    public Date getBirthday() {
        return birthday;
    }
 
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
```
>JSP页面：注意输入框的 name 属性值和上面 POJO 实体类的属性保持一致即可映射成功。
```
<form action="pojo" method="post">
        用户id:<input type="text" name="id" value="2"></br>
        用户名:<input type="text" name="username" value="Marry"></br>
        性别:<input type="text" name="sex" value="女"></br>
        出生日期:<input type="text" name="birthday" value="2017-08-25"></br>
        <input type="submit" value="提交">
    </form>
```
>Controller ：
```
@RequestMapping("/pojo")
    public void pojo(User user){
        System.out.println(user);
    }
```
>定义由String类型到 Date 类型的转换器
```
package com.ys.util;
 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
 
import org.springframework.core.convert.converter.Converter;
 
//需要实现Converter接口，这里是将String类型转换成Date类型
public class DateConverter implements Converter<String, Date> {
 
    @Override
    public Date convert(String source) {
        //实现将字符串转成日期类型(格式是yyyy-MM-dd HH:mm:ss)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return dateFormat.parse(source);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //如果参数绑定失败返回null
        return null;
    }
 
}
```
>在 springmvc.xml 文件中配置转换器
```
<mvc:annotation-driven conversion-service="conversionService"></mvc:annotation-driven>
     
    <bean id="conversionService" class="org.springframework.format.support.FormattingConversionServiceFactoryBean">
        <property name="converters">
            <!-- 自定义转换器的类名 -->
            <bean class="com.ys.util.DateConverter"></bean>
        </property>
    </bean>
```
## 六、复合POJO（实体类）类型的绑定
>这里我们增加一个实体类，ContactInfo.java
```
public class ContactInfo {
    private Integer id;
 
    private String tel;
 
    private String address;
 
    public Integer getId() {
        return id;
    }
 
    public void setId(Integer id) {
        this.id = id;
    }
 
    public String getTel() {
        return tel;
    }
 
    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }
 
    public String getAddress() {
        return address;
    }
 
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }
}
```
>然后在上面的User.java中增加一个属性 private ContactInfo contactInfo
>JSP 页面：注意属性name的命名，User.java 的复合属性名.字段名
```
<form action="pojo" method="post">
        用户id:<input type="text" name="id" value="2"></br>
        用户名:<input type="text" name="username" value="Marry"></br>
        性别:<input type="text" name="sex" value="女"></br>
        出生日期:<input type="text" name="birthday" value="2017-08-25"></br>
        地址:<input type="text" name="contactInfo.address" value="2017-08-25"></br>
        电话:<input type="text" name="contactInfo.tel" value="2017-08-25"></br>
        <input type="submit" value="提交">
</form>
``` 
## 七、数组类型的绑定
需求：我们查询出所有User 的信息，并且在JSP页面遍历显示，这时候点击提交按钮，需要在 Controller 中获得页面中显示 User 类的 id 的所有值的数组集合。

>JSP 页面：注意用户id的name值定义为 userId
```
<form action="array" method="post">
    <c:forEach items="${listUser}" var="${varUser}">
        用户id:<input type="text" name="userId" value="${varUser.id}"></br>
        用户名:<input type="text" name="userName" value="${varUser.userName}"></br>
    </c:forEach>   
        <input type="submit" value="提交">
</form>
```
>Controller.java
```
@RequestMapping("/array")
    public void pojo(Integer[] userId){
        for(int i : userId){
           System.out.println(i);
        }
    }
```
## 八、List类型的绑定
需求：批量修改 User 用户的信息

>第一步：创建 UserVo.java，封装 List<User> 属性
```
package com.ys.po;
 
import java.util.List;
 
public class UserVo {
     
    private List<User> userList;
    public List<User> getUserList() {
        return userList;
    }
    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
```
第二步：为了简化过程，我们直接从 Controller 中查询所有 User 信息，然后在页面显示

>Controller
```
@RequestMapping("selectAllUserAndList")
    public ModelAndView selectAllUserAndList(){
        List<User> listUser = userService.selectAllUser();
        ModelAndView mv = new ModelAndView();
        mv.addObject("listUser", listUser);
        mv.setViewName("list.jsp");
        return mv;
    }
```
>JSP 页面
```
<form action="array" method="post">
    <c:forEach items="${listUser}" var="${varUser}" varStatus="status">
        用户id:<input type="text" name="userList[${status.index}]" value="${varUser.id}"></br>
        用户名:<input type="text" name="userList[${status.index}]" value="${varUser.userName}"></br>
    </c:forEach>   
        <input type="submit" value="提交">
</form>
```
>第三步：修改页面的值后，点击提交
```
@RequestMapping("list")
    public void list(UserVo userVo){
       System.out.println(userVo.getUserList);
    }
```
由于我们在 JSP 页面 input 输入框定义的name属性名是 userList[${status.index}].id 这种形式的，这里我们直接用 UserVo 就能获取页面批量提交的 User信息 
## 八、Map类型的绑定
首先在 UserVo 里面增加一个属性 Map<String,User> userMap

第二步：JSP页面，注意看 <input >输入框 name 的属性值
```
<form action="array" method="post">
    <c:forEach items="${listUser}" var="${varUser}" varStatus="status">
        用户id:<input type="text" name="userMap['x'].id" value="${varUser.id}"></br>
        用户名:<input type="text" name="userMap['y'].name" value="${varUser.userName}"></br>
    </c:forEach>   
        <input type="submit" value="提交">
</form>
```
第三步：Controller 中获取页面的属性
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
