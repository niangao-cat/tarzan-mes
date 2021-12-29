package tarzan.material.infra.repository.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.entity.MtPfepDistribution;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.material.domain.repository.MtPfepDistributionRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtPfepDistributionVO;
import tarzan.material.domain.vo.MtPfepDistributionVO2;
import tarzan.material.domain.vo.MtPfepDistributionVO3;
import tarzan.material.domain.vo.MtUomVO;
import tarzan.material.infra.mapper.MtPfepDistributionMapper;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModAreaDistributionRepository;
import tarzan.modeling.domain.repository.MtModAreaRepository;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.vo.MtModAreaDistributionVO;
import tarzan.modeling.domain.vo.MtModAreaDistributionVO2;


/**
 * 物料配送属性 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@Component
public class MtPfepDistributionRepositoryImpl extends BaseRepositoryImpl<MtPfepDistribution>
                implements MtPfepDistributionRepository {

    @Autowired
    private MtPfepDistributionMapper mtPfepDistributionMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired

    private MtModAreaRepository mtModAreaRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;


    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepository;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtModAreaDistributionRepository mtModAreaDistributionRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pfepDistributionAttrPropertyUpdate(Long tenantId, MtExtendVO10 mtExtendVO10) {
        if (StringUtils.isEmpty(mtExtendVO10.getKeyId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "keyId ", "【API：pfepDistributionAttrPropertyUpdate】"));
        }
        MtPfepDistribution pfepDistribution = new MtPfepDistribution();
        pfepDistribution.setTenantId(tenantId);
        pfepDistribution.setPfepDistributionId(mtExtendVO10.getKeyId());
        pfepDistribution = mtPfepDistributionMapper.selectOne(pfepDistribution);
        if (null == pfepDistribution) {
            throw new MtException("MT_MATERIAL_0093",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0093", "MATERIAL",
                                            mtExtendVO10.getKeyId(), "mt_pfep_distribution",
                                            "【API:pfepDistributionAttrPropertyUpdate】"));
        }
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_pfep_distribution_attr", mtExtendVO10.getKeyId(),
                        mtExtendVO10.getEventId(), mtExtendVO10.getAttrs());

    }


    @Override
    public List<MtPfepDistributionVO2> materialDistributionPfepQuery(Long tenantId, MtPfepDistributionVO3 dto) {
        // 返回结果
        List<MtPfepDistributionVO2> resultList = new ArrayList<>();


        // 第一步，判断参数合规性
        // 校验参数（organizationType和organizationId）是否同时输入/同时不输入
        if (StringUtils.isNotEmpty(dto.getOrganizationId()) && StringUtils.isEmpty(dto.getOrganizationType())
                        || StringUtils.isEmpty(dto.getOrganizationId())
                                        && StringUtils.isNotEmpty(dto.getOrganizationType())) {
            throw new MtException("MT_MATERIAL_0072",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0072", "MATERIAL",
                                            "organizationType、organizationId", "【API:materialDistributionPfepQuery】"));
        }
        // 输入则校验参数organizationType等于WORKCELL
        if (StringUtils.isNotEmpty(dto.getOrganizationType()) && !"WORKCELL".equals(dto.getOrganizationType())) {
            throw new MtException("MT_MATERIAL_0089", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0089", "MATERIAL", "organizationType", "【API:materialDistributionPfepQuery】"));
        }
        // 传入参数organizationId调用API{workcellBasicPropertyGet}
        if (StringUtils.isNotEmpty(dto.getOrganizationId())) {
            MtModWorkcell mtModWorkcell =
                            mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, dto.getOrganizationId());
            if (mtModWorkcell == null) {
                throw new MtException("MT_MATERIAL_0090",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0090",
                                                "MATERIAL", "organizationId", "【API:materialDistributionPfepQuery】"));
            }
        }
        // 校验参数（materialId和siteId）是否同时输入/同时不输入
        if (StringUtils.isNotEmpty(dto.getMaterialId()) && StringUtils.isEmpty(dto.getSiteId())
                        || StringUtils.isEmpty(dto.getMaterialId()) && StringUtils.isNotEmpty(dto.getSiteId())) {
            throw new MtException("MT_MATERIAL_0072",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0072", "MATERIAL",
                                            "materialId，siteId", "【API:materialDistributionPfepQuery】"));
        }

        if (StringUtils.isNotEmpty(dto.getMaterialId()) && StringUtils.isNotEmpty(dto.getSiteId())) {
            // 输入则调用API{ materialSiteLimitRelationshipGet }，获取物料站点关系materialSiteId
            MtMaterialSite mtMaterialSite = new MtMaterialSite();
            mtMaterialSite.setSiteId(dto.getSiteId());
            mtMaterialSite.setMaterialId(dto.getMaterialId());
            String materialSiteId = mtMaterialSiteRepository.materialSiteLimitRelationGet(tenantId, mtMaterialSite);
            if (StringUtils.isEmpty(materialSiteId)) {
                throw new MtException("MT_MATERIAL_0055",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055",
                                                "MATERIAL", "materialSiteId", "【API:materialDistributionPfepQuery】"));
            } else {
                // 若参数materialSiteId也输入则校验是否与i中获取相同
                if (StringUtils.isNotEmpty(dto.getMaterialSiteId())
                                && !materialSiteId.equals(dto.getMaterialSiteId())) {
                    throw new MtException("MT_MATERIAL_0074", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_MATERIAL_0074", "MATERIAL", "【API:materialDistributionPfepQuery】"));
                } else {
                    dto.setMaterialSiteId(materialSiteId);
                }
            }

        }

        if (StringUtils.isNotEmpty(dto.getAreaId())) {
            MtModArea area = mtModAreaRepository.areaBasicPropertyGet(tenantId, dto.getAreaId());
            if (area == null) {
                throw new MtException("MT_MATERIAL_0055", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0055", "MATERIAL", "areaId", "【API:materialDistributionPfepQuery】"));
            }
        }
        // 按若参数全部为空则报错
        if (StringUtils.isEmpty(dto.getMaterialId()) && StringUtils.isEmpty(dto.getOrganizationId())
                        && StringUtils.isEmpty(dto.getOrganizationType()) && StringUtils.isEmpty(dto.getSiteId())
                        && StringUtils.isEmpty(dto.getAreaId()) && StringUtils.isEmpty(dto.getMaterialSiteId())) {
            throw new MtException("MT_MATERIAL_0091", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0091", "MATERIAL", "areaId", "【API:materialDistributionPfepQuery】"));
        }
        // 根据属性查询
        List<MtPfepDistribution> distributions = mtPfepDistributionMapper.materialDistributionPfepQuery(tenantId, dto);

        for (MtPfepDistribution distribution : distributions) {
            MtPfepDistributionVO2 result = new MtPfepDistributionVO2();
            result.setMaterialSiteId(distribution.getMaterialSiteId());
            result.setOrganizationType(distribution.getOrganizationType());
            result.setOrganizationId(distribution.getOrganizationId());
            result.setAreaId(distribution.getAreaId());
            result.setLocatorId(distribution.getLocatorId());
            result.setLocatorCapacity(distribution.getLocatorCapacity());
            result.setFromScheduleRateFlag(distribution.getFromScheduleRateFlag());
            result.setMaterialConsumeRateUomId(distribution.getMaterialConsumeRateUomId());
            result.setMaterialConsumeRate(distribution.getMaterialConsumeRate());
            result.setBufferInventory(distribution.getBufferInventory());
            result.setBufferPeriod(distribution.getBufferPeriod());
            result.setMinInventory(distribution.getMinInventory());
            result.setMaxInventory(distribution.getMaxInventory());
            result.setPackQty(distribution.getPackQty());
            result.setMultiplesOfPackFlag(distribution.getMultiplesOfPackFlag());
            result.setRouteLocatorId(distribution.getAreaLocatorId());

            // API{areaDistrubutionPropertyGet}循环获取剩余输出参数
            MtModAreaDistributionVO distributionVO = new MtModAreaDistributionVO();
            distributionVO.setAreaId(distribution.getAreaId());
            distributionVO.setLocatorId(distribution.getLocatorId());
            MtModAreaDistributionVO2 propertyGet =
                            mtModAreaDistributionRepository.areaDistributionPropertyGet(tenantId, distributionVO);
            if (propertyGet == null) {
                throw new MtException("MT_MATERIAL_0092", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0092", "MATERIAL", "【API:materialDistributionPfepQuery】"));
            }
            result.setDistributionMode(propertyGet.getDistributionMode());
            result.setPullTimeIntervalFlag(propertyGet.getPullTimeIntervalFlag());
            result.setDistributionCycle(propertyGet.getDistributionCycle());
            result.setBusinessType(propertyGet.getBusinessType());
            result.setInstructCreatedByEo(propertyGet.getInstructCreatedByEo());
            result.setSourceLocatorId(propertyGet.getSourceLocatorId());
            result.setSequence(propertyGet.getSequence());
            result.setPullToArrive(propertyGet.getPullToArrive());

            resultList.add(result);
        }
        return resultList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String materialPfepDistributionUpdate(Long tenantId, MtPfepDistributionVO vo) {
        // 1.检验参数
        if (StringUtils.isNotEmpty(vo.getMaterialConsumeRateUomId())) {
            MtUomVO mtUomVO = mtUomRepository.uomPropertyGet(tenantId, vo.getMaterialConsumeRateUomId());
            if (null == mtUomVO) {
                throw new MtException("MT_MATERIAL_0055",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055",
                                                "MATERIAL", "materialConsumeRateUomId",
                                                "【API:materialPfepDistributionUpdate】"));
            }
        }

        if (StringUtils.isNotEmpty(vo.getLocatorId()) && StringUtils.isNotEmpty(vo.getAreaLocatorId())) {
            List<String> locatorIdList = Arrays.asList(vo.getLocatorId(), vo.getAreaLocatorId());
            List<MtModLocator> mtModLocators =
                            mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, locatorIdList);
            if (CollectionUtils.isEmpty(mtModLocators)) {
                throw new MtException("MT_MATERIAL_0055",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055",
                                                "MATERIAL", " locatorId（routeLocatorId）",
                                                "【API:materialPfepDistributionUpdate】"));
            }
        }

        // 当OrganizationType不为空 且不等于WORKCELL则报错 2020年2月18日与郭王勋确认修改 by xujin
        if (StringUtils.isNotEmpty(vo.getOrganizationType()) && !"WORKCELL".equals(vo.getOrganizationType())) {
            throw new MtException("MT_MATERIAL_0089",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0089", "MATERIAL",
                                            " organizationType", "【API:materialPfepDistributionUpdate】"));
        }

        if ((StringUtils.isNotEmpty(vo.getOrganizationType()) && StringUtils.isEmpty(vo.getOrganizationId()))
                        || (StringUtils.isEmpty(vo.getOrganizationType())
                                        && StringUtils.isNotEmpty(vo.getOrganizationId()))) {
            throw new MtException("MT_MATERIAL_0072",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0072", "MATERIAL",
                                            " organizationType、organizationId",
                                            "【API:materialPfepDistributionUpdate】"));
        }

        if (StringUtils.isNotEmpty(vo.getMaterialSiteId())) {
            MtMaterialSite mtMaterialSite =
                            mtMaterialSiteRepository.relationLimitMaterialSiteGet(tenantId, vo.getMaterialSiteId());
            if (null == mtMaterialSite || !"Y".equals(mtMaterialSite.getEnableFlag())) {
                throw new MtException("MT_MATERIAL_0055",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055",
                                                "MATERIAL", "materialSiteId", "【API:materialPfepDistributionUpdate】"));
            }

            String siteId = mtMaterialSite.getSiteId();

            MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, siteId);
            // 走到这说明查询结果不为空，且enableFlag='Y'
            if (!"MANUFACTURING".equals(mtModSite.getSiteType())) {
                throw new MtException("MT_MATERIAL_0079",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0079",
                                                "MATERIAL", "materialSiteId", "【API:materialPfepDistributionUpdate】"));
            }
        }

        // 更新或新增前唯一性校验
        if (StringUtils.isNotEmpty(vo.getMaterialSiteId()) && StringUtils.isNotEmpty(vo.getOrganizationId())
                        && StringUtils.isNotEmpty(vo.getOrganizationType())) {
            MtPfepDistribution distribution = new MtPfepDistribution();
            distribution.setTenantId(tenantId);
            distribution.setMaterialSiteId(vo.getMaterialSiteId());
            distribution.setOrganizationId(vo.getOrganizationId());
            distribution.setOrganizationType(vo.getOrganizationType());
            distribution = mtPfepDistributionMapper.selectOne(distribution);
            if (null != distribution) {
                throw new MtException("MT_MATERIAL_0088",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0088",
                                                "MATERIAL", "materialSiteId、organizationId、organizationType",
                                                "【API:materialPfepDistributionUpdate】"));
            }
        }

        // 2.根据传入参数判断新增或更新模式
        // a) 若pfepDistributionId传入不为空,更新操作
        if (StringUtils.isNotEmpty(vo.getPfepDistributionId())) {
            // 检验输入id是否存在
            MtPfepDistribution distribution = new MtPfepDistribution();
            distribution.setTenantId(tenantId);
            distribution.setPfepDistributionId(vo.getPfepDistributionId());
            MtPfepDistribution pfepDistribution = mtPfepDistributionMapper.selectOne(distribution);
            if (null == pfepDistribution) {
                throw new MtException("MT_MATERIAL_0055",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0055",
                                                "MATERIAL", "pfepDistributionId",
                                                "【API:materialPfepDistributionUpdate】"));
            }

            // 开始更新数据
            if (null != vo.getMaterialSiteId()) {
                distribution.setMaterialSiteId(vo.getMaterialSiteId());
            }
            if (null != vo.getOrganizationType()) {
                distribution.setOrganizationType(vo.getOrganizationType());
            }
            if (null != vo.getOrganizationId()) {
                distribution.setOrganizationId(vo.getOrganizationId());
            }
            if (null != vo.getLocatorId()) {
                distribution.setLocatorId(vo.getLocatorId());
            }
            if (null != vo.getLocatorCapacity()) {
                distribution.setLocatorCapacity(vo.getLocatorCapacity());
            }
            if (null != vo.getFromScheduleRateFlag()) {
                distribution.setFromScheduleRateFlag(vo.getFromScheduleRateFlag());
            }
            if (null != vo.getMaterialConsumeRateUomId()) {
                distribution.setMaterialConsumeRateUomId(vo.getMaterialConsumeRateUomId());
            }
            if (null != vo.getMaterialConsumeRate()) {
                distribution.setMaterialConsumeRate(vo.getMaterialConsumeRate());
            }
            if (null != vo.getBufferPeriod()) {
                distribution.setBufferPeriod(vo.getBufferPeriod());
            }
            if (null != vo.getBufferInventory()) {
                distribution.setBufferInventory(vo.getBufferInventory());
            }
            if (null != vo.getMinInventory()) {
                distribution.setMinInventory(vo.getMinInventory());
            }
            if (null != vo.getMaxInventory()) {
                distribution.setMaxInventory(vo.getMaxInventory());
            }
            if (null != vo.getPackQty()) {
                distribution.setPackQty(vo.getPackQty());
            }
            if (null != vo.getMultiplesOfPackFlag()) {
                distribution.setMultiplesOfPackFlag(vo.getMultiplesOfPackFlag());
            }
            if (null != vo.getAreaId()) {
                distribution.setAreaId(vo.getAreaId());
            }
            if (null != vo.getAreaLocatorId()) {
                distribution.setAreaLocatorId(vo.getAreaLocatorId());
            }
            if (null != vo.getEnableFlag()) {
                distribution.setEnableFlag(vo.getEnableFlag());
            }

            this.updateByPrimaryKey(distribution);

            return distribution.getPfepDistributionId();
        } else {
            // 新增数据
            MtPfepDistribution distribution = new MtPfepDistribution();
            distribution.setTenantId(tenantId);

            if (null != vo.getMaterialSiteId()) {
                distribution.setMaterialSiteId(vo.getMaterialSiteId());
            }
            if (null != vo.getOrganizationType()) {
                distribution.setOrganizationType(vo.getOrganizationType());
            }
            if (null != vo.getOrganizationId()) {
                distribution.setOrganizationId(vo.getOrganizationId());
            }
            if (null != vo.getLocatorId()) {
                distribution.setLocatorId(vo.getLocatorId());
            }
            if (null != vo.getLocatorCapacity()) {
                distribution.setLocatorCapacity(vo.getLocatorCapacity());
            }
            if (null != vo.getFromScheduleRateFlag()) {
                distribution.setFromScheduleRateFlag(vo.getFromScheduleRateFlag());
            }
            if (null != vo.getMaterialConsumeRateUomId()) {
                distribution.setMaterialConsumeRateUomId(vo.getMaterialConsumeRateUomId());
            }
            if (null != vo.getMaterialConsumeRate()) {
                distribution.setMaterialConsumeRate(vo.getMaterialConsumeRate());
            }
            if (null != vo.getBufferPeriod()) {
                distribution.setBufferPeriod(vo.getBufferPeriod());
            }
            if (null != vo.getBufferInventory()) {
                distribution.setBufferInventory(vo.getBufferInventory());
            }
            if (null != vo.getMinInventory()) {
                distribution.setMinInventory(vo.getMinInventory());
            }
            if (null != vo.getMaxInventory()) {
                distribution.setMaxInventory(vo.getMaxInventory());
            }
            if (null != vo.getPackQty()) {
                distribution.setPackQty(vo.getPackQty());
            }
            if (null != vo.getMultiplesOfPackFlag()) {
                distribution.setMultiplesOfPackFlag(vo.getMultiplesOfPackFlag());
            }
            if (null != vo.getAreaId()) {
                distribution.setAreaId(vo.getAreaId());
            }
            if (null != vo.getAreaLocatorId()) {
                distribution.setAreaLocatorId(vo.getAreaLocatorId());
            }
            if (null != vo.getEnableFlag()) {
                distribution.setEnableFlag(vo.getEnableFlag());
            }

            this.insertSelective(distribution);
            return distribution.getPfepDistributionId();
        }
    }
}
