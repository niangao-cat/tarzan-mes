package tarzan.material.app.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import io.tarzan.common.api.dto.MtExtendAttrDTO;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;
import io.tarzan.common.app.service.MtExtendSettingsService;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.material.api.dto.MtMaterialSiteDTO4;
import tarzan.material.app.service.MtMaterialCategoryAssignService;
import tarzan.material.app.service.MtMaterialSiteService;
import tarzan.material.domain.entity.MtMaterialCategoryAssign;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.vo.MaterialSiteVO;
import tarzan.material.infra.mapper.MtMaterialCategoryAssignMapper;
import tarzan.material.infra.mapper.MtMaterialSiteMapper;

/**
 * 物料站点分配应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
@Service
public class MtMaterialSiteServiceImpl extends BaseServiceImpl<MtMaterialSite> implements MtMaterialSiteService {
    private static final String MT_MATERIAL_SIT_ATTR = "mt_material_site_attr";
    @Autowired
    private MtMaterialSiteMapper mtMaterialSiteMapper;
    @Autowired
    private MtExtendSettingsService mtExtendSettingsService;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtMaterialCategoryAssignService mtMaterialCategoryAssignService;
    @Autowired
    private MtMaterialCategoryAssignMapper mtMaterialCategoryAssignMapper;

    @Override
    public Page<MaterialSiteVO> selectMaterialSiteById(Long tenantId, String materialId, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> mtMaterialSiteMapper.selectMaterialSiteById(tenantId, materialId));
    }

    @Override
    public List<MtExtendAttrDTO> queryExtendAttrForUi(Long tenantId, String materialSiteId) {
        if (StringUtils.isEmpty(materialSiteId)) {
            return Collections.emptyList();
        }
        return mtExtendSettingsService.attrQuery(tenantId, materialSiteId, MT_MATERIAL_SIT_ATTR);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveExtendAttrForUi(Long tenantId, String materialSiteId, List<MtExtendAttrDTO3> materialSiteAttrs) {
        if (StringUtils.isNotEmpty(materialSiteId) && CollectionUtils.isNotEmpty(materialSiteAttrs)) {
            mtExtendSettingsService.attrSave(tenantId, MT_MATERIAL_SIT_ATTR, materialSiteId, null, materialSiteAttrs);
        }
    }

    @Override
    public String materialSiteLimitRelationGet(Long tenantId, MtMaterialSite dto) {
        if (StringUtils.isEmpty(dto.getMaterialId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "materialId", "【API:materialSiteLimitRelationGet】"));
        }

        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_MATERIAL_0054", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0054", "MATERIAL", "siteId", "【API:materialSiteLimitRelationGet】"));
        }

        MtMaterialSite tmp = new MtMaterialSite();
        tmp.setMaterialId(dto.getMaterialId());
        tmp.setSiteId(dto.getSiteId());
        tmp = mtMaterialSiteMapper.selectOne(tmp);
        if (tmp == null) {
            return "";
        }
        return tmp.getMaterialSiteId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void materialSiteSave(Long tenantId, List<MtMaterialSiteDTO4> dto, String materialId) {
        if (CollectionUtils.isNotEmpty(dto)) {
            dto.forEach(everDto -> {
                MtMaterialSite param = new MtMaterialSite();
                BeanUtils.copyProperties(everDto, param);
                param.setTenantId(tenantId);
                param.setMaterialId(materialId);

                // 校验唯一性
                MtMaterialSite temp = new MtMaterialSite();
                temp.setTenantId(tenantId);
                temp.setMaterialId(materialId);
                temp.setSiteId(everDto.getSiteId());
                List<MtMaterialSite> mtMaterialSites = mtMaterialSiteMapper.select(temp);
                if (StringUtils.isEmpty(param.getMaterialSiteId())) {
                    if (mtMaterialSites != null && mtMaterialSites.size() > 0) {
                        throw new MtException("MT_MATERIAL_0082", mtErrorMessageRepository
                                        .getErrorMessageWithModule(tenantId, "MT_MATERIAL_0082", "MATERIAL"));
                    }
                    this.insertSelective(param);
                } else {
                    if (mtMaterialSites != null && mtMaterialSites.size() > 0) {
                        if (!mtMaterialSites.get(0).getMaterialSiteId().equals(everDto.getMaterialSiteId())) {
                            throw new MtException("MT_MATERIAL_0082", mtErrorMessageRepository
                                            .getErrorMessageWithModule(tenantId, "MT_MATERIAL_0082", "MATERIAL"));
                        }
                    }
                    this.updateByPrimaryKey(param);
                }
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void materialSiteDelete(Long tenantId, List<String> materialSiteIds) {
        List<String> materialCategoryAssignIds = new ArrayList<String>();
        if (CollectionUtils.isNotEmpty(materialSiteIds)) {
            materialSiteIds.forEach(materialSiteId -> {
                if (StringUtils.isNotEmpty(materialSiteId)) {
                    // 删除物料站点
                    MtMaterialSite param = new MtMaterialSite();
                    param.setTenantId(tenantId);
                    param.setMaterialSiteId(materialSiteId);
                    deleteByPrimaryKey(param);

                    // 根据物料站点删除物料类别分配信息
                    MtMaterialCategoryAssign assign = new MtMaterialCategoryAssign();
                    assign.setTenantId(tenantId);
                    assign.setMaterialSiteId(materialSiteId);
                    Criteria criteria = new Criteria(assign);
                    criteria.where(MtMaterialCategoryAssign.FIELD_MATERIAL_SITE_ID,
                                    MtMaterialCategoryAssign.FIELD_TENANT_ID);
                    List<MtMaterialCategoryAssign> all =
                                    mtMaterialCategoryAssignMapper.selectOptional(assign, criteria);
                    all.forEach((ever) -> materialCategoryAssignIds.add(ever.getMaterialCategoryAssignId()));
                    if (CollectionUtils.isNotEmpty(materialCategoryAssignIds)) {
                        mtMaterialCategoryAssignService.materialCategoryAssignDelete(tenantId,
                                        materialCategoryAssignIds);
                    }
                }
            });
        }
    }
}
