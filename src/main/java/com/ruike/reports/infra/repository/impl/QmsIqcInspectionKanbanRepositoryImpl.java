package com.ruike.reports.infra.repository.impl;

import com.ruike.reports.api.dto.QmsIqcInspectionKanbanQueryDTO;
import com.ruike.reports.api.dto.QmsSupplierQualityQueryDTO;
import com.ruike.reports.domain.repository.QmsIqcInspectionKanbanRepository;
import com.ruike.reports.domain.vo.ChartsResultVerticalAxisVO;
import com.ruike.reports.domain.vo.ChartsSquareResultVO;
import com.ruike.reports.domain.vo.QmsIqcInspectionKanbanVO;
import com.ruike.reports.infra.mapper.QmsIqcInspectionKanbanMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.CollectorsUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtUserOrganizationRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.qms.infra.constant.QmsConstants.ConstantValue.YES;

/**
 * <p>
 * IQC检验看板 资源库实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/28 10:54
 */
@Component
public class QmsIqcInspectionKanbanRepositoryImpl implements QmsIqcInspectionKanbanRepository {
    private final QmsIqcInspectionKanbanMapper qmsIqcInspectionKanbanMapper;

    public QmsIqcInspectionKanbanRepositoryImpl(QmsIqcInspectionKanbanMapper qmsIqcInspectionKanbanMapper) {
        this.qmsIqcInspectionKanbanMapper = qmsIqcInspectionKanbanMapper;
    }

    @Autowired
    private MtUserOrganizationRepository userOrganizationRepository;

    @Autowired
    private MtErrorMessageRepository errorMessageRepository;

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    public String defaultSiteUi(Long tenantId) {
        // 获取用户默认站点
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        MtUserOrganization userOrganization = new MtUserOrganization();
        userOrganization.setUserId(userId);
        userOrganization.setOrganizationType("SITE");
        MtUserOrganization defaultSite =
                userOrganizationRepository.userDefaultOrganizationGet(tenantId, userOrganization);
        if (null == defaultSite) {
            throw new MtException("MT_PERMISSION_0006", errorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_PERMISSION_0006", "PERMISSION", "【API:defaultSiteUi】"));
        }
        return defaultSite.getOrganizationId();
    }

    @Override
    public Page<QmsIqcInspectionKanbanVO> pagedKanbanList(Long tenantId, QmsIqcInspectionKanbanQueryDTO dto, PageRequest pageRequest) {
        String siteId = defaultSiteUi(tenantId);
        Page<QmsIqcInspectionKanbanVO> page = PageHelper.doPage(pageRequest, () -> qmsIqcInspectionKanbanMapper.selectKanbanList(tenantId, dto, siteId));
        if (CollectionUtils.isNotEmpty(page.getContent())) {
            page.getContent().forEach(this::rateCalculate);
        }
        return page;
    }

    @Override
    public List<QmsIqcInspectionKanbanVO> kanbanListGet(Long tenantId, QmsIqcInspectionKanbanQueryDTO dto) {
        String siteId = defaultSiteUi(tenantId);
        List<QmsIqcInspectionKanbanVO> list = qmsIqcInspectionKanbanMapper.selectKanbanList(tenantId, dto, siteId);
        list.forEach(this::rateCalculate);
        return list;
    }

    @Override
    public Page<QmsIqcInspectionKanbanVO> pagedQualityList(Long tenantId, QmsSupplierQualityQueryDTO dto, PageRequest pageRequest) {
        String siteId = defaultSiteUi(tenantId);
        Page<QmsIqcInspectionKanbanVO> page = PageHelper.doPage(pageRequest, () -> qmsIqcInspectionKanbanMapper.selectQualityList(tenantId, dto, siteId));
        if (CollectionUtils.isNotEmpty(page.getContent())) {
            page.getContent().forEach(this::rateCalculate);
        }
        return page;
    }

    @Override
    public List<QmsIqcInspectionKanbanVO> qualityListGet(Long tenantId, QmsSupplierQualityQueryDTO dto) {
        String siteId = defaultSiteUi(tenantId);
        List<QmsIqcInspectionKanbanVO> list = qmsIqcInspectionKanbanMapper.selectQualityList(tenantId, dto, siteId);
        list.forEach(this::rateCalculate);
        return list;
    }

    @Override
    public ChartsSquareResultVO qualityChartMapGet(Long tenantId, QmsSupplierQualityQueryDTO dto) {
        String siteId = defaultSiteUi(tenantId);
        List<QmsIqcInspectionKanbanVO> list = qmsIqcInspectionKanbanMapper.selectQualityList(tenantId, dto, siteId);
        list.forEach(this::rateCalculate);
        ChartsSquareResultVO result = new ChartsSquareResultVO();
        // 按物料和供应商维度汇总
        Map<String, BigDecimal> map = list.stream().collect(Collectors.groupingBy(rec -> rec.getMaterialName() + "_" + rec.getSupplierName(), CollectorsUtil.summingBigDecimal(rec -> Optional.ofNullable(YES.equals(dto.getOkFlag()) ? rec.getOkRate() : rec.getNgRate()).orElse(BigDecimal.ZERO))));
        // 矩阵横纵轴重组
        List<String> materialList = list.stream().map(QmsIqcInspectionKanbanVO::getMaterialName).distinct().collect(Collectors.toList());
        result.setHorizontalAxisList(materialList);
        List<ChartsResultVerticalAxisVO> verticalAxisList = new ArrayList<>();
        Set<String> supplierSet = list.stream().map(QmsIqcInspectionKanbanVO::getSupplierName).collect(Collectors.toSet());
        // 先循环物料，后循环供应商，将结果展开成矩阵
        supplierSet.forEach(supplierName -> {
            ChartsResultVerticalAxisVO yAxis = new ChartsResultVerticalAxisVO();
            yAxis.setVerticalAxis(supplierName);
            List<String> valueList = new ArrayList<>();
            materialList.forEach(materialName -> {
                String key = materialName + "_" + supplierName;
                valueList.add(map.getOrDefault(key, BigDecimal.ZERO).toPlainString());
            });
            yAxis.setValueList(valueList);
            verticalAxisList.add(yAxis);
        });
        result.setVerticalAxisList(verticalAxisList);
        return result;
    }

    private void rateCalculate(QmsIqcInspectionKanbanVO vo) {
        if (vo.getTotalNum().compareTo(BigDecimal.ZERO) > 0) {
            vo.setOkRate(vo.getOkNum().divide(vo.getTotalNum(), 6, BigDecimal.ROUND_HALF_UP).multiply(ONE_HUNDRED).setScale(2, BigDecimal.ROUND_HALF_UP));
            vo.setNgRate(vo.getNgNum().divide(vo.getTotalNum(), 6, BigDecimal.ROUND_HALF_UP).multiply(ONE_HUNDRED).setScale(2, BigDecimal.ROUND_HALF_UP));
        }
    }
}
