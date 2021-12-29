package com.ruike.wms.app.service.impl;

import com.ruike.hme.api.dto.HmeObjectRecordLockDTO;
import com.ruike.hme.app.service.HmeObjectRecordLockService;
import com.ruike.hme.domain.entity.HmeObjectRecordLock;
import com.ruike.hme.domain.repository.HmeObjectRecordLockRepository;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.wms.api.dto.WmsCodeIdentifyDTO;
import com.ruike.wms.api.dto.WmsLocatorScanDTO;
import com.ruike.wms.api.dto.WmsLocatorTransferDTO;
import com.ruike.wms.api.dto.WmsMaterialLotScanDTO;
import com.ruike.wms.app.service.WmsBarCodeIdentifyService;
import com.ruike.wms.app.service.WmsLocatorTransferService;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.vo.WmsLocatorTransferVO;
import com.ruike.wms.domain.vo.WmsLocatorTransferVO2;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsLocatorTransferMapper;
import io.choerodon.core.exception.CommonException;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.vo.MtExtendAttrVO1;
import io.tarzan.common.domain.vo.MtExtendVO1;
import io.tarzan.common.domain.vo.MtExtendVO5;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.vo.MtModLocatorVO1;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: tarzan-mes
 * @description: 货位扫描
 * @author: han.zhang
 * @create: 2020/05/08 10:59
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WmsLocatorTransferServiceImpl implements WmsLocatorTransferService {
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtContainerLoadDetailRepository containerLoadDetailRepository;
    @Autowired
    private WmsBarCodeIdentifyService wmsBarCodeIdentifyService;
    @Autowired
    private WmsLocatorTransferMapper wmsLocatorTransferMapper;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;
    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtContainerRepository mtContainerRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private HmeObjectRecordLockRepository hmeObjectRecordLockRepository;
    @Autowired
    private HmeObjectRecordLockService hmeObjectRecordLockService;

    @Override
    public MtModLocator locatorScan(Long tenantId, WmsLocatorScanDTO dto) {
        //根据货位编码查询id
        MtModLocatorVO1 modLocator = new MtModLocatorVO1();
        modLocator.setLocatorCode(dto.getLocatorCode());
        List<String> list = mtModLocatorRepository.propertyLimitLocatorQuery(tenantId, modLocator);
        if (CollectionUtils.isEmpty(list)) {
            throw new MtException("WMS_LOCATOR_TRANSFER_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_LOCATOR_TRANSFER_0001",
                            "WMS"));
        }
        //根据货位ID获取货位描述
        MtModLocator mtModLocator = mtModLocatorRepository.locatorBasicPropertyGet(tenantId, list.get(0));

        if(!WmsConstant.LOCATOR_CATEGORY_INVENTORY.equals(mtModLocator.getLocatorCategory())){
            throw new MtException("WMS_LOCATOR_TRANSFER_0002",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_LOCATOR_TRANSFER_0002",
                            "WMS"));
        }
        return mtModLocator;
    }

    @Override
    @ProcessLovValue
    public WmsLocatorTransferVO2 materialLotScan(Long tenantId, WmsMaterialLotScanDTO wmsMaterialLotScanDTO) {
        WmsLocatorTransferVO2 vo2 = new WmsLocatorTransferVO2();
        // 扫描的条码是容器类型,获取物料批
        WmsCodeIdentifyDTO wmsCodeIdentifyDTO = wmsBarCodeIdentifyService.codeIdentify(tenantId, wmsMaterialLotScanDTO.getMaterialLotCode());
        vo2.setCodeType(wmsCodeIdentifyDTO.getCodeType());
        //根据货位编码查询id
        List<MtModLocator> locatorList = mtModLocatorRepository.selectByCondition(Condition.builder(MtModLocator.class)
                .andWhere(Sqls.custom().andEqualTo(MtModLocator.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtModLocator.FIELD_LOCATOR_CODE, wmsMaterialLotScanDTO.getLocatorCode())
                        .andEqualTo(MtModLocator.FIELD_ENABLE_FLAG, WmsConstant.CONSTANT_Y)).build());
        if (CollectionUtils.isEmpty(locatorList)) {
            throw new MtException("WMS_LOCATOR_TRANSFER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_LOCATOR_TRANSFER_0001", "WMS"));
        }
        MtModLocator scanLocator = locatorList.get(0);

        List<String> materialLotIds = new ArrayList<>();
        // 容器编码
        String containerCode = "";
        //判断条码是物料条码还是容器条码
        if (WmsBarCodeIdentifyService.CONTAINER.equals(wmsCodeIdentifyDTO.getCodeType())) {
            //如果是容器条码
            MtContLoadDtlVO10 contLoadDtlVO = new MtContLoadDtlVO10();
            contLoadDtlVO.setContainerId(wmsCodeIdentifyDTO.getCodeId());
            List<MtContLoadDtlVO4> contLoadDtls = containerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId,
                    contLoadDtlVO);
            materialLotIds = contLoadDtls.stream().map(MtContLoadDtlVO4::getMaterialLotId).collect(Collectors.toList());

            MtContainer mtContainer = mtContainerRepository.selectByPrimaryKey(wmsCodeIdentifyDTO.getCodeId());
            //货位校验
            MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(mtContainer.getLocatorId());
            // 货位编码
            vo2.setLocatorCode(mtModLocator.getLocatorCode());
            containerCode = mtContainer.getContainerCode();
            if (StringUtils.equals(mtContainer.getLocatorId(), scanLocator.getLocatorId()) || !StringUtils.equals(scanLocator.getParentLocatorId(), mtModLocator.getParentLocatorId())) {
                throw new MtException("WMS_LOCATOR_TRANSFER_0008",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_LOCATOR_TRANSFER_0008",
                                "WMS"));
            }
        }else{
            materialLotIds.add(wmsCodeIdentifyDTO.getCodeId());
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(wmsCodeIdentifyDTO.getCodeId());

            //货位校验
            MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(mtMaterialLot.getLocatorId());

            if(StringUtils.equals(mtModLocator.getLocatorId(), scanLocator.getLocatorId()) || !StringUtils.equals(scanLocator.getParentLocatorId(), mtModLocator.getParentLocatorId())){
                throw new MtException("WMS_LOCATOR_TRANSFER_0008",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_LOCATOR_TRANSFER_0008",
                                "WMS"));
            }
        }

        //获取物料批扩展属性 add by yuchao.wang for yiwei.zhou at 2020.10.12
        Map<String, String> attrMap = new HashMap<String, String>();
        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setTableName(WmsConstant.AttrTable.MT_MATERIAL_LOT_ATTR);
        mtExtendVO1.setKeyIdList(materialLotIds);
        List<MtExtendVO5> attrs = new ArrayList<>();
        MtExtendVO5 extend1 = new MtExtendVO5();
        extend1.setAttrName(WmsConstant.MaterialLotAttr.MF_FLAG);
        attrs.add(extend1);
        mtExtendVO1.setAttrs(attrs);
        List<MtExtendAttrVO1> extendAttrList = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
        if (CollectionUtils.isNotEmpty(extendAttrList)) {
            extendAttrList.forEach(item -> attrMap.put(item.getKeyId(), item.getAttrValue()));
        }

        //校验上层容器
        MtContLoadDtlVO5 mtContLoadDtlVO5 = new MtContLoadDtlVO5();
        mtContLoadDtlVO5.setLoadObjectType(wmsCodeIdentifyDTO.getCodeType());
        mtContLoadDtlVO5.setLoadObjectId(wmsCodeIdentifyDTO.getCodeId());
        mtContLoadDtlVO5.setTopLevelFlag(HmeConstants.ConstantValue.YES);
        List<String> containerIds = containerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId, mtContLoadDtlVO5);
        if (CollectionUtils.isNotEmpty(containerIds)) {
            MtContainer mtContainer = mtContainerRepository.selectByPrimaryKey(containerIds.get(0));
            throw new MtException("WMS_COST_CENTER_0055", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0055", WmsConstant.ConstantValue.WMS, wmsMaterialLotScanDTO.getMaterialLotCode(), mtContainer != null ? mtContainer.getContainerCode() : ""));
        }

        List<WmsLocatorTransferVO> wmsLocatorTransferVOs = wmsLocatorTransferMapper.selectMaterialLotInfo(tenantId, materialLotIds);
        for(WmsLocatorTransferVO wmsLocatorTransferVO:wmsLocatorTransferVOs){
            if(StringUtils.isNotBlank(wmsLocatorTransferVO.getQualityStatus())){
                String qualityStatusMeaning = lovAdapter.queryLovMeaning("WMS.MTLOT.QUALITY_STATUS", tenantId, wmsLocatorTransferVO.getQualityStatus());
                wmsLocatorTransferVO.setQualityStatusMeaning(qualityStatusMeaning);
            }
        }
        if (WmsBarCodeIdentifyService.CONTAINER.equals(wmsCodeIdentifyDTO.getCodeType())) {
            // 扫描条码为容器
            List<String> materialIdList = wmsLocatorTransferVOs.stream().map(WmsLocatorTransferVO::getMaterialId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            // 物料多个时 不显示版本 物料及单位 只显示数量及库位
            Double totalQty = wmsLocatorTransferVOs.stream().map(WmsLocatorTransferVO::getPrimaryUomQty).filter(Objects::nonNull).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
            vo2.setPrimaryUomQty(totalQty != null ? totalQty : 0D);

            if (CollectionUtils.isNotEmpty(materialIdList) && materialIdList.size() == 1) {
                // 根据物料及物料版本分组
                Map<String, List<WmsLocatorTransferVO>> transferCodeList = wmsLocatorTransferVOs.stream().collect(Collectors.groupingBy(barcode -> barcode.getMaterialId() + "_" + (StringUtils.isBlank(barcode.getMaterialVersion()) ? "" : barcode.getMaterialVersion())));
                // 当物料只有一种时 物料版本多个  不显示版本
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(materialIdList.get(0));
                MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterial.getPrimaryUomId());
                vo2.setMaterialCode(mtMaterial.getMaterialCode());
                vo2.setMaterialName(mtMaterial.getMaterialName());
                vo2.setUomCode(mtUom.getUomCode());
                if (transferCodeList.entrySet().size() == 1) {
                    vo2.setMaterialVersion(wmsLocatorTransferVOs.get(0).getMaterialVersion());
                }
            }
        } else if (WmsBarCodeIdentifyService.MATERIAL_LOT.equals(wmsCodeIdentifyDTO.getCodeType())) {
            // 扫描条码为物料批时
            if (CollectionUtils.isNotEmpty(wmsLocatorTransferVOs)) {
                vo2.setMaterialCode(wmsLocatorTransferVOs.get(0).getMaterialCode());
                vo2.setMaterialName(wmsLocatorTransferVOs.get(0).getMaterialName());
                vo2.setLocatorCode(wmsLocatorTransferVOs.get(0).getLocatorCode());
                vo2.setLot(wmsLocatorTransferVOs.get(0).getLot());
                vo2.setMaterialVersion(wmsLocatorTransferVOs.get(0).getMaterialVersion());
                vo2.setQualityStatusMeaning(wmsLocatorTransferVOs.get(0).getQualityStatusMeaning());
                vo2.setUomCode(wmsLocatorTransferVOs.get(0).getPrimaryUomCode());
                vo2.setPrimaryUomQty(wmsLocatorTransferVOs.get(0).getPrimaryUomQty());
            }
        }
        for (WmsLocatorTransferVO vo :
                wmsLocatorTransferVOs) {
            if (!MtBaseConstants.QUALITY_STATUS.OK.equals(vo.getQualityStatus())) {
                throw new MtException("WMS_LOCATOR_TRANSFER_0003",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_LOCATOR_TRANSFER_0003",
                                "WMS"));
            }
            if (!MtBaseConstants.YES.equals(vo.getEnableFlag())) {
                throw new MtException("WMS_LOCATOR_TRANSFER_0004",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_LOCATOR_TRANSFER_0004",
                                "WMS"));
            }
            if(MtBaseConstants.YES.equals(vo.getFreezeFlag())){
                throw new MtException("WMS_LOCATOR_TRANSFER_0005",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_LOCATOR_TRANSFER_0005",
                                "WMS"));
            }
            //在制品不能转移
            String mfFlag = attrMap.getOrDefault(vo.getMaterialLotId(), "");
            if (MtBaseConstants.YES.equals(mfFlag)) {
                throw new MtException("WMS_LOCATOR_TRANSFER_0007",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_LOCATOR_TRANSFER_0007", "WMS"));
            }

            //增加条码类型及条码ID add by yuchao.wang for yiwei.zhou at 2020.10.12
            vo.setCodeType(wmsCodeIdentifyDTO.getCodeType());
            vo.setCodeId(wmsCodeIdentifyDTO.getCodeId());
            vo.setContainerCode(containerCode);
        }
        vo2.setLocatorTransferVOList(wmsLocatorTransferVOs);
        return vo2;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WmsLocatorTransferDTO transfer(Long tenantId, WmsLocatorTransferDTO wmsLocatorTransferDTO) {
        // 20211121 add by sanfeng.zhang for wenxin.zhang 加锁
        List<HmeObjectRecordLock> recordLockList = new ArrayList();
        for (WmsLocatorTransferVO transferVO: wmsLocatorTransferDTO.getMaterialLotList()) {
            //加锁
            HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
            hmeObjectRecordLockDTO.setFunctionName("库位转移");
            hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PDA);
            hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.BARCODE);
            hmeObjectRecordLockDTO.setObjectRecordId(transferVO.getMaterialLotId());
            hmeObjectRecordLockDTO.setObjectRecordCode(transferVO.getMaterialLotCode());
            HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
            recordLockList.add(hmeObjectRecordLock);
        }
        if(CollectionUtils.isNotEmpty(recordLockList)) {
            // 加锁
            hmeObjectRecordLockRepository.batchCommonLockObject2(tenantId, recordLockList);
        }
        try {
            //生成库位转移请求事件
            String eventRequestCreateId = mtEventRequestRepository.eventRequestCreate(tenantId, "LOCATOR_TRANSFER_REQUISTION");

            //创建库位转移事件
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventRequestId(eventRequestCreateId);
            eventCreateVO.setEventTypeCode("LOCATOR_TRANSFER");
            String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

            //查询父层库位id
            List<String> parentLocatorIds = mtModLocatorRepository.parentLocatorQuery(tenantId, wmsLocatorTransferDTO.getLocatorId(), "FIRST");
            if(CollectionUtils.isEmpty(parentLocatorIds)){
                throw new MtException("WMS_LOCATOR_TRANSFER_0006",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_LOCATOR_TRANSFER_0006", "WMS"));
            }
            String parentLocatorId = parentLocatorIds.get(0);
            //查询父层库位编码
            MtModLocator parentLocator = new MtModLocator();
            parentLocator.setLocatorId(parentLocatorId);
            parentLocator = mtModLocatorRepository.selectByPrimaryKey(parentLocator);
            //判断容器还是条码 直接调用API转移 modify by yuchao.wang for yiwei.zhou at 2020.10.12
            List<String> containerIdList = new ArrayList<>();
            List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
            for (WmsLocatorTransferVO vo : wmsLocatorTransferDTO.getMaterialLotList()) {
                WmsObjectTransactionRequestVO transactionRequestDTO = new WmsObjectTransactionRequestVO();
                transactionRequestDTO.setEventId(eventId);
                transactionRequestDTO.setMaterialLotId(vo.getMaterialLotId());
                transactionRequestDTO.setMaterialId(vo.getMaterialId());
                transactionRequestDTO.setMaterialCode(vo.getMaterialCode());
                transactionRequestDTO.setTransactionQty(BigDecimal.valueOf(Optional.ofNullable(vo.getPrimaryUomQty()).orElse(0D)));
                transactionRequestDTO.setLotNumber(vo.getLot());
                transactionRequestDTO.setTransactionUom(vo.getPrimaryUomCode());
                transactionRequestDTO.setPlantId(vo.getSiteId());
                transactionRequestDTO.setWarehouseId(vo.getWarehouseId());
                transactionRequestDTO.setLocatorId(vo.getLocatorId());
                transactionRequestDTO.setTransferPlantId(vo.getSiteId());
                transactionRequestDTO.setTransferWarehouseId(parentLocator.getLocatorId());
                transactionRequestDTO.setTransferLocatorId(wmsLocatorTransferDTO.getLocatorId());
                transactionRequestDTO.setSupplierCode(vo.getSupplierCode());
                transactionRequestDTO.setSupplierSiteCode(vo.getSupplierSiteCode());
                transactionRequestDTO.setMergeFlag(MtBaseConstants.NO);
                transactionRequestDTO.setTransactionTime(new Date());
                transactionRequestDTO.setBarcode(vo.getMaterialLotCode());
                transactionRequestDTO.setTransactionTypeCode("WMS_LOCATOR_TRAN");
                transactionRequestDTO.setTransactionReasonCode("同一仓库下的库位转移");

                if (WmsBarCodeIdentifyService.CONTAINER.equals(vo.getCodeType())) {
                    transactionRequestDTO.setContainerId(vo.getCodeId());

                    //如果是容器，则转移当前容器并记录
                    if (!containerIdList.contains(vo.getCodeId())) {
                        MtContainerVO7 conDto = new MtContainerVO7();
                        conDto.setContainerId(vo.getCodeId());
                        conDto.setTargetSiteId(vo.getSiteId());
                        conDto.setTargetLocatorId(wmsLocatorTransferDTO.getLocatorId());
                        conDto.setEventRequestId(eventRequestCreateId);
                        mtContainerRepository.containerTransfer(tenantId, conDto);
                        containerIdList.add(vo.getCodeId());
                    }
                } else {
                    //如果是条码，直接转移条码
                    MtMaterialLotVO9 lotDto = new MtMaterialLotVO9();
                    lotDto.setMaterialLotId(vo.getMaterialLotId());
                    lotDto.setTargetSiteId(vo.getSiteId());
                    lotDto.setTargetLocatorId(wmsLocatorTransferDTO.getLocatorId());
                    lotDto.setEventRequestId(eventRequestCreateId);
                    mtMaterialLotRepository.materialLotTransfer(tenantId, lotDto);
                }
                objectTransactionRequestList.add(transactionRequestDTO);
            }

            //记录库位转移事件
            wmsObjectTransactionRepository.objectTransactionSync(tenantId,objectTransactionRequestList);
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        } finally {
            hmeObjectRecordLockRepository.batchReleaseLock(tenantId , recordLockList , HmeConstants.ConstantValue.YES);
        }
        return wmsLocatorTransferDTO;
    }
}