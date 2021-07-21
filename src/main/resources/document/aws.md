## aws相关

### 虚拟服务器:EC2

Elastic-Compute-Cloud,弹性计算云


### 存储对象文件服务器:S3:Amazon Simple Storage Service

存储桶对象，可以添加生命周期，可以归档到Glacier
S3可以托管静态网站


### 数据存储服务器:EBS:Amazon Elastic Block Store
虚拟硬盘,可支持ssd和机械硬盘的实例存储，做共享文件服务器，通过NFS客户端


### 关系数据库服务器:RDS:Amazon Relational Database Service

RDS和DynomoDB,前者是传统的关系型数据库,目前包括Mysql,PostgrepSQL, Oracle和 MS SQL; 后者是NoSQL

采用asw cloudwatch监控数据库各项指标：可用存储空间、CPU利用率、可用内存等等

nosql如果不想用dynamoDB，也可以自己在ec2里创建其他，比如mongodb;
dynamoDB直接托管、一键部署、自动扩容


### cloudwatch 监控服务器状态,恢复


### ELB 负载均衡器:Elastic Load Balancing
对外暴露负载均衡服务器即可+ OpenFeign做代理访问 + API Gateway；调用 API gateway 的 endpoint


### SQS 简单消息队列服务:Simple Queue Service


###  aws lambda:Amazon Serverless Application Model(Amazon SAM）
lambda 配合sam一起实用

