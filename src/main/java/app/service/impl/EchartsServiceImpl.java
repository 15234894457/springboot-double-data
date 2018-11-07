package app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.configuration.DS;
import app.mapper.b.EchartsMapper;
import app.po.AnOverviewOfTheData;
import app.po.PieEcharts;
import app.service.EchartsService;

@Service
public class EchartsServiceImpl implements EchartsService {

	@Autowired
	private EchartsMapper echartsMapper;


	@DS("datasource2")
	public List<Map> findCount() {
		List<AnOverviewOfTheData> findPmCount = echartsMapper.findPmCount();
		List<AnOverviewOfTheData> findVmCount=echartsMapper.findVmCount();
		List<AnOverviewOfTheData> findDockerCount=echartsMapper.findDockerCount();
		List<AnOverviewOfTheData> findCcCount=echartsMapper.findCcCount();
		
		List<Map> lists=new ArrayList();
		 HashMap map = new HashMap();
		 map.put("title", findPmCount.get(0).getTitle());
		 map.put("name", findPmCount.get(0).getName());
		 HashMap map2 = new HashMap();
		 map2.put("title", findVmCount.get(0).getTitle());
		 map2.put("name", findVmCount.get(0).getName());
		 HashMap map3 = new HashMap();
		 map3.put("title", findDockerCount.get(0).getTitle());
		 map3.put("name", findDockerCount.get(0).getName());
		 HashMap map4 = new HashMap();
		 map4.put("title", findCcCount.get(0).getTitle());
		 map4.put("name", findCcCount.get(0).getName());
		 lists.add(map);
		 lists.add(map2);
		 lists.add(map3);
		 lists.add(map4);
		return lists;
	}


	@DS("datasource2")
	public List<PieEcharts> findVmPie() {
		List<PieEcharts> findVmPie = echartsMapper.findVmPie();
		return findVmPie;
	}


	@DS("datasource2")
	public List<PieEcharts> findDockerPie() {
		List<PieEcharts> findDockerPie = echartsMapper.findDockerPie();
		return findDockerPie;
	}


	@DS("datasource2")
	public List<PieEcharts> findVmLine() {
		List<PieEcharts> findVmLine = echartsMapper.findVmLine();
		return findVmLine;
	}


	
}
