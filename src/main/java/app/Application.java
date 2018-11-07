package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.swagger2.annotations.EnableSwagger2;


//一般你启动springboot项目，都会写一个有@SpringBootApplication注解的类
//你在这个注解中添加exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class}
//即可无数据库运行
//@SpringBootApplication//(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableSwagger2
@EnableDiscoveryClient
@ServletComponentScan
@ComponentScan("app")
public class Application { 

	public static void main(String[] args) { 		
		SpringApplication.run(Application.class, args); 	
	}
}
