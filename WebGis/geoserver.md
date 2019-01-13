当使用openlayers加载geoserver的WFS服务时，会遇到跨域访问错误。网上有很多处理方法，这里介绍一种我试用成功的方法。

在geoserver的安装目录中，找到geoserver/webapps/geoserver/WEB-INF/web.xml文件，然后找到以下两快，然后取消该两处的注释。最后重启geoserver服务即可。

openlayers 版本：4.6.4

geoserver 版本：2.12.1
```
<filter>
    <filter-name>cross-origin</filter-name>
    <filter-class>org.eclipse.jetty.servlets.CrossOriginFilter</filter-class>
</filter>
```
```
<filter-mapping>
   <filter-name>cross-origin</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```
