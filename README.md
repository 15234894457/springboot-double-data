# springboot-double-data
springboot-double-data


应用场景

项目需要同时连接两个不同的数据库A, B，并且它们都为主从架构，一台写库，多台读库。

多数据源

首先要将spring boot自带的DataSourceAutoConfiguration禁掉，因为它会读取application.properties文件的spring.datasource.*属性并自动配置单数据源。在@SpringBootApplication注解中添加exclude属性即可：

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

然后在application.properties中配置多数据源连接信息：

#mysql1
spring.datasource.db1.url=jdbc:mysql://10.96.140.136:3306/activiti?useUnicode=true&characterEncoding=utf-8&useSSL=false
spring.datasource.db1.username=ipdata
spring.datasource.db1.password=open2013
spring.datasource.db1.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.db1.max-idle=10
spring.datasource.db1.max-wait=10000
spring.datasource.db1.min-idle=5
spring.datasource.db1.initial-size=5

#mysql2
spring.datasource.db2.url=jdbc:mysql://10.96.140.136:3306/cmdb_resource_module?useUnicode=true&characterEncoding=utf-8&useSSL=false
spring.datasource.db2.username=ipdata
spring.datasource.db2.password=open2013
spring.datasource.db2.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.db2.max-idle=10
spring.datasource.db2.max-wait=10000
spring.datasource.db2.min-idle=5
spring.datasource.db2.initial-size=5


由于我们禁掉了自动数据源配置，因些下一步就需要手动将这些数据源创建出来：
package app.configuration;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
 
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
 
/**
 * 多数据源配置类
 * Created by pure on 2018-05-06.
 */
@Configuration
public class DataSourceConfig {
	
	//由于我们禁掉了自动数据源配置，因些下一步就需要手动将这些数据源创建出来：
    //数据源1
    @Bean(name = "datasource1")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.db1") // application.properteis中对应属性的前缀
    public DataSource dataSource1() {
        return DataSourceBuilder.create().build();
    }
 
    //数据源2
    @Bean(name = "datasource2")
    @ConfigurationProperties(prefix = "spring.datasource.db2") // application.properteis中对应属性的前缀
    public DataSource dataSource2() {
        return DataSourceBuilder.create().build();
    }
 

}

接下来需要配置两个mybatis的SqlSessionFactory分别使用不同的数据源：
package app.configuration;


import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = {"app.mapper.a"}, sqlSessionFactoryRef = "sqlSessionFactory1")
public class MybatisDbAConfig { 
	@Autowired
    @Qualifier("datasource1")
    private DataSource ds1;
	
	@Bean 
	public SqlSessionFactory sqlSessionFactory1() throws Exception { 
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean(); 
		factoryBean.setDataSource(ds1); // 使用ds1数据源, 连接ds1库 
		return factoryBean.getObject(); 
		}

	@Bean 
	public SqlSessionTemplate sqlSessionTemplate1() throws Exception {
		SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory1()); // 使用上面配置的Factory 
		return template; 
	}

}




经过上面的配置后，app.mapper.b下的Mapper接口，都会使用titan数据源。同理可配第二个SqlSessionFactory:
package app.configuration;


import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = {"app.mapper.b"}, sqlSessionFactoryRef = "sqlSessionFactory2")
public class MybatisDbBConfig { 
	@Autowired
    @Qualifier("datasource2")
    private DataSource ds2;
	
	@Bean 
	public SqlSessionFactory sqlSessionFactory2() throws Exception { 
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean(); 
		factoryBean.setDataSource(ds2); // 使用ds2数据源, 连接ds2库 
		return factoryBean.getObject(); 
		}

	@Bean 
	public SqlSessionTemplate sqlSessionTemplate2() throws Exception {
		SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory2()); // 使用上面配置的Factory 
		return template; 
	}

}


完成这些配置后，假设有2个Mapper app.mapper.a和app.mapper.b，使用前者时会自动连接ds1库，后者连接ds2库。

动态数据源

使用动态数据源的初衷，是能在应用层做到读写分离，即在程序代码中控制不同的查询方法去连接不同的库。除了这种方法以外，数据库中间件也是个不错的选择，它的优点是数据库集群对应用来说只暴露为单库，不需要切换数据源的代码逻辑。

我们通过自定义注解 + AOP的方式实现数据源动态切换。

首先定义一个DataSourceContextHolder, 用于保存当前线程使用的数据源名：
package app.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class DataSourceContextHolder {

	 private static Logger log = LogManager.getLogger(DataSourceContextHolder.class);	
	 /**
     * 默认数据源
     */
	public static final String DEFAULT_DS = "db1";
	
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<>(); 
	// 设置数据源名 
	public static void setDB(String dbType) {
		System.out.println("切换到{}数据源"+ dbType);
		contextHolder.set(dbType); 
	} 
	// 获取数据源名
	public static String getDB() { 
		return (contextHolder.get());
	} 
	// 清除数据源名
	public static void clearDB() { 
		contextHolder.remove(); 
	}


}


然后自定义一个javax.sql.DataSource接口的实现，这里只需要继承Spring为我们预先实现好的父类AbstractRoutingDataSource即可：
package app.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource{

	private static final Logger log = LogManager.getLogger(DynamicDataSource.class);

	@Override 
	protected Object determineCurrentLookupKey() { 
		System.out.println("数据源为{}"+DataSourceContextHolder.getDB());
		return DataSourceContextHolder.getDB(); 
	}
	
}

创建动态数据源：
 /**
     * 动态数据源: 通过AOP在不同数据源之间动态切换
     * @return
     */
    @Bean(name = "datasource1")
    public DataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        // 默认数据源
        dynamicDataSource.setDefaultTargetDataSource(dataSource1());
        // 配置多数据源
        Map<Object, Object> dsMap = new HashMap<>(2);
        dsMap.put("datasource1", dataSource1());
        dsMap.put("datasource2", dataSource2());
 

        dynamicDataSource.setTargetDataSources(dsMap);
        
        return dynamicDataSource;
    }
    
    自定义注释@DS用于在编码时指定方法使用哪个数据源：
    package app.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
 
/**
 * 自定义注释@DS用于在编码时指定方法使用哪个数据源：
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DS {
    String value() default "datasource1";
}

编写AOP切面，实现切换逻辑：
package app.configuration;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
/**
 * 自定义注解 + AOP的方式实现数据源动态切换。
 * Created by pure on 2018-05-06.
 */
@Aspect
@Component
public class DynamicDataSourceAspect {
 
    @Before("@annotation(DS)")
    public void beforeSwitchDS(JoinPoint point){
        //获得当前访问的class
        Class<?> className = point.getTarget().getClass();
        //获得访问的方法名
        String methodName = point.getSignature().getName();
        //得到方法的参数的类型
        Class[] argClass = ((MethodSignature)point.getSignature()).getParameterTypes();
        String dataSource = DataSourceContextHolder.DEFAULT_DS;
        try {
            // 得到访问的方法对象
            Method method = className.getMethod(methodName, argClass);
            // 判断是否存在@DS注解
            if (method.isAnnotationPresent(DS.class)) {
                DS annotation = method.getAnnotation(DS.class);
                // 取出注解中的数据源名
                dataSource = annotation.value();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 切换数据源
        DataSourceContextHolder.setDB(dataSource);
    }
    
    @After("@annotation(DS)")
    public void afterSwitchDS(JoinPoint point){
        DataSourceContextHolder.clearDB();
    }
}

完成上述配置后，在先前SqlSessionFactory配置中指定使用DynamicDataSource就可以在Service中愉快的切换数据源了：
@DS("datasource2")
	public List<PieEcharts> findVmPie() {
		List<PieEcharts> findVmPie = echartsMapper.findVmPie();
		return findVmPie;
	}
  
  
  
  
  本人博客：https://www.cnblogs.com/-flq/p/9921336.html

