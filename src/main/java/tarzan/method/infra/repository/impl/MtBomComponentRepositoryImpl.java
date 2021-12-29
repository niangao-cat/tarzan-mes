package tarzan.method.infra.repository.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.*;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.repository.*;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtMaterialVO1;
import tarzan.material.domain.vo.MtUomVO1;
import tarzan.method.domain.entity.*;
import tarzan.method.domain.repository.*;
import tarzan.method.domain.vo.*;
import tarzan.method.infra.mapper.MtBomComponentMapper;
import tarzan.method.infra.mapper.MtBomMapper;
import tarzan.method.infra.mapper.MtBomSubstituteMapper;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;

/**
 * 装配清单行 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@Component
public class MtBomComponentRepositoryImpl extends BaseRepositoryImpl<MtBomComponent>
                implements MtBomComponentRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtBomRepository mtBomRepository;

    @Autowired
    private MtBomHisRepository mtBomHisRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtBomSubstituteRepository mtBomSubstituteRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepository;

    @Autowired
    private MtBomSubstituteGroupRepository mtBomSubstituteGroupRepository;

    @Autowired
    private MtBomReferencePointRepository mtBomReferencePointRepository;

    @Autowired
    private MtBomSiteAssignRepository mtBomSiteAssignRepository;

    @Autowired
    private CustomSequence customSequence;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtBomComponentMapper mtBomComponentMapper;

    @Autowired
    private MtBomMapper mtBomMapper;

    @Autowired
    private MtBomSubstituteMapper mtBomSubstituteMapper;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtPfepManufacturingRepository mtPfepManufacturingRepository;

    @Autowired
    private MtPfepScheduleRepository mtPfepScheduleRepository;

    @Override
    public MtBomComponentVO8 bomComponentBasicGet(Long tenantId, String bomComponentId) {
        if (StringUtils.isEmpty(bomComponentId)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomComponentId", "【API:bomComponentBasicGet】"));
        }

        MtBomComponent mtBomComponent = new MtBomComponent();
        mtBomComponent.setBomComponentId(bomComponentId);
        mtBomComponent.setTenantId(tenantId);
        mtBomComponent = this.mtBomComponentMapper.selectOne(mtBomComponent);
        if (null == mtBomComponent) {
            return null;
        }

        MtBomComponentVO8 bomComponent = new MtBomComponentVO8();
        BeanUtils.copyProperties(mtBomComponent, bomComponent);

        MtBom mtBom = new MtBom();
        mtBom.setBomId(mtBomComponent.getBomId());
        mtBom.setTenantId(tenantId);
        mtBom = this.mtBomMapper.selectOne(mtBom);
        if (null == mtBom || null == mtBom.getPrimaryQty()) {
            bomComponent.setPreQty(Double.valueOf(0.0D));
            return bomComponent;
        }

        Double componentQty = mtBomComponent.getQty() == null ? Double.valueOf(0.0D) : mtBomComponent.getQty();
        Double bomQty = mtBom.getPrimaryQty();
        BigDecimal qty = new BigDecimal(componentQty.toString());
        BigDecimal primaryQty = new BigDecimal(bomQty.toString());
        bomComponent.setPreQty(qty.divide(primaryQty, 6, BigDecimal.ROUND_HALF_DOWN).doubleValue());
        return bomComponent;
    }

    @Override
    public List<MtBomComponentVO16> propertyLimitBomComponentQuery(Long tenantId, MtBomComponentVO condition) {
        if (ObjectFieldsHelper.isAllFieldNull(condition)) {
            throw new MtException("MT_BOM_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0002", "BOM", "【API:propertyLimitBomComponentQuery】"));
        }

        List<MtBomComponent> mtBomComponents = this.mtBomComponentMapper.selectBomComponents(tenantId, condition);
        if (CollectionUtils.isEmpty(mtBomComponents)) {
            return Collections.emptyList();
        }

        Map<String, List<MtBomComponent>> listMap =
                        mtBomComponents.stream().collect(Collectors.groupingBy(MtBomComponent::getBomId));
        final List<MtBomComponentVO16> list = new ArrayList<>();
        listMap.entrySet().stream().forEach(t -> {
            MtBomComponentVO16 temp = new MtBomComponentVO16();
            temp.setBomId(t.getKey());
            temp.setBomComponentId(
                            t.getValue().stream().map(MtBomComponent::getBomComponentId).collect(Collectors.toList()));
            list.add(temp);
        });
        return list;
    }

    @Override
    public Double attritionLimitComponentQtyCalculate(Long tenantId, MtBomComponentVO6 dto) {
        if (StringUtils.isEmpty(dto.getBomComponentId())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomComponentId", "【API:attritionLimitComponentQtyCalculate】"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "qty", "【API:attritionLimitComponentQtyCalculate】"));
        }

        MtBomComponent mtBomComponent = new MtBomComponent();
        mtBomComponent.setTenantId(tenantId);
        mtBomComponent.setBomComponentId(dto.getBomComponentId());
        mtBomComponent = this.mtBomComponentMapper.selectOne(mtBomComponent);
        if (mtBomComponent == null) {
            throw new MtException("MT_BOM_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0015", "BOM", "【API:attritionLimitComponentQtyCalculate】"));
        }

        String bomId = StringUtils.isEmpty(mtBomComponent.getBomId()) ? "" : mtBomComponent.getBomId();
        Double bomComponentQty = mtBomComponent.getQty() == null ? Double.valueOf(0.0D) : mtBomComponent.getQty();
        String attritionPolicy = StringUtils.isEmpty(mtBomComponent.getAttritionPolicy()) ? ""
                        : mtBomComponent.getAttritionPolicy();
        Double attritionChance = mtBomComponent.getAttritionChance() == null ? Double.valueOf(0.0D)
                        : mtBomComponent.getAttritionChance();
        Double attritionQty = mtBomComponent.getAttritionQty() == null ? Double.valueOf(0.0D)
                        : mtBomComponent.getAttritionQty();

        MtBom mtBom = new MtBom();
        mtBom.setTenantId(tenantId);
        mtBom.setBomId(bomId);
        mtBom = this.mtBomMapper.selectOne(mtBom);
        if (mtBom == null) {
            throw new MtException("MT_BOM_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0004", "BOM", "【API:attritionLimitComponentQtyCalculate】"));
        }

        Double primaryQty = mtBom.getPrimaryQty() == null ? Double.valueOf(1.0D) : mtBom.getPrimaryQty();
        BigDecimal componentQty = null;
        BigDecimal tmpCalcQty = null;

        if (StringUtils.isEmpty(attritionPolicy) || "N".equals(dto.getAttritionFlag())) {
            // componentQty = qty * bomComponentQty / primaryQty

            componentQty = new BigDecimal(dto.getQty().toString()).multiply(new BigDecimal(bomComponentQty.toString()))
                            .divide(new BigDecimal(primaryQty.toString()), 10, BigDecimal.ROUND_HALF_DOWN);
        } else if (attritionPolicy.equals("1")) {
            // componentQty = qty * (bomComponentQty + attritionQty)/primaryQty

            tmpCalcQty = new BigDecimal(bomComponentQty.toString()).add(new BigDecimal(attritionQty.toString()));
            componentQty = new BigDecimal(dto.getQty().toString()).multiply(tmpCalcQty)
                            .divide(new BigDecimal(primaryQty.toString()), 10, BigDecimal.ROUND_HALF_DOWN);
        } else if (attritionPolicy.equals("2")) {
            // componentQty=qty*bomComponentQty*(1+attritionChance/100)/primaryQty

            tmpCalcQty = new BigDecimal(attritionChance.toString()).divide(new BigDecimal("100"), 10,
                            BigDecimal.ROUND_HALF_DOWN);
            tmpCalcQty = BigDecimal.ONE.add(tmpCalcQty);
            componentQty = new BigDecimal(dto.getQty().toString()).multiply(new BigDecimal(bomComponentQty.toString()))
                            .multiply(tmpCalcQty)
                            .divide(new BigDecimal(primaryQty.toString()), 10, BigDecimal.ROUND_HALF_DOWN);
        } else if (attritionPolicy.equals("3")) {
            // componentQty=qty*(bomComponentQty+attritionQty)*(1+attritionChance/100)/primaryQty

            BigDecimal tmpQty = new BigDecimal(bomComponentQty.toString()).add(new BigDecimal(attritionQty.toString()));
            tmpCalcQty = new BigDecimal(attritionChance.toString()).divide(new BigDecimal("100"), 10,
                            BigDecimal.ROUND_HALF_DOWN);
            tmpCalcQty = BigDecimal.ONE.add(tmpCalcQty);
            componentQty = new BigDecimal(dto.getQty().toString()).multiply(tmpQty).multiply(tmpCalcQty)
                            .divide(new BigDecimal(primaryQty.toString()), 10, BigDecimal.ROUND_HALF_DOWN);
        } else {
            throw new MtException("MT_BOM_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0017", "BOM", "【API:attritionLimitComponentQtyCalculate】"));
        }

        return componentQty.doubleValue();
    }

    @Override
    public List<String> materialLimitBomQuery(Long tenantId, String siteId, String materialId,
                    String bomComponentType) {
        if (StringUtils.isEmpty(siteId)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "siteId", "【API:materialLimitBomQuery】"));
        }
        if (StringUtils.isEmpty(materialId)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "materialId", "【API:materialLimitBomQuery】"));
        }

        if (StringUtils.isNotEmpty(bomComponentType)) {
            List<MtGenType> mtGenTypes = this.mtGenTypeRepository.getGenTypes(tenantId, "BOM", "BOM_COMPONENT_TYPE");
            if (CollectionUtils.isEmpty(mtGenTypes)) {
                throw new MtException("MT_BOM_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0018", "BOM", "【API:materialLimitBomQuery】"));
            }
            if (!mtGenTypes.stream().anyMatch(t -> t.getTypeCode().equals(bomComponentType))) {
                throw new MtException("MT_BOM_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0019", "BOM", "【API:materialLimitBomQuery】"));
            }
        }

        List<MtBomComponent> mtBomComponents = this.mtBomComponentMapper.selectByBomCompCondition(tenantId, siteId,
                        materialId, bomComponentType);
        if (CollectionUtils.isEmpty(mtBomComponents)) {
            return Collections.emptyList();
        }

        return mtBomComponents.stream().map(MtBomComponent::getBomId).collect(Collectors.toList());
    }

    @Override
    public List<MtBomComponentVO2> bomComponentQtyCalculate(Long tenantId, MtBomComponentVO5 dto) {
        if (StringUtils.isEmpty(dto.getBomId())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomId", "【API:bomComponentQtyCalculate】"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "qty", "【API:bomComponentQtyCalculate】"));
        }

        MtBom mtBom = new MtBom();
        mtBom.setTenantId(tenantId);
        mtBom.setBomId(dto.getBomId());
        mtBom = this.mtBomMapper.selectOne(mtBom);
        if (mtBom == null) {
            throw new MtException("MT_BOM_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0004", "BOM", "【API:bomComponentQtyCalculate】"));
        }

        List<MtBomComponent> mtBomComponents =
                        this.mtBomComponentMapper.selectEnableByBomId(tenantId, mtBom.getBomId());
        if (CollectionUtils.isEmpty(mtBomComponents)) {
            return Collections.emptyList();
        }

        /*
         * 新增逻辑 2019/04/18 如果 bomComponentId 不为空，且通过有效性校验，则后续步骤均只计算传入的bomComponentId的用量
         */
        if (StringUtils.isNotEmpty(dto.getBomComponentId())) {
            Map<String, MtBomComponent> bomComponentMap =
                            mtBomComponents.stream().collect(Collectors.toMap(t -> t.getBomComponentId(), m -> m));

            MtBomComponent currentBomComponent = bomComponentMap.get(dto.getBomComponentId());
            if (currentBomComponent == null || StringUtils.isEmpty(currentBomComponent.getBomComponentId())) {
                throw new MtException("MT_BOM_0054",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0054", "BOM",
                                                "bomComponentId:" + dto.getBomComponentId(),
                                                "【API:bomComponentQtyCalculate】"));
            }

            mtBomComponents.clear();
            mtBomComponents.add(currentBomComponent);
        }
        // 替代数据
        List<String> bomComponentIds =
                        mtBomComponents.stream().map(MtBomComponent::getBomComponentId).collect(Collectors.toList());
        Date curDate = new Date();
        Map<String, MtBomSubstituteGroupVO6> groupVO6Map = new HashMap<>(bomComponentIds.size());
        if (CollectionUtils.isNotEmpty(bomComponentIds)) {
            List<MtBomSubstituteGroup> mtBomSubstituteGroups = mtBomSubstituteGroupRepository
                            .selectByCondition(Condition.builder(MtBomSubstituteGroup.class).andWhere(Sqls.custom()
                                            .andEqualTo(MtBomSubstituteGroup.FIELD_TENANT_ID, tenantId)
                                            .andEqualTo(MtBomSubstituteGroup.FIELD_ENABLE_FLAG, MtBaseConstants.YES))
                                            .andWhere(Sqls.custom().andIn(MtBomSubstituteGroup.FIELD_BOM_COMPONENT_ID,
                                                            bomComponentIds))
                                            .build());
            Map<String, List<MtBomSubstituteGroup>> mtBomSubstituteGroupMap = mtBomSubstituteGroups.stream()
                            .collect(Collectors.groupingBy(MtBomSubstituteGroup::getBomComponentId));

            List<String> bomSubstituteGroupIds = mtBomSubstituteGroups.stream()
                            .map(MtBomSubstituteGroup::getBomSubstituteGroupId).collect(Collectors.toList());
            Map<String, List<MtBomSubstitute>> mtBomSubstituteMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(bomSubstituteGroupIds)) {
                List<MtBomSubstitute> mtBomSubstitutes = mtBomSubstituteRepository.selectByCondition(Condition
                                .builder(MtBomSubstitute.class)
                                .andWhere(Sqls.custom().andEqualTo(MtBomSubstitute.FIELD_TENANT_ID, tenantId)
                                                .andLessThan(MtBomSubstitute.FIELD_DATE_FROM, curDate))
                                .andWhere(Sqls.custom().andEqualTo(MtAssembleControl.FIELD_DATE_TO, null)
                                                .orGreaterThanOrEqualTo(MtAssembleControl.FIELD_DATE_TO, curDate))
                                .andWhere(Sqls.custom().andIn(MtBomSubstituteGroup.FIELD_BOM_SUBSTITUTE_GROUP_ID,
                                                bomSubstituteGroupIds))
                                .build());
                mtBomSubstituteMap = mtBomSubstitutes.stream()
                                .collect(Collectors.groupingBy(MtBomSubstitute::getBomSubstituteGroupId));
            }
            for (Map.Entry<String, List<MtBomSubstituteGroup>> entry : mtBomSubstituteGroupMap.entrySet()) {
                MtBomSubstituteGroupVO6 mtBomSubstituteGroupVO6 = new MtBomSubstituteGroupVO6();
                mtBomSubstituteGroupVO6.setBomComponentId(entry.getKey());
                mtBomSubstituteGroupVO6.setMtBomSubstituteGroup(entry.getValue().get(0));
                mtBomSubstituteGroupVO6.setMtBomSubstitutes(
                                mtBomSubstituteMap.get(entry.getValue().get(0).getBomSubstituteGroupId()));
                groupVO6Map.put(entry.getKey(), mtBomSubstituteGroupVO6);
            }
        }


        List<MtBomComponentVO2> result = new ArrayList<MtBomComponentVO2>();
        for (MtBomComponent mtBomComponent : mtBomComponents) {
            MtBomSubstituteGroupVO6 mtBomSubstituteGroupVO6 = groupVO6Map.get(mtBomComponent.getBomComponentId());
            MtBomComponentVO6 bomComponentVO6 = new MtBomComponentVO6();

            // 新增逻辑（如果attritionFlag传入为Y，直接调用，如果不为Y，传入为N）
            if (!"Y".equals(dto.getAttritionFlag())) {
                bomComponentVO6.setAttritionFlag("N");
            } else {
                bomComponentVO6.setAttritionFlag(dto.getAttritionFlag());
            }
            bomComponentVO6.setBomComponentId(mtBomComponent.getBomComponentId());
            bomComponentVO6.setQty(1.0D);
            Double step4Qty = attritionLimitComponentQtyCalculate(tenantId, bomComponentVO6);

            MtBomSubstituteVO6 bomSubstituteVO6 = new MtBomSubstituteVO6();
            bomSubstituteVO6.setBomComponentId(mtBomComponent.getBomComponentId());
            bomSubstituteVO6.setQty(1.0D);

            // 新增逻辑（如果substituteFlag传入为Y，直接调用，如果不为Y，传入为N）
            if (!"Y".equals(dto.getSubstituteFlag())) {
                bomSubstituteVO6.setSubstituteFlag("N");
            } else {
                bomSubstituteVO6.setSubstituteFlag(dto.getSubstituteFlag());
            }

            List<MtBomSubstituteVO3> bomSubstitutes = this.mtBomSubstituteRepository.bomSubstituteQtyCalculate(tenantId,
                            bomSubstituteVO6, mtBomComponent, mtBomSubstituteGroupVO6);
            if (CollectionUtils.isEmpty(bomSubstitutes)) {
                continue;
            }

            for (MtBomSubstituteVO3 bomSubstitute : bomSubstitutes) {
                MtBomComponentVO2 vo = new MtBomComponentVO2();
                vo.setBomComponentId(mtBomComponent.getBomComponentId());
                vo.setMaterialId(bomSubstitute.getMaterialId());

                BigDecimal componentQty =
                                new BigDecimal(dto.getQty().toString()).multiply(new BigDecimal(step4Qty.toString()))
                                                .multiply(new BigDecimal(bomSubstitute.getComponentQty().toString()));

                MtMaterialVO mtMaterialVO =
                                this.mtMaterialRepository.materialPropertyGet(tenantId, bomSubstitute.getMaterialId());
                if (mtMaterialVO != null) {
                    MtUomVO1 uomModel = new MtUomVO1();
                    uomModel.setSourceUomId(mtMaterialVO.getPrimaryUomId());
                    uomModel.setSourceValue(componentQty.doubleValue());
                    uomModel = this.mtUomRepository.uomDecimalProcess(tenantId, uomModel);
                    vo.setComponentQty(uomModel.getTargetValue());
                } else {
                    vo.setComponentQty(componentQty.doubleValue());
                }
                result.add(vo);
            }
        }
        return result;
    }
    // public List<MtBomComponentVO2> bomComponentQtyCalculate(Long tenantId, MtBomComponentVO5 dto) {
    // if (StringUtils.isEmpty(dto.getBomId())) {
    // throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
    // "MT_BOM_0001", "BOM", "bomId", "【API:bomComponentQtyCalculate】"));
    // }
    // if (dto.getQty() == null) {
    // throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
    // "MT_BOM_0001", "BOM", "qty", "【API:bomComponentQtyCalculate】"));
    // }
    //
    // MtBom mtBom = new MtBom();
    // mtBom.setTenantId(tenantId);
    // mtBom.setBomId(dto.getBomId());
    // mtBom = this.mtBomMapper.selectOne(mtBom);
    // if (mtBom == null) {
    // throw new MtException("MT_BOM_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
    // "MT_BOM_0004", "BOM", "【API:bomComponentQtyCalculate】"));
    // }
    //
    // List<MtBomComponent> mtBomComponents =
    // this.mtBomComponentMapper.selectEnableByBomId(tenantId, mtBom.getBomId());
    // if (CollectionUtils.isEmpty(mtBomComponents)) {
    // return Collections.emptyList();
    // }
    //
    // /*
    // * 新增逻辑 2019/04/18 如果 bomComponentId 不为空，且通过有效性校验，则后续步骤均只计算传入的bomComponentId的用量
    // */
    // if (StringUtils.isNotEmpty(dto.getBomComponentId())) {
    // Map<String, MtBomComponent> bomComponentMap =
    // mtBomComponents.stream().collect(Collectors.toMap(t -> t.getBomComponentId(), m -> m));
    //
    // MtBomComponent currentBomComponent = bomComponentMap.get(dto.getBomComponentId());
    // if (currentBomComponent == null || StringUtils.isEmpty(currentBomComponent.getBomComponentId()))
    // {
    // throw new MtException("MT_BOM_0054",
    // mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0054", "BOM",
    // "bomComponentId:" + dto.getBomComponentId(),
    // "【API:bomComponentQtyCalculate】"));
    // }
    //
    // mtBomComponents.clear();
    // mtBomComponents.add(currentBomComponent);
    // }
    //
    // List<MtBomComponentVO2> result = new ArrayList<MtBomComponentVO2>();
    // for (MtBomComponent mtBomComponent : mtBomComponents) {
    //
    // MtBomComponentVO6 bomComponentVO6 = new MtBomComponentVO6();
    //
    // // 新增逻辑（如果attritionFlag传入为Y，直接调用，如果不为Y，传入为N）
    // if (!"Y".equals(dto.getAttritionFlag())) {
    // bomComponentVO6.setAttritionFlag("N");
    // } else {
    // bomComponentVO6.setAttritionFlag(dto.getAttritionFlag());
    // }
    // bomComponentVO6.setBomComponentId(mtBomComponent.getBomComponentId());
    // bomComponentVO6.setQty(1.0D);
    // Double step4Qty = attritionLimitComponentQtyCalculate(tenantId, bomComponentVO6);
    //
    // MtBomSubstituteVO6 bomSubstituteVO6 = new MtBomSubstituteVO6();
    // bomSubstituteVO6.setBomComponentId(mtBomComponent.getBomComponentId());
    // bomSubstituteVO6.setQty(1.0D);
    //
    // // 新增逻辑（如果substituteFlag传入为Y，直接调用，如果不为Y，传入为N）
    // if (!"Y".equals(dto.getSubstituteFlag())) {
    // bomSubstituteVO6.setSubstituteFlag("N");
    // } else {
    // bomSubstituteVO6.setSubstituteFlag(dto.getSubstituteFlag());
    // }
    //
    // List<MtBomSubstituteVO3> bomSubstitutes =
    // this.mtBomSubstituteRepository.bomSubstituteQtyCalculate(tenantId, bomSubstituteVO6);
    // if (CollectionUtils.isEmpty(bomSubstitutes)) {
    // continue;
    // }
    //
    // for (MtBomSubstituteVO3 bomSubstitute : bomSubstitutes) {
    // MtBomComponentVO2 vo = new MtBomComponentVO2();
    // vo.setBomComponentId(mtBomComponent.getBomComponentId());
    // vo.setMaterialId(bomSubstitute.getMaterialId());
    //
    // BigDecimal componentQty =
    // new BigDecimal(dto.getQty().toString()).multiply(new BigDecimal(step4Qty.toString()))
    // .multiply(new BigDecimal(bomSubstitute.getComponentQty().toString()));
    //
    // MtMaterialVO mtMaterialVO =
    // this.mtMaterialRepository.materialPropertyGet(tenantId, bomSubstitute.getMaterialId());
    // if (mtMaterialVO != null) {
    // MtUomVO1 uomModel = new MtUomVO1();
    // uomModel.setSourceUomId(mtMaterialVO.getPrimaryUomId());
    // uomModel.setSourceValue(componentQty.doubleValue());
    // uomModel = this.mtUomRepository.uomDecimalProcess(tenantId, uomModel);
    // vo.setComponentQty(uomModel.getTargetValue());
    // } else {
    // vo.setComponentQty(componentQty.doubleValue());
    // }
    // result.add(vo);
    // }
    // }
    // return result;
    // }

    @Override
    public MtBomComponentVO3 bomComponentMaterialValidate(Long tenantId, String bomId, String materialId) {
        MtBomComponentVO3 result = new MtBomComponentVO3();
        result.setVerifyResult("");
        if (StringUtils.isEmpty(bomId)) {
            result.setMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0001", "BOM",
                            "bomId", "【API:bomComponentMaterialValidate】"));
            result.setVerifyResult("FALSE");
            return result;
        }
        if (StringUtils.isEmpty(materialId)) {
            result.setMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0001", "BOM",
                            "materialId", "【API:bomComponentMaterialValidate】"));
            result.setVerifyResult("FALSE");
            return result;
        }

        MtBom mtBom = new MtBom();
        mtBom.setTenantId(tenantId);
        mtBom.setBomId(bomId);
        mtBom = this.mtBomMapper.selectOne(mtBom);
        if (mtBom == null) {
            result.setMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0004", "BOM",
                            "【API:bomComponentMaterialValidate】"));
            result.setVerifyResult("FALSE");
            return result;
        }

        // 获取物料分配的站点siteId
        MtMaterialSite mtMaterialSite = new MtMaterialSite();
        mtMaterialSite.setMaterialId(materialId);
        List<String> materialSiteIds = mtMaterialSiteRepository.materialLimitSiteQuery(tenantId, mtMaterialSite);

        // 获取bom分配的有效siteId
        List<String> bomSiteIds = mtBomSiteAssignRepository.bomLimitEnableSiteQuery(tenantId, bomId);

        // 判断物料和bom分配的站点是否存在相同的
        boolean sameSiteFlag = false;
        for (int i = 0; i < materialSiteIds.size(); i++) {
            if (bomSiteIds.contains(materialSiteIds.get(i))) {
                sameSiteFlag = true;
                break;
            }
        }

        if (!sameSiteFlag) {
            result.setMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0022", "BOM",
                            "【API:bomComponentMaterialValidate】"));
            result.setVerifyResult("FALSE");
            return result;
        }

        List<MtBomComponent> bomComponents = this.mtBomComponentMapper.selectEnableByBomId(tenantId, bomId);
        if (CollectionUtils.isEmpty(bomComponents)) {
            result.setMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0023", "BOM",
                            "【API:bomComponentMaterialValidate】"));
            return result;
        }

        boolean materialFlag = bomComponents.stream().anyMatch(t -> t.getMaterialId().equals(materialId));
        if (materialFlag) {
            result.setMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0024", "BOM",
                            "【API:bomComponentMaterialValidate】"));
            result.setVerifyResult("COMPONENT");
            return result;
        }

        final List<String> substituteIds = new ArrayList<String>();
        for (MtBomComponent mtBomComponent : bomComponents) {
            List<MtBomSubstituteGroupVO3> tmpList = null;
            try {
                tmpList = this.mtBomSubstituteGroupRepository.bomSubstituteQuery(tenantId,
                                mtBomComponent.getBomComponentId());
                if (CollectionUtils.isNotEmpty(tmpList)) {
                    tmpList.stream().forEach(t -> substituteIds.add(t.getBomSubstituteId()));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (CollectionUtils.isEmpty(substituteIds)) {
            result.setMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0023", "BOM",
                            "【API:bomComponentMaterialValidate】"));
            return result;
        }

        List<MtBomSubstitute> bomSubstitutes = this.mtBomSubstituteMapper.selectByIdsCustom(tenantId, substituteIds);
        if (CollectionUtils.isEmpty(bomSubstitutes)) {
            result.setMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0023", "BOM",
                            "【API:bomComponentMaterialValidate】"));
            return result;
        }

        boolean substituteFlag = bomSubstitutes.stream().anyMatch(t -> t.getMaterialId().equals(materialId));
        if (substituteFlag) {
            result.setMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0024", "BOM",
                            "【API:bomComponentMaterialValidate】"));
            result.setVerifyResult("SUBSTITUTE");
            return result;
        } else {
            result.setMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0023", "BOM",
                            "【API:bomComponentMaterialValidate】"));
            result.setVerifyResult("FALSE");
            return result;
        }
    }

    @Override
    public List<MtBomComponentVO13> bomComponentBasicBatchGet(Long tenantId, List<String> bomComponentIds) {
        if (CollectionUtils.isEmpty(bomComponentIds)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomComponentId", "【API:bomComponentBasicBatchGet】"));
        }
        return this.mtBomComponentMapper.selectByIdsCustom(tenantId, bomComponentIds);
    }

    /**
     * bomComponentUpdate-新增更新装配清单组件行
     *
     * @param tenantId
     * @param dto
     * @return 处理的组件行所属装配清单ID bomId
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtBomComponentVO15 bomComponentUpdate(Long tenantId, MtBomComponentVO10 dto, String fullUpdate) {
        if (StringUtils.isEmpty(dto.getBomId())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomId", "【API:bomComponentUpdate】"));
        }

        if (CollectionUtils.isEmpty(dto.getBomComponents())) {
            throw new MtException("MT_BOM_0060", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0060", "BOM", "【API:bomComponentUpdate】"));
        }

        // 校验(更新时不能传空字符串)
        for (MtBomComponentVO9 c : dto.getBomComponents()) {
            if (StringUtils.isEmpty(c.getBomComponentId())) {
                if (null == c.getLineNumber()) {
                    throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_BOM_0001", "BOM", "lineNumber", "【API:bomComponentUpdate】"));
                }
                if (StringUtils.isEmpty(c.getMaterialId())) {
                    throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_BOM_0001", "BOM", "materialId", "【API:bomComponentUpdate】"));
                }
                if (StringUtils.isEmpty(c.getBomComponentType())) {
                    throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_BOM_0001", "BOM", "bomComponentType", "【API:bomComponentUpdate】"));
                }
                if (null == c.getDateFrom()) {
                    throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_BOM_0001", "BOM", "dateFrom", "【API:bomComponentUpdate】"));
                }
                if (null == c.getQty()) {
                    throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_BOM_0001", "BOM", "qty", "【API:bomComponentUpdate】"));
                } else {
                    if (!NumberHelper.isSixDecimal(c.getQty().toString())) {
                        throw new MtException("MT_BOM_0047", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_BOM_0047", "BOM", "【API:bomComponentUpdate】"));
                    }
                }
                if (StringUtils.isNotEmpty(c.getIssuedLocatorId())) {
                    MtModLocator mtModLocator =
                                    mtModLocatorRepository.locatorBasicPropertyGet(tenantId, c.getIssuedLocatorId());
                    if (null == mtModLocator || !"Y".equals(mtModLocator.getEnableFlag())) {
                        throw new MtException("MT_BOM_0054",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0054",
                                                        "BOM", c.getIssuedLocatorId(), "【API:bomComponentUpdate】"));
                    }
                }
            } else {
                if (null != c.getMaterialId() && "".equals(c.getMaterialId())) {
                    throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_BOM_0001", "BOM", "materialId", "【API:bomComponentUpdate】"));
                }
                if (null != c.getBomComponentType() && "".equals(c.getBomComponentType())) {
                    throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_BOM_0001", "BOM", "bomComponentType", "【API:bomComponentUpdate】"));
                }

                if (null != c.getQty() && !NumberHelper.isSixDecimal(c.getQty().toString())) {
                    throw new MtException("MT_BOM_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_BOM_0047", "BOM", "【API:bomComponentUpdate】"));
                }
                if (StringUtils.isNotEmpty(c.getIssuedLocatorId())) {
                    MtModLocator mtModLocator =
                                    mtModLocatorRepository.locatorBasicPropertyGet(tenantId, c.getIssuedLocatorId());
                    if (null == mtModLocator || !"Y".equals(mtModLocator.getEnableFlag())) {
                        throw new MtException("MT_BOM_0054",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0054",
                                                        "BOM", c.getIssuedLocatorId(), "【API:bomComponentUpdate】"));
                    }
                }
            }
        }

        // 获取基础的BOM信息
        MtBomVO7 oldBom = this.mtBomRepository.bomBasicGet(tenantId, dto.getBomId());
        if (null == oldBom) {
            throw new MtException("MT_BOM_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0004", "BOM", "【API:bomComponentUpdate】"));
        }

        String dealBomId = oldBom.getBomId();
        List<String> dealBomComponentIds = new ArrayList<String>();
        List<String> dealBomComponentHisIds = new ArrayList<String>();

        // 获取BOM是否为自动升版本模式
        String autoRevisionFlag = mtBomRepository.bomAutoRevisionGet(tenantId, dto.getBomId());

        // 获取是否存在新增行或者修改内容包含materialId/bomComponentType/assemble_method
        boolean changeFlag = false;
        List<MtBomComponentVO9> addList = dto.getBomComponents().stream()
                        .filter(t -> StringUtils.isEmpty(t.getBomComponentId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(addList)) {
            changeFlag = true;
        } else {
            List<String> allComponentId = new ArrayList<String>();
            for (MtBomComponentVO9 ever : dto.getBomComponents()) {
                allComponentId.add(ever.getBomComponentId());
            }

            List<MtBomComponentVO13> allComponent = bomComponentBasicBatchGet(tenantId, allComponentId);
            for (MtBomComponentVO9 ever : dto.getBomComponents()) {
                Optional<MtBomComponentVO13> bomComponentOp = allComponent.stream()
                                .filter(t -> ever.getBomComponentId().equals(t.getBomComponentId())).findFirst();
                if (bomComponentOp.isPresent()) {
                    String bcMaterialId = null == bomComponentOp.get().getMaterialId() ? ""
                                    : bomComponentOp.get().getMaterialId();
                    String bcType = null == bomComponentOp.get().getBomComponentType() ? ""
                                    : bomComponentOp.get().getBomComponentType();
                    String bcAssembleMethod = null == bomComponentOp.get().getAssembleMethod() ? ""
                                    : bomComponentOp.get().getAssembleMethod();

                    String materialId = null == ever.getMaterialId() ? bcMaterialId : ever.getMaterialId();
                    String bomComponentType = null == ever.getBomComponentType() ? bcType : ever.getBomComponentType();
                    String assembleMethod =
                                    null == ever.getAssembleMethod() ? bcAssembleMethod : ever.getAssembleMethod();

                    if (!bcMaterialId.equals(materialId) || !bcType.equals(bomComponentType)
                                    || !bcAssembleMethod.equals(assembleMethod)) {
                        changeFlag = true;
                        break;
                    }
                } else {
                    throw new MtException("MT_BOM_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_BOM_0015", "BOM", "【API:bomComponentUpdate】"));
                }
            }
        }

        // 调用API【bomCopy】复制为新的装配清单
        if ("Y".equals(autoRevisionFlag) && changeFlag) {
            MtBomVO4 bomVo4 = new MtBomVO4();
            bomVo4.setBomId(oldBom.getBomId());
            bomVo4.setRevision(oldBom.getRevision());
            String revision = this.mtBomRepository.bomRevisionGenerate(tenantId, bomVo4);

            // 复制 Bom 屬性赋值
            MtBomVO2 bomVo2 = new MtBomVO2();
            bomVo2.setBomId(oldBom.getBomId());
            bomVo2.setBomName(oldBom.getBomName());
            bomVo2.setRevision(revision);
            bomVo2.setBomType(oldBom.getBomType());
            dealBomId = this.mtBomRepository.bomCopy(tenantId, bomVo2);
        }

        for (MtBomComponentVO9 mtBomComponent : dto.getBomComponents()) {
            if (StringUtils.isEmpty(mtBomComponent.getBomComponentId())) {
                MtBomComponent bomComponent = new MtBomComponent();
                BeanUtils.copyProperties(mtBomComponent, bomComponent);
                bomComponent.setBomId(dealBomId);
                bomComponent.setTenantId(tenantId);

                MtBomComponent queryBomComponent = new MtBomComponent();
                queryBomComponent.setTenantId(tenantId);
                queryBomComponent.setBomId(bomComponent.getBomId());
                queryBomComponent.setMaterialId(bomComponent.getMaterialId());
                queryBomComponent.setLineNumber(bomComponent.getLineNumber());
                queryBomComponent.setBomComponentType(bomComponent.getBomComponentType());
                if (null != mtBomComponentMapper.selectOne(queryBomComponent)) {
                    throw new MtException("MT_BOM_0063",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0063", "BOM",
                                                    "MT_BOM_COMPONENT",
                                                    "BOM_ID, MATERIAL_ID, LINE_NUMBER, BOM_COMPONENT_TYPE",
                                                    "【API:bomComponentUpdate】"));
                }
                self().insertSelective(bomComponent);

                dealBomComponentIds.add(bomComponent.getBomComponentId());

                if (!("Y".equals(autoRevisionFlag) && changeFlag)) {
                    MtBomHisVO1 mtBomHisVo1 = new MtBomHisVO1();
                    mtBomHisVo1.setBomId(dealBomId);
                    mtBomHisVo1.setEventTypeCode("BOM_COMPONENT_CREATE");
                    // dealBomComponentHisIds.addAll(
                    // mtBomHisRepository.bomAllHisCreate(tenantId, mtBomHisVo1).getBomComponentHisId());

                    // 返回历史id后再更新主表的latesthisid add by peng.yuan 2019-11-28
                    // List<String> bomComponentHisId =
                    // mtBomHisRepository.bomAllHisCreate(tenantId, mtBomHisVo1).getBomComponentHisId();
                    // if (CollectionUtils.isNotEmpty(bomComponentHisId)) {
                    // bomComponentHisId.sort(Comparator.comparingDouble(
                    // (String t) -> Double.valueOf(StringUtils.isEmpty(t) ? "0" : t)));
                    // bomComponent.setLatestHisId(bomComponentHisId.get(bomComponentHisId.size() - 1));
                    // }
                    self().updateByPrimaryKeySelective(bomComponent);
                }
            } else {
                MtBomComponent oldBomComponent = bomComponentBasicGet(tenantId, mtBomComponent.getBomComponentId());
                if (null == oldBomComponent) {
                    throw new MtException("MT_BOM_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_BOM_0015", "BOM", "【API:bomComponentUpdate】"));
                }
                if (!oldBomComponent.getBomId().equals(dto.getBomId())) {
                    throw new MtException("MT_BOM_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_BOM_0046", "BOM", "【API:bomComponentUpdate】"));
                }

                if ("Y".equals(autoRevisionFlag) && changeFlag) {
                    MtBomComponent newBomComponent = new MtBomComponent();
                    newBomComponent.setTenantId(tenantId);
                    newBomComponent.setBomId(dealBomId);
                    newBomComponent.setCopiedFromComponentId(mtBomComponent.getBomComponentId());
                    newBomComponent = this.mtBomComponentMapper.selectOne(newBomComponent);

                    newBomComponent.setTenantId(tenantId);
                    if (mtBomComponent.getLineNumber() != null) {
                        newBomComponent.setLineNumber(mtBomComponent.getLineNumber());
                    }
                    if (StringUtils.isNotEmpty(mtBomComponent.getMaterialId())) {
                        newBomComponent.setMaterialId(mtBomComponent.getMaterialId());
                    }
                    if (StringUtils.isNotEmpty(mtBomComponent.getBomComponentType())) {
                        newBomComponent.setBomComponentType(mtBomComponent.getBomComponentType());
                    }
                    if (mtBomComponent.getDateFrom() != null) {
                        newBomComponent.setDateFrom(mtBomComponent.getDateFrom());
                    }
                    if (mtBomComponent.getDateTo() != null) {
                        newBomComponent.setDateTo(mtBomComponent.getDateTo());
                    }
                    if (mtBomComponent.getQty() != null) {
                        newBomComponent.setQty(mtBomComponent.getQty());
                    }
                    if (StringUtils.isNotEmpty(mtBomComponent.getKeyMaterialFlag())) {
                        newBomComponent.setKeyMaterialFlag(mtBomComponent.getKeyMaterialFlag());
                    }
                    if (StringUtils.isNotEmpty(mtBomComponent.getAssembleAsReqFlag())) {
                        newBomComponent.setAssembleAsReqFlag(mtBomComponent.getAssembleAsReqFlag());
                    }
                    if (StringUtils.isNotEmpty(mtBomComponent.getAssembleMethod())) {
                        newBomComponent.setAssembleMethod(mtBomComponent.getAssembleMethod());
                    }
                    if (StringUtils.isNotEmpty(mtBomComponent.getAttritionPolicy())) {
                        newBomComponent.setAttritionPolicy(mtBomComponent.getAttritionPolicy());
                    }
                    if (mtBomComponent.getAttritionChance() != null) {
                        newBomComponent.setAttritionChance(mtBomComponent.getAttritionChance());
                    }
                    if (mtBomComponent.getAttritionQty() != null) {
                        newBomComponent.setAttritionQty(mtBomComponent.getAttritionQty());
                    }
                    if (mtBomComponent.getIssuedLocatorId() != null) {
                        newBomComponent.setIssuedLocatorId(mtBomComponent.getIssuedLocatorId());
                    }
                    if ("Y".equals(fullUpdate)) {
                        newBomComponent = (MtBomComponent) ObjectFieldsHelper.setStringFieldsEmpty(newBomComponent);
                        self().updateByPrimaryKey(newBomComponent);
                    } else {
                        self().updateByPrimaryKeySelective(newBomComponent);
                    }

                    dealBomComponentIds.add(newBomComponent.getBomComponentId());
                } else {
                    oldBomComponent.setTenantId(tenantId);
                    if (mtBomComponent.getLineNumber() != null) {
                        oldBomComponent.setLineNumber(mtBomComponent.getLineNumber());
                    }
                    if (StringUtils.isNotEmpty(mtBomComponent.getMaterialId())) {
                        oldBomComponent.setMaterialId(mtBomComponent.getMaterialId());
                    }
                    if (StringUtils.isNotEmpty(mtBomComponent.getBomComponentType())) {
                        oldBomComponent.setBomComponentType(mtBomComponent.getBomComponentType());
                    }
                    if (mtBomComponent.getDateFrom() != null) {
                        oldBomComponent.setDateFrom(mtBomComponent.getDateFrom());
                    }
                    if (mtBomComponent.getDateTo() != null) {
                        oldBomComponent.setDateTo(mtBomComponent.getDateTo());
                    }
                    if (mtBomComponent.getQty() != null) {
                        oldBomComponent.setQty(mtBomComponent.getQty());
                    }
                    if (StringUtils.isNotEmpty(mtBomComponent.getKeyMaterialFlag())) {
                        oldBomComponent.setKeyMaterialFlag(mtBomComponent.getKeyMaterialFlag());
                    }
                    if (StringUtils.isNotEmpty(mtBomComponent.getAssembleAsReqFlag())) {
                        oldBomComponent.setAssembleAsReqFlag(mtBomComponent.getAssembleAsReqFlag());
                    }
                    if (StringUtils.isNotEmpty(mtBomComponent.getAssembleMethod())) {
                        oldBomComponent.setAssembleMethod(mtBomComponent.getAssembleMethod());
                    }
                    if (StringUtils.isNotEmpty(mtBomComponent.getAttritionPolicy())) {
                        oldBomComponent.setAttritionPolicy(mtBomComponent.getAttritionPolicy());
                    }
                    if (mtBomComponent.getAttritionChance() != null) {
                        oldBomComponent.setAttritionChance(mtBomComponent.getAttritionChance());
                    }
                    if (mtBomComponent.getAttritionQty() != null) {
                        oldBomComponent.setAttritionQty(mtBomComponent.getAttritionQty());
                    }
                    if (mtBomComponent.getIssuedLocatorId() != null) {
                        oldBomComponent.setIssuedLocatorId(mtBomComponent.getIssuedLocatorId());
                    }

                    MtBomHisVO1 mtBomHisVo1 = new MtBomHisVO1();
                    mtBomHisVo1.setBomId(dealBomId);
                    mtBomHisVo1.setEventTypeCode("BOM_COMPONENT_UPDATE");
                    // dealBomComponentHisIds.addAll(
                    // mtBomHisRepository.bomAllHisCreate(tenantId, mtBomHisVo1).getBomComponentHisId());

                    // 返回历史id后再更新主表的latesthisid add by peng.yuan 2019-11-28
                    // List<String> bomComponentHisId =
                    // mtBomHisRepository.bomAllHisCreate(tenantId, mtBomHisVo1).getBomComponentHisId();
                    // if (CollectionUtils.isNotEmpty(bomComponentHisId)) {
                    // bomComponentHisId.sort(Comparator.comparingDouble(
                    // (String t) -> Double.valueOf(StringUtils.isEmpty(t) ? "0" : t)));
                    // oldBomComponent.setLatestHisId(bomComponentHisId.get(bomComponentHisId.size() - 1));
                    // }


                    if ("Y".equals(fullUpdate)) {
                        oldBomComponent = (MtBomComponent) ObjectFieldsHelper.setStringFieldsEmpty(oldBomComponent);
                        self().updateByPrimaryKey(oldBomComponent);
                    } else {
                        self().updateByPrimaryKeySelective(oldBomComponent);
                    }
                    dealBomComponentIds.add(oldBomComponent.getBomComponentId());
                }
            }
        }

        MtBomComponentVO15 result = new MtBomComponentVO15();
        result.setBomId(dealBomId);
        result.setBomComponentId(dealBomComponentIds);
        // result.setBomComponentHisId(dealBomComponentHisIds);

        return result;
    }

    /**
     * 根据来源Bom的组件更新目标Bom的组件
     *
     * @author chuang.yang
     * @date 2019/4/27
     * @param tenantId
     * @param sourceBomComponents
     * @param targetBomComponents
     * @param targetBomId
     * @param now
     * @return java.util.List<java.lang.String>
     */
    @Override
    public List<String> sourceLimitTargetBomComponentUpdateSqlGet(Long tenantId,
                    List<MtBomComponent> sourceBomComponents, List<MtBomComponent> targetBomComponents,
                    String targetBomId, Date now) {
        List<String> sqlList = new ArrayList<>();
        if (CollectionUtils.isEmpty(sourceBomComponents) && CollectionUtils.isEmpty(targetBomComponents)) {
            return sqlList;
        }

        Long userId = DetailsHelper.getUserDetails().getUserId();

        // 来源有，目标全无，则以来源数据新增
        if (CollectionUtils.isEmpty(targetBomComponents)) {
            for (MtBomComponent s : sourceBomComponents) {
                MtBomComponent newBomComponent = new MtBomComponent();
                newBomComponent.setTenantId(tenantId);
                newBomComponent.setBomComponentId(this.customSequence.getNextKey("mt_bom_component_s"));
                newBomComponent.setBomId(targetBomId);
                newBomComponent.setLineNumber(s.getLineNumber());
                newBomComponent.setMaterialId(s.getMaterialId());
                newBomComponent.setBomComponentType(s.getBomComponentType());
                newBomComponent.setDateFrom(s.getDateFrom());
                newBomComponent.setDateTo(s.getDateTo());
                newBomComponent.setQty(s.getQty());
                newBomComponent.setKeyMaterialFlag(s.getKeyMaterialFlag());
                newBomComponent.setAssembleMethod(s.getAssembleMethod());
                newBomComponent.setAssembleAsReqFlag(s.getAssembleAsReqFlag());
                newBomComponent.setAttritionPolicy(s.getAttritionPolicy());
                newBomComponent.setAttritionChance(s.getAttritionChance());
                newBomComponent.setAttritionQty(s.getAttritionQty());
                newBomComponent.setCopiedFromComponentId(s.getBomComponentId());
                newBomComponent.setIssuedLocatorId(s.getIssuedLocatorId());
                newBomComponent.setCreationDate(now);
                newBomComponent.setCreatedBy(userId);
                newBomComponent.setLastUpdateDate(now);
                newBomComponent.setLastUpdatedBy(userId);
                newBomComponent.setObjectVersionNumber(1L);
                newBomComponent.setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_component_cid_s")));
                sqlList.addAll(MtSqlHelper.getInsertSql(newBomComponent));

                // 处理组件对应参考点信息
                List<String> bomReferencePointSqlList =
                                mtBomReferencePointRepository.sourceLimitTargetBomReferencePointUpdateGet(tenantId,
                                                s.getBomComponentId(), newBomComponent.getBomComponentId(), now);
                sqlList.addAll(bomReferencePointSqlList);

                // 处理组件对应替代组信息
                List<String> bomSubstituteGroupSqlList =
                                mtBomSubstituteGroupRepository.sourceLimitTargetBomSubstituteGroupUpdateGet(tenantId,
                                                s.getBomComponentId(), newBomComponent.getBomComponentId(), now);
                sqlList.addAll(bomSubstituteGroupSqlList);
            }
        } else if (CollectionUtils.isEmpty(sourceBomComponents)) {
            // 目标有，来源全无，则目标全部失效
            for (MtBomComponent t : targetBomComponents) {
                t.setTenantId(tenantId);
                t.setDateTo(now);
                t.setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_component_cid_s")));
                t.setLastUpdatedBy(userId);
                t.setLastUpdateDate(now);
                sqlList.addAll(MtSqlHelper.getUpdateSql(t));
            }
        } else {
            // 目标有，来源也有，则匹配 MATERIAL_ID + BOM_COMPONENT_TYPE 相同的数据
            Map<String, MtBomComponent> sourceBomComponentMap =
                            sourceBomComponents.stream().collect(Collectors.toMap(m -> m.getBomComponentId(), m -> m));

            for (MtBomComponent t : targetBomComponents) {
                // 对每一个目标组件筛选来源组件
                List<MtBomComponent> result = sourceBomComponents.stream()
                                .filter(s -> s.getMaterialId().equals(t.getMaterialId())
                                                && s.getBomComponentType().equals(t.getBomComponentType()))
                                .collect(Collectors.toList());

                // 如果无对应来源组件，则无效改目标组件
                if (CollectionUtils.isEmpty(result)) {
                    t.setTenantId(tenantId);
                    t.setDateTo(now);
                    t.setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_component_cid_s")));
                    t.setLastUpdatedBy(userId);
                    t.setLastUpdateDate(now);
                    sqlList.addAll(MtSqlHelper.getUpdateSql(t));
                } else {
                    MtBomComponent tempSource = result.get(0);

                    // 剔除来源组件Map数据: 筛选结果唯一
                    sourceBomComponentMap.remove(tempSource.getBomComponentId());

                    // 根据筛选出来的来源组件更新该目标组件
                    t.setTenantId(tenantId);
                    t.setLineNumber(tempSource.getLineNumber());
                    t.setDateFrom(tempSource.getDateFrom());
                    t.setDateTo(tempSource.getDateTo());
                    t.setQty(tempSource.getQty());
                    t.setKeyMaterialFlag(tempSource.getKeyMaterialFlag());
                    t.setAssembleMethod(tempSource.getAssembleMethod());
                    t.setAssembleAsReqFlag(tempSource.getAssembleAsReqFlag());
                    t.setAttritionPolicy(tempSource.getAttritionPolicy());
                    t.setAttritionChance(tempSource.getAttritionChance());
                    t.setAttritionQty(tempSource.getAttritionQty());
                    t.setCopiedFromComponentId(tempSource.getBomComponentId());
                    t.setIssuedLocatorId(tempSource.getIssuedLocatorId());
                    t.setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_component_cid_s")));
                    t.setLastUpdatedBy(userId);
                    t.setLastUpdateDate(now);
                    sqlList.addAll(MtSqlHelper.getUpdateSql(t));

                    // 处理组件对应参考点信息
                    List<String> bomReferencePointSqlList =
                                    mtBomReferencePointRepository.sourceLimitTargetBomReferencePointUpdateGet(tenantId,
                                                    tempSource.getBomComponentId(), t.getBomComponentId(), now);
                    sqlList.addAll(bomReferencePointSqlList);

                    // 处理组件对应替代组信息
                    List<String> bomSubstituteGroupSqlList = mtBomSubstituteGroupRepository
                                    .sourceLimitTargetBomSubstituteGroupUpdateGet(tenantId,
                                                    tempSource.getBomComponentId(), t.getBomComponentId(), now);
                    sqlList.addAll(bomSubstituteGroupSqlList);
                }
            }

            // 如果处理完所有目标组件后，还有来源组件未筛选到，则对这部分来源组件执行目标新增
            if (!MapUtils.isEmpty(sourceBomComponentMap)) {
                for (Map.Entry<String, MtBomComponent> entry : sourceBomComponentMap.entrySet()) {
                    MtBomComponent sourceBomComponent = entry.getValue();
                    MtBomComponent newBomComponent = new MtBomComponent();
                    newBomComponent.setTenantId(tenantId);
                    newBomComponent.setBomComponentId(this.customSequence.getNextKey("mt_bom_component_s"));
                    newBomComponent.setBomId(targetBomId);
                    newBomComponent.setLineNumber(sourceBomComponent.getLineNumber());
                    newBomComponent.setMaterialId(sourceBomComponent.getMaterialId());
                    newBomComponent.setBomComponentType(sourceBomComponent.getBomComponentType());
                    newBomComponent.setDateFrom(sourceBomComponent.getDateFrom());
                    newBomComponent.setDateTo(sourceBomComponent.getDateTo());
                    newBomComponent.setQty(sourceBomComponent.getQty());
                    newBomComponent.setKeyMaterialFlag(sourceBomComponent.getKeyMaterialFlag());
                    newBomComponent.setAssembleMethod(sourceBomComponent.getAssembleMethod());
                    newBomComponent.setAssembleAsReqFlag(sourceBomComponent.getAssembleAsReqFlag());
                    newBomComponent.setAttritionPolicy(sourceBomComponent.getAttritionPolicy());
                    newBomComponent.setAttritionChance(sourceBomComponent.getAttritionChance());
                    newBomComponent.setAttritionQty(sourceBomComponent.getAttritionQty());
                    newBomComponent.setCopiedFromComponentId(sourceBomComponent.getBomComponentId());
                    newBomComponent.setIssuedLocatorId(sourceBomComponent.getIssuedLocatorId());
                    newBomComponent.setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_component_cid_s")));
                    newBomComponent.setCreationDate(now);
                    newBomComponent.setCreatedBy(userId);
                    newBomComponent.setLastUpdateDate(now);
                    newBomComponent.setLastUpdatedBy(userId);
                    newBomComponent.setObjectVersionNumber(1L);
                    sqlList.addAll(MtSqlHelper.getInsertSql(newBomComponent));
                }
            }
        }
        return sqlList;
    }

    /**
     * bomComponentDetailInsert 根据传入组件以及组件属性
     * 返回组件及属性（Locator、referencePoint、SubstituteGroup）的insertSqlList
     *
     * @param tenantId
     * @param newBomId
     * @param dtos
     * @param now
     * @return java.util.List<java.lang.String>
     * @author chuang.yang
     * @date 2019/3/16
     */
    @Override
    public List<String> bomComponentDetailInsert(Long tenantId, String newBomId, List<MtBomComponentVO7> dtos,
                    Date now) {
        Long userId = DetailsHelper.getUserDetails() == null ? -1 : DetailsHelper.getUserDetails().getUserId();
        List<String> insertSqlList = new ArrayList<>();

        for (MtBomComponentVO7 bomComponent : dtos) {
            // 2.2. 新增 BomComponent 以及 属性信息
            String newBomComponentId = customSequence.getNextKey("mt_bom_component_s");
            MtBomComponent newBomComponent = new MtBomComponent();
            newBomComponent.setTenantId(tenantId);
            newBomComponent.setBomComponentId(newBomComponentId);
            newBomComponent.setBomId(newBomId);
            newBomComponent.setLineNumber(bomComponent.getLineNumber());
            newBomComponent.setMaterialId(bomComponent.getMaterialId());
            newBomComponent.setBomComponentType(bomComponent.getBomComponentType());
            newBomComponent.setDateFrom(bomComponent.getDateFrom());
            newBomComponent.setDateTo(bomComponent.getDateTo());
            newBomComponent.setQty(bomComponent.getQty());
            newBomComponent.setKeyMaterialFlag(bomComponent.getKeyMaterialFlag());
            newBomComponent.setAssembleMethod(bomComponent.getAssembleMethod());
            newBomComponent.setAssembleAsReqFlag(bomComponent.getAssembleAsReqFlag());
            newBomComponent.setAttritionPolicy(bomComponent.getAttritionPolicy());
            newBomComponent.setAttritionChance(bomComponent.getAttritionChance());
            newBomComponent.setAttritionQty(bomComponent.getAttritionQty());
            newBomComponent.setIssuedLocatorId(bomComponent.getIssuedLocatorId());
            newBomComponent.setCreatedBy(userId);
            newBomComponent.setCreationDate(now);
            newBomComponent.setLastUpdatedBy(userId);
            newBomComponent.setLastUpdateDate(now);
            newBomComponent.setObjectVersionNumber(1L);
            newBomComponent.setCid(Long.valueOf(customSequence.getNextKey("mt_bom_component_cid_s")));
            insertSqlList.addAll(MtSqlHelper.getInsertSql(newBomComponent));

            // 生成 bomReferencePoint
            if (CollectionUtils.isNotEmpty(bomComponent.getMtBomReferencePointList())) {
                for (MtBomReferencePointVO9 bomReferencePoint : bomComponent.getMtBomReferencePointList()) {
                    MtBomReferencePoint newBomReferencePoint = new MtBomReferencePoint();
                    newBomReferencePoint.setTenantId(tenantId);
                    newBomReferencePoint.setBomReferencePointId(customSequence.getNextKey("mt_bom_reference_point_s"));
                    newBomReferencePoint.setBomComponentId(newBomComponentId);
                    newBomReferencePoint.setReferencePoint(bomReferencePoint.getReferencePoint());
                    newBomReferencePoint.setQty(bomReferencePoint.getQty());
                    newBomReferencePoint.setLineNumber(bomReferencePoint.getLineNumber());
                    newBomReferencePoint.setEnableFlag(bomReferencePoint.getEnableFlag());
                    newBomReferencePoint.setCreatedBy(userId);
                    newBomReferencePoint.setCreationDate(now);
                    newBomReferencePoint.setLastUpdatedBy(userId);
                    newBomReferencePoint.setLastUpdateDate(now);
                    newBomReferencePoint.setObjectVersionNumber(1L);
                    newBomReferencePoint
                                    .setCid(Long.valueOf(customSequence.getNextKey("mt_bom_reference_point_cid_s")));
                    insertSqlList.addAll(MtSqlHelper.getInsertSql(newBomReferencePoint));
                }
            }

            // 生成 bomSubstituteGroup 及其 属性
            if (CollectionUtils.isNotEmpty(bomComponent.getMtBomSubstituteGroupList())) {
                for (MtBomSubstituteGroupVO4 bomSubstituteGroup : bomComponent.getMtBomSubstituteGroupList()) {
                    String newBomSubstituteGroupId = customSequence.getNextKey("mt_bom_substitute_group_s");
                    MtBomSubstituteGroup newBomSubstituteGroup = new MtBomSubstituteGroup();
                    newBomSubstituteGroup.setTenantId(tenantId);
                    newBomSubstituteGroup.setBomSubstituteGroupId(newBomSubstituteGroupId);
                    newBomSubstituteGroup.setBomComponentId(newBomComponentId);
                    newBomSubstituteGroup.setSubstituteGroup(bomSubstituteGroup.getSubstituteGroup());
                    newBomSubstituteGroup.setSubstitutePolicy(bomSubstituteGroup.getSubstitutePolicy());
                    newBomSubstituteGroup.setEnableFlag(bomSubstituteGroup.getEnableFlag());
                    newBomSubstituteGroup.setCreatedBy(userId);
                    newBomSubstituteGroup.setCreationDate(now);
                    newBomSubstituteGroup.setLastUpdatedBy(userId);
                    newBomSubstituteGroup.setLastUpdateDate(now);
                    newBomSubstituteGroup.setObjectVersionNumber(1L);
                    newBomSubstituteGroup
                                    .setCid(Long.valueOf(customSequence.getNextKey("mt_bom_substitute_group_cid_s")));
                    insertSqlList.addAll(MtSqlHelper.getInsertSql(newBomSubstituteGroup));

                    // 生成 bomSubstitute
                    if (CollectionUtils.isNotEmpty(bomSubstituteGroup.getMtBomSubstituteList())) {
                        for (MtBomSubstituteVO8 bomSubstitute : bomSubstituteGroup.getMtBomSubstituteList()) {
                            MtBomSubstitute newBomSubstitute = new MtBomSubstitute();
                            newBomSubstitute.setTenantId(tenantId);
                            newBomSubstitute.setBomSubstituteId(customSequence.getNextKey("mt_bom_substitute_s"));
                            newBomSubstitute.setBomSubstituteGroupId(newBomSubstituteGroupId);
                            newBomSubstitute.setMaterialId(bomSubstitute.getMaterialId());
                            newBomSubstitute.setDateFrom(bomSubstitute.getDateFrom());
                            newBomSubstitute.setDateTo(bomSubstitute.getDateTo());
                            newBomSubstitute.setSubstituteValue(bomSubstitute.getSubstituteValue());
                            newBomSubstitute.setSubstituteUsage(bomSubstitute.getSubstituteUsage());
                            newBomSubstitute.setCreatedBy(userId);
                            newBomSubstitute.setCreationDate(now);
                            newBomSubstitute.setLastUpdatedBy(userId);
                            newBomSubstitute.setLastUpdateDate(now);
                            newBomSubstitute.setObjectVersionNumber(1L);
                            newBomSubstitute.setCid(Long.valueOf(customSequence.getNextKey("mt_bom_substitute_cid_s")));
                            insertSqlList.addAll(MtSqlHelper.getInsertSql(newBomSubstitute));
                        }
                    }
                }
            }
        }

        return insertSqlList;
    }

    /**
     * 计算单个装配清单组件行用量/sen.luo 2018-04-01
     *
     * @param tenantId
     * @param dto
     * @return
     */
    @Override
    public List<MtBomComponentVO12> singleBomComponentQtyCalculate(Long tenantId, MtBomComponentVO11 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getBomComponentId())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomComponentId", "【API:singleBomComponentQtyCalculate】"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "qty", "【API:singleBomComponentQtyCalculate】"));
        }
        // BOM_COMPONENT_ID在MT_BOM_COMPONENT表获取数据
        MtBomComponent mtBomComponent = new MtBomComponent();
        mtBomComponent.setTenantId(tenantId);
        mtBomComponent.setBomComponentId(dto.getBomComponentId());
        mtBomComponent = mtBomComponentMapper.selectOne(mtBomComponent);
        if (mtBomComponent == null) {
            throw new MtException("MT_BOM_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0015", "BOM", "【API:singleBomComponentQtyCalculate】"));
        }
        MtBomComponentVO8 v8 = bomComponentBasicGet(tenantId, dto.getBomComponentId());
        if (v8 == null) {
            throw new MtException("MT_BOM_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0015", "BOM", "【API:singleBomComponentQtyCalculate】"));
        }

        MtBomVO7 bom = this.mtBomRepository.bomBasicGet(tenantId, v8.getBomId());
        if (null == bom) {
            throw new MtException("MT_BOM_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0004", "BOM", "【API:singleBomComponentQtyCalculate】"));
        }

        List<MtBomComponentVO12> resultList = new ArrayList<>();
        // 新增逻辑（如substituteFlag传入为Y则直接调用，如果不为Y，按N传入)
        MtBomSubstituteVO6 bomSubstituteVO6 = new MtBomSubstituteVO6();
        bomSubstituteVO6.setBomComponentId(dto.getBomComponentId());
        bomSubstituteVO6.setQty(Double.valueOf(1.0D));

        // 新增逻辑（如substituteFlag传入为Y则直接调用，如果不为Y，按N传入)
        if (!"Y".equals(dto.getSubstituteFlag())) {
            bomSubstituteVO6.setSubstituteFlag("N");
        } else {
            bomSubstituteVO6.setSubstituteFlag(dto.getSubstituteFlag());
        }

        List<MtBomSubstituteVO3> bomSubstitutes =
                        this.mtBomSubstituteRepository.bomSubstituteQtyCalculate(tenantId, bomSubstituteVO6);

        for (MtBomSubstituteVO3 v3 : bomSubstitutes) {
            BigDecimal qty = BigDecimal.valueOf(dto.getQty())
                            .divide(BigDecimal.valueOf(bom.getPrimaryQty()), 10, BigDecimal.ROUND_HALF_DOWN)
                            .multiply(BigDecimal.valueOf(v3.getComponentQty()));

            MtMaterialVO1 mtMaterialVO1 = mtMaterialRepository.materialUomGet(tenantId, v3.getMaterialId());
            if (mtMaterialVO1 == null) {
                throw new MtException("MT_BOM_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0059", "BOM", "【API:singleBomComponentQtyCalculate】"));
            }

            MtUomVO1 transferUomVO = new MtUomVO1();
            transferUomVO.setSourceValue(qty.doubleValue());
            transferUomVO.setSourceUomId(mtMaterialVO1.getPrimaryUomId());

            MtUomVO1 uomModel = mtUomRepository.uomDecimalProcess(tenantId, transferUomVO);

            MtBomComponentVO12 bomComponentVO12 = new MtBomComponentVO12();
            bomComponentVO12.setMaterialId(v3.getMaterialId());
            if (uomModel != null) {
                bomComponentVO12.setComponentQty(uomModel.getTargetValue());
            }

            resultList.add(bomComponentVO12);
        }
        return resultList;
    }

    @Override
    public void bomComponentAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {

    }

    @Override
    public List<MtBomComponent> selectBomComponentByBomIds(Long tenantId, List<String> bomIds) {
        return null;
    }

    @Override
    public List<MtBomComponentVO17> bomComponentLimitOperationBatchGet(Long tenantId, List<String> bomComponentIds) {
        return null;
    }

    @Override
    public List<MtBomComponent> selectBomComponentByBomIdsAll(Long tenantId, List<String> bomIds) {
        return null;
    }

    @Override
    public List<MtBomComponent> selectBomComponentByBomComponentIds(Long tenantId, List<String> bomComponentIds) {
        if (CollectionUtils.isEmpty(bomComponentIds)) {
            return Collections.emptyList();
        }
        SecurityTokenHelper.close();
        return mtBomComponentMapper.selectByCondition(Condition.builder(MtBomComponent.class)
                        .andWhere(Sqls.custom().andEqualTo(MtBomComponent.FIELD_TENANT_ID, tenantId))
                        .andWhere(Sqls.custom().andIn(MtBomComponent.FIELD_BOM_COMPONENT_ID, bomComponentIds)).build());
    }

    @Override
    public List<MtBomComponentVO25> bomComponentQtyBatchCalculate(Long tenantId, MtBomComponentVO22 dto) {
        final String apiName = "【API:bomComponentQtyBatchCalculate】";
        if (CollectionUtils.isEmpty(dto.getBomMessages())
                        || dto.getBomMessages().stream().anyMatch(c -> MtIdHelper.isIdNull(c.getBomId()))) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomId", apiName));
        }
        if (dto.getBomMessages().stream().anyMatch(c -> c.getQty() == null)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "qty", apiName));
        }
        if (StringUtils.isNotEmpty(dto.getAssembleMethod())) {
            MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
            mtGenTypeVO2.setModule("BOM");
            mtGenTypeVO2.setTypeGroup(MtBaseConstants.GEN_TYPE_GROUP.ASSY_METHOD);
            List<MtGenType> assembleMethodMap = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
            if (CollectionUtils.isEmpty(assembleMethodMap) || assembleMethodMap.stream()
                            .noneMatch(t -> t.getTypeCode().equals(dto.getAssembleMethod()))) {
                throw new MtException("MT_BOM_0106", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "BOM",
                                "MT_BOM_0106", "assembleMethod", "ASSY_METHOD", apiName));
            }
        }

        Map<String, MtModSite> siteMap = new HashMap<>();
        if (MtBaseConstants.YES.equalsIgnoreCase(dto.getIsPhantomUnfold())) {
            for (MtBomComponentVO21 vo : dto.getBomMessages()) {
                if (MtIdHelper.isIdNull(vo.getSiteId())) {
                    throw new MtException("MT_BOM_0109",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0109", "BOM",
                                                    vo.getBomId().toString(), "siteId",
                                                    "【API:bomComponentQtyBatchCalculate】"));
                }
            }

            List<String> siteIds = dto.getBomMessages().stream().map(MtBomComponentVO21::getSiteId).distinct()
                            .collect(Collectors.toList());
            List<MtModSite> sites = mtModSiteRepository.siteBasicPropertyBatchGet(tenantId, siteIds);
            if (CollectionUtils.isEmpty(sites)) {
                throw new MtException("MT_BOM_0078", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0078", "BOM", siteIds.toString(), "【API:bomComponentQtyBatchCalculate】"));
            }
            for (MtModSite site : sites) {
                if (!siteIds.contains(site.getSiteId()) || !MtBaseConstants.YES.equals(site.getEnableFlag())) {
                    throw new MtException("MT_BOM_0078",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0078", "BOM",
                                                    site.getSiteId().toString(),
                                                    "【API:bomComponentQtyBatchCalculate】"));
                }
            }

            List<MtBomSiteAssignVO2> mtBomSiteAssigns =
                            mtBomSiteAssignRepository.bomListLimitSiteQuery(
                                            tenantId, dto.getBomMessages().stream().map(MtBomComponentVO21::getBomId)
                                                            .distinct().collect(Collectors.toList()),
                                            MtBaseConstants.YES);
            Map<String, List<String>> assignSiteMap =
                            mtBomSiteAssigns.stream().collect(Collectors.groupingBy(MtBomSiteAssignVO2::getBomId,
                                            Collectors.mapping(MtBomSiteAssignVO2::getSiteId, Collectors.toList())));
            for (MtBomComponentVO21 bomMessage : dto.getBomMessages()) {
                if (!assignSiteMap.get(bomMessage.getBomId()).contains(bomMessage.getSiteId())) {
                    throw new MtException("MT_BOM_0075",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0075", "BOM",
                                                    bomMessage.getSiteId(), bomMessage.getBomId(),
                                                    "【API:bomComponentQtyBatchCalculate】"));
                }
            }
            siteMap = sites.stream().collect(Collectors.toMap(MtModSite::getSiteId, c -> c));
        }

        return bomComponentQtyBatchCalculateRecursive(tenantId, dto.getAttritionFlag(), dto.getSubstituteFlag(),
                        dto.getKeyComponentFlag(), dto.getIsPhantomUnfold(), dto.getBomMessages(), dto.getMaterialIds(),
                        dto.getBomComponentIds(), dto.getAssembleMethod(), new Date(),
                        Collections.synchronizedList(new ArrayList<>()), siteMap);
    }

    private List<MtBomComponentVO25> bomComponentQtyBatchCalculateRecursive(Long tenantId, String attritionFlag,
                    String substituteFlag, String keyComponentFlag, String isPhantomUnfold,
                    List<MtBomComponentVO21> bomMessages, List<String> pMaterialIds, List<String> pBomComponentIds,
                    String assembleMethod, Date now, List<MtBomComponentVO25> result, Map<String, MtModSite> siteMap) {
        if (CollectionUtils.isEmpty(bomMessages)) {
            return result;
        }

        // 2.获取装配清单
        List<MtBom> originBoms = validateBom(tenantId, bomMessages);
        List<String> bomIds = originBoms.stream().map(MtBom::getBomId).distinct().collect(Collectors.toList());

        // 3.获取有效的bom组件
        SecurityTokenHelper.close();
        List<MtBomComponent> allEnableBomComponents = this.mtBomComponentMapper.selectByCondition(Condition
                        .builder(MtBomComponent.class)
                        .andWhere(Sqls.custom().andEqualTo(MtBomComponent.FIELD_TENANT_ID, tenantId)
                                        .andLessThanOrEqualTo(MtBomComponent.FIELD_DATE_FROM, now))
                        .andWhere(Sqls.custom().andGreaterThanOrEqualTo(MtBomComponent.FIELD_DATE_TO, now)
                                        .orIsNull(MtBomComponent.FIELD_DATE_TO))
                        .andWhere(Sqls.custom().andIn(MtBomComponent.FIELD_BOM_ID, bomIds)).build());
        if (CollectionUtils.isEmpty(allEnableBomComponents)) {
            return result;
        }

        List<MtBomComponent> phantomBomComponents = new ArrayList<>();
        if (MtBaseConstants.YES.equals(isPhantomUnfold)) {
            phantomBomComponents = allEnableBomComponents.stream().filter(
                            c -> MtBaseConstants.BOM_COMPONENT_TYPE.PHANTOM.equalsIgnoreCase(c.getBomComponentType()))
                            .collect(Collectors.toList());
        }
        List<String> phantomBomComponentIds = phantomBomComponents.stream().map(MtBomComponent::getBomComponentId)
                        .collect(Collectors.toList());

        List<MtBomComponent> conditionBomComponents = new ArrayList<>(allEnableBomComponents);
        if (MtBaseConstants.YES.equals(keyComponentFlag)) {
            conditionBomComponents = conditionBomComponents.stream()
                            .filter(c -> keyComponentFlag.equals(c.getKeyMaterialFlag())).collect(Collectors.toList());
        }
        if (StringUtils.isNotEmpty(assembleMethod)) {
            conditionBomComponents = conditionBomComponents.stream()
                            .filter(c -> assembleMethod.equals(c.getAssembleMethod())).collect(Collectors.toList());
        }
        if (!MtBaseConstants.YES.equals(substituteFlag) && CollectionUtils.isNotEmpty(pMaterialIds)) {
            conditionBomComponents = conditionBomComponents.stream()
                            .filter(c -> pMaterialIds.contains(c.getMaterialId())).collect(Collectors.toList());
        }

        List<String> mergeBomComponentIds = new ArrayList<>();
        mergeBomComponentIds.addAll(conditionBomComponents.stream().map(MtBomComponent::getBomComponentId)
                        .collect(Collectors.toList()));
        mergeBomComponentIds.addAll(phantomBomComponentIds);
        mergeBomComponentIds = mergeBomComponentIds.stream().distinct().collect(Collectors.toList());
        final List<String> distinctMergeBomComponentIds = mergeBomComponentIds;
        List<MtBomComponent> mergeBomComponents = allEnableBomComponents.stream()
                        .filter(c -> distinctMergeBomComponentIds.contains(c.getBomComponentId()))
                        .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(mergeBomComponents)) {
            return result;
        }

        // 5.获取组件考虑损耗率之后的单件理论用量
        MtBomComponentVO6 mtBomComponentVO6 = new MtBomComponentVO6();
        mtBomComponentVO6.setAttritionFlag(
                        MtBaseConstants.YES.equalsIgnoreCase(attritionFlag) ? MtBaseConstants.YES : MtBaseConstants.NO);
        mtBomComponentVO6.setBomMessageList(new ArrayList<>());

        for (MtBom originBom : originBoms) {
            List<MtBomComponentVO21> list =
                            bomMessages.stream().filter(c -> c.getBomId().compareTo(originBom.getBomId()) == 0)
                                            .collect(Collectors.toList());

            for (MtBomComponentVO21 vo : list) {
                MtBomVO26 mtBomVO26 = new MtBomVO26();
                mtBomVO26.setBomId(vo.getBomId());
                mtBomVO26.setPrimaryQty(originBom.getPrimaryQty());
                mtBomVO26.setQty(vo.getQty());
                mtBomVO26.setBomComponentList(new ArrayList<>());
                mtBomComponentVO6.getBomMessageList().add(mtBomVO26);

                List<MtBomComponent> bomCompList = mergeBomComponents.stream()
                                .filter(c -> c.getBomId().compareTo(vo.getBomId()) == 0).collect(Collectors.toList());
                bomCompList.stream().forEach(c -> {
                    MtBomComponentVO27 mtBomComponentVO27 = new MtBomComponentVO27();
                    mtBomComponentVO27.setAttritionChance(c.getAttritionChance());
                    mtBomComponentVO27.setAttritionPolicy(c.getAttritionPolicy());
                    mtBomComponentVO27.setAttritionQty(c.getAttritionQty());
                    mtBomComponentVO27.setBomComponentId(c.getBomComponentId());
                    mtBomComponentVO27.setBomComponentQty(c.getQty());
                    mtBomVO26.getBomComponentList().add(mtBomComponentVO27);
                });
            }
        }
        List<MtBomComponentVO28> attritionLimitResult =
                        attritionLimitComponentQtyBatchCalculate(tenantId, mtBomComponentVO6);

        // 6.获取组件考虑替代项之后的单件理论用量和组件物料ID
        MtBomSubstituteVO15 mtBomSubstituteVO15 = new MtBomSubstituteVO15();
        mtBomSubstituteVO15.setBomComponentList(new ArrayList<>());
        mtBomSubstituteVO15.setMaterialIds(pMaterialIds);
        mtBomSubstituteVO15.setSubstituteFlag(MtBaseConstants.YES.equalsIgnoreCase(substituteFlag) ? MtBaseConstants.YES
                        : MtBaseConstants.NO);
        for (MtBomComponent mtBomComponent : mergeBomComponents) {
            MtBomSubstituteVO14 mtBomSubstituteVO14 = new MtBomSubstituteVO14();
            mtBomSubstituteVO14.setBomComponentId(mtBomComponent.getBomComponentId());
            mtBomSubstituteVO14.setBomComponentMaterialId(mtBomComponent.getMaterialId());
            mtBomSubstituteVO14.setQty(1.0D);
            mtBomSubstituteVO15.getBomComponentList().add(mtBomSubstituteVO14);
        }
        List<MtBomSubstituteVO17> mtBomSubstituteList =
                        mtBomSubstituteRepository.bomSubstituteQtyBatchCalculate(tenantId, mtBomSubstituteVO15);
        List<MtBomSubstituteVO17> bomSubstituteList = new ArrayList<>();
        for (MtBomSubstituteVO17 mtBomSubstituteVO17 : mtBomSubstituteList) {
            if (bomSubstituteList.stream().noneMatch(
                            c -> c.getBomComponentId().compareTo(mtBomSubstituteVO17.getBomComponentId()) == 0)) {
                bomSubstituteList.add(mtBomSubstituteVO17);
            }
        }

        Map<String, MtBomComponent> mergeBomComponentMap = mergeBomComponents.stream()
                        .collect(Collectors.toMap(MtBomComponent::getBomComponentId, c -> c));
        List<PhantomUnfoldBom> phantomUnfoldBoms = Collections.synchronizedList(new ArrayList<>());

        bomMessages.parallelStream().forEach(bomMessage -> {
            MtBomComponentVO25 mtBomComponentVO25 = new MtBomComponentVO25();
            mtBomComponentVO25.setSiteId(bomMessage.getSiteId());
            mtBomComponentVO25.setBomId(bomMessage.getBomId());
            mtBomComponentVO25.setQty(bomMessage.getQty());
            mtBomComponentVO25.setBomComponentList(new ArrayList<>());
            result.add(mtBomComponentVO25);

            Optional<MtBomComponentVO28> optional = attritionLimitResult.stream().filter(
                            c -> c.getBomId().compareTo(bomMessage.getBomId()) == 0 && BigDecimal.valueOf(c.getQty())
                                            .compareTo(BigDecimal.valueOf(bomMessage.getQty())) == 0)
                            .findFirst();
            if (optional.isPresent()) {
                for (MtBomComponentVO29 mtBomComponentVO29 : optional.get().getBomComponentList()) {
                    MtBomComponent mtBomComponent = mergeBomComponentMap.get(mtBomComponentVO29.getBomComponentId());
                    PhantomUnfoldBom phantomUnfoldBom = null;

                    if (MtBaseConstants.BOM_COMPONENT_TYPE.PHANTOM.equals(mtBomComponent.getBomComponentType())) {
                        phantomUnfoldBom = new PhantomUnfoldBom();
                        phantomUnfoldBom.setSiteId(bomMessage.getSiteId());
                        phantomUnfoldBom.setBomId(bomMessage.getBomId());
                        phantomUnfoldBom.setQty(bomMessage.getQty());
                        phantomUnfoldBom.setBomComponentId(mtBomComponent.getBomComponentId());
                        phantomUnfoldBoms.add(phantomUnfoldBom);
                    }

                    MtBomComponentVO24 mtBomComponentVO24 = new MtBomComponentVO24();
                    mtBomComponentVO24.setBomComponentId(mtBomComponent.getBomComponentId());
                    mtBomComponentVO24.setBomComponentType(mtBomComponent.getBomComponentType());
                    mtBomComponentVO24.setKeyMaterialFlag(mtBomComponent.getKeyMaterialFlag());

                    MtBomSubstituteVO17 mtBomSubstituteVO17 = bomSubstituteList.stream().filter(
                                    c -> c.getBomComponentId().compareTo(mtBomComponent.getBomComponentId()) == 0)
                                    .findFirst().get();
                    mtBomComponentVO24.setSubstituteFlag(mtBomSubstituteVO17.getSubstituteFlag());
                    mtBomComponentVO24.setMaterialMessageList(new ArrayList<>());

                    for (MtBomSubstituteVO16 mtBomSubstituteVO16 : mtBomSubstituteVO17.getSubstituteMaterialList()) {
                        if (CollectionUtils.isNotEmpty(pMaterialIds)) {
                            if (MtBaseConstants.YES.equalsIgnoreCase(isPhantomUnfold)) {
                                if (!MtBaseConstants.BOM_COMPONENT_TYPE.PHANTOM
                                                .equals(mtBomComponent.getBomComponentType())) {
                                    if (pMaterialIds.contains(mtBomSubstituteVO16.getMaterialId())) {
                                        MtBomComponentVO23 mtBomComponentVO23 = new MtBomComponentVO23();
                                        mtBomComponentVO23.setMaterialId(mtBomSubstituteVO16.getMaterialId());
                                        mtBomComponentVO23.setQty(BigDecimal
                                                        .valueOf(mtBomSubstituteVO16.getComponentQty())
                                                        .multiply(BigDecimal
                                                                        .valueOf(mtBomComponentVO29.getComponentQty()))
                                                        .doubleValue());
                                        mtBomComponentVO24.getMaterialMessageList().add(mtBomComponentVO23);

                                        if (phantomUnfoldBom != null) {
                                            phantomUnfoldBom.getBomComponents().add(mtBomComponentVO23);
                                        }
                                    }
                                } else {
                                    MtBomComponentVO23 mtBomComponentVO23 = new MtBomComponentVO23();
                                    mtBomComponentVO23.setMaterialId(mtBomSubstituteVO16.getMaterialId());
                                    mtBomComponentVO23.setQty(BigDecimal.valueOf(mtBomSubstituteVO16.getComponentQty())
                                                    .multiply(BigDecimal.valueOf(mtBomComponentVO29.getComponentQty()))
                                                    .doubleValue());
                                    mtBomComponentVO24.getMaterialMessageList().add(mtBomComponentVO23);

                                    if (phantomUnfoldBom != null) {
                                        phantomUnfoldBom.getBomComponents().add(mtBomComponentVO23);
                                    }
                                }
                            } else {
                                if (pMaterialIds.contains(mtBomSubstituteVO16.getMaterialId())) {
                                    MtBomComponentVO23 mtBomComponentVO23 = new MtBomComponentVO23();
                                    mtBomComponentVO23.setMaterialId(mtBomSubstituteVO16.getMaterialId());
                                    mtBomComponentVO23.setQty(BigDecimal.valueOf(mtBomSubstituteVO16.getComponentQty())
                                                    .multiply(BigDecimal.valueOf(mtBomComponentVO29.getComponentQty()))
                                                    .doubleValue());
                                    mtBomComponentVO24.getMaterialMessageList().add(mtBomComponentVO23);

                                    if (phantomUnfoldBom != null) {
                                        phantomUnfoldBom.getBomComponents().add(mtBomComponentVO23);
                                    }
                                }
                            }
                        } else {
                            MtBomComponentVO23 mtBomComponentVO23 = new MtBomComponentVO23();
                            mtBomComponentVO23.setMaterialId(mtBomSubstituteVO16.getMaterialId());
                            mtBomComponentVO23.setQty(BigDecimal.valueOf(mtBomSubstituteVO16.getComponentQty())
                                            .multiply(BigDecimal.valueOf(mtBomComponentVO29.getComponentQty()))
                                            .doubleValue());
                            mtBomComponentVO24.getMaterialMessageList().add(mtBomComponentVO23);

                            if (phantomUnfoldBom != null) {
                                phantomUnfoldBom.getBomComponents().add(mtBomComponentVO23);
                            }
                        }
                    }
                    mtBomComponentVO25.getBomComponentList().add(mtBomComponentVO24);
                }
            }
        });
        // 虚拟件逻辑注释
        // if (MtBaseConstants.YES.equalsIgnoreCase(isPhantomUnfold) &&
        // CollectionUtils.isNotEmpty(phantomUnfoldBoms)) {
        // List<MtBomComponentVO21> nextbomMessages = new ArrayList<>();
        // List<PhantomUnfoldBomDetail> phantomUnfoldBomDetails = new ArrayList<>();
        // List<MtPfepManufacturingVO1> mtPfepManufacturingVO1s = new ArrayList<>();
        // List<MtPfepScheduleVO2> scheduleVO2s = new ArrayList<>();
        //
        // for (PhantomUnfoldBom phantomUnfoldBom : phantomUnfoldBoms) {
        // List<String> materialIds = new ArrayList<>();
        // for (MtBomComponentVO23 mtBomComponentVO23 : phantomUnfoldBom.getBomComponents()) {
        // if (!materialIds.contains(mtBomComponentVO23.getMaterialId())) {
        // materialIds.add(mtBomComponentVO23.getMaterialId());
        // }
        //
        // PhantomUnfoldBomDetail phantomUnfoldBomDetail = new PhantomUnfoldBomDetail();
        // phantomUnfoldBomDetail.setSiteId(phantomUnfoldBom.getSiteId());
        // phantomUnfoldBomDetail.setBomComponentId(phantomUnfoldBom.getBomComponentId());
        // phantomUnfoldBomDetail.setMaterialId(mtBomComponentVO23.getMaterialId());
        // phantomUnfoldBomDetail.setQty(mtBomComponentVO23.getQty());
        // phantomUnfoldBomDetail.setDefaultBomId(String.valueOf(-1L));
        // phantomUnfoldBomDetail.setBomId(phantomUnfoldBom.getBomId());
        // phantomUnfoldBomDetails.add(phantomUnfoldBomDetail);
        // }
        //
        // materialIds.stream().forEach(c -> {
        // if (MtBaseConstants.ORGANIZATION_REL_TYPE.MANUFACTURING
        // .equalsIgnoreCase(siteMap.get(phantomUnfoldBom.getSiteId()).getSiteType())) {
        // MtPfepManufacturingVO1 manufacturingVO1 = new MtPfepManufacturingVO1();
        // manufacturingVO1.setMaterialId(c);
        // manufacturingVO1.setSiteId(phantomUnfoldBom.getSiteId());
        // mtPfepManufacturingVO1s.add(manufacturingVO1);
        // } else {
        // MtPfepScheduleVO2 scheduleVO2 = new MtPfepScheduleVO2();
        // scheduleVO2.setMaterialId(c);
        // scheduleVO2.setSiteId(phantomUnfoldBom.getSiteId());
        // scheduleVO2s.add(scheduleVO2);
        // }
        // });
        // }
        //
        // if (CollectionUtils.isNotEmpty(mtPfepManufacturingVO1s)) {
        // List<MtPfepManufacturingVO11> pfepManufacturingVO7s = mtPfepManufacturingRepository
        // .pfepManufacturingBatchGet(tenantId, mtPfepManufacturingVO1s,
        // Arrays.asList(MtPfepManufacturing.FIELD_DEFAULT_BOM_ID));
        // for (MtPfepManufacturingVO11 vo : pfepManufacturingVO7s) {
        // List<PhantomUnfoldBomDetail> filterList = phantomUnfoldBomDetails.stream()
        // .filter(c -> c.getMaterialId().compareTo(vo.getMaterialId()) == 0
        // && c.getSiteId().compareTo(vo.getSiteId()) == 0)
        // .collect(Collectors.toList());
        //
        // for (PhantomUnfoldBomDetail phantomUnfoldBomDetail : filterList) {
        // if (MtIdHelper.isIdNotNull(vo.getDefaultBomId())) {
        // MtBomComponentVO21 mtBomComponentVO21 = new MtBomComponentVO21();
        // mtBomComponentVO21.setBomId(vo.getDefaultBomId());
        // mtBomComponentVO21.setSiteId(vo.getSiteId());
        // mtBomComponentVO21.setQty(phantomUnfoldBomDetail.getQty());
        // nextbomMessages.add(mtBomComponentVO21);
        // phantomUnfoldBomDetail.setDefaultBomId(vo.getDefaultBomId());
        // }
        // }
        // }
        // }
        // if (CollectionUtils.isNotEmpty(scheduleVO2s)) {
        // List<MtPfepScheduleVO4> mtPfepScheduleVO7s =
        // mtPfepScheduleRepository.pfepScheduleBatchGet(tenantId,
        // scheduleVO2s, Arrays.asList(MtPfepSchedule.FIELD_DEFAULT_BOM_ID));
        // for (MtPfepScheduleVO4 vo : mtPfepScheduleVO7s) {
        // List<PhantomUnfoldBomDetail> filterList = phantomUnfoldBomDetails.stream()
        // .filter(c -> c.getMaterialId().compareTo(vo.getMaterialId()) == 0
        // && c.getSiteId().compareTo(vo.getSiteId()) == 0)
        // .collect(Collectors.toList());
        //
        // for (PhantomUnfoldBomDetail phantomUnfoldBomDetail : filterList) {
        // if (MtIdHelper.isIdNotNull(vo.getDefaultBomId())) {
        // MtBomComponentVO21 mtBomComponentVO21 = new MtBomComponentVO21();
        // mtBomComponentVO21.setBomId(vo.getDefaultBomId());
        // mtBomComponentVO21.setSiteId(vo.getSiteId());
        // mtBomComponentVO21.setQty(phantomUnfoldBomDetail.getQty());
        // nextbomMessages.add(mtBomComponentVO21);
        // phantomUnfoldBomDetail.setDefaultBomId(vo.getDefaultBomId());
        // }
        // }
        // }
        // }
        //
        // List<MtBomComponentVO25> recurisveList = bomComponentQtyBatchCalculateRecursive(tenantId,
        // attritionFlag,
        // substituteFlag, keyComponentFlag, isPhantomUnfold, nextbomMessages, pMaterialIds,
        // pBomComponentIds, assembleMethod, now, Collections.synchronizedList(new ArrayList<>()),
        // siteMap);
        //
        // // 去重
        // List<PhantomUnfoldBomDetail> tmpPhantomUnfoldBomDetails =
        // phantomUnfoldBomDetails.stream().distinct().collect(Collectors.toList());
        //
        // result.parallelStream().forEach(vo -> {
        // List<MtBomComponentVO24> bomComponentList = new ArrayList<>();
        //
        // for (MtBomComponentVO24 mtBomComponentVO24 : vo.getBomComponentList()) {
        // List<String> materialMessages = mtBomComponentVO24.getMaterialMessageList().stream()
        // .map(c -> c.getMaterialId() + ":" + c.getQty()).collect(Collectors.toList());
        //
        // List<PhantomUnfoldBomDetail> filterList = tmpPhantomUnfoldBomDetails.stream()
        // .filter(c -> c.getSiteId().compareTo(vo.getSiteId()) == 0
        // && c.getBomId().compareTo(vo.getBomId()) == 0
        // && c.getBomComponentId().compareTo(
        // mtBomComponentVO24.getBomComponentId()) == 0
        // && materialMessages.contains(c.getMaterialId() + ":" + c.getQty()))
        // .collect(Collectors.toList());
        // if (CollectionUtils.isEmpty(filterList)) {
        // bomComponentList.add(mtBomComponentVO24);
        // continue;
        // }
        //
        // final List<MtBomComponentVO24> tmpList = new ArrayList<>();
        // for (PhantomUnfoldBomDetail phantomUnfoldBomDetail : filterList) {
        // Optional<MtBomComponentVO25> optional = recurisveList.stream().filter(
        // c -> c.getBomId().compareTo(phantomUnfoldBomDetail.getDefaultBomId()) == 0
        // && c.getSiteId().compareTo(
        // phantomUnfoldBomDetail.getSiteId()) == 0
        // && BigDecimal.valueOf(phantomUnfoldBomDetail.getQty())
        // .compareTo(BigDecimal.valueOf(c.getQty())) == 0)
        // .findFirst();
        // if (optional.isPresent()) {
        // tmpList.addAll(optional.get().getBomComponentList());
        // }
        // }
        //
        // bomComponentList.addAll(tmpList);
        // }
        // vo.setBomComponentList(bomComponentList);
        // });
        // }
        return result;
    }

    private List<MtBom> validateBom(Long tenantId, List<MtBomComponentVO21> bomMessages) {
        List<String> bomIds =
                        bomMessages.stream().map(MtBomComponentVO21::getBomId).distinct().collect(Collectors.toList());
        List<MtBom> mtBoms = mtBomRepository.bomLimitBomIdQuery(tenantId, bomIds);
        if (CollectionUtils.isEmpty(mtBoms)) {
            throw new MtException("MT_BOM_0074", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0074", "BOM", bomIds.toString(), "【API:bomComponentQtyBatchCalculate】"));
        }

        List<String> notExistsBomIds = mtBoms.stream().filter(t -> !bomIds.contains(t.getBomId())).map(MtBom::getBomId)
                        .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(notExistsBomIds)) {
            throw new MtException("MT_BOM_0074", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0074", "BOM", notExistsBomIds.toString(), "【API:bomComponentQtyBatchCalculate】"));
        }
        return mtBoms;
    }

    private static class PhantomUnfoldBom {
        private String siteId;
        private String bomId;
        private Double qty;
        private String bomComponentId;
        private List<MtBomComponentVO23> bomComponents = new ArrayList<>();

        public String getSiteId() {
            return siteId;
        }

        public void setSiteId(String siteId) {
            this.siteId = siteId;
        }

        public String getBomId() {
            return bomId;
        }

        public void setBomId(String bomId) {
            this.bomId = bomId;
        }

        public Double getQty() {
            return qty;
        }

        public void setQty(Double qty) {
            this.qty = qty;
        }

        public String getBomComponentId() {
            return bomComponentId;
        }

        public void setBomComponentId(String bomComponentId) {
            this.bomComponentId = bomComponentId;
        }

        public List<MtBomComponentVO23> getBomComponents() {
            return bomComponents;
        }

        public void setBomComponents(List<MtBomComponentVO23> bomComponents) {
            this.bomComponents = bomComponents;
        }

    }

    private static class PhantomUnfoldBomDetail {
        private String siteId;
        private String bomId;
        private String bomComponentId;
        private String materialId;
        private Double qty;
        private String defaultBomId;

        public String getSiteId() {
            return siteId;
        }

        public void setSiteId(String siteId) {
            this.siteId = siteId;
        }

        public String getBomId() {
            return bomId;
        }

        public void setBomId(String bomId) {
            this.bomId = bomId;
        }

        public String getBomComponentId() {
            return bomComponentId;
        }

        public void setBomComponentId(String bomComponentId) {
            this.bomComponentId = bomComponentId;
        }

        public String getMaterialId() {
            return materialId;
        }

        public void setMaterialId(String materialId) {
            this.materialId = materialId;
        }

        public Double getQty() {
            return qty;
        }

        public void setQty(Double qty) {
            this.qty = qty;
        }

        public String getDefaultBomId() {
            return defaultBomId;
        }

        public void setDefaultBomId(String defaultBomId) {
            this.defaultBomId = defaultBomId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            PhantomUnfoldBomDetail that = (PhantomUnfoldBomDetail) o;
            return Objects.equals(getSiteId(), that.getSiteId()) && Objects.equals(getBomId(), that.getBomId())
                            && Objects.equals(getBomComponentId(), that.getBomComponentId())
                            && Objects.equals(getMaterialId(), that.getMaterialId())
                            && Objects.equals(getQty(), that.getQty())
                            && Objects.equals(getDefaultBomId(), that.getDefaultBomId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getSiteId(), getBomId(), getBomComponentId(), getMaterialId(), getQty(),
                            getDefaultBomId());
        }
    }

    @Override
    public List<MtBomComponentVO28> attritionLimitComponentQtyBatchCalculate(Long tenantId, MtBomComponentVO6 dto) {
        if (dto.getBomMessageList().stream().anyMatch(c -> MtIdHelper.isIdNull(c.getBomId()))) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "bomId", "BOM", "【API:attritionLimitComponentQtyBatchCalculate】"));
        }
        if (dto.getBomMessageList().stream().anyMatch(c -> c.getPrimaryQty() == null)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "primaryQty", "【API:attritionLimitComponentQtyBatchCalculate】"));
        }
        if (dto.getBomMessageList().stream().anyMatch(c -> c.getQty() == null)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "qty", "BOM", "【API:attritionLimitComponentQtyBatchCalculate】"));
        }

        List<MtBomComponentVO28> result = new ArrayList<>();
        MtBomComponentVO28 mtBomComponentVO28;
        for (MtBomVO26 vo : dto.getBomMessageList()) {
            if (vo.getBomComponentList().stream().anyMatch(c -> MtIdHelper.isIdNull(c.getBomComponentId()))) {
                throw new MtException("MT_BOM_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0001", "BOM",
                                                "bomComponentId", "【API:attritionLimitComponentQtyBatchCalculate】"));
            }
            if (vo.getBomComponentList().stream().anyMatch(c -> c.getBomComponentQty() == null)) {
                throw new MtException("MT_BOM_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0001", "BOM",
                                                "bomComponentQty", "【API:attritionLimitComponentQtyBatchCalculate】"));
            }

            mtBomComponentVO28 = new MtBomComponentVO28();
            List<MtBomComponentVO29> bomComponentList = new ArrayList<>();
            mtBomComponentVO28.setBomComponentList(bomComponentList);
            mtBomComponentVO28.setBomId(vo.getBomId());
            mtBomComponentVO28.setQty(vo.getQty());
            result.add(mtBomComponentVO28);

            // BOM基本数量
            BigDecimal primaryQty = BigDecimal.valueOf(vo.getPrimaryQty());
            // 产品需求数量
            BigDecimal qty = BigDecimal.valueOf(vo.getQty());
            MtBomComponentVO29 mtBomComponentVO29;
            for (MtBomComponentVO27 bomComponent : vo.getBomComponentList()) {
                mtBomComponentVO29 = new MtBomComponentVO29();
                // 组件数量
                BigDecimal bomComponentQty = BigDecimal.valueOf(bomComponent.getBomComponentQty());
                // 损耗策略 1按固定值，2按百分比，3固定值+百分比
                String attritionPolicy = StringUtils.isEmpty(bomComponent.getAttritionPolicy()) ? ""
                                : bomComponent.getAttritionPolicy();
                // 损耗百分比
                BigDecimal attritionChance = bomComponent.getAttritionChance() == null ? BigDecimal.ZERO
                                : BigDecimal.valueOf(bomComponent.getAttritionChance());
                // 固定损耗值
                BigDecimal attritionQty = bomComponent.getAttritionQty() == null ? BigDecimal.ZERO
                                : BigDecimal.valueOf(bomComponent.getAttritionQty());
                // 返回值
                BigDecimal componentQty;

                // 临时计算变量
                BigDecimal tmpCalcQty;

                if (StringUtils.isEmpty(attritionPolicy) || MtBaseConstants.NO.equals(dto.getAttritionFlag())) {
                    componentQty = qty.multiply(bomComponentQty).divide(primaryQty, 10, BigDecimal.ROUND_HALF_DOWN);
                } else if ("1".equals(attritionPolicy)) {
                    componentQty = qty.multiply(bomComponentQty).divide(primaryQty, 10, BigDecimal.ROUND_HALF_DOWN)
                                    .add(attritionQty);
                } else if ("2".equals(attritionPolicy)) {
                    tmpCalcQty = attritionChance.divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_DOWN);
                    tmpCalcQty = BigDecimal.ONE.add(tmpCalcQty);
                    componentQty = qty.multiply(bomComponentQty).multiply(tmpCalcQty).divide(primaryQty, 10,
                                    BigDecimal.ROUND_HALF_DOWN);
                } else if ("3".equals(attritionPolicy)) {
                    BigDecimal tmpQty = new BigDecimal(bomComponentQty.toString());
                    tmpCalcQty = new BigDecimal(attritionChance.toString()).divide(new BigDecimal("100"), 10,
                                    BigDecimal.ROUND_HALF_DOWN);
                    tmpCalcQty = BigDecimal.ONE.add(tmpCalcQty);
                    componentQty = qty.multiply(tmpQty).multiply(tmpCalcQty)
                                    .divide(primaryQty, 10, BigDecimal.ROUND_HALF_DOWN).add(attritionQty);
                } else {
                    throw new MtException("MT_BOM_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_BOM_0017", "BOM", "【API:attritionLimitComponentQtyBatchCalculate】"));
                }
                mtBomComponentVO29.setBomComponentId(bomComponent.getBomComponentId());
                mtBomComponentVO29.setComponentQty(componentQty.doubleValue());
                bomComponentList.add(mtBomComponentVO29);
            }
        }
        return result;
    }
}
