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

5. 启动项目并访问 http://127.0.0.1:8081，进入eureka管理界面说明eureka注册中心到此已经启动好了

[截图]

> ***踩坑：*** 
>
> 
>
> 1. 项目启动不起来，因为包的依赖冲突，将包去掉，重新编译启动即可
>
>    ![image-20210406090530071](C:\Users\Lenovo\AppData\Roaming\Typora\typora-user-images\image-20210406090530071.png)
>
>    ![image-20210406090759633](C:\Users\Lenovo\AppData\Roaming\Typora\typora-user-images\image-20210406090759633.png)
>
> 2. 如何开启Run DashBoard，因为在微服务中运营一个项目可能会启动很多个服务，而为了方便管理Idea提供了一个控制面板堆所有服务进行统一的维护，如图：
>
>    【截图】
>
>    但在我创建完项目，并没有找到Run DashBoard在哪里，请教小度后，网上给的大多数方案是在`.idea` 文件中修改配置，但修改后依然无法显示。在阅贴无数后我找到了问题出在了哪里，idea在XXX版本后将Run DashBoard合并进了services。所以只要将services面板打开，就能发现控制界面了。
>
>    【截图】



###	Eureka客户端配置

1. 创建一个Spring Boot 项目。

2. 引入SpringCloud 依赖。

   ```java
      <dependency>
               <groupId>org.springframework.cloud</groupId>
               <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
           </dependency>
   
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-web</artifactId>
           </dependency>
   ```

   

3. 添加`@EnableDiscoveryClient`注解

   ```java
   @EnableDiscoveryClient
   @SpringBootApplication
   public class EurekaClientAApplication {
       public static void main(String[] args) {
           SpringApplication.run(EurekaClientAApplication.class, args);
       }
   }
   ```

   

4. 创建配置文件，并添加eureka-client的配置

   ```yaml
   server:
     port: 8101
   spring:
     application:
       name: erueka-client-a
   eureka:
     client:
       register-with-eureka: true
       fetch-registry: true
       service-url:
         defaultZone: http://127.0.0.1:8081/eureka/
   ```

5. 启动项目，并刷新eureka管理界面，发现在client中多了一行，证明client服务已经启动，并已成功交给client进行管理

   【截图】

> ***采坑***
>
> 1. 在引入springcloud 后项目无法启动
>
>    【截图】
>
>    可以从报错中大致推断出是引入的`eureka-client`版本与springboot版本不兼容。所以去查询了springboot与`eureka-client` 的版本对应表却只得到了这样一张关系图：
>
>    【截图】
>
>    这里只有springboot与springcloud的版本对应关系，好像并没有解决我们的问题。于是我去git中查看大佬们都是引用的什么版本，在翻阅了很多个项目后，发现了一个隐藏配置
>
>    ```yaml
>        <dependencyManagement>
>        <dependencies>
>            <dependency>
>                <groupId>org.springframework.cloud</groupId>
>                <artifactId>spring-cloud-dependencies</artifactId>
>                <version>2020.0.2</version>
>                <type>pom</type>
>                <scope>import</scope>
>            </dependency>
>        </dependencies>
>        </dependencyManagement>
>        
>          <dependencies>
>            <dependency>
>                <groupId>org.springframework.cloud</groupId>
>                <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
>            </dependency>
>          </dependencies>
>    
>    ```
>
>    大佬们的项目中并没有单独维护`eureka-client` 的版本，而是通过一个·`dependencyManagement`标签进行控制。
>
>    如法炮制，并参照上图springboot与springcloud的版本对应表修改了version属性，重新编译启动，成功！

###	服务调用——Ribbon

> 注册中心已经有了，客户端也已经有了，那么服务与服务之间是如何调用的呢。
>
> 目前常见的服务调用有`Ribbon` 与`feign` 两种方式。

1.  在刚才创建的服务中编写一个controller

   【截图】

2. 创建一个新的springboot项目，作为第二个client (两个client一个充当服务提供者，一个充当服务使用者)

3. 在新建的项目中的启动入口添加RestTemplate对象

   ```java
   @Bean
   @LoadBalanced
   RestTemplate restTemplate(){
       return new RestTemplate();
   }
   ```

4. 在新的项目中创建一个controller 作为服务调用者

   ```java
   @RestController
   public class Test {
   
       @Autowired
       RestTemplate restTemplate;
       
       @RequestMapping(value = "getServiceB_Ribbon")
       public String getServiceB_Ribbon() {
           return restTemplate.getForObject("http://erueka-client-b/hello", String.class);
       }
   }
   ```

###	服务调用——feign

###	Eureka注册中心添加认证



## day 2

### Spring Cloud Hystrix

1. Hystrix 是spring 提供的一套具有服务容错，线程隔离等服务的框架，常用于微服务中做服务熔断，服务降级，线程隔离，请求缓存，请求合并等功能。
2. 为什么要使用Hystrix，在微服务中经常是服务之间互相调用，例如A调用B，而B中又依赖于C，在这样一个服务调用链中，如果底层服务（C)，因为各种原因不能正常提供服务，则B就会一直等待C的响应，而A也会因为B的等待而无响应。因此在微服务中任何一个环节出现问题都会对整个系统造成相当大的影响。所以需要有一个监听器来实时监听各个服务的生命特征，当一个服务出现了异常情况，监听器需要告诉请求该服务的其他服务，这个服务现在不能正常提供服务，不要死等这个服务，而造成整个服务链条的故障。

### Hystrix 引入

1. 引入maven包

   ```
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
   </dependency>
   ```

2. 在Spring启动类上添加`@EnableCircuitBreaker` 注解

   ```java
   @EnableCircuitBreaker
   @EnableDiscoveryClient
   @SpringBootApplication
   public class EurekaClientAApplication {
       
       public static void main(String[] args) {
           SpringApplication.run(EurekaClientAApplication.class, args);
       }
       @Bean
       @LoadBalanced
       RestTemplate restTemplate(){
           return new RestTemplate();
       }
   }
   ```

3. 

### 服务降级

​	

### 服务熔断

### 线程隔离

### 请求缓存

### 请求合并





Eureka配置中的enable-self-preservation的作用？

如何开启Run DashBoard