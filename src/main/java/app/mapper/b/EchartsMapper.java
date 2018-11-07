package app.mapper.b;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import app.po.AnOverviewOfTheData;
import app.po.PieEcharts;

@Mapper
public interface EchartsMapper {

	@Select("SELECT COUNT(0) title,'物理机' as name from res_pm_unit")
	List<AnOverviewOfTheData> findPmCount();
	
	@Select("SELECT COUNT(0) title,'虚拟机' as name from res_vm_unit")
	List<AnOverviewOfTheData> findVmCount();
	
	@Select("SELECT COUNT(0) title,'容器' as name from res_docker_unit")
	List<AnOverviewOfTheData> findDockerCount();
	
	@Select("SELECT COUNT(0) title,'存储' as name from res_cc_unit")
	List<AnOverviewOfTheData> findCcCount();

	@Select("SELECT COUNT(0) value ,t1.business_code as name from res_vm_unit t1 GROUP BY t1.business_code order by value desc LIMIT 10")
	List<PieEcharts> findVmPie();
	
	@Select("SELECT COUNT(0) value ,t1.organization_code as name from res_docker_unit t1 GROUP BY t1.organization_code order by value desc LIMIT 10")
	List<PieEcharts> findDockerPie();

	@Select("SELECT COUNT(0) value ,DATE_FORMAT( t1.creatTime, '%Y-%m-%d') as name from res_vm_unit t1 GROUP BY DATE_FORMAT( t1.creatTime, '%Y-%m-%d')")
	List<PieEcharts> findVmLine();


}
