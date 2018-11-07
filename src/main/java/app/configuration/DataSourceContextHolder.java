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
		log.debug("切换到{}数据源", dbType);
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
