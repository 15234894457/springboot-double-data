package app.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource{

	private static final Logger log = LogManager.getLogger(DynamicDataSource.class);

	@Override 
	protected Object determineCurrentLookupKey() { 
		System.out.println("数据源为{}"+DataSourceContextHolder.getDB());
		log.debug("数据源为{}", DataSourceContextHolder.getDB()); 
		return DataSourceContextHolder.getDB(); 
	}
	
}
