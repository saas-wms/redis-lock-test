# redis-lock-test
需要注意配置文件的Redis的主机地址和端口

2018.4.29 由于原先没有说明故在此加一下
本项目是一个区别于以往的setnx,getSet的另外一种实现Redis分布式锁的方式
采用pipeline方式进行加锁，lua脚本的方式进行解锁

在clone项目之后，需要去：application.properties 
修改一下redis的IP和端口

启动项目后可访问：http://localhost:9090/swagger-ui.html
这是一个项目内置的Swagger地址，可以进行快捷调试Controller提供的REST接口
