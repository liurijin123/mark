数据库数据：

![book表](http://img.liutong.fun/031916444725632.png)
book表
![stu表](http://img.liutong.fun/20200119220156.png)
stu表

## 1.内连接
  内连接查询操作列出与连接条件匹配的数据行，它使用比较运算符比较被连接列的列值。
  ![](http://img.liutong.fun/微信图片_20200312093100.png)

  ```
  select * from book as a,stu as b where a.sutid = b.stuid

  select * from book as a inner join stu as b on a.sutid = b.stuid
  ```
  ![](http://img.liutong.fun/20200119220358.png)
## 2.外连接
  ### 2.1.左联接
  左联接：是以左表为基准，将a.stuid = b.stuid的数据进行连接，然后将左表没有的对应项显示，右表的列为NULL
  ![](http://img.liutong.fun/微信图片_20200312093107.jpg)
  ```
    select * from book as a left join stu as b on a.sutid = b.stuid
  ```
  ![](http://img.liutong.fun/20200119220426.png)
  ### 2.2.右连接
  右连接：是以右表为基准，将a.stuid = b.stuid的数据进行连接，然以将右表没有的对应项显示，左表的列为NULL
  ![](http://img.liutong.fun/微信图片_20200312093112.png)
  ```
    select * from book as a right join stu as b on a.sutid = b.stuid
  ```
  ![](http://img.liutong.fun/20200119220504.png)
  ### 2.3.全连接
    全连接：完整外部联接返回左表和右表中的所有行。当某行在另一个表中没有匹配行时，则另一个表的选择列表列包含空值。如果表之间有匹配行，则整个结果集行包含基表的数据值。
  ![](http://img.liutong.fun/微信图片_20200312093115.png)
  ```
    select * from book as a full outer join stu as b on a.sutid = b.stuid
  ```
  ![](http://img.liutong.fun/20200119220523.png)
## 3.交叉连接
  交叉连接：交叉联接返回左表中的所有行，左表中的每一行与右表中的所有行组合。交叉联接也称作笛卡尔积。
  ```
  select * from book as a cross join stu as b order by a.id
  ```
  ![](http://img.liutong.fun/20200119220547.png)
