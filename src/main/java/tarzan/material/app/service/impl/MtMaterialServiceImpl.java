package tarzan.material.app.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.common.Criteria;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.tarzan.common.app.service.MtExtendSettingsService;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.material.api.dto.MtMaterialDTO5;
import tarzan.material.api.dto.MtMaterialDTO6;
import tarzan.material.app.service.MtMaterialCategoryAssignService;
import tarzan.material.app.service.MtMaterialService;
import tarzan.material.app.service.MtMaterialSiteService;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.infra.mapper.MtMaterialMapper;
import tarzan.material.infra.mapper.MtUomMapper;

/**
 * 物料基础属性应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
@Service
public class MtMaterialServiceImpl extends BaseServiceImpl<MtMaterial> implements MtMaterialService {
    private static final String MT_MATERIAL_ATTR = "mt_material_attr";

    @Autowired
    private MtExtendSettingsService mtExtendSettingsService;
    @Autowired
    private MtMaterialSiteService mtMaterialSiteService;
    @Autowired
    private MtMaterialCategoryAssignService mtMaterialCategoryAssignService;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtMaterialMapper mtMaterialMapper;

    @Autowired
    private MtUomMapper mtUomMapper;

    @Override
    public MtMaterialVO materialPropertyGetForUi(Long tenantId, String materialId) {
        return mtMaterialRepository.materialPropertyGet(tenantId, materialId);
    }

    @Override
    public Page<MtMaterialVO> materialListUi(Long tenantId, MtMaterialDTO6 dto, PageRequest pageRequest) {
        // 设置查询条件
        MtMaterialVO materialVO = new MtMaterialVO();
        materialVO.setTenantId(tenantId);
        materialVO.setMaterialCode(dto.getMaterialCode());
        materialVO.setMaterialName(dto.getMaterialName());
        materialVO.setMaterialDesignCode(dto.getMaterialDesignCode());
        materialVO.setMaterialIdentifyCode(dto.getMaterialIdentifyCode());
        // 根据条件查询分页数据
        Page<MtMaterial> mtMaterials = PageHelper.doPage(pageRequest,
                        () -> mtMaterialMapper.selectMaterialViewForUi(tenantId, materialVO));
        // 创建分页对象
        Page<MtMaterialVO> result = new Page<MtMaterialVO>();
        result.setTotalPages(mtMaterials.getTotalPages());
        result.setTotalElements(mtMaterials.getTotalElements());
        result.setNumberOfElements(mtMaterials.getNumberOfElements());
        result.setSize(mtMaterials.getSize());
        result.setNumber(mtMaterials.getNumber());

        // 将查询出的物料信息的所有单位Id进行汇总
        List<String> uomIds = new ArrayList<>();
        mtMaterials.getContent().forEach(t -> {
            uomIds.add(t.getPrimaryUomId());
            uomIds.add(t.getSecondaryUomId());
            uomIds.add(t.getSizeUomId());
            uomIds.add(t.getWeightUomId());
            uomIds.add(t.getShelfLifeUomId());
            uomIds.add(t.getVolumeUomId());
        });
        // 单位Id进行去重
        List<String> idList = uomIds.stream().filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());

        // 根据去重后的uomId进行批量获取单位信息
        Map<String, MtUom> uomMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(idList)) {
            List<MtUom> mtUoms = mtUomMapper.selectUomBatch(tenantId, idList);
            if (CollectionUtils.isNotEmpty(mtUoms)) {
                uomMap = mtUoms.stream().collect(Collectors.toMap(MtUom::getUomId, t -> t));
            }
        }


        // 封装新的分页对象
        List<MtMaterialVO> list = Collections.synchronizedList(new ArrayList<>());

        Map<String, MtUom> finalUomMap = uomMap;
        mtMaterials.parallelStream().forEach(t -> {
            MtUom uom;
            MtMaterialVO mtMaterialVO = new MtMaterialVO();

            uom = finalUomMap.get(t.getSizeUomId());
            if (uom != null) {
                mtMaterialVO.setSizeUomCode(uom.getUomCode());
                mtMaterialVO.setSizeUomName(uom.getUomName());
            }


            uom = finalUomMap.get(t.getWeightUomId());
            if (uom != null) {
                mtMaterialVO.setWeightUomCode(uom.getUomCode());
                mtMaterialVO.setWeightUomName(uom.getUomName());
            }

            uom = finalUomMap.get(t.getPrimaryUomId());
            if (uom != null) {
                mtMaterialVO.setPrimaryUomCode(uom.getUomCode());
                mtMaterialVO.setPrimaryUomName(uom.getUomName());
            }

            uom = finalUomMap.get(t.getShelfLifeUomId());
            if (uom != null) {
                mtMaterialVO.setShelfLifeUomCode(uom.getUomCode());
                mtMaterialVO.setShelfLifeUomName(uom.getUomName());
            }

            uom = finalUomMap.get(t.getVolumeUomId());
            if (uom != null) {
                mtMaterialVO.setVolumeUomCode(uom.getUomCode());
                mtMaterialVO.setVolumeUomName(uom.getUomName());
            }

            uom = finalUomMap.get(t.getSecondaryUomId());
            if (uom != null) {
                mtMaterialVO.setSecondaryUomCode(uom.getUomCode());
                mtMaterialVO.setSecondaryUomName(uom.getUomName());
            }

            mtMaterialVO.setTenantId(t.getTenantId());
            mtMaterialVO.setMaterialId(t.getMaterialId());
            mtMaterialVO.setMaterialCode(t.getMaterialCode());
            mtMaterialVO.setMaterialName(t.getMaterialName());
            mtMaterialVO.setMaterialDesignCode(t.getMaterialDesignCode());
            mtMaterialVO.setMaterialIdentifyCode(t.getMaterialIdentifyCode());
            mtMaterialVO.setLength(t.getLength());
            mtMaterialVO.setWidth(t.getWidth());
            mtMaterialVO.setHeight(t.getHeight());
            mtMaterialVO.setSizeUomId(t.getSizeUomId());
            mtMaterialVO.setModel(t.getModel());
            mtMaterialVO.setVolume(t.getVolume());
            mtMaterialVO.setVolumeUomId(t.getVolumeUomId());
            mtMaterialVO.setWeight(t.getWeight());
            mtMaterialVO.setWeightUomId(t.getWeightUomId());
            mtMaterialVO.setShelfLife(t.getShelfLife());
            mtMaterialVO.setShelfLifeUomId(t.getShelfLifeUomId());
            mtMaterialVO.setPrimaryUomId(t.getPrimaryUomId());
            mtMaterialVO.setSecondaryUomId(t.getSecondaryUomId());
            mtMaterialVO.setConversionRate(t.getConversionRate());
            mtMaterialVO.setEnableFlag(t.getEnableFlag());
            mtMaterialVO.setCid(t.getCid());
            mtMaterialVO.setCreationDate(t.getCreationDate());
            mtMaterialVO.setCreatedBy(t.getCreatedBy());
            mtMaterialVO.setLastUpdateDate(t.getLastUpdateDate());
            mtMaterialVO.setLastUpdatedBy(t.getLastUpdatedBy());
            mtMaterialVO.setObjectVersionNumber(t.getObjectVersionNumber());
            list.add(mtMaterialVO);
        });
        list.sort(Comparator.comparingDouble((MtMaterialVO t) -> Double
                        .valueOf(StringUtils.isEmpty(t.getMaterialId()) ? "0" : t.getMaterialId())));
        // 返回结果
        result.setContent(list);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String materialSaveForUi(Long tenantId, MtMaterialDTO5 dto) {
        MtMaterial mtMaterial = new MtMaterial();
        BeanUtils.copyProperties(dto, mtMaterial);
        mtMaterial.setTenantId(tenantId);

        // 校验唯一性
        MtMaterial temp = new MtMaterial();
        temp.setTenantId(tenantId);
        temp.setMaterialCode(mtMaterial.getMaterialCode());
        List<MtMaterial> mtMaterials = mtMaterialMapper.select(temp);
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            if (mtMaterials != null && mtMaterials.size() > 0) {
                throw new MtException("MT_MATERIAL_0064", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0064", "MATERIAL"));
            }
            insertSelective(mtMaterial);
        } else {
            if (mtMaterials != null && mtMaterials.size() > 0) {
                if (!mtMaterials.get(0).getMaterialId().equals(dto.getMaterialId())) {
                    throw new MtException("MT_MATERIAL_0064", mtErrorMessageRepository
                                    .getErrorMessageWithModule(tenantId, "MT_MATERIAL_0064", "MATERIAL"));
                }
            }
            updateByPrimaryKey(mtMaterial);
        }

        // 写入物料站点
        if (CollectionUtils.isNotEmpty(dto.getMtMaterialSites())) {
            mtMaterialSiteService.materialSiteSave(tenantId, dto.getMtMaterialSites(), mtMaterial.getMaterialId());
        }

        // 写入物料类别分配
        if (CollectionUtils.isNotEmpty(dto.getMtMaterialCategoryAssigns())) {
            mtMaterialCategoryAssignService.materialCategoryAssignSave(tenantId, dto.getMtMaterialCategoryAssigns(),
                            mtMaterial.getMaterialId());
        }
        return mtMaterial.getMaterialId();
    }

    @Override
    public MtMaterial materialCheckForUi(Long tenantId, String materialCode) {
        MtMaterial material = new MtMaterial();
        material.setTenantId(tenantId);
        material.setMaterialCode(materialCode);
        Criteria criteria = new Criteria(material);
        criteria.where(MtMaterial.FIELD_MATERIAL_CODE, MtMaterial.FIELD_TENANT_ID);
        List<MtMaterial> materials = mtMaterialMapper.selectOptional(material, criteria);
        if (CollectionUtils.isEmpty(materials)) {
            return null;
        } else {
            return materials.get(0);
        }
    }
}
