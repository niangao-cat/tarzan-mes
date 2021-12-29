package com.ruike.hme.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ruike.hme.api.dto.HmeAfterSaleQuotationHeaderDto;
import com.ruike.hme.app.service.HmeAfterSaleQuotationHeaderService;
import com.ruike.hme.domain.entity.HmeAfterSaleQuotationHeader;
import com.ruike.hme.domain.entity.HmeServiceReceive;
import com.ruike.hme.domain.entity.HmeServiceSplitRecord;
import com.ruike.hme.domain.repository.HmeAfterSaleQuotationHeaderRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.mapper.HmeAfterSaleQuotationHeaderMapper;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.utils.SendESBConnect;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spring.web.json.Json;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.modeling.domain.entity.MtCustomer;
import tarzan.modeling.domain.repository.MtCustomerRepository;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 售后报价单头表应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2021-09-26 11:07:31
 */
@Service
@Slf4j
public class HmeAfterSaleQuotationHeaderServiceImpl implements HmeAfterSaleQuotationHeaderService {

    @Autowired
    private HmeAfterSaleQuotationHeaderRepository hmeAfterSaleQuotationHeaderRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private HmeAfterSaleQuotationHeaderMapper hmeAfterSaleQuotationHeaderMapper;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtCustomerRepository mtCustomerRepository;
    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;
    @Autowired
    private SendESBConnect sendESBConnect;

    @Override
    public HmeAfterSaleQuotationHeaderVO3 scanSn(Long tenantId, String snNum) throws ParseException {
        //根据SN编码查询接收拆箱登记表数据、售后返品拆机表数据、售后报价单头表数据
        HmeAfterSaleQuotationHeaderVO2 hmeAfterSaleQuotationHeaderVO2 = hmeAfterSaleQuotationHeaderRepository.serviceReceiveAndRecordInfoQuery(tenantId, snNum);
        //查询界面头部数据
        HmeAfterSaleQuotationHeaderVO3 result = hmeAfterSaleQuotationHeaderRepository.queryHeadData(tenantId, snNum, hmeAfterSaleQuotationHeaderVO2);
        String sendTo = result.getHeadData().getSendTo();
        //光学物料无需更换标识
        HmeAfterSaleQuotationHeader hmeAfterSaleQuotationHeader = hmeAfterSaleQuotationHeaderVO2.getHmeAfterSaleQuotationHeader();
        if(Objects.nonNull(hmeAfterSaleQuotationHeader) && StringUtils.isNotBlank(hmeAfterSaleQuotationHeader.getOpticsNoFlag())){
            result.setOpticsNoFlag(hmeAfterSaleQuotationHeader.getOpticsNoFlag());
        }else {
            result.setOpticsNoFlag("N");
        }
        //电学物料无需更换标识
        if(Objects.nonNull(hmeAfterSaleQuotationHeader) && StringUtils.isNotBlank(hmeAfterSaleQuotationHeader.getElectricNoFlag())){
            result.setElectricNoFlag(hmeAfterSaleQuotationHeader.getElectricNoFlag());
        }else {
            result.setElectricNoFlag("N");
        }
        //查询光学物料行数据
        List<HmeAfterSaleQuotationHeaderVO4> opticsLineList = hmeAfterSaleQuotationHeaderRepository.queryLineData(tenantId,
                hmeAfterSaleQuotationHeaderVO2, "OPTICS", sendTo);
        result.setOpticsLineList(opticsLineList);
        //查询电学物料行数据
        List<HmeAfterSaleQuotationHeaderVO4> electricLineList = hmeAfterSaleQuotationHeaderRepository.queryLineData(tenantId,
                hmeAfterSaleQuotationHeaderVO2, "ELECTRIC", sendTo);
        result.setElectricLineList(electricLineList);
        //查询工时费行数据
        List<HmeAfterSaleQuotationHeaderVO4> hourFeeLineList = hmeAfterSaleQuotationHeaderRepository.queryLineData(tenantId,
                hmeAfterSaleQuotationHeaderVO2, "HOUR_FEE", sendTo);
        result.setHourFeeLineList(hourFeeLineList);
        //扫描SN对应的物料批ID
        MtMaterialLot mtMaterialLot = hmeAfterSaleQuotationHeaderVO2.getMtMaterialLot();
        if(Objects.nonNull(mtMaterialLot)){
            result.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        }
        return result;
    }

    @Override
    public HmeAfterSaleQuotationHeaderVO3 createQuery(Long tenantId, String snNum) throws ParseException {
        //按照场景二逻辑，根据SN编码查询接收拆箱登记表数据、售后返品拆机表数据
        HmeAfterSaleQuotationHeaderVO2 hmeAfterSaleQuotationHeaderVO2 = hmeAfterSaleQuotationHeaderRepository.serviceReceiveAndRecordInfoCreateQuery(tenantId, snNum);
        //查询界面头部数据
        HmeAfterSaleQuotationHeaderVO3 result = hmeAfterSaleQuotationHeaderRepository.queryHeadData(tenantId, snNum, hmeAfterSaleQuotationHeaderVO2);
        HmeAfterSaleQuotationHeaderVO6 headData = result.getHeadData();
        headData.setStatus("NEW");
        String statusMeaning = lovAdapter.queryLovMeaning("HME.QUOTATION_STATUS", tenantId, headData.getStatus());
        if(StringUtils.isNotBlank(statusMeaning)){
            headData.setStatusMeaning(statusMeaning);
        }
        //查询工时费行数据
        List<HmeAfterSaleQuotationHeaderVO4> hourFeeLineList = hmeAfterSaleQuotationHeaderRepository.hourfeeLineCreateQuery(tenantId);
        result.setHourFeeLineList(hourFeeLineList);
        //光学物料无需更换标识
        result.setOpticsNoFlag("N");
        //电学物料无需更换标识
        result.setElectricNoFlag("N");
        //扫描SN对应的物料批ID
        MtMaterialLot mtMaterialLot = hmeAfterSaleQuotationHeaderVO2.getMtMaterialLot();
        if(Objects.nonNull(mtMaterialLot)){
            result.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        }
        return result;
    }

    @Override
    public String sendDateQueryByMaterial(Long tenantId, HmeAfterSaleQuotationHeaderDto dto) throws ParseException {
        //必输校验
        if(StringUtils.isBlank(dto.getSendTo())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "送达方"));
        }
        if(StringUtils.isBlank(dto.getMaterialId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "物料"));
        }
        String componentStr = lovAdapter.queryLovMeaning("HME.AFTER_SALES_WARRANTY", tenantId, "COMPONENT");
        BigDecimal component = new BigDecimal(componentStr);
        Date nowDate = new Date();
        List<String> materialIdList = new ArrayList<>();
        materialIdList.add(dto.getMaterialId());
        List<HmeAfterSaleQuotationHeaderVO5> headerUpdateDateList = new ArrayList<>();
        if(StringUtils.isNotBlank(dto.getMaterialLotId())){
            headerUpdateDateList = hmeAfterSaleQuotationHeaderMapper.quotationHeaderUpdateDateQuery(tenantId,
                    dto.getMaterialLotId(), null, dto.getSendTo(),null, materialIdList);
        }
        if(CollectionUtils.isEmpty(headerUpdateDateList)){
            headerUpdateDateList = hmeAfterSaleQuotationHeaderMapper.quotationHeaderUpdateDateQuery(tenantId,
                    null, dto.getSnNum(), dto.getSendTo(),null, materialIdList);
        }
        if(CollectionUtils.isNotEmpty(headerUpdateDateList)){
            headerUpdateDateList = headerUpdateDateList.stream()
                    .sorted(Comparator.comparing(HmeAfterSaleQuotationHeaderVO5::getLastUpdateDate).reversed())
                    .collect(Collectors.toList());
            Date lastUpdateDate = headerUpdateDateList.get(0).getLastUpdateDate();
            //根据sernr、lastUpdateDate查询接口表中满足条件的日期
            List<Date> ifaceLastUpdateDateList = hmeAfterSaleQuotationHeaderMapper.snSapIfaceUpdateDateQuery(tenantId, dto.getSnNum(), lastUpdateDate);
            if(CollectionUtils.isNotEmpty(ifaceLastUpdateDateList)){
                List<Date> finalIfaceLastUpdateDateList = new ArrayList<>();
                for (Date ifaceLastUpdateDate:ifaceLastUpdateDateList) {
                    BigDecimal bigDecimal = hmeAfterSaleQuotationHeaderRepository.daysBetween(ifaceLastUpdateDate, nowDate);
                    //如果当前时间-lastUpdateDate < 组件质保期，则此lastUpdateDate符合要求，从中取出最大的lastUpdateDate作为质保内发货日期
                    if(bigDecimal.compareTo(component) < 0){
                        finalIfaceLastUpdateDateList.add(ifaceLastUpdateDate);
                    }
                }
                if(CollectionUtils.isNotEmpty(finalIfaceLastUpdateDateList)){
                    finalIfaceLastUpdateDateList = finalIfaceLastUpdateDateList.stream().sorted(Comparator.comparing(Date::getTime).reversed()).collect(Collectors.toList());
                    Date date = finalIfaceLastUpdateDateList.get(0);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    return simpleDateFormat.format(date);
                }
            }
        }
        return "";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Long tenantId, HmeAfterSaleQuotationHeaderVO3 dto) {
        List<HmeAfterSaleQuotationHeaderVO4> opticsLineList = dto.getOpticsLineList();
        List<HmeAfterSaleQuotationHeaderVO4> electricLineList = dto.getElectricLineList();
        List<HmeAfterSaleQuotationHeaderVO4> hourFeeLineList = dto.getHourFeeLineList();
        //校验行数据中是否有物料重复
        List<String> materialIdList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(opticsLineList)){
            materialIdList.addAll(opticsLineList.stream().map(HmeAfterSaleQuotationHeaderVO4::getMaterialId).collect(Collectors.toList()));
        }
        if(CollectionUtils.isNotEmpty(electricLineList)){
            materialIdList.addAll(electricLineList.stream().map(HmeAfterSaleQuotationHeaderVO4::getMaterialId).collect(Collectors.toList()));
        }
        if(CollectionUtils.isNotEmpty(hourFeeLineList)){
            materialIdList.addAll(hourFeeLineList.stream().map(HmeAfterSaleQuotationHeaderVO4::getMaterialId).collect(Collectors.toList()));
        }
        if(CollectionUtils.isNotEmpty(materialIdList)){
            int size = materialIdList.size();
            materialIdList = materialIdList.stream().distinct().collect(Collectors.toList());
            int distinctSize = materialIdList.size();
            if(size != distinctSize){
                throw new MtException("HME_QUOTATION_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_QUOTATION_006", "HME"));
            }
        }
        HmeAfterSaleQuotationHeaderVO6 headData = dto.getHeadData();
        if("NEW".equals(headData.getStatus())){
            //报价单状态为新建时的保存
            hmeAfterSaleQuotationHeaderRepository.newStatusSave(tenantId, dto);
        }else if ("STORAGE".equals(headData.getStatus())){
            //报价单状态为暂存时的保存
            hmeAfterSaleQuotationHeaderRepository.storageStatusSave(tenantId, dto);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeAfterSaleQuotationHeaderVO13 submit(Long tenantId, HmeAfterSaleQuotationHeaderVO3 dto) {
        List<HmeAfterSaleQuotationHeaderVO7> headerList = new ArrayList<>();
        HmeAfterSaleQuotationHeaderVO7 header = new HmeAfterSaleQuotationHeaderVO7();
        //头数据
        HmeAfterSaleQuotationHeaderVO6 headData = dto.getHeadData();
        if(!"STORAGE".equals(headData.getStatus())){
            //请先保存报价单后提交
            throw new MtException("HME_QUOTATION_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_QUOTATION_008", "HME"));
        }
        int size = 0;
        if (CollectionUtils.isNotEmpty(dto.getOpticsLineList())){
            size = size + dto.getOpticsLineList().size();
        }
        if (CollectionUtils.isNotEmpty(dto.getElectricLineList())){
            size = size + dto.getElectricLineList().size();
        }
        if (CollectionUtils.isNotEmpty(dto.getHourFeeLineList())){
            size = size + dto.getHourFeeLineList().size();
        }
        if(("N".equals(dto.getOpticsNoFlag()) || "N".equals(dto.getElectricNoFlag())) && size == 0 ){
            //光学物料或电学物料存在未输入的情况,请确认
            throw new MtException("HME_QUOTATION_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_QUOTATION_007", "HME"));
        }
        //客户编码 取售达方编码
        MtCustomer mtCustomer = mtCustomerRepository.selectByPrimaryKey(headData.getSoldTo());
        header.setPARTN_NUMB(mtCustomer.getCustomerCode());
        //报价单备注
        header.setZBZL("");
        //维修设备编码(SN) 取扫描的序列号
        header.setZSERNR(headData.getSnNum());
        //根据物料ID、站点ID查询设备型号
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? 1L : curUser.getUserId();
        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
        String equipmentModel = hmeAfterSaleQuotationHeaderMapper.getEquipmentModelByMaterialSite(tenantId, headData.getMaterialId(), defaultSiteId);
        header.setZSBXH(StringUtils.isBlank(equipmentModel)?"":equipmentModel);
        //工程师
        header.setZSQR("");
        //行数据
        List<HmeAfterSaleQuotationHeaderVO8> lineList = new ArrayList<>();
        //光学物料行
        List<HmeAfterSaleQuotationHeaderVO4> opticsLineList = dto.getOpticsLineList();
        for (HmeAfterSaleQuotationHeaderVO4 hmeAfterSaleQuotationHeaderVO4:opticsLineList) {
            HmeAfterSaleQuotationHeaderVO8 line = new HmeAfterSaleQuotationHeaderVO8();
            line.setMATERIAL(hmeAfterSaleQuotationHeaderVO4.getMaterialCode());
            line.setREQ_QTY(hmeAfterSaleQuotationHeaderVO4.getRequsetQty());
            line.setZTEXT(StringUtils.isBlank(hmeAfterSaleQuotationHeaderVO4.getRemark())?"":hmeAfterSaleQuotationHeaderVO4.getRemark());
            lineList.add(line);
        }
        //电学物料行
        List<HmeAfterSaleQuotationHeaderVO4> electricLineList = dto.getElectricLineList();
        for (HmeAfterSaleQuotationHeaderVO4 hmeAfterSaleQuotationHeaderVO4:electricLineList) {
            HmeAfterSaleQuotationHeaderVO8 line = new HmeAfterSaleQuotationHeaderVO8();
            line.setMATERIAL(hmeAfterSaleQuotationHeaderVO4.getMaterialCode());
            line.setREQ_QTY(hmeAfterSaleQuotationHeaderVO4.getRequsetQty());
            line.setZTEXT(StringUtils.isBlank(hmeAfterSaleQuotationHeaderVO4.getRemark())?"":hmeAfterSaleQuotationHeaderVO4.getRemark());
            lineList.add(line);
        }
        //工时费
        List<HmeAfterSaleQuotationHeaderVO4> hourFeeLineList = dto.getHourFeeLineList();
        for (HmeAfterSaleQuotationHeaderVO4 hmeAfterSaleQuotationHeaderVO4:hourFeeLineList) {
            HmeAfterSaleQuotationHeaderVO8 line = new HmeAfterSaleQuotationHeaderVO8();
            line.setMATERIAL(hmeAfterSaleQuotationHeaderVO4.getMaterialCode());
            line.setREQ_QTY(hmeAfterSaleQuotationHeaderVO4.getRequsetQty());
            line.setZTEXT(StringUtils.isBlank(hmeAfterSaleQuotationHeaderVO4.getRemark())?"":hmeAfterSaleQuotationHeaderVO4.getRemark());
            lineList.add(line);
        }
        Map<String, Object> requestInfoMap = new HashMap<>();
        headerList.add(header);
        requestInfoMap.put("header", headerList);
        requestInfoMap.put("item", lineList);
        //调用SAP售后单提交接口
        Map<String, Object> map = sendESBConnect.sendEsb(requestInfoMap, "",
                "HmeAfterSaleQuotationHeaderServiceImpl.submit", ItfConstant.InterfaceCode.ESB_AFTER_SALE_QUOTATION_SUBMIT);
        log.info("sendESBConnect return data:{}", JSON.toJSONString(map));
        String esbReturnStr = JSON.toJSON(map.get("RETURN")).toString();
        List<HmeAfterSaleQuotationHeaderVO10> esbReturn = JSONArray.parseArray(esbReturnStr, HmeAfterSaleQuotationHeaderVO10.class);
        HmeAfterSaleQuotationHeaderVO10 hmeAfterSaleQuotationHeaderVO10 = esbReturn.get(0);
        //如果成功，则更新表数据
        if("S".equals(hmeAfterSaleQuotationHeaderVO10.getTYPE())){
            String quotationCode = hmeAfterSaleQuotationHeaderVO10.getQUOTATION_NO();
            String quotationHeaderId = headData.getQuotationHeaderId();
            HmeAfterSaleQuotationHeader hmeAfterSaleQuotationHeader = new HmeAfterSaleQuotationHeader();
            hmeAfterSaleQuotationHeader.setQuotationHeaderId(quotationHeaderId);
            hmeAfterSaleQuotationHeader.setQuotationCode(quotationCode);
            hmeAfterSaleQuotationHeader.setStatus("SUBMIT");
            hmeAfterSaleQuotationHeader.setSubmissionBy(userId);
            hmeAfterSaleQuotationHeader.setSubmissionData(new Date());
            hmeAfterSaleQuotationHeaderMapper.updateByPrimaryKeySelective(hmeAfterSaleQuotationHeader);
        }
        //将返回消息返回
        HmeAfterSaleQuotationHeaderVO13 result = new HmeAfterSaleQuotationHeaderVO13();
        result.setType(hmeAfterSaleQuotationHeaderVO10.getTYPE());
        result.setMessage(hmeAfterSaleQuotationHeaderVO10.getMESSAGE());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeAfterSaleQuotationHeaderVO13 edit(Long tenantId, HmeAfterSaleQuotationHeaderVO3 dto) {
        HmeAfterSaleQuotationHeaderVO6 headData = dto.getHeadData();
        HmeAfterSaleQuotationHeader hmeAfterSaleQuotationHeader = hmeAfterSaleQuotationHeaderRepository.selectByPrimaryKey(headData.getQuotationHeaderId());
        if(!"SUBMIT".equals(hmeAfterSaleQuotationHeader.getStatus())){
            //只有提交状态的报价单才允许修改
            throw new MtException("HME_QUOTATION_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_QUOTATION_009", "HME"));
        }
        List<HmeAfterSaleQuotationHeaderVO4> opticsLineList = dto.getOpticsLineList();
        List<HmeAfterSaleQuotationHeaderVO4> electricLineList = dto.getElectricLineList();
        List<HmeAfterSaleQuotationHeaderVO4> hourFeeLineList = dto.getHourFeeLineList();
        //校验行数据中是否有物料重复
        List<String> materialIdList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(opticsLineList)){
            materialIdList.addAll(opticsLineList.stream().map(HmeAfterSaleQuotationHeaderVO4::getMaterialId).collect(Collectors.toList()));
        }
        if(CollectionUtils.isNotEmpty(electricLineList)){
            materialIdList.addAll(electricLineList.stream().map(HmeAfterSaleQuotationHeaderVO4::getMaterialId).collect(Collectors.toList()));
        }
        if(CollectionUtils.isNotEmpty(hourFeeLineList)){
            materialIdList.addAll(hourFeeLineList.stream().map(HmeAfterSaleQuotationHeaderVO4::getMaterialId).collect(Collectors.toList()));
        }
        if(CollectionUtils.isNotEmpty(materialIdList)){
            int size = materialIdList.size();
            materialIdList = materialIdList.stream().distinct().collect(Collectors.toList());
            int distinctSize = materialIdList.size();
            if(size != distinctSize){
                throw new MtException("HME_QUOTATION_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_QUOTATION_006", "HME"));
            }
        }
        //组装头数据
        List<HmeAfterSaleQuotationHeaderVO11> headerList = new ArrayList<>();
        HmeAfterSaleQuotationHeaderVO11 header = new HmeAfterSaleQuotationHeaderVO11();
        //报价单号
        String quotationCode = headData.getQuotationCode();
        header.setQUOTATION_NO(quotationCode);
        //售达方编码 若无修改则该值不传，有修改时才传
        if(!hmeAfterSaleQuotationHeader.getSoldTo().equals(headData.getSoldTo())){
            MtCustomer mtCustomer = mtCustomerRepository.selectByPrimaryKey(headData.getSoldTo());
            header.setPARTN_NUMB(mtCustomer.getCustomerCode());
        }
        //组装行数据
        List<HmeAfterSaleQuotationHeaderVO12> lineList = new ArrayList<>();
        //2021-10-21 10:36 edit by chaonan.hu for wenxin.zhang 传递给SAP的修改数据中只会存在标识为I、D、U中的一种类型数据
        if(CollectionUtils.isNotEmpty(dto.getDeleteLineList())){
            //如果删除物料集合不为空时，传递给SAP的只有标识为D的物料  业务保证此时不会有插入或更新物料的操作同时存在
            for (HmeAfterSaleQuotationHeaderVO4 deleteLine:dto.getDeleteLineList()) {
                HmeAfterSaleQuotationHeaderVO12 line = new HmeAfterSaleQuotationHeaderVO12();
                line.setMATERIAL(deleteLine.getMaterialCode());
                line.setREQ_QTY(deleteLine.getRequsetQty());
                line.setZTEXT(deleteLine.getRemark());
                line.setUPDATEFLAG("D");
                line.setZREJ("");
                lineList.add(line);
            }
        }else {
            //如果删除物料集合不为空时，判断两个类型行物料中是否有插入物料的数据
            List<HmeAfterSaleQuotationHeaderVO4> insertLineList = new ArrayList<>();
            insertLineList.addAll(dto.getOpticsLineList().stream().filter(item -> StringUtils.isBlank(item.getQuotationLineId())).collect(Collectors.toList()));
            insertLineList.addAll(dto.getElectricLineList().stream().filter(item -> StringUtils.isBlank(item.getQuotationLineId())).collect(Collectors.toList()));
            insertLineList.addAll(dto.getHourFeeLineList().stream().filter(item -> StringUtils.isBlank(item.getQuotationLineId())).collect(Collectors.toList()));
            if(CollectionUtils.isNotEmpty(insertLineList)){
                //如果有插入物料的数据，则传递给SAP的只有标识为I的物料
                for (HmeAfterSaleQuotationHeaderVO4 opticsLine:insertLineList) {
                    HmeAfterSaleQuotationHeaderVO12 line = new HmeAfterSaleQuotationHeaderVO12();
                    line.setMATERIAL(opticsLine.getMaterialCode());
                    line.setREQ_QTY(opticsLine.getRequsetQty());
                    line.setZTEXT(opticsLine.getRemark());
                    line.setUPDATEFLAG("I");
                    line.setZREJ("");
                    lineList.add(line);
                }
            }else{
                //如果没有插入物料的数据，则传递给SAP的只有标识为U的物料
                List<HmeAfterSaleQuotationHeaderVO4> updateLineList = new ArrayList<>();
                updateLineList.addAll(dto.getOpticsLineList());
                updateLineList.addAll(dto.getElectricLineList());
                updateLineList.addAll(dto.getHourFeeLineList());
                for (HmeAfterSaleQuotationHeaderVO4 opticsLine:updateLineList) {
                    HmeAfterSaleQuotationHeaderVO12 line = new HmeAfterSaleQuotationHeaderVO12();
                    line.setMATERIAL(opticsLine.getMaterialCode());
                    line.setREQ_QTY(opticsLine.getRequsetQty());
                    line.setZTEXT(opticsLine.getRemark());
                    line.setUPDATEFLAG("U");
                    line.setZREJ("");
                    lineList.add(line);
                }
            }
        }
        Map<String, Object> requestInfoMap = new HashMap<>();
        headerList.add(header);
        requestInfoMap.put("header", headerList);
        requestInfoMap.put("item", lineList);
        //调用SAP售后单修改接口
        Map<String, Object> map = sendESBConnect.sendEsb(requestInfoMap, "",
                "HmeAfterSaleQuotationHeaderServiceImpl.edit", ItfConstant.InterfaceCode.ESB_AFTER_SALE_QUOTATION_EDIT);
        log.info("sendESBConnect return data:{}", JSON.toJSONString(map));
        String esbReturnStr = JSON.toJSON(map.get("RETURN")).toString();
        List<HmeAfterSaleQuotationHeaderVO10> esbReturn = JSONArray.parseArray(esbReturnStr, HmeAfterSaleQuotationHeaderVO10.class);
        HmeAfterSaleQuotationHeaderVO10 hmeAfterSaleQuotationHeaderVO10 = esbReturn.get(0);
        //如果成功，则更新表数据
        if("S".equals(hmeAfterSaleQuotationHeaderVO10.getTYPE())){
            //返回成功则更新MES的头行表
            hmeAfterSaleQuotationHeaderRepository.storageStatusSave(tenantId, dto);
        }
        HmeAfterSaleQuotationHeaderVO13 result = new HmeAfterSaleQuotationHeaderVO13();
        result.setType(hmeAfterSaleQuotationHeaderVO10.getTYPE());
        result.setMessage(hmeAfterSaleQuotationHeaderVO10.getMESSAGE());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeAfterSaleQuotationHeaderVO13 cancel(Long tenantId, HmeAfterSaleQuotationHeaderVO3 dto) {
        if(StringUtils.isBlank(dto.getCancelReason())){
            //拒绝原因为空,不允许取消
            throw new MtException("HME_QUOTATION_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_QUOTATION_011", "HME"));
        }
        HmeAfterSaleQuotationHeaderVO6 headData = dto.getHeadData();
        HmeAfterSaleQuotationHeader hmeAfterSaleQuotationHeader = hmeAfterSaleQuotationHeaderRepository.selectByPrimaryKey(headData.getQuotationHeaderId());
        if("CANCEL".equals(hmeAfterSaleQuotationHeader.getStatus())){
            //报价单已取消,无需重复取消
            throw new MtException("HME_QUOTATION_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_QUOTATION_010", "HME"));
        }
        //如果报价单是提交状态，则先调用SAP接口
        if("SUBMIT".equals(hmeAfterSaleQuotationHeader.getStatus())){
            //组装头数据
            List<HmeAfterSaleQuotationHeaderVO11> headerList = new ArrayList<>();
            HmeAfterSaleQuotationHeaderVO11 header = new HmeAfterSaleQuotationHeaderVO11();
            //报价单号
            String quotationCode = headData.getQuotationCode();
            header.setQUOTATION_NO(quotationCode);
            //组装行数据
            List<HmeAfterSaleQuotationHeaderVO12> lineList = new ArrayList<>();
            for (HmeAfterSaleQuotationHeaderVO4 opticsLine:dto.getOpticsLineList()) {
                HmeAfterSaleQuotationHeaderVO12 line = new HmeAfterSaleQuotationHeaderVO12();
                line.setMATERIAL(opticsLine.getMaterialCode());
                line.setREQ_QTY(opticsLine.getRequsetQty());
                line.setZTEXT(opticsLine.getRemark());
                line.setUPDATEFLAG("U");
                line.setZREJ(dto.getCancelReason());
                lineList.add(line);
            }
            for (HmeAfterSaleQuotationHeaderVO4 electricLine:dto.getElectricLineList()) {
                HmeAfterSaleQuotationHeaderVO12 line = new HmeAfterSaleQuotationHeaderVO12();
                line.setMATERIAL(electricLine.getMaterialCode());
                line.setREQ_QTY(electricLine.getRequsetQty());
                line.setZTEXT(electricLine.getRemark());
                line.setUPDATEFLAG("U");
                line.setZREJ(dto.getCancelReason());
                lineList.add(line);
            }
            for (HmeAfterSaleQuotationHeaderVO4 hourFeeLine:dto.getHourFeeLineList()) {
                HmeAfterSaleQuotationHeaderVO12 line = new HmeAfterSaleQuotationHeaderVO12();
                line.setMATERIAL(hourFeeLine.getMaterialCode());
                line.setREQ_QTY(hourFeeLine.getRequsetQty());
                line.setZTEXT(hourFeeLine.getRemark());
                line.setUPDATEFLAG("U");
                line.setZREJ(dto.getCancelReason());
                lineList.add(line);
            }
            Map<String, Object> requestInfoMap = new HashMap<>();
            headerList.add(header);
            requestInfoMap.put("header", headerList);
            requestInfoMap.put("item", lineList);
            //调用SAP售后单修改接口
            Map<String, Object> map = sendESBConnect.sendEsb(requestInfoMap, "",
                    "HmeAfterSaleQuotationHeaderServiceImpl.edit", ItfConstant.InterfaceCode.ESB_AFTER_SALE_QUOTATION_EDIT);
            log.info("sendESBConnect return data:{}", JSON.toJSONString(map));
            String esbReturnStr = JSON.toJSON(map.get("RETURN")).toString();
            List<HmeAfterSaleQuotationHeaderVO10> esbReturn = JSONArray.parseArray(esbReturnStr, HmeAfterSaleQuotationHeaderVO10.class);
            HmeAfterSaleQuotationHeaderVO10 hmeAfterSaleQuotationHeaderVO10 = esbReturn.get(0);
            //如果成功，则更新表数据
            if("S".equals(hmeAfterSaleQuotationHeaderVO10.getTYPE())){
                //更新头的状态为取消
                hmeAfterSaleQuotationHeader.setStatus("CANCEL");
                hmeAfterSaleQuotationHeader.setAttribute1(dto.getCancelReason());
                hmeAfterSaleQuotationHeaderMapper.updateByPrimaryKeySelective(hmeAfterSaleQuotationHeader);
            }
            HmeAfterSaleQuotationHeaderVO13 result = new HmeAfterSaleQuotationHeaderVO13();
            result.setType(hmeAfterSaleQuotationHeaderVO10.getTYPE());
            result.setMessage(hmeAfterSaleQuotationHeaderVO10.getMESSAGE());
            return result;
        }else {
            //更新头的状态为取消
            hmeAfterSaleQuotationHeader.setStatus("CANCEL");
            hmeAfterSaleQuotationHeader.setAttribute1(dto.getCancelReason());
            hmeAfterSaleQuotationHeaderMapper.updateByPrimaryKeySelective(hmeAfterSaleQuotationHeader);
            HmeAfterSaleQuotationHeaderVO13 result = new HmeAfterSaleQuotationHeaderVO13();
            result.setType("S");
            result.setMessage("取消成功");
            return result;
        }
    }
}
