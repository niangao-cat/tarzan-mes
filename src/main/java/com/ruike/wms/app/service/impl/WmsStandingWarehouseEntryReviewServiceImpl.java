package com.ruike.wms.app.service.impl;

import com.jcraft.jsch.Logger;
import com.netflix.discovery.converters.Auto;
import com.ruike.hme.app.upload.importer.HmeCosChipNumImportServiceImpl;
import com.ruike.hme.domain.vo.HmeCosChipNumImportVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.itf.api.dto.ItfMaterialLotConfirmIfaceDTO;
import com.ruike.itf.app.service.ItfMaterialLotConfirmIfaceService;
import com.ruike.itf.domain.vo.ItfMaterialLotConfirmIfaceVO4;
import com.ruike.wms.api.dto.WmsStandingWarehouseEntryReviewDTO;
import com.ruike.wms.api.dto.WmsStandingWarehouseEntryReviewDTO2;
import com.ruike.wms.app.service.WmsStandingWarehouseEntryReviewService;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.util.StringCommonUtils;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtExtendVO5;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtContainerLoadDetail;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtContLoadDtlVO10;
import tarzan.inventory.domain.vo.MtContLoadDtlVO4;
import tarzan.inventory.domain.vo.MtMaterialLotAttrVO2;
import tarzan.inventory.domain.vo.MtMaterialLotAttrVO3;
import tarzan.inventory.infra.mapper.MtMaterialLotMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.ruike.wms.infra.constant.WmsConstant.ConstantValue.WMS;
import static com.ruike.wms.infra.constant.WmsConstant.InstructionStatus.EXECUTE;
import static com.ruike.wms.infra.constant.WmsConstant.InstructionStatus.RELEASED;

@Service
@Slf4j
public class WmsStandingWarehouseEntryReviewServiceImpl implements WmsStandingWarehouseEntryReviewService {
    @Autowired
    MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtContainerRepository mtContainerRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;
    @Autowired
    private MtMaterialLotMapper mtMaterialLotMapper;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private HmeCosChipNumImportServiceImpl hmeCosChipNumImportService;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private ItfMaterialLotConfirmIfaceService itfMaterialLotConfirmIfaceService;

    @Override
    public WmsStandingWarehouseEntryReviewDTO queryBarcode(Long tenantId, WmsStandingWarehouseEntryReviewDTO dto) {
        if (StringUtils.isBlank(dto.getContainerCode())) {
            throw new MtException("WX_WMS_ASRS_CHECK_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX_WMS_ASRS_CHECK_001", "WMS"));
        }
        MtContainer mtContainer = new MtContainer();
        mtContainer.setTenantId(tenantId);
        mtContainer.setContainerCode(dto.getContainerCode());
        MtContainer mtContainer1 = mtContainerRepository.selectOne(mtContainer);
        if (mtContainer1 == null) {
            throw new MtException("WX_WMS_ASRS_CHECK_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX_WMS_ASRS_CHECK_001", "WMS", dto.getContainerCode()));
        }
        if (StringUtils.isNotEmpty(mtContainer1.getTopContainerId())) {
            throw new MtException("WX_WMS_ASRS_CHECK_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX_WMS_ASRS_CHECK_002", "WMS", dto.getContainerCode()));
        }
        //校验容器下是否含有物料批条码
        List<MtContainerLoadDetail> mtContainerLoadDetails = mtContainerLoadDetailRepository.containerLimitMaterialLotAndContainer(tenantId,mtContainer1.getContainerId());
        List<String> loadObjectTypes = mtContainerLoadDetails.stream().map(MtContainerLoadDetail::getLoadObjectType).collect(Collectors.toList());
        if(!loadObjectTypes.contains("MATERIAL_LOT")){
            throw new MtException("WX_WMS_ASRS_CHECK_012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX_WMS_ASRS_CHECK_012", "WMS", dto.getContainerCode()));
        }
        WmsStandingWarehouseEntryReviewDTO returnDto = new WmsStandingWarehouseEntryReviewDTO();

        MtModLocator mtModLocator1 = mtModLocatorRepository.selectByPrimaryKey(mtContainer1.getLocatorId());
        MtModLocator mtModLocator11 = mtModLocatorRepository.selectByPrimaryKey(mtModLocator1.getParentLocatorId());

        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("WX.WMS.ASRS_LOCATOR", tenantId);
        List<String> list = lovValueDTOS.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(lovValueDTOS)) {
            //验证容器货位
            if (mtModLocator11 != null) {
                if (!list.contains(mtModLocator11.getLocatorCode())) {
                    throw new MtException("WX_WMS_ASRS_CHECK_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WX_WMS_ASRS_CHECK_008", "WMS", dto.getContainerCode()));
                }
            } else {
                throw new MtException("WX_WMS_ASRS_CHECK_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WX_WMS_ASRS_CHECK_008", "WMS", dto.getContainerCode()));
            }
        } else {
            throw new MtException("WX_WMS_ASRS_CHECK_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX_WMS_ASRS_CHECK_008", "WMS", dto.getContainerCode()));
        }
        MtModLocator mtModLocator3 = mtModLocatorRepository.selectByPrimaryKey(mtContainer1.getLocatorId());
        returnDto.setLocatorCode(mtModLocator3.getLocatorCode());
        returnDto.setLocatorName(mtModLocator3.getLocatorName());

        List<WmsStandingWarehouseEntryReviewDTO2> resultList = new ArrayList<>();
        //调用API查询物料批【containerLimitMaterialLotQuery】
        MtContLoadDtlVO10 mtContLoadDtlVO10 = new MtContLoadDtlVO10();
        mtContLoadDtlVO10.setContainerId(mtContainer1.getContainerId());
        mtContLoadDtlVO10.setAllLevelFlag(HmeConstants.ConstantValue.YES);
        List<MtContLoadDtlVO4> mtContLoadDtlVO4s = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, mtContLoadDtlVO10);
        MtContainer mtContainer2 = mtContainerRepository.selectByPrimaryKey(mtContainer1.getContainerId());
        if (CollectionUtils.isNotEmpty(mtContLoadDtlVO4s)) {
            String error = "";
            for (MtContLoadDtlVO4 getMaterialLot : mtContLoadDtlVO4s) {
                // mtContLoadDtlVO4s.stream().forEach(getMaterialLot -> {
                WmsStandingWarehouseEntryReviewDTO2 result = new WmsStandingWarehouseEntryReviewDTO2();
                List<MtExtendAttrVO> mtExtendAttrVOS = new ArrayList<>();
                //物料版本
                MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
                mtMaterialLotAttrVO2.setMaterialLotId(getMaterialLot.getMaterialLotId());
                mtMaterialLotAttrVO2.setAttrName(HmeConstants.ExtendAttr.MATERIAL_VERSION);
                mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
                //String attrValue2 = mtMaterialLotMapper.getAttrValue(tenantId, getMaterialLot.getMaterialLotId(), HmeConstants.ExtendAttr.MATERIAL_VERSION);
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS)) {
                    result.setMaterialVersion(mtExtendAttrVOS.get(0).getAttrValue());
                } else {
                    result.setMaterialVersion(null);
                }

                //调用API[materialLotPropertyBatchGet]
                List<MtMaterialLot> materialLots = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, Collections.singletonList(getMaterialLot.getMaterialLotId()));
                if (CollectionUtils.isNotEmpty(materialLots)) {
                    //校验物料批库位
                    MtModLocator mtModLocator19 = mtModLocatorRepository.selectByPrimaryKey(materialLots.get(0).getLocatorId());
                    MtModLocator mtModLocator99 = mtModLocatorRepository.selectByPrimaryKey(mtModLocator19.getParentLocatorId());
                    if (mtModLocator99!=null) {
                        if (!list.contains(mtModLocator99.getLocatorCode())) {
                            error += materialLots.get(0).getMaterialLotCode() + "-";
                        }
                    }

                    result.setMaterialLotCode(materialLots.get(0).getMaterialLotCode());
                    result.setMaterialLotId(materialLots.get(0).getMaterialLotId());
                    //result.setMaterialContainerCode(materialLots.get(0).getCurrentContainerId());

                    MtContainer containerCode = new MtContainer();
                    containerCode.setTenantId(tenantId);
                    if (StringUtils.isBlank(materialLots.get(0).getCurrentContainerId())) {
                        throw new MtException("WX_WMS_ASRS_CHECK_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WX_WMS_ASRS_CHECK_011", "WMS", materialLots.get(0).getMaterialLotCode()));
                    } else {
                        containerCode.setContainerId(materialLots.get(0).getCurrentContainerId());
                        containerCode = mtContainerRepository.selectOne(containerCode);
                        result.setMaterialContainerCode(containerCode.getContainerCode());
                    }

                    //物料信息
                    MtMaterial mtMaterial = new MtMaterial();
                    mtMaterial.setTenantId(tenantId);
                    mtMaterial.setMaterialId(materialLots.get(0).getMaterialId());
                    MtMaterial mtMaterial1 = mtMaterialRepository.selectByPrimaryKey(mtMaterial);
                    if (mtMaterial1 != null) {
                        result.setMaterialCode(mtMaterial1.getMaterialCode());
                        result.setMaterialName(mtMaterial1.getMaterialName());
                    }
                    result.setMaterialQty(new BigDecimal(materialLots.get(0).getPrimaryUomQty()));

                    if (!StringUtils.equals(materialLots.get(0).getQualityStatus(), HmeConstants.ConstantValue.OK)) {
                        throw new MtException("WX_WMS_ASRS_CHECK_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WX_WMS_ASRS_CHECK_003", "WMS", result.getMaterialLotCode()));
                    }
                    if (StringUtils.equals(materialLots.get(0).getFreezeFlag(), HmeConstants.ConstantValue.YES)) {
                        throw new MtException("WX_WMS_ASRS_CHECK_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WX_WMS_ASRS_CHECK_004", "WMS", result.getMaterialLotCode()));
                    }
                    if (StringUtils.equals(materialLots.get(0).getStocktakeFlag(), HmeConstants.ConstantValue.YES)) {
                        throw new MtException("WX_WMS_ASRS_CHECK_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WX_WMS_ASRS_CHECK_005", "WMS", result.getMaterialLotCode()));
                    }
                    if (StringUtils.equals(materialLots.get(0).getEnableFlag(), HmeConstants.ConstantValue.NO)) {
                        throw new MtException("WX_WMS_ASRS_CHECK_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WX_WMS_ASRS_CHECK_006", "WMS", result.getMaterialLotCode()));
                    }
                    result.setQualityStatus(materialLots.get(0).getQualityStatus());
                    result.setFreezeFlag(materialLots.get(0).getFreezeFlag());
                    result.setStocktakeFlag(materialLots.get(0).getStocktakeFlag());

                    MtModLocator mtModLocator12 = mtModLocatorRepository.selectByPrimaryKey(materialLots.get(0).getLocatorId());
                    MtModLocator mtModLocator22 = mtModLocatorRepository.selectByPrimaryKey(mtModLocator12.getParentLocatorId());

                    if (mtModLocator22 != null) {
                        if (!list.contains(mtModLocator22.getLocatorCode())) {
                            throw new MtException("WX_WMS_ASRS_CHECK_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WX_WMS_ASRS_CHECK_008", "WMS", dto.getContainerCode()));
                        }

                        result.setLocatorCode(mtModLocator12.getLocatorCode());
                        result.setLocatorName(mtModLocator12.getLocatorName());
                    }
                }else {
                    throw new MtException("WX_WMS_ASRS_CHECK_012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WX_WMS_ASRS_CHECK_012", "WMS", mtContainer2.getContainerCode()));
                }

                //复核标识
                mtMaterialLotAttrVO2.setAttrName(HmeConstants.ExtendAttr.STATUS);
                mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
                //String attrValue = mtMaterialLotMapper.getAttrValue(tenantId, getMaterialLot.getMaterialLotId(), HmeConstants.ExtendAttr.STATUS);
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS)) {
                    if (StringUtils.equals(mtExtendAttrVOS.get(0).getAttrValue(), "INSTOCK") || StringUtils.equals(mtExtendAttrVOS.get(0).getAttrValue(), "CHECKED")) {
                        String meaning = lovAdapter.queryLovMeaning("WMS.MTLOT.STATUS", tenantId, mtExtendAttrVOS.get(0).getAttrValue());
                        result.setCheckConfirm(mtExtendAttrVOS.get(0).getAttrValue());
                        if (StringUtils.isNotBlank(meaning)) {
                            result.setCheckConfirmFlag(meaning);
                        }
                    } else {
                        throw new MtException("WX_WMS_ASRS_CHECK_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WX_WMS_ASRS_CHECK_009", "WMS", result.getMaterialLotCode()));
                    }
                }
                resultList.add(result);
            }
            //校验物料批的库位
            if (StringUtils.isNotBlank(error)) {
                throw new MtException("WMS_ASRS_CHECK_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_ASRS_CHECK_010", "WMS", error));
            }
            //});
        }
        //调用API【materialLotPropertyBatchGet】之后再次进行校验

        returnDto.setList(resultList);
        return returnDto;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ItfMaterialLotConfirmIfaceVO4> confirm(Long tenantId, WmsStandingWarehouseEntryReviewDTO dto) {
        if (dto != null) {
            if (CollectionUtils.isNotEmpty(dto.getList())) {
                MtContainer mtContainer = new MtContainer();
                mtContainer.setTenantId(tenantId);
                mtContainer.setContainerCode(dto.getContainerCode());
                List<MtContainer> mtContainers = mtContainerRepository.select(mtContainer);
                if(CollectionUtils.isNotEmpty(mtContainers)){
                    MtModLocator mtModLocator = mtModLocatorRepository.locatorBasicPropertyGet(tenantId,mtContainers.get(0).getLocatorId());
                    if("AUTO".equals(mtModLocator.getLocatorType())){
                        throw new MtException("Exception","当前容器已在立库货位，不可再进行入库复核！");
                    }
                }

                //生成事件
                MtEventCreateVO event = new MtEventCreateVO();
                event.setEventTypeCode("CHECK_MATERIAL_LOT_ATTR_STATUS");
                String eventId = mtEventRepository.eventCreate(tenantId, event);

                dto.getList().stream().forEach(a -> {
                    List<MtExtendVO5> attrList = new ArrayList<>();

                    MtExtendVO5 waferNumAttr = new MtExtendVO5();
                    waferNumAttr.setAttrName(HmeConstants.ExtendAttr.STATUS);

                    MtExtendVO5 waferNumAttr2 = new MtExtendVO5();
                    waferNumAttr2.setAttrName(HmeConstants.ExtendAttr.OLD_STATUS);
                    //复核确认
                    if (StringUtils.equals(dto.getCheckConfirm(), HmeConstants.ConstantValue.YES)) {
                        waferNumAttr.setAttrValue("CHECKED");
                        waferNumAttr2.setAttrValue("INSTOCK");
                    }
                    //复核取消
                    if (StringUtils.equals(dto.getCheckCancel(), HmeConstants.ConstantValue.YES)) {
                        waferNumAttr.setAttrValue("INSTOCK");
                        waferNumAttr2.setAttrValue("CHECKED");
                    }
                    attrList.add(waferNumAttr);
                    attrList.add(waferNumAttr2);
                    //调用API{ materialLotLimitAttrUpdate }进行扩展属性记录
                    MtMaterialLotAttrVO3 mtMaterialLotAttrVO3 = new MtMaterialLotAttrVO3();
                    mtMaterialLotAttrVO3.setEventId(eventId);
                    mtMaterialLotAttrVO3.setMaterialLotId(a.getMaterialLotId());
                    mtMaterialLotAttrVO3.setAttr(attrList);
                    mtMaterialLotRepository.materialLotLimitAttrUpdate(tenantId, mtMaterialLotAttrVO3);
                });

                //调用API【itfMaterialLotConfirmOrChangeIface】
                ItfMaterialLotConfirmIfaceDTO itfMaterialLotConfirmIfaceDTO = new ItfMaterialLotConfirmIfaceDTO();
                itfMaterialLotConfirmIfaceDTO.setBarcodeList(Collections.singletonList(dto.getContainerCode()));
                List<ItfMaterialLotConfirmIfaceVO4> itfMaterialLotConfirmIfaceVO4s = itfMaterialLotConfirmIfaceService.itfMaterialLotConfirmOrChangeIface(tenantId, itfMaterialLotConfirmIfaceDTO);
                List<ItfMaterialLotConfirmIfaceVO4> itfMaterialLotConfirmIfaceVO4s1 = itfMaterialLotConfirmIfaceVO4s.stream().filter(line -> line.getSuccess()!=true).collect(Collectors.toList());
                itfMaterialLotConfirmIfaceVO4s = itfMaterialLotConfirmIfaceVO4s.stream().filter(line -> line.getSuccess()==true).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(itfMaterialLotConfirmIfaceVO4s)) {
                    return itfMaterialLotConfirmIfaceVO4s;
                } else {
                    if(CollectionUtils.isNotEmpty(itfMaterialLotConfirmIfaceVO4s1)){
                        throw new MtException("MtException",itfMaterialLotConfirmIfaceVO4s1.get(0).getErrorMessage());
                    }
                    //生成事件
                    MtEventCreateVO eventFlase = new MtEventCreateVO();
                    eventFlase.setEventTypeCode("CHECK_MATERIAL_LOT_ATTR_STATUS");
                    String eventFlaseId = mtEventRepository.eventCreate(tenantId, eventFlase);


                    dto.getList().stream().forEach(a -> {
                        List<MtExtendVO5> attrList = new ArrayList<>();
                        MtExtendVO5 waferNumAttr2 = new MtExtendVO5();
                        waferNumAttr2.setAttrName(HmeConstants.ExtendAttr.OLD_STATUS);
                        //复核确认
                        if (StringUtils.equals(dto.getCheckConfirm(), HmeConstants.ConstantValue.YES)) {
                            waferNumAttr2.setAttrValue("CHECKED");
                        }
                        //复核取消
                        if (StringUtils.equals(dto.getCheckCancel(), HmeConstants.ConstantValue.YES)) {
                            waferNumAttr2.setAttrValue("INSTOCK");
                        }
                        attrList.add(waferNumAttr2);
                        //调用API{ materialLotLimitAttrUpdate }进行扩展属性记录
                        MtMaterialLotAttrVO3 mtMaterialLotAttrVO3 = new MtMaterialLotAttrVO3();
                        mtMaterialLotAttrVO3.setEventId(eventFlaseId);
                        mtMaterialLotAttrVO3.setMaterialLotId(a.getMaterialLotId());
                        mtMaterialLotAttrVO3.setAttr(attrList);
                        mtMaterialLotRepository.materialLotLimitAttrUpdate(tenantId, mtMaterialLotAttrVO3);
                    });
                    return itfMaterialLotConfirmIfaceVO4s;
                }
            }
        }
        return null;
    }
}
