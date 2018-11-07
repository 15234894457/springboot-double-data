package app.mapper.a;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import app.po.Num;
import app.po.gdClass;

@Mapper
public interface RepairOrderMapper {

    @Select("SELECT	count(0) num, t1.procDefName repairo FROM	Op_His_Flow_Instance t1 WHERE DATE(t1.startTime)= #{date} and t1.procDefName != '持续改进' GROUP BY	t1.procDefName")
    List<gdClass> findgdClassification(@Param("date") String date);

    @Select("SELECT	CONCAT(ROUND((SELECT	COUNT(0) num1	FROM	Op_His_Flow_Instance t1		WHERE	t1.endTime IS NOT NULL and DATE(t1.startTime)= #{date} and t1.procDefName != '持续改进') /(SELECT	COUNT(0) num2	FROM		Op_His_Flow_Instance t3 where DATE(t3.startTime)= #{date} and t3.procDefName != '持续改进')* 100, 2),'','%') num")
	List<Num> findWorkOrdersSettledYesterday(@Param("date")String date);
    
    @Select("SELECT	count(0) num, t1.procDefName repairo FROM	Op_His_Flow_Instance t1 WHERE DATE(t1.startTime)= #{date} and t1.procDefName != '持续改进' GROUP BY	t1.procDefName")
	List<gdClass> TodaysWorksheetSource(@Param("date")String date);

	@Select("select t1.NAME_ name1, t1.CREATE_TIME_ time, t3.NAME_ name2 from ACT_RU_TASK t1 , ACT_RU_IDENTITYLINK t2 , ACT_ID_GROUP t3 WHERE  t1.ID_ =t2.TASK_ID_  AND t2.GROUP_ID_=t3.ID_ and DATE(t1.CREATE_TIME_) = #{date} ")
	List<Map> NewRrdersHaveBeenAddedRecently(@Param("date")String date);

	@Select("select  COUNT(0) num , t1.platform pfName from Op_Monitor_Alarm t1 where  DATE(t1.octime)=#{date}  and  isIgnore= -1  and t1.platform !='' GROUP BY  t1.platform")
	List<Map> HotSpotFault(@Param("date")String date);
	
	@Select("SELECt	COUNT(0)count,t1.gName userteam FROM `Op_His_Flow_Instance` t1 LEFT JOIN(	SELECT	fi_fiId,MAX(CASE parameter	WHEN 'userTeam' THEN	`value`	ELSE	NULL	END	)AS userTeam	FROM	Op_His_Form_Content	GROUP BY	fi_fiId)t2 ON t1.`fiId` = t2.fi_fiId WHERE	t1.procDefName = '服务请求' AND DATE(startTime)= #{date} GROUP BY	t1.gName")
	List<Map> WorkOrdersDistributionToday(@Param("date")String date);

	@Select("SELECT	COUNT(0)count,	t2.userTeam userteam FROM	`Op_His_Flow_Instance` t1 LEFT JOIN(	SELECT		fi_fiId,		MAX(			CASE parameter			WHEN 'userTeam' THEN				`value`			ELSE				NULL			END		)AS userTeam	FROM		Op_His_Form_Content	GROUP BY		fi_fiId )t2 ON t1.`fiId` = t2.fi_fiId WHERE	t1.procDefName = '服务请求' AND DATE(startTime)= #{date} GROUP BY	t2.userTeam")
	List<Map> SingleServiceFailureToday(@Param("date")String date);

	@Select("SELECT count(0) chartValue FROM Op_His_Flow_Development w where DATE(startTime) =#{date}  and w.serviceName is not NULL   GROUP BY w.serviceName")
	List<Map> TimeAllocationOfWorkOrder(@Param("date")String date);

	
}
