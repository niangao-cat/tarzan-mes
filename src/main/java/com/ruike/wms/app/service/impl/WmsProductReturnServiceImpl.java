package com.ruike.wms.app.service.impl;

import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.itf.api.dto.ItfSoDeliveryChanOrPostDTO;
import com.ruike.itf.domain.repository.ItfSoDeliveryChanOrPostIfaceRepository;
import com.ruike.wms.api.dto.WmsMaterialDocReceviceDto;
import com.ruike.wms.api.dto.WmsTransactionTypeDTO;
import com.ruike.wms.app.service.WmsProductReturnService;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.*;
import com.ruike.wms.infra.constant.WmsConstant;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.*;
import javassist.expr.NewArray;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.actual.domain.entity.MtInstructionActualDetail;
import tarzan.actual.domain.repository.MtInstructionActualDetailRepository;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.actual.domain.vo.*;
import tarzan.actual.infra.mapper.MtInstructionActualDetailMapper;
import tarzan.actual.infra.mapper.MtInstructionActualMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.repository.MtSitePlantReleationRepository;
import tarzan.instruction.api.dto.MtInstructionDocDTO2;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.*;
import tarzan.instruction.infra.mapper.MtInstructionMapper;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.inventory.infra.mapper.MtMaterialLotMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.modeling.domain.entity.MtCustomer;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtCustomerRepository;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


/**
 * ????????????????????????????????????
 *
 * @author li.zhang13@hand-china.com 2021/07/07 14:45
 */
@Service
public class WmsProductReturnServiceImpl implements WmsProductReturnService {

    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtSitePlantReleationRepository mtSitePlantReleationRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtCustomerRepository mtCustomerRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtInstructionActualDetailRepository mtInstructionActualDetailRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtInstructionActualRepository mtInstructionActualRepository;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;
    @Autowired
    private WmsTransactionTypeRepository wmsTransactionTypeRepository;
    @Autowired
    private MtInstructionRepository mtInstructionRepository;
    @Autowired
    private MtInstructionActualDetailMapper mtInstructionActualDetailMapper;
    @Autowired
    private MtInstructionMapper mtInstructionMapper;
    @Autowired
    private MtMaterialLotMapper mtMaterialLotMapper;
    @Autowired
    private MtInstructionActualMapper mtInstructionActualMapper;
    @Autowired
    private WmsProductReturnService wmsProductReturnService;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private ItfSoDeliveryChanOrPostIfaceRepository itfSoDeliveryChanOrPostIfaceRepository;

    /**
     * ???????????????????????????
     *
     * @param tenantId          ??????
     * @param instructionDocNum ?????????
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductReturnVO>
     * @author li.zhang13@hand-china.com 2021/07/07 15:00:00
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @ProcessLovValue
    public WmsProductReturnVO docScan(Long tenantId, String instructionDocNum) {
        //????????????
        WmsProductReturnVO wmsProductReturnVO = new WmsProductReturnVO();
        List<WmsProductReturnVO2> wmsProductReturnVO2List = new ArrayList<>();
        if(instructionDocNum.contains("@")){
            instructionDocNum = instructionDocNum.replaceAll("@00","");
        }
        //??????API[propertyLimitInstructionDocQuery]?????????????????????????????????ID
        MtInstructionDocVO4 mtInstructionDocVO4 = new MtInstructionDocVO4();
        mtInstructionDocVO4.setInstructionDocNum(instructionDocNum);
        List<String> instructionDocIdList = mtInstructionDocRepository.propertyLimitInstructionDocQuery(tenantId,mtInstructionDocVO4);
        //???????????????????????????
        if (CollectionUtils.isEmpty(instructionDocIdList)) {
            throw new MtException("WX_WMS_PRODUCT_RETURN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX_WMS_PRODUCT_RETURN_001", "WMS", instructionDocNum));
        }
        //??????API[instructionDocPropertyGet]????????????????????????
        MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.instructionDocPropertyGet(tenantId, instructionDocIdList.get(0));
        wmsProductReturnVO.setInstructionDocId(mtInstructionDoc.getInstructionDocId());
        wmsProductReturnVO.setInstructionDocNum(instructionDocNum);
        //???????????????????????????????????????
        if (!mtInstructionDoc.getInstructionDocType().equals("SALES_RETURN")) {
            throw new MtException("WX_WMS_PRODUCT_RETURN_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX_WMS_PRODUCT_RETURN_002", "WMS"));
        }
        //??????????????????
        if (!mtInstructionDoc.getInstructionDocStatus().equals("RELEASED") && !mtInstructionDoc.getInstructionDocStatus().equals("EXECUTE")) {
            throw new MtException("WX_WMS_PRODUCT_RETURN_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX_WMS_PRODUCT_RETURN_003", "WMS",instructionDocNum));
        }
        //??????????????????
        wmsProductReturnVO.setSiteId(mtInstructionDoc.getSiteId());
//        MtSitePlantReleation mtSitePlantReleation = new MtSitePlantReleation();
//        mtSitePlantReleation.setTenantId(tenantId);
//        mtSitePlantReleation.setSiteId(mtInstructionDoc.getSiteId());
        MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, mtInstructionDoc.getSiteId());
        wmsProductReturnVO.setPlantCode(mtModSite.getSiteName());
        //?????????????????????????????????????????????
        wmsProductReturnVO.setDocTypeId(mtInstructionDoc.getInstructionDocType());
        String docType = lovAdapter.queryLovMeaning("WX.WMS.SO_DOC_TYPE", tenantId, mtInstructionDoc.getInstructionDocType());
        wmsProductReturnVO.setDocType(docType);
        //???????????????
        wmsProductReturnVO.setDocNumber(instructionDocNum);
        //?????????????????????????????????????????????
        wmsProductReturnVO.setDocStatusId(mtInstructionDoc.getInstructionDocStatus());
        String docStatus = lovAdapter.queryLovMeaning("WX.WMS.SO_DELIVERY_STATUS", tenantId, mtInstructionDoc.getInstructionDocStatus());
        wmsProductReturnVO.setDocStatus(docStatus);
        //????????????id??????????????????
        MtCustomer mtCustomer = mtCustomerRepository.selectByPrimaryKey(mtInstructionDoc.getCustomerId());
        wmsProductReturnVO.setCustomerId(mtInstructionDoc.getCustomerId());
        wmsProductReturnVO.setCustomerName(mtCustomer.getCustomerName());
        //??????API[instructionDocLimitInstructionAndActualQuery]????????????????????????
        MtInstructionDocVO5 mtInstructionDocVO5 =  mtInstructionDocRepository.instructionDocLimitInstructionAndActualQuery(tenantId,instructionDocIdList.get(0));
        List<MtInstructionVO2> mtInstructionVO2List = mtInstructionDocVO5.getInstructionMessageList();
        if(CollectionUtils.isEmpty(mtInstructionVO2List)){
            return wmsProductReturnVO;
        }
        for(MtInstructionVO2 mtInstructionVO2: mtInstructionVO2List){
            WmsProductReturnVO2 wmsProductReturnVO2 = new WmsProductReturnVO2();
            wmsProductReturnVO2.setInstructionId(mtInstructionVO2.getInstructionId());
            //??????API[materialPropertyGet]??????????????????
            MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId,mtInstructionVO2.getMaterialId());
            wmsProductReturnVO2.setMaterialId(mtInstructionVO2.getMaterialId());
            wmsProductReturnVO2.setMaterialCode(mtMaterialVO.getMaterialCode());
            wmsProductReturnVO2.setMaterialName(mtMaterialVO.getMaterialName());
            //???????????????????????????
            String materialVersion = mtInstructionMapper.selectAttrValue(tenantId,"MATERIAL_VERSION",mtInstructionVO2.getInstructionId());
            String soLineNum = mtInstructionMapper.selectAttrValue(tenantId,"SO_LINE_NUM",mtInstructionVO2.getInstructionId());
            String soNum = mtInstructionMapper.selectAttrValue(tenantId,"SO_NUM",mtInstructionVO2.getInstructionId());
            if(StringUtils.isBlank(materialVersion)){
                wmsProductReturnVO2.setMaterialVersion("");
            }else{
                wmsProductReturnVO2.setMaterialVersion(materialVersion);
            }
            if(StringUtils.isBlank(soLineNum)){
                wmsProductReturnVO2.setSalesOrderLineNum("");
            }else{
                wmsProductReturnVO2.setSalesOrderLineNum(soLineNum);
            }
            if(StringUtils.isBlank(soNum)){
                wmsProductReturnVO2.setSalesOrderNum("");
            }else{
                wmsProductReturnVO2.setSalesOrderNum(soNum);
            }
            //??????????????????
            wmsProductReturnVO2.setDemondQty((mtInstructionVO2.getQuantity()).equals(Double.NaN) ? 0D:mtInstructionVO2.getQuantity());
            if(CollectionUtils.isEmpty(mtInstructionVO2.getInstructionActualMessageList())){
                wmsProductReturnVO2.setActualQty(0d);
                wmsProductReturnVO2.setSerialAccount(0d);
            }else{
                wmsProductReturnVO2.setActualQty((mtInstructionVO2.getInstructionActualMessageList().get(0).getActualQty()).equals(Double.NaN) ? 0D:(mtInstructionVO2.getInstructionActualMessageList().get(0).getActualQty()));
                MtInstructionActualDetail mtInstructionActualDetail = new MtInstructionActualDetail();
                mtInstructionActualDetail.setTenantId(tenantId);
                mtInstructionActualDetail.setActualId((mtInstructionVO2.getInstructionActualMessageList().get(0).getActualId()));
                List<MtInstructionActualDetail> mtInstructionActualDetailList = mtInstructionActualDetailRepository.select(mtInstructionActualDetail);
                List<MtInstructionActualDetail> mtInstructionActualDetailList1 = mtInstructionActualDetailList.stream()
                        .collect(Collectors.collectingAndThen(Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(MtInstructionActualDetail::getMaterialLotId))),ArrayList::new));
                wmsProductReturnVO2.setSerialAccount(Double.valueOf(mtInstructionActualDetailList1.size()));
            }
            wmsProductReturnVO2.setScanQty(0D);
            wmsProductReturnVO2.setScanSerialAccount(0D);
            //??????????????????
            wmsProductReturnVO2.setToLocatorId(mtInstructionVO2.getToLocatorId());
            wmsProductReturnVO2.setToLocator(mtModLocatorRepository.selectByPrimaryKey(mtInstructionVO2.getToLocatorId()).getLocatorCode());
            //????????????????????????????????????????????????
            wmsProductReturnVO2.setSalesOrderLineStatusId(mtInstructionVO2.getInstructionStatus());
            String lineStatus = lovAdapter.queryLovMeaning("WX.WMS.SO_DELIVERY_LINE_STATUS", tenantId, mtInstructionVO2.getInstructionStatus());
            wmsProductReturnVO2.setSalesOrderLineStatus(lineStatus);
            wmsProductReturnVO2.setUomId(mtInstructionVO2.getUomId());
            wmsProductReturnVO2.setToSiteId(mtInstructionVO2.getToSiteId());
            wmsProductReturnVO2List.add(wmsProductReturnVO2);
        }
        List<WmsProductReturnVO2> wmsProductReturnVO2List1 = wmsProductReturnVO2List.stream().filter(item ->!"CANCEL".equals(item.getSalesOrderLineStatusId())).collect(Collectors.toList());
        wmsProductReturnVO.setInstructionList(wmsProductReturnVO2List1);
        return wmsProductReturnVO;
    }

    /**
     * ?????????????????????
     *
     * @param tenantId          ??????
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductReturnVO>
     * @author li.zhang13@hand-china.com 2021/07/07 15:00:00
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WmsMaterialDocReturnVO materialDocScan(Long tenantId, String materialLotCode, String instructionDocId, List<WmsProductReturnVO2> instructionList, List<WmsProductReturnVO2> instructionselectedList, String locatorId, WmsMaterialDocReturnVO vo) {
        //????????????
        WmsMaterialDocReturnVO wmsMaterialDocReturnVO = new WmsMaterialDocReturnVO();
        //???????????????????????????????????????????????????????????????????????????
        if(vo != null){
            //????????????????????????????????????
            //?????????????????????????????????
            List<MtInstructionActual> mtInstructionActualList = mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId,vo.getWmsProductReturnVO2().getInstructionId());
            //?????????????????????????????????????????????
            //????????????????????????
            MtInstructionActualDetail mtInstructionActualDetail = new MtInstructionActualDetail();
            mtInstructionActualDetail.setTenantId(tenantId);
            mtInstructionActualDetail.setActualId(mtInstructionActualList.get(0).getActualId());
            mtInstructionActualDetail.setMaterialLotId(vo.getMaterialLotId());
            List<MtInstructionActualDetail> mtInstructionActualDetails = mtInstructionActualDetailRepository.select(mtInstructionActualDetail);
            //????????????????????????????????????
            if(CollectionUtils.isNotEmpty(mtInstructionActualDetails) && StringUtils.isBlank(mtInstructionActualDetails.get(0).getToLocatorId())){
                wmsMaterialDocReturnVO.setMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WX.WMS_PRODUCTION_0020", "WMS",vo.getMaterialLotCode(),"??????"));
                wmsMaterialDocReturnVO.setMaterialLotCode(vo.getMaterialLotCode());
                return wmsMaterialDocReturnVO;
            }
        }
        MtMaterialLotVO21 mtMaterialLotVO21 = new MtMaterialLotVO21();
        mtMaterialLotVO21.setMaterialLotCode(materialLotCode);
        //??????api[propertyLimitMaterialLotPropertyQuery]?????????????????????
        List<MtMaterialLotVO22> mtMaterialLotVO22List = mtMaterialLotRepository.propertyLimitMaterialLotPropertyQuery(tenantId,mtMaterialLotVO21);
        //???????????????????????????
        if (CollectionUtils.isEmpty(mtMaterialLotVO22List)) {
            throw new MtException("WX_WMS_PRODUCT_RETURN_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX_WMS_PRODUCT_RETURN_008", "WMS", materialLotCode));
        }
        if (!mtMaterialLotVO22List.get(0).getEnableFlag().equals("N")) {
            throw new MtException("WX_WMS_PRODUCT_RETURN_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX_WMS_PRODUCT_RETURN_009", "WMS", materialLotCode));
        }
        //?????????????????????????????????1
//        if(mtMaterialLotVO22List.get(0).getPrimaryUomQty()<1){
//            throw new MtException("WX_WMS_PRODUCT_RETURN_017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "WX_WMS_PRODUCT_RETURN_017", "WMS", materialLotCode));
//        }
        //?????????????????????
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, mtMaterialLotVO22List.get(0).getMaterialLotId());
        //???????????????????????????
        String materialLotVersion = mtMaterialLotMapper.selectAttrValue(tenantId,"MATERIAL_VERSION",mtMaterialLotVO22List.get(0).getMaterialLotId());
        String materialLotStatus = mtMaterialLotMapper.selectAttrValue(tenantId,"STATUS",mtMaterialLotVO22List.get(0).getMaterialLotId());
        //??????????????????????????????????????????
        if (materialLotStatus.equals("SCANNED")) {
            throw new MtException("WX_WMS_PRODUCT_RETURN_015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX_WMS_PRODUCT_RETURN_015", "WMS", materialLotCode));
        }
        //???????????????????????????
        WmsProductReturnVO2 wmsProductReturnVO2 = new WmsProductReturnVO2();
        WmsProductReturnVO2 wmsProductReturnVO21 = new WmsProductReturnVO2();
        if(CollectionUtils.isEmpty(instructionselectedList)){
            //???????????????id??????
            MtInstructionVO10 mtInstructionVO10 = new MtInstructionVO10();
            mtInstructionVO10.setSourceDocId(instructionDocId);
            List<String> instructionIdList = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstructionVO10);
            List<String> idList = new ArrayList<>();
            List<String> idList1 = new ArrayList<>();
            List<String> idList3 = new ArrayList<>();
            List<String> idList4 = new ArrayList<>();
            List<String> idList5 = new ArrayList<>();
            //??????sn
            for(String id:instructionIdList){
                String sn = mtInstructionMapper.selectAttrValue(tenantId,"SN" ,id);
                if(!StringUtils.isBlank(sn) && sn.equals(materialLotCode)){
                    idList.add(id);
                }
                if(StringUtils.isNotBlank(sn)){
                    idList5.add(id);
                }
            }
            List<WmsProductReturnVO2> wmsProductReturnVO2List = new ArrayList<>();
            //?????????????????????sn
            if(idList.size()==1){
                wmsProductReturnVO2List = instructionList.stream().filter(item ->item.getInstructionId().equals(idList.get(0))).collect(Collectors.toList());
            }else if(idList.size() > 1){
                //?????????????????????sn
                for(String a:idList){
                    wmsProductReturnVO21 = instructionList.stream().filter(item ->item.getInstructionId().equals(idList.get(0))).collect(Collectors.toList()).get(0);
                    wmsProductReturnVO2List.add(wmsProductReturnVO21);
                }
            }else if(idList.size()==0){
                wmsProductReturnVO2List = instructionList;
            }
            //????????????Id
            List<WmsProductReturnVO2> wmsProductReturnVO2s = wmsProductReturnVO2List.stream()
                    .filter(item ->item.getMaterialId().equals(mtMaterialLot.getMaterialId()))
                    .collect(Collectors.toList());
            //??????????????????
            MtMaterial mtMaterial = mtMaterialRepository.materialPropertyGet(tenantId,mtMaterialLot.getMaterialId());
            if(CollectionUtils.isEmpty(wmsProductReturnVO2s)){
                throw new MtException("WX_WMS_PRODUCT_RETURN_019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WX_WMS_PRODUCT_RETURN_019", "WMS",materialLotCode,mtMaterial.getMaterialCode()));
            }else{
                //????????????
                //?????????????????????????????????????????????
                MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
                mtExtendVO1.setTableName("mt_instruction_attr");
                mtExtendVO1.setKeyIdList(wmsProductReturnVO2s.stream().map(WmsProductReturnVO2::getInstructionId).collect(Collectors.toList()));
                List<MtExtendVO5> attrs = new ArrayList<>();
                MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                mtExtendVO5.setAttrName("MATERIAL_VERSION");
                attrs.add(mtExtendVO5);
                mtExtendVO1.setAttrs(attrs);
                List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId,mtExtendVO1);
                idList3.addAll(mtExtendVO1.getKeyIdList());
                if(StringUtils.isBlank(materialLotVersion)){
                    //???????????????????????????????????????????????????
                    for(MtExtendAttrVO1 mtExtendAttrVO1:mtExtendAttrVO1s){
                        if(StringUtils.isNotBlank(mtExtendAttrVO1.getAttrValue())){
                            idList3.remove(mtExtendAttrVO1.getKeyId());
                        }
                    }
                    if(idList3.size()==0){
                        //?????????????????????
                        throw new MtException("WX_WMS_PRODUCT_RETURN_020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WX_WMS_PRODUCT_RETURN_020", "WMS",materialLotCode,materialLotVersion));
                    }else if(idList3.size()==1){
                        //???????????????
                        if(idList.size() == 0){
                            if(idList5.contains(idList3.get(0))){
                                throw new MtException("WX_WMS_SO_DELIVERY_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "WX_WMS_SO_DELIVERY_0010", "WMS"));
                            }
                        }
                        wmsProductReturnVO2 = instructionList.stream().filter(item ->item.getInstructionId().equals(idList3.get(0))).collect(Collectors.toList()).get(0);
                    }else{
                        //?????????????????????
                        //???????????????????????????COMPLETE
                        for(String id:idList3){
                            WmsProductReturnVO2 wmsProductReturnVO22 = instructionList.stream().filter(item ->item.getInstructionId().equals(id)).collect(Collectors.toList()).get(0);
                            if(!wmsProductReturnVO22.getSalesOrderLineStatusId().equals("COMPLETE")){
                                idList4.add(wmsProductReturnVO22.getInstructionId());
                            }
                        }
                        if(idList4.size() ==0){
                            //????????????
                            throw new MtException("WX_WMS_PRODUCT_RETURN_020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WX_WMS_PRODUCT_RETURN_020", "WMS",materialLotCode,materialLotVersion));
                        }else if(idList4.size() ==1){
                            //???????????????
                            if(idList.size() == 0){
                                if(idList5.contains(idList4.get(0))){
                                    throw new MtException("WX_WMS_SO_DELIVERY_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "WX_WMS_SO_DELIVERY_0010", "WMS"));
                                }
                            }
                            wmsProductReturnVO2 = instructionList.stream().filter(item ->item.getInstructionId().equals(idList4.get(0))).collect(Collectors.toList()).get(0);
                        }else{
                            if(idList.size() == 0){
                                //?????????????????????sn??????
                                List<String> idList6 = idList4.stream().filter(item->!idList5.contains(item)).collect(Collectors.toList());
                                if(idList6.size() == 0){
                                    throw new MtException("WX_WMS_SO_DELIVERY_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "WX_WMS_SO_DELIVERY_0010", "WMS"));
                                }else if(idList6.size() == 1){
                                    wmsProductReturnVO2 = instructionList.stream().filter(item ->item.getInstructionId().equals(idList6.get(0))).collect(Collectors.toList()).get(0);
                                }else{
                                    wmsMaterialDocReturnVO.setIdList(idList6);
                                    wmsMaterialDocReturnVO.setMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "WX_WMS_PRODUCT_RETURN_005", "WMS", materialLotCode));
                                    wmsMaterialDocReturnVO.setInstructionList(instructionList);
                                    return wmsMaterialDocReturnVO;
                                }
                            }else{
                                wmsMaterialDocReturnVO.setIdList(idList4);
                                wmsMaterialDocReturnVO.setMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "WX_WMS_PRODUCT_RETURN_005", "WMS", materialLotCode));
                                wmsMaterialDocReturnVO.setInstructionList(instructionList);
                                return wmsMaterialDocReturnVO;
                            }
                        }
                    }
                }else{
                    //????????????????????????
                    for(MtExtendAttrVO1 mtExtendAttrVO1:mtExtendAttrVO1s){
                        if(materialLotVersion.equals(mtExtendAttrVO1.getAttrValue())){
                            idList1.add(mtExtendAttrVO1.getKeyId());
                        }
                    }
                    if(idList1.size() > 1){
                        //?????????????????????
                        //???????????????????????????COMPLETE
                        for(String id:idList1){
                            WmsProductReturnVO2 wmsProductReturnVO22 = instructionList.stream().filter(item ->item.getInstructionId().equals(id)).collect(Collectors.toList()).get(0);
                            if(!wmsProductReturnVO22.getSalesOrderLineStatusId().equals("COMPLETE")){
                                idList4.add(wmsProductReturnVO22.getInstructionId());
                            }
                        }
                        if(idList4.size() ==0){
                            //????????????
                            throw new MtException("WX_WMS_PRODUCT_RETURN_020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WX_WMS_PRODUCT_RETURN_020", "WMS",materialLotCode,materialLotVersion));
                        }else if(idList4.size() ==1){
                            //???????????????
                            if(idList.size() == 0){
                                if(idList5.contains(idList3.get(0))){
                                    throw new MtException("WX_WMS_SO_DELIVERY_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "WX_WMS_SO_DELIVERY_0010", "WMS"));
                                }
                            }
                            wmsProductReturnVO2 = instructionList.stream().filter(item ->item.getInstructionId().equals(idList4.get(0))).collect(Collectors.toList()).get(0);
                        }else{
                            if(idList.size() == 0){
                                //?????????????????????sn??????
                                List<String> idList6 = idList4.stream().filter(item->!idList5.contains(item)).collect(Collectors.toList());
                                if(idList6.size() == 0){
                                    throw new MtException("WX_WMS_SO_DELIVERY_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "WX_WMS_SO_DELIVERY_0010", "WMS"));
                                }else if(idList6.size() == 1){
                                    wmsProductReturnVO2 = instructionList.stream().filter(item ->item.getInstructionId().equals(idList6.get(0))).collect(Collectors.toList()).get(0);
                                }else{
                                    wmsMaterialDocReturnVO.setIdList(idList6);
                                    wmsMaterialDocReturnVO.setMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "WX_WMS_PRODUCT_RETURN_005", "WMS", materialLotCode));
                                    wmsMaterialDocReturnVO.setInstructionList(instructionList);
                                    return wmsMaterialDocReturnVO;
                                }
                            }else{
                                wmsMaterialDocReturnVO.setIdList(idList4);
                                wmsMaterialDocReturnVO.setMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "WX_WMS_PRODUCT_RETURN_005", "WMS", materialLotCode));
                                wmsMaterialDocReturnVO.setInstructionList(instructionList);
                                return wmsMaterialDocReturnVO;
                            }
                        }
                    }else if(idList1.size() == 1){
                        //???????????????
                        if(idList.size() == 0){
                            if(idList5.contains(idList1.get(0))){
                                throw new MtException("WX_WMS_SO_DELIVERY_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "WX_WMS_SO_DELIVERY_0010", "WMS"));
                            }
                        }
                        wmsProductReturnVO2 = instructionList.stream().filter(item ->item.getInstructionId().equals(idList1.get(0))).collect(Collectors.toList()).get(0);
                    }else{
                        //??????????????????????????????????????????
                        for(MtExtendAttrVO1 mtExtendAttrVO1:mtExtendAttrVO1s){
                            if(StringUtils.isNotBlank(mtExtendAttrVO1.getAttrValue())){
                                idList3.remove(mtExtendAttrVO1.getKeyId());
                            }
                        }
                        if(idList3.size()==0){
                            //?????????????????????
                            throw new MtException("WX_WMS_PRODUCT_RETURN_020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WX_WMS_PRODUCT_RETURN_020", "WMS",materialLotCode,materialLotVersion));
                        }else if(idList3.size()==1){
                            //???????????????
                            if(idList.size() == 0){
                                if(idList5.contains(idList3.get(0))){
                                    throw new MtException("WX_WMS_SO_DELIVERY_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "WX_WMS_SO_DELIVERY_0010", "WMS"));
                                }
                            }
                            wmsProductReturnVO2 = instructionList.stream().filter(item ->item.getInstructionId().equals(idList3.get(0))).collect(Collectors.toList()).get(0);
                        }else{
                            //?????????????????????
                            //???????????????????????????COMPLETE
                            for(String id:idList3){
                                WmsProductReturnVO2 wmsProductReturnVO22 = instructionList.stream().filter(item ->item.getInstructionId().equals(id)).collect(Collectors.toList()).get(0);
                                if(!wmsProductReturnVO22.getSalesOrderLineStatusId().equals("COMPLETE")){
                                    idList4.add(wmsProductReturnVO22.getInstructionId());
                                }
                            }
                            if(idList4.size() ==0){
                                //?????????????????????
                                throw new MtException("WX_WMS_PRODUCT_RETURN_020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "WX_WMS_PRODUCT_RETURN_020", "WMS",materialLotCode,materialLotVersion));
                            }else if(idList4.size() ==1){
                                //???????????????
                                if(idList.size() == 0){
                                    if(idList5.contains(idList4.get(0))){
                                        throw new MtException("WX_WMS_SO_DELIVERY_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                "WX_WMS_SO_DELIVERY_0010", "WMS"));
                                    }
                                }
                                wmsProductReturnVO2 = instructionList.stream().filter(item ->item.getInstructionId().equals(idList4.get(0))).collect(Collectors.toList()).get(0);
                            }else{
                                if(idList.size() == 0){
                                    //?????????????????????sn??????
                                    List<String> idList6 = idList4.stream().filter(item->!idList5.contains(item)).collect(Collectors.toList());
                                    if(idList6.size() == 0){
                                        throw new MtException("WX_WMS_SO_DELIVERY_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                "WX_WMS_SO_DELIVERY_0010", "WMS"));
                                    }else if(idList6.size() == 1){
                                        wmsProductReturnVO2 = instructionList.stream().filter(item ->item.getInstructionId().equals(idList6.get(0))).collect(Collectors.toList()).get(0);
                                    }else{
                                        wmsMaterialDocReturnVO.setIdList(idList6);
                                        wmsMaterialDocReturnVO.setMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                "WX_WMS_PRODUCT_RETURN_005", "WMS", materialLotCode));
                                        wmsMaterialDocReturnVO.setInstructionList(instructionList);
                                        return wmsMaterialDocReturnVO;
                                    }
                                }else{
                                    wmsMaterialDocReturnVO.setIdList(idList4);
                                    wmsMaterialDocReturnVO.setMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "WX_WMS_PRODUCT_RETURN_005", "WMS", materialLotCode));
                                    wmsMaterialDocReturnVO.setInstructionList(instructionList);
                                    return wmsMaterialDocReturnVO;
                                }
                            }
                        }
                    }
                }
            }
            //?????????????????????????????????????????????sn

        }else{
            wmsProductReturnVO2 = instructionselectedList.get(0);
        }
        //????????????????????????????????????+1????????????????????????
        if(wmsProductReturnVO2.getActualQty()+1>wmsProductReturnVO2.getDemondQty()){
            throw new MtException("WX_WMS_PRODUCT_RETURN_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX_WMS_PRODUCT_RETURN_016", "WMS"));
        }
        //??????API[instructionDocPropertyGet]????????????????????????
        MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.instructionDocPropertyGet(tenantId, instructionDocId);
        wmsMaterialDocReturnVO.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        wmsMaterialDocReturnVO.setMaterialLotCode(materialLotCode);
        wmsMaterialDocReturnVO.setLotMaterialId(mtMaterialLot.getMaterialId());
        //??????????????????
        MtMaterial mtMaterial = mtMaterialRepository.materialPropertyGet(tenantId,mtMaterialLot.getMaterialId());
        wmsMaterialDocReturnVO.setLotMaterialCode(mtMaterial.getMaterialCode());
        wmsMaterialDocReturnVO.setLotMaterialVersion(materialLotVersion);
        wmsMaterialDocReturnVO.setLotMaterialQty(1d);
        //????????????
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("SALES_RETURN");
        String eventId = mtEventRepository.eventCreate(tenantId,mtEventCreateVO);
        //??????????????????
        MtInstructionActualVO mtInstructionActualVO = new MtInstructionActualVO();
        mtInstructionActualVO.setInstructionId(wmsProductReturnVO2.getInstructionId());
        //?????????????????????????????????
        List<MtInstructionActual> mtInstructionActualList = mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId,wmsProductReturnVO2.getInstructionId());
        mtInstructionActualVO.setActualQty(1d);
        mtInstructionActualVO.setEventId(eventId);
        mtInstructionActualVO.setInstructionType("RETURN_FROM_CUSTOMER");
        mtInstructionActualVO.setMaterialId(wmsProductReturnVO2.getMaterialId());
        mtInstructionActualVO.setUomId(mtMaterialLot.getPrimaryUomId());
        mtInstructionActualVO.setToSiteId(wmsProductReturnVO2.getToSiteId());
        mtInstructionActualVO.setToLocatorId(wmsProductReturnVO2.getToLocatorId());
        mtInstructionActualVO.setCustomerId(mtInstructionDoc.getCustomerId());
        mtInstructionActualVO.setCustomerSiteId(mtInstructionDoc.getCustomerSiteId());
        MtInstructionActualVO1 mtInstructionActualVO1 = mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionActualVO);
        //??????????????????
        MtInstructionActualDetail mtInstructionActualDetail = new MtInstructionActualDetail();
        mtInstructionActualDetail.setActualId(mtInstructionActualVO1.getActualId());
        mtInstructionActualDetail.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        mtInstructionActualDetail.setUomId(mtMaterialLot.getPrimaryUomId());
        mtInstructionActualDetail.setActualQty(1d);
        if(!StringUtils.isBlank(locatorId)){
            mtInstructionActualDetail.setToLocatorId(locatorId);
        }
        mtInstructionActualDetailRepository.instructionActualDetailCreate(tenantId, mtInstructionActualDetail);
        //????????????
        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        mtMaterialLotVO2.setMaterialLotCode(materialLotCode);
        mtMaterialLotVO2.setEventId(eventId);
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, HmeConstants.ConstantValue.NO);
        //????????????????????????
        MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
        mtExtendVO10.setKeyId(mtMaterialLot.getMaterialLotId());
        mtExtendVO10.setEventId(eventId);
        List<MtExtendVO5> attrs = new ArrayList<>();
        MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("STATUS");
        mtExtendVO5.setAttrValue("SCANNED");
        attrs.add(mtExtendVO5);
        mtExtendVO10.setAttrs(attrs);
        mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);
        //??????????????????
        MtInstructionVO mtInstructionVO = new MtInstructionVO();
        mtInstructionVO.setInstructionId(wmsProductReturnVO2.getInstructionId());
        mtInstructionVO.setEventId(eventId);
        String instructionStatus = "";
        if(CollectionUtils.isEmpty(mtInstructionActualList)){
            if(wmsMaterialDocReturnVO.getLotMaterialQty() == 0){
                instructionStatus = "RELEASED";
            }else if(wmsMaterialDocReturnVO.getLotMaterialQty() < wmsProductReturnVO2.getDemondQty()){
                instructionStatus = "EXECUTE";
            }else if(wmsMaterialDocReturnVO.getLotMaterialQty().equals(wmsProductReturnVO2.getDemondQty())){
                instructionStatus = "COMPLETE";
            }
        }else{
            if(mtInstructionActualList.get(0).getActualQty()+1d == 0){
                instructionStatus = "RELEASED";
            }else if(mtInstructionActualList.get(0).getActualQty()+1d < wmsProductReturnVO2.getDemondQty()){
                instructionStatus = "EXECUTE";
            }else if(mtInstructionActualList.get(0).getActualQty()+1d == wmsProductReturnVO2.getDemondQty()){
                instructionStatus = "COMPLETE";
            }
        }
        String instructionStatusMeaning = lovAdapter.queryLovMeaning("WX.WMS.SO_DELIVERY_LINE_STATUS", tenantId, instructionStatus);
        mtInstructionVO.setInstructionStatus(instructionStatus);
        mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, WmsConstant.CONSTANT_N);
        //?????????????????????
        WmsProductReturnVO2 finalWmsProductReturnVO = wmsProductReturnVO2;
        Double actualQty = instructionList.stream().filter(item ->
                item.getInstructionId().equals(finalWmsProductReturnVO.getInstructionId())).collect(Collectors.toList()).get(0)
                .getActualQty()+1;
        Double serialAccount = instructionList.stream().filter(item ->
                item.getInstructionId().equals(finalWmsProductReturnVO.getInstructionId())).collect(Collectors.toList()).get(0)
                .getSerialAccount()+1;
        instructionList.stream().filter(item ->
                item.getInstructionId().equals(finalWmsProductReturnVO.getInstructionId())).collect(Collectors.toList()).get(0)
                .setActualQty(actualQty);
        instructionList.stream().filter(item ->
                item.getInstructionId().equals(finalWmsProductReturnVO.getInstructionId())).collect(Collectors.toList()).get(0)
                .setSerialAccount(serialAccount);
        instructionList.stream().filter(item ->
                item.getInstructionId().equals(finalWmsProductReturnVO.getInstructionId())).collect(Collectors.toList()).get(0)
                .setSalesOrderLineStatusId(instructionStatus);
        instructionList.stream().filter(item ->
                item.getInstructionId().equals(finalWmsProductReturnVO.getInstructionId())).collect(Collectors.toList()).get(0)
                .setSalesOrderLineStatus(instructionStatusMeaning);
        wmsMaterialDocReturnVO.setInstructionList(instructionList);
        wmsProductReturnVO2.setActualQty(actualQty);
        wmsProductReturnVO2.setSerialAccount(serialAccount);
        wmsProductReturnVO2.setSalesOrderLineStatusId(instructionStatus);
        wmsProductReturnVO2.setSalesOrderLineStatus(instructionStatusMeaning);
        wmsMaterialDocReturnVO.setWmsProductReturnVO2(wmsProductReturnVO2);
        return wmsMaterialDocReturnVO;
    }

    /**
     * ??????????????????
     *
     * @param tenantId          ??????
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductReturnVO>
     * @author li.zhang13@hand-china.com 2021/07/07 15:00:00
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WmsQtyChangeVO qtyChange(Long tenantId, Double changeQty, WmsMaterialDocReturnVO wmsMaterialDocReturnVO){
        WmsQtyChangeVO wmsQtyChangeVO = new WmsQtyChangeVO();
        if(wmsMaterialDocReturnVO.getWmsProductReturnVO2().getActualQty()-wmsMaterialDocReturnVO.getLotMaterialQty()+changeQty>wmsMaterialDocReturnVO.getWmsProductReturnVO2().getDemondQty()){
            throw new MtException("WX_WMS_PRODUCT_RETURN_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX_WMS_PRODUCT_RETURN_016", "WMS"));
        }
        wmsQtyChangeVO.setLotMaterialQty(changeQty);
        //?????????????????????????????????
        List<MtInstructionActual> mtInstructionActualList = mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId,wmsMaterialDocReturnVO.getWmsProductReturnVO2().getInstructionId());
        //??????????????????
        mtInstructionActualMapper.updateActualQty(tenantId,
                mtInstructionActualList.get(0).getActualQty()+changeQty-wmsMaterialDocReturnVO.getLotMaterialQty(),
                wmsMaterialDocReturnVO.getWmsProductReturnVO2().getInstructionId());
        //????????????????????????
        mtInstructionActualDetailMapper.updateQty(tenantId,changeQty,
                mtInstructionActualList.get(0).getActualId(),wmsMaterialDocReturnVO.getMaterialLotId());
        //????????????
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("SALES_RETURN");
        String eventId = mtEventRepository.eventCreate(tenantId,mtEventCreateVO);
        //??????????????????
        MtInstructionVO mtInstructionVO = new MtInstructionVO();
        mtInstructionVO.setInstructionId(wmsMaterialDocReturnVO.getWmsProductReturnVO2().getInstructionId());
        mtInstructionVO.setEventId(eventId);
        String instructionStatus = "";
        if(mtInstructionActualList.get(0).getActualQty()+changeQty-wmsMaterialDocReturnVO.getLotMaterialQty()== 0){
            instructionStatus = "RELEASED";
        }else if(mtInstructionActualList.get(0).getActualQty()+changeQty-wmsMaterialDocReturnVO.getLotMaterialQty() < wmsMaterialDocReturnVO.getWmsProductReturnVO2().getDemondQty()){
            instructionStatus = "EXECUTE";
        }else if(mtInstructionActualList.get(0).getActualQty()+changeQty-wmsMaterialDocReturnVO.getLotMaterialQty() == wmsMaterialDocReturnVO.getWmsProductReturnVO2().getDemondQty()){
            instructionStatus = "COMPLETE";
        }
        String instructionStatusMeaning = lovAdapter.queryLovMeaning("WX.WMS.SO_DELIVERY_LINE_STATUS", tenantId, instructionStatus);
        mtInstructionVO.setInstructionStatus(instructionStatus);
        mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, WmsConstant.CONSTANT_N);
        //?????????????????????
        Double actualQty = wmsMaterialDocReturnVO.getInstructionList().stream().filter(item ->
                item.getInstructionId().equals(wmsMaterialDocReturnVO.getWmsProductReturnVO2().getInstructionId())).collect(Collectors.toList()).get(0)
                .getActualQty()+changeQty-wmsMaterialDocReturnVO.getLotMaterialQty();
        wmsMaterialDocReturnVO.getInstructionList().stream().filter(item ->
                item.getInstructionId().equals(wmsMaterialDocReturnVO.getWmsProductReturnVO2().getInstructionId())).collect(Collectors.toList()).get(0)
                .setActualQty(actualQty);
        wmsMaterialDocReturnVO.getInstructionList().stream().filter(item ->
                item.getInstructionId().equals(wmsMaterialDocReturnVO.getWmsProductReturnVO2().getInstructionId())).collect(Collectors.toList()).get(0)
                .setSalesOrderLineStatusId(instructionStatus);
        wmsMaterialDocReturnVO.getInstructionList().stream().filter(item ->
                item.getInstructionId().equals(wmsMaterialDocReturnVO.getWmsProductReturnVO2().getInstructionId())).collect(Collectors.toList()).get(0)
                .setSalesOrderLineStatus(instructionStatusMeaning);
        wmsQtyChangeVO.setInstructionList(wmsMaterialDocReturnVO.getInstructionList());
        return wmsQtyChangeVO;
    }

    /**
     * ??????????????????
     *
     * @param tenantId          ??????
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductReturnVO>
     * @author li.zhang13@hand-china.com 2021/07/07 15:00:00
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WmsLocatorDocReturnVO locatorDocScan(Long tenantId, String locatorCode, WmsMaterialDocReturnVO wmsMaterialDocReturnVO) {
        //????????????
        WmsLocatorDocReturnVO wmsLocatorDocReturnVO = new WmsLocatorDocReturnVO();
        MtModLocator mtModLocator = new MtModLocator();
        mtModLocator.setTenantId(tenantId);
        mtModLocator.setLocatorCode(locatorCode);
        //????????????????????????
        List<MtModLocator> mtModLocatorList = mtModLocatorRepository.select(mtModLocator);
        if(CollectionUtils.isEmpty(mtModLocatorList) || mtModLocatorList.get(0).getEnableFlag().equals("N")){
            throw new MtException("WX_WMS_PRODUCT_RETURN_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX_WMS_PRODUCT_RETURN_006", "WMS", locatorCode));
        }
        if(!mtModLocatorList.get(0).getLocatorType().equals("4")){
            throw new MtException("WX_WMS_PRODUCT_RETURN_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX_WMS_PRODUCT_RETURN_013", "WMS", locatorCode));
        }
        //????????????Id
        List<String> parentLocatorIdS = mtModLocatorRepository.parentLocatorQuery(tenantId, mtModLocatorList.get(0).getLocatorId(), "FIRST");
        if(!parentLocatorIdS.get(0).equals(wmsMaterialDocReturnVO.getWmsProductReturnVO2().getToLocatorId())){
            throw new MtException("WX_WMS_PRODUCT_RETURN_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX_WMS_PRODUCT_RETURN_007", "WMS",locatorCode));
        }
        wmsLocatorDocReturnVO.setLocatorId(mtModLocatorList.get(0).getLocatorId());
        wmsLocatorDocReturnVO.setLocatorName(mtModLocatorList.get(0).getLocatorName());
        wmsLocatorDocReturnVO.setLocatorCode(locatorCode);
        //????????????Id
        List<MtInstructionActual> mtInstructionActualList = mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId,wmsMaterialDocReturnVO.getWmsProductReturnVO2().getInstructionId());
        //???????????????????????????Id
        mtInstructionActualDetailMapper.updateLocatorByActualId(tenantId,mtModLocatorList.get(0).getLocatorId(),
                mtInstructionActualList.get(0).getActualId(),wmsMaterialDocReturnVO.getMaterialLotId());
        return wmsLocatorDocReturnVO;
    }

    /**
     * ????????????
     *
     * @param tenantId          ??????
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductReturnVO>
     * @author li.zhang13@hand-china.com 2021/07/07 15:00:00
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @ProcessLovValue
    public WmsMaterialDocReturnVO2 docDetailQuery(Long tenantId, WmsProductReturnVO2 wmsProductReturnVO2) {
        //????????????
        WmsMaterialDocReturnVO2 wmsMaterialDocReturnVO2 = new WmsMaterialDocReturnVO2();
        List<WmsMaterialLineReturnVO> wmsMaterialLineReturnVOList = new ArrayList<>();
        //????????????Id
        MtInstructionActual mtInstructionActual = new MtInstructionActual();
        mtInstructionActual.setInstructionId(wmsProductReturnVO2.getInstructionId());
        List<String> actualIdList = mtInstructionActualRepository.propertyLimitInstructionActualQuery(tenantId, mtInstructionActual);
        if(CollectionUtils.isEmpty(actualIdList)){
            wmsProductReturnVO2.setActualQty(0d);
            //???????????????
            wmsMaterialDocReturnVO2.setWmsProductReturnVO2(wmsProductReturnVO2);
            return wmsMaterialDocReturnVO2;
        }
        wmsProductReturnVO2.setActualQty(mtInstructionActualRepository.instructionActualPropertyGet(tenantId,actualIdList.get(0)).getActualQty());
        //???????????????
        //????????????????????????
        MtInstructionActualDetail mtInstructionActualDetail = new MtInstructionActualDetail();
        mtInstructionActualDetail.setTenantId(tenantId);
        mtInstructionActualDetail.setActualId(actualIdList.get(0));
        List<MtInstructionActualDetail> mtInstructionActualDetails = mtInstructionActualDetailRepository.select(mtInstructionActualDetail);
        if(CollectionUtils.isEmpty(mtInstructionActualDetails)){
            wmsProductReturnVO2.setSerialAccount(0d);
            wmsProductReturnVO2.setSalesOrderLineStatus("??????");
            wmsMaterialDocReturnVO2.setWmsProductReturnVO2(wmsProductReturnVO2);
            return wmsMaterialDocReturnVO2;
        }
        List<String> mtMaterialLotIdList = mtInstructionActualDetails.stream().map(MtInstructionActualDetail::getMaterialLotId).collect(Collectors.toList());
        wmsProductReturnVO2.setSerialAccount(Double.valueOf(mtMaterialLotIdList.size()));
        //???????????????
        wmsMaterialDocReturnVO2.setWmsProductReturnVO2(wmsProductReturnVO2);
        //???????????????????????????
        List<MtMaterialLot> mtMaterialLots = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId,mtMaterialLotIdList);
        List<String> mtMaterialIdList = mtMaterialLots.stream().map(MtMaterialLot::getMaterialId).collect(Collectors.toList());
        //??????????????????????????????
        List<MtMaterialVO> mtMaterialVOS = mtMaterialRepository.materialPropertyBatchGet(tenantId,mtMaterialIdList);
        for(MtInstructionActualDetail mtInstructionActualDetail1:mtInstructionActualDetails){
            WmsMaterialLineReturnVO wmsMaterialLineReturnVO = new WmsMaterialLineReturnVO();
            List<MtMaterialLot> mtMaterialLotList = mtMaterialLots.stream().filter(item ->item.getMaterialLotId().equals(mtInstructionActualDetail1.getMaterialLotId())).collect(Collectors.toList());
            wmsMaterialLineReturnVO.setActdetmaterialLotId(mtMaterialLotList.get(0).getMaterialLotId());
            wmsMaterialLineReturnVO.setActdetmaterialLotCode(mtMaterialLotList.get(0).getMaterialLotCode());
            wmsMaterialLineReturnVO.setActdetMaterialId(mtMaterialLotList.get(0).getMaterialId());
            List<MtMaterialVO> mtMaterialVOList = mtMaterialVOS.stream().filter(item ->item.getMaterialId().equals(mtMaterialLotList.get(0).getMaterialId())).collect(Collectors.toList());
            wmsMaterialLineReturnVO.setActdetMaterialName(mtMaterialVOList.get(0).getMaterialName());
            wmsMaterialLineReturnVO.setActdetMaterialCode(mtMaterialVOList.get(0).getMaterialCode());
            //???????????????????????????
            String materialVersion = mtMaterialLotMapper.selectAttrValue(tenantId,"MATERIAL_VERSION",mtMaterialLotList.get(0).getMaterialLotId());
            wmsMaterialLineReturnVO.setActdetMaterialVersion(materialVersion);
            wmsMaterialLineReturnVO.setActdetQty(mtInstructionActualDetail1.getActualQty());
            wmsMaterialLineReturnVO.setActdetUomId(mtMaterialLotList.get(0).getPrimaryUomId());
            wmsMaterialLineReturnVO.setActdetMaterialUom(mtUomRepository.uomPropertyGet(tenantId,mtMaterialLotList.get(0).getPrimaryUomId()).getUomCode());
            wmsMaterialLineReturnVO.setActdetMaterialLot(mtMaterialLotList.get(0).getLot());
            wmsMaterialLineReturnVO.setActdetLocatorId(mtInstructionActualDetail1.getToLocatorId());
            if(StringUtils.isBlank(mtInstructionActualDetail1.getToLocatorId())){
                wmsMaterialLineReturnVO.setActdetLocatorCode("");
            }else{
                wmsMaterialLineReturnVO.setActdetLocatorCode(mtModLocatorRepository.locatorBasicPropertyGet(tenantId,mtInstructionActualDetail1.getToLocatorId()).getLocatorCode());
            }
            wmsMaterialLineReturnVO.setActdetQualityStatus(mtMaterialLotList.get(0).getQualityStatus());
            String qualityStatusMeaning = lovAdapter.queryLovMeaning("WX.WMS.SO_DELIVERY_LINE_STATUS", tenantId, mtMaterialLotList.get(0).getQualityStatus());
            wmsMaterialLineReturnVO.setActdetQualityStatusMeaning(qualityStatusMeaning);
            wmsMaterialLineReturnVO.setActdetEnableFlag(mtMaterialLotList.get(0).getEnableFlag());
            if(mtMaterialLotList.get(0).getEnableFlag().equals("Y")){
                wmsMaterialLineReturnVO.setActdetEnableFlagMeaning("??????");
            }
            if(mtMaterialLotList.get(0).getEnableFlag().equals("N")){
                wmsMaterialLineReturnVO.setActdetEnableFlagMeaning("??????");
            }
            String status = mtMaterialLotMapper.selectAttrValue(tenantId,"STATUS",mtMaterialLotList.get(0).getMaterialLotId());
            if(status.equals("SCANNED")){
                wmsMaterialLineReturnVO.setSelectFlag("Y");
            }else{
                wmsMaterialLineReturnVO.setSelectFlag("N");
            }
            wmsMaterialLineReturnVOList.add(wmsMaterialLineReturnVO);
        }
        wmsMaterialDocReturnVO2.setWmsMaterialLineReturnVOList(wmsMaterialLineReturnVOList);
        return wmsMaterialDocReturnVO2;
    }

    /**
     * ??????????????????
     *
     * @param tenantId          ??????
     * @param wmsMaterialDocReturnVO2
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductReturnVO>
     * @author li.zhang13@hand-china.com 2021/07/07 15:00:00
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WmsMaterialDeleteReturnVO2 docDetailDelete(Long tenantId, WmsMaterialDocReturnVO2 wmsMaterialDocReturnVO2) {
        //????????????
        WmsMaterialDeleteReturnVO2 wmsMaterialDeleteReturnVO2 = new WmsMaterialDeleteReturnVO2();
        //????????????Id
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("SALES_RETURN");
        String eventId = mtEventRepository.eventCreate(tenantId,mtEventCreateVO);
        //?????????????????????????????????
        List<MtInstructionActual> mtInstructionActualList = mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId,wmsMaterialDocReturnVO2.getWmsProductReturnVO2().getInstructionId());
        //??????????????????
        MtInstructionActualVO mtInstructionActualVO = new MtInstructionActualVO();
        mtInstructionActualVO.setInstructionId(wmsMaterialDocReturnVO2.getWmsProductReturnVO2().getInstructionId());
        Double qty = 0d;
        for(WmsMaterialLineReturnVO wmsMaterialLineReturnVO: wmsMaterialDocReturnVO2.getWmsMaterialLineReturnVOList()){
            qty = qty+wmsMaterialLineReturnVO.getActdetQty();
        }
        mtInstructionActualVO.setActualQty(-qty);
        mtInstructionActualVO.setToLocatorId(wmsMaterialDocReturnVO2.getWmsProductReturnVO2().getToLocatorId());
        mtInstructionActualVO.setEventId(eventId);
        MtInstructionActualVO1 mtInstructionActualVO1 = mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionActualVO);
        for(WmsMaterialLineReturnVO wmsMaterialLineReturnVO: wmsMaterialDocReturnVO2.getWmsMaterialLineReturnVOList()){
            //????????????????????????
            mtInstructionActualDetailMapper.deleteByMaterialLotId(tenantId,mtInstructionActualVO1.getActualId(),wmsMaterialLineReturnVO.getActdetmaterialLotId());
            //????????????
            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
            mtMaterialLotVO2.setMaterialLotCode(wmsMaterialLineReturnVO.getActdetmaterialLotCode());
            mtMaterialLotVO2.setEventId(eventId);
            mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, HmeConstants.ConstantValue.NO);
            //???????????????????????????OLD_STATUS??????
            String status = mtMaterialLotMapper.selectAttrValue(tenantId,"OLD_STATUS",wmsMaterialLineReturnVO.getActdetmaterialLotId());
            //????????????????????????
            MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
            mtExtendVO10.setKeyId(wmsMaterialLineReturnVO.getActdetmaterialLotId());
            mtExtendVO10.setEventId(eventId);
            List<MtExtendVO5> attrs = new ArrayList<>();
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("STATUS");
            mtExtendVO5.setAttrValue("NEW");
            attrs.add(mtExtendVO5);
            mtExtendVO10.setAttrs(attrs);
            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);
        }
        //????????????????????????????????????????????????????????????
        MtInstructionActual mtInstructionActual = mtInstructionActualRepository.instructionActualPropertyGet(tenantId,mtInstructionActualVO1.getActualId());
        if(mtInstructionActual.getActualQty() == 0){
            mtInstructionActualMapper.deleteByPrimaryKey(mtInstructionActual.getActualId());
        }
        //??????????????????
        MtInstructionVO mtInstructionVO = new MtInstructionVO();
        mtInstructionVO.setInstructionId(wmsMaterialDocReturnVO2.getWmsProductReturnVO2().getInstructionId());
        mtInstructionVO.setEventId(eventId);
        String instructionStatus = "";
        if(mtInstructionActualList.get(0).getActualQty()-qty== 0){
            instructionStatus = "RELEASED";
        }else if(mtInstructionActualList.get(0).getActualQty()-qty < wmsMaterialDocReturnVO2.getWmsProductReturnVO2().getDemondQty()){
            instructionStatus = "EXECUTE";
        }else if(mtInstructionActualList.get(0).getActualQty()-qty == wmsMaterialDocReturnVO2.getWmsProductReturnVO2().getDemondQty()){
            instructionStatus = "COMPLETE";
        }
        String instructionStatusMeaning = lovAdapter.queryLovMeaning("WX.WMS.SO_DELIVERY_LINE_STATUS", tenantId, instructionStatus);
        mtInstructionVO.setInstructionStatus(instructionStatus);
        mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, WmsConstant.CONSTANT_N);
        return wmsMaterialDeleteReturnVO2;
    }

    /**
     * ????????????
     *
     * @param tenantId          ??????
     * @param wmsProductReturnVO
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductReturnVO>
     * @author li.zhang13@hand-china.com 2021/07/07 15:00:00
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WmsProductExecuteVO execute(Long tenantId, WmsProductReturnVO wmsProductReturnVO) {
        //????????????
        WmsProductExecuteVO wmsProductExecuteVO = new WmsProductExecuteVO();
        //?????????????????????
        MtInstructionVO10 mtInstructionVO10 = new MtInstructionVO10();
        mtInstructionVO10.setSourceDocId(wmsProductReturnVO.getInstructionDocId());
        List<String> instructionIdList = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstructionVO10);
        //????????????????????????
        List<MtInstructionActual> mtInstructionActuals = mtInstructionActualRepository.instructionLimitActualBatchGet(tenantId,instructionIdList);
        //????????????Id??????????????????????????????
        List<MtInstructionActualDetail> mtInstructionActualDetails = mtInstructionActualDetailRepository.instructionActualLimitDetailBatchQuery(tenantId,
                mtInstructionActuals.stream().map(MtInstructionActual::getActualId).collect(Collectors.toList()));
        //?????????????????????????????????
        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setTableName("mt_material_lot_attr");
        mtExtendVO1.setKeyIdList(mtInstructionActualDetails.stream().map(MtInstructionActualDetail::getMaterialLotId).collect(Collectors.toList()));
        List<MtExtendVO5> attrs = new ArrayList<>();
        MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("STATUS");
        attrs.add(mtExtendVO5);
        mtExtendVO1.setAttrs(attrs);
        List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId,mtExtendVO1);
        //?????????????????????????????????SCANNED
        for(MtExtendAttrVO1 mtExtendAttrVO1:mtExtendAttrVO1s){
            if(!mtExtendAttrVO1.getAttrValue().equals("SCANNED")){
                throw new MtException("WX_WMS_PRODUCT_RETURN_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WX_WMS_PRODUCT_RETURN_011", "WMS"));
            }
        }
        for(MtInstructionActualDetail mtInstructionActualDetail:mtInstructionActualDetails){
            //???????????????????????????????????????
            if(StringUtils.isBlank(mtInstructionActualDetail.getToLocatorId())){
                MtInstructionActual mtInstructionActual = mtInstructionActualRepository.instructionActualPropertyGet(tenantId,mtInstructionActualDetail.getActualId());
                wmsProductExecuteVO.setInstructionId(mtInstructionActual.getInstructionId());
                wmsProductExecuteVO.setMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WX_WMS_PRODUCT_RETURN_010", "WMS"));
                return wmsProductExecuteVO;
            }
        }
        //????????????
        for(WmsProductReturnVO2 wmsProductReturnVO2:wmsProductReturnVO.getInstructionList()){
            if(wmsProductReturnVO2.getActualQty()<wmsProductReturnVO2.getDemondQty()){
                throw new MtException("WX_WMS_PRODUCT_RETURN_014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WX_WMS_PRODUCT_RETURN_014", "WMS"));
            }
        }
        //????????????Id
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("SALES_RETURN");
        String eventId = mtEventRepository.eventCreate(tenantId,mtEventCreateVO);
        //???????????????????????????
        MtMaterialLotVO42 mtMaterialLotVO42 = new MtMaterialLotVO42();
        mtMaterialLotVO42.setEventId(eventId);
        List<MtMaterialLotVO41> materialLotList = new ArrayList<>();
        //????????????
        List<WmsObjectTransactionRequestVO> wmsObjectTransactionRequestVOS = new ArrayList<>();
        List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>();
        for(WmsProductReturnVO2 wmsProductReturnVO2:wmsProductReturnVO.getInstructionList()){
            //??????????????????????????????
            String flag = mtInstructionMapper.selectAttrValue(tenantId,"SPEC_STOCK_FLAG",wmsProductReturnVO2.getInstructionId());
            //???????????????????????????
            List<MtInstructionActual> mtInstructionActualList = mtInstructionActuals.stream()
                    .filter(item ->item.getInstructionId().equals(wmsProductReturnVO2.getInstructionId()))
                    .collect(Collectors.toList());
            //??????????????????????????????????????????
            List<MtInstructionActualDetail> mtInstructionActualDetailList = mtInstructionActualDetails.stream()
                    .filter(item ->item.getActualId().equals(mtInstructionActualList.get(0).getActualId()))
                    .collect(Collectors.toList());
            for(MtInstructionActualDetail mtInstructionActualDetail:mtInstructionActualDetailList){
                MtMaterialLotVO41 mtMaterialLotVO41 = new MtMaterialLotVO41();
                mtMaterialLotVO41.setMaterialLotId(mtInstructionActualDetail.getMaterialLotId());
                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, mtInstructionActualDetail.getMaterialLotId());
                mtMaterialLotVO41.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
                mtMaterialLotVO41.setSiteId(wmsProductReturnVO.getSiteId());
                mtMaterialLotVO41.setEnableFlag("Y");
                mtMaterialLotVO41.setQualityStatus("OK");
                mtMaterialLotVO41.setMaterialId(wmsProductReturnVO2.getMaterialId());
                mtMaterialLotVO41.setPrimaryUomId(mtInstructionActualDetail.getUomId());
                mtMaterialLotVO41.setTrxPrimaryUomQty(mtInstructionActualDetail.getActualQty());
                mtMaterialLotVO41.setLocatorId(mtInstructionActualDetail.getToLocatorId());
                mtMaterialLotVO41.setLot(mtMaterialLot.getLot());
                mtMaterialLotVO41.setSupplierId(mtMaterialLot.getSupplierId());
                mtMaterialLotVO41.setSupplierSiteId(mtMaterialLot.getSupplierSiteId());
                mtMaterialLotVO41.setCustomerId(mtMaterialLot.getCustomerId());
                mtMaterialLotVO41.setCustomerSiteId(mtMaterialLot.getCustomerSiteId());
                mtMaterialLotVO41.setCreateReason("PRODUCTION_RETURN");
                mtMaterialLotVO41.setEoId(mtMaterialLot.getEoId());
                mtMaterialLotVO41.setInSiteTime(mtMaterialLot.getInSiteTime());
                mtMaterialLotVO41.setInLocatorTime(currentTimeGet());
                materialLotList.add(mtMaterialLotVO41);
                //??????????????????????????????
                MtCommonExtendVO6 mtCommonExtendVO6 = new MtCommonExtendVO6();
                mtCommonExtendVO6.setKeyId(mtInstructionActualDetail.getMaterialLotId());
                List<MtCommonExtendVO5> mtCommonExtendVO5s = new ArrayList<>();
                MtCommonExtendVO5 mtCommonExtendVO5;
                mtCommonExtendVO5 = new MtCommonExtendVO5();
                mtCommonExtendVO5.setAttrName("STATUS");
                mtCommonExtendVO5.setAttrValue("INSTOCK");
                mtCommonExtendVO5s.add(mtCommonExtendVO5);
                //??????????????????Y?????????mtmaterialLot????????????SO_NUM???SO_LINE_NUM??????
                if("E".equals(flag)){
                    mtCommonExtendVO5 = new MtCommonExtendVO5();
                    mtCommonExtendVO5.setAttrName("SO_NUM");
                    mtCommonExtendVO5.setAttrValue(wmsProductReturnVO2.getSalesOrderNum());
                    mtCommonExtendVO5s.add(mtCommonExtendVO5);
                    mtCommonExtendVO5 = new MtCommonExtendVO5();
                    mtCommonExtendVO5.setAttrName("SO_LINE_NUM");
                    mtCommonExtendVO5.setAttrValue(wmsProductReturnVO2.getSalesOrderLineNum());
                    mtCommonExtendVO5s.add(mtCommonExtendVO5);
                }
                mtCommonExtendVO6.setAttrs(mtCommonExtendVO5s);
                attrPropertyList.add(mtCommonExtendVO6);
                //????????????
                WmsObjectTransactionRequestVO wmsObjectTransactionRequestVO = new WmsObjectTransactionRequestVO();
                wmsObjectTransactionRequestVO.setTransactionTypeCode("WMS_RETURN_FROM_CUS");
                wmsObjectTransactionRequestVO.setEventId(eventId);
                wmsObjectTransactionRequestVO.setMaterialLotId(mtInstructionActualDetail.getMaterialLotId());
                wmsObjectTransactionRequestVO.setMaterialId(wmsProductReturnVO2.getMaterialId());
                wmsObjectTransactionRequestVO.setTransactionQty(BigDecimal.valueOf(mtInstructionActualDetail.getActualQty()));
                wmsObjectTransactionRequestVO.setLotNumber(mtMaterialLot.getLot());
                wmsObjectTransactionRequestVO.setTransferLotNumber(mtMaterialLot.getLot());
                wmsObjectTransactionRequestVO.setTransactionUom(mtUomRepository.uomPropertyGet(tenantId,mtInstructionActualDetail.getUomId()).getUomCode());
                wmsObjectTransactionRequestVO.setTransactionTime(currentTimeGet());
                wmsObjectTransactionRequestVO.setTransactionReasonCode("????????????");
                wmsObjectTransactionRequestVO.setPlantId(wmsProductReturnVO.getSiteId());
                wmsObjectTransactionRequestVO.setTransferPlantId(wmsProductReturnVO.getSiteId());
                wmsObjectTransactionRequestVO.setTransferWarehouseId(mtModLocatorRepository.locatorBasicPropertyGet(tenantId,mtInstructionActualDetail.getToLocatorId()).getParentLocatorId());
                wmsObjectTransactionRequestVO.setTransferLocatorId(mtInstructionActualDetail.getToLocatorId());
                wmsObjectTransactionRequestVO.setSourceDocType("SALES_RETURN");
                wmsObjectTransactionRequestVO.setSourceDocId(wmsProductReturnVO.getInstructionDocId());
                wmsObjectTransactionRequestVO.setSourceDocLineId(wmsProductReturnVO2.getInstructionId());
                String soNum = null;
                String soLineNum = null;
                if("E".equals(flag)){
                    soNum = wmsProductReturnVO2.getSalesOrderNum();
                    soLineNum = wmsProductReturnVO2.getSalesOrderLineNum();
                }else{
                    soNum = mtMaterialLotMapper.selectAttrValue(tenantId,"SO_NUM",mtInstructionActualDetail.getMaterialLotId());
                    soLineNum = mtMaterialLotMapper.selectAttrValue(tenantId,"SO_LINE_NUM",mtInstructionActualDetail.getMaterialLotId());
                }
                wmsObjectTransactionRequestVO.setSoNum(soNum);
                wmsObjectTransactionRequestVO.setSoLineNum(soLineNum);
                WmsTransactionTypeDTO wmsTransactionTypeDTO = wmsTransactionTypeRepository.getTransactionType(tenantId,"WMS_RETURN_FROM_CUS");
                wmsObjectTransactionRequestVO.setMoveType(wmsTransactionTypeDTO.getMoveType());
                wmsObjectTransactionRequestVOS.add(wmsObjectTransactionRequestVO);
            }
        }
        mtMaterialLotVO42.setMaterialLotList(materialLotList);
        //??????????????????????????????
        mtMaterialLotRepository.materialLotAccumulate(tenantId,mtMaterialLotVO42);
        //????????????????????????
        mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId,"mt_material_lot_attr",eventId,attrPropertyList);
        //????????????
        wmsObjectTransactionRepository.objectTransactionSync(tenantId, wmsObjectTransactionRequestVOS);
        //??????????????????
        String instructionDocStatus = "";
        MtInstruction mtInstruction = new MtInstruction();
        mtInstruction.setTenantId(tenantId);
        mtInstruction.setSourceDocId(wmsProductReturnVO.getInstructionDocId());
        List<MtInstruction> mtInstructionList = mtInstructionRepository.select(mtInstruction);
        for(MtInstruction mtInstruction1:mtInstructionList){
            if(mtInstruction1.getInstructionStatus().equals("EXECUTE")){
                instructionDocStatus = "EXECUTE";
            }
        }
        List<MtInstruction> mtInstructionList1 = mtInstructionList.stream().filter(item ->!"CANCEL".equals(item.getInstructionStatus())).collect(Collectors.toList());
        Set<String> instructionstatusSet = mtInstructionList1.stream().map(MtInstruction::getInstructionStatus).collect(Collectors.toSet());
        List<String> instructionstatusList = new ArrayList<>(instructionstatusSet);
        if(instructionstatusList.size()==1){
            if(instructionstatusList.get(0).equals("COMPLETE")){
                instructionDocStatus = "COMPLETE";
            }
            if(instructionstatusList.get(0).equals("RELEASED")){
                instructionDocStatus = "RELEASED";
            }
        }else{
            instructionDocStatus = "EXECUTE";
        }
        MtInstructionDocDTO2 mtInstructionDocDTO2 = new MtInstructionDocDTO2();
        mtInstructionDocDTO2.setInstructionDocId(wmsProductReturnVO.getInstructionDocId());
        mtInstructionDocDTO2.setInstructionDocStatus(instructionDocStatus);
        mtInstructionDocDTO2.setEventId(eventId);
        mtInstructionDocRepository.instructionDocUpdate(tenantId, mtInstructionDocDTO2, WmsConstant.CONSTANT_N);
        //????????????[soDeliveryChangeOrPostIface]
        List<ItfSoDeliveryChanOrPostDTO> itfSoDeliveryChanOrPostList = new ArrayList<>();
        ItfSoDeliveryChanOrPostDTO itfSoDeliveryChanOrPostDTO = new ItfSoDeliveryChanOrPostDTO();
        itfSoDeliveryChanOrPostDTO.setType("POST");
        itfSoDeliveryChanOrPostDTO.setInstructionDocId(wmsProductReturnVO.getInstructionDocId());
        itfSoDeliveryChanOrPostList.add(itfSoDeliveryChanOrPostDTO);
        List<ItfSoDeliveryChanOrPostDTO> itfSoDeliveryChanOrPostDTOS = itfSoDeliveryChanOrPostIfaceRepository.soDeliveryChangeOrPostIface(tenantId,itfSoDeliveryChanOrPostList);
        if(CollectionUtils.isNotEmpty(itfSoDeliveryChanOrPostDTOS) && "E".equals(itfSoDeliveryChanOrPostDTOS.get(0).getStatus())){
            throw new MtException("wms-product-return-error",itfSoDeliveryChanOrPostDTOS.get(0).getMessage());
        }
        return wmsProductExecuteVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WmsProductExitVO exitJudge(Long tenantId, WmsProductReturnVO wmsProductReturnVO) {
        //????????????
        WmsProductExitVO wmsProductExitVO = new WmsProductExitVO();
        String msg = "";
        for(WmsProductReturnVO2 wmsProductReturnVO2:wmsProductReturnVO.getInstructionList()){
            //?????????????????????????????????
            List<MtInstructionActual> mtInstructionActualList = mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId,wmsProductReturnVO2.getInstructionId());
            String flag = "N";
            if(!CollectionUtils.isEmpty(mtInstructionActualList)){
                //????????????????????????
                MtInstructionActualDetail mtInstructionActualDetail = new MtInstructionActualDetail();
                mtInstructionActualDetail.setTenantId(tenantId);
                mtInstructionActualDetail.setActualId(mtInstructionActualList.get(0).getActualId());
                List<MtInstructionActualDetail> mtInstructionActualDetails = mtInstructionActualDetailRepository.select(mtInstructionActualDetail);
                for(MtInstructionActualDetail mtInstructionActualDetail1:mtInstructionActualDetails){
                    if(StringUtils.isBlank(mtInstructionActualDetail1.getToLocatorId())){
                        flag = "Y";
                    }
                }
            }
            if(flag.equals("Y")){
                msg = msg+ "["+wmsProductReturnVO2.getSalesOrderLineNum() +"]"+"-";
            }
        }
        if(!StringUtils.isBlank(msg)){
            wmsProductExitVO.setMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WX_WMS_PRODUCT_RETURN_018", "WMS", msg));
        }
        return wmsProductExitVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exitDelete(Long tenantId, WmsProductReturnVO wmsProductReturnVO) {
        for(WmsProductReturnVO2 wmsProductReturnVO2:wmsProductReturnVO.getInstructionList()){
            //?????????????????????????????????
            List<MtInstructionActual> mtInstructionActualList = mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId,wmsProductReturnVO2.getInstructionId());
            if(!CollectionUtils.isEmpty(mtInstructionActualList)){
                //????????????????????????
                MtInstructionActualDetail mtInstructionActualDetail = new MtInstructionActualDetail();
                mtInstructionActualDetail.setTenantId(tenantId);
                mtInstructionActualDetail.setActualId(mtInstructionActualList.get(0).getActualId());
                List<MtInstructionActualDetail> mtInstructionActualDetails = mtInstructionActualDetailRepository.select(mtInstructionActualDetail);
                //????????????Id
                MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
                mtEventCreateVO.setEventTypeCode("SALES_RETURN");
                String eventId = mtEventRepository.eventCreate(tenantId,mtEventCreateVO);
                //????????????????????????
                Double qty = 0d;
                for(MtInstructionActualDetail mtInstructionActualDetail1:mtInstructionActualDetails){
                    if(StringUtils.isBlank(mtInstructionActualDetail1.getToLocatorId())){
                        qty = qty + mtInstructionActualDetail1.getActualQty();
                        mtInstructionActualDetailMapper.deleteByMaterialLotId(tenantId,mtInstructionActualList.get(0).getActualId(),mtInstructionActualDetail1.getMaterialLotId());
                        //???????????????????????????OLD_STATUS??????
                        String status = mtMaterialLotMapper.selectAttrValue(tenantId,"OLD_STATUS",mtInstructionActualDetail1.getMaterialLotId());
                        //????????????????????????
                        MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
                        mtExtendVO10.setKeyId(mtInstructionActualDetail1.getMaterialLotId());
                        mtExtendVO10.setEventId(eventId);
                        List<MtExtendVO5> attrs = new ArrayList<>();
                        MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                        mtExtendVO5.setAttrName("STATUS");
                        mtExtendVO5.setAttrValue(status);
                        attrs.add(mtExtendVO5);
                        mtExtendVO10.setAttrs(attrs);
                        mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);
                    }
                }
                //??????????????????
                MtInstructionVO mtInstructionVO = new MtInstructionVO();
                mtInstructionVO.setInstructionId(wmsProductReturnVO2.getInstructionId());
                mtInstructionVO.setEventId(eventId);
                String instructionStatus = "";
                if(mtInstructionActualList.get(0).getActualQty()-qty== 0){
                    instructionStatus = "RELEASED";
                }
                mtInstructionVO.setInstructionStatus(instructionStatus);
                mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, WmsConstant.CONSTANT_N);
                mtInstructionActualMapper.updateActualQty(tenantId,mtInstructionActualList.get(0).getActualQty()-qty,wmsProductReturnVO2.getInstructionId());
            }
        }
    }

    /***
     * @Description: ??????????????????
     */
    public static Date currentTimeGet() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(currentTime.format(formatter), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }
}
