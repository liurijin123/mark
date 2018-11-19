## 一、配置/etc/my.cnf
```
[mysqld]
datadir=/data/mysql
port=3306
bind-address = 0.0.0.0
```
重启MySQL
```
service mysql restart
```
## 二、点击SSH
此处的ip就是服务器的ip，端口默认都是22  

用户名：root（就是远程连接服务器时的用户）

密码：就是远程连接服务器的密码

![](https://img-blog.csdn.net/20180218143212666?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvYmVuYmVuMTU4MA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

## 三、连接服务器中的mysql

连接名：随便起

ip：localhost 

端口：3306  

用户名：root 

密码：mysql登录密码

![](https://img-blog.csdn.net/20180218142341100?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvYmVuYmVuMTU4MA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

