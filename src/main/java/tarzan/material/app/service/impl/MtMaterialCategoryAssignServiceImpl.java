package tarzan.material.app.service.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.material.api.dto.MtMaterialCategoryAssignDTO;
import tarzan.material.app.service.MtMaterialCategoryAssignService;
import tarzan.material.app.service.MtMaterialSiteService;
import tarzan.material.domain.entity.MtMaterialCategoryAssign;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.repository.MtMaterialCategoryAssignRepository;
import tarzan.material.domain.vo.MaterialCategoryAssignVO;
import tarzan.material.domain.vo.MtMaterialCategoryAssignVO;
import tarzan.material.infra.mapper.MtMaterialCategoryAssignMapper;

/**
 * 物料类别分配应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
@Service
public class MtMaterialCategoryAssignServiceImpl extends BaseServiceImpl<MtMaterialCategoryAssign>
                implements MtMaterialCategoryAssignService {
    @Autowired
    private MtMaterialCategoryAssignMapper mtMaterialCategoryAssignMapper;
    @Autowired
    private MtMaterialCategoryAssignRepository mtMaterialCategoryAssignRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtMaterialSiteService mtMaterialSiteService;

    @Override
    public Page<MaterialCategoryAssignVO> selectMaterialCategoryAssiteById(Long tenantId, String materialId,
                                                                           PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest,
                        () -> mtMaterialCategoryAssignMapper.selectMaterialCategoryAssiteById(tenantId, materialId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void materialCategoryAssignSave(Long tenantId, List<MtMaterialCategoryAssignDTO> dto, String materialId) {
        if (CollectionUtils.isNotEmpty(dto)) {
            dto.forEach(everDto -> {
                String materialSiteId;
                MtMaterialCategoryAssignVO verify = new MtMaterialCategoryAssignVO();
                verify.setMaterialCategoryId(everDto.getMaterialCategoryId());
                verify.setSiteId(everDto.getSiteId());
                verify.setMaterialId(materialId);
                if (StringUtils.isNotEmpty(everDto.getMaterialCategoryAssignId())) {
                    verify.setMaterialCategoryAssignId(everDto.getMaterialCategoryAssignId());
                }
                // 验证输入的物料类别集和物料站点有效性 校验数据唯一性
                if (!"Y".equals(mtMaterialCategoryAssignRepository.materialCategoryAssignUniqueValidate(tenantId,
                                verify))) {
                    throw new MtException("MT_MATERIAL_0094", mtErrorMessageRepository
                                    .getErrorMessageWithModule(tenantId, "MT_MATERIAL_0094", "MATERIAL"));
                }

                // 获取物料站点信息
                MtMaterialSite site = new MtMaterialSite();
                site.setMaterialId(materialId);
                site.setSiteId(everDto.getSiteId());
                materialSiteId = mtMaterialSiteService.materialSiteLimitRelationGet(tenantId, site);

                // 准备需要修改的数据 有站点的数据才能保存物料分配信息
                if (StringUtils.isNotEmpty(materialSiteId)) {
                    MtMaterialCategoryAssign perData = new MtMaterialCategoryAssign();
                    perData.setMaterialCategoryAssignId(everDto.getMaterialCategoryAssignId());
                    perData.setMaterialCategoryId(everDto.getMaterialCategoryId());
                    perData.setMaterialSiteId(materialSiteId);
                    perData.setTenantId(tenantId);

                    if (StringUtils.isEmpty(perData.getMaterialCategoryAssignId())) {
                        insertSelective(perData);
                    } else {
                        updateByPrimaryKey(perData);
                    }
                } else {
                    // 物料站点关系不存在 不能保存物料类别分配
                    throw new MtException("MT_MATERIAL_0074", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_MATERIAL_0074", "MATERIAL", "【API:materialCategoryAssignSave】"));
                }
            });
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void materialCategoryAssignDelete(Long tenantId, List<String> materialCategoryAssignIds) {
        if (CollectionUtils.isNotEmpty(materialCategoryAssignIds)) {
            materialCategoryAssignIds.forEach(materialCategoryAssignId -> {
                if (StringUtils.isNotEmpty(materialCategoryAssignId)) {
                    MtMaterialCategoryAssign perData = new MtMaterialCategoryAssign();
                    perData.setMaterialCategoryAssignId(materialCategoryAssignId);
                    perData.setTenantId(tenantId);
                    deleteByPrimaryKey(perData);
                }
            });
        }
    }
}
