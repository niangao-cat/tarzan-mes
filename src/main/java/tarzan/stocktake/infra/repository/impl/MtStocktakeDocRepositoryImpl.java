package tarzan.stocktake.infra.repository.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenStatusRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtSqlHelper;
import io.tarzan.common.domain.vo.MtGenStatusVO2;
import io.tarzan.common.domain.vo.MtNumrangeVO2;
import io.tarzan.common.domain.vo.MtNumrangeVO5;
import tarzan.actual.domain.entity.MtStocktakeActual;
import tarzan.actual.domain.repository.MtStocktakeActualRepository;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtContainerVO;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.vo.MtModLocatorVO9;
import tarzan.stocktake.domain.entity.MtStocktakeDoc;
import tarzan.stocktake.domain.entity.MtStocktakeDocHis;
import tarzan.stocktake.domain.entity.MtStocktakeRange;
import tarzan.stocktake.domain.repository.MtStocktakeDocHisRepository;
import tarzan.stocktake.domain.repository.MtStocktakeDocRepository;
import tarzan.stocktake.domain.repository.MtStocktakeRangeRepository;
import tarzan.stocktake.domain.vo.*;
import tarzan.stocktake.infra.mapper.MtStocktakeDocMapper;

/**
 * 盘点单据头表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:35:24
 */
@Component
public class MtStocktakeDocRepositoryImpl extends BaseRepositoryImpl<MtStocktakeDoc>
                implements MtStocktakeDocRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtGenStatusRepository mtGenStatusRepository;

    @Autowired
    private CustomSequence customSequence;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtContainerRepository mtContainerRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtStocktakeActualRepository mtStocktakeActualRepository;

    @Autowired
    private MtStocktakeRangeRepository mtStocktakeRangeRepository;

    @Autowired
    private MtStocktakeDocHisRepository mtStocktakeDocHisRepository;

    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;

    @Autowired
    private MtStocktakeDocMapper mtStocktakeDocMapper;

    @Override
    public List<String> propertyLimitStocktakeDocQuery(Long tenantId, MtStocktakeDocVO dto) {
        return mtStocktakeDocMapper.selectStocktakeDocByStocktakeIdList(tenantId, dto);
    }

    @Override
    public MtStocktakeDoc stocktakeDocPropertyGet(Long tenantId, String stocktakeId) {
        if (StringUtils.isEmpty(stocktakeId)) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "stocktakeId", "【API：stocktakeDocPropertyGet】"));

        }

        MtStocktakeDoc mtStocktakeDoc = new MtStocktakeDoc();
        mtStocktakeDoc.setTenantId(tenantId);
        mtStocktakeDoc.setStocktakeId(stocktakeId);
        return mtStocktakeDocMapper.selectOne(mtStocktakeDoc);
    }

    @Override
    public List<MtStocktakeDoc> stocktakeDocPropertyBatchGet(Long tenantId, List<String> stocktakeIdList) {
        if (CollectionUtils.isEmpty(stocktakeIdList)) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "stocktakeIdList", "【API：stocktakeDocPropertyBatchGet】"));
        }

        return mtStocktakeDocMapper.selectByIdList(tenantId, stocktakeIdList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String stocktakeDocCreate(Long tenantId, MtStocktakeDocVO1 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "siteId", "【API：stocktakeDocCreate】"));
        }
        if (StringUtils.isEmpty(dto.getAreaLocatorId())) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "areaLocatorId", "【API：stocktakeDocCreate】"));
        }
        if (StringUtils.isEmpty(dto.getOpenFlag())) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "openFlag", "【API：stocktakeDocCreate】"));
        }
        if (StringUtils.isEmpty(dto.getAdjustTimelyFlag())) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "adjustTimelyFlag", "【API：stocktakeDocCreate】"));
        }

        // 1.1. 验证区域货位是否为 AREA 类型
        MtModLocator mtModLocator = mtModLocatorRepository.locatorBasicPropertyGet(tenantId, dto.getAreaLocatorId());
        if (mtModLocator == null || !"AREA".equals(mtModLocator.getLocatorCategory())) {
            throw new MtException("MT_STOCKTAKE_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0002", "STOCKTAKE", "【API：stocktakeDocCreate】"));
        }

        // 1.2. 验证区域货位下的货位，是否有 INVENTORY 类型的货位
        MtModLocatorVO9 mtModLocatorVO9 = new MtModLocatorVO9();
        mtModLocatorVO9.setLocatorId(dto.getAreaLocatorId());
        mtModLocatorVO9.setQueryType("ALL");
        List<String> subLocatorIds = mtModLocatorRepository.subLocatorQuery(tenantId, mtModLocatorVO9);

        List<MtModLocator> subLocators = mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, subLocatorIds);
        if (CollectionUtils.isEmpty(subLocators)) {
            throw new MtException("MT_STOCKTAKE_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0005", "STOCKTAKE", dto.getAreaLocatorId(), "【API：stocktakeDocCreate】"));
        }

        // 筛选区域子库存中为库存类型的库位
        List<MtModLocator> subInventoryLocator = subLocators.stream()
                        .filter(t -> "INVENTORY".equals(t.getLocatorCategory())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(subInventoryLocator)) {
            throw new MtException("MT_STOCKTAKE_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0005", "STOCKTAKE", dto.getAreaLocatorId(), "【API：stocktakeDocCreate】"));
        }

        // 记录inventory类型的库位，用于下面逻辑判断
        List<String> adjustLocatIds;

        // 1.3. 验证货位是否均为库存类型的货位
        if (CollectionUtils.isNotEmpty(dto.getLocatorIdList())) {
            List<MtModLocator> locators =
                            mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, dto.getLocatorIdList());
            if (CollectionUtils.isEmpty(locators)) {
                throw new MtException("MT_STOCKTAKE_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_STOCKTAKE_0005", "STOCKTAKE", dto.getAreaLocatorId(), "【API：stocktakeDocCreate】"));
            }

            List<String> unInventoryLocatorIds = new ArrayList<>();
            for (MtModLocator locator : locators) {
                if (!"INVENTORY".equals(locator.getLocatorCategory())) {
                    unInventoryLocatorIds.add(locator.getLocatorId());
                }
            }
            if (CollectionUtils.isNotEmpty(unInventoryLocatorIds)) {
                throw new MtException("MT_STOCKTAKE_0003",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0003",
                                                "STOCKTAKE", unInventoryLocatorIds.toString(),
                                                "【API：stocktakeDocCreate】"));
            }

            adjustLocatIds = dto.getLocatorIdList();
        } else {
            adjustLocatIds = subInventoryLocator.stream().map(MtModLocator::getLocatorId).collect(Collectors.toList());
        }

        boolean materialIdListIsEmpty = CollectionUtils.isEmpty(dto.getMaterialIdList());

        // 1.4. 判断是否已存在当前盘点范围下的盘点单
        MtStocktakeDocVO mtStocktakeDocVO = new MtStocktakeDocVO();
        mtStocktakeDocVO.setAreaLocatorId(dto.getAreaLocatorId());
        mtStocktakeDocVO.setStocktakeStatusList(
                        Arrays.asList("NEW", "RELEASED", "FIRSTCOUNTCOMPLETED", "COUNTCOMPLETED"));
        List<String> stocktakeIds = propertyLimitStocktakeDocQuery(tenantId, mtStocktakeDocVO);

        if (CollectionUtils.isNotEmpty(stocktakeIds)) {
            // 验证同一物料，同一货位是否已经存在某个盘点单下
            // 如果存在则报错
            for (String stocktakeId : stocktakeIds) {
                MtStocktakeRange mtStocktakeRange = new MtStocktakeRange();
                mtStocktakeRange.setStocktakeId(stocktakeId);
                List<MtStocktakeRange> stocktakeRanges =
                                mtStocktakeRangeRepository.stocktakeRangeQuery(tenantId, mtStocktakeRange);

                if (CollectionUtils.isNotEmpty(stocktakeRanges)) {
                    // 筛选盘点范围对象类型为LOCATOR的数据，同时按对象ID汇总
                    Map<String, List<MtStocktakeRange>> stocktakeRangeLocatorMap =
                                    stocktakeRanges.stream().filter(t -> "LOCATOR".equals(t.getRangeObjectType()))
                                                    .collect(Collectors.groupingBy(t -> t.getRangeObjectId()));

                    // 筛选盘点范围对象类型为LOCATOR的数据，同时按对象ID汇总
                    Map<String, List<MtStocktakeRange>> stocktakeRangeMaterialMap =
                                    stocktakeRanges.stream().filter(t -> "MATERIAL".equals(t.getRangeObjectType()))
                                                    .collect(Collectors.groupingBy(t -> t.getRangeObjectId()));

                    if (materialIdListIsEmpty) {
                        // 如果传入对象物料ID列表为空，则只需校验locatorId是否已经存在
                        adjustLocatIds.forEach(locatorId -> {
                            if (CollectionUtils.isNotEmpty(stocktakeRangeLocatorMap.get(locatorId))) {
                                throw new MtException("MT_STOCKTAKE_0006",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_STOCKTAKE_0006", "STOCKTAKE", this.selectByPrimaryKey(stocktakeId).getStocktakeNum(),
                                                        "【API：stocktakeDocCreate】"));
                            }
                        });

                    } else {
                        // 先限制locator，筛选所有需要判断的locatorId对应盘点范围
                        List<MtStocktakeRange> stocktakeRangeLocators = new ArrayList<>();
                        adjustLocatIds.forEach(locatorId -> {
                            List<MtStocktakeRange> stocktakeRangesCurrent = stocktakeRangeLocatorMap.get(locatorId);
                            if (CollectionUtils.isNotEmpty(stocktakeRangesCurrent)) {
                                stocktakeRangeLocators.addAll(stocktakeRangesCurrent);
                            }
                        });

                        // 再判断这些盘点范围中，是否有输入的materiId，有则报错
                        if (CollectionUtils.isNotEmpty(stocktakeRangeLocators)) {
                            // 如果已经有locator存在，因为此时传入了物料，所以已存在的locator也必须有物料，否则报错
                            if (MapUtils.isEmpty(stocktakeRangeMaterialMap)) {
                                throw new MtException("MT_STOCKTAKE_0006",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_STOCKTAKE_0006", "STOCKTAKE", this.selectByPrimaryKey(stocktakeId).getStocktakeNum(),
                                                        "【API：stocktakeDocCreate】"));
                            }

                            dto.getMaterialIdList().forEach(materialId -> {
                                if (CollectionUtils.isNotEmpty(stocktakeRangeMaterialMap.get(materialId))) {
                                    throw new MtException("MT_STOCKTAKE_0006",
                                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                            "MT_STOCKTAKE_0006", "STOCKTAKE", this.selectByPrimaryKey(stocktakeId).getStocktakeNum(),
                                                            "【API：stocktakeDocCreate】"));
                                }
                            });
                        }

                    }
                }
            }
        }

        // 1.5. 判断是否已存在输入 stocktakeNum 的盘点单
        String stocktakeNum = dto.getStocktakeNum();
        if (StringUtils.isNotEmpty(stocktakeNum)) {
            mtStocktakeDocVO = new MtStocktakeDocVO();
            mtStocktakeDocVO.setStocktakeNum(stocktakeNum);
            stocktakeIds = propertyLimitStocktakeDocQuery(tenantId, mtStocktakeDocVO);
            if (CollectionUtils.isNotEmpty(stocktakeIds)) {
                throw new MtException("MT_STOCKTAKE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_STOCKTAKE_0004", "STOCKTAKE", stocktakeNum, "【API：stocktakeDocCreate】"));
            }
        } else {
            MtNumrangeVO2 createNum = constructNumRange(dto, "STOCKTAKE_NUM", dto.getNumObjectTypeCode(),
                            dto.getNumCallObjectCodeList(), dto.getNumIncomingValueList(), dto.getStocktakeNum());
            MtNumrangeVO5 generateNum = mtNumrangeRepository.numrangeGenerate(tenantId, createNum);
            stocktakeNum = generateNum.getNumber();
        }

        // 1.6. 判断是否已存在输入 identification 的盘点单
        String identification = dto.getIdentification();
        if (StringUtils.isNotEmpty(identification)) {
            mtStocktakeDocVO = new MtStocktakeDocVO();
            mtStocktakeDocVO.setIdentification(identification);
            stocktakeIds = propertyLimitStocktakeDocQuery(tenantId, mtStocktakeDocVO);
            if (CollectionUtils.isNotEmpty(stocktakeIds)) {
                throw new MtException("MT_STOCKTAKE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_STOCKTAKE_0004", "STOCKTAKE", identification, "【API：stocktakeDocCreate】"));
            }
        } else {
            identification = stocktakeNum;
        }

        MtStocktakeDoc queryStocktakeDoc = new MtStocktakeDoc();
        queryStocktakeDoc.setStocktakeNum(stocktakeNum);
        queryStocktakeDoc.setIdentification(identification);
        List<MtStocktakeDoc> originDocList = mtStocktakeDocMapper.selectByUnique(tenantId, queryStocktakeDoc);
        if (CollectionUtils.isNotEmpty(originDocList)) {
            final String checkNum = queryStocktakeDoc.getStocktakeNum();
            String errorFieldName = originDocList.stream().anyMatch(o -> checkNum.equals(o.getStocktakeNum()))
                            ? "stocktakeNum: " + queryStocktakeDoc.getStocktakeNum()
                            : "identification: " + queryStocktakeDoc.getIdentification();
            throw new MtException("MT_STOCKTAKE_0030", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0030", "STOCKTAKE", errorFieldName, "【API:stocktakeDocCreate】"));
        }

        List<String> sqlList = new ArrayList<>();
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date now = new Date();

        // 2. 在表MT_STOCKTAKE_DOC中新增一条数据
        MtStocktakeDoc newStocktakeDoc = new MtStocktakeDoc();
        newStocktakeDoc.setTenantId(tenantId);
        String newStocktakeId = this.customSequence.getNextKey("mt_stocktake_doc_s");
        newStocktakeDoc.setStocktakeId(newStocktakeId);
        newStocktakeDoc.setStocktakeNum(stocktakeNum);
        newStocktakeDoc.setStocktakeStatus("NEW");
        newStocktakeDoc.setStocktakeLastStatus("");
        newStocktakeDoc.setSiteId(dto.getSiteId());
        newStocktakeDoc.setAreaLocatorId(dto.getAreaLocatorId());
        newStocktakeDoc.setOpenFlag(dto.getOpenFlag());
        newStocktakeDoc.setMaterialRangeFlag(materialIdListIsEmpty ? "N" : "Y");
        newStocktakeDoc.setAdjustTimelyFlag(dto.getAdjustTimelyFlag());
        newStocktakeDoc.setMaterialLotLockFlag("");
        newStocktakeDoc.setIdentification(identification);
        newStocktakeDoc.setRemark(dto.getRemarks());
        newStocktakeDoc.setCid(Long.valueOf(this.customSequence.getNextKey("mt_stocktake_doc_cid_s")));
        newStocktakeDoc.setCreatedBy(userId);
        newStocktakeDoc.setCreationDate(now);
        newStocktakeDoc.setLastUpdatedBy(userId);
        newStocktakeDoc.setLastUpdateDate(now);
        newStocktakeDoc.setObjectVersionNumber(1L);
        sqlList.addAll(MtSqlHelper.getInsertSql(newStocktakeDoc));

        // 3. 获取盘点单据更新事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setEventTypeCode("STOCKTAKE_DOC_CREATE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 4. 记录历史，在历史表MT_STOCKTAKE_DOC_HIS中记录
        MtStocktakeDocHis newStocktakeDocHis = new MtStocktakeDocHis();
        BeanUtils.copyProperties(newStocktakeDoc, newStocktakeDocHis);
        newStocktakeDocHis.setTenantId(tenantId);
        newStocktakeDocHis.setStocktakeHisId(this.customSequence.getNextKey("mt_stocktake_doc_his_s"));
        newStocktakeDocHis.setCid(Long.valueOf(this.customSequence.getNextKey("mt_stocktake_doc_his_cid_s")));
        newStocktakeDocHis.setEventId(eventId);
        sqlList.addAll(MtSqlHelper.getInsertSql(newStocktakeDocHis));

        // 5. 新增盘点范围表MT_STOCKTAKE_RANGE数据
        adjustLocatIds.forEach(locatorId -> {
            MtStocktakeRange newStocktakeRange = new MtStocktakeRange();
            newStocktakeRange.setTenantId(tenantId);
            newStocktakeRange.setStocktakeRangeId(this.customSequence.getNextKey("mt_stocktake_range_s"));
            newStocktakeRange.setStocktakeId(newStocktakeId);
            newStocktakeRange.setCid(Long.valueOf(this.customSequence.getNextKey("mt_stocktake_range_cid_s")));
            newStocktakeRange.setRangeObjectId(locatorId);
            newStocktakeRange.setRangeObjectType("LOCATOR");
            newStocktakeRange.setCreatedBy(userId);
            newStocktakeRange.setCreationDate(now);
            newStocktakeRange.setLastUpdateDate(now);
            newStocktakeRange.setLastUpdatedBy(userId);
            sqlList.addAll(MtSqlHelper.getInsertSql(newStocktakeRange));
        });

        if (!materialIdListIsEmpty) {
            dto.getMaterialIdList().forEach(materialId -> {
                MtStocktakeRange newStocktakeRange = new MtStocktakeRange();
                newStocktakeRange.setTenantId(tenantId);
                newStocktakeRange.setStocktakeRangeId(this.customSequence.getNextKey("mt_stocktake_range_s"));
                newStocktakeRange.setStocktakeId(newStocktakeId);
                newStocktakeRange.setCid(Long.valueOf(this.customSequence.getNextKey("mt_stocktake_range_cid_s")));
                newStocktakeRange.setRangeObjectId(materialId);
                newStocktakeRange.setRangeObjectType("MATERIAL");
                newStocktakeRange.setCreatedBy(userId);
                newStocktakeRange.setCreationDate(now);
                newStocktakeRange.setLastUpdateDate(now);
                newStocktakeRange.setLastUpdatedBy(userId);
                sqlList.addAll(MtSqlHelper.getInsertSql(newStocktakeRange));
            });
        }

        // 执行批量新增
        this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));

        return newStocktakeId;
    }

    /**
     * 构建号码段对象
     *
     * @author benjamin
     * @date 2019-07-18 12:42
     * @param createVO MtStocktakeDocVO1
     * @param objectCode 对象编码
     * @param objectTypeCode 对象类型编码
     * @param callObjectCodeList 标识传入参数列表
     * @param incomingValueList 标识参数列表
     * @param outsideNum 原有值
     * @return MtNumrangeVO2
     */
    private MtNumrangeVO2 constructNumRange(MtStocktakeDocVO1 createVO, String objectCode, String objectTypeCode,
                    Map<String, String> callObjectCodeList, List<String> incomingValueList, String outsideNum) {
        MtNumrangeVO2 createNum = new MtNumrangeVO2();
        createNum.setObjectCode(objectCode);
        createNum.setObjectTypeCode(StringUtils.isEmpty(objectTypeCode) ? null : objectTypeCode);
        createNum.setSiteId(createVO.getSiteId());
        if (StringUtils.isNotEmpty(outsideNum)) {
            createNum.setOutsideNum(outsideNum);
        } else {
            createNum.setCallObjectCodeList(
                            callObjectCodeList == null || callObjectCodeList.size() == 0 ? null : callObjectCodeList);
            createNum.setIncomingValueList(CollectionUtils.isEmpty(incomingValueList) ? null : incomingValueList);
        }
        return createNum;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void stocktakeDocUpdate(Long tenantId, MtStocktakeDocVO2 dto) {
        // 1.判断输入参数是否合规
        if (StringUtils.isEmpty(dto.getStocktakeId())) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "stocktakeId", "【API：stocktakeDocUpdate】"));

        }
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "eventId", "【API：stocktakeDocUpdate】"));

        }
        // 获取状态组
        MtGenStatusVO2 mtGenStatusVO2 = new MtGenStatusVO2();
        mtGenStatusVO2.setModule("STOCKTAKE");
        mtGenStatusVO2.setStatusGroup("STOCKTAKE_STATUS");
        List<MtGenStatus> mtGenStatuses = mtGenStatusRepository.groupLimitStatusQuery(tenantId, mtGenStatusVO2);
        if (CollectionUtils.isNotEmpty(mtGenStatuses)) {
            // 1-b
            List<String> statuses = mtGenStatuses.stream().map(t -> t.getStatusCode()).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(dto.getStocktakeStatus()) && !statuses.contains(dto.getStocktakeStatus())) {
                throw new MtException("MT_STOCKTAKE_0008",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0008",
                                                "STOCKTAKE", "stocktakeStatus", statuses.toString(),
                                                "【API：stocktakeDocUpdate】"));

            }
            // 1-c
            if (StringUtils.isNotEmpty(dto.getStocktakeLastStatus())
                            && !statuses.contains(dto.getStocktakeLastStatus())) {
                throw new MtException("MT_STOCKTAKE_0008",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0008",
                                                "STOCKTAKE", "stocktakeLastStatus", statuses.toString(),
                                                "【API：stocktakeDocUpdate】"));

            }

        }

        // 1-d
        MtStocktakeDoc mtStocktakeDoc = stocktakeDocPropertyGet(tenantId, dto.getStocktakeId());
        if (mtStocktakeDoc == null) {
            throw new MtException("MT_STOCKTAKE_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0009", "STOCKTAKE", "stocktakeId", "【API：stocktakeDocUpdate】"));

        }
        if ("COMPLETED".equals(mtStocktakeDoc.getStocktakeStatus())) {
            throw new MtException("MT_STOCKTAKE_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0010", "STOCKTAKE", "COMPLETED", "【API：stocktakeDocUpdate】"));
        }
        if ("CLOSED".equals(mtStocktakeDoc.getStocktakeStatus())) {
            throw new MtException("MT_STOCKTAKE_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0010", "STOCKTAKE", "CLOSED", "【API：stocktakeDocUpdate】"));
        }
        // 2.更新盘点单据表数据
        if (StringUtils.isNotEmpty(dto.getStocktakeStatus())) {
            mtStocktakeDoc.setStocktakeStatus(dto.getStocktakeStatus());
        }
        if (dto.getStocktakeLastStatus() != null) {
            mtStocktakeDoc.setStocktakeLastStatus(dto.getStocktakeLastStatus());
        }
        if (StringUtils.isNotEmpty(dto.getOpenFlag())) {
            mtStocktakeDoc.setOpenFlag(dto.getOpenFlag());
        }
        if (StringUtils.isNotEmpty(dto.getAdjustTimelyFlag())) {
            mtStocktakeDoc.setAdjustTimelyFlag(dto.getAdjustTimelyFlag());
        }
        if (StringUtils.isNotEmpty(dto.getMaterialLotLockFlag())) {
            mtStocktakeDoc.setMaterialLotLockFlag(dto.getMaterialLotLockFlag());
        }
        mtStocktakeDoc.setRemark(dto.getRemark());
        mtStocktakeDoc.setTenantId(tenantId);
        self().updateByPrimaryKeySelective(mtStocktakeDoc);

        // 3.记录盘点单历史表数据
        MtStocktakeDocHis mtStocktakeDocHis = new MtStocktakeDocHis();
        BeanUtils.copyProperties(mtStocktakeDoc, mtStocktakeDocHis);
        mtStocktakeDocHis.setTenantId(tenantId);
        mtStocktakeDocHis.setEventId(dto.getEventId());
        mtStocktakeDocHisRepository.insertSelective(mtStocktakeDocHis);
    }

    @Override
    public void stocktakeDocStatusUpdateVerify(Long tenantId, MtStocktakeDocVO3 dto) {
        // 1.判断合规性
        if (StringUtils.isEmpty(dto.getStocktakeId())) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "stocktakeId", "【API：stocktakeDocStatusUpdateVerify】"));

        }
        if (StringUtils.isEmpty(dto.getTargetStocktakeStatus())) {
            throw new MtException("MT_STOCKTAKE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0001",
                                            "STOCKTAKE", "targetStocktakeStatus",
                                            "【API：stocktakeDocStatusUpdateVerify】"));

        }

        // 获取状态组
        MtGenStatusVO2 mtGenStatusVO2 = new MtGenStatusVO2();
        mtGenStatusVO2.setModule("STOCKTAKE");
        mtGenStatusVO2.setStatusGroup("STOCKTAKE_STATUS");
        List<MtGenStatus> mtGenStatuses = mtGenStatusRepository.groupLimitStatusQuery(tenantId, mtGenStatusVO2);
        List<String> statuses = mtGenStatuses.stream().map(t -> t.getStatusCode()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(statuses) || !statuses.contains(dto.getTargetStocktakeStatus())) {
            throw new MtException("MT_STOCKTAKE_0008",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0008",
                                            "STOCKTAKE", "targetStocktakeStatus", statuses.toString(),
                                            "【API：stocktakeDocStatusUpdateVerify】"));

        }

        // 2.调用API{ stocktakeDocPropertyGet}
        MtStocktakeDoc mtStocktakeDoc = stocktakeDocPropertyGet(tenantId, dto.getStocktakeId());
        // 2-a
        if (mtStocktakeDoc == null || StringUtils.isEmpty(mtStocktakeDoc.getStocktakeStatus())) {
            throw new MtException("MT_STOCKTAKE_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0009", "STOCKTAKE", "stocktakeId", "【API：stocktakeDocStatusUpdateVerify】"));

        }

        // 2-b
        if ("NEW".equals(dto.getTargetStocktakeStatus())) {
            throw new MtException("MT_STOCKTAKE_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0011", "STOCKTAKE", "【API：stocktakeDocStatusUpdateVerify】"));

        }

        // 2-c
        if ("RELEASED".equals(dto.getTargetStocktakeStatus())) {
            if (!"NEW".equals(mtStocktakeDoc.getStocktakeStatus())) {
                throw new MtException("MT_STOCKTAKE_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_STOCKTAKE_0012", "STOCKTAKE", "【API：stocktakeDocStatusUpdateVerify】"));
            }
            return;
        }

        // 2-d
        if ("FIRSTCOUNTCOMPLETED".equals(dto.getTargetStocktakeStatus())) {
            if (!"RELEASED".equals(mtStocktakeDoc.getStocktakeStatus())) {
                throw new MtException("MT_STOCKTAKE_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_STOCKTAKE_0013", "STOCKTAKE", "【API：stocktakeDocStatusUpdateVerify】"));
            }
            return;
        }

        // 2-e
        if ("COUNTCOMPLETED".equals(dto.getTargetStocktakeStatus())) {
            if ("RELEASED".equals(mtStocktakeDoc.getStocktakeStatus())
                            || "FIRSTCOUNTCOMPLETED".equals(mtStocktakeDoc.getStocktakeStatus())) {
                return;
            } else {
                throw new MtException("MT_STOCKTAKE_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_STOCKTAKE_0014", "STOCKTAKE", "【API：stocktakeDocStatusUpdateVerify】"));
            }
        }

        // 2-f
        if ("COMPLETED".equals(dto.getTargetStocktakeStatus())) {
            if ("FIRSTCOUNTCOMPLETED".equals(mtStocktakeDoc.getStocktakeStatus())
                            || "COUNTCOMPLETED".equals(mtStocktakeDoc.getStocktakeStatus())) {
                return;
            } else {
//                throw new MtException("MT_STOCKTAKE_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                                "MT_STOCKTAKE_0015", "STOCKTAKE", "【API：stocktakeDocStatusUpdateVerify】"));
            }
        }

        // 2-g
        if ("CLOSED".equals(dto.getTargetStocktakeStatus())) {
            if ("COMPLETED".equals(mtStocktakeDoc.getStocktakeStatus())) {
                throw new MtException("MT_STOCKTAKE_0016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_STOCKTAKE_0016", "STOCKTAKE", "【API：stocktakeDocStatusUpdateVerify】"));

            }
            return;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void stocktakeDocRelease(Long tenantId, String stocktakeId, String eventRequestId) {
        // 第一步，验证参数合规性
        if (StringUtils.isEmpty(stocktakeId)) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "stocktakeId", "【API：stocktakeDocRelease】"));
        }

        // 1-b
        MtStocktakeDocVO3 mtStocktakeDocVO3 = new MtStocktakeDocVO3();
        mtStocktakeDocVO3.setStocktakeId(stocktakeId);
        mtStocktakeDocVO3.setTargetStocktakeStatus("RELEASED");
        stocktakeDocStatusUpdateVerify(tenantId, mtStocktakeDocVO3);

        // 第二步，创建事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(eventRequestId);
        eventCreateVO.setEventTypeCode("STOCKTAKE_RELEASE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 第三步，调用API{stocktakeDocUpdate}更新表状态
        // (1-b中校验通过则一定有数据)
        MtStocktakeDoc mtStocktakeDoc = stocktakeDocPropertyGet(tenantId, stocktakeId);
        MtStocktakeDocVO2 mtStocktakeDocVO2 = new MtStocktakeDocVO2();
        mtStocktakeDocVO2.setStocktakeId(stocktakeId);
        mtStocktakeDocVO2.setStocktakeStatus("RELEASED");
        if (mtStocktakeDoc != null) {
            mtStocktakeDocVO2.setStocktakeLastStatus(mtStocktakeDoc.getStocktakeStatus());
        }
        mtStocktakeDocVO2.setEventBy(DetailsHelper.getUserDetails().getUserId());
        mtStocktakeDocVO2.setEventId(eventId);
        mtStocktakeDocVO2.setEventTime(new Date(System.currentTimeMillis()));
        stocktakeDocUpdate(tenantId, mtStocktakeDocVO2);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void stocktakeDocComplete(Long tenantId, String stocktakeId, String eventRequestId) {
        MtStocktakeDocVO3 checkVO = new MtStocktakeDocVO3();
        checkVO.setStocktakeId(stocktakeId);
        checkVO.setTargetStocktakeStatus("COMPLETED");
        stocktakeDocStatusUpdateVerify(tenantId, checkVO);

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(eventRequestId);
        eventCreateVO.setEventTypeCode("STOCKTAKE_COMPLETED");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        MtStocktakeDoc srcStocktakeDoc = stocktakeDocPropertyGet(tenantId, stocktakeId);

        MtStocktakeDocVO2 updateVO = new MtStocktakeDocVO2();
        updateVO.setStocktakeId(stocktakeId);
        updateVO.setStocktakeStatus(checkVO.getTargetStocktakeStatus());
        updateVO.setStocktakeLastStatus(srcStocktakeDoc.getStocktakeStatus());
        updateVO.setEventId(eventId);

        stocktakeDocUpdate(tenantId, updateVO);
    }

    @Override
    public void stocktakeDocClose(Long tenantId, String stocktakeId, String eventRequestId) {
        MtStocktakeDocVO3 checkVO = new MtStocktakeDocVO3();
        checkVO.setStocktakeId(stocktakeId);
        checkVO.setTargetStocktakeStatus("CLOSED");
        stocktakeDocStatusUpdateVerify(tenantId, checkVO);

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(eventRequestId);
        eventCreateVO.setEventTypeCode("STOCKTAKE_CLOSED");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        MtStocktakeDoc srcStocktakeDoc = stocktakeDocPropertyGet(tenantId, stocktakeId);

        MtStocktakeDocVO2 updateVO = new MtStocktakeDocVO2();
        updateVO.setStocktakeId(stocktakeId);
        updateVO.setStocktakeStatus(checkVO.getTargetStocktakeStatus());
        updateVO.setStocktakeLastStatus(srcStocktakeDoc.getStocktakeStatus());
        updateVO.setEventId(eventId);

        stocktakeDocUpdate(tenantId, updateVO);
    }

    @Override
    public List<String> stocktakeRangeLimitMaterialLotQuery(Long tenantId, String stocktakeId) {
        if (StringUtils.isEmpty(stocktakeId)) {
            throw new MtException("MT_STOCKTAKE_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0001",
                                            "STOCKTAKE", "stocktakeId", "【API：stocktakeRangeLimitMaterialLotQuery】"));
        }

        MtStocktakeDoc queryDoc = stocktakeDocPropertyGet(tenantId, stocktakeId);
        if (queryDoc == null) {
            throw new MtException("MT_STOCKTAKE_0024",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0024",
                                            "STOCKTAKE", stocktakeId, "【API：stocktakeRangeLimitMaterialLotQuery】"));
        }

        // step - 2 get stock range object id
        MtStocktakeRange queryRange = new MtStocktakeRange();
        queryRange.setStocktakeId(stocktakeId);
        List<MtStocktakeRange> stocktakeRangeList =
                        mtStocktakeRangeRepository.stocktakeRangeQuery(tenantId, queryRange);
        List<String> locatorList =
                        stocktakeRangeList.stream().filter(range -> "LOCATOR".equals(range.getRangeObjectType()))
                                        .map(MtStocktakeRange::getRangeObjectId).collect(Collectors.toList());
        List<String> materialList =
                        stocktakeRangeList.stream().filter(range -> "MATERIAL".equals(range.getRangeObjectType()))
                                        .map(MtStocktakeRange::getRangeObjectId).collect(Collectors.toList());

        // step - 3 use locator list to get material lot list
        List<String> materialLotIdList = new ArrayList<>();
        MtContainerVO queryContainerVO = new MtContainerVO();
        for (String locatorId : locatorList) {
            queryContainerVO.setLocatorId(locatorId);
            queryContainerVO.setEnableFlag("Y");
            queryContainerVO.setSubLocatorFlag("N");
            materialLotIdList.addAll(mtContainerRepository.locatorLimitMaterialLotQuery(tenantId, queryContainerVO));
        }

        // step - 4 use material list to get material lot list
        if (CollectionUtils.isNotEmpty(materialLotIdList) && CollectionUtils.isNotEmpty(materialList)) {
            List<MtMaterialLot> mtMaterialLotList =
                            mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIdList);
            materialLotIdList.removeAll(
                            mtMaterialLotList.stream().filter(lot -> !materialList.contains(lot.getMaterialId()))
                                            .map(MtMaterialLot::getMaterialLotId).collect(Collectors.toList()));
        }

        return materialLotIdList;
    }

    @Override
    public List<String> stocktaketAndMaterialLotDifferenceQuery(Long tenantId, String stocktakeId) {
        List<String> rangeMaterialLotIdList = stocktakeRangeLimitMaterialLotQuery(tenantId, stocktakeId);

        MtStocktakeActual mtStocktakeActual = new MtStocktakeActual();
        mtStocktakeActual.setStocktakeId(stocktakeId);
        List<MtStocktakeActual> actualList =
                        mtStocktakeActualRepository.stocktakeActualQuery(tenantId, mtStocktakeActual);

        List<String> actualMaterialLotIdList =
                        actualList.stream().map(MtStocktakeActual::getMaterialLotId).collect(Collectors.toList());

        rangeMaterialLotIdList.removeAll(actualMaterialLotIdList);

        return rangeMaterialLotIdList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void stocktakeDocFirstcountComplete(Long tenantId, String stocktakeId, String eventRequestId) {
        // 1.校验参数的合规性
        if (StringUtils.isEmpty(stocktakeId)) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "stocktakeId", "【API：stocktakeDocFirstcountComplete】"));
        }
        MtStocktakeDocVO3 mtStocktakeDocVO3 = new MtStocktakeDocVO3();
        mtStocktakeDocVO3.setStocktakeId(stocktakeId);
        mtStocktakeDocVO3.setTargetStocktakeStatus("FIRSTCOUNTCOMPLETED");
        stocktakeDocStatusUpdateVerify(tenantId, mtStocktakeDocVO3);

        // 2.创建事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(eventRequestId);
        eventCreateVO.setEventTypeCode("STOCKTAKE_FIRSTCOUNTCOMPLETED");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 3.调用API{ stocktakeDocUpdate}更新表状态
        MtStocktakeDoc mtStocktakeDoc = stocktakeDocPropertyGet(tenantId, stocktakeId);
        MtStocktakeDocVO2 mtStocktakeDocVO2 = new MtStocktakeDocVO2();
        mtStocktakeDocVO2.setStocktakeId(stocktakeId);
        mtStocktakeDocVO2.setStocktakeStatus("FIRSTCOUNTCOMPLETED");
        if (mtStocktakeDoc != null) {
            mtStocktakeDocVO2.setStocktakeLastStatus(mtStocktakeDoc.getStocktakeStatus());
        }
        mtStocktakeDocVO2.setEventId(eventId);
        stocktakeDocUpdate(tenantId, mtStocktakeDocVO2);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void stocktakeDocCountComplete(Long tenantId, String stocktakeId, String eventRequestId) {
        // 1.校验参数的合规性
        if (StringUtils.isEmpty(stocktakeId)) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "stocktakeId", "【API：stocktakeDocCountComplete】"));
        }
        MtStocktakeDocVO3 mtStocktakeDocVO3 = new MtStocktakeDocVO3();
        mtStocktakeDocVO3.setStocktakeId(stocktakeId);
        mtStocktakeDocVO3.setTargetStocktakeStatus("COUNTCOMPLETED");
        stocktakeDocStatusUpdateVerify(tenantId, mtStocktakeDocVO3);

        // 2.创建事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(eventRequestId);
        eventCreateVO.setEventTypeCode("STOCKTAKE_COUNTCOMPLETED");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 3.更新表状态
        MtStocktakeDoc mtStocktakeDoc = stocktakeDocPropertyGet(tenantId, stocktakeId);
        MtStocktakeDocVO2 mtStocktakeDocVO2 = new MtStocktakeDocVO2();
        mtStocktakeDocVO2.setStocktakeId(stocktakeId);
        mtStocktakeDocVO2.setStocktakeStatus("COUNTCOMPLETED");
        if (mtStocktakeDoc != null) {
            mtStocktakeDocVO2.setStocktakeLastStatus(mtStocktakeDoc.getStocktakeStatus());
        }
        mtStocktakeDocVO2.setEventId(eventId);
        stocktakeDocUpdate(tenantId, mtStocktakeDocVO2);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void stocktakeStatusBackout(Long tenantId, MtStocktakeDocVO4 doc) {
        if (StringUtils.isEmpty(doc.getStocktakeId())) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "stocktakeId", "【API：stocktakeStatusBackout】"));
        }

        MtStocktakeDoc mtStocktakeDoc = stocktakeDocPropertyGet(tenantId, doc.getStocktakeId());
        if (mtStocktakeDoc == null || StringUtils.isEmpty(mtStocktakeDoc.getStocktakeId())) {
            throw new MtException("MT_STOCKTAKE_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0009", "STOCKTAKE", "stocktakeId", "【API：stocktakeStatusBackout】"));
        } else if ("NEW".equals(mtStocktakeDoc.getStocktakeStatus())) {
            throw new MtException("MT_STOCKTAKE_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0010", "STOCKTAKE", "NEW", "【API：stocktakeStatusBackout】"));
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(doc.getEventRequestId());
        eventCreateVO.setEventTypeCode("STOCKTAKE_BACKOUT");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        MtStocktakeDocVO2 updateVO = new MtStocktakeDocVO2();
        updateVO.setStocktakeId(mtStocktakeDoc.getStocktakeId());
        updateVO.setEventId(eventId);
        if (StringUtils.isNotEmpty(mtStocktakeDoc.getStocktakeLastStatus())) {
            updateVO.setStocktakeStatus(mtStocktakeDoc.getStocktakeLastStatus());
        } else {
            switch (mtStocktakeDoc.getStocktakeStatus()) {
                case "RELEASED":
                    updateVO.setStocktakeStatus("NEW");
                    break;
                case "FIRSTCOUNTCOMPLETED":
                case "COUNTCOMPLETED":
                    updateVO.setStocktakeStatus("RELEASED");
                    break;
                default:
                    break;
            }
        }
        updateVO.setStocktakeLastStatus("");
        updateVO.setRemark(StringUtils.isEmpty(doc.getRemark()) ? null : doc.getRemark());
        stocktakeDocUpdate(tenantId, updateVO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void stocktakeLimitMaterialLotUnlock(Long tenantId, String stocktakeId, String eventRequestId) {
        if (StringUtils.isEmpty(stocktakeId)) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "stocktakeId", "【API：stocktakeLimitMaterialLotUnlock】"));
        }

        MtStocktakeDoc mtStocktakeDoc = stocktakeDocPropertyGet(tenantId, stocktakeId);
        if (mtStocktakeDoc == null) {
            throw new MtException("MT_STOCKTAKE_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0009", "STOCKTAKE", "stocktakeId", "【API：stocktakeLimitMaterialLotUnlock】"));
        }

        if ("N".equals(mtStocktakeDoc.getMaterialLotLockFlag())) {
            throw new MtException("MT_STOCKTAKE_0028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0028", "STOCKTAKE", "【API：stocktakeLimitMaterialLotUnlock】"));
        }

        List<String> materialLotList = stocktakeRangeLimitMaterialLotQuery(tenantId, stocktakeId);
        if (CollectionUtils.isEmpty(materialLotList)) {
            throw new MtException("MT_STOCKTAKE_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0029", "STOCKTAKE", "【API：stocktakeLimitMaterialLotUnlock】"));
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(eventRequestId);
        eventCreateVO.setEventTypeCode("STOCKTAKE_MATERIALLOT_UNLOCK");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        MtStocktakeDocVO2 updateVO = new MtStocktakeDocVO2();
        updateVO.setStocktakeId(mtStocktakeDoc.getStocktakeId());
        updateVO.setEventId(eventId);
        updateVO.setMaterialLotLockFlag("N");
        stocktakeDocUpdate(tenantId, updateVO);

        MtMaterialLotVO2 updateMaterialLotVO;
        for (String lotId : materialLotList) {
            updateMaterialLotVO = new MtMaterialLotVO2();
            updateMaterialLotVO.setMaterialLotId(lotId);
            updateMaterialLotVO.setEventId(eventId);
            updateMaterialLotVO.setStocktakeFlag("N");
            mtMaterialLotRepository.materialLotUpdate(tenantId, updateMaterialLotVO, "N");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void stocktakeLimitMaterialLotLock(Long tenantId, String stocktakeId, String eventRequestId) {
        if (StringUtils.isEmpty(stocktakeId)) {
            throw new MtException("MT_STOCKTAKE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0001", "STOCKTAKE", "stocktakeId", "【API：stocktakeLimitMaterialLotLock】"));
        }

        MtStocktakeDoc mtStocktakeDoc = stocktakeDocPropertyGet(tenantId, stocktakeId);
        if (mtStocktakeDoc == null) {
            throw new MtException("MT_STOCKTAKE_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0009", "STOCKTAKE", "stocktakeId", "【API：stocktakeLimitMaterialLotLock】"));
        }

        if ("Y".equals(mtStocktakeDoc.getMaterialLotLockFlag())) {
            throw new MtException("MT_STOCKTAKE_0028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0028", "STOCKTAKE", "【API：stocktakeLimitMaterialLotLock】"));
        }

        List<String> materialLotList = stocktakeRangeLimitMaterialLotQuery(tenantId, stocktakeId);
        if (CollectionUtils.isEmpty(materialLotList)) {
            throw new MtException("MT_STOCKTAKE_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_STOCKTAKE_0029", "STOCKTAKE", "【API：stocktakeLimitMaterialLotLock】"));
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(eventRequestId);
        eventCreateVO.setEventTypeCode("STOCKTAKE_MATERIALLOT_LOCK");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        MtStocktakeDocVO2 updateVO = new MtStocktakeDocVO2();
        updateVO.setStocktakeId(mtStocktakeDoc.getStocktakeId());
        updateVO.setEventId(eventId);
        updateVO.setMaterialLotLockFlag("Y");
        stocktakeDocUpdate(tenantId, updateVO);

        MtMaterialLotVO2 updateMaterialLotVO;
        for (String lotId : materialLotList) {
            updateMaterialLotVO = new MtMaterialLotVO2();
            updateMaterialLotVO.setMaterialLotId(lotId);
            updateMaterialLotVO.setEventId(eventId);
            updateMaterialLotVO.setStocktakeFlag("Y");
            mtMaterialLotRepository.materialLotUpdate(tenantId, updateMaterialLotVO, "N");
        }
    }
}
