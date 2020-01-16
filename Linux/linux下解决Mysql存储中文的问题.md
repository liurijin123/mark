mysql -u root -p进入Mysql命令行环境，然后输入命令查看当前编码格式：

>mysql> show variables like '%char%';

输出：
```
+--------------------------+----------------------------+
| Variable_name            | Value                      |
+--------------------------+----------------------------+
| character_set_client     | utf8                       |
| character_set_connection | utf8                       |
| character_set_database   | latin1                     |
| character_set_filesystem | binary                     |
| character_set_results    | utf8                       |
| character_set_server     | latin1                     |
| character_set_system     | utf8                       |
| character_sets_dir       | /usr/share/mysql/charsets/ |
+--------------------------+----------------------------+
```
可以看出，character_set_database和character_set_server的编码格式为latin1，这时候你如果使用INSERT插入中文会报下面的错误：

ERROR 1366 (HY000): Incorrect string value: '\xE5\x93\x88\xE5\x93\x88' for column 'answer' at row 1

我们需要将其改为支持中文的编码格式，比如gb2312，gbk，utf8等。gb2312是简体中文的码，gbk支持简体中文及繁体中文，utf8支持几乎所有字符。这里我们就将其改为utf8。

1，用ls命令查找/etc目录下是否有my.cnf文件；

2，如果没有，就要从/usr/share/mysql/下拷贝一个到/etc ，查看/usr/share/mysql/目录下的文件，发现有一个my-default.cnf配置文件，将其拷贝到/etc目录下并将其改名为my.cnf文件：

>sudo cp /usr/share/mysql/my-default.cnf /etc/my.cnf

3，修改配置文件my.cnf，在其中添加字符集设置：

```
[mysqld]
collation-server = utf8_unicode_ci
init-connect='SET NAMES utf8'
character-set-server = utf8

[client]
default-character-set=utf8

[mysql]
default-character-set=utf8
```
4，重启mysql服务：

>sudo service mysql restart
5，再进入mysql命令行环境查看编码格式：

>mysql> show variables like '%char%';
```
+--------------------------+----------------------------+
| Variable_name            | Value                      |
+--------------------------+----------------------------+
| character_set_client     | utf8                       |
| character_set_connection | utf8                       |
| character_set_database   | utf8                       |
| character_set_filesystem | binary                     |
| character_set_results    | utf8                       |
| character_set_server     | utf8                       |
| character_set_system     | utf8                       |
| character_sets_dir       | /usr/share/mysql/charsets/ |
+--------------------------+----------------------------+
```
OK！数据库编码都变为utf8格式了，这时候创建一个数据库，并在其中添加中文：
