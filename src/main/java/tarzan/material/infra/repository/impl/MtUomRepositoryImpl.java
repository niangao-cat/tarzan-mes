package tarzan.material.infra.repository.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.StringHelper;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.*;
import tarzan.material.infra.mapper.MtMaterialMapper;
import tarzan.material.infra.mapper.MtUomMapper;

/**
 * 单位 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:10:08
 */
@Component
public class MtUomRepositoryImpl extends BaseRepositoryImpl<MtUom> implements MtUomRepository {


    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtUomMapper mtUomMapper;

    @Autowired
    private MtMaterialMapper materialMapper;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Override
    public MtUomVO uomPropertyGet(Long tenantId, String uomId) {
        if (StringUtils.isEmpty(uomId)) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "uomId", "【API:uomPropertyGet】"));
        }
        MtUomVO uomVO = new MtUomVO();
        uomVO.setUomId(uomId);
        List<MtUomVO> result = mtUomMapper.selectOneView(tenantId, uomVO);
        if (CollectionUtils.isEmpty(result)) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public List<MtUomVO> propertyLimitUomQuery(Long tenantId, MtUomVO dto) {
        dto.setTenantId(tenantId);
        return mtUomMapper.selectOneView(tenantId, dto);
    }

    @Override
    public MtUomVO1 uomDecimalProcess(Long tenantId, MtUomVO1 dto) {
        // 基本校验
        if (StringUtils.isEmpty(dto.getSourceUomId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "sourceUomId", "【API:uomDecimalProcess】"));
        }
        if (dto.getSourceValue() == null) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "sourceValue", "【API:uomDecimalProcess】"));
        }

        MtUom source = new MtUom();
        source.setTenantId(tenantId);
        source.setUomId(dto.getSourceUomId());
        source = mtUomMapper.selectOne(source);
        if (source == null) {
            // 单位编码不存在报错
            throw new MtException("MT_MATERIAL_0049", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0049", "MATERIAL", "【API:uomDecimalProcess】"));
        } else {

            BigDecimal bg;
            double data;

            switch (source.getProcessMode()) {
                case "ROUND_UP":
                    bg = new BigDecimal(dto.getSourceValue().toString());
                    data = bg.setScale(source.getDecimalNumber().intValue(), BigDecimal.ROUND_UP).doubleValue();
                    dto.setTargetValue(data);
                    break;
                case "ROUND":
                    bg = new BigDecimal(dto.getSourceValue().toString());
                    data = bg.setScale(source.getDecimalNumber().intValue(), BigDecimal.ROUND_HALF_UP).doubleValue();
                    dto.setTargetValue(data);
                    break;
                case "ROUND_DOWN":
                    bg = new BigDecimal(dto.getSourceValue().toString());
                    data = bg.setScale(source.getDecimalNumber().intValue(), BigDecimal.ROUND_DOWN).doubleValue();
                    dto.setTargetValue(data);
                    break;
                default:
                    throw new RuntimeException("Error Process Mode Type.");
            }
            return dto;
        }
    }

    @Override
    public MtUomVO1 uomConversion(Long tenantId, MtUomVO1 dto) {
        // 基本校验

        if (dto.getSourceValue() == null) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "sourceValue", "【API:uomConversion】"));
        }

        if (StringUtils.isEmpty(dto.getSourceUomId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "sourceUomId", "【API:uomConversion】"));
        }
        if (StringUtils.isEmpty(dto.getTargetUomId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "targetUomId", "【API:uomConversion】"));
        }

        // 批量查询：来源单位和目标单位信息
        List<MtUom> mtUomList = mtUomMapper.selectByCondition(Condition.builder(MtUom.class)
                        .andWhere(Sqls.custom().andEqualTo(MtUom.FIELD_TENANT_ID, tenantId).andIn(MtUom.FIELD_UOM_ID,
                                        Arrays.asList(dto.getSourceUomId(), dto.getTargetUomId())))
                        .build());

        Optional<MtUom> sourceUom =
                        mtUomList.stream().filter(t -> t.getUomId().equals(dto.getSourceUomId())).findFirst();
        if (!sourceUom.isPresent()) {
            throw new MtException("MT_MATERIAL_0050", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0050", "MATERIAL", "【API:uomConversion】"));
        }

        Optional<MtUom> targetUom =
                        mtUomList.stream().filter(t -> t.getUomId().equals(dto.getSourceUomId())).findFirst();
        if (!targetUom.isPresent()) {
            throw new MtException("MT_MATERIAL_0051", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0051", "MATERIAL", "【API:uomConversion】"));
        }

        MtUom source = sourceUom.get();
        MtUom target = targetUom.get();

        if (!source.getUomType().equals(target.getUomType())) {
            throw new MtException("MT_MATERIAL_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0001", "MATERIAL", "【API:uomConversion】"));
        }

        // 执行换算
        double data = new BigDecimal(dto.getSourceValue().toString())
                        .divide(new BigDecimal(source.getConversionValue().toString()), 10, BigDecimal.ROUND_HALF_DOWN)
                        .multiply(new BigDecimal(target.getConversionValue().toString())).doubleValue();
        MtUomVO1 uomVO1 = new MtUomVO1();
        uomVO1.setSourceUomId(target.getUomId());
        uomVO1.setSourceValue(data);
        uomVO1 = uomDecimalProcess(tenantId, uomVO1);
        dto.setTargetValue(uomVO1.getTargetValue());
        return dto;
    }

    @Override
    public MtUomVO2 primaryUomGet(Long tenantId, String uomId) {

        MtUomVO2 dto = new MtUomVO2();
        dto.setUomId(uomId);

        if (StringUtils.isEmpty(uomId)) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "uomId", "【API:primaryUomGet】"));
        }
        MtUom uom = new MtUom();
        uom.setTenantId(tenantId);
        uom.setUomId(uomId);
        uom = mtUomMapper.selectOne(uom);
        if (uom == null) {
            throw new MtException("MT_MATERIAL_0052", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0052", "MATERIAL", "【API:primaryUomGet】"));

        } else {
            MtUom primaryUom = new MtUom();
            primaryUom.setTenantId(tenantId);
            primaryUom.setUomType(uom.getUomType());
            primaryUom.setPrimaryFlag("Y");
            List<MtUom> primaryUomLists = mtUomMapper.select(primaryUom);

            if (CollectionUtils.isEmpty(primaryUomLists)) {
                throw new MtException("MT_MATERIAL_0002", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0002", "MATERIAL", uom.getUomCode(), "【API:primaryUomGet】"));

            } else if (primaryUomLists.size() > 1) {

                throw new MtException("MT_MATERIAL_0003", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0002", "MATERIAL", uom.getUomCode(), "【API:primaryUomGet】"));

            } else {
                dto.setTargetUomId(primaryUomLists.get(0).getUomId());
                return dto;
            }
        }
    }

    @Override
    public List<MtUom> sameTypeUomQuery(Long tenantId, String sourceUomId) {

        if (StringUtils.isEmpty(sourceUomId)) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "sourceUomId", "【API:sameTypeUomQuery】"));
        }

        MtUom uom = new MtUom();
        uom.setTenantId(tenantId);
        uom.setUomId(sourceUomId);
        uom = mtUomMapper.selectOne(uom);
        if (uom == null) {
            throw new MtException("MT_MATERIAL_0052", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0052", "MATERIAL", "【API:sameTypeUomQuery】"));
        }

        MtUom uom1 = new MtUom();
        uom1.setTenantId(tenantId);
        uom1.setUomType(uom.getUomType());
        uom1.setEnableFlag("Y");
        return mtUomMapper.select(uom1);
    }

    @Override
    public MtUomVO3 materialUomConversion(Long tenantId, MtUomVO3 dto) {

        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "materialId", "【API:materialUomConversion】"));
        }
        if (dto.getSourceValue() == null) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "sourceValue", "【API:materialUomConversion】"));
        }
        if (StringUtils.isEmpty(dto.getSourceUomId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "sourceUomId", "【API:materialUomConversion】"));
        }

        MtMaterial item = new MtMaterial();
        item.setTenantId(tenantId);
        item.setMaterialId(dto.getMaterialId());
        item = materialMapper.selectOne(item);

        if (item == null) {
            throw new MtException("MT_MATERIAL_0053", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0053", "MATERIAL", "【API:materialUomConversion】"));
        }
        if (StringUtils.isEmpty(item.getSecondaryUomId())) {
            dto.setTargetValue(null);
            return dto;
        }
        if (item.getConversionRate() == null) {
            throw new MtException("MT_MATERIAL_0006", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0006", "MATERIAL", "【API:materialUomConversion】"));
        }

        String tempUom = null;
        double tempRate = item.getConversionRate();
        if (item.getPrimaryUomId().equals(dto.getSourceUomId())) {
            tempUom = item.getSecondaryUomId();
            tempRate = dto.getSourceValue() * tempRate;
        } else if (item.getSecondaryUomId().equals(dto.getSourceUomId())) {
            tempUom = item.getPrimaryUomId();
            tempRate = dto.getSourceValue() / tempRate;
        } else {
            throw new MtException("MT_MATERIAL_0007", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0007", "MATERIAL", "【API:materialUomConversion】"));
        }
        MtUomVO1 uomVO1 = new MtUomVO1();
        uomVO1.setSourceUomId(tempUom);
        uomVO1.setSourceValue(tempRate);
        MtUomVO1 uomVO2 = uomDecimalProcess(tenantId, uomVO1);
        dto.setTargetValue(uomVO2.getTargetValue());
        return dto;
    }

    @Override
    public List<MtUomVO> uomPropertyBatchGet(Long tenantId, List<String> uomIds) {
        if (CollectionUtils.isEmpty(uomIds)) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "uomId", "【API:uomPropertyBatchGet】"));
        }
        return this.mtUomMapper.selectByIdsCustom(tenantId, uomIds);
    }

    @Override
    public List<MtUomVO5> propertyLimitUomPropertyQuery(Long tenantId, MtUomVO4 dto) {
        // 查询数据
        List<MtUomVO5> voList = mtUomMapper.selectCondition(tenantId, dto);
        if (CollectionUtils.isEmpty(voList)) {
            return Collections.emptyList();
        }
        // 根据第获取到的uomType列表，调用API{groupLimitTypeQuery}
        MtGenTypeVO2 queryType = new MtGenTypeVO2();
        queryType.setModule("MATERIAL");
        queryType.setTypeGroup("UOM_TYPE");
        Map<String, String> uomTypeDesc = new HashMap<>(0);
        List<MtGenType> uomTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, queryType);

        if (CollectionUtils.isNotEmpty(uomTypes)) {
            uomTypeDesc = uomTypes.stream()
                            .collect(Collectors.toMap(MtGenType::getTypeCode, MtGenType::getDescription));
        }
        queryType = new MtGenTypeVO2();
        queryType.setModule("MATERIAL");
        queryType.setTypeGroup("DECIMAL_PROCESS_MODE");
        Map<String, String> processModeDesc = new HashMap<>(0);
        List<MtGenType> processModes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, queryType);
        if (CollectionUtils.isNotEmpty(processModes)) {
            processModeDesc = processModes.stream()
                            .collect(Collectors.toMap(MtGenType::getTypeCode, MtGenType::getDescription));
        }
        for (MtUomVO5 vo5 : voList) {
            vo5.setUomTypeDesc(uomTypeDesc.get(vo5.getUomType()));
            vo5.setProcessModeDesc(processModeDesc.get(vo5.getProcessMode()));
        }
        voList.sort(Comparator.comparingDouble(
                        (MtUomVO5 t) -> Double.valueOf(StringUtils.isEmpty(t.getUomId()) ? "0" : t.getUomId())));
        return voList;
    }

    /**
     * 根据单位编码，批量获取单位信息
     *
     * @author chuang.yang
     * @date 2019/11/18
     * @param tenantId
     * @param uomCodes
     * @return java.util.List<tarzan.material.domain.entity.MtUom>
     */
    @Override
    public List<MtUom> uomPropertyBatchGetByCodes(Long tenantId, List<String> uomCodes) {
        if (CollectionUtils.isEmpty(uomCodes)) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "uomId", "【API:uomPropertyBatchGetByCodes】"));
        }
        String whereInValuesSql = StringHelper.getWhereInValuesSql("tb.UOM_CODE", uomCodes, 1000);
        return this.mtUomMapper.selectByUomCode(tenantId, whereInValuesSql);
    }

    /**
     * uomAttrPropertyUpdate-单位新增&更新扩展表属性
     *
     * @author chuang.yang
     * @date 2019/11/20
     * @param tenantId
     * @param dto
     * @return void
     */
    @Override
    public void uomAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "keyId", "【API:uomAttrPropertyUpdate】"));
        }

        // 获取主表数据
        MtUom mtUom = mtUomMapper.selectByPrimaryKey(dto.getKeyId());
        if (mtUom == null || StringUtils.isEmpty(mtUom.getUomId())) {
            throw new MtException("MT_MATERIAL_0093",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0093", "MATERIAL",
                                            "keyId:" + dto.getKeyId(), "mt_uom", "【API:uomAttrPropertyUpdate】"));
        }

        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_uom_attr", dto.getKeyId(), dto.getEventId(),
                        dto.getAttrs());
    }

    @Override
    public List<MtUomVO8> uomDecimalBatchProcess(Long tenantId, List<MtUomVO7> uomList) {

        // 参数非空校验
        Optional<MtUomVO7> emptySourceUomId =
                        uomList.stream().filter(t -> StringUtils.isEmpty(t.getSourceUomId())).findAny();
        Optional<MtUomVO7> emptySourceValue = uomList.stream().filter(t -> t.getSourceValue() == null).findAny();

        if (emptySourceUomId.isPresent()) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "sourceUomId", "【uomDecimalBatchProcess】"));
        }

        if (emptySourceValue.isPresent()) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "sourceValue", "【uomDecimalBatchProcess】"));
        }

        List<String> uomIds = uomList.stream().map(MtUomVO7::getSourceUomId).distinct().collect(Collectors.toList());
        List<MtUomVO> sourceUomList = uomPropertyBatchGet(tenantId, uomIds);
        Map<String, MtUomVO> sourceUomMap = sourceUomList.stream().collect(Collectors.toMap(MtUom::getUomId, t -> t));

        List<MtUomVO8> result = Collections.synchronizedList(new ArrayList<>(uomList.size()));
        for (MtUomVO7 mtUomVO7 : uomList) {

            // 若输入的单位ID获取到的值为空，返回报错消息
            MtUomVO source = sourceUomMap.get(mtUomVO7.getSourceUomId());
            if (source == null) {
                throw new MtException("MT_MATERIAL_0049", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0049", "MATERIAL", "【uomDecimalBatchProcess】"));
            }

            MtUomVO8 dto = new MtUomVO8();
            dto.setSourceUomId(mtUomVO7.getSourceUomId());
            dto.setSourceValue(mtUomVO7.getSourceValue());

            BigDecimal bg;
            double data;

            switch (source.getProcessMode()) {
                case "ROUND_UP":
                    bg = new BigDecimal(dto.getSourceValue().toString());
                    data = bg.setScale(source.getDecimalNumber().intValue(), BigDecimal.ROUND_UP).doubleValue();
                    dto.setTargetValue(data);
                    break;
                case "ROUND":
                    bg = new BigDecimal(dto.getSourceValue().toString());
                    data = bg.setScale(source.getDecimalNumber().intValue(), BigDecimal.ROUND_HALF_UP).doubleValue();
                    dto.setTargetValue(data);
                    break;
                case "ROUND_DOWN":
                    bg = new BigDecimal(dto.getSourceValue().toString());
                    data = bg.setScale(source.getDecimalNumber().intValue(), BigDecimal.ROUND_DOWN).doubleValue();
                    dto.setTargetValue(data);
                    break;
                default:
                    throw new RuntimeException("Error Process Mode Type.");
            }
            result.add(dto);
        }

        return result;
    }
}
