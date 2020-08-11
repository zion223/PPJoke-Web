# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Web Starter](https://docs.spring.io/spring-boot/docs/{bootVersion}/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

├── HELP.md
├── mvnw
├── mvnw.cmd
├── pom.xml
├── server.iml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── demo
│   │   │               ├── ApiResponse.java                接口数据返回包装
│   │   │               ├── DemoApplication.java            程序入口类
│   │   │               ├── JsonConverterConfig.java        Json转换中文乱码
│   │   │               ├── JsonNullConfig.java             Jso转换null转""
│   │   │               ├── Swagger2Config.java             在线Api文档
│   │   │               ├── SwaggerMvcConfig.java           同上
│   │   │               ├── controller                      控制器
│   │   │               │   ├── CommentController.java      评论相关的接口处理
│   │   │               │   ├── FeedsController.java        帖子相关的接口处理
│   │   │               │   ├── TagListController.java      标签相关接口处理
│   │   │               │   ├── UgcController.java          互动行为接口处理
│   │   │               │   └── UserController.java         用户相关的接口处理
│   │   │               ├── mapper                          数据库查询-接口定义的地方
│   │   │               │   ├── CommentMapper.java          
│   │   │               │   ├── FeedsMapper.java
│   │   │               │   ├── MockFeedsMapper.java
│   │   │               │   ├── MockUserMapper.java
│   │   │               │   ├── TagListMapper.java
│   │   │               │   ├── UgcCommentMapper.java
│   │   │               │   ├── UgcMapper.java
│   │   │               │   └── UserMapper.java
│   │   │               ├── mock                            构造mock数据
│   │   │               │   ├── MockFeedsController.java    构造mock数据的接口控制器
│   │   │               │   ├── comments                     
│   │   │               │   │   ├── CommentJsonParse.java   解析评论json
│   │   │               │   │   ├── FeedsComment.java       解析帖子json
│   │   │               │   │   └── comment.json            评论数据构造的json文件
│   │   │               │   ├── feeds                       
│   │   │               │   │   ├── HotFeeds.java           帖子的javaBean
│   │   │               │   │   ├── HotJsonParse.java       解析帖子的
│   │   │               │   │   └── mock.json               帖子数据构造的json文件
│   │   │               │   ├── taglist                      
│   │   │               │   │   ├── TagList.java            标签javaBean
│   │   │               │   │   ├── TagListJsonParse.java   解析标签json数据的
│   │   │               │   │   └── mock.json               标签数据构造的json文件
│   │   │               │   └── user 
│   │   │               │       └── MockUserController.java 构造用户信息数据的控制器
│   │   │               └── table
│   │   │                   ├── TableComment.java            各个java实体Bean以及数据库表
│   │   │                   ├── TableFeedUgc.java
│   │   │                   ├── TableHotFeeds.java
│   │   │                   ├── TableTagList.java
│   │   │                   ├── TableUser.java
│   │   │                   └── User.java
│   │   └── resources
│   │       ├── application.yml                             程序配置的类 相当于android主工程的build.gradle
│   │       ├── com
│   │       │   └── example
│   │       │       └── demo
│   │       │           └── mapper                          mapper接口真正的实现的地方，写法符合mybatis3.6语法
│   │       │               ├── CommentMapper.xml          
│   │       │               ├── FeedsMapper.xml
│   │       │               ├── MockFeedsMapper.xml
│   │       │               ├── MockUserMapper.xml
│   │       │               ├── TagListMapper.xml
│   │       │               ├── UgcCommentMapper.xml
│   │       │               ├── UgcMapper.xml
│   │       │               └── UserMapper.xml
│___|_______|_____________________________________________



