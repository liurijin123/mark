## 安装NDNsim
### 支持的平台
ndn-cxx使用持续集成，并已在以下平台上经过测试：

+ Ubuntu 16.04（amd64，armhf，i386）
+ Ubuntu 18.04（amd64）
+ Ubuntu 19.10（amd64）
+ macOS 10.13
+ macOS 10.14
+ macOS 10.15
+ CentOS 7（带有gcc 7和Boost 1.58.0）

ndn-cxx可以在以下平台上运行，尽管它们不受官方支持：

+ Debian> = 9
+ Fedora> = 24
+ Gentoo Linux
+ Raspbian> = 2017年8月16日
+ FreeBSD 11.2
### 1.1 核心依赖（必须装）

+ GCC> = 5.3或clang> = 3.6
+ python2> = 2.7或python3> = 3.4
+ Boost库> = 1.58
+ pkg-config
+ SQLite 3.x
+ OpenSSL> = 1.0.2
+ Apple安全框架（仅在macOS上）

以下是每个平台上安装编译器的详细步骤，所有必需的开发工具和库以及ndn-cxx必备软件。

**macOS**

从App Store安装Xcode，或至少安装命令行工具（）xcode-select --install

如果使用Homebrew（推荐），请在终端中输入以下内容：

>brew install boost openssl pkg-config

注意

如果使用Homebrew安装依赖项后执行了主要操作系统升级，请记住要重新安装所有软件包。

**Ubuntu**

在终端中，输入：

>sudo apt install build-essential libboost-all-dev libssl-dev libsqlite3-dev pkg-config python-minimal

**Fedora**

在终端中，输入：

>sudo yum install gcc-g++ sqlite-devel boost-devel openssl-devel

**FreeBSD**

在终端中，输入：

>sudo pkg install python pkgconf sqlite3 boost-libs

### 1.2 NS-3 Python bindings 的依赖组件（可视化必装）

要构建教程，手册页和API文档，需要安装以下依赖项：

doxygen

graphviz

python-sphinx

sphinxcontrib-doxylink

以下列出了常见平台上安装这些必备组件的步骤：

**在具有Homebrew和pip的macOS上：**

```
brew install doxygen graphviz

sudo pip install sphinx sphinxcontrib-doxylink
```

**在Ubuntu上：**

```
sudo apt install doxygen graphviz python3-pip

sudo pip3 install sphinx sphinxcontrib-doxylink
```

**在Fedora上：**

```
sudo yum install doxygen graphviz python-sphinx

sudo pip install sphinxcontrib-doxylink
```

**在FreeBSD上：**

```
sudo pkg install doxygen graphviz py27-sphinx
```

### 1.3 下载ndnsim源代码

ndnSIM包由三部分组成：

+ NS-3的自定义分支，包含一些有用的补丁
+ 一个自定义的python绑定生成库（如果你想使用NS-3的python绑定和/或可视化模块，则是必需的）
+ ndnSIM模块的源代码修改了ndn-cxx库和NDN转发守护进程（NFD）的源代码，作为git子模块附加到ndnSIM git存储库

打开命令行

输入

```
mkdir ndnSIM
cd ndnSIM
git clone https://github.com/named-data-ndnSIM/ns-3-dev.git ns-3
git clone https://github.com/named-data-ndnSIM/pybindgen.git pybindgen
git clone --recursive https://github.com/named-data-ndnSIM/ndnSIM.git ns-3/src/ndnSIM
```

最后一个命令下载所有子模块的ndnSIM源代码和源代码（即ndn-cxx和NFD）。

### 1.4 编译并运行ndnSIM

ndnSIM使用标准的NS-3编译过程。 通常，以下命令应足以配置和构建启用了python绑定的ndnSIM：
```
cd /ndnSIM/ns-3/
./waf configure --enable-examples
./waf
```
### 1.5 使用ndnSIM进行仿真

#### 示例模拟场景

当NS-3配置了–with-examples标志时，您可以直接运行本教程的示例部分中描述的所有示例。

例如，要运行ndn-simple.cpp场景，可以运行以下命令：

>./waf --run=ndn-simple

要运行ndn-grid.cpp场景：

>./waf --run=ndn-grid

要在启用NS-3的日志记录模块的情况下运行示例模拟方案（请注意，这仅在NS-3以调试模式编译时才有效，也就是单纯进行./waf configure 而不加 -d optimized）：

>NS_LOG=ndn.Producer:ndn.Consumer ./waf --run=<scenario name 如 ndn-simple>

如果你已经使用python绑定编译，那么你可以尝试使用visualizer运行这些模拟(参见1.2)：

>./waf --run=ndn-simple --vis

#### 真实仿真实验

仿真场景可以直接在scratch /或src / ndnSIM / examples文件夹中的NS-3内编写。替代和推荐的方法是在单独的存储库中编写模拟方案，与NS-3或ndnSIM无关。 例如，您可以使用以下模板编写扩展，模拟方案和度量标准处理脚本：

http://github.com/cawka/ndnSIM-scenario-template:

事实上，GitHub上已经有了两个成熟的模板

https://github.com/spirosmastorakis/scenario-ChronoSync

https://github.com/named-data-ndnSIM/scenario-ndn-ping/tree/master/scenarios
