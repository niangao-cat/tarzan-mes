package com.ruike.wms.app.service.impl;

import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.itf.api.dto.ItfLightTaskIfaceDTO;
import com.ruike.itf.app.service.ItfLightTaskIfaceService;
import com.ruike.itf.app.service.ItfObjectTransactionIfaceService;
import com.ruike.itf.domain.entity.ItfLightTaskIface;
import com.ruike.itf.domain.repository.ItfLightTaskIfaceRepository;
import com.ruike.itf.domain.vo.ItfLightTaskIfaceVO;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.infra.mapper.ItfLightTaskIfaceMapper;
import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsCostCtrMaterialService;
import com.ruike.wms.domain.entity.WmsObjectTransaction;
import com.ruike.wms.domain.repository.WmsDocPrivilegeRepository;
import com.ruike.wms.domain.repository.WmsInvTransferIssueRepository;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.api.dto.WmsCostCtrMaterialDTO4;
import com.ruike.wms.api.dto.WmsCostCtrMaterialDTO6;
import com.ruike.wms.domain.vo.*;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsCostCenterReturnMapper;
import com.ruike.wms.infra.mapper.WmsCostCtrMaterialMapper;
import com.ruike.wms.infra.mapper.WmsInstructionSnRelMapper;
import com.ruike.wms.infra.mapper.WmsOutSourceMapper;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.CollectorsUtil;
import io.tarzan.common.domain.vo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtInstructionActualDetail;
import tarzan.actual.domain.repository.MtInstructionActualDetailRepository;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionVO3;
import tarzan.instruction.infra.mapper.MtInstructionDocMapper;
import tarzan.instruction.infra.mapper.MtInstructionMapper;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.inventory.infra.mapper.MtMaterialLotMapper;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import io.choerodon.mybatis.helper.OptionalHelper;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.vo.MtModLocatorVO16;

import static com.ruike.wms.infra.constant.WmsConstant.ConstantValue.WMS;
import static java.util.stream.Collectors.toList;

/**
 * 成本中心领料单执行应用服务实现
 *
 * @author jiangling.zheng@hand-china.com 2020-04-15 15:28
 */
@Service
public class WmsCostCtrMaterialServiceImpl implements WmsCostCtrMaterialService {

    @Autowired
    private WmsCostCtrMaterialMapper wmsCostCtrMaterialMapper;
    @Autowired
    private WmsInstructionSnRelMapper wmsInstructionSnRelMapper;

    @Autowired
    MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;

    @Autowired
    private MtMaterialLotMapper mtMaterialLotMapper;

    @Autowired
    private MtInstructionMapper mtInstructionMapper;

    @Autowired
    private WmsInvTransferIssueRepository wmsInvTransferIssueRepository;

    @Autowired
    private MtInstructionDocMapper mtInstructionDocMapper;

    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;

    @Autowired
    private MtMaterialLotRepository materialLotRepository;

    @Autowired
    private MtInstructionRepository mtLogisticInstructionService;
    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;
    @Autowired
    private MtInstructionActualDetailRepository mtInstructionActualDetailRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private WmsCostCenterReturnMapper wmsCostCenterReturnMapper;

    @Autowired
    private ItfObjectTransactionIfaceService itfObjectTransactionIfaceService;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtContainerRepository mtContainerRepository;

    @Autowired
    private WmsDocPrivilegeRepository wmsDocPrivilegeRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private WmsOutSourceMapper wmsOutSourceMapper;

    @Autowired
    private ItfLightTaskIfaceService itfLightTaskIfaceService;

    @Autowired
    private ItfLightTaskIfaceMapper itfLightTaskIfaceMapper;

    @Override
    @ProcessLovValue(targetField = {"", "docLineList", ""})
    public WmsCostCtrMaterialDTO docQuery(Long tenantId, String docBarCode) {
        if (StringUtils.isEmpty(docBarCode)) {
            throw new MtException("WMS_COST_CENTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0001", WmsConstant.ConstantValue.WMS, "docBarCode", ""));
        }
        // 扫码单据查询
        WmsCostCtrMaterialDTO doc = wmsCostCtrMaterialMapper.selectDocCondition(tenantId, docBarCode);
        // 单据存在性校验
        if (doc == null) {
            throw new MtException("WMS_COST_CENTER_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0002", WmsConstant.ConstantValue.WMS, docBarCode, ""));
        }
        // 单据类型校验(成本中心领料单)
        if (!WmsConstant.InstructionStatus.CCA_REQUISITION.equals(doc.getInstructionDocType())) {

            throw new MtException("WMS_COST_CENTER_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0003", WmsConstant.ConstantValue.WMS, docBarCode, ""));
        }
        // 单据状态校验(新建、执行中)
        if (!WmsConstant.InstructionStatus.NEW.equals(doc.getInstructionDocStatus()) && !WmsConstant.InstructionStatus.EXECUTE.equals(doc.getInstructionDocStatus())) {
            throw new MtException("WMS_COST_CENTER_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0004", WmsConstant.ConstantValue.WMS, docBarCode, ""));
        }
        // 单据行查询
        List<WmsCostCtrMaterialDTO2> docLineList =
                wmsCostCtrMaterialMapper.selectDocLineCondition(tenantId, doc.getInstructionDocId(), null);

        //V20211129 modify by penglin.sui 将值集查询提取到循环外
        List<LovValueDTO> lovValueDTOList1 = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(docLineList)){
            lovValueDTOList1 = lovAdapter.queryLovValue("WX.WMS.LOCATOR_TYPE_LIMIT", tenantId);
        }

        //获取推荐货位
        for(WmsCostCtrMaterialDTO2 wmsCostCtrMaterialDTO2:docLineList){
            if(StringUtils.isBlank(wmsCostCtrMaterialDTO2.getMaterialVersion())){
                wmsCostCtrMaterialDTO2.setMaterialVersion("");
            }
            MtModLocatorVO16 materialLocator = new MtModLocatorVO16();
            if(CollectionUtils.isNotEmpty(lovValueDTOList1)){
                List<String> locatorTypeList = lovValueDTOList1.stream().map(LovValueDTO::getValue).collect(toList());
                materialLocator = wmsOutSourceMapper.getMaterialLocatorCodeByType(tenantId, wmsCostCtrMaterialDTO2.getMaterialId(),
                        doc.getSiteId(), wmsCostCtrMaterialDTO2.getFromWarehouseId(), wmsCostCtrMaterialDTO2.getMaterialVersion(),locatorTypeList);
            }else{
                materialLocator = wmsOutSourceMapper.getMaterialLocatorCode(tenantId, wmsCostCtrMaterialDTO2.getMaterialId(),
                        doc.getSiteId(), wmsCostCtrMaterialDTO2.getFromWarehouseId(), wmsCostCtrMaterialDTO2.getMaterialVersion());
            }
            if (materialLocator != null) {
                wmsCostCtrMaterialDTO2.setRecommendLocatorId(materialLocator.getLocatorId());
                wmsCostCtrMaterialDTO2.setRecommendLocatorCode(materialLocator.getLocatorCode());
            }
            if(StringUtils.isNotBlank(wmsCostCtrMaterialDTO2.getRecommendLocatorCode())){
                //判断推荐货位是否在值集ITF.LOCATOR_LABEL_ID中
                List<LovValueDTO> LovValueDTOs = lovAdapter.queryLovValue("ITF.LOCATOR_LABEL_ID", tenantId);
                List<LovValueDTO> LovValueDTOList = LovValueDTOs.stream().filter(item ->item.getValue().equals(wmsCostCtrMaterialDTO2.getRecommendLocatorCode())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(LovValueDTOList)){
                    wmsCostCtrMaterialDTO2.setLightFlag("Y");
                    wmsCostCtrMaterialDTO2.setLightStatus("OFF");
                }else{
                    wmsCostCtrMaterialDTO2.setLightFlag("N");
                }
            }
        }
        doc.setDocLineList(docLineList);
        Long userId = DetailsHelper.getUserDetails().getUserId();
        List<String> FromWarehouseIdList = docLineList.stream().map(item -> item.getFromWarehouseId()).collect(Collectors.toList());
        //调用API检验是否有权限
        Boolean flag = wmsDocPrivilegeRepository.existsWarehousePrivileged(tenantId,WmsWarehousePrivilegeBatchQueryDTO.builder()
                .userId(userId)
                .locatorIdList(FromWarehouseIdList)
                .docType(WmsConstant.DocType.CCA_REQUISITION)
                .operationType(WmsConstant.OperationType.EXECUTE)
                .build());
        if(!flag){
            MtModLocator locator = mtModLocatorRepository.selectByPrimaryKey(FromWarehouseIdList.get(0));
            String locatorCode = Objects.isNull(locator) ? "" : locator.getLocatorCode();
            WmsCommonUtils.processValidateMessage(tenantId, "WMS_COST_CENTER_0067", WMS, locatorCode);
        }
        return doc;
    }

    @Override
    @ProcessLovValue(targetField = {"", "", ""})
    public WmsCostCtrMaterialDTO11 containerOrMaterialLotQuery(Long tenantId, WmsCostCtrMaterialDTO4 dto) {

        if (StringUtils.isEmpty(dto.getBarCode())) {
            throw new MtException("WMS_COST_CENTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0001", WmsConstant.ConstantValue.WMS, "barCode", ""));
        }
        if (CollectionUtils.isEmpty(dto.getDocLineList())) {
            throw new MtException("WMS_COST_CENTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0001", WmsConstant.ConstantValue.WMS, "docLineList", ""));
        }

        List<WmsCostCtrMaterialDTO2> docLineList = dto.getDocLineList();
        // 获取条码类型及物料批
        WmsCostCtrMaterialDTO6 materialLotDto = wmsInvTransferIssueRepository.materialLotInfoQuery(tenantId, dto.getBarCode());
        String codeType = materialLotDto.getCodeType();
        List<String> materialLotIds = materialLotDto.getMaterialLotIds();
        if (CollectionUtils.isEmpty(materialLotIds)) {
            return null;
        }
        //校验物料批扩展属性是否有销售订单
        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setTableName(WmsConstant.AttrTable.MT_MATERIAL_LOT_ATTR);
        mtExtendVO1.setKeyIdList(materialLotIds);
        List<MtExtendVO5> attrs = new ArrayList<>();
        MtExtendVO5 extend1 = new MtExtendVO5();
        extend1.setAttrName(WmsConstant.MaterialLotAttr.SO_NUM);
        attrs.add(extend1);
        MtExtendVO5 extend2 = new MtExtendVO5();
        extend2.setAttrName(WmsConstant.MaterialLotAttr.SO_LINE_NUM);
        attrs.add(extend2);
        MtExtendVO5 extend3 = new MtExtendVO5();
        extend3.setAttrName(HmeConstants.ExtendAttr.SAP_ACCOUNT_FLAG);
        attrs.add(extend3);
        MtExtendVO5 extend4 = new MtExtendVO5();
        extend4.setAttrName(HmeConstants.ExtendAttr.MF_FLAG);
        attrs.add(extend4);
        mtExtendVO1.setAttrs(attrs);
        List<MtExtendAttrVO1> extendAttrList = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
        if (CollectionUtils.isNotEmpty(extendAttrList)) {
            extendAttrList.forEach(item -> {
                if(item.getAttrName().equals(HmeConstants.ExtendAttr.SAP_ACCOUNT_FLAG)){
                    if(item.getAttrValue().equals(HmeConstants.ConstantValue.NO)){
                        throw new MtException("HME_MATERIAL_LOT_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_MATERIAL_LOT_0003", "HME", dto.getBarCode()));
                    }
                } else if (item.getAttrName().equals(HmeConstants.ExtendAttr.MF_FLAG)) {
                    if(HmeConstants.ConstantValue.YES.equals(item.getAttrValue())) {
                        // 条码${1}需为非在制品,请检查!
                        throw new MtException("WMS_COST_CENTER_0100", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_COST_CENTER_0100", "WMS", dto.getBarCode()));
                    }
                } else{
                    if (StringUtils.isNotBlank(item.getAttrValue())) {
                        throw new MtException("WMS_COST_CENTER_0053", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_COST_CENTER_0053", WmsConstant.ConstantValue.WMS));
                    }
                }
            });
        }

        return materialLotQuery(tenantId, codeType, dto, docLineList, materialLotIds);
    }

    @Override
    public Map<String, Object> checkMaterialLotQuality(Long tenantId, WmsCostCtrMaterialDTO4 dto) {
        if (StringUtils.isBlank(dto.getBarCode())) {
            throw new MtException("WMS_COST_CENTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0001", WmsConstant.ConstantValue.WMS, "barCode", ""));
        }
        // 获取条码类型及物料批
        WmsCostCtrMaterialDTO6 materialLotDto = wmsInvTransferIssueRepository.materialLotInfoQuery(tenantId, dto.getBarCode());
        String codeType = materialLotDto.getCodeType();
        List<String> materialLotIds = materialLotDto.getMaterialLotIds();

        Map<String, Object> resultMap = new HashMap<>();
        List<WmsCostCtrMaterialDTO3> dtoList = wmsCostCtrMaterialMapper.selectMaterialLotCondition(tenantId, materialLotIds);
        //1-质量状态通过 2-不通过
        String flag = "1";
        for (WmsCostCtrMaterialDTO3 wmsCostCtrMaterialDTO3 : dtoList) {
            if (!WmsConstant.ConstantValue.OK.equals(wmsCostCtrMaterialDTO3.getQualityStatus()) && HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)) {
                flag = "2";
            } else if (!WmsConstant.ConstantValue.OK.equals(wmsCostCtrMaterialDTO3.getQualityStatus()) && HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
                flag = "2";
            }
        }
        resultMap.put("flag",flag);
        return resultMap;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public WmsCostCtrMaterialDTO execute(Long tenantId, WmsCostCtrMaterialDTO7 docDto) {
        if (CollectionUtils.isEmpty(docDto.getBarCodes())) {
            throw new MtException("WMS_COST_CENTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0001", WmsConstant.ConstantValue.WMS, "BarCodeList"));
        }
        if (CollectionUtils.isEmpty(docDto.getDocLineList())) {
            throw new MtException("WMS_COST_CENTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0001", WmsConstant.ConstantValue.WMS, "docLineList"));
        }
        List<String> barCodeList = docDto.getBarCodes();
        List<WmsCostCtrMaterialDTO2> docLineList = docDto.getDocLineList();
        // 获取所有扫描过的条码信息
        List<WmsCostCtrMaterialDTO3> barDtoList = new ArrayList<>();
        for (String barCode : barCodeList) {
            WmsCostCtrMaterialDTO6 materialLotDto = wmsInvTransferIssueRepository.materialLotInfoQuery(tenantId, barCode);
            List<String> materialLotIds = materialLotDto.getMaterialLotIds();
            if (CollectionUtils.isEmpty(materialLotIds)) {
                return null;
            }
            List<WmsCostCtrMaterialDTO3> materialDtoList = wmsCostCtrMaterialMapper.selectMaterialLotCondition(tenantId, materialLotIds);
            // 若扫描是容器 传入容器id
            if (HmeConstants.LoadTypeCode.CONTAINER.equals(materialLotDto.getCodeType())) {
                materialDtoList = materialDtoList.stream().map(code -> {
                    code.setContainerId(materialLotDto.getContainerId());
                    return code;
                }).collect(Collectors.toList());
            }
            barDtoList.addAll(materialDtoList);
        }

        //获取物料批扩展属性
        List<String> materialLotIdList = barDtoList.stream().map(WmsCostCtrMaterialDTO3::getMaterialLotId).distinct().collect(Collectors.toList());
        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setTableName(WmsConstant.AttrTable.MT_MATERIAL_LOT_ATTR);
        mtExtendVO1.setKeyIdList(materialLotIdList);
        List<MtExtendVO5> attrs = new ArrayList<>();
        MtExtendVO5 extend1 = new MtExtendVO5();
        extend1.setAttrName(WmsConstant.MaterialLotAttr.SO_NUM);
        attrs.add(extend1);
        MtExtendVO5 extend2 = new MtExtendVO5();
        extend2.setAttrName(WmsConstant.MaterialLotAttr.SO_LINE_NUM);
        attrs.add(extend2);
        mtExtendVO1.setAttrs(attrs);
        List<MtExtendAttrVO1> extendAttrList = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
        Map<String, List<MtExtendAttrVO1>> materialLotAttrMap =
                extendAttrList.stream().collect(Collectors.groupingBy(MtExtendAttrVO1::getKeyId));

        //获取单据的移动类型
        MtExtendVO dto = new MtExtendVO();
        dto.setKeyId(docDto.getInstructionDocId());
        dto.setAttrName("MOVE_TYPE");
        dto.setTableName("mt_instruction_doc_attr");
        List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, dto);
        String docMoveType = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";

        //判断事务类型
        String transactionTypeCode = "";
        switch (docMoveType) {
            case "201" : transactionTypeCode = WmsConstant.TransactionTypeCode.WMS_COST_CENTER_I;break;
            case "Z01" : transactionTypeCode = WmsConstant.TransactionTypeCode.WMS_INSDID_ORDER_RD_I;break;
            case "Z07" : transactionTypeCode = WmsConstant.TransactionTypeCode.WMS_INSDID_ORDER_E_I;break;
            case "Z05" : transactionTypeCode = WmsConstant.TransactionTypeCode.WMS_INSDID_ORDER_S_I;break;
            case "202" : transactionTypeCode = WmsConstant.TransactionTypeCode.WMS_COST_CENTER_R;break;
            case "Z02" : transactionTypeCode = WmsConstant.TransactionTypeCode.WMS_INSDID_ORDER_RD_R;break;
            case "Z08" : transactionTypeCode = WmsConstant.TransactionTypeCode.WMS_INSDID_ORDER_E_R;break;
            case "Z06" : transactionTypeCode = WmsConstant.TransactionTypeCode.WMS_INSDID_ORDER_S_R;break;
        }
        if(StringUtils.isBlank(transactionTypeCode)){
            throw new MtException("WMS_COST_CENTER_0070", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0070", "WMS",docDto.getInstructionDocNum()));
        }
        //获取移动类型
        String moveType = wmsCostCenterReturnMapper.queryMoveTypeByTransactionTypeCode(tenantId, transactionTypeCode);
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();

        for (WmsCostCtrMaterialDTO2 docLine : docLineList) {
            // 获取执行物料行下本次扫描的条码
            List<WmsCostCtrMaterialDTO3> lotDtoList = barDtoList.stream().filter(item -> docLine.getMaterialId().equals(item.getMaterialId())
                    && ((StringUtils.trimToEmpty(docLine.getMaterialVersion()).equals(StringUtils.trimToEmpty(item.getMaterialVersion()))) || StringUtils.isEmpty(docLine.getMaterialVersion()))).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(lotDtoList)) {
                continue;
            }
            // 创建成本中心领料请求事件
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "COSTCENTER_ISSUE_REQUISTION_EXECUTE");
            // 创建成本中心领料事件
            MtEventCreateVO eventCreate = new MtEventCreateVO();
            eventCreate.setEventRequestId(eventRequestId);
            eventCreate.setEventTypeCode("COSTCENTER_ISSUE_EXECUTE");
            String eventId = mtEventRepository.eventCreate(tenantId, eventCreate);
            WmsObjectTransactionVO objectTransactionVO = new WmsObjectTransactionVO();
            objectTransactionVO.setEventId(eventId);
            objectTransactionVO.setTransactionTime(new Date());
            objectTransactionVO.setSourceDocId(docDto.getInstructionDocId());
            objectTransactionVO.setSourceDocNum(docDto.getInstructionDocNum());
            objectTransactionVO.setSourceDocLineId(docLine.getInstructionId());
            objectTransactionVO.setSourceDocLineNum(docLine.getInstructionNum());
            objectTransactionVO.setSourceDocType(docDto.getInstructionDocType());
            if (WmsConstant.SettleAccounts.COST_CENTER.equals(docDto.getSettleAccounts())) {
                objectTransactionVO.setCostCenterCode(docDto.getCostCenterCode());
                objectTransactionVO.setInternalOrderCode(null);
            } else if (WmsConstant.SettleAccounts.INTERNAL_ORDER.equals(docDto.getSettleAccounts())) {
                objectTransactionVO.setCostCenterCode(null);
                objectTransactionVO.setInternalOrderCode(docDto.getCostCenterCode());
            }
            objectTransactionVO.setRemark("领料单执行");
            objectTransactionVO.setTransactionTypeCode(transactionTypeCode);
            objectTransactionVO.setMoveType(moveType);

            // 调用{ sequenceLimitMaterialLotBatchConsume }进行消耗
            MtMaterialLotVO15 mtMaterialLotVO15 = new MtMaterialLotVO15();
            mtMaterialLotVO15.setAllConsume(WmsConstant.CONSTANT_Y);
            mtMaterialLotVO15.setInstructionDocId(docDto.getInstructionDocId());
            mtMaterialLotVO15.setEventRequestId(eventRequestId);
            mtMaterialLotVO15.setMtMaterialLotSequenceList(lotDtoList.stream()
                    .map(detail -> {
                        MtMaterialLotVO16 mtMaterialLotVO161 = new MtMaterialLotVO16();
                        mtMaterialLotVO161.setMaterialLotId(detail.getMaterialLotId());
                        return mtMaterialLotVO161;
                    }).collect(Collectors.toList()));
            materialLotRepository.sequenceLimitMaterialLotBatchConsume(tenantId, mtMaterialLotVO15);

            for (WmsCostCtrMaterialDTO3 lotDto : lotDtoList) {
                // 步骤1：本次执行条码记录成本中心领料事务
                if (materialLotAttrMap.containsKey(lotDto.getMaterialLotId())) {
                    List<MtExtendAttrVO1> extendAttrs = materialLotAttrMap.get(lotDto.getMaterialLotId());
                    for (MtExtendAttrVO1 extendAttr : extendAttrs) {
                        if (WmsConstant.MaterialLotAttr.SO_NUM.equals(extendAttr.getAttrName())) {
                            objectTransactionVO.setSoNum(extendAttr.getAttrValue());
                        } else if (WmsConstant.MaterialLotAttr.SO_LINE_NUM.equals(extendAttr.getAttrName())) {
                            objectTransactionVO.setSoLineNum(extendAttr.getAttrValue());
                        }
                    }
                }
                objectTransactionVO.setTransactionQty(lotDto.getPrimaryUomQty());
                addObjectTransaction(tenantId, objectTransactionVO, lotDto, objectTransactionRequestList);
                // 步骤2：更新条码状态(条码更新失效 数量改成0 放到API[sequenceLimitMaterialLotBatchConsume])
                List<MtExtendVO5> mtExtendVO5s = new ArrayList<>();
                MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                mtExtendVO5.setAttrName(WmsConstant.ExtendAttr.STATUS);
                mtExtendVO5.setAttrValue(WmsConstant.MaterialLotStatus.SHIPPED);
                mtExtendVO5s.add(mtExtendVO5);
                mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", lotDto.getMaterialLotId(), eventId, mtExtendVO5s);

                // 步骤3：建立条码与执行单据行关系
                MtInstructionVO3 logisticInstructionVO3 = new MtInstructionVO3();
                List<MtInstructionVO3.MaterialLotList> list = new ArrayList<>();
                MtInstructionVO3.MaterialLotList materialLotList = new MtInstructionVO3.MaterialLotList();
                materialLotList.setMaterialLotId(lotDto.getMaterialLotId());
                materialLotList.setQty(lotDto.getPrimaryUomQty().doubleValue());
                materialLotList.setContainerId(lotDto.getContainerId());
                materialLotList.setFromLocatorId(lotDto.getWarehouseId());
                materialLotList.setUomId(lotDto.getPrimaryUomId());
                if (StringUtils.isNotBlank(lotDto.getContainerId())) {
                    materialLotList.setContainerId(lotDto.getContainerId());
                }
                list.add(materialLotList);
                logisticInstructionVO3.setInstructionId(docLine.getInstructionId());
                logisticInstructionVO3.setMaterialLotMessageList(list);
                logisticInstructionVO3.setEventRequestId(eventRequestId);
                mtLogisticInstructionService.instructionExecute(tenantId, logisticInstructionVO3);
            }
            // 步骤4：更新成本中心领料单行数据
            MtInstruction mtInstruction = mtInstructionMapper.selectByPrimaryKey(docLine.getInstructionId());
            //2020-10-16 edit by chaonan.hu for kang.wang 实际接收数量大于等于制单数量时，单据行置完成
            if (docLine.getQuantity().compareTo(docLine.getActualQty()) <= 0) {
                mtInstruction.setInstructionStatus(WmsConstant.InstructionStatus.COMPLETE);
            } else {
                mtInstruction.setInstructionStatus(WmsConstant.InstructionStatus.EXECUTE);
            }
            OptionalHelper.optional(Collections.singletonList(MtInstruction.FIELD_INSTRUCTION_STATUS));
            mtInstructionMapper.updateOptional(mtInstruction);
        }
        // 步骤5：更新成本中心领料单头状态
        List<WmsCostCtrMaterialDTO2> docLineTempList =
                wmsCostCtrMaterialMapper.selectDocLineCondition(tenantId, docDto.getInstructionDocId(), null);
        MtInstructionDoc mtInstructionDoc = mtInstructionDocMapper.selectByPrimaryKey(docDto.getInstructionDocId());
        if (docLineTempList.stream().allMatch(item ->
                WmsConstant.DocStatus.COMPLETE.equals(item.getInstructionStatus()))) {
            mtInstructionDoc.setInstructionDocStatus(WmsConstant.InstructionStatus.COMPLETE);
        } else {
            mtInstructionDoc.setInstructionDocStatus(WmsConstant.InstructionStatus.EXECUTE);
        }
        OptionalHelper.optional(Collections.singletonList(MtInstructionDoc.FIELD_INSTRUCTION_DOC_STATUS));
        mtInstructionDocMapper.updateOptional(mtInstructionDoc);
        // 返回单据头信息
        WmsCostCtrMaterialDTO wmsCostCtrMaterialDTO = wmsCostCtrMaterialMapper.selectDocCondition(tenantId, docDto.getInstructionDocNum());
        // 本次执行条码记录成本中心领料事务
        List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS = wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
        itfObjectTransactionIfaceService.sendSapMaterialMove(tenantId, wmsObjectTransactionResponseVOS);
        return wmsCostCtrMaterialDTO;
    }

    @Override
    @ProcessLovValue(targetField = {"", "barDtoList", ""})
    public WmsCostCtrMaterialDTO9 docDetailQuery(Long tenantId, WmsCostCtrMaterialDTO8 dto) {
        if (StringUtils.isEmpty(dto.getInstructionId())) {
            throw new MtException("WMS_COST_CENTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0001", WmsConstant.ConstantValue.WMS, "instructionId", ""));
        }
        // 获取行明细信息
        WmsCostCtrMaterialDTO2 docLine = dto.getDocLine();
        // 获取本次扫码条码信息
        List<WmsCostCtrMaterialDTO3> barDtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dto.getBarCodes())) {
            List<String> barCodeList = dto.getBarCodes();
            for (String barCode : barCodeList) {
                WmsCostCtrMaterialDTO6 materialLotDto = wmsInvTransferIssueRepository.materialLotInfoQuery(tenantId, barCode);
                List<String> materialLotIds = materialLotDto.getMaterialLotIds();
                if (CollectionUtils.isNotEmpty(materialLotIds)) {
                    List<WmsCostCtrMaterialDTO3> materialDtoList = wmsCostCtrMaterialMapper.selectMaterialLotCondition(tenantId, materialLotIds);
                    materialDtoList.removeIf(item -> !(
                            StringUtils.equals(docLine.getMaterialId(), item.getMaterialId())
                                    && ((StringUtils.trimToEmpty(docLine.getMaterialVersion()).equals(StringUtils.trimToEmpty(item.getMaterialVersion()))) || StringUtils.isEmpty(docLine.getMaterialVersion()))));
                    materialDtoList.forEach(t -> {
                        t.setIsContainerCode(HmeConstants.LoadTypeCode.CONTAINER.equals(materialLotDto.getCodeType()) ? "Y" : "N");
                        if (HmeConstants.LoadTypeCode.CONTAINER.equals(materialLotDto.getCodeType())) {
                            t.setContainerId(materialLotDto.getContainerId());
                            MtContainer mtContainer = mtContainerRepository.selectByPrimaryKey(materialLotDto.getContainerId());
                            t.setContainerCode(mtContainer != null ? mtContainer.getContainerCode() : "");
                        }
                    });
                    barDtoList.addAll(materialDtoList);
                }
            }
            // 前台缓存数据标记
            barDtoList.forEach(barDto -> {
                barDto.setCacheFlag(WmsConstant.CONSTANT_Y);
            });
        }
        // 获取历史条码信息
        WmsCostCtrMaterialDTO9 returnDto = new WmsCostCtrMaterialDTO9();
        BeanUtils.copyProperties(docLine, returnDto);
        List<MtInstructionActualDetail> mtInstructionActualDetails = mtInstructionActualDetailRepository
                .instructionLimitActualDetailQuery(tenantId, dto.getInstructionId());
        Map<String, List<MtInstructionActualDetail>> actualDetailMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(mtInstructionActualDetails)) {
            actualDetailMap = mtInstructionActualDetails.stream().collect(Collectors.groupingBy(details -> details.getMaterialLotId()));
        }
        List<String> containerIdList = mtInstructionActualDetails.stream().map(MtInstructionActualDetail::getContainerId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<String, List<MtContainer>> containerMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(containerIdList)) {
            List<MtContainer> containerList = mtContainerRepository.containerPropertyBatchGet(tenantId, containerIdList);
            containerMap = containerList.stream().collect(Collectors.groupingBy(container -> container.getContainerId()));
        }
        List<String> matLotIds = mtInstructionActualDetails.stream().map(MtInstructionActualDetail::getMaterialLotId).collect(Collectors.toList());

        Map<String, List<MtInstructionActualDetail>> finalActualDetailMap = actualDetailMap;
        Map<String, List<MtContainer>> finalContainerMap = containerMap;
        if (CollectionUtils.isNotEmpty(matLotIds)) {
            List<WmsCostCtrMaterialDTO3> materialDtoList = wmsCostCtrMaterialMapper.selectMaterialLotCondition(tenantId, matLotIds);
            materialDtoList.forEach(barDto -> {
                List<MtInstructionActualDetail> detailList = finalActualDetailMap.get(barDto.getMaterialLotId());
                if (CollectionUtils.isNotEmpty(detailList)) {
                    barDto.setPrimaryUomQty(detailList.get(0).getActualQty() != null ? BigDecimal.valueOf(detailList.get(0).getActualQty()) : BigDecimal.ZERO);
                    List<MtContainer> containerList = finalContainerMap.get(detailList.get(0).getContainerId());
                    if (CollectionUtils.isNotEmpty(containerList)) {
                        barDto.setContainerId(containerList.get(0).getContainerId());
                        barDto.setContainerCode(containerList.get(0).getContainerCode());
                    }
                }
                barDto.setCacheFlag(WmsConstant.ConstantValue.NO);
            });
            barDtoList.addAll(materialDtoList);
        }
        returnDto.setBarDtoList(barDtoList);
        return returnDto;
    }

    @Override
    public WmsCostCtrMaterialDTO9 deleteMaterialLot(Long tenantId, WmsCostCtrMaterialDTO8 dto) {
        return docDetailQuery(tenantId, dto);
    }

    @Override
    public List<WmsCostCtrMaterialVO> lightOnOrOff(Long tenantId, List<ItfLightTaskIfaceDTO> dtoList) {
        if(CollectionUtils.isNotEmpty(dtoList)){
            List<WmsCostCtrMaterialVO> resultList = new ArrayList<>();
            List<ItfLightTaskIfaceDTO> itfLightTaskIfaceDTOS = new ArrayList<>();
            for(ItfLightTaskIfaceDTO itfLightTaskIfaceDTO:dtoList){
                if(StringUtils.isNotBlank(itfLightTaskIfaceDTO.getTaskNum())){
                    itfLightTaskIfaceDTO.setTaskStatus("OFF");
                }else{
                    itfLightTaskIfaceDTO.setTaskStatus("");
                    itfLightTaskIfaceDTO.setTaskType("OUT");
                }
                itfLightTaskIfaceDTOS.add(itfLightTaskIfaceDTO);
            }
            List<ItfLightTaskIfaceVO> itfLightTaskIfaceVOS = itfLightTaskIfaceService.itfLightTaskIface(tenantId,itfLightTaskIfaceDTOS);
            if(CollectionUtils.isNotEmpty(itfLightTaskIfaceVOS)){
                List<String> taskNumList = itfLightTaskIfaceVOS.stream().map(ItfLightTaskIfaceVO::getTaskNum).collect(toList());
                List<ItfLightTaskIface> itfLightTaskIfaceList = itfLightTaskIfaceMapper.selectByTaskNum(tenantId,taskNumList);
                for(ItfLightTaskIfaceVO itfLightTaskIfaceVO:itfLightTaskIfaceVOS){
                    WmsCostCtrMaterialVO wmsCostCtrMaterialVO = new WmsCostCtrMaterialVO();
                    wmsCostCtrMaterialVO.setTaskNum(itfLightTaskIfaceVO.getTaskNum());
                    wmsCostCtrMaterialVO.setStatus(itfLightTaskIfaceVO.getStatus());
                    wmsCostCtrMaterialVO.setMessage(itfLightTaskIfaceVO.getMessage());
                    List<ItfLightTaskIface> itfLightTaskIfaces = itfLightTaskIfaceList.stream().filter(item ->item.getTaskNum().equals(itfLightTaskIfaceVO.getTaskNum())).collect(toList());
                    wmsCostCtrMaterialVO.setInstructionId(itfLightTaskIfaces.get(0).getDocLineId());
                    if(StringUtils.isNotBlank(dtoList.get(0).getTaskNum())){
                        wmsCostCtrMaterialVO.setTaskStatus("OFF");
                    }else{
                        wmsCostCtrMaterialVO.setTaskStatus("ON");
                    }
                    resultList.add(wmsCostCtrMaterialVO);
                }
            }
            return resultList;
        }
        return null;
    }

    private WmsCostCtrMaterialDTO11 materialLotQuery(Long tenantId, String codeType, WmsCostCtrMaterialDTO4 barCodeDto,
                                                          List<WmsCostCtrMaterialDTO2> docLineList, List<String> materialLotIds) {
        //获取行指定信息
        List<WmsInstructionSnRelVO> wmsInstructionSnRelVOS = wmsInstructionSnRelMapper.selectInstruction(tenantId);

        List<WmsCostCtrMaterialDTO3> dtoList = wmsCostCtrMaterialMapper.selectMaterialLotCondition(tenantId, materialLotIds);

        //20201021 wenzhang.yu for kang.wang  校验物料批扩展状态
        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setTableName(WmsConstant.AttrTable.MT_MATERIAL_LOT_ATTR);
        mtExtendVO1.setKeyIdList(materialLotIds);
        List<MtExtendVO5> attrs = new ArrayList<>();
        MtExtendVO5 extend1 = new MtExtendVO5();
        extend1.setAttrName(WmsConstant.MaterialLotAttr.STATUS);
        attrs.add(extend1);
        mtExtendVO1.setAttrs(attrs);
        List<MtExtendAttrVO1> extendAttrList = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
        if (CollectionUtils.isEmpty(extendAttrList)) {
            throw new MtException("WMS_COST_CENTER_0069", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0069",WmsConstant.ConstantValue.WMS, "WMS.MATERIAL_LOT_STATUS_LIMIT"));
        }
        List<LovValueDTO> materialLotStatus = lovAdapter.queryLovValue("WMS.MATERIAL_LOT_STATUS_LIMIT", tenantId);

        Map<String, BigDecimal> materialQtyMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        String isContainterCode = HmeConstants.LoadTypeCode.CONTAINER.equals(codeType) ? WmsConstant.CONSTANT_Y : WmsConstant.CONSTANT_N;
        List<ItfLightTaskIfaceDTO> itfLightTaskIfaceDTOS = new ArrayList<>();
        // 容器条码下物料批的物料相同
        for (WmsCostCtrMaterialDTO3 dto : dtoList) {
            dto.setIsContainerCode(isContainterCode);
            // 有效性校验
            if (!WmsConstant.CONSTANT_Y.equals(dto.getEnableFlag()) && HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)) {
                throw new MtException("WMS_COST_CENTER_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0007", WmsConstant.ConstantValue.WMS, barCodeDto.getBarCode(), dto.getMaterialLotCode()));
            } else if (!WmsConstant.CONSTANT_Y.equals(dto.getEnableFlag()) && HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
                throw new MtException("WMS_COST_CENTER_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0009", WmsConstant.ConstantValue.WMS, dto.getMaterialLotCode()));
            }
            // 是否冻结校验
            if (WmsConstant.CONSTANT_Y.equals(dto.getFreezeFlag()) && HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)) {
                throw new MtException("WMS_COST_CENTER_0024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0024", WmsConstant.ConstantValue.WMS, barCodeDto.getBarCode(), dto.getMaterialLotCode()));
            } else if (WmsConstant.CONSTANT_Y.equals(dto.getFreezeFlag()) && HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
                throw new MtException("WMS_COST_CENTER_0025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0025", WmsConstant.ConstantValue.WMS, dto.getMaterialLotCode()));
            }
            //条码盘点停用标识校验
            if (WmsConstant.CONSTANT_Y.equals(dto.getStocktakeFlag()) && HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)) {
                throw new MtException("WMS_COST_CENTER_0033", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0033", WmsConstant.ConstantValue.WMS, barCodeDto.getBarCode(), dto.getMaterialLotCode()));
            } else if (WmsConstant.CONSTANT_Y.equals(dto.getStocktakeFlag()) && HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
                throw new MtException("WMS_COST_CENTER_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0034", WmsConstant.ConstantValue.WMS, dto.getMaterialLotCode()));
            }
            // 校验条码物料是否在成本中心单据行物料中
            // 获取当前页面单据行信息
            List<String> materialIds = docLineList.stream().map(WmsCostCtrMaterialDTO2::getMaterialId)
                    .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
            if (!materialIds.contains(dto.getMaterialId()) && HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)) {
                throw new MtException("WMS_COST_CENTER_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0011", WmsConstant.ConstantValue.WMS, barCodeDto.getBarCode(), dto.getMaterialLotCode()));
            } else if (!materialIds.contains(dto.getMaterialId()) && HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
                throw new MtException("WMS_COST_CENTER_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0012", WmsConstant.ConstantValue.WMS, dto.getMaterialLotCode()));
            }

      /*      //匹配指定sn
            List<WmsInstructionSnRelVO> wmsInstructionSnRelVOList1 = wmsInstructionSnRelVOS.stream().filter(item -> item.getMaterialLotId().equals(dto.getMaterialLotId())).collect(toList());
            //条码没被指定
            if (CollectionUtils.isEmpty(wmsInstructionSnRelVOList1)) {
                List<String> instructionIdList = wmsInstructionSnRelVOS.stream().map(WmsInstructionSnRelVO::getInstructionId).collect(toList());
                docLineList = docLineList.stream().filter(item -> !instructionIdList.contains(item.getInstructionId())).collect(toList());
                if(CollectionUtils.isEmpty(docLineList)){
                    throw new MtException("WX_WMS_SO_DELIVERY_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WX_WMS_SO_DELIVERY_0010", "WMS"));
                }
            }else {
                //条码被指定
                List<String> instructionIdList = wmsInstructionSnRelVOList1.stream().map(WmsInstructionSnRelVO::getInstructionId).collect(toList());
                List<WmsCostCtrMaterialDTO2> lineList2 = docLineList.stream().filter(item -> instructionIdList.contains(item.getInstructionId())).collect(toList());
                if (CollectionUtils.isEmpty(lineList2)) {
                    //条码被指定的行不在该单据下
                    //获取该条码被指定的行状态
                    List<MtInstruction> mtInstructions = mtLogisticInstructionService.instructionPropertyBatchGet(tenantId, instructionIdList);
                    for (MtInstruction mtInstruction : mtInstructions) {
                        //判断行状态是否在值集中
                        List<LovValueDTO> status = lovAdapter.queryLovValue("WX.WMS.AUTO_SN_DOC_STATUS_LIMIT", tenantId);
                        List<LovValueDTO> statusList = status.stream().filter(item -> item.getValue().equals(mtInstruction.getInstructionStatus())).collect(Collectors.toList());
                        if (CollectionUtils.isEmpty(statusList)) {
                            //不存在报错
                            //获取单据num
                            MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.instructionDocPropertyGet(tenantId, mtInstruction.getSourceDocId());
                            throw new MtException("WX_WMS_SO_DELIVERY_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WX_WMS_SO_DELIVERY_0009", "WMS",dto.getMaterialLotCode(),mtInstructionDoc.getInstructionDocNum()));
                        }
                    }
                } else {
                    //条码被指定的行在该单据下
                    docLineList = lineList2;
                }
            }*/




            // 校验物料版本
            // 匹配对应单据行（物料+版本唯一，单据条码为空时只通过物料匹配）
            List<WmsCostCtrMaterialDTO2> docLines = docLineList.stream().filter(item ->
                    dto.getMaterialId().equals(item.getMaterialId())
                            && (((StringUtils.trimToEmpty(dto.getMaterialVersion())
                            .equals(StringUtils.trimToEmpty(Optional.ofNullable(item.getMaterialVersion()).orElse(dto.getMaterialVersion())))
                    ) || (StringUtils.isEmpty(item.getMaterialVersion()))))).collect(Collectors.toList());
            if (docLines.size() == 0 && HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)) {
                throw new MtException("WMS_COST_CENTER_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0013", WmsConstant.ConstantValue.WMS, barCodeDto.getBarCode(), dto.getMaterialLotCode()));
            } else if (docLines.size() == 0 && HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
                throw new MtException("WMS_COST_CENTER_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0014", WmsConstant.ConstantValue.WMS, dto.getMaterialLotCode()));
            }
            // 校验条码的仓库与对应的成本中心领料单行中对应的物料行的仓库是否一致
            long warehouseCount = docLines.stream().filter(item ->
                    StringUtils.equals(dto.getWarehouseId(), item.getFromWarehouseId())
            ).count();
            if (warehouseCount == 0 && HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)) {
                throw new MtException("WMS_COST_CENTER_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0015", WmsConstant.ConstantValue.WMS, barCodeDto.getBarCode(), dto.getMaterialLotCode()));
            } else if (warehouseCount == 0 && HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
                throw new MtException("WMS_COST_CENTER_0016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0016", WmsConstant.ConstantValue.WMS, dto.getMaterialLotCode(), ""));
            }
            // 校验物料条码主单位与指令行中对应的单位是否一致
            long uomCount = docLines.stream().filter(item ->
                    StringUtils.equals(dto.getPrimaryUomId(), item.getUomId())
            ).count();
            if (uomCount == 0) {
                if (HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
                    throw new MtException("WMS_COST_CENTER_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_COST_CENTER_0037", WmsConstant.ConstantValue.WMS, dto.getMaterialLotCode(), ""));
                } else if (HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)) {
                    throw new MtException("WMS_COST_CENTER_0036", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_COST_CENTER_0036", WmsConstant.ConstantValue.WMS, barCodeDto.getBarCode(), dto.getMaterialLotCode()));
                }
            }
            // 校验条码的货位与对应的成本中心领料单行中对应的物料行的货位是否一致
            long locatorCount = docLines.stream().filter(item ->
                    StringUtils.equals(Optional.ofNullable(dto.getLocatorId()).orElse(""),
                            Optional.ofNullable(Optional.ofNullable("".equals(item.getFromLocatorId()) ? null : item.getFromLocatorId()).orElse(dto.getLocatorId())).orElse(""))
            ).count();
            if (locatorCount == 0 && HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)) {
                throw new MtException("WMS_COST_CENTER_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0017", WmsConstant.ConstantValue.WMS, barCodeDto.getBarCode(), dto.getMaterialLotCode()));
            } else if (locatorCount == 0 && HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
                throw new MtException("WMS_COST_CENTER_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0018", WmsConstant.ConstantValue.WMS, dto.getMaterialLotCode(), ""));
            }

            Long userId = DetailsHelper.getUserDetails().getUserId();
            //调用API判断是否有权限
            wmsDocPrivilegeRepository.isWarehousePrivileged(tenantId,WmsWarehousePrivilegeQueryDTO.builder()
                    .userId(userId)
                    .locatorId(docLines.get(0).getFromWarehouseId())
                    .docType(WmsConstant.DocType.CCA_REQUISITION)
                    .operationType(WmsConstant.OperationType.EXECUTE).build());
            // 条码数量+扫描累计数量+物料行已执行数量是否超过制单数量
            WmsCostCtrMaterialDTO2 docLine = docLines.get(0);
            BigDecimal actualQty = Optional.ofNullable(docLine.getActualQty()).orElse(BigDecimal.ZERO);
            //当前物料已扫描未执行实物总数
            BigDecimal scannedMaterialQty = BigDecimal.ZERO;
            List<WmsCostCtrMaterialDTO3> scannedBarcodes = barCodeDto.getBarCodeList();
            //已扫描条码
            if (scannedBarcodes.size() > 0) {
                //获取已扫描条码中与当前物料一致的条码数量和实物数量
                List<WmsCostCtrMaterialDTO3> scannedMaterialCodeDtoList = scannedBarcodes.stream().filter(m -> dto.getMaterialId().equals(m.getMaterialId())
                        && StringUtils.trimToEmpty(dto.getMaterialVersion()).equals(StringUtils.trimToEmpty(m.getMaterialVersion()))).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(scannedMaterialCodeDtoList)) {
                    scannedMaterialQty = scannedMaterialQty.add(scannedMaterialCodeDtoList.stream().collect(CollectorsUtil.summingBigDecimal(m -> Optional.ofNullable(m.getPrimaryUomQty()).orElse(BigDecimal.ZERO))));
                }
            }
            String materialQtyKey = dto.getMaterialId() + dto.getMaterialVersion();
            BigDecimal materialQty = Optional.ofNullable(materialQtyMap.get(materialQtyKey)).orElse(BigDecimal.ZERO);
            materialQtyMap.put(materialQtyKey, materialQty.add(dto.getPrimaryUomQty()));
            BigDecimal totalQty = actualQty.add(scannedMaterialQty).add(materialQty).add(dto.getPrimaryUomQty());
            //获取当前条码中所有相同物料
            List<WmsCostCtrMaterialDTO3> currentSameMaterialList = dtoList.stream().filter(m -> dto.getMaterialId().equals(m.getMaterialId())
                    && StringUtils.trimToEmpty(dto.getMaterialVersion()).equals(StringUtils.trimToEmpty(m.getMaterialVersion()))).collect(Collectors.toList());
            // 2020-12-28 add by sanfeng.zhang 无需汇总  返回当前条码数量
            currentSameMaterialList = currentSameMaterialList.stream().filter(csm -> StringUtils.equals(csm.getMaterialLotId(), dto.getMaterialLotId())).collect(Collectors.toList());
            int addCodeQty = currentSameMaterialList.size();
            BigDecimal addQuantity = currentSameMaterialList.stream().collect(CollectorsUtil.summingBigDecimal(m -> Optional.ofNullable(m.getPrimaryUomQty()).orElse(BigDecimal.ZERO)));
            dto.setAddCodeQty(addCodeQty);
            dto.setAddQuantity(addQuantity);
            if (totalQty.compareTo(docLine.getQuantity()) > 0) {//已执行+已扫描+当前条码数量>制单数量
                if (actualQty.add(scannedMaterialQty).add(materialQty).compareTo(docLine.getQuantity()) > 0) {//已执行数量+已扫描数量+当前条码已统计数量>制单数量,已超发
                    if (HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)) {
                        throw new MtException("WMS_COST_CENTER_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_COST_CENTER_0019", WmsConstant.ConstantValue.WMS, barCodeDto.getBarCode(),
                                totalQty.toString(), docLine.getQuantity().toString()));
                    } else if (HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
                        throw new MtException("WMS_COST_CENTER_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_COST_CENTER_0019", WmsConstant.ConstantValue.WMS, dto.getMaterialLotCode(),
                                totalQty.toString(), docLine.getQuantity().toString(), ""));
                    }
                } else {
                    Map<String, String> excessMap = wmsCostCtrMaterialMapper.selectExcess(tenantId, docLines.get(0).getInstructionId());
                    if (excessMap == null || !"M".equalsIgnoreCase(excessMap.get("EXCESS_SETTING"))) {
                        if (HmeConstants.LoadTypeCode.CONTAINER.equals(codeType)) {
                            throw new MtException("WMS_COST_CENTER_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WMS_COST_CENTER_0019", WmsConstant.ConstantValue.WMS, barCodeDto.getBarCode(),
                                    totalQty.toString(), docLine.getQuantity().toString()));
                        } else if (HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(codeType)) {
                            throw new MtException("WMS_COST_CENTER_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WMS_COST_CENTER_0019", WmsConstant.ConstantValue.WMS, dto.getMaterialLotCode(),
                                    totalQty.toString(), docLine.getQuantity().toString(), ""));
                        }
                    }
                }
            }

            //20201021 wenzhang.yu for kang.wang  校验物料批扩展状态
            List<MtExtendAttrVO1> statusList = extendAttrList.stream().filter(t -> t.getKeyId().equals(dto.getMaterialLotId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(statusList)) {
                throw new MtException("WMS_COST_CENTER_0069", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0069",WmsConstant.ConstantValue.WMS, "WMS.MATERIAL_LOT_STATUS_LIMIT"));
            } else {
                if (CollectionUtils.isNotEmpty(materialLotStatus)) {
                    List<String> materialLotStatusList = materialLotStatus.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                    if (!materialLotStatusList.contains(statusList.get(0).getAttrValue())) {
                        throw new MtException("WMS_COST_CENTER_0069", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_COST_CENTER_0069",WmsConstant.ConstantValue.WMS, "WMS.MATERIAL_LOT_STATUS_LIMIT"));
                    }
                }
            }



            //匹配指定sn
            List<WmsInstructionSnRelVO> wmsInstructionSnRelVOList1 = wmsInstructionSnRelVOS.stream().filter(item -> item.getMaterialLotId().equals(dto.getMaterialLotId())).collect(toList());
            //条码没被指定
            if (CollectionUtils.isEmpty(wmsInstructionSnRelVOList1)) {
                List<String> instructionIdList = wmsInstructionSnRelVOS.stream().map(WmsInstructionSnRelVO::getInstructionId).collect(toList());
                docLines = docLines.stream().filter(item -> !instructionIdList.contains(item.getInstructionId())).collect(toList());
                if(CollectionUtils.isEmpty(docLines)){
                    throw new MtException("WX_WMS_SO_DELIVERY_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WX_WMS_SO_DELIVERY_0010", "WMS"));
                }
            }else {
                //条码被指定
                List<String> instructionIdList = wmsInstructionSnRelVOList1.stream().map(WmsInstructionSnRelVO::getInstructionId).collect(toList());
                List<WmsCostCtrMaterialDTO2> lineList2 = docLines.stream().filter(item -> instructionIdList.contains(item.getInstructionId())).collect(toList());
                if (CollectionUtils.isEmpty(lineList2)) {
                    //条码被指定的行不在该单据下
                    //获取该条码被指定的行状态
                    List<MtInstruction> mtInstructions = mtLogisticInstructionService.instructionPropertyBatchGet(tenantId, instructionIdList);
                    for (MtInstruction mtInstruction : mtInstructions) {
                        //判断行状态是否在值集中
                        List<LovValueDTO> status = lovAdapter.queryLovValue("WX.WMS.AUTO_SN_DOC_STATUS_LIMIT", tenantId);
                        List<LovValueDTO> statusLists = status.stream().filter(item -> item.getValue().equals(mtInstruction.getInstructionStatus())).collect(Collectors.toList());
                        if (CollectionUtils.isEmpty(statusLists)) {
                            //不存在报错
                            //获取单据num
                            MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.instructionDocPropertyGet(tenantId, mtInstruction.getSourceDocId());
                            throw new MtException("WX_WMS_SO_DELIVERY_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WX_WMS_SO_DELIVERY_0009", "WMS",dto.getMaterialLotCode(),mtInstructionDoc.getInstructionDocNum()));
                        }
                    }
                } else {
                    //条码被指定的行在该单据下
                    docLines = lineList2;
                }
            }
            if(StringUtils.isNotBlank(docLines.get(0).getTaskNum())){
                ItfLightTaskIfaceDTO itfLightTaskIfaceDTO = new ItfLightTaskIfaceDTO();
                itfLightTaskIfaceDTO.setTaskNum(docLines.get(0).getTaskNum());
                itfLightTaskIfaceDTO.setTaskStatus("OFF");
                itfLightTaskIfaceDTOS.add(itfLightTaskIfaceDTO);
            }
        }
        List<WmsCostCtrMaterialVO> wmsCostCtrMaterialVOS= new ArrayList<>();
        if(CollectionUtils.isNotEmpty(itfLightTaskIfaceDTOS)){
            List<ItfLightTaskIfaceVO> itfLightTaskIfaceVOS = itfLightTaskIfaceService.itfLightTaskIface(tenantId,itfLightTaskIfaceDTOS);
            if(CollectionUtils.isNotEmpty(itfLightTaskIfaceVOS)){
                List<String> taskNumList = itfLightTaskIfaceVOS.stream().map(ItfLightTaskIfaceVO::getTaskNum).collect(toList());
                List<ItfLightTaskIface> itfLightTaskIfaceList = itfLightTaskIfaceMapper.selectByTaskNum(tenantId,taskNumList);
                for(ItfLightTaskIfaceVO itfLightTaskIfaceVO:itfLightTaskIfaceVOS){
                    WmsCostCtrMaterialVO wmsCostCtrMaterialVO = new WmsCostCtrMaterialVO();
                    wmsCostCtrMaterialVO.setStatus(itfLightTaskIfaceVO.getStatus());
                    wmsCostCtrMaterialVO.setMessage(itfLightTaskIfaceVO.getMessage());
                    wmsCostCtrMaterialVO.setTaskNum(itfLightTaskIfaceVO.getTaskNum());
                    wmsCostCtrMaterialVO.setTaskStatus("OFF");
                    List<ItfLightTaskIface> itfLightTaskIfaces = itfLightTaskIfaceList.stream().filter(item ->item.getTaskNum().equals(itfLightTaskIfaceVO.getTaskNum())).collect(toList());
                    wmsCostCtrMaterialVO.setInstructionId(itfLightTaskIfaces.get(0).getDocLineId());
                    wmsCostCtrMaterialVOS.add(wmsCostCtrMaterialVO);
                }
            }
        }
        WmsCostCtrMaterialDTO11 wmsCostCtrMaterialDTO11 = new WmsCostCtrMaterialDTO11();
        wmsCostCtrMaterialDTO11.setWmsCostCtrMaterialDTO3List(dtoList);
        wmsCostCtrMaterialDTO11.setWmsCostCtrMaterialVOS(wmsCostCtrMaterialVOS);
        return wmsCostCtrMaterialDTO11;
    }

    private void addObjectTransaction(Long tenantId,
                                      WmsObjectTransactionVO dto,
                                      WmsCostCtrMaterialDTO3 lotDto,
                                      List<WmsObjectTransactionRequestVO> objectTransactionList) {
        objectTransactionList.add(new WmsObjectTransactionRequestVO() {{
            setInsideOrder(dto.getInternalOrderCode());
            setTransactionId(null);
            setTransactionTypeCode(dto.getTransactionTypeCode());
            setMoveType(dto.getMoveType());
            setEventId(dto.getEventId());
            setBarcode(lotDto.getMaterialLotCode());
            setMaterialLotId(lotDto.getMaterialLotId());
            setPlantCode(lotDto.getSiteCode());
            setPlantId(lotDto.getSiteId());
            setMaterialCode(lotDto.getMaterialCode());
            setMaterialId(lotDto.getMaterialId());
            setTransactionQty(dto.getTransactionQty());
            setLotNumber(lotDto.getLot());
            setTransactionUom(lotDto.getPrimaryUomCode());
            setTransactionTime(new Date());
            setSoNum(dto.getSoNum());
            setSoLineNum(dto.getSoLineNum());
            setWarehouseId(lotDto.getWarehouseId());
            setLocatorId(lotDto.getLocatorId());
            setSourceDocId(dto.getSourceDocId());
            setSourceDocLineId(dto.getSourceDocLineId());
            setSourceDocNum(dto.getSourceDocNum());
            setSourceDocLineNum(dto.getSourceDocLineNum());
            setSourceDocType(dto.getSourceDocType());
            setTransactionReasonCode("成本中心领料");
            setCostCenterCode(dto.getCostCenterCode());
            setRemark(dto.getRemark());
            setMergeFlag(WmsConstant.CONSTANT_N);
        }});
    }
}
