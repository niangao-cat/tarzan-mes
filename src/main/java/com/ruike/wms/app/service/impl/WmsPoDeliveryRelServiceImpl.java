package com.ruike.wms.app.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ruike.hme.api.dto.HmeObjectRecordLockDTO;
import com.ruike.hme.app.service.HmeObjectRecordLockService;
import com.ruike.hme.domain.entity.HmeObjectRecordLock;
import com.ruike.hme.domain.repository.HmeObjectRecordLockRepository;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.itf.app.service.ItfSrmInstructionIfaceService;
import com.ruike.itf.domain.entity.ItfSrmInstructionIface;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.qms.domain.entity.QmsIqcHeader;
import com.ruike.qms.domain.entity.QmsMaterialInspExempt;
import com.ruike.qms.domain.repository.QmsIqcHeaderRepository;
import com.ruike.qms.domain.repository.QmsMaterialInspExemptRepository;
import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsCommonApiService;
import com.ruike.wms.app.service.WmsPoDeliveryRelService;
import com.ruike.wms.domain.entity.WmsDocLotRel;
import com.ruike.wms.domain.entity.WmsMaterialLotDocRel;
import com.ruike.wms.domain.entity.WmsPoDeliveryRel;
import com.ruike.wms.domain.entity.WmsPutInStorageTask;
import com.ruike.wms.domain.repository.*;
import com.ruike.wms.domain.vo.*;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsPoDeliveryRelMapper;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.util.NumberHelper;
import io.tarzan.common.domain.vo.*;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.message.MessageClient;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.actual.domain.entity.MtInstructionActualDetail;
import tarzan.actual.domain.repository.MtInstructionActualDetailRepository;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.actual.domain.vo.MtInstructionActualDetailVO;
import tarzan.actual.domain.vo.MtInstructionActualVO;
import tarzan.actual.domain.vo.MtInstructionActualVO1;
import tarzan.actual.infra.mapper.MtInstructionActualMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.api.dto.MtInstructionDocDTO2;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDetailRepository;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.*;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtUomVO;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.repository.MtSupplierRepository;
import tarzan.modeling.domain.vo.MtModLocatorVO7;
import tarzan.modeling.domain.vo.MtModLocatorVO8;
import tarzan.modeling.domain.vo.MtModLocatorVO9;
import tarzan.modeling.domain.vo.MtModSiteVO6;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ???????????????????????????????????????????????????????????????
 *
 * @author han.zhang03@hand-china.com 2020-03-27 18:46:38
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class WmsPoDeliveryRelServiceImpl implements WmsPoDeliveryRelService, AopProxy<WmsPoDeliveryRelServiceImpl> {
    /**
     * ????????????
     */
    private static final String DOC_STATUS_CANCEL = "CANCEL";
    /**
     * ??????????????????
     */
    private static final String DOC_STATUS_COMPLETED = "RECEIVE_COMPLETE";
    /**
     * ??????????????????
     */
    private static final String DOC_STATUS_STOCK_IN_COMPLETED = "STOCK_IN_COMPLETE";


    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;
    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;
    @Autowired
    private MtInstructionRepository mtInstructionRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtSupplierRepository supplierRepository;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private MtMaterialRepository materialRepository;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private MtInstructionActualRepository mtInstructionActualRepository;
    @Autowired
    private MtInstructionDetailRepository mtInstructionDetailRepository;
    @Autowired
    private MtInstructionActualDetailRepository mtInstructionActualDetailRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtExtendSettingsMapper mtExtendSettingMapper;
    @Autowired
    private MtInstructionActualMapper mtInstructionActualMapper;

    @Autowired
    private WmsPoDeliveryRelMapper wmsPoDeliveryRelMapper;
    @Autowired
    private WmsMaterialLotDocRelRepository wmsMaterialLotOcRelRepository;
    @Autowired
    private WmsPoDeliveryRepository wmsPoDeliveryRepository;
    @Autowired
    private WmsPutInStorageTaskRepository WmsPutInStorageTaskRepository;
    @Autowired
    private WmsDocLotRelRepository wmsDocLotRelRepository;
    @Autowired
    private WmsCommonApiService commonApiService;

    @Autowired
    private QmsMaterialInspExemptRepository qmsMaterialInspExemptRepository;
    @Autowired
    private QmsIqcHeaderRepository iqcHeaderRepository;

    @Autowired
    private MessageClient messageClient;

    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;

    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private ItfSrmInstructionIfaceService itfSrmInstructionIfaceService;

    @Autowired
    private WmsMaterialLotRepository wmsMaterialLotRepository;

    @Autowired
    private WmsPoDeliveryRelRepository wmsPoDeliveryRelRepository;
    @Autowired
    private HmeObjectRecordLockRepository hmeObjectRecordLockRepository;
    @Autowired
    private HmeObjectRecordLockService hmeObjectRecordLockService;


    /**
     * ???????????????
     *
     * @param tenantId ??????ID
     * @param dto      ???????????????
     * @return ?????????
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtInstructionDocVO3 createOrder(Long tenantId, WmsPoDeliveryDTO dto) {
        //?????????????????????????????????
        if(BigDecimal.ZERO.compareTo(dto.getLineDTOList().get(0).getQuantity())==0){
            if(BigDecimal.ZERO.compareTo(dto.getLineDTOList().get(0).getExchangeQty())==0){
                throw new MtException("WX.WMS_PO_DELIVERY_0001 ", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WX.WMS_PO_DELIVERY_0001 ", "WMS"));
            }
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("DELIVERY_DOC_CREATE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        //????????????????????????
        //??????API?????????docNum??????id
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("WMS.DELIVERY_TICKET_QUERY", tenantId);
        List<String> instructionDocTypeList = lovValueDTOS.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        List<MtInstructionDoc> instructionDocList = mtInstructionDocRepository.selectByCondition(Condition.builder(MtInstructionDoc.class)
                .andWhere(Sqls.custom().andEqualTo(MtInstructionDoc.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtInstructionDoc.FIELD_SUPPLIER_ID, dto.getSupplierId())
                        .andEqualTo(MtInstructionDoc.FIELD_SITE_ID, dto.getSiteId())
                        .andNotEqualTo(MtInstructionDoc.FIELD_INSTRUCTION_DOC_STATUS, WmsConstant.DocStatus.CANCEL)
                        .andIn(MtInstructionDoc.FIELD_INSTRUCTION_DOC_TYPE, instructionDocTypeList)).build());
        List<String> docIds = instructionDocList.stream().map(MtInstructionDoc::getInstructionDocId).collect(Collectors.toList());
        List<String> keyIdList = new ArrayList<>();
        for (WmsPoDeliveryDTO.DeliveryOrderLineDTO lineDTO :
                dto.getLineDTOList()) {
            BigDecimal currentExchangeQty = BigDecimal.ZERO;

            //??????????????????????????????
            //26???????????????
            List<String> locatorList = wmsPoDeliveryRelMapper.queryLocatorBySite(tenantId, dto.getSiteId(), "26");
            if (CollectionUtils.isEmpty(locatorList)) {
                throw new MtException("WMS_INV_TRANSFER_0038", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_INV_TRANSFER_0038", "WMS"));
            }
            BigDecimal quantity = wmsPoDeliveryRelMapper.propertyLimitSumOnhandQtyGet(tenantId, lineDTO.getMaterialId(), dto.getSupplierId(), locatorList.get(0), "20100101");

            //????????????
            BigDecimal ModeQty = BigDecimal.ZERO;

            //????????????
            BigDecimal exchangedQty = BigDecimal.ZERO;

            if (CollectionUtils.isNotEmpty(docIds)) {
                //??????????????????
                //???????????????ID???????????????ID??????
                List<String> instructionIdList = wmsPoDeliveryRelMapper.propertyInstructionListQuery(tenantId, docIds, lineDTO.getMaterialId(), lineDTO.getMaterialVersion());
                if (CollectionUtils.isNotEmpty(instructionIdList)) {
                    //?????????id????????????????????? ????????????
                    ModeQty = wmsPoDeliveryRelMapper.queryModeQty(tenantId, instructionIdList);

                    //??????actualId
                    List<String> actualIdList = wmsPoDeliveryRelMapper.propertyActualListQuery(tenantId, docIds, lineDTO.getMaterialId(), lineDTO.getMaterialVersion());
                    if (CollectionUtils.isNotEmpty(actualIdList)) {
                        exchangedQty = wmsPoDeliveryRelMapper.queryExchangedQty(tenantId, actualIdList);
                    }
                }
            }

            currentExchangeQty = quantity.subtract(ModeQty).subtract(exchangedQty);

            //????????? ????????????
            if (lineDTO.getExchangeQty() != null && BigDecimal.ZERO.compareTo(lineDTO.getExchangeQty()) < 0) {
                if (lineDTO.getExchangeQty().compareTo(currentExchangeQty) > 0) {
                    MtMaterial mtMaterial = new MtMaterial();
                    mtMaterial.setMaterialId(lineDTO.getMaterialId());
                    mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtMaterial);
                    throw new MtException("MT_INSTRUCTION_0056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INSTRUCTION_0056", "INSTRUCTION", mtMaterial.getMaterialCode(), currentExchangeQty.toString()));
                }
            }

            //?????????ID?????? add by yuchao.wang for kang.wang at 2020.8.31
            keyIdList.addAll(lineDTO.getOrderIdDTOS().stream()
                    .map(WmsPoDeliveryDTO.DeliveryOrderLineDTO.OrderIdDTO::getPoLineId).collect(Collectors.toList()));

        }

        //???????????? add by yuchao.wang for kang.wang at 2020.8.31
        String instructionDocType = "DELIVERY_DOC";
        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setTableName("mt_instruction_attr");
        mtExtendVO1.setKeyIdList(keyIdList);
        MtExtendVO5 extend5 = new MtExtendVO5();
        extend5.setAttrName("PO_TYPE");
        mtExtendVO1.setAttrs(Collections.singletonList(extend5));
        List<MtExtendAttrVO1> extendAttrList = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
        List<String> attrValueList = extendAttrList.stream().map(MtExtendAttrVO1::getAttrValue).collect(Collectors.toList());
        if (attrValueList.contains("3")) {
            instructionDocType = "OUTSOURCING_DELIVERY_DOC";
        }

        //???????????????
        MtInstructionDocDTO2 mtInstructionDoc2 = new MtInstructionDocDTO2();
        mtInstructionDoc2.setInstructionDocNum(dto.getInstructionDocNum());
        mtInstructionDoc2.setInstructionDocType(instructionDocType);
        mtInstructionDoc2.setSiteId(dto.getSiteId());
        mtInstructionDoc2.setSupplierId(dto.getSupplierId());
        mtInstructionDoc2.setCustomerId(dto.getCustomerId());
        mtInstructionDoc2.setCustomerSiteId(dto.getCustomerSiteId());
        mtInstructionDoc2.setInstructionDocStatus(dto.getInstructionDocStatus());
        mtInstructionDoc2.setExpectedArrivalTime(dto.getDemandTime());
        mtInstructionDoc2.setRemark(dto.getRemark());
        mtInstructionDoc2.setMark(dto.getMark());
//        mtInstructionDoc2.setEventId(eventId);
        mtInstructionDoc2.setSupplierSiteId(dto.getSupplierSiteId());

        MtInstructionDocVO3 mtInstructionDoc3 = mtInstructionDocRepository.instructionDocUpdate(tenantId, mtInstructionDoc2, "N");

        //???????????????????????????????????????????????????
        Long instructionLineNum = 0L;
        for (WmsPoDeliveryDTO.DeliveryOrderLineDTO lineDTO : dto.getLineDTOList()) {
            //??????????????????10
            instructionLineNum += 10L;
            //????????????????????????
            MtInstructionVO createVO = new MtInstructionVO();
            createVO.setSourceDocId(mtInstructionDoc3.getInstructionDocId());
            createVO.setMaterialId(lineDTO.getMaterialId());
            createVO.setQuantity(new Double(String.valueOf(lineDTO.getQuantity())));
            createVO.setUomId(lineDTO.getUomId());
            createVO.setInstructionStatus(lineDTO.getInstructionStatus());
            createVO.setExchangeQty(new Double(String.valueOf(lineDTO.getExchangeQty())));
            createVO.setExchangeFlag(lineDTO.getExchangeFlag());
            createVO.setUaiFlag(lineDTO.getUaiFlag());
            createVO.setSiteId(dto.getSiteId());
            createVO.setInstructionType(WmsConstant.InstructionType.RECEIVE_FROM_SUPPLIER);
            createVO.setToSiteId(dto.getSiteId());
            createVO.setToLocatorId(lineDTO.getToLocatorId());
            createVO.setSupplierId(dto.getSupplierId());
            createVO.setBusinessType("PO_RECEIVING");
            //?????????????????????
            MtInstructionVO6 mtInstruction6 = mtInstructionRepository.instructionUpdate(tenantId, createVO, "N");

            //???????????????????????????
            MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
            mtExtendVO10.setKeyId(mtInstruction6.getInstructionId());
            List<MtExtendVO5> mtExtendVO5s = new ArrayList<>(4);

            //????????????
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("MATERIAL_VERSION");
            mtExtendVO5.setAttrValue(lineDTO.getMaterialVersion());
            mtExtendVO5s.add(mtExtendVO5);

            //??????????????????
            MtExtendVO5 mtExtendVO52 = new MtExtendVO5();
            mtExtendVO52.setAttrName("EXCHANGE_QTY");
            mtExtendVO52.setAttrValue(String.valueOf(lineDTO.getExchangeQty()));
            mtExtendVO5s.add(mtExtendVO52);

            //??????????????????
            MtExtendVO5 mtExtendVO53 = new MtExtendVO5();
            mtExtendVO53.setAttrName("EXCHANGE_FLAG");
            mtExtendVO53.setAttrValue(lineDTO.getExchangeFlag());
            mtExtendVO5s.add(mtExtendVO53);

            //????????????
            MtExtendVO5 mtExtendVO54 = new MtExtendVO5();
            mtExtendVO54.setAttrName(HmeConstants.ExtendAttr.UAI_FLAG);
            mtExtendVO54.setAttrValue(lineDTO.getUaiFlag());
            mtExtendVO5s.add(mtExtendVO54);

            //??????
            MtExtendVO5 mtExtendVO56 = new MtExtendVO5();
            mtExtendVO56.setAttrName("INSTRUCTION_LINE_NUM");
            mtExtendVO56.setAttrValue(instructionLineNum.toString());
            mtExtendVO5s.add(mtExtendVO56);

            //?????????????????????????????????????????????
            //????????????
            WmsPoDeliveryRel mtPoDeliveryRel = new WmsPoDeliveryRel();
            mtPoDeliveryRel.setDeliveryDocId(mtInstructionDoc3.getInstructionDocId());
            mtPoDeliveryRel.setDeliveryDocLineId(mtInstruction6.getInstructionId());
            mtPoDeliveryRel.setSrmLineNum(instructionLineNum.toString());
            String poLineId = "";
            for (WmsPoDeliveryDTO.DeliveryOrderLineDTO.OrderIdDTO orderIdDto : lineDTO.getOrderIdDTOS()) {
                mtPoDeliveryRel.setPoId(orderIdDto.getPoId());
                mtPoDeliveryRel.setTenantId(tenantId);
                mtPoDeliveryRel.setPoLineId(orderIdDto.getPoLineId());
                mtPoDeliveryRel.setQuantity(orderIdDto.getQuantity());
                mtPoDeliveryRel.setPoStockInQty(BigDecimal.valueOf(0));
                //??????????????????
                wmsPoDeliveryRepository.insertSelective(mtPoDeliveryRel);
                //??????????????????????????????
//                MtInstruction mtInstruction = new MtInstruction();
//                mtInstruction.setInstructionId(orderIdDto.getPoLineId());
//                mtInstruction = mtInstructionRepository.selectByPrimaryKey(mtInstruction);

                /**
                 * ??????????????????????????????????????????????????????
                 */
//                //?????????????????????????????????
//                MtInstruction mi = new MtInstruction();
//                mi.setInstructionId(mtInstruction.getInstructionId());
//                Long quantity = Objects.isNull(orderIdDto.getQuantity())?0:orderIdDto.getQuantity();
//                Double coverQty = Objects.isNull(mtInstruction.getCoverQty())?0:mtInstruction.getCoverQty();
//                mi.setCoverQty(BigDecimal.valueOf(quantity).add(BigDecimal.valueOf(coverQty)).doubleValue());

//                mtInstructionRepository.updateByPrimaryKeySelective(mi);
                //???????????????????????????
                MtInstructionActual mtInstructionActual = new MtInstructionActual();
                mtInstructionActual.setInstructionId(orderIdDto.getPoLineId());
                mtInstructionActual.setInstructionType(WmsConstant.InstructionType.RECEIVE_FROM_SUPPLIER);
                List<MtInstructionActual> mtInstructionActuals = mtInstructionActualRepository.select(mtInstructionActual);
                BigDecimal quantity = Objects.isNull(orderIdDto.getQuantity()) ? BigDecimal.ZERO : orderIdDto.getQuantity();
                //???????????? ?????????????????? ????????????????????????????????????????????????
                if (CollectionUtils.isEmpty(mtInstructionActuals)) {
                    MtInstruction mtInstruction = mtInstructionRepository.selectByPrimaryKey(orderIdDto.getPoLineId());
                    MtInstructionActualVO mtInstructionActualVO = new MtInstructionActualVO();
                    mtInstructionActualVO.setInstructionId(orderIdDto.getPoLineId());
                    mtInstructionActualVO.setActualQty(quantity.doubleValue());
                    mtInstructionActualVO.setEventId(eventId);
                    mtInstructionActualVO.setInstructionType(WmsConstant.InstructionType.RECEIVE_FROM_SUPPLIER);
                    mtInstructionActualVO.setBusinessType("PO_RECEIVING");
                    mtInstructionActualVO.setToSiteId(mtInstruction.getToSiteId());
                    mtInstructionActualVO.setToLocatorId(mtInstruction.getToLocatorId());
                    mtInstructionActualVO.setMaterialId(mtInstruction.getMaterialId());
                    mtInstructionActualVO.setUomId(mtInstruction.getUomId());
                    mtInstructionActualVO.setSupplierId(mtInstruction.getSupplierId());
                    mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionActualVO);
                } else {
                    for (MtInstructionActual item : mtInstructionActuals) {
                        //???actualQty??????????????????????????????????????????
                        MtInstructionActualVO instructionActualVO = new MtInstructionActualVO();
                        instructionActualVO.setActualId(item.getActualId());
                        instructionActualVO.setActualQty(quantity.doubleValue());
                        instructionActualVO.setEventId(eventId);
                        mtInstructionActualRepository.instructionActualUpdate(tenantId, instructionActualVO);
                    }
                }
                poLineId = orderIdDto.getPoLineId();
            }
            List<MtExtendSettings> attrList = new ArrayList<>(1);
            MtExtendSettings mtExtendSettings = new MtExtendSettings();
            mtExtendSettings.setAttrName("SO_NUM");
            attrList.add(mtExtendSettings);
            MtExtendSettings mtExtendSettings2 = new MtExtendSettings();
            mtExtendSettings2.setAttrName("SO_LINE_NUM");
            attrList.add(mtExtendSettings2);
            List<MtExtendAttrVO> mtExtendAttrVOs = mtExtendSettingMapper.attrPropertyQuery(tenantId,
                    "mt_instruction_attr", "INSTRUCTION_ID", poLineId, attrList);
            for (MtExtendAttrVO mtExtendAttrVO : mtExtendAttrVOs
            ) {
                MtExtendVO5 mtExtendVO57 = new MtExtendVO5();
                mtExtendVO57.setAttrName(mtExtendAttrVO.getAttrName());
                mtExtendVO57.setAttrValue(mtExtendAttrVO.getAttrValue());
                mtExtendVO5s.add(mtExtendVO57);
            }
            mtExtendVO10.setAttrs(mtExtendVO5s);
            //????????????
            mtInstructionRepository.instructionAttrPropertyUpdate(tenantId, mtExtendVO10);

            //??????????????????
            MtInstructionActualVO instructionActualVO = new MtInstructionActualVO();
            BeanUtils.copyProperties(createVO, instructionActualVO);
            instructionActualVO.setInstructionId(mtInstruction6.getInstructionId());
            instructionActualVO.setSourceOrderType(WmsConstant.InspectionDocType.DELIVERY_DOC);
            instructionActualVO.setSourceOrderId(mtInstructionDoc3.getInstructionDocId());
            instructionActualVO.setActualQty(0D);
            instructionActualVO.setEventId(eventId);
            mtInstructionActualRepository.instructionActualUpdate(tenantId, instructionActualVO);

            //????????????????????????????????????????????????????????????
            MtInstructionVO instructionVO = new MtInstructionVO();
            BeanUtils.copyProperties(createVO, instructionVO);
            instructionVO.setInstructionType(WmsConstant.InstructionType.TRANSFER_OVER_LOCATOR);
            instructionVO.setBusinessType(WmsConstant.BusinessType.PO_INSTOCK);
            //??? ?????????????????????????????????
            instructionVO.setInstructionNum(null);
            instructionVO.setIdentification(null);
            instructionVO.setFromSiteId(dto.getSiteId());
            instructionVO.setFromLocatorId(lineDTO.getToLocatorId());
            instructionVO.setToSiteId(null);
//            instructionVO.setToLocatorId(null);
            //?????????????????????
            MtInstructionVO6 mtInstructionVO6 = mtInstructionRepository.instructionUpdate(tenantId, instructionVO, "N");

            MtExtendVO10 extendVO10 = new MtExtendVO10();
            extendVO10.setKeyId(mtInstructionVO6.getInstructionId());
            List<MtExtendVO5> extendVO5List = new ArrayList<>(4);

            //????????????
            MtExtendVO5 extendVO5 = new MtExtendVO5();
            extendVO5.setAttrName("MATERIAL_VERSION");
            extendVO5.setAttrValue(lineDTO.getMaterialVersion());
            extendVO5List.add(extendVO5);

            //??????????????????
            MtExtendVO5 extendVO51 = new MtExtendVO5();
            extendVO51.setAttrName("EXCHANGE_QTY");
            extendVO51.setAttrValue(String.valueOf(lineDTO.getExchangeQty()));
            extendVO5List.add(extendVO51);

            //??????????????????
            MtExtendVO5 extendVO52 = new MtExtendVO5();
            extendVO52.setAttrName("EXCHANGE_FLAG");
            extendVO52.setAttrValue(lineDTO.getExchangeFlag());
            extendVO5List.add(extendVO52);

            //????????????
            MtExtendVO5 extendVO53 = new MtExtendVO5();
            extendVO53.setAttrName(HmeConstants.ExtendAttr.UAI_FLAG);
            extendVO53.setAttrValue(lineDTO.getUaiFlag());
            extendVO5List.add(extendVO53);

            //??????
            MtExtendVO5 extendVO54 = new MtExtendVO5();
            extendVO54.setAttrName("INSTRUCTION_LINE_NUM");
            extendVO54.setAttrValue(instructionLineNum.toString());
            extendVO5List.add(extendVO54);
            for (MtExtendAttrVO mtExtendAttrVO : mtExtendAttrVOs
            ) {
                MtExtendVO5 mtExtendVO57 = new MtExtendVO5();
                mtExtendVO57.setAttrName(mtExtendAttrVO.getAttrName());
                mtExtendVO57.setAttrValue(mtExtendAttrVO.getAttrValue());
                extendVO5List.add(mtExtendVO57);
            }
            extendVO10.setAttrs(extendVO5List);

            mtInstructionRepository.instructionAttrPropertyUpdate(tenantId, extendVO10);

            //??????????????????
            MtInstructionActualVO mtInstructionActualVO = new MtInstructionActualVO();
            BeanUtils.copyProperties(instructionVO, mtInstructionActualVO);
            mtInstructionActualVO.setInstructionId(mtInstructionVO6.getInstructionId());
            mtInstructionActualVO.setSourceOrderType(WmsConstant.InspectionDocType.DELIVERY_DOC);
            mtInstructionActualVO.setSourceOrderId(mtInstructionDoc3.getInstructionDocId());
            mtInstructionActualVO.setActualQty(0D);
            mtInstructionActualVO.setEventId(eventId);
            mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionActualVO);

        }

        //??????????????????????????????
        boolean exchangeFlag = dto.getLineDTOList().stream().anyMatch(deliveryOrderLineDTO -> deliveryOrderLineDTO.getExchangeFlag().equals(MtBaseConstants.YES));
//        if (exchangeFlag) {
        //?????????????????????
//            MtInstructionDocVO4 instructionDocVO4 = new MtInstructionDocVO4();
//            instructionDocVO4.setSupplierId(mtInstructionDoc2.getSupplierId());
//            instructionDocVO4.setInstructionDocType("EXCHANGE");
//            List<String> exchangeDocId = mtInstructionDocRepository.propertyLimitInstructionDocQuery(tenantId, instructionDocVO4);
//            if (CollectionUtils.isEmpty(exchangeDocId)) {
//                throw new MtException("WMS_PO_DELIVERY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                        "WMS_PO_DELIVERY_0001", "WMS"));
//            }
        //update by sanfeng.zhang wangkang ?????????????????????
        //?????????????????????
//            for (WmsPoDeliveryDTO.DeliveryOrderLineDTO lineDTO : dto.getLineDTOList()) {

//                //??????????????????????????????????????????
//                if (MtBaseConstants.YES.equals(lineDTO.getExchangeFlag())) {
//                    //?????????????????????
//                    MtInstructionVO10 mtInstruction = new MtInstructionVO10();
//                    mtInstruction.setSourceDocId(exchangeDocId.get(0));
//                    mtInstruction.setInstructionType("EXCHANGE");
//                    mtInstruction.setSiteId(mtInstructionDoc2.getSiteId());
//                    mtInstruction.setMaterialId(lineDTO.getMaterialId());
//                    //???????????????ID???????????????ID??????
//                    List<String> instructionIdList = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstruction);
//                    if (CollectionUtils.isEmpty(instructionIdList)) {
//                        MtMaterial mtMaterial = new MtMaterial();
//                        mtMaterial.setMaterialId(lineDTO.getMaterialId());
//                        mtMaterial = materialRepository.selectByPrimaryKey(mtMaterial);
//                        throw new MtException("WMS_PO_DELIVERY_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                                "WMS_PO_DELIVERY_0002", "WMS", mtMaterial.getMaterialCode()));
//                    }
//                    MtInstruction instruction = mtInstructionRepository.instructionPropertyGet(tenantId, instructionIdList.get(0));
//                    //????????????????????????
//                    MtInstructionVO mtInstructionVO = new MtInstructionVO();
//                    mtInstructionVO.setInstructionId(instruction.getInstructionId());
//                    mtInstructionVO.setCoverQty(BigDecimal.valueOf(instruction.getCoverQty()).add(lineDTO.getExchangeQty()).doubleValue());
//                    mtInstructionVO.setEventId(eventId);
//                    MtInstructionVO6 mtInstruction6 = mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, "N");
//                }
//            }
//        }

        return mtInstructionDoc3;
    }

    @Override
    public WmsDliveryNumReturnDTO createOrderNumber(Long tenantId, WmsDeliveryNumberCreateDTO createDTO) {
        WmsDliveryNumReturnDTO returnDTO = new WmsDliveryNumReturnDTO();

        //??????api??????????????????
        MtNumrangeVO2 mtNumrangeVO2 = new MtNumrangeVO2();
        mtNumrangeVO2.setObjectCode(createDTO.getObjectCode());
        mtNumrangeVO2.setObjectTypeCode(createDTO.getObjectTypeCode());
        mtNumrangeVO2.setSiteId(createDTO.getSiteId());
        Map<String, String> map = new HashMap<>(2);
        map.put("siteCode", createDTO.getCallObjectCodeList().getSiteCode());
        map.put("supplierCode", createDTO.getCallObjectCodeList().getSupplierCode());
        mtNumrangeVO2.setCallObjectCodeList(map);
        MtNumrangeVO5 mtNumrangeVO5 = mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrangeVO2);
        returnDTO.setNumber(mtNumrangeVO5.getNumber());
        if (CollectionUtils.isEmpty(createDTO.getInstructionLineList())) {
            return returnDTO;
        }

        //??????????????????????????????????????????????????????????????????
        Map<String, List<WmsDeliveryNumberCreateDTO.InstructionLineList>> groupedMap = createDTO.getInstructionLineList().stream()
                .collect(Collectors.groupingBy(goods -> goods.getMaterialId() + "_" + goods.getMaterialVersion() + "_" + goods.getToLocatorId()));

        //????????????????????????????????????
        List<WmsDliveryNumReturnDTO.DeliveryNumReturnLineDTO> groupedList = new ArrayList<>(groupedMap.size());
        //??????
        Long instructionLineNum = 0L;
        for (Map.Entry<String, List<WmsDeliveryNumberCreateDTO.InstructionLineList>> m : groupedMap.entrySet()) {
            instructionLineNum += 10;
            List<WmsDeliveryNumberCreateDTO.InstructionLineList> instructionLineLists = m.getValue();
            //????????????????????????????????????????????????????????????
            List<String> instructionNums = instructionLineLists.stream().map(dto -> {
                return dto.getInstructionDocNum() + "#" + dto.getInstructionNum();
            }).collect(Collectors.toList());
            String instructionNum = String.join(",", instructionNums);

            //???id??????id????????????
            List<WmsPoDeliveryDTO.DeliveryOrderLineDTO.OrderIdDTO> orderIdDTOS = new ArrayList<>();
            instructionLineLists.stream().forEach(t -> {
                WmsPoDeliveryDTO.DeliveryOrderLineDTO.OrderIdDTO orderIdDTO = new WmsPoDeliveryDTO.DeliveryOrderLineDTO.OrderIdDTO();
                orderIdDTO.setPoId(t.getPoId());
                orderIdDTO.setPoLineId(t.getPoLineId());
                orderIdDTO.setQuantity(t.getQuantity());
                orderIdDTOS.add(orderIdDTO);
            });

            //?????????????????????
//            double sum = instructionLineLists.stream().mapToDouble(com.ruike.wms.api.dto.WmsDeliveryNumberCreateDTO.InstructionLineList::getQuantity).sum();
            BigDecimal sum = instructionLineLists.stream().map(WmsDeliveryNumberCreateDTO.InstructionLineList::getQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
            //????????????
            WmsDliveryNumReturnDTO.DeliveryNumReturnLineDTO instructionLine = new WmsDliveryNumReturnDTO.DeliveryNumReturnLineDTO();
            instructionLine.setInstructionLineNum(instructionLineNum.toString());
            instructionLine.setInstructionNum(instructionNum);
            instructionLine.setQuantity2(sum);
            instructionLine.setIdDtoS(orderIdDTOS);
            instructionLine.setMaterialId(instructionLineLists.get(0).getMaterialId());
            instructionLine.setMaterialCode(instructionLineLists.get(0).getMaterialCode());
            instructionLine.setMaterialVersion(instructionLineLists.get(0).getMaterialVersion());
            instructionLine.setMaterialName(instructionLineLists.get(0).getMaterialName());
            instructionLine.setPrimaryUomCode(instructionLineLists.get(0).getPrimaryUomCode());
            instructionLine.setUomId(instructionLineLists.get(0).getUomId());
            instructionLine.setSoNum(instructionLineLists.get(0).getSoNum());
            instructionLine.setSoLineNum(instructionLineLists.get(0).getSoLineNum());
            instructionLine.setToLocatorId(instructionLineLists.get(0).getToLocatorId());
            groupedList.add(instructionLine);
        }
        returnDTO.setLineList(groupedList);

        return returnDTO;
    }

    private void generateLotNumber(Long tenantId, WmsPoDeliveryScanReturnDTO returnInfo) {
        //??????api?????????????????????
        MtNumrangeVO2 mtNumrangeVO2 = new MtNumrangeVO2();
        mtNumrangeVO2.setObjectCode("RECEIPT_BATCH");
        mtNumrangeVO2.setSiteId(returnInfo.getSiteId());
        Map<String, String> map = new HashMap<>(2);
        MtModSiteVO6 mtModSiteVO6 = new MtModSiteVO6();
        mtModSiteVO6.setSiteId(returnInfo.getSiteId());
        map.put("SITECODE", mtModSiteRepository.propertyLimitSitePropertyQuery(tenantId, mtModSiteVO6).get(0).getSiteCode());
        MtSupplier mtSupplier1 = new MtSupplier();
        mtSupplier1.setSupplierId(returnInfo.getSupplierId());
        mtSupplier1 = supplierRepository.selectByPrimaryKey(mtSupplier1);
        map.put("SUPPLIERCODE", mtSupplier1.getSupplierCode());
        mtNumrangeVO2.setCallObjectCodeList(map);

        MtNumrangeVO5 mtNumrangeVO5 = mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrangeVO2);

        if (Objects.isNull(mtNumrangeVO5)) {
            throw new MtException("MT_INVENTORY_0036",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0036",
                            "INVENTORY"));
        }
        returnInfo.setNumber(mtNumrangeVO5.getNumber());

        WmsDocLotRel wmsDocLotRel = new WmsDocLotRel();
        wmsDocLotRel.setTenantId(tenantId);
        wmsDocLotRel.setDocId(returnInfo.getInstructionDocId());
        wmsDocLotRel.setDocType(WmsConstant.DocType.DELIVERY_DOC);
        wmsDocLotRel.setLot(mtNumrangeVO5.getNumber());
        wmsDocLotRelRepository.insertSelective(wmsDocLotRel);
    }

    @Override
    @ProcessLovValue
    public List<WmsPoDeliveryScanLineReturnDTO> getLineList(Long tenantId, String instructionDocId) {
        List<WmsPoDeliveryScanLineReturnDTO> list = wmsPoDeliveryRelMapper.selectLineByDocId(tenantId, instructionDocId);
        list.forEach(rec -> rec.setCoverQty(rec.getActualQty().doubleValue()));
        return list;
    }

    @Override
    @ProcessLovValue
    public WmsPoDeliveryScanReturnDTO poDeliveryScan(Long tenantId, WmsPoDeliveryScanDTO dto) {
        if (StringUtils.isEmpty(dto.getInstructionDocNum())) {
            throw new MtException("WMS_PO_DELIVERY_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_PO_DELIVERY_0003", "WMS"));
        }
        //?????????????????????
        WmsPoDeliveryScanReturnDTO returnInfo = wmsPoDeliveryRelMapper.selectDocInfoByNum(tenantId, dto.getInstructionDocNum());
        if (Objects.isNull(returnInfo)) {
            throw new MtException("MT_INVENTORY_0038", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_INVENTORY_0038", "INVENTORY", dto.getInstructionDocNum()));
        }

        //????????????
        switch (returnInfo.getInstructionDocStatus()) {
            case DOC_STATUS_CANCEL:
                throw new MtException("MT_INVENTORY_0039", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_INVENTORY_0039", "INVENTORY"));
            case DOC_STATUS_COMPLETED:
                throw new MtException("MT_INVENTORY_0040", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_INVENTORY_0040", "INVENTORY"));
            case DOC_STATUS_STOCK_IN_COMPLETED:
                throw new MtException("MT_INVENTORY_0040", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_INVENTORY_0040", "INVENTORY"));
        }

        // ????????????????????????????????????????????????
        if (StringUtils.isEmpty(returnInfo.getNumber())) {
            this.generateLotNumber(tenantId, returnInfo);
        }

        //??????????????????
        List<WmsPoDeliveryScanLineReturnDTO> lineList = self().getLineList(tenantId, returnInfo.getInstructionDocId());
        if(returnInfo.getInstructionDocType().equals(WmsConstant.DocType.OUTSOURCING_DELIVERY_DOC)){
            if(CollectionUtils.isNotEmpty(lineList)){
                List<String> instructionIdList = lineList.stream().map(WmsPoDeliveryScanLineReturnDTO::getInstructionId).collect(Collectors.toList());
                List<WmsPoDeliveryRelVO> wmsPoDeliveryRelVOList = wmsPoDeliveryRelMapper.selectByDeliveryDocLineId(tenantId,instructionIdList);
                if(CollectionUtils.isNotEmpty(wmsPoDeliveryRelVOList)){
                    MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
                    mtExtendVO1.setTableName("mt_instruction_attr");
                    List<String> ids = wmsPoDeliveryRelVOList.stream().map(WmsPoDeliveryRelVO::getDeliveryDocLineId).collect(Collectors.toList());
                    mtExtendVO1.setKeyIdList(ids);
                    List<MtExtendVO5> attrs = new ArrayList<>();
                    MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                    mtExtendVO5.setAttrName("INSTRUCTION_LINE_NUM");
                    attrs.add(mtExtendVO5);
                    mtExtendVO1.setAttrs(attrs);
                    List<MtExtendAttrVO1> mtExtendAttrVO1S = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId,mtExtendVO1);
                    for(WmsPoDeliveryRelVO wmsPoDeliveryRelVO:wmsPoDeliveryRelVOList){
                        if(wmsPoDeliveryRelVO.getCount() != 1){
                            List<MtExtendAttrVO1> mtExtendAttrVO1List = mtExtendAttrVO1S.stream().filter(item ->item.getKeyId().equals(wmsPoDeliveryRelVO.getDeliveryDocLineId())).collect(Collectors.toList());
                            throw new MtException("Exception","???????????????("+mtExtendAttrVO1List.get(0).getAttrValue()+")???????????????????????????1???");
                        }
                    }
                }
            }
        }

        returnInfo.setLineReturnDTOList(lineList);
        return returnInfo;
    }

    @Override
    public List<MtMaterialLot> createBarcode(Long tenantId, WmsCreateBarcodeDTO dto) {
        if (Objects.isNull(dto.getCreateQty())) {
            throw new MtException("WMS_PO_DELIVERY_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_PO_DELIVERY_0004", "WMS"));
        }
        if (dto.getCreateQty() <= 0) {
            throw new MtException("WMS_PO_DELIVERY_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_PO_DELIVERY_0005", "WMS"));
        }
        MtInstructionDoc mtInstructionDoc = new MtInstructionDoc();
        mtInstructionDoc.setInstructionDocId(dto.getInstructionDocId());
        mtInstructionDoc = mtInstructionDocRepository.selectByPrimaryKey(mtInstructionDoc);
        //??????????????????????????????????????????????????????????????????   ?????????????????????????????????????????????+??????????????????
        MtInstruction instruction = new MtInstruction();
        instruction.setInstructionId(dto.getInstructionId());
        instruction = mtInstructionRepository.selectByPrimaryKey(instruction);
        //???????????????????????????????????????
        //??????????????????
        MtExtendSettings mtExtendSettings = new MtExtendSettings();
        mtExtendSettings.setAttrName("EXCHANGE_QTY");
        List<MtExtendSettings> attrList = new ArrayList<>(1);
        attrList.add(mtExtendSettings);
        List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingMapper.attrPropertyQuery(tenantId,
                "mt_instruction_attr", "INSTRUCTION_ID", instruction.getInstructionId(), attrList);
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOS)) {
            //?????????????????????
            if (NumberHelper.isDouble(mtExtendAttrVOS.get(0).getAttrValue())) {
                Double aDouble = Double.valueOf(mtExtendAttrVOS.get(0).getAttrValue());
                /*if (aDouble > 0) {*/
                if (BigDecimal.valueOf(dto.getCreateQty()).multiply(dto.getPrimaryUomQty()).compareTo(
                        BigDecimal.valueOf(instruction.getQuantity()).add(BigDecimal.valueOf(aDouble))) == 1) {
                    throw new MtException("MT_INVENTORY_0051", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0051", "INVENTORY"));
                }
                //}

            } else {
                throw new MtException("WMS_PO_DELIVERY_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_PO_DELIVERY_0006", "WMS", instruction.getInstructionNum()));
            }
        }

        //add by sanfeng.zahng 2020/9/4
        List<MtModLocator> mtModLocatorList = mtModLocatorRepository.selectByCondition(Condition.builder(MtModLocator.class).andWhere(Sqls.custom().andEqualTo(MtModLocator.FIELD_TENANT_ID, tenantId)
                .andEqualTo(MtModLocator.FIELD_PARENT_LOCATOR_ID, instruction.getToLocatorId())
                .andEqualTo(MtModLocator.FIELD_LOCATOR_TYPE, "RECEIVE_PENDING")).build());

        if (CollectionUtils.isEmpty(mtModLocatorList) || mtModLocatorList.size() > 1) {
            MtModLocator locator = mtModLocatorRepository.selectByPrimaryKey(instruction.getToLocatorId());
            throw new MtException("RK_INVENTORY_0021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "RK_INVENTORY_0021", "INVENTORY", locator != null ? locator.getLocatorCode() : ""));
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("DELIVERY_MATERIALLOT_CREATE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        //?????????????????????????????????
        MtNumrangeVO2 mtNumrangeVO2 = new MtNumrangeVO2();
        BeanUtils.copyProperties(dto.getNumrangeGenerateDTOS(), mtNumrangeVO2);
        Map<String, String> map = new HashMap<>(2);
        map.put("siteCode", dto.getNumrangeGenerateDTOS().getCallObjectCodeList().getSiteCode());
        mtNumrangeVO2.setCallObjectCodeList(map);

        List<MtMaterialLotVO20> materialLotList = new ArrayList<>();
        for (int i = 0; i < dto.getCreateQty(); i++) {
            //???????????????
            MtNumrangeVO5 mtNumrangeVO5 = mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrangeVO2);
            //??????api??????
            MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
            mtMaterialLotVO20.setSiteId(dto.getSiteId());
            mtMaterialLotVO20.setQualityStatus(dto.getQualityStatus());
            mtMaterialLotVO20.setMaterialId(dto.getMaterialId());
            mtMaterialLotVO20.setPrimaryUomId(dto.getPrimaryUomId());
            mtMaterialLotVO20.setPrimaryUomQty(Optional.ofNullable(dto.getPrimaryUomQty()).orElse(BigDecimal.ZERO).doubleValue());
            mtMaterialLotVO20.setEnableFlag(dto.getEnableflag());
            mtMaterialLotVO20.setSupplierId(dto.getSupplierId());
            mtMaterialLotVO20.setMaterialLotCode(mtNumrangeVO5.getNumber());
            mtMaterialLotVO20.setLocatorId(mtModLocatorList.get(0).getLocatorId());
            mtMaterialLotVO20.setCreateReason(dto.getCreateReason());
            mtMaterialLotVO20.setSupplierId(mtInstructionDoc.getSupplierId());
            materialLotList.add(mtMaterialLotVO20);
        }
        //????????????
        List<MtMaterialLotVO19> mtMaterialLotVO19s = mtMaterialLotRepository.materialLotBatchUpdate(tenantId, materialLotList, eventId, "");
        //?????????????????????
        for (MtMaterialLotVO19 vo19 :
                mtMaterialLotVO19s) {
            List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
            //??????
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName(HmeConstants.ExtendAttr.STATUS);
            mtExtendVO5.setAttrValue("NEW");
            mtExtendVO5List.add(mtExtendVO5);
            //????????????
            MtExtendVO5 mtExtendVO52 = new MtExtendVO5();
            mtExtendVO52.setAttrName("MATERIAL_VERSION");
            mtExtendVO52.setAttrValue(dto.getMaterialVersion());
            mtExtendVO5List.add(mtExtendVO52);
            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, new MtExtendVO10() {{
                setKeyId(vo19.getMaterialLotId());
                setEventId(eventId);
                setAttrs(mtExtendVO5List);
            }});
        }

        List<MtMaterialLot> mtMaterialLots = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, mtMaterialLotVO19s.stream().map(MtMaterialLotVO19::getMaterialLotId).collect(Collectors.toList()));
        //????????????????????????????????????
        for (MtMaterialLotVO19 vo19 :
                mtMaterialLotVO19s) {
            WmsMaterialLotDocRel wmsMaterialLotOcRel = new WmsMaterialLotDocRel();
            wmsMaterialLotOcRel.setMaterialLotId(vo19.getMaterialLotId());
            wmsMaterialLotOcRel.setInstructionDocId(dto.getInstructionDocId());
            wmsMaterialLotOcRel.setInstructionId(dto.getInstructionId());
            wmsMaterialLotOcRel.setInstructionDocType(WmsConstant.InspectionDocType.DELIVERY_DOC);
            wmsMaterialLotOcRelRepository.insertSelective(wmsMaterialLotOcRel);
        }

        /**
         * ??????????????????????????????????????????
         */
        MtInstructionDetailVO2 mtInstructionDetailVO2 = new MtInstructionDetailVO2();
        mtInstructionDetailVO2.setInstructionId(dto.getInstructionId());
        mtInstructionDetailVO2.setMaterialLotIdList(mtMaterialLotVO19s.stream().map(MtMaterialLotVO19::getMaterialLotId).collect(Collectors.toList()));
        mtInstructionDetailRepository.instructionDetailCreate(tenantId, mtInstructionDetailVO2);

        return mtMaterialLots;
    }

    @Override
    public void cancelPoDelivery(Long tenantId, String instructionDocId) {
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("DELIVERY_DOC_CANCEL");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        //??????????????????id????????????????????????
        MtInstructionDoc mtInstructionDoc = new MtInstructionDoc();
        mtInstructionDoc.setInstructionDocId(instructionDocId);
        mtInstructionDoc = mtInstructionDocRepository.selectByPrimaryKey(mtInstructionDoc);
        //???????????????????????????????????????MES
        if (!WmsConstant.ConstantValue.MES.equals(mtInstructionDoc.getMark())) {
            throw new MtException("MT_INVENTORY_0052", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_INVENTORY_0052", "INVENTORY"));
        }
        //????????????????????????????????????RELEASED
        if (!WmsConstant.InstructionStatus.RELEASED.equals(mtInstructionDoc.getInstructionDocStatus())) {
            throw new MtException("WMS_PO_DELIVERY_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_PO_DELIVERY_0007", "WMS"));
        }
        //SRM???????????????
        MtExtendVO mtExtendVO = new MtExtendVO();
        mtExtendVO.setTableName("mt_instruction_doc_attr");
        mtExtendVO.setKeyId(instructionDocId);
        mtExtendVO.setAttrName("SRM_FLAG");
        List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
        if (CollectionUtils.isNotEmpty(attrVOList)) {
            if (StringUtils.equals("SRM", attrVOList.get(0).getAttrValue())) {
                throw new MtException("WMS_PO_DELIVERY_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_PO_DELIVERY_0013", "WMS"));
            }
        }

        //???????????????????????????????????????CANCEL
        MtInstructionDocDTO2 mtInstructionDoc2 = new MtInstructionDocDTO2();
        mtInstructionDoc2.setInstructionDocId(instructionDocId);
        mtInstructionDoc2.setInstructionDocType(WmsConstant.InspectionDocType.DELIVERY_DOC);
        mtInstructionDoc2.setInstructionDocStatus(DOC_STATUS_CANCEL);
        mtInstructionDoc2.setEventId(eventId);
        mtInstructionDocRepository.instructionDocUpdate(tenantId, mtInstructionDoc2, "N");
        //??????????????????????????????????????????
        MtInstruction instruction = new MtInstruction();
        instruction.setSourceDocId(mtInstructionDoc.getInstructionDocId());
        List<MtInstruction> mtInstructions = mtInstructionRepository.select(instruction);

        //?????????????????????????????????
        for (MtInstruction mi :
                mtInstructions) {
            MtInstructionVO vo = new MtInstructionVO();
            vo.setInstructionId(mi.getInstructionId());
            vo.setInstructionStatus("CANCEL");
            vo.setEventId(eventId);
            mtInstructionRepository.instructionUpdate(tenantId, vo, "N");
        }

        //???????????????????????????????????????????????????????????????????????????????????????????????????
        WmsPoDeliveryRel wmsPoDeliveryRel = new WmsPoDeliveryRel();
        wmsPoDeliveryRel.setDeliveryDocId(instructionDocId);
        List<WmsPoDeliveryRel> wmsPoDeliveryRels = wmsPoDeliveryRelMapper.select(wmsPoDeliveryRel);
        for (WmsPoDeliveryRel rel :
                wmsPoDeliveryRels) {
//            MtInstruction mtInstruction = new MtInstruction();
//            mtInstruction.setInstructionId(rel.getPoLineId());
//            mtInstruction = mtInstructionRepository.selectByPrimaryKey(mtInstruction);
//            if (Objects.isNull(mtInstruction.getCoverQty())) {
//                throw new MtException(mtInstruction.getInstructionNum() + "-???????????????????????????(cover_qty)??????");
//            }
//            if (Objects.isNull(rel.getQuantity())) {
//                throw new MtException(mtInstructionDoc.getInstructionDocNum() + "-??????????????????????????????????????????????????????(quantity)??????");
//            }
            //????????????
//            MtInstructionVO mtInstructionVO = new MtInstructionVO();
//            mtInstructionVO.setInstructionId(mtInstruction.getInstructionId());
//            mtInstructionVO.setCoverQty(BigDecimal.valueOf(mtInstruction.getCoverQty()).subtract(BigDecimal.valueOf(rel.getQuantity())).doubleValue());
//            mtInstructionVO.setEventId(eventId);
//            MtInstructionVO6 mtInstruction6 = mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, "N");
            /**
             * ?????????coverQty??????????????????????????????actualQty by han.zhang 2020-06-03
             */
            MtInstructionActual mtInstructionActual = new MtInstructionActual();
            mtInstructionActual.setInstructionId(rel.getPoLineId());
            List<String> actualIdList = mtInstructionActualRepository.propertyLimitInstructionActualQuery(tenantId, mtInstructionActual);
            //?????????????????????????????????????????????
            if (CollectionUtils.isEmpty(actualIdList)) {
                continue;
            }
            for (String id :
                    actualIdList) {

                MtInstructionActual mtInstructionActual1 = mtInstructionActualRepository.instructionActualPropertyGet(tenantId, id);
                if (ObjectUtil.isNull(mtInstructionActual1) || ObjectUtil.isNull(mtInstructionActual1.getActualQty())) {
                    continue;
                }

                MtInstructionActualVO mtInstructionActualVO = new MtInstructionActualVO();
                mtInstructionActualVO.setActualId(mtInstructionActual1.getActualId());
                mtInstructionActualVO.setActualQty(rel.getQuantity().negate().doubleValue());
                mtInstructionActualVO.setEventId(eventId);
                mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionActualVO);
            }
        }

        //???????????????????????????????????????????????????????????????????????????????????????????????????????????????

        //???????????????????????????????????????????????????????????????????????????????????????
        boolean exchangeFlag = false;
        for (MtInstruction mi :
                mtInstructions) {
            //??????????????????
            List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId,
                    new MtExtendVO1("mt_instruction_attr", Collections.singletonList(mi.getInstructionId()), "EXCHANGE_FLAG"));
            for (MtExtendAttrVO1 extendAttr :
                    mtExtendAttrVO1s) {
                //????????????????????????
                if ("EXCHANGE_FLAG".equals(extendAttr.getAttrName())) {
                    if (MtBaseConstants.YES.equals(extendAttr.getAttrValue())) {
                        //??????????????????????????????????????????????????????REAMRK????????????????????????
                        mi.setRemark(extendAttr.getAttrValue());
                        exchangeFlag = true;
                    }
                }
            }
        }
        //???????????????????????????????????????????????????????????????????????????
        //2020-12-10 add by sanfeng.zhang for wangkang ??????????????????
//        if (exchangeFlag) {
//            //?????????????????????
//            MtInstructionDocVO4 mtLogisticInstructionDoc = new MtInstructionDocVO4();
/////        mtLogisticInstructionDoc.setSupplierId(instruction.getSupplierId());
//            mtLogisticInstructionDoc.setSupplierId(mtInstructionDoc.getSupplierId());
//            mtLogisticInstructionDoc.setInstructionDocType("EXCHANGE");
//            List<String> docIds = mtInstructionDocRepository.propertyLimitInstructionDocQuery(tenantId, mtLogisticInstructionDoc);
//            if (CollectionUtils.isEmpty(docIds)) {
//                throw new MtException("WMS_PO_DELIVERY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                        "WMS_PO_DELIVERY_0001", "WMS"));
//            }
//            for (MtInstruction m :
//                    mtInstructions) {
//                //??????????????????????????????Y??????????????????
//                if (!MtBaseConstants.YES.equals(m.getRemark())) {
//                    continue;
//                }
//                //???????????????????????????????????????
//                Double exchangeQty = 0D;
//                //??????????????????
//                List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingMapper.attrPropertyBatchQuery(tenantId,
//                        "mt_instruction_attr", "INSTRUCTION_ID", Collections.singletonList(m.getInstructionId()));
//                for (MtExtendAttrVO1 extendAttr :
//                        mtExtendAttrVO1s) {
//                    //??????????????????
//                    if ("EXCHANGE_QTY".equals(extendAttr.getAttrName())) {
//                        //?????????????????????????????????
//                        String attrValue = extendAttr.getAttrValue();
//                        if (!NumberHelper.isDouble(attrValue)) {
//                            throw new MtException("WMS_PO_DELIVERY_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                                    "WMS_PO_DELIVERY_0008", "WMS", m.getInstructionNum()));
//                        }
//                        exchangeQty = Double.valueOf(extendAttr.getAttrValue());
//                        break;
//                    }
//                }
//
//                //?????????????????????
//                MtInstructionVO10 mtInstructionVO10 = new MtInstructionVO10();
//                mtInstructionVO10.setSourceDocId(docIds.get(0));
//                mtInstructionVO10.setMaterialId(m.getMaterialId());
//                List<String> instructionIdList = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstructionVO10);
//                if (CollectionUtils.isEmpty(instructionIdList)) {
//                    MtMaterial mtMaterial = new MtMaterial();
//                    mtMaterial.setMaterialId(m.getMaterialId());
//                    mtMaterial = materialRepository.selectByPrimaryKey(mtMaterial);
//                    throw new MtException("WMS_PO_DELIVERY_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                            "WMS_PO_DELIVERY_0002", "WMS", mtMaterial.getMaterialCode()));
//                }
//                //??????????????????????????????????????????
//                //?????????id?????????????????????
//                List<MtInstruction> instructionList = mtInstructionRepository.instructionPropertyBatchGet(tenantId, instructionIdList);
//                Double coverQty = instructionList.get(0).getCoverQty();
//
//
//                MtEventCreateVO createVO = new MtEventCreateVO();
//                createVO.setEventTypeCode("DELIVERY_DOC_CANCEL");
//                String newEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
//
//                //??????????????????????????????
//                MtInstructionVO mtInstructionVO = new MtInstructionVO();
//                mtInstructionVO.setInstructionId(instructionIdList.get(0));
//                mtInstructionVO.setCoverQty(BigDecimal.valueOf(coverQty).subtract(BigDecimal.valueOf(exchangeQty)).doubleValue());
//                mtInstructionVO.setEventId(newEventId);
//                mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, "N");
//            }

//        }
    }

    private void materialLotValidate(Long tenantId, String instructionDocNum, WmsMaterialLotAttrVO materialLot, WmsInstructionAttrVO instructionAttr) {

        if (!WmsConstant.CONSTANT_N.equals(materialLot.getEnableFlag())) {
            throw new MtException("MT_INVENTORY_0041", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_INVENTORY_0041", "INVENTORY", materialLot.getMaterialLotCode()));
        }

        if (!WmsConstant.QualityStatus.PENDING.equals(materialLot.getQualityStatus())) {
            throw new MtException("MT_INVENTORY_0042", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_INVENTORY_0042", "INVENTORY", materialLot.getMaterialLotCode()));
        }

        if (!WmsConstant.QualityStatus.NEW.equals(materialLot.getStatus())) {
            throw new MtException("QMS_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_LOT_0001", "MATERIAL_LOT", materialLot.getMaterialLotCode()));
        }

        //????????????????????????
        if (StringUtils.isNotEmpty(instructionDocNum)) {
            if (Objects.isNull(instructionAttr)) {
                throw new MtException("MT_INVENTORY_0038", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_INVENTORY_0038", "INVENTORY", instructionDocNum));
            } else {
                List<LovValueDTO> lovValueList = lovAdapter.queryLovValue("WMS.DELIVERY_TICKET_QUERY", tenantId);
                if (lovValueList.stream().noneMatch(rec -> rec.getValue().equals(instructionAttr.getInstructionDocType()))) {
                    throw new MtException("MT_INVENTORY_0038", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0038", "INVENTORY", instructionDocNum));
                }
            }

            if (instructionAttr.getLotFlag()) {
                throw new MtException("MT_INVENTORY_0043", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_INVENTORY_0043", "INVENTORY", materialLot.getMaterialLotCode(), instructionAttr.getInstructionNum()));
            }
        } else {
            if (Objects.isNull(instructionAttr)) {
                throw new MtException("MT_INVENTORY_0055", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_INVENTORY_0055", "INVENTORY"));
            }
        }

        if (!materialLot.getMaterialId().equals(instructionAttr.getMaterialId())) {
            throw new MtException("MT_INVENTORY_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_INVENTORY_0044", "INVENTORY", materialLot.getMaterialLotCode(), materialLot.getMaterialCode(), materialLot.getMaterialName(), instructionDocNum));
        }
        if (!materialLot.getPrimaryUomId().equals(instructionAttr.getUomId())) {
            throw new MtException("WMS_DISTRIBUTION_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_DISTRIBUTION_0004", "WMS", materialLot.getMaterialLotCode()));
        }


    }

    /**
     * @param tenantId
     * @param dto
     * @return java.lang.Object
     * @Description ??????????????????
     * @Author wenzhang.yu
     * @Date 2020/4/10 13:48
     * @version 1.0
     **/
    @Transactional(rollbackFor = Exception.class)
    @Override
    @ProcessLovValue
    public WmsPoDeliveryScanReturnDTO2 materialLotCodeScan(Long tenantId, WmsPoDeliveryScanDTO2 dto) {
        //?????????
        WmsPoDeliveryScanReturnDTO2 wmsPoDeliveryScanReturnDTO2 = new WmsPoDeliveryScanReturnDTO2();
        // ??????????????????
        WmsMaterialLotAttrVO materialLot = wmsMaterialLotRepository.selectWithAttrByCode(tenantId, dto.getMaterialLotCode());
        if (Objects.isNull(materialLot)) {
            throw new MtException("MT_INVENTORY_0054", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_INVENTORY_0054", "MATERIAL_LOT", dto.getMaterialLotCode()));
        }
        // ?????????????????????????????????
        WmsInstructionAttrVO instructionAttr = wmsPoDeliveryRelRepository.selectLineByBarcodeAndDocNum(tenantId, dto.getInstructionDocNum(), materialLot.getMaterialLotId());

        // ????????????
        this.materialLotValidate(tenantId, dto.getInstructionDocNum(), materialLot, instructionAttr);
        wmsPoDeliveryScanReturnDTO2.setMaterialVersion(materialLot.getMaterialVersion());

        //????????????
        BigDecimal actualQty = instructionAttr.getActualQty();
        BigDecimal exchangedQty = Optional.ofNullable(instructionAttr.getExchangedQty()).orElse(BigDecimal.ZERO);
        BigDecimal exchange = Optional.ofNullable(instructionAttr.getExchangeQty()).orElse(BigDecimal.ZERO);
        BigDecimal primaryUomQty = materialLot.getPrimaryUomQty();
        BigDecimal res = instructionAttr.getQuantity().subtract(actualQty).add(exchange).subtract(exchangedQty);
        if (primaryUomQty.compareTo(res) > 0) {
            throw new MtException("MT_INVENTORY_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_INVENTORY_0045", "INVENTORY", dto.getMaterialLotCode()));
        }

        //???????????????????????????????????????????????????
        WmsPoDeliveryScanDTO wmsPoDeliveryScanDTO = new WmsPoDeliveryScanDTO();
        wmsPoDeliveryScanDTO.setInstructionDocNum(Optional.ofNullable(dto.getInstructionDocNum()).orElse(instructionAttr.getInstructionDocNum()));

        WmsPoDeliveryScanReturnDTO WmsPoDeliveryScanReturnDTO = this.poDeliveryScan(tenantId, wmsPoDeliveryScanDTO);
        wmsPoDeliveryScanReturnDTO2.setWmsPoDeliveryScanReturnDTO(WmsPoDeliveryScanReturnDTO);
        dto.setNumber(WmsPoDeliveryScanReturnDTO.getNumber());

        //????????????
        MtModLocatorVO7 mtModLocatorVO7 = new MtModLocatorVO7();
        mtModLocatorVO7.setLocatorId(instructionAttr.getToLocatorId());
        List<MtModLocatorVO8> mtModLocatorVO8s = mtModLocatorRepository.propertyLimitLocatorPropertyQuery(tenantId, mtModLocatorVO7);
        String messageLocatorCode = mtModLocatorVO8s.get(0).getLocatorCode();
        String receivePendingLocatorId = "";

        MtModLocatorVO9 mtModLocatorVO9 = new MtModLocatorVO9();
        mtModLocatorVO9.setLocatorId(instructionAttr.getToLocatorId());
        mtModLocatorVO9.setQueryType("FIRST");
        List<String> locatorIds = mtModLocatorRepository.subLocatorQuery(tenantId, mtModLocatorVO9);
        if (CollectionUtils.isNotEmpty(locatorIds)) {
            boolean flag = true;
            for (String locatorId : locatorIds) {
                MtModLocatorVO7 subLocator = new MtModLocatorVO7();
                subLocator.setLocatorId(locatorId);
                subLocator.setLocatorType("RECEIVE_PENDING");
                List<MtModLocatorVO8> subLocatorList = mtModLocatorRepository.propertyLimitLocatorPropertyQuery(tenantId, subLocator);
                if (CollectionUtils.isNotEmpty(subLocatorList)) {
                    receivePendingLocatorId = subLocatorList.get(0).getLocatorId();
                    flag = false;
                    break;
                }
            }
            if (flag) {
                throw new MtException("MT_INVENTORY_0057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_INVENTORY_0057", "INVENTORY", messageLocatorCode));
            }
        } else {
            throw new MtException("MT_INVENTORY_0057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_INVENTORY_0057", "INVENTORY", messageLocatorCode));
        }

        //????????????
        //??????????????????
        BigDecimal nowActualQty;
        BigDecimal nowExchange = BigDecimal.ZERO;
        BigDecimal newExchangedQty;
        String actualId = mtInstructionActualRepository.instructionLimitActualBatchGet(tenantId, Collections.singletonList(instructionAttr.getInstructionId())).get(0).getActualId();

        if (WmsConstant.CONSTANT_Y.equals(instructionAttr.getExchangeFlag()) && (exchange.compareTo(exchangedQty) != 0)) {
            //??????????????????
            BigDecimal subResQty = exchange.subtract(exchangedQty);
            if (materialLot.getPrimaryUomQty().compareTo(subResQty) > 0) {
                nowExchange = exchange.subtract(exchangedQty);
                nowActualQty = materialLot.getPrimaryUomQty().subtract(nowExchange);
            } else {
                nowExchange = materialLot.getPrimaryUomQty();
                nowActualQty = BigDecimal.ZERO;
            }
            wmsPoDeliveryScanReturnDTO2.setCoverQty(nowActualQty);
            wmsPoDeliveryScanReturnDTO2.setExchangedQty(nowExchange);
            newExchangedQty = nowExchange.add(exchangedQty);
        } else {
            //?????????????????????
            nowActualQty = materialLot.getPrimaryUomQty();
            wmsPoDeliveryScanReturnDTO2.setCoverQty(nowActualQty);
            //???????????????,??????????????????=0
            wmsPoDeliveryScanReturnDTO2.setExchangedQty(BigDecimal.ZERO);
            newExchangedQty = exchange.add(nowExchange);
        }
        // ????????????
        instructionExecute(tenantId, nowActualQty, materialLot, receivePendingLocatorId, instructionAttr, actualId);
        // ????????????
        String newInstructionStatus = updateMostDate(tenantId, materialLot.getMaterialLotId(), nowActualQty, newExchangedQty, exchange, instructionAttr, actualId);

        // ??????????????????
        wmsPoDeliveryScanReturnDTO2.setMaterialCode(materialLot.getMaterialCode());
        wmsPoDeliveryScanReturnDTO2.setMaterialName(materialLot.getMaterialName());
        wmsPoDeliveryScanReturnDTO2.setUomCode(materialLot.getPrimaryUomCode());
        wmsPoDeliveryScanReturnDTO2.setMaterialQty(materialLot.getPrimaryUomQty().doubleValue());
        wmsPoDeliveryScanReturnDTO2.setInstructionStatus(newInstructionStatus);
        return wmsPoDeliveryScanReturnDTO2;
    }

    /**
     * @param tenantId
     * @param materialLotId
     * @param actualQty
     * @param exchangedQty
     * @param instruction
     * @return void
     * @Description ??????????????????
     * @Author wenzhang.yu
     * @Date 2020/4/13 18:35
     * @version 1.0
     **/
    private String updateMostDate(Long tenantId, String materialLotId, BigDecimal actualQty, BigDecimal exchangedQty, BigDecimal exchange, WmsInstructionAttrVO instruction, String actualId) {
        BigDecimal newActualQty = actualQty.add(instruction.getActualQty());
        //????????????
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("MATERIALLOT_SCAN");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        //??????api????????????
        List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
        MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName(HmeConstants.ExtendAttr.STATUS);
        mtExtendVO5.setAttrValue("SCANNED");
        mtExtendVO5List.add(mtExtendVO5);
        MtExtendVO10 mtExtendVO10 = new MtExtendVO10();

        mtExtendVO10.setKeyId(materialLotId);
        mtExtendVO10.setEventId(eventId);
        mtExtendVO10.setAttrs(mtExtendVO5List);
        mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);

        //??????????????????
        List<MtExtendVO5> attrs = new ArrayList<>();
        MtExtendVO5 mtExtendVO51 = new MtExtendVO5();
        mtExtendVO51.setAttrName("EXCHANGED_QTY");
        mtExtendVO51.setAttrValue(exchangedQty.toPlainString());
        attrs.add(mtExtendVO51);

        MtExtendVO5 mtExtendVO52 = new MtExtendVO5();
        mtExtendVO52.setAttrName("ACTUAL_RECEIVE_QTY");
        mtExtendVO52.setAttrValue(newActualQty.toPlainString());
        attrs.add(mtExtendVO52);

        MtExtendVO10 actualAttrUpdate = new MtExtendVO10();
        actualAttrUpdate.setEventId(eventId);
        actualAttrUpdate.setKeyId(actualId);
        actualAttrUpdate.setAttrs(attrs);
        mtInstructionActualRepository.instructionActualAttrPropertyUpdate(tenantId, actualAttrUpdate);

        MtInstructionVO mtInstructionVO = new MtInstructionVO();
        BigDecimal quantity = instruction.getQuantity();

        BigDecimal qty = newActualQty.add(exchangedQty);
        BigDecimal totalQty = quantity.add(exchange);

        if (qty.compareTo(BigDecimal.ZERO) == 0) {
            mtInstructionVO.setInstructionStatus(WmsConstant.InstructionStatus.RELEASED);
        } else if (qty.compareTo(BigDecimal.ZERO) > 0 && qty.compareTo(totalQty) < 0) {
            mtInstructionVO.setInstructionStatus(WmsConstant.InstructionStatus.RECEIVE_EXECUTE);
        } else if (qty.equals(totalQty)) {
            mtInstructionVO.setInstructionStatus(WmsConstant.InstructionStatus.COMPLETED);
        }
        mtInstructionVO.setInstructionId(instruction.getInstructionId());
        mtInstructionVO.setEventId(eventId);
        mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, "N");
        return mtInstructionVO.getInstructionStatus();
    }


    /**
     * @param tenantId
     * @param instructionDocNum
     * @return void
     * @Description ????????????????????????
     * @Author wenzhang.yu
     * @Date 2020/4/21 22:25
     * @version 1.0
     **/
    @Override
    public WmsPoDeliveryVO5 iscompleted(Long tenantId, String instructionDocNum) {
        //2020-09-07 edit by chaonan.hu for kang.wang ??????boolean??????????????????????????????
        WmsPoDeliveryVO5 result = new WmsPoDeliveryVO5();
        //??????num??????id
        MtInstructionDocVO4 mtLogisticInstructionDoc = new MtInstructionDocVO4();
        mtLogisticInstructionDoc.setInstructionDocNum(instructionDocNum);
        String docId = mtInstructionDocRepository.propertyLimitInstructionDocQuery(tenantId, mtLogisticInstructionDoc).get(0);

        //??????id?????????????????????
        MtInstructionVO10 mtInstructionVO10 = new MtInstructionVO10();
        mtInstructionVO10.setSourceDocId(docId);
        List<String> instructionIdList = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstructionVO10);
        List<MtInstruction> instructionList = mtInstructionRepository.instructionPropertyBatchGet(tenantId, instructionIdList);
        boolean flag = true;
        for (MtInstruction mtInstruction : instructionList) {
            if ("RECEIVE_FROM_SUPPLIER".equals(mtInstruction.getInstructionType()) && !"COMPLETED".equals(mtInstruction.getInstructionStatus())) {
                flag = false;
                break;
            }
        }
        result.setFlag(flag);
        return result;
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????2020/9/28???
     *
     * @param tenantId
     * @param dto
     * @return com.ruike.wms.api.dto.WmsPoDeliveryConfirmDTO
     * @author jiangling.zheng@hand-china.com 2020/9/28 10:33
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public WmsPoDeliveryConfirmDTO confirmAcceptance(Long tenantId, WmsPoDeliveryConfirmDTO dto) {
        // 20210906 add by sanfeng.zhang for peng.zhao ???????????????
        //??????
        HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
        hmeObjectRecordLockDTO.setFunctionName("????????????");
        hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
        hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.DOCUMENT);
        hmeObjectRecordLockDTO.setObjectRecordId("");
        hmeObjectRecordLockDTO.setObjectRecordCode(dto.getInstructionDocNum());
        HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
        //??????
        hmeObjectRecordLockRepository.commonLockWo(hmeObjectRecordLock);

        try {
            SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //??????num??????id
            MtInstructionDoc instructionDoc = mtInstructionDocRepository.selectOne(new MtInstructionDoc() {{
                setInstructionDocNum(dto.getInstructionDocNum());
                setTenantId(tenantId);
            }});
            // 20210915 add by sanfeng.zhang for peng.zhao ?????????????????? ???????????????????????????
            if (DOC_STATUS_COMPLETED.equals(instructionDoc.getInstructionDocStatus())) {
                throw new MtException("MT_INVENTORY_0040", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_INVENTORY_0040", "INVENTORY"));
            }
            String docId = instructionDoc.getInstructionDocId();
            //??????id?????????????????????
            /*List<MtInstruction> instructionList = mtInstructionRepository.select(new MtInstruction(){{
                setTenantId(tenantId);
                setSourceDocId(docId);
                setInstructionType(WmsConstant.InstructionType.RECEIVE_FROM_SUPPLIER);
            }});*/
            //????????????????????????
            WmsPoDeliveryVO5 completedFlag = this.iscompleted(tenantId, dto.getInstructionDocNum());
            if (!completedFlag.isFlag()) {
                throw new MtException("WMS_PO_DELIVERY_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_PO_DELIVERY_0012", "WMS", dto.getInstructionDocNum()));
            }

            MtInstructionVO10 mtInstructionVO10 = new MtInstructionVO10();
            mtInstructionVO10.setSourceDocId(docId);
            mtInstructionVO10.setInstructionType("RECEIVE_FROM_SUPPLIER");
            List<String> instructionIdList = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstructionVO10);
            List<MtInstruction> instructionList = mtInstructionRepository.instructionPropertyBatchGet(tenantId, instructionIdList);
            log.info("api confirmAcceptance instructionList : {}", JSONArray.toJSONString(instructionList));
            //??????????????????
            // ??????????????????
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "PO_RECEIVING");
            //????????????
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode(WmsConstant.InstructionType.RECEIVE_FROM_SUPPLIER);
            eventCreateVO.setEventRequestId(eventRequestId);
            String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
            WmsDocLotRel deliveryDoc = wmsDocLotRelRepository.selectOne(new WmsDocLotRel() {{
                setTenantId(tenantId);
                setDocId(docId);
                setDocType(WmsConstant.DocType.DELIVERY_DOC);
            }});
            //????????????
            //??????materialLotId
            List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
            MtInstructionVO mtInstructionVO;
            Long receivedBy = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
            // ???????????? ?????????????????????
            List<String> warehouseIdList = instructionList.stream().map(MtInstruction::getToLocatorId).distinct().collect(Collectors.toList());
            Map<String, List<MtModLocator>> locatorMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(warehouseIdList)) {
                List<MtModLocator> locatorList = mtModLocatorRepository.selectByCondition(Condition.builder(MtModLocator.class).andWhere(Sqls.custom()
                        .andEqualTo(MtModLocator.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtModLocator.FIELD_LOCATOR_TYPE, "RECEIVE_PENDING")
                        .andIn(MtModLocator.FIELD_PARENT_LOCATOR_ID, warehouseIdList)
                        .andEqualTo(MtModLocator.FIELD_ENABLE_FLAG, HmeConstants.ConstantValue.YES)).build());
                if (CollectionUtils.isNotEmpty(locatorList)) {
                    locatorMap = locatorList.stream().collect(Collectors.groupingBy(MtModLocator::getParentLocatorId));
                }
            }
            //???????????????????????????????????????
            for (MtInstruction instruction : instructionList) {
                //????????????ID
                String receivePendingLocatorId = "";
                List<MtModLocator> receiveLocatorList = locatorMap.get(instruction.getToLocatorId());

                if (CollectionUtils.isNotEmpty(receiveLocatorList)) {
                    receivePendingLocatorId = receiveLocatorList.get(0).getLocatorId();
                } else {
                    MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(instruction.getToLocatorId());
                    throw new MtException("MT_INVENTORY_0057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0057", "INVENTORY", mtModLocator != null ? mtModLocator.getLocatorCode() : ""));
                }
                //?????????????????????
                Boolean exemptionFlag = getFreeCheckFlag(tenantId, instructionDoc.getSiteId(), instruction.getMaterialId());
                log.info("api confirmAcceptance exemptionFlag : {}", exemptionFlag);
                if (exemptionFlag) {
                    billProcess(tenantId, receivePendingLocatorId, instructionDoc, instruction);
                }

                if (StringUtils.equals(instruction.getBusinessType(), "PO_RECEIVING")) {
                    WmsPoDeliveryConfirmDTO2 dto2 = new WmsPoDeliveryConfirmDTO2();
                    dto2.setEventId(eventId);
                    dto2.setExemptionFlag(exemptionFlag);
                    dto2.setReceivePendingLocatorId(receivePendingLocatorId);
                    dto2.setInstructionDoc(instructionDoc);
                    dto2.setInstruction(instruction);
                    dto2.setLot(deliveryDoc.getLot());
                    dto2.setLot(dto.getNumber());
                    List<WmsObjectTransactionRequestVO> transactionRequestVOList = instructionActualProcess(tenantId, dto2);
                    if (CollectionUtils.isNotEmpty(transactionRequestVOList)) {
                        objectTransactionRequestList.addAll(transactionRequestVOList);
                    }
                }
                mtInstructionVO = new MtInstructionVO();
                mtInstructionVO.setInstructionId(instruction.getInstructionId());
                mtInstructionVO.setInstructionStatus("COMPLETED");
                mtInstructionVO.setEventId(eventId);
                mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, "N");
                //????????????????????????????????? add by sanfeng.zhang for kang.wang at 2020.9.17
                List<MtExtendVO5> vo5List = new ArrayList<>();
                MtExtendVO5 vo5 = new MtExtendVO5();
                vo5.setAttrName("ACTUAL_RECEIVED_DATE");
                vo5.setAttrValue(fm.format(new Date()));
                vo5List.add(vo5);

                MtExtendVO5 vo52 = new MtExtendVO5();
                vo52.setAttrName("ACTUAL_RECEIVED_BY");
                vo52.setAttrValue(String.valueOf(receivedBy));

                MtExtendVO5 flagAttr = new MtExtendVO5();
                flagAttr.setAttrName("EXEMPTION_FLAG");
                if (exemptionFlag) {
                    flagAttr.setAttrValue(WmsConstant.CONSTANT_N);
                } else {
                    flagAttr.setAttrValue(WmsConstant.CONSTANT_Y);

                }
                vo5List.add(flagAttr);

                vo5List.add(vo52);
                mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_attr", instruction.getInstructionId(), eventId, vo5List);
            }
            //??????API???????????? add by yuchao.wang for kang.wang at 2020.8.24
            if (CollectionUtils.isNotEmpty(objectTransactionRequestList)) {
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
            }
            //	???????????????
            MtInstructionDocDTO2 mtInstructionDoc2 = new MtInstructionDocDTO2();
            mtInstructionDoc2.setEventId(eventId);
            mtInstructionDoc2.setInstructionDocId(docId);
            mtInstructionDoc2.setInstructionDocStatus("RECEIVE_COMPLETE");
            mtInstructionDocRepository.instructionDocUpdate(tenantId, mtInstructionDoc2, "N");

            //???????????????-?????????????????????
            String receivedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            List<MtExtendVO5> mtExtendVO5List1 = new ArrayList<>();
            MtExtendVO5 mtExtendVO51 = new MtExtendVO5();
            mtExtendVO51.setAttrName("ACTUAL_RECEIVED_DATE");
            mtExtendVO51.setAttrValue(receivedTime);
            mtExtendVO5List1.add(mtExtendVO51);

            MtExtendVO5 mtExtendVO52 = new MtExtendVO5();
            mtExtendVO52.setAttrName("ACTUAL_RECEIVED_BY");
            mtExtendVO52.setAttrValue(String.valueOf(receivedBy));
            mtExtendVO5List1.add(mtExtendVO52);
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_doc_attr", docId, eventId,
                    mtExtendVO5List1);
            /**
             * ?????????????????? add by yuchao.wang for kang.wang at 2020.8.31
             */
            //?????????????????????
            List<MtInstructionActual> mtInstructionActuals = wmsPoDeliveryRelMapper.queryActualQty(tenantId, "RECEIVE_FROM_SUPPLIER", instructionIdList);
            if (CollectionUtils.isEmpty(mtInstructionActuals)) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "??????????????????"));
            }
            Map<String, BigDecimal> instructionActualQtyMap = new HashMap<>();
            mtInstructionActuals.forEach(item -> instructionActualQtyMap.put(item.getInstructionId(), BigDecimal.valueOf(item.getActualQty())));

            //???????????????????????????
            List<WmsPoDeliveryRel> wmsPoDeliveryRelList = wmsPoDeliveryRelMapper.queryReceiveQty(tenantId, docId, instructionIdList);
            if (CollectionUtils.isEmpty(wmsPoDeliveryRelList)) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "????????????????????????????????????"));
            }
            List<WmsPoDeliveryRel> updateList = new ArrayList<>();
            Map<String, List<WmsPoDeliveryRel>> groupMap = wmsPoDeliveryRelList
                    .stream().collect(Collectors.groupingBy(WmsPoDeliveryRel::getDeliveryDocLineId));
            for (Map.Entry<String, List<WmsPoDeliveryRel>> entry : groupMap.entrySet()) {
                if (!instructionActualQtyMap.containsKey(entry.getKey())) {
                    throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CHIP_TRANSFER_013", "HME", "??????????????????"));
                }
                BigDecimal totalQty = instructionActualQtyMap.get(entry.getKey());

                //?????????????????????
                for (WmsPoDeliveryRel rel : entry.getValue()) {
                    BigDecimal remainQty = rel.getQuantity().subtract(rel.getReceivedQty());
                    if (rel.getQuantity().compareTo(rel.getReceivedQty()) > 0) {
                        BigDecimal receivedQty = totalQty.min(remainQty);

                        WmsPoDeliveryRel update = new WmsPoDeliveryRel();
                        update.setPoDeliveryRelId(rel.getPoDeliveryRelId());
                        update.setReceivedQty(receivedQty);
                        updateList.add(update);
                        totalQty = totalQty.subtract(receivedQty);

                        if (totalQty.compareTo(BigDecimal.ONE) < 0) {
                            break;
                        }
                    }
                }
            }

            //?????????????????????????????????
            Long userId = -1L;
            if (!Objects.isNull(DetailsHelper.getUserDetails())
                    && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())) {
                userId = DetailsHelper.getUserDetails().getUserId();
            }
            if (CollectionUtils.isNotEmpty(updateList)) {
                wmsPoDeliveryRelMapper.batchUpdateReceivedQty(userId, updateList);
            }
            for (MtInstruction mtInstruction : instructionList) {
                //?????????????????????
                Boolean exemptionFlag = getFreeCheckFlag(tenantId, instructionDoc.getSiteId(), mtInstruction.getMaterialId());
                MtInstructionActual mtInstructionActual = new MtInstructionActual();
                mtInstructionActual.setInstructionId(mtInstruction.getInstructionId());
                mtInstructionActual.setInstructionType(WmsConstant.InstructionType.RECEIVE_FROM_SUPPLIER);
                mtInstructionActual.setBusinessType("PO_RECEIVING");
                List<String> actualIdList = mtInstructionActualRepository.propertyLimitInstructionActualQuery(tenantId,
                        mtInstructionActual);
                if (CollectionUtils.isNotEmpty(actualIdList)) {
                    MtInstructionActual mtInstructionActual1 = mtInstructionActualRepository.instructionActualPropertyGet(tenantId, actualIdList.get(0));
                    if (mtInstructionActual1.getActualQty() == null) {
                        continue;
                    }
                    //???????????????????????????????????????????????????????????????????????????????????????????????????
                    WmsPoDeliveryRel wmsPoDeliveryRel = new WmsPoDeliveryRel();
                    wmsPoDeliveryRel.setDeliveryDocLineId(mtInstruction.getInstructionId());
                }
                //??????
                if (!exemptionFlag) {
                    WmsPutInStorageTask wmsPutInStorageTask = new WmsPutInStorageTask();
                    wmsPutInStorageTask.setTenantId(tenantId);
                    wmsPutInStorageTask.setTaskType(WmsConstant.InspectionDocType.DELIVERY_DOC);
                    wmsPutInStorageTask.setInstructionDocId(docId);
                    wmsPutInStorageTask.setInstructionId(mtInstruction.getInstructionId());
                    wmsPutInStorageTask.setTaskStatus("STOCK_PENDING");
                    wmsPutInStorageTask.setInstructionDocType(WmsConstant.InspectionDocType.DELIVERY_DOC);
                    wmsPutInStorageTask.setMaterialId(mtInstruction.getMaterialId());
                    List<MtInstructionActual> mtInstructionActualList = mtInstructionActualRepository.select(new MtInstructionActual() {{
                        setTenantId(tenantId);
                        setInstructionId(mtInstruction.getInstructionId());
                    }});
                    wmsPutInStorageTask.setTaskQty(BigDecimal.valueOf(mtInstructionActualList.get(0).getActualQty()));
                    wmsPutInStorageTask.setExecuteQty(BigDecimal.valueOf(0));
                    WmsPutInStorageTaskRepository.insertSelective(wmsPutInStorageTask);
                }

                WmsPoDeliveryRel wmsPoDeliveryRel = new WmsPoDeliveryRel();
                wmsPoDeliveryRel.setDeliveryDocId(docId);
                wmsPoDeliveryRel.setDeliveryDocLineId(mtInstruction.getInstructionId());
                wmsPoDeliveryRel.setBomType(WmsConstant.DocType.DELIVERY_DOC);
                List<WmsPoDeliveryRel> wmsPoDeliveryRels = wmsPoDeliveryRelMapper.select(wmsPoDeliveryRel);
                for (WmsPoDeliveryRel poDeliveryRel : wmsPoDeliveryRels) {
                    MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.instructionDocPropertyGet(tenantId, poDeliveryRel.getPoId());
                    if (!Objects.isNull(mtInstructionDoc) && "OUTSOURCING_PO".equals(mtInstructionDoc.getInstructionDocType())) {
                        //??????????????????
                        List<MtExtendVO5> list = new ArrayList<>();
                        MtExtendVO5 extendVO5 = new MtExtendVO5();
                        extendVO5.setAttrName("RECEIVED_QTY");
                        extendVO5.setAttrValue(String.valueOf(poDeliveryRel.getQuantity()));
                        list.add(extendVO5);
                        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_attr", poDeliveryRel.getPoLineId(), eventId, list);
                        MtInstructionVO10 instructionVO10 = new MtInstructionVO10();
                        mtInstruction.setSourceInstructionId(mtInstruction.getInstructionId());
                        //???????????????ID???????????????ID??????
                        List<String> lineIdList = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, instructionVO10);
                        for (String lineId : lineIdList) {
                            MtExtendVO mtExtendVO = new MtExtendVO();
                            mtExtendVO.setTableName("mt_instruction_attr");
                            mtExtendVO.setKeyId(lineId);
                            mtExtendVO.setAttrName("BOM_USUAGE");
                            List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
                            for (MtExtendAttrVO mtExtendAttrVO : mtExtendAttrVOList) {
                                String bomUsuage = mtExtendAttrVO.getAttrValue();
                                // BigDecimal bd1 = poDeliveryRel.getQuantity();
                                BigDecimal bd2 = new BigDecimal(bomUsuage);
                                List<MtExtendVO5> extendVO5List = new ArrayList<>();
                                MtExtendVO5 extendVO53 = new MtExtendVO5();
                                extendVO53.setAttrName("RECEIVED_QTY");
                                extendVO53.setAttrValue(String.valueOf(bd2.multiply(bd2)));
                                extendVO5List.add(extendVO53);
                                mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_attr", poDeliveryRel.getPoLineId(), eventId, extendVO5List);
                            }
                        }
                    }
                }
            }
            /**
             * ???????????????????????????????????????????????? by han.zhang 2020-05-15
             */
            messageClient.sendToAll(WmsConstant.REVEIVED_BOARD_THIRTY_MATRIAL_QUANTITY_UPDATED, MtBaseConstants.YES);
            sendSrmDocStatus(docId, tenantId);
        }catch (Exception e){
            throw new CommonException(e.getMessage());
        }finally {
            //??????
            hmeObjectRecordLockRepository.releaseLock(hmeObjectRecordLock, HmeConstants.ConstantValue.YES);
        }
        return dto;
    }


    /**
     * ?????????SRM???????????????
     *
     * @param instructionDocId
     * @param tenantId
     */
    private void sendSrmDocStatus(String instructionDocId, Long tenantId) {
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue(ItfConstant.LovCode.ITF_TIMELY_INTERNAL_FLAG, tenantId);
        if (CollectionUtils.isEmpty(lovValueDTOS)) {
            throw new CommonException(ItfConstant.LovCode.ITF_TIMELY_INTERNAL_FLAG + "??????????????????\n????????????Y???N???Y??????????????????N?????????????????????");
        }
        String interfaceFlag = lovValueDTOS.get(0).getMeaning();
        if ("Y".equals(interfaceFlag)) {
            //itfSrmInstructionIfaceService.sendInstructionDocStatusSrm();
            List<ItfSrmInstructionIface> itfSrmInstructionIfaces = itfSrmInstructionIfaceService.selectMtDocStatus(instructionDocId, tenantId);
            if (CollectionUtils.isNotEmpty(itfSrmInstructionIfaces)) {
                List<ItfSrmInstructionIface> newList = new ArrayList<>();
                for (ItfSrmInstructionIface iface : itfSrmInstructionIfaces) {
                    if ("SRM".equals(iface.getAttribute1())) {
                        iface.setInstructionDocId(instructionDocId);
                        newList.add(iface);
                    }
                }
                itfSrmInstructionIfaceService.sendInstructionDocStatusSrm(newList, tenantId);
            }
        }
    }

    /**
     * @return void
     * @Description ????????????API??????
     * @author yuchao.wang
     * @date 2020/8/24 18:38
     */
    private void addObjectTransaction(Long tenantId,
                                      String eventId,
                                      String materialLotId,
                                      List<MtCommonExtendVO5> materialLotAttrs,
                                      MtInstructionDoc instructionDoc,
                                      MtInstruction mtInstruction,
                                      List<WmsObjectTransactionRequestVO> objectTransactionList) {
        //??????API???????????????????????????
        MtMaterialLot materialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, materialLotId);
        if (Objects.isNull(materialLot) || StringUtils.isEmpty(materialLot.getMaterialLotId())) {
            throw new MtException("WMS_PO_DELIVERY_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_PO_DELIVERY_0009", "WMS"));
        }

        WmsObjectTransactionRequestVO objectTransactionRequestVO = new WmsObjectTransactionRequestVO() {{
            setTransactionId(null);
            setTenantId(tenantId);
            setTransactionTypeCode("WMS_RECEIVE_FROM_SUPP");
            setEventId(eventId);
            setMaterialLotId(materialLotId);
            setMaterialId(materialLot.getMaterialId());
            setTransactionQty(new BigDecimal(materialLot.getPrimaryUomQty()));
            setLotNumber(materialLot.getLot());
            setTransferLotNumber(materialLot.getLot());
            setTransactionTime(new Date());
            setTransactionReasonCode("????????????");
            setPlantId(materialLot.getSiteId());
            setTransferPlantId(materialLot.getSiteId());
            setTransferLocatorId(materialLot.getLocatorId());
            setSourceDocType(instructionDoc.getInstructionDocType());
            setSourceDocId(instructionDoc.getInstructionDocId());
            setSourceDocLineId(mtInstruction.getInstructionId());
            setMergeFlag("N");
        }};

        //?????????????????????
        if (StringUtils.isNotEmpty(materialLot.getSupplierId())) {
            MtSupplier mtSupplier = new MtSupplier();
            mtSupplier.setSupplierId(materialLot.getSupplierId());
            mtSupplier = supplierRepository.selectByPrimaryKey(mtSupplier);
            if (!Objects.isNull(mtSupplier)) {
                objectTransactionRequestVO.setSupplierCode(mtSupplier.getSupplierCode());
            }
        }

        //??????????????????
        if (StringUtils.isNotEmpty(materialLot.getLocatorId())) {
            MtModLocator mtModLocator = mtModLocatorRepository.locatorBasicPropertyGet(tenantId, materialLot.getLocatorId());
            if (!Objects.isNull(mtModLocator)) {
                objectTransactionRequestVO.setTransferWarehouseId(mtModLocator.getParentLocatorId());
            }
        }

        //????????????????????????
        for (MtCommonExtendVO5 extendAttr : materialLotAttrs) {
            if (HmeConstants.ExtendAttr.SO_NUM.equals(extendAttr.getAttrName())) {
                objectTransactionRequestVO.setSoNum(extendAttr.getAttrValue());
            } else if (HmeConstants.ExtendAttr.SO_LINE_NUM.equals(extendAttr.getAttrName())) {
                objectTransactionRequestVO.setSoLineNum(extendAttr.getAttrValue());
            }
        }

        //??????????????????
        if (StringUtils.isNotEmpty(materialLot.getPrimaryUomId())) {
            MtUomVO mtUomVO = mtUomRepository.uomPropertyGet(tenantId, materialLot.getPrimaryUomId());
            if (!Objects.isNull(mtUomVO)) {
                objectTransactionRequestVO.setTransactionUom(mtUomVO.getUomCode());
            }
        }

        objectTransactionList.add(objectTransactionRequestVO);
    }

    /**
     * @param tenantId
     * @param dtos
     * @return void
     * @Description ??????????????????
     * @Author wenzhang.yu
     * @Date 2020/4/14 18:06
     * @version 1.0
     **/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public WmsPoDeliveryScanLineReturnDTO deleteDetail(Long tenantId, List<WmsPoDeliveryDetailDTO> dtos) {
        if (CollectionUtils.isEmpty(dtos)) {
            throw new MtException("WMS_PO_DELIVERY_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_PO_DELIVERY_0010", "WMS"));
        }

        WmsPoDeliveryScanLineReturnDTO wmsPoDeliveryScanLineReturnDTO = new WmsPoDeliveryScanLineReturnDTO();

        //????????????
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("MATERIALLOT_SCAN_CANCEL");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        BigDecimal exchange = new BigDecimal(0);
        BigDecimal primaryUomQty = new BigDecimal(0);
        BigDecimal actualQty = new BigDecimal(0);
        BigDecimal exchangedQty = new BigDecimal(0);
        //????????????
        BigDecimal quantity = new BigDecimal(0);
        //?????????????????????????????????
        BigDecimal needSplitActualQty = new BigDecimal(0);
        //?????????????????????????????????
        BigDecimal exchangeNeedQty = new BigDecimal(0);


        MtInstruction instruction = mtInstructionRepository.instructionPropertyGet(tenantId, dtos.get(0).getInstructionId());
        String receivePendingLocatorId = "";

        MtModLocatorVO9 mtModLocatorVO9 = new MtModLocatorVO9();
        mtModLocatorVO9.setLocatorId(instruction.getToLocatorId());
        mtModLocatorVO9.setQueryType("FIRST");
        List<String> locatorIds = mtModLocatorRepository.subLocatorQuery(tenantId, mtModLocatorVO9);

        for (String locatorId : locatorIds) {
            MtModLocatorVO7 subLocator = new MtModLocatorVO7();
            subLocator.setLocatorId(locatorId);
            subLocator.setLocatorType("RECEIVE_PENDING");
            List<MtModLocatorVO8> subLocatorList = mtModLocatorRepository.propertyLimitLocatorPropertyQuery(tenantId, subLocator);
            if (CollectionUtils.isNotEmpty(subLocatorList)) {
                receivePendingLocatorId = subLocatorList.get(0).getLocatorId();
            }
        }
        if (StringUtils.isEmpty(receivePendingLocatorId)) {
            MtModLocatorVO7 mtModLocatorVO7 = new MtModLocatorVO7();
            mtModLocatorVO7.setLocatorId(instruction.getToLocatorId());
            List<MtModLocatorVO8> mtModLocatorVO8s = mtModLocatorRepository.propertyLimitLocatorPropertyQuery(tenantId, mtModLocatorVO7);
            String messageLocatorCode = mtModLocatorVO8s.get(0).getLocatorCode();
            throw new MtException("MT_INVENTORY_0057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_INVENTORY_0057", "INVENTORY", messageLocatorCode));
        }


        quantity = new BigDecimal(instruction.getQuantity());

        MtExtendVO extendVO = new MtExtendVO();
        extendVO.setTableName("mt_instruction_attr");
        extendVO.setKeyId(instruction.getInstructionId());
        // ??????????????????????????????
        List<MtExtendAttrVO> attrValueList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);

        //EXCHANGE_FLAG ????????????????????????
        Optional<MtExtendAttrVO> exchangedFlagVO = attrValueList.stream()
                .filter(t -> "EXCHANGE_FLAG".equals(t.getAttrName())).findFirst();
        String exchangedFlag = exchangedFlagVO.get().getAttrValue();

        //??????EXCHANGE_QTY?????????????????????)
        Optional<MtExtendAttrVO> exchangeQtyVO = attrValueList.stream()
                .filter(t -> "EXCHANGE_QTY".equals(t.getAttrName())).findFirst();
        if (exchangeQtyVO.isPresent()) {
            exchange = new BigDecimal(exchangeQtyVO.get().getAttrValue());
        }


        MtInstructionActual mtInstructionActual = new MtInstructionActual();
        mtInstructionActual.setInstructionId(instruction.getInstructionId());
        mtInstructionActual.setInstructionType(WmsConstant.InstructionType.RECEIVE_FROM_SUPPLIER);
        mtInstructionActual.setBusinessType("PO_RECEIVING");
        mtInstructionActual.setMaterialId(instruction.getMaterialId());
        List<String> actualIdList = mtInstructionActualRepository.propertyLimitInstructionActualQuery(tenantId,
                mtInstructionActual);
        if (CollectionUtils.isNotEmpty(actualIdList)) {
            MtInstructionActual mtInstructionActual1 = mtInstructionActualRepository.instructionActualPropertyGet(tenantId, actualIdList.get(0));
            if (!Objects.isNull(mtInstructionActual1)) {
                actualQty = new BigDecimal(mtInstructionActual1.getActualQty());
            }
            MtExtendSettings mtExtendSettings = new MtExtendSettings();
            mtExtendSettings.setAttrName("EXCHANGED_QTY");
            List<MtExtendSettings> attrList = new ArrayList<>(1);
            attrList.add(mtExtendSettings);
            List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingMapper.attrPropertyQuery(tenantId,
                    "mt_instruction_actual_attr", "ACTUAL_ID", actualIdList.get(0), attrList);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS)) {
                exchangedQty = new BigDecimal(mtExtendAttrVOS.get(0).getAttrValue());
            }

        }


//        if (exchangedFlag.equals(WmsConstant.CONSTANT_Y) && (exchange.compareTo(exchangedQty) != 0)) {
        if (WmsConstant.CONSTANT_Y.equals(exchangedFlag)) {
            //??????????????????

            for (WmsPoDeliveryDetailDTO dto :
                    dtos) {
                //????????????
                MtMaterialLotVO3 param = new MtMaterialLotVO3();
                param.setMaterialLotCode(dto.getMaterialLotCode());
                List<String> materialLotIds = mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, param);
                String materialLotId = materialLotIds.get(0);
                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, materialLotId);
                primaryUomQty = new BigDecimal(mtMaterialLot.getPrimaryUomQty());

                Double subResQty = exchange.subtract(exchangedQty).doubleValue();
                if (actualQty.compareTo(primaryUomQty) > 0) {
                    needSplitActualQty = primaryUomQty;
                } else {
                    needSplitActualQty = actualQty;
                    exchangeNeedQty = primaryUomQty.subtract(actualQty);
                }
                actualQty = actualQty.subtract(primaryUomQty);
                exchangedQty = exchangedQty.subtract(exchangeNeedQty);
                if (actualQty.compareTo(new BigDecimal(0)) < 0) {
                    actualQty = new BigDecimal(0);
                }
                if (exchangedQty.compareTo(new BigDecimal(0)) < 0) {
                    exchangedQty = new BigDecimal(0);
                }

                //????????????
                MtEventCreateVO eventCreateVO1 = new MtEventCreateVO();
                eventCreateVO1.setEventTypeCode("MATERIALLOT_SCAN_CANCEL");
                String eventId1 = mtEventRepository.eventCreate(tenantId, eventCreateVO1);

                MtInstructionActualVO mtInstructionActualVO = new MtInstructionActualVO();

                mtInstructionActualVO.setActualId(actualIdList.size() > 0 ? actualIdList.get(0) : "");
                mtInstructionActualVO.setInstructionId(instruction.getInstructionId());
                mtInstructionActualVO.setInstructionType(WmsConstant.InstructionType.RECEIVE_FROM_SUPPLIER);
                mtInstructionActualVO.setSourceOrderType(WmsConstant.DocType.DELIVERY_DOC);
                mtInstructionActualVO.setSourceOrderId(instruction.getSourceDocId());
                mtInstructionActualVO.setBusinessType("PO_RECEIVING");
                mtInstructionActualVO.setMaterialId(instruction.getMaterialId());
                mtInstructionActualVO.setUomId(instruction.getUomId());
                mtInstructionActualVO.setActualQty(-needSplitActualQty.doubleValue());
                mtInstructionActualVO.setEventId(eventId1);
                mtInstructionActualVO.setSupplierId(instruction.getSupplierId());
                mtInstructionActualVO.setToSiteId(instruction.getToSiteId());
                mtInstructionActualVO.setToLocatorId(instruction.getToLocatorId());
                mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionActualVO);

                //??????????????????
                MtInstructionActualDetail mtInstructionActualDetail2 = new MtInstructionActualDetail();
                mtInstructionActualDetail2.setTenantId(tenantId);
                mtInstructionActualDetail2.setActualId(mtInstructionActualVO.getActualId());
                mtInstructionActualDetail2.setMaterialLotId(materialLotId);
                mtInstructionActualDetailRepository.delete(mtInstructionActualDetail2);

                MtInstructionActualDetail mtInstructionActualDetail = new MtInstructionActualDetail();
                mtInstructionActualDetail.setTenantId(tenantId);
                mtInstructionActualDetail.setActualId(actualIdList.get(0));
                mtInstructionActualDetail.setMaterialLotId(materialLotId);
                List<MtInstructionActualDetail> detailList = mtInstructionActualDetailRepository.select(mtInstructionActualDetail);

                if (CollectionUtils.isNotEmpty(detailList)) {
                    MtInstructionActualDetail deleteDetail = new MtInstructionActualDetail();
                    deleteDetail.setTenantId(tenantId);
                    deleteDetail.setActualDetailId(detailList.get(0).getActualDetailId());
                    deleteDetail.setObjectVersionNumber(detailList.get(0).getObjectVersionNumber());
                    mtInstructionActualMapper.deleteByPrimaryKey(deleteDetail);
                }


                //??????API??????????????????
                MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
                mtMaterialLotAttrVO2.setMaterialLotId(materialLotId);
                mtMaterialLotAttrVO2.setAttrName(HmeConstants.ExtendAttr.STATUS);
                List<MtExtendAttrVO> materialLotAttrVO = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
                if ("TO_ACCEPT".equals(materialLotAttrVO.get(0).getAttrValue())) {
                    throw new MtException("MT_INVENTORY_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0047", "INVENTORY"));
                }


                //	????????????
                mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
                    setEventId(eventId);
                    setLot("");
                    setMaterialLotCode(dto.getMaterialLotCode());
                }}, "N");

                //??????api????????????
                List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
                MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                mtExtendVO5.setAttrName(HmeConstants.ExtendAttr.STATUS);
                mtExtendVO5.setAttrValue(HmeConstants.StatusCode.NEW);
                mtExtendVO5List.add(mtExtendVO5);
                mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, new MtExtendVO10() {{
                    setKeyId(materialLotId);
                    setEventId(eventId);
                    setAttrs(mtExtendVO5List);
                }});
            }
        } else {
            //?????????????????????
            for (WmsPoDeliveryDetailDTO dto :
                    dtos) {
                //????????????
                MtMaterialLotVO3 param = new MtMaterialLotVO3();
                param.setMaterialLotCode(dto.getMaterialLotCode());
                List<String> materialLotIds = mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, param);
                String materialLotId = materialLotIds.get(0);
                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, materialLotId);
                primaryUomQty = new BigDecimal(mtMaterialLot.getPrimaryUomQty());

                //????????????????????????
                actualQty = actualQty.subtract(primaryUomQty);
                if (actualQty.compareTo(new BigDecimal(0)) < 0) {
                    actualQty = new BigDecimal(0);
                }

                needSplitActualQty = primaryUomQty;
                //????????????
                MtEventCreateVO eventCreateVO1 = new MtEventCreateVO();
                eventCreateVO1.setEventTypeCode("MATERIALLOT_SCAN_CANCEL");
                String eventId1 = mtEventRepository.eventCreate(tenantId, eventCreateVO1);

                MtInstructionActualVO mtInstructionActualVO = new MtInstructionActualVO();

                mtInstructionActualVO.setActualId(actualIdList.size() > 0 ? actualIdList.get(0) : "");
                mtInstructionActualVO.setInstructionId(instruction.getInstructionId());
                mtInstructionActualVO.setInstructionType(WmsConstant.InstructionType.RECEIVE_FROM_SUPPLIER);
                mtInstructionActualVO.setSourceOrderType(WmsConstant.DocType.DELIVERY_DOC);
                mtInstructionActualVO.setSourceOrderId(instruction.getSourceDocId());
                mtInstructionActualVO.setBusinessType("PO_RECEIVING");
                mtInstructionActualVO.setMaterialId(instruction.getMaterialId());
                mtInstructionActualVO.setUomId(instruction.getUomId());
                mtInstructionActualVO.setActualQty(-needSplitActualQty.doubleValue());
                mtInstructionActualVO.setEventId(eventId1);
                mtInstructionActualVO.setSupplierId(instruction.getSupplierId());
                mtInstructionActualVO.setToSiteId(instruction.getToSiteId());
                mtInstructionActualVO.setToLocatorId(instruction.getToLocatorId());
                mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionActualVO);

                //?????????????????????
                MtInstructionActualDetail mtInstructionActualDetail2 = new MtInstructionActualDetail();
                mtInstructionActualDetail2.setTenantId(tenantId);
                mtInstructionActualDetail2.setActualId(mtInstructionActualVO.getActualId());
                mtInstructionActualDetail2.setMaterialLotId(materialLotId);
                mtInstructionActualDetailRepository.delete(mtInstructionActualDetail2);

                MtInstructionActualDetail mtInstructionActualDetail = new MtInstructionActualDetail();
                mtInstructionActualDetail.setTenantId(tenantId);
                mtInstructionActualDetail.setActualId(actualIdList.get(0));
                mtInstructionActualDetail.setMaterialLotId(materialLotId);
                List<MtInstructionActualDetail> detailList = mtInstructionActualDetailRepository.select(mtInstructionActualDetail);

                if (CollectionUtils.isNotEmpty(detailList)) {
                    MtInstructionActualDetail deleteDetail = new MtInstructionActualDetail();
                    deleteDetail.setTenantId(tenantId);
                    deleteDetail.setActualDetailId(detailList.get(0).getActualDetailId());
                    deleteDetail.setObjectVersionNumber(detailList.get(0).getObjectVersionNumber());
                    mtInstructionActualMapper.deleteByPrimaryKey(deleteDetail);
                }


                //??????API??????????????????
                MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
                mtMaterialLotAttrVO2.setMaterialLotId(materialLotId);
                mtMaterialLotAttrVO2.setAttrName(HmeConstants.ExtendAttr.STATUS);
                List<MtExtendAttrVO> materialLotAttrVO = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
                if ("TO_ACCEPT".equals(materialLotAttrVO.get(0).getAttrValue())) {
                    throw new MtException("MT_INVENTORY_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0047", "INVENTORY"));
                }


                //	????????????
                mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
                    setEventId(eventId);
                    setLot("");
                    setMaterialLotCode(dto.getMaterialLotCode());
                }}, "N");

                //??????api????????????
                List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
                MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                mtExtendVO5.setAttrName(HmeConstants.ExtendAttr.STATUS);
                mtExtendVO5.setAttrValue(HmeConstants.StatusCode.NEW);
                mtExtendVO5List.add(mtExtendVO5);
                mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, new MtExtendVO10() {{
                    setKeyId(materialLotId);
                    setEventId(eventId);
                    setAttrs(mtExtendVO5List);
                }});
            }

        }
        if (WmsConstant.CONSTANT_Y.equals(exchangedFlagVO.get().getAttrValue())) {
            //??????????????????
            MtInstructionVO mtInstructionVO = new MtInstructionVO();
            Double doubleActualQty = actualQty.doubleValue();
            Double doubleQuantity = quantity.doubleValue();
            if (doubleActualQty <= 0) {
                mtInstructionVO.setInstructionStatus("RELEASED");
            } else if (doubleActualQty > 0 && doubleActualQty < doubleQuantity) {
                mtInstructionVO.setInstructionStatus(WmsConstant.InstructionStatus.RECEIVE_EXECUTE);
            } else if (doubleActualQty.equals(doubleQuantity)) {
                mtInstructionVO.setInstructionStatus("COMPLETED");
            }
            mtInstructionVO.setInstructionId(instruction.getInstructionId());
            mtInstructionVO.setEventId(eventId);
            mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, "N");


            if (StringUtils.isNotEmpty(mtInstructionVO.getInstructionStatus())) {
                List<LovValueDTO> lovList = commonApiService.queryLovValueList(tenantId, "WMS.DELIVERY_DOC_LINE.STATUS", mtInstructionVO.getInstructionStatus());
                wmsPoDeliveryScanLineReturnDTO.setStatusMeaning(lovList.get(0).getMeaning());
            }

            List<MtExtendVO5> attrs = new ArrayList<>();
            MtExtendVO5 mtExtendVO51 = new MtExtendVO5();
            mtExtendVO51.setAttrName("EXCHANGED_QTY");
            mtExtendVO51.setAttrValue(String.valueOf(exchangedQty));
            attrs.add(mtExtendVO51);

            MtExtendVO5 mtExtendVO52 = new MtExtendVO5();
            mtExtendVO52.setAttrName("ACTUAL_RECEIVE_QTY");
            mtExtendVO52.setAttrValue(String.valueOf(actualQty));
            attrs.add(mtExtendVO52);

            MtExtendVO10 actualAttrUpdate = new MtExtendVO10();
            actualAttrUpdate.setEventId(eventId);
            actualAttrUpdate.setKeyId(actualIdList.get(0));
            actualAttrUpdate.setAttrs(attrs);
            mtInstructionActualRepository.instructionActualAttrPropertyUpdate(tenantId, actualAttrUpdate);
        } else {
            //??????????????????
            MtInstructionVO mtInstructionVO = new MtInstructionVO();
            Double doubleActualQty = actualQty.doubleValue();
            Double doubleQuantity = quantity.doubleValue();
            if (doubleActualQty == 0) {
                mtInstructionVO.setInstructionStatus(WmsConstant.InstructionStatus.RELEASED);
            } else if (doubleActualQty > 0 && doubleActualQty < doubleQuantity) {
                mtInstructionVO.setInstructionStatus(WmsConstant.InstructionStatus.RECEIVE_EXECUTE);
            } else if (doubleActualQty.equals(doubleQuantity)) {
                mtInstructionVO.setInstructionStatus(WmsConstant.InstructionStatus.COMPLETED);
            }

            if (StringUtils.isNotEmpty(mtInstructionVO.getInstructionStatus())) {
                List<LovValueDTO> lovList = commonApiService.queryLovValueList(tenantId, "WMS.DELIVERY_DOC_LINE.STATUS", mtInstructionVO.getInstructionStatus());
                wmsPoDeliveryScanLineReturnDTO.setStatusMeaning(lovList.get(0).getMeaning());
            }

            mtInstructionVO.setInstructionId(instruction.getInstructionId());
            mtInstructionVO.setEventId(eventId);
            mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, "N");
        }
        MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.instructionDocPropertyGet(tenantId, instruction.getSourceDocId());
        //??????????????????????????????????????????
        if (WmsConstant.InstructionStatus.RECEIVE_EXECUTE.equals(mtInstructionDoc.getInstructionDocStatus())) {
            //???????????????
            MtInstructionVO10 mtInstructionTemp = new MtInstructionVO10();
            mtInstructionTemp.setSourceDocId(mtInstructionDoc.getInstructionDocId());
            //???????????????ID???????????????ID??????
            List<String> instructionIdListTemp = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstructionTemp);
            List<MtInstruction> instructionTemp = mtInstructionRepository.instructionPropertyBatchGet(tenantId, instructionIdListTemp);
            List<MtInstruction> instructionTemp1 =
                    instructionTemp.parallelStream().filter(s -> !"RELEASED".equals(s.getInstructionStatus())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(instructionTemp1)) {
                //	???????????????
                MtInstructionDocDTO2 mtInstructionDoc2 = new MtInstructionDocDTO2();
                mtInstructionDoc2.setEventId(eventId);
                mtInstructionDoc2.setInstructionDocId(mtInstructionDoc.getInstructionDocId());
                mtInstructionDoc2.setInstructionDocStatus("RELEASED");
                mtInstructionDocRepository.instructionDocUpdate(tenantId, mtInstructionDoc2, "N");
            }
        }
        return wmsPoDeliveryScanLineReturnDTO;
    }

    /**
     * @param tenantId
     * @param instructionId
     * @return com.ruike.wms.api.dto.WmsPoDeliveryDetailReturnDTO
     * @Description ??????????????????
     * @Author wenzhang.yu
     * @Date 2020/4/14 18:06
     * @version 1.0
     **/
    @Override
    public WmsPoDeliveryDetailReturnDTO queryDetail(Long tenantId, String instructionId) {
        //?????????
        WmsPoDeliveryDetailReturnDTO wmsPoDeliveryDetailReturnDTO = new WmsPoDeliveryDetailReturnDTO();
        //??????API???????????????????????????
        wmsPoDeliveryDetailReturnDTO.setQcFlag(WmsConstant.CONSTANT_Y);
        //	??????????????????
        MtExtendVO extendVO = new MtExtendVO();
        extendVO.setTableName("mt_instruction_attr");
        extendVO.setKeyId(instructionId);
        // ??????????????????????????????
        List<MtExtendAttrVO> attrValueList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);

        //EXCHANGE_FLAG ????????????????????????
        Optional<MtExtendAttrVO> exchangedFlagVO = attrValueList.stream()
                .filter(t -> "EXCHANGE_FLAG".equals(t.getAttrName())).findFirst();
        wmsPoDeliveryDetailReturnDTO.setExchangedFlag(exchangedFlagVO.get().getAttrValue());

        //??????EXCHANGE_QTY?????????????????????)
        Optional<MtExtendAttrVO> exchangeQtyVO = attrValueList.stream()
                .filter(t -> "EXCHANGE_QTY".equals(t.getAttrName())).findFirst();
        if (exchangeQtyVO.isPresent()) {
            wmsPoDeliveryDetailReturnDTO.setExchangeQty(exchangeQtyVO.get().getAttrValue());
        } else {
            wmsPoDeliveryDetailReturnDTO.setExchangeQty("0");
        }
        //UAI_FLAG??????????????? ???
        Optional<MtExtendAttrVO> uaiFlagVO = attrValueList.stream()
                .filter(t -> HmeConstants.ExtendAttr.UAI_FLAG.equals(t.getAttrName())).findFirst();
        // wmsPoDeliveryDetailReturnDTO.setUaiFlag(uaiFlagVO.get().getAttrValue());


        //?????????????????????
        MtInstruction mtInstruction = mtInstructionRepository.instructionPropertyGet(tenantId, instructionId);

        String actualId = "";
        MtInstructionActual mtInstructionActual = new MtInstructionActual();
        mtInstructionActual.setInstructionId(mtInstruction.getInstructionId());
        mtInstructionActual.setInstructionType(WmsConstant.InstructionType.RECEIVE_FROM_SUPPLIER);
        mtInstructionActual.setBusinessType("PO_RECEIVING");
        mtInstructionActual.setMaterialId(mtInstruction.getMaterialId());
        List<String> actualIdList = mtInstructionActualRepository.propertyLimitInstructionActualQuery(tenantId,
                mtInstructionActual);
        if (CollectionUtils.isNotEmpty(actualIdList)) {
            actualId = actualIdList.get(0);
        }


        String exchangedQty = "0";
        //??????????????????
        String actualQty = "0";

        List<String> materialLotIds = new ArrayList<>();

        if (StringUtils.isNotEmpty(actualId)) {
            MtExtendVO mtExtendVO = new MtExtendVO();
            mtExtendVO.setTableName("mt_instruction_actual_attr");
            mtExtendVO.setKeyId(actualId);
            mtExtendVO.setAttrName("EXCHANGED_QTY");
            List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
                exchangedQty = mtExtendAttrVOList.get(0).getAttrValue();
            }
            MtInstructionActual mtInstructionActual1 = mtInstructionActualRepository.instructionActualPropertyGet(tenantId, actualId);
            if (!Objects.isNull(mtInstructionActual1)) {
                actualQty = String.valueOf(mtInstructionActual1.getActualQty());
            }

            MtInstructionActualDetail detail = new MtInstructionActualDetail();
            detail.setActualId(actualId);
            List<MtInstructionActualDetailVO> detailList = mtInstructionActualDetailRepository
                    .propertyLimitInstructionActualDetailQuery(tenantId, detail);

            materialLotIds = detailList.stream().sorted(Comparator.comparing(MtInstructionActualDetailVO::getCreationDate).reversed()).map(MtInstructionActualDetailVO::getMaterialLotId).collect(Collectors.toList());
        }
        wmsPoDeliveryDetailReturnDTO.setExchangedQty(exchangedQty);

        wmsPoDeliveryDetailReturnDTO.setCoverQty(Double.valueOf(actualQty));


        //??????API??????????????????
        MtInstruction instruction = mtInstructionRepository.instructionPropertyGet(tenantId, instructionId);

        //???????????? ????????????
        MtMaterialVO material = mtMaterialRepository.materialPropertyGet(tenantId, instruction.getMaterialId());
        wmsPoDeliveryDetailReturnDTO.setMaterialCode(material.getMaterialCode());
        wmsPoDeliveryDetailReturnDTO.setMaterialName(material.getMaterialName());

        //????????????
        wmsPoDeliveryDetailReturnDTO.setInstructionStatus(instruction.getInstructionStatus());
        wmsPoDeliveryDetailReturnDTO.setQuantity(instruction.getQuantity());

        wmsPoDeliveryDetailReturnDTO.setCoverQty(instruction.getCoverQty());
        //??????
        if (StringUtils.isNotEmpty(instruction.getUomId())) {
            MtUomVO mtUomVO = mtUomRepository.uomPropertyGet(tenantId, instruction.getUomId());
            if (!Objects.isNull(mtUomVO)) {
                wmsPoDeliveryDetailReturnDTO.setUomCode(mtUomVO.getUomCode());
            }
        }

        List<WmsPoDeliveryDetailReturnDTO.WmsPoDeliveryDetailLineReturnDTO> lineReturnDTOList = new ArrayList<>();

        //????????????
        if (CollectionUtils.isNotEmpty(materialLotIds)) {
            for (String materialLotId : materialLotIds) {
                //??????API??????????????????
                MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
                mtMaterialLotAttrVO2.setMaterialLotId(materialLotId);
                mtMaterialLotAttrVO2.setAttrName(HmeConstants.ExtendAttr.STATUS);
                List<MtExtendAttrVO> materialLotAttrVO = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
                if ("SCANNED".equals(materialLotAttrVO.get(0).getAttrValue()) || "TO_ACCEPT".equals(materialLotAttrVO.get(0).getAttrValue())) {
                    WmsPoDeliveryDetailReturnDTO.WmsPoDeliveryDetailLineReturnDTO wmsPoDeliveryDetailLineReturnDTO = new WmsPoDeliveryDetailReturnDTO.WmsPoDeliveryDetailLineReturnDTO();

                    //????????????
                    MtMaterialLot mtMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, materialLotId);
                    wmsPoDeliveryDetailLineReturnDTO.setMateriallotCode(mtMaterialLot.getMaterialLotCode());
                    wmsPoDeliveryDetailLineReturnDTO.setLot(mtMaterialLot.getLot());
                    //???????????? ????????????
                    MtMaterialVO materialTemp = mtMaterialRepository.materialPropertyGet(tenantId, mtMaterialLot.getMaterialId());
                    wmsPoDeliveryDetailLineReturnDTO.setMaterialCode(materialTemp.getMaterialCode());
                    wmsPoDeliveryDetailLineReturnDTO.setMaterialName(materialTemp.getMaterialName());

                    //??????
                    wmsPoDeliveryDetailLineReturnDTO.setPrimaryUomQty(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()));

                    //??????
                    if (StringUtils.isNotEmpty(mtMaterialLot.getPrimaryUomId())) {
                        MtUomVO mtUomVO = mtUomRepository.uomPropertyGet(tenantId, mtMaterialLot.getPrimaryUomId());
                        if (!Objects.isNull(mtUomVO)) {
                            wmsPoDeliveryDetailLineReturnDTO.setUomCode(mtUomVO.getUomCode());
                        }
                    }

                    //????????????
                    List<LovValueDTO> lovList = commonApiService.queryLovValueList(tenantId, "WMS.MTLOT.QUALITY_STATUS", mtMaterialLot.getQualityStatus());
                    wmsPoDeliveryDetailLineReturnDTO.setQualityStatus(lovList.get(0).getMeaning());

                    //????????????
//                MtModLocator mtModLocator = mtModLocatorRepository.locatorBasicPropertyGet(tenantId, mtMaterialLot.getLocatorId());
//                wmsPoDeliveryDetailLineReturnDTO.setLocatorCode(mtModLocator.getLocatorCode());

                    lineReturnDTOList.add(wmsPoDeliveryDetailLineReturnDTO);
                }
            }
        } else {
            throw new MtException("WMS_PO_DELIVERY_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_PO_DELIVERY_0011", "WMS"));
        }

        wmsPoDeliveryDetailReturnDTO.setLineReturnDTOList(lineReturnDTOList);
        return wmsPoDeliveryDetailReturnDTO;
    }

    /**
     * @param tenantId      1
     * @param nowActualQty  2
     * @param mtMaterialLot 3
     * @param toLocatorId   4
     * @return : void
     * @Description: ????????????
     * @author: tong.li
     * @date 2020/6/11 20:32
     * @version 1.0
     */
    private void instructionExecute(Long tenantId, BigDecimal nowActualQty, WmsMaterialLotAttrVO
            mtMaterialLot, String toLocatorId, WmsInstructionAttrVO instruction, String actualId) {
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "PO_RECEIVING_BARCODE_SCAN");
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("MATERIALLOT_SCAN_CANCEL");
        eventCreateVO.setEventRequestId(eventRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        // ????????????
        MtInstructionActualVO mtInstructionActualVO = new MtInstructionActualVO();
        mtInstructionActualVO.setActualId(actualId);
        mtInstructionActualVO.setInstructionId(instruction.getInstructionId());
        mtInstructionActualVO.setInstructionType(WmsConstant.InstructionType.RECEIVE_FROM_SUPPLIER);
        mtInstructionActualVO.setSourceOrderType(WmsConstant.DocType.DELIVERY_DOC);
        mtInstructionActualVO.setSourceOrderId(instruction.getSourceDocId());
        mtInstructionActualVO.setBusinessType("PO_RECEIVING");
        mtInstructionActualVO.setMaterialId(instruction.getMaterialId());
        mtInstructionActualVO.setUomId(instruction.getUomId());
        mtInstructionActualVO.setActualQty(nowActualQty.doubleValue());
        mtInstructionActualVO.setEventId(eventId);
        mtInstructionActualVO.setSupplierId(instruction.getSupplierId());
        mtInstructionActualVO.setToSiteId(instruction.getToSiteId());
        mtInstructionActualVO.setToLocatorId(toLocatorId);
        MtInstructionActualVO1 mtInstructionActualVO1 = mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionActualVO);

        MtInstructionActualDetail mtInstructionActualDetail = new MtInstructionActualDetail();
        mtInstructionActualDetail.setActualId(mtInstructionActualVO1.getActualId());
        mtInstructionActualDetail.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        mtInstructionActualDetail.setUomId(mtMaterialLot.getPrimaryUomId());
        mtInstructionActualDetail.setActualQty(mtMaterialLot.getPrimaryUomQty().doubleValue());
        mtInstructionActualDetailRepository
                .instructionActualDetailCreate(tenantId, mtInstructionActualDetail);

    }

    private void createBill(MtInstructionDoc instructionDoc, String lotNumber, String uaiFlag, MtInstruction
            instruction,
                            BigDecimal exchangedQty, BigDecimal actualQty, String receivePendingLocatorId, Long tenantId, String
                                    materialVersion) {
        try {
            QmsIqcHeader iqcHeader = new QmsIqcHeader();
            iqcHeader.setSiteId(instructionDoc.getSiteId());
            iqcHeader.setReceiptLot(lotNumber);
            iqcHeader.setReceiptBy(String.valueOf(DetailsHelper.getUserDetails().getUserId()));
            iqcHeader.setDocType(WmsConstant.DocType.DELIVERY_DOC);
            if (WmsConstant.CONSTANT_Y.equals(uaiFlag)) {
                iqcHeader.setUaiFlag(WmsConstant.CONSTANT_Y);
            }
            iqcHeader.setDocHeaderId(instructionDoc.getInstructionDocId());
            iqcHeader.setDocLineId(instruction.getInstructionId());
            iqcHeader.setMaterialId(instruction.getMaterialId());
            iqcHeader.setMaterialVersion(materialVersion);
            iqcHeader.setQuantity(exchangedQty.add(actualQty));
            iqcHeader.setUomId(instruction.getUomId());
            iqcHeader.setSupplierId(instructionDoc.getSupplierId());
            iqcHeader.setLocatorId(receivePendingLocatorId);
            iqcHeader.setCreatedDate(new Date());

            Boolean urgentFlag = false;
            //??????????????????
            List<MtExtendAttrVO1> mtExtendAttrVO1s1 = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId,
                    new MtExtendVO1("mt_instruction_doc_attr", Collections.singletonList(instructionDoc.getInstructionDocId()), "URGENT_FLAG"));
            for (MtExtendAttrVO1 extendAttr :
                    mtExtendAttrVO1s1) {
                //????????????
                if ("URGENT_FLAG".equals(extendAttr.getAttrName())) {
                    urgentFlag = true;
                }
            }
            if (urgentFlag) {
                iqcHeader.setIdentification("URGENT");
            }
            iqcHeaderRepository.createIqcBill(tenantId, iqcHeader);
        } catch (Exception e) {
            log.info("????????????????????????");
            //2021-09-24 16:27 edit by choanan.hu for peng.zhao ??????????????????siteId????????????siteCode???2000????????????????????????????????????
            MtModSite mtModSite = mtModSiteRepository.selectByPrimaryKey(instructionDoc.getSiteId());
            if("2000".equals(mtModSite.getSiteCode())){
                throw new MtException("Exception","????????????????????????");
            }
        }
    }

    /**
     * ?????????????????????
     *
     * @param tenantId
     * @param siteId
     * @param supplierId
     * @param materialId
     * @return java.lang.Boolean
     * @author jiangling.zheng@hand-china.com 2020/9/28 10:54
     */
    private Boolean getExemptionFlag(Long tenantId, String siteId, String supplierId, String materialId) {
        Boolean exemptionFlag = false;
        QmsMaterialInspExempt qmsMaterialInspExempt = new QmsMaterialInspExempt();
        qmsMaterialInspExempt.setSiteId(siteId);
        qmsMaterialInspExempt.setSupplierId(supplierId);
        qmsMaterialInspExempt.setMaterialId(materialId);
        qmsMaterialInspExempt.setTenantId(tenantId);
        qmsMaterialInspExempt.setType("PO_RECEIVING");
        qmsMaterialInspExempt.setEnableFlag(WmsConstant.CONSTANT_Y);
        List<QmsMaterialInspExempt> qmsMaterialInspExempts = qmsMaterialInspExemptRepository.select(qmsMaterialInspExempt);
        if (CollectionUtils.isNotEmpty(qmsMaterialInspExempts) &&
                WmsConstant.CONSTANT_Y.equals(qmsMaterialInspExempts.get(0).getExemptionFlag())) {
            exemptionFlag = true;
        }
        return exemptionFlag;
    }

    private Boolean getFreeCheckFlag(Long tenantId, String siteId, String materialId) {
        Boolean exemptionFlag = false;

        List<String> freeCheckFlagList = wmsPoDeliveryRelMapper.getFreeCheckFlag(tenantId, siteId, materialId);
        if (CollectionUtils.isNotEmpty(freeCheckFlagList) && WmsConstant.CONSTANT_X.equals(freeCheckFlagList.get(0))) {
            exemptionFlag = true;
        }
        return exemptionFlag;
    }

    /**
     * ????????????
     *
     * @param tenantId
     * @param receivePendingLocatorId
     * @param instructionDoc
     * @param instruction
     * @return void
     * @author jiangling.zheng@hand-china.com 2020/9/28 11:01
     */
    private void billProcess(Long tenantId, String receivePendingLocatorId, MtInstructionDoc
            instructionDoc, MtInstruction instruction) {
        WmsPoDeliveryRelDTO poDeliveryRelDTO = new WmsPoDeliveryRelDTO();
        poDeliveryRelDTO.setDocId(instructionDoc.getInstructionDocId());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        poDeliveryRelDTO.setNowDateFrom(zero);

        Calendar calendarTo = Calendar.getInstance();
        calendarTo.setTime(new Date());
        calendarTo.set(Calendar.HOUR_OF_DAY, 23);
        calendarTo.set(Calendar.MINUTE, 59);
        calendarTo.set(Calendar.SECOND, 59);
        Date dateTo = calendarTo.getTime();
        poDeliveryRelDTO.setNowDateTo(dateTo);
        String lotNumber = wmsPoDeliveryRelMapper.selectLot(tenantId, poDeliveryRelDTO);

        BigDecimal exchangedQty = BigDecimal.ZERO;
        BigDecimal actualQty = BigDecimal.ZERO;

        MtInstructionActual mtInstructionActual = new MtInstructionActual();
        mtInstructionActual.setInstructionId(instruction.getInstructionId());
        mtInstructionActual.setInstructionType(WmsConstant.InstructionType.RECEIVE_FROM_SUPPLIER);
        mtInstructionActual.setBusinessType("PO_RECEIVING");
        mtInstructionActual.setMaterialId(instruction.getMaterialId());
        List<String> actualIdList = mtInstructionActualRepository.propertyLimitInstructionActualQuery(tenantId,
                mtInstructionActual);
        if (CollectionUtils.isNotEmpty(actualIdList)) {
            MtInstructionActual mtInstructionActual1 = mtInstructionActualRepository.selectByPrimaryKey(actualIdList.get(0));
            if (!Objects.isNull(mtInstructionActual1)) {
                actualQty = BigDecimal.valueOf(mtInstructionActual1.getActualQty());
            }
            MtExtendSettings mtExtendSettings = new MtExtendSettings();
            mtExtendSettings.setAttrName("EXCHANGED_QTY");
            List<MtExtendSettings> attrList = new ArrayList<>(1);
            attrList.add(mtExtendSettings);
            List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingMapper.attrPropertyQuery(tenantId,
                    "mt_instruction_actual_attr", "ACTUAL_ID", actualIdList.get(0), attrList);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS)) {
                exchangedQty = new BigDecimal(mtExtendAttrVOS.get(0).getAttrValue());
            }

        }
        String uaiFlag = "N";
        String materialVersion = "";
        //??????????????????
        List<MtExtendAttrVO1> mtExtendAttrVO1s2 = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId,
                new MtExtendVO1("mt_instruction_attr", Collections.singletonList(instruction.getInstructionId()), HmeConstants.ExtendAttr.UAI_FLAG, HmeConstants.ExtendAttr.MATERIAL_VERSION));
        for (MtExtendAttrVO1 extendAttr :
                mtExtendAttrVO1s2) {
            //????????????
            if (HmeConstants.ExtendAttr.UAI_FLAG.equals(extendAttr.getAttrName())) {
                uaiFlag = extendAttr.getAttrValue();
            }
            //????????????
            if (HmeConstants.ExtendAttr.MATERIAL_VERSION.equals(extendAttr.getAttrName())) {
                materialVersion = extendAttr.getAttrValue();
            }

        }
        //???????????????
        createBill(instructionDoc, lotNumber, uaiFlag, instruction, exchangedQty, actualQty, receivePendingLocatorId, tenantId, materialVersion);
    }

    private List<WmsObjectTransactionRequestVO> instructionActualProcess(Long tenantId, WmsPoDeliveryConfirmDTO2
            dto) {
        log.info("api confirmAcceptance instructionActualProcess  WmsPoDeliveryConfirmDTO2: {}", JSON.toJSONString(dto));
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        String eventId = dto.getEventId();
        Boolean exemptionFlag = dto.getExemptionFlag();
        String receivePendingLocatorId = dto.getReceivePendingLocatorId();
        MtInstructionDoc instructionDoc = dto.getInstructionDoc();
        MtInstruction instruction = dto.getInstruction();
        MtInstructionActual mtInstructionActual = new MtInstructionActual();
        mtInstructionActual.setInstructionId(instruction.getInstructionId());
        mtInstructionActual.setInstructionType(WmsConstant.InstructionType.RECEIVE_FROM_SUPPLIER);
        mtInstructionActual.setBusinessType("PO_RECEIVING");
        List<String> actualIdList = mtInstructionActualRepository.propertyLimitInstructionActualQuery(tenantId,
                mtInstructionActual);
        MtInstructionActualDetail detail = new MtInstructionActualDetail();
        detail.setActualId(actualIdList.get(0));
        List<MtInstructionActualDetailVO> detailList = mtInstructionActualDetailRepository
                .propertyLimitInstructionActualDetailQuery(tenantId, detail);
        log.info("api confirmAcceptance instructionActualProcess  detailList:{}", JSONArray.toJSONString(detailList));
        // ???detailList??????????????????materialLotId???????????????????????????API{materialLotInitialize}{materialLotAttrPropertyUpdate}
        if (CollectionUtils.isNotEmpty(detailList)) {
            Double exchangedQty = 0.0D;
            int j = 1;
            List<MtMaterialLotVO20> materialLotList = detailList.stream().map(t -> new MtMaterialLotVO20() {{
                setMaterialLotId(t.getMaterialLotId());
                setLocatorId(receivePendingLocatorId);
                setEnableFlag(WmsConstant.CONSTANT_Y);
                setLot(dto.getLot());
                setQualityStatus(exemptionFlag ? "PENDING" : "OK");
            }}).collect(Collectors.toList());
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, materialLotList, eventId, "N");

            List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>();
            for (MtInstructionActualDetailVO mtInstructionActualDetailVO : detailList) {
                String materialLotId = mtInstructionActualDetailVO.getMaterialLotId();

                //??????API {onhandQtyUpdateProcess} ?????????????????????
                MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
                mtInvOnhandQuantityVO9.setSiteId(instruction.getSiteId());
                mtInvOnhandQuantityVO9.setEventId(eventId);
                mtInvOnhandQuantityVO9.setLocatorId(receivePendingLocatorId);
                //?????????????????????
                //??????actualId ??????API {attrPropertyQuery} ?????????????????????EXCHANGED_QTY???????????????
                MtExtendVO mtExtendVO = new MtExtendVO();
                mtExtendVO.setKeyId(mtInstructionActualDetailVO.getActualId());
                mtExtendVO.setTableName("mt_instruction_actual_attr");
                mtExtendVO.setAttrName("EXCHANGED_QTY");
                List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && j == 1) {
                    exchangedQty = Double.parseDouble(mtExtendAttrVOS.get(0).getAttrValue());
                    j++;
                }
                log.info("api confirmAcceptance instructionActualProcess exchangedQty before: {}", exchangedQty);

                //??????materialLotId???????????????primaryUomQty
                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(mtInstructionActualDetailVO.getMaterialLotId());
                Double primaryUomQty = mtMaterialLot.getPrimaryUomQty();
                if (exchangedQty >= primaryUomQty) {
                    //??????exchangedQty >= primaryUomQty, ???API?????????0???exchangedQty = exchangedQty - primaryUomQty ??????????????????
                    mtInvOnhandQuantityVO9.setChangeQuantity(0.0D);
                    exchangedQty = exchangedQty - primaryUomQty;
                } else {
                    //?????????API?????????primaryUomQty - exchangedQty ??????exchangedQty = 0 ??????????????????
                    mtInvOnhandQuantityVO9.setChangeQuantity(primaryUomQty - exchangedQty);
                    exchangedQty = 0.0D;
                }
                log.info("api confirmAcceptance instructionActualProcess  exchangedQty after: {}", exchangedQty);
                if ((BigDecimal.valueOf(mtInvOnhandQuantityVO9.getChangeQuantity()).compareTo(BigDecimal.ZERO) != 0)) {
                    //?????????????????????0????????????API
                    mtInvOnhandQuantityVO9.setMaterialId(instruction.getMaterialId());
                    mtInvOnhandQuantityVO9.setLotCode(dto.getLot());
                    mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);
                }
                log.info("api confirmAcceptance instructionActualProcess  mtInvOnhandQuantityVO9: {}", mtInvOnhandQuantityVO9);

                //????????????
                List<MtCommonExtendVO5> extendVO5List = new ArrayList<>();
                MtCommonExtendVO5 mtExtendVO5 = new MtCommonExtendVO5();
                mtExtendVO5.setAttrName(HmeConstants.ExtendAttr.STATUS);
                mtExtendVO5.setAttrValue("TO_ACCEPT");
                extendVO5List.add(mtExtendVO5);
                //????????????????????????
                MtCommonExtendVO5 mtExtendVO51 = new MtCommonExtendVO5();
                mtExtendVO51.setAttrName("RECEIPT_DATE");
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String receiptDate = sdf.format(date);
                mtExtendVO51.setAttrValue(receiptDate);
                extendVO5List.add(mtExtendVO51);

                //????????????
                MtCommonExtendVO5 numAttr = new MtCommonExtendVO5();
                numAttr.setAttrName(HmeConstants.ExtendAttr.DELIVERY_NUM);
                numAttr.setAttrValue(dto.getInstructionDoc().getInstructionDocNum());
                extendVO5List.add(numAttr);

                //????????????ID
                MtCommonExtendVO5 docAttr = new MtCommonExtendVO5();
                docAttr.setAttrName(HmeConstants.ExtendAttr.DELIVERY_DOC_ID);
                docAttr.setAttrValue(dto.getInstructionDoc().getInstructionDocId());
                extendVO5List.add(docAttr);

                //?????????????????????????????????????????? add by yuchao.wang for kang.wang at 2020.8.24
                List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId,
                        new MtExtendVO1("mt_instruction_attr", Collections.singletonList(instruction.getInstructionId()), HmeConstants.ExtendAttr.SO_NUM, HmeConstants.ExtendAttr.SO_LINE_NUM, HmeConstants.ExtendAttr.INSTRUCTION_LINE_NUM, HmeConstants.ExtendAttr.DELIVERY_LINE_NUM));
                for (MtExtendAttrVO1 extendAttr : mtExtendAttrVO1s) {
                    if (HmeConstants.ExtendAttr.SO_NUM.equals(extendAttr.getAttrName())) {
                        MtCommonExtendVO5 mtExtendVO52 = new MtCommonExtendVO5();
                        mtExtendVO52.setAttrName(HmeConstants.ExtendAttr.SO_NUM);
                        mtExtendVO52.setAttrValue(extendAttr.getAttrValue());
                        extendVO5List.add(mtExtendVO52);
                    } else if (HmeConstants.ExtendAttr.SO_LINE_NUM.equals(extendAttr.getAttrName())) {
                        MtCommonExtendVO5 mtExtendVO52 = new MtCommonExtendVO5();
                        mtExtendVO52.setAttrName(HmeConstants.ExtendAttr.SO_LINE_NUM);
                        mtExtendVO52.setAttrValue(extendAttr.getAttrValue());
                        extendVO5List.add(mtExtendVO52);
                    }
                    if (HmeConstants.ExtendAttr.INSTRUCTION_LINE_NUM.equals(extendAttr.getAttrName())) {
                        //???????????????
                        MtCommonExtendVO5 lineNumAttr = new MtCommonExtendVO5();
                        lineNumAttr.setAttrName(HmeConstants.ExtendAttr.DELIVERY_LINE_NUM);
                        lineNumAttr.setAttrValue(extendAttr.getAttrValue());
                        extendVO5List.add(lineNumAttr);
                    }
                }

                MtCommonExtendVO6 extendVO6 = new MtCommonExtendVO6();
                extendVO6.setKeyId(materialLotId);
                extendVO6.setAttrs(extendVO5List);
                attrPropertyList.add(extendVO6);

                //?????????????????? add by yuchao.wang for kang.wang at 2020.8.24
                addObjectTransaction(tenantId, eventId, materialLotId,
                        extendVO5List, instructionDoc, instruction, objectTransactionRequestList);
            }

            //????????????????????????
            mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr", eventId, attrPropertyList);
        }
        return objectTransactionRequestList;
    }
}
