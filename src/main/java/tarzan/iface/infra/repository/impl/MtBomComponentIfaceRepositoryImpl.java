package tarzan.iface.infra.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.domain.AuditDomain;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.util.StringHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.iface.domain.entity.MtBomComponentIface;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.repository.MtBomComponentIfaceRepository;
import tarzan.iface.domain.repository.MtSitePlantReleationRepository;
import tarzan.iface.domain.vo.MtSitePlantReleationVO;
import tarzan.iface.domain.vo.MtSitePlantReleationVO1;
import tarzan.iface.domain.vo.MtSitePlantReleationVO3;
import tarzan.iface.infra.mapper.MtBomComponentIfaceMapper;
import tarzan.method.domain.entity.MtBom;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.method.domain.repository.MtBomRepository;
import tarzan.method.domain.vo.MtBomComponentVO7;
import tarzan.method.domain.vo.MtBomSubstituteGroupVO4;
import tarzan.method.domain.vo.MtBomSubstituteVO8;
import tarzan.method.domain.vo.MtBomVO11;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * BOM接口表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
@Component
public class MtBomComponentIfaceRepositoryImpl extends BaseRepositoryImpl<MtBomComponentIface>
        implements MtBomComponentIfaceRepository {

    private static final int SQL_ITEM_COUNT_LIMIT = 1000;
    private static final String IFACE = "【API：bomInterfaceImport】";
    private static final String MODULE = "INTERFACE";

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtBomComponentIfaceMapper mtBomComponentIfaceMapper;

    @Autowired
    private MtBomRepository mtBomRepository;
    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private MtSitePlantReleationRepository mtSitePlantReleationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Override
    public void bomInterfaceImport(Long tenantId) {
        // change data status
        Long start = System.currentTimeMillis();
        List<MtBomComponentIface> bomComponentIfaceList = self().updateIfaceStatus(tenantId, "P");
        // get data list
        Map<Double, List<MtBomComponentIface>> ifacePerBatch = bomComponentIfaceList.stream().collect(
                Collectors.groupingBy(MtBomComponentIface::getBatchId, TreeMap::new, Collectors.toList()));

        for (Map.Entry<Double, List<MtBomComponentIface>> entry : ifacePerBatch.entrySet()) {
            try {
                List<AuditDomain> ifaceList = self().saveBomComponentIface(tenantId, entry.getValue());

                self().log(tenantId, ifaceList);
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
                    ifs.setTenantId(tenantId);

                });
                ifaceList.addAll(entry.getValue());
                self().log(tenantId, ifaceList);
            }
        }
        System.out.println("bom接口耗时：" + (System.currentTimeMillis() - start) + "毫秒");
    }

    @Override
    public void myBomInterfaceImport(Long tenantId, Long batchId) {
        // change data status
        Long start = System.currentTimeMillis();
        List<MtBomComponentIface> bomComponentIfaceList = self().myUpdateIfaceStatus(tenantId, "P", batchId);
        // get data list
        Map<Double, List<MtBomComponentIface>> ifacePerBatch = bomComponentIfaceList.stream().collect(
                Collectors.groupingBy(MtBomComponentIface::getBatchId, TreeMap::new, Collectors.toList()));

        for (Map.Entry<Double, List<MtBomComponentIface>> entry : ifacePerBatch.entrySet()) {
            try {
                List<AuditDomain> ifaceList = self().mySaveBomComponentIface(tenantId, entry.getValue());

                log(tenantId, ifaceList);
            } catch (Exception e) {
                List<AuditDomain> ifaceList = new ArrayList<>(entry.getValue().size());
                e.printStackTrace();
                String error = e.getMessage().length() > 1000 ? e.getMessage().substring(0, 999) : e.getMessage();
                String msg = error + ";数据异常.";
                entry.getValue().forEach(ifs -> {
                    ifs.setStatus("E");
                    ifs.setMessage(msg);
                    ifs.setTenantId(tenantId);

                });
                ifaceList.addAll(entry.getValue());
                log(tenantId, ifaceList);
            }
        }
        System.out.println("bom接口耗时：" + (System.currentTimeMillis() - start) + "毫秒");
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public List<AuditDomain> saveBomComponentIface(Long tenantId, List<MtBomComponentIface> mtBomComponentIfaceList) {
        List<AuditDomain> ifaceList = new ArrayList<>(mtBomComponentIfaceList.size());

        // group iface list by bom code
        Map<Tuple, List<MtBomComponentIface>> ifacePerBomCode = mtBomComponentIfaceList.stream().collect(Collectors
                .groupingBy(b -> new Tuple(b.getBomCode(), b.getBomObjectType(), b.getBomAlternate())));

        // get site plant relation
        Map<String, MtSitePlantReleation> sitePlantMap = getSitePlantRelation(tenantId, mtBomComponentIfaceList);

        // map component item to material
        Map<ItemPlant, List<MtSitePlantReleationVO1>> componentMaterialMap =
                getMaterialSite(tenantId, mtBomComponentIfaceList, "MATERIAL");

        // map component item to bom
        Map<ItemPlant, List<MtSitePlantReleationVO1>> bomSiteMap =
                getMaterialSite(tenantId, mtBomComponentIfaceList, "BOM");

        // workorder数据
        List<String> workorderNum = mtBomComponentIfaceList.stream()
                .filter(c -> "WO".equalsIgnoreCase(c.getBomObjectType())
                        && StringUtils.isNotEmpty(c.getBomObjectCode()))
                .map(MtBomComponentIface::getBomObjectCode).distinct().collect(Collectors.toList());
        Map<String, MtWorkOrder> mtWorkOrderMap = null;
        if (CollectionUtils.isNotEmpty(workorderNum)) {
            List<MtWorkOrder> mtWorkOrders = mtWorkOrderRepository.woLimitWorkNUmQuery(tenantId, workorderNum);
            if (CollectionUtils.isNotEmpty(mtWorkOrders)) {
                mtWorkOrderMap = mtWorkOrders.stream().collect(Collectors.toMap(MtWorkOrder::getWorkOrderNum, c -> c));
            }
        }

        // 批量获取bom数据
        // 20211110 modify by sanfeng.zhang for wenxin.zhang 根据bomCode、revision和BomType 版本没有值则默认MAIN
        List<MtBomComponentIface> bomComponentIfaces = mtBomComponentIfaceList.stream().map(mbc -> {
            if (StringUtils.isBlank(mbc.getBomAlternate())) {
                mbc.setBomAlternate("MAIN");
            }
            return mbc;
        }).collect(Collectors.toList());
        Map<BomTuple, MtBom> mtBomMap = null;
//        List<String> bomCodes = mtBomComponentIfaceList.stream().map(MtBomComponentIface::getBomCode)
//                .collect(Collectors.toList());
        List<String> bomIds = new ArrayList<>(ifacePerBomCode.size());
//        if (CollectionUtils.isNotEmpty(bomComponentIfaces)) {
//            List<MtBom> mtBoms = mtBomRepository.bomLimitBomNameQuery(tenantId, bomCodes);
//            if (CollectionUtils.isNotEmpty(mtBoms)) {
//                mtBomMap = mtBoms.stream().collect(Collectors
//                        .toMap(c -> new BomTuple(c.getBomName(), c.getRevision(), c.getBomType()), c -> c));
//                bomIds.addAll(mtBoms.stream().map(MtBom::getBomId).distinct().collect(Collectors.toList()));
//            }
//        }
        if (CollectionUtils.isNotEmpty(bomComponentIfaces)) {
            List<MtBom> mtBoms = mtBomComponentIfaceMapper.bomLimitBomCodeAndRevisionAndBomTypeQuery(tenantId, bomComponentIfaces);
            if (CollectionUtils.isNotEmpty(mtBoms)) {
                mtBomMap = mtBoms.stream().collect(Collectors
                        .toMap(c -> new BomTuple(c.getBomName(), c.getRevision(), c.getBomType()), c -> c));
                bomIds.addAll(mtBoms.stream().map(MtBom::getBomId).distinct().collect(Collectors.toList()));
            }
        }

        // 批量获取bomComponent数据
        Map<BomComponentTuple, MtBomComponent> mtBomComponentMap = null;
        if (CollectionUtils.isNotEmpty(bomIds)) {
            List<MtBomComponent> mtBomComponents =
                    mtBomComponentRepository.selectBomComponentByBomIds(tenantId, bomIds);
            if (CollectionUtils.isNotEmpty(mtBomComponents)) {
                mtBomComponentMap = mtBomComponents.stream()
                        .collect(Collectors.toMap(c -> new BomComponentTuple(c.getBomId(), c.getMaterialId(),
                                c.getBomComponentType(), c.getLineNumber()), c -> c));
            }
        }
        // 批量获取issueLocatorId
        List<String> issueLocatorCodes = mtBomComponentIfaceList.stream().map(MtBomComponentIface::getIssueLocatorCode)
                .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        Map<String, String> issueLocatorIdMap =
                mtModLocatorRepository.selectModLocatorForCodes(tenantId, issueLocatorCodes).stream().collect(
                        Collectors.toMap(MtModLocator::getLocatorCode, MtModLocator::getLocatorId));
        // create event
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("BOM_INTERFACE_UPDATE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 公有变量
        Long userId = Long.valueOf(-1L);
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        Date now = new Date(System.currentTimeMillis());
        BomTuple bomTuple;
        MtBom mtBom;
        ItemPlant itemPlant;
        MtBomComponent mtBomComponent = null;
        BomComponentTuple bomComponentTuple;
        List<MtBomVO11> mtBoms = new ArrayList<>(ifacePerBomCode.size());
        MtBomVO11 bom;
        List<MtBomComponentVO7> bomComponentList;
        for (Map.Entry<Tuple, List<MtBomComponentIface>> bomEntry : ifacePerBomCode.entrySet()) {
            MtBomComponentIface headBomIface = bomEntry.getValue().get(0);
            // 新增全量更新标识
            String allUpdate = headBomIface.getUpdateMethod();
            String messageCode = "";

            // solve bom & bom components
            bomTuple = new BomTuple(headBomIface.getBomCode(),
                    StringUtils.isEmpty(headBomIface.getBomAlternate()) ? "MAIN"
                            : headBomIface.getBomAlternate(),
                    headBomIface.getBomObjectType());
            if (MapUtils.isNotEmpty(mtBomMap) && mtBomMap.get(bomTuple) != null) {
                mtBom = mtBomMap.get(bomTuple);
            } else {
                mtBom = new MtBom();
            }
            bom = constructBom(headBomIface, mtBom);
            bom.setBomComponentIface(headBomIface);
            Boolean allFlag = "ALL".equals(allUpdate);
            Boolean allUpdateFlag = true;
            Boolean headValidateFlag = true;
            bomComponentList = new ArrayList<>(bomEntry.getValue().size());
            for (MtBomComponentIface iface : bomEntry.getValue()) {
                if (sitePlantMap.get(iface.getPlantCode()) == null) {
                    if (allFlag) {
                        allUpdateFlag = false;
                        messageCode = mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0012",
                                MODULE, iface.getIfaceId(), IFACE);
                        break;
                    }
                    ifaceList.add(constructIfaceMessage(tenantId, iface, "E",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0012",
                                    "INTERFACE", iface.getIfaceId(), "【API：bomInterfaceImport】"),
                            now, userId));
                    continue;
                }
                // construct bom component
                itemPlant = new ItemPlant(iface.getPlantCode(), iface.getComponentItemCode());
                if (componentMaterialMap == null || CollectionUtils.isEmpty(componentMaterialMap.get(itemPlant))) {
                    if (allFlag) {
                        allUpdateFlag = false;
                        messageCode = mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0014",
                                MODULE, iface.getIfaceId(), IFACE);
                        break;
                    }
                    ifaceList.add(constructIfaceMessage(tenantId, iface, "E",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0014",
                                    "INTERFACE", iface.getIfaceId(), "【API：bomInterfaceImport】"),
                            now, userId));
                    continue;
                }
                if (StringUtils.isNotEmpty(bom.getBomId())) {
                    bomComponentTuple = new BomComponentTuple(bom.getBomId(),
                            componentMaterialMap.get(itemPlant).get(0).getMaterialId(),
                            "6".equals(iface.getWipSupplyType()) ? "PHANTOM" : "ASSEMBLING",
                            iface.getComponentLineNum());
                    if (MapUtils.isNotEmpty(mtBomComponentMap) && mtBomComponentMap.get(bomComponentTuple) != null) {
                        mtBomComponent = mtBomComponentMap.get(bomComponentTuple);
                    } else {
                        mtBomComponent = new MtBomComponent();
                    }
                } else {
                    mtBomComponent = new MtBomComponent();
                }
                bomComponentList.add(constructBomComponent(iface, issueLocatorIdMap,
                        componentMaterialMap.get(itemPlant).get(0), mtBomComponent, mtBom.getBomId()));
                ifaceList.add(constructIfaceMessage(tenantId, iface, "S", "成功.", now, userId));
            }

            // 全量更新时行数据需同时满足条件 add by lgc 当bom头数据校验不通过时，继续执行下一bom的数据
            // update bom site assign
            if (allUpdateFlag) {
                if (sitePlantMap == null || sitePlantMap.get(headBomIface.getPlantCode()) == null) {
                    messageCode = mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0013",
                            "INTERFACE", headBomIface.getIfaceId(), "【API:bomInterfaceImport】");
                    headValidateFlag = false;
                }
                // base on object type
                if (headValidateFlag && StringUtils.isNotEmpty(headBomIface.getBomObjectType())) {
                    bom.setBomObjectType(headBomIface.getBomObjectType());
                }
                if (headValidateFlag) {
                    if ("MATERIAL".equals(headBomIface.getBomObjectType())) {
                        // map assembly item to material
                        itemPlant = new tarzan.iface.infra.repository.impl.MtBomComponentIfaceRepositoryImpl.ItemPlant(
                                headBomIface.getPlantCode(), headBomIface.getBomObjectCode());
                        // update pfep
                        if (MapUtils.isEmpty(bomSiteMap) || CollectionUtils.isEmpty(bomSiteMap.get(itemPlant))) {
                            messageCode = mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_INTERFACE_0013", "INTERFACE", headBomIface.getIfaceId(),
                                    "【API:bomInterfaceImport】");
                            headValidateFlag = false;
                        }
                        if (headValidateFlag) {
                            bom.setMaterialSiteId(bomSiteMap.get(itemPlant).get(0).getMaterialSiteId());
                            // 20210903 add by sanfeng.zhang for wenxin.zhang 物料类型的 覆盖bomName
                            bom.setBomName(headBomIface.getBomObjectCode());
                        }
                    } else if ("WO".equals(headBomIface.getBomObjectType())) {
                        if (MapUtils.isEmpty(mtWorkOrderMap)
                                || mtWorkOrderMap.get(headBomIface.getBomObjectCode()) == null) {
                            messageCode = mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_INTERFACE_0022", "INTERFACE", headBomIface.getIfaceId(),
                                    "【API:bomInterfaceImport】");
                            headValidateFlag = false;
                        }
                        if (headValidateFlag) {
                            bom.setWorkOrder(mtWorkOrderMap.get(headBomIface.getBomObjectCode()));
                        }
                    }
                }
            }

            if (allFlag && !allUpdateFlag || !headValidateFlag) {
                for (MtBomComponentIface iface : bomEntry.getValue()) {
                    ifaceList.add(constructIfaceMessage(tenantId, iface, "E", messageCode, now, userId));
                }
                continue;
            }

            bom.setSiteId(sitePlantMap.get(headBomIface.getPlantCode()).getSiteId());
            if (CollectionUtils.isEmpty(bomComponentList)) {
                continue;
            }
            bom.setMtBomComponentList(bomComponentList);
            bom.setEventId(eventId);
            mtBoms.add(bom);
        }
        /**
         * 处理bom-bomComponent-attr
         */
        if (CollectionUtils.isNotEmpty(mtBoms)) {
            mtBomRepository.bomAllBatchUpdate(tenantId, mtBoms);
        }
        return ifaceList;
    }

    @Override
    public List<AuditDomain> mySaveBomComponentIface(Long tenantId, List<MtBomComponentIface> mtBomComponentIfaceList) {
        List<AuditDomain> ifaceList = new ArrayList<>(mtBomComponentIfaceList.size());

        // group iface list by bom code
        Map<Tuple, List<MtBomComponentIface>> ifacePerBomCode = mtBomComponentIfaceList.stream().collect(Collectors
                .groupingBy(b -> new Tuple(b.getBomCode(), b.getBomObjectType(), b.getBomAlternate())));

        // get site plant relation
        Map<String, MtSitePlantReleation> sitePlantMap = getSitePlantRelation(tenantId, mtBomComponentIfaceList);

        // map component item to material
        Map<ItemPlant, List<MtSitePlantReleationVO1>> componentMaterialMap =
                getMaterialSite(tenantId, mtBomComponentIfaceList, "MATERIAL");

        // map component item to bom
        Map<ItemPlant, List<MtSitePlantReleationVO1>> bomSiteMap =
                getMaterialSite(tenantId, mtBomComponentIfaceList, "BOM");

        // workorder数据
        List<String> workorderNum = mtBomComponentIfaceList.stream()
                .filter(c -> "WO".equalsIgnoreCase(c.getBomObjectType())
                        && StringUtils.isNotEmpty(c.getBomObjectCode()))
                .map(MtBomComponentIface::getBomObjectCode).distinct().collect(Collectors.toList());
        Map<String, MtWorkOrder> mtWorkOrderMap = null;
        if (CollectionUtils.isNotEmpty(workorderNum)) {
            List<MtWorkOrder> mtWorkOrders = mtWorkOrderRepository.woLimitWorkNUmQuery(tenantId, workorderNum);
            if (CollectionUtils.isNotEmpty(mtWorkOrders)) {
                mtWorkOrderMap = mtWorkOrders.stream().collect(Collectors.toMap(MtWorkOrder::getWorkOrderNum, c -> c));
            }
        }

        // 批量获取bom数据
        // 20211110 modify by sanfeng.zhang for wenxin.zhang 根据bomCode、revision和BomType 版本没有值则默认MAIN
        List<MtBomComponentIface> bomComponentIfaces = mtBomComponentIfaceList.stream().map(mbc -> {
            if (StringUtils.isBlank(mbc.getBomAlternate())) {
                mbc.setBomAlternate("MAIN");
            }
            return mbc;
        }).collect(Collectors.toList());
        Map<BomTuple, MtBom> mtBomMap = null;
//        List<String> bomCodes = mtBomComponentIfaceList.stream().map(MtBomComponentIface::getBomCode)
//                .collect(Collectors.toList());
        List<String> bomIds = new ArrayList<>(ifacePerBomCode.size());
//        if (CollectionUtils.isNotEmpty(bomComponentIfaces)) {
//            List<MtBom> mtBoms = mtBomRepository.bomLimitBomNameQuery(tenantId, bomCodes);
//            if (CollectionUtils.isNotEmpty(mtBoms)) {
//                mtBomMap = mtBoms.stream().collect(Collectors
//                        .toMap(c -> new BomTuple(c.getBomName(), c.getRevision(), c.getBomType()), c -> c));
//                bomIds.addAll(mtBoms.stream().map(MtBom::getBomId).distinct().collect(Collectors.toList()));
//            }
//        }
        if (CollectionUtils.isNotEmpty(bomComponentIfaces)) {
            List<MtBom> mtBoms = mtBomComponentIfaceMapper.bomLimitBomCodeAndRevisionAndBomTypeQuery(tenantId, bomComponentIfaces);
            if (CollectionUtils.isNotEmpty(mtBoms)) {
                mtBomMap = mtBoms.stream().collect(Collectors
                        .toMap(c -> new BomTuple(c.getBomName(), c.getRevision(), c.getBomType()), c -> c));
                bomIds.addAll(mtBoms.stream().map(MtBom::getBomId).distinct().collect(Collectors.toList()));
            }
        }

        // 批量获取bomComponent数据
        Map<BomComponentTuple, MtBomComponent> mtBomComponentMap = null;
        if (CollectionUtils.isNotEmpty(bomIds)) {
            List<MtBomComponent> mtBomComponents =
                    mtBomComponentRepository.selectBomComponentByBomIds(tenantId, bomIds);
            if (CollectionUtils.isNotEmpty(mtBomComponents)) {
                mtBomComponentMap = mtBomComponents.stream()
                        .collect(Collectors.toMap(c -> new BomComponentTuple(c.getBomId(), c.getMaterialId(),
                                c.getBomComponentType(), c.getLineNumber()), c -> c));
            }
        }
        // 批量获取issueLocatorId
        List<String> issueLocatorCodes = mtBomComponentIfaceList.stream().map(MtBomComponentIface::getIssueLocatorCode)
                .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        Map<String, String> issueLocatorIdMap =
                mtModLocatorRepository.selectModLocatorForCodes(tenantId, issueLocatorCodes).stream().collect(
                        Collectors.toMap(MtModLocator::getLocatorCode, MtModLocator::getLocatorId));
        // create event
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("BOM_INTERFACE_UPDATE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 公有变量
        Long userId = Long.valueOf(-1L);
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        Date now = new Date(System.currentTimeMillis());
        BomTuple bomTuple;
        MtBom mtBom;
        ItemPlant itemPlant;
        MtBomComponent mtBomComponent = null;
        BomComponentTuple bomComponentTuple;
        List<MtBomVO11> mtBoms = new ArrayList<>(ifacePerBomCode.size());
        MtBomVO11 bom;
        List<MtBomComponentVO7> bomComponentList;
        for (Map.Entry<Tuple, List<MtBomComponentIface>> bomEntry : ifacePerBomCode.entrySet()) {
            MtBomComponentIface headBomIface = bomEntry.getValue().get(0);
            // 新增全量更新标识
            String allUpdate = headBomIface.getUpdateMethod();
            String messageCode = "";

            // solve bom & bom components
            bomTuple = new BomTuple(headBomIface.getBomCode(),
                    StringUtils.isEmpty(headBomIface.getBomAlternate()) ? "MAIN"
                            : headBomIface.getBomAlternate(),
                    headBomIface.getBomObjectType());
            if (MapUtils.isNotEmpty(mtBomMap) && mtBomMap.get(bomTuple) != null) {
                mtBom = mtBomMap.get(bomTuple);
            } else {
                mtBom = new MtBom();
            }
            bom = constructBom(headBomIface, mtBom);
            bom.setBomComponentIface(headBomIface);
            Boolean allFlag = "ALL".equals(allUpdate);
            Boolean allUpdateFlag = true;
            Boolean headValidateFlag = true;
            bomComponentList = new ArrayList<>(bomEntry.getValue().size());
            for (MtBomComponentIface iface : bomEntry.getValue()) {
                if (sitePlantMap.get(iface.getPlantCode()) == null) {
                    if (allFlag) {
                        allUpdateFlag = false;
                        messageCode = mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0012",
                                MODULE, iface.getIfaceId(), IFACE);
                        break;
                    }
                    ifaceList.add(constructIfaceMessage(tenantId, iface, "E",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0012",
                                    "INTERFACE", iface.getIfaceId(), "【API：bomInterfaceImport】"),
                            now, userId));
                    continue;
                }
                // construct bom component
                itemPlant = new ItemPlant(iface.getPlantCode(), iface.getComponentItemCode());
                if (componentMaterialMap == null || CollectionUtils.isEmpty(componentMaterialMap.get(itemPlant))) {
                    if (allFlag) {
                        allUpdateFlag = false;
                        messageCode = mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0014",
                                MODULE, iface.getIfaceId(), IFACE);
                        break;
                    }
                    ifaceList.add(constructIfaceMessage(tenantId, iface, "E",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0014",
                                    "INTERFACE", iface.getIfaceId(), "【API：bomInterfaceImport】"),
                            now, userId));
                    continue;
                }
                if (StringUtils.isNotEmpty(bom.getBomId())) {
                    bomComponentTuple = new BomComponentTuple(bom.getBomId(),
                            componentMaterialMap.get(itemPlant).get(0).getMaterialId(),
                            "6".equals(iface.getWipSupplyType()) ? "PHANTOM" : "ASSEMBLING",
                            iface.getComponentLineNum());
                    if (MapUtils.isNotEmpty(mtBomComponentMap) && mtBomComponentMap.get(bomComponentTuple) != null) {
                        mtBomComponent = mtBomComponentMap.get(bomComponentTuple);
                    } else {
                        mtBomComponent = new MtBomComponent();
                    }
                } else {
                    mtBomComponent = new MtBomComponent();
                }
                bomComponentList.add(myConstructBomComponent(iface, issueLocatorIdMap,
                        componentMaterialMap, mtBomComponent, mtBom.getBomId(), bomEntry.getValue(),itemPlant));
                ifaceList.add(constructIfaceMessage(tenantId, iface, "S", "成功.", now, userId));
            }

            // 全量更新时行数据需同时满足条件 add by lgc 当bom头数据校验不通过时，继续执行下一bom的数据
            // update bom site assign
            if (allUpdateFlag) {
                if (sitePlantMap == null || sitePlantMap.get(headBomIface.getPlantCode()) == null) {
                    messageCode = mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INTERFACE_0013",
                            "INTERFACE", headBomIface.getIfaceId(), "【API:bomInterfaceImport】");
                    headValidateFlag = false;
                }
                // base on object type
                if (headValidateFlag && StringUtils.isNotEmpty(headBomIface.getBomObjectType())) {
                    bom.setBomObjectType(headBomIface.getBomObjectType());
                }
                if (headValidateFlag) {
                    if ("MATERIAL".equals(headBomIface.getBomObjectType())) {
                        // map assembly item to material
                        itemPlant = new tarzan.iface.infra.repository.impl.MtBomComponentIfaceRepositoryImpl.ItemPlant(
                                headBomIface.getPlantCode(), headBomIface.getBomObjectCode());
                        // update pfep
                        if (MapUtils.isEmpty(bomSiteMap) || CollectionUtils.isEmpty(bomSiteMap.get(itemPlant))) {
                            messageCode = mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_INTERFACE_0013", "INTERFACE", headBomIface.getIfaceId(),
                                    "【API:bomInterfaceImport】");
                            headValidateFlag = false;
                        }
                        if (headValidateFlag) {
                            bom.setMaterialSiteId(bomSiteMap.get(itemPlant).get(0).getMaterialSiteId());
                            // 20210903 add by sanfeng.zhang for wenxin.zhang 物料类型的 覆盖bomName
                            bom.setBomName(headBomIface.getBomObjectCode());
                        }
                    } else if ("WO".equals(headBomIface.getBomObjectType())) {
                        if (MapUtils.isEmpty(mtWorkOrderMap)
                                || mtWorkOrderMap.get(headBomIface.getBomObjectCode()) == null) {
                            messageCode = mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_INTERFACE_0022", "INTERFACE", headBomIface.getIfaceId(),
                                    "【API:bomInterfaceImport】");
                            headValidateFlag = false;
                        }
                        if (headValidateFlag) {
                            bom.setWorkOrder(mtWorkOrderMap.get(headBomIface.getBomObjectCode()));
                        }
                    }
                }
            }

            if (allFlag && !allUpdateFlag || !headValidateFlag) {
                for (MtBomComponentIface iface : bomEntry.getValue()) {
                    ifaceList.add(constructIfaceMessage(tenantId, iface, "E", messageCode, now, userId));
                }
                continue;
            }

            bom.setSiteId(sitePlantMap.get(headBomIface.getPlantCode()).getSiteId());
            if (CollectionUtils.isEmpty(bomComponentList)) {
                continue;
            }
            bom.setMtBomComponentList(bomComponentList);
            bom.setEventId(eventId);
            mtBoms.add(bom);
        }
        /**
         * 处理bom-bomComponent-attr
         */
        if (CollectionUtils.isNotEmpty(mtBoms)) {
            mtBomRepository.myBomAllBatchUpdate(tenantId, mtBoms);
        }
        return ifaceList;
    }

    /**
     * 根据工厂编码获取工厂与站点关系
     * <p>
     * 站点类型指定为manufacturing
     *
     * @param tenantId                租户Id
     * @param mtBomComponentIfaceList List<MtBomComponentIface>
     * @return Map<String, MtSitePlantReleation>
     * @author benjamin
     * @date 2019-06-27 10:21
     */
    private Map<String, MtSitePlantReleation> getSitePlantRelation(Long tenantId,
                                                                   List<MtBomComponentIface> mtBomComponentIfaceList) {
        List<String> plantCodeList = mtBomComponentIfaceList.stream().map(MtBomComponentIface::getPlantCode).distinct()
                .collect(Collectors.toList());
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
            } else {
                sitePlantMap = new HashMap<>();
            }
        }
        return sitePlantMap;
    }

    /**
     * 根据工厂物料获取物料站点关系
     * <p>
     * 站点类型指定为manufacturing
     *
     * @param tenantId 租户Id
     * @return Map<String, MtSitePlantReleationVO1>
     * @author benjamin
     * @date 2019-06-27 10:29
     */
    private Map<ItemPlant, List<MtSitePlantReleationVO1>> getMaterialSite(Long tenantId,
                                                                          List<MtBomComponentIface> ifaceList, String type) {
        MtSitePlantReleationVO mtSitePlantReleationVO = new MtSitePlantReleationVO();
        mtSitePlantReleationVO.setPlantCodeList(ifaceList.stream().map(MtBomComponentIface::getPlantCode).distinct()
                .collect(Collectors.toList()));
        if ("MATERIAL".equalsIgnoreCase(type)) {
            mtSitePlantReleationVO.setItemCodeList(ifaceList.stream().map(MtBomComponentIface::getComponentItemCode)
                    .distinct().collect(Collectors.toList()));
        } else {
            mtSitePlantReleationVO.setItemCodeList(ifaceList.stream().map(MtBomComponentIface::getBomObjectCode)
                    .distinct().collect(Collectors.toList()));
        }
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
     * 构建BOM对象
     *
     * @param headBomIface MtBomComponentIface
     * @return BomVO6
     * @author benjamin
     * @date 2019-06-27 10:31
     */
    private MtBomVO11 constructBom(MtBomComponentIface headBomIface, MtBom mtBom) {
        MtBomVO11 bom = new MtBomVO11();
        bom.setBomId(mtBom == null ? null : mtBom.getBomId());
        bom.setBomName(headBomIface.getBomCode());
        bom.setRevision(StringUtils.isEmpty(headBomIface.getBomAlternate()) ? "MAIN" : headBomIface.getBomAlternate());
        bom.setBomType(headBomIface.getBomObjectType());
        bom.setCurrentFlag("Y");
        bom.setDateFrom(headBomIface.getBomStartDate());
        bom.setDateTo(headBomIface.getBomEndDate());
        bom.setDescription(headBomIface.getBomDescription());
        bom.setBomStatus(
                StringUtils.isEmpty(headBomIface.getBomStatus()) || "ACTIVE".equals(headBomIface.getBomStatus())
                        ? "CAN_RELEASE"
                        : "NEW");
        bom.setReleasedFlag("Y");
        bom.setPrimaryQty(headBomIface.getStandardQty() == null ? Double.valueOf(0.0D) : headBomIface.getStandardQty());
        bom.setAssembleAsMaterialFlag("Y");
        bom.setAutoRevisionFlag("N");
        bom.setUpdateMethod(headBomIface.getUpdateMethod());
        return bom;
    }

    /**
     * 构建BOM组件对象
     *
     * @param mtBomComponentIface MtBomComponentIface
     * @return BomComponentVO7
     * @author benjamin
     * @date 2019-06-27 10:31
     */
    private MtBomComponentVO7 constructBomComponent(MtBomComponentIface mtBomComponentIface,
                                                    Map<String, String> issueLocatorIdMap, MtSitePlantReleationVO1 mtSitePlantReleationVO1,
                                                    MtBomComponent originBomcom, String bomId) {
        MtBomComponentVO7 bomComponent = new MtBomComponentVO7();
        // 原有数据字段
        if (originBomcom != null) {
            bomComponent.setKeyMaterialFlag(originBomcom.getKeyMaterialFlag());
            bomComponent.setAssembleAsReqFlag(originBomcom.getAssembleAsReqFlag());
            bomComponent.setAttritionPolicy(originBomcom.getAttritionPolicy());
            bomComponent.setAttritionQty(originBomcom.getAttritionQty());
            bomComponent.setCopiedFromComponentId(originBomcom.getCopiedFromComponentId());
            bomComponent.setBomComponentId(originBomcom.getBomComponentId());
        }
        bomComponent.setLineNumber(mtBomComponentIface.getComponentLineNum());
        bomComponent.setMaterialId(mtSitePlantReleationVO1.getMaterialId());
        bomComponent.setBomComponentType(mtBomComponentIface.getBomComponentType());
        bomComponent.setDateFrom(mtBomComponentIface.getComponentStartDate());
        bomComponent.setDateTo(mtBomComponentIface.getComponentEndDate());
        bomComponent.setQty(mtBomComponentIface.getBomUsage());
        bomComponent.setAssembleMethod(StringUtils.isNotEmpty(mtBomComponentIface.getAssembleMethod())
                ? mtBomComponentIface.getAssembleMethod()
                : MtBaseConstants.ASSEMBLE_METHOD.ISSUE);

        bomComponent.setAttritionChance(mtBomComponentIface.getComponentShrinkage());
        bomComponent.setIssuedLocatorId(issueLocatorIdMap.get(mtBomComponentIface.getIssueLocatorCode()));
        bomComponent.setBomId(bomId);
        bomComponent.setBomComponentIface(mtBomComponentIface);
        return bomComponent;
    }


    private MtBomComponentVO7 myConstructBomComponent(MtBomComponentIface mtBomComponentIface, Map<String, String> issueLocatorIdMap,
                                                      Map<ItemPlant, List<MtSitePlantReleationVO1>> componentMaterialMap,
                                                      MtBomComponent originBomcom, String bomId, List<MtBomComponentIface> componentIfaces, ItemPlant itemPlant) {
        MtBomComponentVO7 bomComponent = new MtBomComponentVO7();
        MtSitePlantReleationVO1 mtSitePlantReleationVO1 = componentMaterialMap.get(itemPlant).get(0);
        // 原有数据字段
        if (originBomcom != null) {
            bomComponent.setKeyMaterialFlag(originBomcom.getKeyMaterialFlag());
            bomComponent.setAssembleAsReqFlag(originBomcom.getAssembleAsReqFlag());
            bomComponent.setAttritionPolicy(originBomcom.getAttritionPolicy());
            bomComponent.setAttritionQty(originBomcom.getAttritionQty());
            bomComponent.setCopiedFromComponentId(originBomcom.getCopiedFromComponentId());
            bomComponent.setBomComponentId(originBomcom.getBomComponentId());
        }
        bomComponent.setLineNumber(mtBomComponentIface.getComponentLineNum());
        bomComponent.setMaterialId(mtSitePlantReleationVO1.getMaterialId());
        bomComponent.setBomComponentType(mtBomComponentIface.getBomComponentType());
        bomComponent.setDateFrom(mtBomComponentIface.getComponentStartDate());
        bomComponent.setDateTo(mtBomComponentIface.getComponentEndDate());
        bomComponent.setQty(mtBomComponentIface.getBomUsage());
        bomComponent.setAssembleMethod(StringUtils.isNotEmpty(mtBomComponentIface.getAssembleMethod())
                ? mtBomComponentIface.getAssembleMethod()
                : MtBaseConstants.ASSEMBLE_METHOD.ISSUE);

        bomComponent.setAttritionChance(mtBomComponentIface.getComponentShrinkage());
        bomComponent.setIssuedLocatorId(issueLocatorIdMap.get(mtBomComponentIface.getIssueLocatorCode()));
        bomComponent.setBomId(bomId);
        bomComponent.setBomComponentIface(mtBomComponentIface);
        if (StringUtils.isNotEmpty(mtBomComponentIface.getSubstituteGroup())) {
            MtBomSubstituteGroupVO4 mtBomSubstituteGroupVO4 = new MtBomSubstituteGroupVO4();
            String substituteGroup = mtBomComponentIface.getSubstituteGroup();
            mtBomSubstituteGroupVO4.setSubstituteGroup(substituteGroup);
            String substitutePolicy = mtBomComponentIface.getLineAttribute15();
            if ("1".equals(substitutePolicy)) {
                substitutePolicy = "CHANCE";
            } else if ("2".equals(substitutePolicy)) {
                substitutePolicy = "PRIORITY";
            }
            mtBomSubstituteGroupVO4.setSubstitutePolicy(substitutePolicy);
            mtBomSubstituteGroupVO4.setEnableFlag("Y");

            // 管理替代组
            List<MtBomComponentIface> mtBomComponentIfaces = componentIfaces.stream().filter(a -> substituteGroup.equals(a.getSubstituteGroup())).collect(Collectors.toList());
            List<MtBomSubstituteVO8> vo8s = new ArrayList<>();
            for (MtBomComponentIface iface : mtBomComponentIfaces) {
                ItemPlant itemPlant1 = new ItemPlant(iface.getPlantCode(), iface.getComponentItemCode());
                MtSitePlantReleationVO1 materialId = componentMaterialMap.get(itemPlant1).get(0);
                if(Objects.isNull(materialId) || materialId.getMaterialId().equals(mtSitePlantReleationVO1.getMaterialId())){
                    continue;
                }
                MtBomSubstituteVO8 mtBomSubstituteVO8 = new MtBomSubstituteVO8();
                mtBomSubstituteVO8.setMaterialId(materialId.getMaterialId());

                String substituteValueStr = mtBomComponentIface.getLineAttribute15().equals("1")
                        ? mtBomComponentIface.getLineAttribute17()
                        : mtBomComponentIface.getLineAttribute16();
                Double substituteValue = "".equals(substituteValueStr) ? null : Double.valueOf(substituteValueStr);
                mtBomSubstituteVO8.setSubstituteValue(substituteValue);
                mtBomSubstituteVO8.setSubstituteUsage(mtBomComponentIface.getBomUsage());
                mtBomSubstituteVO8.setDateFrom(mtBomComponentIface.getComponentStartDate());
                mtBomSubstituteVO8.setDateTo(mtBomComponentIface.getComponentEndDate());
                vo8s.add(mtBomSubstituteVO8);
            }
            mtBomSubstituteGroupVO4.setMtBomSubstituteList(vo8s);

            bomComponent.setMtBomSubstituteGroupList(CollectionUtil.toList(mtBomSubstituteGroupVO4));
        }
        return bomComponent;
    }

    /**
     * 构建返回消息
     *
     * @param mtBomComponentIface MtBomComponentIface
     * @param message             错误消息
     * @return MtBomComponentIface
     * @author benjamin
     * @date 2019-06-27 17:02
     */
    private MtBomComponentIface constructIfaceMessage(Long tenantId, MtBomComponentIface mtBomComponentIface,
                                                      String status, String message, Date date, Long userId) {
        mtBomComponentIface.setTenantId(tenantId);
        mtBomComponentIface.setStatus(status);
        mtBomComponentIface.setMessage(message);
        mtBomComponentIface.setLastUpdateDate(date);
        mtBomComponentIface.setLastUpdatedBy(userId);

        return mtBomComponentIface;
    }

    @Override
    public List<MtBomComponentIface> myUpdateIfaceStatus(Long tenantId, String status, Long batchId) {
        List<MtBomComponentIface> bomComponentIfaceList = mtBomComponentIfaceMapper.myGetUnprocessedList(tenantId, batchId);
        if (CollectionUtils.isNotEmpty(bomComponentIfaceList)) {
            List<String> sqlList = new ArrayList<>(bomComponentIfaceList.size());
            List<AuditDomain> auditDomains = new ArrayList<>(bomComponentIfaceList.size());
            bomComponentIfaceList.stream().forEach(ifs -> {
                ifs.setStatus(status);
                ifs.setTenantId(tenantId);
            });
            auditDomains.addAll(bomComponentIfaceList);
            sqlList.addAll(constructSql(auditDomains));
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
        return bomComponentIfaceList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<MtBomComponentIface> updateIfaceStatus(Long tenantId, String status) {
        List<MtBomComponentIface> bomComponentIfaceList = mtBomComponentIfaceMapper.getUnprocessedList(tenantId);
        if (CollectionUtils.isNotEmpty(bomComponentIfaceList)) {
            List<String> sqlList = new ArrayList<>(bomComponentIfaceList.size());
            List<AuditDomain> auditDomains = new ArrayList<>(bomComponentIfaceList.size());
            bomComponentIfaceList.stream().forEach(ifs -> {
                ifs.setStatus(status);
                ifs.setTenantId(tenantId);
            });
            auditDomains.addAll(bomComponentIfaceList);
            sqlList.addAll(constructSql(auditDomains));
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
        return bomComponentIfaceList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void log(Long tenantId, List<AuditDomain> ifaceSqlList) {
        if (CollectionUtils.isNotEmpty(ifaceSqlList)) {
            List<String> sqlList = constructSql(ifaceSqlList);
            List<List<String>> commitSqlList = commitSqlList(sqlList, 10);
            for (List<String> commitSql : commitSqlList) {
                this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
            }
        }
    }

    @Override
    public List<Long> selectBatchId(Long tenantId) {
        return mtBomComponentIfaceMapper.selectBatchId(tenantId);
    }

    private static class Tuple {
        private String bomCode;
        private String bomObjectType;
        private String bomAlternative;

        public Tuple(String bomCode, String bomObjectType, String bomAlternative) {
            this.bomCode = bomCode;
            this.bomObjectType = bomObjectType;
            this.bomAlternative = bomAlternative;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Tuple tuple = (Tuple) o;

            if (bomCode != null ? !bomCode.equals(tuple.bomCode) : tuple.bomCode != null) {
                return false;
            }
            if (bomObjectType != null ? !bomObjectType.equals(tuple.bomObjectType) : tuple.bomObjectType != null) {
                return false;
            }
            return bomAlternative != null ? bomAlternative.equals(tuple.bomAlternative) : tuple.bomAlternative == null;
        }

        @Override
        public int hashCode() {
            int result = bomCode != null ? bomCode.hashCode() : 0;
            result = 31 * result + (bomObjectType != null ? bomObjectType.hashCode() : 0);
            result = 31 * result + (bomAlternative != null ? bomAlternative.hashCode() : 0);
            return result;
        }
    }

    private static class ItemPlant implements Serializable {
        private static final long serialVersionUID = -2958353779967068024L;
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

    private static class BomTuple implements Serializable {
        private static final long serialVersionUID = 2836064759329462110L;
        private String bomName;
        private String revision;
        private String bomType;

        public BomTuple(String bomName, String revision, String bomType) {
            this.bomName = bomName;
            this.revision = revision;
            this.bomType = bomType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            BomTuple bomTuple = (BomTuple) o;
            return Objects.equals(bomName, bomTuple.bomName) && Objects.equals(revision, bomTuple.revision)
                    && Objects.equals(bomType, bomTuple.bomType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(bomName, revision, bomType);
        }
    }

    private static class BomComponentTuple implements Serializable {
        private static final long serialVersionUID = -892544759584408160L;
        private String bomId;
        private String materialId;
        private String bomComponentType;
        private Long lineNumber;

        public BomComponentTuple(String bomId, String materialId, String bomComponentType, Long lineNumber) {
            this.bomId = bomId;
            this.materialId = materialId;
            this.bomComponentType = bomComponentType;
            this.lineNumber = lineNumber;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            BomComponentTuple that = (BomComponentTuple) o;
            return Objects.equals(bomId, that.bomId) && Objects.equals(materialId, that.materialId)
                    && Objects.equals(bomComponentType, that.bomComponentType)
                    && Objects.equals(lineNumber, that.lineNumber);
        }

        @Override
        public int hashCode() {
            return Objects.hash(bomId, materialId, bomComponentType, lineNumber);
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
