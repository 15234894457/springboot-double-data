package app.service;

import java.util.List;
import java.util.Map;

import app.po.AnOverviewOfTheData;
import app.po.PieEcharts;

public interface EchartsService {


	List<Map> findCount();

	List<PieEcharts> findVmPie();

	List<PieEcharts> findDockerPie();

	List<PieEcharts> findVmLine();

}
