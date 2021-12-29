package tarzan.inventory.infra.repository.impl;

import java.util.Collections;
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
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.repository.MtContainerTypeRepository;
import tarzan.inventory.domain.vo.MtContainerTypeAttrVO1;
import tarzan.inventory.domain.vo.MtContainerTypeAttrVO2;
import tarzan.inventory.domain.vo.MtContainerTypeAttrVO3;
import tarzan.inventory.domain.vo.MtContainerTypeVO;
import tarzan.inventory.domain.vo.MtContainerTypeVO1;
import tarzan.inventory.infra.mapper.MtContainerTypeMapper;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtUomVO;

/**
 * 容器类型，定义一类容器和一类容器的控制属性，包括容器可装载的对象类型、最大容量、混装类型等 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:32
 */
@Component
public class MtContainerTypeRepositoryImpl extends BaseRepositoryImpl<MtContainerType>
                implements MtContainerTypeRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtContainerTypeMapper mtContainerTypeMapper;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Override
    public MtContainerType containerTypePropertyGet(Long tenantId, String containerTypeId) {
        // Step 1判断输入参数是否合规
        if (StringUtils.isEmpty(containerTypeId)) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "containerTypeId", "【API:containerTypePropertyGet】"));

        }
        MtContainerType type = new MtContainerType();
        type.setTenantId(tenantId);
        type.setContainerTypeId(containerTypeId);
        return mtContainerTypeMapper.selectOne(type);
    }

    @Override
    public List<String> propertyLimitContainerTypeQuery(Long tenantId, MtContainerType dto, String fuzzyQueryFlag) {
        List<MtContainerType> list =
                        this.mtContainerTypeMapper.selectPropertyLimitContainerType(tenantId, dto, fuzzyQueryFlag);

        return list.stream().map(MtContainerType::getContainerTypeId).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String containerTypePropertyUpdate(Long tenantId, MtContainerType dto, String fullUpdate) {
        // Step1判断输入参数是否合规
        if (StringUtils.isEmpty(dto.getContainerTypeId()) && StringUtils.isEmpty(dto.getContainerTypeCode())) {
            throw new MtException("MT_MATERIAL_LOT_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0002",
                                            "MATERIAL_LOT", "containerTypeId、containerTypeCode",
                                            "【API:containerTypePropertyUpdate】"));

        }

        if (StringUtils.isEmpty(dto.getSizeUomId()) && dto.getLength() != null) {
            throw new MtException("MT_MATERIAL_LOT_0014",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0014",
                                            "MATERIAL_LOT", "length", "sizeUomId",
                                            "【API:containerTypePropertyUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getSizeUomId()) && dto.getWidth() != null) {
            throw new MtException("MT_MATERIAL_LOT_0014",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0014",
                                            "MATERIAL_LOT", "width", "sizeUomId", "【API:containerTypePropertyUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getSizeUomId()) && dto.getHeight() != null) {
            throw new MtException("MT_MATERIAL_LOT_0014",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0014",
                                            "MATERIAL_LOT", "height", "sizeUomId",
                                            "【API:containerTypePropertyUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getWeightUomId()) && dto.getWeight() != null) {
            throw new MtException("MT_MATERIAL_LOT_0014",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0014",
                                            "MATERIAL_LOT", "weight", "weightUomId",
                                            "【API:containerTypePropertyUpdate】"));

        }
        if (StringUtils.isEmpty(dto.getWeightUomId()) && dto.getMaxLoadWeight() != null) {
            throw new MtException("MT_MATERIAL_LOT_0014",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0014",
                                            "MATERIAL_LOT", "maxLoadWeight", "weightUomId",
                                            "【API:containerTypePropertyUpdate】"));

        }

        if ("Y".equals(dto.getLocationEnabledFlag()) && dto.getLocationRow() == null) {
            throw new MtException("MT_MATERIAL_LOT_0027", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0027", "MATERIAL_LOT", "【API:containerTypePropertyUpdate】"));

        }
        if ("Y".equals(dto.getLocationEnabledFlag()) && dto.getLocationColumn() == null) {
            throw new MtException("MT_MATERIAL_LOT_0027", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0027", "MATERIAL_LOT", "【API:containerTypePropertyUpdate】"));

        }
        // Step2根据输入参数判断需新增数据或是更新数据
        MtContainerType mtContainerType;

        List<String> containerTypeIds;
        if (StringUtils.isNotEmpty(dto.getContainerTypeId())) {
            mtContainerType = containerTypePropertyGet(tenantId, dto.getContainerTypeId());
            if (mtContainerType == null) {
                throw new MtException("MT_MATERIAL_LOT_0025", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_MATERIAL_LOT_0025", "MATERIAL_LOT", "【API:containerTypePropertyUpdate】"));
            }

            // Step3根据输入参数更新容器类型数据
            mtContainerType.setContainerTypeCode(dto.getContainerTypeCode());
            mtContainerType.setContainerTypeDescription(dto.getContainerTypeDescription());
            mtContainerType.setEnableFlag(dto.getEnableFlag());
            mtContainerType.setPackingLevel(dto.getPackingLevel());
            mtContainerType.setCapacityQty(dto.getCapacityQty());
            mtContainerType.setMixedMaterialFlag(dto.getMixedMaterialFlag());
            mtContainerType.setMixedEoFlag(dto.getMixedEoFlag());
            mtContainerType.setMixedWoFlag(dto.getMixedWoFlag());
            mtContainerType.setMixedOwnerFlag(dto.getMixedOwnerFlag());
            mtContainerType.setLength(dto.getLength());
            mtContainerType.setWidth(dto.getWidth());
            mtContainerType.setHeight(dto.getHeight());
            mtContainerType.setWeight(dto.getWeight());
            mtContainerType.setSizeUomId(dto.getSizeUomId());
            mtContainerType.setMaxLoadWeight(dto.getMaxLoadWeight());
            mtContainerType.setWeightUomId(dto.getWeightUomId());
            mtContainerType.setLocationEnabledFlag(dto.getLocationEnabledFlag());
            mtContainerType.setLocationRow(dto.getLocationRow());
            mtContainerType.setLocationColumn(dto.getLocationColumn());
            if (MtBaseConstants.YES.equals(fullUpdate)) {
                mtContainerType = (MtContainerType) ObjectFieldsHelper.setStringFieldsEmpty(mtContainerType);
                if (mtContainerType.getContainerTypeCode() != null) {
                    MtContainerType temp = new MtContainerType();
                    temp.setContainerTypeCode(mtContainerType.getContainerTypeCode());
                    containerTypeIds = propertyLimitContainerTypeQuery(tenantId, temp, MtBaseConstants.NO);
                    MtContainerType finalMtContainerType = mtContainerType;
                    if (CollectionUtils.isNotEmpty(containerTypeIds) && containerTypeIds.stream()
                                    .anyMatch(t -> !t.equals(finalMtContainerType.getContainerTypeId()))) {
                        throw new MtException("MT_MATERIAL_LOT_0075",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_MATERIAL_LOT_0075", "MATERIAL_LOT", "MT_CONTAINER_TYPE",
                                                        "CONTAINER_TYPE_CODE", "【API:containerTypePropertyUpdate】"));

                    }
                }
                self().updateByPrimaryKey(mtContainerType);
            } else {
                if (mtContainerType.getContainerTypeCode() != null) {
                    MtContainerType temp = new MtContainerType();
                    temp.setContainerTypeCode(mtContainerType.getContainerTypeCode());
                    containerTypeIds = propertyLimitContainerTypeQuery(tenantId, temp, MtBaseConstants.NO);
                    MtContainerType finalMtContainerType = mtContainerType;
                    if (CollectionUtils.isNotEmpty(containerTypeIds) && containerTypeIds.stream()
                                    .anyMatch(t -> !t.equals(finalMtContainerType.getContainerTypeId()))) {
                        throw new MtException("MT_MATERIAL_LOT_0075",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "MT_MATERIAL_LOT_0075", "MATERIAL_LOT", "MT_CONTAINER_TYPE",
                                                        "CONTAINER_TYPE_CODE", "【API:containerTypePropertyUpdate】"));

                    }
                }
                self().updateByPrimaryKeySelective(mtContainerType);
            }

        } else {
            MtContainerType temp = new MtContainerType();
            temp.setContainerTypeCode(dto.getContainerTypeCode());
            containerTypeIds = propertyLimitContainerTypeQuery(tenantId, temp, MtBaseConstants.NO);
            if (CollectionUtils.isNotEmpty(containerTypeIds)) {
                throw new MtException("MT_MATERIAL_LOT_0075",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0075",
                                                "MATERIAL_LOT", "MT_CONTAINER_TYPE", "CONTAINER_TYPE_CODE",
                                                "【API:containerTypePropertyUpdate】"));

            }
            // Step 4根据输入参数新增容器类型数据
            mtContainerType = new MtContainerType();
            if (StringUtils.isEmpty(dto.getEnableFlag())) {
                mtContainerType.setEnableFlag("Y");
            } else {
                mtContainerType.setEnableFlag(dto.getEnableFlag());
            }

            if (StringUtils.isEmpty(dto.getMixedMaterialFlag())) {
                mtContainerType.setMixedMaterialFlag("N");
            } else {
                mtContainerType.setMixedMaterialFlag(dto.getMixedMaterialFlag());
            }

            if (StringUtils.isEmpty(dto.getMixedEoFlag())) {
                mtContainerType.setMixedEoFlag("Y");
            } else {
                mtContainerType.setMixedEoFlag(dto.getMixedEoFlag());
            }

            if (StringUtils.isEmpty(dto.getMixedWoFlag())) {
                mtContainerType.setMixedWoFlag("Y");
            } else {
                mtContainerType.setMixedWoFlag(dto.getMixedWoFlag());
            }

            if (StringUtils.isEmpty(dto.getMixedOwnerFlag())) {
                mtContainerType.setMixedOwnerFlag("N");
            } else {
                mtContainerType.setMixedOwnerFlag(dto.getMixedOwnerFlag());
            }

            if (StringUtils.isEmpty(dto.getLocationEnabledFlag())) {
                mtContainerType.setLocationEnabledFlag("N");
            } else {
                mtContainerType.setLocationEnabledFlag(dto.getLocationEnabledFlag());
            }


            mtContainerType.setTenantId(tenantId);
            mtContainerType.setContainerTypeCode(dto.getContainerTypeCode());
            mtContainerType.setContainerTypeDescription(dto.getContainerTypeDescription());
            mtContainerType.setPackingLevel(dto.getPackingLevel());
            mtContainerType.setCapacityQty(dto.getCapacityQty());
            mtContainerType.setLength(dto.getLength());
            mtContainerType.setWidth(dto.getWidth());
            mtContainerType.setHeight(dto.getHeight());
            mtContainerType.setWeight(dto.getWeight());
            mtContainerType.setSizeUomId(dto.getSizeUomId());
            mtContainerType.setMaxLoadWeight(dto.getMaxLoadWeight());
            mtContainerType.setWeightUomId(dto.getWeightUomId());
            mtContainerType.setLocationRow(dto.getLocationRow());
            mtContainerType.setLocationColumn(dto.getLocationColumn());
            self().insertSelective(mtContainerType);
        }

        return mtContainerType.getContainerTypeId();
    }

    @Override
    public void containerTypeEnableValidate(Long tenantId, String containerTypeId) {
        // Step1判断输入参数是否合规
        if (StringUtils.isEmpty(containerTypeId)) {
            throw new MtException("MT_MATERIAL_LOT_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0002",
                                            "MATERIAL_LOT", "containerTypeId", "【API:containerTypeEnableValidate】"));

        }

        // Step2根据输入参数containerTypeId调用
        MtContainerType type = containerTypePropertyGet(tenantId, containerTypeId);
        if (type == null) {
            throw new MtException("MT_MATERIAL_LOT_0025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0025", "MATERIAL_LOT", "【API:containerTypeEnableValidate】"));

        }
        if (!"Y".equals(type.getEnableFlag())) {
            throw new MtException("MT_MATERIAL_LOT_0028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0028", "MATERIAL_LOT", "【API:containerTypeEnableValidate】"));
        }
    }

    @Override
    public List<MtExtendAttrVO> containerTypeLimitAttrQuery(Long tenantId, MtContainerTypeAttrVO1 dto) {
        // 判断输入参数是否合规
        if (StringUtils.isEmpty(dto.getContainerTypeId())) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "containerTypeId", "【API:containerTypeLimitAttrQuery】"));

        }

        MtExtendVO extendVO = new MtExtendVO();
        extendVO.setTableName("mt_container_type_attr");
        extendVO.setKeyId(dto.getContainerTypeId());
        extendVO.setAttrName(dto.getAttrName());
        return mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
    }

    @Override
    public List<String> attrLimitContainerTypeQuery(Long tenantId, MtContainerTypeAttrVO2 dto) {
        // 判断输入参数是否合规
        if (CollectionUtils.isEmpty(dto.getAttr())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "attrName", "【API:attrLimitContainerTypeQuery】"));

        }

        // 存在attrName为空报错
        if (dto.getAttr().stream().anyMatch(attr -> StringUtils.isEmpty(attr.getAttrName()))) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "attrName", "【API:attrLimitContainerTypeQuery】"));

        }

        Map<String, String> property = dto.getAttr().stream().collect(Collectors.toMap(t -> t.getAttrName(),
                        t -> t.getAttrValue() == null ? "" : t.getAttrValue(), (k1, k2) -> k1));
        List<String> ids = mtExtendSettingsRepository.attrBatchPropertyLimitKidQuery(tenantId, "mt_container_type_attr",
                        property);
        // 根据主键进行筛选
        if (StringUtils.isNotEmpty(dto.getContainerTypeId())) {
            ids = ids.stream().filter(t -> dto.getContainerTypeId().equals(t)).collect(Collectors.toList());
        }

        return ids;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void containerTypeLimitAttrUpdate(Long tenantId, MtContainerTypeAttrVO3 dto) {
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_container_type_attr", dto.getContainerTypeId(),
                        null, dto.getAttr());
    }

    @Override
    public List<MtContainerType> containerTypePropertyBatchGet(Long tenantId, List<String> containerTypeIdList) {
        if (CollectionUtils.isEmpty(containerTypeIdList)) {
            throw new MtException("MT_MATERIAL_LOT_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0001",
                                            "MATERIAL_LOT", "containerTypeId", "【API:containerTypePropertyBatchGet】"));
        }
        return mtContainerTypeMapper.selectByIdsCustom(tenantId, containerTypeIdList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void containerTypeAttrPropertyUpdate(Long tenantId, MtExtendVO10 mtExtendVO10) {
        if (StringUtils.isEmpty(mtExtendVO10.getKeyId())) {
            throw new MtException("MT_MATERIAL_LOT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0001", "MATERIAL_LOT", "keyId", "【API:containerTypeAttrPropertyUpdate】"));
        }
        MtContainerType type = new MtContainerType();
        type.setTenantId(tenantId);
        type.setContainerTypeId(mtExtendVO10.getKeyId());
        MtContainerType containerType = mtContainerTypeMapper.selectOne(type);
        if (null == containerType) {
            throw new MtException("MT_MATERIAL_LOT_0089",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0089",
                                            "MATERIAL_LOT", mtExtendVO10.getKeyId(), "mt_container_type",
                                            "【API:containerTypeAttrPropertyUpdate】"));
        }
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_container_type_attr", mtExtendVO10.getKeyId(),
                        mtExtendVO10.getEventId(), mtExtendVO10.getAttrs());
    }

    @Override
    public List<MtContainerTypeVO1> propertyLimitContainerTypePropertyQuery(Long tenantId, MtContainerTypeVO dto) {
        List<MtContainerTypeVO1> voList = mtContainerTypeMapper.selectCondition(tenantId, dto);
        if (CollectionUtils.isEmpty(voList)) {
            return Collections.emptyList();
        }
        List<String> sizeUomIdList = voList.stream().map(MtContainerTypeVO1::getSizeUomId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        List<String> weightUomIdList = voList.stream().map(MtContainerTypeVO1::getWeightUomId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());

        Map<String, MtUomVO> sizeUomMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(sizeUomIdList)) {
            List<MtUomVO> sizeUomVOS = mtUomRepository.uomPropertyBatchGet(tenantId, sizeUomIdList);
            if (CollectionUtils.isNotEmpty(sizeUomVOS)) {
                sizeUomMap = sizeUomVOS.stream().collect(Collectors.toMap(MtUom::getUomId, t -> t));
            }
        }

        Map<String, MtUomVO> weightUomVOMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(weightUomIdList)) {
            List<MtUomVO> weightUomVOS = mtUomRepository.uomPropertyBatchGet(tenantId, weightUomIdList);
            if (CollectionUtils.isNotEmpty(weightUomVOS)) {
                weightUomVOMap = weightUomVOS.stream().collect(Collectors.toMap(MtUom::getUomId, t -> t));
            }
        }

        for (MtContainerTypeVO1 typeVO1 : voList) {
            typeVO1.setSizeUomCode(null == sizeUomMap.get(typeVO1.getSizeUomId()) ? null
                            : sizeUomMap.get(typeVO1.getSizeUomId()).getUomCode());
            typeVO1.setSizeUomName(null == sizeUomMap.get(typeVO1.getSizeUomId()) ? null
                            : sizeUomMap.get(typeVO1.getSizeUomId()).getUomName());
            typeVO1.setWeightUomCode(null == weightUomVOMap.get(typeVO1.getWeightUomId()) ? null
                            : weightUomVOMap.get(typeVO1.getWeightUomId()).getUomCode());
            typeVO1.setWeightUomName(null == weightUomVOMap.get(typeVO1.getWeightUomId()) ? null
                            : weightUomVOMap.get(typeVO1.getWeightUomId()).getUomName());

        }

        return voList;
    }


}
