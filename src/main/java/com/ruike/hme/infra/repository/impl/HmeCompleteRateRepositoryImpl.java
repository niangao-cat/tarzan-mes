package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.vo.HmeMonthlyPlanVO;
import com.ruike.hme.infra.mapper.HmeCompleteRateMapper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeCompleteRate;
import com.ruike.hme.domain.repository.HmeCompleteRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 月度计划表 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2021-07-16 14:36:03
 */
@Component
public class HmeCompleteRateRepositoryImpl extends BaseRepositoryImpl<HmeCompleteRate> implements HmeCompleteRateRepository {

    @Autowired
    private HmeCompleteRateMapper hmeCompleteRateMapper;
    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void batchDelateCompleteRate(Long tenantId, String month) {
        hmeCompleteRateMapper.batchDelateCompleteRate(tenantId, month);
    }

    @Override
    public void batchInsertCompleteRate(Long tenantId, List<HmeMonthlyPlanVO> resultList, String month) {
        List<String> ids = this.customDbRepository.getNextKeys("hme_complete_rate_s", resultList.size());
        List<String> cIds = this.customDbRepository.getNextKeys("hme_complete_rate_cid_s", resultList.size());
        int index = 0;
        Date nowDate = new Date();
        List<String> sqlList = new ArrayList<>();
        for (HmeMonthlyPlanVO hmeMonthlyPlanVO:resultList) {
            HmeCompleteRate hmeCompleteRate = new HmeCompleteRate();
            hmeCompleteRate.setTenantId(tenantId);
            hmeCompleteRate.setCompleteRateId(ids.get(index));
            hmeCompleteRate.setAreaId(hmeMonthlyPlanVO.getAreaId());
            hmeCompleteRate.setAreaCode(hmeMonthlyPlanVO.getAreaCode());
            hmeCompleteRate.setAreaName(hmeMonthlyPlanVO.getAreaName());
            hmeCompleteRate.setProdLineId(hmeMonthlyPlanVO.getProdLineId());
            hmeCompleteRate.setProdLineCode(hmeMonthlyPlanVO.getProdLineCode());
            hmeCompleteRate.setProdLineName(hmeMonthlyPlanVO.getProdLineName());
            hmeCompleteRate.setMaterialId(hmeMonthlyPlanVO.getMaterialId());
            hmeCompleteRate.setMaterialCode(hmeMonthlyPlanVO.getMaterialCode());
            hmeCompleteRate.setMaterialName(hmeMonthlyPlanVO.getMaterialName());
            hmeCompleteRate.setPlanQty(hmeMonthlyPlanVO.getPlanQty());
            hmeCompleteRate.setCompleteQty(hmeMonthlyPlanVO.getQty());
            hmeCompleteRate.setInstockQty(hmeMonthlyPlanVO.getActualQty());
            hmeCompleteRate.setCompleteRate(hmeMonthlyPlanVO.getPlanReachRate());
            hmeCompleteRate.setCompleteDate(month);
            hmeCompleteRate.setCid(Long.valueOf(cIds.get(index)));
            hmeCompleteRate.setObjectVersionNumber(1L);
            hmeCompleteRate.setCreationDate(nowDate);
            hmeCompleteRate.setCreatedBy(-1L);
            hmeCompleteRate.setLastUpdatedBy(-1L);
            hmeCompleteRate.setLastUpdateDate(new Date());
            sqlList.addAll(customDbRepository.getInsertSql(hmeCompleteRate));
            index++;
        }
        if(CollectionUtils.isNotEmpty(sqlList)){
            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
    }
}
