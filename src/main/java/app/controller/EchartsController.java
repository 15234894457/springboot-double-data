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

import app.po.PieEcharts;
import app.service.EchartsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@EnableAutoConfiguration
@RefreshScope
@ComponentScan
@Api("echarts")
@RequestMapping("/echarts")
public class EchartsController {
	
	 @Autowired
	 private EchartsService echartsService;

	 @RequestMapping(value="/findCount",method=RequestMethod.GET) 
	 @ResponseBody
	 @ApiOperation("数据概览总数")
     public List<Map>  findCount(){ 
		
		 return echartsService.findCount();
     }
	 @RequestMapping(value="/findVmPie",method=RequestMethod.GET) 
	 @ResponseBody
	 @ApiOperation("vmPie")
	 public List<PieEcharts>  findVmPie(){ 
		 
		 return echartsService.findVmPie();
	 }
	 @RequestMapping(value="/findDockerPie",method=RequestMethod.GET) 
	 @ResponseBody
	 @ApiOperation("dockerPie")
	 public List<PieEcharts>  findDockerPie(){ 
		 
		 return echartsService.findDockerPie();
	 }
	 @RequestMapping(value="/findVmLine",method=RequestMethod.GET) 
	 @ResponseBody
	 @ApiOperation("VmLine")
	 public List<PieEcharts>  findVmLine(){ 
		 
		 return echartsService.findVmLine();
	 }
}
