package tarzan.iface.infra.repository.impl;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import com.ruike.itf.domain.entity.ItfMaterialSiteAttr;
import com.ruike.itf.infra.mapper.ItfMaterialSiteAttrMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Language;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ruike.itf.domain.entity.ItfMaterialSiteAttr;
import com.ruike.itf.infra.mapper.ItfMaterialSiteAttrMapper;
import com.ruike.itf.infra.util.InterfaceUtils;

import io.choerodon.core.exception.CommonException;
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
import lombok.extern.slf4j.Slf4j;
import tarzan.iface.domain.entity.MtInvItemIface;
import tarzan.iface.domain.entity.MtMaterialBasic;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.repository.MtInvItemIfaceRepository;
import tarzan.iface.domain.vo.MtInvIfaceVO;
import tarzan.iface.infra.mapper.MtInvItemIfaceMapper;
import tarzan.iface.infra.mapper.MtMaterialBasisMapper;
import tarzan.material.domain.entity.*;
import tarzan.material.domain.vo.MtMaterialVO6;
import tarzan.material.domain.vo.MtUomVO;
import tarzan.material.infra.mapper.MtMaterialMapper;
import tarzan.material.infra.mapper.MtMaterialNewMapper;
import tarzan.material.infra.mapper.MtMaterialSiteMapper;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 物料接口表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
@Slf4j
@Component
public class MtInvItemIfaceRepositoryImpl extends BaseRepositoryImpl<MtInvItemIface>
        implements MtInvItemIfaceRepository {


    private static final int SQL_ITEM_COUNT_LIMIT = 5000;
    private static final String MT_MATERIAL_SITE_ATTR_TABLE = "mt_material_site_attr";

    @Autowired
    private MtInvItemIfaceMapper mtInvItemIfaceMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtMaterialMapper mtMaterialMapper;

    @Autowired
    private MtMaterialBasisMapper mtMaterialBasisMapper;

    @Autowired
    private ItfMaterialSiteAttrMapper itfMaterialSiteAttrMapper;

    @Autowired
    private MtMaterialSiteMapper mtMaterialSiteMapper;

    @Autowired
    private MtMaterialNewMapper mtMaterialNewMapper;

    @Override
    public void materialInterfaceImport(Long tenantId) {
        Long start = System.currentTimeMillis();
        // 1.获取mt_inv_item_iface表中的数据,只处理status为N或E的数据、更新临时状态
        List<MtInvItemIface> ifaceList = self().updateIfaceStatus(tenantId, "P");

        // 2.根据配置batchId分批进行数据导入
        Map<Double, List<MtInvItemIface>> invItemIfaceMap = ifaceList.stream()
                        .collect(Collectors.groupingBy(MtInvItemIface::getBatchId, TreeMap::new, Collectors.toList()));
        for (Map.Entry<Double, List<MtInvItemIface>> entry : invItemIfaceMap.entrySet()) {
            try {

                List<AuditDomain> logList = self().saveInterfaceData(tenantId, entry.getValue());
                self().log(logList);
            } catch (Exception e) {
                List<AuditDomain> logList = new ArrayList<>(entry.getValue().size());
                for (MtInvItemIface ifs : entry.getValue()) {
                    ifs.setStatus("E");
                    String message = StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage().replace("'", "\"")
                                    : "数据异常.";
                    ifs.setMessage(message.length() > 1000 ? message.substring(message.length() - 1000) : message);
                    ifs.setTenantId(tenantId);
                    logList.add(ifs);
                }
                self().log(logList);
            }
        }
        System.out.println("接口执行耗时：" + (System.currentTimeMillis() - start) + " 毫秒");
    }

    @Override
    public void myMaterialInterfaceImport(Long tenantId, Double batchId) {
        if (Objects.isNull(tenantId) || Objects.isNull(batchId)) {
            throw new CommonException("租户ID和batchId不允许为空");
        }
        Long start = System.currentTimeMillis();
        try {
            // 1.获取mt_inv_item_iface表中的数据,只处理当前批次的数据
            MtInvItemIface mtInvItemIface = new MtInvItemIface();
            mtInvItemIface.setTenantId(tenantId);
            mtInvItemIface.setBatchId(batchId);
            List<MtInvItemIface> ifaceList = mtInvItemIfaceMapper.select(mtInvItemIface);
            mySaveInterfaceData(tenantId, ifaceList);
            mtInvItemIfaceMapper.updateStatusByBatchId(batchId, "S", "成功");
        } catch (Exception e) {
            e.printStackTrace();
            mtInvItemIfaceMapper.updateStatusByBatchId(batchId, "E", e.getMessage());
        }
        System.out.println("接口执行耗时：" + (System.currentTimeMillis() - start) + " 毫秒");
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<MtInvItemIface> updateIfaceStatus(Long tenantId, String status) {
        // 添加表锁
        List<MtInvItemIface> ifaceList = mtInvItemIfaceMapper.getUnprocessedList(tenantId);
        if (CollectionUtils.isNotEmpty(ifaceList)) {
            List<String> sqlList = new ArrayList<>(ifaceList.size());
            List<AuditDomain> auditDomains = new ArrayList<>(ifaceList.size());
            ifaceList.stream().forEach(ifs -> {
                ifs.setStatus(status);
                ifs.setTenantId(tenantId);

            });
            auditDomains.addAll(ifaceList);
            sqlList.addAll(constructSql(auditDomains));
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
        return ifaceList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<AuditDomain> saveInterfaceData(Long tenantId, List<MtInvItemIface> invItemIfaceList) {
        Long userId = Long.valueOf(-1L);
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        Date now = new Date(System.currentTimeMillis());

        List<String> sqlList = new ArrayList<>();
        List<AuditDomain> ifaceList = new ArrayList<>(invItemIfaceList.size());

        Map<String, List<MtInvItemIface>> ifacePerItemCode =
                        invItemIfaceList.stream().collect(Collectors.groupingBy(MtInvItemIface::getItemCode));

        // 获取对应的物料数据Code
        Long insertMaterialCount = 0L;
        List<String> materialCodeList = invItemIfaceList.stream().map(MtInvItemIface::getItemCode).distinct()
                        .collect(Collectors.toList());
        String whereInValuesSql = StringHelper.getWhereInValuesSql("item.MATERIAL_CODE", materialCodeList, 1000);
        List<MtMaterial> materialList = mtInvItemIfaceMapper.getMaterialList(tenantId, whereInValuesSql);
        Map<String, MtMaterial> materialCodeIdMap = null;
        if (CollectionUtils.isNotEmpty(materialList)) {
            materialCodeIdMap = materialList.stream().collect(Collectors.toMap(MtMaterial::getMaterialCode, c -> c));
            final Map<String, MtMaterial> tempMaterialCodeIdMap = materialCodeIdMap;
            insertMaterialCount = invItemIfaceList.stream()
                            .filter(c -> !tempMaterialCodeIdMap.containsKey(c.getItemCode())).count();
        }

        // 根据物料code获取多语言信息
        String materialTl = StringHelper.getWhereInValuesSql("p.ITEM_CODE", invItemIfaceList.stream()
                        .map(MtInvItemIface::getItemCode).distinct().collect(Collectors.toList()), 1000);
        List<MtInvIfaceVO> itemTlList = mtInvItemIfaceMapper.getItemTlList(tenantId, materialTl);
        Map<String, List<MtInvIfaceVO>> itemTlListMap = null;
        if (CollectionUtils.isNotEmpty(itemTlList)) {
            itemTlListMap = itemTlList.stream().collect(Collectors.groupingBy(MtInvIfaceVO::getItemCode));
        }

        // 获取对应的主单位
        List<String> uomCodeList = invItemIfaceList.stream().map(MtInvItemIface::getPrimaryUom).distinct()
                        .collect(Collectors.toList());
        String uomCodeListSql = StringHelper.getWhereInValuesSql("u.UOM_CODE", uomCodeList, 1000);
        List<MtUomVO> uomList = mtInvItemIfaceMapper.getUomList(tenantId, uomCodeListSql);
        Map<String, MtUomVO> mtUomVOMap = null;
        if (CollectionUtils.isNotEmpty(uomList)) {
            mtUomVOMap = uomList.stream().collect(Collectors.toMap(MtUom::getUomCode, t -> t));
        }

        // 获取单位编码
        List<String> sizeUomCodes = invItemIfaceList.stream().filter(t -> StringUtils.isNotEmpty(t.getSizeUomCode()))
                        .map(MtInvItemIface::getSizeUomCode).distinct().collect(Collectors.toList());
        Map<String, MtUomVO> sizeUomVOMap = null;
        if (CollectionUtils.isNotEmpty(sizeUomCodes)) {
            uomCodeListSql = StringHelper.getWhereInValuesSql("u.UOM_CODE", sizeUomCodes, 1000);
            List<MtUomVO> tempUomList = mtInvItemIfaceMapper.getUomList(tenantId, uomCodeListSql);
            if (CollectionUtils.isNotEmpty(tempUomList)) {
                sizeUomVOMap = tempUomList.stream().collect(Collectors.toMap(MtUom::getUomCode, c -> c));
            }
        }
        List<String> volumeUomCodes =
                        invItemIfaceList.stream().filter(t -> StringUtils.isNotEmpty(t.getVolumeUomCode()))
                                        .map(MtInvItemIface::getVolumeUomCode).distinct().collect(Collectors.toList());
        Map<String, MtUomVO> volumeUomVOMap = null;
        if (CollectionUtils.isNotEmpty(volumeUomCodes)) {
            uomCodeListSql = StringHelper.getWhereInValuesSql("u.UOM_CODE", volumeUomCodes, 1000);
            List<MtUomVO> tempUomList = mtInvItemIfaceMapper.getUomList(tenantId, uomCodeListSql);
            if (CollectionUtils.isNotEmpty(tempUomList)) {
                volumeUomVOMap = tempUomList.stream().collect(Collectors.toMap(MtUom::getUomCode, c -> c));
            }
        }
        List<String> shelfLifeUomCodes = invItemIfaceList.stream()
                        .filter(t -> StringUtils.isNotEmpty(t.getShelfLifeUomCode()))
                        .map(MtInvItemIface::getShelfLifeUomCode).distinct().collect(Collectors.toList());
        Map<String, MtUomVO> shelfLifeUomVOMap = null;
        if (CollectionUtils.isNotEmpty(shelfLifeUomCodes)) {
            uomCodeListSql = StringHelper.getWhereInValuesSql("u.UOM_CODE", shelfLifeUomCodes, 1000);
            List<MtUomVO> tempUomList = mtInvItemIfaceMapper.getUomList(tenantId, uomCodeListSql);
            if (CollectionUtils.isNotEmpty(tempUomList)) {
                shelfLifeUomVOMap = tempUomList.stream().collect(Collectors.toMap(MtUom::getUomCode, c -> c));
            }
        }
        List<String> secondaryUomCodes = invItemIfaceList.stream()
                        .filter(t -> StringUtils.isNotEmpty(t.getSecondaryUomCode()))
                        .map(MtInvItemIface::getSecondaryUomCode).distinct().collect(Collectors.toList());
        Map<String, MtUomVO> secondaryUomVOMap = null;
        if (CollectionUtils.isNotEmpty(secondaryUomCodes)) {
            uomCodeListSql = StringHelper.getWhereInValuesSql("u.UOM_CODE", secondaryUomCodes, 1000);
            List<MtUomVO> tempUomList = mtInvItemIfaceMapper.getUomList(tenantId, uomCodeListSql);
            if (CollectionUtils.isNotEmpty(tempUomList)) {
                secondaryUomVOMap = tempUomList.stream().collect(Collectors.toMap(MtUom::getUomCode, c -> c));
            }
        }
        List<String> weightCodes = invItemIfaceList.stream().filter(t -> StringUtils.isNotEmpty(t.getWeightUomCode()))
                        .map(MtInvItemIface::getWeightUomCode).distinct().collect(Collectors.toList());
        Map<String, MtUomVO> weightUomVOMap = null;
        if (CollectionUtils.isNotEmpty(weightCodes)) {
            uomCodeListSql = StringHelper.getWhereInValuesSql("u.UOM_CODE", secondaryUomCodes, 1000);
            List<MtUomVO> tempUomList = mtInvItemIfaceMapper.getUomList(tenantId, uomCodeListSql);
            if (CollectionUtils.isNotEmpty(tempUomList)) {
                weightUomVOMap = tempUomList.stream().collect(Collectors.toMap(MtUom::getUomCode, c -> c));
            }
        }

        // 根据工厂获取站点
        List<String> plantCodeList = invItemIfaceList.stream().map(MtInvItemIface::getPlantCode).distinct()
                        .collect(Collectors.toList());
        List<MtSitePlantReleation> sitePlantRelationList =
                        mtInvItemIfaceMapper.getMateriaSiteReleationList(tenantId, plantCodeList);
        Map<String, List<MtSitePlantReleation>> sitePerPlantMap = null;
        if (CollectionUtils.isNotEmpty(sitePlantRelationList)) {
            sitePerPlantMap = sitePlantRelationList.stream()
                            .collect(Collectors.groupingBy(MtSitePlantReleation::getPlantCode));
        }

        // 获取物料站点信息

        String materialCodeSql = StringHelper.getWhereInValuesSql("t.MATERIAL_CODE", invItemIfaceList.stream()
                        .map(MtInvItemIface::getItemCode).distinct().collect(Collectors.toList()), 1000);
        List<MtMaterialSite> siteList = mtInvItemIfaceMapper.getMateriaSitelList1(tenantId, materialCodeSql);

        Map<String, List<MtMaterialSite>> materialSitePerId = null;
        List<String> mSiteList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(siteList)) {
            materialSitePerId = siteList.stream().collect(Collectors.groupingBy(MtMaterialSite::getMaterialId));
            mSiteList = siteList.stream().map(MtMaterialSite::getMaterialSiteId).distinct()
                            .collect(Collectors.toList());
        }
        // 获取pfep数据
        Map<String, MtPfepPurchase> mtPfepPurchaseMap = null;
        Map<String, MtPfepSchedule> mtPfepScheduleMap = null;
        Map<String, MtPfepInventory> mtPfepInventoryMap = null;
        Map<String, MtMaterialBasic> mtMaterialBasicMap = null;

        if (CollectionUtils.isNotEmpty(mSiteList)) {
            String materialSiteSql = StringHelper.getWhereInValuesSql("p.MATERIAL_SITE_ID", mSiteList, 1000);
            List<MtPfepPurchase> mtPfepPurchaseList =
                            mtInvItemIfaceMapper.getMtPfepPurchaseList(tenantId, materialSiteSql);
            if (CollectionUtils.isNotEmpty(mtPfepPurchaseList)) {
                mtPfepPurchaseMap = mtPfepPurchaseList.stream()
                                .collect(Collectors.toMap(MtPfepPurchase::getMaterialSiteId, c -> c));
            }
            List<MtPfepSchedule> mtPfepScheduleList =
                            mtInvItemIfaceMapper.getMtPfepScheduleList(tenantId, materialSiteSql);
            if (CollectionUtils.isNotEmpty(mtPfepScheduleList)) {
                mtPfepScheduleMap = mtPfepScheduleList.stream()
                                .collect(Collectors.toMap(MtPfepSchedule::getMaterialSiteId, c -> c));
            }
            List<MtPfepInventory> mtPfepInventoryList =
                            mtInvItemIfaceMapper.getMtPfepInventoryList(tenantId, materialSiteSql);
            if (CollectionUtils.isNotEmpty(mtPfepInventoryList)) {
                mtPfepInventoryMap = mtPfepInventoryList.stream()
                                .collect(Collectors.toMap(MtPfepInventory::getMaterialSiteId, c -> c));
            }
            List<MtMaterialBasic> mtMaterialBasicList =
                            mtInvItemIfaceMapper.getMtMaterialBasisList(tenantId, materialSiteSql);
            if (CollectionUtils.isNotEmpty(mtMaterialBasicList)) {
                mtMaterialBasicMap = mtMaterialBasicList.stream()
                                .collect(Collectors.toMap(MtMaterialBasic::getMaterialSiteId, c -> c));
            }
        }

        // 获取库位信息
        List<String> stockLocatorCodes = invItemIfaceList.stream()
                        .filter(c -> StringUtils.isNotEmpty(c.getStockLocatorCode()))
                        .map(MtInvItemIface::getStockLocatorCode).distinct().collect(Collectors.toList());
        List<MtModLocator> mtModLocators = mtModLocatorRepository.selectModLocatorForCodes(tenantId, stockLocatorCodes);
        Map<String, String> mtModLocatorMap = mtModLocators.stream()
                        .collect(Collectors.toMap(MtModLocator::getLocatorCode, MtModLocator::getLocatorId));

        // 批量获取Id、Cid
        List<String> materialds = this.customDbRepository.getNextKeys("mt_material_s",
                        invItemIfaceList.size() - materialList.size());
        List<String> materialCids = this.customDbRepository.getNextKeys("mt_material_cid_s", invItemIfaceList.size());
        List<String> materialSiteIds =
                        this.customDbRepository.getNextKeys("mt_material_site_s", invItemIfaceList.size() * 3);
        Map<MtInvItemIface, List<String>> attrMap = new HashMap<>(invItemIfaceList.size());

        List<AuditDomain> materialLists = new ArrayList<>(invItemIfaceList.size());
        List<MtMaterialSite> materialSiteUpdatelList = new ArrayList<>(invItemIfaceList.size() * 3);
        List<MtMaterialSite> materialSiteInsertlList = new ArrayList<>(invItemIfaceList.size() * 3);
        List<MtPfepSchedule> pfepScheduleUpdatelList = new ArrayList<>(invItemIfaceList.size() * 3);
        List<MtPfepPurchase> pfepPurchaseUpdatelList = new ArrayList<>(invItemIfaceList.size() * 3);
        List<MtPfepInventory> pfepInventoryUpdatelList = new ArrayList<>(invItemIfaceList.size() * 3);
        List<MtMaterialBasic> materialBasicUpdateList = new ArrayList<>(invItemIfaceList.size() * 3);
        MtMaterial material;
        MtMaterialSite newMaterialSite;
        for (Map.Entry<String, List<MtInvItemIface>> entry : ifacePerItemCode.entrySet()) {
            MtInvItemIface headInvItemIface = entry.getValue().get(0);
            try {
                material = new MtMaterial();
                // 校验当前ItemCode对应PlantCode是否包含MANUFACTURING类型的站点
                /*boolean sitePerPlantMapCheck =
                                sitePerPlantMap == null || sitePerPlantMap.get(headInvItemIface.getPlantCode()) == null;
                boolean siteTypeCheck = sitePerPlantMapCheck ? sitePerPlantMapCheck
                                : sitePerPlantMap.get(headInvItemIface.getPlantCode()).stream()
                                                .noneMatch(t -> "MANUFACTURING".equals(t.getSiteType()));
                if (siteTypeCheck) {
                    for (MtInvItemIface iface : entry.getValue()) {
                        ifaceList.add(constructIfaceMessage(tenantId, iface, "E",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INTERFACE_0012", "INTERFACE", iface.getIfaceId(),
                                                        "【API：materialInterfaceImport】"),
                                        now, userId));
                    }
                    continue;
                }*/

                // 校验物料单位
                if (MapUtils.isEmpty(mtUomVOMap) || mtUomVOMap.get(headInvItemIface.getPrimaryUom()) == null) {
                    for (MtInvItemIface iface : entry.getValue()) {
                        ifaceList.add(constructIfaceMessage(tenantId, iface, "E", "该物料主单位找不到单位ID，无法导入数据！", now,
                                        userId));
                    }
                    continue;
                }
                if (StringUtils.isNotEmpty(headInvItemIface.getSizeUomCode()) && (MapUtils.isEmpty(sizeUomVOMap)
                                || sizeUomVOMap.get(headInvItemIface.getSizeUomCode()) == null)) {
                    for (MtInvItemIface iface : entry.getValue()) {
                        ifaceList.add(constructIfaceMessage(tenantId, iface, "E",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INTERFACE_0042", "INTERFACE", iface.getIfaceId(),
                                                        "【API：materialInterfaceImport】"),
                                        now, userId));
                    }
                    continue;
                }
                if (StringUtils.isNotEmpty(headInvItemIface.getVolumeUomCode()) && (MapUtils.isEmpty(volumeUomVOMap)
                                || volumeUomVOMap.get(headInvItemIface.getVolumeUomCode()) == null)) {
                    for (MtInvItemIface iface : entry.getValue()) {
                        ifaceList.add(constructIfaceMessage(tenantId, iface, "E",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INTERFACE_0043", "INTERFACE", iface.getIfaceId(),
                                                        "【API：materialInterfaceImport】"),
                                        now, userId));
                    }
                    continue;
                }
                if (StringUtils.isNotEmpty(headInvItemIface.getShelfLifeUomCode()) && (MapUtils
                                .isEmpty(shelfLifeUomVOMap)
                                || shelfLifeUomVOMap.get(headInvItemIface.getShelfLifeUomCode()) == null)) {
                    for (MtInvItemIface iface : entry.getValue()) {
                        ifaceList.add(constructIfaceMessage(tenantId, iface, "E",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INTERFACE_0044", "INTERFACE", iface.getIfaceId(),
                                                        "【API：materialInterfaceImport】"),
                                        now, userId));
                    }
                    continue;
                }
                if (StringUtils.isNotEmpty(headInvItemIface.getSecondaryUomCode()) && (MapUtils
                                .isEmpty(secondaryUomVOMap)
                                || secondaryUomVOMap.get(headInvItemIface.getSecondaryUomCode()) == null)) {
                    for (MtInvItemIface iface : entry.getValue()) {
                        ifaceList.add(constructIfaceMessage(tenantId, iface, "E",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INTERFACE_0045", "INTERFACE", iface.getIfaceId(),
                                                        "【API：materialInterfaceImport】"),
                                        now, userId));
                    }
                    continue;
                }
                if (StringUtils.isNotEmpty(headInvItemIface.getWeightUomCode()) && (MapUtils.isEmpty(weightUomVOMap)
                                || weightUomVOMap.get(headInvItemIface.getWeightUomCode()) == null)) {
                    for (MtInvItemIface iface : entry.getValue()) {
                        ifaceList.add(constructIfaceMessage(tenantId, iface, "E",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INTERFACE_0046", "INTERFACE", iface.getIfaceId(),
                                                        "【API：materialInterfaceImport】"),
                                        now, userId));
                    }
                    continue;
                }
                if (StringUtils.isNotEmpty(headInvItemIface.getStockLocatorCode()) && (MapUtils.isEmpty(mtModLocatorMap)
                                || mtModLocatorMap.get(headInvItemIface.getStockLocatorCode()) == null)) {
                    for (MtInvItemIface iface : entry.getValue()) {
                        ifaceList.add(constructIfaceMessage(tenantId, iface, "E",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INTERFACE_0047", "INTERFACE", iface.getIfaceId(),
                                                        "【API：materialInterfaceImport】"),
                                        now, userId));
                    }
                    continue;
                }

                // 多语言信息
                Map<String, Map<String, String>> customerTlMap = new HashMap<>();
                if (MapUtils.isNotEmpty(itemTlListMap)
                                && CollectionUtils.isNotEmpty(itemTlListMap.get(headInvItemIface.getItemCode()))) {
                    List<MtInvIfaceVO> mtInvIfaceVOS = itemTlListMap.get(headInvItemIface.getItemCode());
                    Map<String, String> tlMap = new HashMap<>(2);
                    for (MtInvIfaceVO mtInvIfaceVO : mtInvIfaceVOS) {
                        tlMap.put(mtInvIfaceVO.getLang(), mtInvIfaceVO.getDescription());
                    }
                    customerTlMap.put("materialName", tlMap);
                }
                // construct material
                material.setMaterialCode(headInvItemIface.getItemCode());
                material.setMaterialDesignCode(headInvItemIface.getMaterialDesignCode());
                material.setMaterialIdentifyCode(headInvItemIface.getMaterialIdentifyCode());
                material.setLength(headInvItemIface.getLength());
                material.setWidth(headInvItemIface.getWidth());
                material.setHeight(headInvItemIface.getHeight());
                material.setModel(headInvItemIface.getModel());
                material.setVolume(headInvItemIface.getVolume());
                material.setShelfLife(headInvItemIface.getShelfLife());
                material.setConversionRate(headInvItemIface.getConversionRate());
                if (StringUtils.isNotEmpty(headInvItemIface.getVolumeUomCode())) {
                    material.setVolumeUomId(volumeUomVOMap.get(headInvItemIface.getVolumeUomCode()).getUomId());
                }
                if (StringUtils.isNotEmpty(headInvItemIface.getSizeUomCode())) {
                    material.setSizeUomId(sizeUomVOMap.get(headInvItemIface.getSizeUomCode()).getUomId());
                }
                if (StringUtils.isNotEmpty(headInvItemIface.getShelfLifeUomCode())) {
                    material.setShelfLifeUomId(
                                    shelfLifeUomVOMap.get(headInvItemIface.getShelfLifeUomCode()).getUomId());
                }
                if (StringUtils.isNotEmpty(headInvItemIface.getSecondaryUomCode())) {
                    material.setSecondaryUomId(
                                    secondaryUomVOMap.get(headInvItemIface.getSecondaryUomCode()).getUomId());
                }
                if (StringUtils.isNotEmpty(headInvItemIface.getSecondaryUomCode())) {
                    material.setSecondaryUomId(
                                    secondaryUomVOMap.get(headInvItemIface.getSecondaryUomCode()).getUomId());
                }
                if (StringUtils.isNotEmpty(headInvItemIface.getWeightUomCode())) {
                    material.setWeightUomId(weightUomVOMap.get(headInvItemIface.getWeightUomCode()).getUomId());
                }
                material.setPrimaryUomId(mtUomVOMap.get(headInvItemIface.getPrimaryUom()).getUomId());
                material.setMaterialName(headInvItemIface.getDescriptions());
                material.setEnableFlag(headInvItemIface.getEnableFlag());
                if (MapUtils.isNotEmpty(customerTlMap)) {
                    material.set_tls(customerTlMap);
                }
                material.setLastUpdateDate(now);
                material.setLastUpdatedBy(userId);
                material.setCid(Long.valueOf(materialCids.get(ifaceList.size())));
                material.setTenantId(tenantId);
                if (materialCodeIdMap == null || materialCodeIdMap.get(headInvItemIface.getItemCode()) == null) {
                    material.setMaterialId(materialds.remove(0));
                    material.setCreatedBy(userId);
                    material.setCreationDate(now);
                    material.setObjectVersionNumber(1L);
                    materialLists.add(material);
                } else {
                    MtMaterial orgin = materialCodeIdMap.get(headInvItemIface.getItemCode());
                    material.setMaterialId(orgin.getMaterialId());
                    material.setCreationDate(orgin.getCreationDate());
                    material.setCreatedBy(orgin.getCreatedBy());
                    material.setObjectVersionNumber(orgin.getObjectVersionNumber() + 1L);
                    materialLists.add(material);
                }

                // 扩展属性列表
                for (MtInvItemIface iface : entry.getValue()) {
                    // 校验当前ItemCode对应PlantCode是否包含MANUFACTURING类型的站点
                    boolean sitePerPlantMapCheck =
                                    sitePerPlantMap == null || sitePerPlantMap.get(iface.getPlantCode()) == null;
                    boolean siteTypeCheck = sitePerPlantMapCheck ? sitePerPlantMapCheck
                                    : sitePerPlantMap.get(iface.getPlantCode()).stream()
                                                    .noneMatch(t -> "MANUFACTURING".equals(t.getSiteType()));
                    if (siteTypeCheck) {
                        ifaceList.add(constructIfaceMessage(tenantId, iface, "E",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INTERFACE_0012", iface.getIfaceId().toString(),
                                                        "【API：materialInterfaceImport】"),
                                        now, userId));
                        continue;
                    }

                    List<String> temp = new ArrayList<>(3);
                    if (materialSitePerId == null
                                    || CollectionUtils.isEmpty(materialSitePerId.get(material.getMaterialId()))) {

                        for (MtSitePlantReleation rel : sitePerPlantMap.get(iface.getPlantCode())) {
                            // add
                            newMaterialSite = constructMaterialSite(tenantId, null, rel, material.getMaterialId(),
                                            iface.getEnableFlag(), now, userId, iface.getItemId());
                            newMaterialSite.setMaterialSiteId(materialSiteIds.remove(0));
                            materialSiteInsertlList.add(newMaterialSite);

                            pfepScheduleUpdatelList
                                            .add(constructPfepSchedual(tenantId, newMaterialSite.getMaterialSiteId(),
                                                            null, iface, rel.getSiteType(), now, userId));
                            pfepPurchaseUpdatelList
                                            .add(constructPfepPurchase(tenantId, newMaterialSite.getMaterialSiteId(),
                                                            null, iface, rel.getSiteType(), now, userId));
                            if (StringUtils.isNotEmpty(iface.getStockLocatorCode())) {
                                pfepInventoryUpdatelList.add(constructPfepInventory(tenantId,
                                                newMaterialSite.getMaterialSiteId(), null,
                                                mtModLocatorMap.get(iface.getStockLocatorCode()), now, userId));
                            }
                            materialBasicUpdateList.add(constructBasis(tenantId, newMaterialSite.getMaterialSiteId(),
                                            null, iface, now, userId));
                            temp.add(newMaterialSite.getMaterialSiteId());
                        }
                    } else {
                        for (MtSitePlantReleation rel : sitePerPlantMap.get(iface.getPlantCode())) {
                            MtMaterialSite originMaterialSite = materialSitePerId.get(material.getMaterialId()).stream()
                                            .filter(t -> rel.getSiteId().equals(t.getSiteId())).findAny().orElse(null);
                            if (originMaterialSite == null) {
                                // add
                                newMaterialSite = constructMaterialSite(tenantId, null, rel, material.getMaterialId(),
                                                iface.getEnableFlag(), now, userId, iface.getItemId());
                                newMaterialSite.setMaterialSiteId(materialSiteIds.remove(0));
                                materialSiteInsertlList.add(newMaterialSite);
                            } else {
                                // update
                                newMaterialSite = constructMaterialSite(tenantId, originMaterialSite, rel,
                                                material.getMaterialId(), iface.getEnableFlag(), now, userId,
                                                iface.getItemId());
                                materialSiteUpdatelList.add(newMaterialSite);

                            }
                            newMaterialSite.setTenantId(tenantId);
                            pfepScheduleUpdatelList.add(constructPfepSchedual(tenantId,
                                            newMaterialSite.getMaterialSiteId(),
                                            MapUtils.isEmpty(mtPfepScheduleMap) ? null
                                                            : mtPfepScheduleMap
                                                                            .get(newMaterialSite.getMaterialSiteId()),
                                            iface, rel.getSiteType(), now, userId));
                            pfepPurchaseUpdatelList.add(constructPfepPurchase(tenantId,
                                            newMaterialSite.getMaterialSiteId(),
                                            MapUtils.isEmpty(mtPfepPurchaseMap) ? null
                                                            : mtPfepPurchaseMap
                                                                            .get(newMaterialSite.getMaterialSiteId()),
                                            iface, rel.getSiteType(), now, userId));
                            if (StringUtils.isNotEmpty(iface.getStockLocatorCode())) {
                                if (StringUtils.isNotEmpty(iface.getStockLocatorCode())) {
                                    pfepInventoryUpdatelList.add(constructPfepInventory(tenantId,
                                                    newMaterialSite.getMaterialSiteId(),
                                                    MapUtils.isEmpty(mtPfepInventoryMap) ? null
                                                                    : mtPfepInventoryMap.get(newMaterialSite
                                                                                    .getMaterialSiteId()),
                                                    mtModLocatorMap.get(iface.getStockLocatorCode()), now, userId));
                                }
                            }
                            materialBasicUpdateList.add(constructBasis(tenantId, newMaterialSite.getMaterialSiteId(),
                                            MapUtils.isEmpty(mtMaterialBasicMap) ? null
                                                            : mtMaterialBasicMap
                                                                            .get(newMaterialSite.getMaterialSiteId()),
                                            iface, now, userId));
                            temp.add(newMaterialSite.getMaterialSiteId());
                        }
                    }
                    attrMap.put(iface, temp);
                    ifaceList.add(constructIfaceMessage(tenantId, iface, "S", "成功.", now, userId));
                }
            } catch (MtException e) {
                throw new MtException("MT_INTERFACE_0011",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0011",
                                                "INTERFACE", headInvItemIface.getIfaceId(), e.getMessage(),
                                                "【API:materialInterfaceImport】"));
            }
        }
        pfepScheduleUpdatelList = pfepScheduleUpdatelList.stream().filter(c -> c != null).collect(Collectors.toList());
        pfepPurchaseUpdatelList = pfepPurchaseUpdatelList.stream().filter(c -> c != null).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(materialLists)) {
            sqlList.addAll(constructSql(materialLists));
            materialLists = null;
        }
        if (CollectionUtils.isNotEmpty(materialSiteUpdatelList)) {
            List<String> materialSiteCids = this.customDbRepository.getNextKeys("mt_material_site_cid_s",
                            materialSiteUpdatelList.size());
            List<AuditDomain> tempList = new ArrayList<>(materialSiteUpdatelList.size());
            materialSiteUpdatelList.stream().forEach(c -> {
                c.setCid(Long.valueOf(materialSiteCids.remove(0)));
            });
            tempList.addAll(materialSiteUpdatelList);
            sqlList.addAll(constructSql(tempList));
            materialSiteUpdatelList = null;
            tempList = null;
        }
        if (CollectionUtils.isNotEmpty(materialSiteInsertlList)) {
            List<String> materialSiteCids = this.customDbRepository.getNextKeys("mt_material_site_cid_s",
                            materialSiteInsertlList.size());
            List<AuditDomain> tempList = new ArrayList<>(materialSiteInsertlList.size());
            materialSiteInsertlList.stream().forEach(c -> {
                c.setCid(Long.valueOf(materialSiteCids.remove(0)));
            });
            tempList.addAll(materialSiteInsertlList);
            sqlList.addAll(constructSql(tempList));
            materialSiteInsertlList = null;
            tempList = null;
        }

        /**
         * 物料/物料站点关系保存
         */
        if (CollectionUtils.isNotEmpty(sqlList)) {
            List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
            sqlList.clear();
        }
        if (CollectionUtils.isNotEmpty(pfepScheduleUpdatelList)) {
            List<String> cids = this.customDbRepository.getNextKeys("mt_pfep_schedule_cid_s",
                            pfepScheduleUpdatelList.size());
            List<MtPfepSchedule> insertList = pfepScheduleUpdatelList.stream()
                            .filter(t -> StringUtils.isEmpty(t.getPfepScheduleId())).collect(Collectors.toList());
            List<String> ids = this.customDbRepository.getNextKeys("mt_pfep_schedule_s", insertList.size());
            List<AuditDomain> tempList = new ArrayList<>(pfepScheduleUpdatelList.size());
            pfepScheduleUpdatelList.stream().forEach(c -> {
                if (StringUtils.isEmpty(c.getPfepScheduleId())) {
                    c.setPfepScheduleId(ids.remove(0));
                }
                c.setCid(Long.valueOf(cids.remove(0)));
            });
            tempList.addAll(pfepScheduleUpdatelList);
            sqlList.addAll(constructSql(tempList));
            pfepScheduleUpdatelList = null;
            tempList = null;
        }
        if (CollectionUtils.isNotEmpty(pfepPurchaseUpdatelList)) {
            List<String> cids = this.customDbRepository.getNextKeys("mt_pfep_purchase_cid_s",
                            pfepPurchaseUpdatelList.size());
            List<MtPfepPurchase> insertList = pfepPurchaseUpdatelList.stream()
                            .filter(t -> StringUtils.isEmpty(t.getPfepPurchaseId())).collect(Collectors.toList());
            List<String> ids = this.customDbRepository.getNextKeys("mt_pfep_purchase_s", insertList.size());
            List<AuditDomain> tempList = new ArrayList<>(pfepPurchaseUpdatelList.size());
            pfepPurchaseUpdatelList.stream().forEach(c -> {
                if (StringUtils.isEmpty(c.getPfepPurchaseId())) {
                    c.setPfepPurchaseId(ids.remove(0));
                }
                c.setCid(Long.valueOf(cids.remove(0)));
            });
            tempList.addAll(pfepPurchaseUpdatelList);
            sqlList.addAll(constructSql(tempList));
            pfepPurchaseUpdatelList = null;
            tempList = null;
        }

        if (CollectionUtils.isNotEmpty(pfepInventoryUpdatelList)) {
            List<String> cids = this.customDbRepository.getNextKeys("mt_pfep_inventory_cid_s",
                            pfepInventoryUpdatelList.size());
            List<MtPfepInventory> insertList = pfepInventoryUpdatelList.stream()
                            .filter(t -> StringUtils.isEmpty(t.getPfepInventoryId())).collect(Collectors.toList());
            List<String> ids = this.customDbRepository.getNextKeys("mt_pfep_inventory_s", insertList.size());
            List<AuditDomain> tempList = new ArrayList<>(pfepInventoryUpdatelList.size());
            pfepInventoryUpdatelList.stream().forEach(c -> {
                if (StringUtils.isEmpty(c.getPfepInventoryId())) {
                    c.setPfepInventoryId(ids.remove(0));
                }
                c.setCid(Long.valueOf(cids.remove(0)));
            });
            tempList.addAll(pfepInventoryUpdatelList);
            sqlList.addAll(constructSql(tempList));
            pfepInventoryUpdatelList = null;
            tempList = null;
        }
        if (CollectionUtils.isNotEmpty(materialBasicUpdateList)) {
            List<String> cids = this.customDbRepository.getNextKeys("mt_material_basic_cid_s",
                            materialBasicUpdateList.size());
            List<AuditDomain> tempList = new ArrayList<>(materialBasicUpdateList.size());
            materialBasicUpdateList.stream().forEach(c -> {
                c.setCid(Long.valueOf(cids.remove(0)));
            });
            tempList.addAll(materialBasicUpdateList);
            sqlList.addAll(constructSql(tempList));
            materialBasicUpdateList = null;
            tempList = null;
        }

        /**
         * PFEP数据保存
         */
        if (CollectionUtils.isNotEmpty(sqlList)) {
            List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
            sqlList.clear();
        }
        /**
         * 处理拓展属性
         */
        if (MapUtils.isNotEmpty(attrMap)) {
            sqlList.addAll(saveAttrColumnAttr(tenantId, attrMap));
            if (CollectionUtils.isNotEmpty(sqlList)) {
                List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
                for (List<String> commitSql : commitSqlList) {
                    this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
                }
            }
        }
        return ifaceList;
    }


    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void mySaveInterfaceData(Long tenantId, List<MtInvItemIface> invItemIfaceList) {
        if (CollectionUtils.isEmpty(invItemIfaceList)) {
            log.error("物料接口，没有数据!");
            return;
        }
        // 查询物料属性所关联的ID
        Map<String, Map<String, Object>> materialAttr = selectMaterialAttribute(tenantId, invItemIfaceList);
        // 建立物料数据
        List<MtMaterial> mtMaterials = createMtMaterialRel(tenantId, materialAttr, invItemIfaceList);
        Map<String, MtMaterial> material =
                        mtMaterials.stream().collect(Collectors.toMap(a -> a.getMaterialCode(), dto -> dto));
        // 建立物料站点数据
        List<MtMaterialSite> mtMaterialSites =
                        createMtMaterialSiteRel(tenantId, materialAttr, mtMaterials, invItemIfaceList);
        Map<String, MtMaterialSite> materialSiteMap = mtMaterialSites.stream()
                        .collect(Collectors.toMap(a -> a.getMaterialId() + "-" + a.getSiteId(), dto -> dto));
        // 建立物料扩展数据
        List<ItfMaterialSiteAttr> mtMaterialSiteAttr =
                        createMtMaterialSiteAttr(tenantId, invItemIfaceList, materialSiteMap, materialAttr, material);
        // 建立物料生产数据
        List<MtMaterialBasic> mtMaterialBasic =
                        createMtMaterialBasic(tenantId, invItemIfaceList, materialSiteMap, materialAttr, material);
        // 物料表
        List<MtMaterial> insertMaterial = mtMaterials.stream().filter(a -> Objects.isNull(a.getObjectVersionNumber()))
                        .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(insertMaterial)) {
            insertMaterial.forEach(a -> {
                a.setObjectVersionNumber(1l);
            });
            List<List<MtMaterial>> materialList = InterfaceUtils.splitSqlList(insertMaterial, SQL_ITEM_COUNT_LIMIT);
            for (List<MtMaterial> materials : materialList) {
                List<MtMaterialVO6>tempList=new ArrayList<>();
                for (MtMaterial temp:
                        materials) {
                    MtMaterialVO6 mtMaterialVO6 = new MtMaterialVO6();
                    BeanUtils.copyProperties(temp,mtMaterialVO6);
                    tempList.add(mtMaterialVO6);
                }
                mtMaterialNewMapper.batchInsert(tempList);
//                mtMaterialMapper.insert(materials.get(0));

                mtMaterialNewMapper.batchInsertTl(tempList);

            }
        }
        List<MtMaterial> updateMaterial = mtMaterials.stream().filter(a -> !Objects.isNull(a.getObjectVersionNumber()))
                        .collect(Collectors.toList());
        for (MtMaterial mtMaterial : updateMaterial) {
            if (!Objects.isNull(mtMaterial.getObjectVersionNumber())) {
                mtMaterialMapper.updateByPrimaryKey(mtMaterial);
            }
        }
        // 物料站点表
        List<MtMaterialSite> insertSites = mtMaterialSites.stream()
                        .filter(a -> Objects.isNull(a.getObjectVersionNumber())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(insertSites)) {
            insertSites.forEach(a -> {
                a.setObjectVersionNumber(1l);
            });
            List<List<MtMaterialSite>> siteList = InterfaceUtils.splitSqlList(insertSites, SQL_ITEM_COUNT_LIMIT);
            for (List<MtMaterialSite> sites : siteList) {
                mtMaterialSiteMapper.batchInsert(sites);
            }
        }
        List<MtMaterialSite> updateSite = mtMaterialSites.stream()
                        .filter(a -> !Objects.isNull(a.getObjectVersionNumber())).collect(Collectors.toList());
        for (MtMaterialSite mtMaterialSite : updateSite) {
            if (!Objects.isNull(mtMaterialSite.getObjectVersionNumber())) {
                mtMaterialSiteMapper.updateByPrimaryKey(mtMaterialSite);
            }
        }

        // 物料站点扩展表
        List<String> materialSiteIds =
                        mtMaterialSites.stream().map(MtMaterialSite::getMaterialSiteId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(materialSiteIds)) {
            String materialSiteIdStrs = "('" + StringUtils.join(materialSiteIds, "','") + "')";
            String sql = "delete from mt_material_site_attr where material_site_id in " + materialSiteIdStrs;
            mtInvItemIfaceMapper.deleteMaterialSiteAttrData(sql);

            String basicSql = "delete from mt_material_basic where material_site_id in " + materialSiteIdStrs;
            mtInvItemIfaceMapper.deleteMaterialSiteAttrData(basicSql);
        }
        if (CollectionUtils.isNotEmpty(mtMaterialSiteAttr)) {
            List<List<ItfMaterialSiteAttr>> splitSqlList =
                            InterfaceUtils.splitSqlList(mtMaterialSiteAttr, SQL_ITEM_COUNT_LIMIT);
            for (List<ItfMaterialSiteAttr> siteAttr : splitSqlList) {
                itfMaterialSiteAttrMapper.batchInsert(siteAttr);
            }

        }
        // 物料属性扩展表
        if (CollectionUtils.isNotEmpty(mtMaterialBasic)) {
            List<List<MtMaterialBasic>> basicList = InterfaceUtils.splitSqlList(mtMaterialBasic, SQL_ITEM_COUNT_LIMIT);
            for (List<MtMaterialBasic> basic : basicList) {
                mtMaterialBasisMapper.batchInsert(basic);
            }
        }
    }

    /**
     * 创建物料业务属性表
     *
     * @param tenantId
     * @param invItemIfaceList
     * @param materialSiteMap
     * @param materialAttr
     * @param materialMap
     * @return
     */
    public List<MtMaterialBasic> createMtMaterialBasic(Long tenantId, List<MtInvItemIface> invItemIfaceList,
                    Map<String, MtMaterialSite> materialSiteMap, Map<String, Map<String, Object>> materialAttr,
                    Map<String, MtMaterial> materialMap) {
        Map<String, Object> site = materialAttr.get("site");
        List<MtMaterialBasic> basics = new ArrayList<>();
        Date date = new Date();
        for (MtInvItemIface itemIface : invItemIfaceList) {
            MtMaterial mtMaterialId = materialMap.get(itemIface.getItemCode());
            MtSitePlantReleation siteId = (MtSitePlantReleation) site.get(itemIface.getPlantCode());
            siteId = Optional.ofNullable(siteId).orElse(new MtSitePlantReleation());
            String mapKey = mtMaterialId.getMaterialId() + "-" + siteId.getSiteId();
            MtMaterialSite mtMaterialSite = materialSiteMap.get(mapKey);
            if (!Objects.isNull(mtMaterialSite)) {
                MtMaterialBasic mtMaterialBasic = new MtMaterialBasic();
                BeanUtils.copyProperties(itemIface, mtMaterialBasic);
                mtMaterialBasic.setMaterialSiteId(mtMaterialSite.getMaterialSiteId());
                mtMaterialBasic.setMaterialId(mtMaterialId.getMaterialId());
                mtMaterialBasic.setLongDescription(itemIface.getDescriptionMir());
                mtMaterialBasic.setMakeBuyCode(itemIface.getPlanningMakeBuyCode());
                // 架构字段
                mtMaterialBasic.setTenantId(tenantId);
                mtMaterialBasic.setCreatedBy(-1l);
                mtMaterialBasic.setLastUpdatedBy(-1l);
                mtMaterialBasic.setCreationDate(date);
                mtMaterialBasic.setLastUpdateDate(date);
                mtMaterialBasic.setObjectVersionNumber(1l);
                mtMaterialBasic.setCid(-1l);
                basics.add(mtMaterialBasic);
            }
        }
        return basics;
    }

    /**
     * 赋值物料站点扩展表
     *
     * @param tenantId
     * @param invItemIfaceList
     * @param materialSiteMap
     * @param materialAttr
     * @param materialMap
     * @return
     */
    public List<ItfMaterialSiteAttr> createMtMaterialSiteAttr(Long tenantId, List<MtInvItemIface> invItemIfaceList,
                    Map<String, MtMaterialSite> materialSiteMap, Map<String, Map<String, Object>> materialAttr,
                    Map<String, MtMaterial> materialMap) {
        Map<String, Object> site = materialAttr.get("site");
        List<ItfMaterialSiteAttr> attrs = new ArrayList<>(invItemIfaceList.size() * 50);
        for (MtInvItemIface itemIface : invItemIfaceList) {
            MtMaterial mtMaterialId = materialMap.get(itemIface.getItemCode());
            MtSitePlantReleation siteId = (MtSitePlantReleation) site.get(itemIface.getPlantCode());
            siteId = Optional.ofNullable(siteId).orElse(new MtSitePlantReleation());
            String mapKey = mtMaterialId.getMaterialId() + "-" + siteId.getSiteId();
            MtMaterialSite mtMaterialSite = materialSiteMap.get(mapKey);
            List<ItfMaterialSiteAttr> mtMaterialSiteAttr =
                            getMtMaterialSiteAttr(tenantId, mtMaterialSite.getMaterialSiteId(), itemIface);
            attrs.addAll(mtMaterialSiteAttr);
        }
        return attrs;
    }

    /**
     * 创建MtMaterialSiteAttr
     *
     * @param tenantId
     * @param materialSiteId
     * @param itemIface
     * @return
     */
    public List<ItfMaterialSiteAttr> getMtMaterialSiteAttr(Long tenantId, String materialSiteId,
                    MtInvItemIface itemIface) {
        List<ItfMaterialSiteAttr> attrs = new ArrayList<>(50);
        Date date = new Date();
        for (Field field : itemIface.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            if (fieldName.contains("attribute")) {
                try {
                    ItfMaterialSiteAttr itfMaterialSiteAttr = new ItfMaterialSiteAttr();
                    String attribute = Objects.isNull(field.get(itemIface)) ? "" : field.get(itemIface).toString();
                    itfMaterialSiteAttr.setAttrName(fieldName);
                    log.info("<====fieldName:{}, attribute:{}-----------", fieldName, attribute);
                    //2021-07-13 15:10 edit by chaonan.hu for peng.zhao 如果是attribute18,需要将小写转大写
                    if("attribute18".equals(fieldName) && StringUtils.isNotBlank(attribute)){
                        itfMaterialSiteAttr.setAttrValue(attribute.toUpperCase());
                        log.info("<====attribute:{}-----------", itfMaterialSiteAttr.getAttrValue());
                    }else{
                        itfMaterialSiteAttr.setAttrValue(attribute);
                    }
                    itfMaterialSiteAttr.setMaterialSiteId(materialSiteId);
                    itfMaterialSiteAttr.setAttrId(customDbRepository.getNextKey("mt_material_site_attr_s"));
                    // 架构字段
                    itfMaterialSiteAttr.setLang("");
                    itfMaterialSiteAttr.setTenantId(tenantId);
                    itfMaterialSiteAttr.setCreatedBy(-1l);
                    itfMaterialSiteAttr.setLastUpdatedBy(-1l);
                    itfMaterialSiteAttr.setCreationDate(date);
                    itfMaterialSiteAttr.setLastUpdateDate(date);
                    itfMaterialSiteAttr.setObjectVersionNumber(1l);
                    itfMaterialSiteAttr.setCid(-1l);
                    attrs.add(itfMaterialSiteAttr);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return attrs;
    }

    /**
     * 赋值物料站点表
     *
     * @param tenantId
     * @param materialAttr
     * @param mtMaterials
     * @param invItemIfaceList
     * @return
     */
    private List<MtMaterialSite> createMtMaterialSiteRel(Long tenantId, Map<String, Map<String, Object>> materialAttr,
                    List<MtMaterial> mtMaterials, List<MtInvItemIface> invItemIfaceList) {
        List<MtMaterialSite> mtMaterialSites = new ArrayList<>();
        Map<String, Object> site = materialAttr.get("site");
        Map<String, MtMaterial> material =
                        mtMaterials.stream().collect(Collectors.toMap(a -> a.getMaterialCode(), dto -> dto));
        List<String> materialSiteIds = customDbRepository.getNextKeys("mt_material_site_s", invItemIfaceList.size());
        List<String> materialIds = mtMaterials.stream().map(MtMaterial::getMaterialId).collect(Collectors.toList());
        Date date = new Date();
        if (CollectionUtils.isNotEmpty(materialIds)) {
            List<MtMaterialSite> materialSites = mtInvItemIfaceMapper.selectMaterialSite(tenantId,
                            "('" + StringUtils.join(materialIds, "','") + "')");
            Map<String, MtMaterialSite> materialSiteMap = materialSites.stream()
                            .collect(Collectors.toMap(a -> a.getMaterialId() + "-" + a.getSiteId(), dto -> dto));
            for (MtInvItemIface itemIface : invItemIfaceList) {
                MtMaterial mtMaterialId = material.get(itemIface.getItemCode());
                MtSitePlantReleation siteId = (MtSitePlantReleation) site.get(itemIface.getPlantCode());
                siteId = Optional.ofNullable(siteId).orElse(new MtSitePlantReleation());
                String mapKey = mtMaterialId.getMaterialId() + "-" + siteId.getSiteId();
                MtMaterialSite getMtMaterialSite = materialSiteMap.get(mapKey);
                MtMaterialSite mtMaterialSite = getMtMaterialSite(tenantId, mtMaterialId.getMaterialId(),
                                siteId.getSiteId(), itemIface.getEnableFlag(), date);
                if (Objects.isNull(getMtMaterialSite)) {
                    mtMaterialSite.setMaterialSiteId(materialSiteIds.remove(0));
                } else {
                    mtMaterialSite.setMaterialSiteId(getMtMaterialSite.getMaterialSiteId());
                    mtMaterialSite.setObjectVersionNumber(getMtMaterialSite.getObjectVersionNumber());
                }
                mtMaterialSites.add(mtMaterialSite);
            }
        } else {
            for (MtInvItemIface itemIface : invItemIfaceList) {
                MtMaterial mtMaterialId = material.get(itemIface.getItemCode());
                MtSitePlantReleation siteId = (MtSitePlantReleation) site.get(itemIface.getPlantCode());
                siteId = Optional.ofNullable(siteId).orElse(new MtSitePlantReleation());
                MtMaterialSite mtMaterialSite = getMtMaterialSite(tenantId, mtMaterialId.getMaterialId(),
                                siteId.getSiteId(), itemIface.getEnableFlag(), date);
                mtMaterialSite.setMaterialSiteId(materialSiteIds.remove(0));
                mtMaterialSites.add(mtMaterialSite);

            }
        }

        return mtMaterialSites;
    }

    /**
     * @param tenantId
     * @param materialId
     * @param siteId
     * @param enableFlag
     * @param date
     * @return
     */
    public MtMaterialSite getMtMaterialSite(Long tenantId, String materialId, String siteId, String enableFlag,
                    Date date) {
        MtMaterialSite mtMaterialSite = new MtMaterialSite();
        mtMaterialSite.setMaterialId(materialId);
        mtMaterialSite.setSiteId(siteId);
        mtMaterialSite.setEnableFlag(enableFlag);
        // 架构字段
        mtMaterialSite.setTenantId(tenantId);
        mtMaterialSite.setCreatedBy(-1l);
        mtMaterialSite.setLastUpdatedBy(-1l);
        mtMaterialSite.setCreationDate(date);
        mtMaterialSite.setLastUpdateDate(date);
        mtMaterialSite.setCid(-1l);
        return mtMaterialSite;
    }

    /**
     * 赋值物料表数据
     *
     * @param tenantId
     * @param materialAttr
     * @param invItemIfaceList
     * @return
     */
    public List<MtMaterial> createMtMaterialRel(Long tenantId, Map<String, Map<String, Object>> materialAttr,
                    List<MtInvItemIface> invItemIfaceList) {
        List<MtMaterial> mtMaterials = new ArrayList<>();
        Date date = new Date();
        Map<String, Object> material = materialAttr.get("material");
        Map<String, Object> primaryUom = materialAttr.get("primaryUom");
        Map<String, Object> sizeUom = materialAttr.get("sizeUom");
        Map<String, Object> volumeUom = materialAttr.get("volumeUom");
        Map<String, Object> weightUom = materialAttr.get("weightUom");
        Map<String, Object> shelfLifeUom = materialAttr.get("shelfLifeUom");
        Map<String, Object> secondaryUom = materialAttr.get("secondaryUom");
        List<String> materialIds = customDbRepository.getNextKeys("mt_material_s", invItemIfaceList.size());
        for (MtInvItemIface itemIface : invItemIfaceList) {
            MtMaterial mtMaterial = new MtMaterial();
            BeanUtils.copyProperties(itemIface, mtMaterial);
            if (Strings.isNotEmpty(itemIface.getPrimaryUom())) {
                MtUomVO primaryUomId = (MtUomVO) primaryUom.get(itemIface.getPrimaryUom());
                primaryUomId = Optional.ofNullable(primaryUomId).orElse(new MtUomVO());
                if (!Objects.isNull(primaryUomId)) {
                    mtMaterial.setPrimaryUomId(primaryUomId.getUomId());
                }
            }

            if (Strings.isNotEmpty(itemIface.getSizeUomCode())) {
                MtUomVO sizeUomId = (MtUomVO) sizeUom.get(itemIface.getSizeUomCode());
                sizeUomId = Optional.ofNullable(sizeUomId).orElse(new MtUomVO());
                if (!Objects.isNull(sizeUomId)) {
                    mtMaterial.setSizeUomId(sizeUomId.getUomId());
                }
            }

            if (Strings.isNotEmpty(itemIface.getVolumeUomCode())) {
                MtUomVO volumeUomId = (MtUomVO) volumeUom.get(itemIface.getVolumeUomCode());
                volumeUomId = Optional.ofNullable(volumeUomId).orElse(new MtUomVO());
                if (!Objects.isNull(volumeUomId)) {
                    mtMaterial.setVolumeUomId(volumeUomId.getUomId());
                }
            }

            if (Strings.isNotEmpty(itemIface.getWeightUomCode())) {
                MtUomVO weightUomId = (MtUomVO) weightUom.get(itemIface.getWeightUomCode());
                weightUomId = Optional.ofNullable(weightUomId).orElse(new MtUomVO());
                if (!Objects.isNull(weightUomId)) {
                    mtMaterial.setWeightUomId(weightUomId.getUomId());
                }
            }

            if (Strings.isNotEmpty(itemIface.getShelfLifeUomCode())) {
                MtUomVO shelfLifeUomId = (MtUomVO) shelfLifeUom.get(itemIface.getShelfLifeUomCode());
                shelfLifeUomId = Optional.ofNullable(shelfLifeUomId).orElse(new MtUomVO());
                if (!Objects.isNull(shelfLifeUomId)) {
                    mtMaterial.setShelfLifeUomId(shelfLifeUomId.getUomId());
                }
            }

            if (Strings.isNotEmpty(itemIface.getSecondaryUomCode())) {
                MtUomVO secondaryUomId = (MtUomVO) secondaryUom.get(itemIface.getSecondaryUomCode());
                secondaryUomId = Optional.ofNullable(secondaryUomId).orElse(new MtUomVO());
                if (!Objects.isNull(secondaryUomId)) {
                    mtMaterial.setSecondaryUomId(secondaryUomId.getUomId());
                }
            }

            MtMaterial materialId = (MtMaterial) material.get(itemIface.getItemCode());
            if (!Objects.isNull(materialId)) {
                mtMaterial.setMaterialId(materialId.getMaterialId());
                mtMaterial.setObjectVersionNumber(materialId.getObjectVersionNumber());
            } else {
                mtMaterial.setMaterialId(materialIds.remove(0));
                mtMaterial.setObjectVersionNumber(null);
            }
            mtMaterial.setMaterialCode(itemIface.getItemCode());
            mtMaterial.setMaterialName(itemIface.getDescriptions());
            // 多语言
            Map<String, Map<String, String>> _tls = new HashMap<>();
            Map<String, String> _tls2 = new HashMap<>();
            _tls2.put("zh_CN", itemIface.getDescriptions());
            _tls2.put("en_US", itemIface.getDescriptions());
            _tls.put("materialName", _tls2);
            mtMaterial.set_tls(_tls);

            // 架构字段赋值
            mtMaterial.setTenantId(tenantId);
            mtMaterial.setCreatedBy(-1l);
            mtMaterial.setLastUpdatedBy(-1l);
            mtMaterial.setCreationDate(date);
            mtMaterial.setLastUpdateDate(date);
            mtMaterial.setCid(-1l);
            mtMaterials.add(mtMaterial);
        }
        return mtMaterials;
    }

    /**
     * 查询关联关系
     *
     * @param tenantId
     * @param invItemIfaceList
     * @return
     */
    public Map<String, Map<String, Object>> selectMaterialAttribute(Long tenantId,
                    List<MtInvItemIface> invItemIfaceList) {
        List<String> plantCodes = invItemIfaceList.stream().filter(a -> Strings.isNotEmpty(a.getPlantCode()))
                        .map(MtInvItemIface::getPlantCode).distinct().collect(Collectors.toList());
        List<String> itemCodes = invItemIfaceList.stream().filter(a -> Strings.isNotEmpty(a.getItemCode()))
                        .map(MtInvItemIface::getItemCode).distinct().collect(Collectors.toList());
        List<String> primaryUoms = invItemIfaceList.stream().filter(a -> Strings.isNotEmpty(a.getPrimaryUom()))
                        .map(MtInvItemIface::getPrimaryUom).distinct().collect(Collectors.toList());
        List<String> sizeUomCodes = invItemIfaceList.stream().filter(a -> Strings.isNotEmpty(a.getSizeUomCode()))
                        .map(MtInvItemIface::getSizeUomCode).distinct().collect(Collectors.toList());
        List<String> volumeUomCodes = invItemIfaceList.stream().filter(a -> Strings.isNotEmpty(a.getVolumeUomCode()))
                        .map(MtInvItemIface::getVolumeUomCode).distinct().collect(Collectors.toList());
        List<String> weightUomCodes = invItemIfaceList.stream().filter(a -> Strings.isNotEmpty(a.getWeightUomCode()))
                        .map(MtInvItemIface::getWeightUomCode).distinct().collect(Collectors.toList());
        List<String> shelfLifeUomCodes = invItemIfaceList.stream()
                        .filter(a -> Strings.isNotEmpty(a.getShelfLifeUomCode()))
                        .map(MtInvItemIface::getShelfLifeUomCode).distinct().collect(Collectors.toList());
        List<String> secondaryUomCodes = invItemIfaceList.stream()
                        .filter(a -> Strings.isNotEmpty(a.getSecondaryUomCode()))
                        .map(MtInvItemIface::getSecondaryUomCode).distinct().collect(Collectors.toList());
        List<String> stockLocatorCodes = invItemIfaceList.stream()
                        .filter(a -> Strings.isNotEmpty(a.getStockLocatorCode()))
                        .map(MtInvItemIface::getStockLocatorCode).distinct().collect(Collectors.toList());
        Map<String, Map<String, Object>> data = new HashMap<>();
        // 查询站点
        if (CollectionUtils.isNotEmpty(plantCodes)) {
            List<MtSitePlantReleation> siteList = mtInvItemIfaceMapper.selectSiteByPlantCode(tenantId,
                            "('" + StringUtils.join(plantCodes, "','") + "')");
            Map<String, Object> site =
                            siteList.stream().collect(Collectors.toMap(MtSitePlantReleation::getPlantCode, dto -> dto));
            data.put("site", site);
        }
        // 查询物料是否已存在
        if (CollectionUtils.isNotEmpty(itemCodes)) {
            List<MtMaterial> materialList = mtInvItemIfaceMapper.selectMaterialByItemCodes(tenantId,
                            "('" + StringUtils.join(itemCodes, "','") + "')");
            Map<String, Object> material =
                            materialList.stream().collect(Collectors.toMap(MtMaterial::getMaterialCode, dto -> dto));
            data.put("material", material);
        }
        // 查询单位1
        if (CollectionUtils.isNotEmpty(primaryUoms)) {
            List<MtUomVO> primaryUomList = mtInvItemIfaceMapper.selectPrimaryUom(tenantId,
                            "('" + StringUtils.join(primaryUoms, "','") + "')");
            Map<String, Object> primaryUom =
                            primaryUomList.stream().collect(Collectors.toMap(MtUomVO::getUomCode, dto -> dto));
            data.put("primaryUom", primaryUom);
        }
        // 查询单位2
        if (CollectionUtils.isNotEmpty(sizeUomCodes)) {
            List<MtUomVO> sizeUomList = mtInvItemIfaceMapper.selectPrimaryUom(tenantId,
                            "('" + StringUtils.join(sizeUomCodes, "','") + "')");
            Map<String, Object> sizeUom =
                            sizeUomList.stream().collect(Collectors.toMap(MtUomVO::getUomCode, dto -> dto));
            data.put("sizeUom", sizeUom);
        }
        // 查询单位3
        if (CollectionUtils.isNotEmpty(volumeUomCodes)) {
            List<MtUomVO> volumeUomList = mtInvItemIfaceMapper.selectPrimaryUom(tenantId,
                            "('" + StringUtils.join(volumeUomCodes, "','") + "')");
            Map<String, Object> volumeUom =
                            volumeUomList.stream().collect(Collectors.toMap(MtUomVO::getUomCode, dto -> dto));
            data.put("volumeUom", volumeUom);
        }
        // 查询单位4
        if (CollectionUtils.isNotEmpty(weightUomCodes)) {
            List<MtUomVO> weightUomList = mtInvItemIfaceMapper.selectPrimaryUom(tenantId,
                            "('" + StringUtils.join(weightUomCodes, "','") + "')");
            Map<String, Object> weightUom =
                            weightUomList.stream().collect(Collectors.toMap(MtUomVO::getUomCode, dto -> dto));
            data.put("weightUom", weightUom);
        }
        // 查询单位5
        if (CollectionUtils.isNotEmpty(shelfLifeUomCodes)) {
            List<MtUomVO> shelfLifeUomList = mtInvItemIfaceMapper.selectPrimaryUom(tenantId,
                            "('" + StringUtils.join(shelfLifeUomCodes, "','") + "')");
            Map<String, Object> shelfLifeUom =
                            shelfLifeUomList.stream().collect(Collectors.toMap(MtUomVO::getUomCode, dto -> dto));
            data.put("shelfLifeUom", shelfLifeUom);
        }
        // 查询单位6
        if (CollectionUtils.isNotEmpty(secondaryUomCodes)) {
            List<MtUomVO> secondaryUomList = mtInvItemIfaceMapper.selectPrimaryUom(tenantId,
                            "('" + StringUtils.join(secondaryUomCodes, "','") + "')");
            Map<String, Object> secondaryUom =
                            secondaryUomList.stream().collect(Collectors.toMap(MtUomVO::getUomCode, dto -> dto));
            data.put("secondaryUom", secondaryUom);
        }
        // 查询仓库
        if (CollectionUtils.isNotEmpty(stockLocatorCodes)) {
            List<MtModLocator> stockLocatorList = mtInvItemIfaceMapper.selectStockLocatorByLocatorCode(tenantId,
                            "('" + StringUtils.join(stockLocatorCodes, "','") + "')");
            Map<String, Object> stockLocator = stockLocatorList.stream()
                            .collect(Collectors.toMap(MtModLocator::getLocatorCode, dto -> dto));
            data.put("stockLocator", stockLocator);
        }

        return data;
    }

    private List<String> saveAttrColumnAttr(Long tenantId, Map<MtInvItemIface, List<String>> attrMap) {

        // 获取系统支持的所有语言环境
        List<Language> languages = LanguageHelper.languages();

        // 扩展表批量查询
        List<List<String>> lists = attrMap.entrySet().stream().map(t -> t.getValue()).collect(Collectors.toList());
        List<String> kidList = new ArrayList<>(attrMap.size() * 3);
        for (List<String> list : lists) {
            kidList.addAll(list);
        }

        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setTableName(MT_MATERIAL_SITE_ATTR_TABLE);
        mtExtendVO1.setKeyIdList(kidList);
        List<MtExtendAttrVO1> extendAttrList = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);

        Map<String, List<MtExtendAttrVO1>> originMap =
                        extendAttrList.stream().collect(Collectors.groupingBy(t -> t.getKeyId()));

        Map<String, List<MtExtendVO5>> attrTableMap = new HashMap<>(attrMap.size());
        List<String> result = new ArrayList<>();
        attrMap.entrySet().stream().forEach(t -> {
            Map<String, String> fieldMap = ObjectFieldsHelper.getAttributeFields(t.getKey(), "attribute");

            // 扩展属性更新时未传入时置为空
            if (MapUtils.isNotEmpty(originMap)) {
                for (String s : t.getValue()) {
                    if (CollectionUtils.isNotEmpty(originMap.get(s))) {
                        for (MtExtendAttrVO1 attrVO1 : originMap.get(s)) {
                            fieldMap.putIfAbsent(attrVO1.getAttrName(), "");
                        }
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
                for (String s : t.getValue()) {
                    attrTableMap.put(s, saveExtendList);
                }
            }
        });
        if (MapUtils.isNotEmpty(attrTableMap)) {
            result.addAll(mtExtendSettingsRepository.attrPropertyUpdateForIface(tenantId, MT_MATERIAL_SITE_ATTR_TABLE,
                            attrTableMap));
        }
        return result;

    }

    /**
     * 构建物料站点对象
     *
     * @param tenantId 租户Id
     * @param mtMaterialSite MtMaterialSite
     * @param sitePlantReleation MtSitePlantReleation
     * @param enableFlag 是否有效
     * @param materialId 物料Id
     * @param now 处理时间
     * @param userId 处理人
     * @param sourceIdentificationId 来源标识Id
     * @return MtMaterialSite
     * @author benjamin
     * @date 2019-07-30 18:19
     */
    private MtMaterialSite constructMaterialSite(Long tenantId, MtMaterialSite mtMaterialSite,
                    MtSitePlantReleation sitePlantReleation, String materialId, String enableFlag, Date now,
                    Long userId, Double sourceIdentificationId) {
        MtMaterialSite ms = new MtMaterialSite();
        ms.setEnableFlag(enableFlag);
        ms.setLastUpdateDate(now);
        ms.setLastUpdatedBy(userId);
        ms.setSiteId(sitePlantReleation.getSiteId());
        ms.setSourceIdentificationId(String.valueOf(sourceIdentificationId));
        ms.setTenantId(tenantId);
        boolean isNew = mtMaterialSite == null;
        if (isNew) {
            ms.setCreatedBy(userId);
            ms.setCreationDate(now);
            ms.setObjectVersionNumber(1L);
            ms.setMaterialId(materialId);
        } else {
            ms.setMaterialId(mtMaterialSite.getMaterialId());
            ms.setMaterialSiteId(mtMaterialSite.getMaterialSiteId());
            ms.setCreatedBy(mtMaterialSite.getCreatedBy());
            ms.setCreationDate(mtMaterialSite.getCreationDate());
            ms.setLastUpdatedBy(userId);
            ms.setLastUpdateDate(now);
            ms.setObjectVersionNumber(mtMaterialSite.getObjectVersionNumber() + 1L);
        }
        return ms;
    }

    /**
     * 构建物料计划属性Sql
     *
     * @param mtInvItemIface MtInvItemIface
     * @param type 站点类型
     * @param now 处理时间
     * @param userId 处理人
     * @return List
     * @author benjamin
     * @date 2019-07-30 18:20
     */
    private MtPfepSchedule constructPfepSchedual(Long tenantId, String materialSiteId, MtPfepSchedule mtPfepSchedule,
                    MtInvItemIface mtInvItemIface, String type, Date now, Long userId) {
        if (!"SCHEDULE".equals(type)) {
            return null;
        }
        if (mtPfepSchedule == null) {
            mtPfepSchedule = new MtPfepSchedule();
        }

        if ("1".equals(mtInvItemIface.getPlanningMakeBuyCode())) {
            mtPfepSchedule.setPreProcessingLeadTime(mtInvItemIface.getPrerpocessingLeadTime());
            mtPfepSchedule.setProcessingLeadTime(mtInvItemIface.getWipLeadTime());
            mtPfepSchedule.setPostProcessingLeadTime(mtInvItemIface.getInstockLeadTime());
            mtPfepSchedule.setKeyComponentFlag("Y");
        }
        mtPfepSchedule.setEnableFlag("Y");
        if (StringUtils.isNotEmpty(mtPfepSchedule.getMaterialSiteId())) {
            // update
            mtPfepSchedule.setLastUpdateDate(now);
            mtPfepSchedule.setLastUpdatedBy(userId);
            mtPfepSchedule.setTenantId(tenantId);
            mtPfepSchedule.setObjectVersionNumber(mtPfepSchedule.getObjectVersionNumber() + 1L);

        } else {
            // insert
            mtPfepSchedule.setMaterialSiteId(materialSiteId);
            mtPfepSchedule.setCreatedBy(userId);
            mtPfepSchedule.setCreationDate(now);
            mtPfepSchedule.setLastUpdateDate(now);
            mtPfepSchedule.setLastUpdatedBy(userId);
            mtPfepSchedule.setObjectVersionNumber(1L);
            mtPfepSchedule.setTenantId(tenantId);
        }
        return mtPfepSchedule;
    }

    /**
     * 构建物料采购属性Sql
     *
     * @param mtInvItemIface MtInvItemIface
     * @param type 站点类型
     * @param now 处理时间
     * @param userId 处理人
     * @return List
     * @author benjamin
     * @date 2019-07-30 18:22
     */
    private MtPfepPurchase constructPfepPurchase(Long tenantId, String materialSiteId, MtPfepPurchase purchase,
                    MtInvItemIface mtInvItemIface, String type, Date now, Long userId) {
        if (!"PURCHASE".equals(type)) {
            return null;
        }
        if (purchase == null) {
            purchase = new MtPfepPurchase();
        }
        if ("2".equals(mtInvItemIface.getPlanningMakeBuyCode())) {
            purchase.setPurchaseCycle(mtInvItemIface.getPurchaseLeadtime());
            purchase.setRequirementLeadTime(mtInvItemIface.getInstockLeadTime());
            purchase.setMinPackageQty(mtInvItemIface.getMinPackQty());
            purchase.setMinPurchaseQty(mtInvItemIface.getMinimumOrderQuantity());
        }
        purchase.setEnableFlag("Y");
        if (StringUtils.isNotEmpty(purchase.getMaterialSiteId())) {
            // update
            purchase.setBuyer(mtInvItemIface.getBuyerCode());
            purchase.setLastUpdateDate(now);
            purchase.setLastUpdatedBy(userId);
            purchase.setTenantId(tenantId);
            purchase.setObjectVersionNumber(purchase.getObjectVersionNumber() + 1L);
        } else {
            // insert
            purchase.setMaterialSiteId(materialSiteId);
            purchase.setBuyer(mtInvItemIface.getBuyerCode());
            purchase.setCreatedBy(userId);
            purchase.setCreationDate(now);
            purchase.setLastUpdateDate(now);
            purchase.setLastUpdatedBy(userId);
            purchase.setObjectVersionNumber(1L);
            purchase.setTenantId(tenantId);
        }
        return purchase;
    }

    /**
     * @Title:
     * @MethodName: constructPfepInventory
     * @Param [tenantId, materialSiteId, mtPfepInventory, stockLocatorCode, now, userId]
     * @Return tarzan.material.domain.entity.MtPfepInventory
     * @author: guichuan.li
     * @date: 2020/2/10 1:46 下午
     */
    private MtPfepInventory constructPfepInventory(Long tenantId, String materialSiteId,
                    MtPfepInventory mtPfepInventory, String stockLocatorCode, Date now, Long userId) {
        if (mtPfepInventory == null) {
            mtPfepInventory = new MtPfepInventory();
            mtPfepInventory.setEnableFlag("Y");
        }
        if (StringUtils.isNotEmpty(mtPfepInventory.getMaterialSiteId())) {
            // update
            mtPfepInventory.setStockLocatorId(stockLocatorCode);
            mtPfepInventory.setObjectVersionNumber(mtPfepInventory.getObjectVersionNumber() + 1L);
        } else {
            // insert
            mtPfepInventory.setMaterialSiteId(materialSiteId);
            mtPfepInventory.setStockLocatorId(stockLocatorCode);
            mtPfepInventory.setCreatedBy(userId);
            mtPfepInventory.setCreationDate(now);
            mtPfepInventory.setObjectVersionNumber(1L);
        }
        mtPfepInventory.setTenantId(tenantId);
        mtPfepInventory.setLastUpdateDate(now);
        mtPfepInventory.setLastUpdatedBy(userId);
        return mtPfepInventory;
    }

    /**
     * 构建物料业务属性Sql
     *
     * @param mtInvItemIface MtInvItemIface
     * @param now 处理时间
     * @param userId 处理人
     * @return List
     * @author benjamin
     * @date 2019-07-30 18:23
     */
    private MtMaterialBasic constructBasis(Long tenantId, String materialSiteId, MtMaterialBasic basis,
                    MtInvItemIface mtInvItemIface, Date now, Long userId) {
        if (basis == null) {
            basis = new MtMaterialBasic();
        }

        basis.setMaterialId(materialSiteId);
        basis.setOldItemCode(mtInvItemIface.getOldItemCode());
        basis.setLongDescription(mtInvItemIface.getDescriptionMir());
        basis.setItemType(mtInvItemIface.getItemType());
        basis.setMakeBuyCode(mtInvItemIface.getPlanningMakeBuyCode());
        basis.setLotControlCode(mtInvItemIface.getLotControlCode());
        basis.setQcFlag(mtInvItemIface.getQcFlag());
        basis.setReceivingRoutingId(mtInvItemIface.getReceivingRoutingId());
        basis.setWipSupplyType(mtInvItemIface.getWipSupplyType());
        basis.setVmiFlag(mtInvItemIface.getVmiFlag());
        basis.setItemGroup(mtInvItemIface.getItemGroup());
        basis.setProductGroup(mtInvItemIface.getProductGroup());
        if (StringUtils.isNotEmpty(basis.getMaterialSiteId())) {
            // update
            basis.setLastUpdateDate(now);
            basis.setLastUpdatedBy(userId);
            basis.setTenantId(tenantId);
            basis.setObjectVersionNumber(basis.getObjectVersionNumber());
        } else {
            // insert
            basis.setMaterialSiteId(materialSiteId);
            basis.setCreatedBy(userId);
            basis.setCreationDate(now);
            basis.setLastUpdateDate(now);
            basis.setLastUpdatedBy(userId);
            basis.setObjectVersionNumber(1L);
            basis.setTenantId(tenantId);
        }
        return basis;
    }

    /**
     * 构建返回消息
     *
     * @param mtInvItemIface MtInvItemIface
     * @param message 错误消息
     * @return MtBomComponentIface
     * @author benjamin
     * @date 2019-06-27 17:02
     */
    private MtInvItemIface constructIfaceMessage(Long tenantId, MtInvItemIface mtInvItemIface, String status,
                    String message, Date date, Long userId) {
        mtInvItemIface.setTenantId(tenantId);
        mtInvItemIface.setStatus(status);
        mtInvItemIface.setMessage(message);
        mtInvItemIface.setLastUpdateDate(date);
        mtInvItemIface.setLastUpdatedBy(userId);
        return mtInvItemIface;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void log(List<AuditDomain> ifaceSqlList) {
        if (CollectionUtils.isNotEmpty(ifaceSqlList)) {
            List<String> sqlList = constructSql(ifaceSqlList);
            List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
        }
    }

    /**
     * 生成拆分的sql
     *
     * @param ifaceSqlList
     * @return
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
}
