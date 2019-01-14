当使用openlayers加载geoserver的WFS服务时，会遇到跨域访问错误。网上有很多处理方法，这里介绍一种我试用成功的方法。

在geoserver的安装目录中，找到geoserver/webapps/geoserver/WEB-INF/web.xml文件，然后找到以下两快，然后取消该两处的注释。最后重启geoserver服务即可。

openlayers 版本：5.3.0

geoserver 版本：2.13.1
```
    <filter>
		<filter-name>CorsFilter</filter-name>
		<filter-class>org.apache.catalina.filters.CorsFilter</filter-class>
		<init-param>
			<param-name>cors.allowed.origins</param-name>
			<param-value>*</param-value>
		</init-param>
	</filter>
```
```
    <filter-mapping>
		<filter-name>CorsFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>
```
加入一个jar包
cors-filter-1.7.1.jar
