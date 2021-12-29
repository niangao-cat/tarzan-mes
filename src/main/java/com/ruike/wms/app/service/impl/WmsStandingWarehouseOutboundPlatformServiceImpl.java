package com.ruike.wms.app.service.impl;

import com.ruike.itf.api.dto.ItfFinishDeliveryInstructionIfaceDTO;
import com.ruike.itf.domain.entity.WcsTaskIface;
import com.ruike.itf.domain.repository.ItfFinishDeliveryInstructionIfaceRepository;
import com.ruike.itf.domain.repository.WcsTaskIfaceRepository;
import com.ruike.itf.domain.vo.ItfFinishDeliveryInstructionIfaceVO;
import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsStandingWarehouseOutboundPlatformService;
import com.ruike.wms.domain.entity.WmsInstructionSnRel;
import com.ruike.wms.domain.repository.WmsInstructionSnRelRepository;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsInstructionSnRelMapper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import io.choerodon.core.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.inventory.domain.entity.MtInvOnhandQuantity;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class WmsStandingWarehouseOutboundPlatformServiceImpl implements WmsStandingWarehouseOutboundPlatformService {
    @Autowired
    MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private WmsInstructionSnRelMapper wmsInstructionSnRelMapper;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;
    @Autowired
    private MtInstructionRepository mtInstructionRepository;
    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private WmsInstructionSnRelRepository wmsInstructionSnRelRepository;
    @Autowired
    private WcsTaskIfaceRepository wcsTaskIfaceRepository;
    @Autowired
    private ItfFinishDeliveryInstructionIfaceRepository itfFinishDeliveryInstructionIfaceRepository;


    @Override
    @ProcessLovValue
    public WmsStandingWarehouseOutboundPlatformHeadDTO queryHead(Long tenantId, String instructionDocNum) {
        //单据限制
        WmsStandingWarehouseOutboundPlatformHeadDTO returnDto = new WmsStandingWarehouseOutboundPlatformHeadDTO();
        if (StringUtils.isBlank(instructionDocNum)) {
            throw new MtException("WX_WMS_ASRS_OUT_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX_WMS_ASRS_OUT_0014", "WMS"));
        }
        //查询头
        WmsStandingWarehouseOutboundPlatformHeadDTO instructionDocNum1 = wmsInstructionSnRelMapper.getInstructionDocNum(tenantId, instructionDocNum);
        List<LovValueDTO> typeLovValue = lovAdapter.queryLovValue("WX.WMS.WCS_TASK_DOC_TYPE_LIMIT", tenantId);
        List<LovValueDTO> statusLovValue = lovAdapter.queryLovValue("WX.WMS.WCS_TASK_DOC_STATUS_LIMIT", tenantId);
        List<String> lovValues = new ArrayList<>();
        if (instructionDocNum1 != null) {
            if (CollectionUtils.isNotEmpty(typeLovValue)) {
                typeLovValue.stream().forEach(type -> {
                    lovValues.add(type.getValue());
                });
            }
            //类型限制
            if (!lovValues.contains(instructionDocNum1.getInstructionDocType())) {
                throw new MtException("WX_WMS_ASRS_OUT_00015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WX_WMS_ASRS_OUT_00015", "WMS", instructionDocNum));
            }
            lovValues.clear();
            if (CollectionUtils.isNotEmpty(statusLovValue)) {
                statusLovValue.stream().forEach(status -> {
                    lovValues.add(status.getValue());
                });
            }
            //状态限制
            if (!lovValues.contains(instructionDocNum1.getInstructionDocStatus())) {
                throw new MtException("WX_WMS_ASRS_OUT_00016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WX_WMS_ASRS_OUT_00016", "WMS", instructionDocNum));
            }
        } else {
            throw new MtException("WX_WMS_ASRS_OUT_00017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX_WMS_ASRS_OUT_00017", "WMS", instructionDocNum));
        }
        BeanUtils.copyProperties(instructionDocNum1, returnDto);
        return returnDto;
    }

    @Override
    @ProcessLovValue
    public List<WmsStandingWarehouseOutboundPlatformDTO> queryList(Long tenantId, String instructionDocId) {
        List<WmsStandingWarehouseOutboundPlatformDTO> returnList = new ArrayList<>();

        List<WmsStandingWarehouseOutboundPlatformDTO> instructionDocId1 = wmsInstructionSnRelMapper.getInstructionDocId(tenantId, instructionDocId);
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("WX.WMS.EXIT_NUM", tenantId);

        if (CollectionUtils.isNotEmpty(instructionDocId1)) {
            instructionDocId1.stream().forEach(instruction -> {
                WmsStandingWarehouseOutboundPlatformDTO returnDto = new WmsStandingWarehouseOutboundPlatformDTO();
                BeanUtils.copyProperties(instruction, returnDto);
                //立库现有量为E
                if (StringUtils.equals(instruction.getSpecStockFlag(), WmsConstant.KEY_IFACE_STATUS_ERROR)) {
                    List<MtMaterialLot> specStockFlag = wmsInstructionSnRelMapper.getSpecStockFlag(instruction.getFromLocatorId(), instruction.getInstructionId(), instruction.getSoNum(), instruction.getSoLineNum());
                    double doublesum = specStockFlag.stream().mapToDouble(MtMaterialLot::getPrimaryUomQty).sum();
                    returnDto.setStangingQuantity(new Double(doublesum).longValue());
                } else if (!StringUtils.equals(instruction.getSpecStockFlag(), WmsConstant.KEY_IFACE_STATUS_ERROR)) {
                    //propertyLimitDetailOnhandQtyPropertyQuery  propertyLimitDetailOnhandQtyQuery
//                    MtModLocator mtModLocator = new MtModLocator();
//                    mtModLocator.setTenantId(tenantId);
//                    mtModLocator.setParentLocatorId(instruction.getFromLocatorId());
//                    mtModLocator.setLocatorType("AUTO");
//                    List<MtModLocator> select = mtModLocatorRepository.select(mtModLocator);
//                    if (CollectionUtils.isNotEmpty(select)) {
//                        MtInvOnhandQuantityVO mtInvOnhandQuantityVO = new MtInvOnhandQuantityVO();
//                        mtInvOnhandQuantityVO.setMaterialId(instruction.getMaterialId());
//                        mtInvOnhandQuantityVO.setLocatorId(select.get(0).getLocatorId());
//                        List<MtInvOnhandQuantity> mtInvOnhandQuantities = mtInvOnhandQuantityRepository.propertyLimitDetailOnhandQtyQuery(tenantId, mtInvOnhandQuantityVO);
//                        double doublesum = mtInvOnhandQuantities.stream().mapToDouble(MtInvOnhandQuantity::getOnhandQuantity).sum();
//
//                        returnDto.setStangingQuantity(new Double(doublesum).longValue());
//                    }
                    List<MtMaterialLot> specStockFlag1 = wmsInstructionSnRelMapper.getSpecStockFlagNot(instruction.getFromLocatorId(), instruction.getInstructionId());
                    double doublesum1 = specStockFlag1.stream().mapToDouble(MtMaterialLot::getPrimaryUomQty).sum();
                    returnDto.setStangingQuantity(new Double(doublesum1).longValue());
                } else {
                    returnDto.setStangingQuantity(0L);
                }
                //出口
                returnDto.setExitNum(lovValueDTOS.get(0).getValue());
                returnList.add(returnDto);
            });
        }
        return returnList;
    }

    @Override
    @ProcessLovValue
    public List<WmsStandingWarehouseOutboundPlatformLineDTO> snSpecified(Long tenantId, WmsStandingWarehouseOutboundPlatformDTO dto) {
        List<WmsStandingWarehouseOutboundPlatformLineDTO> sn = new ArrayList<>();
        if (StringUtils.isNotEmpty(dto.getSn())) {
            sn = wmsInstructionSnRelMapper.getNewSn(tenantId, dto.getSn());
            if (CollectionUtils.isNotEmpty(sn)) {
                return sn;
            }
        } else {
            sn = wmsInstructionSnRelMapper.getSn(tenantId, dto.getInstructionId(), dto.getMaterialLotCodesList());
            if (CollectionUtils.isNotEmpty(sn)) {
                return sn;
            }
        }
        return sn;
    }

    @Override
    @ProcessLovValue
    public String snEntry(Long tenantId, String instructionId, String sn) {
        String flag = "";
        MtInstruction mtInstruction = new MtInstruction();
        mtInstruction.setTenantId(tenantId);
        mtInstruction.setInstructionId(instructionId);
        MtInstruction mtInstruction1 = mtInstructionRepository.selectOne(mtInstruction);

        List<WmsStandingWarehouseOutboundPlatformLineDTO> newSn = new ArrayList<>();
        List<WmsStandingWarehouseOutboundPlatformLineDTO> sn1 = new ArrayList<>();
        if (StringUtils.isNotEmpty(sn)) {
            newSn = wmsInstructionSnRelMapper.getNewSn(tenantId, sn);
        } else {
            sn1 = wmsInstructionSnRelMapper.getSn(tenantId, instructionId, new ArrayList<>());
        }

        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("WX.WMS.WCS_TASK_DOC_LINE_STATUS_LIMIT", tenantId);
        List<String> lovValue = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(lovValueDTOS)) {
            lovValue = lovValueDTOS.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        }
        if (!lovValue.contains(mtInstruction1.getInstructionStatus())) {
            throw new MtException("WX_WMS_ASRS_OUT_00021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX_WMS_ASRS_OUT_00021", "WMS", mtInstruction1.getInstructionStatus()));
            //flag = WmsConstant.CONSTANT_N;
        } else {
            if (CollectionUtils.isNotEmpty(newSn) || CollectionUtils.isNotEmpty(sn1)) {
                flag = WmsConstant.CONSTANT_N;
                return flag;
            }
            flag = WmsConstant.CONSTANT_Y;
        }
        return flag;
    }

    @Override
    @ProcessLovValue
    public Void snCheck(Long tenantId, WmsStandingWarehouseOutboundPlatformDTO dto) {
        List<String> barCodes = new ArrayList<>();
        List<WmsStandingWarehouseOutboundPlatformLineDTO> sn = wmsInstructionSnRelMapper.getSn(tenantId, dto.getInstructionId(), barCodes);
        if (CollectionUtils.isNotEmpty(sn)) {
            sn.stream().forEach(getMaterialLotCode -> {
                barCodes.add(getMaterialLotCode.getMaterialLotCode());
            });
        }
        //批量粘贴校验
        if (CollectionUtils.isNotEmpty(dto.getMaterialLotCodesList())) {
            dto.getMaterialLotCodesList().stream().forEach(materialLotCode -> {
                //校验SN是否关联单据行
                List<WmsStandingWarehouseOutboundPlatformLineDTO> snList = wmsInstructionSnRelMapper.snRelation(tenantId, materialLotCode, null);
                if (CollectionUtils.isNotEmpty(snList)) {
                    List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("WX.WMS.AUTO_SN_DOC_STATUS_LIMIT", tenantId);
                    List<String> lineStatus = lovValueDTOS.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                    if (!lineStatus.contains(snList.get(0).getInstructionStatus())) {
                        throw new MtException("WX_WMS_ASRS_OUT_00019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WX_WMS_ASRS_OUT_00019", "WMS", materialLotCode));
                    }
                }

                //已指定校验
                if (barCodes.contains(materialLotCode)) {
                    throw new MtException("WX_WMS_ASRS_OUT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WX_WMS_ASRS_OUT_0001", "WMS", materialLotCode));
                }
                barCodes.clear();
                barCodes.add(materialLotCode);
            });
            List<MtMaterialLot> materialLots = mtMaterialLotRepository.materialLotByCodeBatchGet(tenantId, barCodes);
            if (CollectionUtils.isNotEmpty(materialLots)) {
                materialLots.stream().forEach(materialLot -> {
                    //有效性校验：
                    if (!StringUtils.equals(materialLot.getEnableFlag(), WmsConstant.CONSTANT_Y)) {
                        throw new MtException("WX_WMS_ASRS_OUT_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WX_WMS_ASRS_OUT_0002", "WMS", materialLot.getMaterialLotCode()));
                    }
                    // 根据输入参数获取拓展属性
                    MtExtendVO extendVO = new MtExtendVO();
                    extendVO.setTableName("mt_material_lot_attr");
                    extendVO.setKeyId(materialLot.getMaterialLotId());
                    extendVO.setAttrName(WmsConstant.ExtendAttr.STATUS);
                    List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
                    if (CollectionUtils.isNotEmpty(mtExtendAttrVOS)) {
                        //状态校验
                        if (!StringUtils.equals(mtExtendAttrVOS.get(0).getAttrValue(), WmsConstant.InstructionStatus.INSTOCK)) {
                            throw new MtException("WX_WMS_ASRS_OUT_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WX_WMS_ASRS_OUT_0003", "WMS", materialLot.getMaterialLotCode()));
                        }
                    }
                    //物料校验
                    MtInstruction mtInstruction = new MtInstruction();
                    mtInstruction.setTenantId(tenantId);
                    mtInstruction.setInstructionId(dto.getInstructionId());
                    MtInstruction mtInstruction1 = mtInstructionRepository.selectByPrimaryKey(mtInstruction);
                    if (!StringUtils.equals(mtInstruction1.getMaterialId(), materialLot.getMaterialId())) {
                        throw new MtException("WX_WMS_ASRS_OUT_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WX_WMS_ASRS_OUT_0004", "WMS", materialLot.getMaterialLotCode()));
                    }
                    //版本校验
                    MtExtendVO mtExtendVO = new MtExtendVO();
                    mtExtendVO.setTableName("mt_instruction_attr");
                    mtExtendVO.setKeyId(dto.getInstructionId());
                    mtExtendVO.setAttrName(WmsConstant.MaterialLotAttr.MATERIAL_VERSION);
                    List<MtExtendAttrVO> mtExtendAttrVOS1 = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);

                    mtExtendVO.setTableName("mt_material_lot_attr");
                    mtExtendVO.setKeyId(materialLot.getMaterialLotId());
                    extendVO.setAttrName(WmsConstant.MaterialLotAttr.MATERIAL_VERSION);
                    List<MtExtendAttrVO> mtExtendAttrVOS3 = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
                    if (CollectionUtils.isNotEmpty(mtExtendAttrVOS1)) {
                        if (StringUtils.isNotBlank(mtExtendAttrVOS1.get(0).getAttrValue())) {
                            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS3)) {
                                if (!StringUtils.equals(mtExtendAttrVOS1.get(0).getAttrValue(), mtExtendAttrVOS3.get(0).getAttrValue())) {
                                    throw new MtException("WX_WMS_ASRS_OUT_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "WX_WMS_ASRS_OUT_0005", "WMS", materialLot.getMaterialLotCode()));
                                }
                            } else {
                                throw new MtException("WX_WMS_ASRS_OUT_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "WX_WMS_ASRS_OUT_0005", "WMS", materialLot.getMaterialLotCode()));
                            }
                        }
                    }

                    //仓库校验
                    List<MtModLocator> locatorList = mtModLocatorRepository.selectByCondition(Condition.builder(MtModLocator.class)
                            .andWhere(Sqls.custom()
                                    .andEqualTo(MtModLocator.FIELD_TENANT_ID, tenantId)
                                    .andEqualTo(MtModLocator.FIELD_LOCATOR_ID, materialLot.getLocatorId()))
                            .build());
                    if (CollectionUtils.isNotEmpty(locatorList)) {
                        locatorList.stream().forEach(locator -> {
                            if (!StringUtils.equals(locator.getParentLocatorId(), mtInstruction1.getFromLocatorId())) {
                                throw new MtException("WX_WMS_ASRS_OUT_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "WX_WMS_ASRS_OUT_0006", "WMS", materialLot.getMaterialLotCode()));
                            }
                        });
                    }

                    //销售订单校验
                    if (StringUtils.equals(dto.getSpecStockFlag(), WmsConstant.KEY_IFACE_STATUS_ERROR)) {
                        //instructionId对应
                        mtExtendVO.setTableName("mt_instruction_attr");
                        mtExtendVO.setKeyId(dto.getInstructionId());
                        mtExtendVO.setAttrName(WmsConstant.MaterialLotAttr.SO_NUM);
                        List<MtExtendAttrVO> mtExtendAttrVO9 = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
                        mtExtendVO.setAttrName(WmsConstant.MaterialLotAttr.SO_LINE_NUM);
                        List<MtExtendAttrVO> mtExtendAttrVOS8 = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);

                        //物料批对应
                        extendVO.setKeyId(materialLot.getMaterialLotId());
                        extendVO.setTableName("mt_material_lot_attr");
                        extendVO.setAttrName(WmsConstant.MaterialLotAttr.SO_NUM);
                        List<MtExtendAttrVO> mtExtendAttrVOS99 = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
                        extendVO.setAttrName(WmsConstant.MaterialLotAttr.SO_LINE_NUM);
                        List<MtExtendAttrVO> mtExtendAttrVOS88 = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);

                        if (CollectionUtils.isNotEmpty(mtExtendAttrVO9) && CollectionUtils.isNotEmpty(mtExtendAttrVOS99)) {
                            if (!StringUtils.equals(mtExtendAttrVO9.get(0).getAttrValue(), mtExtendAttrVOS99.get(0).getAttrValue())) {
                                throw new MtException("WX_WMS_ASRS_OUT_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "WX_WMS_ASRS_OUT_0007", "WMS", materialLot.getMaterialLotCode()));
                            } else if (!StringUtils.equals(mtExtendAttrVOS8.get(0).getAttrValue(), mtExtendAttrVOS88.get(0).getAttrValue())) {
                                throw new MtException("WX_WMS_ASRS_OUT_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "WX_WMS_ASRS_OUT_0007", "WMS", materialLot.getMaterialLotCode()));
                            }
                        }

                    }
                });
            }
        }
        return null;
    }

    @Override
    @ProcessLovValue
    public List<WmsStandingWarehouseOutboundPlatformLineDTO> snBatchEntry(Long tenantId, WmsStandingWarehouseOutboundPlatformDTO dto) {
        //校验是否允许编辑，
        //校验
        snCheck(tenantId, dto);
        //校验完成之后进行录入
        List<WmsStandingWarehouseOutboundPlatformLineDTO> materialLots = wmsInstructionSnRelMapper.snInsert(tenantId, dto.getMaterialLotCodesList());
        if (CollectionUtils.isNotEmpty(materialLots)) {
            return materialLots;
        } else {
            return new ArrayList<>();
        }
    }

/*    @Override
    public List<WmsStandingWarehouseOutboundPlatformLineDTO> snSignEntry(Long tenantId, WmsStandingWarehouseOutboundPlatformDTO dto) {
        //校验
        snCheck(tenantId, dto);
        List<WmsStandingWarehouseOutboundPlatformLineDTO> sn = wmsInstructionSnRelMapper.getSn(tenantId, dto.getInstructionId(), dto.getMaterialLotCodesList());
        if (CollectionUtils.isNotEmpty(sn)) {
            return sn;
        } else {
            return new ArrayList<>();
        }
    }*/

    @Override
    @ProcessLovValue
    @Transactional(rollbackFor = Exception.class)
    public Void snBatchSaveEntry(Long tenantId, WmsStandingWarehouseOutboundPlatformDTO dto) {
        if (CollectionUtils.isNotEmpty(dto.getMaterialLotCodesList())) {
            List<WmsStandingWarehouseOutboundPlatformLineDTO> sn = wmsInstructionSnRelMapper.snInsert(tenantId, dto.getMaterialLotCodesList());
            List<WmsInstructionSnRel> list = new ArrayList<>();
            List<String> keyIds = customDbRepository.getNextKeys("wms_instruction_sn_rel_s", sn.size());
            List<String> cIds = customDbRepository.getNextKeys("wms_instruction_sn_rel_cid_s", sn.size());
            int index = 0;
            //指定SN的数量与单据行数量是否一致
            double sum = sn.stream().mapToDouble(WmsStandingWarehouseOutboundPlatformLineDTO::getPrimaryUomQty).sum();
            if (new BigDecimal(dto.getQuantity()).compareTo(new BigDecimal(sum)) != 0) {
                throw new MtException("WX_WMS_ASRS_OUT_00020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WX_WMS_ASRS_OUT_00020", "WMS", mtInstructionRepository.selectByPrimaryKey(dto.getInstructionId()).getInstructionNum()));
            }
            List<WmsStandingWarehouseOutboundPlatformLineDTO> sn1 = wmsInstructionSnRelMapper.getSn(tenantId, dto.getInstructionId(), dto.getMaterialLotCodesList());
            if (CollectionUtils.isEmpty(sn1)) {
                if (CollectionUtils.isNotEmpty(sn)) {
                    for (WmsStandingWarehouseOutboundPlatformLineDTO insert : sn) {
                        WmsInstructionSnRel wmsInstructionSnRel = new WmsInstructionSnRel();
                        wmsInstructionSnRel.setWmsInstructionSnRelId(keyIds.get(index));
                        wmsInstructionSnRel.setCid(Long.valueOf(cIds.get(index++)));
                        wmsInstructionSnRel.setTenantId(tenantId);
                        wmsInstructionSnRel.setSiteId(dto.getSiteId());
                        wmsInstructionSnRel.setInstructionDocId(dto.getInstructionDocId());
                        wmsInstructionSnRel.setInstructionId(dto.getInstructionId());
                        wmsInstructionSnRel.setMaterialLotId(insert.getMaterialLotId());
                        list.add(wmsInstructionSnRel);
                    }
                }
                if (CollectionUtils.isNotEmpty(list)) {
                    wmsInstructionSnRelRepository.batchInsert(list);
                }
            } else {
                throw new MtException("WX_WMS_ASRS_OUT_00018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WX_WMS_ASRS_OUT_00018", "WMS", sn1.get(0).getMaterialLotCode()));
            }
        }
        return null;
    }

    @Override
    @ProcessLovValue
    @Transactional(rollbackFor = Exception.class)
    public Void snDeleteEntry(Long tenantId, WmsStandingWarehouseOutboundPlatformDTO dto) {
        List<WmsStandingWarehouseOutboundPlatformLineDTO> sn = wmsInstructionSnRelMapper.getSn(tenantId, dto.getInstructionId(), dto.getMaterialLotCodesList());
        List<WmsInstructionSnRel> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(sn)) {
            sn.stream().forEach(materLot -> {
                WmsInstructionSnRel wmsInstructionSnRel = new WmsInstructionSnRel();
                wmsInstructionSnRel.setTenantId(tenantId);
                wmsInstructionSnRel.setInstructionId(dto.getInstructionId());
                wmsInstructionSnRel.setMaterialLotId(materLot.getMaterialLotId());
                List<WmsInstructionSnRel> select = wmsInstructionSnRelRepository.select(wmsInstructionSnRel);
                list.addAll(select);
            });
        }
        if (CollectionUtils.isNotEmpty(list)) {
            wmsInstructionSnRelRepository.batchDeleteByPrimaryKey(list);
        }
        return null;
    }

    @Override
    @ProcessLovValue
    @Transactional(rollbackFor = Exception.class)
    public List<ItfFinishDeliveryInstructionIfaceVO> snBatchOutBound(Long tenantId, List<WmsStandingWarehouseOutboundPlatformDTO> dtoList) {
        List<ItfFinishDeliveryInstructionIfaceVO> resultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dtoList)) {
            //仓库校验
            List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("WX.WMS.AUTO_WAREHOUSE", tenantId);
            List<String> lovs = lovValueDTOS.stream().map(LovValueDTO::getValue).collect(Collectors.toList());

            //行状态
            List<LovValueDTO> lineStatusList = lovAdapter.queryLovValue("WX.WMS.WCS_TASK_DOC_LINE_STATUS_LIMIT", tenantId);
            List<String> lineStatus = lineStatusList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());

            List<String> instrucionIdList = dtoList.stream().map(WmsStandingWarehouseOutboundPlatformDTO::getInstructionId).collect(Collectors.toList());
            dtoList.stream().forEach(dto -> {
                //（1）单据行状态校验
                if (!lineStatus.contains(dto.getInstructionStatus())) {
                    throw new MtException("WX_WMS_ASRS_OUT_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WX_WMS_ASRS_OUT_0008", "WMS", dto.getInstructionLineNum()));
                }

                if (!lovs.contains(dto.getLocatorCode())) {
                    throw new MtException("WX_WMS_ASRS_OUT_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WX_WMS_ASRS_OUT_0009", "WMS", dto.getInstructionLineNum()));
                }
                //指定SN的数量
                List<WmsStandingWarehouseOutboundPlatformLineDTO> wmsStandingWarehouseOutboundPlatformLineDTOS = snSpecified(tenantId, dto);
                if (CollectionUtils.isEmpty(wmsStandingWarehouseOutboundPlatformLineDTOS)) {
                    //立库现有量校验
                    if (BigDecimal.ZERO.compareTo(new BigDecimal(dto.getStangingQuantity())) == 0) {
                        throw new MtException("WX_WMS_ASRS_OUT_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WX_WMS_ASRS_OUT_0010", "WMS", dto.getInstructionLineNum()));
                    }
                } else {
                    //若一致
                    wmsStandingWarehouseOutboundPlatformLineDTOS.stream().forEach(locator -> {
                        List<MtMaterialLot> locatorId = wmsInstructionSnRelMapper.getLocatorId(tenantId, locator.getTopLlocatorId());
                        if (CollectionUtils.isEmpty(locatorId)) {
                            throw new MtException("WX_WMS_ASRS_OUT_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WX_WMS_ASRS_OUT_0012", "WMS", dto.getInstructionLineNum()));
                        }
                    });

                    //立库现有量校验
                    if (BigDecimal.ZERO.compareTo(new BigDecimal(dto.getStangingQuantity())) == 0) {
                        throw new MtException("WX_WMS_ASRS_OUT_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WX_WMS_ASRS_OUT_0010", "WMS", dto.getInstructionLineNum()));
                    }

                    //SN校验 :判断单据行指定SN的数量与单据的需求数量是否一致
                    Stream<Double> doubleStream = wmsStandingWarehouseOutboundPlatformLineDTOS.stream().map(WmsStandingWarehouseOutboundPlatformLineDTO::getPrimaryUomQty);
                    double sum1 = doubleStream.mapToDouble(Double::doubleValue).sum();
                    //double v1 = dto.getQuantity() - sum1;
                    //若不一致
                    if (new BigDecimal(dto.getQuantity()).compareTo(new BigDecimal(sum1)) == 0) {
                        //若一致
                        wmsStandingWarehouseOutboundPlatformLineDTOS.stream().forEach(locator -> {
                            List<MtMaterialLot> locatorId = wmsInstructionSnRelMapper.getLocatorId(tenantId, locator.getTopLlocatorId());
                            if (CollectionUtils.isEmpty(locatorId)) {
                                throw new MtException("WX_WMS_ASRS_OUT_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "WX_WMS_ASRS_OUT_0012", "WMS", dto.getInstructionLineNum()));
                            }
                        });
                    } else {
                        throw new MtException("WX_WMS_ASRS_OUT_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WX_WMS_ASRS_OUT_0011", "WMS", dto.getInstructionLineNum()));
                    }
                }
                //单据行是否存在未完成任务校验
                WcsTaskIface wcsTaskIface = new WcsTaskIface();
                wcsTaskIface.setTenantId(tenantId);
                wcsTaskIface.setDocLineId(dto.getInstructionId());
                List<WcsTaskIface> selects = wcsTaskIfaceRepository.select(wcsTaskIface);

                if (CollectionUtils.isNotEmpty(selects)) {
                    List<String> status = selects.stream().map(WcsTaskIface::getTaskStatus).collect(Collectors.toList());
                    for (String s : status) {
                        if (StringUtils.equals(s, WmsConstant.NEW) || StringUtils.equals(s, WmsConstant.EXECUTING)) {
                            throw new MtException("WX_WMS_ASRS_OUT_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WX_WMS_ASRS_OUT_0013", "WMS", dto.getInstructionLineNum()));
                        }
                    }
                }
                //执行逻辑
                List<WmsStandingWarehouseOutboundPlatformLineDTO> materialLotList = snSpecified(tenantId, dto);
//                List<WmsStandingWarehouseOutboundPlatformLineDTO> materialLotList = wmsInstructionSnRelMapper.getSn(tenantId, dto.getInstructionId(), new ArrayList<>());

                //（1）单据行未指定SN，调用API：{itfWCSTaskIface}
                ItfFinishDeliveryInstructionIfaceDTO noPoint = new ItfFinishDeliveryInstructionIfaceDTO();
                noPoint.setInstructionDocId(dtoList.get(0).getInstructionDocId());
                noPoint.setInstructionId(dto.getInstructionId());
                noPoint.setMaterialCode(dto.getMaterialCode());
                noPoint.setMaterialVersion(dto.getMaterialVersion());
                if (StringUtils.equals(dto.getSpecStockFlag(), WmsConstant.KEY_IFACE_STATUS_ERROR)) {
                    noPoint.setSoNum(dto.getSoNum());
                    noPoint.setSoLineNum(dto.getSoLineNum());
                }
                noPoint.setExitNum(dto.getExitNum());
                noPoint.setWarehouseCode(dto.getLocatorCode());

                if (CollectionUtils.isEmpty(materialLotList)) {
                    dto.setActualQty(dto.getActualQty() != null ? dto.getActualQty() : 0D);
                    //（1）单据行未指定SN，调用API：{itfWCSTaskIface}
                    BigDecimal v = new BigDecimal(dto.getQuantity()).subtract(new BigDecimal(dto.getActualQty()));
                    BigDecimal v1 = v.subtract(new BigDecimal(dto.getStangingQuantity()));
                    if (v.compareTo(new BigDecimal(dto.getStangingQuantity())) == -1) {
                        noPoint.setQty(v);
                    } else if (v.compareTo(new BigDecimal(dto.getStangingQuantity())) == 1) {
                        noPoint.setQty(new BigDecimal(dto.getStangingQuantity()));
                    } else{
                        noPoint.setQty(new BigDecimal(dto.getStangingQuantity()));
                    }
                } else {
                    //（2）单据行指定了SN，调用API：{itfWCSTaskIface}
                    List<Double> doubleSum = new ArrayList<>();
                    List<String> materialLotCodeList = new ArrayList<>();
                    materialLotList.stream().forEach(materialLotLocator -> {
                        List<MtMaterialLot> locatorId = wmsInstructionSnRelMapper.getSnLocatorId(tenantId, materialLotLocator.getTopLlocatorId(),materialLotLocator.getMaterialLotCode());
                        if (CollectionUtils.isNotEmpty(locatorId)) {
                            double sum = locatorId.stream().mapToDouble(MtMaterialLot::getPrimaryUomQty).sum();
                            doubleSum.add(sum);
                            materialLotCodeList.add(materialLotLocator.getMaterialLotCode());
                        }
                    });
                    double sum = doubleSum.stream().mapToDouble(Double::doubleValue).sum();
                    noPoint.setQty(new BigDecimal(sum));
                    noPoint.setMaterialLotCodeList(materialLotCodeList);
                }
                List<ItfFinishDeliveryInstructionIfaceVO> itfFinishDeliveryInstructionIfaceVOS = itfFinishDeliveryInstructionIfaceRepository.itfWCSTaskIface(tenantId, Collections.singletonList(noPoint));
                resultList.addAll(itfFinishDeliveryInstructionIfaceVOS);
            });
        }
        return resultList;
    }

    @Override
    public List<ItfFinishDeliveryInstructionIfaceVO> snCancel(Long tenantId, WmsStandingWarehouseOutboundPlatformReturnDTO2 dto) {
        //库任务清单操作栏，可对出库任务进行取消，调用API：{itfWCSTaskIface}
        ItfFinishDeliveryInstructionIfaceDTO cancel = new ItfFinishDeliveryInstructionIfaceDTO();
        cancel.setTaskNum(dto.getTaskNum());
        cancel.setTaskStatus(WmsConstant.DocStatus.CANCEL);
        List<ItfFinishDeliveryInstructionIfaceVO> itfFinishDeliveryInstructionIfaceVOS = itfFinishDeliveryInstructionIfaceRepository.itfWCSTaskIface(tenantId, Collections.singletonList(cancel));
        return itfFinishDeliveryInstructionIfaceVOS;
    }


    @Override
    @ProcessLovValue
    public Page<WmsStandingWarehouseOutboundPlatformReturnDTO2> figure(Long tenantId, PageRequest pageRequest) {
        Page<WmsStandingWarehouseOutboundPlatformReturnDTO2> resultPage = PageHelper.doPageAndSort(pageRequest, () -> wmsInstructionSnRelMapper.exitNum(tenantId, "", new ArrayList<>()));
        return resultPage;
    }


    @Override
    @ProcessLovValue
    public WmsStandingWarehouseOutboundPlatformReturnDTO mainQuery(Long tenantId) {
        WmsStandingWarehouseOutboundPlatformReturnDTO returnDto = new WmsStandingWarehouseOutboundPlatformReturnDTO();
        //出库任务区域显示
        List<WcsTaskIface> wcsTaskIfaces1 = wcsTaskIfaceRepository.selectByCondition(Condition.builder(WcsTaskIface.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(WcsTaskIface.FIELD_TASK_STATUS, WmsConstant.EXECUTING))
                .orderByAsc(WcsTaskIface.FIELD_CREATION_DATE)
                .build());
        if (CollectionUtils.isNotEmpty(wcsTaskIfaces1)) {
            returnDto.setTaskNum(wcsTaskIfaces1.get(0).getTaskNum());
            returnDto.setCreationDate(wcsTaskIfaces1.get(0).getCreationDate());
            returnDto.setLastUpdateDate(wcsTaskIfaces1.get(0).getLastUpdateDate());


            long executeTime = System.currentTimeMillis() - returnDto.getLastUpdateDate().getTime();
            long execute = TimeUnit.MILLISECONDS.toMinutes(executeTime);
            Double minute = new BigDecimal((float) execute).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            //执行时长
            returnDto.setExecuteTime(String.format("%.1f", minute) + "min");

            long lastTime = returnDto.getLastUpdateDate().getTime() - returnDto.getCreationDate().getTime();
            long last = TimeUnit.MILLISECONDS.toMinutes(lastTime);
            Double minutes = new BigDecimal((float) last).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            returnDto.setLastCreationDate(String.format("%.1f", minutes) + "min");
        }
        //2)	图标
        List<WmsStandingWarehouseOutboundPlatformReturnDTO3> wmsStandingWarehouseOutboundPlatformReturnDTO3s = wmsInstructionSnRelMapper.statusList(tenantId);
        if (CollectionUtils.isNotEmpty(wmsStandingWarehouseOutboundPlatformReturnDTO3s)) {
            for (WmsStandingWarehouseOutboundPlatformReturnDTO3 wmsStandingWarehouseOutboundPlatformReturnDTO3 : wmsStandingWarehouseOutboundPlatformReturnDTO3s) {
                String meaning = lovAdapter.queryLovMeaning("WX.WMS.TASK_STATUS", tenantId, wmsStandingWarehouseOutboundPlatformReturnDTO3.getStatusList());
                wmsStandingWarehouseOutboundPlatformReturnDTO3.setStatusListMeaning(meaning);
            }
            returnDto.setReturnDTO3List(wmsStandingWarehouseOutboundPlatformReturnDTO3s);
        }
        return returnDto;
    }
}
