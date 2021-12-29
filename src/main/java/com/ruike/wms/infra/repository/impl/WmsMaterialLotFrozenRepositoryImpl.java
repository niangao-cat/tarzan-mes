package com.ruike.wms.infra.repository.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.ruike.itf.app.service.ItfObjectTransactionIfaceService;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.wms.api.dto.WmsTransactionTypeDTO;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.*;
import com.ruike.wms.infra.constant.WmsConstant;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import io.choerodon.core.exception.CommonException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.BaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.date.DateUtil;
import com.ruike.wms.api.dto.WmsMaterialLotFrozenDTO;
import com.ruike.wms.domain.entity.WmsObjectTransaction;
import com.ruike.wms.domain.repository.WmsMaterialLotFrozenRepository;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.infra.mapper.WmsMaterialLotMapper;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtExtendVO5;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO9;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;

/**
 * WmsMaterialLotFrozenRepositoryImpl
 *
 * @author liyuan.lv@hand-china.com 2020/04/28 11:26
 */
@Component
public class WmsMaterialLotFrozenRepositoryImpl implements WmsMaterialLotFrozenRepository {
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;
    @Autowired
    private WmsMaterialLotMapper wmsMaterialLotMapper;
    @Autowired
    private WmsTransactionTypeRepository wmsTransactionTypeRepository;
    @Autowired
    private ItfObjectTransactionIfaceService itfObjectTransactionIfaceService;
    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Override
    @ProcessLovValue
    public Page<WmsMaterialLotVO2> queryForMaterialLot(Long tenantId, WmsMaterialLotFrozenDTO dto, PageRequest pageRequest) {
        if (WmsConstant.CONSTANT_Y.equals(dto.getPdaFlag()) && CollectionUtils.isNotEmpty(dto.getMaterialLotCode())) {
            List<MtMaterialLot> materialLot = wmsMaterialLotMapper.selectMaterialLotByCodes(tenantId, dto.getMaterialLotCode());
            if (CollectionUtils.isEmpty(materialLot)) {
                throw new MtException("WMS_MATERIAL_LOT_FZ_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_MATERIAL_LOT_FZ_004", "WMS"));
            }
            for (MtMaterialLot item:materialLot) {
                if(WmsConstant.CONSTANT_N.equals(item.getEnableFlag())){
                    throw new MtException("WMS_MATERIAL_LOT_FZ_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_MATERIAL_LOT_FZ_004", "WMS"));
                }
            }
        }

        WmsMaterialLotVO wmsMaterialLotVO = new WmsMaterialLotVO();
        wmsMaterialLotVO.setSiteId(dto.getSiteId());
        wmsMaterialLotVO.setMaterialLotCode(dto.getMaterialLotCode());
        wmsMaterialLotVO.setEnableFlag(WmsConstant.CONSTANT_Y);
        wmsMaterialLotVO.setMaterialId(dto.getMaterialId());
        wmsMaterialLotVO.setLocatorId(dto.getLocatorId());
        wmsMaterialLotVO.setLot(dto.getLotCode());
        wmsMaterialLotVO.setSupplierId(dto.getSupplierId());
        wmsMaterialLotVO.setFreezeFlag(dto.getFreezeFlag());
        wmsMaterialLotVO.setMaterialVersion(dto.getMaterialVersion());
        wmsMaterialLotVO.setWarehouseId(dto.getWarehouseId());
        wmsMaterialLotVO.setFreezeDateFrom(dto.getFreezeDateFrom());
        wmsMaterialLotVO.setFreezeDateTo(dto.getFreezeDateTo());
        wmsMaterialLotVO.setSupplierLotValue(dto.getSupplierLotValue());
        wmsMaterialLotVO.setSoNumValue(dto.getSoNumValue());
        wmsMaterialLotVO.setLotStatus(dto.getLotStatus());
        wmsMaterialLotVO.setQualityStatus(dto.getQualityStatus());
        return PageHelper.doPageAndSort(pageRequest, () -> selectMaterialLotByCondition(tenantId, wmsMaterialLotVO));
    }

    @Override
    public List<WmsMaterialLotVO2> selectMaterialLotByCondition(Long tenantId, WmsMaterialLotVO dto) {
        return wmsMaterialLotMapper.selectMaterialLotByCondition(tenantId, dto);
    }

    @Override
    public List<WmsMaterialLotVO2> selectMaterialLotByIds(Long tenantId, List<String> materialLotIds) {
        return wmsMaterialLotMapper.selectMaterialLotByIds(tenantId, materialLotIds);
    }

    @Transactional(rollbackFor = Exception.class)
    @ProcessLovValue
    @Override
    public List<WmsMaterialLotVO2> executeFreeze(Long tenantId, WmsMaterialLotVO3 dto) {
        if (CollectionUtils.isEmpty(dto.getWmsMaterialLotVo2List())) {
            throw new MtException("WMS_MATERIAL_LOT_FZ_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MATERIAL_LOT_FZ_001", "WMS"));
        }

        String eventRequestId;
        if (WmsConstant.CONSTANT_Y.equals(dto.getFreezeFlag())) {
            eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "MATERIAL_FREEZE");
        } else {
            eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "MATERIAL_FREE");
        }

        Date currentDate = new Date();
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        for (WmsMaterialLotVO2 line : dto.getWmsMaterialLotVo2List()) {
            line.setRemark(dto.getFreezeReason());

            // 记录事务
            WmsObjectTransactionVO objectTransactionVO = new WmsObjectTransactionVO();
            objectTransactionVO.setTransactionTime(currentDate);
            objectTransactionVO.setTransactionQty(line.getPrimaryUomQty());

            Map<String, WmsTransactionTypeDTO> trxMap = wmsTransactionTypeRepository.getAllTransactionType(tenantId);

            String moveType = StringUtils.EMPTY;
            String soNum = StringUtils.EMPTY;
            String soLineNum = StringUtils.EMPTY;

            //查找条码销售订单
            MtExtendVO mtTestVO1 = new MtExtendVO();
            mtTestVO1.setKeyId(line.getMaterialLotId());
            mtTestVO1.setAttrName("SO_NUM");
            mtTestVO1.setTableName("mt_material_lot_attr");
            List<MtExtendAttrVO> mtTestVS1 = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtTestVO1);

            if (CollectionUtils.isNotEmpty(mtTestVS1)) {
                soNum = mtTestVS1.get(0).getAttrValue();
            }

            //查找条码销售订单
            MtExtendVO mtTestVO2 = new MtExtendVO();
            mtTestVO2.setKeyId(line.getMaterialLotId());
            mtTestVO2.setAttrName("SO_LINE_NUM");
            mtTestVO2.setTableName("mt_material_lot_attr");
            List<MtExtendAttrVO> mtTestVS2 = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtTestVO2);

            if (CollectionUtils.isNotEmpty(mtTestVS2)) {
                soLineNum = mtTestVS2.get(0).getAttrValue();
            }


            if (WmsConstant.CONSTANT_Y.equals(dto.getFreezeFlag())) {
                // 冻结
                moveType =trxMap.get("WMS_INVENTORY_FRE").getMoveType();

                String freezeOutEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                    setEventRequestId(eventRequestId);
                    setLocatorId(line.getLocatorId());
                    setEventTypeCode("MATERIAL_FREEZE_OUT");
                }});

                String freezeInEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                    setEventRequestId(eventRequestId);
                    setLocatorId(line.getLocatorId());
                    setEventTypeCode("MATERIAL_FREEZE_IN");
                }});

                objectTransactionVO.setEventId(freezeInEventId);
                objectTransactionVO.setTransactionTypeCode("WMS_INVENTORY_FRE");

                MtModLocator locatorCondition = new MtModLocator();
                locatorCondition.setTenantId(tenantId);
                locatorCondition.setParentLocatorId(line.getWarehouseId());
                locatorCondition.setEnableFlag(WmsConstant.CONSTANT_Y);
                locatorCondition.setLocatorCategory(WmsConstant.LOCATOR_CATEGORY_INVENTORY);
                locatorCondition.setLocatorType(WmsConstant.LocatorType.TYPE_19);
                MtModLocator transferLocator = mtModLocatorRepository.selectOne(locatorCondition);
                if (Objects.isNull(transferLocator)) {
                    throw new MtException("WMS_MATERIAL_LOT_FZ_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_MATERIAL_LOT_FZ_003", "WMS"));
                }
                objectTransactionVO.setTransferLocatorId(transferLocator.getLocatorId());
                objectTransactionVO.setTransferLocatorCode(transferLocator.getLocatorCode());

                // 扣减条码来源仓库存现有量
                MtInvOnhandQuantityVO9 reduceUpdateOnHandVO = new MtInvOnhandQuantityVO9();
                reduceUpdateOnHandVO.setSiteId(line.getSiteId());
                reduceUpdateOnHandVO.setMaterialId(line.getMaterialId());
                reduceUpdateOnHandVO.setLocatorId(line.getLocatorId());
                reduceUpdateOnHandVO.setLotCode(line.getLot());
                reduceUpdateOnHandVO.setEventId(freezeOutEventId);
                reduceUpdateOnHandVO.setChangeQuantity(Optional.ofNullable(line.getPrimaryUomQty()).orElse(BigDecimal.ZERO).doubleValue());
                reduceUpdateOnHandVO.setOwnerId("");
                reduceUpdateOnHandVO.setOwnerType("");

                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, reduceUpdateOnHandVO);

                // 增加入库上架目标仓库库存现有量
                MtInvOnhandQuantityVO9 updateOnHandVO = new MtInvOnhandQuantityVO9();
                updateOnHandVO.setSiteId(line.getSiteId());
                updateOnHandVO.setMaterialId(line.getMaterialId());
                updateOnHandVO.setLocatorId(transferLocator.getLocatorId());
                updateOnHandVO.setLotCode(line.getLot());
                updateOnHandVO.setEventId(freezeInEventId);
                updateOnHandVO.setChangeQuantity(Optional.ofNullable(line.getPrimaryUomQty()).orElse(BigDecimal.ZERO).doubleValue());
                updateOnHandVO.setOwnerId("");
                updateOnHandVO.setOwnerType("");

                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, updateOnHandVO);

                // 更新条码数据
                mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
                    setEventId(freezeInEventId);
                    setMaterialLotId(line.getMaterialLotId());
                    setLocatorId(transferLocator.getLocatorId());
                    setFreezeFlag(WmsConstant.CONSTANT_Y);
                }}, WmsConstant.CONSTANT_N);

                List<MtExtendVO5> mtExtendVo5List = new ArrayList<>();
                //
                String currentTimeStr = DateUtil.format(currentDate, BaseConstants.Pattern.DATETIME);
                MtExtendVO5 freezeDateAttr = new MtExtendVO5();
                freezeDateAttr.setAttrName("FREEZE_DATE");
                freezeDateAttr.setAttrValue(currentTimeStr);
                mtExtendVo5List.add(freezeDateAttr);

                MtExtendVO5 remarkAttr = new MtExtendVO5();
                remarkAttr.setAttrName("REMARK");
                remarkAttr.setAttrValue(dto.getFreezeReason());
                mtExtendVo5List.add(remarkAttr);
                mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, new MtExtendVO10() {{
                    setKeyId(line.getMaterialLotId());
                    setEventId(freezeInEventId);
                    setAttrs(mtExtendVo5List);
                }});
            } else {
                // 解冻
                moveType =trxMap.get("WMS_INVENTORY_UNFRE").getMoveType();

                String freeOutEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                    setEventRequestId(eventRequestId);
                    setLocatorId(line.getLocatorId());
                    setEventTypeCode("MATERIAL_FREE_OUT");
                }});

                String freeInEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                    setEventRequestId(eventRequestId);
                    setLocatorId(line.getLocatorId());
                    setEventTypeCode("MATERIAL_FREE_IN");
                }});

                objectTransactionVO.setEventId(freeInEventId);
                objectTransactionVO.setTransactionTypeCode("WMS_INVENTORY_UNFRE");

                WmsObjectTransaction wmsObjectTransaction = wmsObjectTransactionRepository
                        .selectLastTrxByMaterialLotId(tenantId, "WMS_INVENTORY_FRE", line.getMaterialLotId());
                if (Objects.isNull(wmsObjectTransaction)) {
                    throw new MtException("WMS_MATERIAL_LOT_FZ_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_MATERIAL_LOT_FZ_002", "WMS"));
                }
                objectTransactionVO.setTransferLocatorId(wmsObjectTransaction.getLocatorId());
                objectTransactionVO.setTransferLocatorCode(wmsObjectTransaction.getLocatorCode());

                // 扣减条码来源仓库存现有量
                MtInvOnhandQuantityVO9 reduceUpdateOnHandVO = new MtInvOnhandQuantityVO9();
                reduceUpdateOnHandVO.setSiteId(line.getSiteId());
                reduceUpdateOnHandVO.setMaterialId(line.getMaterialId());
                reduceUpdateOnHandVO.setLocatorId(line.getLocatorId());
                reduceUpdateOnHandVO.setLotCode(line.getLot());
                reduceUpdateOnHandVO.setEventId(freeOutEventId);
                reduceUpdateOnHandVO.setChangeQuantity(line.getPrimaryUomQty().doubleValue());
                reduceUpdateOnHandVO.setOwnerId("");
                reduceUpdateOnHandVO.setOwnerType("");

                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, reduceUpdateOnHandVO);

                // 增加入库上架目标仓库库存现有量
                MtInvOnhandQuantityVO9 updateOnHandVO = new MtInvOnhandQuantityVO9();
                updateOnHandVO.setSiteId(line.getSiteId());
                updateOnHandVO.setMaterialId(line.getMaterialId());
                updateOnHandVO.setLocatorId(wmsObjectTransaction.getLocatorId());
                updateOnHandVO.setLotCode(line.getLot());
                updateOnHandVO.setEventId(freeInEventId);
                updateOnHandVO.setChangeQuantity(line.getPrimaryUomQty().doubleValue());
                updateOnHandVO.setOwnerId("");
                updateOnHandVO.setOwnerType("");

                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, updateOnHandVO);

                // 更新条码数据
                mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
                    setEventId(freeInEventId);
                    setMaterialLotId(line.getMaterialLotId());
                    setLocatorId(wmsObjectTransaction.getLocatorId());
                    setFreezeFlag("N");
                }}, "N");
            }

            addObjectTransaction(tenantId,moveType,soNum,soLineNum,objectTransactionVO, line, objectTransactionRequestList);

        }

        List<String> materialLotIds = dto.getWmsMaterialLotVo2List().stream()
                .map(WmsMaterialLotVO2::getMaterialLotId).collect(Collectors.toList());
        List<WmsMaterialLotVO2> wmsMaterialLotVO2s = selectMaterialLotByIds(tenantId, materialLotIds);
        // 记录事物
        List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS = wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
        itfObjectTransactionIfaceService.sendSapMaterialMove(tenantId,wmsObjectTransactionResponseVOS);
        return wmsMaterialLotVO2s;
    }

    private void addObjectTransaction(Long tenantId,
                                      String moveType,
                                      String soNum,
                                      String soLineNum,
                                      WmsObjectTransactionVO dto,
                                      WmsMaterialLotVO2 line,
                                      List<WmsObjectTransactionRequestVO> objectTransactionList) {
        objectTransactionList.add(new WmsObjectTransactionRequestVO() {{
            setTransactionId(null);
            setTransactionTypeCode(dto.getTransactionTypeCode());
            setEventId(dto.getEventId());
            setBarcode(line.getMaterialLotCode());
            setMaterialLotId(line.getMaterialLotId());
            setPlantCode(line.getSiteCode());
            setPlantId(line.getSiteId());
            setMaterialCode(line.getMaterialCode());
            setMaterialId(line.getMaterialId());
            setTransactionQty(dto.getTransactionQty());
            setLotNumber(line.getLot());
            setTransactionUom(line.getPrimaryUomCode());
            setTransactionTime(dto.getTransactionTime());
            setWarehouseId(line.getWarehouseId());
            setWarehouseCode(line.getWarehouseCode());
            setLocatorCode(line.getLocatorCode());
            setLocatorId(line.getLocatorId());
            setSupplierCode(line.getSupplierCode());
            setTransferPlantId(line.getSiteId());
            setTransferPlantCode(line.getSiteCode());
            setTransferWarehouseId(line.getWarehouseId());
            setTransferWarehouseCode(line.getWarehouseCode());
            setTransferLocatorId(dto.getTransferLocatorId());
            setTransferLocatorCode(dto.getTransferLocatorCode());
            setRemark(line.getRemark());
            //setMergeFlag("N");
            setTenantId(tenantId);
            setMoveType(moveType);

            setSoNum(soNum);
            setSoLineNum(soLineNum);
        }});
    }

}
