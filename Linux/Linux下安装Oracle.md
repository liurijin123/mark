## 一、修改操作系统核心参数

在Root用户下执行以下步骤：

### 1）修改用户的SHELL的限制，修改/etc/security/limits.conf文件

输入命令：vi /etc/security/limits.conf，按i键进入编辑模式，将下列内容加入该文件。

```
oracle soft nproc 2047
 
oracle hard nproc 16384
 
oracle soft nofile 1024
 
oracle hard nofile 65536
```

编辑完成后按Esc键，输入“:wq”存盘退出

### 2）修改/etc/pam.d/login 文件，输入命令：vi /etc/pam.d/login，按i键进入编辑模式，将下列内容加入该文件。
```
session required /lib/security/pam_limits.so
 
session required pam_limits.so
```

编辑完成后按Esc键，输入“:wq”存盘退出

### 3）修改linux内核，修改/etc/sysctl.conf文件，输入命令: vi /etc/sysctl.conf ，按i键进入编辑模式，将下列内容加入该文件

```
fs.file-max = 6815744
 
fs.aio-max-nr = 1048576
 
kernel.shmall = 2097152
 
kernel.shmmax = 2147483648
 
kernel.shmmni = 4096
 
kernel.sem = 250 32000 100 128
 
net.ipv4.ip_local_port_range = 9000 65500
 
net.core.rmem_default = 4194304
 
net.core.rmem_max = 4194304
 
net.core.wmem_default = 262144
 
net.core.wmem_max = 1048576
```

编辑完成后按Esc键，输入“:wq”存盘退出

### 4）要使 /etc/sysctl.conf 更改立即生效，执行以下命令。 输入：sysctl -p 显示如下：

```
linux:~ # sysctl -p
 
net.ipv4.icmp_echo_ignore_broadcasts = 1
 
net.ipv4.conf.all.rp_filter = 1
 
fs.file-max = 6815744
 
fs.aio-max-nr = 1048576
 
kernel.shmall = 2097152
 
kernel.shmmax = 2147483648
 
kernel.shmmni = 4096
 
kernel.sem = 250 32000 100 128
 
net.ipv4.ip_local_port_range = 9000 65500
 
net.core.rmem_default = 4194304
 
net.core.rmem_max = 4194304
 
net.core.wmem_default = 262144
 
net.core.wmem_max = 1048576
```

### 5）编辑 /etc/profile ，输入命令：vi /etc/profile，按i键进入编辑模式，将下列内容加入该文件。

```
if [ $USER = "oracle" ]; then
 
if [ $SHELL = "/bin/ksh" ]; then
 
ulimit -p 16384
 
ulimit -n 65536
 
else
 
ulimit -u 16384 -n 65536
 
fi
 
fi
```
编辑完成后按Esc键，输入“:wq”存盘退出

### 6）创建相关用户和组，作为软件安装和支持组的拥有者。

创建Oracle用户和密码,输入命令： 
```
groupadd oinstall; groupadd dba

useradd -g oinstall -g dba -m oracle
 
passwd oracle
```

然后会让你输入密码，密码任意输入2次，但必须保持一致，回车确认。

### 7）创建数据库软件目录和数据文件存放目录，目录的位置，根据自己的情况来定，注意磁盘空间即可，这里我把其放到oracle用户下,例如：

输入命令：

```
mkdir /home/oracle/app
 
mkdir /home/oracle/app/oracle
 
mkdir /home/oracle/app/oradata
 
mkdir /home/oracle/app/oracle/product
```

### 8)更改目录属主为Oracle用户所有，输入命令：

```
chown -R oracle:oinstall /home/oracle/app
```

### 9)配置oracle用户的环境变量，首先，切换到新创建的oracle用户下,

输入：su – oracle ，然后直接在输入 ： vi .bash_profile

按i编辑 .bash_profile,进入编辑模式，增加以下内容：

```
export ORACLE_BASE=/home/oracle/app
 
export ORACLE_HOME=$ORACLE_BASE/oracle/product/11.2.0/dbhome_1
 
export ORACLE_SID=orcl
 
export PATH=$PATH:$HOME/bin:$ORACLE_HOME/bin
 
export LD_LIBRARY_PATH=$ORACLE_HOME/lib:/usr/lib
```
编辑完成后按Esc键，输入“:wq”存盘退出

## 二、安装过程

### 1）当上述系统要求操作全部完成后，注销系统，在图形界面以Oracle用户登陆。首先将下载的Oracle安装包复制到linux中，用SSH其他ftp工具拷贝。

打开一个终端，运行unzip命令解压oracle安装文件，如：

输入命令：

```
unzip linux.x64_11gR2_database_1of2.zip
 
unzip linux.x64_11gR2_database_2of2.zip
```

解压完成后 cd 进入其解压后的目录database

输入命令：

cd database

使用ls命令可以查看解压后database所包含的文件

### 2）执行安装，输入命令：./runInstaller(中文系统下输入：LANG=en_US ./runInstaller)

**解决依赖检查问题：我们可以从安装linux的光盘或ISO中查找所缺的包(将镜像文件挂载)**

```
# mount -o loop {linux系统镜像名} /mnt
 
# cd /mnt/Packages
```

然后使用rpm –ivh xxx.rpm 来进行安装

**解决swap size检查失败问题：**

1、使用root用户，在/tmp（随意），下执行下面语句

dd if=/dev/zero of=swapfree bs=32k count=65515

(增加swap大小为bs*count，bs为block，count为数量)

上图可以看出，通过这个语句创建了一个2G的文件swapfree

2、将创建的文件用做交换分区

执行语句：mkswap swapfree

3、开启这个交换空间

执行语句：swapon swapfree

4、通过free命令查看，交换空间在原来交换空间的基础上增加了2G

5、在/etc/fstab中加入下面两行，设置此交换分区开机启动

/dec/hdb5 swap swap defaults 0 0

/tmp/swapfree swap swap defaults 0 0

**解决安装过程中86%可能会遇到报错如出错问题：**

```
# cd /mnt/Packages
 
# rpm -ivh glibc-common-2.12-1.25.el6.x86_64.rpm
 
# rpm -ivh kernel-headers-2.6.32-131.0.15.el6.x86_64.rpm
 
# rpm -ivh libgcc-4.4.5-6.el6.x86_64.rpm
 
# rpm -ivh glibc-2.12-1.25.el6.x86_64.rpm
 
# rpm -ivh libgomp-4.4.5-6.el6.x86_64.rpm
 
# rpm -ivh nscd-2.12-1.25.el6.x86_64.rpm
 
# rpm -ivh glibc-headers-2.12-1.25.el6.x86_64.rpm
 
# rpm -ivh glibc-devel-2.12-1.25.el6.x86_64.rpm
 
# rpm -ivh mpfr-2.4.1-6.el6.x86_64.rpm
 
# rpm -ivh ppl-0.10.2-11.el6.x86_64.rpm
 
# rpm -ivh cloog-ppl-0.15.7-1.2.el6.x86_64.rpm
 
# rpm -ivh cpp-4.4.5-6.el6.x86_64.rpm
 
# rpm -ivh gcc-4.4.5-6.el6.x86_64.rpm
```

注：以上是安装gcc，软件安装顺序不能错。

```
# rpm -ivh libstdc++-4.4.5-6.el6.x86_64.rpm
 
# rpm -ivh libstdc++-devel-4.4.5-6.el6.x86_64.rpm
 
# rpm -ivh gcc-c++-4.4.5-6.el6.x86_64.rpm
```

注：以上是安装gcc-c++

根据这个方法解决了错误。

安装完成后，系统会提示你需要用root权限执行2个shell脚本。按照其提示的路径，找到其所在的位置，如：我的就在/home/oracle/app/oracle/product/11.2.0/dbhome_1/root.sh

和 /home/oracle/oraInventory/orainstRoot.sh 新开启一个终端，输入命令：

```
su – root
 
cd /home/oracle/app/oracle/product/11.2.0/dbhome_1
 
sh root.sh
 
cd /home/oracle/oraInventory
 
sh orainstRoot.sh
```










