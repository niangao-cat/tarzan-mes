package tarzan.iface.infra.repository.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.helper.LanguageHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import io.tarzan.common.domain.util.StringHelper;
import io.tarzan.common.domain.vo.MtExtendAttrVO1;
import io.tarzan.common.domain.vo.MtExtendVO1;
import io.tarzan.common.domain.vo.MtExtendVO5;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.entity.MtWorkOrderIface;
import tarzan.iface.domain.repository.MtSitePlantReleationRepository;
import tarzan.iface.domain.repository.MtWorkOrderIfaceRepository;
import tarzan.iface.domain.vo.MtSitePlantReleationVO;
import tarzan.iface.domain.vo.MtSitePlantReleationVO1;
import tarzan.iface.domain.vo.MtSitePlantReleationVO3;
import tarzan.iface.infra.mapper.MtWorkOrderIfaceMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.entity.MtWorkOrderHis;
import tarzan.order.domain.repository.MtWorkOrderRepository;

/**
 * 工单接口表 资源库实现
 *
 * @author xiao.tang02@hand-china.com 2019-08-23 14:16:17
 */
@Component
public class MtWorkOrderIfaceRepositoryImpl extends BaseRepositoryImpl<MtWorkOrderIface>
        implements MtWorkOrderIfaceRepository {

    private static final int SQL_ITEM_COUNT_LIMIT = 1000;

    private static final String WORK_ORDER_ATTR_TABLE = "mt_work_order_attr";

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtSitePlantReleationRepository mtSitePlantReleationRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorService;

    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtWorkOrderIfaceMapper mtWorkOrderIfaceMapper;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Override
    public void workOrderInterfaceImport(Long tenantId) {
        Long start = System.currentTimeMillis();
        List<MtWorkOrderIface> bomComponentIfaceList = self().updateIfaceStatus(tenantId, "P");
        // get data list
        Map<Double, List<MtWorkOrderIface>> ifacePerBatch = bomComponentIfaceList.stream().collect(
                Collectors.groupingBy(MtWorkOrderIface::getBatchId, TreeMap::new, Collectors.toList()));

        for (Map.Entry<Double, List<MtWorkOrderIface>> entry : ifacePerBatch.entrySet()) {
            try {
                // change data status
                List<AuditDomain> ifaceList = saveWorkOrderIface(tenantId, entry.getValue());
                log(tenantId, ifaceList);
            } catch (Exception e) {
                List<AuditDomain> ifaceList = new ArrayList<>(entry.getValue().size());

                String errorMsg = StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage().replace("'", "\"") : "数据异常.";
                if (errorMsg.length() > 1000) {
                    errorMsg = errorMsg.substring(errorMsg.length() - 1000);
                }
                final String msg = errorMsg;
                entry.getValue().forEach(ifs -> {
                    ifs.setStatus("E");
                    ifs.setMessage(msg);

                });
                ifaceList.addAll(entry.getValue());
                log(tenantId, ifaceList);
            }
        }
        System.out.println("工单接口接口导入" + bomComponentIfaceList.size() + "条数据，总耗时：" + (System.currentTimeMillis() - start)
                + "毫秒");
    }

    @Override
    public void myWorkOrderInterfaceImport(Long tenantId, Long batchId) {
        Long start = System.currentTimeMillis();
        List<MtWorkOrderIface> bomComponentIfaceList = self().myUpdateIfaceStatus(tenantId, "P", batchId);
        // get data list
        Map<Double, List<MtWorkOrderIface>> ifacePerBatch = bomComponentIfaceList.stream().collect(
                Collectors.groupingBy(MtWorkOrderIface::getBatchId, TreeMap::new, Collectors.toList()));

        for (Map.Entry<Double, List<MtWorkOrderIface>> entry : ifacePerBatch.entrySet()) {
            try {
                // change data status
                List<AuditDomain> ifaceList = saveWorkOrderIface(tenantId, entry.getValue());
                log(tenantId, ifaceList);
            } catch (Exception e) {
                List<AuditDomain> ifaceList = new ArrayList<>(entry.getValue().size());
                e.printStackTrace();
                String error = e.getMessage().length() > 1000 ? e.getMessage().substring(0, 999) : e.getMessage();
                String msg = error + ";数据异常.";
                entry.getValue().forEach(ifs -> {
                    ifs.setStatus("E");
                    ifs.setMessage(msg);

                });
                ifaceList.addAll(entry.getValue());
                log(tenantId, ifaceList);
            }
        }
        System.out.println("工单接口接口导入" + bomComponentIfaceList.size() + "条数据，总耗时：" + (System.currentTimeMillis() - start)
                + "毫秒");
    }

    @Override
    public List<AuditDomain> saveWorkOrderIface(Long tenantId, List<MtWorkOrderIface> mtWorkOrderIfaceList) {
        List<AuditDomain> ifaceList = new ArrayList<>(mtWorkOrderIfaceList.size());
        Long userId = Long.valueOf(-1L);
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        Date now = new Date(System.currentTimeMillis());
        Map<String, List<MtWorkOrderIface>> ifacePerPlant =
                mtWorkOrderIfaceList.stream().collect(Collectors.groupingBy(MtWorkOrderIface::getPlantCode));

        // query site plant relation
        Map<String, MtSitePlantReleation> sitePlantMap =
                getSitePlantRelation(tenantId, new ArrayList<>(ifacePerPlant.keySet()));

        // ItemCode
        List<String> itemCodes = mtWorkOrderIfaceList.stream().map(MtWorkOrderIface::getItemCode).distinct()
                .collect(Collectors.toList());
        Map<ItemPlant, List<MtSitePlantReleationVO1>> materialSiteMap =
                getMaterialSite(tenantId, new ArrayList<>(ifacePerPlant.keySet()), itemCodes);
        // 根据物料编码获取物料信息
        List<MtMaterial> mtMaterials = mtMaterialRepository.queryMaterialByCode(tenantId, itemCodes);
        Map<String, MtMaterial> mtMaterialMap =
                mtMaterials.stream().collect(Collectors.toMap(MtMaterial::getMaterialCode, c -> c));
        // locatorCodes
        List<String> locatorCodes = mtWorkOrderIfaceList.stream()
                .filter(c -> StringUtils.isNotEmpty(c.getCompleteLocator()))
                .map(MtWorkOrderIface::getCompleteLocator).distinct().collect(Collectors.toList());
        List<MtModLocator> mtModLocators = mtModLocatorService.selectModLocatorForCodes(tenantId, locatorCodes);
        Map<String, MtModLocator> modLocatorMap =
                mtModLocators.stream().collect(Collectors.toMap(MtModLocator::getLocatorCode, c -> c));
        // proLineCodes
        List<String> proLineCodes =
                mtWorkOrderIfaceList.stream().filter(c -> StringUtils.isNotEmpty(c.getProdLineCode()))
                        .map(MtWorkOrderIface::getProdLineCode).distinct().collect(Collectors.toList());
        List<MtModProductionLine> mtModProductionLines =
                mtModProductionLineRepository.prodLineByproLineCodes(tenantId, proLineCodes);
        Map<String, MtModProductionLine> modProductionLineMap = mtModProductionLines.stream()
                .collect(Collectors.toMap(MtModProductionLine::getProdLineCode, c -> c));
        // woNum
        List<String> woNum = mtWorkOrderIfaceList.stream().filter(c -> StringUtils.isNotEmpty(c.getWorkOrderNum()))
                .map(MtWorkOrderIface::getWorkOrderNum).distinct().collect(Collectors.toList());
        List<MtWorkOrder> mtWorkOrders = mtWorkOrderRepository.woLimitWorkNUmQuery(tenantId, woNum);
        Map<String, MtWorkOrder> workOrderMap =
                mtWorkOrders.stream().collect(Collectors.toMap(MtWorkOrder::getWorkOrderNum, c -> c));

        // create event
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("WO_INTERFACE_UPDATE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        ItemPlant itemPlant;
        MtMaterial mtMaterial;
        MtWorkOrder mtWorkOrder;
        MtWorkOrderHis mtWorkOrderHis;

        List<String> newWoCids = this.customDbRepository.getNextKeys("mt_work_order_cid_s", mtWorkOrderIfaceList.size());
        List<String> newWoIds = this.customDbRepository.getNextKeys("mt_work_order_s", mtWorkOrderIfaceList.size());
        List<String> newWoHisCids =
                this.customDbRepository.getNextKeys("mt_work_order_his_cid_s", mtWorkOrderIfaceList.size());
        List<String> newWoHIsIds = this.customDbRepository.getNextKeys("mt_work_order_his_s", mtWorkOrderIfaceList.size());

        List<AuditDomain> workOrderList = new ArrayList<>(mtWorkOrderIfaceList.size());
        List<AuditDomain> workOrderHisList = new ArrayList<>(mtWorkOrderIfaceList.size());
        Map<MtWorkOrderIface, String> attrMap = new HashMap<>(mtWorkOrderIfaceList.size());
        for (Map.Entry<String, List<MtWorkOrderIface>> entry : ifacePerPlant.entrySet()) {
            // query material site relation
            for (MtWorkOrderIface iface : entry.getValue()) {
                itemPlant = new ItemPlant(iface.getPlantCode(), iface.getItemCode());
                if (sitePlantMap == null || sitePlantMap.get(entry.getKey()) == null) {
                    ifaceList.add(constructIfaceMessage(tenantId, iface, "E",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0012",
                                    "INTERFACE", iface.getIfaceId(), "【API:workOrderInterfaceImport】"),
                            now, userId));
                    continue;
                }
                if (MapUtils.isEmpty(materialSiteMap) || CollectionUtils.isEmpty(materialSiteMap.get(itemPlant))) {
                    ifaceList.add(constructIfaceMessage(tenantId, iface, "E",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0023",
                                    "INTERFACE", iface.getIfaceId(), "【API:workOrderInterfaceImport】"),
                            now, userId));
                    continue;
                }

                if (MapUtils.isEmpty(mtMaterialMap) || mtMaterialMap.get(iface.getItemCode()) == null) {
                    ifaceList.add(constructIfaceMessage(tenantId, iface, "E",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0019",
                                    "INTERFACE", iface.getIfaceId(), "【API:workOrderInterfaceImport】"),
                            now, userId));
                    continue;
                }
                mtMaterial = mtMaterialMap.get(iface.getItemCode());
                String locatorId = null;
                if (StringUtils.isNotEmpty(iface.getCompleteLocator())) {
                    if (MapUtils.isEmpty(modLocatorMap) || modLocatorMap.get(iface.getCompleteLocator()) == null) {
                        ifaceList.add(constructIfaceMessage(tenantId, iface, "E",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0020",
                                        "INTERFACE", iface.getIfaceId(),
                                        "【API:workOrderInterfaceImport】"),
                                now, userId));
                        continue;
                    }
                    locatorId = modLocatorMap.get(iface.getCompleteLocator()).getLocatorId();
                }

                String prodLineId = null;
                if (StringUtils.isNotEmpty(iface.getProdLineCode())) {
                    if (MapUtils.isEmpty(modProductionLineMap)
                            || modProductionLineMap.get(iface.getProdLineCode()) == null) {
                        ifaceList.add(constructIfaceMessage(tenantId, iface, "E",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0021",
                                        "INTERFACE", iface.getIfaceId(),
                                        "【API:workOrderInterfaceImport】"),
                                now, userId));
                        continue;
                    } else {
                        prodLineId = modProductionLineMap.get(iface.getProdLineCode()).getProdLineId();
                    }
                }

                // query origin work order
                mtWorkOrder = workOrderMap.get(iface.getWorkOrderNum());
                if (mtWorkOrder != null
                        && mtWorkOrder.getSiteId().equals(sitePlantMap.get(entry.getKey()).getSiteId())) {
                    if ("EORELEASED".equals(mtWorkOrder.getStatus())
                            && !"RELEASED".equals(iface.getWorkOrderStatus())) {
                        ifaceList.add(constructIfaceMessage(tenantId, iface, "E",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0025",
                                        iface.getIfaceId(),
                                        "【API:workOrderInterfaceImport】"),
                                now, userId));
                        continue;
                    }
                }
                if (mtWorkOrder == null) {
                    mtWorkOrder = new MtWorkOrder();
                    mtWorkOrder.setLastWoStatus("");
                    mtWorkOrder.setCreatedBy(userId);
                    mtWorkOrder.setCreationDate(now);
                    mtWorkOrder.setObjectVersionNumber(1L);
                    mtWorkOrder.setWorkOrderId(newWoIds.remove(0));
                } else {
                    mtWorkOrder.setObjectVersionNumber(mtWorkOrder.getObjectVersionNumber() + 1L);
                }
                mtWorkOrder.setCid(Long.valueOf(newWoCids.remove(0)));
                mtWorkOrder.setLastUpdatedBy(userId);
                mtWorkOrder.setLastUpdateDate(now);
                mtWorkOrder.setTenantId(tenantId);

                // construct work order object
                Double preQty = mtWorkOrder.getQty() == null ? Double.valueOf(0.0D) : mtWorkOrder.getQty();
                mtWorkOrder = constructWorkOrder(iface, mtWorkOrder, sitePlantMap.get(entry.getKey()).getSiteId(),
                        mtMaterial.getMaterialId(), mtMaterial.getPrimaryUomId(), locatorId, prodLineId,
                        iface.getWorkOrderStatus(), iface.getWorkOrderId(), now);

                Double AfterQty = mtWorkOrder.getQty() == null ? Double.valueOf(0.0D) : mtWorkOrder.getQty();
                mtWorkOrderHis = new MtWorkOrderHis();
                mtWorkOrderHis.setTenantId(tenantId);
                mtWorkOrderHis.setWorkOrderHisId(newWoHIsIds.remove(0));
                mtWorkOrderHis.setWorkOrderId(mtWorkOrder.getWorkOrderId());
                mtWorkOrderHis.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
                mtWorkOrderHis.setWorkOrderType(mtWorkOrder.getWorkOrderType());
                mtWorkOrderHis.setSiteId(mtWorkOrder.getSiteId());
                mtWorkOrderHis.setProductionLineId(mtWorkOrder.getProductionLineId());
                mtWorkOrderHis.setWorkcellId(mtWorkOrder.getWorkcellId());
                mtWorkOrderHis.setMakeOrderId(mtWorkOrder.getMakeOrderId());
                mtWorkOrderHis.setProductionVersion(mtWorkOrder.getProductionVersion());
                mtWorkOrderHis.setMaterialId(mtWorkOrder.getMaterialId());
                mtWorkOrderHis.setQty(mtWorkOrder.getQty());
                mtWorkOrderHis.setUomId(mtWorkOrder.getUomId());
                mtWorkOrderHis.setPriority(mtWorkOrder.getPriority());
                mtWorkOrderHis.setStatus(mtWorkOrder.getStatus());
                mtWorkOrderHis.setLastWoStatus(mtWorkOrder.getLastWoStatus());
                mtWorkOrderHis.setPlanStartTime(mtWorkOrder.getPlanStartTime());
                mtWorkOrderHis.setPlanEndTime(mtWorkOrder.getPlanEndTime());
                mtWorkOrderHis.setLocatorId(mtWorkOrder.getLocatorId());
                mtWorkOrderHis.setBomId(mtWorkOrder.getBomId());
                mtWorkOrderHis.setRouterId(mtWorkOrder.getRouterId());
                mtWorkOrderHis.setValidateFlag(mtWorkOrder.getValidateFlag());
                mtWorkOrderHis.setRemark(mtWorkOrder.getRemark());
                mtWorkOrderHis.setOpportunityId(mtWorkOrder.getOpportunityId());
                mtWorkOrderHis.setCustomerId(mtWorkOrder.getCustomerId());
                mtWorkOrderHis.setCompleteControlType(mtWorkOrder.getCompleteControlType());
                mtWorkOrderHis.setCompleteControlQty(mtWorkOrder.getCompleteControlQty());
                mtWorkOrderHis.setEventId(eventId);
                mtWorkOrderHis.setTrxQty(
                        BigDecimal.valueOf(AfterQty).subtract(BigDecimal.valueOf(preQty)).doubleValue());
                mtWorkOrderHis.setSourceIdentificationId(mtWorkOrder.getSourceIdentificationId());
                mtWorkOrderHis.setCid(Long.valueOf(newWoHisCids.remove(0)));
                mtWorkOrderHis.setCreationDate(now);
                mtWorkOrderHis.setCreatedBy(userId);
                mtWorkOrderHis.setLastUpdateDate(now);
                mtWorkOrderHis.setLastUpdatedBy(userId);
                mtWorkOrderHis.setObjectVersionNumber(1L);
                mtWorkOrderHis.setEventId(eventId);
                mtWorkOrder.setLatestHisId(mtWorkOrderHis.getWorkOrderHisId());

                workOrderList.add(mtWorkOrder);
                workOrderHisList.add(mtWorkOrderHis);

                // save extend columns
                attrMap.put(iface, mtWorkOrder.getWorkOrderId());
                ifaceList.add(constructIfaceMessage(tenantId, iface, "S", "成功.", now, userId));
            }
        }
        List<String> sqlList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(workOrderList)) {
            sqlList.addAll(constructSql(workOrderList));
            workOrderList = null;
        }
        if (CollectionUtils.isNotEmpty(workOrderHisList)) {
            sqlList.addAll(constructSql(workOrderHisList));
            workOrderHisList = null;
        }
        /**
         * 保存数据
         */
        if (CollectionUtils.isNotEmpty(sqlList)) {
            List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
            sqlList.clear();
        }
        /**
         * 处理扩展属性
         */
        if (MapUtils.isNotEmpty(attrMap)) {
            sqlList.addAll(saveAttrColumn(tenantId, attrMap, WORK_ORDER_ATTR_TABLE, "attribute"));
        }
        if (CollectionUtils.isNotEmpty(sqlList)) {
            List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
        }
        return ifaceList;
    }

    /**
     * 保存扩展属性
     *
     * @param tenantId tenantId
     * @author benjamin
     * @date 2019-08-19 21:50
     */
    private List<String> saveAttrColumn(Long tenantId, Map<MtWorkOrderIface, String> attrMap, String tableName,
                                        String attributePrefix) {

        // 获取系统支持的所有语言环境
        List<Language> languages = LanguageHelper.languages();
        List<String> result = new ArrayList<>();

        // 扩展表批量查询
        List<String> kidList = attrMap.entrySet().stream().map(t -> t.getValue()).collect(Collectors.toList());
        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setTableName(tableName);
        mtExtendVO1.setKeyIdList(kidList);
        List<MtExtendAttrVO1> extendAttrList = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
        Map<String, List<MtExtendAttrVO1>> originMap =
                extendAttrList.stream().collect(Collectors.groupingBy(t -> t.getKeyId()));
        Map<String, List<MtExtendVO5>> attrTableMap = new HashMap<>(attrMap.size());
        attrMap.entrySet().stream().forEach(t -> {
            Map<String, String> fieldMap = ObjectFieldsHelper.getAttributeFields(t.getKey(), attributePrefix);
            // 扩展属性更新时未传入时置为空
            if (MapUtils.isNotEmpty(originMap)) {
                if (CollectionUtils.isNotEmpty(originMap.get(t.getValue()))) {
                    for (MtExtendAttrVO1 attrVO1 : originMap.get(t.getValue())) {
                        fieldMap.putIfAbsent(attrVO1.getAttrName(), "");
                    }
                }
            }

            List<MtExtendVO5> saveExtendList = new ArrayList<>();
            MtExtendVO5 saveExtend;
            for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
                for (Language language : languages) {
                    saveExtend = new MtExtendVO5();
                    saveExtend.setAttrName(entry.getKey());
                    saveExtend.setAttrValue(entry.getValue());
                    saveExtend.setLang(language.getCode());
                    saveExtendList.add(saveExtend);
                }
                attrTableMap.put(t.getValue(), saveExtendList);
            }
        });
        if (MapUtils.isNotEmpty(attrTableMap)) {
            result.addAll(mtExtendSettingsRepository.attrPropertyUpdateForIface(tenantId, tableName, attrTableMap));
        }
        return result;
    }

    /**
     * 构建工单对象
     *
     * @param iface                  MtWorkOrderIface
     * @param mtWorkOrder            MtWorkOrder
     * @param siteId                 站点Id
     * @param materialId             物料Id
     * @param uomId                  单位Id
     * @param locatorId              货位Id
     * @param prodLineId             生产线Id
     * @param status                 状态
     * @param sourceIdentificationId 来源标识Id
     * @return MtWorkOrderVO
     * @author benjamin
     * @date 2019-08-19 20:52
     */
    private MtWorkOrder constructWorkOrder(MtWorkOrderIface iface, MtWorkOrder mtWorkOrder, String siteId,
                                           String materialId, String uomId, String locatorId, String prodLineId, String status,
                                           Double sourceIdentificationId, Date date) {
        mtWorkOrder.setSiteId(siteId);
        mtWorkOrder.setMaterialId(materialId);
        mtWorkOrder.setUomId(uomId);
        mtWorkOrder.setLocatorId(locatorId);
        mtWorkOrder.setProductionLineId(prodLineId);
        mtWorkOrder.setWorkOrderNum(iface.getWorkOrderNum());
        mtWorkOrder.setWorkOrderType(iface.getWorkOrderType());
        mtWorkOrder.setQty(iface.getQuantity() == null ? Double.valueOf(0.0D) : iface.getQuantity());
        mtWorkOrder.setPlanStartTime(iface.getScheduleStartDate() == null ? date : iface.getScheduleStartDate());
        mtWorkOrder.setPlanEndTime(iface.getScheduleEndDate() == null ? date : iface.getScheduleEndDate());
        mtWorkOrder.setProductionVersion(iface.getProductionVersion());
        mtWorkOrder.setRemark(iface.getRemark());
        mtWorkOrder.setValidateFlag("Y");
        mtWorkOrder.setStatus(StringUtils.isEmpty(status) ? "NEW" : status);
        mtWorkOrder.setSourceIdentificationId(sourceIdentificationId);
        mtWorkOrder.setCompleteControlQty(iface.getCompleteControlQty());
        mtWorkOrder.setCompleteControlType(iface.getCompleteControlType());
        return mtWorkOrder;
    }

    /**
     * 根据工厂编码获取工厂与站点关系
     * <p>
     * 站点类型指定为manufacturing
     *
     * @param tenantId      tenantId
     * @param plantCodeList 工厂编码集合
     * @return Map<String, MtSitePlantReleation>
     * @author benjamin
     * @date 2019-06-27 10:21
     */
    private Map<String, MtSitePlantReleation> getSitePlantRelation(Long tenantId, List<String> plantCodeList) {

        Map<String, MtSitePlantReleation> sitePlantMap = null;
        if (CollectionUtils.isNotEmpty(plantCodeList)) {
            MtSitePlantReleationVO3 mtSitePlantReleationVO3 = new MtSitePlantReleationVO3();
            mtSitePlantReleationVO3.setSiteType("MANUFACTURING");
            mtSitePlantReleationVO3.setPlantCodes(plantCodeList);
            List<MtSitePlantReleation> relationByPlantAndSiteType = mtSitePlantReleationRepository
                    .getRelationByPlantAndSiteType(tenantId, mtSitePlantReleationVO3);

            if (CollectionUtils.isNotEmpty(relationByPlantAndSiteType)) {
                sitePlantMap = relationByPlantAndSiteType.stream()
                        .collect(Collectors.toMap(MtSitePlantReleation::getPlantCode, c -> c));
            }
        }
        return sitePlantMap;

    }

    /**
     * 根据工厂物料获取物料站点关系
     * <p>
     * 站点类型指定为manufacturing
     *
     * @param tenantId     tenantId
     * @param itemCodeList 物料编码集合
     * @return Map<String, MtInterFaceVO1>
     * @author benjamin
     * @date 2019-06-27 10:29
     */
    private Map<ItemPlant, List<MtSitePlantReleationVO1>> getMaterialSite(Long tenantId, List<String> plantCodeList,
                                                                          List<String> itemCodeList) {
        MtSitePlantReleationVO mtSitePlantReleationVO = new MtSitePlantReleationVO();
        mtSitePlantReleationVO.setPlantCodeList(plantCodeList);
        mtSitePlantReleationVO.setItemCodeList(itemCodeList);
        mtSitePlantReleationVO.setSiteType("MANUFACTURING");
        List<MtSitePlantReleationVO1> mtSitePlantReleationVO1s =
                mtSitePlantReleationRepository.itemMaterialSiteIdBatchQuery(tenantId, mtSitePlantReleationVO);
        Map<ItemPlant, List<MtSitePlantReleationVO1>> itemPlantListMap = null;
        if (CollectionUtils.isNotEmpty(mtSitePlantReleationVO1s)) {
            itemPlantListMap = mtSitePlantReleationVO1s.stream()
                    .collect(Collectors.groupingBy(t -> new ItemPlant(t.getPlantCode(), t.getItemCode())));
        }
        return itemPlantListMap;
    }

    /**
     * 构建返回消息
     *
     * @param mtWorkOrderIface MtWorkOrderIface
     * @param message          错误消息
     * @return MtWorkOrderIface
     * @author benjamin
     * @date 2019-06-27 17:02
     */
    private MtWorkOrderIface constructIfaceMessage(Long tenantId, MtWorkOrderIface mtWorkOrderIface, String status,
                                                   String message, Date date, Long userId) {
        mtWorkOrderIface.setTenantId(tenantId);
        mtWorkOrderIface.setStatus(status);
        mtWorkOrderIface.setMessage(message);
        mtWorkOrderIface.setLastUpdateDate(date);
        mtWorkOrderIface.setLastUpdatedBy(userId);

        return mtWorkOrderIface;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public List<MtWorkOrderIface> updateIfaceStatus(Long tenantId, String status) {
        List<MtWorkOrderIface> list = mtWorkOrderIfaceMapper.getUnprocessedList(tenantId);
        if (CollectionUtils.isNotEmpty(list)) {
            List<String> sqlList = new ArrayList<>(list.size());
            List<AuditDomain> auditDomains = new ArrayList<>(list.size());
            list.stream().forEach(ifs -> {
                ifs.setStatus(status);
                ifs.setTenantId(tenantId);
            });
            auditDomains.addAll(list);
            sqlList.addAll(constructSql(auditDomains));
            List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
        }
        return list;
    }

    @Override
    public List<MtWorkOrderIface> myUpdateIfaceStatus(Long tenantId, String status, Long batchId) {
        List<MtWorkOrderIface> list = mtWorkOrderIfaceMapper.getMyUnprocessedList(tenantId, batchId);
        if (CollectionUtils.isNotEmpty(list)) {
            List<String> sqlList = new ArrayList<>(list.size());
            List<AuditDomain> auditDomains = new ArrayList<>(list.size());
            list.stream().forEach(ifs -> {
                ifs.setStatus(status);
                ifs.setTenantId(tenantId);
            });
            auditDomains.addAll(list);
            sqlList.addAll(constructSql(auditDomains));
            List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
        }
        return list;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public void log(Long tenantId, List<AuditDomain> ifaceSqlList) {
        if (CollectionUtils.isNotEmpty(ifaceSqlList)) {
            List<String> sqlList = constructSql(ifaceSqlList);
            List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }

        }
    }

    /**
     * 分割数据集合 限制数量每项不多于SQL_ITEM_COUNT_LIMIT
     *
     * @return List<List>
     * @author benjamin
     * @date 2019-06-25 18:40
     */
    private List<String> constructSql(List<AuditDomain> ifaceSqlList) {
        List<String> sqlList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(ifaceSqlList)) {
            List<List<AuditDomain>> splitSqlList = StringHelper.splitSqlList(ifaceSqlList, SQL_ITEM_COUNT_LIMIT);
            for (List<AuditDomain> domains : splitSqlList) {
                sqlList.addAll(customDbRepository.getReplaceSql(domains));
            }
        }
        return sqlList;
    }

    private List<List<String>> commitSqlList(List<String> sqlList, int splitNum) {

        List<List<String>> returnList = new ArrayList<>();
        if (sqlList.size() <= splitNum) {
            returnList.add(sqlList);
        } else {
            int splitCount = sqlList.size() / splitNum;
            int splitRest = sqlList.size() % splitNum;

            for (int i = 0; i < splitCount; i++) {
                returnList.add(sqlList.subList(i * splitNum, (i + 1) * splitNum));
            }

            if (splitRest > 0) {
                returnList.add(sqlList.subList(splitCount * splitNum, sqlList.size()));
            }
        }
        return returnList;
    }

    private static class ItemPlant implements Serializable {
        private static final long serialVersionUID = -2761453607185360979L;
        private String plantCode;
        private String itemCode;

        ItemPlant(String plantCode, String itemCode) {
            this.plantCode = plantCode;
            this.itemCode = itemCode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ItemPlant itemPlant = (ItemPlant) o;
            return Objects.equals(plantCode, itemPlant.plantCode) && Objects.equals(itemCode, itemPlant.itemCode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(plantCode, itemCode);
        }
    }
}
