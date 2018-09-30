数据库数据：

![book表](https://images0.cnblogs.com/blog/390884/201410/031916444725632.png)
book表
![stu表](https://images0.cnblogs.com/blog/390884/201410/031917185195500.png)
stu表

## 1.内连接
  内连接查询操作列出与连接条件匹配的数据行，它使用比较运算符比较被连接列的列值。
  ```
  select * from book as a,stu as b where a.sutid = b.stuid

  select * from book as a inner join stu as b on a.sutid = b.stuid
  ```
  ![](https://images0.cnblogs.com/blog/390884/201410/031919334726729.png)
## 2.外连接
  ### 2.1.左联接
  左联接：是以左表为基准，将a.stuid = b.stuid的数据进行连接，然后将左表没有的对应项显示，右表的列为NULL
  ```
    select * from book as a left join stu as b on a.sutid = b.stuid
  ```
  ![](https://images0.cnblogs.com/blog/390884/201410/031923512064134.png)
  ### 2.2.右连接
  右连接：是以右表为基准，将a.stuid = b.stuid的数据进行连接，然以将右表没有的对应项显示，左表的列为NULL
  ```
    select * from book as a right join stu as b on a.sutid = b.stuid
  ```
  ![](https://images0.cnblogs.com/blog/390884/201410/031925286448136.png)
  ### 2.3.全连接
    全连接：完整外部联接返回左表和右表中的所有行。当某行在另一个表中没有匹配行时，则另一个表的选择列表列包含空值。如果表之间有匹配行，则整个结果集行包含基表的数据值。
  ```
    select * from book as a full outer join stu as b on a.sutid = b.stuid
  ```
  ![](https://images0.cnblogs.com/blog/390884/201410/031929431919967.png)
## 3.交叉连接
  交叉连接：交叉联接返回左表中的所有行，左表中的每一行与右表中的所有行组合。交叉联接也称作笛卡尔积。
  ```
  select * from book as a cross join stu as b order by a.id
  ```
  ![](https://images0.cnblogs.com/blog/390884/201410/031933183471500.png)
