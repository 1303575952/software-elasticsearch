# software-elasticsearch
SpringBoot+ElasticSearch+Jest软件著作权搜索

前端待改

ESRepository.java中须配置ElasticSearch的index和type

```
public static final String INDEX = "indextest";
public static final String TYPE = "typetest";
```

localhost:8080

MySQL——>ElasticSearch数据同步需要conf下文件，参考教程https://blog.csdn.net/xiaocy66/article/details/83997248
在logstash的bin目录下执行logstash.bat -f ../config/logstash.conf