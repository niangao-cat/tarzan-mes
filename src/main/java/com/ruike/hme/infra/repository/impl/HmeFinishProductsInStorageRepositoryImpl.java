package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeObjectRecordLockDTO;
import com.ruike.hme.app.service.HmeObjectRecordLockService;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.entity.HmeObjectRecordLock;
import com.ruike.hme.domain.entity.HmeWoJobSn;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.*;
import com.ruike.itf.app.service.ItfObjectTransactionIfaceService;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.utils.Utils;
import com.ruike.wms.app.service.impl.WmsCommonServiceComponent;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtGenStatusRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.vo.*;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import tarzan.calendar.domain.entity.MtShift;
import tarzan.calendar.infra.mapper.MtShiftMapper;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.api.dto.MtInstructionDocDTO2;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDetail;
import tarzan.instruction.domain.repository.MtInstructionDetailRepository;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.*;
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
import tarzan.material.domain.repository.MtPfepInventoryRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.*;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO2;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO3;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.domain.vo.MtWorkOrderVO4;
import tarzan.order.domain.vo.MtWorkOrderVO50;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;
import static com.ruike.hme.infra.constant.HmeConstants.LoadTypeCode.CONTAINER;
import static com.ruike.hme.infra.constant.HmeConstants.LoadTypeCode.MATERIAL_LOT;
import static java.util.stream.Collectors.toList;

/**
 * @Classname HmeFinishProductsInStorageRepositoryImpl
 * @Description 半成品/成品入库扫描 资源库实现
 * @Date 2020/6/2 16:27
 * @Created by Deng xu
 */
@Component
public class HmeFinishProductsInStorageRepositoryImpl implements HmeFinishProductsInStorageRepository {

    @Autowired
    private HmeFinishProductsInStorageMapper mapper;
    @Autowired
    private MtContainerRepository mtContainerRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;
    @Autowired
    private MtGenStatusRepository mtGenStatusRepository;
    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;
    @Autowired
    private WmsSiteRepository wmsSiteRepository;
    @Autowired
    private MtInstructionRepository mtInstructionRepository;
    @Autowired
    private MtInstructionDetailRepository mtInstructionDetailRepository;
    @Autowired
    private MtPfepInventoryRepository mtPfepInventoryRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtUserOrganizationRepository mtUserOrganizationRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtModLocatorOrgRelRepository mtModLocatorOrgRelRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private WmsTransactionTypeRepository wmsTransactionTypeRepository;
    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private HmeWorkCellDetailsReportMapper hmeWorkCellDetailsReportMapper;

    @Autowired
    private MtExtendSettingsMapper mtExtendSettingMapper;
    @Autowired
    private HmeMaterialTransferRepository hmeMaterialTransferRepository;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;
    @Autowired
    private MtExtendSettingsMapper mtExtendSettingsMapper;
    @Autowired
    private HmeWoJobSnRepository hmeWoJobSnRepository;
    @Autowired
    private HmeWoJobSnMapper hmeWoJobSnMapper;
    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;
    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;

    @Autowired
    private MtShiftMapper mtShiftMapper;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private WmsCommonServiceComponent commonServiceComponent;

    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;

    @Autowired
    private WmsTransactionTypeRepository transactionTypeRepository;

    @Autowired
    private HmeEoJobSnLotMaterialMapper hmeEoJobSnLotMaterialMapper;

    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;

    @Autowired
    private ItfObjectTransactionIfaceService itfObjectTransactionIfaceService;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private HmeObjectRecordLockRepository hmeObjectRecordLockRepository;

    @Autowired
    private HmeObjectRecordLockService hmeObjectRecordLockService;


    /**
     * @param tenantId    租户ID
     * @param containerVO 参数
     * @return : com.ruike.hme.domain.vo.HmeInStorageContainerVO
     * @Description: 半成品/成品入库扫描-扫描外箱条码获取信息
     * @author: Deng Xu
     * @date 2020/6/2 16:37
     * @version 1.0
     */
    @Override
    public HmeInStorageContainerVO queryContainer(Long tenantId, HmeInStorageContainerVO containerVO) {
        if (StringUtils.equals(HmeConstants.ConstantValue.NO, containerVO.getSelectFlag())) {
            //物料及物料版本汇总
            Map<String, List<HmeInStorageMaterialVO>> materialMap = containerVO.getSourceMaterialList().stream().collect(Collectors.groupingBy(HmeInStorageMaterialVO::getMaterialVerStr));
            List<HmeInStorageMaterialVO> voList = new ArrayList<>();
            //物料加版本汇总
            for (Map.Entry<String, List<HmeInStorageMaterialVO>> entry : materialMap.entrySet()) {
                HmeInStorageMaterialVO materialVO = new HmeInStorageMaterialVO();
                HmeInStorageMaterialVO materialObj = entry.getValue().get(0);
                BeanUtils.copyProperties(materialObj, materialVO);
                Double total = entry.getValue().stream().map(HmeInStorageMaterialVO::getPrimaryUomQty).filter(Objects::nonNull).collect(toList()).stream().mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                if (total != null) {
                    materialVO.setPrimaryUomQty(BigDecimal.valueOf(total));
                } else {
                    materialVO.setPrimaryUomQty(BigDecimal.ZERO);
                }
                voList.add(materialVO);
            }
            containerVO.setMaterialList(voList);
            return containerVO;
        }

        //返回外箱条码信息
        HmeInStorageContainerVO returnData = new HmeInStorageContainerVO();
        returnData.setContainerCode(containerVO.getContainerCode());
        //调用API【codeOrIdentificationLimitObjectGet】获取物料批或者容器下物料批信息,可能获取到多个
        MtMaterialLotVO30 materialLotVo30 = new MtMaterialLotVO30();
        materialLotVo30.setCode(containerVO.getContainerCode());
        materialLotVo30.setAllLevelFlag(YES);
        MtMaterialLotVO29 mtMaterialLotVO29 = mtMaterialLotRepository.codeOrIdentificationLimitObjectGet(tenantId, materialLotVo30);
        if (mtMaterialLotVO29 == null) {
            throw new MtException("HME_EO_JOB_TIME_SN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_001", "HME"));
        }

        //物料批列表
        List<String> materialLotIdList = new ArrayList<>();
        //容器或物料批id
        String containerId = "";
        if (StringUtils.equals(mtMaterialLotVO29.getCodeType(), CONTAINER)) {
            MtContainer queryContainer = new MtContainer();
            queryContainer.setContainerCode(containerVO.getContainerCode());
            queryContainer.setTenantId(tenantId);
            queryContainer = mtContainerRepository.selectOne(queryContainer);
            //如果获取不到，则提示“当前编码不存在”
            if (null == queryContainer || StringUtils.isEmpty(queryContainer.getContainerId())) {
                throw new MtException("HME_IN_STORER_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_IN_STORER_001", "HME"));
            }

            //如果Status 不等于“CANRELEASE”,则提示“当前编码对应外箱装箱不可用”
            if (!HmeConstants.StatusCode.CANRELEASE.equals(queryContainer.getStatus())) {
                throw new MtException("HME_IN_STORER_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_IN_STORER_002", "HME"));
            }

            containerId = queryContainer.getContainerId();

            //获取容器内的物料批
            if (CollectionUtils.isNotEmpty(mtMaterialLotVO29.getLoadingObjectlList())) {
                mtMaterialLotVO29.getLoadingObjectlList().forEach(vo -> {
                    if (StringUtils.equals(vo.getLoadObjectType(), MATERIAL_LOT)) {
                        materialLotIdList.add(vo.getLoadObjectId());
                    } else if (StringUtils.equals(vo.getLoadObjectType(), CONTAINER)) {
                        //调用API【containerLimitMaterialLotQuery】获取物料批信息,可能获取到多个
                        MtContLoadDtlVO10 contLoadDtlVO10 = new MtContLoadDtlVO10();
                        contLoadDtlVO10.setAllLevelFlag(YES);
                        contLoadDtlVO10.setContainerId(vo.getLoadObjectId());
                        List<MtContLoadDtlVO4> contLoadDtls = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, contLoadDtlVO10);
                        if (CollectionUtils.isNotEmpty(contLoadDtls)) {
                            materialLotIdList.addAll(contLoadDtls.stream().map(MtContLoadDtlVO4::getMaterialLotId).collect(toList()));
                        }
                    }
                });
            }

            if (CollectionUtils.isEmpty(materialLotIdList)) {
                throw new MtException("HME_IN_STORER_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_IN_STORER_003", "HME"));
            }
        } else if (StringUtils.equals(mtMaterialLotVO29.getCodeType(), MATERIAL_LOT)) {
            //调用materialLotPropertyGet-获取物料批属性
            MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, containerVO.getContainerCode());
            if (materialLot == null) {
                throw new MtException("HME_CHIP_TRANSFER_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_002", "HME", containerVO.getContainerCode()));
            }

            materialLotIdList.add(mtMaterialLotVO29.getCodeId());
            containerId = mtMaterialLotVO29.getCodeId();
        }
        returnData.setCodeType(mtMaterialLotVO29.getCodeType());

        //2020-08-13 add by sanfeng.zhang 容器存在上层容器不允许扫描
        MtContLoadDtlVO5 mtContLoadDtlVO5 = new MtContLoadDtlVO5();
        mtContLoadDtlVO5.setLoadObjectType(mtMaterialLotVO29.getCodeType());
        mtContLoadDtlVO5.setLoadObjectId(containerId);
        mtContLoadDtlVO5.setTopLevelFlag(YES);
        List<String> containerIds = mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId, mtContLoadDtlVO5);
        if (CollectionUtils.isNotEmpty(containerIds)) {
            MtContainer mtContainer = mtContainerRepository.selectByPrimaryKey(containerIds.get(0));
            throw new MtException("WMS_COST_CENTER_0055", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0055", WmsConstant.ConstantValue.WMS, containerVO.getContainerCode(), mtContainer != null ? mtContainer.getContainerCode() : ""));
        }

        returnData.setContainerId(containerId);

        //物料批中物料信息集合
        List<HmeInStorageMaterialVO> materialList = new ArrayList<>();
        List<HmeInStorageMaterialVO> sourceMaterialList = new ArrayList<>();
        //物料数量
        BigDecimal primaryUomQty = BigDecimal.ZERO;
        //物料标识，当物料批中物料ID不同时为N
        String sameMaterialFlag = "";
        //用于记录这些物料批中物料ID、编码、名称
        String materialId = "";
        String materialCode = "";
        String materialName = "";
        String materialVersion = "";
        String qualityStatus = "OK";
        Boolean versionFlag = false;
        //可能获取到多个物料批，获取这些物料批的物料信息
        materialList = mapper.queryMaterialLot(materialLotIdList);

        List<String> materialIdList = materialList.stream().map(HmeInStorageMaterialVO::getMaterialId).distinct().collect(toList());
        Integer index = 0;
        if (CollectionUtils.isNotEmpty(materialList)) {
            for (HmeInStorageMaterialVO materialVO : materialList) {
                //累加数量
                if (materialVO.getPrimaryUomQty() != null) {
                    primaryUomQty = primaryUomQty.add(materialVO.getPrimaryUomQty());
                }
                //如果存在mt_material_lot.quality_status不为‘OK’的数据
                if (!HmeConstants.ConstantValue.OK.equals(materialVO.getQualityStatus())) {
//                    qualityStatus = HmeConstants.ConstantValue.NG;
                    throw new MtException("WMS_COST_CENTER_0049", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_COST_CENTER_0049", WmsConstant.ConstantValue.WMS));
                }
                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialVO);
                if (mtMaterialLot == null) {
                    throw new MtException("MT_ASSEMBLE_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0017", "ASSEMBLE", materialVO.getMaterialCode()));
                }
                //校验条码是否在线边仓
                String warehouseId = mapper.queryLocatorCodeByLocatorId(tenantId, mtMaterialLot.getLocatorId());
                MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(warehouseId);
                String locatorType = mtModLocator != null ? mtModLocator.getLocatorType() : "";
                if (!StringUtils.equals(locatorType, "14")) {
                    throw new MtException("HME_IN_STORER_015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_IN_STORER_015", "HME", mtMaterialLot.getMaterialLotCode()));
                }

                //调用materialLotLimitAttrQuery-获取物料批扩展属性
                MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
                mtMaterialLotAttrVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                //物料版本
                mtMaterialLotAttrVO2.setAttrName("MATERIAL_VERSION");
                List<MtExtendAttrVO> versionList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);

                if (index == 0) {
                    materialVersion = CollectionUtils.isNotEmpty(versionList) ? versionList.get(0).getAttrValue() : "";
                }
                String materialVer = CollectionUtils.isNotEmpty(versionList) ? versionList.get(0).getAttrValue() : "";
                if (!StringUtils.equals(materialVer, materialVersion)) {
                    versionFlag = true;
                }
                materialVO.setMaterialVersion(materialVer);
                materialVO.setContainerId(containerId);

                if (!YES.equals(mtMaterialLot.getEnableFlag())) {
                    throw new MtException("HME_IN_STORER_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_IN_STORER_010", "HME"));
                }

                if (YES.equals(mtMaterialLot.getFreezeFlag())) {
                    throw new MtException("HME_IN_STORER_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_IN_STORER_011", "HME"));
                }

                if (YES.equals(mtMaterialLot.getStocktakeFlag())) {
                    throw new MtException("HME_IN_STORER_012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_IN_STORER_012", "HME"));
                }
                //在制品校验
                List<MtExtendSettings> attrList = new ArrayList<>();
                MtExtendSettings mtExtendSettings = new MtExtendSettings();
                mtExtendSettings.setAttrName("MF_FLAG");
                attrList.add(mtExtendSettings);
                List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingMapper.attrPropertyQuery(tenantId,
                        "mt_material_lot_attr", "MATERIAL_LOT_ID", mtMaterialLot.getMaterialLotId(), attrList);
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
                    if (StringUtils.equals(YES, mtExtendAttrVOList.get(0).getAttrValue())) {
                        throw new MtException("HME_IN_STORER_014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_IN_STORER_014", "HME"));
                    }
                }

                List<String> instructionDocIdList = hmeWorkCellDetailsReportMapper.queryInstructionDocId(tenantId, mtMaterialLot.getMaterialLotId());
                if (CollectionUtils.isNotEmpty(instructionDocIdList)) {
                    throw new MtException("HME_IN_STORER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_IN_STORER_013", "HME"));
                }
                materialVO.setMaterialVerStr(materialVO.getMaterialId() + (StringUtils.isBlank(materialVO.getMaterialVersion()) ? "" : materialVO.getMaterialVersion()));

                sourceMaterialList.add(materialVO);
                index++;
            }
        }
        if (CollectionUtils.isNotEmpty(materialIdList)) {
            if (materialIdList.size() == 1) {
                if (CollectionUtils.isNotEmpty(materialList)) {
                    materialCode = materialList.get(0).getMaterialCode();
                    materialName = materialList.get(0).getMaterialName();
                    if (!versionFlag) {
                        materialVersion = materialList.get(0).getMaterialVersion();
                        returnData.setMaterialVersion(materialVersion);
                    }
                }
            }
        }

        returnData.setPrimaryUomQty(primaryUomQty);
        returnData.setSameMaterialFlag(sameMaterialFlag);
        returnData.setMaterialCode(materialCode);
        returnData.setMaterialName(materialName);
        returnData.setQualityStatus(qualityStatus);
        List<HmeInStorageMaterialVO> newMaterialList = new ArrayList<>();
        returnData.setSourceMaterialList(sourceMaterialList);
        materialList.addAll(containerVO.getMaterialList());
        //物料及物料版本汇总
        Map<String, List<HmeInStorageMaterialVO>> materialMap = materialList.stream().collect(Collectors.groupingBy(HmeInStorageMaterialVO::getMaterialVerStr));

        //物料加版本汇总
        for (Map.Entry<String, List<HmeInStorageMaterialVO>> entry : materialMap.entrySet()) {
            HmeInStorageMaterialVO materialVO = new HmeInStorageMaterialVO();
            HmeInStorageMaterialVO materialObj = entry.getValue().get(0);
            BeanUtils.copyProperties(materialObj, materialVO);
            Double total = entry.getValue().stream().map(HmeInStorageMaterialVO::getPrimaryUomQty).filter(Objects::nonNull).collect(toList()).stream().mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
            if (total != null) {
                materialVO.setPrimaryUomQty(BigDecimal.valueOf(total));
            } else {
                materialVO.setPrimaryUomQty(BigDecimal.ZERO);
            }
            newMaterialList.add(materialVO);
        }

        returnData.setMaterialList(newMaterialList);
        return returnData;
    }

    /**
     * @param tenantId    租户ID
     * @param containerId 外箱ID
     * @return : java.util.List<com.ruike.hme.domain.vo.HmeInStorageMaterialVO>
     * @Description: 半成品/成品入库扫描-装箱明细查询
     * @author: Deng Xu
     * @date 2020/6/3 11:02
     * @version 1.0
     */
    @Override
    public List<HmeInStorageMaterialVO> queryContainerDetail(Long tenantId, String containerId) {
        //调用API【containerLimitMaterialLotQuery】查询物料批信息
        MtContLoadDtlVO10 contLoadDtlVO10 = new MtContLoadDtlVO10();
        contLoadDtlVO10.setAllLevelFlag(YES);
        contLoadDtlVO10.setContainerId(containerId);
        List<MtContLoadDtlVO4> contLoadDtls = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, contLoadDtlVO10);
        if (CollectionUtils.isEmpty(contLoadDtls)) {
            return new ArrayList<>();
        }
        List<String> materialLotIdList = contLoadDtls.stream().map(MtContLoadDtlVO4::getMaterialLotId)
                .distinct().collect(toList());
        if (CollectionUtils.isEmpty(materialLotIdList)) {
            return new ArrayList<>();
        }
        //根据物料批ID集合查询信息
        List<HmeInStorageMaterialVO> returnList = mapper.queryMaterialLot(materialLotIdList);
        //获取状态描述
        for (HmeInStorageMaterialVO materialVO : returnList) {
            if (StringUtils.isNotEmpty(materialVO.getQualityStatus())) {
                //调用api【groupLimitStatusQuery】
                MtGenStatusVO2 mtGenStatusVO2 = new MtGenStatusVO2();
                mtGenStatusVO2.setModule("MATERIAL_LOT");
                mtGenStatusVO2.setStatusGroup("QUALITY_STATUS");
                mtGenStatusVO2.setStatusCode(materialVO.getQualityStatus());

                //调用materialLotLimitAttrQuery-获取物料批扩展属性
                MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
                mtMaterialLotAttrVO2.setMaterialLotId(materialVO.getMaterialLotId());
                //物料版本
                mtMaterialLotAttrVO2.setAttrName("MATERIAL_VERSION");
                List<MtExtendAttrVO> versionList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
                if (CollectionUtils.isNotEmpty(versionList)) {
                    materialVO.setMaterialVersion(versionList.get(0).getAttrValue());
                }

                List<MtGenStatus> genStatusList =
                        mtGenStatusRepository.groupLimitStatusQuery(tenantId, mtGenStatusVO2);
                if (null != genStatusList.get(0) && StringUtils.isNotEmpty(genStatusList.get(0).getDescription())) {
                    materialVO.setQualityStatusDes(genStatusList.get(0).getDescription());
                }
            }
        }
        return returnList;
    }

    /**
     * @param tenantId        租户ID
     * @param containerVOList 容器对象集合
     * @return : void
     * @Description: 半成品/成品入库扫描-入库
     * @author: Deng Xu
     * @date 2020/6/3 13:46
     * @version 1.0
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeInStorageContainerVO> finishProductsInStorage(Long tenantId, List<HmeInStorageContainerVO> containerVOList) {
        for (HmeInStorageContainerVO hmeInStorageContainerVO : containerVOList) {
            //加锁
            HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
            hmeObjectRecordLockDTO.setFunctionName("成品入库扫描");
            hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PDA);
            hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.BARCODE);
            hmeObjectRecordLockDTO.setObjectRecordId(hmeInStorageContainerVO.getContainerId());
            hmeObjectRecordLockDTO.setObjectRecordCode(hmeInStorageContainerVO.getContainerCode());
            HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
            //加锁
            hmeObjectRecordLockRepository.commonLockWo(hmeObjectRecordLock);
        }
        try {
            //校验容器集合是否传递,否则报错：请先输入或扫描外箱编码
            if (CollectionUtils.isEmpty(containerVOList)) {
                throw new MtException("HME_IN_STORER_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_IN_STORER_004", "HME"));
            }
            String batchId = Utils.getBatchId();
            //调用{eventRequestCreate}创建请求事件
            String receiptRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "PRODUCT_RECEIPT_CREATE");

            //调用{eventCreate}创建事件
            MtEventCreateVO receiptEvent = new MtEventCreateVO();
            receiptEvent.setEventTypeCode("PRODUCT_RECEIPT_CREATE");
            receiptEvent.setEventRequestId(receiptRequestId);
            String receiptEventId = mtEventRepository.eventCreate(tenantId, receiptEvent);


            //调用API【instructionDocUpdate】执行单据创建
            Long userId = DetailsHelper.getUserDetails().getUserId();
            String siteId = wmsSiteRepository.userDefaultSite(tenantId);
            MtInstructionDocDTO2 instructionDoc = new MtInstructionDocDTO2();
            instructionDoc.setInstructionDocStatus(HmeConstants.StatusCode.RELEASED);
            instructionDoc.setInstructionDocType("PRODUCT_RECEIPT");
            instructionDoc.setSiteId(siteId);
            instructionDoc.setDemandTime(new Date());
            instructionDoc.setEventRequestId(receiptRequestId);
            instructionDoc.setPersonId(userId);

            MtInstructionDocVO3 newInstructionDoc = mtInstructionDocRepository.instructionDocUpdate(tenantId, instructionDoc, HmeConstants.ConstantValue.NO);
            if (StringUtils.isEmpty(newInstructionDoc.getInstructionDocId())) {
                throw new MtException("HME_IN_STORER_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_IN_STORER_005", "HME"));
            }

            //所有外箱物料批信息
            List<HmeInStorageMaterialVO> materialList = new ArrayList<>();
            //外箱依次执行
            String locatorId = "";
            List<String> containerIdList = new ArrayList<>();
            List<String> scanMaterialLotIdList = new ArrayList<>();
            for (HmeInStorageContainerVO containerVO : containerVOList) {
                if (StringUtils.isBlank(containerVO.getLocatorId())) {
                    throw new MtException("HME_IN_STORER_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_IN_STORER_007", "HME"));
                }
                locatorId = containerVO.getLocatorId();

                List<String> materialLotIdList = new ArrayList<>();
                //判断容器还是物料批 调用API【codeOrIdentificationLimitObjectGet】
                MtMaterialLotVO30 materialLotVo30 = new MtMaterialLotVO30();
                materialLotVo30.setCode(containerVO.getContainerCode());
                materialLotVo30.setAllLevelFlag(YES);
                MtMaterialLotVO29 mtMaterialLotVO29 = mtMaterialLotRepository.codeOrIdentificationLimitObjectGet(tenantId, materialLotVo30);
                if (mtMaterialLotVO29 == null) {
                    throw new MtException("HME_EO_JOB_TIME_SN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_TIME_SN_001", "HME"));
                }

                if (StringUtils.equals(mtMaterialLotVO29.getCodeType(), "MATERIAL_LOT")) {
                    //物料批
                    MtMaterialLot mtMaterialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, containerVO.getContainerCode());
                    scanMaterialLotIdList.add(mtMaterialLot.getMaterialLotId());
                    materialLotIdList.add(mtMaterialLot.getMaterialLotId());
                } else if (StringUtils.equals(mtMaterialLotVO29.getCodeType(), "CONTAINER")) {
                    MtContainer queryContainer = new MtContainer();
                    queryContainer.setContainerCode(containerVO.getContainerCode());
                    queryContainer.setTenantId(tenantId);
                    queryContainer = mtContainerRepository.selectOne(queryContainer);
                    //如果获取不到，则提示“当前编码不存在”
                    if (null == queryContainer || StringUtils.isEmpty(queryContainer.getContainerId())) {
                        throw new MtException("HME_IN_STORER_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_IN_STORER_001", "HME"));
                    }

                    //获取容器内的物料批
                    if (CollectionUtils.isNotEmpty(mtMaterialLotVO29.getLoadingObjectlList())) {
                        mtMaterialLotVO29.getLoadingObjectlList().forEach(vo -> {
                            if (StringUtils.equals(vo.getLoadObjectType(), "MATERIAL_LOT")) {
                                materialLotIdList.add(vo.getLoadObjectId());
                            } else if (StringUtils.equals(vo.getLoadObjectType(), "CONTAINER")) {
                                //调用API【containerLimitMaterialLotQuery】获取物料批信息,可能获取到多个
                                MtContLoadDtlVO10 contLoadDtlVO10 = new MtContLoadDtlVO10();
                                contLoadDtlVO10.setAllLevelFlag(YES);
                                contLoadDtlVO10.setContainerId(vo.getLoadObjectId());
                                List<MtContLoadDtlVO4> contLoadDtls = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, contLoadDtlVO10);
                                if (CollectionUtils.isNotEmpty(contLoadDtls)) {
                                    materialLotIdList.addAll(contLoadDtls.stream().map(MtContLoadDtlVO4::getMaterialLotId).collect(toList()));
                                }
                            }
                        });
                    }
                    containerIdList.add(containerVO.getContainerId());
                }
                if (CollectionUtils.isEmpty(materialLotIdList)) {
                    throw new MtException("HME_IN_STORER_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_IN_STORER_003", "HME"));
                }
                //可能获取到多个物料批，获取这些物料批的物料信息
                List<HmeInStorageMaterialVO> materialVOList = mapper.queryMaterialLot(materialLotIdList);
                materialVOList.forEach(e -> {
                    e.setContainerId(containerVO.getContainerId());
                    e.setSourceDocId(newInstructionDoc.getInstructionDocId());
                });
                materialList.addAll(materialVOList);
            }
            List<String> materialLotIds = materialList.stream().map(HmeInStorageMaterialVO::getMaterialLotId).distinct().collect(toList());
            MtExtendVO1 mtExtendVO = new MtExtendVO1();
            mtExtendVO.setTableName("mt_material_lot_attr");
            mtExtendVO.setKeyIdList(materialLotIds);
            List<MtExtendVO5> attrs = new ArrayList<>();
            MtExtendVO5 mvAttr = new MtExtendVO5();
            mvAttr.setAttrName("MATERIAL_VERSION");
            attrs.add(mvAttr);
            MtExtendVO5 statusAttr = new MtExtendVO5();
            statusAttr.setAttrName("STATUS");
            attrs.add(statusAttr);
            mtExtendVO.setAttrs(attrs);
            List<MtExtendAttrVO1> attrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO);
            List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>();
            for (HmeInStorageMaterialVO materialVO : materialList) {
                //调用materialLotLimitAttrQuery-获取物料批扩展属性
                Optional<MtExtendAttrVO1> materialVersionOpt = attrVO1List.stream().filter(attrVO1 -> StringUtils.equals(materialVO.getMaterialLotId(), attrVO1.getKeyId()) && StringUtils.equals("MATERIAL_VERSION", attrVO1.getAttrName())).findFirst();
                materialVO.setMaterialVersion(materialVersionOpt.isPresent() ? materialVersionOpt.get().getAttrValue() : "");
                materialVO.setMaterialVerStr(materialVO.getMaterialId() + (StringUtils.isBlank(materialVO.getMaterialVersion()) ? "" : materialVO.getMaterialVersion()));
                // 更新条码状态 不为待上架 则更新为待上架
                Optional<MtExtendAttrVO1> statusOpt = attrVO1List.stream().filter(attrVO1 -> StringUtils.equals(materialVO.getMaterialLotId(), attrVO1.getKeyId()) && StringUtils.equals("STATUS", attrVO1.getAttrName())).findFirst();
                String barcodeStatus = statusOpt.isPresent() ? statusOpt.get().getAttrValue() : "";
                // 20210914 add by sanfeng.zhang for hui.gu 已入库则不允许成品入库
                if (StringUtils.equals(barcodeStatus, "INSTOCK")) {
                    throw new MtException("HME_IN_STORER_023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_IN_STORER_023", "HME", materialVO.getMaterialLotCode()));
                }
                // 20210914 add by sanfeng.zhang for hui.gu 条码【${1}】所在仓库与目标仓库一致,请检查
                if (StringUtils.equals(materialVO.getWarehouseId(), locatorId)) {
                    throw new MtException("HME_IN_STORER_022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_IN_STORER_022", "HME", materialVO.getMaterialLotCode()));
                }

                if (!StringUtils.equals(barcodeStatus, "TO_ACCEPT")) {
                    MtCommonExtendVO6 mtCommonExtendVO6 = new MtCommonExtendVO6();
                    mtCommonExtendVO6.setKeyId(materialVO.getMaterialLotId());
                    List<MtCommonExtendVO5> attrList = new ArrayList<>();
                    MtCommonExtendVO5 mtCommonExtendVO5 = new MtCommonExtendVO5();
                    mtCommonExtendVO5.setAttrName("STATUS");
                    mtCommonExtendVO5.setAttrValue("TO_ACCEPT");
                    attrList.add(mtCommonExtendVO5);
                    mtCommonExtendVO6.setAttrs(attrList);
                    attrPropertyList.add(mtCommonExtendVO6);
                }
                // 校验条码是否生成入库单产品
                List<String> instructionDocIdList = hmeWorkCellDetailsReportMapper.queryInstructionDocId(tenantId, materialVO.getMaterialLotId());
                if (CollectionUtils.isNotEmpty(instructionDocIdList)) {
                    throw new MtException("HME_IN_STORER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_IN_STORER_013", "HME"));
                }
            }
            if (CollectionUtils.isNotEmpty(attrPropertyList)) {
                mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr", receiptEventId, attrPropertyList);
            }

            //将所有物料批信息根据物料ID分组
            Map<String, List<HmeInStorageMaterialVO>> groupMaterialList = materialList.stream().collect(Collectors.groupingBy(item -> item.getMaterialVerStr()));
            //赋值行和头id
            List<HmeInStorageMaterialVO> storageMaterialVOList = new ArrayList<>();
            //对于每个物料进行操作
            Integer index = 1;
            List<HmeInstructionVO> mtInstructionVO6List = new ArrayList<>();
            for (Map.Entry<String, List<HmeInStorageMaterialVO>> entry : groupMaterialList.entrySet()) {
                List<HmeInStorageMaterialVO> materialLotList = entry.getValue();
                String materialId = materialLotList.get(0).getMaterialId();
                List<String> materialLotIdList = materialLotList.stream().map(HmeInStorageMaterialVO::getMaterialLotId).distinct().collect(toList());
                //将这些物料批数量汇总
                Double quantity = materialLotList.stream().map(HmeInStorageMaterialVO::getPrimaryUomQty).filter(Objects::nonNull).collect(toList()).stream().mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                //调用API【instructionUpdate】执行指令创建
                MtInstructionVO mtInstructionVO = new MtInstructionVO();
                mtInstructionVO.setInstructionStatus(HmeConstants.StatusCode.RELEASED);
                mtInstructionVO.setSourceDocId(newInstructionDoc.getInstructionDocId());
                mtInstructionVO.setSiteId(siteId);
                mtInstructionVO.setFromSiteId(siteId);
                mtInstructionVO.setToSiteId(siteId);
                String fromParentLocatorId = mapper.queryLocatorCodeByLocatorId(tenantId, materialLotList.get(0).getLocatorId());
                mtInstructionVO.setFromLocatorId(fromParentLocatorId);

                mtInstructionVO.setToLocatorId(locatorId);
                mtInstructionVO.setMaterialId(materialId);
                mtInstructionVO.setUomId(materialLotList.get(0).getPrimaryUomId());
                mtInstructionVO.setQuantity(quantity);
                mtInstructionVO.setInstructionType("TRANSFER_OVER_LOCATOR");
                mtInstructionVO.setDemandTime(new Date());
                mtInstructionVO.setBusinessType("PRODUCT_RECEIPT");
                mtInstructionVO.setEventRequestId(receiptRequestId);
                MtInstructionVO6 newInstruction = mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, HmeConstants.ConstantValue.NO);
                //对于每个物料里的物料批调用API【instructionDetailCreate】执行指令明细行创建
                if (CollectionUtils.isNotEmpty(materialLotIdList)) {
                    MtInstructionDetailVO2 instructionDetailVO2 = new MtInstructionDetailVO2();
                    instructionDetailVO2.setInstructionId(newInstruction.getInstructionId());
                    instructionDetailVO2.setMaterialLotIdList(materialLotIdList);
                    mtInstructionDetailRepository.instructionDetailCreate(tenantId, instructionDetailVO2);
                }

                List<MtExtendVO5> extendList = new ArrayList<>();
                MtExtendVO5 mtExtendOne = new MtExtendVO5();
                mtExtendOne.setAttrName("INSTRUCTION_LINE_NUM");
                Integer sort = index * 10;
                mtExtendOne.setAttrValue(String.valueOf(sort));
                extendList.add(mtExtendOne);

                MtExtendVO5 mtExtendTwo = new MtExtendVO5();
                mtExtendTwo.setAttrName("MATERIAL_VERSION");
                mtExtendTwo.setAttrValue(materialLotList.get(0).getMaterialVersion());
                extendList.add(mtExtendTwo);
                mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_attr", newInstruction.getInstructionId(), receiptEventId, extendList);

                HmeInstructionVO hmeInstructionVO = new HmeInstructionVO();
                BeanUtils.copyProperties(newInstruction, hmeInstructionVO);
                List<HmeInStorageMaterialVO> list = new ArrayList<>();
                //赋值行id
                for (HmeInStorageMaterialVO storageMaterialVO : materialLotList) {
                    MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(storageMaterialVO.getMaterialLotId());

                    HmeInStorageMaterialVO materialVO = new HmeInStorageMaterialVO();
                    BeanUtils.copyProperties(storageMaterialVO, materialVO);
                    materialVO.setSourceDocLineId(newInstruction.getInstructionId());
                    materialVO.setParentLocatorId(mtMaterialLot != null ? mtMaterialLot.getLocatorId() : "");
                    storageMaterialVOList.add(materialVO);

                    storageMaterialVO.setParentLocatorId(mtMaterialLot != null ? mtMaterialLot.getLocatorId() : "");
                    list.add(storageMaterialVO);
                }

                hmeInstructionVO.setMaterialLotListList(list);
                mtInstructionVO6List.add(hmeInstructionVO);

                index++;
            }

            //2020-08-13 add by sanfeng.zhang 入库过账逻辑
            String attrVal = mapper.queryLocatorAttr(tenantId, locatorId);
            //Y 执行过账
            if (StringUtils.equals(YES, attrVal)) {
                //调用{eventRequestCreate}创建请求事件
                String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "PRODUCT_RECEIPT");

                //调用{eventCreate}创建事件
                MtEventCreateVO event = new MtEventCreateVO();
                event.setEventTypeCode("PRODUCT_RECEIPT");
                event.setEventRequestId(eventRequestId);
                String eventId = mtEventRepository.eventCreate(tenantId, event);

                // 查询该库位跟顶层站点类型关系
                MtModLocatorOrgRelVO2 rel = new MtModLocatorOrgRelVO2();
                rel.setLocatorId(locatorId);
                rel.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.SITE);
                List<MtModLocatorOrgRelVO3> existSiteList = mtModLocatorOrgRelRepository.locatorLimitOrganizationQuery(tenantId, rel);
                if (CollectionUtils.isEmpty(existSiteList)) {
                    throw new MtException("HME_IN_STORER_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_IN_STORER_008", "HME"));
                }

                //目标仓库
                Condition condition = new Condition(MtModLocator.class);
                condition.and().andEqualTo("parentLocatorId", locatorId)
                        .andEqualTo("locatorType", "DEFAULT_STORAGE")
                        .andEqualTo("locatorCategory", "INVENTORY");
                List<MtModLocator> mtModLocators = mtModLocatorRepository.selectByCondition(condition);
                if (CollectionUtils.isEmpty(mtModLocators)) {
                    throw new MtException("HME_IN_STORER_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_IN_STORER_009", "HME"));
                }


                for (HmeInStorageMaterialVO materialVO : storageMaterialVOList) {
                    MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialVO.getMaterialLotId());

                    //调用{attrTableUpdate}更新物料批拓展表
                    List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
                    MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                    mtExtendVO5.setAttrName("STATUS");
                    mtExtendVO5.setAttrValue("INSTOCK");
                    mtExtendVO5List.add(mtExtendVO5);
                    // 20210820 add by sanfeng.zhang for peng.zhao 去掉售后标识
                    MtExtendVO5 afFlagAttr = new MtExtendVO5();
                    afFlagAttr.setAttrName("AF_FLAG");
                    afFlagAttr.setAttrValue("");
                    mtExtendVO5List.add(afFlagAttr);
                    mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", mtMaterialLot.getMaterialLotId(), eventId, mtExtendVO5List);

                    //记录入库事务
                    List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
                    WmsObjectTransactionRequestVO wmsObjectTransactionRequestVO = new WmsObjectTransactionRequestVO();
                    String parentLocatorId = mapper.queryLocatorCodeByLocatorId(tenantId, mtMaterialLot.getLocatorId());
                    if (StringUtils.equals(locatorId, parentLocatorId)) {
                        wmsObjectTransactionRequestVO.setTransactionTypeCode("WMS_LOCATOR_TRAN");
                    } else {
                        wmsObjectTransactionRequestVO.setTransactionTypeCode("WMS_WAREHOUSE_TRAN");
                    }

                    wmsObjectTransactionRequestVO.setEventId(eventId);
                    wmsObjectTransactionRequestVO.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                    wmsObjectTransactionRequestVO.setMaterialId(materialVO.getMaterialId());
                    wmsObjectTransactionRequestVO.setTransactionQty(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()));
                    String primaryUomId = mtMaterialLot.getPrimaryUomId();
                    MtUom mtUom = mtUomRepository.selectByPrimaryKey(primaryUomId);
                    wmsObjectTransactionRequestVO.setLotNumber(mtMaterialLot.getLot());
                    wmsObjectTransactionRequestVO.setTransferLotNumber(mtMaterialLot.getLot());
                    wmsObjectTransactionRequestVO.setTransactionUom(mtUom != null ? mtUom.getUomCode() : "");
                    wmsObjectTransactionRequestVO.setTransactionTime(new Date());
                    wmsObjectTransactionRequestVO.setTransactionReasonCode("成品入库");
                    wmsObjectTransactionRequestVO.setPlantId(mtMaterialLot.getSiteId());
                    wmsObjectTransactionRequestVO.setWarehouseId(parentLocatorId);
                    MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(mtMaterialLot.getLocatorId());
                    wmsObjectTransactionRequestVO.setLocatorId(mtModLocator != null ? mtModLocator.getLocatorId() : "");
                    wmsObjectTransactionRequestVO.setTransferPlantId(existSiteList.get(0).getOrganizationId());
                    wmsObjectTransactionRequestVO.setTransferWarehouseId(locatorId);
                    wmsObjectTransactionRequestVO.setTransferLocatorId(mtModLocators.get(0).getLocatorId());

                    wmsObjectTransactionRequestVO.setSourceDocType("PRODUCT_RECEIPT");
                    wmsObjectTransactionRequestVO.setMergeFlag("N");
                    //增加soNum soLineNum
                    MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
                    mtMaterialLotAttrVO2.setAttrName("SO_NUM");
                    mtMaterialLotAttrVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                    List<MtExtendAttrVO> soNumList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
                    if (CollectionUtils.isNotEmpty(soNumList)) {
                        wmsObjectTransactionRequestVO.setSoNum(soNumList.get(0).getAttrValue());
                    }

                    mtMaterialLotAttrVO2.setAttrName("SO_LINE_NUM");
                    List<MtExtendAttrVO> soLineNumList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
                    if (CollectionUtils.isNotEmpty(soLineNumList)) {
                        wmsObjectTransactionRequestVO.setSoLineNum(soLineNumList.get(0).getAttrValue());
                    }

                    wmsObjectTransactionRequestVO.setSourceDocId(materialVO.getSourceDocId());
                    wmsObjectTransactionRequestVO.setSourceDocLineId(materialVO.getSourceDocLineId());

                    Condition typeCondition = new Condition(WmsTransactionType.class);
                    typeCondition.and().andEqualTo("transactionTypeCode", wmsObjectTransactionRequestVO.getTransactionTypeCode());
                    List<WmsTransactionType> wmsTransactionTypes = wmsTransactionTypeRepository.selectByCondition(typeCondition);
                    wmsObjectTransactionRequestVO.setMoveType(CollectionUtils.isEmpty(wmsTransactionTypes) ? "" : wmsTransactionTypes.get(0).getMoveType());

                    MtContainer mtContainer = mtContainerRepository.selectByPrimaryKey(materialVO.getContainerId());
                    if (mtContainer != null) {
                        wmsObjectTransactionRequestVO.setContainerId(materialVO.getContainerId());
                    }
                    wmsObjectTransactionRequestVO.setAttribute16(batchId);
                    objectTransactionRequestList.add(wmsObjectTransactionRequestVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
                }


                //执行容器库存转移及事务记录
                for (String container : containerIdList) {
                    MtContainerVO7 dto = new MtContainerVO7();
                    dto.setContainerId(container);
                    dto.setEventRequestId(eventRequestId);

                    dto.setTargetSiteId(existSiteList.get(0).getOrganizationId());

                    dto.setTargetLocatorId(mtModLocators.get(0).getLocatorId());

                    //转移容器
                    mtContainerRepository.containerTransfer(tenantId, dto);
                }

                //执行物料批库存转移及事务记录
                if (CollectionUtils.isNotEmpty(scanMaterialLotIdList)) {
                    MtMaterialLotVO14 dto = new MtMaterialLotVO14();

                    dto.setMaterialLotIds(scanMaterialLotIdList);
                    dto.setEventRequestId(eventRequestId);

                    dto.setTargetSiteId(existSiteList.get(0).getOrganizationId());

                    dto.setTargetLocatorId(mtModLocators.get(0).getLocatorId());

                    //转移物料批
                    mtMaterialLotRepository.materialLotBatchTransfer(tenantId, dto);
                }
                List<MtInstructionVO3> mtInstructionVO3List = new ArrayList<>();

                //执行并完成指令，执行并完成指令，所有指令完成后完成指令单据
                for (HmeInstructionVO mtInstructionVO6 : mtInstructionVO6List) {
                    List<MtInstructionVO3.MaterialLotList> list = new ArrayList<>();
                    for (HmeInStorageMaterialVO materialVO : mtInstructionVO6.getMaterialLotListList()) {
                        //获取每行的物料批列表
                        MtInstructionVO3.MaterialLotList newMaterialLotList = new MtInstructionVO3.MaterialLotList();
                        newMaterialLotList.setMaterialLotId(materialVO.getMaterialLotId());
                        newMaterialLotList.setQty(materialVO.getPrimaryUomQty().doubleValue());
                        newMaterialLotList.setContainerId(materialVO.getContainerId());
                        newMaterialLotList.setToLocatorId(mtModLocators.get(0).getLocatorId());
                        newMaterialLotList.setFromLocatorId(materialVO.getParentLocatorId());
                        newMaterialLotList.setUomId(materialVO.getPrimaryUomId());
                        list.add(newMaterialLotList);
                    }

                    MtInstructionVO3 logisticInstructionVo3 = new MtInstructionVO3();
                    logisticInstructionVo3.setInstructionId(mtInstructionVO6.getInstructionId());
                    logisticInstructionVo3.setMaterialLotMessageList(list);
                    logisticInstructionVo3.setEventRequestId(eventRequestId);

                    mtInstructionRepository.instructionExecute(tenantId, logisticInstructionVo3);

                    mtInstructionRepository.instructionCompleteVerify(tenantId, mtInstructionVO6.getInstructionId());

                    mtInstructionRepository.instructionComplete(tenantId, mtInstructionVO6.getInstructionId(), eventRequestId);

                    mtInstructionVO3List.add(logisticInstructionVo3);
                }

                //查询所有行状态为COMPLETED,更新指令状态
                List<MtInstruction> mtInstructionList = mtInstructionRepository.selectByCondition(Condition.builder(MtInstruction.class)
                        .andWhere(Sqls.custom().andEqualTo(MtInstruction.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(MtInstruction.FIELD_SOURCE_DOC_ID, newInstructionDoc.getInstructionDocId())).build());
                if (CollectionUtils.isNotEmpty(mtInstructionList)) {
                    String instructionStatus = "COMPLETED";
                    Boolean completeStatus = false;
                    for (MtInstruction mtInstruction : mtInstructionList) {
                        if (!StringUtils.equals(instructionStatus, mtInstruction.getInstructionStatus())) {
                            completeStatus = true;
                        }
                    }
                    if (!completeStatus) {
                        mtInstructionDocRepository.instructionDocComplete(tenantId, newInstructionDoc.getInstructionDocId(), eventRequestId);
                    }
                }

                //删除mt_instruction_detail
                if (CollectionUtils.isNotEmpty(mtInstructionVO3List)) {
                    for (MtInstructionVO3 mtInstructionVO3 : mtInstructionVO3List) {
                        List<String> materialLotIdList = mtInstructionVO3.getMaterialLotMessageList().stream().map(MtInstructionVO3.MaterialLotList::getMaterialLotId).collect(toList());
                        List<MtInstructionDetail> detail = mtInstructionDetailRepository.selectByCondition(Condition.builder(MtInstructionDetail.class)
                                .andWhere(Sqls.custom().andEqualTo(MtInstructionDetail.FIELD_TENANT_ID, tenantId)
                                        .andEqualTo(MtInstructionDetail.FIELD_INSTRUCTION_ID, mtInstructionVO3.getInstructionId())
                                        .andIn(MtInstructionDetail.FIELD_MATERIAL_LOT_ID, materialLotIdList)).build());

                        mtInstructionDetailRepository.batchDeleteByPrimaryKey(detail);
                    }
                }
            }
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        } finally {
            for (HmeInStorageContainerVO hmeInStorageContainerVO : containerVOList) {
                HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
                hmeObjectRecordLockDTO.setFunctionName("成品入库扫描");
                hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
                hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.BARCODE);
                hmeObjectRecordLockDTO.setObjectRecordId(hmeInStorageContainerVO.getContainerId());
                hmeObjectRecordLockDTO.setObjectRecordCode(hmeInStorageContainerVO.getContainerCode());
                HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
                //解锁
                hmeObjectRecordLockRepository.releaseLock(hmeObjectRecordLock, HmeConstants.ConstantValue.YES);
            }
        }

        return containerVOList;
    }

    @Override
    public List<MtModLocator> queryLocatorList(Long tenantId) {
        Long userId = DetailsHelper.getUserDetails().getUserId();
        MtUserOrganization mtUserOrganization = new MtUserOrganization();
        mtUserOrganization.setUserId(userId);
        mtUserOrganization.setOrganizationType("LOCATOR");
        List<MtUserOrganization> mtUserOrganizationList = mtUserOrganizationRepository.userOrganizationPermissionQuery(tenantId, mtUserOrganization);
        List<String> organizationIdList = mtUserOrganizationList.stream().map(MtUserOrganization::getOrganizationId).collect(toList());
        if (CollectionUtils.isEmpty(organizationIdList)) {
            return Collections.emptyList();
        }
        return mapper.queryLocatorList(tenantId, organizationIdList);
    }

    @Override
    public MtModLocator queryDefaultLocatorList(Long tenantId) {
        Long userId = DetailsHelper.getUserDetails().getUserId();
        MtUserOrganization userOrganization = new MtUserOrganization();
        userOrganization.setUserId(userId);
        userOrganization.setOrganizationType("LOCATOR");
        MtUserOrganization defaultSite = mtUserOrganizationRepository.userDefaultOrganizationGet(tenantId, userOrganization);
        if (null == defaultSite) {
            throw new MtException("MT_PERMISSION_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_PERMISSION_0006", "PERMISSION", "【API:defaultSiteUi】"));
        }

        List<String> organizationIdList = new ArrayList<>();
        organizationIdList.add(defaultSite.getOrganizationId());
        List<MtModLocator> mtModLocatorList = mapper.queryLocatorList(tenantId, organizationIdList);
        if (CollectionUtils.isEmpty(mtModLocatorList)) {
            return null;
        }
        return mtModLocatorList.get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCosResVO scanMaterialLot(Long tenantId, HmeCosScanCodeVO codeVO) {
        HmeCosResVO resVO = new HmeCosResVO();
        //扫描条码后校验
        MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, codeVO.getMaterialLotCode());
        if (materialLot == null) {
            throw new MtException("HME_CHIP_TRANSFER_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_002", "HME", codeVO.getMaterialLotCode()));
        }
        this.verifyMaterialLotCode(tenantId, materialLot);

        resVO.setMaterialLotCode(materialLot.getMaterialLotCode());
        resVO.setMaterialLotId(materialLot.getMaterialLotId());
        resVO.setMaterialId(materialLot.getMaterialId());
        resVO.setQty(BigDecimal.valueOf(materialLot.getPrimaryUomQty()));
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(materialLot.getMaterialId());
        if (mtMaterial != null) {
            resVO.setMaterialCode(mtMaterial.getMaterialCode());
            resVO.setMaterialName(mtMaterial.getMaterialName());
        }
        List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_WORKCELL_ID, codeVO.getWorkcellId())
                        .andEqualTo(HmeEoJobSn.FIELD_OPERATION_ID, codeVO.getOperationId())
                        .andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, materialLot.getMaterialLotId())
                        .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE, HmeConstants.JobType.COS_COMPLETED)
                        .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE)).build());
        //工单扩展字段
        MtExtendSettings reworkAttr = new MtExtendSettings();
        reworkAttr.setAttrName("WORK_ORDER_ID");
        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                "mt_material_lot_attr", "MATERIAL_LOT_ID", materialLot.getMaterialLotId(), Collections.singletonList(reworkAttr));
        if (CollectionUtils.isEmpty(mtExtendAttrVOList)) {
            throw new MtException("HME_NC_0021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0021", "HME", materialLot.getMaterialLotCode()));
        }
        resVO.setWorkOrderId(mtExtendAttrVOList.get(0).getAttrValue());

        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        Long userId = userDetails != null ? userDetails.getUserId() : -1L;

        if (CollectionUtils.isEmpty(hmeEoJobSnList)) {
            //不存在 则新增数据
            HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
            hmeEoJobSn.setTenantId(tenantId);
            hmeEoJobSn.setSiteInDate(new Date());
            hmeEoJobSn.setSiteInBy(userId);
            hmeEoJobSn.setShiftId(codeVO.getShiftId());
            hmeEoJobSn.setWorkcellId(codeVO.getWorkcellId());
            hmeEoJobSn.setOperationId(codeVO.getOperationId());
            hmeEoJobSn.setWorkOrderId(mtExtendAttrVOList.get(0).getAttrValue());
            hmeEoJobSn.setMaterialLotId(materialLot.getMaterialLotId());
            hmeEoJobSn.setSnMaterialId(materialLot.getMaterialId());
            hmeEoJobSn.setReworkFlag(HmeConstants.ConstantValue.NO);
            hmeEoJobSn.setJobType(HmeConstants.JobType.COS_COMPLETED);
            if (materialLot.getPrimaryUomQty() != null) {
                hmeEoJobSn.setSnQty(BigDecimal.valueOf(materialLot.getPrimaryUomQty()));
            }
            hmeEoJobSnRepository.insertSelective(hmeEoJobSn);

            resVO.setEoJobSnId(hmeEoJobSn.getJobId());

            //工单工艺在制记录表
            List<HmeWoJobSn> hmeWoJobSnList = hmeWoJobSnRepository.selectByCondition(Condition.builder(HmeWoJobSn.class)
                    .andWhere(Sqls.custom().andEqualTo(HmeWoJobSn.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeWoJobSn.FIELD_WORK_ORDER_ID, mtExtendAttrVOList.get(0).getAttrValue())
                            .andEqualTo(HmeWoJobSn.FIELD_OPERATION_ID, codeVO.getOperationId())).build());

            if (CollectionUtils.isEmpty(hmeWoJobSnList)) {
                HmeWoJobSn hmeWoJobSn = new HmeWoJobSn();
                hmeWoJobSn.setTenantId(tenantId);
                String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
                hmeWoJobSn.setSiteId(defaultSiteId);
                hmeWoJobSn.setWorkOrderId(mtExtendAttrVOList.get(0).getAttrValue());
                hmeWoJobSn.setOperationId(codeVO.getOperationId());
                hmeWoJobSn.setSiteInNum(materialLot.getPrimaryUomQty().longValue());
                hmeWoJobSn.setObjectVersionNumber(0L);
                hmeWoJobSnRepository.insertSelective(hmeWoJobSn);

                resVO.setWoJobSnId(hmeWoJobSn.getWoJobSnId());
            } else {
                HmeWoJobSn hmeWoJobSn = hmeWoJobSnList.get(0);
                BigDecimal qty = BigDecimal.valueOf(materialLot.getPrimaryUomQty()).add(BigDecimal.valueOf(hmeWoJobSn.getSiteInNum()));
                hmeWoJobSn.setSiteInNum(qty.longValue());
                hmeWoJobSnMapper.updateByPrimaryKeySelective(hmeWoJobSn);
                resVO.setWoJobSnId(hmeWoJobSn.getWoJobSnId());
            }
        } else {
            resVO.setEoJobSnId(hmeEoJobSnList.get(0).getJobId());
        }
        return resVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCosScanCodeVO2 materialLotSiteOut(Long tenantId, HmeCosScanCodeVO2 codeVO) {
        if (CollectionUtils.isNotEmpty(codeVO.getCodeList())) {
            String objectTransactionBatchId = Utils.getBatchId();
            // 创建请求事件
            String requestId = commonServiceComponent.generateEventRequest(tenantId, "HME_COS_COMPLETE");

            //调用{eventCreate}创建事件 条码更新、现存量增加
            MtEventCreateVO processEvent = new MtEventCreateVO();
            processEvent.setEventTypeCode("HME_EO_COMPLETE");
            processEvent.setEventRequestId(requestId);
            String processEventId = mtEventRepository.eventCreate(tenantId, processEvent);

            //报工
            List<WmsObjectTransactionResponseVO> reportList = new ArrayList<>();
            //完工
            List<WmsObjectTransactionResponseVO> workList = new ArrayList<>();

            for (HmeCosResVO resVO : codeVO.getCodeList()) {

                //更新eoJobSn
                HmeEoJobSn hmeEoJobSn = hmeEoJobSnRepository.selectByPrimaryKey(resVO.getEoJobSnId());
                CustomUserDetails userDetails = DetailsHelper.getUserDetails();
                Long userId = userDetails != null ? userDetails.getUserId() : -1L;
                hmeEoJobSn.setSiteOutDate(new Date());
                hmeEoJobSn.setSiteOutBy(userId);
                hmeEoJobSnMapper.updateByPrimaryKeySelective(hmeEoJobSn);

                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(resVO.getMaterialLotId());

                //校验在制品
                MtExtendVO mfFlagAttr = new MtExtendVO();
                mfFlagAttr.setAttrName("MF_FLAG");
                mfFlagAttr.setKeyId(resVO.getMaterialLotId());
                mfFlagAttr.setTableName("mt_material_lot_attr");
                List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mfFlagAttr);
                String mfFlag = "";
                if (CollectionUtils.isNotEmpty(attrVOList)) {
                    mfFlag = attrVOList.get(0).getAttrValue();
                }
                if (!StringUtils.equals(mfFlag, YES)) {
                    throw new MtException("HME_NC_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0017", "HME", mtMaterialLot.getMaterialLotCode()));
                }
                //工单工艺在制记录表
                HmeWoJobSn hmeWoJobSn = hmeWoJobSnRepository.selectByPrimaryKey(resVO.getWoJobSnId());
                BigDecimal qty = hmeWoJobSn.getProcessedNum() != null ? BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()).add(BigDecimal.valueOf(hmeWoJobSn.getProcessedNum())) : BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty());
                hmeWoJobSn.setProcessedNum(qty.longValue());
                hmeWoJobSnMapper.updateByPrimaryKeySelective(hmeWoJobSn);

                //获取工段下的货位
                List<String> locatorIdList = mapper.queryLocatorByWorkCell(tenantId, hmeEoJobSn.getWorkcellId());
                String locatorId = mtMaterialLot.getLocatorId();
                if (CollectionUtils.isNotEmpty(locatorIdList)) {
                    //货位不一致 更新条码的货位
                    if (!StringUtils.equals(mtMaterialLot.getLocatorId(), locatorIdList.get(0))) {
                        MtMaterialLotVO2 materialLotVO2 = new MtMaterialLotVO2();
                        materialLotVO2.setEventId(processEventId);
                        materialLotVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                        materialLotVO2.setLocatorId(locatorIdList.get(0));
                        mtMaterialLotRepository.materialLotUpdate(tenantId, materialLotVO2, "N");
                    }
                    locatorId = locatorIdList.get(0);
                }

                //更新条码扩展
                List<MtExtendVO5> mtLotExtendList = new ArrayList<>();
                MtExtendVO5 mfFlagExtend = new MtExtendVO5();
                mfFlagExtend.setAttrName("MF_FLAG");
                mfFlagExtend.setAttrValue(HmeConstants.ConstantValue.NO);
                mtLotExtendList.add(mfFlagExtend);

                MtExtendVO5 statusExtend = new MtExtendVO5();
                statusExtend.setAttrName("STATUS");
                statusExtend.setAttrValue(WmsConstant.MaterialLotStatus.INSTOCK);
                mtLotExtendList.add(statusExtend);

                mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, new MtExtendVO10() {
                    {
                        setKeyId(resVO.getMaterialLotId());
                        setEventId(processEventId);
                        setAttrs(mtLotExtendList);
                    }
                });
                //更新现有量
                //调用API{onhandQtyUpdateProcess}
                MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
                mtInvOnhandQuantityVO9.setSiteId(mtMaterialLot.getSiteId());
                mtInvOnhandQuantityVO9.setMaterialId(mtMaterialLot.getMaterialId());
                mtInvOnhandQuantityVO9.setLocatorId(locatorId);
                mtInvOnhandQuantityVO9.setLotCode(mtMaterialLot.getLot());
                mtInvOnhandQuantityVO9.setChangeQuantity(mtMaterialLot.getPrimaryUomQty());
                mtInvOnhandQuantityVO9.setEventId(processEventId);
                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);

                MtWorkOrderVO4 mtWorkOrderVO4 = new MtWorkOrderVO4();
                mtWorkOrderVO4.setWorkOrderId(hmeEoJobSn.getWorkOrderId());
                mtWorkOrderVO4.setTrxCompletedQty(mtMaterialLot.getPrimaryUomQty());
                mtWorkOrderVO4.setWorkcellId(hmeEoJobSn.getWorkcellId());
                mtWorkOrderVO4.setEventRequestId(requestId);
                mtWorkOrderRepository.woComplete(tenantId, mtWorkOrderVO4);

                //完工事务
                List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
                WmsObjectTransactionRequestVO woCompleteTransactionRequestVO = new WmsObjectTransactionRequestVO();
                woCompleteTransactionRequestVO.setTransactionTypeCode("HME_WO_COMPLETE");
                woCompleteTransactionRequestVO.setEventId(processEventId);
                woCompleteTransactionRequestVO.setMaterialLotId(resVO.getMaterialLotId());
                woCompleteTransactionRequestVO.setMaterialId(resVO.getMaterialId());
                woCompleteTransactionRequestVO.setTransactionQty(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()));
                woCompleteTransactionRequestVO.setLotNumber(mtMaterialLot.getLot());
                MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterialLot.getPrimaryUomId());
                if (Objects.nonNull(mtUom)) {
                    woCompleteTransactionRequestVO.setTransactionUom(mtUom.getUomCode());
                }
                woCompleteTransactionRequestVO.setTransactionTime(new Date());
                woCompleteTransactionRequestVO.setPlantId(mtMaterialLot.getSiteId());

                MtModLocator mtModLocator1 = hmeEoJobSnLotMaterialMapper.queryAreaLocator(tenantId, mtMaterialLot.getLocatorId());
                if (Objects.nonNull(mtModLocator1)) {
                    woCompleteTransactionRequestVO.setWarehouseId(mtModLocator1.getLocatorId());
                }
                woCompleteTransactionRequestVO.setLocatorId(locatorId);
                MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(resVO.getWorkOrderId());
                if (mtWorkOrder != null) {
                    woCompleteTransactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
                    MtModProductionLine mtModProductionLine = mtModProductionLineRepository
                            .prodLineBasicPropertyGet(tenantId, mtWorkOrder.getProductionLineId());
                    if (Objects.nonNull(mtModProductionLine)) {
                        woCompleteTransactionRequestVO.setProdLineCode(mtModProductionLine.getProdLineCode());
                    }
                }
                //工序号
                List<String> operationSequenceList = mapper.queryOperationSequence(tenantId, hmeWoJobSn.getWorkOrderId(), hmeWoJobSn.getOperationId());
                if (CollectionUtils.isNotEmpty(operationSequenceList)) {
                    woCompleteTransactionRequestVO.setOperationSequence(operationSequenceList.get(0));
                }

                //批次
                woCompleteTransactionRequestVO.setLotNumber(mtMaterialLot.getLot());

                //报工事务
                WmsObjectTransactionRequestVO woReportTransactionRequestVO = new WmsObjectTransactionRequestVO();
                BeanUtils.copyProperties(woCompleteTransactionRequestVO, woReportTransactionRequestVO);
                woReportTransactionRequestVO.setTransactionTypeCode("HME_WORK_REPORT");

                //查询移动类型
                List<WmsTransactionType> transactionTypes = transactionTypeRepository.selectByCondition(Condition.builder(WmsTransactionType.class)
                        .andWhere(Sqls.custom().andEqualTo(WmsTransactionType.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(WmsTransactionType.FIELD_TRANSACTION_TYPE_CODE, woCompleteTransactionRequestVO.getTransactionTypeCode())
                        ).build());
                if (CollectionUtils.isNotEmpty(transactionTypes)) {
                    woCompleteTransactionRequestVO.setMoveType(transactionTypes.get(0).getMoveType());
                }

                //完工
                woCompleteTransactionRequestVO.setContainerId(mtMaterialLot.getCurrentContainerId());
                woCompleteTransactionRequestVO.setAttribute16(objectTransactionBatchId);
                objectTransactionRequestList.add(woCompleteTransactionRequestVO);
                List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS1 = wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
                workList.addAll(wmsObjectTransactionResponseVOS1);
                //报工
                List<WmsObjectTransactionRequestVO> objectTransactionRequestList1 = new ArrayList<>();
                woReportTransactionRequestVO.setAttribute16(objectTransactionBatchId);
                objectTransactionRequestList1.add(woReportTransactionRequestVO);
                List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS = wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList1);
                reportList.addAll(wmsObjectTransactionResponseVOS);
            }

            //报工实时接口，完工实时接口
            itfObjectTransactionIfaceService.sendSapProdMaterialMove(tenantId,reportList,workList);

        }
        return codeVO;
    }

    @Override
    public List<HmeCosResVO> querySiteInMaterialLot(Long tenantId, HmeCosScanCodeVO codeVO) {
        List<HmeCosResVO> resVOList = new ArrayList<>();
        List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_WORKCELL_ID, codeVO.getWorkcellId())
                        .andEqualTo(HmeEoJobSn.FIELD_OPERATION_ID, codeVO.getOperationId())
                        .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE, HmeConstants.JobType.COS_COMPLETED)
                        .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE)).build());

        for (HmeEoJobSn hmeEoJobSn : hmeEoJobSnList) {
            HmeCosResVO hmeCosResVO = new HmeCosResVO();
            hmeCosResVO.setEoJobSnId(hmeEoJobSn.getJobId());
            hmeCosResVO.setWorkOrderId(hmeEoJobSn.getWorkOrderId());

            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeEoJobSn.getMaterialLotId());
            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtMaterialLot.getMaterialId());
            if (mtMaterial != null) {
                hmeCosResVO.setMaterialName(mtMaterial.getMaterialName());
                hmeCosResVO.setMaterialCode(mtMaterial.getMaterialCode());
            }
            hmeCosResVO.setMaterialId(mtMaterialLot.getMaterialId());
            hmeCosResVO.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            hmeCosResVO.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
            hmeCosResVO.setQty(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()));


            List<HmeWoJobSn> hmeWoJobSnList = hmeWoJobSnRepository.selectByCondition(Condition.builder(HmeWoJobSn.class)
                    .andWhere(Sqls.custom().andEqualTo(HmeWoJobSn.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeWoJobSn.FIELD_WORK_ORDER_ID, hmeEoJobSn.getWorkOrderId())
                            .andEqualTo(HmeWoJobSn.FIELD_OPERATION_ID, codeVO.getOperationId())).build());

            if (CollectionUtils.isNotEmpty(hmeEoJobSnList)) {
                hmeCosResVO.setWoJobSnId(hmeWoJobSnList.get(0).getWoJobSnId());
            }
            resVOList.add(hmeCosResVO);
        }
        return resVOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeCosResVO> batchCancelSiteIn(Long tenantId, List<HmeCosResVO> cosResVOList) {
        if (CollectionUtils.isNotEmpty(cosResVOList)) {
            //取消进站
            for (HmeCosResVO hmeCosResVO : cosResVOList) {
                HmeEoJobSn hmeEoJobSn = hmeEoJobSnRepository.selectByPrimaryKey(hmeCosResVO.getEoJobSnId());
                if (hmeEoJobSn != null) {
                    List<HmeWoJobSn> hmeWoJobSnList = hmeWoJobSnRepository.selectByCondition(Condition.builder(HmeWoJobSn.class)
                            .andWhere(Sqls.custom().andEqualTo(HmeWoJobSn.FIELD_TENANT_ID, tenantId)
                                    .andEqualTo(HmeWoJobSn.FIELD_WORK_ORDER_ID, hmeEoJobSn.getWorkOrderId())
                                    .andEqualTo(HmeWoJobSn.FIELD_OPERATION_ID, hmeEoJobSn.getOperationId())).build());
                    if (CollectionUtils.isNotEmpty(hmeWoJobSnList)) {
                        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeCosResVO.getMaterialLotId());
                        HmeWoJobSn hmeWoJobSn = hmeWoJobSnList.get(0);
                        BigDecimal siteInNum = BigDecimal.valueOf(hmeWoJobSn.getSiteInNum()).subtract(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()));
                        hmeWoJobSn.setSiteInNum(siteInNum.longValue());
                        hmeWoJobSnMapper.updateByPrimaryKeySelective(hmeWoJobSn);
                    }
                    //清空作业信息
                    hmeEoJobSnRepository.deleteByPrimaryKey(hmeEoJobSn);
                }
            }
        }
        return cosResVOList;
    }

    @Override
    public List<MtModLocator> queryLocatorListPermission(Long tenantId) {
        Long userId = DetailsHelper.getUserDetails().getUserId();
        MtUserOrganization mtUserOrganization = new MtUserOrganization();
        mtUserOrganization.setUserId(userId);
        mtUserOrganization.setOrganizationType("SITE");
        List<MtUserOrganization> mtUserOrganizationList = mtUserOrganizationRepository.userOrganizationPermissionQuery(tenantId, mtUserOrganization);
        List<String> organizationIdList = mtUserOrganizationList.stream().map(MtUserOrganization::getOrganizationId).collect(toList());
        if (CollectionUtils.isEmpty(organizationIdList)) {
            return Collections.emptyList();
        }
        return mapper.queryLocatorListPermission(tenantId, userId,organizationIdList);
    }

    private void verifyMaterialLotCode(Long tenantId, MtMaterialLot materialLot) {
        //调用materialLotLimitAttrQuery-获取物料批扩展属性
        MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
        mtMaterialLotAttrVO2.setMaterialLotId(materialLot.getMaterialLotId());
        //在制品
        mtMaterialLotAttrVO2.setAttrName("MF_FLAG");
        String mfFlag = "";
        List<MtExtendAttrVO> flagList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(flagList)) {
            mfFlag = flagList.get(0).getAttrValue();
        }
        if (!StringUtils.equals(mfFlag, YES)) {
            throw new MtException("HME_NC_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0017", "HME", materialLot.getMaterialLotCode()));
        }

        //不良芯片
        Integer ncCount = mapper.queryMaterialLotNcCount(tenantId, materialLot.getMaterialLotId());
        if (ncCount > 0) {
            throw new MtException("HME_NC_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0019", "HME", materialLot.getMaterialLotCode()));
        }
        //贴片
        List<HmeMaterialLotLoad> materialLotLoadList = mapper.queryMaterialLotLoadList(tenantId, materialLot.getMaterialLotId());
        if (CollectionUtils.isNotEmpty(materialLotLoadList)) {
            throw new MtException("HME_NC_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0020", "HME", materialLot.getMaterialLotCode()));
        }
        //未出站
        List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, materialLot.getMaterialLotId())
                        .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE)).build());
        if (CollectionUtils.isNotEmpty(hmeEoJobSnList)) {
            HmeEoJobSn hmeEoJobSn = hmeEoJobSnList.get(0);
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(hmeEoJobSn.getWorkcellId());
            List<LovValueDTO> valueDTOList = lovAdapter.queryLovValue("HME.JOB_TYPE", tenantId);
            Optional<LovValueDTO> jobTypeOpt = valueDTOList.stream().filter(dto -> StringUtils.equals(dto.getValue(), hmeEoJobSn.getJobType())).findFirst();
            String jobType = jobTypeOpt.isPresent() ? jobTypeOpt.get().getMeaning() : "";
            throw new MtException("HME_CHIP_TRANSFER_017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_017", "HME", mtModWorkcell != null ? mtModWorkcell.getWorkcellCode() : "", jobType));
        }
    }
}
