package app.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.configuration.DS;
import app.mapper.a.RepairOrderMapper;
import app.po.Num;
import app.po.gdClass;
import app.service.RepairOrderService;

@Service
public class RepairOrderServiceImpl implements RepairOrderService {

	@Autowired
	private RepairOrderMapper repairOrderMapper;

	@DS("datasource1")
	public List<gdClass> findgdClassification() {

		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

		return repairOrderMapper.findgdClassification(date);
	}

	@DS("datasource1")
	public List<Num> findWorkOrdersSettledYesterday() {
    	DateFormat d = new SimpleDateFormat("yyyy-MM-dd");
    	// System.out.println();
    	String format = d.format(new Date());
    	Calendar c = Calendar.getInstance();
    	try {
			c.setTime(d.parse(format));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	c.set(Calendar.DATE, c.get(Calendar.DATE)-1);
    	 String date = d.format(c.getTime());

		return repairOrderMapper.findWorkOrdersSettledYesterday(date);
	}

	@DS("datasource1")
	public List<gdClass> TodaysWorksheetSource() {
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

		return repairOrderMapper.TodaysWorksheetSource(date);
	}

	@DS("datasource1")
	public List<Map> NewRrdersHaveBeenAddedRecently() {
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

		return repairOrderMapper.NewRrdersHaveBeenAddedRecently(date);
	}

	@DS("datasource1")
	public List<Map> HotSpotFault() {
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

		return repairOrderMapper.HotSpotFault(date);
	}

	@DS("datasource1")
	public List<Map> WorkOrdersDistributionToday() {
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

		return repairOrderMapper.WorkOrdersDistributionToday(date);
	}

	@DS("datasource1")
	public List<Map> SingleServiceFailureToday() {
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

		return repairOrderMapper.SingleServiceFailureToday(date);
	}

	@DS("datasource1")
	public List<Map> TimeAllocationOfWorkOrder() {
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

		return repairOrderMapper.TimeAllocationOfWorkOrder(date);
	}	
}
