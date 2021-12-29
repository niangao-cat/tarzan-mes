package com.ruike.wms.infra.repository.impl;

import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.wms.api.dto.WmsSummaryOfCosBarcodeProcessingDTO;
import com.ruike.wms.api.dto.WorkOrderInProcessDetailsQueryReportDTO;
import com.ruike.wms.domain.repository.WmsSummaryOfCosBarcodeProcessingRepository;
import com.ruike.wms.domain.repository.WorkOrderInProcessDetailsQueryReportRepository;
import com.ruike.wms.domain.vo.WmsSummaryOfCosBarcodeProcessingVO;
import com.ruike.wms.domain.vo.WorkOrderInProcessDetailsQueryReportVO;
import com.ruike.wms.infra.mapper.WmsSummaryOfCosBarcodeProcessingMapper;
import com.ruike.wms.infra.mapper.WorkOrderInProcessDetailsQueryReportMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.sys.MtUserClient;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description COS条码加工汇总表
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/18
 * @time 14:35
 * @version 0.0.1
 * @return
 */
@Component
public class WmsSummaryOfCosBarcodeProcessingRepositoryImpl implements WmsSummaryOfCosBarcodeProcessingRepository {

    @Autowired
    WmsSummaryOfCosBarcodeProcessingMapper mapper;

    @Autowired
    private LovAdapter lovAdapter;

    /**
     * @description COS条码加工汇总表
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
    public Page<WmsSummaryOfCosBarcodeProcessingVO> list(Long tenantId, PageRequest pageRequest, WmsSummaryOfCosBarcodeProcessingDTO dto) {

        // 获取当前用户Id
        Long currentUserId = MtUserClient.getCurrentUserId();

        // 查询直接数据
        List<LovValueDTO> lovValueList = lovAdapter.queryLovValue("HME.REPORT_JOB_TYPE", tenantId);

        //设置值集数据
        dto.setJobTypes(lovValueList.stream().map(LovValueDTO::getValue).collect(Collectors.toList()));

        // 查询方法
        Page<WmsSummaryOfCosBarcodeProcessingVO> result = PageHelper.doPage(pageRequest, () -> mapper.list(tenantId, currentUserId, dto));
        // 批量查询不良数
        List<WmsSummaryOfCosBarcodeProcessingVO> processingVOList = new ArrayList<>();
        List<WmsSummaryOfCosBarcodeProcessingVO> jobVOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(result.getContent())) {
            List<String> workOrderIdList = result.getContent().stream().map(WmsSummaryOfCosBarcodeProcessingVO::getWorkOrderId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            List<String> waferNumList = result.getContent().stream().map(WmsSummaryOfCosBarcodeProcessingVO::getWaferNum).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            List<String> materialLotIdList = result.getContent().stream().map(WmsSummaryOfCosBarcodeProcessingVO::getMaterialLotId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            List<String> createByList = result.getContent().stream().map(WmsSummaryOfCosBarcodeProcessingVO::getCreatedBy).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            List<String> workcellIdList = result.getContent().stream().map(WmsSummaryOfCosBarcodeProcessingVO::getWorkcellId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            List<String> jobIdList = result.getContent().stream().map(WmsSummaryOfCosBarcodeProcessingVO::getJobId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            // 重新取不良数量
            processingVOList = mapper.batchListQueryNg(tenantId, workOrderIdList, waferNumList, materialLotIdList, createByList, workcellIdList);

            // 重新查询设备
            jobVOList = mapper.batchListQueryAssetEncoding(tenantId,jobIdList);
        }
        Map<Object, List<WmsSummaryOfCosBarcodeProcessingVO>> processingVOMap = processingVOList.stream().collect(Collectors.groupingBy(vo -> this.spliceKey(vo)));
        Map<Object, List<WmsSummaryOfCosBarcodeProcessingVO>> jobVOMap = jobVOList.stream().collect(Collectors.groupingBy(vo -> vo.getJobId()));
        for (WmsSummaryOfCosBarcodeProcessingVO wmsSummaryOfCosBarcodeProcessingVO : result.getContent()) {
            String resultKey = this.spliceKey(wmsSummaryOfCosBarcodeProcessingVO);
            List<WmsSummaryOfCosBarcodeProcessingVO> barcodeProcessingVOList = processingVOMap.get(resultKey);
            List<WmsSummaryOfCosBarcodeProcessingVO> barcodeJobVOList = jobVOMap.get(wmsSummaryOfCosBarcodeProcessingVO.getJobId());
            if (CollectionUtils.isNotEmpty(barcodeProcessingVOList)) {
                wmsSummaryOfCosBarcodeProcessingVO.setNgQty(barcodeProcessingVOList.get(0).getNgQty());
            } else {
                wmsSummaryOfCosBarcodeProcessingVO.setNgQty(BigDecimal.ZERO);
            }

            wmsSummaryOfCosBarcodeProcessingVO.setOkQty(wmsSummaryOfCosBarcodeProcessingVO.getSnQty().subtract(wmsSummaryOfCosBarcodeProcessingVO.getNgQty()));

            if (CollectionUtils.isNotEmpty(barcodeJobVOList)) {
                wmsSummaryOfCosBarcodeProcessingVO.setAssetEncoding(barcodeJobVOList.get(0).getAssetEncoding());
            } else {
                wmsSummaryOfCosBarcodeProcessingVO.setAssetEncoding("");
            }
        }
        return result;
    }

    private String spliceKey(WmsSummaryOfCosBarcodeProcessingVO processingVO) {
        StringBuffer sb = new StringBuffer();
        sb.append(processingVO.getWorkOrderId());
        sb.append(processingVO.getWaferNum());
        sb.append(processingVO.getMaterialLotId());
        sb.append(processingVO.getCreatedBy());
        sb.append(processingVO.getWorkcellId());
        return sb.toString();
    }

}
