package tarzan.pull.infra.repository.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.CollectorsUtil;
import io.tarzan.common.domain.util.MtBaseConstants;
import tarzan.calendar.domain.repository.MtCalendarShiftRepository;
import tarzan.calendar.domain.vo.MtCalendarShiftVO13;
import tarzan.calendar.domain.vo.MtCalendarShiftVO14;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionVO;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtMaterialCategorySite;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.repository.MtMaterialCategoryAssignRepository;
import tarzan.material.domain.repository.MtMaterialCategorySiteRepository;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.material.domain.repository.MtPfepDistributionCategoryRepository;
import tarzan.material.domain.repository.MtPfepDistributionRepository;
import tarzan.material.domain.vo.MtMaterialCategoryAssignVO;
import tarzan.material.domain.vo.MtPfepDistributionVO2;
import tarzan.material.domain.vo.MtPfepDistributionVO3;
import tarzan.material.domain.vo.MtPfepInventoryCategoryVO3;
import tarzan.material.domain.vo.MtPfepInventoryCategoryVO4;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.repository.MtModAreaRepository;
import tarzan.pull.domain.entity.MtPullOnhandSnapshot;
import tarzan.pull.domain.repository.MtPullOnhandSnapshotRepository;
import tarzan.pull.domain.vo.MtPullOnhandSnapshotVO;
import tarzan.pull.domain.vo.MtPullOnhandSnapshotVO2;
import tarzan.pull.domain.vo.MtPullOnhandSnapshotVO3;
import tarzan.pull.domain.vo.MtPullOnhandSnapshotVO4;
import tarzan.pull.domain.vo.MtPullOnhandSnapshotVO5;
import tarzan.pull.domain.vo.MtPullOnhandSnapshotVO6;
import tarzan.pull.domain.vo.MtPullOnhandSnapshotVO7;
import tarzan.pull.domain.vo.MtPullOnhandSnapshotVO8;
import tarzan.pull.infra.mapper.MtPullOnhandSnapshotMapper;

/**
 * 拉动线边库存快照 资源库实现
 *
 * @author yiyang.xie 2020-02-04 15:53:01
 */
@Component
public class MtPullOnhandSnapshotRepositoryImpl extends BaseRepositoryImpl<MtPullOnhandSnapshot>
                implements MtPullOnhandSnapshotRepository {

    @Autowired
    private MtPullOnhandSnapshotMapper mtPullOnhandSnapshotMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModAreaRepository mtModAreaRepository;

    @Autowired
    private MtMaterialCategoryAssignRepository mtMaterialCategoryAssignRepository;

    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;

    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepository;

    @Autowired
    private MtPfepDistributionRepository mtPfepDistributionRepository;

    @Autowired
    private MtMaterialCategorySiteRepository mtMaterialCategorySiteRepository;

    @Autowired
    private MtPfepDistributionCategoryRepository mtPfepDistributionCategoryRepository;

    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;

    @Autowired
    private MtCalendarShiftRepository mtCalendarShiftRepository;

    @Autowired
    private MtInstructionRepository mtInstructionRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MtPullOnhandSnapshotVO2> onhandSnapShotCreate(Long tenantId, List<MtPullOnhandSnapshotVO> vo) {

        // 1.检验参数合格性
        if (CollectionUtils.isEmpty(vo)) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "onhandSnapShotList", "【API：onhandSnapShotCreate】"));
        }
        for (MtPullOnhandSnapshotVO snapshot : vo) {
            if (StringUtils.isEmpty(snapshot.getMaterialId())) {
                throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_INVENTORY_0001", "INVENTORY", "MaterialId", "【API：onhandSnapShotCreate】"));
            }

            if (StringUtils.isEmpty(snapshot.getSiteId())) {
                throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_INVENTORY_0001", "INVENTORY", "SiteId", "【API：onhandSnapShotCreate】"));
            }
            if (StringUtils.isEmpty(snapshot.getAreaId())) {
                throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_INVENTORY_0001", "INVENTORY", "AreaId", "【API：onhandSnapShotCreate】"));
            }

            if (StringUtils.isEmpty(snapshot.getLocatorId())) {
                throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_INVENTORY_0001", "INVENTORY", "LocatorId", "【API：onhandSnapShotCreate】"));
            }
            if (StringUtils.isEmpty(snapshot.getSnapshotRevision())) {
                throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_INVENTORY_0001", "INVENTORY", "SnapshotRevision", "【API：onhandSnapShotCreate】"));
            }
            if (snapshot.getCreatedBy() == null) {
                throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_INVENTORY_0001", "INVENTORY", "CreateBy", "【API：onhandSnapShotCreate】"));
            }

            MtModArea mtModArea = mtModAreaRepository.areaBasicPropertyGet(tenantId, snapshot.getAreaId());

            if (null == mtModArea) {
                throw new MtException("MT_INVENTORY_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_INVENTORY_0004", "INVENTORY", "【API：onhandSnapShotCreate】"));
            }
        }


        List<MtPullOnhandSnapshotVO2> resultList = new ArrayList<>();


        List<String> sqlList = new ArrayList<>();

        List<String> snapshotIds = customDbRepository.getNextKeys("mt_pull_onhand_snapshot_s", vo.size());
        List<String> snapshotCIds = customDbRepository.getNextKeys("mt_pull_onhand_snapshot_cid_s", vo.size());

        Date now = new Date();

        int index = 0;
        for (MtPullOnhandSnapshotVO snapshotVO : vo) {
            MtPullOnhandSnapshotVO2 result = new MtPullOnhandSnapshotVO2();

            MtInvOnhandQuantityVO quantityVO = new MtInvOnhandQuantityVO();
            quantityVO.setMaterialId(snapshotVO.getMaterialId());
            quantityVO.setSiteId(snapshotVO.getSiteId());
            quantityVO.setLocatorId(snapshotVO.getLocatorId());
            Double sumAvailableQty =
                            mtInvOnhandQuantityRepository.propertyLimitSumAvailableOnhandQtyGet(tenantId, quantityVO);

            MtPullOnhandSnapshot snapshot = new MtPullOnhandSnapshot();
            snapshot.setMaterialId(snapshotVO.getMaterialId());
            snapshot.setSiteId(snapshotVO.getSiteId());
            snapshot.setLocatorId(snapshotVO.getLocatorId());
            snapshot.setAreaId(snapshotVO.getAreaId());
            snapshot.setTenantId(tenantId);
            List<MtPullOnhandSnapshot> onhandSnapshots = mtPullOnhandSnapshotMapper.select(snapshot);

            for (MtPullOnhandSnapshot onhandSnapshot : onhandSnapshots) {
                if (!MtBaseConstants.NO.equals(onhandSnapshot.getLatestRevisionFlag())) {
                    onhandSnapshot.setLatestRevisionFlag(MtBaseConstants.NO);
                    // 更新
                    sqlList.addAll(customDbRepository.getUpdateSql(onhandSnapshot));
                }
            }

            snapshot = new MtPullOnhandSnapshot();
            snapshot.setOnhandQty(sumAvailableQty);
            snapshot.setLatestRevisionFlag(MtBaseConstants.YES);
            snapshot.setSnapshotRevision(snapshotVO.getSnapshotRevision());
            snapshot.setMaterialId(snapshotVO.getMaterialId());
            snapshot.setSiteId(snapshotVO.getSiteId());
            snapshot.setLocatorId(snapshotVO.getLocatorId());
            snapshot.setAreaId(snapshotVO.getAreaId());
            snapshot.setCreatedBy(snapshotVO.getCreatedBy());
            snapshot.setTenantId(tenantId);

            snapshot.setOnhandSnapshotId(snapshotIds.get(index));
            snapshot.setCid(Long.valueOf(snapshotCIds.get(index)));
            snapshot.setLastUpdatedBy(snapshotVO.getCreatedBy());
            snapshot.setLastUpdateDate(now);
            snapshot.setCreationDate(now);

            sqlList.addAll(customDbRepository.getInsertSql(snapshot));

            result.setOnhandSnapshotId(snapshot.getOnhandSnapshotId());
            result.setSnapshotRevision(snapshot.getSnapshotRevision());
            result.setAreaId(snapshot.getAreaId());
            result.setLocatorId(snapshot.getLocatorId());
            result.setMaterialId(snapshot.getMaterialId());
            result.setOnhandQty(snapshot.getOnhandQty());
            result.setSiteId(snapshot.getSiteId());
            resultList.add(result);
            index++;
        }

        // 批量执行
        if (CollectionUtils.isNotEmpty(sqlList)) {
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }

        return resultList;
    }


    @Override
    public Double distributionActualQtyCalculate(Long tenantId, MtPullOnhandSnapshotVO8 dto) {
        Double result = null;
        // 第一步，判断multiplesOfPackFlag整包配送标识
        if (dto.getDistributionQty() == null) {
            throw new MtException("MT_PULL_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_PULL_0012", "PULL", "distributionQty", "【API：distributionActualQtyCalculate】"));
        }
        if (StringUtils.isEmpty(dto.getMultiplesOfPackFlag())) {
            throw new MtException("MT_PULL_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_PULL_0012", "PULL", "multiplesOfPackFlag", "【API：distributionActualQtyCalculate】"));
        }
        if (dto.getPackQty() == null) {
            throw new MtException("MT_PULL_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_PULL_0012", "PULL", "packQty", "【API：distributionActualQtyCalculate】"));
        }

        // a)若为“Y”，则actualQty为distrubutionQty除以packQty所得的数量，不为整数则向上取整，再乘以packQty
        if (MtBaseConstants.YES.equals(dto.getMultiplesOfPackFlag())) {
            result = BigDecimal.valueOf(dto.getDistributionQty())
                            .divide(BigDecimal.valueOf(dto.getPackQty()), 0, BigDecimal.ROUND_UP)
                            .multiply(BigDecimal.valueOf(dto.getPackQty())).doubleValue();
        } else if (MtBaseConstants.NO.equals(dto.getMultiplesOfPackFlag())) {
            result = dto.getDistributionQty();
        }

        return result;
    }

    @Override
    public MtPullOnhandSnapshotVO4 inventoryWaveReplenishmentQtyCalculate(Long tenantId, MtPullOnhandSnapshotVO3 dto) {
        MtPullOnhandSnapshotVO4 result = new MtPullOnhandSnapshotVO4();
        // 第一步，判断参数合规性
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_PULL_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_PULL_0012", "PULL", "materialId", "【API：inventoryWaveReplenishmentQtyCalculate】"));
        }

        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_PULL_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_PULL_0012", "PULL", "siteId", "【API：inventoryWaveReplenishmentQtyCalculate】"));
        }
        if (dto.getDistributionCycle() == null) {
            throw new MtException("MT_PULL_0012",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_PULL_0012", "PULL",
                                            "distributionCycle", "【API：inventoryWaveReplenishmentQtyCalculate】"));
        }
        if (StringUtils.isEmpty(dto.getLocatorId())) {
            throw new MtException("MT_PULL_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_PULL_0012", "PULL", "locatorId", "【API：inventoryWaveReplenishmentQtyCalculate】"));
        }
        if (dto.getPullToArrive() == null) {
            throw new MtException("MT_PULL_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_PULL_0012", "PULL", "pullToArrive", "【API：inventoryWaveReplenishmentQtyCalculate】"));
        }
        if (StringUtils.isEmpty(dto.getSnapshotRevision())) {
            throw new MtException("MT_PULL_0012",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_PULL_0012", "PULL",
                                            "snapshotRevision", "【API：inventoryWaveReplenishmentQtyCalculate】"));
        }

        if (CollectionUtils.isEmpty(dto.getBufferInventory())) {
            throw new MtException("MT_PULL_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_PULL_0012", "PULL", "bufferInventory", "【API：inventoryWaveReplenishmentQtyCalculate】"));
        }
        if (CollectionUtils.isEmpty(dto.getMaterialConsumeRate())) {
            throw new MtException("MT_PULL_0012",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_PULL_0012", "PULL",
                                            "materialConsumeRate", "【API：inventoryWaveReplenishmentQtyCalculate】"));
        }
        // API{ relationshipLimitMaterialSiteGet }获取materialId和siteId

        // 第二步，传入参数snapshotRevision、locatorId、materialSiteId调用API{ pullOnhandSnapshotQuery }获取返回参数onhandQty
        MtPullOnhandSnapshotVO5 snapshotVO5 = new MtPullOnhandSnapshotVO5();
        snapshotVO5.setSnapshotRevision(dto.getSnapshotRevision());
        snapshotVO5.setLocatorId(dto.getLocatorId());
        snapshotVO5.setMaterialId(dto.getMaterialId());
        snapshotVO5.setSiteId(dto.getSiteId());
        List<MtPullOnhandSnapshotVO6> snapshots = pullOnhandSnapshotQuery(tenantId, snapshotVO5);
        if (CollectionUtils.isEmpty(snapshots)) {
            throw new MtException("MT_PULL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_PULL_0001", "PULL", "【API：inventoryWaveReplenishmentQtyCalculate】"));
        }
        Double onhandQty = snapshots.get(0).getOnhandQty();

        // 第三步，将onhandQty与P*∑@+a（max)进行比较
        // 计算P*∑@+a（max)
        BigDecimal sumRate = dto.getMaterialConsumeRate().stream().collect(
                        CollectorsUtil.summingBigDecimal(c -> c == null ? BigDecimal.ZERO : BigDecimal.valueOf(c)));
        // a（max)
        Double aMax = dto.getBufferInventory().stream().max(Comparator.comparing(BigDecimal::valueOf)).get();
        BigDecimal sumQty = BigDecimal.valueOf(dto.getPullToArrive()).multiply(sumRate).add(BigDecimal.valueOf(aMax));

        if (BigDecimal.valueOf(onhandQty).compareTo(sumQty) > 0) {
            result.setDistributionFlag(MtBaseConstants.NO);
        } else {
            result.setDistributionFlag(MtBaseConstants.YES);
            if (BigDecimal.valueOf(onhandQty).compareTo(BigDecimal.ZERO) == 0) {
                result.setDistributionQty(BigDecimal.valueOf(dto.getDistributionCycle()).multiply(sumRate)
                                .add(BigDecimal.valueOf(aMax)).doubleValue());
            } else {
                result.setDistributionQty(
                                BigDecimal.valueOf(dto.getDistributionCycle()).multiply(sumRate).doubleValue());
            }
        }
        return result;
    }


    @Override
    public List<MtPullOnhandSnapshotVO6> pullOnhandSnapshotQuery(Long tenantId, MtPullOnhandSnapshotVO5 dto) {

        // 第一步，判断参数合规性
        if (StringUtils.isEmpty(dto.getOnhandSnapshotId()) && StringUtils.isEmpty(dto.getMaterialId())
                        && StringUtils.isEmpty(dto.getAreaId()) && StringUtils.isEmpty(dto.getSiteId())
                        && StringUtils.isEmpty(dto.getLocatorId()) && StringUtils.isEmpty(dto.getSnapshotRevision())
                        && dto.getCreatedBy() == null && StringUtils.isEmpty(dto.getLatestRevisionFlag())) {
            throw new MtException("MT_INVENTORY_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0002", "INVENTORY", "【API：pullOnhandSnapshotQuery】"));
        }

        // 第二步，根据输入参数限制在表MT_PULL_ONHAND_SNAPSHOT中查询并返回所有查询结果
        return mtPullOnhandSnapshotMapper.pullOnhandSnapshotQuery(tenantId, dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void inventoryWaveReplenishmentPullProcess(Long tenantId, MtPullOnhandSnapshotVO7 dto) {

        List<String> materialIds = new ArrayList<>();

        // 第一步，判断参数合规性
        if (StringUtils.isEmpty(dto.getAreaId())) {
            throw new MtException("MT_PULL_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_PULL_0012", "PULL", "areaId", "【API：inventoryWaveReplenishmentPullProcess】"));
        }
        if (dto.getCreatedBy() == null) {
            throw new MtException("MT_PULL_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_PULL_0012", "PULL", "createdBy", "【API：inventoryWaveReplenishmentPullProcess】"));
        }

        // 调用API{ materialDistributionPfepQuery }匹配输入参数routeId获取改该配送路线下的所有物料配送属性
        MtPfepDistributionVO3 vo3 = new MtPfepDistributionVO3();
        vo3.setAreaId(dto.getAreaId());
        List<MtPfepDistributionVO2> distributions =
                        mtPfepDistributionRepository.materialDistributionPfepQuery(tenantId, vo3);


        // 传入参数areaId调用API{ materialCategoryDistributionPfepQuery }匹配输入参数routeId获取改该配送路线下的所有物料类别配送属性
        MtPfepInventoryCategoryVO3 categoryVO3 = new MtPfepInventoryCategoryVO3();
        categoryVO3.setAreaId(dto.getAreaId());
        List<MtPfepInventoryCategoryVO4> categoryDistributions = mtPfepDistributionCategoryRepository
                        .materialCategoryDistributionPfepQuery(tenantId, categoryVO3);

        if (CollectionUtils.isEmpty(distributions) && CollectionUtils.isEmpty(categoryDistributions)) {
            throw new MtException("MT_PULL_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_PULL_0002", "PULL", "areaId", "【API：inventoryWaveReplenishmentPullProcess】"));
        }
        List<MtMaterialSite> mtMaterialSites = new ArrayList<>();

        Map<String, MtMaterialSite> materialSiteMap = new HashMap<>();

        List<MtMaterialCategorySite> mtMaterialCategorySites = new ArrayList<>();

        Map<String, MtMaterialCategorySite> categorySiteMap = new HashMap<>();

        if (CollectionUtils.isNotEmpty(distributions)) {
            // 获取到后判断distributionMode配送模式是否为“INVENTORY_WAVE”
            if (distributions.stream().anyMatch(t -> !"INVENTORY_WAVE".equals(t.getDistributionMode()))) {
                throw new MtException("MT_PULL_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_PULL_0003", "PULL", "areaId", "【API：inventoryWaveReplenishmentPullProcess】"));
            }


            mtMaterialSites = distributions.stream().map(t -> {
                MtMaterialSite mtMaterialSite =
                                mtMaterialSiteRepository.relationLimitMaterialSiteGet(tenantId, t.getMaterialSiteId());
                return mtMaterialSite;
            }).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(mtMaterialSites)) {
                materialSiteMap = mtMaterialSites.stream()
                                .collect(Collectors.toMap(MtMaterialSite::getMaterialSiteId, t -> t));
            }

        }

        if (CollectionUtils.isNotEmpty(categoryDistributions)) {
            // 获取到后判断distributionMode配送模式是否为“INVENTORY_WAVE”
            if (categoryDistributions.stream().anyMatch(t -> !"INVENTORY_WAVE".equals(t.getDistributionMode()))) {
                throw new MtException("MT_PULL_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_PULL_0003", "PULL", "areaId", "【API：inventoryWaveReplenishmentPullProcess】"));
            }

            mtMaterialCategorySites = categoryDistributions.stream().map(t -> {
                MtMaterialCategorySite mtMaterialCategorySite = mtMaterialCategorySiteRepository
                                .relationLimitMaterialCategorySiteGet(tenantId, t.getMaterialCategorySiteId());
                return mtMaterialCategorySite;
            }).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(mtMaterialCategorySites)) {
                categorySiteMap = mtMaterialCategorySites.stream()
                                .collect(Collectors.toMap(MtMaterialCategorySite::getMaterialCategorySiteId, t -> t));
            }
        }

        if (CollectionUtils.isNotEmpty(mtMaterialSites) && CollectionUtils.isNotEmpty(mtMaterialCategorySites)) {
            for (MtMaterialSite mtMaterialSite : mtMaterialSites) {
                for (MtMaterialCategorySite mtMaterialCategorySite : mtMaterialCategorySites) {
                    if (mtMaterialSite.getSiteId().equals(mtMaterialCategorySite.getSiteId())) {
                        MtMaterialCategoryAssignVO assignVO = new MtMaterialCategoryAssignVO();
                        assignVO.setSiteId(mtMaterialSite.getSiteId());
                        assignVO.setMaterialId(mtMaterialSite.getMaterialId());
                        assignVO.setMaterialCategoryId(mtMaterialCategorySite.getMaterialCategoryId());
                        String validate = mtMaterialCategoryAssignRepository.materialCategoryAssignValidate(tenantId,
                                        assignVO);
                        if (MtBaseConstants.YES.equals(validate)) {
                            throw new MtException("MT_PULL_0004", mtErrorMessageRepository.getErrorMessageWithModule(
                                            tenantId, "MT_PULL_0004", "PULL", mtMaterialSite.getMaterialId(),
                                            mtMaterialSite.getSiteId(), mtMaterialCategorySite.getMaterialCategoryId(),
                                            "【API：inventoryWaveReplenishmentPullProcess】"));
                        }
                    }
                }
            }
        }

        // 第二步，数据处理。将根据物料类别获取的物料类别配送属性转为物料配送属性，以保证格式统一
        // 当前时间
        final Date currentDate = new Date(System.currentTimeMillis());
        // 调用API{ eventRequestCreate}传入
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "PULL_DIS_INSTRUCTION_CREATE");

        // 传入组织类型organizationType=“AREA”，organizationId=“areaId”，date=“currentDate”调用API{
        // dateLimitcalendarShiftGet}
        MtCalendarShiftVO13 mtCalendarShift = new MtCalendarShiftVO13();
        mtCalendarShift.setOrganizationId(dto.getAreaId());
        mtCalendarShift.setOrganizationType("AREA");
        mtCalendarShift.setDate(currentDate);
        List<MtCalendarShiftVO14> mtCalendarShifts =
                        mtCalendarShiftRepository.dateLimitCalendarShiftGet(tenantId, mtCalendarShift);
        // 确认只有一条
        MtCalendarShiftVO14 shift = new MtCalendarShiftVO14();
        if (CollectionUtils.isNotEmpty(mtCalendarShifts)) {
            shift = mtCalendarShifts.get(0);
        }


        // 第三步，创建库存快照
        // 对于获取的是物料类别存储属性则传入materialCategoryId 和siteId调用{ categorySiteLimitMaterialQuery }获取该站点该物料类别下
        List<MtPullOnhandSnapshotVO> snapshotVOList = new ArrayList<>();

        Map<MtPullOnhandSnapshotVO, MtPfepDistributionVO2> pullMap = new HashMap<>();

        for (MtPfepDistributionVO2 distribution : distributions) {
            MtMaterialSite materialSite = materialSiteMap.get(distribution.getMaterialSiteId());
            MtPullOnhandSnapshotVO snapshotVO = new MtPullOnhandSnapshotVO();

            if (materialSite != null) {
                materialIds.add(materialSite.getMaterialId());
                snapshotVO.setMaterialId(materialSite.getMaterialId());
                snapshotVO.setSiteId(materialSite.getSiteId());

                snapshotVO.setAreaId(distribution.getAreaId());
                snapshotVO.setLocatorId(distribution.getLocatorId());
                snapshotVO.setCreatedBy(dto.getCreatedBy());
                snapshotVO.setSnapshotRevision(
                                distribution.getAreaId() + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

                pullMap.put(snapshotVO, distribution);

                snapshotVOList.add(snapshotVO);
            }
        }

        for (MtPfepInventoryCategoryVO4 categoryDistribution : categoryDistributions) {

            MtPfepDistributionVO2 distribution = new MtPfepDistributionVO2();
            distribution.setOrganizationType(categoryDistribution.getOrganizationType());
            distribution.setOrganizationId(categoryDistribution.getOrganizationId());
            distribution.setAreaId(categoryDistribution.getAreaId());
            distribution.setLocatorId(categoryDistribution.getLocatorId());
            distribution.setLocatorCapacity(categoryDistribution.getLocatorCapacity());
            distribution.setFromScheduleRateFlag(categoryDistribution.getFromScheduleRateFlag());
            distribution.setMaterialConsumeRateUomId(categoryDistribution.getMaterialConsumeRateUomId());
            distribution.setMaterialConsumeRate(categoryDistribution.getMaterialConsumeRate());
            distribution.setBufferInventory(categoryDistribution.getBufferInventory());
            distribution.setBufferPeriod(categoryDistribution.getBufferPeriod());
            distribution.setMinInventory(categoryDistribution.getMinInventory());
            distribution.setMaxInventory(categoryDistribution.getMaxInventory());
            distribution.setPackQty(categoryDistribution.getPackQty());
            distribution.setMultiplesOfPackFlag(categoryDistribution.getMultiplesOfPackFlag());
            distribution.setRouteLocatorId(categoryDistribution.getRouteLocatorId());
            distribution.setDistributionMode(categoryDistribution.getDistributionMode());
            distribution.setPullTimeIntervalFlag(categoryDistribution.getPullTimeIntervalFlag());
            distribution.setDistributionCycle(categoryDistribution.getDistributionCycle());
            distribution.setBusinessType(categoryDistribution.getBusinessType());
            distribution.setInstructCreatedByEo(categoryDistribution.getInstructCreatedByEo());
            distribution.setSourceLocatorId(categoryDistribution.getSourceLocatorId());
            distribution.setSequence(categoryDistribution.getSequence());
            distribution.setPullToArrive(categoryDistribution.getPullToArrive());

            MtMaterialCategorySite categorySite = categorySiteMap.get(categoryDistribution.getMaterialCategorySiteId());

            MtMaterialCategoryAssignVO assignVO = new MtMaterialCategoryAssignVO();
            String siteId = null;
            if (categorySite != null) {
                assignVO.setMaterialCategoryId(categorySite.getMaterialCategoryId());
                assignVO.setSiteId(categorySite.getSiteId());
                siteId = categorySite.getSiteId();
            }
            List<String> materialList =
                            mtMaterialCategoryAssignRepository.categorySiteLimitMaterialQuery(tenantId, assignVO);
            materialList = materialList.stream().filter(t -> !materialIds.contains(t)).collect(Collectors.toList());
            for (String materialId : materialList) {
                MtPullOnhandSnapshotVO snapshotVO = new MtPullOnhandSnapshotVO();
                snapshotVO.setMaterialId(materialId);
                snapshotVO.setSiteId(siteId);
                snapshotVO.setAreaId(categoryDistribution.getAreaId());
                snapshotVO.setLocatorId(categoryDistribution.getLocatorId());
                snapshotVO.setCreatedBy(dto.getCreatedBy());
                snapshotVO.setSnapshotRevision(categoryDistribution.getAreaId()
                                + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
                pullMap.put(snapshotVO, distribution);
                snapshotVOList.add(snapshotVO);
            }
        }

        // 创建库存快照
        List<MtPullOnhandSnapshotVO2> snapshotVO2s = onhandSnapShotCreate(tenantId, snapshotVOList);


        for (MtPullOnhandSnapshotVO2 snapshot : snapshotVO2s) {

            MtPullOnhandSnapshotVO snapshotVO = new MtPullOnhandSnapshotVO();
            snapshotVO.setMaterialId(snapshot.getMaterialId());
            snapshotVO.setSiteId(snapshot.getSiteId());
            snapshotVO.setAreaId(snapshot.getAreaId());
            snapshotVO.setLocatorId(snapshot.getLocatorId());
            snapshotVO.setSnapshotRevision(snapshot.getSnapshotRevision());

            if (snapshot.getOnhandQty() == null) {
                break;
            }

            MtPfepDistributionVO2 distribution = pullMap.get(snapshotVO);

            // 调用API{ inventoryWaveReplenishmentQtyCalculate}获取distrubutionFlag，distrubutionQty

            MtPullOnhandSnapshotVO3 snapshotVO3 = new MtPullOnhandSnapshotVO3();
            snapshotVO3.setMaterialId(snapshot.getMaterialId());
            snapshotVO3.setSiteId(snapshot.getSiteId());
            snapshotVO3.setDistributionCycle(distribution.getDistributionCycle());
            snapshotVO3.setLocatorId(snapshot.getLocatorId());
            snapshotVO3.setSnapshotRevision(snapshot.getSnapshotRevision());
            snapshotVO3.setPullToArrive(distribution.getPullToArrive());
            snapshotVO3.setMaterialConsumeRate(Arrays.asList(distribution.getMaterialConsumeRate()));
            snapshotVO3.setBufferInventory(Arrays.asList(distribution.getBufferInventory()));

            MtPullOnhandSnapshotVO4 calculate = inventoryWaveReplenishmentQtyCalculate(tenantId, snapshotVO3);

            if (MtBaseConstants.NO.equals(calculate.getDistributionFlag())) {
                continue;
            }

            if (MtBaseConstants.YES.equals(calculate.getDistributionFlag())) {
                // API{ distributionActualQtyCalculate }获取实际配送数actualQty
                MtPullOnhandSnapshotVO8 vo8 = new MtPullOnhandSnapshotVO8();
                vo8.setDistributionQty(calculate.getDistributionQty());
                vo8.setMultiplesOfPackFlag(distribution.getMultiplesOfPackFlag());
                vo8.setPackQty(distribution.getPackQty());
                Double actualQty = distributionActualQtyCalculate(tenantId, vo8);

                // 调用API{ instructionUpdate }指令创建
                MtInstructionVO mtInstructionVO = new MtInstructionVO();
                mtInstructionVO.setInstructionType("RECEIVE_FROM_COSTCENTER");
                mtInstructionVO.setSiteId(snapshot.getSiteId());
                mtInstructionVO.setMaterialId(snapshot.getMaterialId());
                mtInstructionVO.setFromSiteId(snapshot.getSiteId());
                mtInstructionVO.setFromLocatorId(distribution.getSourceLocatorId());
                mtInstructionVO.setToLocatorId(distribution.getLocatorId());
                mtInstructionVO.setQuantity(actualQty);

                // 校验materialId有效性
                MtMaterial mtMaterial = mtMaterialRepository.materialPropertyGet(tenantId, snapshot.getMaterialId());
                if (mtMaterial != null) {
                    mtInstructionVO.setUomId(mtMaterial.getPrimaryUomId());
                }

                mtInstructionVO.setDisRouterId(dto.getAreaId());
                mtInstructionVO.setShiftCode(shift.getShiftCode());
                mtInstructionVO.setShiftDate(shift.getShiftDate());
                mtInstructionVO.setEventRequestId(eventRequestId);
                mtInstructionVO.setInstructionType("TRANSFER_OVER_LOCATOR");
                mtInstructionVO.setNumIncomingValueList(dto.getNumIncomingValueList());

                //String instructionId = mtInstructionRepository.instructionUpdate(tenantId, mtInstructionVO, "N")
                 //               .getInstructionId();
                // 指令下达
                //mtInstructionRepository.instructionRelease(tenantId, instructionId, eventRequestId);
            }
        }
    }
}
