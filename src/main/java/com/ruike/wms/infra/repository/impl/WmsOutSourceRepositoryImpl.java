package com.ruike.wms.infra.repository.impl;

import cn.hutool.core.util.ObjectUtil;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.itf.api.dto.ItfLightTaskIfaceDTO;
import com.ruike.itf.app.service.ItfLightTaskIfaceService;
import com.ruike.itf.app.service.ItfObjectTransactionIfaceService;
import com.ruike.itf.domain.entity.ItfLightTaskIface;
import com.ruike.itf.domain.vo.ItfLightTaskIfaceVO;
import com.ruike.itf.infra.mapper.ItfLightTaskIfaceMapper;
import com.ruike.wms.api.dto.WmsOutSourceDTO;
import com.ruike.wms.api.dto.WmsOutSourceSendDTO;
import com.ruike.wms.domain.entity.WmsPfepInertiaLocator;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsOutSourceRepository;
import com.ruike.wms.domain.repository.WmsPfepInertiaLocatorRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.*;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsOutSourceMapper;
import com.ruike.wms.infra.mapper.WmsPfepInertiaLocatorMapper;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.vo.*;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.actual.domain.entity.MtInstructionActualDetail;
import tarzan.actual.domain.repository.MtInstructionActualDetailRepository;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.actual.domain.vo.MtInstructionActualVO;
import tarzan.actual.domain.vo.MtInstructionActualVO1;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.api.dto.MtInstructionDocDTO2;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.*;
import tarzan.instruction.infra.mapper.MtInstructionMapper;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.material.domain.entity.MtPfepInventory;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtPfepInventoryRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtMaterialVO3;
import tarzan.material.domain.vo.MtPfepInventoryVO;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorOrgRelRepository;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtSupplierRepository;
import tarzan.modeling.domain.repository.MtSupplierSiteRepository;
import tarzan.modeling.domain.vo.MtModLocatorVO16;
import tarzan.modeling.domain.vo.MtModLocatorVO9;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @program: tarzan-mes
 * @description: ??????????????????
 * @author: han.zhang
 * @create: 2020/06/18 11:12
 */
@Component
@Transactional(rollbackFor = Exception.class)
public class WmsOutSourceRepositoryImpl implements WmsOutSourceRepository {
    @Autowired
    private WmsOutSourceMapper wmsOutSourceMapper;
    @Autowired
    private MtUserClient mtUserClient;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtInstructionRepository mtInstructionRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private MtInstructionActualRepository mtInstructionActualRepository;
    @Autowired
    private MtInstructionActualDetailRepository mtInstructionActualDetailRepository;
    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;
    @Autowired
    private MtModLocatorOrgRelRepository mtModLocatorOrgRelRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtExtendSettingsMapper mtExtendSettingsMapper;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private WmsTransactionTypeRepository wmsTransactionTypeRepository;
    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;
    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;
    @Autowired
    private MtSupplierRepository mtSupplierRepository;
    @Autowired
    private MtSupplierSiteRepository mtSupplierSiteRepository;

    @Autowired
    private ItfObjectTransactionIfaceService itfObjectTransactionIfaceService;
    @Autowired
    private MtInstructionMapper mtInstructionMapper;
    @Autowired
    private MtPfepInventoryRepository mtPfepInventoryRepository;
    @Autowired
    private WmsPfepInertiaLocatorRepository wmsPfepInertiaLocatorRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private ItfLightTaskIfaceService itfLightTaskIfaceService;
    @Autowired
    private ItfLightTaskIfaceMapper itfLightTaskIfaceMapper;


    @Override
    public List<WmsOutSourceOrderQueryVO> selectOutSourceOrder(Long tenantId, String instructionDocNum) {
        //????????????
        List<WmsOutSourceOrderQueryVO> wmsOutSourceOrderQueryVOS = wmsOutSourceMapper.selectOutSourceOrder(tenantId, instructionDocNum);
        //???????????????
        wmsOutSourceOrderQueryVOS.forEach(item -> {
            if (ObjectUtil.isNotNull(item.getPersonId())) {
                MtUserInfo mtUserInfo = mtUserClient.userInfoGet(tenantId, item.getPersonId());
                item.setPersonName(mtUserInfo.getRealName());
            }
        });
        return wmsOutSourceOrderQueryVOS;
    }

    @Override
    @ProcessLovValue(targetField = {"lineList"})
    public WmsOutSourceScanVO scanOutSourceOrder(Long tenantId, String instructionDocNum) {
        WmsOutSourceScanVO wmsOutSourceScanVO = new WmsOutSourceScanVO();
        //???????????????
        List<WmsOutSourceOrderQueryVO> wmsOutSourceOrderQueryVOS = wmsOutSourceMapper.selectOutSourceOrderNotLike(tenantId, instructionDocNum);
        //????????????????????????
        if (CollectionUtils.isEmpty(wmsOutSourceOrderQueryVOS)) {
            throw new MtException("MT_INVENTORY_0058",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0058",
                            "INVENTORY"));
        }
        //??????????????????
        if (!WmsConstant.InstructionStatus.RELEASED.equals(wmsOutSourceOrderQueryVOS.get(0).getInstructionDocStatus())) {
            throw new MtException("MT_INVENTORY_0071",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0071",
                            "INVENTORY"));
        }
        //??????????????????
        /*if (!(WmsConstant.InspectionDocType.OUTSOURCING_INVOICE.equals(wmsOutSourceOrderQueryVOS.get(0).getInstructionDocType())
                || WmsConstant.InspectionDocType.OUTSOURCING_RETURN.equals(wmsOutSourceOrderQueryVOS.get(0).getInstructionDocType()))) {*/
        if (!StringUtils.equalsAny(wmsOutSourceOrderQueryVOS.get(0).getInstructionDocType(),
                WmsConstant.InspectionDocType.OUTSOURCING_INVOICE, WmsConstant.InspectionDocType.OUTSOURCING_RETURN)) {
            throw new MtException("MT_INVENTORY_0072",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0072",
                            "INVENTORY"));
        }
        wmsOutSourceScanVO.setHeadVO(wmsOutSourceOrderQueryVOS.get(0));

        //???????????????
        List<WmsOutSourceLineVO> wmsOutSourceLineVoS = wmsOutSourceMapper.selectOutSourceLine(tenantId, wmsOutSourceOrderQueryVOS.get(0).getInstructionDocId());
        //2020-10-20 edit by chaonan.hu for yiwei.zhou ??????????????????????????? ??????????????????????????????
        //2021-07-06 edit by li.zhang?????????????????????????????????????????????????????????
        //?????????????????????MT_INSTRUCTION???QUANTITY???QUANTITY????????????????????????????????????????????????ACTUAL_ORDERED_QTY??????????????????????????????0
        if (WmsConstant.InspectionDocType.OUTSOURCING_INVOICE.equals(wmsOutSourceOrderQueryVOS.get(0).getInstructionDocType())) {
            for (WmsOutSourceLineVO wmsOutSourceLineVO : wmsOutSourceLineVoS) {
                MtExtendVO mtExtendVO = new MtExtendVO();
                mtExtendVO.setTableName("mt_instruction_attr");
                mtExtendVO.setKeyId(wmsOutSourceLineVO.getInstructionId());
                mtExtendVO.setAttrName("ACTUAL_ORDERED_QTY");
                List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotBlank(mtExtendAttrVOS.get(0).getAttrValue())) {
                    wmsOutSourceLineVO.setQuantity(Double.valueOf(mtExtendAttrVOS.get(0).getAttrValue()));
                } else {
                    wmsOutSourceLineVO.setQuantity(0D);
                }
                //??????????????????
                //?????????Id??????????????????-????????????
                String lineMaterialVersion = "";
                mtExtendVO.setAttrName("MATERIAL_VERSION");
                mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotBlank(mtExtendAttrVOS.get(0).getAttrValue())) {
                    lineMaterialVersion = mtExtendAttrVOS.get(0).getAttrValue();
                }
                List<LovValueDTO> LovValueDTOList1 = lovAdapter.queryLovValue("WX.WMS.LOCATOR_TYPE_LIMIT", tenantId);
                MtModLocatorVO16 materialLocator = new MtModLocatorVO16();
                if(CollectionUtils.isNotEmpty(LovValueDTOList1)){
                    List<String> locatorTypeList = LovValueDTOList1.stream().map(LovValueDTO::getValue).collect(toList());
                    materialLocator = wmsOutSourceMapper.getMaterialLocatorCodeByType(tenantId, wmsOutSourceLineVO.getMaterialId(),
                            wmsOutSourceOrderQueryVOS.get(0).getSiteId(), wmsOutSourceLineVO.getFromLocatorId(), lineMaterialVersion,locatorTypeList);
                }else{
                    materialLocator = wmsOutSourceMapper.getMaterialLocatorCode(tenantId, wmsOutSourceLineVO.getMaterialId(),
                            wmsOutSourceOrderQueryVOS.get(0).getSiteId(), wmsOutSourceLineVO.getFromLocatorId(), lineMaterialVersion);
                }
                if (materialLocator != null) {
                    wmsOutSourceLineVO.setGetMaterialLocatorId(materialLocator.getLocatorId());
                    wmsOutSourceLineVO.setGetMaterialLocatorCode(materialLocator.getLocatorCode());
                }
//                String materialLocator = getMaterialLocator(tenantId, wmsOutSourceOrderQueryVOS.get(0).getSiteId(), wmsOutSourceLineVO);
//                if(StringUtils.isNotBlank(materialLocator)){
//                    wmsOutSourceLineVO.setGetMaterialLocatorCode(materialLocator);
//                }
                if(StringUtils.isNotBlank(wmsOutSourceLineVO.getGetMaterialLocatorCode())){
                    //?????????????????????????????????ITF.LOCATOR_LABEL_ID???
                    List<LovValueDTO> LovValueDTOs = lovAdapter.queryLovValue("ITF.LOCATOR_LABEL_ID", tenantId);
                    List<LovValueDTO> LovValueDTOList = LovValueDTOs.stream().filter(item ->item.getValue().equals(wmsOutSourceLineVO.getGetMaterialLocatorCode())).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(LovValueDTOList)){
                        wmsOutSourceLineVO.setLightFlag("Y");
                        wmsOutSourceLineVO.setLightStatus("OFF");
                    }else{
                        wmsOutSourceLineVO.setLightFlag("N");
                    }
                }
            }
        }else{
            ////2021-07-06 edit by li.zhang??????????????????????????????????????????
            for (WmsOutSourceLineVO wmsOutSourceLineVO : wmsOutSourceLineVoS) {
                String locatorRecomMode = mtInstructionMapper.getMode(tenantId,wmsOutSourceOrderQueryVOS.get(0).getSiteId(),wmsOutSourceLineVO.getMaterialId(),wmsOutSourceLineVO.getToLocatorId());
                if(StringUtils.isBlank(locatorRecomMode)){
                    wmsOutSourceLineVO.setGetMaterialLocatorCode("");
                }else if(locatorRecomMode.equals("POSITION")){
                    MtPfepInventoryVO mtPfepInventoryVO = new MtPfepInventoryVO();
                    mtPfepInventoryVO.setMaterialId(wmsOutSourceLineVO.getMaterialId());
                    mtPfepInventoryVO.setSiteId(wmsOutSourceOrderQueryVOS.get(0).getSiteId());
                    mtPfepInventoryVO.setOrganizationType("LOCATOR");
                    mtPfepInventoryVO.setOrganizationId(wmsOutSourceLineVO.getToLocatorId());
                    MtPfepInventory mtPfepInventory = mtPfepInventoryRepository.pfepInventoryGet(tenantId,mtPfepInventoryVO);
                    if(StringUtils.isBlank(mtPfepInventory.getStockLocatorId())){
                        wmsOutSourceLineVO.setGetMaterialLocatorCode("");
                    }else{
                        wmsOutSourceLineVO.setGetMaterialLocatorCode(mtModLocatorRepository.selectByPrimaryKey(mtPfepInventory.getStockLocatorId()).getLocatorCode());
                    }
                }else if(locatorRecomMode.equals("INERTIA")){
                    WmsPfepInertiaLocator wmsPfepInertiaLocator = new WmsPfepInertiaLocator();
                    wmsPfepInertiaLocator.setTenantId(tenantId);
                    wmsPfepInertiaLocator.setMaterialId(wmsOutSourceLineVO.getMaterialId());
                    wmsPfepInertiaLocator.setSiteId(wmsOutSourceOrderQueryVOS.get(0).getSiteId());
                    wmsPfepInertiaLocator.setWarehouseId(wmsOutSourceLineVO.getToLocatorId());
                    List<WmsPfepInertiaLocator> wmsPfepInertiaLocatorList = wmsPfepInertiaLocatorRepository.select(wmsPfepInertiaLocator);
                    if(wmsPfepInertiaLocatorList.size() !=0){
                        wmsOutSourceLineVO.setGetMaterialLocatorCode(mtModLocatorRepository.selectByPrimaryKey(wmsPfepInertiaLocatorList.get(0).getLocatorId()).getLocatorCode());
                    }else{
                        wmsOutSourceLineVO.setGetMaterialLocatorCode("");
                    }
                }
            }
        }
        MtExtendSettings mtExtendSettings = new MtExtendSettings();
        mtExtendSettings.setAttrName("MATERIAL_VERSION");
        wmsOutSourceLineVoS.forEach(item -> {
            long codeQty = 0L;
            if (StringUtils.isNotEmpty(item.getActualId())) {
                MtInstructionActualDetail mtInstructionActualDetail = new MtInstructionActualDetail();
                mtInstructionActualDetail.setActualId(item.getActualId());
                int count = mtInstructionActualDetailRepository.selectCount(mtInstructionActualDetail);
                codeQty = (long) count;
            }

            List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                    "mt_instruction_attr", "INSTRUCTION_ID", item.getInstructionId(), Collections.singletonList(mtExtendSettings));
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS)) {
                item.setMaterialVersion(mtExtendAttrVOS.get(0).getAttrValue());
            }

            item.setCodeQty(codeQty);
            //2020-10-27 edit by chaonan.hu for yiwei.zhou ????????????????????????????????????????????????
            if(WmsConstant.InstructionStatus.COMPLETED.equals(item.getInstructionStatus())){
                item.setSortInt(2);
            }else{
                item.setSortInt(1);
            }
        });
        wmsOutSourceLineVoS = wmsOutSourceLineVoS.stream().sorted(Comparator.comparing(WmsOutSourceLineVO::getSortInt)).collect(toList());
        wmsOutSourceScanVO.setLineList(wmsOutSourceLineVoS);
        return wmsOutSourceScanVO;
    }

    @Override
    @ProcessLovValue
    @Transactional(rollbackFor = Exception.class)
    public WmsOutSourceMaterialReturnVO materialLotScan(Long tenantId, WmsOutSourceScanMaterialQueryVO materialQueryVO) {
        //??????????????????
        //modify by jiangling.zheng ????????????
        /*MtMaterialLot mtMaterialLot = new MtMaterialLot();
        mtMaterialLot.setTenantId(tenantId);
        mtMaterialLot.setMaterialLotCode(materialQueryVO.getMaterialLotCode());
        List<MtMaterialLot> materialLots = mtMaterialLotRepository.select(mtMaterialLot);*/
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(materialQueryVO.getMaterialLotCode());
        }});
        //if (CollectionUtils.isEmpty(materialLots)) {
        if (Objects.isNull(mtMaterialLot)) {
            throw new MtException("MT_INVENTORY_0054",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0054",
                            "MATERIAL_LOT", materialQueryVO.getMaterialLotCode()));
        }
        //????????????????????????
        if (MtBaseConstants.YES.equals(mtMaterialLot.getFreezeFlag())) {
            throw new MtException("WMS_COST_CENTER_0025",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_COST_CENTER_0025",
                            WmsConstant.ConstantValue.WMS, materialQueryVO.getMaterialLotCode()));
        }
        //??????????????????????????????
        if (MtBaseConstants.YES.equals(mtMaterialLot.getStocktakeFlag())) {
            throw new MtException("WMS_COST_CENTER_0034",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_COST_CENTER_0034",
                            WmsConstant.ConstantValue.WMS, materialQueryVO.getMaterialLotCode()));
        }

        //???????????????????????????
        MtExtendVO mtExtendVO0 = new MtExtendVO();
        mtExtendVO0.setKeyId(mtMaterialLot.getMaterialLotId());
        mtExtendVO0.setAttrName("MF_FLAG");
        mtExtendVO0.setTableName("mt_material_lot_attr");
        List<MtExtendAttrVO> mtExtendAttrVOS0 = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO0);
        if (!CollectionUtils.isEmpty(mtExtendAttrVOS0) && MtBaseConstants.YES.equals(mtExtendAttrVOS0.get(0).getAttrValue())) {
            throw new MtException("WMS_DISTRIBUTION_0003",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_DISTRIBUTION_0003",
                            WmsConstant.ConstantValue.WMS, materialQueryVO.getMaterialLotCode()));
        }

        //????????????????????????
        MtExtendVO mtTestVO1 = new MtExtendVO();
        mtTestVO1.setKeyId(mtMaterialLot.getMaterialLotId());
        mtTestVO1.setAttrName("SO_NUM");
        mtTestVO1.setTableName("mt_material_lot_attr");
        List<MtExtendAttrVO> mtTestVS1 = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtTestVO1);

        if (CollectionUtils.isNotEmpty(mtTestVS1)) {
            if (StringUtils.isNotEmpty(mtTestVS1.get(0).getAttrValue())) {
                throw new MtException("WMS_OUTSOURCE_EXECUTE_0001",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_OUTSOURCE_EXECUTE_0001",
                                WmsConstant.ConstantValue.WMS, materialQueryVO.getMaterialLotCode()));
            }
        }

        //????????????????????????
        MtExtendVO mtTestVO2 = new MtExtendVO();
        mtTestVO2.setKeyId(mtMaterialLot.getMaterialLotId());
        mtTestVO2.setAttrName("SO_LINE_NUM");
        mtTestVO2.setTableName("mt_material_lot_attr");
        List<MtExtendAttrVO> mtTestVS2 = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtTestVO2);

        if (CollectionUtils.isNotEmpty(mtTestVS2)) {
            if (StringUtils.isNotEmpty(mtTestVS2.get(0).getAttrValue())) {
                throw new MtException("WMS_OUTSOURCE_EXECUTE_0001",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_OUTSOURCE_EXECUTE_0001",
                                WmsConstant.ConstantValue.WMS, materialQueryVO.getMaterialLotCode()));
            }
        }


        // ?????????????????????
        MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.selectByPrimaryKey(materialQueryVO.getInstructionDocId());
        //??????????????????????????????SUPPLY_FLAG
        MtExtendVO mtExtendSf = new MtExtendVO();
        mtExtendSf.setTableName("mt_instruction_doc_attr");
        mtExtendSf.setKeyId(mtInstructionDoc.getInstructionDocId());
        mtExtendSf.setAttrName("SUPPLY_FLAG");
        List<MtExtendAttrVO> supplyFlagList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendSf);
        // mtMaterialLot = materialLots.get(0);
        //??????????????????
        if (StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_INVOICE, mtInstructionDoc.getInstructionDocType()) &&
                !MtBaseConstants.YES.equals(mtMaterialLot.getEnableFlag())) {
            throw new MtException("MT_INVENTORY_0059",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0059",
                            "INVENTORY", materialQueryVO.getMaterialLotCode()));
        } else if (StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_RETURN, mtInstructionDoc.getInstructionDocType()) &&
                MtBaseConstants.YES.equals(mtMaterialLot.getEnableFlag())) {
            throw new MtException("WMS_OUT_SOURCE_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_OUT_SOURCE_0005",
                    "WMS", materialQueryVO.getMaterialLotCode()));
        }
        //??????????????????????????????????????????
        Condition condition = StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_INVOICE, mtInstructionDoc.getInstructionDocType()) ?
                Condition.builder(MtInstruction.class).andWhere(Sqls.custom()
                                .andEqualTo(MtInstruction.FIELD_SOURCE_DOC_ID, materialQueryVO.getInstructionDocId())
                        //2020-10-20 edit by chaonan.hu for yiwei.zhou ??????SOURCE_INSTRUCTION_ID?????????????????????
//                        .andIsNotNull(MtInstruction.FIELD_SOURCE_INSTRUCTION_ID)
//                        .andNotEqualTo(MtInstruction.FIELD_SOURCE_INSTRUCTION_ID, "")
                ).build() :
                Condition.builder(MtInstruction.class).andWhere(Sqls.custom()
                        .andEqualTo(MtInstruction.FIELD_SOURCE_DOC_ID, materialQueryVO.getInstructionDocId())).build();
        // end modify
        List<MtInstruction> mtInstructions = mtInstructionRepository.selectByCondition(condition);

        //???????????????????????????????????????????????????????????????????????????
        List<MtInstructionActual> mtInstructionActualList = new ArrayList<>();
        // modify by jiangling.zheng 20200907
        /*mtInstructions.forEach(item -> {
            MtInstructionActual mtInstructionActual = new MtInstructionActual();
            mtInstructionActual.setInstructionId(item.getInstructionId());
            mtInstructionActual.setBusinessType(item.getBusinessType());
            mtInstructionActual.setInstructionType(item.getInstructionType());
            mtInstructionActual.setMaterialId(item.getMaterialId());
            List<MtInstructionActual> mtInstructionActuals = mtInstructionActualRepository.select(mtInstructionActual);

            //????????????????????????
            mtInstructionActuals.forEach(actual -> {
                MtInstructionActualDetail actualDetail = new MtInstructionActualDetail();
                actualDetail.setActualId(actual.getActualId());
                actualDetail.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                int count = mtInstructionActualDetailRepository.selectCount(actualDetail);
                if (count > 0) {
                    throw new MtException("MT_INVENTORY_0063",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0063",
                                    "INVENTORY"));
                }
            });
            if (CollectionUtils.isNotEmpty(mtInstructionActuals)) {
                mtInstructionActualList.add(mtInstructionActuals.get(0));
            }
        });*/
        //end modify
        //?????????????????????
        MtExtendVO mtExtendVO = new MtExtendVO();
        mtExtendVO.setKeyId(mtMaterialLot.getMaterialLotId());
        mtExtendVO.setAttrName(WmsConstant.ExtendAttr.STATUS);
        mtExtendVO.setTableName("mt_material_lot_attr");
        List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
        if (CollectionUtils.isEmpty(mtExtendAttrVOS) || (StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_INVOICE, mtInstructionDoc.getInstructionDocType()) &&
                !WmsConstant.InstructionStatus.INSTOCK.equals(mtExtendAttrVOS.get(0).getAttrValue()))) {
            throw new MtException("MT_INVENTORY_0060",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0060",
                            "INVENTORY"));
        }
        //????????????????????????
        if (StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_INVOICE, mtInstructionDoc.getInstructionDocType()) &&
                !MtBaseConstants.QUALITY_STATUS.OK.equals(mtMaterialLot.getQualityStatus())) {
            throw new MtException("MT_INVENTORY_0062",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0062",
                            "INVENTORY"));
        }

//        MtInstruction mtInstruction = new MtInstruction();
//        mtInstruction.setInstructionId(materialQueryVO.getInstructionId());
//        mtInstruction = mtInstructionRepository.selectByPrimaryKey(mtInstruction);


        //????????????????????????????????????
        /*if("SCANNED".equals(mtExtendAttrVOS.get(0).getAttrValue())){
            throw new MtException("WMS_OUT_SOURCE_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_OUT_SOURCE_0006", "WMS", materialQueryVO.getMaterialLotCode()));
        }*/

        //mtInstructions
        //????????????????????????????????????
        MtExtendVO mtExtendVO1 = new MtExtendVO();
        mtExtendVO1.setKeyId(mtMaterialLot.getMaterialLotId());
        mtExtendVO1.setAttrName("MATERIAL_VERSION");
        mtExtendVO1.setTableName("mt_material_lot_attr");
        List<MtExtendAttrVO> mtExtendAttrVOS1 = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO1);
        //??????vii.????????????????????????,??????????????????????????????
        MtMaterialLot finalMtMaterialLot = mtMaterialLot;

        //?????????????????????????????????????????????
        MtInstruction mtInstruction = new MtInstruction();

        boolean materialFlag = false;
        for (MtInstruction item : mtInstructions) {

            MtExtendVO mtExtendVO2 = new MtExtendVO();
            mtExtendVO2.setKeyId(item.getInstructionId());
            mtExtendVO2.setAttrName("MATERIAL_VERSION");
            mtExtendVO2.setTableName("mt_instruction_attr");
            List<MtExtendAttrVO> mtExtendAttrVOS2 = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO2);

            //??????????????????????????????
            if (CollectionUtils.isEmpty(mtExtendAttrVOS1) || StringUtils.isEmpty(mtExtendAttrVOS1.get(0).getAttrValue())) {
                if (CollectionUtils.isEmpty(mtExtendAttrVOS2) || StringUtils.isEmpty(mtExtendAttrVOS2.get(0).getAttrValue())) {
                    if (item.getMaterialId().equals(finalMtMaterialLot.getMaterialId())) {
                        materialFlag = true;
                        mtInstruction = item;
                    }
                }
            }
            //?????????????????????????????????
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS2) && CollectionUtils.isNotEmpty(mtExtendAttrVOS1)) {
                if (item.getMaterialId().equals(finalMtMaterialLot.getMaterialId())
                        && mtExtendAttrVOS2.get(0).getAttrValue().equals(mtExtendAttrVOS1.get(0).getAttrValue())) {
                    materialFlag = true;
                    mtInstruction = item;
                }
            }
            if (!materialFlag) {
                //????????????+???????????????????????????????????????
                continue;
            }
            // add by jiangling.zheng 20200907
            MtInstructionActual mtInstructionActual = new MtInstructionActual();
            mtInstructionActual.setInstructionId(item.getInstructionId());
            mtInstructionActual.setBusinessType(item.getBusinessType());
            mtInstructionActual.setInstructionType(item.getInstructionType());
            mtInstructionActual.setMaterialId(item.getMaterialId());
            List<MtInstructionActual> mtInstructionActuals = mtInstructionActualRepository.select(mtInstructionActual);
            //????????????????????????
            mtInstructionActuals.forEach(actual -> {
                MtInstructionActualDetail actualDetail = new MtInstructionActualDetail();
                actualDetail.setActualId(actual.getActualId());
                actualDetail.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                int count = mtInstructionActualDetailRepository.selectCount(actualDetail);
                if (count > 0) {
                    throw new MtException("MT_INVENTORY_0063",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0063",
                                    "INVENTORY"));
                }
            });
            if (materialFlag && CollectionUtils.isNotEmpty(mtInstructionActuals)) {
                mtInstructionActualList.add(mtInstructionActuals.get(0));
            }
            if (materialFlag) {
                //????????????+??????????????????????????????
                break;
            }
            // end add
        }
        //????????????????????????????????????
        if ("SCANNED".equals(mtExtendAttrVOS.get(0).getAttrValue())) {
            throw new MtException("WMS_OUT_SOURCE_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_OUT_SOURCE_0006", "WMS", materialQueryVO.getMaterialLotCode()));
        }
        //???????????????????????????????????????????????? ?????????
        if (!materialFlag) {
            throw new MtException("MT_INVENTORY_0065",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0065",
                            "INVENTORY"));
        }

        //??????????????????mt_material_lot.PRIMARY_UOM_ID????????????mt_instruction.UOM_ID????????????
        if (!StringUtils.equals(mtInstruction.getUomId(), mtMaterialLot.getPrimaryUomId())) {
            throw new MtException("WMS_DISTRIBUTION_0004",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_DISTRIBUTION_0004",
                            WmsConstant.ConstantValue.WMS, materialQueryVO.getMaterialLotCode()));
        }


        // add by jiangling.zheng

        // ?????????????????????
        if (!StringUtils.equals(WmsConstant.InstructionStatus.RELEASED, mtInstruction.getInstructionStatus())) {
            throw new MtException("WMS_OUT_SOURCE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_OUT_SOURCE_0004", WmsConstant.ConstantValue.WMS, materialQueryVO.getMaterialLotCode(), mtInstruction.getInstructionNum()));
        }
        //2020-10-20 21:30 add by chaonan.hu for yiwei.zhou
        //??????????????????????????????????????????
        if (StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_INVOICE, mtInstructionDoc.getInstructionDocType())) {
            if (StringUtils.isNotBlank(mtMaterialLot.getLocatorId())) {
                //????????????LOCATOR_ID???PARENT_LOCATOR_ID????????????mt_instruction.FROM_LOCATOR_ID??????????????????
                MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(mtMaterialLot.getLocatorId());
                if (!mtModLocator.getParentLocatorId().equals(mtInstruction.getFromLocatorId())) {
                    String instructionLineNum = "";
                    MtExtendVO mtExtendVO2 = new MtExtendVO();
                    mtExtendVO2.setKeyId(mtInstruction.getInstructionId());
                    mtExtendVO2.setAttrName("INSTRUCTION_LINE_NUM");
                    mtExtendVO2.setTableName("mt_instruction_attr");
                    List<MtExtendAttrVO> mtExtendAttrVOS2 = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO2);
                    if (CollectionUtils.isNotEmpty(mtExtendAttrVOS2) && StringUtils.isNotBlank(mtExtendAttrVOS2.get(0).getAttrValue())) {
                        instructionLineNum = mtExtendAttrVOS2.get(0).getAttrValue();
                    }
                    throw new MtException("WMS_OUTSOURCE_EXECUTE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_OUTSOURCE_EXECUTE_0004", WmsConstant.ConstantValue.WMS, mtMaterialLot.getMaterialLotCode(), instructionLineNum));
                }
            } else {
                String instructionLineNum = "";
                MtExtendVO mtExtendVO2 = new MtExtendVO();
                mtExtendVO2.setKeyId(mtInstruction.getInstructionId());
                mtExtendVO2.setAttrName("INSTRUCTION_LINE_NUM");
                mtExtendVO2.setTableName("mt_instruction_attr");
                List<MtExtendAttrVO> mtExtendAttrVOS2 = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO2);
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS2) && StringUtils.isNotBlank(mtExtendAttrVOS2.get(0).getAttrValue())) {
                    instructionLineNum = mtExtendAttrVOS2.get(0).getAttrValue();
                }
                throw new MtException("WMS_OUTSOURCE_EXECUTE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_OUTSOURCE_EXECUTE_0004", WmsConstant.ConstantValue.WMS, mtMaterialLot.getMaterialLotCode(), instructionLineNum));
            }
        }
        String eventTypeCode = StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_INVOICE, mtInstructionDoc.getInstructionDocType()) ?
                "OUTSOURCING_SENDING_BARCODE_SCAN" : "OUTSOURCING_RETURNED_BARCODE_SCAN";
        //end add
        //????????????????????????
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode(eventTypeCode);
        String barcodeScanEventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);

        //????????????
        //????????????????????????????????????????????????????????????????????????
        Double actualQty = (CollectionUtils.isEmpty(mtInstructionActualList) || ObjectUtil.isNull(mtInstructionActualList.get(0).getActualQty()))
                ? 0D : mtInstructionActualList.get(0).getActualQty();
        // actual_qty +???????????????quantity
        Double totalQty = actualQty + finalMtMaterialLot.getPrimaryUomQty();
        //??????????????????????????????, ??????????????????????????????????????????????????????
        // add by jiangling.zheng
        BigDecimal quantity;
        if (StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_INVOICE, mtInstructionDoc.getInstructionDocType())) {
            MtExtendVO mtExtend = new MtExtendVO();
            mtExtend.setTableName("mt_instruction_attr");
            mtExtend.setKeyId(mtInstruction.getInstructionId());
            mtExtend.setAttrName("ACTUAL_ORDERED_QTY");
            List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtend);
            if (CollectionUtils.isEmpty(mtExtendAttrVOList)) {
                quantity = BigDecimal.ZERO;
            } else {
                quantity = StringUtils.isNotEmpty(mtExtendAttrVOList.get(0).getAttrValue()) ? new BigDecimal(mtExtendAttrVOList.get(0).getAttrValue()) : BigDecimal.ZERO;
            }
        } else {
            quantity = BigDecimal.valueOf(mtInstruction.getQuantity());
        }
        //2020-10-20 22:11 add by chaonan.hu for yiwei.zhou
        //?????????????????????mt_instruction_doc_attr. SUPPLY_FLAG=Y?????????????????????
        if (StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_INVOICE, mtInstructionDoc.getInstructionDocType())) {
            if (CollectionUtils.isNotEmpty(supplyFlagList) && "Y".equals(supplyFlagList.get(0).getAttrValue())) {
                if (BigDecimal.valueOf(totalQty).compareTo(quantity) > 0) {
                    //2020-10-29 16:37 edit by chaonan.hu for yiwei.zhou ????????????Y???????????????????????????????????????????????????
                    List<WmsOutSourceMaterialReturnVO> wmsOutSourceMaterialReturnVOS = wmsOutSourceMapper.selectMaterialLotData(tenantId, materialQueryVO.getMaterialLotCode());
                    WmsOutSourceMaterialReturnVO result = wmsOutSourceMaterialReturnVOS.get(0);
                    result.setPopWindowFlag("1");
                    return result;
                }
            }else{
                if (BigDecimal.valueOf(totalQty).compareTo(quantity) > 0) {
                    ///??????????????????true??????????????????????????????????????????????????????
                    Boolean cacheFlag = false;
                    if(materialQueryVO.getCacheFlag()!= null){
                        cacheFlag = materialQueryVO.getCacheFlag();
                    }
                    if(!cacheFlag){
                        List<WmsOutSourceMaterialReturnVO> wmsOutSourceMaterialReturnVOS = wmsOutSourceMapper.selectMaterialLotData(tenantId, materialQueryVO.getMaterialLotCode());
                        WmsOutSourceMaterialReturnVO result = wmsOutSourceMaterialReturnVOS.get(0);
                        result.setPopWindowFlag("2");
                        return result;
                    }
                }
            }
        }
        //???????????????????????????
        MtInstructionActualVO mtInstructionActualVO = new MtInstructionActualVO();
        if (CollectionUtils.isNotEmpty(mtInstructionActualList)) {
            mtInstructionActualVO.setActualId(mtInstructionActualList.get(0).getActualId());
        }
        mtInstructionActualVO.setInstructionId(mtInstruction.getInstructionId());
        mtInstructionActualVO.setInstructionType(mtInstruction.getInstructionType());
        mtInstructionActualVO.setBusinessType(mtInstruction.getBusinessType());
        mtInstructionActualVO.setMaterialId(mtInstruction.getMaterialId());
        mtInstructionActualVO.setSupplierId(mtInstruction.getSupplierId());
        mtInstructionActualVO.setSupplierSiteId(mtInstruction.getSupplierSiteId());
        mtInstructionActualVO.setActualQty(finalMtMaterialLot.getPrimaryUomQty());
        mtInstructionActualVO.setEventId(barcodeScanEventId);
        mtInstructionActualVO.setUomId(mtInstruction.getUomId());
        mtInstructionActualVO.setFromSiteId(mtInstruction.getFromSiteId());
        mtInstructionActualVO.setToSiteId(mtInstruction.getToSiteId());
        mtInstructionActualVO.setFromLocatorId(mtInstruction.getFromLocatorId());
        mtInstructionActualVO.setToLocatorId(mtInstruction.getToLocatorId());
        MtInstructionActualVO1 mtInstructionActualVO1 = mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionActualVO);
        // ??????
        if (StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_INVOICE, mtInstructionDoc.getInstructionDocType()) &&
                BigDecimal.valueOf(totalQty).compareTo(quantity) > -1) {
            MtInstruction mtInstruction2 = new MtInstruction();
            mtInstruction2.setInstructionStatus(WmsConstant.InstructionStatus.COMPLETED);
            mtInstruction2.setInstructionId(mtInstruction.getInstructionId());
            mtInstructionRepository.updateByPrimaryKeySelective(mtInstruction2);
        }
        // ??????
        if (StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_RETURN, mtInstructionDoc.getInstructionDocType())) {
            if (BigDecimal.valueOf(totalQty).compareTo(quantity) > 0) {
                throw new MtException("WMS_OUT_SOURCE_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_OUT_SOURCE_0007", WmsConstant.ConstantValue.WMS));
            } else if (BigDecimal.valueOf(totalQty).compareTo(quantity) == 0) {
                MtInstruction mtInstruction2 = new MtInstruction();
                mtInstruction2.setInstructionStatus(WmsConstant.InstructionStatus.COMPLETED);
                mtInstruction2.setInstructionId(mtInstruction.getInstructionId());
                mtInstructionRepository.updateByPrimaryKeySelective(mtInstruction2);
            }
        }
        // ???????????????????????? OLD_STATUS????????????????????????????????????
        List<MtExtendVO5> mtExtendVOList = new ArrayList<>();
        MtExtendVO5 mtExtend = new MtExtendVO5();
        mtExtend.setAttrName(WmsConstant.ExtendAttr.OLD_STATUS);
        mtExtend.setAttrValue(mtExtendAttrVOS.get(0).getAttrValue());
        mtExtendVOList.add(mtExtend);
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", finalMtMaterialLot.getMaterialLotId(), barcodeScanEventId, mtExtendVOList);
        // end add
        //?????????????????????SCANNED
        List<MtExtendVO5> mtExtendVO5s = new ArrayList<>();
        MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName(WmsConstant.ExtendAttr.STATUS);
        mtExtendVO5.setAttrValue("SCANNED");
        mtExtendVO5s.add(mtExtendVO5);
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", finalMtMaterialLot.getMaterialLotId(), barcodeScanEventId, mtExtendVO5s);

        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        mtMaterialLotVO2.setMaterialLotId(finalMtMaterialLot.getMaterialLotId());
        mtMaterialLotVO2.setEventId(barcodeScanEventId);
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, MtBaseConstants.NO);

        MtInstructionActualDetail instructionActualDetail = new MtInstructionActualDetail();
        instructionActualDetail.setActualId(mtInstructionActualVO1.getActualId());
        instructionActualDetail.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        instructionActualDetail.setUomId(mtMaterialLot.getPrimaryUomId());
        instructionActualDetail.setActualQty(mtMaterialLot.getPrimaryUomQty());
        mtInstructionActualDetailRepository.instructionActualDetailCreate(tenantId, instructionActualDetail);

        // ?????????????????????????????????????????????????????????
        List<WmsOutSourceMaterialReturnVO> wmsOutSourceMaterialReturnVOS = wmsOutSourceMapper.selectMaterialLotData(tenantId, materialQueryVO.getMaterialLotCode());
        //??????????????????????????????????????????????????????
        //2020-10-07 19:12 edit by chaonan.hu for yiwei.zhou ????????????????????????????????????????????????????????????????????????
        if ((StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_INVOICE, mtInstructionDoc.getInstructionDocType()) &&
                BigDecimal.valueOf(totalQty).compareTo(quantity) > -1) ||
                (StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_RETURN, mtInstructionDoc.getInstructionDocType()) &&
                        BigDecimal.valueOf(totalQty).compareTo(quantity) == 0)) {
            wmsOutSourceMaterialReturnVOS.get(0).setInstructionStatus(WmsConstant.InstructionStatus.COMPLETED);
        }
        WmsOutSourceMaterialReturnVO wmsOutSourceMaterialReturnVO = wmsOutSourceMaterialReturnVOS.get(0);
        //2020-10-21 11:18 add by chaonan.hu for yiwei.zhou ????????????????????????
        MtModLocator mtModLocator = getMaterialLocator(tenantId, mtInstructionDoc.getSiteId(),
                mtInstruction.getMaterialId(), mtInstruction.getFromLocatorId(), mtInstruction.getInstructionId());
        if (mtModLocator != null) {
            wmsOutSourceMaterialReturnVO.setGetMaterialLocatorId(mtModLocator.getLocatorId());
            wmsOutSourceMaterialReturnVO.setGetMaterialLocatorCode(mtModLocator.getLocatorCode());
        }
        wmsOutSourceMaterialReturnVO.setPopWindowFlag("3");
        MtInstruction instruction = mtInstruction;
        List<WmsOutSourceLineVO> wmsOutSourceLineVOS = materialQueryVO.getDocLineList().stream().filter(item ->item.getInstructionId().equals(instruction.getInstructionId())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(wmsOutSourceLineVOS)){
            List<ItfLightTaskIfaceDTO> itfLightTaskIfaceDTOS = new ArrayList<>();
            if(StringUtils.isNotBlank(wmsOutSourceLineVOS.get(0).getTaskNum())){
                ItfLightTaskIfaceDTO itfLightTaskIfaceDTO = new ItfLightTaskIfaceDTO();
                itfLightTaskIfaceDTO.setTaskNum(wmsOutSourceLineVOS.get(0).getTaskNum());
                itfLightTaskIfaceDTO.setTaskStatus("OFF");
                itfLightTaskIfaceDTOS.add(itfLightTaskIfaceDTO);
            }
            if(CollectionUtils.isNotEmpty(itfLightTaskIfaceDTOS)){
                List<ItfLightTaskIfaceVO> itfLightTaskIfaceVOS = itfLightTaskIfaceService.itfLightTaskIface(tenantId,itfLightTaskIfaceDTOS);
                if(CollectionUtils.isNotEmpty(itfLightTaskIfaceVOS)){
                    List<String> taskNumList = itfLightTaskIfaceVOS.stream().map(ItfLightTaskIfaceVO::getTaskNum).collect(toList());
                    List<ItfLightTaskIface> itfLightTaskIfaceList = itfLightTaskIfaceMapper.selectByTaskNum(tenantId,taskNumList);
                    wmsOutSourceMaterialReturnVO.setStatus(itfLightTaskIfaceList.get(0).getStatus());
                    wmsOutSourceMaterialReturnVO.setTaskNum(null);
                    wmsOutSourceMaterialReturnVO.setTaskStatus("OFF");
                    wmsOutSourceMaterialReturnVO.setInstructionId(itfLightTaskIfaceList.get(0).getDocLineId());
                }
            }
        }
        return wmsOutSourceMaterialReturnVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WmsOutSourceSendDTO send(Long tenantId, WmsOutSourceSendDTO sendDto) {
        // ?????????????????????
        MtInstructionDoc doc = mtInstructionDocRepository.selectByPrimaryKey(sendDto.getInstructionDocId());
        //2020-10-20 22:34 edit by chaonan.hu for yiwei.zhou ?????????????????????????????????COMPLETED??????????????????SOURCE_INSTRUCTION_ID???????????????
//        //?????????????????????????????????COMPLETED??????
        Condition condition = StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_INVOICE, doc.getInstructionDocType()) ?
                Condition.builder(MtInstruction.class).andWhere(Sqls.custom()
                                .andEqualTo(MtInstruction.FIELD_SOURCE_DOC_ID, sendDto.getInstructionDocId())
//                        .andIsNotNull(MtInstruction.FIELD_SOURCE_INSTRUCTION_ID)
//                        .andNotEqualTo(MtInstruction.FIELD_SOURCE_INSTRUCTION_ID, "")
                ).build() :
                Condition.builder(MtInstruction.class).andWhere(Sqls.custom()
                        .andEqualTo(MtInstruction.FIELD_SOURCE_DOC_ID, sendDto.getInstructionDocId())).build();
        List<MtInstruction> mtInstructions = mtInstructionRepository.selectByCondition(condition);
//        boolean present = mtInstructions.stream().anyMatch(item -> !item.getInstructionStatus().equals(WmsConstant.InstructionStatus.COMPLETED));
//        if (present) {
//            throw new MtException("MT_INVENTORY_0067",
//                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0067",
//                            "INVENTORY"));
//        }

        //??????????????????
        String eventRequestType = StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_INVOICE, doc.getInstructionDocType()) ?
                WmsConstant.EventType.OUTSOURCING_SENDING : WmsConstant.EventType.OUTSOURCING_RETURNED;
        String outsorceSendEvenRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, eventRequestType);
        //????????????
        String eventType = StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_INVOICE, doc.getInstructionDocType()) ?
                WmsConstant.EventType.OUTSOURCING_SUPPLIER_RECEIVE : WmsConstant.EventType.OUTSOURCING_SUPPLIER_DELIVERY;
        //????????????
        String eventSuppType = StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_INVOICE, doc.getInstructionDocType()) ?
                WmsConstant.EventType.RETURN_TO_SUPPLIER : WmsConstant.EventType.RECEIVE_FROM_SUPPLIER;
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode(eventType);
        mtEventCreateVO.setEventRequestId(outsorceSendEvenRequestId);
        String receiveEventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);

        //?????????????????????
        //2020-10-20 22:50 edit by chaonan.hu for yiwei.zhou ???????????????????????????COMPLETED??????????????????????????????
        boolean present = mtInstructions.stream().anyMatch(item -> !item.getInstructionStatus().equals(WmsConstant.InstructionStatus.COMPLETED));
        if (!present) {
            MtInstructionDocDTO2 mtInstructionDocDTO2 = new MtInstructionDocDTO2();
            mtInstructionDocDTO2.setInstructionDocId(sendDto.getInstructionDocId());
            mtInstructionDocDTO2.setInstructionDocStatus(WmsConstant.InstructionStatus.COMPLETED);
            mtInstructionDocDTO2.setEventId(receiveEventId);
            mtInstructionDocRepository.instructionDocUpdate(tenantId, mtInstructionDocDTO2, MtBaseConstants.NO);
        }
        //????????????????????????????????????  ?????????  LOCATOR_TYPE = 20 ???????????????   ??? ??????  LOCATOR_CATEGORY = INVENTORY   ??? locatorid
        List<String> locatorIdList = wmsOutSourceMapper.selectLocator(tenantId, mtInstructions.get(0).getSiteId());
        if (locatorIdList.size() != 1) {
            throw new MtException("MT_INVENTORY_0073",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0073",
                            "INVENTORY"));
        }
        //????????????
        String locId = locatorIdList.get(0);
        String materialLotStatus = StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_INVOICE, doc.getInstructionDocType()) ?
                WmsConstant.MaterialLotStatus.SHIPPED : WmsConstant.MaterialLotStatus.INSTOCK;
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        WmsObjectTransactionRequestVO batchTrxVO;
        WmsObjectTransactionRequestVO trxVO;

        // ??????????????????
        String transactionTypeCode = WmsConstant.TransactionTypeCode.WMS_WAREHOUSE_TRAN;
        String sendTrxTypeCode = WmsConstant.TransactionTypeCode.WMS_OUTSOURCING_S;
        String returnTrxTypeCode = WmsConstant.TransactionTypeCode.WMS_OUTSOURCING_R;
        WmsTransactionType type = wmsTransactionTypeRepository.selectOne(new WmsTransactionType() {{
            setTenantId(tenantId);
            setTransactionTypeCode(transactionTypeCode);
        }});
        // ??????
        WmsTransactionType sendType = wmsTransactionTypeRepository.selectOne(new WmsTransactionType() {{
            setTenantId(tenantId);
            setTransactionTypeCode(sendTrxTypeCode);
        }});
        // ??????
        WmsTransactionType returnType = wmsTransactionTypeRepository.selectOne(new WmsTransactionType() {{
            setTenantId(tenantId);
            setTransactionTypeCode(returnTrxTypeCode);
        }});
        //??????????????????????????????????????????????????????????????????false,?????????
        boolean scanFlag = false;
        // end add
        for (MtInstruction item : mtInstructions) {
            Double actualQty = 0D;
            //???????????????????????????
            MtInstructionActual mtInstructionActual = new MtInstructionActual();
            mtInstructionActual.setInstructionId(item.getInstructionId());
            mtInstructionActual.setBusinessType(item.getBusinessType());
            mtInstructionActual.setInstructionType(item.getInstructionType());
            mtInstructionActual.setMaterialId(item.getMaterialId());
            List<MtInstructionActual> mtInstructionActuals = mtInstructionActualRepository.select(mtInstructionActual);
            if (CollectionUtils.isNotEmpty(mtInstructionActuals)) {
                actualQty = mtInstructionActuals.get(0).getActualQty();
            } else {
                continue;
            }

            List<String> actualIds = mtInstructionActuals.stream().map(MtInstructionActual::getActualId).collect(toList());
            List<MtInstructionActualDetail> actualDetails =
                    mtInstructionActualDetailRepository.instructionActualLimitDetailBatchQuery(tenantId, actualIds);
            //2020-10-07 10:20 edit by chaonan.hu for yiwei.zhou
            if (actualQty == 0) {
                //???actualQty=0???
                if (CollectionUtils.isNotEmpty(actualDetails)) {
                    //????????????????????????????????????{??????????????????????????????????????????,?????????!}
                    throw new MtException("WMS_OUTSOURCE_EXECUTE_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_OUTSOURCE_EXECUTE_0002", WmsConstant.ConstantValue.WMS));
                } else {
                    //??????????????????????????????????????????????????????MtInstruction?????????????????????
                    continue;
                }
            } else {
                //???actualQty!=0???
                if (CollectionUtils.isEmpty(actualDetails)) {
                    //?????????????????????????????????{??????????????????????????????????????????,?????????!}
                    throw new MtException("WMS_OUT_SOURCE_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_OUT_SOURCE_0003", WmsConstant.ConstantValue.WMS));
                }
                //??????????????????????????????????????????
            }
            // end modify
            // add by jiangling.zheng ??????????????????
            //?????????????????????????????????
            String locatorId = null;
            if (StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_RETURN, doc.getInstructionDocType())) {
                MtModLocatorVO9 mtModLocatorVO9 = new MtModLocatorVO9();
                mtModLocatorVO9.setLocatorId(item.getToLocatorId());
                mtModLocatorVO9.setQueryType("FIRST");
                mtModLocatorVO9.setLocatorCategory("INVENTORY");
                mtModLocatorVO9.setLocatorType("RECEIVE_PENDING");
                List<String> locatorIds = mtModLocatorRepository.subLocatorQuery(tenantId, mtModLocatorVO9);
                if (locatorIds.size() != 1) {
                    throw new MtException("WMS_OUT_SOURCE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_OUT_SOURCE_0001", WmsConstant.ConstantValue.WMS));
                }
                locatorId = locatorIds.get(0);
            }
            List<MtMaterialLotVO16> mtMaterialLotSequenceList = new ArrayList<>();
            //changeQuantity ??????API{ onhandQtyUpdateProcess }?????????????????????
            Double changeQuantity = 0D;
            //superHairInstructionFlag ???????????????????????????????????????????????????
            Boolean superHairInstructionFlag = false;
            for (MtInstructionActualDetail detail : actualDetails) {
                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(detail.getMaterialLotId());
                // ????????????????????????
                //2020-10-20 23:17 edit by chaonan.hu for yiwei.zhou ????????????????????????SCANNED?????????????????????????????????
                MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
                mtMaterialLotAttrVO2.setMaterialLotId(detail.getMaterialLotId());
                mtMaterialLotAttrVO2.setAttrName("STATUS");
                List<MtExtendAttrVO> mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && "SCANNED".equals(mtExtendAttrVOS.get(0).getAttrValue())) {
                    scanFlag = true;
                    if (StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_RETURN, doc.getInstructionDocType())) {
                        // ????????????
//                        MtNumrangeVO5 mtNumrangeVO = mtNumrangeRepository.numrangeGenerate(tenantId, new MtNumrangeVO2() {{
//                            setObjectCode("RECEIPT_BATCH");
//                        }});
                        // ?????????????????????
                        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                        mtMaterialLotVO2.setMaterialLotId(detail.getMaterialLotId());
                        //2020-11-11 10:16 edit by chaonan.hu for yiwei.zhou ????????????????????????????????????????????????????????????
                        mtMaterialLotVO2.setLot("20100101");
                        mtMaterialLotVO2.setEventId(receiveEventId);
                        mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");
                        // ??????????????????
                        MtMaterialLotVO23 dto = new MtMaterialLotVO23();
                        dto.setMaterialLotId(detail.getMaterialLotId());
                        dto.setPrimaryUomId(detail.getUomId());
                        dto.setPrimaryUomQty(detail.getActualQty());
                        dto.setLocatorId(locatorId);
                        dto.setEventRequestId(outsorceSendEvenRequestId);
                        mtMaterialLotRepository.materialLotInitialize(tenantId, dto);
                    }
                    changeQuantity += mtMaterialLot.getPrimaryUomQty();
                    superHairInstructionFlag = true;
                    //????????????????????????
                    MtMaterialLotVO16 mtMaterialLotVO16 = new MtMaterialLotVO16();
                    mtMaterialLotVO16.setMaterialLotId(detail.getMaterialLotId());
                    mtMaterialLotSequenceList.add(mtMaterialLotVO16);
                    MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
                    mtExtendVO10.setKeyId(detail.getMaterialLotId());
                    mtExtendVO10.setEventId(receiveEventId);
                    MtExtendVO5 mtExtendVO51 = new MtExtendVO5();
                    mtExtendVO51.setAttrName("STATUS");
                    mtExtendVO51.setAttrValue(materialLotStatus);
                    mtExtendVO10.setAttrs(Collections.singletonList(mtExtendVO51));
                    mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);
                    //???????????????????????????????????????????????????
                    mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(detail.getMaterialLotId());
                    // add by jiangling.zheng
                    MtUom myUom = mtUomRepository.selectByPrimaryKey(mtMaterialLot.getPrimaryUomId());
                    MtModLocator locator = mtModLocatorRepository.selectByPrimaryKey(mtMaterialLot.getLocatorId());
                    // ?????????????????????
                    String supplierCode = Strings.isNotEmpty(doc.getSupplierId()) ?
                            mtSupplierRepository.selectByPrimaryKey(doc.getSupplierId()).getSupplierCode() : null;
                    String supplierSiteCode = Strings.isNotEmpty(doc.getSupplierSiteId()) ?
                            mtSupplierSiteRepository.selectByPrimaryKey(doc.getSupplierSiteId()).getSupplierSiteCode() : null;
                    trxVO = new WmsObjectTransactionRequestVO();
                    trxVO.setTransactionId(null);
                    trxVO.setEventId(receiveEventId);
                    trxVO.setBarcode(mtMaterialLot.getMaterialLotCode());
                    trxVO.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                    trxVO.setMaterialId(mtMaterialLot.getMaterialId());
                    trxVO.setTransactionQty(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()));
                    trxVO.setLotNumber("20100101");
                    trxVO.setTransactionUom(myUom.getUomCode());
                    trxVO.setTransactionTime(new Date());
                    trxVO.setSourceDocId(doc.getInstructionDocId());
                    trxVO.setSourceDocLineNum(item.getInstructionNum());
                    trxVO.setSourceDocLineId(item.getInstructionId());
                    trxVO.setSourceDocNum(doc.getInstructionDocNum());
                    trxVO.setSourceDocType(doc.getInstructionDocType());
                    trxVO.setMergeFlag(WmsConstant.ConstantValue.NO);
                    trxVO.setPlantId(mtMaterialLot.getSiteId());
                    trxVO.setWarehouseId(locator.getParentLocatorId());
                    trxVO.setLocatorId(mtMaterialLot.getLocatorId());
                    trxVO.setSupplierCode(supplierCode);
                    trxVO.setSupplierSiteCode(supplierSiteCode);
                    if (StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_INVOICE, doc.getInstructionDocType())) {
                        trxVO.setTransactionTypeCode(sendTrxTypeCode);
                        trxVO.setMoveType(sendType.getMoveType());
                        trxVO.setRemark("??????????????????");
                        trxVO.setTransactionReasonCode("????????????");
                    } else {
                        trxVO.setTransactionTypeCode(returnTrxTypeCode);
                        trxVO.setMoveType(returnType.getMoveType());
                        trxVO.setRemark("??????????????????");
                        trxVO.setTransactionReasonCode("????????????");
                    }
                    // ???????????????????????????
                    objectTransactionRequestList.add(trxVO);
                    batchTrxVO = new WmsObjectTransactionRequestVO();
                    BeanUtils.copyProperties(trxVO, batchTrxVO);
                    batchTrxVO.setTransactionTypeCode(transactionTypeCode);
                    batchTrxVO.setMoveType(type.getMoveType());
                    batchTrxVO.setTransferPlantId(mtMaterialLot.getSiteId());
                    batchTrxVO.setTransferWarehouseId(locator.getParentLocatorId());
                    batchTrxVO.setTransferLocatorId(mtMaterialLot.getLocatorId());
                    //2020-11-11 10:16 edit by chaonan.hu for yiwei.zhou ???????????????????????????????????????????????????
                    if (StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_INVOICE, doc.getInstructionDocType())) {
                        batchTrxVO.setLotNumber(mtMaterialLot.getLot());
                        batchTrxVO.setTransferLotNumber("20100101");
                        batchTrxVO.setRemark("????????????????????????");
                        batchTrxVO.setTransactionReasonCode("????????????????????????");
                        objectTransactionRequestList.add(batchTrxVO);
                    }
//                    else {
//                        batchTrxVO.setLotNumber("20100101");
//                        batchTrxVO.setTransferLotNumber(mtMaterialLot.getLot());
//                        batchTrxVO.setRemark("????????????????????????");
//                        batchTrxVO.setTransactionReasonCode("????????????????????????");
//                    }
                }
                // end add
            }
            //mtMaterialLotSequenceList???????????????????????????????????????????????????????????????
            if (CollectionUtils.isEmpty(mtMaterialLotSequenceList)) {
                continue;
            }
            // end add
            // modify by jiangling.zheng desc ??????????????????????????????
            if (StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_INVOICE, doc.getInstructionDocType())) {
                //2020-10-21 10:02 edit by chaonan.hu for yiwei.zhou ????????????????????????????????????Id???????????????????????????
                MtMaterialLotVO15 mtMaterialLotVO15 = new MtMaterialLotVO15();
                mtMaterialLotVO15.setMtMaterialLotSequenceList(mtMaterialLotSequenceList);
                mtMaterialLotVO15.setPrimaryUomId(item.getUomId());
                mtMaterialLotVO15.setTrxPrimaryUomQty(actualQty);
                mtMaterialLotVO15.setAllConsume(MtBaseConstants.YES);
                mtMaterialLotVO15.setInstructionDocId(sendDto.getInstructionDocId());
                mtMaterialLotVO15.setEventRequestId(outsorceSendEvenRequestId);
                mtMaterialLotRepository.sequenceLimitMaterialLotBatchConsume(tenantId, mtMaterialLotVO15);
            }
            // end modify
            MtInvOnhandQuantityVO9 onhandQuantityVO9 = new MtInvOnhandQuantityVO9();
            onhandQuantityVO9.setSiteId(item.getSiteId());
            onhandQuantityVO9.setMaterialId(item.getMaterialId());
            onhandQuantityVO9.setLocatorId(locId);
            onhandQuantityVO9.setLotCode("20100101");
            onhandQuantityVO9.setEventId(receiveEventId);
            onhandQuantityVO9.setOwnerType("IIS");
            onhandQuantityVO9.setOwnerId(item.getSupplierId());
            //2020-10-21 10:17 edit by chaonan.hu for yiwei.zhou
            //changeQuantity?????????actualQty??????actual_detail?????????????????????SCANNED?????????????????????
            onhandQuantityVO9.setChangeQuantity(changeQuantity);
            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, onhandQuantityVO9);

            //??????????????????
            //2020-10-21 10:40 edit by chaonan.hu for yiwei.zhou
            //??????????????????SUPPLY_FLAG??????Y,??????????????????????????????????????????????????????????????????????????????????????????
            //??????????????????????????????SUPPLY_FLAG
            MtExtendVO mtExtendSf = new MtExtendVO();
            mtExtendSf.setTableName("mt_instruction_doc_attr");
            mtExtendSf.setKeyId(sendDto.getInstructionDocId());
            mtExtendSf.setAttrName("SUPPLY_FLAG");
            List<MtExtendAttrVO> supplyFlagList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendSf);
            if ((CollectionUtils.isEmpty(supplyFlagList) || !"Y".equals(supplyFlagList.get(0).getAttrValue()))
                    && superHairInstructionFlag && WmsConstant.InstructionStatus.COMPLETED.equals(item.getInstructionStatus())) {
                //?????????????????????????????????
                // add by jiangling.zheng
                BigDecimal quantity;
                if (StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_INVOICE, doc.getInstructionDocType())) {
                    MtExtendVO mtExtend = new MtExtendVO();
                    mtExtend.setTableName("mt_instruction_attr");
                    mtExtend.setKeyId(item.getInstructionId());
                    mtExtend.setAttrName("ACTUAL_ORDERED_QTY");
                    List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtend);
                    if (CollectionUtils.isEmpty(mtExtendAttrVOList)) {
                        quantity = BigDecimal.ZERO;
                    } else {
                        quantity = StringUtils.isNotEmpty(mtExtendAttrVOList.get(0).getAttrValue()) ? new BigDecimal(mtExtendAttrVOList.get(0).getAttrValue()) : BigDecimal.ZERO;
                    }
                } else {
                    quantity = BigDecimal.valueOf(item.getQuantity());
                }
                BigDecimal coverQty = BigDecimal.valueOf(item.getQuantity()).subtract(quantity);
                // end add
                BigDecimal overQuantity = BigDecimal.valueOf(actualQty).subtract(quantity);
                //???????????????????????????
                MtInstructionDoc mtInstructionDoc = new MtInstructionDoc();
                mtInstructionDoc.setSupplierId(item.getSupplierId());
                mtInstructionDoc.setSiteId(item.getSiteId());
                mtInstructionDoc.setInstructionDocType(WmsConstant.InspectionDocType.OVER);
                List<MtInstructionDoc> mtInstructionDocs = mtInstructionDocRepository.select(mtInstructionDoc);
                //??????????????????????????????id???????????????????????????????????????????????????????????????
                String sourceDocId = null;
                MtInstruction overMtInstruction = null;
                String itemMaterialVersion = null;
                MtExtendVO mtExtend = new MtExtendVO();
                mtExtend.setTableName("mt_instruction_attr");
                mtExtend.setKeyId(item.getInstructionId());
                mtExtend.setAttrName("MATERIAL_VERSION");
                List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtend);
                if(CollectionUtils.isNotEmpty(mtExtendAttrVOList)){
                    itemMaterialVersion = mtExtendAttrVOList.get(0).getAttrValue();
                }
                if (CollectionUtils.isNotEmpty(mtInstructionDocs)) {
                    sourceDocId = mtInstructionDocs.get(0).getInstructionDocId();
                    //2020-10-28 10:04 edit by chaonan.hu for yiwei.zhou
                    //????????????SourceDocId+MaterialId+MaterialVersion????????????????????????????????????overMtInstruction
                    //????????????????????????????????????MATERIAL_VERSION
                    MtInstruction mtInstruction = new MtInstruction();
                    mtInstruction.setSourceDocId(mtInstructionDocs.get(0).getInstructionDocId());
                    mtInstruction.setMaterialId(item.getMaterialId());
                    List<MtInstruction> mtInstructions1 = mtInstructionRepository.select(mtInstruction);
                    for (MtInstruction overInstruction : mtInstructions1) {
                        //??????????????????????????????MATERIAL_VERSION
                        String overMaterialVersion = null;
                        mtExtend.setKeyId(overInstruction.getInstructionId());
                        List<MtExtendAttrVO> overExtendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtend);
                        if(CollectionUtils.isNotEmpty(overExtendAttrVOList)){
                            overMaterialVersion = overExtendAttrVOList.get(0).getAttrValue();
                        }
                        if((StringUtils.isEmpty(itemMaterialVersion) && StringUtils.isEmpty(overMaterialVersion)) ||
                            itemMaterialVersion.equals(overMaterialVersion)){
                            overMtInstruction = overInstruction;
                            break;
                        }

                    }
                } else {
                    MtInstructionDocDTO2 mtInstructionDocVO2 = new MtInstructionDocDTO2();
                    mtInstructionDocVO2.setSiteId(item.getSiteId());
                    mtInstructionDocVO2.setSupplierId(item.getSupplierId());
                    mtInstructionDocVO2.setSupplierSiteId(item.getSupplierSiteId());
                    mtInstructionDocVO2.setEventRequestId(outsorceSendEvenRequestId);
                    mtInstructionDocVO2.setInstructionDocType(WmsConstant.InspectionDocType.OVER);
                    MtInstructionDocVO3 mtInstructionDocVO3 = mtInstructionDocRepository.instructionDocUpdate(tenantId, mtInstructionDocVO2, MtBaseConstants.NO);
                    sourceDocId = mtInstructionDocVO3.getInstructionDocId();
                }

                MtInstructionVO mtInstructionVO = new MtInstructionVO();
                mtInstructionVO.setInstructionId(ObjectUtil.isNotNull(overMtInstruction) ? overMtInstruction.getInstructionId() : null);
                mtInstructionVO.setInstructionStatus(WmsConstant.InstructionStatus.RELEASED);
                MtEventCreateVO mtEventCreateVO2 = new MtEventCreateVO();
                mtEventCreateVO2.setEventTypeCode(eventSuppType);
                mtEventCreateVO2.setEventRequestId(outsorceSendEvenRequestId);
                String supplierEventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO2);
                if (StringUtils.isNotEmpty(mtInstructionVO.getInstructionId())) {
                    mtInstructionVO.setEventId(supplierEventId);
                    if (StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_INVOICE, doc.getInstructionDocType())) {
                        mtInstructionVO.setQuantity(overQuantity.add(BigDecimal.valueOf(overMtInstruction.getQuantity())).subtract(coverQty).doubleValue());
                        //2020-10-13 edit by chaonan.hu for yiwei.zhou ??????????????????COVER_QTY???NULL/????????????0?????????
                        if (Objects.isNull(overMtInstruction.getCoverQty())) {
                            mtInstructionVO.setCoverQty(BigDecimal.ZERO.subtract(coverQty).doubleValue());
                        } else {
                            mtInstructionVO.setCoverQty(BigDecimal.valueOf(overMtInstruction.getCoverQty()).subtract(coverQty).doubleValue());
                        }
                    } else {
                        Double overCoverQty = overMtInstruction.getCoverQty() == null ? 0D : overMtInstruction.getCoverQty();
                        Double diffValue = overMtInstruction.getQuantity() - overCoverQty;
                        Double overQty = diffValue.compareTo(actualQty) > 0 ? overMtInstruction.getQuantity() - actualQty : overCoverQty;
                        mtInstructionVO.setQuantity(overQty);
                    }
                } else {
                    mtInstructionVO.setInstructionType("RETURN_TO_SUPPLIER");
                    if (StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_INVOICE, doc.getInstructionDocType())) {
                        mtInstructionVO.setQuantity(overQuantity.doubleValue());
                    } else {
                        mtInstructionVO.setQuantity(0D);
                    }

                }
                mtInstructionVO.setSourceDocId(sourceDocId);
                mtInstructionVO.setSiteId(item.getSiteId());
                mtInstructionVO.setMaterialId(item.getMaterialId());
                mtInstructionVO.setUomId(item.getUomId());
                mtInstructionVO.setFromSiteId(item.getSiteId());
                mtInstructionVO.setFromLocatorId(locId);
                mtInstructionVO.setSupplierId(item.getSupplierId());
                mtInstructionVO.setSupplierSiteId(item.getSupplierSiteId());
                mtInstructionVO.setBusinessType("OUTSOURCING_SENDING");
                mtInstructionVO.setEventRequestId(outsorceSendEvenRequestId);
                MtInstructionVO6 mtInstructionVO6 = mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, MtBaseConstants.NO);
                //2020-10-28 10:48 add by chaonan.hu for yiwei.zhou ????????????????????????API{AttrPropertyUpdate}??????????????????
                if (StringUtils.isEmpty(mtInstructionVO.getInstructionId())) {
                    List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
                    MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                    mtExtendVO5.setAttrName("MATERIAL_VERSION");
                    mtExtendVO5.setAttrValue(StringUtils.isEmpty(itemMaterialVersion)?"":itemMaterialVersion);
                    mtExtendVO5List.add(mtExtendVO5);
                    mtExtendSettingsRepository.attrPropertyUpdate(tenantId,"mt_instruction_attr",
                            mtInstructionVO6.getInstructionId(), supplierEventId, mtExtendVO5List);
                }
            }

        }
        if(!scanFlag){
            throw new MtException("WMS_DISTRIBUTION_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_DISTRIBUTION_0013", WmsConstant.ConstantValue.WMS));
        }
        // ??????????????????????????????
        List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS = wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
        List<String> tranType = new ArrayList<>();
        if (StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_INVOICE, doc.getInstructionDocType())) {
            tranType.add("311");
            tranType.add("541");
        } else {
            tranType.add("542");
            tranType.add("311");
        }
        itfObjectTransactionIfaceService.sendSapMaterialMoveSort(tenantId, wmsObjectTransactionResponseVOS, tranType);
        return sendDto;
    }

    @Override
    @ProcessLovValue
    public List<WmsOsDetailVO> queryDetail(Long tenantId, String instructionId) {
        return wmsOutSourceMapper.selectDetail(tenantId, instructionId);
    }

    @Override
    @ProcessLovValue
    @Transactional(rollbackFor = Exception.class)
    public WmsOutSourceScanMaterialQueryVO returnMaterialLot(Long tenantId, WmsOutSourceScanMaterialQueryVO materialQueryVO) {

        //????????????????????????
        MtInstructionDoc mtInstructionDoc = new MtInstructionDoc();
        mtInstructionDoc.setInstructionDocId(materialQueryVO.getInstructionDocId());
        mtInstructionDoc = mtInstructionDocRepository.selectByPrimaryKey(mtInstructionDoc);

        if (WmsConstant.InstructionStatus.COMPLETED.equals(mtInstructionDoc.getInstructionDocStatus())) {
            throw new MtException("MT_INVENTORY_0068",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0068",
                            "INVENTORY"));
        }
        //????????????
        String eventTypeCode = StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_INVOICE, mtInstructionDoc.getInstructionDocType()) ?
                "OUTSOURCING_SENDING_BARCODE_SCAN_CANCEL" : "OUTSOURCING_RETURNED_BARCODE_SCAN_CANCEL";
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode(eventTypeCode);
        String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
        //?????????????????????????????????????????????
        MtInstruction mtInstruction = mtInstructionRepository.selectByPrimaryKey(materialQueryVO.getInstructionId());
        for (String materialLotId :
                materialQueryVO.getMaterialLotIds()) {
            /*//????????????????????????
            MtInstructionDoc mtInstructionDoc = new MtInstructionDoc();
            mtInstructionDoc.setInstructionDocId(materialQueryVO.getInstructionDocId());
            mtInstructionDoc = mtInstructionDocRepository.selectByPrimaryKey(mtInstructionDoc);

            if (WmsConstant.InstructionStatus.COMPLETED.equals(mtInstructionDoc.getInstructionDocStatus())) {
                throw new MtException("MT_INVENTORY_0068",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0068",
                                "INVENTORY"));
            }
            //????????????
            String eventTypeCode = StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_INVOICE, mtInstructionDoc.getInstructionDocType()) ?
                    "OUTSOURCING_SENDING_BARCODE_SCAN_CANCEL" : "OUTSOURCING_RETURNED_BARCODE_SCAN_CANCEL";
            MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
            mtEventCreateVO.setEventTypeCode(eventTypeCode);
            String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);*/

            //??????????????????
            MtMaterialLot mtMaterialLot = new MtMaterialLot();
            mtMaterialLot.setMaterialLotId(materialLotId);
            mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(mtMaterialLot);
            // modify by jiangling.zheng 20200909 ??????????????????????????????????????????
            /*Condition condition = StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_INVOICE, mtInstructionDoc.getInstructionDocType()) ?
                    Condition.builder(MtInstruction.class).andWhere(Sqls.custom()
                            .andEqualTo(MtInstruction.FIELD_SOURCE_DOC_ID, mtInstructionDoc.getInstructionDocId())
                            .andIsNotNull(MtInstruction.FIELD_SOURCE_INSTRUCTION_ID)
                            .andNotEqualTo(MtInstruction.FIELD_SOURCE_INSTRUCTION_ID, "")).build() :
                    Condition.builder(MtInstruction.class).andWhere(Sqls.custom()
                            .andEqualTo(MtInstruction.FIELD_SOURCE_DOC_ID, mtInstructionDoc.getInstructionDocId())).build();
            List<MtInstruction> mtInstructions = mtInstructionRepository.selectByCondition(condition);*/

            MtExtendVO mtExtendVO1 = new MtExtendVO();
            mtExtendVO1.setKeyId(materialLotId);
            mtExtendVO1.setAttrName("MATERIAL_VERSION");
            mtExtendVO1.setTableName("mt_material_lot_attr");
            List<MtExtendAttrVO> mtExtendAttrVOS1 = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO1);
            //??????vii.????????????????????????,??????????????????????????????
            /*boolean materialFlag = false;
            for (MtInstruction item : mtInstructions) {
                MtExtendVO mtExtendVO2 = new MtExtendVO();
                mtExtendVO2.setKeyId(item.getInstructionId());
                mtExtendVO2.setAttrName("MATERIAL_VERSION");
                mtExtendVO2.setTableName("mt_instruction_attr");
                List<MtExtendAttrVO> mtExtendAttrVOS2 = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO2);

                //??????????????????????????????
                if (CollectionUtils.isEmpty(mtExtendAttrVOS1) || StringUtils.isEmpty(mtExtendAttrVOS1.get(0).getAttrValue())) {
                    if (CollectionUtils.isEmpty(mtExtendAttrVOS2) || StringUtils.isEmpty(mtExtendAttrVOS2.get(0).getAttrValue())) {
                        if (item.getMaterialId().equals(mtMaterialLot.getMaterialId())) {
                            materialFlag = true;
                            mtInstruction = item;
                        }
                    }
                }
                //?????????????????????????????????
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS2) && CollectionUtils.isNotEmpty(mtExtendAttrVOS1)) {
                    if (item.getMaterialId().equals(mtMaterialLot.getMaterialId())
                            && mtExtendAttrVOS2.get(0).getAttrValue().equals(mtExtendAttrVOS1.get(0).getAttrValue())) {
                        materialFlag = true;
                        mtInstruction = item;
                    }
                }
            }

            if (!materialFlag) {*/
            if (Objects.isNull(mtInstruction)) {
                throw new MtException("MT_INVENTORY_0065",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0065",
                                "INVENTORY"));
            }
            materialQueryVO.setInstructionStatus(mtInstruction.getInstructionStatus());
            // end modify
            MtInstructionActual mtInstructionActual = new MtInstructionActual();
            mtInstructionActual.setInstructionId(mtInstruction.getInstructionId());
            mtInstructionActual.setInstructionType(mtInstruction.getInstructionType());
            mtInstructionActual.setBusinessType(mtInstruction.getBusinessType());
            mtInstructionActual.setMaterialId(mtInstruction.getMaterialId());
            List<MtInstructionActual> mtInstructionActuals = mtInstructionActualRepository.select(mtInstructionActual);
            if (CollectionUtils.isEmpty(mtInstructionActuals)) {
                throw new MtException("MT_INVENTORY_0073",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0073",
                                "INVENTORY"));
            }
            MtInstructionActual updateActual = new MtInstructionActual();
            updateActual.setActualId(mtInstructionActuals.get(0).getActualId());
            updateActual.setActualQty(BigDecimal.valueOf(mtInstructionActuals.get(0).getActualQty()).subtract(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty())).doubleValue());
            mtInstructionActualRepository.updateByPrimaryKeySelective(updateActual);

            //????????????
            MtInstructionActualDetail mtInstructionActualDetail = new MtInstructionActualDetail();
            mtInstructionActualDetail.setTenantId(tenantId);
            mtInstructionActualDetail.setActualId(updateActual.getActualId());
            mtInstructionActualDetail.setMaterialLotId(materialLotId);
            MtInstructionActualDetail mtInstructionActualDetailDb = mtInstructionActualDetailRepository.selectOne(mtInstructionActualDetail);
            //2020-10-07 20:03 ????????????????????????????????????????????????????????????????????????????????????
            if (ObjectUtil.isEmpty(mtInstructionActualDetailDb)) {
                throw new MtException("WMS_OUTSOURCE_EXECUTE_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_OUTSOURCE_EXECUTE_0003", WmsConstant.ConstantValue.WMS, mtMaterialLot.getMaterialLotCode()));
            }
            mtInstructionActualDetailRepository.delete(mtInstructionActualDetail);
            // add by jiangling.zheng
            Double actualQty = (CollectionUtils.isEmpty(mtInstructionActuals) || ObjectUtil.isNull(mtInstructionActuals.get(0).getActualQty()))
                    ? 0D : mtInstructionActuals.get(0).getActualQty();
            Double totalQty = actualQty - mtMaterialLot.getPrimaryUomQty();
//            if (totalQty > 0) {
            BigDecimal quantity;
            if (StringUtils.equals(WmsConstant.InspectionDocType.OUTSOURCING_INVOICE, mtInstructionDoc.getInstructionDocType())) {
                // ??????
                MtExtendVO mtExtend = new MtExtendVO();
                mtExtend.setTableName("mt_instruction_attr");
                mtExtend.setKeyId(mtInstruction.getInstructionId());
                mtExtend.setAttrName("ACTUAL_ORDERED_QTY");
                List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtend);
                if (CollectionUtils.isEmpty(mtExtendAttrVOList)) {
                    quantity = BigDecimal.ZERO;
                } else {
                    quantity = StringUtils.isNotEmpty(mtExtendAttrVOList.get(0).getAttrValue()) ? new BigDecimal(mtExtendAttrVOList.get(0).getAttrValue()) : BigDecimal.ZERO;
                }
            } else {
                // ??????
                quantity = BigDecimal.valueOf(mtInstruction.getQuantity());
            }
            if (BigDecimal.valueOf(totalQty).compareTo(quantity) < 0) {
                MtInstruction mtInstruction2 = new MtInstruction();
                mtInstruction2.setInstructionId(mtInstruction.getInstructionId());
                mtInstruction2.setInstructionStatus(WmsConstant.InstructionStatus.RELEASED);
                mtInstructionRepository.updateByPrimaryKeySelective(mtInstruction2);
                materialQueryVO.setInstructionStatus(WmsConstant.InstructionStatus.RELEASED);
            }
            /*} else {
                mtInstruction2.setInstructionStatus(WmsConstant.InstructionStatus.NEW);
                mtInstructionRepository.updateByPrimaryKeySelective(mtInstruction2);
                materialQueryVO.setInstructionStatus(WmsConstant.InstructionStatus.NEW);
            }*/
            //end add
            // ????????????????????????
            MtExtendVO mtExtendVO = new MtExtendVO();
            mtExtendVO.setKeyId(mtMaterialLot.getMaterialLotId());
            mtExtendVO.setAttrName(WmsConstant.ExtendAttr.OLD_STATUS);
            mtExtendVO.setTableName("mt_material_lot_attr");
            List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            if (CollectionUtils.isEmpty(mtExtendAttrVOS)) {
                throw new MtException("WMS_OUT_SOURCE_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_OUT_SOURCE_0008", WmsConstant.ConstantValue.WMS));
            }
            // ??????????????????
            List<MtExtendVO5> mtExtendVO5s = new ArrayList<>();
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName(WmsConstant.ExtendAttr.STATUS);
            mtExtendVO5.setAttrValue(mtExtendAttrVOS.get(0).getAttrValue());
            mtExtendVO5s.add(mtExtendVO5);
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", materialLotId, eventId, mtExtendVO5s);
        }
        //2020-10-21 14:27 add by chaonan.hu for yiwei.zhou ????????????????????????
        MtModLocator mtModLocator = getMaterialLocator(tenantId, mtInstructionDoc.getSiteId(),
                mtInstruction.getMaterialId(), mtInstruction.getFromLocatorId(), mtInstruction.getInstructionId());
        if (mtModLocator != null) {
            materialQueryVO.setGetMaterialLocatorId(mtModLocator.getLocatorId());
            materialQueryVO.setGetMaterialLocatorCode(mtModLocator.getLocatorCode());
        }
        return materialQueryVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WmsOutSourceDTO materialLotSplit(Long tenantId, WmsOutSourceDTO dto) {
        //???????????????
        if (StringUtils.isEmpty(dto.getInstructionDocId())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "?????????Id"));
        }
        if (StringUtils.isEmpty(dto.getSourceMaterialLotCode())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "????????????"));
        }
        if (Objects.isNull(dto.getSplitQty())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "????????????"));
        }
        MtMaterialLot sourceMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getSourceMaterialLotCode());
        }});
        //???????????????????????????????????????????????????????????????????????????????????????????????????
        if(StringUtils.isNotBlank(dto.getInstructionId())){
            long actialDetailTotal = wmsOutSourceMapper.getActialDetailTotal(tenantId, dto.getInstructionId(), sourceMaterialLot.getMaterialLotId());
            if (actialDetailTotal > 0) {
                WmsOutSourceScanMaterialQueryVO wmsOutSourceScanMaterialQueryVO = new WmsOutSourceScanMaterialQueryVO();
                wmsOutSourceScanMaterialQueryVO.setInstructionDocId(dto.getInstructionDocId());
                wmsOutSourceScanMaterialQueryVO.setInstructionId(dto.getInstructionId());
                wmsOutSourceScanMaterialQueryVO.setMaterialLotIds(Collections.singletonList(sourceMaterialLot.getMaterialLotId()));
                returnMaterialLot(tenantId, wmsOutSourceScanMaterialQueryVO);
            }
        }
        //????????????
        //????????????ID
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "MATERIAL_LOT_SPLIT");
        //??????ID
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("MATERIAL_LOT_SPLIT");
        mtEventCreateVO.setEventRequestId(eventRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
        MtMaterialVO3 mtMaterialVO3 = new MtMaterialVO3();
        mtMaterialVO3.setSourceMaterialLotId(sourceMaterialLot.getMaterialLotId());
        mtMaterialVO3.setSplitPrimaryQty(dto.getSplitQty().doubleValue());
        mtMaterialVO3.setEventRequestId(eventRequestId);
        if (StringUtils.isNotBlank(dto.getTargetMaterialLotCode())) {
            mtMaterialVO3.setSplitMaterialLotCode(dto.getTargetMaterialLotCode());
        }
        String targetMaterialLotId = mtMaterialLotRepository.materialLotSplit(tenantId, mtMaterialVO3);
        //????????????????????????????????????0?????????????????????
        sourceMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(sourceMaterialLot.getMaterialLotId());
        if (sourceMaterialLot.getPrimaryUomQty() == 0) {
            MtMaterialLotVO2 mtMaterialLotVo2 = new MtMaterialLotVO2();
            mtMaterialLotVo2.setEventId(eventId);
            mtMaterialLotVo2.setMaterialLotId(sourceMaterialLot.getMaterialLotId());
            mtMaterialLotVo2.setEnableFlag(HmeConstants.ConstantValue.NO);
            mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVo2, HmeConstants.ConstantValue.NO);
        }
        //??????{ materialLotLimitAttrQuery}??????????????????????????????????????????????????????????????????
        String sourceMaterialLotId = sourceMaterialLot.getMaterialLotId();
        List<MtExtendAttrVO> mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, new MtMaterialLotAttrVO2() {{
            setMaterialLotId(sourceMaterialLotId);
        }});
        List<MtExtendVO5> mtExtendVO5s = new ArrayList<>();
        //?????????????????????ORIGINAL_ID????????????????????????
        boolean flag = false;
        for (MtExtendAttrVO mtExtendAttrVO : mtExtendAttrVOS) {
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName(mtExtendAttrVO.getAttrName());
            if ("ORIGINAL_ID".equals(mtExtendAttrVO.getAttrName())) {
                mtExtendVO5.setAttrValue(sourceMaterialLot.getMaterialLotId());
                flag = true;
            } else {
                mtExtendVO5.setAttrValue(mtExtendAttrVO.getAttrValue());
            }
            mtExtendVO5s.add(mtExtendVO5);
        }
        if (!flag) {
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("ORIGINAL_ID");
            mtExtendVO5.setAttrValue(sourceMaterialLot.getMaterialLotId());
            mtExtendVO5s.add(mtExtendVO5);
        }
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", targetMaterialLotId, eventId, mtExtendVO5s);
        //????????????
        MtMaterialLot targetMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(targetMaterialLotId);
        dto.setTargetMaterialLotCode(targetMaterialLot.getMaterialLotCode());
        dto.setTargetMaterialLotId(targetMaterialLotId);
        return dto;
    }

    MtModLocator getMaterialLocator(Long tenantId, String siteId, String materialId, String fromLocatorId, String instructionId) {
        String lineMaterialVersion = "";
        MtExtendVO mtExtendVO = new MtExtendVO();
        mtExtendVO.setKeyId(instructionId);
        mtExtendVO.setTableName("mt_instruction_attr");
        mtExtendVO.setAttrName("MATERIAL_VERSION");
        List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotBlank(mtExtendAttrVOS.get(0).getAttrValue())) {
            lineMaterialVersion = mtExtendAttrVOS.get(0).getAttrValue();
        }
        return wmsOutSourceMapper.getMaterialLocator(tenantId, materialId,
                siteId, fromLocatorId, lineMaterialVersion);
    }
}