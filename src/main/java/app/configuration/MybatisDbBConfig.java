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
		factoryBean.setDataSource(ds2); // 使用titan数据源, 连接titan库 
		return factoryBean.getObject(); 
		}

	@Bean 
	public SqlSessionTemplate sqlSessionTemplate2() throws Exception {
		SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory2()); // 使用上面配置的Factory 
		return template; 
	}

}

