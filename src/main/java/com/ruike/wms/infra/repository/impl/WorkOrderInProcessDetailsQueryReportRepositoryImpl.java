package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.api.dto.WorkOrderInProcessDetailsQueryReportDTO;
import com.ruike.wms.domain.repository.WorkOrderInProcessDetailsQueryReportRepository;
import com.ruike.wms.domain.vo.WorkOrderInProcessDetailsQueryReportVO;
import com.ruike.wms.infra.mapper.WorkOrderInProcessDetailsQueryReportMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.sys.MtUserClient;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * @description 工单在制明细查询报表
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/18
 * @time 14:35
 * @version 0.0.1
 * @return
 */
@Component
public class WorkOrderInProcessDetailsQueryReportRepositoryImpl  implements WorkOrderInProcessDetailsQueryReportRepository {

    @Autowired
    WorkOrderInProcessDetailsQueryReportMapper mapper;


    /**
     * @description 工单在制明细查询报表
     * @param tenantId
     * @param pageRequest
     * @param dto
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2020/11/18
     * @time 15:31
     * @version 0.0.1
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WorkOrderInProcessDetailsQueryReportVO>
     */
    @Override
    @ProcessLovValue
    public Page<WorkOrderInProcessDetailsQueryReportVO> list(Long tenantId, PageRequest pageRequest, WorkOrderInProcessDetailsQueryReportDTO dto) {

        // 获取当前用户Id
        Long currentUserId = MtUserClient.getCurrentUserId();

        // 查询方法
        Page<WorkOrderInProcessDetailsQueryReportVO> result = PageHelper.doPage(pageRequest, () -> mapper.list(tenantId, currentUserId, dto));

        // 设置呆滞时间
        for (WorkOrderInProcessDetailsQueryReportVO vo : result) {
            //是否冻结
            if(StringUtils.isBlank(vo.getFreezeFlag())){
                vo.setFreezeFlag("N");
            }
            // 设置时间标准
            vo.setTimeStardand("24h");
            // 判断加工开始时间是否有值， 没有则设置成最大数据
            if(vo.getWorkingDate()==null){
                vo.setTimeDifferenceStr("OO");
                vo.setTimeFlag("否");
            }else {
                // 校验加工结束时间是否有值， 有则对加工时间进行转换
                if(vo.getCompletedDate()!=null){

                    // 加工结束时间-加工开始时间 获得总时长
                    Long restTime  = vo.getCompletedDate().getTime()-vo.getWorkingDate().getTime();

                    // 转成XXXX天XX时xx分xx秒
                    vo.setTimeDifferenceStr(transforDateTimeToString(restTime));

                    // 判断时间是否大于24h 否则呆滞时间为否，否则填是
                    if(((int)(restTime/(24*3600*1000)))>0){
                        vo.setTimeFlag("是");
                    }else {
                        vo.setTimeFlag("否");
                    }
                }else {
                    Calendar ca = Calendar.getInstance();
                    // 加工结束时间-加工开始时间 获得总时长
                    Long restTime  = ca.getTimeInMillis()-vo.getWorkingDate().getTime();

                    // 转成XXXX天XX时xx分xx秒
                    vo.setTimeDifferenceStr(transforDateTimeToString(restTime));

                    // 判断时间是否大于24h 否则呆滞时间为否，否则填是
                    if(((int)(restTime/(24*3600*1000)))>0){
                        vo.setTimeFlag("是");
                    }else {
                        vo.setTimeFlag("否");
                    }
                }

            }
        }
        return result;
    }

    private  String transforDateTimeToString(Long time){

        // 初始化剩余时间
        Long timeRest = time;
        // 获取天数
        int day = (int)(time/(24*3600*1000));
        timeRest = timeRest%(24*3600*1000);

        // 天数转为XXXX格式
        String dayStr = String.valueOf(day);
        while (dayStr.length()<2){
            dayStr= '0'+dayStr;
        }

        // 获取小时
        int hour = (int)(timeRest/(3600*1000));
        timeRest = timeRest%(3600*1000);

        // 获取分
        int minute = (int)(timeRest/(60*1000));
        timeRest = timeRest%(60*1000);

        // 获取秒
        int second =(int)(timeRest/1000);

        // 日期格式转换
        StringBuilder sb = new StringBuilder();
        sb.append(dayStr);
        sb.append("天");
        sb.append(hour<10?('0'+String.valueOf(hour)):hour);
        sb.append("时");
        sb.append(minute<10?('0'+String.valueOf(minute)):minute);
        sb.append("分");
        sb.append(second<10?('0'+String.valueOf(second)):second);
        sb.append("秒");

        return sb.toString();
    }
}
