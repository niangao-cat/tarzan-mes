package tarzan.inventory.infra.repository.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.SequenceInfo;
import io.tarzan.common.domain.util.*;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import tarzan.general.domain.entity.MtEventType;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventTypeRepository;
import tarzan.general.domain.vo.MtEventTypeVO;
import tarzan.general.domain.vo.MtEventVO1;
import tarzan.general.infra.mapper.MtEventTypeMapper;
import tarzan.inventory.domain.entity.MtInvJournal;
import tarzan.inventory.domain.entity.MtInvOnhandHold;
import tarzan.inventory.domain.entity.MtInvOnhandQuantity;
import tarzan.inventory.domain.repository.MtInvJournalRepository;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.trans.MtInvOnhandQuantityTransferMapper;
import tarzan.inventory.domain.vo.*;
import tarzan.inventory.infra.mapper.MtInvOnhandHoldMapper;
import tarzan.inventory.infra.mapper.MtInvOnhandQuantityMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.material.domain.vo.MtMaterialSiteVO3;
import tarzan.material.domain.vo.MtMaterialSiteVO4;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModLocatorOrgRelRepository;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO;
import tarzan.modeling.domain.vo.MtModLocatorVO15;

/**
 * ????????? ???????????????
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:53
 */
@Component
public class MtInvOnhandQuantityRepositoryImpl extends BaseRepositoryImpl<MtInvOnhandQuantity>
                implements MtInvOnhandQuantityRepository {

    @Autowired
    private MtInvOnhandQuantityMapper mtInvOnhandQuantityMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModLocatorOrgRelRepository iMtModLocatorOrganizationRelService;

    @Autowired
    private MtModSiteRepository iMtModSiteService;

    @Autowired
    private MtMaterialRepository iMtMaterialService;

    @Autowired
    private MtMaterialSiteRepository iMtMaterialSiteRepository;

    @Autowired
    private MtModLocatorRepository iMtModLocatorService;

    @Autowired
    private MtEventTypeMapper mtEventTypeMapper;

    @Autowired
    private MtInvOnhandHoldMapper mtInvOnhandHoldMapper;

    @Autowired
    private MtInvJournalRepository iMtInvJournalService;

    @Autowired
    private MtEventRepository mtEventService;

    @Autowired
    private MtGenTypeRepository mtGenTypeService;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtModSiteRepository iMtModSiteRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtEventTypeRepository mtEventTypeRepository;

    @Autowired
    private MtInvOnhandQuantityTransferMapper onhandQuantityTransferMapper;

    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;



    @Override
    public Double organizationSumOnhandQtyGet(Long tenantId, MtInvOnhandQuantityVO7 dto) {
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "materialId", "???API???organizationSumOnhandQtyGet???"));
        }
        if (StringUtils.isEmpty(dto.getOrganizationId())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "organizationId", "???API???organizationSumOnhandQtyGet???"));
        }
        if (StringUtils.isEmpty(dto.getOrganizationType())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "organizationType", "???API???organizationSumOnhandQtyGet???"));
        }

        List<MtModLocatorOrgRelVO> mtModLocatorOrganizationRelVOS =
                        iMtModLocatorOrganizationRelService.organizationLimitLocatorQuery(tenantId,
                                        dto.getOrganizationType(), dto.getOrganizationId(), "ALL");

        // ?????????????????????????????????
        List<String> locatorIds = mtModLocatorOrganizationRelVOS.stream().map(MtModLocatorOrgRelVO::getLocatorId)
                        .distinct().collect(Collectors.toList());


        if (CollectionUtils.isNotEmpty(locatorIds)) {
            return mtInvOnhandQuantityMapper.organizationSumOnhandQtyGet(tenantId, locatorIds, dto.getMaterialId(),
                            dto.getLotCode(), dto.getOwnerType(), dto.getOwnerId());
        } else {
            return Double.valueOf(0.0D);
        }
    }

    @Override
    public List<MtInvOnhandQuantity> organizationDetailOnhandQtyQuery(Long tenantId, MtInvOnhandQuantityVO7 dto) {
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "materialId", "???API???organizationDetailOnhandQtyQuery???"));
        }
        if (StringUtils.isEmpty(dto.getOrganizationId())) {
            throw new MtException("MT_INVENTORY_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0001",
                                            "INVENTORY", "organizationId", "???API???organizationDetailOnhandQtyQuery???"));
        }
        if (StringUtils.isEmpty(dto.getOrganizationType())) {
            throw new MtException("MT_INVENTORY_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0001",
                                            "INVENTORY", "organizationType", "???API???organizationDetailOnhandQtyQuery???"));
        }

        List<MtModLocatorOrgRelVO> mtModLocatorOrganizationRelVOS =
                        iMtModLocatorOrganizationRelService.organizationLimitLocatorQuery(tenantId,
                                        dto.getOrganizationType(), dto.getOrganizationId(), "ALL");

        // ?????????????????????????????????
        List<String> locatorIds = mtModLocatorOrganizationRelVOS.stream().map(MtModLocatorOrgRelVO::getLocatorId)
                        .distinct().collect(Collectors.toList());

        return mtInvOnhandQuantityMapper.selectByOrganizationDetail(tenantId, locatorIds, dto.getMaterialId(),
                        dto.getLotCode(), dto.getOwnerType(), dto.getOwnerId());
    }

    @Override
    public List<MtInvOnhandQuantity> propertyLimitDetailOnhandQtyQuery(Long tenantId, MtInvOnhandQuantityVO condition) {
        if (ObjectFieldsHelper.isAllFieldNull(condition)) {
            throw new MtException("MT_INVENTORY_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0002", "INVENTORY", "???API:propertyLimitDetailOnhandQtyQuery???"));
        }

        MtInvOnhandQuantity mtInvOnhandQuantity = new MtInvOnhandQuantity();
        BeanUtils.copyProperties(condition, mtInvOnhandQuantity);
        mtInvOnhandQuantity.setTenantId(tenantId);

        Criteria criteria = new Criteria(mtInvOnhandQuantity);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtInvOnhandQuantity.FIELD_TENANT_ID, Comparison.EQUAL));
        if (condition.getSiteId() != null) {
            whereFields.add(new WhereField(MtInvOnhandQuantity.FIELD_SITE_ID, Comparison.EQUAL));
        }
        if (condition.getMaterialId() != null) {
            whereFields.add(new WhereField(MtInvOnhandQuantity.FIELD_MATERIAL_ID, Comparison.EQUAL));
        }
        if (condition.getLocatorId() != null) {
            whereFields.add(new WhereField(MtInvOnhandQuantity.FIELD_LOCATOR_ID, Comparison.EQUAL));
        }
        if (condition.getLotCode() != null) {
            whereFields.add(new WhereField(MtInvOnhandQuantity.FIELD_LOT_CODE, Comparison.EQUAL));
        }
        if (condition.getOwnerId() != null) {
            whereFields.add(new WhereField(MtInvOnhandQuantity.FIELD_OWNER_ID, Comparison.EQUAL));
        }
        if (condition.getOwnerType() != null) {
            whereFields.add(new WhereField(MtInvOnhandQuantity.FIELD_OWNER_TYPE, Comparison.EQUAL));
        }
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        List<MtInvOnhandQuantity> mtInvOnhandQuantitys =
                        this.mtInvOnhandQuantityMapper.selectOptional(mtInvOnhandQuantity, criteria);
        if (CollectionUtils.isEmpty(mtInvOnhandQuantitys)) {
            return Collections.emptyList();
        }

        return mtInvOnhandQuantitys;
    }

    @Override
    public Double propertyLimitSumOnhandQtyGet(Long tenantId, MtInvOnhandQuantityVO condition) {
        if (ObjectFieldsHelper.isAllFieldNull(condition)) {
            throw new MtException("MT_INVENTORY_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0002", "INVENTORY", "???API:propertyLimitSumOnhandQtyGet???"));
        }

        List<MtInvOnhandQuantity> mtInvOnhandQuantitys = propertyLimitDetailOnhandQtyQuery(tenantId, condition);
        if (CollectionUtils.isEmpty(mtInvOnhandQuantitys)) {
            return Double.valueOf(0.0D);
        }

        return mtInvOnhandQuantitys.stream().filter(c -> null != c.getOnhandQuantity())
                        .collect(CollectorsUtil.summingBigDecimal(c -> BigDecimal.valueOf(c.getOnhandQuantity())))
                        .doubleValue();
    }

    @Override
    public List<MtInvOnhandQuantity> propertyLimitDetailOnhandQtyBatchQuery(Long tenantId,
                    MtInvOnhandQuantityVO1 condition) {
        if (ObjectFieldsHelper.isAllFieldNull(condition)) {
            throw new MtException("MT_INVENTORY_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0002", "INVENTORY", "???API:propertyLimitDetailOnhandQtyBatchQuery???"));
        }

        return this.mtInvOnhandQuantityMapper.selectByConditions(tenantId, condition);
    }

    @Override
    public MtInvOnhandQuantityVO2 onhandQtyUpdateVerify(Long tenantId, MtInvOnhandQuantityVO8 dto) {
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "siteId", "???API???onhandQtyUpdateVerify???"));
        }
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "materialId", "???API???onhandQtyUpdateVerify???"));
        }
        if (StringUtils.isEmpty(dto.getLocatorId())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "locatorId", "???API???onhandQtyUpdateVerify???"));
        }
        if (dto.getChangeQuantity() == null) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "changeQuantity", "???API???onhandQtyUpdateVerify???"));
        }
        if (dto.getChangeQuantity() < 0) {
            throw new MtException("MT_INVENTORY_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0017", "INVENTORY", "???API???onhandQtyUpdateVerify???"));
        }
        if (StringUtils.isEmpty(dto.getEventTypeCode())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "eventTypeCode", "???API???onhandQtyUpdateVerify???"));
        }
        if ((StringUtils.isEmpty(dto.getOwnerType()) && StringUtils.isNotEmpty(dto.getOwnerId()))
                        || (StringUtils.isNotEmpty(dto.getOwnerType()) && StringUtils.isEmpty(dto.getOwnerId()))) {
            throw new MtException("MT_INVENTORY_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0018", "INVENTORY", "ownerType", "ownerId", "???API???onhandQtyUpdateVerify???"));
        }
        if (StringUtils.isNotEmpty(dto.getOwnerType())) {
            MtGenTypeVO2 mtGenTypeVO = new MtGenTypeVO2();
            mtGenTypeVO.setModule("INVENTORY");
            mtGenTypeVO.setTypeGroup("INVENTORY_OWNER_TYPE");
            List<MtGenType> invOwnerTypes = mtGenTypeService.groupLimitTypeQuery(tenantId, mtGenTypeVO);
            List<String> invOwnerTypeCodes =
                            invOwnerTypes.stream().map(MtGenType::getTypeCode).collect(Collectors.toList());
            if (!invOwnerTypeCodes.contains(dto.getOwnerType())) {
                throw new MtException("MT_INVENTORY_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_INVENTORY_0019", "INVENTORY", dto.getOwnerType(), "???API???onhandQtyUpdateVerify???"));
            }
        }

        // ??????????????????
        MtModSite modSite = iMtModSiteService.siteBasicPropertyGet(tenantId, dto.getSiteId());
        if (modSite == null || !"Y".equals(modSite.getEnableFlag())) {
            throw new MtException("MT_INVENTORY_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0003", "INVENTORY", "siteId", "???API???onhandQtyUpdateVerify???"));
        }

        // ??????????????????
        MtMaterial material = iMtMaterialService.materialPropertyGet(tenantId, dto.getMaterialId());
        if (material == null || !"Y".equals(material.getEnableFlag())) {
            throw new MtException("MT_INVENTORY_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0003", "INVENTORY", "materialId", "???API???onhandQtyUpdateVerify???"));
        }

        // ??????????????????????????????
        MtMaterialSite materialSite = new MtMaterialSite();
        materialSite.setMaterialId(dto.getMaterialId());
        materialSite.setSiteId(dto.getSiteId());
        String materialSiteId = iMtMaterialSiteRepository.materialSiteLimitRelationGet(tenantId, materialSite);
        if (StringUtils.isEmpty(materialSiteId)) {
            throw new MtException("MT_INVENTORY_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0005", "INVENTORY", "???API???onhandQtyUpdateVerify???"));
        }

        // ?????????????????????????????????????????????
        MtInvOnhandQuantityVO2 result = new MtInvOnhandQuantityVO2();
        String parentLocatorId =
                        iMtModLocatorService.locatorLimitInventoryCategoryLocatorGet(tenantId, dto.getLocatorId());
        if (StringUtils.isEmpty(parentLocatorId)) {
            result.setActualChangeLocatorId(dto.getLocatorId());
            result.setActualChangeQty(0.0D);
            return result;
        }
        result.setActualChangeLocatorId(parentLocatorId);

        // ?????????????????? eventTypeCode ????????????
        MtEventType eventType = new MtEventType();
        eventType.setTenantId(tenantId);
        eventType.setEventTypeCode(dto.getEventTypeCode());
        eventType = mtEventTypeMapper.selectOne(eventType);
        if (eventType == null || !"Y".equals(eventType.getEnableFlag())) {
            throw new MtException("MT_INVENTORY_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0003", "INVENTORY", "eventTypeCode", "???API???onhandQtyUpdateVerify???"));
        }


        // ??????OnhandChangeFlag??????Y ???????????????????????????????????? ??????API
        if (!"Y".equals(eventType.getOnhandChangeFlag())) {
            result.setActualChangeQty(Double.valueOf(0.0D));
            return result;
        }

        // I??????????????????API ; D??????????????????????????????????????????
        if ("I".equals(eventType.getOnhandChangeType())) {
            result.setActualChangeQty(dto.getChangeQuantity());
            return result;
        } else if ("D".equals(eventType.getOnhandChangeType())) {
            // ???????????????????????????????????????
            // NegativeFlag???Y???????????????????????? ??????????????????????????? ??????API
            MtModLocator mtModLocator = iMtModLocatorService.locatorBasicPropertyGet(tenantId, parentLocatorId);
            if (mtModLocator != null && "Y".equals(mtModLocator.getNegativeFlag())) {
                result.setActualChangeQty(-dto.getChangeQuantity());
                return result;
            }

            // ?????????????????????????????????????????????????????????????????????
            MtInvOnhandQuantityVO invOnhandQuantityVO = new MtInvOnhandQuantityVO();
            invOnhandQuantityVO.setSiteId(dto.getSiteId());
            invOnhandQuantityVO.setMaterialId(dto.getMaterialId());
            invOnhandQuantityVO.setLocatorId(parentLocatorId);
            invOnhandQuantityVO.setLotCode(dto.getLotCode() == null ? "" : dto.getLotCode());
            invOnhandQuantityVO.setOwnerType(dto.getOwnerType() == null ? "" : dto.getOwnerType());
            invOnhandQuantityVO.setOwnerId(dto.getOwnerId() == null ? "" : dto.getOwnerId());
            Double availOnhandQty = propertyLimitSumAvailableOnhandQtyGet(tenantId, invOnhandQuantityVO);

            if (BigDecimal.valueOf(availOnhandQty).compareTo(BigDecimal.valueOf(dto.getChangeQuantity())) >= 0) {
                result.setActualChangeQty(-dto.getChangeQuantity());
                return result;
            } else {
                throw new MtException("MT_INVENTORY_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_INVENTORY_0012", "INVENTORY", "???API???onhandQtyUpdateVerify???"));
            }
        } else {
            // ???????????? ???????????????????????????????????? ??????API
            result.setActualChangeQty(Double.valueOf(0.0D));
            return result;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void onhandQtyUpdate(Long tenantId, MtInvOnhandQuantityVO9 dto) {
        if (dto.getChangeQuantity() != null) {
            // ????????????????????????????????????????????????????????????????????????
            MtInvOnhandQuantity newInvOnhandQuantity = new MtInvOnhandQuantity();
            newInvOnhandQuantity.setSiteId(dto.getSiteId());
            newInvOnhandQuantity.setLotCode(dto.getLotCode());
            newInvOnhandQuantity.setMaterialId(dto.getMaterialId());
            newInvOnhandQuantity.setLocatorId(dto.getLocatorId());
            newInvOnhandQuantity.setOwnerId(dto.getOwnerId());
            newInvOnhandQuantity.setOwnerType(dto.getOwnerType());

            MtInvOnhandQuantity oldInvOnhandQuantity =
                            mtInvOnhandQuantityMapper.selectOnhandQuantity(tenantId, newInvOnhandQuantity);

            // ?????????????????????????????????
            BigDecimal resultQty = null;

            // ???????????????????????????
            if (oldInvOnhandQuantity != null && StringUtils.isNotEmpty(oldInvOnhandQuantity.getOnhandQuantityId())) {
                // ???????????????
                oldInvOnhandQuantity = mtInvOnhandQuantityMapper.selectForUpdate(tenantId,
                                oldInvOnhandQuantity.getOnhandQuantityId());

                // ???????????? ???????????????????????????
                BigDecimal newOnhandQty = BigDecimal.valueOf(oldInvOnhandQuantity.getOnhandQuantity())
                                .add(BigDecimal.valueOf(dto.getChangeQuantity()));

                if (newOnhandQty.compareTo(BigDecimal.ZERO) == 0 && !StringUtils.equals(dto.getOwnerType(), "SI")) {
                    // ??????
                    self().deleteByPrimaryKey(oldInvOnhandQuantity);
                } else {
                    // ??????
                    oldInvOnhandQuantity.setOnhandQuantity(newOnhandQty.doubleValue());
                    oldInvOnhandQuantity.setTenantId(tenantId);
                    self().updateByPrimaryKeySelective(oldInvOnhandQuantity);
                }

                resultQty = newOnhandQty;
            } else {
                newInvOnhandQuantity.setOnhandQuantity(dto.getChangeQuantity());
                newInvOnhandQuantity.setTenantId(tenantId);
                self().insertSelective(newInvOnhandQuantity);

                resultQty = BigDecimal.valueOf(newInvOnhandQuantity.getOnhandQuantity());
            }

            // ?????????????????????
            MtInvJournal mtInvJournal = new MtInvJournal();
            mtInvJournal.setTenantId(tenantId);
            mtInvJournal.setSiteId(dto.getSiteId());
            mtInvJournal.setMaterialId(dto.getMaterialId());
            mtInvJournal.setLocatorId(dto.getLocatorId());
            mtInvJournal.setLotCode(dto.getLotCode());
            mtInvJournal.setChangeQuantity(dto.getChangeQuantity());
            mtInvJournal.setOnhandQuantity(resultQty.doubleValue());
            mtInvJournal.setEventId(dto.getEventId());
            Long userId;
            if (DetailsHelper.getUserDetails() == null) {
                userId = -1L;
            } else {
                userId = DetailsHelper.getUserDetails().getUserId();
            }
            mtInvJournal.setEventBy(userId);
            mtInvJournal.setEventTime(new Date());
            mtInvJournal.setOwnerId(dto.getOwnerId());
            mtInvJournal.setOwnerType(dto.getOwnerType());

            iMtInvJournalService.insertSelective(mtInvJournal);
        }
    }

    @Override
    public Double propertyLimitSumAvailableOnhandQtyGet(Long tenantId, MtInvOnhandQuantityVO dto) {
        if (ObjectFieldsHelper.isAllFieldNull(dto)) {
            throw new MtException("MT_INVENTORY_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0002", "INVENTORY", "???API???propertyLimitSumAvailableOnhandQtyGet???"));
        }

        // ???????????????????????? ?????????????????????
        Double sumOnhandQty = propertyLimitSumOnhandQtyGet(tenantId, dto);

        // ???????????????????????? ???????????????????????????
        BigDecimal sumOnhandHoldQty = BigDecimal.ZERO;

        MtInvOnhandHold invOnhandHold = new MtInvOnhandHold();
        invOnhandHold.setTenantId(tenantId);
        invOnhandHold.setSiteId(dto.getSiteId());
        invOnhandHold.setMaterialId(dto.getMaterialId());
        invOnhandHold.setLocatorId(dto.getLocatorId());
        invOnhandHold.setLotCode(dto.getLotCode());
        invOnhandHold.setOwnerId(dto.getOwnerId());
        invOnhandHold.setOwnerType(dto.getOwnerType());
        List<MtInvOnhandHold> invOnhandHolds = mtInvOnhandHoldMapper.select(invOnhandHold);

        if (CollectionUtils.isNotEmpty(invOnhandHolds)) {
            sumOnhandHoldQty = invOnhandHolds.stream().filter(c -> null != c.getHoldQuantity())
                            .collect(CollectorsUtil.summingBigDecimal(c -> BigDecimal.valueOf(c.getHoldQuantity())));
        }

        return BigDecimal.valueOf(sumOnhandQty).subtract(sumOnhandHoldQty).doubleValue();
    }

    @Override
    public List<MtInvOnhandQuantityVO6> propertyLimitDetailAvailableOnhandQtyQuery(Long tenantId,
                    MtInvOnhandQuantityVO dto) {
        if (ObjectFieldsHelper.isAllFieldNull(dto)) {
            throw new MtException("MT_INVENTORY_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0002", "INVENTORY", "???API???propertyLimitDetailAvailableOnhandQtyQuery???"));
        }

        // ?????????????????????
        List<MtInvOnhandQuantity> invOnhandQuantities = propertyLimitDetailOnhandQtyQuery(tenantId, dto);

        // ???????????????????????????
        MtInvOnhandHold invOnhandHold = new MtInvOnhandHold();
        invOnhandHold.setTenantId(tenantId);
        invOnhandHold.setSiteId(dto.getSiteId());
        invOnhandHold.setMaterialId(dto.getMaterialId());
        invOnhandHold.setLocatorId(dto.getLocatorId());
        invOnhandHold.setLotCode(dto.getLotCode());
        invOnhandHold.setOwnerId(dto.getOwnerId());
        invOnhandHold.setOwnerType(dto.getOwnerType());
        List<MtInvOnhandHold> invOnhandHolds = mtInvOnhandHoldMapper.select(invOnhandHold);

        //
        List<MtInvOnhandQuantityVO6> availableOnhandVOS = new ArrayList<>();
        for (MtInvOnhandQuantity quantity : invOnhandQuantities) {
            // ?????????????????? ???????????????
            List<MtInvOnhandHold> result = invOnhandHolds.stream()
                            .filter(hold -> quantity.getSiteId().equals(hold.getSiteId())
                                            && quantity.getMaterialId().equals(hold.getMaterialId())
                                            && quantity.getLocatorId().equals(hold.getLocatorId())
                                            && quantity.getLotCode().equals(hold.getLotCode()))
                            .collect(Collectors.toList());

            // ??????????????????
            BigDecimal sumOnhandHoldQty = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(result)) {
                sumOnhandHoldQty = result.stream().filter(c -> null != c.getHoldQuantity()).collect(
                                CollectorsUtil.summingBigDecimal(c -> BigDecimal.valueOf(c.getHoldQuantity())));
            }

            // ??????????????????
            BigDecimal availableQty = BigDecimal.valueOf(quantity.getOnhandQuantity()).subtract(sumOnhandHoldQty);

            // ????????????0?????????
            if (availableQty.compareTo(BigDecimal.ZERO) > 0) {
                MtInvOnhandQuantityVO6 availableOnhandVO = new MtInvOnhandQuantityVO6();
                availableOnhandVO.setSiteId(quantity.getSiteId());
                availableOnhandVO.setMaterialId(quantity.getMaterialId());
                availableOnhandVO.setLocatorId(quantity.getLocatorId());
                availableOnhandVO.setLotCode(quantity.getLotCode());
                availableOnhandVO.setAvailableQty(availableQty.doubleValue());
                availableOnhandVO.setOwnerId(quantity.getOwnerId());
                availableOnhandVO.setOwnerType(quantity.getOwnerType());
                availableOnhandVOS.add(availableOnhandVO);
            }
        }

        return availableOnhandVOS;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void onhandQtyUpdateProcess(Long tenantId, MtInvOnhandQuantityVO9 dto) {
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "siteId", "???API???onhandQtyUpdateProcess???"));
        }
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "materialId", "???API???onhandQtyUpdateProcess???"));
        }
        if (StringUtils.isEmpty(dto.getLocatorId())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "locatorId", "???API???onhandQtyUpdateProcess???"));
        }
        if (null == dto.getChangeQuantity()) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "changeQuantity", "???API???onhandQtyUpdateProcess???"));
        }
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "eventId", "???API???onhandQtyUpdateProcess???"));
        }

        MtEventVO1 mtEventVO1 = this.mtEventService.eventGet(tenantId, dto.getEventId());
        if (null == mtEventVO1 || StringUtils.isEmpty(mtEventVO1.getEventTypeCode())) {
            throw new MtException("MT_INVENTORY_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0004", "INVENTORY", "eventTypeCode", "???API???onhandQtyUpdateProcess???"));
        }

        MtInvOnhandQuantityVO8 updateOnhandVerifyVO = new MtInvOnhandQuantityVO8();
        updateOnhandVerifyVO.setSiteId(dto.getSiteId());
        updateOnhandVerifyVO.setMaterialId(dto.getMaterialId());
        updateOnhandVerifyVO.setLocatorId(dto.getLocatorId());
        updateOnhandVerifyVO.setLotCode(dto.getLotCode());
        updateOnhandVerifyVO.setChangeQuantity(dto.getChangeQuantity());
        updateOnhandVerifyVO.setEventTypeCode(mtEventVO1.getEventTypeCode());
        updateOnhandVerifyVO.setOwnerId(dto.getOwnerId());
        updateOnhandVerifyVO.setOwnerType(dto.getOwnerType());
        MtInvOnhandQuantityVO2 verifyResult = onhandQtyUpdateVerify(tenantId, updateOnhandVerifyVO);

        MtInvOnhandQuantityVO9 updateOnhandVO = new MtInvOnhandQuantityVO9();
        updateOnhandVO.setSiteId(dto.getSiteId());
        updateOnhandVO.setMaterialId(dto.getMaterialId());
        updateOnhandVO.setLocatorId(verifyResult.getActualChangeLocatorId());
        updateOnhandVO.setLotCode(dto.getLotCode());
        updateOnhandVO.setChangeQuantity(verifyResult.getActualChangeQty());
        updateOnhandVO.setEventId(dto.getEventId());
        updateOnhandVO.setOwnerId(dto.getOwnerId());
        updateOnhandVO.setOwnerType(dto.getOwnerType());
        onhandQtyUpdate(tenantId, updateOnhandVO);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onhandQtyUpdateBatchProcess(Long tenantId, MtInvOnhandQuantityVO16 dto) {
        List<MtInvOnhandQuantityVO13> onhandList = dto.getOnhandList();
        if (CollectionUtils.isEmpty(onhandList)) {
            return;
        }
        String apiName = "???API:onhandQtyUpdateBatchProcess???";
        if (MtIdHelper.isIdNull(dto.getEventId())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "eventId", apiName));
        }
        // ???????????????eventId??????{eventGet}??????eventTypeCode
        MtEventVO1 mtEventVO1 = mtEventRepository.eventGet(tenantId, dto.getEventId());
        if (mtEventVO1 == null || StringUtils.isEmpty(mtEventVO1.getEventTypeCode())) {
            throw new MtException("MT_INVENTORY_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0003", "INVENTORY", "eventId", apiName));
        }

        // ????????????????????????????????????eventId???????????????????????????eventTypeCode?????????API???onhandQtyUpdateBatchVerify???
        MtInvOnhandQuantityVO14 mtInvOnhandQuantityVO14 = new MtInvOnhandQuantityVO14();
        mtInvOnhandQuantityVO14.setEventTypeCode(mtEventVO1.getEventTypeCode());
        mtInvOnhandQuantityVO14.setOnhandList(onhandList);
        List<MtInvOnhandQuantityVO17> verifyList = self().onhandQtyUpdateBatchVerify(tenantId, mtInvOnhandQuantityVO14);

        /// ??????????????????????????????API????????????????????????API???onhandQtyBatchUpdate??? ?????????????????????
        onhandList = verifyList.stream()
                        .map(t -> onhandQuantityTransferMapper.invOnhandQuantityVO17ToInvOnhandQuantityVO13(t))
                        .collect(Collectors.toList());
        dto.setOnhandList(onhandList);
        self().onhandQtyBatchUpdate(tenantId, dto);

    }

    @Override
    public List<MtInvOnhandQuantityVO17> onhandQtyUpdateBatchVerify(Long tenantId, MtInvOnhandQuantityVO14 dto) {
        String apiName = "???API:onhandQtyUpdateBatchVerify???";
        if (CollectionUtils.isEmpty(dto.getOnhandList())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "onhandList", apiName));
        }

        if (StringUtils.isEmpty(dto.getEventTypeCode())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "eventTypeCode", apiName));
        }
        List<MtInvOnhandQuantityVO13> onhandList = dto.getOnhandList();

        Set<String> siteIdSet = new HashSet<>(onhandList.size());
        Set<String> materialIdSet = new HashSet<>(onhandList.size());
        Set<String> locatorIdSet = new HashSet<>(onhandList.size());
        Set<String> ownerTypeSet = new HashSet<>(onhandList.size());

        MtGenTypeVO2 mtGenTypeVO = new MtGenTypeVO2();
        mtGenTypeVO.setModule("INVENTORY");
        mtGenTypeVO.setTypeGroup("INVENTORY_OWNER_TYPE");
        List<MtGenType> invOwnerTypes = mtGenTypeService.groupLimitTypeQuery(tenantId, mtGenTypeVO);
        Set<String> ownerTypeCodeSet = invOwnerTypes.stream().map(MtGenType::getTypeCode).collect(Collectors.toSet());
        // ?????????????????????
        for (MtInvOnhandQuantityVO13 inputParam : dto.getOnhandList()) {
            if (MtIdHelper.isIdNull(inputParam.getSiteId())) {
                throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_INVENTORY_0001", "INVENTORY", "siteId", apiName));
            }
            if (MtIdHelper.isIdNull(inputParam.getMaterialId())) {
                throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_INVENTORY_0001", "materialId", apiName));
            }
            if (MtIdHelper.isIdNull(inputParam.getLocatorId())) {
                throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_INVENTORY_0001", "INVENTORY", "locatorId", apiName));
            }
            if (inputParam.getChangeQuantity() == null) {
                throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_INVENTORY_0001", "INVENTORY", "changeQuantity", apiName));
            }
            if (inputParam.getChangeQuantity().compareTo(0.0D) < 0) {
                throw new MtException("MT_INVENTORY_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_INVENTORY_0017", "INVENTORY", apiName));
            }

            if (StringUtils.isEmpty(inputParam.getOwnerType()) ^ MtIdHelper.isIdNull(inputParam.getOwnerId())) {
                throw new MtException("MT_INVENTORY_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_INVENTORY_0018", "INVENTORY", "ownerType", "ownerId", apiName));
            }
            siteIdSet.add(inputParam.getSiteId());
            materialIdSet.add(inputParam.getMaterialId());
            locatorIdSet.add(inputParam.getLocatorId());
            if (StringUtils.isNotEmpty(inputParam.getOwnerType())) {
                ownerTypeSet.add(inputParam.getOwnerType());
            }
        }

        // ownerType???????????????
        ownerTypeSet.removeAll(ownerTypeCodeSet);
        if (CollectionUtils.isNotEmpty(ownerTypeSet)) {
            String ownerTypeError = String.join(",", ownerTypeSet);
            throw new MtException("MT_INVENTORY_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0019", "INVENTORY", ownerTypeError, apiName));
        }

        // ???????????????????????????????????????
        List<MtModSite> mtModSites =
                        iMtModSiteRepository.siteBasicPropertyBatchGet(tenantId, new ArrayList<>(siteIdSet)).stream()
                                        .filter(t -> MtBaseConstants.YES.equalsIgnoreCase(t.getEnableFlag()))
                                        .collect(Collectors.toList());
        if (mtModSites.size() != siteIdSet.size()) {
            siteIdSet.removeAll(mtModSites.stream().map(MtModSite::getSiteId).collect(Collectors.toList()));
            String siteErrorStr = siteIdSet.stream().map(t -> t + "").collect(Collectors.joining(","));
            throw new MtException("MT_INVENTORY_0027", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0027", "INVENTORY", siteErrorStr, apiName));
        }


        // ??????????????????????????????????????????????????????????????????
        List<MtMaterialVO> mtMaterialVOS =
                        mtMaterialRepository.materialPropertyBatchGet(tenantId, new ArrayList<>(materialIdSet)).stream()
                                        .filter(t -> MtBaseConstants.YES.equalsIgnoreCase(t.getEnableFlag()))
                                        .collect(Collectors.toList());
        if (mtMaterialVOS.size() != materialIdSet.size()) {
            materialIdSet.removeAll(
                            mtMaterialVOS.stream().map(MtMaterialVO::getMaterialId).collect(Collectors.toList()));
            String materialErrorStr = materialIdSet.stream().map(t -> t + "").collect(Collectors.joining(","));
            throw new MtException("MT_INVENTORY_0028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0028", "INVENTORY", materialErrorStr, apiName));
        }

        // ?????????????????????????????????????????????????????????????????????????????????
        // ?????????????????????????????????????????????????????????????????????siteId???materialId??????????????????????????????
        List<MtMaterialSiteVO3> mtMaterialSiteVO3List =
                        onhandList.stream().map(t -> new MtMaterialSiteVO3(t.getMaterialId(), t.getSiteId())).distinct()
                                        .collect(Collectors.toList());
        Map<MtMaterialSiteVO3, String> mtMaterialSiteMap = iMtMaterialSiteRepository
                        .materialSiteLimitRelationBatchGet(tenantId, mtMaterialSiteVO3List).stream()
                        .collect(Collectors.toMap(t -> new MtMaterialSiteVO3(t.getMaterialId(), t.getSiteId()),
                                        MtMaterialSiteVO4::getEnableFlag));

        // ???materialSiteId?????????enableFlag??????Y?????????????????????
        List<MtMaterialSiteVO3> invalidMaterialSiteList = mtMaterialSiteVO3List.stream()
                        .filter(t -> StringUtils.isEmpty(mtMaterialSiteMap.get(t))
                                        || !MtBaseConstants.YES.equalsIgnoreCase(mtMaterialSiteMap.get(t)))
                        .collect(Collectors.toList());

        // ?????????????????????MT_INVENTORY_0029???
        // ??????????????????${siteCode+materialCode???????????????????????????????????????code??????id?????????}??????????????????????????????,?????????!${onhandQtyUpdateBatchVerify}
        Map<String, String> materialCodeMap = mtMaterialVOS.stream()
                        .collect(Collectors.toMap(MtMaterialVO::getMaterialId, MtMaterialVO::getMaterialCode));
        Map<String, String> siteCodeMap =
                        mtModSites.stream().collect(Collectors.toMap(MtModSite::getSiteId, MtModSite::getSiteCode));
        if (CollectionUtils.isNotEmpty(invalidMaterialSiteList)) {
            String materialSiteErrorStr = invalidMaterialSiteList.stream()
                            .map(t -> siteCodeMap.get(t.getSiteId()) + "+" + materialCodeMap.get(t.getMaterialId()))
                            .collect(Collectors.joining(","));
            throw new MtException("MT_INVENTORY_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0029", "INVENTORY", materialSiteErrorStr, apiName));

        }

        // ?????????????????????????????????,?????????????????????????????????????????????????????????????????????????????????
        MtEventTypeVO mtEventTypeVO = new MtEventTypeVO();
        mtEventTypeVO.setEventTypeCode(dto.getEventTypeCode());
        List<String> eventTypeIds = mtEventTypeRepository.propertyLimitEventTypeQuery(tenantId, mtEventTypeVO);
        if (CollectionUtils.isEmpty(eventTypeIds)) {
            throw new MtException("MT_INVENTORY_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0003", "INVENTORY", dto.getEventTypeCode(), apiName));
        }
        MtEventType mtEventType = mtEventTypeRepository.eventTypeGet(tenantId, eventTypeIds.get(0));
        if (mtEventType == null || !MtBaseConstants.YES.equalsIgnoreCase(mtEventType.getEnableFlag())) {
            throw new MtException("MT_INVENTORY_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0003", "INVENTORY", dto.getEventTypeCode(), apiName));
        }

        // ?????????????????????locatorId????????????locatorIds?????????API{locatorListLimitInvCategoryLocatorGet}???????????????????????????????????????????????????
        List<MtModLocatorVO15> paranetInventoryList = iMtModLocatorService
                        .locatorListLimitInvCategoryLocatorGet(tenantId, new ArrayList<>(locatorIdSet));
        Map<String, String> parentInventoryIdMap =
                        paranetInventoryList.stream().filter(t -> MtIdHelper.isIdNotNull(t.getInventoryLocatorId()))
                                        .collect(Collectors.toMap(MtModLocatorVO15::getLocatorId,
                                                        MtModLocatorVO15::getInventoryLocatorId));

        List<MtInvOnhandQuantityVO17> resultList = new ArrayList<>(onhandList.size());
        List<MtInvOnhandQuantityVO17> existInventoryLocatorList = new ArrayList<>(onhandList.size());
        for (MtInvOnhandQuantityVO13 mtInvOnhandQuantityVO13 : onhandList) {
            String inventoryLocatorId = parentInventoryIdMap.get(mtInvOnhandQuantityVO13.getLocatorId());
            // ?????????inventoryLocatorId???????????????????????????????????????????????????
            MtInvOnhandQuantityVO17 mtInvOnhandQuantityVO17 = onhandQuantityTransferMapper
                            .invOnhandQuantityVO13ToInvOnhandQuantityVO17(mtInvOnhandQuantityVO13);
            if (MtIdHelper.isIdNull(inventoryLocatorId)) {
                mtInvOnhandQuantityVO17.setActualChangeQuantity(0.0D);
                resultList.add(mtInvOnhandQuantityVO17);
            } else {
                existInventoryLocatorList.add(mtInvOnhandQuantityVO17);
            }
        }

        // ???inventoryLocatorId?????????
        if (CollectionUtils.isNotEmpty(existInventoryLocatorList)) {
            // ?????????inventoryLocatorI????????????actualLocatorIds?????????API{locatorBasicPropertyBatchGet}
            List<String> inventoryIds = parentInventoryIdMap.values().stream().distinct().collect(Collectors.toList());
            List<MtModLocator> inventoryLocatorList = iMtModLocatorService
                            .locatorBasicPropertyBatchGet(tenantId, new ArrayList<>(inventoryIds)).stream()
                            .filter(t -> MtBaseConstants.YES.equalsIgnoreCase(t.getEnableFlag()))
                            .collect(Collectors.toList());

            // ??????????????????????????????????????????????????????enableFlag??????Y?????????????????????
            if (inventoryLocatorList.size() != inventoryIds.size()) {
                Map<String, String> inventoryIdMap = paranetInventoryList.stream()
                                .filter(t -> MtIdHelper.isIdNull(t.getInventoryLocatorId()))
                                .collect(Collectors.toMap(MtModLocatorVO15::getInventoryLocatorId,
                                                MtModLocatorVO15::getLocatorId));
                inventoryIds.removeAll(inventoryLocatorList.stream().map(MtModLocator::getLocatorId)
                                .collect(Collectors.toSet()));

                String inventoryError = inventoryIds.stream()
                                .collect(ArrayList::new, (k, v) -> k.add(inventoryIdMap.get(v)), ArrayList::addAll)
                                .stream().map(t -> t + "").collect(Collectors.joining(","));
                throw new MtException("MT_INVENTORY_0030", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_INVENTORY_0030", "INVENTORY", inventoryError, apiName));
            }

            // ??????EventType???onhandChangeFlag??????Y?????????????????????????????????????????????API????????????
            if (!MtBaseConstants.YES.equalsIgnoreCase(mtEventType.getOnhandChangeFlag())) {
                existInventoryLocatorList.forEach(t -> t.setActualChangeQuantity(0.0D));
                resultList.addAll(existInventoryLocatorList);
                return resultList;
            }

            // ??????EventType???onhandChangeType???????????????????????????????????????????????????API????????????
            if (StringUtils.isEmpty(mtEventType.getOnhandChangeType())) {
                existInventoryLocatorList.forEach(t -> t.setActualChangeQuantity(0.0D));
                resultList.addAll(existInventoryLocatorList);
                return resultList;
            } // ??????EventType???onhandChangeType??????I?????????????????????????????????
            else if ("I".equalsIgnoreCase(mtEventType.getOnhandChangeType())) {
                existInventoryLocatorList
                                .forEach(t -> t.setActualLocatorId(parentInventoryIdMap.get(t.getActualLocatorId())));
                resultList.addAll(existInventoryLocatorList);
                return resultList;
            } // ???onhandQtyUpdateBatchVerify-011???????????????onhandChangeType??????D??????????????????????????????
            else if ("D".equalsIgnoreCase(mtEventType.getOnhandChangeType())) {
                Map<String, String> locatorNegativeMap = inventoryLocatorList.stream()
                                .collect(Collectors.toMap(MtModLocator::getLocatorId, MtModLocator::getNegativeFlag));
                // ???????????????????????????
                List<MtInvOnhandQuantityVO17> nonNegativeList = new ArrayList<>(existInventoryLocatorList.size());
                for (MtInvOnhandQuantityVO17 mtInvOnhandQuantityVO17 : existInventoryLocatorList) {
                    String negativeFlag = locatorNegativeMap
                                    .get(parentInventoryIdMap.get(mtInvOnhandQuantityVO17.getActualLocatorId()));
                    // ?????????inventoryLocatorId??????negativeFlag???Y???actualChangeQuantity?????????????????????,?????????????????????
                    if (MtBaseConstants.YES.equalsIgnoreCase(negativeFlag)) {
                        mtInvOnhandQuantityVO17
                                        .setActualChangeQuantity(-mtInvOnhandQuantityVO17.getActualChangeQuantity());
                        resultList.add(mtInvOnhandQuantityVO17);
                    } else {
                        nonNegativeList.add(mtInvOnhandQuantityVO17);
                    }

                }

                // ?????????inventoryLocatorId??????negativeFlag???N???
                if (CollectionUtils.isNotEmpty(nonNegativeList)) {
                    for (MtInvOnhandQuantityVO17 vo : nonNegativeList) {
                        vo.setSiteId(MtFieldsHelper.getOrDefault(vo.getSiteId(), MtBaseConstants.LONG_SPECIAL));
                        vo.setActualLocatorId(
                                        MtFieldsHelper.getOrDefault(parentInventoryIdMap.get(vo.getActualLocatorId()),
                                                        MtBaseConstants.LONG_SPECIAL));
                        vo.setMaterialId(MtFieldsHelper.getOrDefault(vo.getMaterialId(), MtBaseConstants.LONG_SPECIAL));
                        vo.setOwnerId(MtFieldsHelper.getOrDefault(vo.getOwnerId(), MtBaseConstants.LONG_SPECIAL));
                        vo.setLotCode(MtFieldsHelper.getOrDefault(vo.getLotCode(), MtBaseConstants.STRING_SPECIAL));
                        vo.setOwnerType(MtFieldsHelper.getOrDefault(vo.getOwnerType(), MtBaseConstants.STRING_SPECIAL));
                    }
                    // ??????inventoryLocatorId???N?????????actualLocatorId???????????????????????????????????????
                    // ???siteId???materialId???locatorId???lotCode???ownerType???ownerId??????????????????????????????????????????????????????????????????
                    // ??????????????????????????????changeQuantity?????????sumChangeQuantity?????????????????????????????????
                    Map<MtInvOnhandQuantityVO, BigDecimal> sumHoldQuantityMap = nonNegativeList.stream()
                                    .collect(Collectors.groupingBy(
                                                    t -> new MtInvOnhandQuantityVO(
                                                                    t.getSiteId(), t.getMaterialId(),
                                                                    t.getActualLocatorId(), t.getLotCode(),
                                                                    t.getOwnerType(), t.getOwnerId()),
                                                    CollectorsUtil.summingBigDecimal(t -> BigDecimal
                                                                    .valueOf(t.getActualChangeQuantity()))));
                    // ??????API{propertyLimitSumAvailableOnhandQtyBatchGet}????????????????????????
                    Map<MtInvOnhandQuantityVO, BigDecimal> sumAvailableQtyMap =
                                    propertyLimitSumAvailableOnhandQtyBatchGet(tenantId,
                                                    new ArrayList<>(sumHoldQuantityMap.keySet())).stream().collect(
                                                                    Collectors.toMap(t -> new MtInvOnhandQuantityVO(
                                                                                    t.getSiteId(), t.getMaterialId(),
                                                                                    t.getLocatorId(), t.getLotCode(), t
                                                                                                    .getOwnerType(),
                                                                                    t.getOwnerId()),
                                                                                    MtInvOnhandQuantityVO12::getSumAvailableQty));

                    // ???????????????????????????sumAvailableQty???sumChangeQuantity?????????
                    for (Map.Entry<MtInvOnhandQuantityVO, BigDecimal> entry : sumHoldQuantityMap.entrySet()) {
                        MtInvOnhandQuantityVO key = entry.getKey();
                        BigDecimal sumHoldQuantity = entry.getValue();
                        BigDecimal sumAvailableQty = sumAvailableQtyMap.get(entry.getKey());
                        if (sumAvailableQty == null) {
                            sumAvailableQty = BigDecimal.ZERO;
                        }
                        // ???sumAvailable??????sumHoldQuantity????????????????????????
                        if (sumAvailableQty.compareTo(sumHoldQuantity) < 0) {
                            String siteCode = siteCodeMap.get(key.getSiteId());
                            String materialCode = materialCodeMap.get(key.getMaterialId());
                            MtModLocator locator =
                                            iMtModLocatorService.locatorBasicPropertyGet(tenantId, key.getLocatorId());
                            String locatorCode = locator == null ? "" : locator.getLocatorCode();
                            String owner = key.getOwnerType() + "+" + key.getOwnerId();
                            throw new MtException("MT_INVENTORY_0012",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                            "MT_INVENTORY_0012", "INVENTORY", siteCode, locatorCode,
                                                            materialCode, key.getLotCode(), owner,
                                                            sumAvailableQty.toString(), sumHoldQuantity.toString(),
                                                            apiName));
                        }
                    }
                    // ???sumAvailable????????????sumChangeQuantity??????????????????
                    nonNegativeList.forEach(t -> t.setActualChangeQuantity(-t.getActualChangeQuantity()));
                    resultList.addAll(nonNegativeList);
                }
            }

        }

        return resultList;
    }

    @Override
    public List<MtInvOnhandQuantityVO12> propertyLimitSumAvailableOnhandQtyBatchGet(Long tenantId,
                    List<MtInvOnhandQuantityVO> vos) {
        if (CollectionUtils.isEmpty(vos) || vos.stream().anyMatch(MtFieldsHelper::isAllFieldNull)) {
            throw new MtException("MT_INVENTORY_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0002", "INVENTORY", "???API:propertyLimitSumAvailableOnhandQtyBatchGet???"));
        }

        // ?????????????????????????????????????????????????????????0,????????????????????????
        for (MtInvOnhandQuantityVO vo : vos) {
            vo.setSiteId(MtFieldsHelper.getOrDefault(vo.getSiteId(), MtBaseConstants.LONG_SPECIAL));
            vo.setLocatorId(MtFieldsHelper.getOrDefault(vo.getLocatorId(), MtBaseConstants.LONG_SPECIAL));
            vo.setMaterialId(MtFieldsHelper.getOrDefault(vo.getMaterialId(), MtBaseConstants.LONG_SPECIAL));
            vo.setOwnerId(MtFieldsHelper.getOrDefault(vo.getOwnerId(), MtBaseConstants.LONG_SPECIAL));
            vo.setLotCode(MtFieldsHelper.getOrDefault(vo.getLotCode(), MtBaseConstants.STRING_SPECIAL));
            vo.setOwnerType(MtFieldsHelper.getOrDefault(vo.getOwnerType(), MtBaseConstants.STRING_SPECIAL));
        }

        // ???????????????????????? ?????????????????????
        List<String> siteIds =
                        vos.stream().map(MtInvOnhandQuantityVO::getSiteId).distinct().collect(Collectors.toList());
        List<String> materialIds =
                        vos.stream().map(MtInvOnhandQuantityVO::getMaterialId).distinct().collect(Collectors.toList());
        List<String> locatorIds =
                        vos.stream().map(MtInvOnhandQuantityVO::getLocatorId).distinct().collect(Collectors.toList());
        List<String> ownerIds =
                        vos.stream().map(MtInvOnhandQuantityVO::getOwnerId).distinct().collect(Collectors.toList());

        List<String> lotCodes =
                        vos.stream().map(MtInvOnhandQuantityVO::getLotCode).distinct().collect(Collectors.toList());
        List<String> ownerTypes =
                        vos.stream().map(MtInvOnhandQuantityVO::getOwnerType).distinct().collect(Collectors.toList());

        // ??????????????????
        List<MtInvOnhandQuantity> mtInvOnHandQuantityList = mtInvOnhandQuantityMapper.selectByCondition(Condition
                        .builder(MtInvOnhandQuantity.class)
                        .andWhere(Sqls.custom().andEqualTo(MtInvOnhandQuantity.FIELD_TENANT_ID, tenantId))
                        .orWhere(Sqls.custom().andIn(MtInvOnhandQuantity.FIELD_SITE_ID, siteIds, true))
                        .orWhere(Sqls.custom().andIn(MtInvOnhandQuantity.FIELD_MATERIAL_ID, materialIds, true))
                        .orWhere(Sqls.custom().andIn(MtInvOnhandQuantity.FIELD_LOCATOR_ID, locatorIds, true))
                        .orWhere(Sqls.custom().andIn(MtInvOnhandQuantity.FIELD_OWNER_ID, ownerIds, true))
                        .orWhere(Sqls.custom().andIn(MtInvOnhandQuantity.FIELD_LOT_CODE, lotCodes, true))
                        .orWhere(Sqls.custom().andIn(MtInvOnhandQuantity.FIELD_OWNER_TYPE, ownerTypes, true)).build());

        Map<MtInvOnhandQuantityVO, Double> sumOnHandQtyMap = mtInvOnHandQuantityList.stream()
                        .collect(Collectors.toMap(t -> new MtInvOnhandQuantityVO(
                                        MtFieldsHelper.getOrDefault(t.getSiteId(), MtBaseConstants.LONG_SPECIAL),
                                        MtFieldsHelper.getOrDefault(t.getMaterialId(), MtBaseConstants.LONG_SPECIAL),
                                        MtFieldsHelper.getOrDefault(t.getLocatorId(), MtBaseConstants.LONG_SPECIAL),
                                        MtFieldsHelper.getOrDefault(t.getLotCode(), MtBaseConstants.STRING_SPECIAL),
                                        MtFieldsHelper.getOrDefault(t.getOwnerType(), MtBaseConstants.STRING_SPECIAL),
                                        MtFieldsHelper.getOrDefault(t.getOwnerId(), MtBaseConstants.LONG_SPECIAL)),
                                        MtInvOnhandQuantity::getOnhandQuantity));
        // ?????????????????????
        List<MtInvOnhandHold> mtInvOnHandHolds = mtInvOnhandHoldMapper.selectByCondition(Condition
                        .builder(MtInvOnhandHold.class)
                        .andWhere(Sqls.custom().andEqualTo(MtInvOnhandHold.FIELD_TENANT_ID, tenantId))
                        .orWhere(Sqls.custom().andIn(MtInvOnhandHold.FIELD_SITE_ID, siteIds, true))
                        .orWhere(Sqls.custom().andIn(MtInvOnhandHold.FIELD_MATERIAL_ID, materialIds, true))
                        .orWhere(Sqls.custom().andIn(MtInvOnhandHold.FIELD_LOCATOR_ID, locatorIds, true))
                        .orWhere(Sqls.custom().andIn(MtInvOnhandHold.FIELD_OWNER_ID, ownerIds, true))
                        .orWhere(Sqls.custom().andIn(MtInvOnhandHold.FIELD_LOT_CODE, lotCodes, true))
                        .orWhere(Sqls.custom().andIn(MtInvOnhandHold.FIELD_OWNER_TYPE, ownerTypes, true)).build());


        Map<MtInvOnhandQuantityVO, BigDecimal> sumOnHandHoldQtyMap = mtInvOnHandHolds.stream()
                        .collect(Collectors.groupingBy(t -> new MtInvOnhandQuantityVO(
                                        MtFieldsHelper.getOrDefault(t.getSiteId(), MtBaseConstants.LONG_SPECIAL),
                                        MtFieldsHelper.getOrDefault(t.getMaterialId(), MtBaseConstants.LONG_SPECIAL),
                                        MtFieldsHelper.getOrDefault(t.getLocatorId(), MtBaseConstants.LONG_SPECIAL),
                                        MtFieldsHelper.getOrDefault(t.getLotCode(), MtBaseConstants.STRING_SPECIAL),
                                        MtFieldsHelper.getOrDefault(t.getOwnerType(), MtBaseConstants.STRING_SPECIAL),
                                        MtFieldsHelper.getOrDefault(t.getOwnerId(), MtBaseConstants.LONG_SPECIAL)),
                                        CollectorsUtil.summingBigDecimal(
                                                        t -> BigDecimal.valueOf(t.getHoldQuantity()))));

        List<MtInvOnhandQuantityVO12> result = new ArrayList<>(vos.size());
        for (MtInvOnhandQuantityVO vo : vos) {
            MtInvOnhandQuantityVO temp = new MtInvOnhandQuantityVO(vo.getSiteId(), vo.getMaterialId(),
                            vo.getLocatorId(), vo.getLotCode(), vo.getOwnerType(), vo.getOwnerId());
            MtInvOnhandQuantityVO12 mtInvOnhandQuantityVO12 = new MtInvOnhandQuantityVO12();
            mtInvOnhandQuantityVO12.setSiteId(vo.getSiteId());

            mtInvOnhandQuantityVO12.setMaterialId(vo.getMaterialId());
            mtInvOnhandQuantityVO12.setLocatorId(vo.getLocatorId());
            mtInvOnhandQuantityVO12.setLotCode(vo.getLotCode());
            mtInvOnhandQuantityVO12.setOwnerType(vo.getOwnerType());
            mtInvOnhandQuantityVO12.setOwnerId(vo.getOwnerId());
            BigDecimal sumOnHandQty = sumOnHandQtyMap.get(temp) == null ? BigDecimal.ZERO
                            : BigDecimal.valueOf(sumOnHandQtyMap.get(temp));
            BigDecimal sumOnHandHoldQty =
                            sumOnHandHoldQtyMap.get(temp) == null ? BigDecimal.ZERO : sumOnHandHoldQtyMap.get(temp);
            BigDecimal sumAvailableQty = sumOnHandQty.subtract(sumOnHandHoldQty);
            mtInvOnhandQuantityVO12.setSumAvailableQty(sumAvailableQty);
            result.add(mtInvOnhandQuantityVO12);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onhandQtyBatchUpdate(Long tenantId, MtInvOnhandQuantityVO16 dto) {
        List<MtInvOnhandQuantityVO13> onhandList = dto.getOnhandList();
        if (CollectionUtils.isEmpty(onhandList)) {
            return;
        }
        String apiName = "???API:onhandQtyBatchUpdate???";
        // ?????????????????????
        if (MtIdHelper.isIdNull(dto.getEventId())) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "eventId", apiName));
        }
        Set<String> siteIdSet = new HashSet<>(onhandList.size());
        Set<String> materialIdSet = new HashSet<>(onhandList.size());
        Set<String> locatorIdSet = new HashSet<>(onhandList.size());
        Set<String> ownerTypeSet = new HashSet<>(onhandList.size());
        Set<String> ownerIdSet = new HashSet<>(onhandList.size());
        Set<String> lotCodeSet = new HashSet<>(onhandList.size());
        onhandList.forEach(t -> {
            if (MtIdHelper.isIdNull(t.getSiteId())) {
                throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_INVENTORY_0001", "INVENTORY", "siteId", apiName));
            }
            if (MtIdHelper.isIdNull(t.getMaterialId())) {
                throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_INVENTORY_0001", "INVENTORY", "materialId", apiName));
            }
            if (MtIdHelper.isIdNull(t.getLocatorId())) {
                throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_INVENTORY_0001", "INVENTORY", "locatorId", apiName));
            }
            if (t.getChangeQuantity() == null) {
                throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_INVENTORY_0001", "INVENTORY", "changeQuantity", apiName));
            }
            // ??????????????????????????????????????????
            t.setOwnerId(MtFieldsHelper.getOrDefault(t.getOwnerId(), MtBaseConstants.LONG_SPECIAL));
            t.setOwnerType(MtFieldsHelper.getOrDefault(t.getOwnerType(), MtBaseConstants.STRING_SPECIAL));
            t.setLotCode(MtFieldsHelper.getOrDefault(t.getLotCode(), MtBaseConstants.STRING_SPECIAL));

            siteIdSet.add(t.getSiteId());
            materialIdSet.add(t.getMaterialId());
            locatorIdSet.add(t.getLocatorId());
            lotCodeSet.add(t.getLotCode());
            ownerTypeSet.add(t.getOwnerType());
            ownerIdSet.add(t.getOwnerId());
        });
        // ??????siteId+materialId+locatorId+lotCode +ownerType+ownerId????????????changeQuantity???
        // ??????changeQuantity???0???????????????????????????????????????????????????0?????????API??????true???
        Map<MtInvOnhandQuantityVO, BigDecimal> sumChangeQtyMap = onhandList.stream()
                        .filter(t -> t.getChangeQuantity().compareTo(0.0D) != 0)
                        .collect(Collectors.groupingBy(
                                        t -> onhandQuantityTransferMapper.invOnhandQuantityVO13ToInvOnhandQuantityVO(t),
                                        CollectorsUtil.summingBigDecimal(
                                                        t -> BigDecimal.valueOf(t.getChangeQuantity()))));
        if (MapUtils.isEmpty(sumChangeQtyMap)) {
            return;
        }

        // ???????????????????????????MT_INV_ONHAND_QUANTITY????????????????????????
        final Map<MtInvOnhandQuantityVO, MtInvOnhandQuantity> onhandQuantityMap = self().selectByCondition(Condition
                        .builder(MtInvOnhandQuantity.class)
                        .andWhere(Sqls.custom().andEqualTo(MtInvOnhandQuantity.FIELD_TENANT_ID, tenantId)
                                        .andIn(MtInvOnhandQuantity.FIELD_SITE_ID, new ArrayList<>(siteIdSet), true))
                        .andWhere(Sqls.custom().andIn(MtInvOnhandQuantity.FIELD_MATERIAL_ID,
                                        new ArrayList<>(materialIdSet), true))
                        .andWhere(Sqls.custom().andIn(MtInvOnhandQuantity.FIELD_LOCATOR_ID,
                                        new ArrayList<>(locatorIdSet), true))
                        .andWhere(Sqls.custom().andIn(MtInvOnhandQuantity.FIELD_OWNER_ID, new ArrayList<>(ownerIdSet),
                                        true))

                        .andWhere(Sqls.custom().andIn(MtInvOnhandQuantity.FIELD_OWNER_TYPE,
                                        new ArrayList<>(ownerTypeSet), true))
                        .andWhere(Sqls.custom().andIn(MtInvOnhandQuantity.FIELD_LOT_CODE, new ArrayList<>(lotCodeSet),
                                        true))
                        .build()).stream()
                        .collect(Collectors.toMap(
                                        t -> onhandQuantityTransferMapper.invOnhandQuantityToInvOnhandQuantityVO(t),
                                        t -> t));

        // ??????????????????????????????
        List<String> onhandQuantityIds =
                        sumChangeQtyMap.keySet().stream().map(onhandQuantityMap::get).filter(Objects::nonNull)
                                        .map(MtInvOnhandQuantity::getOnhandQuantityId).collect(Collectors.toList());

        // ????????????????????????????????????????????????
        Map<MtInvOnhandQuantityVO, MtInvOnhandQuantity> invOnhandQuantityMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(onhandQuantityIds)) {
            invOnhandQuantityMap = self().selectByCondition(Condition.builder(MtInvOnhandQuantity.class)
                            .andWhere(Sqls.custom().andEqualTo(MtInvOnhandQuantity.FIELD_TENANT_ID, tenantId)
                                            .andIn(MtInvOnhandQuantity.FIELD_ONHAND_QUANTITY_ID, onhandQuantityIds))
                            .forUpdate().build()).stream()
                            .collect(Collectors.toMap(
                                            t -> onhandQuantityTransferMapper.invOnhandQuantityToInvOnhandQuantityVO(t),
                                            t -> t));
        }

        Long userId = MtUserClient.getCurrentUserId();
        Date now = new Date();

        // ?????????????????????
        List<Map.Entry<MtInvOnhandQuantityVO, BigDecimal>> insertQuantityList = new ArrayList<>();

        // sql??????
        List<String> sqlList = new ArrayList<>(sumChangeQtyMap.size() * 2);

        // ???????????????????????????ID???CID
        SequenceInfo sequenceInfo = MtSqlHelper.getSequenceInfo(MtInvJournal.class);
        List<String> journalIds =
                        mtCustomDbRepository.getNextKeys(sequenceInfo.getPrimarySequence(), sumChangeQtyMap.size());
        List<String> journalCIds =
                        mtCustomDbRepository.getNextKeys(sequenceInfo.getCidSequence(), sumChangeQtyMap.size());
        // ???????????????id?????????
        int journalIdCursor = 0;

        for (Map.Entry<MtInvOnhandQuantityVO, BigDecimal> entry : sumChangeQtyMap.entrySet()) {
            MtInvOnhandQuantity invOnhandQuantity = invOnhandQuantityMap.get(entry.getKey());
            if (invOnhandQuantity == null) {
                // ??????????????????????????????
                insertQuantityList.add(entry);
            } else {
                // ????????????????????????sql
                invOnhandQuantity.setOnhandQuantity(entry.getValue()
                                .add(BigDecimal.valueOf(invOnhandQuantity.getOnhandQuantity())).doubleValue());
                if (invOnhandQuantity.getOnhandQuantity().compareTo(0.0D) != 0) {
                    invOnhandQuantity.setLastUpdatedBy(userId);
                    invOnhandQuantity.setLastUpdateDate(now);
                    sqlList.addAll(mtCustomDbRepository.getUpdateSql(invOnhandQuantity));
                } else {
                    // ????????????????????????????????????0???????????????????????????
                    sqlList.addAll(mtCustomDbRepository.getDeleteSql(invOnhandQuantity));
                }

                MtInvJournal invJournal = onhandQuantityTransferMapper.invOnhandQuantityToInvJournal(invOnhandQuantity);
                invJournal.setTenantId(tenantId);
                invJournal.setJournalId(journalIds.get(journalIdCursor));
                invJournal.setCid(Long.valueOf(journalCIds.get(journalIdCursor)));
                invJournal.setEventId(dto.getEventId());
                invJournal.setEventBy(userId);
                invJournal.setEventTime(now);
                invJournal.setChangeQuantity(entry.getValue().doubleValue());
                invJournal.setCreatedBy(userId);
                invJournal.setCreationDate(now);
                invJournal.setLastUpdatedBy(userId);
                invJournal.setLastUpdateDate(now);
                sqlList.addAll(mtCustomDbRepository.getInsertSql(invJournal));
                journalIdCursor++;

            }
        }

        // ?????????????????????
        if (CollectionUtils.isNotEmpty(insertQuantityList)) {
            // ????????????ID???CID
            sequenceInfo = MtSqlHelper.getSequenceInfo(MtInvOnhandQuantity.class);
            List<String> holdIds = mtCustomDbRepository.getNextKeys(sequenceInfo.getPrimarySequence(),
                            insertQuantityList.size());
            List<String> holdCIds =
                            mtCustomDbRepository.getNextKeys(sequenceInfo.getCidSequence(), insertQuantityList.size());
            // ?????????????????????
            int index = 0;
            for (Map.Entry<MtInvOnhandQuantityVO, BigDecimal> entry : insertQuantityList) {
                // ????????????sql
                MtInvOnhandQuantity invOnhandQuantity =
                                onhandQuantityTransferMapper.invOnhandQuantityVOToInvOnhandQuantity(entry.getKey());
                invOnhandQuantity.setTenantId(tenantId);
                invOnhandQuantity.setOnhandQuantityId(holdIds.get(index));
                invOnhandQuantity.setCid(Long.valueOf(holdCIds.get(index)));
                invOnhandQuantity.setOnhandQuantity(entry.getValue().doubleValue());
                invOnhandQuantity.setCreatedBy(userId);
                invOnhandQuantity.setCreationDate(now);
                invOnhandQuantity.setLastUpdatedBy(userId);
                invOnhandQuantity.setLastUpdateDate(now);
                sqlList.addAll(mtCustomDbRepository.getInsertSql(invOnhandQuantity));
                index++;

                // ?????????????????????sql
                MtInvJournal invJournal = onhandQuantityTransferMapper.invOnhandQuantityToInvJournal(invOnhandQuantity);
                invJournal.setTenantId(tenantId);
                invJournal.setJournalId(journalIds.get(journalIdCursor));
                invJournal.setCid(Long.valueOf(journalCIds.get(journalIdCursor)));
                invJournal.setEventId(dto.getEventId());
                invJournal.setEventBy(userId);
                invJournal.setEventTime(now);
                invJournal.setChangeQuantity(entry.getValue().doubleValue());
                invJournal.setCreatedBy(userId);
                invJournal.setCreationDate(now);
                invJournal.setLastUpdatedBy(userId);
                invJournal.setLastUpdateDate(now);
                sqlList.addAll(mtCustomDbRepository.getInsertSql(invJournal));
                journalIdCursor++;
            }
        }

        if (CollectionUtils.isNotEmpty(sqlList)) {
            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
    }
}
