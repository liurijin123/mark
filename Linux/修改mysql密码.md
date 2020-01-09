//停止数据库服务

service mysql stop

mysqld_safe --user=mysql --skip-grant-tables --skip-networking&

mysql -u root mysql

//修改密码,需要修改两次，一次authentication_string，一次 password

mysql> UPDATE user SET authentication_string=PASSWORD("123456") WHERE user='root'; 

>Query OK, 4 rows affected (0.00 sec)

>Rows matched: 4  Changed: 4  Warnings: 0 

mysql> update user set password=password('123456') where user='root';

>Query OK, 4 rows affected (0.00 sec)

>Rows matched: 4  Changed: 4  Warnings: 0

mysql> exit

//启动

service mysql start

------------------------

今天使用MySQL时，我创建了一个新用户：
CREATE USER 'ubermensch'@'%' IDENTIFIED BY 'man2017';
然后登陆该用户时却一直报错（密码正确）：
C:\Users\超人林>mysql -u ubermensch -p
Enter password: *******
ERROR 1045 (28000): Access denied for user 'ubermensch'@'localhost' (using password: YES)
后来翻阅网上资料发现MySQL中默认存在一个用户名为空的账户，只要在本地，可以不用输入账号密码即可登录到MySQL中。mysql在验证用户登陆的时候，首先是验证host列，如果host列在验证user列，再password列，而现在按照我之前的连接语句：按照host列找到为空的那列（空匹配所有用户名），所以匹配到了这条记录，然后发现这条记录的密码为空，而我的语句里面有密码，那么就会报错。
解决办法：删除匿名用户。

mysql> use mysql;
Database changed
mysql> delete from user where user='';
Query OK, 1 row affected (0.00 sec)
mysql> flush privileges;
Query OK, 0 rows affected (0.00 sec)
mysql> exit;
