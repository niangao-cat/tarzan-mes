package com.ruike.wms.infra.repository.impl;

import cn.hutool.core.date.DateUtil;

import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.wms.api.dto.*;
import com.ruike.wms.domain.entity.WmsJobRecord;
import com.ruike.wms.domain.repository.WmsJobRecordRepository;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsInvOnhandQtyRecordMapper;
import com.ruike.wms.infra.mapper.WmsJobRecordMapper;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import io.tarzan.common.infra.feign.MtUserService;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.wms.domain.entity.WmsInvOnhandQtyRecord;
import com.ruike.wms.domain.repository.WmsInvOnhandQtyRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.inventory.api.dto.MtInvJournalDTO2;
import tarzan.inventory.api.dto.MtInvJournalDTO4;
import tarzan.inventory.infra.mapper.MtInvJournalMapper;
import tarzan.modeling.app.service.MtCustomerService;
import tarzan.modeling.app.service.MtSupplierService;
import tarzan.modeling.domain.entity.MtCustomer;
import tarzan.modeling.domain.entity.MtSupplier;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletResponse;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 仓库物料每日进销存表 资源库实现
 *
 * @author jiangling.zheng@hand-china.com 2020-11-18 09:53:55
 */
@Component
public class WmsInvOnhandQtyRecordRepositoryImpl extends BaseRepositoryImpl<WmsInvOnhandQtyRecord> implements WmsInvOnhandQtyRecordRepository {


    private static final int SQL_ITEM_COUNT_LIMIT = 1000;
    private static final String SHEET_NAME = "每日进销存报表";
    private static final String FILE_NAME = DateUtil.format(new Date(),"yyyyMMdd")+".xls";
    private static final List<String> TITLE = Stream.of("站点", "站点描述", "仓库", "仓库描述", "物料", "物料描述").collect(Collectors.toList());
    @Autowired
    private WmsInvOnhandQtyRecordMapper wmsInvOnhandQtyRecordMapper;
    private static final List<String> TITLE_DATE = Stream.of("入库", "出库", "结存").collect(Collectors.toList());
    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private WmsJobRecordRepository wmsJobRecordRepository;
    @Autowired
    private WmsJobRecordMapper wmsJobRecordMapper;

    @Autowired
    private MtCustomerService mtCustomerService;
    @Autowired
    private MtSupplierService mtSupplierService;

    @Autowired
    private MtInvJournalMapper mtInvJournalMapper;

    @Autowired
    private MtUserClient userClient;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void invOnhandQtyRecordSync(Long tenantId, String syncType){
        if (StringUtils.equals(syncType, WmsConstant.SyncType.ONLINE)){
            // 上线初始化
            onlineInvOnhandQtyRecord(tenantId);
        } else if (StringUtils.equals(syncType, WmsConstant.SyncType.HALF)){
            // 中途初始化
            halfInvOnhandQtyRecord(tenantId);
        } else if (StringUtils.equals(syncType, WmsConstant.SyncType.INIT)){
            // 每日进销存同步
            initInvOnhandQtyRecord(tenantId);
        } else {
            return;
        }
        // 导入成功后，记录本次导入成功时间
        WmsJobRecord job = wmsJobRecordRepository.selectOne(new WmsJobRecord(){{
            setTenantId(tenantId);
            setJobCode(WmsConstant.JobCode.INV_ONHAND_QTY_RECORD_JOB);
        }});
        WmsJobRecord insertJob = new WmsJobRecord();
        insertJob.setTenantId(tenantId);
        insertJob.setJobCode(WmsConstant.JobCode.INV_ONHAND_QTY_RECORD_JOB);
        insertJob.setLastSyncDate(new Date());
        if (Objects.isNull(job)){
            wmsJobRecordRepository.insertSelective(insertJob);
        } else {
            insertJob.setJobRecordId(job.getJobRecordId());
            wmsJobRecordMapper.updateByPrimaryKeySelective(insertJob);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchInsertInvOnhandQtyRecord(List<WmsInvOnhandQtyRecord> recordList){
        if (CollectionUtils.isNotEmpty(recordList)) {
            List<List<WmsInvOnhandQtyRecord>> splitSqlList = InterfaceUtils.splitSqlList(recordList, SQL_ITEM_COUNT_LIMIT);
            for (List<WmsInvOnhandQtyRecord> domains : splitSqlList) {
                wmsInvOnhandQtyRecordMapper.batchInsertInvOnhandQtyRecord(domains);
            }
        }
    }

    @Override
    public List<WmsInvOnhandQtyRecordDTO2> invOnhandQtyQuery(Long tenantId, WmsInvOnhandQtyRecordDTO4 dto) {
        // 获取进销存基础数据
        List<WmsInvOnhandQtyRecordDTO2> groupList = wmsInvOnhandQtyRecordMapper.invOnhandQtyGroupQuery(tenantId, dto);
        if (CollectionUtils.isNotEmpty(groupList)){
            // 获取进销存基础数据对应日期
            List<String> dateList = wmsInvOnhandQtyRecordMapper.invOnhandQtyDateQuery(tenantId, dto);
            List<WmsInvOnhandQtyRecordDTO3> qtyList = wmsInvOnhandQtyRecordMapper.invOnhandQtyQuery(tenantId, dto);
            for (WmsInvOnhandQtyRecordDTO2 group : groupList){
                // 获取每日数据
                List<WmsInvOnhandQtyRecordDTO3> qtyRecordDTO3List = new ArrayList<>(dateList.size());
                for (String showDate : dateList){
                    List<WmsInvOnhandQtyRecordDTO3> anyDateQtyList = qtyList.stream().filter(c ->
                            StringUtils.equals(c.getSiteId(), group.getSiteId()) &&
                            StringUtils.equals(c.getWarehouseId(), group.getWarehouseId())&&
                            StringUtils.equals(c.getMaterialId(), group.getMaterialId())&&
                            StringUtils.equals(c.getShowDate(), showDate)).collect(Collectors.toList());
                    WmsInvOnhandQtyRecordDTO3 qtyRecordDTO3 = new WmsInvOnhandQtyRecordDTO3();
                    if (CollectionUtils.isNotEmpty(anyDateQtyList)) {
                        qtyRecordDTO3 = anyDateQtyList.get(0);
                    } else {
                        qtyRecordDTO3.setShowDate(showDate);
                        qtyRecordDTO3.setSumInQty(WmsConstant.ConstantValue.STRING_ZERO);
                        qtyRecordDTO3.setSumOutQty(WmsConstant.ConstantValue.STRING_ZERO);
                        qtyRecordDTO3.setSumInvRecordQty(WmsConstant.ConstantValue.STRING_ZERO);
                    }
                    qtyRecordDTO3List.add(qtyRecordDTO3);
                }
                group.setQtyRecordDTO3List(qtyRecordDTO3List);
            }
        }
        return groupList;
    }

    @Override
    public void invOnhandQtyExport(Long tenantId, WmsInvOnhandQtyRecordDTO4 dto, HttpServletResponse response) {
        // 获取日期
        List<String> dateList = wmsInvOnhandQtyRecordMapper.invOnhandQtyDateQuery(tenantId, dto);
        // 设置title
        List<String> titleList = new ArrayList<>();
        titleList.addAll(TITLE);
        titleList.addAll(dateList);
        excelExport(tenantId, dto, titleList, response);
    }

    @Override
    public Page<MtInvJournalDTO2> queryInvJournalReport(Long tenantId, WmsInvOnhandJournalDTO dto, PageRequest pageRequest) {
        Page<MtInvJournalDTO2> result = PageHelper.doPageAndSort(pageRequest,
                () -> wmsInvOnhandQtyRecordMapper.queryInvJournalReport(tenantId, dto));

        //查询用户信息
        List<Long> userList = result.stream().map(MtInvJournalDTO2::getEventBy).filter(Objects::nonNull)
                .distinct().collect(Collectors.toList());
        Map<Long, MtUserInfo> userInfoMap = userClient.userInfoBatchGet(tenantId, userList);

        //获取客户或者供应商信息
        List<String> ownerIdList = result.stream().map(MtInvJournalDTO2::getOwnerId).filter(StringUtils::isNotBlank)
                .distinct().collect(Collectors.toList());
        List<MtCustomer> customerList = mtCustomerService.batchSelectCustomerByIdList(tenantId, ownerIdList);
        List<MtSupplier> supplierList = mtSupplierService.batchSelectSupplierByIdList(tenantId, ownerIdList);

        //查询GenType
        MtGenTypeVO2 mtGenTypeVO = new MtGenTypeVO2();
        mtGenTypeVO.setModule("INVENTORY");
        mtGenTypeVO.setTypeGroup("INVENTORY_OWNER_TYPE");
        List<MtGenType> invOwnerTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO);

        //构造返回信息
        for (MtInvJournalDTO2 t : result) {
            switch (t.getOwnerType()) {
                case "SI":
                case "IIS":
                    Optional<MtSupplier> supplier = supplierList.stream()
                            .filter(tt -> tt.getSupplierId().equals(t.getOwnerId())).findFirst();
                    supplier.ifPresent(s -> {
                        t.setOwnerDesc(s.getSupplierName());
                        t.setOwnerCode(s.getSupplierCode());
                    });
                    break;
                case "CI":
                case "IIC":
                    Optional<MtCustomer> customer = customerList.stream()
                            .filter(tt -> tt.getCustomerId().equals(t.getOwnerId())).findFirst();
                    customer.ifPresent(c -> {
                        t.setOwnerDesc(c.getCustomerName());
                        t.setOwnerCode(c.getCustomerCode());
                    });
                    break;
                case "OI":
                case "PI":
                    t.setOwnerCode(t.getOwnerId());
                    t.setOwnerDesc("");
                    break;
                default:
                    break;
            }

            Optional<MtGenType> first =
                    invOwnerTypes.stream().filter(tt -> tt.getTypeCode().equals(t.getOwnerType())).findFirst();
            first.ifPresent(mtGenType -> t.setOwnerTypeDesc(mtGenType.getDescription()));

            if (userInfoMap != null && userInfoMap.containsKey(t.getEventBy())) {
                t.setEventByUserName(userInfoMap.get(t.getEventBy()).getLoginName());
            }
        }

        return result;
    }

    private void onlineInvOnhandQtyRecord(Long tenantId){
        List<WmsInvOnhandQtyRecord> invOnhandQtyRecordList = new ArrayList<>();
        // 获取库存日记账数据
        List<WmsInvOnhandQtyRecordDTO> invJournalList = wmsInvOnhandQtyRecordMapper.invJournalQuery(tenantId, null);
        WmsInvOnhandQtyRecord record = null;
        // 获取序列
        List<String> ids = customDbRepository.getNextKeys("wms_inv_onhand_qty_record_s", invJournalList.size());
        List<String> cids = customDbRepository.getNextKeys("wms_inv_onhand_qty_record_cid_s", invJournalList.size());
        for (WmsInvOnhandQtyRecordDTO dto : invJournalList) {
            record = new WmsInvOnhandQtyRecord();
            record.setOnhandQtyRecordId(ids.remove(0));
            record.setCid(Long.valueOf(cids.remove(0)));
            record.setTenantId(dto.getTenantId());
            record.setSiteId(dto.getSiteId());
            record.setMaterialId(dto.getMaterialId());
            record.setLocatorId(dto.getLocatorId());
            BigDecimal inQty = dto.getInQty() == null ? BigDecimal.ZERO : dto.getInQty();
            BigDecimal outQty = dto.getOutQty() == null ? BigDecimal.ZERO : dto.getOutQty();
            record.setOutQty(outQty);
            record.setInQty(inQty);
            record.setInvRecordQty(inQty.subtract(outQty.abs()));
            record.setShotDate(new Date());
            record.setShowDate(new Date());
            record.setLot(null);
            record.setObjectVersionNumber(1L);
            record.setCreationDate(new Date());
            record.setLastUpdateDate(new Date());
            invOnhandQtyRecordList.add(record);
        }
        self().batchInsertInvOnhandQtyRecord(invOnhandQtyRecordList);
    }

    private void halfInvOnhandQtyRecord(Long tenantId){
        // 获取现有量数据
        List<WmsInvOnhandQtyRecordDTO> invOnhandHoldList = wmsInvOnhandQtyRecordMapper.invOnhandQuantityQuery(tenantId);
        if (CollectionUtils.isNotEmpty(invOnhandHoldList)){
            // 获取当前时间
            LocalDate startDate = LocalDate.now();
            Date showDate = Date.from(startDate.minusDays(1L).atStartOfDay(ZoneId.systemDefault()).toInstant());
            // 获取当前时间 前一天的库存日记账数据
            List<WmsInvOnhandQtyRecordDTO> oldInvJournalList = wmsInvOnhandQtyRecordMapper.invJournalQuery(tenantId, startDate);
            WmsInvOnhandQtyRecord record = null;
            //BeanCopier copier = BeanCopier.create(WmsInvOnhandQtyRecordDTO.class, WmsInvOnhandQtyRecord.class, false);
            // 获取序列
            List<String> ids = customDbRepository.getNextKeys("wms_inv_onhand_qty_record_s", invOnhandHoldList.size());
            List<String> cids = customDbRepository.getNextKeys("wms_inv_onhand_qty_record_cid_s", invOnhandHoldList.size());
            List<WmsInvOnhandQtyRecord> invOnhandQtyRecordList = new ArrayList<>();
            for (WmsInvOnhandQtyRecordDTO dto : invOnhandHoldList) {
                record = new WmsInvOnhandQtyRecord();
                record.setOnhandQtyRecordId(ids.remove(0));
                record.setCid(Long.valueOf(cids.remove(0)));
                record.setTenantId(dto.getTenantId());
                record.setSiteId(dto.getSiteId());
                record.setMaterialId(dto.getMaterialId());
                record.setLocatorId(dto.getLocatorId());
                List<WmsInvOnhandQtyRecordDTO> recordDTOList = oldInvJournalList.stream().filter(c -> dto.getTenantId().equals(c.getTenantId()) &&
                        dto.getSiteId().equals(c.getSiteId()) && dto.getLocatorId().equals(c.getLocatorId()) &&
                        dto.getMaterialId().equals(c.getMaterialId())).collect(Collectors.toList());
                BigDecimal inQty = BigDecimal.ZERO;
                BigDecimal outQty = BigDecimal.ZERO;
                if (CollectionUtils.isNotEmpty(recordDTOList)) {
                    inQty = recordDTOList.get(0).getInQty() == null ? BigDecimal.ZERO : recordDTOList.get(0).getInQty();
                    outQty = recordDTOList.get(0).getOutQty() == null ? BigDecimal.ZERO : recordDTOList.get(0).getOutQty();
                }
                record.setOutQty(outQty);
                record.setInQty(inQty);
                record.setInvRecordQty(dto.getInvRecordQty());
                record.setShotDate(showDate);
                record.setShowDate(showDate);
                record.setLot(null);
                record.setObjectVersionNumber(1L);
                record.setCreationDate(new Date());
                record.setLastUpdateDate(new Date());
                invOnhandQtyRecordList.add(record);
            }
            if (CollectionUtils.isNotEmpty(oldInvJournalList)){
                invOnhandQtyRecordList.addAll(convertOldJournalData(oldInvJournalList, invOnhandQtyRecordList, showDate));
            }
            self().batchInsertInvOnhandQtyRecord(invOnhandQtyRecordList);
        }
    }

    private void initInvOnhandQtyRecord(Long tenantId){
        // 获取最后一次同步时间
        LocalDate lastRunDate = wmsInvOnhandQtyRecordMapper.jobLastSyncDateGet(tenantId, WmsConstant.JobCode.INV_ONHAND_QTY_RECORD_JOB);
        if (lastRunDate == null) {
            lastRunDate = LocalDate.now().minusDays(1L);
        }
        // 获取当前时间
        LocalDate currDate = LocalDate.now();
        // 计算当前时间与最后更新时间相隔多少天，
        // Period next = Period.between(localDate, currDate);
        Long diffDays = currDate.toEpochDay() - lastRunDate.toEpochDay();
        // 漏掉某天的数据,则需要重新补齐，保持每日数据连贯性
        for (Long i = 1L; i<= diffDays; i++) {
            // 按上次同步时间往后累加
            LocalDate startDate = lastRunDate.plusDays(i);
            Date showDate = Date.from(startDate.minusDays(1L).atStartOfDay(ZoneId.systemDefault()).toInstant());
            // 清除当天数据
            wmsInvOnhandQtyRecordMapper.batchDeleteList(tenantId, showDate);
            // 获取库存日记账数据
            List<WmsInvOnhandQtyRecordDTO> invJournalList = wmsInvOnhandQtyRecordMapper.invJournalQuery(tenantId, startDate);
            List<WmsInvOnhandQtyRecord> recordList = new ArrayList<>();
            if (CollectionUtils.isEmpty(invJournalList)) {
                // 如果无当天数据，则获取前一天数据
                List<WmsInvOnhandQtyRecord> invOnhandQtyRecordList =
                        wmsInvOnhandQtyRecordMapper.invOnhandQtyRecordQuery(tenantId, startDate);
                if (CollectionUtils.isEmpty(invOnhandQtyRecordList)) {
                    // batchInsertInvOnhandQtyRecord
                    // 获取序列
                    List<String> ids = customDbRepository.getNextKeys("wms_inv_onhand_qty_record_s", invOnhandQtyRecordList.size());
                    List<String> cids = customDbRepository.getNextKeys("wms_inv_onhand_qty_record_cid_s", invOnhandQtyRecordList.size());
                    invOnhandQtyRecordList.forEach(item -> {
                        item.setOnhandQtyRecordId(ids.remove(0));
                        item.setCid(Long.valueOf(cids.remove(0)));
                        item.setShotDate(showDate);
                        item.setShowDate(showDate);
                    });
                    recordList.addAll(invOnhandQtyRecordList);
                }
            } else {
                recordList.addAll(convertRecordData(tenantId, invJournalList, showDate, startDate));
            }
            // 需按每日进行数据导入
            self().batchInsertInvOnhandQtyRecord(recordList);
        }
        // self().batchInsertInvOnhandQtyRecord(recordList);
    }

    private List<WmsInvOnhandQtyRecord> convertRecordData(Long tenantId, List<WmsInvOnhandQtyRecordDTO> invJournalList,
                                                          Date showDate, LocalDate startDate){
        List<WmsInvOnhandQtyRecord> invOnhandQtyRecordList = new ArrayList<>();
        WmsInvOnhandQtyRecord record = null;
        // 获取序列
        List<String> ids = customDbRepository.getNextKeys("wms_inv_onhand_qty_record_s", invJournalList.size());
        List<String> cids = customDbRepository.getNextKeys("wms_inv_onhand_qty_record_cid_s", invJournalList.size());
        // 获取前一天进销存数据
        List<WmsInvOnhandQtyRecord> oldRecordList =
                wmsInvOnhandQtyRecordMapper.invOnhandQtyRecordQuery(tenantId, startDate.minusDays(1L));
        for (WmsInvOnhandQtyRecordDTO dto : invJournalList){
            record = new WmsInvOnhandQtyRecord();
            record.setOnhandQtyRecordId(ids.remove(0));
            record.setCid(Long.valueOf(cids.remove(0)));
            record.setTenantId(dto.getTenantId());
            record.setSiteId(dto.getSiteId());
            record.setMaterialId(dto.getMaterialId());
            record.setLocatorId(dto.getLocatorId());
            List<WmsInvOnhandQtyRecord> records = oldRecordList.stream().filter(c -> dto.getTenantId().equals(c.getTenantId()) &&
                    dto.getSiteId().equals(c.getSiteId()) && dto.getLocatorId().equals(c.getLocatorId()) &&
                    dto.getMaterialId().equals(c.getMaterialId())).collect(Collectors.toList());
            BigDecimal lastInvRecordQty = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(records)) {
                lastInvRecordQty = records.get(0).getInvRecordQty();
            }
            // 前一天的当前库存+入库数量-出库数量
            BigDecimal inQty = dto.getInQty() == null ? BigDecimal.ZERO : dto.getInQty();
            BigDecimal outQty = dto.getOutQty() == null ? BigDecimal.ZERO : dto.getOutQty();
            BigDecimal invRecordQty = lastInvRecordQty.add(inQty).subtract(outQty.abs());
            record.setOutQty(outQty);
            record.setInQty(inQty);
            record.setInvRecordQty(invRecordQty);
            record.setShotDate(showDate);
            record.setShowDate(showDate);
            record.setLot(null);
            record.setObjectVersionNumber(1L);
            record.setCreationDate(new Date());
            record.setLastUpdateDate(new Date());
            invOnhandQtyRecordList.add(record);
        }
        if (CollectionUtils.isNotEmpty(oldRecordList)){
            invOnhandQtyRecordList.addAll(convertOldRecordData(oldRecordList, invOnhandQtyRecordList, showDate));
        }
        return invOnhandQtyRecordList;
    }

    /**
     * 前一天存在进销存数据处理
     *
     * @param oldRecordList
     * @param invOnhandQtyList
     * @param showDate
     * @author jiangling.zheng@hand-china.com 2020/11/24 21:57
     * @return java.util.List<com.ruike.wms.domain.entity.WmsInvOnhandQtyRecord>
     */

    private List<WmsInvOnhandQtyRecord> convertOldRecordData(List<WmsInvOnhandQtyRecord> oldRecordList,
                                                              List<WmsInvOnhandQtyRecord> invOnhandQtyList, Date showDate){
        List<WmsInvOnhandQtyRecord> invOnhandQtyRecordList = new ArrayList<>();
        WmsInvOnhandQtyRecord record = null;
        // 前一天存在进销存数据，而当天没有的数据，记录到当天记录中
        for (WmsInvOnhandQtyRecord dto : oldRecordList){
            List<WmsInvOnhandQtyRecord> records = invOnhandQtyList.stream().filter(c -> dto.getTenantId().equals(c.getTenantId()) &&
                    dto.getSiteId().equals(c.getSiteId()) && dto.getLocatorId().equals(c.getLocatorId()) &&
                    dto.getMaterialId().equals(c.getMaterialId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(records)) {
                continue;
            }
            record = new WmsInvOnhandQtyRecord();
            record.setTenantId(dto.getTenantId());
            record.setSiteId(dto.getSiteId());
            record.setMaterialId(dto.getMaterialId());
            record.setLocatorId(dto.getLocatorId());
            record.setOutQty(BigDecimal.ZERO);
            record.setInQty(BigDecimal.ZERO);
            record.setInvRecordQty(dto.getInvRecordQty() == null ? BigDecimal.ZERO : dto.getInvRecordQty());
            record.setShotDate(showDate);
            record.setShowDate(showDate);
            record.setLot(null);
            record.setObjectVersionNumber(1L);
            record.setCreationDate(new Date());
            record.setLastUpdateDate(new Date());
            invOnhandQtyRecordList.add(record);
        }
        // 获取序列
        List<String> ids = customDbRepository.getNextKeys("wms_inv_onhand_qty_record_s", invOnhandQtyRecordList.size());
        List<String> cids = customDbRepository.getNextKeys("wms_inv_onhand_qty_record_cid_s", invOnhandQtyRecordList.size());
        invOnhandQtyRecordList.forEach(item -> {
            item.setOnhandQtyRecordId(ids.remove(0));
            item.setCid(Long.valueOf(cids.remove(0)));
        });
        return invOnhandQtyRecordList;
    }

    private List<WmsInvOnhandQtyRecord> convertOldJournalData(List<WmsInvOnhandQtyRecordDTO> invJournalList,
                                                             List<WmsInvOnhandQtyRecord> invOnhandQtyList, Date showDate){
        List<WmsInvOnhandQtyRecord> invOnhandQtyRecordList = new ArrayList<>();
        WmsInvOnhandQtyRecord record = null;
        // 前一天存在库存日记账数据，而现有量没有的数据，记录到当天记录中
        for (WmsInvOnhandQtyRecordDTO dto : invJournalList){
            List<WmsInvOnhandQtyRecord> records = invOnhandQtyList.stream().filter(c -> dto.getTenantId().equals(c.getTenantId()) &&
                    dto.getSiteId().equals(c.getSiteId()) && dto.getLocatorId().equals(c.getLocatorId()) &&
                    dto.getMaterialId().equals(c.getMaterialId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(records)) {
                continue;
            }
            BigDecimal inQty = dto.getInQty() == null ? BigDecimal.ZERO : dto.getInQty();
            BigDecimal outQty = dto.getOutQty() == null ? BigDecimal.ZERO : dto.getOutQty();
            record = new WmsInvOnhandQtyRecord();
            record.setTenantId(dto.getTenantId());
            record.setSiteId(dto.getSiteId());
            record.setMaterialId(dto.getMaterialId());
            record.setLocatorId(dto.getLocatorId());
            record.setOutQty(outQty);
            record.setInQty(inQty);
            record.setInvRecordQty(BigDecimal.ZERO);
            record.setShotDate(showDate);
            record.setShowDate(showDate);
            record.setLot(null);
            record.setObjectVersionNumber(1L);
            record.setCreationDate(new Date());
            record.setLastUpdateDate(new Date());
            invOnhandQtyRecordList.add(record);
        }
        // 获取序列
        List<String> ids = customDbRepository.getNextKeys("wms_inv_onhand_qty_record_s", invOnhandQtyRecordList.size());
        List<String> cids = customDbRepository.getNextKeys("wms_inv_onhand_qty_record_cid_s", invOnhandQtyRecordList.size());
        invOnhandQtyRecordList.forEach(item -> {
            item.setOnhandQtyRecordId(ids.remove(0));
            item.setCid(Long.valueOf(cids.remove(0)));
        });
        return invOnhandQtyRecordList;
    }

    private void excelExport(Long tenantId, WmsInvOnhandQtyRecordDTO4 dto, List<String> titleList, HttpServletResponse response) {

        response.setContentType("application/vnd.ms-excel; charset=\"UTF-8\"");
        response.setHeader("Content-Disposition", "attachment;filename=" + FILE_NAME);
        //创建文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建sheet页
        HSSFSheet sheet = workbook.createSheet(SHEET_NAME);
        //创建题头工作薄
        createExcelHead(workbook, sheet, titleList);

        //创建内容工作薄
        createExcelLine(tenantId, dto, workbook, sheet);

        try {
            OutputStream out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建表头的方法
     *
     * @param workbook
     * @param sheet
     * @param titleList
     * @author jiangling.zheng@hand-china.com 2020/11/21 15:54
     * @return void
     */
    private void createExcelHead(HSSFWorkbook workbook, HSSFSheet sheet, List<String> titleList) {
        HSSFRow row = sheet.createRow(0);
        HSSFRow rowTwo = sheet.createRow(1);
        HSSFCell cell = null;
        HSSFCellStyle style = getCellStyle(workbook);
        int i = 0;
        List<String> titleColList = new ArrayList<>();
        titleColList.addAll(TITLE_DATE);
        for (String col : titleList) {
            if (i < 6){
                sheet.setColumnWidth(i, 6000);
                sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(),
                        rowTwo.getRowNum(), i, i));
                cell = row.createCell(i);
                cell.setCellStyle(style);
                cell.setCellValue(col);
                i++;
            }
            else {
                HSSFCell cell1 = row.createCell(i);
                cell1.setCellStyle(style);
                cell1.setCellValue(col);
                HSSFCell cell2 = row.createCell((i + 1));
                cell2.setCellStyle(style);
                cell2.setCellValue(col);
                HSSFCell cell3 = row.createCell((i + 2));
                cell3.setCellStyle(style);
                cell3.setCellValue(col);
                sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(),
                        row.getRowNum(), i, (i + 2)));
                HSSFCell cell4 = null;
                int j = 0;
                for (String colTitle : titleColList) {
                    cell4 = rowTwo.createCell((i + j));
                    sheet.setColumnWidth((i + j) , 2000);
                    cell4.setCellStyle(style);
                    cell4.setCellValue(colTitle);
                    j++;
                }
                i = i + 3;
            }
        }
    }

    /**
     * 创建内容的方法
     *
     * @param tenantId
     * @param dto
     * @param workbook
     * @param sheet
     * @author jiangling.zheng@hand-china.com 2020/11/21 13:31
     * @return void
     */
    private void createExcelLine(Long tenantId, WmsInvOnhandQtyRecordDTO4 dto, HSSFWorkbook workbook, HSSFSheet sheet) {
        List<WmsInvOnhandQtyRecordDTO2> groupList = wmsInvOnhandQtyRecordMapper.invOnhandQtyGroupQuery(tenantId, dto);
        if (CollectionUtils.isNotEmpty(groupList)){
            // 获取进销存基础数据对应日期
            List<String> dateList = wmsInvOnhandQtyRecordMapper.invOnhandQtyDateQuery(tenantId, dto);
            List<WmsInvOnhandQtyRecordDTO3> qtyList = wmsInvOnhandQtyRecordMapper.invOnhandQtyQuery(tenantId, dto);
            int i = 2;
            for (WmsInvOnhandQtyRecordDTO2 group : groupList){
                //从第二行开始插入内容
                HSSFRow row = sheet.createRow(i);

                // excel固定列
                HSSFCell cell1 = row.createCell(0);
                cell1.setCellStyle(getCellStyleLine(workbook,0));
                cell1.setCellValue(group.getSiteCode());
                HSSFCell cell2 = row.createCell(1);
                cell2.setCellStyle(getCellStyleLine(workbook,1));
                cell2.setCellValue(group.getSiteName());
                HSSFCell cell3 = row.createCell(2);
                cell3.setCellStyle(getCellStyleLine(workbook,2));
                cell3.setCellValue(group.getWarehouseCode());
                HSSFCell cell4 = row.createCell(3);
                cell4.setCellStyle(getCellStyleLine(workbook,3));
                cell4.setCellValue(group.getWarehouseName());
                HSSFCell cell5 = row.createCell(4);
                cell5.setCellStyle(getCellStyleLine(workbook,4));
                cell5.setCellValue(group.getMaterialCode());
                HSSFCell cell6 = row.createCell(5);
                cell6.setCellStyle(getCellStyleLine(workbook,5));
                cell6.setCellValue(group.getMaterialName());
                // 获取每日数据
                int j = 6;
                for (String showDate : dateList){
                    List<WmsInvOnhandQtyRecordDTO3> anyDateQtyList = qtyList.stream().filter(c ->
                            StringUtils.equals(c.getSiteId(), group.getSiteId()) &&
                                    StringUtils.equals(c.getWarehouseId(), group.getWarehouseId())&&
                                    StringUtils.equals(c.getMaterialId(), group.getMaterialId())&&
                                    StringUtils.equals(c.getShowDate(), showDate)).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(anyDateQtyList)) {
                        HSSFCell cell7 = row.createCell(j);
                        cell7.setCellStyle(getCellStyleLine(workbook,j));
                        cell7.setCellValue(anyDateQtyList.get(0).getSumInQty());
                        HSSFCell cell8 = row.createCell((j + 1));
                        cell8.setCellStyle(getCellStyleLine(workbook,(j + 1)));
                        cell8.setCellValue(anyDateQtyList.get(0).getSumOutQty());
                        HSSFCell cell9 = row.createCell((j + 2));
                        cell9.setCellStyle(getCellStyleLine(workbook,(j + 2)));
                        cell9.setCellValue(anyDateQtyList.get(0).getSumInvRecordQty());
                    } else {
                        HSSFCell cell7 = row.createCell(j);
                        cell7.setCellStyle(getCellStyleLine(workbook,j));
                        cell7.setCellValue(WmsConstant.ConstantValue.STRING_ZERO);
                        HSSFCell cell8 = row.createCell((j + 1));
                        cell8.setCellStyle(getCellStyleLine(workbook,(j + 1)));
                        cell8.setCellValue(WmsConstant.ConstantValue.STRING_ZERO);
                        HSSFCell cell9 = row.createCell((j + 2));
                        cell9.setCellStyle(getCellStyleLine(workbook,(j + 2)));
                        cell9.setCellValue(WmsConstant.ConstantValue.STRING_ZERO);
                    }
                    j = j + 3;
                }
                i++;
            }
        }
    }

    /**
     * 设置头表样式
     *
     * @param workbook
     * @author jiangling.zheng@hand-china.com 2020/11/20 16:31
     * @return org.apache.poi.hssf.usermodel.HSSFCellStyle
     */
    private HSSFCellStyle getCellStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    /**
     * 设置行表样式
     *
     * @param workbook
     * @param col
     * @author jiangling.zheng@hand-china.com 2020/11/21 13:32
     * @return org.apache.poi.ss.usermodel.CellStyle
     */
    private CellStyle getCellStyleLine(Workbook workbook, int col) {
        //列样式变化
        CellStyle styleBlue = workbook.createCellStyle();
        styleBlue.setLocked(true);
        if (col < 6) {
            styleBlue.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
        } else {
            styleBlue.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
        }
        return styleBlue;
    }

}
