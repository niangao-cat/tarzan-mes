package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeCosFunctionMaterialTimeService;
import com.ruike.hme.domain.entity.HmeCosFunctionMaterialTime;
import com.ruike.hme.domain.repository.HmeCosFunctionMaterialTimeRepository;
import com.ruike.hme.infra.mapper.HmeCosFunctionMaterialTimeMapper;
import com.ruike.hme.infra.util.CommonUtils;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * COS投料性能时间记录表应用服务默认实现
 *
 * @author penglin.sui@hand-china.com 2021-06-23 18:09:37
 */
@Service
public class HmeCosFunctionMaterialTimeServiceImpl implements HmeCosFunctionMaterialTimeService {

    @Autowired
    private HmeCosFunctionMaterialTimeMapper hmeCosFunctionMaterialTimeMapper;

    @Autowired
    private HmeCosFunctionMaterialTimeRepository hmeCosFunctionMaterialTimeRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    /**
     * 查询上次同步的时间范围
     *
     * @param tenantId 租户ID
     * @return
     */
    private HmeCosFunctionMaterialTime selectPreTime(Long tenantId) {
        //查询上次同步的时间
        List<HmeCosFunctionMaterialTime> hmeCosFunctionMaterialTimeList = hmeCosFunctionMaterialTimeMapper.selectByCondition(Condition.builder(HmeCosFunctionMaterialTime.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(HmeCosFunctionMaterialTime.FIELD_TENANT_ID, tenantId))
                .build());
        if(CollectionUtils.isEmpty(hmeCosFunctionMaterialTimeList)){
            //当前反冲库位下反冲物料【{1}】现有量不足
            throw new MtException("HME_COS_FUNCTION_MATERIAL_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_FUNCTION_MATERIAL_001", "HME" , "hme_cos_function_material_time"));
        }else{
            return hmeCosFunctionMaterialTimeList.get(0);
        }
    }

    /**
     * 时间计算
     *
     * @param date 日期
     * @return java.util.Date
     */
    private Date dateCalculate(Date date , int calendarType , int diff){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendarType, diff);
        Date returnDate = calendar.getTime();
        return returnDate;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCosFunctionMaterialTime selectCurrentTime(Long tenantId) {
        HmeCosFunctionMaterialTime preCosFunctionMaterialTime = selectPreTime(tenantId);
        HmeCosFunctionMaterialTime resultMaterialTime = new HmeCosFunctionMaterialTime();
        Date startTime = dateCalculate(preCosFunctionMaterialTime.getEndTime() , Calendar.SECOND , 1);
        Date endTime = dateCalculate(preCosFunctionMaterialTime.getEndTime() , Calendar.MINUTE , 30);
        resultMaterialTime.setStartTime(startTime);
        resultMaterialTime.setEndTime(endTime);

        return resultMaterialTime;
    }
}
