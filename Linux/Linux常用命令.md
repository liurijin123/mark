## 目录操作
![](http://img.liutong.fun/640.webp)

mkdir 创建目录  make dir

cp 拷贝文件  copy

mv 移动文件   move

rm  删除文件 remove

例子：
```
 # 创建目录和父目录a,b,c,d
 mkdir -p a/b/c/d

 # 拷贝文件夹a到/tmp目录
 cp -rvf a/ /tmp/

 # 移动文件a到/tmp目录，并重命名为b
 mv -vf a /tmp/b

 # 删除机器上的所有文件
 rm -rvf /
```
ls  命令能够看到当前目录的所有内容。ls -l能够看到更多信息

pwd  命令能够看到当前终端所在的目录。

cd  切换目录。

find  find命令通过筛选一些条件，能够找到已经被遗忘的文件。
## 文本处理
![](http://img.liutong.fun/640.jpg)
### 查看文件
**cat**

最常用的就是cat命令了，注意，如果文件很大的话，cat命令的输出结果会疯狂在终端上输出，可以多次按ctrl+c终止。

```
# 查看文件大小
du -h file

# 查看文件内容
cat file
```
**less**

既然cat有这个问题，针对比较大的文件，我们就可以使用less命令打开某个文件。

类似vim，less可以在输入/后进入查找模式，然后按n(N)向下(上)查找。
有许多操作，都和vim类似，你可以类比看下。

**tail**

大多数做服务端开发的同学，都了解这么命令。比如，查看nginx的滚动日志。
```
tail -f access.log
```
tail命令可以静态的查看某个文件的最后n行，与之对应的，head命令查看文件头n行。但head没有滚动功能，就像尾巴是往外长的，不会反着往里长。
```
tail -n100 access.log
head -n100 access.log
```
### 统计
sort和uniq经常配对使用。

sort可以使用-t指定分隔符，使用-k指定要排序的列。

下面这个命令输出nginx日志的ip和每个ip的pv，pv最高的前10
```
#2019-06-26T10:01:57+08:00|nginx001.server.ops.pro.dc|100.116.222.80|10.31.150.232:41021|0.014|0.011|0.000|200|200|273|-|/visit|sign=91CD1988CE8B313B8A0454A4BBE930DF|-|-|http|POST|112.4.238.213

awk -F"|" '{print $3}' access.log | sort | uniq -c | sort -nk1 -r | head -n10
```
### 其他
**grep**

grep用来对内容进行过滤，带上--color参数，可以在支持的终端可以打印彩色，参数n则输出具体的行数，用来快速定位。
比如：查看nginx日志中的POST请求。
```
grep -rn --color POST access.log
```
**diff**

diff命令用来比较两个文件是否的差异
## 压缩
![](http://img.liutong.fun/hWy81umi.jpg)
**.tar**  使用tar命令压缩或解压

**.bz2** 使用bzip2命令操作

**.gz** 使用gzip命令操作

**.zip** 使用unzip命令解压

**.rar** 使用unrar命令解压

最常用的就是.tar.gz文件格式了。其实是经过了tar打包后，再使用gzip压缩。

创建压缩文件
```
tar cvfz  archive.tar.gz dir/
```
解压
```
tar xvfz. archive.tar.gz
```
## 日常运维
![](http://img.liutong.fun/微信图片_20200313170151.jpg)
**mount**

mount命令可以挂在一些外接设备，比如u盘，比如iso
```
mount /dev/sdb1 /xiaodianying
```
**chown**

chown 用来改变文件的所属用户和所属组。

chmod 用来改变文件的访问权限。

**yum**

假定你用的是centos，则包管理工具就是yum。如果你的系统没有wget命令，就可以使用如下命令进行安装。
```
yum install wget -y
```
**systemctl**

当然，centos管理后台服务也有一些套路。service命令就是。systemctl兼容了service命令，我们看一下怎么重启mysql服务。 推荐用下面这个。
```
service mysql restart
systemctl restart  mysqld
```
**kill**

对于普通的进程，就要使用kill命令进行更加详细的控制了。kill命令有很多信号，如果你在用kill -9，你一定想要了解kill -15以及kill -3的区别和用途。

**su**

su用来切换用户。比如你现在是root，想要用xjj用户做一些勾当，就可以使用su切换。
```
su xjj
su - xjj
```
## 系统状态概览
![](http://img.liutong.fun/N2SLWleQ.jpg)

**uname**

uname命令可以输出当前的内核信息，让你了解到用的是什么机器。
```
uname -a
```
**ps**

ps命令能够看到进程/线程状态。和top有些内容重叠，常用。
```
# 找到java进程
ps -ef|grep java
```
**top**

系统状态一览，主要查看。cpu load负载、cpu占用率。使用内存或者cpu最高的一些进程。下面这个命令可以查看某个进程中的线程状态。
```
top -H -p pid
```
**free**

top也能看内存，但不友好，free是专门用来查看内存的。包括物理内存和虚拟内存swap。

**df**

df命令用来查看系统中磁盘的使用量，用来查看磁盘是否已经到达上限。参数h可以以友好的方式进行展示。
```
df -h
```
**ifconfig**
查看ip地址，不啰嗦，替代品是ip addr命令。

**ping**

至于网络通不通，可以使用ping来探测。（不包括那些禁ping的网站）

**netstat**
虽然ss命令可以替代netstat了，但现实中netstat仍然用的更广泛一些。比如，查看当前的所有tcp连接。


