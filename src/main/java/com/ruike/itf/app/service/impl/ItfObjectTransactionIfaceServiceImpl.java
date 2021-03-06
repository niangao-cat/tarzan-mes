package com.ruike.itf.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ruike.hme.domain.entity.HmeServiceSplitRecord;
import com.ruike.hme.infra.mapper.HmeServiceSplitRecordMapper;
import com.ruike.itf.api.dto.ItfObjectTransactionResultDTO;
import com.ruike.itf.api.dto.ItfObjectTransactionResultQueryDTO;
import com.ruike.itf.app.service.ItfObjectTransactionIfaceService;
import com.ruike.itf.domain.entity.ItfFacCollectIface;
import com.ruike.itf.domain.entity.ItfObjectTransactionIface;
import com.ruike.itf.domain.repository.ItfObjectTransactionIfaceRepository;
import com.ruike.itf.domain.vo.ItfObjectTransactionIfaceVO;
import com.ruike.itf.domain.vo.ItfRouterStepAttrVO;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.infra.mapper.ItfObjectTransactionIfaceMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.itf.utils.ItfObjectTransactionUtil;
import com.ruike.itf.utils.SendESBConnect;
import com.ruike.itf.utils.Utils;
import com.ruike.wms.api.dto.WmsTransactionTypeDTO;
import com.ruike.wms.domain.entity.WmsObjectTransaction;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.AopProxy;
import org.springframework.beans.BeanUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * ?????????????????????????????????????????????
 *
 * @author yonghui.zhu@hand-china.com 2020-08-11 09:44:09
 */
@Slf4j
@Service
public class ItfObjectTransactionIfaceServiceImpl implements ItfObjectTransactionIfaceService, AopProxy<ItfObjectTransactionIfaceServiceImpl> {

    private final WmsObjectTransactionRepository wmsObjectTransactionRepository;
    private final ItfObjectTransactionIfaceRepository itfObjectTransactionIfaceRepository;
    private final CustomSequence customSequence;
    private final SendESBConnect sendESBConnect;
    private final LovAdapter lovAdapter;
    private final WmsTransactionTypeRepository wmsTransactionTypeRepository;
    private final MtWorkOrderRepository mtWorkOrderRepository;
    private final HmeServiceSplitRecordMapper hmeServiceSplitRecordMapper;
    private final ItfObjectTransactionIfaceMapper itfObjectTransactionIfaceMapper;

    @Autowired
    public ItfObjectTransactionIfaceServiceImpl(WmsObjectTransactionRepository wmsObjectTransactionRepository,
                                                ItfObjectTransactionIfaceRepository itfObjectTransactionIfaceRepository,
                                                CustomSequence customSequence,
                                                SendESBConnect sendESBConnect,
                                                LovAdapter lovAdapter,
                                                WmsTransactionTypeRepository wmsTransactionTypeRepository,
                                                MtWorkOrderRepository mtWorkOrderRepository,
                                                HmeServiceSplitRecordMapper hmeServiceSplitRecordMapper,
                                                ItfObjectTransactionIfaceMapper itfObjectTransactionIfaceMapper) {
        this.wmsObjectTransactionRepository = wmsObjectTransactionRepository;
        this.itfObjectTransactionIfaceRepository = itfObjectTransactionIfaceRepository;
        this.customSequence = customSequence;
        this.sendESBConnect = sendESBConnect;
        this.lovAdapter = lovAdapter;
        this.wmsTransactionTypeRepository = wmsTransactionTypeRepository;
        this.mtWorkOrderRepository = mtWorkOrderRepository;
        this.hmeServiceSplitRecordMapper = hmeServiceSplitRecordMapper;
        this.itfObjectTransactionIfaceMapper = itfObjectTransactionIfaceMapper;
    }

    @Value("${hwms.interface.defaultNamespace}")
    private String namespace;

    private final static String MERGE_PREFIX = "MM";

    /**
     * ????????????????????????????????????N,E
     */
    private final static String STATUS = "('N','E')";

    /**
     * ????????????
     *
     * @param tenantId
     * @param type     ?????????????????????
     * @return
     * @author jiangling.zheng@hand-china.com 2020/9/12 10:10
     */
    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public List<ItfObjectTransactionIface> processSummary(Long tenantId, String type, List<WmsObjectTransaction> dto) {
        List<WmsObjectTransaction> detailList = new ArrayList<>();
        List<WmsObjectTransaction> transactionList = new ArrayList<>();
        List<String> tranType = dto.stream().map(WmsObjectTransaction::getMoveType).distinct().collect(Collectors.toList());
        String batchId = String.valueOf(new Date().getTime());

        //?????????????????????????????????????????? modify by yuchao.wang for fang.pan at 2020.10.31
        //???????????????????????????????????? modify by penglin.sui for hui.ma at 2021.10.12
        boolean siteOutWorkReportFlag = false;
        //?????????????????????????????????
        boolean mergeWorkReportFlag = false;
        //?????????????????????????????????
        boolean excludeMergeWorkReportFlag = false;
        if (type.split(",").length == 2) {
            if("SITE_OUT_WORK_REPORT".equals(type.split(",")[1])) {
                type = type.split(",")[0];
                siteOutWorkReportFlag = true;
            }else if("HME_WORK_REPORT".equals(type.split(",")[1])){
                type = type.split(",")[0];
                mergeWorkReportFlag = true;
            }else if("EXCLUDE_HME_WORK_REPORT".equals(type.split(",")[1])){
                type = type.split(",")[0];
                excludeMergeWorkReportFlag = true;
            }
        }

        // ?????????????????? ???????????????????????????
        if ("N".equals(type) && !siteOutWorkReportFlag) {
            if(mergeWorkReportFlag){
                detailList = wmsObjectTransactionRepository.selectWorkReportForMergeList(tenantId);
            }else if(excludeMergeWorkReportFlag){
                detailList = wmsObjectTransactionRepository.selectExcludeWorkReportForMergeList(tenantId);
            }else {
                detailList = wmsObjectTransactionRepository.selectForMergeList(tenantId);
            }

            //????????????????????????????????? add by yuchao.wang for jiao.chen at 2020.9.28
            if (CollectionUtils.isNotEmpty(detailList)) {
                // ??????????????????????????????
                detailList.forEach(a -> {
                    a.setMergeFlag("P");
                    a.setMergeId("-100");
                    a.setAttribute15(batchId);
                });
                self().batchUpdateMergeFlag(detailList);

                //??????????????????????????????????????????ID
                List<String> woNumList = detailList.stream().map(WmsObjectTransaction::getWorkOrderNum)
                        .filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(woNumList)) {
                    List<MtWorkOrder> workOrderList = mtWorkOrderRepository.woLimitWorkNUmQuery(tenantId, woNumList);
                    if (CollectionUtils.isNotEmpty(workOrderList)) {
                        Map<String, String> woTypeMap = new HashMap<String, String>();
                        Map<String, String> woIdMap = new HashMap<String, String>();

                        workOrderList.forEach(item -> woTypeMap.put(item.getWorkOrderNum(), item.getWorkOrderType()));
                        workOrderList.forEach(item -> woIdMap.put(item.getWorkOrderNum(), item.getWorkOrderId()));

                        for (int i = 0; i < detailList.size(); i++) {
                            WmsObjectTransaction wmsObjectTransaction = detailList.get(i);

                            //???????????????????????????MES_RK06 ????????????????????????
                            if ("MES_RK06".equals(woTypeMap.getOrDefault(wmsObjectTransaction.getWorkOrderNum(), ""))) {
                                //????????????????????????
                                HmeServiceSplitRecord hmeServiceSplitRecord = hmeServiceSplitRecordMapper.queryWoNumBySnNumAndWoId(tenantId,
                                        woIdMap.getOrDefault(wmsObjectTransaction.getWorkOrderNum(), ""), StringUtils.trimToEmpty(wmsObjectTransaction.getSnNum()));

                                //?????????????????????????????????
                                if (Objects.isNull(hmeServiceSplitRecord)) {
                                    //??????????????????????????????????????????
                                    log.info("ItfObjectTransactionIfaceHandler.processSummary: ??????[" + wmsObjectTransaction.getWorkOrderNum()
                                            + "],??????ID[" + wmsObjectTransaction.getTransactionId() + "]????????????????????????");
                                } else if (StringUtils.isNotBlank(hmeServiceSplitRecord.getInternalOrderNum())) {
                                    //????????????????????? ?????????
                                    //???????????????????????????/????????????
                                    if ("HME_WO_ISSUE".equals(wmsObjectTransaction.getTransactionTypeCode())
                                            || "HME_WO_ISSUE_EXT".equals(wmsObjectTransaction.getTransactionTypeCode())) {
                                        wmsObjectTransaction.setTransactionTypeCode("WMS_INSDID_ORDER_S_I");
                                    } else if ("HME_WO_ISSUE_R".equals(wmsObjectTransaction.getTransactionTypeCode())
                                            || "HME_WO_ISSUE_R_EXT".equals(wmsObjectTransaction.getTransactionTypeCode())) {
                                        wmsObjectTransaction.setTransactionTypeCode("WMS_INSDID_ORDER_S_R");
                                    }
                                    wmsObjectTransaction.setTransactionId(null);
                                    wmsObjectTransaction.setCid(null);
                                    wmsObjectTransactionRepository.insert(wmsObjectTransaction);

                                    //?????????????????????
                                    WmsObjectTransaction remove = detailList.remove(i);
                                    remove.setMergeFlag(ItfConstant.ConstantValue.YES);
                                    transactionList.add(remove);
                                    i--;
                                } else {
                                    //???????????????????????? ?????????
                                    //??????SAP?????????
                                    String sapNum = "";
                                    if (StringUtils.isNotBlank(hmeServiceSplitRecord.getTopSplitRecordId())) {
                                        hmeServiceSplitRecord = hmeServiceSplitRecordMapper.queryWoNumBySplitRecordId(tenantId, hmeServiceSplitRecord.getTopSplitRecordId());
                                        if (Objects.nonNull(hmeServiceSplitRecord)) {
                                            sapNum = hmeServiceSplitRecord.getInternalOrderNum();
                                        }
                                    }

                                    //????????????????????????
                                    if (StringUtils.isBlank(sapNum)) {
                                        //?????????????????????????????????????????????
                                        log.info("ItfObjectTransactionIfaceHandler.processSummary: ??????[" + wmsObjectTransaction.getWorkOrderNum()
                                                + "],??????ID[" + wmsObjectTransaction.getTransactionId() + "],TopSplitRecordId["
                                                + hmeServiceSplitRecord.getTopSplitRecordId() + "]???????????????????????????");
                                    } else {
                                        detailList.get(i).setWorkOrderNum(sapNum);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            log.info("??????????????????????????????????????????{}", dto.size());

            //???????????????????????? modify by yuchao.wang for tianyang.xie at 2020.11.11
            Map<String, String> processErpFlagMap = new HashMap<>();
            List<String> transactionTypeCodeList = dto.stream().map(WmsObjectTransaction::getTransactionTypeCode)
                    .distinct().collect(Collectors.toList());
            List<WmsTransactionType> transactionTypes = wmsTransactionTypeRepository.selectByCondition(Condition.builder(WmsTransactionType.class)
                    .andWhere(Sqls.custom().andEqualTo(WmsTransactionType.FIELD_TENANT_ID, tenantId)
                            .andIn(WmsTransactionType.FIELD_TRANSACTION_TYPE_CODE, transactionTypeCodeList)
                    ).build());
            if (CollectionUtils.isNotEmpty(transactionTypes)) {
                transactionTypes.forEach(item -> processErpFlagMap.put(item.getTransactionTypeCode(), item.getProcessErpFlag()));
            }

            for (WmsObjectTransaction wmsObjectTransaction : dto) {
                if ("Y".equals(processErpFlagMap.getOrDefault(wmsObjectTransaction.getTransactionTypeCode(), "N"))) {
                    detailList.add(wmsObjectTransaction);
                }
            }
        }

        if (CollectionUtils.isEmpty(detailList)) {
            log.error("ItfObjectTransactionIfaceHandler.processSummary??????????????????????????????????????????");
            return new ArrayList<>();
        }

        //?????????????????????????????? add by yuchao.wang for kang.wang at 2020.9.29
        Date accountDate = itfObjectTransactionIfaceRepository.querySilentAccountSet(tenantId);

        // ???????????? ??????????????????
        Date now = Utils.getNowDate();
        Map<ItfObjectTransactionIface, List<WmsObjectTransaction>> summaryMap = detailList.stream().collect(Collectors.groupingBy(WmsObjectTransaction::createSummary));
        List<ItfObjectTransactionIface> workReportList = new ArrayList<>();
        for (Map.Entry<ItfObjectTransactionIface, List<WmsObjectTransaction>> entry : summaryMap.entrySet()) {
            ItfObjectTransactionIface iface = entry.getKey();
            List<WmsObjectTransaction> list = entry.getValue();
            String ifaceId = customSequence.getNextKey("itf_object_transaction_iface_s");
            String mergeId = MERGE_PREFIX + ifaceId;
            iface.setAttribute15(batchId);
            iface.setIfaceId(ifaceId);
            iface.setMergeId(mergeId);
            iface.setSystem(ItfConstant.ConstantValue.WMS);
            iface.setAccountDate(Objects.isNull(accountDate) ? now : accountDate);
            iface.setProcessDate(now);
            iface.setProcessStatus(ItfConstant.ProcessStatus.NEW);
            // ??????????????????????????????????????????????????????
            list.stream().map(WmsObjectTransaction::getTransactionTime).max(Date::compareTo).ifPresent(iface::setTransactionTime);
            iface.setTransactionQty(list.stream().map(WmsObjectTransaction::getTransactionQty).reduce(BigDecimal.ZERO, BigDecimal::add));

            //???????????????543?????????????????????O modify by yuchao.wang for kang.wang at 2020.10.19
            if ("543".equals(iface.getMoveType())) {
                iface.setSpecStockFlag("O");
            }

            //??????????????????????????? modify by yuchao.wang for kang.wang at 2020.10.19
            iface.setSourceDocLineNum(iface.getSourceDocNum() + "-" + iface.getSourceDocLineNum());

            // set who??????
            InterfaceUtils.setDefaultWhoFields(iface);
            list.forEach(rec -> {
                rec.setMergeFlag(ItfConstant.ConstantValue.YES);
                rec.setMergeId(mergeId);
                rec.setAttribute15(batchId);
            });

            //??????????????????????????????????????? add by yuchao.wang for kang.wang at 2020.8.20
            if ("HME_WORK_REPORT".equals(iface.getTransactionTypeCode())) {
                workReportList.add(iface);
            }
            transactionList.addAll(list);
        }
        // ????????????
        List<ItfObjectTransactionIface> list = new ArrayList<>(summaryMap.keySet());

        // ?????????????????????????????? add by yuchao.wang for kang.wang at 2020.8.20
        if (CollectionUtils.isNotEmpty(workReportList)) {
            log.info("<====== ItfObjectTransactionIfaceHandler.processSummary workReportList.size={}, workReportList: {}",
                    workReportList.size(), workReportList);
            //????????????????????????
            List<String> workOrderNumList = workReportList.stream()
                    .map(ItfObjectTransactionIface::getWorkOrderNum).collect(Collectors.toList());
            List<String> operationSequenceList = workReportList.stream()
                    .map(ItfObjectTransactionIface::getOperationSequence).collect(Collectors.toList());
            List<ItfRouterStepAttrVO> itfRouterStepAttrVOList = itfObjectTransactionIfaceRepository
                    .queryAllWorkReportData(tenantId, workOrderNumList, operationSequenceList);
            Map<String, List<ItfRouterStepAttrVO>> groupMap = itfRouterStepAttrVOList.stream()
                    .collect(Collectors.groupingBy(item -> item.getWorkOrderNum() + "-" + item.getStepName()));

            //??????????????????
            list.forEach(item -> {
                if ("HME_WORK_REPORT".equals(item.getTransactionTypeCode())) {
                    String key = item.getWorkOrderNum() + "-" + item.getOperationSequence();
                    if (groupMap.containsKey(key)) {
                        Map<String, String> attrMap = new HashMap<>();
                        groupMap.get(key).forEach(attr -> attrMap.put(attr.getAttrName(), attr.getAttrValue()));

                        if (attrMap.containsKey("lineAttribute1") && attrMap.containsKey("lineAttribute2")) {
                            item.setLineAttribute1(attrMap.get("lineAttribute1"));
                            item.setLineAttribute2(String.valueOf(item.getTransactionQty()
                                    .multiply(new BigDecimal(attrMap.get("lineAttribute2"))).setScale(3, BigDecimal.ROUND_HALF_UP)));
                        }

                        if (attrMap.containsKey("lineAttribute3") && attrMap.containsKey("lineAttribute4")) {
                            item.setLineAttribute3(attrMap.get("lineAttribute3"));
                            item.setLineAttribute4(String.valueOf(item.getTransactionQty()
                                    .multiply(new BigDecimal(attrMap.get("lineAttribute4"))).setScale(3, BigDecimal.ROUND_HALF_UP)));
                        }

                        if (attrMap.containsKey("lineAttribute5") && attrMap.containsKey("lineAttribute6")) {
                            item.setLineAttribute5(attrMap.get("lineAttribute5"));
                            item.setLineAttribute6(String.valueOf(item.getTransactionQty()
                                    .multiply(new BigDecimal(attrMap.get("lineAttribute6"))).setScale(3, BigDecimal.ROUND_HALF_UP)));
                        }

                        if (attrMap.containsKey("lineAttribute7") && attrMap.containsKey("lineAttribute8")) {
                            item.setLineAttribute7(attrMap.get("lineAttribute7"));
                            item.setLineAttribute8(String.valueOf(item.getTransactionQty()
                                    .multiply(new BigDecimal(attrMap.get("lineAttribute8"))).setScale(3, BigDecimal.ROUND_HALF_UP)));
                        }

                        if (attrMap.containsKey("lineAttribute9") && attrMap.containsKey("lineAttribute10")) {
                            item.setLineAttribute9(attrMap.get("lineAttribute9"));
                            item.setLineAttribute10(String.valueOf(item.getTransactionQty()
                                    .multiply(new BigDecimal(attrMap.get("setLineAttribute10"))).setScale(3, BigDecimal.ROUND_HALF_UP)));
                        }

                        //????????????SAP????????? modify by yuchao.wang for fang.pan at 2020.10.21
                        if (CollectionUtils.isNotEmpty(groupMap.get(key))
                                && Objects.nonNull(groupMap.get(key).get(0))
                                && StringUtils.isNotBlank(groupMap.get(key).get(0).getStepName())) {
                            item.setOperationSequence(groupMap.get(key).get(0).getStepName());
                        }
                    }
                }
            });
        }
        // SN??????
        ItfObjectTransactionUtil.sumSNObjectTransaction(list, transactionList, type);
        // ?????????????????????261???262????????????????????????
        ItfObjectTransactionUtil.count261And262(list, transactionList);
        itfObjectTransactionIfaceRepository.batchInsertIface(list);
        wmsObjectTransactionRepository.batchUpdateMergeFlag(transactionList);
        List<ItfObjectTransactionIface> newList = new ArrayList<>(100);
        if ("Y".equals(type)) {
            for (String typeStr : tranType) {
                for (ItfObjectTransactionIface iface : list) {
                    if (typeStr.equals(iface.getMoveType())) {
                        newList.add(iface);
                    }
                }
            }
            materialMove(tenantId, type, newList);
        }

        //V20210630 modify by penglin.sui for tianyang.xie ??????????????????
        if(CollectionUtils.isNotEmpty(list)){
            List<String> workOrderNumList = list.stream()
                    .filter(Objects::nonNull)
                    .map(ItfObjectTransactionIface::getWorkOrderNum)
                    .distinct()
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(workOrderNumList)){
                List<ItfObjectTransactionIface> workOrderMaterialList = new ArrayList<>();
                //???????????????
                List<List<String>> splitSqlList = InterfaceUtils.splitSqlList(workOrderNumList, SQL_ITEM_COUNT_LIMIT);
                for (List<String> domains : splitSqlList) {
                    List<ItfObjectTransactionIface> subWorkOrderMaterialList = itfObjectTransactionIfaceMapper.selectWorkOrderMaterial(tenantId , domains);
                    if(CollectionUtils.isNotEmpty(subWorkOrderMaterialList)){
                        workOrderMaterialList.addAll(subWorkOrderMaterialList);
                    }
                }
                if(CollectionUtils.isNotEmpty(workOrderMaterialList)) {
                    Map<String , String> workOrderMaterialMap = workOrderMaterialList.stream().collect(Collectors.toMap(ItfObjectTransactionIface::getWorkOrderNum, ItfObjectTransactionIface::getWoMaterialCode));
                    for (ItfObjectTransactionIface item : list
                         ) {
                        if(StringUtils.isNotBlank(item.getWorkOrderNum()) &&
                                workOrderMaterialMap.containsKey(item.getWorkOrderNum())) {
                            item.setWoMaterialCode(workOrderMaterialMap.get(item.getWorkOrderNum()));
                        }
                    }
                }
            }
        }

        return list;
    }


    @Override
    public List<ItfObjectTransactionIface> sendSapMaterialMove(Long tenantId, List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS) {
        List<ItfObjectTransactionIface> list = new ArrayList<>();
        String interfaceFlag = interfaceFlag(tenantId);
        if ("Y".equals(interfaceFlag)) {
            if (CollectionUtils.isNotEmpty(wmsObjectTransactionResponseVOS)) {
                List<WmsObjectTransaction> wmsObjectTransactions = wmsObjectTransactionList(wmsObjectTransactionResponseVOS);
                list = this.processSummary(tenantId, interfaceFlag, wmsObjectTransactions);
            }
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public void sendSapProdMaterialMove(Long tenantId, List<WmsObjectTransactionResponseVO> reportList, List<WmsObjectTransactionResponseVO> workList) {
        log.info("<====== ItfObjectTransactionIfaceServiceImpl.sendSapProdMaterialMove reportList={}", reportList, "\nworkList={}", reportList);
        String interfaceFlag = interfaceFlag(tenantId);
        if ("Y".equals(interfaceFlag)) {
            if (CollectionUtils.isNotEmpty(reportList)) {
                this.productionStatementInvoke(tenantId, "Y,SITE_OUT_WORK_REPORT",
                        reportList);
            }
            if (CollectionUtils.isNotEmpty(workList)) {
                List<WmsObjectTransaction> wmsObjectTransactions = wmsObjectTransactionList(workList);
                try {
                    this.processSummary(tenantId, "Y", wmsObjectTransactions);
                } catch (CommonException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public List<ItfObjectTransactionIface> sendSapMaterialMoveSort(Long tenantId, List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS, List<String> tranType) {
        String interfaceFlag = interfaceFlag(tenantId);
        if (!"Y".equals(interfaceFlag)) {
            return new ArrayList<>();
        }
        List<ItfObjectTransactionIface> list = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(wmsObjectTransactionResponseVOS)) {
            List<WmsObjectTransaction> wmsObjectTransactions = wmsObjectTransactionList(wmsObjectTransactionResponseVOS);
            List<WmsObjectTransaction> newList = new ArrayList<>();
            for (String type : tranType) {
                for (WmsObjectTransaction obj : wmsObjectTransactions) {
                    if (type.equals(obj.getMoveType())) {
                        newList.add(obj);
                    }
                }
            }
            if (CollectionUtils.isEmpty(newList)) {
                newList.addAll(wmsObjectTransactions);
            }
            list = this.processSummary(tenantId, interfaceFlag, newList);
        }
        return list;
    }

    /**
     * ??????????????????ERP
     *
     * @param belongTenantId ??????ID
     * @return
     * @author kejin.liu01@hand-china.com 2020/8/13 16:40
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void materialMove(Long belongTenantId, String type, List<ItfObjectTransactionIface> dto) {
        long batchId = new Date().getTime();
        if (!"Y".equals(type)) {
            // ?????????????????????,???????????????(STATUS) ??? N???E
            dto = itfObjectTransactionIfaceMapper.selectObjectTransactionByStatus(STATUS);
            // ??????????????????
            List<String> ketId = dto.stream().map(ItfObjectTransactionIface::getIfaceId).collect(Collectors.toList());
            self().updateTransactionStatus(ketId);
        }
        // ????????????
        if (CollectionUtils.isEmpty(dto)) {
            throw new CommonException("???????????????????????????????????????????????????");
        }
        int itemId = 1;
        // ??????????????????,???????????????MAP???
        List<Map<String, Object>> newData = new ArrayList<>();
        // ?????????SN??????
        for (int i = 0; i < dto.size(); i++) {
            Map<String, Object> map = ItfObjectTransactionUtil.materialMoveMap(dto.get(i), type, batchId, itemId);
            List<Map<String, Object>> list = new ArrayList<>();
            String snNum = dto.get(i).getSnNum();
            if (Strings.isEmpty(snNum)) {
                Map<String, Object> zidMap = new HashMap<>();
                zidMap.put("ZID", dto.get(i).getIfaceId());
                zidMap.put("SERIALNO", null);
                zidMap.put("ZITEM", map.get("ZITEM"));
                list.add(zidMap);
            } else {
                String[] snNums = snNum.split(",");
                for (String str : snNums) {
                    Map<String, Object> zidMap = new HashMap<>();
                    zidMap.put("ZID", dto.get(i).getIfaceId());
                    zidMap.put("SERIALNO", str);
                    zidMap.put("ZITEM", map.get("ZITEM"));
                    list.add(zidMap);
                }
            }
            map.put("SERIAL", list);
            newData.add(map);
            itemId++;
        }

        List<List<Map<String, Object>>> splitSqlList = InterfaceUtils.splitSqlList(newData, 1000);
        for (List<Map<String, Object>> sendList : splitSqlList) {
            Map<String, Object> sendMap = new HashMap<>();
            sendMap.put("ITEM", sendList);
            // ????????????,????????????
            Map<String, Object> resultInfo = sendESBConnect.sendEsb(sendMap, "GOODMVT",
                    "ItfObjectTransactionIfaceHandler.materialMove", ItfConstant.InterfaceCode.ESB_OBJECT_TRANSACTION_SYNC);
            String aReturn = JSON.toJSON(resultInfo.get("RETURN")).toString();
            List<ItfObjectTransactionResultDTO> itfObjectTransactionResultDTOS = JSONArray.parseArray(aReturn, ItfObjectTransactionResultDTO.class);
            // ????????????
            updateStatus(itfObjectTransactionResultDTOS, type);
        }
    }

    /**
     * ???????????????????????????????????????
     *
     * @param ketId
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateTransactionStatus(List<String> ketId) {
        if (CollectionUtils.isNotEmpty(ketId)) {
            itfObjectTransactionIfaceMapper.batchUpdateStatusByPrimaryKey(ketId);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void batchUpdateMergeFlag(List<WmsObjectTransaction> detailList) {
        if (CollectionUtils.isNotEmpty(detailList)) {
            wmsObjectTransactionRepository.batchUpdateMergeFlag(detailList);
        }
    }

    /**
     * ??????????????????ERP
     *
     * @param tenantId
     * @param wmsObjectTransactionResponseVOS
     * @return
     * @author kejin.liu01@hand-china.com 2020/8/24 16:46
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void productionStatementInvoke(Long tenantId, String type, List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS) {
        // ??????????????????
        List<ItfObjectTransactionIface> workReport = new ArrayList<>();

        //?????????????????????????????????????????? modify by yuchao.wang for fang.pan at 2020.10.31
        boolean siteOutWorkReportFlag = false;
        if (type.split(",").length == 2 && "SITE_OUT_WORK_REPORT".equals(type.split(",")[1])) {
            type = type.split(",")[0];
            siteOutWorkReportFlag = true;
        }

        if ("Y".equals(type)) {
            List<WmsObjectTransaction> wmsObjectTransactions = wmsObjectTransactionList(wmsObjectTransactionResponseVOS);

            //????????????????????????????????????????????????type???????????? modify by yuchao.wang for fang.pan at 2020.10.31
            String processSummaryType = "N";
            if (siteOutWorkReportFlag) {
                processSummaryType = "N,SITE_OUT_WORK_REPORT";
            }
            workReport = processSummary(tenantId, processSummaryType, wmsObjectTransactions);
            // ??????HME_WORK_REPORT?????????
            workReport = workReport.stream().filter(a -> "HME_WORK_REPORT".equals(a.getTransactionTypeCode())).collect(Collectors.toList());

        } else {
            workReport = itfObjectTransactionIfaceRepository.selectDataByStatusAndType(STATUS);
        }
        if (CollectionUtils.isEmpty(workReport)) {
            log.error("???????????????????????????????????????");
            return;
        }

        //V20210630 modify by penglin.sui for tianyang.xie EO??????????????????????????????????????????ErpCoProductConfirmRestProxy
        List<ItfObjectTransactionIface> coWorkReportList = workReport.stream()
                .filter(item -> StringUtils.isNotBlank(item.getMaterialCode()) &&
                        StringUtils.isNotBlank(item.getWoMaterialCode()) &&
                        !item.getMaterialCode().equals(item.getWoMaterialCode()))
                .collect(Collectors.toList());

        List<ItfObjectTransactionIface> workReportList = workReport.stream()
                                            .filter(item -> StringUtils.isBlank(item.getMaterialCode()) ||
                                                    StringUtils.isBlank(item.getWoMaterialCode()) ||
                                                    (StringUtils.isNotBlank(item.getMaterialCode()) &&
                                                            StringUtils.isNotBlank(item.getWoMaterialCode()) &&
                                                            item.getMaterialCode().equals(item.getWoMaterialCode())))
                                            .collect(Collectors.toList());
        List<ItfObjectTransactionResultDTO> itfObjectTransactionResultDTOList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(coWorkReportList)){
            List<Map<String, Object>> erpDate = ItfObjectTransactionUtil.productionData(coWorkReportList);
            Map<String, Object> confData = new HashMap<>();
            confData.put("CONF", erpDate);
            Map<String, Object> resultInfo = sendESBConnect.sendEsb(confData, "CONF",
                    "ItfObjectTransactionIfaceHandler.productionStatementInvoke", ItfConstant.InterfaceCode.ESB_CO_PRODUCTION_STATEMENT_SYNC);
            // ????????????
            String aReturn = JSON.toJSON(resultInfo.get("LINKA")).toString();
            List<ItfObjectTransactionResultDTO> itfObjectTransactionResultDTOS = JSONArray.parseArray(aReturn, ItfObjectTransactionResultDTO.class);
            itfObjectTransactionResultDTOList.addAll(itfObjectTransactionResultDTOS);
        }

        if(CollectionUtils.isNotEmpty(workReportList)){
            List<Map<String, Object>> erpDate = ItfObjectTransactionUtil.productionData(workReportList);
            Map<String, Object> confData = new HashMap<>();
            confData.put("CONF", erpDate);
            Map<String, Object> resultInfo = sendESBConnect.sendEsb(confData, "CONF",
                    "ItfObjectTransactionIfaceHandler.productionStatementInvoke", ItfConstant.InterfaceCode.ESB_PRODUCTION_STATEMENT_SYNC);
            // ????????????
            String aReturn = JSON.toJSON(resultInfo.get("LINKA")).toString();
            List<ItfObjectTransactionResultDTO> itfObjectTransactionResultDTOS = JSONArray.parseArray(aReturn, ItfObjectTransactionResultDTO.class);
            itfObjectTransactionResultDTOList.addAll(itfObjectTransactionResultDTOS);
        }

        // ????????????
        for (ItfObjectTransactionResultDTO resultDTOS : itfObjectTransactionResultDTOList) {
            if ("Y".equals(type)) {
                if (!"S".equals(resultDTOS.getZRESULT())) {
                    log.error(resultDTOS.getZID() + ":" + resultDTOS.getZMESSAGE());
                }
            }
            ItfObjectTransactionIface itfObjectTransactionIface = new ItfObjectTransactionIface();
            itfObjectTransactionIface.setProcessStatus(resultDTOS.getZRESULT());
            itfObjectTransactionIface.setProcessMessage(resultDTOS.getZMESSAGE());
            itfObjectTransactionIface.setAttribute1(resultDTOS.getCONF_NO());
            itfObjectTransactionIface.setAttribute3(resultDTOS.getCONF_CNT());
            itfObjectTransactionIface.setIfaceId(resultDTOS.getZID());
            // ??????
            itfObjectTransactionIfaceMapper.updateByPrimaryKeySelective(itfObjectTransactionIface);
        }
    }

    /**
     * @param tenantId
     * @param queryDTO
     * @param pageRequest
     * @return java.util.List<com.ruike.itf.domain.entity.ItfObjectTransactionIface>
     * @description ????????????
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2020/11/11
     * @time 16:50
     * @version 0.0.1
     */
    @Override
    public Page<ItfObjectTransactionIfaceVO> list(Long tenantId, ItfObjectTransactionResultQueryDTO queryDTO, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> itfObjectTransactionIfaceMapper.selectList(tenantId, queryDTO));
    }

    /**
     * @param itfObjectTransactionIface
     * @return com.ruike.itf.domain.entity.ItfObjectTransactionIface
     * @description ????????????
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2020/11/11
     * @time 19:29
     * @version 0.0.1
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ItfObjectTransactionIface update(Long tenantId, ItfObjectTransactionIfaceVO itfObjectTransactionIface) {
        // ???????????????????????? ???????????????
        if (StringUtils.isEmpty(itfObjectTransactionIface.getIfaceId())) {
            throw new MtException("??????????????????????????? ?????????");
        }
        // copy ??????
        ItfObjectTransactionIface dto = new ItfObjectTransactionIface();
        BeanUtils.copyProperties(itfObjectTransactionIface, dto);
        itfObjectTransactionIfaceMapper.updateByPrimaryKeySelective(dto);
        return dto;
    }


    /**
     * ??????????????????ERP ??????ERP??????????????????????????????
     *
     * @param dtos
     * @param type ?????????????????????
     * @author jiangling.zheng@hand-china.com 2020/8/15 10:19
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateStatus(List<ItfObjectTransactionResultDTO> dtos, String type) {
        if (CollectionUtils.isEmpty(dtos)) {
            throw new CommonException("ERP???????????????????????????????????????ERP?????????");
        }

        boolean zflag = true;
        StringBuffer errorMsg = new StringBuffer();
        if ("Y".equals(type)) {
            for (ItfObjectTransactionResultDTO dto : dtos) {
                if (!"S".equals(dto.getZRESULT())) {
                    zflag = false;
                    if (!dto.getZMESSAGE().contains("??????")) {
                        errorMsg.append(dto.getZID()).append(":").append(dto.getZMESSAGE()).append("\n");
                    }
                }
            }
        }
        for (ItfObjectTransactionResultDTO dto : dtos) {
            String remark = null;
            String zmessage = dto.getZMESSAGE();
            if (zmessage.contains("ID???") && zmessage.contains("?????????") &&
                    zmessage.contains("??????") && zmessage.contains("????????????")) {
                String attribute1 = Utils.subString(zmessage, "?????????", "??????").replace(" ", "");
                String attribute2 = Utils.subString(zmessage, "??????", "????????????").replace(" ", "");
                dto.setZMBLNR(attribute1);
                dto.setZMJAHR(attribute2);
                dto.setZRESULT("S");
                remark = "?????????????????????????????????S";
            }
            ItfObjectTransactionIface itfObjectTransactionIface = new ItfObjectTransactionIface();
            itfObjectTransactionIface.setProcessStatus(dto.getZRESULT());
            itfObjectTransactionIface.setProcessMessage(dto.getZMESSAGE());
            itfObjectTransactionIface.setAttribute1(dto.getZMBLNR());
            itfObjectTransactionIface.setAttribute2(dto.getZMJAHR());
            itfObjectTransactionIface.setAttribute3(dto.getZZEILE());
            itfObjectTransactionIface.setRemark(remark);
            itfObjectTransactionIface.setIfaceId(dto.getZID());
            // ??????
            itfObjectTransactionIfaceMapper.updateByPrimaryKeySelective(itfObjectTransactionIface);
        }
        if (!zflag) {
            log.error(errorMsg.toString());
            throw new CommonException(errorMsg.toString());
        }
    }


    /**
     * ?????????????????????
     *
     * @param wmsObjectTransactionResponseVOS
     * @return
     */
    private List<WmsObjectTransaction> wmsObjectTransactionList(List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS) {
        if (CollectionUtils.isEmpty(wmsObjectTransactionResponseVOS)) {
            return new ArrayList<>();
        }
        List<String> transactionIds = wmsObjectTransactionResponseVOS.stream().map(WmsObjectTransactionResponseVO::getTransactionId).distinct().collect(Collectors.toList());
        String transactionId = "'" + StringUtils.join(transactionIds, "','") + "'";
        List<WmsObjectTransaction> wmsObjectTransactions = wmsObjectTransactionRepository.selectByIds(transactionId);
        return wmsObjectTransactions;
    }


    /**
     * ????????????????????????
     *
     * @param tenantId
     * @return
     */
    private String interfaceFlag(Long tenantId) {
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue(ItfConstant.LovCode.ITF_TIMELY_INTERNAL_FLAG, tenantId);
        if (CollectionUtils.isEmpty(lovValueDTOS)) {
            throw new CommonException(ItfConstant.LovCode.ITF_TIMELY_INTERNAL_FLAG + "??????????????????\n????????????Y???N???Y??????????????????N?????????????????????");
        }
        return lovValueDTOS.get(0).getMeaning();
    }


}