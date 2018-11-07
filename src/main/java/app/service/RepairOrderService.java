package app.service;

import java.util.List;
import java.util.Map;

import app.po.Num;
import app.po.gdClass;

public interface RepairOrderService {

	List<gdClass> findgdClassification();

	List<Num> findWorkOrdersSettledYesterday();

	List<gdClass> TodaysWorksheetSource();

	List<Map> NewRrdersHaveBeenAddedRecently();

	List<Map> HotSpotFault();

	List<Map> WorkOrdersDistributionToday();

	List<Map> SingleServiceFailureToday();

	List<Map> TimeAllocationOfWorkOrder();

}
