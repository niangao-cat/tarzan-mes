package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeAfterSaleQuotationLine;
import com.ruike.hme.domain.entity.HmeServiceReceive;
import com.ruike.hme.domain.entity.HmeServiceSplitRecord;
import com.ruike.hme.domain.repository.HmeAfterSaleQuotationLineRepository;
import com.ruike.hme.domain.repository.HmeServiceReceiveRepository;
import com.ruike.hme.domain.repository.HmeServiceSplitRecordRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.mapper.HmeAfterSaleQuotationHeaderMapper;
import com.ruike.hme.infra.mapper.HmeAfterSaleQuotationLineMapper;
import com.ruike.hme.infra.mapper.HmeQuantityAnalyzeLineMapper;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeAfterSaleQuotationHeader;
import com.ruike.hme.domain.repository.HmeAfterSaleQuotationHeaderRepository;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.modeling.domain.entity.MtCustomer;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtCustomerRepository;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 售后报价单头表 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2021-09-26 11:07:31
 */
@Component
public class HmeAfterSaleQuotationHeaderRepositoryImpl extends BaseRepositoryImpl<HmeAfterSaleQuotationHeader> implements HmeAfterSaleQuotationHeaderRepository {

    @Autowired
    private HmeAfterSaleQuotationHeaderMapper hmeAfterSaleQuotationHeaderMapper;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private HmeServiceReceiveRepository hmeServiceReceiveRepository;
    @Autowired
    private HmeServiceSplitRecordRepository hmeServiceSplitRecordRepository;
    @Autowired
    private MtCustomerRepository mtCustomerRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtUserClient userClient;

    @Override
    public HmeAfterSaleQuotationHeaderVO2 serviceReceiveAndRecordInfoQuery(Long tenantId, String snNum) {
        HmeAfterSaleQuotationHeaderVO2 result = new HmeAfterSaleQuotationHeaderVO2();
        List<String> statusList = new ArrayList<>();
        statusList.add("STORAGE");
        statusList.add("SUBMIT");
        statusList.add("NEW");
        List<HmeAfterSaleQuotationHeader> hmeAfterSaleQuotationHeaderList = new ArrayList<>();
        //根据扫描SN作为sn_num在表hme_service_receive查询数据
        List<HmeServiceReceive> hmeServiceReceiveList = new ArrayList<>();
        hmeServiceReceiveList.addAll(hmeAfterSaleQuotationHeaderMapper.serviceReciveQueryBySnNumOrMaterialLot(tenantId, snNum, null));
        //如果扫描SN可以找到物料批ID，则根据物料批ID在表hme_service_receive查询数据
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setMaterialLotCode(snNum);
            setTenantId(tenantId);
        }});
        if(Objects.nonNull(mtMaterialLot)){
            hmeServiceReceiveList.addAll(hmeAfterSaleQuotationHeaderMapper.serviceReciveQueryBySnNumOrMaterialLot(tenantId, null, mtMaterialLot.getMaterialLotId()));
            result.setMtMaterialLot(mtMaterialLot);
        }
        if(CollectionUtils.isNotEmpty(hmeServiceReceiveList)){
            //serviceReceiveId取创建时间最大的那一个
            hmeServiceReceiveList = hmeServiceReceiveList.stream().sorted(Comparator.comparing(HmeServiceReceive::getCreationDate).reversed()).collect(Collectors.toList());
            //2021-11-09 edit by chaonan.hu for wenxin.zhang 如果创建时间最大的那个serviceReceiveId在表hme_service_split_record中不存在，则报错
            String serviceReceiveId = hmeServiceReceiveList.get(0).getServiceReceiveId();
            int count = hmeServiceSplitRecordRepository.selectCount(new HmeServiceSplitRecord() {{
                setServiceReceiveId(serviceReceiveId);
                setTenantId(tenantId);
            }});
            if(count == 0){
                //报错 该条码找不到拆机信息
                throw new MtException("HME_QUOTATION_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_QUOTATION_002", "HME"));
            }
            hmeAfterSaleQuotationHeaderList.addAll(hmeAfterSaleQuotationHeaderMapper.getQuotationHeaderByServiceReceive(tenantId, serviceReceiveId, statusList));
        }
        if(CollectionUtils.isNotEmpty(hmeAfterSaleQuotationHeaderList)){
            //如果找到数据，则为场景一（扫描SN已报价）,取创建时间最大的hmeAfterSaleQuotationHeader
            hmeAfterSaleQuotationHeaderList = hmeAfterSaleQuotationHeaderList.stream().sorted(Comparator.comparing(HmeAfterSaleQuotationHeader::getCreationDate).reversed()).collect(Collectors.toList());
            HmeAfterSaleQuotationHeader hmeAfterSaleQuotationHeader = this.selectByPrimaryKey(hmeAfterSaleQuotationHeaderList.get(0).getQuotationHeaderId());
            String serviceReceiveId = hmeAfterSaleQuotationHeader.getServiceReceiveId();
            HmeServiceReceive hmeServiceReceive = hmeServiceReceiveRepository.selectByPrimaryKey(serviceReceiveId);
            HmeServiceSplitRecord hmeServiceSplitRecord = hmeAfterSaleQuotationHeaderMapper.getServiceSplitRecordByServiceReceiveId(tenantId, serviceReceiveId);
            if(Objects.isNull(hmeServiceSplitRecord)){
                //报错 该条码找不到拆机信息
                throw new MtException("HME_QUOTATION_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_QUOTATION_002", "HME"));
            }
            result.setHmeAfterSaleQuotationHeader(hmeAfterSaleQuotationHeader);
            result.setHmeServiceReceive(hmeServiceReceive);
            result.setHmeServiceSplitRecord(hmeServiceSplitRecord);
        }else {
            //如果找不到数据，则为场景二（扫描SN未报价）
            List<HmeAfterSaleQuotationHeaderVO> serviceReceiveIdList = new ArrayList<>();
            if(Objects.isNull(mtMaterialLot)){
                //如果扫描SN不为MES物料批，则根据SN编码查询符合条件的接收拆箱登记表主键以及售后返品拆机表主键
                serviceReceiveIdList = hmeAfterSaleQuotationHeaderMapper.getServiceReceiveBySnNum(tenantId, snNum);
                if(CollectionUtils.isEmpty(serviceReceiveIdList)){
                    //报错当前SN未进行售后登记
                    throw new MtException("HME_QUOTATION_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_QUOTATION_003", "HME"));
                }
            }else {
                //如果扫描SN为MES物料批，则首先根据物料批ID、站点查询符合条件的接收拆箱登记表主键以及售后返品拆机表主键
                serviceReceiveIdList.addAll(hmeAfterSaleQuotationHeaderMapper.getServiceReceiveByMaterialLot(tenantId,
                        mtMaterialLot.getMaterialLotId(), mtMaterialLot.getSiteId()));
                //再根据SN编码查询符合条件的接收拆箱登记表主键以及售后返品拆机表主键
                serviceReceiveIdList.addAll(hmeAfterSaleQuotationHeaderMapper.getServiceReceiveBySnNum(tenantId, snNum));
                if(CollectionUtils.isEmpty(serviceReceiveIdList)){
                    //报错接收SN未进行售后登记
                    throw new MtException("HME_QUOTATION_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_QUOTATION_004", "HME"));
                }
            }
            //根据物流信息表主键查询出创建时间最晚的那一条物流信息表主键,进而取出最晚物流信息表主键所对应的接收拆箱登记表主键和售后返品拆机表主键
            List<String> logisticsInfoIdList = serviceReceiveIdList.stream().map(HmeAfterSaleQuotationHeaderVO::getLogisticsInfoId).distinct().collect(Collectors.toList());
            String creationDateMaxLogisticsInfoId = hmeAfterSaleQuotationHeaderMapper.getCreationDateMaxLogisticsInfoId(tenantId, logisticsInfoIdList);
            serviceReceiveIdList = serviceReceiveIdList.stream()
                    .filter(item -> creationDateMaxLogisticsInfoId.equals(item.getLogisticsInfoId()))
                    .collect(Collectors.toList());
            String serviceReceiveId = serviceReceiveIdList.get(0).getServiceReceiveId();
            String splitRecordId = serviceReceiveIdList.get(0).getSplitRecordId();
            HmeServiceReceive hmeServiceReceive = hmeServiceReceiveRepository.selectByPrimaryKey(serviceReceiveId);
            HmeServiceSplitRecord hmeServiceSplitRecord = hmeServiceSplitRecordRepository.selectByPrimaryKey(splitRecordId);
            result.setHmeServiceReceive(hmeServiceReceive);
            result.setHmeServiceSplitRecord(hmeServiceSplitRecord);
        }
        return result;
    }

    @Override
    public HmeAfterSaleQuotationHeaderVO3 queryHeadData(Long tenantId, String snNum, HmeAfterSaleQuotationHeaderVO2 hmeAfterSaleQuotationHeaderVO2) throws ParseException {
        HmeAfterSaleQuotationHeader hmeAfterSaleQuotationHeader = hmeAfterSaleQuotationHeaderVO2.getHmeAfterSaleQuotationHeader();
        HmeServiceReceive hmeServiceReceive = hmeAfterSaleQuotationHeaderVO2.getHmeServiceReceive();
        HmeServiceSplitRecord hmeServiceSplitRecord = hmeAfterSaleQuotationHeaderVO2.getHmeServiceSplitRecord();
        MtMaterialLot snMaterialLot = hmeAfterSaleQuotationHeaderVO2.getMtMaterialLot();
        HmeAfterSaleQuotationHeaderVO3 result = new HmeAfterSaleQuotationHeaderVO3();
        HmeAfterSaleQuotationHeaderVO6 headData = new HmeAfterSaleQuotationHeaderVO6();
        headData.setSnNum(snNum);
        headData.setServiceReceiveId(hmeServiceReceive.getServiceReceiveId());
        headData.setSplitRecordId(hmeServiceSplitRecord.getSplitRecordId());
        if(Objects.isNull(hmeAfterSaleQuotationHeader)){
            //送达方 场景二
            MtCustomer mtCustomer = hmeAfterSaleQuotationHeaderMapper.sendToQueryByServiceReceiveId(tenantId, hmeServiceReceive.getServiceReceiveId());
            if(Objects.nonNull(mtCustomer)){
                headData.setSendTo(mtCustomer.getCustomerId());
                headData.setSendToName(mtCustomer.getCustomerName());
                //售达方 场景二 与送达方一样
                headData.setSoldTo(mtCustomer.getCustomerId());
                headData.setSoldToName(mtCustomer.getCustomerName());
            }
        }else {
            headData.setQuotationHeaderId(hmeAfterSaleQuotationHeader.getQuotationHeaderId());
            //报价单号
            headData.setQuotationCode(hmeAfterSaleQuotationHeader.getQuotationCode());
            //送达方 场景一
            MtCustomer sendToCustomer = mtCustomerRepository.selectByPrimaryKey(hmeAfterSaleQuotationHeader.getSendTo());
            headData.setSendTo(hmeAfterSaleQuotationHeader.getSendTo());
            headData.setSendToName(sendToCustomer.getCustomerName());
            //售达方 场景一
            MtCustomer soldToCustomer = mtCustomerRepository.selectByPrimaryKey(hmeAfterSaleQuotationHeader.getSoldTo());
            headData.setSoldTo(hmeAfterSaleQuotationHeader.getSoldTo());
            headData.setSoldToName(soldToCustomer.getCustomerName());
            //提交时间
            headData.setSubmissionData(hmeAfterSaleQuotationHeader.getSubmissionData());
            //最后修改时间
            headData.setLastUpdateDate(hmeAfterSaleQuotationHeader.getLastUpdateDate());
            //上一次报价时间
            headData.setLastOfferDate(hmeAfterSaleQuotationHeaderVO2.getLastOfferDate());
            //报价单状态
            headData.setStatus(hmeAfterSaleQuotationHeader.getStatus());
            //最后更新人
            Long lastUpdatedBy = hmeAfterSaleQuotationHeader.getLastUpdatedBy();
            headData.setLastUpdatedBy(lastUpdatedBy);
            MtUserInfo mtUserInfo = userClient.userInfoGet(tenantId, lastUpdatedBy);
            if(Objects.nonNull(mtUserInfo)){
                headData.setLastUpdatedByName(mtUserInfo.getRealName());
            }
        }
        //产品编码
        if(StringUtils.isNotBlank(hmeServiceReceive.getMaterialId())){
            headData.setMaterialId(hmeServiceReceive.getMaterialId());
            //根据物料ID、站点ID查询物料编码、描述
            MtMaterial mtMaterial = hmeAfterSaleQuotationHeaderMapper.getMaterialInfoByMaterialSite(tenantId, hmeServiceReceive.getMaterialId(), hmeServiceReceive.getSiteId());
            if(Objects.nonNull(mtMaterial)){
                headData.setMaterialCode(mtMaterial.getMaterialCode());
                headData.setMaterialName(mtMaterial.getMaterialName());
                //机型
                String model = hmeAfterSaleQuotationHeaderMapper.getEquipmentModelByMaterialSite(tenantId, headData.getMaterialId(), hmeServiceReceive.getSiteId());
                headData.setModel(model);
            }
        }
        //返回类型
        String backType = hmeServiceSplitRecord.getBackType();
        headData.setBackType(backType);
        if(StringUtils.isNotBlank(backType)){
            String backTypeMeaning = lovAdapter.queryLovMeaning("HME.BACK_TYPE", tenantId, backType);
            headData.setBackTypeMeaning(backTypeMeaning);
        }
        //工单号
        headData.setWorkOrderNum(hmeServiceSplitRecord.getWorkOrderNum());
        //当前工位
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeServiceSplitRecord.getMaterialLotId());
        MtModWorkcell mtModWorkcell = hmeAfterSaleQuotationHeaderMapper.currentWorkcellQuery(tenantId, mtMaterialLot.getMaterialLotCode());
        if(Objects.nonNull(mtModWorkcell)){
            headData.setWorkcellId(mtModWorkcell.getWorkcellId());
            headData.setWorkcellCode(mtModWorkcell.getWorkcellCode());
            headData.setWorkcellName(mtModWorkcell.getWorkcellName());
        }
        //最后SN状态
        String receiveStatus = hmeServiceReceive.getReceiveStatus();
        headData.setReceiveStatus(receiveStatus);
        String receiveStatusMeaning = lovAdapter.queryLovMeaning("HME.RECEIVE_STATUS", tenantId, receiveStatus);
        if(StringUtils.isNotBlank(receiveStatusMeaning)){
            headData.setReceiveStatusMeaning(receiveStatusMeaning);
        }
        //上一次发货时间
        Date lastSendDate = hmeAfterSaleQuotationHeaderMapper.lastSendDateQueryBySnNum(tenantId, snNum);
        headData.setLastSendDate(lastSendDate);
        if(StringUtils.isNotBlank(headData.getStatus())){
            String statusMeaning = lovAdapter.queryLovMeaning("HME.QUOTATION_STATUS", tenantId, headData.getStatus());
            if(StringUtils.isNotBlank(statusMeaning)){
                headData.setStatusMeaning(statusMeaning);
            }
        }
        //上一次报价时间
        if(StringUtils.isNotBlank(headData.getSendTo())){
            List<HmeAfterSaleQuotationHeader> lastOfferDateList = new ArrayList<>();
            lastOfferDateList.addAll(this.selectByCondition(Condition.builder(HmeAfterSaleQuotationHeader.class)
                    .select(HmeAfterSaleQuotationHeader.FIELD_QUOTATION_HEADER_ID, HmeAfterSaleQuotationHeader.FIELD_LAST_UPDATE_DATE)
                    .andWhere(Sqls.custom().andEqualTo(HmeAfterSaleQuotationHeader.FIELD_SN_NUM, snNum)
                            .andEqualTo(HmeAfterSaleQuotationHeader.FIELD_SEND_TO, headData.getSendTo())
                            .andEqualTo(HmeAfterSaleQuotationHeader.FIELD_STATUS, "SUBMIT")
                            .andEqualTo(HmeAfterSaleQuotationHeader.FIELD_TENANT_ID, tenantId))
                    .build()));
            if(Objects.nonNull(snMaterialLot)){
                lastOfferDateList.addAll(this.selectByCondition(Condition.builder(HmeAfterSaleQuotationHeader.class)
                        .select(HmeAfterSaleQuotationHeader.FIELD_QUOTATION_HEADER_ID, HmeAfterSaleQuotationHeader.FIELD_LAST_UPDATE_DATE)
                        .andWhere(Sqls.custom().andEqualTo(HmeAfterSaleQuotationHeader.FIELD_MATERIAL_LOT_ID, snMaterialLot.getMaterialLotId())
                                .andEqualTo(HmeAfterSaleQuotationHeader.FIELD_SEND_TO, headData.getSendTo())
                                .andEqualTo(HmeAfterSaleQuotationHeader.FIELD_STATUS, "SUBMIT")
                                .andEqualTo(HmeAfterSaleQuotationHeader.FIELD_TENANT_ID, tenantId))
                        .build()));
            }
            if(Objects.nonNull(hmeAfterSaleQuotationHeader)){
                lastOfferDateList = lastOfferDateList.stream()
                        .filter(item -> !item.getQuotationHeaderId().equals(hmeAfterSaleQuotationHeader.getQuotationHeaderId()))
                        .sorted(Comparator.comparing(HmeAfterSaleQuotationHeader::getLastUpdateDate).reversed())
                        .collect(Collectors.toList());
            }else {
                lastOfferDateList = lastOfferDateList.stream()
                        .sorted(Comparator.comparing(HmeAfterSaleQuotationHeader::getLastUpdateDate).reversed())
                        .collect(Collectors.toList());
            }
            if(CollectionUtils.isNotEmpty(lastOfferDateList)){
                headData.setLastOfferDate(lastOfferDateList.get(0).getLastUpdateDate());
            }
        }
        //是否保质期内
        if(Objects.nonNull(lastSendDate) && Objects.nonNull(headData.getLastOfferDate())){
            Date nowDate = new Date();
            BigDecimal betweenDays = daysBetween(lastSendDate, nowDate);
            String machine = lovAdapter.queryLovMeaning("HME.AFTER_SALES_WARRANTY", tenantId, "MACHINE");
            if(betweenDays.compareTo(new BigDecimal(machine)) > 0){
                headData.setQualityFlag("否");
            }else {
                headData.setQualityFlag("是");
                //弹窗消息
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
                String lastSendDateStr = "";
                if(Objects.nonNull(lastSendDate)){
                    lastSendDateStr = sdf.format(lastSendDate);
                }
                String lastOfferDateStr = "";
                if(Objects.nonNull(headData.getLastOfferDate())){
                    lastOfferDateStr = sdf.format(headData.getLastOfferDate());
                }
                headData.setMessage("该条码"+ snNum + ", 客户为" + headData.getSendToName() + ", 在"+ lastOfferDateStr +"时间报过价, 发货时间是" + lastSendDateStr);
            }
        }else{
            headData.setQualityFlag("否");
        }
        result.setHeadData(headData);
        return  result;
    }

    @Override
    public List<HmeAfterSaleQuotationHeaderVO4> queryLineData(Long tenantId, HmeAfterSaleQuotationHeaderVO2 hmeAfterSaleQuotationHeaderVO2,
                                                              String demandType, String sendTo) throws ParseException {
        HmeAfterSaleQuotationHeader hmeAfterSaleQuotationHeader = hmeAfterSaleQuotationHeaderVO2.getHmeAfterSaleQuotationHeader();
        if(Objects.isNull(hmeAfterSaleQuotationHeader)){
            return null;
        }
        List<HmeAfterSaleQuotationHeaderVO4> lineDataList = hmeAfterSaleQuotationHeaderMapper.lineDataQuery(tenantId, hmeAfterSaleQuotationHeader.getQuotationHeaderId(), demandType);
        MtMaterialLot mtMaterialLot = hmeAfterSaleQuotationHeaderVO2.getMtMaterialLot();
        if(CollectionUtils.isNotEmpty(lineDataList) && (("OPTICS".equals(demandType)) || "ELECTRIC".equals(demandType))
                && StringUtils.isNotBlank(sendTo) && Objects.nonNull(hmeAfterSaleQuotationHeader)){
            String componentStr = lovAdapter.queryLovMeaning("HME.AFTER_SALES_WARRANTY", tenantId, "COMPONENT");
            BigDecimal component = new BigDecimal(componentStr);
            Date nowDate = new Date();
            //查询质保内发货日期
            List<String> materialIdList = lineDataList.stream().map(HmeAfterSaleQuotationHeaderVO4::getMaterialId).distinct().collect(Collectors.toList());
            List<HmeAfterSaleQuotationHeaderVO5> headerUpdateDateList = new ArrayList<>();
            if(Objects.nonNull(mtMaterialLot)){
                headerUpdateDateList = hmeAfterSaleQuotationHeaderMapper.quotationHeaderUpdateDateQuery(tenantId, mtMaterialLot.getMaterialLotId(), null, sendTo,
                        hmeAfterSaleQuotationHeader.getQuotationHeaderId(), materialIdList);
            }
            if(CollectionUtils.isEmpty(headerUpdateDateList)){
                headerUpdateDateList = hmeAfterSaleQuotationHeaderMapper.quotationHeaderUpdateDateQuery(tenantId, null, hmeAfterSaleQuotationHeader.getSnNum(), sendTo,
                        hmeAfterSaleQuotationHeader.getQuotationHeaderId(), materialIdList);
            }
            if(CollectionUtils.isNotEmpty(headerUpdateDateList)){
                for (HmeAfterSaleQuotationHeaderVO4 hmeAfterSaleQuotationHeaderVO4:lineDataList) {
                    List<HmeAfterSaleQuotationHeaderVO5> singleHeaderUpdateDateList = headerUpdateDateList.stream()
                            .filter(item -> hmeAfterSaleQuotationHeaderVO4.getMaterialId().equals(item.getMaterialId()))
                            .sorted(Comparator.comparing(HmeAfterSaleQuotationHeaderVO5::getLastUpdateDate))
                            .collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(singleHeaderUpdateDateList)){
                        Date lastUpdateDate = singleHeaderUpdateDateList.get(0).getLastUpdateDate();
                        //根据sernr、lastUpdateDate查询接口表中满足条件的日期
                        List<Date> ifaceLastUpdateDateList = hmeAfterSaleQuotationHeaderMapper.snSapIfaceUpdateDateQuery(tenantId, hmeAfterSaleQuotationHeader.getSnNum(), lastUpdateDate);
                        if(CollectionUtils.isNotEmpty(ifaceLastUpdateDateList)){
                            List<Date> finalIfaceLastUpdateDateList = new ArrayList<>();
                            for (Date ifaceLastUpdateDate:ifaceLastUpdateDateList) {
                                BigDecimal bigDecimal = daysBetween(ifaceLastUpdateDate, nowDate);
                                //如果当前时间-lastUpdateDate < 组件质保期，则此lastUpdateDate符合要求，从中取出最大的lastUpdateDate作为质保内发货日期
                                if(bigDecimal.compareTo(component) < 0){
                                    finalIfaceLastUpdateDateList.add(ifaceLastUpdateDate);
                                }
                            }
                            if(CollectionUtils.isNotEmpty(finalIfaceLastUpdateDateList)){
                                finalIfaceLastUpdateDateList = finalIfaceLastUpdateDateList.stream().sorted(Comparator.comparing(Date::getTime).reversed()).collect(Collectors.toList());
                                hmeAfterSaleQuotationHeaderVO4.setSendDate(finalIfaceLastUpdateDateList.get(0));
                            }
                        }
                    }
                }
            }
        }
        return lineDataList;
    }

    @Override
    public HmeAfterSaleQuotationHeaderVO2 serviceReceiveAndRecordInfoCreateQuery(Long tenantId, String snNum) {
        HmeAfterSaleQuotationHeaderVO2 result = new HmeAfterSaleQuotationHeaderVO2();
        //按照场景二（扫描SN未报价）查询数据
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setMaterialLotCode(snNum);
            setTenantId(tenantId);
        }});
        List<HmeAfterSaleQuotationHeaderVO> serviceReceiveIdList = new ArrayList<>();
        if(Objects.isNull(mtMaterialLot)){
            //如果扫描SN不为MES物料批，则根据SN编码查询符合条件的接收拆箱登记表主键以及售后返品拆机表主键
            serviceReceiveIdList = hmeAfterSaleQuotationHeaderMapper.getServiceReceiveBySnNum(tenantId, snNum);
            if(CollectionUtils.isEmpty(serviceReceiveIdList)){
                if(CollectionUtils.isEmpty(serviceReceiveIdList)){
                    //报错当前SN未进行售后登记
                    throw new MtException("HME_QUOTATION_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_QUOTATION_003", "HME"));
                }
            }

        }else {
            result.setMtMaterialLot(mtMaterialLot);
            //如果扫描SN为MES物料批，则首先根据物料批ID、站点查询符合条件的接收拆箱登记表主键以及售后返品拆机表主键
            serviceReceiveIdList.addAll(hmeAfterSaleQuotationHeaderMapper.getServiceReceiveByMaterialLot(tenantId,
                    mtMaterialLot.getMaterialLotId(), mtMaterialLot.getSiteId()));
            //再根据SN编码查询符合条件的接收拆箱登记表主键以及售后返品拆机表主键
            serviceReceiveIdList.addAll(hmeAfterSaleQuotationHeaderMapper.getServiceReceiveBySnNum(tenantId, snNum));
            if(CollectionUtils.isEmpty(serviceReceiveIdList)){
                //报错接收SN未进行售后登记
                throw new MtException("HME_QUOTATION_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_QUOTATION_004", "HME"));
            }
        }
        //根据物流信息表主键查询出创建时间最晚的那一条物流信息表主键,进而取出最晚物流信息表主键所对应的接收拆箱登记表主键和售后返品拆机表主键
        List<String> logisticsInfoIdList = serviceReceiveIdList.stream().map(HmeAfterSaleQuotationHeaderVO::getLogisticsInfoId).distinct().collect(Collectors.toList());
        String creationDateMaxLogisticsInfoId = hmeAfterSaleQuotationHeaderMapper.getCreationDateMaxLogisticsInfoId(tenantId, logisticsInfoIdList);
        serviceReceiveIdList = serviceReceiveIdList.stream()
                .filter(item -> creationDateMaxLogisticsInfoId.equals(item.getLogisticsInfoId()))
                .collect(Collectors.toList());
        String serviceReceiveId = serviceReceiveIdList.get(0).getServiceReceiveId();
        String splitRecordId = serviceReceiveIdList.get(0).getSplitRecordId();
        HmeServiceReceive hmeServiceReceive = hmeServiceReceiveRepository.selectByPrimaryKey(serviceReceiveId);
        HmeServiceSplitRecord hmeServiceSplitRecord = hmeServiceSplitRecordRepository.selectByPrimaryKey(splitRecordId);
        result.setHmeServiceReceive(hmeServiceReceive);
        result.setHmeServiceSplitRecord(hmeServiceSplitRecord);
        return result;
    }

    @Override
    public List<HmeAfterSaleQuotationHeaderVO4> hourfeeLineCreateQuery(Long tenantId) {
        String materialCode = lovAdapter.queryLovMeaning("HME.QUOTATION_HOUR_FEE_MATERIAL", tenantId, "HOUR_FEE_MATERIAL");
        //根据值集维护的物料编码查询物料描述
        MtMaterial mtMaterial = mtMaterialRepository.selectOne(new MtMaterial() {{
            setMaterialCode(materialCode);
            setTenantId(tenantId);
        }});
        if(Objects.isNull(mtMaterial)){
            //报错默认带出的工时费维护错误,请检查值集【HME.QUOTATION_HOUR_FEE_MATERIAL】
            throw new MtException("HME_QUOTATION_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_QUOTATION_005", "HME"));
        }
        HmeAfterSaleQuotationHeaderVO4 hmeAfterSaleQuotationHeaderVO4 = new HmeAfterSaleQuotationHeaderVO4();
        hmeAfterSaleQuotationHeaderVO4.setMaterialId(mtMaterial.getMaterialId());
        hmeAfterSaleQuotationHeaderVO4.setMaterialCode(mtMaterial.getMaterialCode());
        hmeAfterSaleQuotationHeaderVO4.setMaterialName(mtMaterial.getMaterialName());
        hmeAfterSaleQuotationHeaderVO4.setRequsetQty(BigDecimal.valueOf(1));
        List<HmeAfterSaleQuotationHeaderVO4> resultList = new ArrayList<>();
        resultList.add(hmeAfterSaleQuotationHeaderVO4);
        return resultList;
    }

    @Override
    public void newStatusSave(Long tenantId, HmeAfterSaleQuotationHeaderVO3 dto) {
        //新建头表数据
        HmeAfterSaleQuotationHeaderVO6 headData = dto.getHeadData();
        HmeAfterSaleQuotationHeader head = new HmeAfterSaleQuotationHeader();
        head.setTenantId(tenantId);
        String serviceReceiveId = headData.getServiceReceiveId();
        HmeServiceReceive hmeServiceReceive = hmeServiceReceiveRepository.selectByPrimaryKey(serviceReceiveId);
        head.setServiceReceiveId(serviceReceiveId);
        head.setSiteId(hmeServiceReceive.getSiteId());
        head.setSnNum(hmeServiceReceive.getSnNum());
        String splitRecordId = headData.getSplitRecordId();
        HmeServiceSplitRecord hmeServiceSplitRecord = hmeServiceSplitRecordRepository.selectByPrimaryKey(splitRecordId);
        head.setMaterialLotId(hmeServiceSplitRecord.getMaterialLotId());
        head.setStatus("STORAGE");
        head.setSendTo(headData.getSendTo());
        head.setSoldTo(headData.getSoldTo());
        head.setOpticsNoFlag(dto.getOpticsNoFlag());
        head.setElectricNoFlag(dto.getElectricNoFlag());
        this.insertSelective(head);
        //新建行数据
        List<HmeAfterSaleQuotationHeaderVO4> opticsLineList = dto.getOpticsLineList();
        List<HmeAfterSaleQuotationHeaderVO4> electricLineList = dto.getElectricLineList();
        List<HmeAfterSaleQuotationHeaderVO4> hourFeeLineList = dto.getHourFeeLineList();
        int lineTotal = 0;
        if(CollectionUtils.isNotEmpty(opticsLineList)){
            lineTotal = lineTotal + opticsLineList.size();
        }
        if(CollectionUtils.isNotEmpty(electricLineList)){
            lineTotal = lineTotal + electricLineList.size();
        }
        if(CollectionUtils.isNotEmpty(hourFeeLineList)){
            lineTotal = lineTotal + hourFeeLineList.size();
        }
        if(lineTotal > 0){
            List<String> sqlList = new ArrayList<>();
            List<String> ids = mtCustomDbRepository.getNextKeys("hme_after_sale_quotation_line_s", lineTotal);
            List<String> cids = mtCustomDbRepository.getNextKeys("hme_after_sale_quotation_line_cid_s", lineTotal);
            int i = 0;
            Date nowDate = new Date();
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? -1L : curUser.getUserId();
            //光学物料行
            if(CollectionUtils.isNotEmpty(opticsLineList)){
                for (HmeAfterSaleQuotationHeaderVO4 opticsLine:opticsLineList) {
                    HmeAfterSaleQuotationLine line = new HmeAfterSaleQuotationLine();
                    line.setTenantId(tenantId);
                    line.setQuotationLineId(ids.get(i));
                    line.setQuotationHeaderId(head.getQuotationHeaderId());
                    line.setDemandType("OPTICS");
                    line.setMaterialId(opticsLine.getMaterialId());
                    line.setRequsetQty(opticsLine.getRequsetQty());
                    line.setRemark(opticsLine.getRemark());
                    line.setAttribute1(opticsLine.getSituationDesc());
                    line.setCid(Long.valueOf(cids.get(i)));
                    line.setObjectVersionNumber(1L);
                    line.setCreationDate(nowDate);
                    line.setCreatedBy(userId);
                    line.setLastUpdatedBy(userId);
                    line.setLastUpdateDate(nowDate);
                    sqlList.addAll(mtCustomDbRepository.getInsertSql(line));
                    i++;
                }
            }
            //电学物料行
            if(CollectionUtils.isNotEmpty(electricLineList)){
                for (HmeAfterSaleQuotationHeaderVO4 electricLine:electricLineList) {
                    HmeAfterSaleQuotationLine line = new HmeAfterSaleQuotationLine();
                    line.setTenantId(tenantId);
                    line.setQuotationLineId(ids.get(i));
                    line.setQuotationHeaderId(head.getQuotationHeaderId());
                    line.setDemandType("ELECTRIC");
                    line.setMaterialId(electricLine.getMaterialId());
                    line.setRequsetQty(electricLine.getRequsetQty());
                    line.setRemark(electricLine.getRemark());
                    line.setAttribute1(electricLine.getSituationDesc());
                    line.setCid(Long.valueOf(cids.get(i)));
                    line.setObjectVersionNumber(1L);
                    line.setCreationDate(nowDate);
                    line.setCreatedBy(userId);
                    line.setLastUpdatedBy(userId);
                    line.setLastUpdateDate(nowDate);
                    sqlList.addAll(mtCustomDbRepository.getInsertSql(line));
                    i++;
                }
            }
            //工时费行
            if(CollectionUtils.isNotEmpty(hourFeeLineList)){
                for (HmeAfterSaleQuotationHeaderVO4 hourFeeLine:hourFeeLineList) {
                    HmeAfterSaleQuotationLine line = new HmeAfterSaleQuotationLine();
                    line.setTenantId(tenantId);
                    line.setQuotationLineId(ids.get(i));
                    line.setQuotationHeaderId(head.getQuotationHeaderId());
                    line.setDemandType("HOUR_FEE");
                    line.setMaterialId(hourFeeLine.getMaterialId());
                    line.setRequsetQty(hourFeeLine.getRequsetQty());
                    line.setRemark(hourFeeLine.getRemark());
                    line.setAttribute1(hourFeeLine.getSituationDesc());
                    line.setCid(Long.valueOf(cids.get(i)));
                    line.setObjectVersionNumber(1L);
                    line.setCreationDate(nowDate);
                    line.setCreatedBy(userId);
                    line.setLastUpdatedBy(userId);
                    line.setLastUpdateDate(nowDate);
                    sqlList.addAll(mtCustomDbRepository.getInsertSql(line));
                    i++;
                }
            }
            if(CollectionUtils.isNotEmpty(sqlList)){
                jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
            }
        }
    }

    @Override
    public void storageStatusSave(Long tenantId, HmeAfterSaleQuotationHeaderVO3 dto) {
        //更新头表数据
        HmeAfterSaleQuotationHeaderVO6 headData = dto.getHeadData();
        String quotationHeaderId = headData.getQuotationHeaderId();
        HmeAfterSaleQuotationHeader head = this.selectByPrimaryKey(quotationHeaderId);
        head.setSoldTo(headData.getSoldTo());
        head.setOpticsNoFlag(dto.getOpticsNoFlag());
        head.setElectricNoFlag(dto.getElectricNoFlag());
        hmeAfterSaleQuotationHeaderMapper.updateByPrimaryKeySelective(head);
        //判断是否有删除的行数据，批量删除
        List<HmeAfterSaleQuotationHeaderVO4> deleteLineList = dto.getDeleteLineList();
        if(CollectionUtils.isNotEmpty(deleteLineList)){
            List<String> deleteLineIdList = deleteLineList.stream().map(HmeAfterSaleQuotationHeaderVO4::getQuotationLineId).distinct().collect(Collectors.toList());
            hmeAfterSaleQuotationHeaderMapper.batchDeleteLineData(tenantId, deleteLineIdList);
        }
        //对行数据判断该新增还是更新
        List<HmeAfterSaleQuotationHeaderVO4> opticsLineList = dto.getOpticsLineList();
        List<HmeAfterSaleQuotationHeaderVO4> electricLineList = dto.getElectricLineList();
        List<HmeAfterSaleQuotationHeaderVO4> hourFeeLineList = dto.getHourFeeLineList();
        List<HmeAfterSaleQuotationHeaderVO4> updateLineList = new ArrayList<>();
        List<HmeAfterSaleQuotationLine> insertLineList = new ArrayList<>();
        //光学物料
        for (HmeAfterSaleQuotationHeaderVO4 opticsLine:opticsLineList) {
            if(StringUtils.isNotBlank(opticsLine.getQuotationLineId())){
                //更新
                updateLineList.add(opticsLine);
            }else {
                //新增
                HmeAfterSaleQuotationLine line = new HmeAfterSaleQuotationLine();
                line.setTenantId(tenantId);
                line.setQuotationHeaderId(head.getQuotationHeaderId());
                line.setDemandType("OPTICS");
                line.setMaterialId(opticsLine.getMaterialId());
                line.setRequsetQty(opticsLine.getRequsetQty());
                line.setRemark(opticsLine.getRemark());
                line.setAttribute1(opticsLine.getSituationDesc());
                insertLineList.add(line);
            }
        }
        //电学物料
        for (HmeAfterSaleQuotationHeaderVO4 electricLine:electricLineList) {
            if(StringUtils.isNotBlank(electricLine.getQuotationLineId())){
                //更新
                updateLineList.add(electricLine);
            }else {
                //新增
                HmeAfterSaleQuotationLine line = new HmeAfterSaleQuotationLine();
                line.setTenantId(tenantId);
                line.setQuotationHeaderId(head.getQuotationHeaderId());
                line.setDemandType("ELECTRIC");
                line.setMaterialId(electricLine.getMaterialId());
                line.setRequsetQty(electricLine.getRequsetQty());
                line.setRemark(electricLine.getRemark());
                line.setAttribute1(electricLine.getSituationDesc());
                insertLineList.add(line);
            }
        }
        //工时费
        for (HmeAfterSaleQuotationHeaderVO4 hourFeeLine:hourFeeLineList) {
            if(StringUtils.isNotBlank(hourFeeLine.getQuotationLineId())){
                //更新
                updateLineList.add(hourFeeLine);
            }else {
                //新增
                HmeAfterSaleQuotationLine line = new HmeAfterSaleQuotationLine();
                line.setTenantId(tenantId);
                line.setQuotationHeaderId(head.getQuotationHeaderId());
                line.setDemandType("HOUR_FEE");
                line.setMaterialId(hourFeeLine.getMaterialId());
                line.setRequsetQty(hourFeeLine.getRequsetQty());
                line.setRemark(hourFeeLine.getRemark());
                line.setAttribute1(hourFeeLine.getSituationDesc());
                insertLineList.add(line);
            }
        }
        //批量更新
        if(CollectionUtils.isNotEmpty(updateLineList)){
            hmeAfterSaleQuotationHeaderMapper.batchUpdateLineData(tenantId, updateLineList);
        }
        //批量插入
        if(CollectionUtils.isNotEmpty(insertLineList)){
            List<String> sqlList = new ArrayList<>();
            List<String> ids = mtCustomDbRepository.getNextKeys("hme_after_sale_quotation_line_s", insertLineList.size());
            List<String> cids = mtCustomDbRepository.getNextKeys("hme_after_sale_quotation_line_cid_s", insertLineList.size());
            Date nowDate = new Date();
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? -1L : curUser.getUserId();
            int i = 0;
            for (HmeAfterSaleQuotationLine inserLine:insertLineList) {
                inserLine.setQuotationLineId(ids.get(i));
                inserLine.setCid(Long.valueOf(cids.get(i)));
                inserLine.setObjectVersionNumber(1L);
                inserLine.setCreationDate(nowDate);
                inserLine.setCreatedBy(userId);
                inserLine.setLastUpdatedBy(userId);
                inserLine.setLastUpdateDate(nowDate);
                sqlList.addAll(mtCustomDbRepository.getInsertSql(inserLine));
                i++;
            }
            if(CollectionUtils.isNotEmpty(sqlList)){
                jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
            }
        }
    }

    @Override
    public BigDecimal daysBetween(Date minDate, Date maxDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        minDate=sdf.parse(sdf.format(minDate));
        maxDate=sdf.parse(sdf.format(maxDate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(minDate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(maxDate);
        long time2 = cal.getTimeInMillis();
        BigDecimal time = BigDecimal.valueOf(time2 - time1);
        int oneDay = 1000 * 3600 * 24;
        BigDecimal betweenDays= time.divide(BigDecimal.valueOf(oneDay), 2, BigDecimal.ROUND_HALF_UP);
        return betweenDays;
    }
}
