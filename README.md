# redis-lock-test
需要注意配置文件的Redis的主机地址和端口

2018.4.29 由于原先没有说明故在此加一下<br> 
本项目是一个区别于以往的setnx,getSet的另外一种实现Redis分布式锁的方式<br> 
采用set命令方式进行加锁，lua脚本的方式进行解锁<br> 

在clone项目之后，需要去：application.properties <br> 
修改一下redis的IP和端口<br> 

启动项目后可访问：http://localhost:9090/swagger-ui.html<br> 
这是一个项目内置的Swagger地址，可以进行快捷调试Controller提供的REST接口<br> 

备注：开发工具（idea,eclipse）需要装lombok插件

### 2018/05/15 更新 ###
本锁在Redis集群，极端情况会出现多个客户端同时获得锁
极端情况如下：
- 客户端A从master获取到锁
- 在master将锁同步到slave之前，master宕掉了。
- slave节点被晋级为master节点
- 客户端B取得了同一个资源被客户端A已经获取到的另外一个锁。安全失效！

使用本方式的锁的最安全方式就是单实例Redis
