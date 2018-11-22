## 安装rpm包
```
# rpm -ivh ***.rpm　　#其中i表示安装，v表示显示安装过程，h表示显示进度
```
## 升级rpm包
```
# rpm -Uvh ***.rpm
```
## 删除软件包
```
# rpm -e PACKAGE_NAME
# rpm -e –nodeps PACKAGE_NAME    #不考虑依赖包
# rpm -e –allmatches PACKAGE_NAME    #删除所有跟PACKAGE_NAME匹配的所有版本的包
```
查询软件包
```
# rpm -q PACKAGE_NAME
# rpm -qp ***.rpm 获取当前目录下的rpm包相关信息
# rpm -qa | less 列出所有已安装的软件包
# rpm -qf /usr/sbin/httpd 查看某个文件属于哪个软件包，可以是普通文件或可执行文件，跟文件的绝对路径
# rpm -qi PACKAGE_NAME 列出已安装的这个包的标准详细信息
# rpm -ql PACKAGE_NAME 列出rpm包的文件内容
# rpm -q –scripts kernel | less 列出已安装rpm包自带的安装前和安装后脚本
# rpm -qa –queryformat ‘Package %{NAME} was build on %{BUILDHOST}\n’ |less queryformat强大的查询
# rpm –querytags | less 可以列出queryformat可以使用的所有变量从而组合成更强大的查询
```
