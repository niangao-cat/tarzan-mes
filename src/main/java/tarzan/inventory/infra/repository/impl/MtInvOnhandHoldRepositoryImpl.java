package tarzan.inventory.infra.repository.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.util.MtBaseConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.CollectorsUtil;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtInvOnhandHold;
import tarzan.inventory.domain.entity.MtInvOnhandHoldJournal;
import tarzan.inventory.domain.entity.MtInvOnhandQuantity;
import tarzan.inventory.domain.repository.MtInvOnhandHoldJournalRepository;
import tarzan.inventory.domain.repository.MtInvOnhandHoldRepository;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.inventory.infra.mapper.MtInvOnhandHoldMapper;
import tarzan.inventory.infra.mapper.MtInvOnhandQuantityMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.*;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.vo.MtModLocatorVO15;

import static java.util.stream.Collectors.toList;

/**
 * 库存保留量 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:53
 */
@Component
public class MtInvOnhandHoldRepositoryImpl extends BaseRepositoryImpl<MtInvOnhandHold>
                implements MtInvOnhandHoldRepository {


    @Autowired
    private MtInvOnhandHoldMapper mtInvOnhandHoldMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtInvOnhandQuantityMapper mtInvOnhandQuantityMapper;

    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtInvOnhandHoldJournalRepository mtInvOnhandHoldJournalRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private ThreadPoolTaskExecutor poolExecutor;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public MtInvOnhandHold onhandReserveGet(Long tenantId, String onhandHoldId) {
        if (StringUtils.isEmpty(onhandHoldId)) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "onhandHoldId", "【API：onhandReserveGet】"));
        }

        MtInvOnhandHold hold = new MtInvOnhandHold();
        hold.setTenantId(tenantId);
        hold.setOnhandHoldId(onhandHoldId);

        return mtInvOnhandHoldMapper.selectOne(hold);
    }

    @Override
    public List<MtInvOnhandHold> onhandReserveBatchGet(Long tenantId, List<String> onhandHoldIds) {
        if (CollectionUtils.isEmpty(onhandHoldIds)) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "onhandHoldIds", "【API：onhandReserveBatchGet】"));
        }

        return mtInvOnhandHoldMapper.onhandHoldBatchGet(tenantId, onhandHoldIds);
    }

    @Override
    public List<String> propertyLimitOnhandReserveQuery(Long tenantId, MtInvOnhandHoldVO3 dto) {
        if (ObjectFieldsHelper.isAllFieldNull(dto)) {
            throw new MtException("MT_INVENTORY_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0002", "INVENTORY", "【API：propertyLimitOnhandReserveQuery】"));
        }

        return mtInvOnhandHoldMapper.propertyLimitOnhandReserveQuery(tenantId, dto);
    }

    @Override
    public void onhandReserveCreateVerify(Long tenantId, MtInvOnhandHoldVO3 dto) {
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "siteId", "【API：onhandReserveCreateVerify】"));
        }
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "materialId", "【API：onhandReserveCreateVerify】"));
        }
        if (StringUtils.isEmpty(dto.getLocatorId())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "locatorId", "【API：onhandReserveCreateVerify】"));
        }
        if (dto.getHoldQuantity() == null) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "holdQuantity", "【API：onhandReserveCreateVerify】"));
        }
        if (StringUtils.isEmpty(dto.getHoldType())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "holdType", "【API：onhandReserveCreateVerify】"));
        }
        if ((StringUtils.isEmpty(dto.getOwnerType()) && StringUtils.isNotEmpty(dto.getOwnerId()))
                        || (StringUtils.isNotEmpty(dto.getOwnerType()) && StringUtils.isEmpty(dto.getOwnerId()))) {
            throw new MtException("MT_INVENTORY_0018",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0018",
                                            "INVENTORY", "ownerType", "ownerId", "【API：onhandReserveCreateVerify】"));
        }
        if (StringUtils.isNotEmpty(dto.getOwnerType())) {
            MtGenTypeVO2 mtGenTypeVO = new MtGenTypeVO2();
            mtGenTypeVO.setModule("INVENTORY");
            mtGenTypeVO.setTypeGroup("INVENTORY_OWNER_TYPE");
            List<MtGenType> invOwnerTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO);
            List<String> invOwnerTypeCodes =
                            invOwnerTypes.stream().map(MtGenType::getTypeCode).collect(Collectors.toList());
            if (!invOwnerTypeCodes.contains(dto.getOwnerType())) {
                throw new MtException("MT_INVENTORY_0019",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0019",
                                                "INVENTORY", dto.getOwnerType(), "【API：onhandReserveCreateVerify】"));
            }
        }

        // 获取站点信息
        MtModSite modSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, dto.getSiteId());
        if (modSite == null || !"Y".equals(modSite.getEnableFlag())) {
            throw new MtException("MT_INVENTORY_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0003", "INVENTORY", "siteId", "【API：onhandReserveCreateVerify】"));
        }

        // 获取物料信息
        MtMaterial material = mtMaterialRepository.materialPropertyGet(tenantId, dto.getMaterialId());
        if (material == null || !"Y".equals(material.getEnableFlag())) {
            throw new MtException("MT_INVENTORY_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0003", "INVENTORY", "materialId", "【API：onhandReserveCreateVerify】"));
        }

        // 获取物料站点关系信息
        MtMaterialSite materialSite = new MtMaterialSite();
        materialSite.setMaterialId(dto.getMaterialId());
        materialSite.setSiteId(dto.getSiteId());
        String materialSiteId = mtMaterialSiteRepository.materialSiteLimitRelationGet(tenantId, materialSite);
        if (StringUtils.isEmpty(materialSiteId)) {
            throw new MtException("MT_INVENTORY_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0005", "INVENTORY", "【API：onhandReserveCreateVerify】"));
        }

        // 校验库位是否允许记录库存
        mtModLocatorRepository.locatorRecordOnhandVerify(tenantId, dto.getLocatorId());

        // 如果保留类型为 ORDER 的时候，指令类型和指令主键不可为空
        // 否则， 指令类型和指令主键必须为空
        if ("ORDER".equals(dto.getHoldType())) {
            if (StringUtils.isEmpty(dto.getOrderId()) || StringUtils.isEmpty(dto.getOrderType())) {
                throw new MtException("MT_INVENTORY_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_INVENTORY_0006", "INVENTORY", "【API：onhandReserveCreateVerify】"));
            }
        } else {
            if (StringUtils.isNotEmpty(dto.getOrderId()) || StringUtils.isNotEmpty(dto.getOrderType())) {
                throw new MtException("MT_INVENTORY_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_INVENTORY_0014", "INVENTORY", "【API：onhandReserveCreateVerify】"));
            }
        }

        // 获取库位是否允许负库存标识
        // NegativeFlag为Y，表示允许负库存，结束API
        MtModLocator mtModLocator = mtModLocatorRepository.locatorBasicPropertyGet(tenantId, dto.getLocatorId());
        if (mtModLocator != null && "Y".equals(mtModLocator.getNegativeFlag())) {
            return;
        }

        // 获取物料现有量
        MtInvOnhandQuantity invOnhandQuantity = new MtInvOnhandQuantity();
        invOnhandQuantity.setSiteId(dto.getSiteId());
        invOnhandQuantity.setMaterialId(dto.getMaterialId());
        invOnhandQuantity.setLocatorId(dto.getLocatorId());
        invOnhandQuantity.setLotCode(dto.getLotCode());
        invOnhandQuantity.setOwnerId(dto.getOwnerId());
        invOnhandQuantity.setOwnerType(dto.getOwnerType());
        invOnhandQuantity = mtInvOnhandQuantityMapper.selectOnhandQuantity(tenantId, invOnhandQuantity);

        Double onhandQty = 0.0D;
        if (invOnhandQuantity != null) {
            onhandQty = invOnhandQuantity.getOnhandQuantity();
        }

        // 获取物料保留量
        MtInvOnhandHold invOnhandHold = new MtInvOnhandHold();
        invOnhandHold.setSiteId(dto.getSiteId());
        invOnhandHold.setMaterialId(dto.getMaterialId());
        invOnhandHold.setLocatorId(dto.getLocatorId());
        invOnhandHold.setLotCode(dto.getLotCode());
        invOnhandHold.setOwnerId(dto.getOwnerId());
        invOnhandHold.setOwnerType(dto.getOwnerType());
        List<MtInvOnhandHold> invOnhandHolds = mtInvOnhandHoldMapper.selectInvOnhandHolds(tenantId, invOnhandHold);

        // 汇总保留数量
        BigDecimal sumOnhandHoldQty = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(invOnhandHolds)) {
            sumOnhandHoldQty = invOnhandHolds.stream().filter(c -> null != c.getHoldQuantity())
                            .collect(CollectorsUtil.summingBigDecimal(c -> BigDecimal.valueOf(c.getHoldQuantity())));
        }


        // 计算对应站点/物料/库位/批次下的可保留库存量
        BigDecimal availableQty = BigDecimal.valueOf(onhandQty).subtract(sumOnhandHoldQty);

        // 判断本次要保留量是否可以保留
        if (BigDecimal.valueOf(dto.getHoldQuantity()).compareTo(availableQty) > 0) {
            throw new MtException("MT_INVENTORY_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0007", "INVENTORY", "【API：onhandReserveCreateVerify】"));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void onhandReserveCreate(Long tenantId, MtInvOnhandHoldVO2 dto) {
        // 暂存数据
        String invOnhandHoldId;
        Double finalHoldQty;

        // 判断当前维度的库存保存量是否存在
        MtInvOnhandHold oldInvOnhandHold = new MtInvOnhandHold();
        oldInvOnhandHold.setSiteId(dto.getSiteId());
        oldInvOnhandHold.setMaterialId(dto.getMaterialId());
        oldInvOnhandHold.setLocatorId(dto.getLocatorId());
        oldInvOnhandHold.setLotCode(dto.getLotCode());
        oldInvOnhandHold.setHoldType(dto.getHoldType());
        oldInvOnhandHold.setOrderId(dto.getOrderId());
        oldInvOnhandHold.setOrderType(dto.getOrderType());
        oldInvOnhandHold.setOwnerId(dto.getOwnerId());
        oldInvOnhandHold.setOwnerType(dto.getOwnerType());

        oldInvOnhandHold = mtInvOnhandHoldMapper.selectInvOnhandHold(tenantId, oldInvOnhandHold);

        if (oldInvOnhandHold != null && StringUtils.isNotEmpty(oldInvOnhandHold.getOnhandHoldId())) {
            // 更新 累计保留数量
            BigDecimal resultQty = BigDecimal.valueOf(oldInvOnhandHold.getHoldQuantity())
                            .add(BigDecimal.valueOf(dto.getHoldQuantity()));
            oldInvOnhandHold.setHoldQuantity(resultQty.doubleValue());
            oldInvOnhandHold.setTenantId(tenantId);
            self().updateByPrimaryKeySelective(oldInvOnhandHold);

            invOnhandHoldId = oldInvOnhandHold.getOnhandHoldId();
            finalHoldQty = oldInvOnhandHold.getHoldQuantity();
        } else {
            // 新增 该维度的库存保留量
            MtInvOnhandHold newInvOnhandHold = new MtInvOnhandHold();
            newInvOnhandHold.setSiteId(dto.getSiteId());
            newInvOnhandHold.setMaterialId(dto.getMaterialId());
            newInvOnhandHold.setLocatorId(dto.getLocatorId());
            newInvOnhandHold.setLotCode(dto.getLotCode());
            newInvOnhandHold.setHoldQuantity(dto.getHoldQuantity());
            newInvOnhandHold.setHoldType(dto.getHoldType());
            newInvOnhandHold.setOrderId(dto.getOrderId());
            newInvOnhandHold.setOrderType(dto.getOrderType());
            newInvOnhandHold.setOwnerId(dto.getOwnerId());
            newInvOnhandHold.setOwnerType(dto.getOwnerType());
            newInvOnhandHold.setTenantId(tenantId);
            self().insertSelective(newInvOnhandHold);

            invOnhandHoldId = newInvOnhandHold.getOnhandHoldId();
            finalHoldQty = newInvOnhandHold.getHoldQuantity();
        }

        String eventId;

        if (StringUtils.isNotEmpty(dto.getEventId())) {
            eventId = dto.getEventId();
        } else {
            // 生成事件
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("INV_HOLD_CREATE");
            eventCreateVO.setEventRequestId(dto.getEventRequestId());
            eventCreateVO.setShiftDate(dto.getShiftDate());
            eventCreateVO.setShiftCode(dto.getShiftCode());
            eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        }

        // 记录库存保留日记账
        MtInvOnhandHoldJournal journal = new MtInvOnhandHoldJournal();
        journal.setOnhandHoldId(invOnhandHoldId);
        journal.setSiteId(dto.getSiteId());
        journal.setMaterialId(dto.getMaterialId());
        journal.setLocatorId(dto.getLocatorId());
        journal.setLotCode(dto.getLotCode());
        journal.setChangeQuantity(dto.getHoldQuantity());
        journal.setHoldQuantity(finalHoldQty);
        journal.setHoldType(dto.getHoldType());
        journal.setOrderType(dto.getOrderType());
        journal.setOrderId(dto.getOrderId());
        journal.setEventId(eventId);
        journal.setEventBy(DetailsHelper.getUserDetails().getUserId());
        journal.setEventTime(new Date());
        journal.setOwnerId(dto.getOwnerId());
        journal.setOwnerType(dto.getOwnerType());

        journal.setTenantId(tenantId);
        mtInvOnhandHoldJournalRepository.insertSelective(journal);

    }

    @Override
    public void onhandReserveReleaseVerify(Long tenantId, MtInvOnhandHoldVO4 dto) {
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "siteId", "【API：onhandReserveReleaseVerify】"));
        }
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "materialId", "【API：onhandReserveReleaseVerify】"));
        }
        if (StringUtils.isEmpty(dto.getLocatorId())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "locatorId", "【API：onhandReserveReleaseVerify】"));
        }
        if (dto.getReleaseQuantity() == null) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "releaseQuantity", "【API：onhandReserveReleaseVerify】"));
        }
        if (StringUtils.isEmpty(dto.getHoldType())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "holdType", "【API：onhandReserveReleaseVerify】"));
        }
        if ((StringUtils.isEmpty(dto.getOwnerType()) && StringUtils.isNotEmpty(dto.getOwnerId()))
                        || (StringUtils.isNotEmpty(dto.getOwnerType()) && StringUtils.isEmpty(dto.getOwnerId()))) {
            throw new MtException("MT_INVENTORY_0018",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0018",
                                            "INVENTORY", "ownerType", "ownerId", "【API：onhandReserveReleaseVerify】"));
        }
        if (StringUtils.isNotEmpty(dto.getOwnerType())) {
            MtGenTypeVO2 mtGenTypeVO = new MtGenTypeVO2();
            mtGenTypeVO.setModule("INVENTORY");
            mtGenTypeVO.setTypeGroup("INVENTORY_OWNER_TYPE");
            List<MtGenType> invOwnerTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO);
            List<String> invOwnerTypeCodes =
                            invOwnerTypes.stream().map(MtGenType::getTypeCode).collect(Collectors.toList());
            if (!invOwnerTypeCodes.contains(dto.getOwnerType())) {
                throw new MtException("MT_INVENTORY_0019",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0019",
                                                "INVENTORY", dto.getOwnerType(), "【API：onhandReserveReleaseVerify】"));
            }
        }

        // 获取站点信息
        MtModSite modSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, dto.getSiteId());
        if (modSite == null || !"Y".equals(modSite.getEnableFlag())) {
            throw new MtException("MT_INVENTORY_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0003", "INVENTORY", "siteId", "【API：onhandReserveReleaseVerify】"));
        }

        // 获取物料信息
        MtMaterial material = mtMaterialRepository.materialPropertyGet(tenantId, dto.getMaterialId());
        if (material == null || !"Y".equals(material.getEnableFlag())) {
            throw new MtException("MT_INVENTORY_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0003", "INVENTORY", "materialId", "【API：onhandReserveReleaseVerify】"));
        }

        // 获取物料站点关系信息
        MtMaterialSite materialSite = new MtMaterialSite();
        materialSite.setMaterialId(dto.getMaterialId());
        materialSite.setSiteId(dto.getSiteId());
        String materialSiteId = mtMaterialSiteRepository.materialSiteLimitRelationGet(tenantId, materialSite);
        if (StringUtils.isEmpty(materialSiteId)) {
            throw new MtException("MT_INVENTORY_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0005", "INVENTORY", "【API：onhandReserveReleaseVerify】"));
        }

        // 校验库位是否允许记录库存
        mtModLocatorRepository.locatorRecordOnhandVerify(tenantId, dto.getLocatorId());

        // 如果保留类型为 ORDER 的时候，指令类型和指令主键不可为空
        if ("ORDER".equals(dto.getHoldType())
                        && (StringUtils.isEmpty(dto.getOrderId()) || StringUtils.isEmpty(dto.getOrderType()))) {
            throw new MtException("MT_INVENTORY_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0006", "INVENTORY", "【API：onhandReserveReleaseVerify】"));
        }

        // 以当前维度查询库存保留量数据
        MtInvOnhandHold mtInvOnhandHold = new MtInvOnhandHold();
        mtInvOnhandHold.setSiteId(dto.getSiteId());
        mtInvOnhandHold.setMaterialId(dto.getMaterialId());
        mtInvOnhandHold.setLocatorId(dto.getLocatorId());
        mtInvOnhandHold.setLotCode(dto.getLotCode());
        mtInvOnhandHold.setHoldType(dto.getHoldType());
        mtInvOnhandHold.setOrderId(dto.getOrderId());
        mtInvOnhandHold.setOrderType(dto.getOrderType());
        mtInvOnhandHold.setOwnerId(dto.getOwnerId());
        mtInvOnhandHold.setOwnerType(dto.getOwnerType());
        mtInvOnhandHold = mtInvOnhandHoldMapper.selectInvOnhandHold(tenantId, mtInvOnhandHold);

        Double holdQuantity = Double.valueOf(0.0D);
        if (mtInvOnhandHold != null && StringUtils.isNotEmpty(mtInvOnhandHold.getOnhandHoldId())) {
            holdQuantity = mtInvOnhandHold.getHoldQuantity() == null ? Double.valueOf(0.0D)
                            : mtInvOnhandHold.getHoldQuantity();
        }

        if (BigDecimal.valueOf(dto.getReleaseQuantity()).compareTo(BigDecimal.valueOf(holdQuantity)) > 0) {
            throw new MtException("MT_INVENTORY_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0009", "INVENTORY", "【API：onhandReserveReleaseVerify】"));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void onhandReserveRelease(Long tenantId, MtInvOnhandHoldVO5 dto) {

        // 判断当前维度的库存保存量是否存在
        MtInvOnhandHold oldInvOnhandHold = new MtInvOnhandHold();
        oldInvOnhandHold.setSiteId(dto.getSiteId());
        oldInvOnhandHold.setMaterialId(dto.getMaterialId());
        oldInvOnhandHold.setLocatorId(dto.getLocatorId());
        oldInvOnhandHold.setLotCode(dto.getLotCode());
        oldInvOnhandHold.setHoldType(dto.getHoldType());
        oldInvOnhandHold.setOrderId(dto.getOrderId());
        oldInvOnhandHold.setOrderType(dto.getOrderType());
        oldInvOnhandHold.setOwnerId(dto.getOwnerId());
        oldInvOnhandHold.setOwnerType(dto.getOwnerType());

        oldInvOnhandHold = mtInvOnhandHoldMapper.selectInvOnhandHold(tenantId, oldInvOnhandHold);

        if (oldInvOnhandHold != null && StringUtils.isNotEmpty(oldInvOnhandHold.getOnhandHoldId())) {
            // 更新 扣减保留数量
            BigDecimal resultQty = BigDecimal.valueOf(oldInvOnhandHold.getHoldQuantity())
                            .subtract(BigDecimal.valueOf(dto.getReleaseQuantity()));
            oldInvOnhandHold.setHoldQuantity(resultQty.doubleValue());
            oldInvOnhandHold.setTenantId(tenantId);
            if (resultQty.compareTo(BigDecimal.ZERO) == 0) {
                self().deleteByPrimaryKey(oldInvOnhandHold);
            } else {
                self().updateByPrimaryKeySelective(oldInvOnhandHold);
            }

            String eventId;

            if (StringUtils.isNotEmpty(dto.getEventId())) {
                eventId = dto.getEventId();
            } else {
                // 生成事件
                MtEventCreateVO eventCreateVO = new MtEventCreateVO();
                eventCreateVO.setEventTypeCode("INV_HOLD_RELEASE");
                eventCreateVO.setEventRequestId(dto.getEventRequestId());
                eventCreateVO.setShiftCode(dto.getShiftCode());
                eventCreateVO.setShiftDate(dto.getShiftDate());
                eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
            }

            // 记录库存保留日记账
            MtInvOnhandHoldJournal journal = new MtInvOnhandHoldJournal();
            journal.setOnhandHoldId(oldInvOnhandHold.getOnhandHoldId());
            journal.setSiteId(dto.getSiteId());
            journal.setMaterialId(dto.getMaterialId());
            journal.setLocatorId(dto.getLocatorId());
            journal.setLotCode(dto.getLotCode());
            journal.setChangeQuantity(-dto.getReleaseQuantity());
            journal.setHoldQuantity(oldInvOnhandHold.getHoldQuantity());
            journal.setHoldType(dto.getHoldType());
            journal.setOrderType(dto.getOrderType());
            journal.setOrderId(dto.getOrderId());
            journal.setEventId(eventId);
            journal.setEventBy(DetailsHelper.getUserDetails().getUserId());
            journal.setEventTime(new Date());
            journal.setOwnerId(dto.getOwnerId());
            journal.setOwnerType(dto.getOwnerType());

            journal.setTenantId(tenantId);
            mtInvOnhandHoldJournalRepository.insertSelective(journal);
        }
    }

    @Override
    public void onhandReserveAvailableVerify(Long tenantId, MtInvOnhandHoldVO dto) {
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "siteId", "【API：onhandReserveAvailableVerify】"));
        }
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "materialId", "【API：onhandReserveAvailableVerify】"));
        }
        if (StringUtils.isEmpty(dto.getLocatorId())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "locatorId", "【API：onhandReserveAvailableVerify】"));
        }
        if (dto.getQuantity() == null) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "quantity", "【API：onhandReserveAvailableVerify】"));
        }
        if (StringUtils.isEmpty(dto.getHoldType())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "holdType", "【API：onhandReserveAvailableVerify】"));
        }

        if ((StringUtils.isEmpty(dto.getOwnerType()) && StringUtils.isNotEmpty(dto.getOwnerId()))
                        || (StringUtils.isNotEmpty(dto.getOwnerType()) && StringUtils.isEmpty(dto.getOwnerId()))) {
            throw new MtException("MT_INVENTORY_0018",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0018",
                                            "INVENTORY", "ownerType", "ownerId", "【API：onhandReserveAvailableVerify】"));
        }
        if (StringUtils.isNotEmpty(dto.getOwnerType())) {
            MtGenTypeVO2 mtGenTypeVO = new MtGenTypeVO2();
            mtGenTypeVO.setModule("INVENTORY");
            mtGenTypeVO.setTypeGroup("INVENTORY_OWNER_TYPE");
            List<MtGenType> invOwnerTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO);
            List<String> invOwnerTypeCodes =
                            invOwnerTypes.stream().map(MtGenType::getTypeCode).collect(Collectors.toList());
            if (!invOwnerTypeCodes.contains(dto.getOwnerType())) {
                throw new MtException("MT_INVENTORY_0019",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0019",
                                                "INVENTORY", dto.getOwnerType(), "【API：onhandReserveAvailableVerify】"));
            }
        }

        // 如果保留类型为 ORDER 的时候，指令类型和指令主键不可为空
        if ("ORDER".equals(dto.getHoldType())
                        && (StringUtils.isEmpty(dto.getOrderId()) || StringUtils.isEmpty(dto.getOrderType()))) {
            throw new MtException("MT_INVENTORY_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0006", "INVENTORY", "【API：onhandReserveAvailableVerify】"));
        }

        MtInvOnhandHold oldInvOnhandHold = new MtInvOnhandHold();
        oldInvOnhandHold.setSiteId(dto.getSiteId());
        oldInvOnhandHold.setMaterialId(dto.getMaterialId());
        oldInvOnhandHold.setLocatorId(dto.getLocatorId());
        oldInvOnhandHold.setLotCode(dto.getLotCode());
        oldInvOnhandHold.setHoldType(dto.getHoldType());
        oldInvOnhandHold.setOrderType(dto.getOrderType());
        oldInvOnhandHold.setOrderId(dto.getOrderId());
        oldInvOnhandHold.setOwnerType(dto.getOwnerType());
        oldInvOnhandHold.setOwnerId(dto.getOwnerId());
        oldInvOnhandHold = mtInvOnhandHoldMapper.selectInvOnhandHold(tenantId, oldInvOnhandHold);

        if (oldInvOnhandHold == null || oldInvOnhandHold.getHoldQuantity() < dto.getQuantity()) {
            throw new MtException("MT_INVENTORY_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0013", "INVENTORY", "【API：onhandReserveAvailableVerify】"));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void onhandReserveUse(Long tenantId, MtInvOnhandHoldVO6 dto) {
        MtInvOnhandHold oldInvOnhandHold = new MtInvOnhandHold();
        oldInvOnhandHold.setSiteId(dto.getSiteId());
        oldInvOnhandHold.setMaterialId(dto.getMaterialId());
        oldInvOnhandHold.setLocatorId(dto.getLocatorId());
        oldInvOnhandHold.setLotCode(dto.getLotCode());
        oldInvOnhandHold.setHoldType(dto.getHoldType());
        oldInvOnhandHold.setOrderType(dto.getOrderType());
        oldInvOnhandHold.setOrderId(dto.getOrderId());
        oldInvOnhandHold.setOwnerType(dto.getOwnerType());
        oldInvOnhandHold.setOwnerId(dto.getOwnerId());
        oldInvOnhandHold = mtInvOnhandHoldMapper.selectInvOnhandHold(tenantId, oldInvOnhandHold);

        if (oldInvOnhandHold != null && StringUtils.isNotEmpty(oldInvOnhandHold.getOnhandHoldId())) {

            // 计算扣减后的数量
            BigDecimal newHoldQty = BigDecimal.valueOf(oldInvOnhandHold.getHoldQuantity())
                            .subtract(BigDecimal.valueOf(dto.getQuantity()));

            oldInvOnhandHold.setTenantId(tenantId);
            if (newHoldQty.compareTo(BigDecimal.ZERO) == 0) {
                // 删除
                self().deleteByPrimaryKey(oldInvOnhandHold);
            } else {
                // 更新
                oldInvOnhandHold.setHoldQuantity(newHoldQty.doubleValue());
                self().updateByPrimaryKeySelective(oldInvOnhandHold);
            }

            String eventId;

            if (StringUtils.isNotEmpty(dto.getEventId())) {
                eventId = dto.getEventId();
            } else {
                // 生成事件
                MtEventCreateVO eventCreateVO = new MtEventCreateVO();
                eventCreateVO.setEventTypeCode("INV_HOLD_USE");
                eventCreateVO.setEventRequestId(dto.getEventRequestId());
                eventCreateVO.setLocatorId(dto.getLocatorId());
                eventCreateVO.setShiftDate(dto.getShiftDate());
                eventCreateVO.setShiftCode(dto.getShiftCode());
                eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
            }

            // 记录库存保留日记账
            MtInvOnhandHoldJournal journal = new MtInvOnhandHoldJournal();
            journal.setOnhandHoldId(oldInvOnhandHold.getOnhandHoldId());
            journal.setSiteId(dto.getSiteId());
            journal.setMaterialId(dto.getMaterialId());
            journal.setLocatorId(dto.getLocatorId());
            journal.setLotCode(dto.getLotCode());
            journal.setOwnerType(dto.getOwnerType());
            journal.setOwnerId(dto.getOwnerId());
            journal.setChangeQuantity(-dto.getQuantity());
            journal.setHoldQuantity(oldInvOnhandHold.getHoldQuantity());
            journal.setHoldType(dto.getHoldType());
            journal.setOrderType(dto.getOrderType());
            journal.setOrderId(dto.getOrderId());
            journal.setEventId(eventId);
            journal.setEventBy(DetailsHelper.getUserDetails().getUserId());
            journal.setEventTime(new Date());
            journal.setTenantId(tenantId);
            mtInvOnhandHoldJournalRepository.insertSelective(journal);

            // 校验是否可以扣减库存
            MtInvOnhandQuantityVO8 updateOnhandVerifyVO = new MtInvOnhandQuantityVO8();
            updateOnhandVerifyVO.setSiteId(dto.getSiteId());
            updateOnhandVerifyVO.setMaterialId(dto.getMaterialId());
            updateOnhandVerifyVO.setLocatorId(dto.getLocatorId());
            updateOnhandVerifyVO.setLotCode(dto.getLotCode());
            updateOnhandVerifyVO.setOwnerType(dto.getOwnerType());
            updateOnhandVerifyVO.setOwnerId(dto.getOwnerId());
            updateOnhandVerifyVO.setChangeQuantity(dto.getQuantity());
            updateOnhandVerifyVO.setEventTypeCode("INV_HOLD_USE");
            MtInvOnhandQuantityVO2 verifyResult =
                            mtInvOnhandQuantityRepository.onhandQtyUpdateVerify(tenantId, updateOnhandVerifyVO);

            // 扣减库存现有量
            MtInvOnhandQuantityVO9 updateOnhandVO = new MtInvOnhandQuantityVO9();
            updateOnhandVO.setSiteId(dto.getSiteId());
            updateOnhandVO.setMaterialId(dto.getMaterialId());
            updateOnhandVO.setLocatorId(verifyResult.getActualChangeLocatorId());
            updateOnhandVO.setLotCode(dto.getLotCode());
            updateOnhandVO.setOwnerType(dto.getOwnerType());
            updateOnhandVO.setOwnerId(dto.getOwnerId());
            updateOnhandVO.setChangeQuantity(verifyResult.getActualChangeQty());
            updateOnhandVO.setEventId(eventId);
            mtInvOnhandQuantityRepository.onhandQtyUpdate(tenantId, updateOnhandVO);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void onhandReserveUseProcess(Long tenantId, MtInvOnhandHoldVO7 dto) {
        MtInvOnhandHoldVO onhandHoldAvailVO = new MtInvOnhandHoldVO();
        BeanUtils.copyProperties(dto, onhandHoldAvailVO);
        onhandReserveAvailableVerify(tenantId, onhandHoldAvailVO);

        MtInvOnhandHoldVO6 onhandHoldUseVO = new MtInvOnhandHoldVO6();
        BeanUtils.copyProperties(dto, onhandHoldUseVO);
        onhandReserveUse(tenantId, onhandHoldUseVO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void onhandReserveCreateProcess(Long tenantId, MtInvOnhandHoldVO8 dto) {
        MtInvOnhandHoldVO3 onhandHoldPropertyLimitVO = new MtInvOnhandHoldVO3();
        BeanUtils.copyProperties(dto, onhandHoldPropertyLimitVO);
        // onhandHoldPropertyLimitVO.setHoldQuantity(dto.getHoldQuantity());
        onhandReserveCreateVerify(tenantId, onhandHoldPropertyLimitVO);

        MtInvOnhandHoldVO2 onhandHoldCreateVO = new MtInvOnhandHoldVO2();
        BeanUtils.copyProperties(dto, onhandHoldCreateVO);
        // onhandHoldCreateVO.setHoldQuantity(dto.getHoldQuantity());
        onhandReserveCreate(tenantId, onhandHoldCreateVO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void onhandReserveReleaseProcess(Long tenantId, MtInvOnhandHoldVO9 dto) {
        MtInvOnhandHoldVO4 onhandHoldReleaseVerifyVO = new MtInvOnhandHoldVO4();
        BeanUtils.copyProperties(dto, onhandHoldReleaseVerifyVO);
        onhandHoldReleaseVerifyVO.setReleaseQuantity(dto.getReleaseQuantity());
        onhandReserveReleaseVerify(tenantId, onhandHoldReleaseVerifyVO);

        MtInvOnhandHoldVO5 onhandHoldReleaseVO = new MtInvOnhandHoldVO5();
        BeanUtils.copyProperties(dto, onhandHoldReleaseVO);
        onhandHoldReleaseVO.setReleaseQuantity(dto.getReleaseQuantity());
        onhandReserveRelease(tenantId, onhandHoldReleaseVO);
    }

    @Override
    public List<MtInvOnhandHoldVO1> onhandReserveAvailableBatchVerify(Long tenantId, List<MtInvOnhandHoldVO> dtos) {
        if (dtos.stream().anyMatch(c -> StringUtils.isEmpty(c.getSiteId()))) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "NVENTORY", "siteId", "【API:onhandReserveAvailableBatchVerify】"));
        }
        if (dtos.stream().anyMatch(c -> StringUtils.isEmpty(c.getMaterialId()))) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "NVENTORY", "materialId", "【API:onhandReserveAvailableBatchVerify】"));
        }
        if (dtos.stream().anyMatch(c -> StringUtils.isEmpty(c.getLocatorId()))) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "NVENTORY", "locatorId", "【API:onhandReserveAvailableBatchVerify】"));
        }
        if (dtos.stream().anyMatch(c -> c.getQuantity() == null)) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "NVENTORY", "quantity", "【API:onhandReserveAvailableBatchVerify】"));
        }
        if (dtos.stream().anyMatch(c -> StringUtils.isEmpty(c.getHoldType()))) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "NVENTORY", "holdType", "【API:onhandReserveAvailableBatchVerify】"));
        }

        List<String> siteIds = dtos.stream().map(MtInvOnhandHoldVO::getSiteId).distinct().collect(Collectors.toList());
        List<MtModSite> sites = mtModSiteRepository.siteBasicPropertyBatchGet(tenantId, siteIds);
        Map<String, MtModSite> mtModSiteMap = sites.stream().collect(Collectors.toMap(MtModSite::getSiteId, c -> c));
        for (String siteId : siteIds) {
            if (!mtModSiteMap.containsKey(siteId)
                            || !MtBaseConstants.YES.equalsIgnoreCase(mtModSiteMap.get(siteId).getEnableFlag())) {
                throw new MtException("MT_INVENTORY_0027",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0027",
                                                "NVENTORY", siteId.toString(),
                                                "【API:onhandReserveAvailableBatchVerify】"));
            }
        }

        List<String> materialIds =
                        dtos.stream().map(MtInvOnhandHoldVO::getMaterialId).distinct().collect(Collectors.toList());
        List<MtMaterialVO> materials = mtMaterialRepository.materialPropertyBatchGet(tenantId, materialIds);
        Map<String, MtMaterial> mtMaterialMap =
                        materials.stream().collect(Collectors.toMap(MtMaterial::getMaterialId, c -> c));
        for (String materialId : materialIds) {
            if (!mtMaterialMap.containsKey(materialId)
                            || !MtBaseConstants.YES.equalsIgnoreCase(mtMaterialMap.get(materialId).getEnableFlag())) {
                throw new MtException("MT_INVENTORY_0028",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0028",
                                                "NVENTORY", materialId.toString(),
                                                "【API:onhandReserveAvailableBatchVerify】"));
            }
        }

        List<MtMaterialSiteVO3> materialSites = new ArrayList<>();
        MtMaterialSiteVO3 mtMaterialSiteVO3 = null;
        for (MtInvOnhandHoldVO vo : dtos) {
            mtMaterialSiteVO3 = new MtMaterialSiteVO3();
            mtMaterialSiteVO3.setMaterialId(vo.getMaterialId());
            mtMaterialSiteVO3.setSiteId(vo.getSiteId());
            if (!materialSites.contains(mtMaterialSiteVO3)) {
                materialSites.add(mtMaterialSiteVO3);
            }
        }
        List<MtMaterialSiteVO4> relMaterialSites =
                        mtMaterialSiteRepository.materialSiteLimitRelationBatchGet(tenantId, materialSites);
        Map<MtMaterialSiteVO3, MtMaterialSiteVO4> relMaterialSiteMap = relMaterialSites.stream().collect(
                        Collectors.toMap(t -> new MtMaterialSiteVO3(t.getMaterialId(), t.getSiteId()), c -> c));
        List<String> errorMessages = new ArrayList<>();
        for (MtMaterialSiteVO3 vo : materialSites) {
            if (!relMaterialSiteMap.containsKey(vo)
                            || !MtBaseConstants.YES.equalsIgnoreCase(relMaterialSiteMap.get(vo).getEnableFlag())) {
                MtModSite mtModSite = mtModSiteMap.get(vo.getSiteId());
                MtMaterial mtMaterial = mtMaterialMap.get(vo.getMaterialId());
                String errorMessage = mtModSite.getSiteCode() + "+" + mtMaterial.getMaterialCode();
                errorMessages.add(errorMessage);
            }
        }
        if (CollectionUtils.isNotEmpty(errorMessages)) {
            throw new MtException("MT_INVENTORY_0029",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0029",
                                            "NVENTORY", String.join(",", errorMessages),
                                            "【API:onhandReserveAvailableBatchVerify】"));
        }

        List<String> locatorIds =
                        dtos.stream().map(MtInvOnhandHoldVO::getLocatorId).distinct().collect(Collectors.toList());
        List<MtModLocatorVO15> parentLocators =
                        mtModLocatorRepository.locatorListLimitInvCategoryLocatorGet(tenantId, locatorIds);
        Set<String> locatorIdList = new HashSet<>();
        Set<String> negativeLocatorIds = new HashSet<>();
        for (MtModLocatorVO15 vo : parentLocators) {
            if (StringUtils.isNotEmpty(vo.getInventoryLocatorId())) {
                locatorIdList.add(vo.getInventoryLocatorId());
                negativeLocatorIds.add(vo.getInventoryLocatorId());
            }
            if (StringUtils.isNotEmpty(vo.getLocatorId())) {
                locatorIdList.add(vo.getLocatorId());
            }
        }

        List<MtModLocator> locators =
                        mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, new ArrayList<>(locatorIdList));
        Map<String, MtModLocator> mtLocatorMap =
                        locators.stream().collect(Collectors.toMap(MtModLocator::getLocatorId, c -> c));
        for (String locatorId : locatorIdList) {
            if (!mtLocatorMap.containsKey(locatorId)
                            || !MtBaseConstants.YES.equalsIgnoreCase(mtLocatorMap.get(locatorId).getEnableFlag())) {
                throw new MtException("MT_INVENTORY_0030",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0030",
                                                "NVENTORY", locatorId.toString(),
                                                "【API:onhandReserveAvailableBatchVerify】"));
            }
            if (!MtBaseConstants.NO.equalsIgnoreCase(mtLocatorMap.get(locatorId).getNegativeFlag())
                            && negativeLocatorIds.contains(locatorId)) {
                negativeLocatorIds.remove(locatorId);
            }
        }

        List<String> inputOwnerTypeList = dtos.stream().map(MtInvOnhandHoldVO::getOwnerType)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(inputOwnerTypeList)) {
            MtGenTypeVO2 mtGenTypeVO = new MtGenTypeVO2();
            mtGenTypeVO.setModule("INVENTORY");
            mtGenTypeVO.setTypeGroup("INVENTORY_OWNER_TYPE");
            List<MtGenType> ownerTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO);
            List<String> ownerTypeCodes = ownerTypes.stream().map(MtGenType::getTypeCode).collect(Collectors.toList());
            for (String ownerType : inputOwnerTypeList) {
                if (!ownerTypeCodes.contains(ownerType)) {
                    throw new MtException("MT_INVENTORY_0019",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0019",
                                                    "NVENTORY", ownerType, "【API:onhandReserveAvailableBatchVerify】"));
                }
            }
        }

        List<String> inputHoldTypeList = dtos.stream().map(MtInvOnhandHoldVO::getHoldType)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(inputHoldTypeList)) {
            MtGenTypeVO2 mtGenTypeVO = new MtGenTypeVO2();
            mtGenTypeVO.setModule("INVENTORY");
            mtGenTypeVO.setTypeGroup("HOLD_TYPE");
            List<MtGenType> holdTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO);
            List<String> holdTypesCodes = holdTypes.stream().map(MtGenType::getTypeCode).collect(Collectors.toList());
            for (String holdType : inputHoldTypeList) {
                if (!holdTypesCodes.contains(holdType)) {
                    throw new MtException("MT_INVENTORY_0031",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0031",
                                                    "NVENTORY", holdType, holdTypesCodes.toString(),
                                                    "【API:onhandReserveAvailableBatchVerify】"));
                }
            }
        }
        MtGenTypeVO2 mtGenTypeVO = new MtGenTypeVO2();
        mtGenTypeVO.setModule("INVENTORY");
        mtGenTypeVO.setTypeGroup("RESERVE_OBJECT_TYPE");
        List<MtGenType> reserveObjectTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO);
        List<String> reserveObjectTypeCodes =
                        reserveObjectTypes.stream().map(MtGenType::getTypeCode).collect(Collectors.toList());
        for (MtInvOnhandHoldVO dto : dtos) {
            if ((StringUtils.isEmpty(dto.getOwnerType()) && StringUtils.isNotEmpty(dto.getOwnerId()))
                            || (StringUtils.isNotEmpty(dto.getOwnerType()) && StringUtils.isEmpty(dto.getOwnerId()))) {
                throw new MtException("MT_INVENTORY_0018",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0018",
                                                "NVENTORY", "ownerType", "ownerId",
                                                "【API:onhandReserveAvailableBatchVerify】"));
            }

            if (StringUtils.isNotEmpty(dto.getHoldType())) {
                if (!"ORDER".equals(dto.getHoldType())) {
                    if (StringUtils.isNotEmpty(dto.getOrderId()) || StringUtils.isNotEmpty(dto.getOrderType())) {
                        throw new MtException("MT_INVENTORY_0014",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INVENTORY_0014", "NVENTORY",
                                                        "【API:onhandReserveAvailableBatchVerify】"));
                    }
                } else {
                    if (StringUtils.isEmpty(dto.getOrderId()) || StringUtils.isEmpty(dto.getOrderType())) {
                        throw new MtException("MT_INVENTORY_0006",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_INVENTORY_0006", "NVENTORY",
                                                        "【API:onhandReserveAvailableBatchVerify】"));
                    }
                    if (!reserveObjectTypeCodes.contains(dto.getOrderType())) {
                        throw new MtException("MT_INVENTORY_0032", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_INVENTORY_0032", "NVENTORY", dto.getOrderType(),
                                        reserveObjectTypeCodes.toString(), "【API:onhandReserveAvailableBatchVerify】"));
                    }
                }
            }
        }

        List<MtInvOnhandHoldVO1> result = new ArrayList<MtInvOnhandHoldVO1>();
        MtInvOnhandHoldVO1 mtInvOnhandHoldVO1 = null;

        List<String> emptyInventoryLocatorIds =
                        parentLocators.stream().filter(c -> StringUtils.isEmpty(c.getInventoryLocatorId()))
                                        .map(MtModLocatorVO15::getLocatorId).collect(Collectors.toList());
        List<MtInvOnhandHoldVO> emptyInventoryLocatorDtos = dtos.stream()
                        .filter(c -> emptyInventoryLocatorIds.contains(c.getLocatorId())).collect(Collectors.toList());
        Map<String, BigDecimal> emptyInventoryLocatorGroupMap = emptyInventoryLocatorDtos.stream()
                        .collect(Collectors.groupingBy(c -> c.getSiteId() + "^" + c.getMaterialId() + "^"
                                        + c.getLocatorId() + "^" + (c.getLotCode() == null ? "" : c.getLotCode()) + "^"
                                        + (c.getOwnerType() == null ? "" : c.getOwnerType()) + "^"
                                        + (c.getOwnerId() == null ? 0L : c.getOwnerId()) + "^" + c.getHoldType() + "^"
                                        + (c.getOrderType() == null ? "" : c.getOrderType()) + "^"
                                        + (c.getOrderId() == null ? 0L : c.getOrderId()),
                                        CollectorsUtil.summingBigDecimal(c -> BigDecimal.valueOf(c.getQuantity()))));
        for (Map.Entry<String, BigDecimal> entry : emptyInventoryLocatorGroupMap.entrySet()) {
            String[] array = entry.getKey().split("\\^", -1);
            mtInvOnhandHoldVO1 = new MtInvOnhandHoldVO1();
            mtInvOnhandHoldVO1.setSiteId(array[0]);
            mtInvOnhandHoldVO1.setMaterialId(array[1]);
            mtInvOnhandHoldVO1.setActualChangeLocatorId(array[2]);
            mtInvOnhandHoldVO1.setLotCode(array[3]);
            mtInvOnhandHoldVO1.setOwnerType(array[4]);
            mtInvOnhandHoldVO1.setOwnerId(array[5]);
            mtInvOnhandHoldVO1.setHoldType(array[6]);
            mtInvOnhandHoldVO1.setOrderType(array[7]);
            mtInvOnhandHoldVO1.setOrderId(array[8]);
            mtInvOnhandHoldVO1.setActualChangeQuantity(0.0D);
            result.add(mtInvOnhandHoldVO1);
        }

        List<MtModLocatorVO15> unEmptyInventoryLocators = parentLocators.stream()
                        .filter(c -> StringUtils.isNotEmpty(c.getInventoryLocatorId())).collect(Collectors.toList());
        Map<String, String> unEmptyInventoryLocatorMap = unEmptyInventoryLocators.stream().collect(
                        Collectors.toMap(MtModLocatorVO15::getLocatorId, MtModLocatorVO15::getInventoryLocatorId));
        List<MtInvOnhandHoldVO> unEmptyInventoryLocatorDtos =
                        dtos.stream().filter(c -> unEmptyInventoryLocatorMap.containsKey(c.getLocatorId()))
                                        .collect(Collectors.toList());
        unEmptyInventoryLocatorDtos.forEach(c -> {
            c.setLocatorId(unEmptyInventoryLocatorMap.get(c.getLocatorId()));
        });
        Map<String, BigDecimal> unEmptyInventoryLocatorGroupMap = unEmptyInventoryLocatorDtos.stream()
                        .collect(Collectors.groupingBy(c -> c.getSiteId() + "^" + c.getMaterialId() + "^"
                                        + c.getLocatorId() + "^" + (c.getLotCode() == null ? "" : c.getLotCode()) + "^"
                                        + (c.getOwnerType() == null ? "" : c.getOwnerType()) + "^"
                                        + (c.getOwnerId() == null ? 0L : c.getOwnerId()) + "^" + c.getHoldType() + "^"
                                        + (c.getOrderType() == null ? "" : c.getOrderType()) + "^"
                                        + (c.getOrderId() == null ? 0L : c.getOrderId()),
                                        CollectorsUtil.summingBigDecimal(c -> BigDecimal.valueOf(c.getQuantity()))));
        for (Map.Entry<String, BigDecimal> entry : unEmptyInventoryLocatorGroupMap.entrySet()) {
            String[] array = entry.getKey().split("\\^", -1);
            mtInvOnhandHoldVO1 = new MtInvOnhandHoldVO1();
            mtInvOnhandHoldVO1.setSiteId(array[0]);
            mtInvOnhandHoldVO1.setMaterialId(array[1]);
            mtInvOnhandHoldVO1.setActualChangeLocatorId(array[2]);
            mtInvOnhandHoldVO1.setLotCode(array[3]);
            mtInvOnhandHoldVO1.setOwnerType(array[4]);
            mtInvOnhandHoldVO1.setOwnerId(array[5]);
            mtInvOnhandHoldVO1.setHoldType(array[6]);
            mtInvOnhandHoldVO1.setOrderType(array[7]);
            mtInvOnhandHoldVO1.setOrderId(array[8]);
            mtInvOnhandHoldVO1.setActualChangeQuantity(entry.getValue().doubleValue());
            if (result.contains(mtInvOnhandHoldVO1)) {
                int index = result.indexOf(mtInvOnhandHoldVO1);
                MtInvOnhandHoldVO1 tmpInvOnhandHoldVO1 = result.get(index);
                tmpInvOnhandHoldVO1.setActualChangeQuantity(BigDecimal
                                .valueOf(tmpInvOnhandHoldVO1.getActualChangeQuantity())
                                .add(BigDecimal.valueOf(mtInvOnhandHoldVO1.getActualChangeQuantity())).doubleValue());
            } else {
                result.add(mtInvOnhandHoldVO1);
            }
        }

        Map<String, String> materialUomMap = mtMaterialMap.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getPrimaryUomId()));
        List<MtUomVO7> uomList = new ArrayList<>();
        MtUomVO7 mtUomVO7 = null;
        for (MtInvOnhandHoldVO1 vo : result) {
            if (materialUomMap.containsKey(vo.getMaterialId())) {
                mtUomVO7 = new MtUomVO7();
                mtUomVO7.setSourceUomId(materialUomMap.get(vo.getMaterialId()));
                mtUomVO7.setSourceValue(vo.getActualChangeQuantity());
                uomList.add(mtUomVO7);
            }
        }
        List<MtUomVO8> mtUomList = mtUomRepository.uomDecimalBatchProcess(tenantId, uomList);
        Map<OnhandHoldQtyUomTuple, MtUomVO8> uomQtyDecimalMap = mtUomList.stream().distinct().collect(Collectors
                        .toMap(t -> new OnhandHoldQtyUomTuple(t.getSourceValue(), t.getSourceUomId()), t -> t));

        for (MtInvOnhandHoldVO1 vo : result) {
            if (materialUomMap.containsKey(vo.getMaterialId())) {
                String primaryUomId = materialUomMap.get(vo.getMaterialId());
                MtUomVO8 mtUomVO8 = uomQtyDecimalMap
                                .get(new OnhandHoldQtyUomTuple(vo.getActualChangeQuantity(), primaryUomId));
                if (mtUomVO8 != null) {
                    vo.setActualChangeQuantity(mtUomVO8.getTargetValue());
                }
            }
        }

        List<MtInvOnhandHoldVO1> negativeDtos =
                        result.stream().filter(c -> negativeLocatorIds.contains(c.getActualChangeLocatorId()))
                                        .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(negativeDtos)) {
            List<List<MtInvOnhandHold>> resultList = new ArrayList<>();
            List<Future<List<MtInvOnhandHold>>> futureList = new ArrayList<>();
            List<List<MtInvOnhandHoldVO1>> batchList = Lists.partition(negativeDtos, 100);
            for (List<MtInvOnhandHoldVO1> ever : batchList) {
                Condition.Builder builder = Condition.builder(MtInvOnhandHold.class);
                for (MtInvOnhandHoldVO1 vo : ever) {
                    builder.orWhere(Sqls.custom().andEqualTo(MtInvOnhandHold.FIELD_TENANT_ID, tenantId)
                                    .andEqualTo(MtInvOnhandHold.FIELD_SITE_ID, vo.getSiteId(), true)
                                    .andEqualTo(MtInvOnhandHold.FIELD_MATERIAL_ID, vo.getMaterialId(), true)
                                    .andEqualTo(MtInvOnhandHold.FIELD_LOCATOR_ID, vo.getActualChangeLocatorId(), true)
                                    .andEqualTo(MtInvOnhandHold.FIELD_LOT_CODE,
                                                    vo.getLotCode() == null ? "" : vo.getLotCode(), true)
                                    .andEqualTo(MtInvOnhandHold.FIELD_OWNER_TYPE,
                                                    vo.getOwnerType() == null ? "" : vo.getOwnerType(), true)
                                    .andEqualTo(MtInvOnhandHold.FIELD_OWNER_ID,
                                                    vo.getOwnerId() == null ? 0L : vo.getOwnerId(), true)
                                    .andEqualTo(MtInvOnhandHold.FIELD_HOLD_TYPE, vo.getHoldType(), true)
                                    .andEqualTo(MtInvOnhandHold.FIELD_ORDER_TYPE,
                                                    vo.getOrderType() == null ? "" : vo.getOrderType(), true)
                                    .andEqualTo(MtInvOnhandHold.FIELD_ORDER_ID,
                                                    vo.getOrderId() == null ? 0L : vo.getOrderId(), true));
                }
                Future<List<MtInvOnhandHold>> future = poolExecutor.submit(() -> {
                    SecurityTokenHelper.close();
                    return mtInvOnhandHoldMapper.selectByCondition(builder.build());
                });
                futureList.add(future);
                if (futureList.size() == 10) {
                    for (Future<List<MtInvOnhandHold>> tmpFuture : futureList) {
                        try {
                            resultList.add(tmpFuture.get());
                        } catch (InterruptedException | ExecutionException e) {
                            tmpFuture.cancel(true);
                        }
                    }
                    futureList.clear();
                }
            }

            for (Future<List<MtInvOnhandHold>> tmpFuture : futureList) {
                try {
                    resultList.add(tmpFuture.get());
                } catch (InterruptedException | ExecutionException e) {
                    tmpFuture.cancel(true);
                }
            }

            Map<String, BigDecimal> map = new HashMap<>();
            StringBuilder sb = null;
            if (CollectionUtils.isEmpty(resultList)) {
                for (MtInvOnhandHoldVO1 vo : negativeDtos) {
                    sb = new StringBuilder();
                    sb.append(vo.getSiteId()).append(vo.getMaterialId()).append(vo.getActualChangeLocatorId())
                                    .append(vo.getLotCode() == null ? "" : vo.getLotCode())
                                    .append(vo.getOwnerType() == null ? "" : vo.getOwnerType())
                                    .append(vo.getOwnerId() == null ? 0L : vo.getOwnerId()).append(vo.getHoldType())
                                    .append(vo.getOrderType() == null ? "" : vo.getOrderType())
                                    .append(vo.getOrderId() == null ? 0L : vo.getOrderId());
                    map.put(sb.toString(), BigDecimal.ZERO);
                }
            } else {
                for (List<MtInvOnhandHold> mtInvOnhandHolds : resultList) {
                    for (MtInvOnhandHold mtInvOnhandHold : mtInvOnhandHolds) {
                        sb = new StringBuilder();
                        sb.append(mtInvOnhandHold.getSiteId()).append(mtInvOnhandHold.getMaterialId())
                                        .append(mtInvOnhandHold.getLocatorId())
                                        .append(mtInvOnhandHold.getLotCode() == null ? ""
                                                        : mtInvOnhandHold.getLotCode())
                                        .append(mtInvOnhandHold.getOwnerType() == null ? ""
                                                        : mtInvOnhandHold.getOwnerType())
                                        .append(mtInvOnhandHold.getOwnerId() == null ? 0L
                                                        : mtInvOnhandHold.getOwnerId())
                                        .append(mtInvOnhandHold.getHoldType())
                                        .append(mtInvOnhandHold.getOrderType() == null ? ""
                                                        : mtInvOnhandHold.getOrderType())
                                        .append(mtInvOnhandHold.getOrderId() == null ? 0L
                                                        : mtInvOnhandHold.getOrderId());
                        if (!map.containsKey(sb.toString())) {
                            map.put(sb.toString(), BigDecimal.valueOf(mtInvOnhandHold.getHoldQuantity()));
                        } else {
                            BigDecimal qty = map.get(sb.toString());
                            qty = qty.add(BigDecimal.valueOf(mtInvOnhandHold.getHoldQuantity()));
                            map.put(sb.toString(), qty);
                        }
                    }
                }

                if (map.size() != negativeDtos.size()) {
                    for (MtInvOnhandHoldVO1 vo : negativeDtos) {
                        sb = new StringBuilder();
                        sb.append(vo.getSiteId()).append(vo.getMaterialId()).append(vo.getActualChangeLocatorId())
                                        .append(vo.getLotCode() == null ? "" : vo.getLotCode())
                                        .append(vo.getOwnerType() == null ? "" : vo.getOwnerType())
                                        .append(vo.getOwnerId() == null ? 0L : vo.getOwnerId()).append(vo.getHoldType())
                                        .append(vo.getOrderType() == null ? "" : vo.getOrderType())
                                        .append(vo.getOrderId() == null ? 0L : vo.getOrderId());
                        if (!map.containsKey(sb.toString())) {
                            map.put(sb.toString(), BigDecimal.ZERO);
                        }
                    }
                }
            }
            for (MtInvOnhandHoldVO1 vo : negativeDtos) {
                sb = new StringBuilder();
                sb.append(vo.getSiteId()).append(vo.getMaterialId()).append(vo.getActualChangeLocatorId())
                                .append(vo.getLotCode() == null ? "" : vo.getLotCode())
                                .append(vo.getOwnerType() == null ? "" : vo.getOwnerType())
                                .append(vo.getOwnerId() == null ? 0L : vo.getOwnerId()).append(vo.getHoldType())
                                .append(vo.getOrderType() == null ? "" : vo.getOrderType())
                                .append(vo.getOrderId() == null ? 0L : vo.getOrderId());
                if (map.containsKey(sb.toString())) {
                    BigDecimal holdQuantity = map.get(sb.toString());
                    if (BigDecimal.valueOf(vo.getActualChangeQuantity()).compareTo(holdQuantity) > 0) {
                        throw new MtException("MT_INVENTORY_0013", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_INVENTORY_0013", mtModSiteMap.get(vo.getSiteId()).getSiteCode(),
                                        mtLocatorMap.get(vo.getActualChangeLocatorId()).getLocatorCode(),
                                        mtMaterialMap.get(vo.getMaterialId()).getMaterialCode(), vo.getLotCode(),
                                        vo.getOwnerType() + "+" + vo.getOwnerId(), vo.getHoldType(),
                                        vo.getOrderType() + "+" + vo.getOrderId(), holdQuantity.toString(),
                                        vo.getActualChangeQuantity().toString(),
                                        "API:onhandReserveAvailableBatchVerify"));
                    }
                }
            }
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void onhandReserveUseBatchProcess(Long tenantId, MtInvOnhandHoldVO18 mtInvOnhandHoldVO18) {
        List<MtInvOnhandHoldVO1> validateList =
                        onhandReserveAvailableBatchVerify(tenantId, mtInvOnhandHoldVO18.getOnhandReserveList());

        MtInvOnhandHoldVO18 dto = new MtInvOnhandHoldVO18();
        List<MtInvOnhandHoldVO> onhandReserveList = new ArrayList<>();
        dto.setEventId(mtInvOnhandHoldVO18.getEventId());
        dto.setEventLocatorId(mtInvOnhandHoldVO18.getEventLocatorId());
        dto.setEventRequestId(mtInvOnhandHoldVO18.getEventRequestId());
        dto.setShiftCode(mtInvOnhandHoldVO18.getShiftCode());
        dto.setShiftDate(mtInvOnhandHoldVO18.getShiftDate());
        dto.setWorkcellId(mtInvOnhandHoldVO18.getWorkcellId());
        dto.setOnhandReserveList(onhandReserveList);

        MtInvOnhandHoldVO mtInvOnhandHoldVO = null;
        for (MtInvOnhandHoldVO1 mtInvOnhandHoldVO1 : validateList) {
            mtInvOnhandHoldVO = new MtInvOnhandHoldVO();
            mtInvOnhandHoldVO.setHoldType(mtInvOnhandHoldVO1.getHoldType());
            mtInvOnhandHoldVO.setLocatorId(mtInvOnhandHoldVO1.getActualChangeLocatorId());
            mtInvOnhandHoldVO.setLotCode(mtInvOnhandHoldVO1.getLotCode());
            mtInvOnhandHoldVO.setMaterialId(mtInvOnhandHoldVO1.getMaterialId());
            mtInvOnhandHoldVO.setOrderId(mtInvOnhandHoldVO1.getOrderId());
            mtInvOnhandHoldVO.setOrderType(mtInvOnhandHoldVO1.getOrderType());
            mtInvOnhandHoldVO.setOwnerId(mtInvOnhandHoldVO1.getOwnerId());
            mtInvOnhandHoldVO.setOwnerType(mtInvOnhandHoldVO1.getOwnerType());
            mtInvOnhandHoldVO.setQuantity(mtInvOnhandHoldVO1.getActualChangeQuantity());
            mtInvOnhandHoldVO.setSiteId(mtInvOnhandHoldVO1.getSiteId());
            dto.getOnhandReserveList().add(mtInvOnhandHoldVO);
        }

        self().onhandReserveBatchUse(tenantId, dto);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void onhandReserveBatchUse(Long tenantId, MtInvOnhandHoldVO18 mtInvOnhandHoldVO18) {
        List<MtInvOnhandHoldVO> onhandReserveList = mtInvOnhandHoldVO18.getOnhandReserveList();
        if (CollectionUtils.isEmpty(onhandReserveList)) {
            return;
        } else {
            onhandReserveList.forEach(c -> {
                if (c.getQuantity() == null) {
                    c.setQuantity(0.0D);
                }
            });
        }

        if (onhandReserveList.stream()
                        .allMatch(c -> BigDecimal.valueOf(c.getQuantity()).compareTo(BigDecimal.ZERO) == 0)) {
            return;
        }

        Map<String, BigDecimal> sumQtyMap = onhandReserveList.stream()
                        .collect(Collectors.groupingBy(c -> String.valueOf(c.getSiteId())
                                        + String.valueOf(c.getMaterialId()) + String.valueOf(c.getLocatorId())
                                        + (c.getLotCode() == null ? "" : c.getLotCode())
                                        + (c.getOwnerType() == null ? "" : c.getOwnerType())
                                        + (c.getOwnerId() == null ? 0L : c.getOwnerId()) + c.getHoldType()
                                        + (c.getOrderType() == null ? "" : c.getOrderType())
                                        + (c.getOrderId() == null ? 0L : c.getOrderId()),
                                        CollectorsUtil.summingBigDecimal(c -> BigDecimal.valueOf(c.getQuantity()))));
        sumQtyMap = sumQtyMap.entrySet().stream().filter(e -> e.getValue().compareTo(BigDecimal.ZERO) != 0)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<String, List<MtInvOnhandHoldVO>> map = onhandReserveList.stream()
                        .collect(Collectors.groupingBy(c -> String.valueOf(c.getSiteId())
                                        + String.valueOf(c.getMaterialId()) + String.valueOf(c.getLocatorId())
                                        + (c.getLotCode() == null ? "" : c.getLotCode())
                                        + (c.getOwnerType() == null ? "" : c.getOwnerType())
                                        + (c.getOwnerId() == null ? 0L : c.getOwnerId()) + c.getHoldType()
                                        + (c.getOrderType() == null ? "" : c.getOrderType())
                                        + (c.getOrderId() == null ? 0L : c.getOrderId())));
        Map<String, MtInvOnhandHoldVO> validMap = new HashMap<>();
        MtInvOnhandHoldVO mtInvOnhandHoldVO = null;
        for (Map.Entry<String, List<MtInvOnhandHoldVO>> entry : map.entrySet()) {
            mtInvOnhandHoldVO = new MtInvOnhandHoldVO();
            for (MtInvOnhandHoldVO vo : entry.getValue()) {
                mtInvOnhandHoldVO.setHoldType(vo.getHoldType());
                mtInvOnhandHoldVO.setLocatorId(vo.getLocatorId());
                mtInvOnhandHoldVO.setLotCode(vo.getLotCode());
                mtInvOnhandHoldVO.setMaterialId(vo.getMaterialId());
                mtInvOnhandHoldVO.setOrderId(vo.getOrderId());
                mtInvOnhandHoldVO.setOrderType(vo.getOrderType());
                mtInvOnhandHoldVO.setOwnerId(vo.getOwnerId());
                mtInvOnhandHoldVO.setOwnerType(vo.getOwnerType());
                mtInvOnhandHoldVO.setSiteId(vo.getSiteId());
                mtInvOnhandHoldVO.setQuantity(BigDecimal
                                .valueOf(mtInvOnhandHoldVO.getQuantity() == null ? 0.0D
                                                : mtInvOnhandHoldVO.getQuantity())
                                .add(BigDecimal.valueOf(vo.getQuantity())).doubleValue());
            }
            if (BigDecimal.valueOf(mtInvOnhandHoldVO.getQuantity()).compareTo(BigDecimal.ZERO) != 0) {
                validMap.put(entry.getKey(), mtInvOnhandHoldVO);
            }
        }

        List<MtInvOnhandHoldVO> validList = new ArrayList<>(validMap.values());
        List<List<MtInvOnhandHold>> resultList = new ArrayList<>();
        List<Future<List<MtInvOnhandHold>>> futureList = new ArrayList<>();
        List<List<MtInvOnhandHoldVO>> batchList = Lists.partition(validList, 100);
        for (List<MtInvOnhandHoldVO> ever : batchList) {
            Condition.Builder builder = Condition.builder(MtInvOnhandHold.class);
            for (MtInvOnhandHoldVO vo : ever) {
                builder.orWhere(Sqls.custom().andEqualTo(MtInvOnhandHold.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(MtInvOnhandHold.FIELD_SITE_ID, vo.getSiteId(), true)
                                .andEqualTo(MtInvOnhandHold.FIELD_MATERIAL_ID, vo.getMaterialId(), true)
                                .andEqualTo(MtInvOnhandHold.FIELD_LOCATOR_ID, vo.getLocatorId(), true)
                                .andEqualTo(MtInvOnhandHold.FIELD_LOT_CODE,
                                                vo.getLotCode() == null ? "" : vo.getLotCode(), true)
                                .andEqualTo(MtInvOnhandHold.FIELD_OWNER_TYPE,
                                                vo.getOwnerType() == null ? "" : vo.getOwnerType(), true)
                                .andEqualTo(MtInvOnhandHold.FIELD_OWNER_ID,
                                                vo.getOwnerId() == null ? 0L : vo.getOwnerId(), true)
                                .andEqualTo(MtInvOnhandHold.FIELD_HOLD_TYPE, vo.getHoldType(), true)
                                .andEqualTo(MtInvOnhandHold.FIELD_ORDER_TYPE,
                                                vo.getOrderType() == null ? "" : vo.getOrderType(), true)
                                .andEqualTo(MtInvOnhandHold.FIELD_ORDER_ID,
                                                vo.getOrderId() == null ? 0L : vo.getOrderId(), true));
            }
            Future<List<MtInvOnhandHold>> future = poolExecutor.submit(() -> {
                SecurityTokenHelper.close();
                return mtInvOnhandHoldMapper.selectByCondition(builder.build());
            });
            futureList.add(future);
            if (futureList.size() == 10) {
                for (Future<List<MtInvOnhandHold>> tmpFuture : futureList) {
                    try {
                        resultList.add(tmpFuture.get());
                    } catch (InterruptedException | ExecutionException e) {
                        tmpFuture.cancel(true);
                    }
                }
                futureList.clear();
            }
        }

        for (Future<List<MtInvOnhandHold>> tmpFuture : futureList) {
            try {
                resultList.add(tmpFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                tmpFuture.cancel(true);
            }
        }

        if (CollectionUtils.isEmpty(resultList)) {
            return;
        }

        List<String> holdDeleteList = new ArrayList<>();
        List<String> holdUpdateList = new ArrayList<>();
        List<MtInvOnhandHoldJournal> holdJournalList = new ArrayList<>();
        StringBuilder sb = null;
        MtInvOnhandHoldJournal journal = null;
        Date now = new Date();

        for (List<MtInvOnhandHold> mtInvOnhandHolds : resultList) {
            for (MtInvOnhandHold mtInvOnhandHold : mtInvOnhandHolds) {
                mtInvOnhandHold.setTenantId(tenantId);
                sb = new StringBuilder();
                sb.append(mtInvOnhandHold.getSiteId()).append(mtInvOnhandHold.getMaterialId())
                                .append(mtInvOnhandHold.getLocatorId())
                                .append(mtInvOnhandHold.getLotCode() == null ? "" : mtInvOnhandHold.getLotCode())
                                .append(mtInvOnhandHold.getOwnerType() == null ? "" : mtInvOnhandHold.getOwnerType())
                                .append(mtInvOnhandHold.getOwnerId() == null ? 0L : mtInvOnhandHold.getOwnerId())
                                .append(mtInvOnhandHold.getHoldType())
                                .append(mtInvOnhandHold.getOrderType() == null ? "" : mtInvOnhandHold.getOrderType())
                                .append(mtInvOnhandHold.getOrderId() == null ? 0L : mtInvOnhandHold.getOrderId());
                if (sumQtyMap.containsKey(sb.toString())) {
                    BigDecimal newHoldQty = BigDecimal.valueOf(mtInvOnhandHold.getHoldQuantity())
                                    .subtract(sumQtyMap.get(sb.toString()));

                    if (newHoldQty.compareTo(BigDecimal.ZERO) == 0) {
                        holdDeleteList.addAll(customDbRepository.getDeleteSql(mtInvOnhandHold));
                    } else {
                        mtInvOnhandHold.setHoldQuantity(newHoldQty.doubleValue());
                        holdUpdateList.addAll(customDbRepository.getUpdateSql(mtInvOnhandHold));
                    }

                    if (validMap.containsKey(sb.toString())) {
                        MtInvOnhandHoldVO vo = validMap.get(sb.toString());
                        journal = new MtInvOnhandHoldJournal();
                        journal.setOnhandHoldId(mtInvOnhandHold.getOnhandHoldId());
                        journal.setSiteId(vo.getSiteId());
                        journal.setMaterialId(vo.getMaterialId());
                        journal.setLocatorId(vo.getLocatorId());
                        journal.setLotCode(vo.getLotCode());
                        journal.setOwnerType(vo.getOwnerType());
                        journal.setOwnerId(vo.getOwnerId());
                        journal.setChangeQuantity(-vo.getQuantity());
                        journal.setHoldQuantity(mtInvOnhandHold.getHoldQuantity());
                        journal.setHoldType(vo.getHoldType());
                        journal.setOrderType(vo.getOrderType());
                        journal.setOrderId(vo.getOrderId());
                        journal.setEventBy(MtUserClient.getCurrentUserId());
                        journal.setEventTime(now);
                        journal.setTenantId(tenantId);
                        journal.setCreatedBy(MtUserClient.getCurrentUserId());
                        journal.setCreationDate(now);
                        journal.setLastUpdateDate(now);
                        journal.setLastUpdatedBy(MtUserClient.getCurrentUserId());
                        journal.setObjectVersionNumber(1L);
                        holdJournalList.add(journal);
                    }
                }
            }
        }

        if (CollectionUtils.isNotEmpty(holdDeleteList)) {
            this.jdbcTemplate.batchUpdate(holdDeleteList.toArray(new String[holdDeleteList.size()]));
        }
        if (CollectionUtils.isNotEmpty(holdUpdateList)) {
            this.jdbcTemplate.batchUpdate(holdUpdateList.toArray(new String[holdUpdateList.size()]));
        }

        String eventId = null;
        if (StringUtils.isNotEmpty(mtInvOnhandHoldVO18.getEventId())) {
            eventId = mtInvOnhandHoldVO18.getEventId();
        } else {
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            if (mtInvOnhandHoldVO18.getShiftDate() != null) {
                eventCreateVO.setShiftDate(mtInvOnhandHoldVO18.getShiftDate());
            }
            eventCreateVO.setEventTypeCode("INV_HOLD_USE");
            eventCreateVO.setEventRequestId(mtInvOnhandHoldVO18.getEventRequestId());
            eventCreateVO.setLocatorId(mtInvOnhandHoldVO18.getEventLocatorId());

            eventCreateVO.setShiftCode(mtInvOnhandHoldVO18.getShiftCode());
            eventCreateVO.setWorkcellId(mtInvOnhandHoldVO18.getWorkcellId());
            eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        }

        // 判断唯一索引
        List<String> onHandHoldIds = resultList.stream().flatMap(Collection::stream)
                        .map(MtInvOnhandHold::getOnhandHoldId).collect(toList());
        if (CollectionUtils.isNotEmpty(onHandHoldIds)) {
            List<MtInvOnhandHoldJournal> mtInvOnhandHoldJournals = mtInvOnhandHoldJournalRepository
                            .selectByCondition(Condition.builder(MtInvOnhandHoldJournal.class).andWhere(
                                            Sqls.custom().andEqualTo(MtInvOnhandHoldJournal.FIELD_TENANT_ID, tenantId)
                                                            .andEqualTo(MtInvOnhandHoldJournal.FIELD_EVENT_ID, eventId))
                                            .andWhere(Sqls.custom().andIn(MtInvOnhandHoldJournal.FIELD_ONHAND_HOLD_ID,
                                                            onHandHoldIds))
                                            .build());
            if (CollectionUtils.isNotEmpty(mtInvOnhandHoldJournals)) {
                throw new MtException("MT_INVENTORY_0033", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_INVENTORY_0033", "INVENTORY", eventId.toString(), "【API:onhandReserveBatchUse】"));
            }
        }

        if (CollectionUtils.isNotEmpty(holdJournalList)) {
            List<String> holdJournalSqlList = new ArrayList<>();
            List<String> kidList =
                            customDbRepository.getNextKeys("mt_inv_onhand_hold_journal_s", holdJournalList.size());
            List<String> cidList =
                            customDbRepository.getNextKeys("mt_inv_onhand_hold_journal_cid_s", holdJournalList.size());

            for (MtInvOnhandHoldJournal mtInvOnhandHoldJournal : holdJournalList) {
                mtInvOnhandHoldJournal.setOnhandHoldJournalId(kidList.remove(0));
                mtInvOnhandHoldJournal.setCid(Long.valueOf(cidList.remove(0)));
                mtInvOnhandHoldJournal.setEventId(eventId);
                holdJournalSqlList.addAll(customDbRepository.getInsertSql(mtInvOnhandHoldJournal));
            }
            this.jdbcTemplate.batchUpdate(holdJournalSqlList.toArray(new String[holdJournalSqlList.size()]));
        }

        MtInvOnhandQuantityVO16 mtInvOnhandQuantityVO16 = new MtInvOnhandQuantityVO16();
        mtInvOnhandQuantityVO16.setEventId(eventId);
        List<MtInvOnhandQuantityVO13> onhandList = new ArrayList<>();
        for (MtInvOnhandHoldVO vo : validList) {
            MtInvOnhandQuantityVO13 mtInvOnhandQuantityVO13 = new MtInvOnhandQuantityVO13();
            mtInvOnhandQuantityVO13.setSiteId(vo.getSiteId());
            mtInvOnhandQuantityVO13.setMaterialId(vo.getMaterialId());
            mtInvOnhandQuantityVO13.setLocatorId(vo.getLocatorId());
            mtInvOnhandQuantityVO13.setLotCode(vo.getLotCode());
            mtInvOnhandQuantityVO13.setOwnerType(vo.getOwnerType());
            mtInvOnhandQuantityVO13.setOwnerId(vo.getOwnerId());
            mtInvOnhandQuantityVO13.setChangeQuantity(vo.getQuantity());
            onhandList.add(mtInvOnhandQuantityVO13);
        }
        mtInvOnhandQuantityVO16.setOnhandList(onhandList);
        mtInvOnhandQuantityRepository.onhandQtyUpdateBatchProcess(tenantId, mtInvOnhandQuantityVO16);
    }

    private static class OnhandHoldQtyUomTuple implements Serializable {

        private static final long serialVersionUID = -6525671353844838581L;
        private Double qty;
        private String uomId;

        public OnhandHoldQtyUomTuple(Double qty, String uomId) {
            this.qty = qty;
            this.uomId = uomId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            OnhandHoldQtyUomTuple that = (OnhandHoldQtyUomTuple) o;
            return BigDecimal.valueOf(qty).compareTo(BigDecimal.valueOf(that.qty)) == 0
                            && uomId.compareTo(that.uomId) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(qty, uomId);
        }
    }
}
