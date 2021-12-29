package com.ruike.wms.infra.repository.impl;

import com.ruike.itf.api.dto.ItfSrmMaterialWasteIfaceSyncDTO;
import com.ruike.itf.app.service.ItfSrmMaterialWasteIfaceService;
import com.ruike.itf.domain.repository.ItfSrmMaterialWasteIfaceRepository;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.wms.api.dto.WmsMaterialExchangeDocDTO;
import com.ruike.wms.api.dto.WmsMaterialExchangeParamDTO;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsMaterialExchangeRepository;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.vo.WmsMaterialExchangeVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO;
import com.ruike.wms.infra.mapper.WmsMaterialExchangeMapper;
import com.ruike.wms.infra.mapper.WmsMaterialWasteExchangeMapper;
import io.choerodon.core.exception.CommonException;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.vo.MtExtendVO5;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO9;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtSupplierRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-05-19 11:35
 */
@Component
public class WmsMaterialExchangeRepositoryImpl implements WmsMaterialExchangeRepository {

    @Autowired
    private WmsMaterialExchangeMapper wmsMaterialExchangeMapper;

    @Autowired
    private MtUserClient userClient;

    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;

    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtInstructionRepository mtInstructionRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private WmsMaterialWasteExchangeMapper wmsMaterialWasteExchangeMapper;

    @Autowired
    private MtSupplierRepository mtSupplierRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private ItfSrmMaterialWasteIfaceRepository itfSrmMaterialWasteIfaceRepository;

    @Autowired
    private ItfSrmMaterialWasteIfaceService itfSrmMaterialWasteIfaceService;

    @Override
    public List<WmsMaterialExchangeDocDTO> listDocForUi(WmsMaterialExchangeParamDTO dto) {
        List<WmsMaterialExchangeDocDTO> list = wmsMaterialExchangeMapper.selectDocByConditionForUi(dto);
        list.forEach(docDto -> {
            docDto.setCreatedByName(userClient.userInfoGet(dto.getTenantId(), docDto.getCreatedBy()).getRealName());
        });
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WmsMaterialExchangeVO lineStockTransfer(Long tenantId, WmsMaterialExchangeVO wmsMaterialExchangeVO) {
        //生成料废调换发出请求事件
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "SUPPLIER_EXCHANGE_SEND");

        //创建料废调换转出事件
        MtEventCreateVO eventCreateOut = new MtEventCreateVO();
        eventCreateOut.setEventTypeCode("SUPPLIER_EXCHANGE_OUT");
        eventCreateOut.setEventRequestId(eventRequestId);
        String eventIdOut = mtEventRepository.eventCreate(tenantId, eventCreateOut);

        //创建料废调换转入事件
        MtEventCreateVO eventCreateIn = new MtEventCreateVO();
        eventCreateIn.setEventTypeCode("SUPPLIER_EXCHANGE_IN");
        eventCreateIn.setEventRequestId(eventRequestId);
        eventCreateIn.setParentEventId(eventIdOut);
        String eventIdIn = mtEventRepository.eventCreate(tenantId, eventCreateIn);

        //EXECUTE_QTY进行累加
        MtInstruction mtInstruction = mtInstructionRepository.selectByPrimaryKey(wmsMaterialExchangeVO.getInstructionId());
        List<MtExtendVO5> extendVO5List = new ArrayList<>();
        MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("EXECUTE_QTY");
        BigDecimal attrValue = BigDecimal.ZERO;
        if (wmsMaterialExchangeVO.getExecuteQty() == null) {
            attrValue = wmsMaterialExchangeVO.getAddQty();
        } else {
            //校验
            BigDecimal num = BigDecimal.valueOf(mtInstruction.getQuantity()).subtract(wmsMaterialExchangeVO.getExecuteQty());
            if (wmsMaterialExchangeVO.getAddQty().compareTo(num) > 0) {
                throw new MtException("WMS_INV_TRANSFER_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_INV_TRANSFER_0037", "WMS"));
            }
            attrValue = wmsMaterialExchangeVO.getExecuteQty().add(wmsMaterialExchangeVO.getAddQty());
        }
        mtExtendVO5.setAttrValue(attrValue.toString());
        extendVO5List.add(mtExtendVO5);
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_attr", mtInstruction.getInstructionId(), eventIdOut, extendVO5List);

        wmsMaterialExchangeVO.setExecuteQty(attrValue);


        //记录货位转移事务
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        WmsObjectTransactionRequestVO objectTransactionVO = new WmsObjectTransactionRequestVO();
        objectTransactionVO.setTransactionTypeCode("WMS_LOCATOR_TRAN");
        objectTransactionVO.setEventId(eventIdIn);
        objectTransactionVO.setMaterialId(wmsMaterialExchangeVO.getMaterialId());
        objectTransactionVO.setTransactionQty(wmsMaterialExchangeVO.getAddQty());
        objectTransactionVO.setLotNumber("20100101");
        objectTransactionVO.setTransferLotNumber("20100101");
        objectTransactionVO.setTransactionUom(mtInstruction.getUomId());
        objectTransactionVO.setTransactionTime(new Date());
        objectTransactionVO.setTransactionReasonCode("料废调换发出");
        objectTransactionVO.setPlantId(mtInstruction.getSiteId());
        String plantCode = wmsMaterialWasteExchangeMapper.queryPlantCode(tenantId, mtInstruction.getSiteId());
        objectTransactionVO.setPlantCode(plantCode);
        objectTransactionVO.setWarehouseId(mtInstruction.getToLocatorId());
        objectTransactionVO.setLocatorId(wmsMaterialExchangeVO.getLocatorId());
        objectTransactionVO.setTransferPlantId(mtInstruction.getSiteId());
        objectTransactionVO.setTransferWarehouseId(mtInstruction.getToLocatorId());
        //26-料费调换仓的
        MtModLocator mtModLocator = new MtModLocator();
        mtModLocator.setLocatorType("26");
        mtModLocator.setParentLocatorId(mtInstruction.getToLocatorId());
        mtModLocator.setTenantId(tenantId);
        List<MtModLocator> locatorList = mtModLocatorRepository.select(mtModLocator);
        if (CollectionUtils.isEmpty(locatorList)) {
            throw new MtException("WMS_INV_TRANSFER_0038", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_INV_TRANSFER_0038", "WMS"));
        }
        objectTransactionVO.setTransferLocatorId(locatorList.get(0).getLocatorId());
        MtSupplier mtSupplier = mtSupplierRepository.selectByPrimaryKey(mtInstruction.getSupplierId());
        if (mtSupplier == null) {
            throw new MtException("WMS_SUPPLIER_EXCHANGE_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_SUPPLIER_EXCHANGE_004", "WMS"));
        }
        objectTransactionVO.setSupplierCode(mtSupplier.getSupplierCode());
        objectTransactionVO.setSourceDocId(mtInstruction.getSourceDocId());
        objectTransactionVO.setSourceDocLineId(mtInstruction.getInstructionId());
        wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);

        //扣减料费调换仓
        MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
        mtInvOnhandQuantityVO9.setSiteId(wmsMaterialExchangeVO.getSiteId());
        mtInvOnhandQuantityVO9.setLocatorId(wmsMaterialExchangeVO.getLocatorId());
        mtInvOnhandQuantityVO9.setMaterialId(wmsMaterialExchangeVO.getMaterialId());
        mtInvOnhandQuantityVO9.setChangeQuantity(wmsMaterialExchangeVO.getAddQty().doubleValue());
        mtInvOnhandQuantityVO9.setLotCode("20100101");
        mtInvOnhandQuantityVO9.setEventId(eventIdOut);
        mtInvOnhandQuantityVO9.setOwnerType("SI");
        mtInvOnhandQuantityVO9.setOwnerId(mtSupplier.getSupplierId());
        mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);

        //增加料费调换仓
        MtInvOnhandQuantityVO9 addQty = new MtInvOnhandQuantityVO9();
        addQty.setSiteId(wmsMaterialExchangeVO.getSiteId());
        addQty.setLocatorId(locatorList.get(0).getLocatorId());
        addQty.setMaterialId(wmsMaterialExchangeVO.getMaterialId());
        addQty.setChangeQuantity(wmsMaterialExchangeVO.getAddQty().doubleValue());
        addQty.setEventId(eventIdIn);
        addQty.setLotCode("20100101");
        addQty.setOwnerType("SI");
        addQty.setOwnerId(mtSupplier.getSupplierId());
        mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, addQty);

        // 发送SRM接口
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue(ItfConstant.LovCode.ITF_TIMELY_INTERNAL_FLAG, tenantId);
        if (org.apache.commons.collections.CollectionUtils.isEmpty(lovValueDTOS)) {
            throw new CommonException(ItfConstant.LovCode.ITF_TIMELY_INTERNAL_FLAG + "值集没有维护\n含义值为Y或N【Y为实时发送，N为不实时发送】");
        }
        String interfaceFlag = lovValueDTOS.get(0).getMeaning();
        if ("Y".endsWith(interfaceFlag)) {
            try {
                String siteId = addQty.getSiteId();
                String materialId = addQty.getMaterialId();
                String locatorId1 = addQty.getLocatorId();
                String ownerId = addQty.getOwnerId();
                List<ItfSrmMaterialWasteIfaceSyncDTO> syncDTOS = itfSrmMaterialWasteIfaceRepository.selectSrmMaterialWaste(tenantId, siteId, materialId, locatorId1, ownerId);
                itfSrmMaterialWasteIfaceService.srmMaterialWasteExchangeCreate(syncDTOS, tenantId);
            } catch (CommonException e) {
                e.printStackTrace();
            }
        }
        return wmsMaterialExchangeVO;
    }
}
