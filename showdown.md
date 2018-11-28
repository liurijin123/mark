这个插件使用方法十分简单，使用方法如下：

1.新建一个html文件，添加js引用，代码如下：
```
<!DOCTYPE html>
<html>
<head>
    <title>MarkDown</title>
    <script type="text/javascript" src="showdown.min.js"></script>
</head>
</style>
<body>

</body>
</html>
```
2.然后，我们添加一个<textare>用来输入markdown内容，再添加一个div来显示markdown的输出结果。然后，编写javascript代码，取出用户输入的内容，然后把导出的html代码显示在div中，整体页面代码如下：
```
<!DOCTYPE html>
<html>
<head>
    <title>MarkDown</title>
    <script type="text/javascript" src="showdown.min.js"></script>
</head>
<style>
    body {
      font-family: "Helvetica Neue", Helvetica, Microsoft Yahei, Hiragino Sans GB, WenQuanYi Micro Hei, sans-serif;
     font-size: 16px;
      line-height: 1.42857143;
      color: #333;
      background-color: #fff;
    }
    ul li {
        line-height: 24px;
    }
    blockquote {
        border-left:#eee solid 5px;
        padding-left:20px;
    }
    code {
        color:#D34B62;
        background: #F9F2F4;
    }
</style>
<body>
<div>
    <textarea id="content" style="height:400px;width:600px;" onkeyup="compile()"></textarea>
    <div id="result"></div>

</div>
<script type="text/javascript">
function compile(){
    var text = document.getElementById("content").value;
    var converter = new showdown.Converter();
    var html = converter.makeHtml(text);
    document.getElementById("result").innerHTML = html;
}
</script>
</body>
</html>
```
这里我们给<textare>添加onkeyup事件，这样就可以实时的看到编辑结果了。页面运行效果如下：

![](https://images0.cnblogs.com/blog2015/622439/201505/061739215795401.png)




