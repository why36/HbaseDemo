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

## 2. 用Java完成

该部分主要是通过调用Hbase的Java API来完成shell操作，基本上是将一个命令封装成一个函数的形式，然后在`main`函数中通过执行函数序列，来达到和执行`shell`命令相同的结果

相关函数的实现如下：

- 创建表
![](figures/18.png)

- 删除表
![](figures/19.png)

- 增加数据
![](figures/20.png)

- 获取数据
![](figures/21.png)

- 扫描表
![](figures/22.png)

- 增加列族
![](figures/23.png)

- “命令”序列

![](figures/24.png)

- 结果
![](figures/25.png)

![](figures/26.png)

可以看出我们的添加列，列族操作都成功完成



## 3. 用shell完成

1. 创建students表
![](figures/8.png)

1. 扫描创建后的students表
![](figures/9.png)
3. 查询学生来自的省
![](figures/10.png)
4. 增加新的列Courses:English，并添加数据
![](figures/11.png)



5. 增加新的列族Contact和新列Contact:Email，并添加数据
![](figures/14.png)
![](figures/15.png)

6. 删除students表，注意删除前要先将其disable
![](figures/16.png)



## 4. 遇到的问题和解决方案


### 4.1 在shell操作时HMaster消失

在shell运行期间，出现了HMaster消失的问题，查看日志得知是NameNode的DataNode消失

![](figures/12.png)

因此我尝试重新启动hdfs，但是遭遇失败...推测是bdkit的问题，故删除容器重新创建，解决

![](figures/13.png)


### 4.2 Java程序无法运行

在编写完Java程序后，用`mvn package`将其打包，起初我尝试用`java -jar`命令运行它，但报错，

![](figures/17.png)

后来发现这个必须要在hadoop下运行，因此改用`hadoop jar`命令，成功运行



### 5.实验心得

本次实验难度并不高，由于使用了bdkit，也省去了配环境上可能会遇到的问题，遇到的麻烦大多也来自于对Hbase的陌生，在多踩几次坑之后也能应对，总的来说是比较顺利的一次实验。
