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
 
    /**
     * 配置@Transactional注解事物
     * @return
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
    }


}
