package com.ruike.wms.infra.repository.impl;

import com.ruike.qms.api.dto.QmsInvoiceDataQueryDTO1;
import com.ruike.qms.api.dto.QmsInvoiceHeadReturnDTO;
import com.ruike.wms.app.service.WmsCommonApiService;
import com.ruike.wms.domain.repository.WmsOutsourceManagePlatformRepository;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import com.ruike.wms.domain.vo.*;
import com.ruike.wms.infra.barcode.CommonPdfTemplateUtil;
import com.ruike.wms.infra.barcode.CommonQRCodeUtil;
import com.ruike.wms.infra.barcode.GetFileCharset;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsOutsourceManagePlatformMapper;
import com.ruike.wms.infra.mapper.WmsPoDeliveryRelMapper;
import com.ruike.wms.infra.mapper.WmsPurchaseOrderMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.repository.MtUserRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.api.dto.MtInstructionDocDTO2;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionDocVO3;
import tarzan.instruction.domain.vo.MtInstructionVO;
import tarzan.instruction.domain.vo.MtInstructionVO6;
import tarzan.modeling.domain.entity.*;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.repository.MtSupplierRepository;
import tarzan.modeling.domain.repository.MtSupplierSiteRepository;
import tarzan.modeling.domain.vo.MtModSiteVO6;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @Classname WmsOutsourceManagePlatformRepositoryImpl
 * @Description ??????????????????
 * @Date 2020/6/11 19:13
 * @Created by Deng xu
 */
@Component
@Slf4j
public class WmsOutsourceManagePlatformRepositoryImpl implements WmsOutsourceManagePlatformRepository {

    @Autowired
    private WmsOutsourceManagePlatformMapper mapper;
    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtInstructionActualRepository mtInstructionActualRepository;
    @Autowired
    private WmsCommonApiService wmsCommonApiService;
    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtInstructionRepository mtInstructionRepository;
    @Autowired
    private MtSupplierSiteRepository mtSupplierSiteRepository;
    @Autowired
    private WmsSiteRepository wmsSiteRepository;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private MtUserClient userClient;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtUserRepository mtUserRepository;
    @Autowired
    private MtSupplierRepository mtSupplierRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private WmsPurchaseOrderMapper purchaseOrderMapper;
    @Autowired
    private WmsPoDeliveryRelMapper wmsPoDeliveryRelMapper;

    /**
     * @param tenantId    ??????ID
     * @param condition   ????????????
     * @param pageRequest ????????????
     * @return : io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsOutsourceOrderHeadVO>
     * @Description: ??????????????????-?????????????????????
     * @author: Deng Xu
     * @date 2020/6/11 20:47
     * @version 1.0
     */
    @Override
    @ProcessLovValue
    public Page<WmsOutsourceOrderHeadVO> listHeadForUi(Long tenantId, WmsOutsourceOrderHeadVO condition, PageRequest pageRequest) {
        List<LovValueDTO> lovList = wmsCommonApiService.queryLovValueList(tenantId, "WMS.OUTSOURCING_DOC_TYPE", null);
        if (CollectionUtils.isEmpty(lovList)) {
            throw new MtException("WMS_OUTSOURCE_PLATFORM_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_OUTSOURCE_PLATFORM_0002", "WMS"));
        }
        List<String> docTypeList = lovList.stream().map(LovValueDTO::getValue).distinct().collect(toList());
        if (CollectionUtils.isEmpty(docTypeList)) {
            throw new MtException("WMS_OUTSOURCE_PLATFORM_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_OUTSOURCE_PLATFORM_0002", "WMS"));
        }
        if (StringUtils.isNotEmpty(condition.getMaterialId()) || StringUtils.isNotEmpty(condition.getMaterialVersion())) {
            condition.setLineQueryFlag(WmsConstant.CONSTANT_Y);
        }
        condition.setDocTypeList(docTypeList);
        condition.setTenantId(tenantId);
        Page<WmsOutsourceOrderHeadVO> page = PageHelper.doPageAndSort(pageRequest, () -> mapper.queryOutsourceDoc(condition));
        return page;
    }

    /**
     * @param tenantId    ??????ID
     * @param sourceDocId ?????????ID
     * @param pageRequest ????????????
     * @return : io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsOutsourceOrderLineVO>
     * @Description: ??????????????????-???????????????ID???????????????
     * @author: Deng Xu
     * @date 2020/6/12 9:48
     * @version 1.0
     */
    @Override
    public Page<WmsOutsourceOrderLineVO> listLineForUi(Long tenantId, String sourceDocId, PageRequest pageRequest) {
        //????????????????????????????????????????????????????????????
        MtInstructionDoc queryDoc = new MtInstructionDoc();
        queryDoc.setInstructionDocId(sourceDocId);
        queryDoc.setTenantId(tenantId);
        queryDoc = mtInstructionDocRepository.selectOne(queryDoc);
        //????????????????????????,?????????!
        if (null == queryDoc || StringUtils.isEmpty(queryDoc.getInstructionDocType())) {
            throw new MtException("WMS_OUTSOURCE_PLATFORM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_OUTSOURCE_PLATFORM_0001", "WMS"));
        }
        WmsOutsourceOrderLineVO condition = new WmsOutsourceOrderLineVO();
        condition.setSourceDocId(sourceDocId);
        Page<WmsOutsourceOrderLineVO> linePage = PageHelper.doPage(pageRequest, () -> mapper.queryLineByDocId(condition));

        //????????????
        List<String> lineIdList = linePage.getContent().stream().map(WmsOutsourceOrderLineVO::getInstructionId).distinct().collect(toList());
        List<WmsInventoryVO> inventoryQtyList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(lineIdList) && StringUtils.equals(queryDoc.getInstructionDocType(),"OUTSOURCING_INVOICE")){
            inventoryQtyList = mapper.batchQueryLineInventoryQty(tenantId, lineIdList);
        }
        Map<String, List<WmsInventoryVO>> qtyMap = inventoryQtyList.stream().collect(Collectors.groupingBy(dto -> dto.getInstructionId() + "_" + dto.getMaterialVersion()));
        for (WmsOutsourceOrderLineVO line : linePage) {
            //???????????? ?????????
            String keyStr = line.getInstructionId() + "_" + (StringUtils.isBlank(line.getMaterialVersion()) ? "" : line.getMaterialVersion());
            List<WmsInventoryVO> inventoryVOList = qtyMap.get(keyStr);
            if(CollectionUtils.isNotEmpty(inventoryVOList)){
                line.setInventoryQty(inventoryVOList.get(0).getInventoryQty());
            }
            //?????????????????????
            List<WmsOutsourceOrderLineVO> poNumList = mapper.queryPoNum(line);
            if (CollectionUtils.isNotEmpty(poNumList)) {
                String poNumStr = "";
                for (WmsOutsourceOrderLineVO poNum : poNumList) {
                    if (StringUtils.isNotEmpty(poNum.getInstructionDocNum())) {
                        poNumStr += poNum.getInstructionDocNum() + "#" + poNum.getInstructionLineNum();
                    }
                }
                line.setPoLineNum(poNumStr);
            }
            //??????API???propertyLimitInstructionActualQuery?????????????????????ID
            MtInstructionActual mtInstructionActual = new MtInstructionActual();
            mtInstructionActual.setInstructionId(line.getInstructionId());
            mtInstructionActual.setMaterialId(line.getMaterialId());
            List<String> actualIdList = mtInstructionActualRepository.propertyLimitInstructionActualQuery(tenantId,
                    mtInstructionActual);
            if (CollectionUtils.isNotEmpty(actualIdList) && StringUtils.isNotEmpty(actualIdList.get(0))) {
                //??????API???instructionActualPropertyGet????????????????????????
                MtInstructionActual mtInstructionActual1 = mtInstructionActualRepository.instructionActualPropertyGet(tenantId, actualIdList.get(0));
                line.setActualQty(BigDecimal.valueOf(mtInstructionActual1.getActualQty()));
            }
        }
        return linePage;
    }

    @Override
    public Page<WmsOutsourceOrderDetailsVO> listLineDetailForUi(Long tenantId, String lineId, PageRequest pageRequest) {
        Page<WmsOutsourceOrderDetailsVO> page = PageHelper.doPage(pageRequest, () -> mapper.listLineDetailForUi(tenantId, lineId));

        List<LovValueDTO> statusList = lovAdapter.queryLovValue("WMS.MTLOT.STATUS", tenantId);
        List<LovValueDTO> qualityList = lovAdapter.queryLovValue("WMS.MTLOT.QUALITY_STATUS", tenantId);
        for (WmsOutsourceOrderDetailsVO wmsOutsourceOrderDetailsVO : page.getContent()) {
            //????????????
            List<LovValueDTO> statusResultList = statusList.stream().filter(dto -> StringUtils.equals(dto.getValue(), wmsOutsourceOrderDetailsVO.getBarCodeStatus())).collect(toList());
            if (CollectionUtils.isNotEmpty(statusResultList)) {
                wmsOutsourceOrderDetailsVO.setBarCodeStatusName(statusResultList.get(0).getMeaning());
            }

            //????????????
            List<LovValueDTO> qualityResultList = qualityList.stream().filter(dto -> StringUtils.equals(dto.getValue(), wmsOutsourceOrderDetailsVO.getQualityStatus())).collect(toList());
            if (CollectionUtils.isNotEmpty(qualityResultList)) {
                wmsOutsourceOrderDetailsVO.setQualityStatusMeaning(qualityResultList.get(0).getMeaning());
            }

            //?????????
            if (StringUtils.isNotBlank(wmsOutsourceOrderDetailsVO.getLastUpdatedBy())) {
                MtUserInfo mtUserInfo = userClient.userInfoGet(tenantId, Long.valueOf(wmsOutsourceOrderDetailsVO.getLastUpdatedBy()));
                if (mtUserInfo != null) {
                    wmsOutsourceOrderDetailsVO.setLastUpdateByName(mtUserInfo.getRealName());
                }
            }
        }
        return page;
    }

    /**
     * @param tenantId    ??????ID
     * @param sourceDocId ??????????????????ID
     * @return : io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsOutsourceOrderHeadVO>
     * @Description: ??????????????????-???????????????-??????????????????
     * @author: Deng Xu
     * @date 2020/6/16 11:04
     * @version 1.0
     */
    @Override
    @ProcessLovValue
    public WmsOutsourceOrderHeadVO listLineForCreateReturnDoc(Long tenantId, String sourceDocId) {
        //?????????ID???????????????
        WmsOutsourceOrderHeadVO returnVo = new WmsOutsourceOrderHeadVO();
        returnVo.setTenantId(tenantId);
        returnVo.setInstructionDocId(sourceDocId);
        List<WmsOutsourceOrderHeadVO> queryDocList = mapper.queryOutsourceDoc(returnVo);
        if (CollectionUtils.isEmpty(queryDocList) || null == queryDocList.get(0) || StringUtils.isEmpty(queryDocList.get(0).getInstructionDocId())) {
            return new WmsOutsourceOrderHeadVO();
        }
        returnVo = queryDocList.get(0);
        //?????????id???????????????
        WmsOutsourceOrderLineVO condition = new WmsOutsourceOrderLineVO();
        condition.setSourceDocId(sourceDocId);
        returnVo.setLineDataList(mapper.listLineForCreateReturnDoc(condition));
        //?????????????????????ID
        returnVo.setInstructionDocId(null);
        return returnVo;
    }

    /**
     * @param tenantId ??????ID
     * @return : com.ruike.wms.domain.vo.WmsOutsourceOrderHeadVO
     * @Description: ??????????????????-???????????????-????????????
     * @author: Deng Xu
     * @date 2020/7/2 11:39
     * @version 1.0
     */
    @Override
    public WmsOutsourceOrderHeadVO createHeadDoc(Long tenantId) {
        Long userId = DetailsHelper.getUserDetails().getUserId();
        // ??????????????????????????????
        String siteId = wmsSiteRepository.userDefaultSite(tenantId);
        //??????????????????
        MtModSite querySite = new MtModSite();
        querySite.setTenantId(tenantId);
        querySite.setSiteId(siteId);
        querySite = mtModSiteRepository.selectByPrimaryKey(querySite);
        WmsOutsourceOrderHeadVO returnVo = new WmsOutsourceOrderHeadVO();
        //????????????????????????????????????????????????
        //??????API???numrangeGenerate??????????????????????????????
        MtNumrangeVO2 mtNumrangeVO2 = new MtNumrangeVO2();
        mtNumrangeVO2.setObjectCode("INSTRUCTION_DOC_NUM");
        mtNumrangeVO2.setObjectTypeCode("OUTSOURCING_RETURN");
        Map<String, String> map = new HashMap<>(1);
        map.put("siteCode", querySite.getSiteCode());
//        map.put("supplierCode", querySite.getSupplierCode());
        mtNumrangeVO2.setCallObjectCodeList(map);
        MtNumrangeVO5 mtNumrangeVO5 = mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrangeVO2);
        //???????????????????????????,?????????
        if (null == mtNumrangeVO5 || StringUtils.isEmpty(mtNumrangeVO5.getNumber())) {
            throw new MtException("WMS_OUTSOURCE_PLATFORM_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_OUTSOURCE_PLATFORM_0007", "WMS"));
        }
        returnVo.setInstructionDocNum(mtNumrangeVO5.getNumber());
        returnVo.setSiteId(querySite.getSiteId());
        returnVo.setSiteCode(querySite.getSiteCode());
        return returnVo;
    }


    /**
     * @param tenantId ??????ID
     * @param createVo ??????VO
     * @return : com.ruike.wms.domain.vo.WmsOutsourceOrderHeadVO
     * @Description: ??????????????????-???????????????
     * @author: Deng Xu
     * @date 2020/6/16 13:44
     * @version 1.0
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WmsOutsourceOrderHeadVO createReturnDoc(Long tenantId, WmsOutsourceOrderHeadVO createVo) {
        //???????????????????????????????????????,?????????
        if (StringUtils.isEmpty(createVo.getReturnArrivalTime())) {
            throw new MtException("WMS_OUTSOURCE_PLATFORM_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_OUTSOURCE_PLATFORM_0003", "WMS"));
        }
        //??????????????????????????????????????????????????????,????????????
        if (CollectionUtils.isEmpty(createVo.getLineDataList())) {
            throw new MtException("WMS_OUTSOURCE_PLATFORM_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_OUTSOURCE_PLATFORM_0006", "WMS"));
        }
        //????????????????????????????????????????????????,?????????
        if (StringUtils.isEmpty(createVo.getInstructionDocNum())) {
            throw new MtException("WMS_OUTSOURCE_PLATFORM_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_OUTSOURCE_PLATFORM_0007", "WMS"));
        }
        //?????????????????????????????????
        if (StringUtils.isEmpty(createVo.getSupplierId())) {
            throw new MtException("WMS_OUTSOURCE_PLATFORM_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_OUTSOURCE_PLATFORM_0011", "WMS"));
        }
        if(StringUtils.isBlank(createVo.getReason())){
            throw new MtException("WMS_OUTSOURCE_PLATFORM_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_OUTSOURCE_PLATFORM_0013", "WMS"));
        }
        //??????API???eventRequestCreate?????????????????????
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "OUTSOURCING_RETURN_CREATE");
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //??????API???instructionDocUpdate?????????????????????????????????
        MtInstructionDocDTO2 insertDoc = new MtInstructionDocDTO2();
        insertDoc.setInstructionDocStatus("RELEASED");
        insertDoc.setInstructionDocType("OUTSOURCING_RETURN");
        insertDoc.setSiteId(createVo.getSiteId());
        insertDoc.setSupplierId(createVo.getSupplierId());
        insertDoc.setReason(createVo.getReason());
        insertDoc.setSupplierSiteId(createVo.getSupplierId());
        try {
            insertDoc.setExpectedArrivalTime(format.parse(createVo.getReturnArrivalTime()));
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("erorr!", e);
            //????????????????????????,?????????
            throw new MtException("WMS_OUTSOURCE_PLATFORM_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_OUTSOURCE_PLATFORM_0005", "WMS"));
        }
        insertDoc.setRemark(createVo.getRemark());
        insertDoc.setInstructionDocNum(createVo.getInstructionDocNum());
        insertDoc.setEventRequestId(eventRequestId);
        insertDoc.setPersonId(DetailsHelper.getUserDetails().getUserId());
        MtInstructionDocVO3 instructionDocVo = mtInstructionDocRepository.instructionDocUpdate(tenantId, insertDoc, "N");
        //???????????????????????????????????????????????????
        for (WmsOutsourceOrderLineVO lineVo : createVo.getLineDataList()) {
            //??????????????????,?????????
            if (StringUtils.isEmpty(lineVo.getMaterialId())) {
                throw new MtException("WMS_OUTSOURCE_PLATFORM_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_OUTSOURCE_PLATFORM_0010", "WMS"));
            }
            if(StringUtils.isBlank(lineVo.getToLocatorId())){
                throw new MtException("WMS_OUTSOURCE_PLATFORM_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_OUTSOURCE_PLATFORM_0004", "WMS"));
            }

            //????????????????????????????????????,?????????
            if (Objects.isNull(lineVo.getQuantity()) || lineVo.getQuantity().doubleValue() <= 0) {
                throw new MtException("WMS_OUTSOURCE_PLATFORM_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_OUTSOURCE_PLATFORM_0008", "WMS"));
            }
            //?????????????????????????????????????????????:LOCATOR_TYPE = 20 ????????????????????????:LOCATOR_CATEGORY = INVENTORY??? locatorid?????????
            WmsOutsourceOrderHeadVO queryLocatorQty = new WmsOutsourceOrderHeadVO();
            queryLocatorQty.setSiteId(createVo.getSiteId());
            queryLocatorQty.setTenantId(tenantId);
            queryLocatorQty.setSupplierId(createVo.getSupplierId());
            queryLocatorQty.setMaterialId(lineVo.getMaterialId());
            BigDecimal locatorQty = mapper.queryMaterialLocatorQty(queryLocatorQty);
            if (Objects.isNull(locatorQty)) {
                //?????????xxxx??????????????????????????? MT_INVENTORY_0069
                throw new MtException("MT_INVENTORY_0069", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_INVENTORY_0069", "INVENTORY", lineVo.getMaterialCode()));
            }
            //???????????????RELEASED???????????????????????????????????????????????????
            BigDecimal returnOrderQty = mapper.queryMaterialReleasedQty(tenantId, lineVo.getMaterialId());
            //??????-??????????????????????????????????????????????????????????????????????????????
            if ((locatorQty.subtract(returnOrderQty)).subtract(lineVo.getQuantity()).doubleValue() < 0) {
                //???????????????xxxx??????????????????????????? MT_INVENTORY_0069
                throw new MtException("MT_INVENTORY_0069", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_INVENTORY_0069", "INVENTORY", lineVo.getMaterialCode()));
            }
            //??????API???instructionUpdate??????????????????
            MtInstructionVO insertLine = new MtInstructionVO();
            insertLine.setInstructionStatus("RELEASED");
            insertLine.setSourceDocId(instructionDocVo.getInstructionDocId());
            insertLine.setSiteId(createVo.getSiteId());
            insertLine.setMaterialId(lineVo.getMaterialId());
            insertLine.setUomId(lineVo.getUomId());
            insertLine.setToSiteId(createVo.getSiteId());
            insertLine.setToLocatorId(lineVo.getToLocatorId());
            insertLine.setQuantity(Double.parseDouble(String.valueOf(lineVo.getQuantity())));
            insertLine.setSupplierId(createVo.getSupplierId());
            insertLine.setSupplierSiteId(createVo.getSupplierId());
            insertLine.setBusinessType("OUTSOURCING_RETURN");
            insertLine.setFromOwnerType("IIS");
            insertLine.setEventRequestId(eventRequestId);
            insertLine.setInstructionType(WmsConstant.InstructionType.RECEIVE_FROM_SUPPLIER);
            //?????????????????????
            MtInstructionVO6 instructionVo = mtInstructionRepository.instructionUpdate(tenantId, insertLine, "N");
            lineVo.setInstructionId(instructionVo.getInstructionId());
            //??????API???instructionAttrPropertyUpdate????????????????????????
            MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
            mtExtendVO10.setKeyId(instructionVo.getInstructionId());
            List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
            //??????
            MtExtendVO5 lineNum = new MtExtendVO5();
            lineNum.setAttrName("INSTRUCTION_LINE_NUM");
            lineNum.setAttrValue(lineVo.getInstructionLineNum());
            mtExtendVO5List.add(lineNum);
            //????????????
            MtExtendVO5 materialVersion = new MtExtendVO5();
            materialVersion.setAttrName("MATERIAL_VERSION");
            materialVersion.setAttrValue(lineVo.getMaterialVersion());
            mtExtendVO5List.add(materialVersion);
            mtExtendVO10.setAttrs(mtExtendVO5List);
            //????????????
            mtInstructionRepository.instructionAttrPropertyUpdate(tenantId, mtExtendVO10);
        }
        createVo.setInstructionDocId(instructionDocVo.getInstructionDocId());
        return createVo;
    }

    private QmsInvoiceHeadReturnDTO queryHeaderInfo(Long tenantId, MtInstructionDoc mtInstructionDoc){
        //??????
        MtModSiteVO6 mtModSiteVO6 = new MtModSiteVO6();
        mtModSiteVO6.setSiteId(mtInstructionDoc.getSiteId());
        List<MtModSiteVO6> mtModSiteVO6s = mtModSiteRepository.propertyLimitSitePropertyQuery(tenantId, mtModSiteVO6);
        String siteName = mtModSiteVO6s.get(0).getSiteName();
        //????????????????????????
        MtSupplier mtSupplier = mtSupplierRepository.selectByPrimaryKey(mtInstructionDoc.getSupplierId());
        String supplierCode = mtSupplier.getSupplierCode();
        String supplierName = mtSupplier.getSupplierName();
        //????????????
        MtNumrangeVO2 mtNumrangeVO2 = new MtNumrangeVO2();
        mtNumrangeVO2.setObjectCode("INSTRUCTION_DOC_NUM");
        mtNumrangeVO2.setObjectTypeCode("OUTSOURCING_INVOICE");
        Map<String, String> callObjectCodeList = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        callObjectCodeList.put("siteCode", mtModSiteVO6s.get(0).getSiteCode());
        callObjectCodeList.put("supplierCode", supplierCode);
        mtNumrangeVO2.setCallObjectCodeList(callObjectCodeList);
        MtNumrangeVO5 mtNumrangeVO5 = mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrangeVO2);
        String number = mtNumrangeVO5.getNumber();
        //????????????
        String state = "NEW";
        //????????????
        String type = "OUTSOURCING_INVOICE";
        //?????????
        Long userId = DetailsHelper.getUserDetails().getUserId();
        MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, userId);
        String userName = mtUserInfo.getRealName();
        //?????????????????????
        QmsInvoiceHeadReturnDTO qmsInvoiceHeadReturnDTO = new QmsInvoiceHeadReturnDTO();
        qmsInvoiceHeadReturnDTO.setSiteName(siteName);
        qmsInvoiceHeadReturnDTO.setNumber(number);
        qmsInvoiceHeadReturnDTO.setSupplierCode(supplierCode);
        qmsInvoiceHeadReturnDTO.setSupplierName(supplierName);
        qmsInvoiceHeadReturnDTO.setState(state);
        qmsInvoiceHeadReturnDTO.setType(type);
        qmsInvoiceHeadReturnDTO.setEarilyDemandTime(new Date());
        qmsInvoiceHeadReturnDTO.setUserName(userName);
        return qmsInvoiceHeadReturnDTO;
    }

    @Override
    @ProcessLovValue(targetField = {"qmsInvoiceHeadReturnDTO", "",""})
    public WmsReplenishmentOrderVO createHeaderQuery(Long tenantId, String instructionDocId) {
        //???????????????
        WmsReplenishmentOrderVO orderVO = new WmsReplenishmentOrderVO();
        MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.selectByPrimaryKey(instructionDocId);
        QmsInvoiceHeadReturnDTO dto = this.queryHeaderInfo(tenantId, mtInstructionDoc);

        List<MtInstruction> mtInstructionList = mtInstructionRepository.select(new MtInstruction(){{
            setSourceDocId(instructionDocId);
            setTenantId(tenantId);
        }});
        List<String> lineIdList = mtInstructionList.stream().map(MtInstruction::getInstructionId).collect(toList());
        if(CollectionUtils.isEmpty(lineIdList)){
            throw new MtException("WMS_OUTSOURCE_PLATFORM_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_OUTSOURCE_PLATFORM_0015", "WMS", mtInstructionDoc.getInstructionDocNum()));
        }
        //???????????????
        List<WmsReplenishmentOrderLineVO> wmsReplenishmentOrderLineVOList = mapper.queryReplenishmentOrderLine(tenantId, mtInstructionDoc.getInstructionDocId(), lineIdList);
        Long lineNum = 10L;
        List<MtModLocator> mtModLocatorList = mtModLocatorRepository.selectByCondition(Condition.builder(MtModLocator.class)
                .andWhere(Sqls.custom().andEqualTo(MtModLocator.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtModLocator.FIELD_ENABLE_FLAG, WmsConstant.CONSTANT_Y)
                        .andEqualTo(MtModLocator.FIELD_LOCATOR_CODE, "1031")).build());

        //????????????
        List<WmsReplenishmentOrderLineVO> wmsReplenishmentOrderLineVOS = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(mtModLocatorList)){
            wmsReplenishmentOrderLineVOS = mapper.queryInventoryQty(tenantId, mtModLocatorList.get(0).getLocatorId());
        }
        Map<String, List<WmsReplenishmentOrderLineVO>> invMap = wmsReplenishmentOrderLineVOS.stream().collect(Collectors.groupingBy(inv -> inv.getMaterialId() + "_" + inv.getSiteId() + "_" + (StringUtils.isBlank(inv.getMaterialVersion()) ? "" : inv.getMaterialVersion())));

        for (WmsReplenishmentOrderLineVO wmsReplenishmentOrderLineVO : wmsReplenishmentOrderLineVOList) {
            wmsReplenishmentOrderLineVO.setLineNum(lineNum);
            if(CollectionUtils.isNotEmpty(mtModLocatorList)){
                wmsReplenishmentOrderLineVO.setDeliveryWarehouseId(mtModLocatorList.get(0).getLocatorId());
                wmsReplenishmentOrderLineVO.setDeliveryWarehouseCode(mtModLocatorList.get(0).getLocatorCode());
                wmsReplenishmentOrderLineVO.setDeliveryWarehouseName(mtModLocatorList.get(0).getLocatorName());
            }
            String keyStr =  wmsReplenishmentOrderLineVO.getMaterialId() + "_" + wmsReplenishmentOrderLineVO.getSiteId() + "_" + (StringUtils.isNotBlank(wmsReplenishmentOrderLineVO.getMaterialVersion()) ? wmsReplenishmentOrderLineVO.getMaterialVersion() : "");
            List<WmsReplenishmentOrderLineVO> wmsReplenishmentOrderLineVOS1 = invMap.get(keyStr);
            wmsReplenishmentOrderLineVO.setInventoryQuantity(BigDecimal.ZERO);
            if(CollectionUtils.isNotEmpty(wmsReplenishmentOrderLineVOS1)){
                wmsReplenishmentOrderLineVO.setInventoryQuantity(wmsReplenishmentOrderLineVOS1.get(0).getInventoryQuantity());
            }
            lineNum += 10;
        }
        orderVO.setLineVOList(wmsReplenishmentOrderLineVOList);
        orderVO.setQmsInvoiceHeadReturnDTO(dto);
        return orderVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WmsReplenishmentOrderVO createReplenishment(Long tenantId, WmsReplenishmentOrderVO wmsReplenishmentOrderVO) {
        //????????????
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "OUTSOURCING_INVOICE_CREATE");
        //????????????
        MtEventCreateVO eventCreate = new MtEventCreateVO();
        eventCreate.setEventRequestId(eventRequestId);
        eventCreate.setEventTypeCode("OUTSOURCING_INVOICE_CREATE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreate);

        QmsInvoiceHeadReturnDTO qmsInvoiceHeadReturnDTO = wmsReplenishmentOrderVO.getQmsInvoiceHeadReturnDTO();

        List<WmsReplenishmentOrderLineVO> lineVOList = wmsReplenishmentOrderVO.getLineVOList();
        MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.selectByPrimaryKey(lineVOList.get(0).getSourceDocId());
        //???????????????
        MtInstructionDocDTO2 mtInstructionDoc2 = new MtInstructionDocDTO2();
        mtInstructionDoc2.setInstructionDocType("OUTSOURCING_INVOICE");
        mtInstructionDoc2.setInstructionDocStatus("RELEASED");
        mtInstructionDoc2.setSiteId(mtInstructionDoc.getSiteId());
        mtInstructionDoc2.setSupplierId(mtInstructionDoc.getSupplierId());
        mtInstructionDoc2.setSupplierSiteId(mtInstructionDoc.getSupplierSiteId());
        mtInstructionDoc2.setDemandTime(qmsInvoiceHeadReturnDTO.getEarilyDemandTime());
        mtInstructionDoc2.setRemark(qmsInvoiceHeadReturnDTO.getRemark());
        mtInstructionDoc2.setInstructionDocNum(qmsInvoiceHeadReturnDTO.getNumber());
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        Long userId = userDetails != null ? userDetails.getUserId() : -1L;
        mtInstructionDoc2.setPersonId(userId);
        mtInstructionDoc2.setEventRequestId(eventRequestId);
        MtInstructionDocVO3 mtInstructionDocVO3 = mtInstructionDocRepository.instructionDocUpdate(tenantId, mtInstructionDoc2, "N");

        //???????????????????????????
        List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
        MtExtendVO5 vo5 = new MtExtendVO5();
        vo5.setAttrName("SUPPLY_FLAG");
        vo5.setAttrValue(WmsConstant.CONSTANT_Y);
        mtExtendVO5List.add(vo5);
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_doc_attr", mtInstructionDocVO3.getInstructionDocId(),
                eventId, mtExtendVO5List);

        //???????????????
        List<MtExtendVO5> mtExtendList = new ArrayList<>();
        MtExtendVO5 numAttr = new MtExtendVO5();
        numAttr.setAttrName("REPLENISHMENT_LIST_NUM");
        numAttr.setAttrValue(qmsInvoiceHeadReturnDTO.getNumber());
        mtExtendList.add(numAttr);
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_doc_attr", mtInstructionDoc.getInstructionDocId(),
                eventId, mtExtendList);
        for (WmsReplenishmentOrderLineVO wmsReplenishmentOrderLineVO : lineVOList) {
            //???????????????
            MtInstructionVO mtInstructionVO = new MtInstructionVO();
            mtInstructionVO.setInstructionStatus("RELEASED");
            mtInstructionVO.setSourceDocId(mtInstructionDocVO3.getInstructionDocId());
            mtInstructionVO.setSiteId(mtInstructionDoc.getSiteId());
            mtInstructionVO.setMaterialId(wmsReplenishmentOrderLineVO.getMaterialId());
            mtInstructionVO.setUomId(wmsReplenishmentOrderLineVO.getUomId());
            mtInstructionVO.setFromSiteId(mtInstructionDoc.getSiteId());
            mtInstructionVO.setFromLocatorId(wmsReplenishmentOrderLineVO.getDeliveryWarehouseId());
            mtInstructionVO.setQuantity(wmsReplenishmentOrderLineVO.getQuantity().doubleValue());
            mtInstructionVO.setSupplierId(mtInstructionDoc.getSupplierId());
            mtInstructionVO.setSupplierSiteId(mtInstructionDoc.getSupplierSiteId());
            mtInstructionVO.setBusinessType("OUTSOURCING_SENDING");
            mtInstructionVO.setInstructionType("RETURN_TO_SUPPLIER");
            mtInstructionVO.setEventRequestId(eventRequestId);
            MtInstructionVO6 mtInstructionVO6 = mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, "N");

            //????????????
            List<MtExtendVO5> attrList = new ArrayList<>();
            //??????
            MtExtendVO5 lineNumAttr = new MtExtendVO5();
            lineNumAttr.setAttrName("INSTRUCTION_LINE_NUM");
            lineNumAttr.setAttrValue(wmsReplenishmentOrderLineVO.getLineNum().toString());
            attrList.add(lineNumAttr);
            //????????????
            MtExtendVO5 mvAttr = new MtExtendVO5();
            mvAttr.setAttrName("MATERIAL_VERSION");
            mvAttr.setAttrValue(wmsReplenishmentOrderLineVO.getMaterialVersion());
            attrList.add(mvAttr);
            //????????????
            MtExtendVO5 qtyAttr = new MtExtendVO5();
            qtyAttr.setAttrName("ACTUAL_ORDERED_QTY");
            qtyAttr.setAttrValue(wmsReplenishmentOrderLineVO.getQuantity().toString());
            attrList.add(qtyAttr);
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_attr", mtInstructionVO6.getInstructionId(),
                    eventId, attrList);
        }
        return wmsReplenishmentOrderVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WmsInstructionVO2 closeReturnDoc(Long tenantId, WmsInstructionVO2 wmsInstructionVO2) {
        MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.selectByPrimaryKey(wmsInstructionVO2.getSourceDocId());
        //??????????????????
        if(StringUtils.equals(mtInstructionDoc.getInstructionDocType(), "OUTSOURCING_INVOICE")){
            //????????????
            MtExtendVO dto = new MtExtendVO();
            dto.setTableName("mt_instruction_doc_attr");
            dto.setAttrName("SUPPLY_FLAG");
            dto.setKeyId(mtInstructionDoc.getInstructionDocId());
            List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, dto);
            String supplyFlag = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";

            if(!StringUtils.equals(mtInstructionDoc.getInstructionDocStatus(), "RELEASED") || StringUtils.equals(supplyFlag, WmsConstant.CONSTANT_Y)){
                throw new MtException("WMS_INV_TRANSFER_0049", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_INV_TRANSFER_0049", "WMS"));
            }
        }else if(StringUtils.equals(mtInstructionDoc.getInstructionDocType(), "OUTSOURCING_RETURN")){
            if(!StringUtils.equals(mtInstructionDoc.getInstructionDocStatus(), "RELEASED")){
                throw new MtException("exception","??????????????????????????????????????????????????????");
            }
            //???????????????Id???????????????
            MtInstruction mtInstruction = new MtInstruction();
            mtInstruction.setTenantId(tenantId);
            mtInstruction.setSourceDocId(mtInstructionDoc.getInstructionDocId());
            List<MtInstruction> mtInstructions = mtInstructionRepository.select(mtInstruction);
            List<String> instructionIdList = mtInstructions.stream().map(MtInstruction::getInstructionId).collect(toList());
            List<MtInstructionActual> mtInstructionActualList = mtInstructionActualRepository.instructionLimitActualBatchGet(tenantId,instructionIdList);
            if(CollectionUtils.isNotEmpty(mtInstructionActualList)){
                for(MtInstructionActual mtInstructionActual:mtInstructionActualList){
                    if(mtInstructionActual.getActualQty() != 0){
                        throw new MtException("exception","??????????????????????????????????????????????????????");
                    }
                }
            }
        }else{
            throw new MtException("exception","????????????????????????????????????????????????");
        }
        //??????????????????
        Integer count = mapper.queryScannedMaterialLot(tenantId, mtInstructionDoc.getInstructionDocId());
        if(count > 0){
            throw new MtException("WMS_OUTSOURCE_PLATFORM_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_OUTSOURCE_PLATFORM_0012", "WMS"));
        }
        //????????????????????????
        MtExtendVO poAttr = new MtExtendVO();
        poAttr.setTableName("mt_instruction_doc_attr");
        poAttr.setAttrName("PO_NUM");
        poAttr.setKeyId(mtInstructionDoc.getInstructionDocId());
        List<MtExtendAttrVO> poNumList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, poAttr);
        if(CollectionUtils.isNotEmpty(poNumList)){
            MtInstructionDoc instructionDoc = mtInstructionDocRepository.selectOne(new MtInstructionDoc() {{
                setTenantId(tenantId);
                setInstructionDocNum(poNumList.get(0).getAttrValue());
            }});
            if(instructionDoc != null){
                List<MtInstruction> mtInstructionList = purchaseOrderMapper.queryMtInstructionList(instructionDoc.getInstructionDocId());
                for (MtInstruction mtInstruction : mtInstructionList) {
                    //??????????????????
                    Double completeQty = wmsPoDeliveryRelMapper.selectPoQuantityOfComplete("", mtInstruction.getInstructionId());
                    //????????????
                    Double stockQty = wmsPoDeliveryRelMapper.selectPoQuantityOfStockInComplete("", mtInstruction.getInstructionId());

                    BigDecimal receivedQty = BigDecimal.ZERO.add(BigDecimal.valueOf(completeQty)).add(BigDecimal.valueOf(stockQty));
                    if(BigDecimal.ZERO.compareTo(receivedQty) != 0) {
                        throw new MtException("WMS_OUTSOURCE_PLATFORM_0016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_OUTSOURCE_PLATFORM_0016", "WMS", poNumList.get(0).getAttrValue()));
                    }
                }
            }
        }

        //????????????
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "OUTSOURCING_INVOICE_CLOSE");
        //????????????
        MtEventCreateVO eventCreate = new MtEventCreateVO();
        eventCreate.setEventRequestId(eventRequestId);
        eventCreate.setEventTypeCode("OUTSOURCING_INVOICE_CLOSE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreate);

        List<MtInstruction> mtInstructionList = mtInstructionRepository.selectByCondition(Condition.builder(MtInstruction.class)
                .andWhere(Sqls.custom().andEqualTo(MtInstruction.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtInstruction.FIELD_SOURCE_DOC_ID, mtInstructionDoc.getInstructionDocId())).build());
        List<String> instructionIdList = mtInstructionList.stream().map(MtInstruction::getInstructionId).collect(toList());
        if(CollectionUtils.isNotEmpty(instructionIdList)){
            MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
            mtExtendVO1.setTableName("mt_instruction_attr");
            mtExtendVO1.setKeyIdList(instructionIdList);
            List<MtExtendVO5> attrList = new ArrayList<>();
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("MATERIAL_VERSION");
            attrList.add(mtExtendVO5);
            MtExtendVO5 qtyAttr = new MtExtendVO5();
            qtyAttr.setAttrName("ACTUAL_ORDERED_QTY");
            attrList.add(qtyAttr);
            mtExtendVO1.setAttrs(attrList);
            List<MtExtendAttrVO1> attrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
            Map<String, List<MtExtendAttrVO1>> attrMap = attrVO1List.stream().collect(Collectors.groupingBy(attr -> attr.getKeyId() + "_" + attr.getAttrName()));

            //????????????
            List<MtModLocator> locatorList = mtModLocatorRepository.select(new MtModLocator() {{
                setTenantId(tenantId);
                setLocatorType("20");
                setLocatorCategory("INVENTORY");
            }});

            if(CollectionUtils.isEmpty(locatorList)){
                throw new MtException("WMS_OUTSOURCE_PLATFORM_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_OUTSOURCE_PLATFORM_0014", "WMS"));
            }
            //??????????????????
            List<MtInstructionDoc> instructionDocList = mtInstructionDocRepository.selectByCondition(Condition.builder(MtInstructionDoc.class)
                    .andWhere(Sqls.custom().andEqualTo(MtInstructionDoc.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(MtInstructionDoc.FIELD_INSTRUCTION_DOC_TYPE, "OVER")
                            .andEqualTo(MtInstructionDoc.FIELD_SITE_ID, mtInstructionDoc.getSiteId())
                            .andEqualTo(MtInstructionDoc.FIELD_SUPPLIER_ID, mtInstructionDoc.getSupplierId())
                            .andEqualTo(MtInstructionDoc.FIELD_SUPPLIER_SITE_ID, mtInstructionDoc.getSupplierSiteId())).build());
            String instructionDocId = CollectionUtils.isNotEmpty(instructionDocList) ? instructionDocList.get(0).getInstructionDocId() : null;
            for (MtInstruction mtInstruction : mtInstructionList) {
                String keyStr = mtInstruction.getInstructionId() + "_" + "MATERIAL_VERSION";
                List<MtExtendAttrVO1> attrVO1List1 = attrMap.get(keyStr);
                String materialVersion = CollectionUtils.isNotEmpty(attrVO1List1) ? attrVO1List1.get(0).getAttrValue() : "";
                //????????????
                String qtyKey = mtInstruction.getInstructionId() + "_" + "ACTUAL_ORDERED_QTY";
                List<MtExtendAttrVO1> attrVO1List2 = attrMap.get(qtyKey);
                BigDecimal quantity = BigDecimal.ZERO;
                if(CollectionUtils.isNotEmpty(attrVO1List2)){
                    quantity = BigDecimal.valueOf(Double.valueOf(attrVO1List2.get(0).getAttrValue()));
                }

                BigDecimal actualQty = BigDecimal.ZERO;
                if(!StringUtils.equals(mtInstruction.getInstructionStatus(), "COMPLETED")){
                    //??????????????????
                    List<MtInstructionActual> actualList = mtInstructionActualRepository.select(new MtInstructionActual(){{
                        setInstructionId(mtInstruction.getInstructionId());
                        setTenantId(tenantId);
                    }});
                    if(CollectionUtils.isNotEmpty(actualList)){
                        actualQty = BigDecimal.valueOf(actualList.get(0).getActualQty());
                    }
                }
                if(mtInstruction.getQuantity() == null){
                    mtInstruction.setQuantity(0D);
                }
                if(StringUtils.isBlank(instructionDocId)){
                    //????????? ??????????????????
                    MtInstructionDocDTO2 mtInstructionDoc2 = new MtInstructionDocDTO2();
                    mtInstructionDoc2.setInstructionDocType("OVER");
                    mtInstructionDoc2.setSiteId(mtInstruction.getSiteId());
                    mtInstructionDoc2.setSupplierId(mtInstruction.getSupplierId());
                    mtInstructionDoc2.setSupplierSiteId(mtInstruction.getSupplierSiteId());
                    mtInstructionDoc2.setEventRequestId(eventRequestId);
                    MtInstructionDocVO3 mtInstructionDocVO3 = mtInstructionDocRepository.instructionDocUpdate(tenantId, mtInstructionDoc2, "N");
                    instructionDocId = mtInstructionDocVO3.getInstructionDocId();
                }

                //?????????????????????
                List<MtInstruction> instructionList = mapper.queryOutsourceInstruction(tenantId, instructionDocId, mtInstruction.getMaterialId(), materialVersion);
                if(CollectionUtils.isNotEmpty(instructionList)){
                    for (MtInstruction instruction : instructionList) {
                        //??????quantity coverQty
                        MtInstructionVO instructionVO = new MtInstructionVO();
                        instructionVO.setInstructionId(instruction.getInstructionId());
                        instructionVO.setEventId(eventId);
                        BigDecimal quantityLine = instruction.getQuantity() != null ? BigDecimal.valueOf(instruction.getQuantity()) : BigDecimal.ZERO;
                        if(StringUtils.equals(mtInstruction.getInstructionStatus(), "COMPLETED")) {
                            instructionVO.setQuantity(BigDecimal.valueOf(mtInstruction.getQuantity()).add(quantityLine).doubleValue());
                            instructionVO.setCoverQty(instruction.getCoverQty());
                            mtInstructionRepository.instructionUpdate(tenantId, instructionVO, "N");
                        }else {
                            instructionVO.setQuantity(actualQty.add(quantityLine).doubleValue());
                            //??????????????????????????????????????????????????????????????????update by zhanzhao.hand 2021-11-04
                            if(StringUtils.equals(mtInstructionDoc.getInstructionDocType(), "OUTSOURCING_RETURN")){
                                BigDecimal resultQty = (instruction.getCoverQty() != null ? BigDecimal.valueOf(instruction.getCoverQty()) : BigDecimal.ZERO);
                                instructionVO.setCoverQty(resultQty.doubleValue());
                            }else{
                                BigDecimal resultQty = (instruction.getCoverQty() != null ? BigDecimal.valueOf(instruction.getCoverQty()) : BigDecimal.ZERO).subtract(BigDecimal.valueOf(mtInstruction.getQuantity()).subtract(quantity));
                                instructionVO.setCoverQty(resultQty.doubleValue());
                                mtInstructionRepository.instructionUpdate(tenantId, instructionVO, "N");
                            }

                        }

                    }
                }else {
                    //???????????????
                    MtInstructionVO mtInstructionVO = new MtInstructionVO();
                    mtInstructionVO.setInstructionStatus("RELEASED");
                    mtInstructionVO.setSourceDocId(instructionDocId);
                    mtInstructionVO.setSiteId(mtInstructionDoc.getSiteId());
                    mtInstructionVO.setMaterialId(mtInstruction.getMaterialId());
                    mtInstructionVO.setUomId(mtInstruction.getUomId());
                    mtInstructionVO.setFromSiteId(mtInstruction.getSiteId());
                    mtInstructionVO.setFromLocatorId(locatorList.get(0).getLocatorId());
                    mtInstructionVO.setSupplierId(mtInstruction.getSupplierId());
                    mtInstructionVO.setSupplierSiteId(mtInstruction.getSupplierSiteId());
                    mtInstructionVO.setBusinessType("OUTSOURCING_SENDING");
                    mtInstructionVO.setInstructionType("RETURN_TO_SUPPLIER");
                    if(StringUtils.equals(mtInstruction.getInstructionStatus(), "COMPLETED")) {
                        mtInstructionVO.setQuantity(mtInstruction.getQuantity());
                        mtInstructionVO.setCoverQty(BigDecimal.ZERO.doubleValue());
                    }else {
                        mtInstructionVO.setQuantity(actualQty.doubleValue());
                        mtInstructionVO.setCoverQty(quantity.subtract(BigDecimal.valueOf(mtInstruction.getQuantity())).doubleValue());
                    }
                    mtInstructionVO.setEventRequestId(eventRequestId);
                    MtInstructionVO6 mtInstructionVO6 = mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, "N");

                    List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
                    MtExtendVO5 mtExtendVO51 = new MtExtendVO5();
                    mtExtendVO51.setAttrName("MATERIAL_VERSION");
                    mtExtendVO51.setAttrValue(materialVersion);
                    mtExtendVO5List.add(mtExtendVO51);
                    mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_attr", mtInstructionVO6.getInstructionId(), eventId, mtExtendVO5List);
                }
            }
        }
        //?????????????????????
        MtInstructionDocDTO2 dto2 = new MtInstructionDocDTO2();
        dto2.setInstructionDocStatus("CLOSED");
        dto2.setInstructionDocId(mtInstructionDoc.getInstructionDocId());
        dto2.setEventId(eventId);
        mtInstructionDocRepository.instructionDocUpdate(tenantId, dto2, "N");
        return wmsInstructionVO2;
    }

    @Override
    public WmsReplenishmentOrderLineVO queryInventoryQuantity(Long tenantId, WmsReplenishmentOrderLineVO lineVO) {

        List<WmsReplenishmentOrderLineVO> wmsReplenishmentOrderLineVOS = mapper.queryInventoryQty(tenantId, lineVO.getDeliveryWarehouseId());
        Map<String, List<WmsReplenishmentOrderLineVO>> invMap = wmsReplenishmentOrderLineVOS.stream().collect(Collectors.groupingBy(inv -> inv.getMaterialId() + "_" + inv.getSiteId() + "_" + (StringUtils.isBlank(inv.getMaterialVersion()) ? "" : inv.getMaterialVersion())));

        String keyStr =  lineVO.getMaterialId() + "_" + lineVO.getSiteId() + "_" + (StringUtils.isNotBlank(lineVO.getMaterialVersion()) ? lineVO.getMaterialVersion() : "");
        List<WmsReplenishmentOrderLineVO> wmsReplenishmentOrderLineVOS1 = invMap.get(keyStr);
        lineVO.setInventoryQuantity(BigDecimal.ZERO);
        if(CollectionUtils.isNotEmpty(wmsReplenishmentOrderLineVOS1)){
            lineVO.setInventoryQuantity(wmsReplenishmentOrderLineVOS1.get(0).getInventoryQuantity());
        }
        return lineVO;
    }

    @Override
    public void outsourceCreatePdf(Long tenantId, List<String> sourceDocIdList, HttpServletResponse response) {
        List<String> filePathList = new ArrayList();
        String outStream = "";
        //???????????????
        String systemPath = System.getProperty("user.dir");
        String classUrl = this.getClass().getResource("/").getPath();
        log.info("<==== System path :: {}", systemPath);
        log.info("<==== class path :: {}", classUrl);
        String basePath = classUrl + "/templates";
        if (!new File(classUrl).exists()) {
            File file = new File(systemPath + "/templates");
            if (!file.exists()) {
                if (!file.mkdir()) {
                    throw new MtException("???????????????????????????!");
                }
            }
            basePath = systemPath + "/templates";
        } else {
            basePath = classUrl + "/templates";
        }
        String docNumber = "";
        String uuid = UUID.randomUUID().toString();
        ;
        String barcodePath = "";
        String qrcodePath = "";
        String content = "";
        String pdfFileName = uuid + ".pdf";
        String pdfPath = basePath + "/" + pdfFileName;
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        List<File> qrcodeImageFileList = new ArrayList<File>();

        //????????????????????????????????????
        Long lineCount = 10L;
        //??????????????????????????????????????????
        for (String instructionDocId : sourceDocIdList) {
            //???????????????
            WmsOutsourceOrderHeadVO wmsOutsourceOrderHeadVO = this.listLineForCreateReturnDoc(tenantId, instructionDocId);

            //????????????
            List<LovValueDTO> list = lovAdapter.queryLovValue("WMS.OUTSOURCING_DOC_TYPE", tenantId);
            List<LovValueDTO> typeList = list.stream().filter(dto -> StringUtils.equals(dto.getValue(), wmsOutsourceOrderHeadVO.getInstructionDocType())).collect(toList());
            if(CollectionUtils.isNotEmpty(typeList)){
                wmsOutsourceOrderHeadVO.setInstructionDocTypeMeaning(typeList.get(0).getMeaning());
            }

            //???????????????
            String codeUuid = UUID.randomUUID().toString();
            qrcodePath = basePath + "/" + uuid + "_" + docNumber + "_qrcode.png";
            File qrcodeImageFile = new File(qrcodePath);
            qrcodeImageFileList.add(qrcodeImageFile);
            content = wmsOutsourceOrderHeadVO.getInstructionDocNum();
            try {
                CommonQRCodeUtil.encode(content, qrcodePath, qrcodePath, true);
                log.info("<====????????????????????????{}", qrcodePath);
            } catch (Exception e) {
                log.error("<==== WmsDeliveryDocServiceImpl.multiplePrint.encode Error", e);
                throw new MtException(e.getMessage());
            }

            //????????????
            Map<String, Object> imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            imgMap.put("barcodeImage", qrcodePath);
            Map<String, Object> formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);

            //???????????????
            Boolean printflag = false;
            Long currentLine = 0L;
            BigDecimal sumQty = BigDecimal.ZERO;
            List<WmsOutsourceOrderLineVO> wmsOutsourceOrderLineVOList = mapper.queryLineByDocId(new WmsOutsourceOrderLineVO(){{
                setSourceDocId(instructionDocId);
            }});

            String poNumStr = StringUtils.isNotBlank(wmsOutsourceOrderHeadVO.getPoLineNum()) ? wmsOutsourceOrderHeadVO.getPoLineNum() : "" ;

            for (WmsOutsourceOrderLineVO lineDto : wmsOutsourceOrderLineVOList) {
                formMap.put("LineNum" + currentLine.toString(), lineDto.getInstructionLineNum());
                formMap.put("ItemCode" + currentLine.toString(), lineDto.getMaterialCode());
                formMap.put("ItemDesc" + currentLine.toString(), lineDto.getMaterialName());
                BigDecimal actualOrderedQty = BigDecimal.ZERO;
                //????????????????????? ???????????????????????????
                if(StringUtils.equals(wmsOutsourceOrderHeadVO.getInstructionDocType(), WmsConstant.InspectionDocType.OUTSOURCING_RETURN)){
                     actualOrderedQty = lineDto.getQuantity() != null ? lineDto.getQuantity() : BigDecimal.ZERO;
                }else if(StringUtils.equals(wmsOutsourceOrderHeadVO.getInstructionDocType(), WmsConstant.InspectionDocType.OUTSOURCING_INVOICE)) {
                     actualOrderedQty = lineDto.getActualOrderedQty() != null ? lineDto.getActualOrderedQty() : BigDecimal.ZERO;
                }
                formMap.put("Qty" + currentLine.toString(), actualOrderedQty.stripTrailingZeros().toPlainString());
                formMap.put("Version" + currentLine.toString(), lineDto.getMaterialVersion());
                formMap.put("Uom" + currentLine.toString(), lineDto.getUomCode());
                formMap.put("OutWarehouse" + currentLine.toString(), lineDto.getFromLocatorCode());
                formMap.put("InWarehouse" + currentLine.toString(), lineDto.getToLocatorCode());
                formMap.put("remark" + currentLine.toString(), lineDto.getRemark());
                sumQty = sumQty.add(actualOrderedQty);
                currentLine += 1L;
                printflag = false;
                if (currentLine % lineCount == 0) {
                    formMap.put("instructionDocNum", wmsOutsourceOrderHeadVO.getInstructionDocNum());
                    formMap.put("instructionType", wmsOutsourceOrderHeadVO.getInstructionDocTypeMeaning());
                    formMap.put("instructionDocType", wmsOutsourceOrderHeadVO.getInstructionDocTypeMeaning());
                    formMap.put("siteName", wmsOutsourceOrderHeadVO.getSupplierName());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    formMap.put("date", sdf.format(new Date()));
                    formMap.put("instructionNum", poNumStr);
                    formMap.put("receiptSite", wmsOutsourceOrderHeadVO.getCustomerSiteDes());
                    formMap.put("remark", wmsOutsourceOrderHeadVO.getRemark());
                    formMap.put("createdBy", wmsOutsourceOrderHeadVO.getRealName());

                    formMap.put("sumQty", sumQty.stripTrailingZeros().toPlainString());
                    Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                    param.put("formMap", formMap);
                    param.put("imgMap", imgMap);
                    dataList.add(param);
                    currentLine = 0L;
                    sumQty = BigDecimal.ZERO;
                    formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                    printflag = true;
                }
            }
            if (!printflag) {
                formMap.put("instructionDocNum", wmsOutsourceOrderHeadVO.getInstructionDocNum());
                formMap.put("instructionType", wmsOutsourceOrderHeadVO.getInstructionDocTypeMeaning());
                formMap.put("instructionDocType", wmsOutsourceOrderHeadVO.getInstructionDocTypeMeaning());
                formMap.put("siteName", wmsOutsourceOrderHeadVO.getSupplierName());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                formMap.put("date", sdf.format(new Date()));
                formMap.put("instructionNum", poNumStr);
                formMap.put("receiptSite", wmsOutsourceOrderHeadVO.getCustomerSiteDes());
                formMap.put("remark", wmsOutsourceOrderHeadVO.getRemark());
                formMap.put("createdBy", wmsOutsourceOrderHeadVO.getRealName());

                formMap.put("sumQty", sumQty.stripTrailingZeros().toPlainString());
                Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                param.put("formMap", formMap);
                param.put("imgMap", imgMap);
                dataList.add(param);
            }
        }
        if (dataList.size() > 0) {
            //??????PDF
            try {
                log.info("<==== ??????PDF????????????:{}:{}", pdfPath, dataList.size());
                CommonPdfTemplateUtil.multiplePage(basePath + "/hme_outsource_print_template.pdf", pdfPath, dataList);
                log.info("<==== ??????PDF?????????{}", pdfPath);
            } catch (Exception e) {
                log.error("<==== WmsOutsourceManagePlatformRepositoryImpl.outsourceCreatePdf.generatePDFFile Error", e);
                throw new MtException(e.getMessage());
            }
        }

        //?????????????????????????????????
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File pdfFile = new File(pdfPath);
        try {
            //??????????????????
            response.setHeader("Content-Length", String.valueOf(pdfFile.length()));
            response.setHeader("Content-Disposition", "attachment;filename=" + uuid + ".pdf");
            String encoding = new GetFileCharset().guestFileEncoding(pdfFile);
            if (org.apache.commons.lang.StringUtils.isNotEmpty(encoding)) {
                response.setCharacterEncoding(encoding);
            }

            //?????????????????????????????????
            bis = new BufferedInputStream(new FileInputStream(pdfPath));
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            log.error("<==== WmsOutsourceManagePlatformRepositoryImpl.outsourceCreatePdf.outputPDFFile Error", e);
            throw new MtException("Exception", e.getMessage());
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                log.error("<==== WmsOutsourceManagePlatformRepositoryImpl.outsourceCreatePdf.closeIO Error", e);
            }
        }

        //??????????????????
        for (File file : qrcodeImageFileList
        ) {
            if (!file.delete()) {
                log.info("<==== WmsOutsourceManagePlatformRepositoryImpl.outsourceCreatePdf.qrcodeImageFileList Failed: {}", barcodePath);
            }
        }
        if (!pdfFile.delete()) {
            log.info("<==== WmsOutsourceManagePlatformRepositoryImpl.outsourceCreatePdf.pdfFile Failed: {}", barcodePath);
        }
    }

    @Override
    @ProcessLovValue
    public List<WmsOutsourceExportVO> inventoryExcelExport(Long tenantId, WmsOutsourceOrderHeadVO headVO) {
        List<LovValueDTO> lovList = wmsCommonApiService.queryLovValueList(tenantId, "WMS.OUTSOURCING_DOC_TYPE", null);
        if (CollectionUtils.isEmpty(lovList)) {
            throw new MtException("WMS_OUTSOURCE_PLATFORM_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_OUTSOURCE_PLATFORM_0002", "WMS"));
        }
        List<String> docTypeList = lovList.stream().map(LovValueDTO::getValue).distinct().collect(toList());
        if (CollectionUtils.isEmpty(docTypeList)) {
            throw new MtException("WMS_OUTSOURCE_PLATFORM_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_OUTSOURCE_PLATFORM_0002", "WMS"));
        }
        if (StringUtils.isNotEmpty(headVO.getMaterialId()) || StringUtils.isNotEmpty(headVO.getMaterialVersion())) {
            headVO.setLineQueryFlag(WmsConstant.CONSTANT_Y);
        }
        headVO.setDocTypeList(docTypeList);
        headVO.setTenantId(tenantId);
        return mapper.queryInventoryExportInfo(tenantId, headVO);
    }


}
