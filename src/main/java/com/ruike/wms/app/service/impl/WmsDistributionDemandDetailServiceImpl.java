package com.ruike.wms.app.service.impl;

import com.ruike.wms.app.service.WmsDistributionDemandDetailService;
import com.ruike.wms.domain.entity.WmsDistDemandSubstitution;
import com.ruike.wms.domain.entity.WmsDistributionDemandDetail;
import com.ruike.wms.domain.repository.WmsDistDemandSubstitutionRepository;
import com.ruike.wms.domain.repository.WmsDistributionDemandDetailRepository;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.service.BaseServiceImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 配送平台明细 服务实现
 *
 * @author penglin.sui@hand-china.com 2020-07-22 15:45
 */
@Service
public class WmsDistributionDemandDetailServiceImpl extends BaseServiceImpl<WmsDistributionDemandDetail> implements WmsDistributionDemandDetailService {
    private final WmsDistributionDemandDetailRepository wmsDistributionDemandDetailRepository;
    private final WmsDistDemandSubstitutionRepository wmsDistDemandSubstitutionRepository;

    public WmsDistributionDemandDetailServiceImpl(WmsDistributionDemandDetailRepository wmsDistributionDemandDetailRepository, WmsDistDemandSubstitutionRepository wmsDistDemandSubstitutionRepository) {
        this.wmsDistributionDemandDetailRepository = wmsDistributionDemandDetailRepository;
        this.wmsDistDemandSubstitutionRepository = wmsDistDemandSubstitutionRepository;
    }

    @Override
    public List<WmsDistributionDemandDetail> selectListByDemandId(Long tenantId, String distDemandId) {
        List<WmsDistributionDemandDetail> list = wmsDistributionDemandDetailRepository.selectListByDemandId(tenantId, distDemandId);
        List<WmsDistributionDemandDetail> sortedList = list.stream().filter(rec -> WmsConstant.CONSTANT_N.equals(rec.getSubstituteFlag())).collect(Collectors.toList());
        // 原始行增加行号
        AtomicInteger index = new AtomicInteger(1);
        sortedList.forEach(rec -> rec.setLineNum(index.getAndIncrement()));
        Map<String, Integer> sourceMap = sortedList.stream().collect(Collectors.toMap(WmsDistributionDemandDetail::getDemandDetailId, WmsDistributionDemandDetail::getLineNum, (key1, key2) -> key1));
        // 对替代行增加行号以及来源行号
        Map<String, List<WmsDistributionDemandDetail>> substitutionMap = list.stream().filter(rec -> WmsConstant.CONSTANT_Y.equals(rec.getSubstituteFlag())).collect(Collectors.groupingBy(WmsDistributionDemandDetail::getSourceDetailId));
        substitutionMap.forEach((sourceDetailId, subList) -> {
            subList.forEach(sub -> {
                sub.setLineNum(index.getAndIncrement());
                sub.setSourceLineNum(sourceMap.get(sourceDetailId));
            });
            sortedList.addAll(subList);
        });
        return sortedList;
    }

    /**
     * 替代维护校验
     *
     * @param substitution 替代数据
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/31 09:52:01
     */
    private void substitutionValidate(WmsDistributionDemandDetail substitution) {
        List<WmsDistributionDemandDetail> substitutionList = wmsDistributionDemandDetailRepository.selectByCondition(Condition.builder(WmsDistributionDemandDetail.class).
                andWhere(Sqls.custom().andEqualTo(WmsDistributionDemandDetail.FIELD_SOURCE_DETAIL_ID, substitution.getSourceDetailId())
                        .andNotEqualTo(WmsDistributionDemandDetail.FIELD_DEMAND_DETAIL_ID, substitution.getDemandDetailId(), true)).build());
        BigDecimal substitutionQty = substitutionList.stream().map(WmsDistributionDemandDetail::getRequirementQty).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (substitution.getComponentDemandQty().compareTo(substitutionQty) < 0) {
            throw new CommonException("替代数量总和大于组件需求总数量");
        }
    }

    /**
     * 新增替代关系
     *
     * @param tenantId     租户
     * @param substitution 替代数据
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/31 09:53:35
     */
    private void insertSubstitutionRelation(Long tenantId, WmsDistributionDemandDetail substitution, WmsDistributionDemandDetail detail) {
        WmsDistDemandSubstitution wmsDistDemandSubstitution = new WmsDistDemandSubstitution();
        wmsDistDemandSubstitution.setTenantId(tenantId);
        wmsDistDemandSubstitution.setDemandDetailId(substitution.getDemandDetailId());
        wmsDistDemandSubstitution.setDistDemandId(substitution.getDistDemandId());
        wmsDistDemandSubstitution.setComponentMaterialId(detail.getMaterialId());
        wmsDistDemandSubstitution.setComponentQty(detail.getDispatchQty().multiply(detail.getUsageQty()));
        wmsDistDemandSubstitution.setSubstituteMaterialId(substitution.getMaterialId());
        wmsDistDemandSubstitution.setSubstituteQty(substitution.getDispatchQty().multiply(substitution.getUsageQty()));
        wmsDistDemandSubstitution.setSubstituteDate(new Date());
        wmsDistDemandSubstitutionRepository.insertSelective(wmsDistDemandSubstitution);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void insertOrUpdateSubstitution(Long tenantId, WmsDistributionDemandDetail substitution) {
        // 验证数量和替代关系
        this.substitutionValidate(substitution);

        //计算扣减数量
        BigDecimal changeQty;
        // 保存数据，根据ID判断新增或更新
        if (Objects.isNull(substitution.getDemandDetailId())) {
            changeQty = substitution.getRequirementQty();
            substitution.setSubstituteFlag(WmsConstant.CONSTANT_Y);
            this.insertSelective(substitution);
        } else {
            WmsDistributionDemandDetail oldSubstitution = wmsDistributionDemandDetailRepository.selectByPrimaryKey(substitution.getDemandDetailId());
            changeQty = substitution.getRequirementQty().subtract(oldSubstitution.getRequirementQty());
            wmsDistributionDemandDetailRepository.updateByPrimaryKey(substitution);
        }

        // 扣减替代数量
        WmsDistributionDemandDetail detail = wmsDistributionDemandDetailRepository.selectByPrimaryKey(substitution.getSourceDetailId());
        if (changeQty.compareTo(BigDecimal.ZERO) != 0) {
            detail.setRequirementQty(detail.getRequirementQty().subtract(changeQty));
            wmsDistributionDemandDetailRepository.updateByPrimaryKey(detail);
        }
        // 新增替代关系表
        this.insertSubstitutionRelation(tenantId, substitution, detail);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void removeSubstitution(Long tenantId, WmsDistributionDemandDetail substitution) {
        if (WmsConstant.CONSTANT_Y.equals(substitution.getSubstituteFlag())) {
            BigDecimal returnQty = substitution.getRequirementQty();
            WmsDistributionDemandDetail detail = wmsDistributionDemandDetailRepository.selectByPrimaryKey(substitution.getSourceDetailId());
            detail.setRequirementQty(detail.getRequirementQty().add(returnQty));
            wmsDistributionDemandDetailRepository.deleteByPrimaryKey(substitution.getDemandDetailId());
            wmsDistributionDemandDetailRepository.updateByPrimaryKey(detail);
        } else {
            throw new CommonException("仅能删除替代料行");
        }
    }
}
