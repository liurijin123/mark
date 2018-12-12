1.登陆到客户端

>sqlplus / as sysdba

2.关闭数据库

>SQL> shutdown immediate;

3.启动数据库

>SQL> STARTUP MOUNT EXCLUSIVE;

4.更改以下设置
```
SQL> ALTER SYSTEM ENABLE RESTRICTED SESSION;

系统已更改。

SQL> ALTER SYSTEM SET JOB_QUEUE_PROCESSES=0;

系统已更改。

SQL> ALTER SYSTEM SET AQ_TM_PROCESSES=0;

系统已更改。

SQL> ALTER DATABASE OPEN;

数据库已更改。

SQL> ALTER DATABASE CHARACTER SET ZHS16GBK;
```
5.第 1 行出现错误:ORA-12712: 新字符集必须为旧字符集的超集,输入一下设置

>SQL> ALTER DATABASE character set INTERNAL_USE ZHS16GBK;

6.关闭数据库

>SQL> SHUTDOWN immediate;

7.启动数据库

>SQL> startup;

8.验证

>SQL> select userenv('language') from dual;
