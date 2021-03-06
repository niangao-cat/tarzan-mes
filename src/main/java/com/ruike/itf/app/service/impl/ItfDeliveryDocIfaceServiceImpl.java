package com.ruike.itf.app.service.impl;

import com.ruike.itf.api.dto.ItfDeliveryDocLineReturnDTO;
import com.ruike.itf.api.dto.ItfDeliveryDocReturnDTO;
import com.ruike.itf.app.service.ItfDeliveryDocIfaceService;
import com.ruike.itf.domain.entity.*;
import com.ruike.itf.domain.repository.ItfDeliveryDocIfaceRepository;
import com.ruike.itf.domain.repository.ItfDeliveryDocLineIfaceRepository;
import com.ruike.itf.domain.vo.ItfDeliveryDocAndLineIfaceVO;
import com.ruike.itf.domain.vo.ItfPoDeliveryRelHandlerVO;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.infra.mapper.ItfDeliveryDocIfaceMapper;
import com.ruike.itf.infra.mapper.ItfDeliveryDocLineIfaceMapper;
import com.ruike.itf.infra.mapper.ItfInstructionDocAttrMapper;
import com.ruike.itf.utils.GetDeclaredFields;
import com.ruike.itf.utils.SendESBConnect;
import com.ruike.qms.domain.entity.QmsIqcHeader;
import com.ruike.qms.infra.mapper.QmsIqcHeaderMapper;
import com.ruike.wms.domain.entity.WmsPoDeliveryRel;
import com.ruike.wms.domain.repository.WmsPoDeliveryRelRepository;
import com.ruike.wms.infra.mapper.WmsPoDeliveryRelMapper;
import io.choerodon.core.exception.CommonException;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.util.NumberHelper;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtExtendVO5;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.actual.domain.vo.MtInstructionActualVO;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.api.dto.MtInstructionDocDTO2;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionDocVO3;
import tarzan.instruction.domain.vo.MtInstructionVO;
import tarzan.instruction.domain.vo.MtInstructionVO6;
import tarzan.instruction.infra.mapper.MtInstructionMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.method.domain.entity.MtNcCode;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.repository.MtSupplierRepository;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.ruike.itf.infra.constant.ItfConstant.InstructionType.RECEIVE_FROM_SUPPLIER;
import static com.ruike.itf.infra.constant.ItfConstant.InstructionType.TRANSFER_OVER_LOCATOR;

/**
 * ?????????????????????????????????????????????
 *
 * @author yapeng.yao@hand-china.com 2020-09-04 16:29:21
 */
@Slf4j
@Service
public class ItfDeliveryDocIfaceServiceImpl implements ItfDeliveryDocIfaceService, AopProxy<ItfDeliveryDocIfaceServiceImpl> {

    @Autowired
    private ItfDeliveryDocIfaceRepository itfDeliveryDocIfaceRepository;
    @Autowired
    private ItfDeliveryDocIfaceMapper itfDeliveryDocIfaceMapper;
    @Autowired
    private ItfDeliveryDocLineIfaceRepository itfDeliveryDocLineIfaceRepository;
    @Autowired
    private MtSupplierRepository mtSupplierRepository;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;
    @Autowired
    private MtInstructionRepository mtInstructionRepository;
    @Autowired
    private MtInstructionActualRepository mtInstructionActualRepository;
    @Autowired
    private WmsPoDeliveryRelRepository wmsPoDeliveryRelRepository;
    @Autowired
    private WmsPoDeliveryRelMapper wmsPoDeliveryRelMapper;
    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private ItfInstructionDocAttrMapper itfInstructionDocAttrMapper;
    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private SendESBConnect sendESBConnect;
    @Autowired
    private QmsIqcHeaderMapper qmsIqcHeaderMapper;
    @Value("${hwms.system.tenantId}")
    private Long tenantId;
    @Autowired
    private ItfDeliveryDocLineIfaceMapper itfDeliveryDocLineIfaceMapper;
    @Autowired
    private MtInstructionMapper mtInstructionMapper;

    /**
     * ???????????????
     *
     * @param voList
     * @return
     */
    @Override
    public List<ItfDeliveryDocReturnDTO> invoke(List<ItfDeliveryDocAndLineIfaceVO> voList) throws Exception {
        List<ItfDeliveryDocReturnDTO> returnList = new ArrayList<>();
        if (CollectionUtils.isEmpty(voList)) {
            return returnList;
        }

        for (int i = 0, len = voList.size(); i < len; i++) {
            ItfDeliveryDocAndLineIfaceVO docAndLine = voList.get(i);
            // ??????
            docAndLine = checkInfo(docAndLine);
            voList.set(i, docAndLine);
        }

        // ?????????????????????
        if (CollectionUtils.isNotEmpty(voList)) {
            voList = insertIface(voList);
        }

        // ????????????
        List<ItfDeliveryDocAndLineIfaceVO> ifaceList = voList.stream().filter(item ->
                "N".equals(item.getItfDeliveryDocIface().getProcessStatus())).collect(Collectors.toList());
        List<ItfDeliveryDocAndLineIfaceVO> errIfaceList = voList.stream().filter(item ->
                !"N".equals(item.getItfDeliveryDocIface().getProcessStatus())).collect(Collectors.toList());

        // ????????????????????????
        if (CollectionUtils.isNotEmpty(ifaceList)) {
            for (int i = 0, len = ifaceList.size(); i < len; i++) {
                ItfDeliveryDocAndLineIfaceVO ifaceVO = ifaceList.get(i);
                try {
                    // ???????????????
                    updateaOrInsert(ifaceVO);
                } catch (Exception e) {
                    updateIfaceStatus(ifaceVO, e);
                }
                ifaceList.set(i, ifaceVO);
            }
        }
        // set????????????
        setReturnList(ifaceList, errIfaceList, returnList);
        return returnList;
    }

    /**
     * @param dto
     * @return
     * @author kejin.liu01@hand-china.com 2020/10/20 12:22
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<ItfPoDeliveryRelHandlerVO> sendTHDeliveryDoc(List<ItfPoDeliveryRelHandlerVO> dto) {
        List<ItfPoDeliveryRelHandlerVO> newList = new ArrayList<>();
        GetDeclaredFields<ItfPoDeliveryRelHandlerVO> getDeclaredFields = new GetDeclaredFields();
        String[] fields = {"iqcHeaderId", "instructionDocNum", "srmLineNum"};
        for (ItfPoDeliveryRelHandlerVO vo : dto) {
            List<String> fields1 = getDeclaredFields.getDeclaredFields(vo, fields);
            if (CollectionUtils.isNotEmpty(fields1)) {
                throw new CommonException(fields1.toString() + " ???????????????");
            }
            Map<String, Object> record = new HashMap<>();
            record.put("record", vo);
            Map<String, Object> records = new HashMap<>();
            records.put("records", record);
            String status = null;
            String message = null;
            try {
                Map<String, Object> result = sendESBConnect.sendEsb(records, "VERID", "ItfPoDeliveryRelHandler.execute",
                        ItfConstant.InterfaceCode.SRM_DELIVERY_NOTE_CANCEL);
                status = result.get("status").toString();
                message = result.get("message").toString();
            } catch (Exception e) {
                e.printStackTrace();
                status = "E";
                message = e.getMessage();
            }
            QmsIqcHeader qmsIqcHeader = new QmsIqcHeader();
            qmsIqcHeader.setIqcHeaderId(vo.getIqcHeaderId());
            qmsIqcHeader.setAttribute1(status);
            qmsIqcHeader.setAttribute2(message);
            self().updateQmsIqcHeaderStatus(qmsIqcHeader);
            vo.setStatus(status);
            vo.setMessage(message);
            newList.add(vo);
        }
        return newList;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateQmsIqcHeaderStatus(QmsIqcHeader qmsIqcHeader) {
        qmsIqcHeaderMapper.updateByPrimaryKeySelective(qmsIqcHeader);
    }


    /**
     * set????????????
     *
     * @param ifaceList
     * @param errIfaceList
     * @param returnList
     */
    private void setReturnList(List<ItfDeliveryDocAndLineIfaceVO> ifaceList, List<ItfDeliveryDocAndLineIfaceVO> errIfaceList, List<ItfDeliveryDocReturnDTO> returnList) {
        Date nowDate = new Date();
        List<ItfDeliveryDocAndLineIfaceVO> voList = new ArrayList<>(ifaceList.size() + errIfaceList.size());
        voList.addAll(ifaceList);
        voList.addAll(errIfaceList);
        voList.forEach(vo -> {
            ItfDeliveryDocReturnDTO dto = new ItfDeliveryDocReturnDTO();
            List<ItfDeliveryDocLineReturnDTO> itfDeliveryDocLineReturnDTOList = new ArrayList<ItfDeliveryDocLineReturnDTO>();
            ItfDeliveryDocIface itfDeliveryDocIface = vo.getItfDeliveryDocIface();
            List<ItfDeliveryDocLineIface> itfDeliveryDocLineIfaceList = vo.getItfDeliveryDocLineIfaceList();
            itfDeliveryDocLineIfaceList.stream().forEach(lineIface -> {
                ItfDeliveryDocLineReturnDTO itfDeliveryDocLineReturnDTO = new ItfDeliveryDocLineReturnDTO();
                itfDeliveryDocLineReturnDTO.setInstructionLineNum(lineIface.getInstructionLineNum());
                itfDeliveryDocLineReturnDTO.setProcessStatus(lineIface.getProcessStatus());
                itfDeliveryDocLineReturnDTO.setProcessMessage(lineIface.getProcessMessage());
                itfDeliveryDocLineReturnDTO.setProcessDate(nowDate);
                itfDeliveryDocLineReturnDTOList.add(itfDeliveryDocLineReturnDTO);
            });
            dto.setInstructionDocNum(itfDeliveryDocIface.getInstructionDocNum());
            dto.setLineReturnDTOList(itfDeliveryDocLineReturnDTOList);
            dto.setProcessDate(nowDate);
            dto.setProcessStatus(itfDeliveryDocIface.getProcessStatus());
            dto.setProcessMessage(itfDeliveryDocIface.getProcessMessage());
            returnList.add(dto);
        });
    }

    /**
     * ??????????????????
     *
     * @param mtInstructionVO6
     * @param lineIface
     * @param lineNum
     * @return
     */
    private MtExtendVO10 setMtExtendVO10(MtInstructionVO6 mtInstructionVO6, ItfDeliveryDocLineIface lineIface, int lineNum) {
        MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
        String instructionId = mtInstructionVO6.getInstructionId();
        mtExtendVO10.setKeyId(instructionId);
        // create event
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("DELIVERY_IFACE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        mtExtendVO10.setEventId(eventId);
        List<MtExtendVO5> attrs = setMtExtendVO5(lineIface, lineNum);
        mtExtendVO10.setAttrs(attrs);
        return mtExtendVO10;
    }

    private List<MtExtendVO5> setMtExtendVO5(ItfDeliveryDocLineIface lineIface, int lineNum) {
        List<MtExtendVO5> attrs = new ArrayList<>();
        MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
        if (StringUtils.isNotBlank(lineIface.getSoLineNum())) {
            mtExtendVO5.setAttrName("SO_LINE_NUM");
            mtExtendVO5.setAttrValue(lineIface.getSoLineNum());
            attrs.add(mtExtendVO5);
        }

        if (StringUtils.isNotBlank(lineIface.getSoNum())) {
            mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("SO_NUM");
            mtExtendVO5.setAttrValue(lineIface.getSoNum());
            attrs.add(mtExtendVO5);
        }

        if (StringUtils.isNotBlank(lineIface.getUaiFlag())) {
            mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("UAI_FLAG");
            mtExtendVO5.setAttrValue(lineIface.getUaiFlag());
            attrs.add(mtExtendVO5);
        }

        // start
        // ???????????????IQC????????????IQC_VERSION??????????????????????????????1?????????????????????????????????IQC_VERSION????????????MaterialVersion??????????????????
        // update by ?????????????????? 2020-10-07
        if (StringUtils.isNotBlank(lineIface.getAttribute1())) {
            mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("IQC_VERSION");
            mtExtendVO5.setAttrValue(lineIface.getAttribute1());
            attrs.add(mtExtendVO5);
        }
        // end


        String exchangeQty = lineIface.getExchangeQty();
        boolean positiveNumber = NumberHelper.isPositiveNumber(exchangeQty);
        if (StringUtils.isNotBlank(exchangeQty)) {
            mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("EXCHANGE_QTY");
            mtExtendVO5.setAttrValue(exchangeQty);
            attrs.add(mtExtendVO5);
        }

        mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("EXCHANGE_FLAG");
        if (positiveNumber) {
            mtExtendVO5.setAttrValue("Y");
        } else {
            mtExtendVO5.setAttrValue("N");
        }
        attrs.add(mtExtendVO5);

        mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("MATERIAL_VERSION");
        mtExtendVO5.setAttrValue(lineIface.getMaterialVersion());
        attrs.add(mtExtendVO5);

        mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("INSTRUCTION_LINE_NUM");
        mtExtendVO5.setAttrValue("H" + 10 * lineNum);
        attrs.add(mtExtendVO5);

        // start
        // ?????????????????? 2020-10-01
        //1?????????????????????-???????????????????????????P_UOM?????????????????????????????????2
        //2???????????????????????????-???????????????????????????P_UOM_QTY?????????????????????????????????3
        //3????????????????????????-???????????????????????????UOM_RATE?????????????????????????????????4
        mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("PO_UOM");
        mtExtendVO5.setAttrValue(Strings.isEmpty(lineIface.getAttribute2()) ? null : lineIface.getAttribute2());
        attrs.add(mtExtendVO5);

        mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("PO_UOM_QTY");
        mtExtendVO5.setAttrValue(Strings.isEmpty(lineIface.getAttribute3()) ? null : lineIface.getAttribute3());
        attrs.add(mtExtendVO5);

        mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("UOM_RATE");
        mtExtendVO5.setAttrValue(Strings.isEmpty(lineIface.getAttribute4()) ? null : lineIface.getAttribute4());
        attrs.add(mtExtendVO5);
        //2021-05-27 11:53 add by chaonan.hu for peng.zhao ???????????????????????????????????????
        if (StringUtils.isNotBlank(lineIface.getMaterialVersion())) {
            mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("MATERIAL_VERSION");
            mtExtendVO5.setAttrValue(lineIface.getMaterialVersion());
            attrs.add(mtExtendVO5);
        }
        // end
        return attrs;
    }

    /**
     * ????????????
     *
     * @param lineIface
     * @param mtInstructionDocVO3
     * @param siteId
     * @param supplierId
     * @param instructionType
     * @param sumQuantity
     * @param instructionDocNum
     * @return
     */
    private MtInstructionVO setMtInstructionVO(ItfDeliveryDocLineIface lineIface, MtInstructionDocVO3 mtInstructionDocVO3, String siteId, String supplierId, String instructionType, BigDecimal sumQuantity, String instructionDocNum) {
        MtInstructionVO mtInstructionVO = new MtInstructionVO();
        BeanUtils.copyProperties(lineIface, mtInstructionVO);
        mtInstructionVO.setSourceDocId(mtInstructionDocVO3.getInstructionDocId());
        if (RECEIVE_FROM_SUPPLIER.equals(instructionType)) {
            mtInstructionVO.setInstructionType(instructionType);
            mtInstructionVO.setToSiteId(siteId);
            mtInstructionVO.setBusinessType("PO_RECEIVING");
        } else if (TRANSFER_OVER_LOCATOR.equals(instructionType)) {
            mtInstructionVO.setInstructionType(instructionType);
            mtInstructionVO.setFromSiteId(siteId);
            mtInstructionVO.setFromLocatorId(lineIface.getToLocatorId());
            mtInstructionVO.setBusinessType("PO_INSTOCK");
        }
        mtInstructionVO.setInstructionStatus("RELEASED");
        mtInstructionVO.setSiteId(siteId);
        mtInstructionVO.setToLocatorId(lineIface.getToLocatorId());
        if (Objects.nonNull(lineIface.getQuantity())) {
            mtInstructionVO.setQuantity(sumQuantity.doubleValue());
        }
        mtInstructionVO.setSupplierId(supplierId);

//        List<MtInstruction> mtInstructions = mtInstructionRepository.selectByCondition(Condition.builder(MtInstruction.class)
//                .andWhere(Sqls.custom().andEqualTo(MtInstruction.FIELD_TENANT_ID, tenantId)
//                        .andEqualTo(MtInstruction.FIELD_SOURCE_DOC_ID, mtInstructionDocVO3.getInstructionDocId())
//                        .andEqualTo(MtInstruction.FIELD_MATERIAL_ID, lineIface.getMaterialId())
//                        .andEqualTo(MtInstruction.FIELD_SITE_ID, siteId)
//                        .andEqualTo(MtInstruction.FIELD_INSTRUCTION_TYPE, instructionType)).build());
        List<MtInstruction> mtInstructions = mtInstructionMapper.selectByMaterial(tenantId,mtInstructionDocVO3.getInstructionDocId(),
                lineIface.getMaterialId(),siteId,instructionType,lineIface.getMaterialVersion());
        if (CollectionUtils.isNotEmpty(mtInstructions)) {
            // ??????
            MtInstruction mtInstruction = mtInstructions.get(0);
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("DELIVERY_IFACE");
            String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
            mtInstructionVO.setEventId(eventId);
            mtInstructionVO.setInstructionId(mtInstruction.getInstructionId());
            mtInstructionVO.setIdentification(mtInstruction.getIdentification());
            mtInstructionVO.setInstructionType("");
        }
        return mtInstructionVO;
    }

    /**
     * ????????????
     *
     * @param itfDeliveryDocIface
     * @return
     */
    private MtInstructionDocDTO2 setMtInstructionDocDTO2(ItfDeliveryDocIface itfDeliveryDocIface) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String demandTime = itfDeliveryDocIface.getDemandTime();
        String expectedArrivalTime = itfDeliveryDocIface.getExpectedArrivalTime();
        MtInstructionDocDTO2 mtInstructionDocDTO2 = new MtInstructionDocDTO2();
        BeanUtils.copyProperties(itfDeliveryDocIface, mtInstructionDocDTO2);
        String instructionDocNum = mtInstructionDocDTO2.getInstructionDocNum();
        MtInstructionDoc mtInstructionDoc = new MtInstructionDoc();
        if (Strings.isNotEmpty(demandTime)) {
            try {
                Date parseDemandTime = format.parse(demandTime);
                mtInstructionDoc.setDemandTime(parseDemandTime);
            } catch (ParseException e) {
                throw new CommonException(e.getMessage());
            }
        }
        if (Strings.isNotEmpty(expectedArrivalTime)) {
            try {
                Date parseExpectedArrivalTime = format.parse(expectedArrivalTime);
                mtInstructionDoc.setExpectedArrivalTime(parseExpectedArrivalTime);
            } catch (ParseException e) {
                throw new CommonException(e.getMessage());
            }
        }

        mtInstructionDoc.setTenantId(tenantId);
        mtInstructionDoc.setInstructionDocNum(instructionDocNum);
        MtInstructionDoc mtInstructionDocOne = mtInstructionDocRepository.selectOne(mtInstructionDoc);
        if (Objects.nonNull(mtInstructionDocOne)) {
            mtInstructionDocDTO2.setInstructionDocId(mtInstructionDocOne.getInstructionDocId());
            // create event
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("DELIVERY_IFACE");
            String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
            mtInstructionDocDTO2.setEventId(eventId);
        }
        mtInstructionDocDTO2.setDemandTime(mtInstructionDoc.getDemandTime());
        mtInstructionDocDTO2.setExpectedArrivalTime(mtInstructionDoc.getExpectedArrivalTime());
        return mtInstructionDocDTO2;
    }

    /**
     * ??????????????????
     *
     * @param docAndLine
     * @return
     */
    private ItfDeliveryDocAndLineIfaceVO checkInfo(ItfDeliveryDocAndLineIfaceVO docAndLine) {
        Date nowDate = new Date();
        // ????????????
        ItfDeliveryDocIface itfDeliveryDocIface = docAndLine.getItfDeliveryDocIface();
        if (Objects.nonNull(itfDeliveryDocIface)) {
            itfDeliveryDocIface.setProcessDate(nowDate);
            itfDeliveryDocIface.setProcessStatus("N");

            // ??????????????????
            itfDeliveryDocIface = checkHeadInfo(itfDeliveryDocIface);

            String interfaceHeaderId = this.mtCustomDbRepository.getNextKey("itf_delivery_doc_iface_s");
            itfDeliveryDocIface.setInterfaceHeaderId(interfaceHeaderId);
            itfDeliveryDocIface.setTenantId(tenantId);
            itfDeliveryDocIface.setObjectVersionNumber(1L);
            itfDeliveryDocIface.setCreatedBy(-1L);
            itfDeliveryDocIface.setCreationDate(new Date());
            itfDeliveryDocIface.setLastUpdatedBy(-1L);
            itfDeliveryDocIface.setLastUpdateDate(new Date());

            // ????????????
            List<ItfDeliveryDocLineIface> itfDeliveryDocLineIfaceList = docAndLine.getItfDeliveryDocLineIfaceList();
            List<ItfDeliveryDocLineIface> lineIfaceList = new ArrayList<ItfDeliveryDocLineIface>();
            AtomicBoolean lineErrFlag = new AtomicBoolean(false);
            if (CollectionUtils.isNotEmpty(itfDeliveryDocLineIfaceList)) {
                for (int i = 0, len = itfDeliveryDocLineIfaceList.size(); i < len; i++) {
                    ItfDeliveryDocLineIface docLineIface = itfDeliveryDocLineIfaceList.get(i);

                    // start
                    // ???????????????IQC????????????IQC_VERSION??????????????????????????????1?????????????????????????????????IQC_VERSION????????????MaterialVersion??????????????????
                    // update by ?????????????????? 2020-10-07
                    docLineIface.setAttribute1(docLineIface.getMaterialVersion());
                    docLineIface.setMaterialVersion("");
                    // end

                    docLineIface.setProcessDate(nowDate);
                    docLineIface.setProcessStatus("N");
                    // ??????????????????
                    docLineIface = checkLineInfo(docLineIface);
                    if ("E".equalsIgnoreCase(docLineIface.getProcessStatus())) {
                        lineErrFlag.set(true);
                    }
                    String interfaceLineId = this.mtCustomDbRepository.getNextKey("itf_delivery_doc_line_iface_s");
                    docLineIface.setInterfaceLineId(interfaceLineId);
                    docLineIface.setInterfaceHeaderId(interfaceHeaderId);
                    docLineIface.setTenantId(tenantId);
                    docLineIface.setObjectVersionNumber(1L);
                    docLineIface.setCreatedBy(-1L);
                    docLineIface.setCreationDate(new Date());
                    docLineIface.setLastUpdatedBy(-1L);
                    docLineIface.setLastUpdateDate(new Date());
                    itfDeliveryDocLineIfaceList.set(i, docLineIface);
                }
                //??????????????????????????????????????????attribute1??????????????????
                Map<String, List<ItfDeliveryDocLineIface>> map1 = itfDeliveryDocLineIfaceList.stream().collect(Collectors.groupingBy(lineIface -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append(lineIface.getMaterialId());
                    return sb.toString();
                }));
                Boolean flag = false;
                for(Map.Entry<String, List<ItfDeliveryDocLineIface>> map : map1.entrySet()){
                    if(map.getValue().size()>1){
                        List<ItfDeliveryDocLineIface> itfDeliveryDocLineIfaceList1 = map.getValue().stream().filter(item ->item.getMaterialVersion().equals("")).collect(Collectors.toList());
                        if(itfDeliveryDocLineIfaceList1.size() > 1){
                            List<String> iqcMaterialVersionList =itfDeliveryDocLineIfaceList1.stream().map(ItfDeliveryDocLineIface::getAttribute1).distinct().collect(Collectors.toList());
                            if(iqcMaterialVersionList.size()!=1){
                                flag = true;
                            }
                        }
                    }
                }
                if(flag){
                    for(ItfDeliveryDocLineIface itfDeliveryDocLineIface:itfDeliveryDocLineIfaceList){
                        itfDeliveryDocLineIface.setProcessStatus("E");
                        itfDeliveryDocLineIface.setProcessMessage("?????????????????????????????????IQC??????????????????????????????");
                    }
                    lineErrFlag.set(true);
                }
                // ?????????-->??????????????????
                if (!lineErrFlag.get()) {
                    String instructionDocNum = itfDeliveryDocIface.getInstructionDocNum();
                    // ???????????????????????????????????????:????????????????????????????????????????????????????????????
                    Map<String, List<ItfDeliveryDocLineIface>> lineIfaceListMap = groupByCondition(itfDeliveryDocLineIfaceList, instructionDocNum);
                    // ?????????????????????SO_NUM?????????????????????SO_LINE_NUM???????????????MATERIAL_VERSION??????????????????UAI_FLAG???????????????????????????
                    String errMessage = checkLineIfaceListMap(lineIfaceListMap, instructionDocNum);
                    if (StringUtils.isBlank(errMessage)) {
                        // ????????????,???Map??????value??????????????????????????????????????????????????????list
                        int count = 0;
                        for (Map.Entry<String, List<ItfDeliveryDocLineIface>> map : lineIfaceListMap.entrySet()) {
                            List<ItfDeliveryDocLineIface> value = map.getValue();
                            lineIfaceList.addAll(value);
                        }
                    } else {
                        lineErrFlag.set(true);
                        itfDeliveryDocIface.setProcessMessage(errMessage);
                        lineIfaceList = itfDeliveryDocLineIfaceList;
                    }
                } else {
                    lineIfaceList = itfDeliveryDocLineIfaceList;
                }
            }
            // ??????????????????E?????????????????????E
            if (lineErrFlag.get()) {
                itfDeliveryDocIface.setProcessStatus("E");
            }
            docAndLine.setItfDeliveryDocIface(itfDeliveryDocIface);
            docAndLine.setItfDeliveryDocLineIfaceList(lineIfaceList);
        }
        return docAndLine;
    }

    /**
     * ?????????????????????SO_NUM?????????????????????SO_LINE_NUM???????????????MATERIAL_VERSION??????????????????UAI_FLAG???????????????????????????
     *
     * @param lineIfaceListMap
     * @param instructionDocNum
     * @return
     */
    private String checkLineIfaceListMap(Map<String, List<ItfDeliveryDocLineIface>> lineIfaceListMap, String instructionDocNum) {
        StringBuilder errSb = new StringBuilder();
        for (Map.Entry<String, List<ItfDeliveryDocLineIface>> map : lineIfaceListMap.entrySet()) {
            List<ItfDeliveryDocLineIface> valueList = map.getValue();
            Set<String> hashSet = new HashSet<>();
            valueList.stream().forEach(value -> {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(value.getSoNum()).append("_");
                stringBuilder.append(value.getSoLineNum()).append("_");
                // start
                // ???????????????IQC????????????IQC_VERSION??????????????????????????????1?????????????????????????????????IQC_VERSION??????
                // ??????????????????
                // update by ?????????????????? 2020-10-07
                // stringBuilder.append(value.getMaterialVersion()).append("_");
                stringBuilder.append(value.getAttribute1()).append("_");
                // end

                // start
                // 4????????????????????????????????? ?????????????????????????????? ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                // update by ?????????????????? 2020-10-08
                stringBuilder.append(value.getAttribute2()).append("_");
                stringBuilder.append(value.getAttribute4()).append("_");
                // end
                stringBuilder.append(value.getUaiFlag());
                hashSet.add(stringBuilder.toString());
            });
            if (hashSet.size() != 1) {
                // ??????????????????
                errSb.append("????????????").append(instructionDocNum).append("???");
                for (String value : hashSet) {
                    String[] strs = value.split("_");
                    errSb.append("???????????????:").append(strs[0]).append("?????????!");
                    errSb.append("??????????????????:").append(strs[1]).append("?????????!");
                    errSb.append("IQC??????:").append(strs[2]).append("?????????!");
                    errSb.append("????????????:").append(strs[3]).append("?????????!");
                    errSb.append("???????????????:").append(strs[4]).append("?????????!");
                    errSb.append("???????????????:").append(strs[5]).append("?????????!");
                }
            }
        }
        return errSb.toString();
    }

    /**
     * ???????????????????????????????????????:?????????????????????????????????????????????????????????;
     *
     * @param lineIfaceList
     * @param instructionDocNum
     * @return
     */
    private Map<String, List<ItfDeliveryDocLineIface>> groupByCondition(List<ItfDeliveryDocLineIface> lineIfaceList, String instructionDocNum) {
        Map<String, List<ItfDeliveryDocLineIface>> collect = lineIfaceList.stream().collect(Collectors.groupingBy(lineIface -> {
            StringBuilder sb = new StringBuilder();
            sb.append(instructionDocNum).append("-");
            sb.append(lineIface.getMaterialId()).append("_");
            sb.append(lineIface.getUomId()).append("_");
            sb.append(lineIface.getToLocatorId()).append("_");
            //
            sb.append(lineIface.getAttribute1()).append("_");
            sb.append(lineIface.getAttribute2()).append("_");
            sb.append(lineIface.getAttribute4());

            return sb.toString();
        }));
        return collect;
    }

    /**
     * ??????????????????
     *
     * @param docLineIface
     * @return
     */
    private ItfDeliveryDocLineIface checkLineInfo(ItfDeliveryDocLineIface docLineIface) {
        String materialCode = docLineIface.getMaterialCode();
        String uomCode = docLineIface.getUomCode();
        String toLocatorCode = docLineIface.getToLocatorCode();
        BigDecimal quantity = docLineIface.getQuantity();
        String poNum = docLineIface.getPoNum();
        String poLineNum = docLineIface.getPoLineNum();

        String status = "N";
        StringBuilder errMessage = new StringBuilder();
        // ??????
        if (StringUtils.isBlank(materialCode)) {
            status = "E";
            errMessage.append("??????????????????!").append(";");
        } else {
            MtMaterial mtMaterial = queryMtMaterial(tenantId, materialCode);
            if (Objects.isNull(mtMaterial)) {
                status = "E";
                errMessage.append("???????????????!").append(";");
            } else {
                docLineIface.setMaterialId(mtMaterial.getMaterialId());
            }
        }
        // ??????
        if (StringUtils.isBlank(uomCode)) {
            status = "E";
            errMessage.append("????????????!").append(";");
        } else {
            MtUom mtUom = queryMtUom(tenantId, uomCode);
            if (Objects.isNull(mtUom)) {
                status = "E";
                errMessage.append("???????????????!").append(";");
            } else {
                docLineIface.setUomId(mtUom.getUomId());
            }
        }
        // ????????????
        if (StringUtils.isBlank(toLocatorCode)) {
            status = "E";
            errMessage.append("??????????????????!").append(";");
        } else {
            MtModLocator mtModLocator = queryMtModLocator(tenantId, toLocatorCode);
            if (Objects.isNull(mtModLocator)) {
                status = "E";
                errMessage.append("?????????????????????!").append(";");
            } else {
                docLineIface.setToLocatorId(mtModLocator.getLocatorId());
            }
        }
        // ??????
        if (quantity == null) {
            status = "E";
            errMessage.append("??????????????????!").append(";");
        }
        // ???????????????
        if (StringUtils.isBlank(poNum)) {
            status = "E";
            errMessage.append("?????????????????????!").append(";");
        } else {
            MtInstructionDoc mtInstructionDoc = queryMtInstructionDoc(tenantId, poNum);
            if (Objects.isNull(mtInstructionDoc)) {
                status = "E";
                errMessage.append("????????????????????????!").append(";");
            } else {
                docLineIface.setPoId(mtInstructionDoc.getInstructionDocId());
            }
        }
        // ???????????????
        if (StringUtils.isBlank(poLineNum)) {
            status = "E";
            errMessage.append("????????????????????????!").append(";");
        } else {
            String instructionId = itfDeliveryDocIfaceMapper.queryInstructionIdByPoLineNum(tenantId, docLineIface.getPoId(), poLineNum,docLineIface.getMaterialId());
            if (StringUtils.isBlank(instructionId)) {
                status = "E";
                errMessage.append("???????????????????????????!").append(";");
            } else {
                docLineIface.setPoLineId(instructionId);
            }
        }
        docLineIface.setProcessStatus(status);
        docLineIface.setProcessMessage(errMessage.toString());
        return docLineIface;
    }

    /**
     * ??????????????????
     *
     * @param itfDeliveryDocIface
     * @return
     */
    private ItfDeliveryDocIface checkHeadInfo(ItfDeliveryDocIface itfDeliveryDocIface) {
        String instructionDocNum = itfDeliveryDocIface.getInstructionDocNum();
        String siteCode = itfDeliveryDocIface.getSiteCode();
        String supplierCode = itfDeliveryDocIface.getSupplierCode();
        String instructionDocType = itfDeliveryDocIface.getInstructionDocType();
        String status = "N";
        StringBuilder errMessage = new StringBuilder();
        // ?????????
        if (StringUtils.isBlank(instructionDocNum)) {
            status = "E";
            errMessage.append("?????????????????????!").append(";");
        } else {
            // modify ??????????????????
            // ????????????????????????????????????????????????????????????????????????NEW/??????RELEASED?????????????????????????????? start
            MtInstructionDoc mtInstructionDoc = queryMtInstructionDoc(tenantId, itfDeliveryDocIface.getInstructionDocNum());
            if (!Objects.isNull(mtInstructionDoc)) {
                String instructionDocStatus = mtInstructionDoc.getInstructionDocStatus();
                if (!"NEW".equals(instructionDocStatus) && !"RELEASED".equals(instructionDocStatus)) {
                    status = "E";
                    errMessage.append("?????????????????????????????????????????????????????????!").append(";");
                }
            }
            // end
        }
        // ??????
        if (StringUtils.isBlank(siteCode)) {
            status = "E";
            errMessage.append("????????????!").append(";");
        } else {
            MtModSite mtModSite = queryMtModSite(tenantId, siteCode);
            if (Objects.isNull(mtModSite)) {
                status = "E";
                errMessage.append("???????????????!").append(";");
            } else {
                itfDeliveryDocIface.setSiteId(mtModSite.getSiteId());
            }
        }
        // ?????????
        if (StringUtils.isBlank(supplierCode)) {
            status = "E";
            errMessage.append("???????????????!").append(";");
        } else {
            MtSupplier mtSupplier = queryMtSupplier(tenantId, supplierCode);
            if (Objects.isNull(mtSupplier)) {
                status = "E";
                errMessage.append("??????????????????!").append(";");
            } else {
                itfDeliveryDocIface.setSupplierId(mtSupplier.getSupplierId());
            }
        }
        if (StringUtils.isNotBlank(instructionDocType)) {
            if ("0".equals(instructionDocType)) {
                itfDeliveryDocIface.setInstructionDocType("DELIVERY_DOC");
            } else if ("3".equals(instructionDocType)) {
                itfDeliveryDocIface.setInstructionDocType("OUTSOURCING_DELIVERY_DOC");
            }
        }

        itfDeliveryDocIface.setProcessStatus(status);
        itfDeliveryDocIface.setProcessMessage(errMessage.toString());

        return itfDeliveryDocIface;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateaOrInsert(ItfDeliveryDocAndLineIfaceVO ifaceVO) {
        ItfDeliveryDocIface itfDeliveryDocIface = ifaceVO.getItfDeliveryDocIface();
        if (Objects.nonNull(itfDeliveryDocIface)) {
            // ??????
            MtInstructionDocDTO2 mtInstructionDocDTO2 = setMtInstructionDocDTO2(itfDeliveryDocIface);
            MtInstructionDocVO3 mtInstructionDocVO3 = mtInstructionDocRepository.instructionDocUpdate(tenantId, mtInstructionDocDTO2, "N");
            // ??????????????????
            String instructionDocId = mtInstructionDocVO3.getInstructionDocId();
            ItfInstructionDocAttr instructionDocAttr = new ItfInstructionDocAttr();
            instructionDocAttr.setInstructionDocId(instructionDocId);
            instructionDocAttr.setTenantId(tenantId);
            instructionDocAttr.setAttrName("SRM_FLAG");
            List<ItfInstructionDocAttr> itfInstructionDocAttrs = itfInstructionDocAttrMapper.select(instructionDocAttr);
            if (CollectionUtils.isEmpty(itfInstructionDocAttrs)) {
                instructionDocAttr.setAttrId(this.customDbRepository.getNextKey("mt_instruction_doc_attr_s"));
                instructionDocAttr.setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_instruction_doc_attr_cid_s")));
                instructionDocAttr.setAttrValue("SRM");
                itfInstructionDocAttrMapper.insertSelective(instructionDocAttr);
            }


            String siteId = itfDeliveryDocIface.getSiteId();
            String supplierId = itfDeliveryDocIface.getSupplierId();
            // ??????
            List<ItfDeliveryDocLineIface> itfDeliveryDocLineIfaceList = ifaceVO.getItfDeliveryDocLineIfaceList();
            if (CollectionUtils.isNotEmpty(itfDeliveryDocLineIfaceList)) {
                List<WmsPoDeliveryRel> wmsPoDeliveryRelList = new ArrayList<WmsPoDeliveryRel>(itfDeliveryDocLineIfaceList.size());
                String[] instructionTypes = {"RECEIVE_FROM_SUPPLIER", "TRANSFER_OVER_LOCATOR"};
                String instructionDocNum = itfDeliveryDocIface.getInstructionDocNum();
                // ???????????????????????????????????????:????????????????????????????????????????????????????????????
                Map<String, List<ItfDeliveryDocLineIface>> lineIfaceListMap = groupByCondition(itfDeliveryDocLineIfaceList, instructionDocNum);
                // ????????????,???Map??????value??????????????????????????????????????????????????????list
                int lineNum = 1;
                for (Map.Entry<String, List<ItfDeliveryDocLineIface>> map : lineIfaceListMap.entrySet()) {
                    List<ItfDeliveryDocLineIface> value = map.getValue();
                    ItfDeliveryDocLineIface lineIface = value.get(0);
                    // ????????????
                    BigDecimal sumQuantity = getMergeQuantity(value);
                    BigDecimal attribute3 = getMergeAttribute3(value);
                    String instructionId = "";
                    // ????????????
                    for (int i = 0; i < instructionTypes.length; i++) {
                        MtInstructionVO mtInstructionVO = setMtInstructionVO(lineIface, mtInstructionDocVO3, siteId, supplierId, instructionTypes[i], sumQuantity, itfDeliveryDocIface.getInstructionDocNum());
                        MtInstructionVO6 mtInstructionVO6 = mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, "N");
                        mtInstructionVO.setInstructionId(mtInstructionVO6.getInstructionId());
                        if ("RECEIVE_FROM_SUPPLIER".equals(instructionTypes[i])) {
                            instructionId = mtInstructionVO6.getInstructionId();
                        }

                        // ???????????????
                        lineIface.setAttribute3(String.valueOf(attribute3));
                        MtExtendVO10 mtExtendVO10 = setMtExtendVO10(mtInstructionVO6, lineIface, lineNum);
                        mtInstructionRepository.instructionAttrPropertyUpdate(tenantId, mtExtendVO10);
                        // ???????????????????????????
                        MtInstructionActualVO mtInstructionActualVO = setMtInstructionActualVO(mtInstructionVO);
                        mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionActualVO);
                    }
                    lineNum = lineNum + 1;
                    // ???????????????
                    for (int i = 0; i < value.size(); i++) {
                        ItfDeliveryDocLineIface itfDeliveryDocLineIface = value.get(i);
                        wmsPoDeliveryRelList.add(setWmsPoDeliveryRel(mtInstructionDocVO3, itfDeliveryDocLineIface, instructionId));
                    }
                }
                // ??????wms_po_delivery_rel
                updateaOrInsertWmsPoDeliveryRel(wmsPoDeliveryRelList);
            }
            // ???????????????????????????-->S
            overToChangeStatus(ifaceVO);
        }
    }

    private BigDecimal getMergeAttribute3(List<ItfDeliveryDocLineIface> value) {
        BigDecimal sumQuantity = BigDecimal.ZERO;
        for (int i = 0; i < value.size(); i++) {
            ItfDeliveryDocLineIface itfDeliveryDocLineIface = value.get(i);
            if (StringUtils.isNotBlank(itfDeliveryDocLineIface.getAttribute3())) {
                sumQuantity = sumQuantity.add(BigDecimal.valueOf(Double.valueOf(itfDeliveryDocLineIface.getAttribute3())));
            }
        }

        return sumQuantity;
    }

    private void updateaOrInsertWmsPoDeliveryRel(List<WmsPoDeliveryRel> wmsPoDeliveryRelList) {
        List<WmsPoDeliveryRel> insertList = new ArrayList<>(wmsPoDeliveryRelList.size());
        for (WmsPoDeliveryRel wmsPoDeliveryRel : wmsPoDeliveryRelList) {
            // ??????????????????
            WmsPoDeliveryRel wmsPoDeliveryRel1 = new WmsPoDeliveryRel();
            wmsPoDeliveryRel1.setDeliveryDocId(wmsPoDeliveryRel.getDeliveryDocId());
            wmsPoDeliveryRel1.setDeliveryDocLineId(wmsPoDeliveryRel.getDeliveryDocLineId());
            wmsPoDeliveryRel1.setPoId(wmsPoDeliveryRel.getPoId());
            wmsPoDeliveryRel1.setPoLineId(wmsPoDeliveryRel.getPoLineId());
            wmsPoDeliveryRel1.setTenantId(tenantId);
            List<WmsPoDeliveryRel> wmsPoDeliveryRels = wmsPoDeliveryRelRepository.select(wmsPoDeliveryRel1);
            if (CollectionUtils.isEmpty(wmsPoDeliveryRels)) {
                // ??????
                insertList.add(wmsPoDeliveryRel);
            } else {
                // ??????
                WmsPoDeliveryRel wmsPoDeliveryRelOne = wmsPoDeliveryRels.get(0);
                wmsPoDeliveryRelOne.setQuantity(wmsPoDeliveryRel.getQuantity());
                wmsPoDeliveryRelMapper.updateByPrimaryKeySelective(wmsPoDeliveryRelOne);
            }
        }
        if (CollectionUtils.isNotEmpty(insertList)) {
            wmsPoDeliveryRelRepository.batchInsertSelective(insertList);
        }

    }

    /**
     * ???????????????????????????-->S
     *
     * @param ifaceVO
     */
    private void overToChangeStatus(ItfDeliveryDocAndLineIfaceVO ifaceVO) {
        ItfDeliveryDocIface itfDeliveryDocIface = ifaceVO.getItfDeliveryDocIface();
        List<ItfDeliveryDocLineIface> itfDeliveryDocLineIfaceList = ifaceVO.getItfDeliveryDocLineIfaceList();
        // ????????????
        itfDeliveryDocIface.setProcessStatus("S");
        itfDeliveryDocIfaceRepository.updateByPrimaryKeySelective(itfDeliveryDocIface);
        // ????????????
        if (CollectionUtils.isNotEmpty(itfDeliveryDocLineIfaceList)) {
            itfDeliveryDocLineIfaceList.stream().forEach(lineIface -> {
                lineIface.setProcessStatus("S");
            });
            itfDeliveryDocLineIfaceRepository.batchUpdateByPrimaryKeySelective(itfDeliveryDocLineIfaceList);
        }
    }

    /**
     * ??????????????????
     *
     * @param value
     * @return
     */
    private BigDecimal getMergeQuantity(List<ItfDeliveryDocLineIface> value) {
        BigDecimal sumQuantity = BigDecimal.ZERO;
        for (int i = 0; i < value.size(); i++) {
            ItfDeliveryDocLineIface itfDeliveryDocLineIface = value.get(i);
            if (Objects.nonNull(itfDeliveryDocLineIface.getQuantity())) {
                sumQuantity = sumQuantity.add(itfDeliveryDocLineIface.getQuantity());
            }
        }
        return sumQuantity;
    }

    private MtInstructionActualVO setMtInstructionActualVO(MtInstructionVO mtInstructionVO) {
        MtInstructionActualVO mtInstructionActualVO = new MtInstructionActualVO();
        BeanUtils.copyProperties(mtInstructionVO, mtInstructionActualVO);
        mtInstructionActualVO.setActualQty(0.0);
        // create event
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("DELIVERY_IFACE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        mtInstructionActualVO.setEventId(eventId);
        return mtInstructionActualVO;
    }

    /**
     * wms_po_delivery_rel??????
     *
     * @param mtInstructionDocVO3
     * @param lineIface
     * @param instructionId
     * @return
     */
    private WmsPoDeliveryRel setWmsPoDeliveryRel(MtInstructionDocVO3 mtInstructionDocVO3, ItfDeliveryDocLineIface lineIface, String instructionId) {
        WmsPoDeliveryRel wmsPoDeliveryRel = new WmsPoDeliveryRel();
        wmsPoDeliveryRel.setTenantId(tenantId);
        wmsPoDeliveryRel.setPoDeliveryRelId(mtInstructionDocVO3.getInstructionDocId());
        wmsPoDeliveryRel.setDeliveryDocId(mtInstructionDocVO3.getInstructionDocId());
        wmsPoDeliveryRel.setDeliveryDocLineId(instructionId);
        wmsPoDeliveryRel.setSrmLineNum(lineIface.getInstructionLineNum());
        wmsPoDeliveryRel.setPoId(lineIface.getPoId());
        wmsPoDeliveryRel.setPoLineId(lineIface.getPoLineId());
        if (Objects.nonNull(lineIface.getQuantity())) {
            wmsPoDeliveryRel.setQuantity(lineIface.getQuantity());
        }
        wmsPoDeliveryRel.setPoStockInQty(BigDecimal.ZERO);
        wmsPoDeliveryRel.setReceivedQty(BigDecimal.ZERO);
        return wmsPoDeliveryRel;
    }

    /**
     * set?????????????????????
     *
     * @param mtExtendVO5List
     * @param itfMaterialLotIface
     */
    private void setMtExtendVO5List(List<MtExtendVO5> mtExtendVO5List, ItfMaterialLotIface
            itfMaterialLotIface) {
        // ????????????
        MtExtendVO5 mtExtendAttrVO = new MtExtendVO5();
        mtExtendAttrVO.setAttrName("OUTER_BOX");
        mtExtendAttrVO.setAttrValue(itfMaterialLotIface.getOuterBox());
        mtExtendVO5List.add(mtExtendAttrVO);

        // ?????????????????????
        mtExtendAttrVO = new MtExtendVO5();
        mtExtendAttrVO.setAttrName("SUPPLIER_LOT");
        mtExtendAttrVO.setAttrValue(itfMaterialLotIface.getSupplierLot());
        mtExtendVO5List.add(mtExtendAttrVO);

        // ????????????
        mtExtendAttrVO = new MtExtendVO5();
        mtExtendAttrVO.setAttrName("MATERIAL_VERSION");
        mtExtendAttrVO.setAttrValue(itfMaterialLotIface.getMaterialVersion());
        mtExtendVO5List.add(mtExtendAttrVO);

        // ????????????
        mtExtendAttrVO = new MtExtendVO5();
        mtExtendAttrVO.setAttrName("PRODUCT_DATE");
        mtExtendAttrVO.setAttrValue(itfMaterialLotIface.getProductDate());
        mtExtendVO5List.add(mtExtendAttrVO);
    }

    /**
     * ??????Mt_Instruction
     *
     * @param tenantId
     * @param poNum
     * @return
     */
    private MtInstructionDoc queryMtInstructionDoc(Long tenantId, String poNum) {
        MtInstructionDoc mtInstructionDoc = new MtInstructionDoc();
        mtInstructionDoc.setTenantId(tenantId);
        mtInstructionDoc.setInstructionDocNum(poNum);
        return mtInstructionDocRepository.selectOne(mtInstructionDoc);
    }

    /**
     * ?????????????????????
     *
     * @param tenantId
     * @param supplierCode
     * @return
     */
    private MtSupplier queryMtSupplier(Long tenantId, String supplierCode) {
        MtSupplier mtSupplier = new MtSupplier();
        mtSupplier.setTenantId(tenantId);
        mtSupplier.setSupplierCode(supplierCode);
        return mtSupplierRepository.selectOne(mtSupplier);
    }

    /**
     * ??????????????????
     *
     * @param tenantId
     * @param siteCode
     * @return
     */
    private MtModSite queryMtModSite(Long tenantId, String siteCode) {
        MtModSite mtModSite = new MtModSite();
        mtModSite.setTenantId(tenantId);
        mtModSite.setSiteCode(siteCode);
        return mtModSiteRepository.selectOne(mtModSite);
    }

    /**
     * ??????????????????
     *
     * @param tenantId
     * @param materialCode
     * @return
     */
    private MtMaterial queryMtMaterial(Long tenantId, String materialCode) {
        MtMaterial mtMaterial = new MtMaterial();
        mtMaterial.setTenantId(tenantId);
        mtMaterial.setMaterialCode(materialCode);
        return mtMaterialRepository.selectOne(mtMaterial);
    }

    /**
     * ??????????????????
     *
     * @param tenantId
     * @param primaryUomCode
     * @return
     */
    private MtUom queryMtUom(Long tenantId, String primaryUomCode) {
        MtUom mtUom = new MtUom();
        mtUom.setTenantId(tenantId);
        mtUom.setUomCode(primaryUomCode);
        return mtUomRepository.selectOne(mtUom);
    }

    /**
     * ??????????????????
     *
     * @param tenantId
     * @param toLocatorCode
     * @return
     */
    private MtModLocator queryMtModLocator(Long tenantId, String toLocatorCode) {
        MtModLocator mtModLocator = new MtModLocator();
        mtModLocator.setTenantId(tenantId);
        mtModLocator.setLocatorCode(toLocatorCode);
        return mtModLocatorRepository.selectOne(mtModLocator);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    List<ItfDeliveryDocAndLineIfaceVO> insertIface(List<ItfDeliveryDocAndLineIfaceVO> voList){
        voList.stream().forEach(iface -> {
            ItfDeliveryDocIface itfDeliveryDocIface = iface.getItfDeliveryDocIface();
            List<ItfDeliveryDocLineIface> itfDeliveryDocLineIfaceList = iface.getItfDeliveryDocLineIfaceList();
            // ????????????
            itfDeliveryDocIfaceRepository.insertSelective(itfDeliveryDocIface);
            // ????????????
            if (CollectionUtils.isNotEmpty(itfDeliveryDocLineIfaceList)) {
                itfDeliveryDocLineIfaceRepository.batchInsertSelective(itfDeliveryDocLineIfaceList);
            }
        });
        return  voList;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void updateIfaceStatus(ItfDeliveryDocAndLineIfaceVO ifaceVO, Exception e){
        //????????????????????????E
        ItfDeliveryDocIface itfDeliveryDocIface = ifaceVO.getItfDeliveryDocIface();
        itfDeliveryDocIface.setProcessStatus("E");
        String processMessage = itfDeliveryDocIface.getProcessMessage();
        String errMessage = StringUtils.isBlank(processMessage) ? e.getMessage() : processMessage + ";" + e.getMessage();
        itfDeliveryDocIface.setProcessMessage(errMessage);
        ifaceVO.setItfDeliveryDocIface(itfDeliveryDocIface);
        itfDeliveryDocIfaceMapper.updateByPrimaryKeySelective(itfDeliveryDocIface);
        //???????????????ID?????????????????????????????????E
        List<ItfDeliveryDocLineIface> itfDeliveryDocLineIfaceList = itfDeliveryDocLineIfaceRepository.select(new ItfDeliveryDocLineIface() {{
            setTenantId(tenantId);
            setInterfaceHeaderId(itfDeliveryDocIface.getInterfaceHeaderId());
        }});
        for (ItfDeliveryDocLineIface itfDeliveryDocLineIface:itfDeliveryDocLineIfaceList) {
            itfDeliveryDocLineIface.setProcessStatus("E");
            String lineProcessMessage = itfDeliveryDocLineIface.getProcessMessage();
            String lineErrMessage = StringUtils.isBlank(lineProcessMessage) ? e.getMessage() : lineProcessMessage + ";" + e.getMessage();
            itfDeliveryDocLineIface.setProcessMessage(lineErrMessage);
            itfDeliveryDocLineIfaceMapper.updateByPrimaryKeySelective(itfDeliveryDocLineIface);
        }
    }
}
