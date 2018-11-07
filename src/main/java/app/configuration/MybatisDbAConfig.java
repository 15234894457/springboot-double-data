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
		factoryBean.setDataSource(ds1); // 使用titan数据源, 连接titan库 
		return factoryBean.getObject(); 
		}

	@Bean 
	public SqlSessionTemplate sqlSessionTemplate1() throws Exception {
		SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory1()); // 使用上面配置的Factory 
		return template; 
	}

}

