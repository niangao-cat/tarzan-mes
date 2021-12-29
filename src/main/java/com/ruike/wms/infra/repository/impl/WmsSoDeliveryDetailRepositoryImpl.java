package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.api.dto.WmsBarcodeDTO;
import com.ruike.wms.api.dto.WmsSoDeliveryDetailQueryDTO;
import com.ruike.wms.domain.repository.WmsSoDeliveryDetailRepository;
import com.ruike.wms.domain.vo.WmsInstructionActualDetailVO;
import com.ruike.wms.domain.vo.WmsProdPrepareExecVO;
import com.ruike.wms.domain.vo.WmsProductPrepareDetailVO;
import com.ruike.wms.domain.vo.WmsSoDeliveryDetailVO;
import com.ruike.wms.infra.mapper.WmsSoDeliveryDetailMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.ruike.wms.infra.constant.WmsConstant.CONTAINER;
import static com.ruike.wms.infra.constant.WmsConstant.MATERIAL_LOT;

/**
 * <p>
 * 出货单明细 资源库实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/10 11:10
 */
@Component
public class WmsSoDeliveryDetailRepositoryImpl implements WmsSoDeliveryDetailRepository {
    private final WmsSoDeliveryDetailMapper wmsSoDeliveryDetailMapper;

    public WmsSoDeliveryDetailRepositoryImpl(WmsSoDeliveryDetailMapper wmsSoDeliveryDetailMapper) {
        this.wmsSoDeliveryDetailMapper = wmsSoDeliveryDetailMapper;
    }

    @Override
    @ProcessLovValue
    public Page<WmsSoDeliveryDetailVO> selectListByCondition(Long tenantId, List<String> instructionIdList, WmsSoDeliveryDetailQueryDTO dto, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> wmsSoDeliveryDetailMapper.selectListByCondition(tenantId, instructionIdList, dto));
    }

    @Override
    public List<WmsProductPrepareDetailVO> selectPrepareListByLineId(Long tenantId, String instructionId) {
        List<WmsProductPrepareDetailVO> wmsProductPrepareDetailVOS = wmsSoDeliveryDetailMapper.selectPrepareListByLineId(tenantId, instructionId);
        for(WmsProductPrepareDetailVO wmsProductPrepareDetailVO:wmsProductPrepareDetailVOS){
            if(StringUtils.isBlank(wmsProductPrepareDetailVO.getSoNum())){
                wmsProductPrepareDetailVO.setSoNum("");
            }
            if(StringUtils.isBlank(wmsProductPrepareDetailVO.getSoLineNum())){
                wmsProductPrepareDetailVO.setSoLineNum("");
            }
        }
        return wmsProductPrepareDetailVOS;
    }

    @Override
    public List<WmsInstructionActualDetailVO> selectListByDocAndBarcode(Long tenantId, String instructionDocId, List<WmsBarcodeDTO> barcodeList) {
        List<String> materialLotIdList = barcodeList.stream().filter(rec -> MATERIAL_LOT.equals(rec.getLoadObjectType())).map(WmsBarcodeDTO::getLoadObjectId).distinct().collect(Collectors.toList());
        List<String> containerIdList = barcodeList.stream().filter(rec -> CONTAINER.equals(rec.getLoadObjectType())).map(WmsBarcodeDTO::getLoadObjectId).distinct().collect(Collectors.toList());
        return wmsSoDeliveryDetailMapper.selectListByDocAndBarcode(tenantId, instructionDocId, materialLotIdList, containerIdList);
    }

    @Override
    public List<WmsProdPrepareExecVO> selectExecuteListByDocId(Long tenantId, String instructionDocId) {
        return wmsSoDeliveryDetailMapper.selectExecuteListByDocId(tenantId, instructionDocId);
    }
}
