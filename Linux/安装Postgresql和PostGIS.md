## 1.安装 PostgreSQL 和 PostGIS
```
sudo yum install postgresql-server postgresql postgis
```
初次安装后，默认生成一个名为 postgres 的数据库和一个名为 postgres 的数据库用户。这里需要注意的是，同时还生成了一个名为 postgres 的 Linux 系统用户。我们以后在操作 PostgreSQL 的时候都应该在这个新创建的 postgres 用户中进行。
## 2.初始化PostGres数据库
```
service postgresql initdb
```
## 3.添加到自启动服务
```
chkconfig postgresql on
```
## 4.启动服务
```
service postgresql start
```
## 5.账户登录
进入 postgres 账户，并且进入 PostgreSQL 控制台：
```
su – postgres
psql
```
这时相当于系统用户 postgres 以同名数据库用户的身份，登录数据库，否则我们每次执行 psql 的时候都要在参数中指定用户，容易忘。

在 psql 中设置一下密码——需要注意的是，这里设置的密码并不是 postgres 系统帐户的密码，而是在数据库中的用户密码：
```
postgres=# \password postgres
```
## 6.导入 PostGIS 扩展
根据 postgresql 和 postgis 的版本不同，路径会有些差异，主要是路径中包含版本信息：
```
su postgres
createdb template_postgis
createlang plpgsql template_postgis
psql -d template_postgis -f /usr/share/postgresql/contrib/postgis.sql
psql -d template_postgis -f /usr/share/postgresql/contrib/spatial_ref_sys.sql
```
上面的操作中，创建了一个叫做 “template_postgis” 的空数据库。这个数据库是空的，并且属于 postgres 用户。注意，不要往这个数据库中添加数据，这个数据库之所以称为 “模板”（template），就说明它是用来派生用的。

相应的 PostGIS 路径可能不同，如果失败，就在上面的路径附近多尝试一下，找几个 .sql 文件试试看。

## 7.转换 .shp 文件到 PostGIS 数据库中
转换 .shp 到 .sql 文件

首先找到需要转换的文件，假设需要转换的 .shp 文件是：/tmp/demo.shp，那么就做以下操作：
```
su postgres
cd /tmp
shp2pgsql -W GBK -s 3857 ./demo.shp entry > demo.sql
```
这里需要说明一下最后一句各部分所代表的含义：

+ -W GBK：如果你的 .shp 文件包含中文字符，那么请加上这个选项
+ -s 3857：指明文件的参考坐标系统。我的 .shp 文件使用的是 EPSG:3857
+ ./demo.shp：.shp 文件的路径
+ entry：表示要导入的数据库表名——假设这个 .shp 文件表示的是各个入口，所以我命名为 “entry”
+ demo.sql
+ shp2pgsql -W UTF-8 -s 3857 ./lvyou.shp point > point.sql
得到了 .sql 文件后，就可以直接导入到 PostgreSQL 数据库了。

## 8.创建一个 PostGIS 数据库
这里就需要用到前面的 template 了。
```
su postgres
psql
CREATE DATABASE newdb WITH TEMPLATE template_postgis OWNER dbuser;
```
+ newdb: 新的数据库名
+ template_postgis：也就是前面的 template_postgis
+ dbuser：你的账户名，我一般使用 postgres
导入 .sql 文件
```
su postgres
psql
\c newdb
\i demo.sql
\d
```
可以看到，.sql 文件已经被导入了。

删除表
```
drop table table_name;
```

## 9.设置数据库权限
PostgreSQL 默认不对外开放权限，只对监听环回地址。要修改的话，需要找到 postgresql.conf 文件，修改值 listen_addresses：
```
listen_addresses = '*'
```

## 10.开启远程访问权限
修改pg_hba.conf
```
# "local" is for Unix domain socket connections only
local   all             all                                     trust
# IPv4 local connections:
host    all             all             127.0.0.1/32            trust
# IPv6 local connections:
host    all             all             ::1/128                 ident
# Allow replication connections from localhost, by a user with the
# replication privilege.
local   replication     postgres                                peer
host    replication     postgres        127.0.0.1/32            ident
host    replication     postgres        ::1/128                 ident
```





