package com.ruike.itf.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeMonthlyPlan;
import com.ruike.hme.domain.repository.HmeMonthlyPlanRepository;
import com.ruike.itf.api.dto.ItfMonthlyPlanIfaceDTO2;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.itf.domain.entity.ItfMonthlyPlanIface;
import com.ruike.itf.domain.repository.ItfMonthlyPlanIfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 月度计划接口表 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2021-06-01 14:21:59
 */
@Component
public class ItfMonthlyPlanIfaceRepositoryImpl extends BaseRepositoryImpl<ItfMonthlyPlanIface> implements ItfMonthlyPlanIfaceRepository {

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private HmeMonthlyPlanRepository hmeMonthlyPlanRepository;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<ItfMonthlyPlanIface> batchInsertIface(Long tenantId, List<ItfMonthlyPlanIface> ifaceList) {
        List<String> ifaceIdList = this.customDbRepository.getNextKeys("itf_monthly_plan_iface_s", ifaceList.size());
        List<String> cIdList = this.customDbRepository.getNextKeys("itf_monthly_plan_iface_cid_s", ifaceList.size());
        Date nowDate = new Date();
        int i = 0;
        List<String> sqlList = new ArrayList<>();
        for (ItfMonthlyPlanIface itfMonthlyPlanIface:ifaceList) {
            itfMonthlyPlanIface.setTenantId(tenantId);
            itfMonthlyPlanIface.setIfaceId(ifaceIdList.get(i));
            itfMonthlyPlanIface.setStatus("N");
            itfMonthlyPlanIface.setCid(Long.valueOf(cIdList.get(i)));
            itfMonthlyPlanIface.setObjectVersionNumber(1L);
            itfMonthlyPlanIface.setCreatedBy(-1L);
            itfMonthlyPlanIface.setCreationDate(nowDate);
            itfMonthlyPlanIface.setLastUpdatedBy(-1L);
            itfMonthlyPlanIface.setLastUpdateDate(nowDate);
            i++;
            sqlList.addAll(customDbRepository.getInsertSql(itfMonthlyPlanIface));
        }
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(sqlList)){
            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
        return ifaceList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void batchUpdateIface(Long tenantId, List<ItfMonthlyPlanIface> ifaceList) {
        Date nowDate = new Date();
        List<String> sqlList = new ArrayList<>();
        for (ItfMonthlyPlanIface itfMonthlyPlanIface:ifaceList) {
            itfMonthlyPlanIface.setLastUpdatedBy(-1L);
            itfMonthlyPlanIface.setLastUpdateDate(nowDate);
            sqlList.addAll(customDbRepository.getUpdateSql(itfMonthlyPlanIface));
        }
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(sqlList)){
            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void batchInsertMonthlyPlan(Long tenantId, List<HmeMonthlyPlan> hmeMonthlyPlanList) {
        List<String> monthlyPlanIdList = this.customDbRepository.getNextKeys("hme_monthly_plan_s", hmeMonthlyPlanList.size());
        List<String> cIdList = this.customDbRepository.getNextKeys("hme_monthly_plan_cid_s", hmeMonthlyPlanList.size());
        Date nowDate = new Date();
        int i = 0;
        List<String> sqlList = new ArrayList<>();
        for (HmeMonthlyPlan hmeMonthlyPlan:hmeMonthlyPlanList) {
            HmeMonthlyPlan hmeMonthlyPlanDb = hmeMonthlyPlanRepository.selectOne(new HmeMonthlyPlan() {{
                setTenantId(tenantId);
                setSiteId(hmeMonthlyPlan.getSiteId());
                setMaterialId(hmeMonthlyPlan.getMaterialId());
                setBusinessId(hmeMonthlyPlan.getBusinessId());
                setMonth(hmeMonthlyPlan.getMonth());
            }});
            if(Objects.nonNull(hmeMonthlyPlanDb)){
                //更新
                hmeMonthlyPlanDb.setQuantity(hmeMonthlyPlan.getQuantity());
                sqlList.addAll(customDbRepository.getUpdateSql(hmeMonthlyPlanDb));
            }else{
                //新增
                hmeMonthlyPlan.setTenantId(tenantId);
                hmeMonthlyPlan.setMonthlyPlanId(monthlyPlanIdList.get(i));
                hmeMonthlyPlan.setCid(Long.valueOf(cIdList.get(i)));
                hmeMonthlyPlan.setObjectVersionNumber(1L);
                hmeMonthlyPlan.setCreatedBy(-1L);
                hmeMonthlyPlan.setCreationDate(nowDate);
                hmeMonthlyPlan.setLastUpdatedBy(-1L);
                hmeMonthlyPlan.setLastUpdateDate(nowDate);
                i++;
                sqlList.addAll(customDbRepository.getInsertSql(hmeMonthlyPlan));
            }
        }
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(sqlList)){
            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
    }
}
