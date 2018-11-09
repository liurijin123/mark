## 原生JDBC操作流程
1.加载数据库驱动

2.获取连接对象

3.根据SQL获取会话对象

4.设置参数值

5.执行

6.提交

7.关闭
```
package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;

public class JDBC {

	public static void main(String[] args) {
		try {
			//加载数据库驱动
			Class.forName("com.mysql.jdbc.Driver");
			//获取连接对象
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/emoticon?characterEncoding=utf-8","root","989898");
			con.setAutoCommit(false);
			//根据SQL获取会话对象
			PreparedStatement preparedStatement = (PreparedStatement) con.prepareStatement("insert into t_user_info(id,user,sex,age) value(null,?,?,?)");
			//设置参数值
			preparedStatement.setString(1, "tom");
			preparedStatement.setString(2, "男");
			preparedStatement.setInt(3, 21);
			//执行
			preparedStatement.execute();
			//提交
			con.commit();
			//关闭
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
```
