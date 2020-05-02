# LunaSQL
jar下载：http://hikki.top/archives/
<pre>
功能模块：
-数据库管理
-----图形化操作CURD
-----自定义sql/脚本处理
-----表结构/索引管理
-----日志管理

-压力测试(性能评估)
-----数据库参数管理
-----自设计性能评估
---------对应参数，串行执行测试用例
---------并发测试
---------测试日志
-----mysqlslap的部分功能图形化界面实现

-sql优化
-----soar的部分功能图形化界面实现(https://github.com/XiaoMi/soar)

</pre>

<hr>
由于需要适配webswing，开发环境为jdk11<br>
使用需要配置JRE（java版本8以上)<br>
20.3.12<br>
利用webswing框架，也可以在web端使用LunaSQL，满足远程操作服务器的需求（webswing支持的java版本为8-11）。<br>
针对Linux系统做了相应的调整，已部署至云服务器 OS:CentOS 7.3<br>
<br>
20.4.28
更改若干问题，发布jar测试


