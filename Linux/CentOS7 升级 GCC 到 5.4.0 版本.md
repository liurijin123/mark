## CentOS7 升级 GCC 到 5.4.0 版本
### 首先安装CentOS7自带的GCC
>yum install gcc gcc-c++

### 下载GCC5.4源码包并解压
```
cd /usr/local/src
wget https://ftp.gnu.org/gnu/gcc/gcc-5.4.0/gcc-5.4.0.tar.bz2
tar -jxvf gcc-5.4.0.tar.bz2
```
### 下载相关依赖组件

这个过程比较慢，其中会出现超时等提示，耐心等待就好

```
cd gcc-5.4.0
./contrib/download_prerequisites
```

如果网速不好，可以使用这个地址下载
>https://github.com/liurijin123/save/raw/master/gmp-4.3.2.tar.bz2

>https://github.com/liurijin123/save/raw/master/mpc-0.8.1.tar.gz

>https://github.com/liurijin123/save/raw/master/isl-0.14.tar.bz2

>https://github.com/liurijin123/save/raw/master/mpfr-2.4.2.tar.bz2

然后自行修改./contrib/download_prerequisites文件后执行上述命令
### 建立一个文件夹存放编译的文件
```
mkdir gcc-build-5.4.0
cd gcc-build-5.4.0
/usr/local/src/gcc-5.4.0/configure --enable-checking=release --enable-languages=c,c++ --disable-multilib  
```
### 编译并安装
>make

这个过程比较久，根据机器配置，1小时以上

>make install

### 对引用进行处理
```
cd /usr/bin/
mv gcc gcc_back
mv g++ g++_back
ln -s /usr/local/bin/gcc gcc
ln -s /usr/local/bin/g++ g++
```
此时再使用查看版本命令，显示的应该就是5.4.0
```
  gcc -v
```














