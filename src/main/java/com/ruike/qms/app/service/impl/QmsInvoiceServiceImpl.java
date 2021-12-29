package com.ruike.qms.app.service.impl;

import com.ruike.qms.api.dto.*;
import com.ruike.qms.app.service.QmsInvoiceService;
import com.ruike.qms.domain.vo.QmsOutsourceInvoiceVO;
import com.ruike.qms.infra.constant.QmsConstants;
import com.ruike.qms.infra.mapper.QmsInvoiceMapper;
import com.ruike.wms.api.dto.WmsPickReturnLineReceiveVO;
import com.ruike.wms.app.service.WmsCostCenterPickReturnService;
import com.ruike.wms.domain.repository.WmsPoDeliveryRepository;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.repository.MtUserRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.vo.*;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.actual.domain.vo.MtInstructionActualVO;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.api.dto.MtInstructionDocDTO2;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.*;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtMaterialVO1;
import tarzan.material.domain.vo.MtUomVO;
import tarzan.modeling.domain.entity.MtCustomerSite;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.domain.repository.*;
import tarzan.modeling.domain.vo.MtModSiteVO6;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author chaonan.hu@hand-china.com 2020-06-10 10:08:46
 * @description: 外协发货单应用服务默认实现
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class QmsInvoiceServiceImpl implements QmsInvoiceService {

    @Autowired
    private MtInstructionRepository mtInstructionRepository;
    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;
    @Autowired
    private MtSupplierRepository mtSupplierRepository;
    @Autowired
    private MtSupplierSiteRepository mtSupplierSiteRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtInstructionActualRepository mtInstructionActualRepository;
    @Autowired
    private WmsPoDeliveryRepository wmsPoDeliveryRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtUserRepository mtUserRepository;
    @Autowired
    private MtCustomerSiteRepository mtCustomerSiteRepository;
    @Autowired
    private MtExtendSettingsMapper mtExtendSettinsgMapper;
    @Autowired
    private QmsInvoiceMapper qmsInvoiceMapper;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private WmsCostCenterPickReturnService wmsCostCenterPickReturnService;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    public QmsInvoiceAssemblyReturnDTO assemblyDataForUi(Long tenantId, String instructionId) {
        //查询单据头信息
        QmsInvoiceAssemblyHeadReturnDTO assemblyHeadData = new QmsInvoiceAssemblyHeadReturnDTO();
        //1-根据instructionId调用API{instructionPropertyGet}
        MtInstruction mtInstruction = mtInstructionRepository.instructionPropertyGet(tenantId, instructionId);
        //2-采购订单号
        MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.instructionDocPropertyGet(tenantId, mtInstruction.getSourceDocId());
        String instructionDocNum = mtInstructionDoc.getInstructionDocNum();

        //3-行号
        MtExtendVO mtExtendVO = new MtExtendVO();
        mtExtendVO.setTableName("mt_instruction_attr");
        mtExtendVO.setKeyId(instructionId);
        mtExtendVO.setAttrName("INSTRUCTION_LINE_NUM");
        List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
        String lineNum = "";
        if(CollectionUtils.isNotEmpty(mtExtendAttrVOS)){
            lineNum = mtExtendAttrVOS.get(0).getAttrValue();
        }

        //4-物料编码、描述
        MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, mtInstruction.getMaterialId());
        String materialCode = mtMaterialVO.getMaterialCode();
        String materialName = mtMaterialVO.getMaterialName();
        //5-制单数量
        Double quantity = mtInstruction.getQuantity() == null ? 0.0D : mtInstruction.getQuantity();
        //6-单位
        MtUomVO mtUomVO = mtUomRepository.uomPropertyGet(tenantId, mtInstruction.getUomId());
        String uomCode = mtUomVO.getUomCode();
        //封装单据头对象
        assemblyHeadData.setInstructionDocNum(instructionDocNum);
        assemblyHeadData.setMaterialCode(materialCode);
        assemblyHeadData.setLineNum(lineNum);
        assemblyHeadData.setMaterialName(materialName);
        assemblyHeadData.setQuantity(new BigDecimal(quantity));
        assemblyHeadData.setUomCode(uomCode);
        //查询单据组件信息
        //1-根据instructionId调用API{propertyLimitInstructionQuery}
        MtInstructionVO10 mtInstructionVO10 = new MtInstructionVO10();
        mtInstructionVO10.setSourceInstructionId(instructionId);
        List<String> instructionIdList = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstructionVO10);
        List<QmsInvoiceAssemblyLineReturnDTO> assemblyLineDataS = new ArrayList<>();
        if (instructionIdList != null && instructionIdList.size() > 0) {
            //2-调用API{instructionPropertyBatchGet}获取组件信息
            List<MtInstruction> mtInstructions = mtInstructionRepository.instructionPropertyBatchGet(tenantId, instructionIdList);
            for (MtInstruction mtInstruction1 : mtInstructions) {
                //封装组件数据对象
                QmsInvoiceAssemblyLineReturnDTO assemblyLineData = new QmsInvoiceAssemblyLineReturnDTO();
                List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId,
                        new MtExtendVO1("mt_instruction_attr", Collections.singletonList(mtInstruction1.getInstructionId()), "INSTRUCTION_LINE_NUM", "MATERIAL_VERSION", "BOM_USUAGE", "QUANTITY_ORDERED"));

                for (MtExtendAttrVO1 mtExtendAttrVO1 : mtExtendAttrVO1s) {
                    switch (mtExtendAttrVO1.getAttrName()) {
                        case "INSTRUCTION_LINE_NUM":
                            assemblyLineData.setLineNum(mtExtendAttrVO1.getAttrValue());
                            break;
                        case "MATERIAL_VERSION":
                            assemblyLineData.setMaterialVersion(mtExtendAttrVO1.getAttrValue());
                            break;
                        case "BOM_USUAGE":
                            assemblyLineData.setBomUsuage(mtExtendAttrVO1.getAttrValue());
                            break;
                        case "QUANTITY_ORDERED":
                            assemblyLineData.setQuantityOrdered(mtExtendAttrVO1.getAttrValue());
                            break;
                        default:
                            break;
                    }
                }
                //7-物料编码、物料描述
                MtMaterialVO mtMaterialVO1 = mtMaterialRepository.materialPropertyGet(tenantId, mtInstruction1.getMaterialId());
                String materialCodeL = mtMaterialVO1.getMaterialCode();
                String materialNameL = mtMaterialVO1.getMaterialName();
                //8-制单数量
                Double quantityL = mtInstruction1.getQuantity()==null?0.0D:mtInstruction1.getQuantity();
                //9-单位
                MtUomVO mtUomVO1 = mtUomRepository.uomPropertyGet(tenantId, mtInstruction1.getUomId());
                String uomCodeL = mtUomVO1.getUomCode();
                assemblyLineData.setMaterialCode(materialCodeL);
                assemblyLineData.setMaterialName(materialNameL);
                assemblyLineData.setQuantity(new BigDecimal(quantityL));
                assemblyLineData.setUomCode(uomCodeL);
                assemblyLineDataS.add(assemblyLineData);
            }
        }
        //封装返回对象
        QmsInvoiceAssemblyReturnDTO result = new QmsInvoiceAssemblyReturnDTO();
        result.setQmsInvoiceAssemblyHeadReturnDTO(assemblyHeadData);
        result.setQmsInvoiceAssemblyLineReturnDTOS(assemblyLineDataS);
        return result;
    }

    @Override
    @ProcessLovValue(targetField = {"qmsInvoiceHeadReturnDTO", "",""})
    public QmsInvoiceDataReturnDTO invoiceDataForUi(Long tenantId, QmsInvoiceDataQueryDTO queryDTO) {
        List<QmsInvoiceDataQueryDTO1> dtoS = queryDTO.getQmsInvoiceDataQueryDTO1s();
        //查询头信息
        //1-根据行id获取头数据，主要是取siteId、供应商id
        MtInstruction mtInstruction = mtInstructionRepository.selectByPrimaryKey(dtoS.get(0).getInstructionId());
        MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.selectByPrimaryKey(mtInstruction.getSourceDocId());
        //2-工厂
        MtModSiteVO6 mtModSiteVO6 = new MtModSiteVO6();
        mtModSiteVO6.setSiteId(mtInstructionDoc.getSiteId());
        List<MtModSiteVO6> mtModSiteVO6s = mtModSiteRepository.propertyLimitSitePropertyQuery(tenantId, mtModSiteVO6);
        String siteName = mtModSiteVO6s.get(0).getSiteName();
        //3-供应商编码、描述
        MtSupplier mtSupplier = mtSupplierRepository.selectByPrimaryKey(mtInstructionDoc.getSupplierId());
        String supplierCode = mtSupplier.getSupplierCode();
        String supplierName = mtSupplier.getSupplierName();
        //4-发货单号
        MtNumrangeVO2 mtNumrangeVO2 = new MtNumrangeVO2();
        mtNumrangeVO2.setObjectCode("INSTRUCTION_DOC_NUM");
        mtNumrangeVO2.setObjectTypeCode("OUTSOURCING_INVOICE");
        Map<String, String> callObjectCodeList = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        callObjectCodeList.put("siteCode", mtModSiteVO6s.get(0).getSiteCode());
        callObjectCodeList.put("supplierCode", supplierCode);
        mtNumrangeVO2.setCallObjectCodeList(callObjectCodeList);
        MtNumrangeVO5 mtNumrangeVO5 = mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrangeVO2);
        String number = mtNumrangeVO5.getNumber();
        //5-单据状态
        String state = "NEW";
        //6-单据类型
        String type = "OUTSOURCING_INVOICE";
        //7-到货时间
        //调用API {instructionPropertyBatchGet}获取到所有行的预计到货时间，进而求出最早的
        Date earilyDemandTime = null;
        List<String> instructionIdList = dtoS.stream().map(QmsInvoiceDataQueryDTO1::getInstructionId).collect(Collectors.toList());
        List<MtInstruction> mtInstructions = mtInstructionRepository.instructionPropertyBatchGet(tenantId, instructionIdList);
        mtInstructions = mtInstructions.stream().filter(c -> c.getDemandTime() != null).collect(Collectors.toList());
        if(mtInstructions != null && mtInstructions.size() > 0){
            mtInstructions = mtInstructions.stream().sorted(Comparator.comparing(MtInstruction::getDemandTime)).collect(Collectors.toList());
            earilyDemandTime = mtInstructions.get(0).getDemandTime();
        }
        //8-创建人
        Long userId = DetailsHelper.getUserDetails().getUserId();
        MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, userId);
        String userName = mtUserInfo.getRealName();
        //9-收货地址
        String customerSiteId = mtInstructionDoc.getCustomerSiteId();
        MtCustomerSite mtCustomerSite = mtCustomerSiteRepository.selectByPrimaryKey(customerSiteId);
        //封装头数据信息
        QmsInvoiceHeadReturnDTO qmsInvoiceHeadReturnDTO = new QmsInvoiceHeadReturnDTO();
        qmsInvoiceHeadReturnDTO.setSiteName(siteName);
        qmsInvoiceHeadReturnDTO.setNumber(number);
        qmsInvoiceHeadReturnDTO.setSupplierCode(supplierCode);
        qmsInvoiceHeadReturnDTO.setSupplierName(supplierName);
        qmsInvoiceHeadReturnDTO.setState(state);
        qmsInvoiceHeadReturnDTO.setType(type);
        qmsInvoiceHeadReturnDTO.setEarilyDemandTime(earilyDemandTime);
        qmsInvoiceHeadReturnDTO.setUserName(userName);
        qmsInvoiceHeadReturnDTO.setReceivingAddress(mtCustomerSite == null ? "":mtCustomerSite.getDescription());
        //查询行信息
        List<QmsInvoiceLineReturnDTO> qmsInvoiceLineReturnDTOS = new ArrayList<>();
        List<QmsInvoiceInstructionDTO> mtInstructionS = new ArrayList<>();
        for (QmsInvoiceDataQueryDTO1 dto1 : dtoS) {
            //行上的数量
            MtInstruction mtInstruction2 = mtInstructionRepository.selectByPrimaryKey(dto1.getInstructionId());
            BigDecimal quantity = dto1.getQuantity();
            //查询该采购订单行的信息及物料版本，留待最终发货单生成行时使用
            QmsInvoiceInstructionDTO qmsInvoiceInstructionDTO = new QmsInvoiceInstructionDTO();
            BeanUtils.copyProperties(mtInstruction2, qmsInvoiceInstructionDTO);
            MtExtendVO mtExtendVO = new MtExtendVO();
            mtExtendVO.setTableName("mt_instruction_attr");
            mtExtendVO.setKeyId(qmsInvoiceInstructionDTO.getInstructionId());
            mtExtendVO.setAttrName("MATERIAL_VERSION");
            List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            if (mtExtendAttrVOList != null && mtExtendAttrVOList.size() > 0) {
                qmsInvoiceInstructionDTO.setMaterialVersion(mtExtendAttrVOList.get(0).getAttrValue());
            }
            qmsInvoiceInstructionDTO.setQuantityUi(quantity);
            mtInstructionS.add(qmsInvoiceInstructionDTO);
            //1-根据采购订单行id调用API{propertyLimitInstructionQuery}获取组件id
            MtInstructionVO10 mtInstructionVO10 = new MtInstructionVO10();
            mtInstructionVO10.setSourceInstructionId(dto1.getInstructionId());
            List<String> assemblyIds = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstructionVO10);
            for (String assemblyId : assemblyIds) {
                QmsInvoiceLineReturnDTO qmsInvoiceLineReturnDTO = new QmsInvoiceLineReturnDTO();
                //2-根据组件id调用API{instructionPropertyGet}获取数据
                MtInstruction mtInstruction1 = mtInstructionRepository.instructionPropertyGet(tenantId, assemblyId);
                //3-物料编码、描述
                MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, mtInstruction1.getMaterialId());
                String materialCodeL = mtMaterialVO.getMaterialCode();
                String materialNameL = mtMaterialVO.getMaterialName();

                //更换产品api
                QmsInvoiceAssemblyLineReturnDTO assemblyLineData = new QmsInvoiceAssemblyLineReturnDTO();
                List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId,
                        new MtExtendVO1("mt_instruction_attr", Collections.singletonList(mtInstruction1.getInstructionId()), "INSTRUCTION_LINE_NUM", "MATERIAL_VERSION", "BOM_USUAGE"));

                for (MtExtendAttrVO1 mtExtendAttrVO1 : mtExtendAttrVO1s) {
                    switch (mtExtendAttrVO1.getAttrName()) {
                        case "INSTRUCTION_LINE_NUM":
                            assemblyLineData.setLineNum(mtExtendAttrVO1.getAttrValue());
                            break;
                        case "MATERIAL_VERSION":
                            assemblyLineData.setMaterialVersion(mtExtendAttrVO1.getAttrValue());
                            break;
                        case "BOM_USUAGE":
                            assemblyLineData.setBomUsuage(mtExtendAttrVO1.getAttrValue());
                            break;
                        default:
                            break;
                    }
                }

                //4-物料版本
                String materialVersion = assemblyLineData.getMaterialVersion();
                //5-制单数量(行上的数量*组件的BOM用量)
                String attrValue = assemblyLineData.getBomUsuage();
                BigDecimal quantity1 = BigDecimal.ZERO;
                BigDecimal lineQty = BigDecimal.valueOf(1);
                if(mtInstruction.getQuantity() != null && Double.valueOf(0).compareTo(mtInstruction.getQuantity()) != 0){
                    lineQty = BigDecimal.valueOf(mtInstruction.getQuantity());
                }

                if(StringUtils.isNotBlank(attrValue)){
                    quantity1 = quantity.multiply(new BigDecimal(attrValue)).divide(lineQty, BigDecimal.ROUND_HALF_DOWN).setScale(3);
                }
                //6-单位
                MtMaterialVO1 mtMaterialVO1 = mtMaterialRepository.materialUomGet(tenantId, mtInstruction1.getMaterialId());
                String uomCode = mtMaterialVO1.getPrimaryUomCode();
                //7-采购订单行号（采购订单头的num与行号拼接而成）
                String instructionDocNum = mtInstructionDocRepository.selectByPrimaryKey(mtInstruction2.getSourceDocId()).getInstructionDocNum();
                mtExtendVO.setAttrName("INSTRUCTION_LINE_NUM");
                String attrValue1 =  assemblyLineData.getLineNum();
                String orderLineNum = instructionDocNum + "#" + attrValue1;
                //封装行数据信息
                qmsInvoiceLineReturnDTO.setAssemblyId(assemblyId);
                qmsInvoiceLineReturnDTO.setMaterialId(mtInstruction1.getMaterialId());
                qmsInvoiceLineReturnDTO.setMaterialCode(materialCodeL);
                qmsInvoiceLineReturnDTO.setMaterialName(materialNameL);
                qmsInvoiceLineReturnDTO.setMaterialVersion(materialVersion);
                qmsInvoiceLineReturnDTO.setQuantity(quantity1);
                qmsInvoiceLineReturnDTO.setUomCode(uomCode);
                qmsInvoiceLineReturnDTO.setOrderLineNum(orderLineNum);
                qmsInvoiceLineReturnDTOS.add(qmsInvoiceLineReturnDTO);
            }
        }
        //组件信息根据物料和版本汇总
        Map<String, List<QmsInvoiceLineReturnDTO>> listMap = qmsInvoiceLineReturnDTOS.stream().collect(Collectors.groupingBy(qmsInvoiceLineReturnDTO ->
                qmsInvoiceLineReturnDTO.getMaterialCode() + "_" + qmsInvoiceLineReturnDTO.getMaterialVersion()));
        List<QmsInvoiceLineReturnDTO> qmsInvoiceLineReturnDTOList = new ArrayList<>();
        Long lineNum = 10L;
        for (String key : listMap.keySet()) {
            List<QmsInvoiceLineReturnDTO> qmsInvoiceLineReturnDTOS1 = listMap.get(key);
            QmsInvoiceLineReturnDTO qmsInvoiceLineReturnDTO = qmsInvoiceLineReturnDTOS1.get(0);
            //获取这个物料+版本分类下汇总的制单数量和,及采购订单行号
            BigDecimal quantity = new BigDecimal(0);
            String orderLineNum = "";
            for (QmsInvoiceLineReturnDTO qmsInvoiceLineReturnDTO1 : qmsInvoiceLineReturnDTOS1) {
                quantity = quantity.add(qmsInvoiceLineReturnDTO1.getQuantity());
                orderLineNum = orderLineNum + qmsInvoiceLineReturnDTO1.getOrderLineNum();
            }
            qmsInvoiceLineReturnDTO.setQuantity(quantity);
            qmsInvoiceLineReturnDTO.setLineNum(lineNum);
            qmsInvoiceLineReturnDTO.setOrderLineNum(orderLineNum);
            lineNum = lineNum + 10;
            qmsInvoiceLineReturnDTOList.add(qmsInvoiceLineReturnDTO);
        }
        //实际制单数量
        //调用API {propertyLimitInstructionDocQuery}
        MtInstructionDocVO4 mtInstructionDocVO4 = new MtInstructionDocVO4();
        mtInstructionDocVO4.setInstructionDocType("OVER");
        mtInstructionDocVO4.setSupplierId(mtInstructionDoc.getSupplierId());
        List<String> instructionDocIdList = mtInstructionDocRepository.propertyLimitInstructionDocQuery(tenantId, mtInstructionDocVO4);
        for (QmsInvoiceLineReturnDTO qmsInvoiceLineReturnDTO : qmsInvoiceLineReturnDTOList) {
            //查询超发数量，如果找不到，则为0
            //超发数量
            BigDecimal superhairQuantity = new BigDecimal(0);
            //超发库存
            BigDecimal superhairInventory = new BigDecimal(0);
            //原占用数量
            Double coverQtyFirst = 0D;
            String instructionId = null;
            if(instructionDocIdList != null && instructionDocIdList.size() > 0){
                //调用API{propertyLimitInstructionQuery} 注：下面的list实际上只会存在一个值
                MtInstructionVO10 mtInstructionVO10 = new MtInstructionVO10();
                mtInstructionVO10.setSourceDocId(instructionDocIdList.get(0));
                mtInstructionVO10.setMaterialId(qmsInvoiceLineReturnDTO.getMaterialId());
                List<String> instructionIdS = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstructionVO10);
                if(instructionIdS != null && instructionIdS.size() > 0){
                    //调用API{instructionPropertyGet}
                    MtInstruction mtInstruction1 = mtInstructionRepository.instructionPropertyGet(tenantId, instructionIdS.get(0));
                    if(mtInstruction1 != null && mtInstruction1.getQuantity() != null && mtInstruction1.getCoverQty() != null){
                        superhairInventory = new BigDecimal(mtInstruction1.getQuantity());
                        superhairQuantity = new BigDecimal(mtInstruction1.getQuantity() - mtInstruction1.getCoverQty());
                    }
                    if(mtInstruction1 != null){
                        coverQtyFirst = (mtInstruction1.getCoverQty() == null ? 0.0D : mtInstruction1.getCoverQty());
                    }
                    instructionId = instructionIdS.get(0);
                }
            }
            //计算实际制单数量 （制单数量-超发数量），如果该值小于0，则显示为0
            BigDecimal actualQuantity = qmsInvoiceLineReturnDTO.getQuantity().subtract(superhairQuantity);
            if (actualQuantity.compareTo(new BigDecimal(0)) == -1) {
                qmsInvoiceLineReturnDTO.setActualQuantity(new BigDecimal(0));
            } else {
                qmsInvoiceLineReturnDTO.setActualQuantity(actualQuantity);
            }
            //更新单据占用数量
            Double coverQty = 0.0D;
            if (qmsInvoiceLineReturnDTO.getQuantity().compareTo(superhairQuantity) == 1) {
                //如果制单数量 > 超发数量，则本次用尽超发库存，最终单据占用 = 超发库存
                coverQty = superhairInventory.doubleValue();
            } else {
                //否则，单据占用数量 = 原占用数量 + 超发数量 – 制单数量
                coverQty = coverQtyFirst + superhairQuantity.doubleValue() - qmsInvoiceLineReturnDTO.getQuantity().doubleValue();
            }
            //调用API {instructionUpdate}更新单据占用数量
            if (StringUtils.isNotEmpty(instructionId)) {
                MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
                mtEventCreateVO.setEventTypeCode("OUTSOURCING_INVOICE_CREATE");
                String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);

                MtInstructionVO mtInstructionVO = new MtInstructionVO();
                mtInstructionVO.setInstructionId(instructionId);
                mtInstructionVO.setCoverQty(coverQty);
                mtInstructionVO.setEventId(eventId);
                mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, QmsConstants.ConstantValue.NO);
            }
        }
        //封装返回对象
        QmsInvoiceDataReturnDTO qmsInvoiceDataReturnDTO = new QmsInvoiceDataReturnDTO();
        qmsInvoiceDataReturnDTO.setQmsInvoiceHeadReturnDTO(qmsInvoiceHeadReturnDTO);
        qmsInvoiceDataReturnDTO.setQmsInvoiceLineReturnDTOList(qmsInvoiceLineReturnDTOList);
        qmsInvoiceDataReturnDTO.setMtInstructionS(mtInstructionS);
        return qmsInvoiceDataReturnDTO;
    }

    @Override
    @ProcessLovValue(targetField = {"qmsInvoiceHeadReturnDTO", "",""})
    public QmsInvoiceDataReturnDTO outsourceInvoiceQuery(Long tenantId, QmsOutsourceInvoiceVO invoiceVO) {
        QmsInvoiceDataReturnDTO dto =  new QmsInvoiceDataReturnDTO();
        //查询头信息
        //1-根据行id获取头数据，主要是取siteId、供应商id
        MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.selectByPrimaryKey(invoiceVO.getInstructionDocId());
        //检验 存在外协未关闭的 不许创建
        Integer invoiceCount = qmsInvoiceMapper.queryInvoiceListByPoNum(tenantId, mtInstructionDoc.getInstructionDocNum());
        if(invoiceCount.compareTo(Integer.valueOf(0)) > 0 ){
            throw new MtException("QMS_MATERIAL_INSP_P0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_P0044", "QMS"));
        }
        //2-工厂
        MtModSiteVO6 mtModSiteVO6 = new MtModSiteVO6();
        mtModSiteVO6.setSiteId(mtInstructionDoc.getSiteId());
        List<MtModSiteVO6> mtModSiteVO6s = mtModSiteRepository.propertyLimitSitePropertyQuery(tenantId, mtModSiteVO6);
        String siteName = mtModSiteVO6s.get(0).getSiteName();
        //3-供应商编码、描述
        MtSupplier mtSupplier = mtSupplierRepository.selectByPrimaryKey(mtInstructionDoc.getSupplierId());
        String supplierCode = mtSupplier.getSupplierCode();
        String supplierName = mtSupplier.getSupplierName();
        //4-发货单号
        MtNumrangeVO2 mtNumrangeVO2 = new MtNumrangeVO2();
        mtNumrangeVO2.setObjectCode("INSTRUCTION_DOC_NUM");
        mtNumrangeVO2.setObjectTypeCode("OUTSOURCING_INVOICE");
        Map<String, String> callObjectCodeList = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        callObjectCodeList.put("siteCode", mtModSiteVO6s.get(0).getSiteCode());
        callObjectCodeList.put("supplierCode", supplierCode);
        mtNumrangeVO2.setCallObjectCodeList(callObjectCodeList);
        MtNumrangeVO5 mtNumrangeVO5 = mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrangeVO2);
        String number = mtNumrangeVO5.getNumber();
        //5-单据状态
        String state = "NEW";
        //6-单据类型
        String type = "OUTSOURCING_INVOICE";
        //7-到货时间 默认当前的
        Date earilyDemandTime = new Date();
        //8-创建人
        Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
        MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, userId);
        String userName = mtUserInfo.getRealName();
        //封装头数据信息
        QmsInvoiceHeadReturnDTO qmsInvoiceHeadReturnDTO = new QmsInvoiceHeadReturnDTO();
        qmsInvoiceHeadReturnDTO.setSiteName(siteName);
        qmsInvoiceHeadReturnDTO.setNumber(number);
        qmsInvoiceHeadReturnDTO.setSupplierCode(supplierCode);
        qmsInvoiceHeadReturnDTO.setSupplierName(supplierName);
        qmsInvoiceHeadReturnDTO.setState(state);
        qmsInvoiceHeadReturnDTO.setType(type);
        qmsInvoiceHeadReturnDTO.setEarilyDemandTime(earilyDemandTime);
        qmsInvoiceHeadReturnDTO.setUserName(userName);
        dto.setQmsInvoiceHeadReturnDTO(qmsInvoiceHeadReturnDTO);

        List<MtInstruction> lineDetailList = qmsInvoiceMapper.queryInvoiceLineList(tenantId, invoiceVO.getInstructionDocId());
        //查询行信息
        List<QmsInvoiceLineReturnDTO> qmsInvoiceLineReturnDTOS = new ArrayList<>();
        List<QmsInvoiceInstructionDTO> mtInstructionS = new ArrayList<>();
        for (MtInstruction mtInstruction : lineDetailList) {
            //查询该采购订单行的信息及物料版本，留待最终发货单生成行时使用
            QmsInvoiceInstructionDTO qmsInvoiceInstructionDTO = new QmsInvoiceInstructionDTO();
            BeanUtils.copyProperties(mtInstruction, qmsInvoiceInstructionDTO);
            MtExtendVO mtExtendVO = new MtExtendVO();
            mtExtendVO.setTableName("mt_instruction_attr");
            mtExtendVO.setKeyId(qmsInvoiceInstructionDTO.getInstructionId());
            mtExtendVO.setAttrName("MATERIAL_VERSION");
            List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            if (mtExtendAttrVOList != null && mtExtendAttrVOList.size() > 0) {
                qmsInvoiceInstructionDTO.setMaterialVersion(mtExtendAttrVOList.get(0).getAttrValue());
            }
//            qmsInvoiceInstructionDTO.setQuantityUi(quantity);
            mtInstructionS.add(qmsInvoiceInstructionDTO);
            //1-根据采购订单行id调用API{propertyLimitInstructionQuery}获取组件id
            MtInstructionVO10 mtInstructionVO10 = new MtInstructionVO10();
            mtInstructionVO10.setSourceInstructionId(mtInstruction.getInstructionId());
            List<String> assemblyIds = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstructionVO10);
            for (String assemblyId : assemblyIds) {
                QmsInvoiceLineReturnDTO qmsInvoiceLineReturnDTO = new QmsInvoiceLineReturnDTO();
                //2-根据组件id调用API{instructionPropertyGet}获取数据
                MtInstruction mtInstruction1 = mtInstructionRepository.instructionPropertyGet(tenantId, assemblyId);
                //3-物料编码、描述
                MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, mtInstruction1.getMaterialId());
                String materialCodeL = mtMaterialVO.getMaterialCode();
                String materialNameL = mtMaterialVO.getMaterialName();

                //更换产品api
                QmsInvoiceAssemblyLineReturnDTO assemblyLineData = new QmsInvoiceAssemblyLineReturnDTO();
                List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId,
                        new MtExtendVO1("mt_instruction_attr", Collections.singletonList(mtInstruction1.getInstructionId()), "INSTRUCTION_LINE_NUM", "MATERIAL_VERSION", "BOM_USUAGE"));

                for (MtExtendAttrVO1 mtExtendAttrVO1 : mtExtendAttrVO1s) {
                    switch (mtExtendAttrVO1.getAttrName()) {
                        case "INSTRUCTION_LINE_NUM":
                            assemblyLineData.setLineNum(mtExtendAttrVO1.getAttrValue());
                            break;
                        case "MATERIAL_VERSION":
                            assemblyLineData.setMaterialVersion(mtExtendAttrVO1.getAttrValue());
                            break;
                        case "BOM_USUAGE":
                            assemblyLineData.setBomUsuage(mtExtendAttrVO1.getAttrValue());
                            break;
                    }
                }

                //4-物料版本
                String materialVersion = assemblyLineData.getMaterialVersion();
                //5-制单数量(行上的数量*组件的BOM用量)
                BigDecimal quantity1 = StringUtils.isNotBlank(assemblyLineData.getBomUsuage()) ? BigDecimal.valueOf(Double.valueOf(assemblyLineData.getBomUsuage())) : BigDecimal.ZERO;

                //6-单位
                MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtInstruction1.getUomId());
                String uomCode = mtUom.getUomCode();
                mtExtendVO.setAttrName("INSTRUCTION_LINE_NUM");
                //封装行数据信息
                qmsInvoiceLineReturnDTO.setAssemblyId(assemblyId);
                qmsInvoiceLineReturnDTO.setMaterialId(mtInstruction1.getMaterialId());
                qmsInvoiceLineReturnDTO.setMaterialCode(materialCodeL);
                qmsInvoiceLineReturnDTO.setMaterialName(materialNameL);
                qmsInvoiceLineReturnDTO.setMaterialVersion(materialVersion);
                qmsInvoiceLineReturnDTO.setQuantity(quantity1);
                qmsInvoiceLineReturnDTO.setUomCode(uomCode);
                qmsInvoiceLineReturnDTOS.add(qmsInvoiceLineReturnDTO);
            }
        }

        //组件信息根据物料和版本汇总
        Map<String, List<QmsInvoiceLineReturnDTO>> listMap = qmsInvoiceLineReturnDTOS.stream().collect(Collectors.groupingBy(qmsInvoiceLineReturnDTO ->
                qmsInvoiceLineReturnDTO.getMaterialCode() + "_" + qmsInvoiceLineReturnDTO.getMaterialVersion()));
        List<QmsInvoiceLineReturnDTO> qmsInvoiceLineReturnDTOList = new ArrayList<>();

        //发料仓库
        List<MtModLocator> mtModLocatorList = mtModLocatorRepository.selectByCondition(Condition.builder(MtModLocator.class)
                .andWhere(Sqls.custom().andEqualTo(MtModLocator.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtModLocator.FIELD_ENABLE_FLAG, QmsConstants.ConstantValue.YES)
                        .andEqualTo(MtModLocator.FIELD_LOCATOR_CODE, "1031")).build());
        for (String key : listMap.keySet()) {
            List<QmsInvoiceLineReturnDTO> qmsInvoiceLineReturnDTOS1 = listMap.get(key);
            QmsInvoiceLineReturnDTO qmsInvoiceLineReturnDTO = qmsInvoiceLineReturnDTOS1.get(0);
            //获取这个物料+版本分类下汇总的制单数量和,及采购订单行号
            BigDecimal quantity = new BigDecimal(0);
            for (QmsInvoiceLineReturnDTO qmsInvoiceLineReturnDTO1 : qmsInvoiceLineReturnDTOS1) {
                quantity = quantity.add(qmsInvoiceLineReturnDTO1.getQuantity());
            }

            //单位校验
            List<String> uomCodeList = qmsInvoiceLineReturnDTOS1.stream().map(QmsInvoiceLineReturnDTO::getUomCode).distinct().collect(Collectors.toList());
            if(uomCodeList.size() > 1){
                throw new MtException("QMS_MATERIAL_LOT_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_MATERIAL_LOT_0002", "MATERIAL_LOT"));
            }
            qmsInvoiceLineReturnDTO.setQuantity(quantity);
            //发料仓库
            if(CollectionUtils.isNotEmpty(mtModLocatorList)){
                qmsInvoiceLineReturnDTO.setDeliveryWarehouseId(mtModLocatorList.get(0).getLocatorId());
                qmsInvoiceLineReturnDTO.setDeliveryWarehouseName(mtModLocatorList.get(0).getLocatorName());
                qmsInvoiceLineReturnDTO.setDeliveryWarehouseCode(mtModLocatorList.get(0).getLocatorName());

                //库存现有量
                List<WmsPickReturnLineReceiveVO> receiveVOList = new ArrayList<>();
                WmsPickReturnLineReceiveVO receiveVO = new WmsPickReturnLineReceiveVO();
                receiveVO.setIndex(0L);
                receiveVO.setMaterialId(qmsInvoiceLineReturnDTO.getMaterialId());
                receiveVO.setToStorageId(mtModLocatorList.get(0).getLocatorId());
                receiveVOList.add(receiveVO);
                List<WmsPickReturnLineReceiveVO> lineReceiveVOList = wmsCostCenterPickReturnService.queryLocatorQuantity(tenantId, receiveVOList);
                if(CollectionUtils.isNotEmpty(lineReceiveVOList)){
                    qmsInvoiceLineReturnDTO.setInventoryQuantity(lineReceiveVOList.get(0).getOnhandQuantity());
                }
            }
            qmsInvoiceLineReturnDTOList.add(qmsInvoiceLineReturnDTO);
        }
        //实际制单数量
        for (QmsInvoiceLineReturnDTO qmsInvoiceLineReturnDTO : qmsInvoiceLineReturnDTOList) {
            //查询超发数量，如果找不到，则为0
            //超发数量
            BigDecimal superhairQuantity = new BigDecimal(0);

            List<MtInstruction> mtInstructionList = qmsInvoiceMapper.queryOverInvoiceInstructionList(tenantId, mtInstructionDoc.getSupplierId(), qmsInvoiceLineReturnDTO.getMaterialId(), qmsInvoiceLineReturnDTO.getMaterialVersion());
            if(CollectionUtils.isNotEmpty(mtInstructionList)){
                MtInstruction mtInstruction1 = mtInstructionList.get(0);
                if(mtInstruction1 != null){
                    if(mtInstruction1.getQuantity() == null){
                        mtInstruction1.setQuantity(0.0D);
                    }
                    if(mtInstruction1.getCoverQty() == null){
                       mtInstruction1.setCoverQty(0.0D);
                    }
                    superhairQuantity = BigDecimal.valueOf(mtInstruction1.getQuantity()).subtract(BigDecimal.valueOf(mtInstruction1.getCoverQty()));
                }
            }

            //超发库存
            qmsInvoiceLineReturnDTO.setOverQuantity(superhairQuantity.compareTo(BigDecimal.ZERO) > 0 ? superhairQuantity : BigDecimal.ZERO);

            //计算实际制单数量 （制单数量-超发数量），如果该值小于0，则显示为0
            BigDecimal actualQuantity = qmsInvoiceLineReturnDTO.getQuantity().subtract(qmsInvoiceLineReturnDTO.getOverQuantity());
            if (actualQuantity.compareTo(BigDecimal.ZERO) == -1) {
                qmsInvoiceLineReturnDTO.setActualQuantity(BigDecimal.ZERO);
            } else {
                qmsInvoiceLineReturnDTO.setActualQuantity(actualQuantity);
            }
            qmsInvoiceLineReturnDTO.setSubQty(qmsInvoiceLineReturnDTO.getActualQuantity().subtract(qmsInvoiceLineReturnDTO.getInventoryQuantity()));
        }
        //封装返回对象
        Long lineNum = 10L;
        List<QmsInvoiceLineReturnDTO> dtoList = qmsInvoiceLineReturnDTOList.stream().sorted(Comparator.comparing(QmsInvoiceLineReturnDTO::getSubQty).reversed()).collect(Collectors.toList());
        List<QmsInvoiceLineReturnDTO> lineReturnDTOList = dtoList.stream().map(inv ->{int index = dtoList.indexOf(inv); Long num = lineNum * (index+1); inv.setLineNum(num); return inv;}).collect(Collectors.toList());
        QmsInvoiceDataReturnDTO qmsInvoiceDataReturnDTO = new QmsInvoiceDataReturnDTO();
        qmsInvoiceDataReturnDTO.setQmsInvoiceHeadReturnDTO(qmsInvoiceHeadReturnDTO);
        qmsInvoiceDataReturnDTO.setQmsInvoiceLineReturnDTOList(lineReturnDTOList);
        qmsInvoiceDataReturnDTO.setMtInstructionS(mtInstructionS);
        return qmsInvoiceDataReturnDTO;
    }

    @Override
    public void invoiceCreate(Long tenantId, QmsInvoiceDataReturnDTO dto) {
        QmsInvoiceHeadReturnDTO headData = dto.getQmsInvoiceHeadReturnDTO();
        List<QmsInvoiceLineReturnDTO> assemblyData = dto.getQmsInvoiceLineReturnDTOList();
        List<QmsInvoiceInstructionDTO> mtInstructionS = dto.getMtInstructionS();

        MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.selectByPrimaryKey(mtInstructionS.get(0).getSourceDocId());

        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "OUTSOURCING_INVOICE_CREATE");

        //1-调用API {eventRequestCreate}获取事件请求id
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("OUTSOURCING_INVOICE_CREATE");
        mtEventCreateVO.setEventRequestId(eventRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
        //2-调用API {instructiondocupdate} 生成发货单头
        MtInstructionDocDTO2 mtInstructionDocDTO2 = new MtInstructionDocDTO2();
        mtInstructionDocDTO2.setInstructionDocNum(headData.getNumber());
        mtInstructionDocDTO2.setInstructionDocType("OUTSOURCING_INVOICE");
        mtInstructionDocDTO2.setSiteId(mtInstructionDoc.getSiteId());
        mtInstructionDocDTO2.setSupplierId(mtInstructionDoc.getSupplierId());
        mtInstructionDocDTO2.setSupplierSiteId(mtInstructionDoc.getSupplierId());
        mtInstructionDocDTO2.setCustomerId(mtInstructionDoc.getCustomerId());
        mtInstructionDocDTO2.setCustomerSiteId(mtInstructionDoc.getCustomerSiteId());
        mtInstructionDocDTO2.setInstructionDocStatus("RELEASED");
        mtInstructionDocDTO2.setDemandTime(headData.getEarilyDemandTime());
        mtInstructionDocDTO2.setRemark(headData.getRemark());
        mtInstructionDocDTO2.setEventRequestId(eventRequestId);
        MtInstructionDocVO3 mtInstructionDocVO3 = mtInstructionDocRepository.instructionDocUpdate(tenantId, mtInstructionDocDTO2, QmsConstants.ConstantValue.NO);

        //poNum
        MtExtendVO5 poNumAttr = new MtExtendVO5();
        poNumAttr.setAttrName("PO_NUM");
        poNumAttr.setAttrValue(mtInstructionDoc.getInstructionDocNum());
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_doc_attr",
                mtInstructionDocVO3.getInstructionDocId(), eventId, Collections.singletonList(poNumAttr));

        //5-调用API {instructionupdate}发货单组件创建
        Long lineNum2 = 10L;
        //行状态是否全为完成
        Boolean flag = false;
        for (QmsInvoiceLineReturnDTO assembly : assemblyData) {
            MtInstructionVO mtInstructionVO = new MtInstructionVO();
            mtInstructionVO.setSourceDocId(mtInstructionDocVO3.getInstructionDocId());
            mtInstructionVO.setInstructionType("RETURN_TO_SUPPLIER");
            mtInstructionVO.setMaterialId(assembly.getMaterialId());
            mtInstructionVO.setQuantity(assembly.getQuantity().doubleValue());
            MtUom mtUom = new MtUom();
            mtUom.setUomCode(assembly.getUomCode());
            mtUom = mtUomRepository.selectOne(mtUom);
            mtInstructionVO.setUomId(mtUom.getUomId());
            //实际制单数量为零则更新行状态为COMPLETED
            if (BigDecimal.ZERO.compareTo(assembly.getActualQuantity()) == 0){
                mtInstructionVO.setInstructionStatus(WmsConstant.InstructionStatus.COMPLETED);
            }else {
                mtInstructionVO.setInstructionStatus(WmsConstant.InstructionStatus.RELEASED);
                flag = true;
            }
            mtInstructionVO.setSiteId(mtInstructionDoc.getSiteId());
            mtInstructionVO.setFromSiteId(mtInstructionDoc.getSiteId());
            mtInstructionVO.setSupplierId(mtInstructionDocDTO2.getSupplierId());
            mtInstructionVO.setSupplierSiteId(mtInstructionDocDTO2.getSupplierSiteId());
            mtInstructionVO.setDemandTime(mtInstructionDocDTO2.getDemandTime());
            mtInstructionVO.setEventRequestId(eventRequestId);
            mtInstructionVO.setBusinessType("OUTSOURCING_SENDING");
            mtInstructionVO.setFromLocatorId(assembly.getDeliveryWarehouseId());
            MtInstructionVO6 mtInstructionVO6 = mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, QmsConstants.ConstantValue.NO);
            //更新实际制单数量
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("ACTUAL_ORDERED_QTY");
            mtExtendVO5.setAttrValue(assembly.getActualQuantity().toString());
            //物料版本
            MtExtendVO5 versionAttr = new MtExtendVO5();
            versionAttr.setAttrName("MATERIAL_VERSION");
            versionAttr.setAttrValue(assembly.getMaterialVersion());
            List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
            mtExtendVO5List.add(mtExtendVO5);
            mtExtendVO5List.add(versionAttr);
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_attr",
                    mtInstructionVO6.getInstructionId(), eventId, mtExtendVO5List);
            //更新占用数量 实际制单数 < 制单数量
            List<MtInstruction> mtInstructionList = qmsInvoiceMapper.queryOverInvoiceInstructionList(tenantId, mtInstructionDoc.getSupplierId(), assembly.getMaterialId(), assembly.getMaterialVersion());
            if(BigDecimal.ZERO.compareTo(assembly.getActualQuantity()) == 0){
                if(CollectionUtils.isNotEmpty(mtInstructionList)){
                    //扣减超发单对应物料行超发数量
                    MtInstruction mtInstruction = mtInstructionList.get(0);
                    mtInstruction.setQuantity(assembly.getOverQuantity().subtract(assembly.getQuantity()).doubleValue());;
                    mtInstructionRepository.updateByPrimaryKeySelective(mtInstruction);
                }
            }else if(assembly.getActualQuantity().compareTo(assembly.getQuantity()) < 0){
                if(CollectionUtils.isNotEmpty(mtInstructionList)){
                    MtInstruction mtInstruction = mtInstructionList.get(0);
                    BigDecimal coverQty = mtInstruction.getCoverQty() != null ? BigDecimal.valueOf(mtInstruction.getCoverQty()) : BigDecimal.ZERO;
                    BigDecimal resultQty = assembly.getQuantity().subtract(assembly.getActualQuantity());
                    mtInstruction.setCoverQty(coverQty.add(resultQty).doubleValue());
                    mtInstructionRepository.updateByPrimaryKeySelective(mtInstruction);
                }
            }

            //组件行号生成
            MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
            mtExtendVO10.setKeyId(mtInstructionVO6.getInstructionId());
            mtExtendVO10.setEventId(eventId);
            mtExtendVO5.setAttrName("INSTRUCTION_LINE_NUM");
            mtExtendVO5.setAttrValue(lineNum2.toString());
            mtExtendVO10.setAttrs(Collections.singletonList(mtExtendVO5));
            mtInstructionRepository.instructionAttrPropertyUpdate(tenantId, mtExtendVO10);
            lineNum2 = lineNum2 + 10;
            //调用API{instructionActualUpdate}创建指令实绩
            MtInstructionActualVO mtInstructionActualVO = new MtInstructionActualVO();
            mtInstructionActualVO.setInstructionId(mtInstructionVO6.getInstructionId());
            mtInstructionActualVO.setInstructionType("RETURN_TO_SUPPLIER");
            mtInstructionActualVO.setBusinessType("OUTSOURCING_SENDING");
            mtInstructionActualVO.setMaterialId(mtInstructionVO.getMaterialId());
            mtInstructionActualVO.setUomId(mtInstructionVO.getUomId());
            mtInstructionActualVO.setSourceOrderType(WmsConstant.DocType.DELIVERY_DOC);
            mtInstructionActualVO.setSourceOrderId(mtInstructionDocVO3.getInstructionDocId());
            mtInstructionActualVO.setFromSiteId(mtInstructionVO.getFromSiteId());
            mtInstructionActualVO.setFromLocatorId(mtInstructionVO.getFromLocatorId());
            mtInstructionActualVO.setSupplierId(mtInstructionVO.getSupplierId());
            mtInstructionActualVO.setSupplierSiteId(mtInstructionVO.getSupplierSiteId());
            mtInstructionActualVO.setActualQty(0.0D);
            mtInstructionActualVO.setEventId(eventId);
            mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionActualVO);
        }
        if(!flag){
            MtInstructionDocDTO2 mtInstructionDoc2 = new MtInstructionDocDTO2();
            mtInstructionDoc2.setInstructionDocId(mtInstructionDocVO3.getInstructionDocId());
            mtInstructionDoc2.setInstructionDocType("OUTSOURCING_INVOICE");
            mtInstructionDoc2.setInstructionDocStatus(WmsConstant.InstructionStatus.COMPLETED);
            mtInstructionDoc2.setEventId(eventId);
            mtInstructionDocRepository.instructionDocUpdate(tenantId, mtInstructionDoc2, "N");
        }
    }
}
