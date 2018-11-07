package app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import app.po.Num;
import app.po.gdClass;
import app.service.RepairOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@EnableAutoConfiguration
@RefreshScope
@ComponentScan
@Api("repairorder")
@RequestMapping("/repairorder")
public class RepairOrderController {

	@Autowired
	private RepairOrderService repairOrderService;
	
	@RequestMapping(value="/WorkOrdersAreClassifiedToday",method=RequestMethod.GET) 
	@ResponseBody
	@ApiOperation("今日工单分类")
    public List<gdClass>  WorkOrdersAreClassifiedToday(){ 
		
		return repairOrderService.findgdClassification();
    }
    @RequestMapping(value="/WorkOrdersSettledYesterday",method=RequestMethod.GET) 
    @ResponseBody
    @ApiOperation("昨日工单解决情况")
    public List<Num>  WorkOrdersSettledYesterday(){ 
    	
    	return repairOrderService.findWorkOrdersSettledYesterday();
    }
    @RequestMapping(value="/TodaysWorksheetSource",method=RequestMethod.GET) 
    @ResponseBody
    @ApiOperation("今日工单来源")
    public List<gdClass>  TodaysWorksheetSource(){ 
    	
    	return repairOrderService.TodaysWorksheetSource();
    }
    
    @RequestMapping(value="/NewRrdersHaveBeenAddedRecently",method=RequestMethod.GET) 
    @ResponseBody
    @ApiOperation("最近新增工单")
    public List<Map>  NewRrdersHaveBeenAddedRecently(){ 
    
    	return repairOrderService.NewRrdersHaveBeenAddedRecently();
    }
    
    @RequestMapping(value="/HotSpotFault",method=RequestMethod.GET) 
    @ResponseBody
    @ApiOperation("今日热点故障")
    public List<Map>  HotSpotFault(){ 
    
    	return repairOrderService.HotSpotFault();
    }
    @RequestMapping(value="/WorkOrdersDistributionToday",method=RequestMethod.GET) 
    @ResponseBody
    @ApiOperation("今日工单分配情况")
    public List<Map>  WorkOrdersDistributionToday(){ 
    	
    	return repairOrderService.WorkOrdersDistributionToday();
    }
    @RequestMapping(value="/SingleServiceFailureToday",method=RequestMethod.GET) 
    @ResponseBody
    @ApiOperation("今日服务单故障情况")
    public List<Map>  SingleServiceFailureToday(){ 
    	
    	return repairOrderService.SingleServiceFailureToday();
    }
    @RequestMapping(value="/TimeAllocationOfWorkOrder",method=RequestMethod.GET) 
    @ResponseBody
    @ApiOperation("工单时间分配")
    public List<Map>  TimeAllocationOfWorkOrder(){ 
    	
    	return repairOrderService.TimeAllocationOfWorkOrder();
    }
    
}
