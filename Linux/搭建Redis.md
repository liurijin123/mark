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
# redis        Startup script for Redis Server
#
# chkconfig: - 80 12
# description: Redis is an open source, advanced key-value store.
#
# processname: redis-server
# config: /usr/local/redis/etc/redis.conf
# pidfile: /var/run/redis.pid
source /etc/init.d/functions
BIN="/usr/local/redis/bin"
CONFIG="/usr/local/redis/etc/redis.conf"
PIDFILE="/var/run/redis.pid"
### Read configuration
[ -r "$SYSCONFIG" ] && source "$SYSCONFIG"
RETVAL=0
prog="redis-server"
desc="Redis Server"
start() {
        if [ -e $PIDFILE ];then
             echo "$desc already running...."
             exit 1
        fi
        echo -n $"Starting $desc: "
        daemon $BIN/$prog $CONFIG
        RETVAL=$?
        echo
        [ $RETVAL -eq 0 ] && touch /var/lock/subsys/$prog
        return $RETVAL
}
stop() {
        echo -n $"Stop $desc: "
        killproc $prog
        RETVAL=$?
        echo
        [ $RETVAL -eq 0 ] && rm -f /var/lock/subsys/$prog $PIDFILE
        return $RETVAL
}
restart() {
        stop
        start
}
case "$1" in
  start)
        start
        ;;
  stop)
        stop
        ;;
  restart)
        restart
        ;;
  condrestart)
        [ -e /var/lock/subsys/$prog ] && restart
        RETVAL=$?
        ;;
  status)
        status $prog
        RETVAL=$?
        ;;
   *)
        echo $"Usage: $0 {start|stop|restart|condrestart|status}"
        RETVAL=1
esac
exit $RETVAL
```
添加执行权限
```
chmod +x /etc/init.d/redis
```
测试
```
service redis start
service redis stop
```
添加开机启动
```
chkconfig --add redis
```
在使用 service redis start 可能会遇到该错误


"env: /etc/init.d/redis: No such file or directory)"


这是因为在windows下保存了该脚本文件,其中的换行符被改变了,windows中换行符为/r/n


而linux中是/n 所以这边需要把脚本做个转换,我们使用dos2unix

yum install dos2unix 

安装完成后直接执行既可

dos2unix /etc/init.d/redis

再使用service redis start 就没问题啦















