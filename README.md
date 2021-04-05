# spring Cloud 学习

## day 1

### 认识微服务架构

​	微服务架构包含：服务发现注册，配置中心，消息总线，负载均衡，断路器，数据监控等操作

​	服务注册发现：在微服务架构中会有一个注册中心，每个微服务都会向注册中心注册自己的地址与端口信息，注册中心维护着服务名称与服务实例的对应关系。每个微服务都会定时从注册中心获取服务列表，同时回报自己的运行状况，

​	服务注册发现也是微服务架构中的核心功能，其他的配置中心，负载均衡，断路器等都是对系统的稳定性，安全性与可扩展性进行增强。

### Eureka

​	Eureka是spring cloud 集成的服务注册与发现框架。

### Eureka注册中心配置

1. 创建一个eureka-server模块，并使用Spring Initializer初始化一个SpringBoot项目

![img](http://www.macrozheng.com/images/springcloud_eureka_01.png)

2. 选择你需要的 Spring Cloud 组件进行创建

![img](http://www.macrozheng.com/images/springcloud_eureka_03.png)

3. 在启动类上添加`@EnableEurekaServer`注解来启用 Eureka 注册中心功能

```java
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
```

4. 在配置文件`application.yml`中添加Eureka注册中心的配置

```yaml
server:
  port: 8081
spring:
  application:
    name: eureka-server
eureka:
  instance:
    hostname: 127.0.0.1
  client:
    fetch-registry: false  #指定是否要从注册中心获取服务（注册中心不需要开启）
    register-with-eureka: false #指定是否要注册到注册中心（注册中心不需要开启）
  server:
    enable-self-preservation: false #关闭保护模式
```

###	Eureka客户端配置

###	服务调用——feign

###	服务调用——Ribbon

###	Eureka注册中心添加认证

​

​



Eureka配置中的enable-self-preservation的作用？

如何开启Run DashBoard