package tarzan.material.infra.repository.impl;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.StringHelper;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtMaterialVO1;
import tarzan.material.domain.vo.MtMaterialVO2;
import tarzan.material.domain.vo.MtMaterialVO4;
import tarzan.material.domain.vo.MtMaterialVO5;
import tarzan.material.domain.vo.MtUomVO;
import tarzan.material.infra.mapper.MtMaterialMapper;

/**
 * 物料基础属性 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
@Component
public class MtMaterialRepositoryImpl extends BaseRepositoryImpl<MtMaterial> implements MtMaterialRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtUomRepository mtUomRepo;

    @Autowired
    private MtMaterialMapper mtMaterialMapper;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;


    @Override
    public MtMaterialVO materialPropertyGet(Long tenantId, String materialId) {
        if (StringUtils.isEmpty(materialId)) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "materialId", "【API:materialPropertyGet】"));
        }
        MtMaterialVO materialVO = new MtMaterialVO();
        materialVO.setMaterialId(materialId);
        List<MtMaterialVO> ls = mtMaterialMapper.selectMaterialView(tenantId, materialVO);

        if (CollectionUtils.isEmpty(ls)) {
            return null;
        } else {
            return ls.get(0);
        }
    }

    @Override
    public List<String> nameLimitMaterialQuery(Long tenantId, MtMaterial dto) {
        MtMaterialVO tmp = new MtMaterialVO();
        tmp.setMaterialCode(dto.getMaterialCode());
        tmp.setMaterialName(dto.getMaterialName());
        tmp.setMaterialDesignCode(dto.getMaterialDesignCode());
        tmp.setMaterialIdentifyCode(dto.getMaterialIdentifyCode());
        tmp.setEnableFlag("Y");
        List<String> list = propertyLimitMaterialQuery(tenantId, tmp);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list;
    }

    @Override
    public List<String> uomLimitMaterialQuery(Long tenantId, MtMaterialVO dto) {
        MtMaterialVO materialVO = new MtMaterialVO();
        materialVO.setPrimaryUomId(dto.getPrimaryUomId());
        materialVO.setPrimaryUomCode(dto.getPrimaryUomCode());
        materialVO.setSecondaryUomId(dto.getSecondaryUomId());
        materialVO.setSecondaryUomCode(dto.getSecondaryUomCode());
        materialVO.setEnableFlag("Y");
        List<MtMaterialVO> list = mtMaterialMapper.selectMaterialView(tenantId, materialVO);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(MtMaterialVO::getMaterialId).collect(toList());
    }

    @Override
    public List<String> propertyLimitMaterialQuery(Long tenantId, MtMaterialVO dto) {
        MtMaterialVO materialVO = new MtMaterialVO();
        materialVO.setMaterialCode(dto.getMaterialCode());
        materialVO.setMaterialName(dto.getMaterialName());
        materialVO.setMaterialDesignCode(dto.getMaterialDesignCode());
        materialVO.setMaterialIdentifyCode(dto.getMaterialIdentifyCode());
        materialVO.setModel(dto.getModel());
        materialVO.setPrimaryUomId(dto.getPrimaryUomId());
        materialVO.setPrimaryUomCode(dto.getPrimaryUomCode());
        materialVO.setSecondaryUomCode(dto.getSecondaryUomCode());
        materialVO.setSecondaryUomId(dto.getSecondaryUomId());
        materialVO.setEnableFlag(dto.getEnableFlag());

        return mtMaterialMapper.selectMaterialView(tenantId, materialVO).stream().map(MtMaterialVO::getMaterialId)
                        .collect(toList());
    }

    @Override
    public String materialDualUomValidate(Long tenantId, String materialId) {
        if (StringUtils.isEmpty(materialId)) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "materialId", "【API:materialDualUomValidate】"));
        }
        MtMaterial material = new MtMaterial();
        material.setTenantId(tenantId);
        material.setMaterialId(materialId);
        material = mtMaterialMapper.selectOne(material);
        if (material == null) {
            throw new MtException("MT_MATERIAL_0053", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0053", "MATERIAL", "【API:materialDualUomValidate】"));
        }
        return StringUtils.isEmpty(material.getSecondaryUomId()) ? "N" : "Y";
    }

    @Override
    public List<MtMaterialVO> materialPropertyBatchGet(Long tenantId, List<String> materialIds) {
        if (CollectionUtils.isEmpty(materialIds)) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "materialId", "【API:materialPropertyBatchGet】"));
        }
        return this.mtMaterialMapper.selectMaterialByIds(tenantId, materialIds);
    }

    @Override
    public MtMaterialVO1 materialUomGet(Long tenantId, String materialId) {
        if (StringUtils.isEmpty(materialId)) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "materialId", "【API:materialUomGet】"));
        }

        MtMaterialVO mtMaterialVO = self().materialPropertyGet(tenantId, materialId);
        if (mtMaterialVO != null && StringUtils.isNotEmpty(mtMaterialVO.getMaterialId())) {
            MtMaterialVO1 result = new MtMaterialVO1();
            result.setPrimaryUomId(mtMaterialVO.getPrimaryUomId());
            result.setPrimaryUomCode(mtMaterialVO.getPrimaryUomCode());
            result.setPrimaryUomName(mtMaterialVO.getPrimaryUomName());
            result.setSecondaryUomId(mtMaterialVO.getSecondaryUomId());
            result.setSecondaryUomCode(mtMaterialVO.getSecondaryUomCode());
            result.setSecondaryUomName(mtMaterialVO.getSecondaryUomName());
            result.setConversionRate(mtMaterialVO.getConversionRate());
            return result;
        }
        return null;
    }

    @Override
    public List<MtMaterialVO1> materialUomBatchGet(Long tenantId, List<String> materialId) {
        if (CollectionUtils.isEmpty(materialId)) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "materialId", "【API:materialUomBatchGet】"));
        }
        // 根据物料id批量查找
        List<MtMaterialVO> mtMaterialVOS = self().materialPropertyBatchGet(tenantId, materialId);
        if (CollectionUtils.isEmpty(mtMaterialVOS)) {
            return Collections.emptyList();
        }

        // 组装数据
        List<MtMaterialVO1> resultList = mtMaterialVOS.stream().map(t -> {
            MtMaterialVO1 result = new MtMaterialVO1();
            result.setMaterialId(t.getMaterialId());
            result.setPrimaryUomId(t.getPrimaryUomId());
            result.setPrimaryUomCode(t.getPrimaryUomCode());
            result.setPrimaryUomName(t.getPrimaryUomName());
            result.setSecondaryUomId(t.getSecondaryUomId());
            result.setSecondaryUomCode(t.getSecondaryUomCode());
            result.setSecondaryUomName(t.getSecondaryUomName());
            result.setConversionRate(t.getConversionRate());
            return result;
        }).collect(toList());

        return resultList;
    }

    @Override
    public void materialUomTypeValidate(Long tenantId, MtMaterialVO2 dto) {
        // Step 1
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "materialId", "【API:materialUomTypeValidate】"));
        }

        if (StringUtils.isEmpty(dto.getPrimaryUomId()) && StringUtils.isEmpty(dto.getSecondaryUomId())) {
            throw new MtException("MT_MATERIAL_0065",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0065", "MATERIAL",
                                            "primaryUomId、secondaryUomId", "【API:materialUomTypeValidate】"));
        }

        String uomTypeOne = "";
        String uomTypeTwo = "";

        // Step 2
        MtMaterialVO1 mtMaterialVo1 = self().materialUomGet(tenantId, dto.getMaterialId());
        if (StringUtils.isNotEmpty(dto.getPrimaryUomId())) {
            MtUomVO uomVo1 = mtUomRepo.uomPropertyGet(tenantId, dto.getPrimaryUomId());
            if (uomVo1 != null) {
                uomTypeOne = uomVo1.getUomType();
            }

            if (mtMaterialVo1 != null) {
                MtUomVO uomVo2 = mtUomRepo.uomPropertyGet(tenantId, mtMaterialVo1.getPrimaryUomId());
                if (uomVo2 != null) {
                    uomTypeTwo = uomVo2.getUomType();
                }
            }

            if (!uomTypeOne.equals(uomTypeTwo)) {
                throw new MtException("MT_MATERIAL_0066", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0066", "MATERIAL", "【API:materialUomTypeValidate】"));
            }
        }

        uomTypeOne = "";
        uomTypeTwo = "";
        if (StringUtils.isNotEmpty(dto.getSecondaryUomId())) {
            MtUomVO uomVo1 = mtUomRepo.uomPropertyGet(tenantId, dto.getSecondaryUomId());
            if (uomVo1 != null) {
                uomTypeOne = uomVo1.getUomType();
            }

            if (mtMaterialVo1 != null) {
                MtUomVO uomVo2 = mtUomRepo.uomPropertyGet(tenantId, mtMaterialVo1.getSecondaryUomId());
                if (uomVo2 != null) {
                    uomTypeTwo = uomVo2.getUomType();
                }
            }

            if (!uomTypeOne.equals(uomTypeTwo)) {
                throw new MtException("MT_MATERIAL_0066", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0066", "MATERIAL", "【API:materialUomTypeValidate】"));
            }
        }
    }

    @Override
    public List<MtMaterial> queryMaterialByCode(Long tenantId, List<String> materialCodeList) {
        if (CollectionUtils.isEmpty(materialCodeList)) {
            return Collections.emptyList();
        }
        String whereInValuesSql = StringHelper.getWhereInValuesSql("b.MATERIAL_CODE", materialCodeList, 1000);
        return mtMaterialMapper.queryMaterialByCode(tenantId, whereInValuesSql);
    }

    @Override
    public List<MtMaterialVO5> propertyLimitMaterialPropertyQuery(Long tenantId, MtMaterialVO4 dto) {
        List<MtMaterialVO5> voList = mtMaterialMapper.selectCondition(tenantId, dto);
        if (CollectionUtils.isEmpty(voList)) {
            return Collections.emptyList();
        }
        // 获取uom所有数据
        List<String> allUomIds = new ArrayList<>();
        List<String> sizeUomIds = voList.stream().map(MtMaterialVO5::getSizeUomId).filter(StringUtils::isNotEmpty)
                        .distinct().collect(toList());
        List<String> volumeUomIds = voList.stream().map(MtMaterialVO5::getVolumeUomId).filter(StringUtils::isNotEmpty)
                        .distinct().collect(toList());
        List<String> weightUomIds = voList.stream().map(MtMaterialVO5::getWeightUomId).filter(StringUtils::isNotEmpty)
                        .distinct().collect(toList());
        List<String> shelfLifeUomId = voList.stream().map(MtMaterialVO5::getShelfLifeUomId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(toList());
        List<String> primaryUomIds = voList.stream().map(MtMaterialVO5::getPrimaryUomId).filter(StringUtils::isNotEmpty)
                        .distinct().collect(toList());
        List<String> secondaryUomIds = voList.stream().map(MtMaterialVO5::getSecondaryUomId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(toList());
        allUomIds.addAll(sizeUomIds);
        allUomIds.addAll(volumeUomIds);
        allUomIds.addAll(weightUomIds);
        allUomIds.addAll(shelfLifeUomId);
        allUomIds.addAll(primaryUomIds);
        allUomIds.addAll(secondaryUomIds);
        allUomIds.stream().distinct();
        Map<String, MtUom> uomMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(allUomIds)) {
            List<MtUomVO> mtUomVOS = mtUomRepo.uomPropertyBatchGet(tenantId, allUomIds);
            if (CollectionUtils.isNotEmpty(mtUomVOS)) {
                uomMap = mtUomVOS.stream().collect(Collectors.toMap(MtUom::getUomId, t -> t));
            }
        }

        // 组装数据
        MtUom uom;
        for (MtMaterialVO5 vo5 : voList) {
            uom = uomMap.get(vo5.getSizeUomId());
            vo5.setSizeUomCode(null != uom ? uom.getUomCode() : null);
            vo5.setSizeUomName(null != uom ? uom.getUomName() : null);

            uom = uomMap.get(vo5.getVolumeUomId());
            vo5.setVolumeUomCode(null != uom ? uom.getUomCode() : null);
            vo5.setVolumeUomName(null != uom ? uom.getUomName() : null);

            uom = uomMap.get(vo5.getWeightUomId());
            vo5.setWeightUomCode(null != uom ? uom.getUomCode() : null);
            vo5.setWeightUomName(null != uom ? uom.getUomName() : null);

            uom = uomMap.get(vo5.getShelfLifeUomId());
            vo5.setShelfLifeUomCode(null != uom ? uom.getUomCode() : null);
            vo5.setShelfLifeUomName(null != uom ? uom.getUomName() : null);

            uom = uomMap.get(vo5.getPrimaryUomId());
            vo5.setPrimaryUomCode(null != uom ? uom.getUomCode() : null);
            vo5.setPrimaryuomName(null != uom ? uom.getUomName() : null);

            uom = uomMap.get(vo5.getSecondaryUomId());
            vo5.setSecondaryUomCode(null != uom ? uom.getUomCode() : null);
            vo5.setSecondaryUomName(null != uom ? uom.getUomName() : null);
        }
        voList.sort(Comparator.comparingDouble((MtMaterialVO5 t) -> Double
                        .valueOf(StringUtils.isEmpty(t.getMaterialId()) ? "0" : t.getMaterialId())));
        return voList;
    }

    @Override
    public List<MtMaterialVO> materialBasicInfoBatchGet(Long tenantId, List<String> materialIds) {
        if (CollectionUtils.isEmpty(materialIds)) {
            return Collections.emptyList();
        }
        String whereInValuesSql = StringHelper.getWhereInValuesSql("item.MATERIAL_ID", materialIds, 1000);
        return mtMaterialMapper.materialBasicInfoBatchGetByIdList(tenantId, whereInValuesSql);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void materialAttrPropertyUpdate(Long tenantId, MtExtendVO10 mtExtendVO10) {
        if (StringUtils.isEmpty(mtExtendVO10.getKeyId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "keyId ", "【API：materialAttrPropertyUpdate】"));
        }
        MtMaterial mtMaterial = new MtMaterial();
        mtMaterial.setTenantId(tenantId);
        mtMaterial.setMaterialId(mtExtendVO10.getKeyId());
        mtMaterial = mtMaterialMapper.selectOne(mtMaterial);
        if (null == mtMaterial) {
            throw new MtException("MT_MATERIAL_0093",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0093", "MATERIAL",
                                            mtExtendVO10.getKeyId(), "mt_material_b",
                                            "【API:materialAttrPropertyUpdate】"));
        }
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_attr", mtExtendVO10.getKeyId(),
                        mtExtendVO10.getEventId(), mtExtendVO10.getAttrs());

    }
}
