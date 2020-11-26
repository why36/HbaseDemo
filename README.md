# 金融大数据  实验三

#### 181250155 吴泓宇
 
## 1. Hbase的安装和运行

我使用了BDkit进行实验，创建容器后，参照HBase官方文档进行了单机模式下的Quick Get-started，比较简单，不再赘述:

![](figures/1.png)
![](figures/2.png)
![](figures/3.png)

然后进行伪分布模式的设置

进入`hbase-site.xml`,设置`hbase.rootdir`和`hbase.cluster.distributed`,保存并退出，然后重新启动hbase
![](figures/4.png)

此时可以查看HDFS中的Hbase目录

![](figures/5.png)

然后可以创建 backup HBase Master (HMaster) server

![](figures/6.png)

同理可以创建多个 RegionServers

![](figures/7.png)


## 3. 用shell完成

1. 创建students表
![](figures/8.png)

2. 扫描创建后的students表
![](figures/9.png)
3. 查询学生来自的省
![](figures/10.png)
4. 增加新的列Courses:English，并添加数据
![](figures/11.png)

在这期间出现了HMaster消失的问题，查看日志得知是NameNode的DataNode消失
![](figures/12.png)
![](figures/13.png)
![](figures/14.png)
![](figures/15.png)
![](figures/16.png)
![](figures/17.png)

