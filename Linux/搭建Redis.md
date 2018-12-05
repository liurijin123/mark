# 一、Redis的安装
## 1、首先上官网下载Redis 压缩包
## 2、将压缩包拷贝到Linux服务器中，执行解压,然后编译
```
# tar zxvf redis-3.0.7.tar.gz -C /usr/local/
# cd /usr/local/
# ln -sv redis-4.0.6/ redis
# cd redis
# make
```
编译完成之后，可以看到解压文件redis中会有对应的src、conf等文件夹，这和windows下安装解压的文件一样，大部分安装包都会有对应的类文件、配置文件和一些命令文件。
## 4、编译成功后，进入src文件夹，执行make install进行Redis安装
# 二、Redis的部署
## 1、首先为了方便管理，将Redis文件中的conf配置文件和常用命令移动到统一文件中

### a)创建bin和etc文件夹

代码如下:
```
mkdir -p /usr/local/redis/bin
mkdir -p /usr/local/redis/etc
```
### b)执行Linux文件移动命令：

复制代码代码如下:
```
# mv redis.conf etc/
# cd src/
# mv mkreleasehdr.sh redis-benchmark redis-check-aof redis-cli redis-server /usr/local/redis/bin
```
## 2、后台启动redis服务

### a) 切换到/usr/local/redis/etc目录，编辑redis.conf文件,将daemonize属性改为yes（表明需要在后台运行）
### b) 切换到/usr/local/redis/bin目录下执行Redis-server 命令，使用/usr/local/redis/etc/redis.conf 配置文件来启动Redis 服务
```
./redis-server /usr/local/redis/etc/redis.conf
```
## 3、服务端启动成功后，执行redis-cli启动Redis 客户端，查看端口号,默认是6379。
## 4、编辑服务启动脚本vim /etc/init.d/redis
```
#!/bin/sh
#
# Simple Redis init.d script conceived to work on Linux systems
# chkconfig: 2345 90 10 
# description: Redis is a persistent key-value database
# as it does use of the /proc filesystem.

REDISPORT=6379
EXEC=/usr/local/redis/bin/redis-server
CLIEXEC=/usr/local/redis/bin/redis-cli

PIDFILE=/var/run/redis.pid
CONF=/usr/local/redis/etc/redis.conf

case "$1" in
    start)
        if [ -f $PIDFILE ]
        then
                echo "$PIDFILE exists, process is already running or crashed"
        else
                echo "Starting Redis server..."
                $EXEC $CONF
        fi
        ;;
    stop)
        if [ ! -f $PIDFILE ]
        then
                echo "$PIDFILE does not exist, process is not running"
        else
                PID=$(cat $PIDFILE)
                echo "Stopping ..."
                $CLIEXEC -p $REDISPORT shutdown
                while [ -x /proc/${PID} ]
                do
                    echo "Waiting for Redis to shutdown ..."
                    sleep 1
                done
                echo "Redis stopped"
        fi
        ;;
    *)
        echo "Please use start or stop as first argument"
        ;;
esac
```
添加执行权限
```
chmod +x /etc/init.d/redis
```
注册服务
```
chkconfig --add redis
```
开机启动
```
chkconfig redis on
```
测试
```
service redis start
service redis stop
```
**在使用 service redis start 可能会遇到该错误**


"env: /etc/init.d/redis: No such file or directory)"


这是因为在windows下保存了该脚本文件,其中的换行符被改变了,windows中换行符为/r/n


而linux中是/n 所以这边需要把脚本做个转换,我们使用dos2unix

yum install dos2unix 

安装完成后直接执行既可

dos2unix /etc/init.d/redis

再使用service redis start 就没问题啦

**在使用 service redis stop 可能会遇到该错误**
```
/var/run/redis.pid does not exist, process is not running
```
在redis.config中设置daemonize yes，然后重启

若redis无法重启，使用强制关闭
>redis 强制关闭并重启方法
>ps auxf|grep redis |grep -v grep|xargs kill -9 
>/usr/local/bin/redis-server /etc/redis.conf
>ps -ef | grep redis













