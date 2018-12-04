# 一、安装FastDFS环境
## 1、下载安装 libfastcommon
libfastcommon是从 FastDFS 和 FastDHT 中提取出来的公共 C 函数库，基础环境，安装即可 。
(1)下载libfastcommon
```
# wget https://github.com/happyfish100/libfastcommon/archive/V1.0.7.tar.gz
```
(2)解压
```
# tar -zxvf V1.0.7.tar.gz
# cd libfastcommon-1.0.7
```
(3)编译、安装
```
# ./make.sh
# ./make.sh install
```
(4)libfastcommon.so 安装到了/usr/lib64/libfastcommon.so，但是FastDFS主程序设置的lib目录是/usr/local/lib，所以需要创建软链接。
```
# ln -s /usr/lib64/libfastcommon.so /usr/local/lib/libfastcommon.so
# ln -s /usr/lib64/libfastcommon.so /usr/lib/libfastcommon.so
# ln -s /usr/lib64/libfdfsclient.so /usr/local/lib/libfdfsclient.so
# ln -s /usr/lib64/libfdfsclient.so /usr/lib/libfdfsclient.so 
```
## 2、下载安装FastDFS
(1)下载FastDFS
```
# wget https://github.com/happyfish100/fastdfs/archive/V5.05.tar.gz
```
(2)解压
```
# tar -zxvf V5.05.tar.gz
# cd fastdfs-5.05
```
(3)编译、安装
```
# ./make.sh
# ./make.sh install
```
(4)默认安装方式安装后的相应文件与目录
　　A、服务脚本：
```
/etc/init.d/fdfs_storaged
/etc/init.d/fdfs_tracker
```
　　B、配置文件（这三个是作者给的样例配置文件） :
```
/etc/fdfs/client.conf.sample
/etc/fdfs/storage.conf.sample
/etc/fdfs/tracker.conf.sample
```
　　C、命令工具在 /usr/bin/ 目录下：
```
fdfs_appender_test
fdfs_appender_test1
fdfs_append_file
fdfs_crc32
fdfs_delete_file
fdfs_download_file
fdfs_file_info
fdfs_monitor
fdfs_storaged
fdfs_test
fdfs_test1
fdfs_trackerd
fdfs_upload_appender
fdfs_upload_file
stop.sh
restart.sh
```
(5)FastDFS 服务脚本设置的 bin 目录是 /usr/local/bin， 但实际命令安装在 /usr/bin/ 下。

　　两种方式：

　　一是修改FastDFS 服务脚本中相应的命令路径，也就是把 /etc/init.d/fdfs_storaged 和 /etc/init.d/fdfs_tracker 两个脚本中的 /usr/local/bin 修改成 /usr/bin。

 　　　　# vim fdfs_trackerd
     
　　　　使用查找替换命令进统一修改:%s+/usr/local/bin+/usr/bin
    
　　　　# vim fdfs_storaged
    
　　　　使用查找替换命令进统一修改:%s+/usr/local/bin+/usr/bin
    
   二是建立 /usr/bin 到 /usr/local/bin 的软链接，我是用这种方式。　　
```
# ln -s /usr/bin/fdfs_trackerd   /usr/local/bin
# ln -s /usr/bin/fdfs_storaged   /usr/local/bin
# ln -s /usr/bin/stop.sh         /usr/local/bin
# ln -s /usr/bin/restart.sh      /usr/local/bin
``` 
## 3、配置FastDFS跟踪器(Tracker)
配置文件详细说明参考：FastDFS 配置文件详解

(1)进入 /etc/fdfs，复制 FastDFS 跟踪器样例配置文件 tracker.conf.sample，并重命名为 tracker.conf。
```
# cd /etc/fdfs
# cp tracker.conf.sample tracker.conf
# vim tracker.conf
```
(2)编辑tracker.conf ，标红的需要修改下，其它的默认即可。
```
# 配置文件是否不生效，false 为生效
disabled=false

# 提供服务的端口
port=22122

# Tracker 数据和日志目录地址(根目录必须存在,子目录会自动创建)
base_path=/ljzsg/fastdfs/tracker

# HTTP 服务端口
http.server_port=80
```
(3)创建tracker基础数据目录，即base_path对应的目录
```
  # mkdir -p /ljzsg/fastdfs/tracker
```
(4)防火墙中打开跟踪端口（默认的22122）
```
# vim /etc/sysconfig/iptables

添加如下端口行：
-A INPUT -m state --state NEW -m tcp -p tcp --dport 22122 -j ACCEPT

重启防火墙：
# service iptables restart
```
(5)启动Tracker

初次成功启动，会在 /ljzsg/fdfsdfs/tracker/ (配置的base_path)下创建 data、logs 两个目录。
```
可以用这种方式启动
# /etc/init.d/fdfs_trackerd start

也可以用这种方式启动，前提是上面创建了软链接，后面都用这种方式
# service fdfs_trackerd start
```
查看 FastDFS Tracker 是否已成功启动 ，22122端口正在被监听，则算是Tracker服务安装成功。
```
# netstat -unltp|grep fdfs
```
(6)设置Tracker开机启动
```
# chkconfig fdfs_trackerd on

或者：
# vim /etc/rc.d/rc.local
加入配置：
/etc/init.d/fdfs_trackerd start 
```
(7)tracker server 目录及文件结构 

Tracker服务启动成功后，会在base_path下创建data、logs两个目录。目录结构如下：
```
${base_path}
  |__data
  |   |__storage_groups.dat：存储分组信息
  |   |__storage_servers.dat：存储服务器列表
  |__logs
  |   |__trackerd.log： tracker server 日志文件 
```
## 4、配置 FastDFS 存储 (Storage)
(1)进入 /etc/fdfs 目录，复制 FastDFS 存储器样例配置文件 storage.conf.sample，并重命名为 storage.conf
```
# cd /etc/fdfs
# cp storage.conf.sample storage.conf
# vim storage.conf
```
(2)编辑storage.conf

标红的需要修改，其它的默认即可。
```
# 配置文件是否不生效，false 为生效
disabled=false 

# 指定此 storage server 所在 组(卷)
group_name=group1

# storage server 服务端口
port=23000

# 心跳间隔时间，单位为秒 (这里是指主动向 tracker server 发送心跳)
heart_beat_interval=30

# Storage 数据和日志目录地址(根目录必须存在，子目录会自动生成)
base_path=/ljzsg/fastdfs/storage

# 存放文件时 storage server 支持多个路径。这里配置存放文件的基路径数目，通常只配一个目录。
store_path_count=1


# 逐一配置 store_path_count 个路径，索引号基于 0。
# 如果不配置 store_path0，那它就和 base_path 对应的路径一样。
store_path0=/ljzsg/fastdfs/file

# FastDFS 存储文件时，采用了两级目录。这里配置存放文件的目录个数。 
# 如果本参数只为 N（如： 256），那么 storage server 在初次运行时，会在 store_path 下自动创建 N * N 个存放文件的子目录。
subdir_count_per_path=256

# tracker_server 的列表 ，会主动连接 tracker_server
# 有多个 tracker server 时，每个 tracker server 写一行
tracker_server=file.ljzsg.com:22122

# 允许系统同步的时间段 (默认是全天) 。一般用于避免高峰同步产生一些问题而设定。
sync_start_time=00:00
sync_end_time=23:59
# 访问端口
http.server_port=80
```
(3)创建Storage基础数据目录，对应base_path目录
```
# mkdir -p /ljzsg/fastdfs/storage

# 这是配置的store_path0路径
# mkdir -p /ljzsg/fastdfs/file
```
(4)防火墙中打开存储器端口（默认的 23000） 
```
# vim /etc/sysconfig/iptables

添加如下端口行：
-A INPUT -m state --state NEW -m tcp -p tcp --dport 23000 -j ACCEPT

重启防火墙：
# service iptables restart
```
(5)启动 Storage

启动Storage前确保Tracker是启动的。初次启动成功，会在 /ljzsg/fastdfs/storage 目录下创建 data、 logs 两个目录。
```
可以用这种方式启动
# /etc/init.d/fdfs_storaged start

也可以用这种方式，后面都用这种
# service fdfs_storaged start
```
查看 Storage 是否成功启动，23000 端口正在被监听，就算 Storage 启动成功。
```
# netstat -unltp|grep fdfs
```
查看Storage和Tracker是否在通信：
```
/usr/bin/fdfs_monitor /etc/fdfs/storage.conf
```
(6)设置 Storage 开机启动
```
# chkconfig fdfs_storaged on

或者：
# vim /etc/rc.d/rc.local
加入配置：
/etc/init.d/fdfs_storaged start
```
(7)Storage 目录

同 Tracker，Storage 启动成功后，在base_path 下创建了data、logs目录，记录着 Storage Server 的信息。

在 store_path0 目录下，创建了N*N个子目录：
## 5、文件上传测试
(1)修改 Tracker 服务器中的客户端配置文件 
```
# cd /etc/fdfs
# cp client.conf.sample client.conf
# vim client.conf
```
修改如下配置即可，其它默认。
```
# Client 的数据和日志目录
base_path=/ljzsg/fastdfs/client

# Tracker端口
tracker_server=file.ljzsg.com:22122
```
(2)上传测试

 在linux内部执行如下命令上传 namei.jpeg 图片
```
# /usr/bin/fdfs_upload_file /etc/fdfs/client.conf namei.jpeg
```
# 二、安装Nginx
上面将文件上传成功了，但我们无法下载。因此安装Nginx作为服务器以支持Http方式访问文件。同时，后面安装FastDFS的Nginx模块也需要Nginx环境。

Nginx只需要安装到StorageServer所在的服务器即可，用于访问文件。我这里由于是单机，TrackerServer和StorageServer在一台服务器上。

## 1、安装nginx所需环境　　
(1)gcc 安装
```
# yum install gcc-c++
``
(2)PCRE pcre-devel 安装
```
# yum install -y pcre pcre-devel
```
(3)zlib 安装
```
# yum install -y zlib zlib-devel
```
(4)OpenSSL 安装
```
# yum install -y openssl openssl-devel
```
## 2、安装Nginx
(1)下载nginx
```
# wget -c https://nginx.org/download/nginx-1.12.1.tar.gz
```
(2)解压
```
# tar -zxvf nginx-1.12.1.tar.gz
# cd nginx-1.12.1
```
(3)使用默认配置
```
# ./configure
```
(4)编译、安装
```
# make
# make install
```
(5)启动nginx

```
# cd /usr/local/nginx/sbin/
# ./nginx 

其它命令
# ./nginx -s stop
# ./nginx -s quit
# ./nginx -s reload
```
(6)设置开机启动

```
# vim /etc/rc.local

添加一行：
/usr/local/nginx/sbin/nginx

# 设置执行权限
# chmod 755 rc.local
```
(7)查看nginx的版本及模块
```
/usr/local/nginx/sbin/nginx -V
```
(8)防火墙中打开Nginx端口（默认的 80） 

添加后就能在本机使用80端口访问了。

```
# vim /etc/sysconfig/iptables

添加如下端口行：
-A INPUT -m state --state NEW -m tcp -p tcp --dport 80 -j ACCEPT

重启防火墙：
# service iptables restart
```
## 3、访问文件
简单的测试访问文件

(1)修改nginx.conf

```
# vim /usr/local/nginx/conf/nginx.conf

添加如下行，将 /group1/M00 映射到 /ljzsg/fastdfs/file/data
location /group1/M00 {
    alias /ljzsg/fastdfs/file/data;
}

# 重启nginx
# /usr/local/nginx/sbin/nginx -s reload
```
(2)在浏览器访问之前上传的图片
# 三、FastDFS 配置 Nginx 模块
## 1、安装配置Nginx模块
(1)fastdfs-nginx-module 模块说明

　　FastDFS 通过 Tracker 服务器，将文件放在 Storage 服务器存储， 但是同组存储服务器之间需要进行文件复制， 有同步延迟的问题。

　　假设 Tracker 服务器将文件上传到了 192.168.51.128，上传成功后文件 ID已经返回给客户端。

　　此时 FastDFS 存储集群机制会将这个文件同步到同组存储 192.168.51.129，在文件还没有复制完成的情况下，客户端如果用这个文件 ID 在 192.168.51.129 上取文件,就会出现文件无法访问的错误。

　　而 fastdfs-nginx-module 可以重定向文件链接到源服务器取文件，避免客户端由于复制延迟导致的文件无法访问错误。

(2)下载 fastdfs-nginx-module、解压
```
# wget https://github.com/happyfish100/fastdfs-nginx-module/archive/5e5f3566bbfa57418b5506aaefbe107a42c9fcb1.zip

# 解压
# unzip 5e5f3566bbfa57418b5506aaefbe107a42c9fcb1.zip

# 重命名
# mv fastdfs-nginx-module-5e5f3566bbfa57418b5506aaefbe107a42c9fcb1  fastdfs-nginx-module-master
```
(3)配置Nginx

在nginx中添加模块

```
# 先停掉nginx服务
# /usr/local/nginx/sbin/ngix -s stop

进入解压包目录
# cd /softpackages/nginx-1.12.1/

# 添加模块
# ./configure --add-module=../fastdfs-nginx-module-master/src

重新编译、安装
# make && make install
```
(4)查看Nginx的模块
```
# /usr/local/nginx/sbin/nginx -V
```
(5)复制 fastdfs-nginx-module 源码中的配置文件到/etc/fdfs 目录， 并修改
```
# cd /softpackages/fastdfs-nginx-module-master/src

# cp mod_fastdfs.conf /etc/fdfs/
```
修改如下配置，其它默认

```
# 连接超时时间
connect_timeout=10

# Tracker Server
tracker_server=file.ljzsg.com:22122

# StorageServer 默认端口
storage_server_port=23000

# 如果文件ID的uri中包含/group**，则要设置为true
url_have_group_name = true

# Storage 配置的store_path0路径，必须和storage.conf中的一致
store_path0=/ljzsg/fastdfs/file
```
(6)复制 FastDFS 的部分配置文件到/etc/fdfs 目录
```
# cd /softpackages/fastdfs-5.05/conf/

# cp anti-steal.jpg http.conf mime.types /etc/fdfs/
```
(7)配置nginx，修改nginx.conf
```
# vim /usr/local/nginx/conf/nginx.conf
```
修改配置，其它的默认

在80端口下添加fastdfs-nginx模块
```
location ~/group([0-9])/M00 {
    ngx_fastdfs_module;
}
```
(9)启动nginx
```
# /usr/local/nginx/sbin/nginx
```
(10)在地址栏访问。

能下载文件就算安装成功。注意和第三点中直接使用nginx路由访问不同的是，这里配置 fastdfs-nginx-module 模块，可以重定向文件链接到源服务器取文件。
  
  
  
  
  
  
  
  
  
