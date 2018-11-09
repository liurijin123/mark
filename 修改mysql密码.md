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
