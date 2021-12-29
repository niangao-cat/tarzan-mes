package com.ruike.wms.app.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.ruike.wms.app.service.WmsMaterialPostingService;
import com.ruike.wms.domain.repository.WmsMaterialPostingRepository;
import com.ruike.wms.domain.vo.WmsDeliveryPoRelVo;
import com.ruike.wms.domain.vo.WmsInstructionLineVO;
import com.ruike.wms.domain.vo.WmsMaterialLotLineVO;
import com.ruike.wms.domain.vo.WmsMaterialPostingVO;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.util.PageUtil;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;

/**
 * WmsMaterialPostingServiceImpl
 *
 * @author liyuan.lv@hand-china.com 2020/06/14 22:04
 */
@Service
public class WmsMaterialPostingServiceImpl implements WmsMaterialPostingService {

    private final WmsMaterialPostingRepository repository;
    private final LovAdapter lovAdapter;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtInstructionActualRepository mtInstructionActualRepository;

    @Autowired
    public WmsMaterialPostingServiceImpl(WmsMaterialPostingRepository repository, LovAdapter lovAdapter) {
        this.repository = repository;
        this.lovAdapter = lovAdapter;
    }

    @Override
    @ProcessLovValue
    public Page<WmsInstructionLineVO> uiQuery(Long tenantId, WmsMaterialPostingVO dto, PageRequest pageRequest) {
        Page<WmsInstructionLineVO> page = repository.materialPostingQuery(tenantId, dto, pageRequest);
        if (page.size() == 0) {
            return page;
        }
        List<String> idList = page.stream().map(WmsInstructionLineVO::getInstructionId).collect(Collectors.toList());
        List<WmsInstructionLineVO> transList = repository.selectTransInstructionByIdList(tenantId, idList);
        Map<String, WmsInstructionLineVO> transMap = transList.stream().collect(Collectors.toMap(WmsInstructionLineVO::getInstructionId, rec -> rec, (key1, key2) -> key1));
        Map<String, List<WmsDeliveryPoRelVo>> poMap = repository.selectPoByDeliveryIdList(tenantId, page.stream().map(WmsInstructionLineVO::getInstructionId).collect(Collectors.toList()));
        page.forEach(rec -> {
            rec.setInstructionStatusLov("WMS.DELIVERY_DOC_LINE_RFS.STATUS");
            WmsInstructionLineVO transOverLine = transMap.get(rec.getInstructionId());
            if (Objects.nonNull(transOverLine)) {
                rec.setTransOverInstructionId(transOverLine.getTransOverInstructionId());
                rec.setTransOverInstructionStatus(transOverLine.getTransOverInstructionStatus());
                rec.setTransOverInspectionStatus(transOverLine.getTransOverInspectionStatus());
                if (!WmsConstant.InstructionStatus.RELEASED.equals(rec.getTransOverInstructionStatus())) {
                    rec.setInstructionStatusLov("WMS.DELIVERY_DOC_LINE_TOL.STATUS");
                }
            }
            List<WmsDeliveryPoRelVo> poList = poMap.get(rec.getInstructionId());
            if (CollectionUtils.isNotEmpty(poList)) {
                String poNumber = poList.stream().map(po -> po.getPoNumber() + "-" + po.getPoLineNumber()).collect(Collectors.joining(";"));
                rec.setInstructionStatusMeaning(lovAdapter.queryLovMeaning(rec.getInstructionStatusLov(), tenantId, rec.getInstructionStatus()));
                rec.setPoNumber(poNumber);
            }

            //接收数量
            List<MtInstructionActual> mtInstructionActualList = mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId, rec.getInstructionId());
            Double sum = mtInstructionActualList.stream().map(MtInstructionActual::getActualQty).filter(Objects::nonNull).collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue).summaryStatistics().getSum();
            if(sum != null){
                rec.setActualReceiveQty(BigDecimal.valueOf(sum));
            }
        });
        return page;
    }

    @Override
    public Page<WmsMaterialLotLineVO> detailUiQuery(Long tenantId, String instructionId, PageRequest pageRequest) {
        Page<WmsMaterialLotLineVO> page = PageHelper.doPageAndSort(pageRequest, () -> repository.detailQuery(tenantId, instructionId));
        for (WmsMaterialLotLineVO wmsMaterialLotLineVO : page.getContent()) {
            //实际货位
            if(StringUtils.isNotBlank(wmsMaterialLotLineVO.getActualLocatorCode())){
                MtModLocator locator = new MtModLocator();
                locator.setTenantId(tenantId);
                locator.setLocatorCategory("INVENTORY");
                locator.setLocatorCode(wmsMaterialLotLineVO.getActualLocatorCode());
                List<MtModLocator> locatorList = mtModLocatorRepository.select(locator);
                if(org.apache.commons.collections.CollectionUtils.isNotEmpty(locatorList)){
                    wmsMaterialLotLineVO.setActualLocatorName(locatorList.get(0).getLocatorName());
                }
            }
        }
        return page;
    }

    @Override
    public List<WmsInstructionLineVO> executePosting(Long tenantId, List<WmsInstructionLineVO> dtoList) {
        return repository.executePosting(tenantId, dtoList);
    }

    @Override
    public Page<WmsDeliveryPoRelVo> poListQuery(Long tenantId, String deliveryId, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> repository.selectPoByDeliveryId(tenantId, deliveryId));
    }
}
