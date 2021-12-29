package tarzan.material.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.mybatis.service.BaseServiceImpl;
import io.tarzan.common.app.service.MtExtendSettingsService;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.material.api.dto.MtPfepInventoryDTO2;
import tarzan.material.api.dto.MtPfepInventoryDTO3;
import tarzan.material.api.dto.MtPfepInventoryDTO4;
import tarzan.material.app.service.MtPfepInventoryCategoryService;
import tarzan.material.app.service.MtPfepInventoryService;
import tarzan.material.domain.entity.MtMaterialCategorySite;
import tarzan.material.domain.entity.MtPfepInventoryCategory;
import tarzan.material.domain.repository.MtMaterialCategorySiteRepository;
import tarzan.material.infra.mapper.MtMaterialCategorySiteMapper;
import tarzan.material.infra.mapper.MtPfepInventoryCategoryMapper;


/**
 * 物料类别存储属性应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@Service
public class MtPfepInventoryCategoryServiceImpl extends BaseServiceImpl<MtPfepInventoryCategory>
                implements MtPfepInventoryCategoryService {
    private static final String MT_PFEP_INVENTORY_CATEGORY_ATTR = "mt_pfep_inventory_catg_attr";
    @Autowired
    private MtPfepInventoryCategoryMapper mtPfepInventoryCategoryMapper;

    @Autowired
    private MtMaterialCategorySiteMapper mtMaterialCategorySiteMapper;

    @Autowired
    private MtMaterialCategorySiteRepository mtMaterialCategorySiteService;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtExtendSettingsService mtExtendSettingsService;

    @Autowired
    private MtPfepInventoryService mtPfepInventoryService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtPfepInventoryDTO4 pfepInventoryCategoryUpdate(Long tenantId, MtPfepInventoryDTO2 dto) {
        MtPfepInventoryDTO4 dto5 = new MtPfepInventoryDTO4();
        // 基础属性赋值
        MtPfepInventoryCategory mtPfepManufacturing = new MtPfepInventoryCategory();
        BeanUtils.copyProperties(dto, mtPfepManufacturing);
        mtPfepManufacturing.setTenantId(tenantId);
        // 获取类别站点关系ID
        MtMaterialCategorySite site = new MtMaterialCategorySite();
        site.setMaterialCategoryId(dto.getMaterialCategoryId());
        site.setSiteId(dto.getSiteId());
        site.setTenantId(tenantId);
        String MaterialCategorySiteId =
                        mtMaterialCategorySiteService.materialCategorySiteLimitRelationGet(tenantId, site);
        if (StringUtils.isEmpty(MaterialCategorySiteId)) {
            // 物料类别站点关系不存在，请检查！
            throw new MtException("MT_MATERIAL_0075", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0075", "MATERIAL", "API【saveMtPfepInventory】"));
        }
        mtPfepManufacturing.setMaterialCategorySiteId(MaterialCategorySiteId);
        mtPfepManufacturing.setPfepInventoryCategoryId(dto.getKid());
        // 校验唯一性
        Criteria criteria = new Criteria(mtPfepManufacturing);
        List<WhereField> whereFields2 = new ArrayList<WhereField>();
        if (StringUtils.isNotEmpty(mtPfepManufacturing.getPfepInventoryCategoryId())) {
            whereFields2.add(new WhereField(MtPfepInventoryCategory.FIELD_PFEP_INVENTORY_CATEGORY_ID,
                            Comparison.NOT_EQUAL));
        }
        whereFields2.add(new WhereField(MtPfepInventoryCategory.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields2.add(new WhereField(MtPfepInventoryCategory.FIELD_MATERIAL_CATEGORY_SITE_ID, Comparison.EQUAL));
        whereFields2.add(new WhereField(MtPfepInventoryCategory.FIELD_ORGANIZATION_ID, Comparison.EQUAL));
        whereFields2.add(new WhereField(MtPfepInventoryCategory.FIELD_ORGANIZATION_TYPE, Comparison.EQUAL));

        criteria.where(whereFields2.toArray(new WhereField[whereFields2.size()]));
        if (mtPfepInventoryCategoryMapper.selectOptional(mtPfepManufacturing, criteria).size() > 0) {
            throw new MtException("MT_MATERIAL_0061", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0061", "MATERIAL"));
        }
        if (StringUtils.isEmpty(mtPfepManufacturing.getPfepInventoryCategoryId())) {
            insertSelective(mtPfepManufacturing);
        } else {
            updateByPrimaryKey(mtPfepManufacturing);
        }
        // 写入扩展字段
        if (CollectionUtils.isNotEmpty(dto.getMtPfepInventoryAttrs())) {
            mtExtendSettingsService.attrSave(tenantId, MT_PFEP_INVENTORY_CATEGORY_ATTR,
                            mtPfepManufacturing.getPfepInventoryCategoryId(), null, dto.getMtPfepInventoryAttrs());
        }
        dto5.setKid(mtPfepManufacturing.getPfepInventoryCategoryId());
        dto5.setType("category");
        return dto5;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MtPfepInventoryDTO4 copyMtPfepInventoryCategory(Long tenantId, MtPfepInventoryDTO3 dto) {
        // 获取站点关系
        MtMaterialCategorySite mtMaterialCategory = new MtMaterialCategorySite();
        mtMaterialCategory.setTenantId(tenantId);
        mtMaterialCategory.setMaterialCategoryId(dto.getSourceMaterialCategoryId());
        mtMaterialCategory.setSiteId(dto.getTargetSiteId());
        mtMaterialCategory = mtMaterialCategorySiteMapper.selectOne(mtMaterialCategory);

        if (mtMaterialCategory == null) {
            // 目标物料与站点关系不存在，请确认
            throw new MtException("MT_BOM_0022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0022", "BOM", "API【saveMtPfepInventory】"));
        }

        // 获取来源属性
        MtPfepInventoryCategory pfepInventory = new MtPfepInventoryCategory();
        pfepInventory.setMaterialCategorySiteId(mtMaterialCategory.getMaterialCategorySiteId());
        pfepInventory.setOrganizationId(dto.getSourceOrgId());
        pfepInventory.setOrganizationType(dto.getSourceOrgType());
        pfepInventory.setTenantId(tenantId);

        // 目标属性赋值
        MtPfepInventoryCategory target = mtPfepInventoryCategoryMapper.selectOne(pfepInventory);
        if (target == null) {
            throw new MtException("MT_MATERIAL_0085", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0085", "MATERIAL"));
        }
        MtPfepInventoryDTO2 dto2 = new MtPfepInventoryDTO2();
        BeanUtils.copyProperties(target, dto2);
        dto2 = mtPfepInventoryService.setPfepInventorySaveParam(tenantId, dto, dto2,
                        target.getPfepInventoryCategoryId());
        return pfepInventoryCategoryUpdate(tenantId, dto2);
    }
}
