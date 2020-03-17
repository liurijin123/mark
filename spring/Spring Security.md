# Spring Security简介

Spring Security是为基于Spring的应用程序提供声明式安全保护的安全性框架，能够在web请求级别和方法调用级别处理身份认证和授权。

| 模块                  | 描述                                                         |
| --------------------- | ------------------------------------------------------------ |
| ACL                   | 支持通过访问控制列表(access control list，ACL)为域对象提供安全性 |
| 切面(Aspects)         | 当使用Spring Security注解时，会使用基于AspectJ的切面         |
| CAS客户端(CAS Client) | 提供Jasig的中心认证服务(Central Authentication Server，CAS)进行集成的功能 |
| 核心(Core)            | 提供Spring Security基本库                                    |
| 加密(Cryptography)    | 提供加密和密码编码的功能                                     |
| LDAP                  | 支持基于LDAP进行认证                                         |
| OpenID                | 支持使用OpenID进行集中式认证                                 |
| Remoting              | 提供了对Spring Remoting的支持                                |
| 标签库(Tag Library)   | Spring Security的JSP标签库                                   |
| 配置(Configuration)   | 通过XML和Java配置Spring Security的功能支持                   |
| Web                   | 提供了Spring Security基于Filter的Web安全性支持               |

Spring Security配置在一个实现了WebSecurityConfigurer的bean中，或者扩展WebSecurityConfigurerAdapter。
```
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
}
```
可以通过重载三个configure()方法来配置Web安全性

| 方法                                    | 描述                          |
| --------------------------------------- | ----------------------------- |
| configure(WebSecurity)                  | 配置Spring Security的Filter链 |
| configure(HttpSecurity)                 | 配置如何通过拦截器保护请求    |
| configure(AuthenticationManagerBuilder) | 配置user-detail服务           |
**configure(WebSecurity) **用来配置用户信息

```
@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("root").password("989898").roles("ROOT").and();
    }
```
配置用户详细信息的方法

| 方法                                                      | 描述               |
| --------------------------------------------------------- | ------------------ |
| roles(String... roles)                                    | 授予某个用户角色   |
| authorities(GrantedAuthority... authorities)              | 授予某个用户权限   |
| authorities(List<? extends GrantedAuthority> authorities) | 授予某个用户权限   |
| authorities(String... authorities)                        | 授予某个用户权限   |
| accountExpired(boolean accountExpired)                    | 定义账号是否过期   |
| accountLocked(boolean accountLocked)                      | 定义账号是否锁定   |
| credentialsExpired(boolean credentialsExpired)            | 定义凭证是否过期   |
| disabled(boolean disabled)                                | 定义账号是否被禁用 |
| password(String password)                                 | 定义用户密码       |
| and()                                                     | 链接配置           |

**configure(HttpSecurity)**拦截请求
```
@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().loginPage("/login").successForwardUrl("/").and()
                .logout().logoutSuccessUrl("/").logoutRequestMatcher(new AntPathRequestMatcher("/logout")).and()
                .httpBasic().and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
        .antMatchers("/account/add").hasRole("ROOT")
        .antMatchers("/account/delete").hasRole("ADMIN");
    }
```
配置方法

| 方法                                     | 描述                                                |
| ---------------------------------------- | --------------------------------------------------- |
| hasAnyRole(String... authorities)        | 如果用户具备给定权限中的某一个，就允许访问          |
| hasRole(String role)                     | 如果用户具备给定角色，就允许访问                    |
| hasAuthority(String authority)           | 如果用户具备给定权限，就允许访问                    |
| hasIpAddress(String ipAddressExpression) | 如果请求来自给定IP地址，就允许访问                  |
| permitAll()                              | 无条件允许访问                                      |
| anonymous()                              | 允许匿名访问                                        |
| rememberMe()                             | 如果用户是通过Rmember-me功能认证，就允许访问        |
| denyAll()                                | 无条件拒绝所有访问                                  |
| authenticated()                          | 允许认证过的用户访问                                |
| fullyAuthenticated()                     | 如果用户认证是完整的(非Remember-me认证)，就允许访问 |
| access(String attribute)                 | 使用SpEL表达式认证                                  |
| not()                                    | 对其他访问方法结果求反                              |
| hasAnyRole()                             | 如果用户具备给定角色中的某一个，就允许访问          |








## logout404

https://blog.csdn.net/Dongguabai/article/details/81166556?depth_1-utm_source=distribute.pc_relevant.none-task&utm_source=distribute.pc_relevant.none-task
