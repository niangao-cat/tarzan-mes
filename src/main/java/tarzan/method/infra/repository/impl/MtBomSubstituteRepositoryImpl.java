package tarzan.method.infra.repository.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.*;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.entity.MtBomSubstitute;
import tarzan.method.domain.entity.MtBomSubstituteGroup;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.method.domain.repository.MtBomHisRepository;
import tarzan.method.domain.repository.MtBomSubstituteGroupRepository;
import tarzan.method.domain.repository.MtBomSubstituteRepository;
import tarzan.method.domain.vo.*;
import tarzan.method.infra.mapper.MtBomComponentMapper;
import tarzan.method.infra.mapper.MtBomSubstituteGroupMapper;
import tarzan.method.infra.mapper.MtBomSubstituteMapper;

/**
 * 装配清单行替代项 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@Component
public class MtBomSubstituteRepositoryImpl extends BaseRepositoryImpl<MtBomSubstitute>
                implements MtBomSubstituteRepository {

    private static final String PRIORITY = "PRIORITY";
    private static final String CHANCE = "CHANCE";
    private static final String Y_FLAG = "Y";
    private static final String N_FLAG = "N";

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtBomSubstituteMapper mtBomSubstituteMapper;

    @Autowired
    private MtBomComponentMapper mtBomComponentMapper;

    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;

    @Autowired
    private MtBomSubstituteGroupRepository mtBomSubstituteGroupRepository;

    @Autowired
    private MtBomSubstituteGroupMapper mtBomSubstituteGroupMapper;

    @Autowired
    private MtBomHisRepository mtBomHisRepository;

    @Autowired
    private CustomSequence customSequence;

    @Override
    public MtBomSubstitute bomSubstituteBasicGet(Long tenantId, String bomSubstituteId) {
        if (StringUtils.isEmpty(bomSubstituteId)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomSubstituteId", "【API:bomSubstituteBasicGet】"));
        }

        MtBomSubstitute mtBomSubstitute = new MtBomSubstitute();
        mtBomSubstitute.setTenantId(tenantId);
        mtBomSubstitute.setBomSubstituteId(bomSubstituteId);
        return this.mtBomSubstituteMapper.selectOne(mtBomSubstitute);
    }

    @Override
    public List<Map<String, String>> groupLimitBomSubstituteQuery(Long tenantId, String bomSubstituteGroupId) {
        if (StringUtils.isEmpty(bomSubstituteGroupId)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomSubstituteGroupId", "【API:groupLimitBomSubstituteQuery】"));
        }

        MtBomSubstitute mtBomSubstitute = new MtBomSubstitute();
        mtBomSubstitute.setTenantId(tenantId);
        mtBomSubstitute.setBomSubstituteGroupId(bomSubstituteGroupId);
        List<MtBomSubstitute> mtBomSubstitutes = this.mtBomSubstituteMapper.select(mtBomSubstitute);
        if (CollectionUtils.isEmpty(mtBomSubstitutes)) {
            return Collections.emptyList();
        }

        final List<Map<String, String>> list = new ArrayList<>();
        mtBomSubstitutes.stream().forEach(t -> {
            Map<String, String> map = new HashMap<String, String>();
            map.put("bomSubstituteGroupId", t.getBomSubstituteGroupId());
            map.put("bomSubstituteId", t.getBomSubstituteId());
            list.add(map);
        });
        return list;
    }

    @Override
    public List<MtBomSubstituteVO2> propertyLimitBomSubstituteQuery(Long tenantId, MtBomSubstituteVO condition) {
        if (condition.getBomComponentId() == null && condition.getBomId() == null
                        && condition.getBomSubstituteGroupId() == null && condition.getMaterialId() == null
                        && condition.getOnlyAvailableFlag() == null && condition.getDateFrom() == null
                        && condition.getDateTo() == null && condition.getSubstituteValue() == null
                        && condition.getSubstituteUsage() == null) {
            throw new MtException("MT_BOM_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0002", "BOM", "【API:propertyLimitBomSubstituteQuery】"));
        }

        List<MtBomSubstituteVO2> list = this.mtBomSubstituteMapper.selectConditionCustom(tenantId, condition);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        return list;
    }

    @Override
    public List<MtBomSubstituteVO3> bomSubstituteQtyCalculate(Long tenantId, MtBomSubstituteVO6 dto) {
        if (StringUtils.isEmpty(dto.getBomComponentId())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomComponentId", "【API:bomSubstituteQtyCalculate】"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "qty", "【API:bomSubstituteQtyCalculate】"));
        }

        MtBomComponent mtBomComponent = new MtBomComponent();
        mtBomComponent.setTenantId(tenantId);
        mtBomComponent.setBomComponentId(dto.getBomComponentId());
        mtBomComponent = this.mtBomComponentMapper.selectOne(mtBomComponent);
        if (mtBomComponent == null) {
            throw new MtException("MT_BOM_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0015", "BOM", "【API:bomSubstituteQtyCalculate】"));
        }

        // 4: 基于获取到的组件行数据，获取MATERIAL_ID(组件物料ID)
        String bomComponentMaterialId = mtBomComponent.getMaterialId();

        // 6: 基于[parameter1]，调用API【bomSubstituteQuery】，获取替代组属性ID和替代项属性ID
        List<MtBomSubstituteGroupVO3> list =
                        this.mtBomSubstituteGroupRepository.bomSubstituteQuery(tenantId, dto.getBomComponentId());
        // 7: 基于替代组属性ID，在MT_BOM_SUBSTITUTE_GROUP表获取替代策略SUBSTITUTE_POLICY
        String substitutePolicy = null;
        if (CollectionUtils.isEmpty(list)) {
            substitutePolicy = null;
        } else {
            String bomSubstituteGroupId = list.get(0).getBomSubstituteGroupId();
            MtBomSubstituteGroup mtBomSubstituteGroup = new MtBomSubstituteGroup();
            mtBomSubstituteGroup.setTenantId(tenantId);
            mtBomSubstituteGroup.setBomSubstituteGroupId(bomSubstituteGroupId);
            mtBomSubstituteGroup = this.mtBomSubstituteGroupMapper.selectOne(mtBomSubstituteGroup);
            if (mtBomSubstituteGroup == null) {
                throw new MtException("MT_BOM_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0020", "BOM", bomSubstituteGroupId, "【API:bomSubstituteQtyCalculate】"));
            }

            substitutePolicy = mtBomSubstituteGroup.getSubstitutePolicy();
            if (StringUtils.isNotEmpty(substitutePolicy) && !substitutePolicy.equals("PRIORITY")
                            && !substitutePolicy.equals("CHANCE")) {
                throw new MtException("MT_BOM_0021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0021", "BOM", "【API:bomSubstituteQtyCalculate】"));
            }
        }

        List<MtBomSubstituteVO3> result = new ArrayList<MtBomSubstituteVO3>();
        // 如果步骤7获取的替代策略SUBSTITUTE_POLICY为空或传入的[input3]为N时，则表示不考虑替代
        if (StringUtils.isEmpty(substitutePolicy) || "N".equals(dto.getSubstituteFlag())) {
            // 补充：如果[input4]中传入的materialId与步骤4获取的MATERIAL_ID不一致，返回为空
            if (StringUtils.isNotEmpty(dto.getMaterialId()) && !bomComponentMaterialId.equals(dto.getMaterialId())) {
                return Collections.emptyList();
            }

            MtBomSubstituteVO3 bomSubstituteVO = new MtBomSubstituteVO3();
            bomSubstituteVO.setMaterialId(bomComponentMaterialId);
            bomSubstituteVO.setComponentQty(dto.getQty());
            result.add(bomSubstituteVO);
        } else if (!"N".equals(dto.getSubstituteFlag()) && substitutePolicy.equals("CHANCE")) {
            final List<String> substituteIds = new ArrayList<String>();
            list.stream().map(MtBomSubstituteGroupVO3::getBomSubstituteId).forEach(t -> substituteIds.add(t));
            List<MtBomSubstitute> mtBomSubstitutes =
                            this.mtBomSubstituteMapper.selectByIdsCustom(tenantId, substituteIds);

            // 如果传入参数[input4]不为空，则筛选步骤8获取的MATERIAL_ID中，物料ID等于传入的[input4]的行，仅输出传入materialId对应的用量；如果[input4]为空，输出所有结果
            if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                mtBomSubstitutes = mtBomSubstitutes.stream().filter(s -> s.getMaterialId().equals(dto.getMaterialId()))
                                .collect(Collectors.toList());
            }

            for (MtBomSubstitute mtBomSubstitute : mtBomSubstitutes) {
                MtBomSubstituteVO3 bomSubstituteVO = new MtBomSubstituteVO3();
                BigDecimal componentQty = null;
                BigDecimal tmpCalcQty = null;
                Double substituteValue = mtBomSubstitute.getSubstituteValue() == null ? Double.valueOf(0.0D)
                                : mtBomSubstitute.getSubstituteValue();
                Double substituteUsage = mtBomSubstitute.getSubstituteUsage() == null ? Double.valueOf(0.0D)
                                : mtBomSubstitute.getSubstituteUsage();
                String substituteMaterialId = mtBomSubstitute.getMaterialId();

                bomSubstituteVO.setMaterialId(substituteMaterialId);
                tmpCalcQty = new BigDecimal(substituteValue.toString()).divide(new BigDecimal("100"), 6,
                                BigDecimal.ROUND_HALF_DOWN);
                componentQty = new BigDecimal(dto.getQty().toString()).multiply(tmpCalcQty)
                                .multiply(BigDecimal.valueOf(substituteUsage));
                bomSubstituteVO.setComponentQty(componentQty.setScale(6, BigDecimal.ROUND_HALF_DOWN).doubleValue());
                result.add(bomSubstituteVO);
            }
        } else if (!"N".equals(dto.getSubstituteFlag()) && substitutePolicy.equals("PRIORITY")) {
            final List<String> substituteIds = new ArrayList<String>();
            list.stream().map(MtBomSubstituteGroupVO3::getBomSubstituteId).forEach(t -> substituteIds.add(t));
            List<MtBomSubstitute> mtBomSubstitutes =
                            this.mtBomSubstituteMapper.selectByIdsCustom(tenantId, substituteIds);

            // 如果[input4]不为空，在步骤8获取的MATERIAL_ID中，筛选物料ID与传入的[input4]相同的行
            if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                mtBomSubstitutes = mtBomSubstitutes.stream().filter(s -> s.getMaterialId().equals(dto.getMaterialId()))
                                .collect(Collectors.toList());
                for (MtBomSubstitute mtBomSubstitute : mtBomSubstitutes) {
                    MtBomSubstituteVO3 bomSubstituteVO = new MtBomSubstituteVO3();
                    bomSubstituteVO.setMaterialId(mtBomSubstitute.getMaterialId());

                    Double substituteUsage = mtBomSubstitute.getSubstituteUsage() == null ? Double.valueOf(0.0D)
                                    : mtBomSubstitute.getSubstituteUsage();
                    BigDecimal componentQty =
                                    BigDecimal.valueOf(dto.getQty()).multiply(BigDecimal.valueOf(substituteUsage));
                    bomSubstituteVO.setComponentQty(componentQty.setScale(6, BigDecimal.ROUND_HALF_DOWN).doubleValue());
                    result.add(bomSubstituteVO);
                }
            } else {
                MtBomSubstituteVO3 bomSubstituteVO = new MtBomSubstituteVO3();
                bomSubstituteVO.setMaterialId(bomComponentMaterialId);
                bomSubstituteVO.setComponentQty(dto.getQty());
                result.add(bomSubstituteVO);
            }
        }
        return result;
    }

    @Override
    public List<MtBomSubstituteVO3> bomSubstituteQtyCalculate(Long tenantId, MtBomSubstituteVO6 dto,
                    MtBomComponent mtBomComponent, MtBomSubstituteGroupVO6 mtBomSubstituteGroupVO6) {
        if (StringUtils.isEmpty(dto.getBomComponentId())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomComponentId", "【API:bomSubstituteQtyCalculate】"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "qty", "【API:bomSubstituteQtyCalculate】"));
        }

        // 4: 基于获取到的组件行数据，获取MATERIAL_ID(组件物料ID)
        String bomComponentMaterialId = mtBomComponent.getMaterialId();

        String substitutePolicy = null;
        if (mtBomSubstituteGroupVO6 == null || mtBomSubstituteGroupVO6.getMtBomSubstituteGroup() == null) {
            substitutePolicy = null;
        } else {

            MtBomSubstituteGroup mtBomSubstituteGroup = mtBomSubstituteGroupVO6.getMtBomSubstituteGroup();
            substitutePolicy = mtBomSubstituteGroup.getSubstitutePolicy();
            if (StringUtils.isNotEmpty(substitutePolicy) && !substitutePolicy.equals("PRIORITY")
                            && !substitutePolicy.equals("CHANCE")) {
                throw new MtException("MT_BOM_0021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0021", "BOM", "【API:bomSubstituteQtyCalculate】"));
            }
        }

        List<MtBomSubstituteVO3> result = new ArrayList<MtBomSubstituteVO3>();
        // 如果步骤7获取的替代策略SUBSTITUTE_POLICY为空或传入的[input3]为N时，则表示不考虑替代
        if (StringUtils.isEmpty(substitutePolicy) || "N".equals(dto.getSubstituteFlag())) {
            // 补充：如果[input4]中传入的materialId与步骤4获取的MATERIAL_ID不一致，返回为空
            if (StringUtils.isNotEmpty(dto.getMaterialId()) && !bomComponentMaterialId.equals(dto.getMaterialId())) {
                return Collections.emptyList();
            }

            MtBomSubstituteVO3 bomSubstituteVO = new MtBomSubstituteVO3();
            bomSubstituteVO.setMaterialId(bomComponentMaterialId);
            bomSubstituteVO.setComponentQty(dto.getQty());
            result.add(bomSubstituteVO);
        } else if (!"N".equals(dto.getSubstituteFlag()) && substitutePolicy.equals("CHANCE")) {
            List<MtBomSubstitute> mtBomSubstitutes = mtBomSubstituteGroupVO6.getMtBomSubstitutes();
            // 如果传入参数[input4]不为空，则筛选步骤8获取的MATERIAL_ID中，物料ID等于传入的[input4]的行，仅输出传入materialId对应的用量；如果[input4]为空，输出所有结果
            if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                mtBomSubstitutes = mtBomSubstitutes.stream().filter(s -> s.getMaterialId().equals(dto.getMaterialId()))
                                .collect(Collectors.toList());
            }

            for (MtBomSubstitute mtBomSubstitute : mtBomSubstitutes) {
                MtBomSubstituteVO3 bomSubstituteVO = new MtBomSubstituteVO3();
                BigDecimal componentQty = null;
                BigDecimal tmpCalcQty = null;
                Double substituteValue = mtBomSubstitute.getSubstituteValue() == null ? Double.valueOf(0.0D)
                                : mtBomSubstitute.getSubstituteValue();
                Double substituteUsage = mtBomSubstitute.getSubstituteUsage() == null ? Double.valueOf(0.0D)
                                : mtBomSubstitute.getSubstituteUsage();
                String substituteMaterialId = mtBomSubstitute.getMaterialId();

                bomSubstituteVO.setMaterialId(substituteMaterialId);
                tmpCalcQty = new BigDecimal(substituteValue.toString()).divide(new BigDecimal("100"), 6,
                                BigDecimal.ROUND_HALF_DOWN);
                componentQty = new BigDecimal(dto.getQty().toString()).multiply(tmpCalcQty)
                                .multiply(BigDecimal.valueOf(substituteUsage));
                bomSubstituteVO.setComponentQty(componentQty.setScale(6, BigDecimal.ROUND_HALF_DOWN).doubleValue());
                result.add(bomSubstituteVO);
            }
        } else if (!"N".equals(dto.getSubstituteFlag()) && substitutePolicy.equals("PRIORITY")) {
            List<MtBomSubstitute> mtBomSubstitutes = mtBomSubstituteGroupVO6.getMtBomSubstitutes();

            // 如果[input4]不为空，在步骤8获取的MATERIAL_ID中，筛选物料ID与传入的[input4]相同的行
            if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                mtBomSubstitutes = mtBomSubstitutes.stream().filter(s -> s.getMaterialId().equals(dto.getMaterialId()))
                                .collect(Collectors.toList());
                for (MtBomSubstitute mtBomSubstitute : mtBomSubstitutes) {
                    MtBomSubstituteVO3 bomSubstituteVO = new MtBomSubstituteVO3();
                    bomSubstituteVO.setMaterialId(mtBomSubstitute.getMaterialId());

                    Double substituteUsage = mtBomSubstitute.getSubstituteUsage() == null ? Double.valueOf(0.0D)
                                    : mtBomSubstitute.getSubstituteUsage();
                    BigDecimal componentQty =
                                    BigDecimal.valueOf(dto.getQty()).multiply(BigDecimal.valueOf(substituteUsage));
                    bomSubstituteVO.setComponentQty(componentQty.setScale(6, BigDecimal.ROUND_HALF_DOWN).doubleValue());
                    result.add(bomSubstituteVO);
                }
            } else {
                MtBomSubstituteVO3 bomSubstituteVO = new MtBomSubstituteVO3();
                bomSubstituteVO.setMaterialId(bomComponentMaterialId);
                bomSubstituteVO.setComponentQty(dto.getQty());
                result.add(bomSubstituteVO);
            }
        }
        return result;
    }

    @Override
    public List<MtBomSubstitute> bomSubstituteBasicBatchGet(Long tenantId, List<String> bomSubstituteIds) {
        if (CollectionUtils.isEmpty(bomSubstituteIds)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomSubstituteId", "【API:bomSubstituteBasicBatchGet】"));
        }
        return this.mtBomSubstituteMapper.selectByIdsCustom(tenantId, bomSubstituteIds);
    }

    @Override
    public List<MtBomSubstituteVO4> bomComponentSubstituteQuery(Long tenantId, String bomComponentId) {
        List<MtBomSubstituteVO4> resultList = new ArrayList<>();

        // 1. 获取该装配清单行的有效替代组ID和有效替代项ID
        List<MtBomSubstituteGroupVO3> bomSubstituteGroups =
                        mtBomSubstituteGroupRepository.bomSubstituteQuery(tenantId, bomComponentId);
        for (MtBomSubstituteGroupVO3 bomSubstituteGroup : bomSubstituteGroups) {
            MtBomSubstituteVO4 result = new MtBomSubstituteVO4();

            // 2. bom_substitute_group表数据
            MtBomSubstituteGroup mtBomSubstituteGroup = mtBomSubstituteGroupRepository
                            .bomSubstituteGroupBasicGet(tenantId, bomSubstituteGroup.getBomSubstituteGroupId());
            if (mtBomSubstituteGroup != null) {
                result.setSubstitutePolicy(mtBomSubstituteGroup.getSubstitutePolicy());
            }

            // 3. mt_bom_substitute表数据
            MtBomSubstitute mtBomSubstitute = bomSubstituteBasicGet(tenantId, bomSubstituteGroup.getBomSubstituteId());
            if (mtBomSubstitute != null) {
                result.setMaterialId(mtBomSubstitute.getMaterialId());
                result.setSubstituteValue(mtBomSubstitute.getSubstituteValue());
                result.setSubstituteUsage(mtBomSubstitute.getSubstituteUsage());
            }
            resultList.add(result);
        }
        return resultList;
    }

    @Override
    public void bomComponentSubstituteVerify(Long tenantId, MtBomSubstituteVO5 dto) {
        // 1. 验证参数是否满足要求
        if (StringUtils.isEmpty(dto.getBomComponentId())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomComponentId", "【bomComponentSubstituteVerify】"));
        }

        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "materialId", "【bomComponentSubstituteVerify】"));
        }

        // 2. 获取该装配清单行的有效替代物料ID
        List<MtBomSubstituteVO4> bomSubstituteVO4s = bomComponentSubstituteQuery(tenantId, dto.getBomComponentId());

        List<String> materialIds = bomSubstituteVO4s.stream().map(MtBomSubstituteVO4::getMaterialId).distinct()
                        .collect(Collectors.toList());

        // 3. 校验输入materialId是否属于获取的替代物料
        if (!materialIds.contains(dto.getMaterialId())) {
            throw new MtException("MT_BOM_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0037", "BOM", "【bomComponentSubstituteVerify】"));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtBomSubstituteVO10 bomSubstituteUpdate(Long tenantId, MtBomSubstitute dto, String fullUpdate) {
        MtBomSubstituteVO10 result = new MtBomSubstituteVO10();
        if (StringUtils.isEmpty(dto.getBomSubstituteId())) {
            if (StringUtils.isEmpty(dto.getBomSubstituteGroupId())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0001", "BOM", "bomSubstituteGroupId", "【API:bomSubstituteUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0001", "BOM", "materialId", "【API:bomSubstituteUpdate】"));
            }
            if (null == dto.getDateFrom()) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0001", "BOM", "dateFrom", "【API:bomSubstituteUpdate】"));
            }
            if (null == dto.getSubstituteValue()) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0001", "BOM", "substituteValue", "【API:bomSubstituteUpdate】"));
            }
            if (null == dto.getSubstituteUsage()) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0001", "BOM", "substituteUsage", "【API:bomSubstituteUpdate】"));
            } else {
                if (!NumberHelper.isDouble(dto.getSubstituteUsage().toString())
                                || BigDecimal.valueOf(dto.getSubstituteUsage()).compareTo(BigDecimal.ZERO) < 0) {
                    throw new MtException("MT_BOM_0064",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0064", "BOM",
                                                    dto.getSubstituteUsage().toString(), "【API:bomSubstituteUpdate】"));
                }
            }

            MtBomSubstituteGroup mtBomSubstituteGroup = mtBomSubstituteGroupRepository
                            .bomSubstituteGroupBasicGet(tenantId, dto.getBomSubstituteGroupId());
            if (null == mtBomSubstituteGroup) {
                throw new MtException("MT_BOM_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0020", "BOM", dto.getBomSubstituteGroupId(), "【API:bomSubstituteUpdate】"));
            }

            MtBomComponent mtBomComponent = mtBomComponentRepository.bomComponentBasicGet(tenantId,
                            mtBomSubstituteGroup.getBomComponentId());
            if (null == mtBomComponent) {
                throw new MtException("MT_BOM_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0015", "BOM", "【API:bomSubstituteUpdate】"));
            }

            MtBomSubstitute mtBomSubstitute = new MtBomSubstitute();
            mtBomSubstitute.setTenantId(tenantId);
            mtBomSubstitute.setBomSubstituteGroupId(dto.getBomSubstituteGroupId());
            mtBomSubstitute.setMaterialId(dto.getMaterialId());
            mtBomSubstitute = this.mtBomSubstituteMapper.selectOne(mtBomSubstitute);
            if (null != mtBomSubstitute) {
                throw new MtException("MT_BOM_0062", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0062", "BOM", "【API:bomSubstituteUpdate】"));
            }

            dto.setTenantId(tenantId);
            self().insertSelective(dto);

            MtBomHisVO1 mtBomHisVo1 = new MtBomHisVO1();
            mtBomHisVo1.setBomId(mtBomComponent.getBomId());
            mtBomHisVo1.setEventTypeCode("BOM_SUBSTITUTE_CREATE");
            MtBomHisVO4 mtBomHisVo4 = mtBomHisRepository.bomAllHisCreate(tenantId, mtBomHisVo1);

            result.setBomSubstituteId(dto.getBomSubstituteId());
            if (null == mtBomHisVo4) {
                result.setBomSubstituteHisId(Collections.emptyList());
            } else {
                result.setBomSubstituteHisId(mtBomHisVo4.getBomSubstituteHisId());
            }
        } else {
            if (null != dto.getBomSubstituteGroupId() && "".equals(dto.getBomSubstituteGroupId())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0001", "BOM", "bomSubstituteGroupId", "【API:bomSubstituteUpdate】"));
            }
            if (null != dto.getMaterialId() && "".equals(dto.getMaterialId())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0001", "BOM", "materialId", "【API:bomSubstituteUpdate】"));
            }
            if (null != dto.getSubstituteUsage()) {
                if (!NumberHelper.isDouble(dto.getSubstituteUsage().toString())
                                || BigDecimal.valueOf(dto.getSubstituteUsage()).compareTo(BigDecimal.ZERO) < 0) {
                    throw new MtException("MT_BOM_0064",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0064", "BOM",
                                                    dto.getSubstituteUsage().toString(), "【API:bomSubstituteUpdate】"));
                }
            }

            MtBomSubstitute mtBomSubstitute = bomSubstituteBasicGet(tenantId, dto.getBomSubstituteId());
            if (null == mtBomSubstitute) {
                throw new MtException("MT_BOM_0040", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0040", "BOM", "【API:bomSubstituteUpdate】"));
            }

            String bomSubstituteGroupId = null;
            if (null != dto.getBomSubstituteGroupId()) {
                bomSubstituteGroupId = dto.getBomSubstituteGroupId();
            } else {
                bomSubstituteGroupId = mtBomSubstitute.getBomSubstituteGroupId();
            }

            MtBomSubstitute existBomSubstitute = new MtBomSubstitute();
            existBomSubstitute.setTenantId(tenantId);
            existBomSubstitute.setBomSubstituteGroupId(bomSubstituteGroupId);
            existBomSubstitute.setBomSubstituteId(dto.getBomSubstituteId());
            existBomSubstitute = mtBomSubstituteMapper.selectOne(existBomSubstitute);
            if (null == existBomSubstitute) {
                throw new MtException("MT_BOM_0051", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0051", "BOM", "【API:bomSubstituteUpdate】"));
            }

            MtBomSubstituteGroup mtBomSubstituteGroup =
                            mtBomSubstituteGroupRepository.bomSubstituteGroupBasicGet(tenantId, bomSubstituteGroupId);
            if (null == mtBomSubstituteGroup) {
                throw new MtException("MT_BOM_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0020", "BOM", dto.getBomSubstituteGroupId(), "【API:bomSubstituteUpdate】"));
            }

            MtBomComponent mtBomComponent = mtBomComponentRepository.bomComponentBasicGet(tenantId,
                            mtBomSubstituteGroup.getBomComponentId());
            if (null == mtBomComponent) {
                throw new MtException("MT_BOM_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0015", "BOM", "【API:bomSubstituteUpdate】"));
            }

            existBomSubstitute = new MtBomSubstitute();
            mtBomSubstitute.setTenantId(tenantId);
            existBomSubstitute.setBomSubstituteGroupId(bomSubstituteGroupId);
            existBomSubstitute.setMaterialId(
                            null == dto.getMaterialId() ? mtBomSubstitute.getMaterialId() : dto.getMaterialId());
            existBomSubstitute = this.mtBomSubstituteMapper.selectOne(existBomSubstitute);
            if (null != existBomSubstitute
                            && !existBomSubstitute.getBomSubstituteId().equals(mtBomSubstitute.getBomSubstituteId())) {
                throw new MtException("MT_BOM_0062", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0062", "BOM", "【API:bomSubstituteUpdate】"));
            }

            mtBomSubstitute.setMaterialId(dto.getMaterialId());
            mtBomSubstitute.setDateFrom(dto.getDateFrom());
            mtBomSubstitute.setDateTo(dto.getDateTo());
            mtBomSubstitute.setSubstituteValue(dto.getSubstituteValue());
            mtBomSubstitute.setSubstituteUsage(dto.getSubstituteUsage());

            if (Y_FLAG.equals(fullUpdate)) {
                mtBomSubstitute = (MtBomSubstitute) ObjectFieldsHelper.setStringFieldsEmpty(mtBomSubstitute);
                self().updateByPrimaryKey(mtBomSubstitute);
            } else {
                self().updateByPrimaryKeySelective(mtBomSubstitute);
            }


            MtBomHisVO1 mtBomHisVo1 = new MtBomHisVO1();
            mtBomHisVo1.setBomId(mtBomComponent.getBomId());
            mtBomHisVo1.setEventTypeCode("BOM_SUBSTITUTE_UPDATE");
            MtBomHisVO4 mtBomHisVo4 = mtBomHisRepository.bomAllHisCreate(tenantId, mtBomHisVo1);

            result.setBomSubstituteId(dto.getBomSubstituteId());
            if (null == mtBomHisVo4) {
                result.setBomSubstituteHisId(Collections.emptyList());
            } else {
                result.setBomSubstituteHisId(mtBomHisVo4.getBomSubstituteHisId());
            }
        }

        return result;
    }

    @Override
    public List<String> sourceLimitTargetBomSubstituteUpdateGet(Long tenantId, String sourceBomSubstituteGroupId,
                    String targetBomSubstituteGroupId, Date now) {
        MtBomSubstitute mtBomSubstitute = new MtBomSubstitute();
        mtBomSubstitute.setTenantId(tenantId);
        mtBomSubstitute.setBomSubstituteGroupId(sourceBomSubstituteGroupId);
        List<MtBomSubstitute> sourceBomSubstitutes = mtBomSubstituteMapper.select(mtBomSubstitute);

        mtBomSubstitute = new MtBomSubstitute();
        mtBomSubstitute.setTenantId(tenantId);
        mtBomSubstitute.setBomSubstituteGroupId(targetBomSubstituteGroupId);
        List<MtBomSubstitute> targetBomSubstitutes = mtBomSubstituteMapper.select(mtBomSubstitute);

        List<String> sqlList = new ArrayList<>();
        if (CollectionUtils.isEmpty(sourceBomSubstitutes) && CollectionUtils.isEmpty(targetBomSubstitutes)) {
            return sqlList;
        }

        Long userId = DetailsHelper.getUserDetails().getUserId();

        // 来源有，目标全无，则以来源数据新增
        if (CollectionUtils.isEmpty(targetBomSubstitutes)) {
            for (MtBomSubstitute s : sourceBomSubstitutes) {
                MtBomSubstitute newBomSubstitute = new MtBomSubstitute();
                newBomSubstitute.setTenantId(tenantId);
                newBomSubstitute.setBomSubstituteId(this.customSequence.getNextKey("mt_bom_substitute_s"));
                newBomSubstitute.setBomSubstituteGroupId(targetBomSubstituteGroupId);
                newBomSubstitute.setMaterialId(s.getMaterialId());
                newBomSubstitute.setDateFrom(s.getDateFrom());
                newBomSubstitute.setDateTo(s.getDateTo());
                newBomSubstitute.setSubstituteValue(s.getSubstituteValue());
                newBomSubstitute.setSubstituteUsage(s.getSubstituteUsage());
                newBomSubstitute.setCopiedFromSubstituteId(s.getBomSubstituteId());
                newBomSubstitute.setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_substitute_cid_s")));
                newBomSubstitute.setCreatedBy(userId);
                newBomSubstitute.setCreationDate(now);
                newBomSubstitute.setLastUpdateDate(now);
                newBomSubstitute.setLastUpdatedBy(userId);
                newBomSubstitute.setObjectVersionNumber(1L);
                sqlList.addAll(MtSqlHelper.getInsertSql(newBomSubstitute));
            }
        } else if (CollectionUtils.isEmpty(sourceBomSubstitutes)) {
            // 目标有，来源全无，则目标全部失效
            for (MtBomSubstitute t : targetBomSubstitutes) {
                t.setTenantId(tenantId);
                t.setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_substitute_cid_s")));
                t.setDateTo(now);
                t.setLastUpdateDate(now);
                t.setLastUpdatedBy(userId);
                sqlList.addAll(MtSqlHelper.getUpdateSql(t));
            }
        } else {
            // 目标有，来源也有，则匹配 MATERIAL_ID 相同的数据
            Map<String, MtBomSubstitute> sourceBomSubstituteMap = sourceBomSubstitutes.stream()
                            .collect(Collectors.toMap(m -> m.getBomSubstituteId(), m -> m));

            for (MtBomSubstitute t : targetBomSubstitutes) {
                // 对每一个目标组件筛选来源
                List<MtBomSubstitute> result = sourceBomSubstitutes.stream()
                                .filter(s -> s.getMaterialId().equals(t.getMaterialId())).collect(Collectors.toList());

                // 如果无对应来源，则无效改目标
                if (CollectionUtils.isEmpty(result)) {
                    t.setTenantId(tenantId);
                    t.setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_substitute_cid_s")));
                    t.setDateTo(now);
                    t.setLastUpdateDate(now);
                    t.setLastUpdatedBy(userId);
                    sqlList.addAll(MtSqlHelper.getUpdateSql(t));
                } else {
                    MtBomSubstitute tempSource = result.get(0);

                    // 剔除来源Map数据: 筛选结果唯一
                    sourceBomSubstituteMap.remove(tempSource.getBomSubstituteId());

                    // 根据筛选出来的来源更新该目标
                    t.setTenantId(tenantId);
                    t.setDateFrom(tempSource.getDateFrom());
                    t.setDateTo(tempSource.getDateTo());
                    t.setSubstituteValue(tempSource.getSubstituteValue());
                    t.setSubstituteUsage(tempSource.getSubstituteUsage());
                    t.setCopiedFromSubstituteId(tempSource.getBomSubstituteId());
                    t.setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_substitute_cid_s")));
                    t.setLastUpdateDate(now);
                    t.setLastUpdatedBy(userId);
                    sqlList.addAll(MtSqlHelper.getUpdateSql(t));
                }
            }

            // 如果处理完所有目标后，还有来源未筛选到，则对这部分来源执行目标新增
            if (!MapUtils.isEmpty(sourceBomSubstituteMap)) {
                for (Map.Entry<String, MtBomSubstitute> entry : sourceBomSubstituteMap.entrySet()) {
                    MtBomSubstitute sourceBomSubstitute = entry.getValue();

                    MtBomSubstitute newBomSubstitute = new MtBomSubstitute();
                    newBomSubstitute.setTenantId(tenantId);
                    newBomSubstitute.setBomSubstituteId(this.customSequence.getNextKey("mt_bom_substitute_s"));
                    newBomSubstitute.setBomSubstituteGroupId(targetBomSubstituteGroupId);
                    newBomSubstitute.setMaterialId(sourceBomSubstitute.getMaterialId());
                    newBomSubstitute.setDateFrom(sourceBomSubstitute.getDateFrom());
                    newBomSubstitute.setDateTo(sourceBomSubstitute.getDateTo());
                    newBomSubstitute.setSubstituteValue(sourceBomSubstitute.getSubstituteValue());
                    newBomSubstitute.setSubstituteUsage(sourceBomSubstitute.getSubstituteUsage());
                    newBomSubstitute.setCopiedFromSubstituteId(sourceBomSubstitute.getBomSubstituteId());
                    newBomSubstitute.setCid(Long.valueOf(this.customSequence.getNextKey("mt_bom_substitute_cid_s")));
                    newBomSubstitute.setCreatedBy(userId);
                    newBomSubstitute.setCreationDate(now);
                    newBomSubstitute.setLastUpdateDate(now);
                    newBomSubstitute.setLastUpdatedBy(userId);
                    sqlList.addAll(MtSqlHelper.getInsertSql(newBomSubstitute));
                }
            }
        }
        return sqlList;
    }

    @Override
    public List<MtBomSubstituteVO5> bomSubstituteBatchGetByBomCom(Long tenantId, List<String> bomComponentIds) {
        if (CollectionUtils.isEmpty(bomComponentIds)) {
            return Collections.emptyList();
        }
        return mtBomSubstituteMapper.bomSubstituteBatchGetByBomCom(tenantId, bomComponentIds);
    }

    @Override
    public List<MtBomSubstituteVO12> bomSubstituteLimitMaterialQtyBatchCalculate(Long tenantId,
                    List<MtBomSubstituteVO11> dto) {

        List<MtBomSubstituteVO12> resultList = new ArrayList<>();
        // 校验必输参数是否为空
        if (CollectionUtils.isEmpty(dto)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "list", "BOM", "【API:bomSubstituteLimitMaterialQtyBatchCalculate】"));
        }

        if (dto.stream().anyMatch(t -> StringUtils.isEmpty(t.getBomComponentId()))) {
            throw new MtException("MT_BOM_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0001",
                                            "bomComponentId", "BOM",
                                            "【API:bomSubstituteLimitMaterialQtyBatchCalculate】"));
        }

        if (dto.stream().anyMatch(t -> StringUtils.isEmpty(t.getSubstituteMaterialId()))) {
            throw new MtException("MT_BOM_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0001", "BOM",
                                            "substituteMaterialId",
                                            "【API:bomSubstituteLimitMaterialQtyBatchCalculate】"));
        }

        if (dto.stream().anyMatch(t -> t.getQty() == null)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "qty", "BOM", "【API:bomSubstituteLimitMaterialQtyBatchCalculate】"));
        }

        List<String> bomComponentIdList =
                        dto.stream().map(MtBomSubstituteVO11::getBomComponentId).collect(Collectors.toList());
        // 调用API{ bomComponentBasicBatchGet }
        List<MtBomComponentVO13> bomComponentVO13s =
                        mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, bomComponentIdList);
        if (CollectionUtils.isEmpty(bomComponentVO13s)) {
            throw new MtException("MT_BOM_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0015", "BOM", "【API:bomSubstituteLimitMaterialQtyBatchCalculate】"));
        }


        Map<String, List<MtBomSubstituteGroupVO3>> bomSubstituteGroupMap = new HashMap<>();

        List<String> bomSubstituteGroupIds = new ArrayList<>();
        List<String> bomSubstituteIdIds = new ArrayList<>();

        // 依次调用API{bomSubstituteQuery}输入参数
        for (String bomComponentId : bomComponentIdList) {
            List<MtBomSubstituteGroupVO3> list =
                            mtBomSubstituteGroupRepository.bomSubstituteQuery(tenantId, bomComponentId);
            bomSubstituteGroupIds.addAll(list.stream().map(MtBomSubstituteGroupVO3::getBomSubstituteGroupId)
                            .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList()));
            bomSubstituteIdIds.addAll(list.stream().map(MtBomSubstituteGroupVO3::getBomSubstituteId)
                            .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList()));
            if (CollectionUtils.isEmpty(list)) {
                throw new MtException("MT_BOM_0020",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_BOM_0020", "BOM",
                                                bomComponentId, "【API:bomSubstituteLimitMaterialQtyBatchCalculate】"));
            }

            bomSubstituteGroupMap.put(bomComponentId, list);
        }


        Map<String, String> substitutePolicyMap = new HashMap<>();
        // MT_BOM_SUBSTITUTE_GROUP表获取替代策略
        if (CollectionUtils.isNotEmpty(bomSubstituteGroupIds)) {
            List<MtBomSubstituteGroup> substituteGroups = mtBomSubstituteGroupRepository
                            .bomSubstituteGroupBasicBatchGet(tenantId, bomSubstituteGroupIds);
            if (CollectionUtils.isNotEmpty(substituteGroups)) {
                substitutePolicyMap = substituteGroups.stream()
                                .collect(Collectors.toMap(MtBomSubstituteGroup::getBomSubstituteGroupId,
                                                MtBomSubstituteGroup::getSubstitutePolicy));
            }
        }

        // 在MT_BOM_SUBSTITUTE表获取替代值
        Map<String, MtBomSubstitute> substituteMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(bomSubstituteIdIds)) {
            List<MtBomSubstitute> mtBomSubstitutes = this.bomSubstituteBasicBatchGet(tenantId, bomSubstituteIdIds);
            if (CollectionUtils.isNotEmpty(mtBomSubstitutes)) {
                substituteMap = mtBomSubstitutes.stream()
                                .collect(Collectors.toMap(MtBomSubstitute::getBomSubstituteId, t -> t));
            }
        }

        Map<String, List<MtBomSubstituteVO11>> resultMap =
                        dto.stream().collect(Collectors.groupingBy(MtBomSubstituteVO11::getBomComponentId));


        for (Map.Entry<String, List<MtBomSubstituteVO11>> c : resultMap.entrySet()) {

            MtBomSubstituteVO12 result = new MtBomSubstituteVO12();
            result.setBomComponentId(c.getKey());

            List<MtBomSubstituteVO13> substituteList = new ArrayList<>();

            for (MtBomSubstituteVO11 substitute : c.getValue()) {
                List<MtBomSubstituteGroupVO3> substituteGroups =
                                bomSubstituteGroupMap.get(substitute.getBomComponentId());

                Map<String, String> finalSubstitutePolicyMap = substitutePolicyMap;
                Map<String, MtBomSubstitute> finalSubstituteMap = substituteMap;
                if (CollectionUtils.isEmpty(substituteGroups)) {
                    throw new MtException("MT_BOM_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_BOM_0037", "BOM", "【API:bomSubstituteLimitMaterialQtyBatchCalculate】"));
                }

                if (substituteGroups.stream()
                                .noneMatch(t -> finalSubstituteMap.get(t.getBomSubstituteId()) != null
                                                && finalSubstituteMap.get(t.getBomSubstituteId()).getMaterialId()
                                                                .equals(substitute.getSubstituteMaterialId()))) {
                    throw new MtException("MT_BOM_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_BOM_0037", "BOM", "【API:bomSubstituteLimitMaterialQtyBatchCalculate】"));
                }

                if (substituteGroups.stream().anyMatch(
                                t -> StringUtils.isEmpty(finalSubstitutePolicyMap.get(t.getBomSubstituteGroupId())))) {
                    throw new MtException("MT_BOM_0021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_BOM_0021", "BOM", "【API:bomSubstituteLimitMaterialQtyBatchCalculate】"));
                }

                List<MtBomSubstituteVO13> substitutes = substituteGroups.stream()
                                .filter(t -> finalSubstituteMap.get(t.getBomSubstituteId()) != null
                                                && finalSubstituteMap.get(t.getBomSubstituteId()).getMaterialId()
                                                                .equals(substitute.getSubstituteMaterialId()))
                                .map(t -> {
                                    MtBomSubstituteVO13 substituteVO13 = new MtBomSubstituteVO13();

                                    BigDecimal componentQty = BigDecimal.ZERO;

                                    String policy = finalSubstitutePolicyMap.get(t.getBomSubstituteGroupId());


                                    substituteVO13.setSubstitutePolicy(policy);
                                    MtBomSubstitute bomSubstitute = finalSubstituteMap.get(t.getBomSubstituteId());

                                    if (bomSubstitute != null) {
                                        if (CHANCE.equals(policy)) {
                                            componentQty = BigDecimal.valueOf(substitute.getQty())
                                                            .divide(BigDecimal.valueOf(
                                                                            bomSubstitute.getSubstituteUsage()), 6,
                                                                            BigDecimal.ROUND_HALF_DOWN)
                                                            .multiply(BigDecimal.valueOf(100).divide(
                                                                            BigDecimal.valueOf(bomSubstitute
                                                                                            .getSubstituteValue()),
                                                                            6, BigDecimal.ROUND_HALF_DOWN));

                                        } else if (PRIORITY.equals(policy)) {
                                            componentQty = BigDecimal.valueOf(substitute.getQty()).divide(
                                                            BigDecimal.valueOf(bomSubstitute.getSubstituteUsage()), 6,
                                                            BigDecimal.ROUND_HALF_DOWN);
                                        }
                                        substituteVO13.setSubstituteMaterialId(bomSubstitute.getMaterialId());
                                    }
                                    substituteVO13.setComponentQty(componentQty.doubleValue());
                                    return substituteVO13;
                                }).collect(Collectors.toList());
                substituteList.addAll(substitutes);
            }

            result.setSubstituteList(substituteList);
            resultList.add(result);
        }
        return resultList;
    }

    @Override
    public List<MtBomSubstituteVO17> bomSubstituteQtyBatchCalculate(Long tenantId, MtBomSubstituteVO15 dto) {
        if (dto == null || CollectionUtils.isEmpty(dto.getBomComponentList()) || dto.getBomComponentList().stream()
                        .anyMatch(c -> MtIdHelper.isIdNull(c.getBomComponentId()))) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomComponentId", "【API:bomSubstituteQtyBatchCalculate】"));
        }
        if (dto.getBomComponentList().stream().anyMatch(c -> c.getQty() == null)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "qty", "【API:bomSubstituteQtyBatchCalculate】"));
        }

        List<String> bomComponentIds = new ArrayList<>();
        for (MtBomSubstituteVO14 vo : dto.getBomComponentList()) {
            if (MtIdHelper.isIdNull(vo.getBomComponentMaterialId())
                            && !bomComponentIds.contains(vo.getBomComponentId())) {
                bomComponentIds.add(vo.getBomComponentId());
            }
        }

        if (CollectionUtils.isNotEmpty(bomComponentIds)) {
            List<MtBomComponent> mtBomComponents =
                            mtBomComponentRepository.selectBomComponentByBomComponentIds(tenantId, bomComponentIds);
            for (MtBomComponent mtBomComponent : mtBomComponents) {
                List<MtBomSubstituteVO14> filterList = dto.getBomComponentList().stream()
                                .filter(c -> MtIdHelper.isIdNull(c.getBomComponentMaterialId())
                                                && c.getBomComponentId().equals(mtBomComponent.getBomComponentId()))
                                .collect(Collectors.toList());
                for (MtBomSubstituteVO14 vo : filterList) {
                    vo.setBomComponentMaterialId(mtBomComponent.getMaterialId());
                }
            }
        }

        List<MtBomSubstituteVO17> result = new ArrayList<>();
        bomComponentIds = dto.getBomComponentList().stream().map(MtBomSubstituteVO14::getBomComponentId).distinct()
                        .collect(Collectors.toList());
        List<MtBomSubstituteGroupVO7> mtBomSubstituteGroupVO7s =
                        this.mtBomSubstituteGroupRepository.bomSubstituteBatchQuery(tenantId, bomComponentIds);
        Map<String, MtBomSubstituteGroupVO7> mtBomSubstituteGroupMap = mtBomSubstituteGroupVO7s.stream()
                        .collect(Collectors.toMap(MtBomSubstituteGroupVO7::getBomComponentId, c -> c));

        for (MtBomSubstituteVO14 mtBomSubstituteVO14 : dto.getBomComponentList()) {
            MtBomSubstituteGroupVO7 mtBomSubstituteGroupVO7 =
                            mtBomSubstituteGroupMap.get(mtBomSubstituteVO14.getBomComponentId());
            if (mtBomSubstituteGroupVO7 == null) {
                MtBomSubstituteVO17 mtBomSubstituteVO17 = new MtBomSubstituteVO17();
                mtBomSubstituteVO17.setBomComponentId(mtBomSubstituteVO14.getBomComponentId());
                mtBomSubstituteVO17.setQty(mtBomSubstituteVO14.getQty());
                mtBomSubstituteVO17.setSubstituteFlag("N");

                List<MtBomSubstituteVO16> substituteMaterialList = new ArrayList<>();
                MtBomSubstituteVO16 mtBomSubstituteVO16 = new MtBomSubstituteVO16();
                mtBomSubstituteVO16.setComponentQty(mtBomSubstituteVO14.getQty());
                mtBomSubstituteVO16.setMaterialId(mtBomSubstituteVO14.getBomComponentMaterialId());
                mtBomSubstituteVO16.setPriority(null);
                substituteMaterialList.add(mtBomSubstituteVO16);
                mtBomSubstituteVO17.setSubstituteMaterialList(substituteMaterialList);
                result.add(mtBomSubstituteVO17);
            } else {
                BigDecimal qty = BigDecimal.valueOf(mtBomSubstituteVO14.getQty());
                MtBomSubstituteVO17 mtBomSubstituteVO17 = new MtBomSubstituteVO17();
                mtBomSubstituteVO17.setBomComponentId(mtBomSubstituteVO14.getBomComponentId());
                mtBomSubstituteVO17.setQty(mtBomSubstituteVO14.getQty());
                mtBomSubstituteVO17.setSubstituteFlag("Y");

                List<MtBomSubstituteVO16> substituteMaterialList = new ArrayList<>();
                mtBomSubstituteVO17.setSubstituteMaterialList(substituteMaterialList);
                result.add(mtBomSubstituteVO17);

                String substitutePolicy = mtBomSubstituteGroupVO7.getSubstitutePolicy();
                if (StringUtils.isEmpty(substitutePolicy) || MtBaseConstants.NO.equals(dto.getSubstituteFlag())) {
                    mtBomSubstituteVO17.setSubstituteFlag("N");
                    if (CollectionUtils.isEmpty(dto.getMaterialIds())) {
                        MtBomSubstituteVO16 mtBomSubstituteVO16 = new MtBomSubstituteVO16();
                        mtBomSubstituteVO16.setMaterialId(mtBomSubstituteVO14.getBomComponentMaterialId());
                        mtBomSubstituteVO16.setPriority(null);
                        mtBomSubstituteVO16.setComponentQty(qty.doubleValue());
                        substituteMaterialList.add(mtBomSubstituteVO16);
                    } else {
                        if (dto.getMaterialIds().contains(mtBomSubstituteVO14.getBomComponentMaterialId())) {
                            MtBomSubstituteVO16 mtBomSubstituteVO16 = new MtBomSubstituteVO16();
                            mtBomSubstituteVO16.setMaterialId(mtBomSubstituteVO14.getBomComponentMaterialId());
                            mtBomSubstituteVO16.setPriority(null);
                            mtBomSubstituteVO16.setComponentQty(qty.doubleValue());
                            substituteMaterialList.add(mtBomSubstituteVO16);
                        }
                    }
                } else if (!MtBaseConstants.NO.equals(dto.getSubstituteFlag())
                                && MtBaseConstants.SUBSTITUTE_POLICY.CHANCE.equals(substitutePolicy)) {
                    for (MtBomSubstituteGroupVO8 substituteGroupVO8 : mtBomSubstituteGroupVO7.getBomSubstituteList()) {
                        if (CollectionUtils.isEmpty(dto.getMaterialIds())) {
                            MtBomSubstituteVO16 mtBomSubstituteVO16 = new MtBomSubstituteVO16();
                            BigDecimal substituteValue = substituteGroupVO8.getSubstituteValue() == null
                                            ? BigDecimal.ZERO
                                            : BigDecimal.valueOf(substituteGroupVO8.getSubstituteValue());
                            BigDecimal substituteUsage = substituteGroupVO8.getSubstituteUsage() == null
                                            ? BigDecimal.ZERO
                                            : BigDecimal.valueOf(substituteGroupVO8.getSubstituteUsage());
                            BigDecimal tmpCalcQty = substituteValue.divide(new BigDecimal("100"), 6,
                                            BigDecimal.ROUND_HALF_DOWN);
                            BigDecimal componentQty = qty.multiply(tmpCalcQty).multiply(substituteUsage);

                            mtBomSubstituteVO16.setMaterialId(substituteGroupVO8.getMaterialId());
                            mtBomSubstituteVO16.setComponentQty(
                                            componentQty.setScale(6, BigDecimal.ROUND_HALF_DOWN).doubleValue());
                            substituteMaterialList.add(mtBomSubstituteVO16);
                        } else {
                            if (dto.getMaterialIds().contains(substituteGroupVO8.getMaterialId())) {
                                MtBomSubstituteVO16 mtBomSubstituteVO16 = new MtBomSubstituteVO16();
                                BigDecimal substituteValue = substituteGroupVO8.getSubstituteValue() == null
                                                ? BigDecimal.ZERO
                                                : BigDecimal.valueOf(substituteGroupVO8.getSubstituteValue());
                                BigDecimal substituteUsage = substituteGroupVO8.getSubstituteUsage() == null
                                                ? BigDecimal.ZERO
                                                : BigDecimal.valueOf(substituteGroupVO8.getSubstituteUsage());
                                BigDecimal tmpCalcQty = substituteValue.divide(new BigDecimal("100"), 6,
                                                BigDecimal.ROUND_HALF_DOWN);
                                BigDecimal componentQty = qty.multiply(tmpCalcQty).multiply(substituteUsage);

                                mtBomSubstituteVO16.setMaterialId(substituteGroupVO8.getMaterialId());
                                mtBomSubstituteVO16.setComponentQty(
                                                componentQty.setScale(6, BigDecimal.ROUND_HALF_DOWN).doubleValue());
                                substituteMaterialList.add(mtBomSubstituteVO16);
                            }
                        }
                    }
                } else if (!MtBaseConstants.NO.equals(dto.getSubstituteFlag())
                                && MtBaseConstants.SUBSTITUTE_POLICY.PRIORITY.equals(substitutePolicy)) {
                    // return substitute which has min substitute-value in bom substitute list
                    List<MtBomSubstituteGroupVO8> substituteGroupList = mtBomSubstituteGroupVO7.getBomSubstituteList();
                    if (CollectionUtils.isNotEmpty(dto.getMaterialIds())) {
                        substituteGroupList = substituteGroupList.stream()
                                        .filter(t -> dto.getMaterialIds().contains(t.getMaterialId()))
                                        .collect(Collectors.toList());
                    }

                    Optional<MtBomSubstituteGroupVO8> substituteGroupOpt = substituteGroupList.stream()
                                    .min(Comparator.comparing(MtBomSubstituteGroupVO8::getSubstituteValue));
                    if (substituteGroupOpt.isPresent()) {
                        MtBomSubstituteVO16 mtBomSubstituteVO16 = new MtBomSubstituteVO16();
                        mtBomSubstituteVO16.setMaterialId(substituteGroupOpt.get().getMaterialId());
                        mtBomSubstituteVO16.setPriority(substituteGroupOpt.get().getSubstituteValue());
                        BigDecimal substituteUsage = substituteGroupOpt.get().getSubstituteUsage() == null
                                        ? BigDecimal.ZERO
                                        : BigDecimal.valueOf(substituteGroupOpt.get().getSubstituteUsage());
                        BigDecimal componentQty = qty.multiply(substituteUsage);
                        mtBomSubstituteVO16.setComponentQty(
                                        componentQty.setScale(6, BigDecimal.ROUND_HALF_DOWN).doubleValue());
                        substituteMaterialList.add(mtBomSubstituteVO16);
                    }
                } else {
                    throw new MtException("MT_BOM_0021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_BOM_0021", "BOM", "【API:bomSubstituteQtyBatchCalculate】"));
                }
            }
        }
        return result;
    }

}
