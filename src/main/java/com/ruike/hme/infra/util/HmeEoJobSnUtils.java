package com.ruike.hme.infra.util;

import java.math.BigDecimal;
import java.util.Objects;

import com.ruike.hme.domain.entity.HmeOperationTime;
import com.ruike.hme.domain.repository.HmeOperationTimeRepository;
import com.ruike.hme.domain.vo.HmeEoJobSnVO3;
import com.ruike.hme.domain.vo.HmeRouterStepVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeOperationTimeObjectMapper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tarzan.actual.domain.vo.MtEoRouterActualVO42;

/**
 * 工序作业平台-SN作业 工具类
 *
 * @author penglin.sui@hand-china.com 2020-08-18 15:46
 */
@Component
public class HmeEoJobSnUtils {
    /**
     * 获取执行作业在制品状态
     *
     * @author penglin.sui@hand-china.com 2020-08-26 11:21
     */
    public static String eoStepWipStatusGet(HmeRouterStepVO nearStepVO){
        String status = "";
        if(Objects.nonNull(nearStepVO)){
            if(nearStepVO.getQueueQty().compareTo(BigDecimal.ZERO) > 0){
                status = HmeConstants.EoStepWipStatus.QUEUE;
            }else if(nearStepVO.getWorkingQty().compareTo(BigDecimal.ZERO) > 0) {
                status = HmeConstants.EoStepWipStatus.WORKING;
            }else if(nearStepVO.getCompletePendingQty().compareTo(BigDecimal.ZERO) > 0) {
                status = HmeConstants.EoStepWipStatus.COMPLETE_PENDING;
            }else if(nearStepVO.getCompletedQty().compareTo(BigDecimal.ZERO) > 0) {
                status = HmeConstants.EoStepWipStatus.COMPLETED;
            }else if(nearStepVO.getScrappedQty().compareTo(BigDecimal.ZERO) > 0) {
                status = HmeConstants.EoStepWipStatus.SCRAPPED;
            }else if(nearStepVO.getHoldQty().compareTo(BigDecimal.ZERO) > 0) {
                status = HmeConstants.EoStepWipStatus.HOLD;
            }
        }
        return status;
    }

    public static String fetchGroupKey(MtEoRouterActualVO42 mtEoRouterActualVO42){
        return "#" + mtEoRouterActualVO42.getReworkStepFlag() + "#" + mtEoRouterActualVO42.getSourceStatus();
    }
}