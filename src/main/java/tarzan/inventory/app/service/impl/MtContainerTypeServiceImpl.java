package tarzan.inventory.app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ruike.hme.domain.repository.HmeContainerCapacityRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.api.dto.MtExtendAttrDTO;
import io.tarzan.common.app.service.MtExtendSettingsService;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import tarzan.inventory.api.dto.MtContainerTypeDTO;
import tarzan.inventory.api.dto.MtContainerTypeDTO1;
import tarzan.inventory.api.dto.MtContainerTypeDTO2;
import tarzan.inventory.app.service.MtContainerTypeService;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.repository.MtContainerTypeRepository;
import tarzan.inventory.infra.mapper.MtContainerTypeMapper;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtUomVO;

/**
 * 容器类型，定义一类容器和一类容器的控制属性，包括容器可装载的对象类型、最大容量、混装类型等应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:32
 */
@Service
public class MtContainerTypeServiceImpl implements MtContainerTypeService {

    private static final String MT_CONTAINER_TYPE_ATTR = "mt_container_type_attr";
    private static final String MIXED_MATERIAL_FLAG = "mixedMaterialFlag";
    private static final String MIXED_OWNER_FLAG = "mixedOwnerFlag";
    private static final String MIXED_EO_FLAG = "mixedEoFlag";
    private static final String MIXED_WO_FLAG = "mixedWoFlag";

    @Autowired
    private MtContainerTypeRepository mtContainerTypeRepository;

    @Autowired
    private MtContainerTypeMapper mtContainerTypeMapper;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtExtendSettingsService mtExtendSettingsService;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeContainerCapacityRepository hmeContainerCapacityRepository;

    @Override
    public Page<MtContainerTypeDTO1> queryContainerTypeListForUi(Long tenantId, MtContainerTypeDTO dto,
                                                                 PageRequest pageRequest) {
        // 设置条件查询
        MtContainerType mtContainerType = new MtContainerType();
        mtContainerType.setContainerTypeCode(dto.getContainerTypeCode());
        mtContainerType.setContainerTypeDescription(dto.getContainerTypeDescription());
        mtContainerType.setEnableFlag(dto.getEnableFlag());
        mtContainerType.setPackingLevel(dto.getPackingLevel());
        Page<MtContainerType> base = PageHelper.doPageAndSort(pageRequest, () -> mtContainerTypeMapper
                        .selectPropertyLimitContainerType(tenantId, mtContainerType, MtBaseConstants.YES));

        // 组装分页对象
        Page<MtContainerTypeDTO1> page = new Page<>();
        page.setTotalPages(base.getTotalPages());
        page.setTotalElements(base.getTotalElements());
        page.setNumberOfElements(base.getNumberOfElements());
        page.setSize(base.getSize());
        page.setNumber(base.getNumber());

        List<String> allUomIds = new ArrayList<>();
        List<String> sizeUomIds = base.stream().map(MtContainerType::getSizeUomId).collect(Collectors.toList());
        List<String> weightUomIds = base.stream().map(MtContainerType::getWeightUomId).collect(Collectors.toList());
        allUomIds.addAll(sizeUomIds);
        allUomIds.addAll(weightUomIds);

        Map<String, MtUomVO> uomMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(allUomIds)) {
            List<MtUomVO> mtUomVOS = mtUomRepository.uomPropertyBatchGet(tenantId, allUomIds);
            if (CollectionUtils.isNotEmpty(mtUomVOS)) {
                uomMap = mtUomVOS.stream().collect(Collectors.toMap(MtUomVO::getUomId, t -> t));
            }
        }

        // 返回结果组装数据
        List<MtContainerTypeDTO1> result = new ArrayList<>();
        Map<String, MtUomVO> finalUomMap = uomMap;
        base.forEach(t -> {
            MtUomVO uomVO;
            MtContainerTypeDTO1 dto1 = new MtContainerTypeDTO1();
            dto1.setContainerTypeId(t.getContainerTypeId());
            dto1.setContainerTypeCode(t.getContainerTypeCode());
            dto1.setContainerTypeDescription(t.getContainerTypeDescription());
            dto1.setEnableFlag(t.getEnableFlag());
            dto1.setPackingLevel(t.getPackingLevel());
            dto1.setCapacityQty(t.getCapacityQty());
            dto1.setMixedMaterialFlag(t.getMixedMaterialFlag());
            dto1.setMixedEoFlag(t.getMixedEoFlag());
            dto1.setMixedWoFlag(t.getMixedWoFlag());
            dto1.setMixedOwnerFlag(t.getMixedOwnerFlag());
            dto1.setLength(t.getLength());
            dto1.setWidth(t.getWidth());
            dto1.setHeight(t.getHeight());
            dto1.setWeight(t.getWeight());
            dto1.setSizeUomId(t.getSizeUomId());
            uomVO = finalUomMap.get(t.getSizeUomId());
            if (null != uomVO) {
                dto1.setSizeUomCode(uomVO.getUomCode());
                dto1.setSizeUomName(uomVO.getUomName());
            }

            dto1.setMaxLoadWeight(t.getMaxLoadWeight());
            dto1.setWeightUomId(t.getWeightUomId());

            uomVO = finalUomMap.get(t.getWeightUomId());
            if (null != uomVO) {
                dto1.setWeightUomCode(uomVO.getUomCode());
                dto1.setWeightUomName(uomVO.getUomName());
            }
            dto1.setLocationEnabledFlag(t.getLocationEnabledFlag());
            dto1.setLocationRow(t.getLocationRow());
            dto1.setLocationColumn(t.getLocationColumn());
            result.add(dto1);
        });
        page.setContent(result);
        return page;
    }

    @Override
    public MtContainerTypeDTO1 queryContainerTypeDetailForUi(Long tenantId, String containerTypeId) {
        // 查询数据
        MtContainerType mtContainerType = new MtContainerType();
        mtContainerType.setContainerTypeId(containerTypeId);
        List<MtContainerType> types = mtContainerTypeMapper.selectPropertyLimitContainerType(tenantId, mtContainerType,
                        MtBaseConstants.NO);

        if (CollectionUtils.isEmpty(types)) {
            return null;
        }

        MtContainerType type = types.get(0);
        MtContainerTypeDTO1 result = new MtContainerTypeDTO1();
        result.setContainerTypeId(type.getContainerTypeId());
        result.setContainerTypeCode(type.getContainerTypeCode());
        result.setContainerTypeDescription(type.getContainerTypeDescription());
        result.setEnableFlag(type.getEnableFlag());
        result.setPackingLevel(type.getPackingLevel());
        result.setCapacityQty(type.getCapacityQty());
        result.setMixedMaterialFlag(type.getMixedMaterialFlag());
        result.setMixedEoFlag(type.getMixedEoFlag());
        result.setMixedWoFlag(type.getMixedWoFlag());
        result.setMixedOwnerFlag(type.getMixedOwnerFlag());
        result.setLength(type.getLength());
        result.setWidth(type.getWidth());
        result.setHeight(type.getHeight());
        result.setWeight(type.getWeight());
        result.setSizeUomId(type.getSizeUomId());
        result.setMaxLoadWeight(type.getMaxLoadWeight());
        result.setWeightUomId(type.getWeightUomId());
        result.setLocationEnabledFlag(type.getLocationEnabledFlag());
        result.setLocationRow(type.getLocationRow());
        result.setLocationColumn(type.getLocationColumn());

        // size单位
        if (StringUtils.isNotEmpty(result.getSizeUomId())) {
            MtUomVO uomVO = mtUomRepository.uomPropertyGet(tenantId, result.getSizeUomId());
            if (null != uomVO) {
                result.setSizeUomCode(uomVO.getUomCode());
                result.setSizeUomName(uomVO.getUomName());
            }
        }

        // weight单位
        if (StringUtils.isNotEmpty(result.getWeightUomId())) {
            MtUomVO uomVO = mtUomRepository.uomPropertyGet(tenantId, result.getWeightUomId());
            if (null != uomVO) {
                result.setWeightUomCode(uomVO.getUomCode());
                result.setWeightUomName(uomVO.getUomName());
            }
        }


        List<String> flagValues = new ArrayList<>();

        // 判断返回的flag值
        if (MtBaseConstants.YES.equals(result.getMixedMaterialFlag())) {
            flagValues.add(MIXED_MATERIAL_FLAG);
        }

        if (MtBaseConstants.YES.equals(result.getMixedOwnerFlag())) {
            flagValues.add(MIXED_OWNER_FLAG);
        }

        if (MtBaseConstants.YES.equals(result.getMixedEoFlag())) {
            flagValues.add(MIXED_EO_FLAG);
        }

        if (MtBaseConstants.YES.equals(result.getMixedWoFlag())) {
            flagValues.add(MIXED_WO_FLAG);
        }

        result.setFlagValues(flagValues);

        List<MtExtendAttrDTO> attrs =
                        mtExtendSettingsService.attrQuery(tenantId, containerTypeId, MT_CONTAINER_TYPE_ATTR);

        result.setContainerTypeAttrList(attrs);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveContainerTypeForUi(Long tenantId, MtContainerTypeDTO2 dto) {

        // 对象赋值
        MtContainerType mtContainerType = new MtContainerType();
        mtContainerType.setContainerTypeId(dto.getContainerTypeId());
        mtContainerType.setContainerTypeCode(dto.getContainerTypeCode());
        mtContainerType.setContainerTypeDescription(dto.getContainerTypeDescription());
        mtContainerType.setEnableFlag(dto.getEnableFlag());
        mtContainerType.setPackingLevel(dto.getPackingLevel());
        mtContainerType.setCapacityQty(dto.getCapacityQty());
        mtContainerType.setTenantId(tenantId);

        // 校验唯一性
        Criteria criteria = new Criteria(mtContainerType);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        if (StringUtils.isNotEmpty(mtContainerType.getContainerTypeId())) {
            whereFields.add(new WhereField(MtContainerType.FIELD_CONTAINER_TYPE_ID, Comparison.NOT_EQUAL));
        }
        whereFields.add(new WhereField(MtContainerType.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtContainerType.FIELD_CONTAINER_TYPE_CODE, Comparison.EQUAL));
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));

        if (CollectionUtils.isNotEmpty(mtContainerTypeRepository.selectOptional(mtContainerType, criteria))) {
            throw new MtException("MT_MATERIAL_LOT_0096", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0096", "MATERIAL_LOT"));
        }

        // 校验数据
        if ((dto.getLength() != null || dto.getWidth() != null || dto.getHeight() != null)
                        && StringUtils.isEmpty(dto.getSizeUomId())
                        || (dto.getWeight() != null || dto.getMaxLoadWeight() != null)
                                        && StringUtils.isEmpty(dto.getWeightUomId())) {
            throw new MtException("MT_MATERIAL_LOT_0095", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_LOT_0095", "MATERIAL_LOT"));
        }

        List<String> flagValues = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dto.getFlagValues())) {
            flagValues = dto.getFlagValues();
        }

        if (flagValues.contains(MIXED_MATERIAL_FLAG)) {
            mtContainerType.setMixedMaterialFlag(MtBaseConstants.YES);
        } else {
            mtContainerType.setMixedMaterialFlag(MtBaseConstants.NO);
        }

        if (flagValues.contains(MIXED_OWNER_FLAG)) {
            mtContainerType.setMixedOwnerFlag(MtBaseConstants.YES);
        } else {
            mtContainerType.setMixedOwnerFlag(MtBaseConstants.NO);
        }

        if (flagValues.contains(MIXED_WO_FLAG)) {
            mtContainerType.setMixedWoFlag(MtBaseConstants.YES);
        } else {
            mtContainerType.setMixedWoFlag(MtBaseConstants.NO);
        }

        if (flagValues.contains(MIXED_EO_FLAG)) {
            mtContainerType.setMixedEoFlag(MtBaseConstants.YES);
        } else {
            mtContainerType.setMixedEoFlag(MtBaseConstants.NO);
        }

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
        mtContainerType.set_tls(dto.get_tls());

        // 调用更新api
        String mtContainerTypeId = mtContainerTypeRepository.containerTypePropertyUpdate(tenantId, mtContainerType,
                        MtBaseConstants.YES);

        // 保存扩展字段
        if (CollectionUtils.isNotEmpty(dto.getContainerTypeAttrs())) {
            mtExtendSettingsService.attrSave(tenantId, MT_CONTAINER_TYPE_ATTR, mtContainerTypeId, null,
                            dto.getContainerTypeAttrs());
        }

        if(CollectionUtils.isNotEmpty(dto.getContainerCapacityList())){
            hmeContainerCapacityRepository.batchCreateContainerCapacity(tenantId,dto.getContainerCapacityList());
        }

        return mtContainerTypeId;
    }
}
