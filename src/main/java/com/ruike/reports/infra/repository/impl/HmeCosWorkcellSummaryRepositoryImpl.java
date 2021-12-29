package com.ruike.reports.infra.repository.impl;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.domain.entity.HmeCosNcRecord;
import com.ruike.hme.domain.vo.HmeCosBarCodeExceptionVO;
import com.ruike.reports.api.dto.HmeCosWorkcellSummaryQueryDTO;
import com.ruike.reports.domain.repository.HmeCosWorkcellSummaryRepository;
import com.ruike.reports.domain.vo.HmeCosWorkcellSummaryVO;
import com.ruike.reports.infra.mapper.HmeCosWorkcellSummaryMapper;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.util.CollectorsUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * COS工位加工汇总 资源库实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/1 16:09
 */
@Component
public class HmeCosWorkcellSummaryRepositoryImpl implements HmeCosWorkcellSummaryRepository {
    private final HmeCosWorkcellSummaryMapper mapper;
    private static final MtUserInfo BLANK_USER = new MtUserInfo();
    private final MtUserClient userClient;

    public HmeCosWorkcellSummaryRepositoryImpl(HmeCosWorkcellSummaryMapper mapper, MtUserClient userClient) {
        this.mapper = mapper;
        this.userClient = userClient;
    }

    @Override
    public Page<HmeCosWorkcellSummaryVO> pageList(Long tenantId, HmeCosWorkcellSummaryQueryDTO dto, PageRequest pageRequest) {
        List<HmeCosWorkcellSummaryVO> summaryList = getSummaryList(tenantId, dto);
        Page<HmeCosWorkcellSummaryVO> page = WmsCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), summaryList);
        displayFieldsCompletion(tenantId, page.getContent());
        return page;
    }

    @Override
    public List<HmeCosWorkcellSummaryVO> list(Long tenantId, HmeCosWorkcellSummaryQueryDTO dto) {
        List<HmeCosWorkcellSummaryVO> summaryList = getSummaryList(tenantId, dto);
        displayFieldsCompletion(tenantId, summaryList);
        return summaryList;
    }

    private List<HmeCosWorkcellSummaryVO> getSummaryList(Long tenantId, HmeCosWorkcellSummaryQueryDTO dto) {
        List<HmeCosWorkcellSummaryVO> list = mapper.selectListByCondition(tenantId, dto);
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        List<HmeCosBarCodeExceptionVO> equipmentList = mapper.selectEquipmentList(tenantId, list.stream().map(HmeCosWorkcellSummaryVO::getJobId).collect(Collectors.toList()));
        Map<String, String> equipmentMap = equipmentList.stream().collect(Collectors.toMap(HmeCosBarCodeExceptionVO::getJobId, HmeCosBarCodeExceptionVO::getAssetEncoding));
        list.forEach(rec -> rec.setEquipment(equipmentMap.get(rec.getJobId())));
        return list.stream().map(HmeCosWorkcellSummaryVO::summary).collect(Collectors.toList());
    }

    private void displayFieldsCompletion(Long tenantId, List<HmeCosWorkcellSummaryVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        // wafer来料数量
        Map<String, Long> waferMap = list.stream().collect(Collectors.groupingBy(rec -> mapKeyGenerator(rec.getWorkcellId(), rec.getWafer(), rec.getWorkOrderId()), Collectors.summingLong(HmeCosWorkcellSummaryVO::getCosNum)));

        // 加工数量
        Map<String, BigDecimal> snQtyMap = list.stream().collect(Collectors.groupingBy(rec -> mapKeyGenerator(rec.getWorkOrderId(), rec.getWafer(), rec.getOperationId(), rec.getWorkcellId(), String.valueOf(rec.getOperatorId()), DateUtil.formatDate(rec.getCreationDate())), CollectorsUtil.summingBigDecimal(HmeCosWorkcellSummaryVO::getSnQty)));

        // 不良总数
        List<String> operationIdList = list.stream().map(HmeCosWorkcellSummaryVO::getOperationId).distinct().collect(Collectors.toList());
        List<String> workcellIdList = list.stream().map(HmeCosWorkcellSummaryVO::getWorkcellId).distinct().collect(Collectors.toList());
        List<Long> createdByList = list.stream().map(HmeCosWorkcellSummaryVO::getOperatorId).distinct().collect(Collectors.toList());
        List<String> waferList = list.stream().map(HmeCosWorkcellSummaryVO::getWafer).distinct().collect(Collectors.toList());
        List<String> workOrderList = list.stream().map(HmeCosWorkcellSummaryVO::getWorkOrderId).distinct().collect(Collectors.toList());
        List<HmeCosNcRecord> ngQtyList = mapper.selectNgQtyList(tenantId, waferList, workOrderList, operationIdList, workcellIdList, createdByList);
        Map<String, BigDecimal> ngQtyMap = ngQtyList.stream().collect(Collectors.groupingBy(rec -> mapKeyGenerator(rec.getWorkOrderId(), rec.getWaferNum(), rec.getOperationId(), rec.getWorkcellId(), String.valueOf(rec.getCreatedBy()), DateUtil.formatDate(rec.getCreationDate())), CollectorsUtil.summingBigDecimal(HmeCosNcRecord::getDefectCount)));

        // 人员
        Map<Long, MtUserInfo> userMap = userClient.userInfoBatchGet(tenantId, list.stream().map(HmeCosWorkcellSummaryVO::getOperatorId).collect(Collectors.toList()));
        list.forEach(rec -> {
            rec.setWaferNum(waferMap.getOrDefault(mapKeyGenerator(rec.getWorkcellId(), rec.getWafer(), rec.getWorkOrderId()), 0L));
            rec.setSnQty(snQtyMap.getOrDefault(mapKeyGenerator(rec.getWorkOrderId(), rec.getWafer(), rec.getOperationId(), rec.getWorkcellId(), String.valueOf(rec.getOperatorId()), DateUtil.formatDate(rec.getCreationDate())), BigDecimal.ZERO));
            rec.setNgQty(ngQtyMap.getOrDefault(mapKeyGenerator(rec.getWorkOrderId(), rec.getWafer(), rec.getOperationId(), rec.getWorkcellId(), String.valueOf(rec.getOperatorId()), DateUtil.formatDate(rec.getCreationDate())), BigDecimal.ZERO));
            rec.setOkQty(rec.getSnQty().subtract(rec.getNgQty()));
            rec.setOperatorName(userMap.getOrDefault(rec.getOperatorId(), BLANK_USER).getRealName());
        });
    }

    private String mapKeyGenerator(String... element) {
        return StringUtils.join(element, '#');
    }
}
