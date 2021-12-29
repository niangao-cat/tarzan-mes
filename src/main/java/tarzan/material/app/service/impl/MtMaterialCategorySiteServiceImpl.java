package tarzan.material.app.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.material.api.dto.MtMaterialCategorySiteDTO;
import tarzan.material.app.service.MtMaterialCategoryService;
import tarzan.material.app.service.MtMaterialCategorySiteService;
import tarzan.material.domain.entity.MtMaterialCategorySite;
import tarzan.material.domain.repository.MtMaterialCategorySiteRepository;
import tarzan.material.domain.vo.MtMaterialCategorySiteVO;
import tarzan.material.domain.vo.MtMaterialCategorySiteVO2;
import tarzan.material.domain.vo.MtMaterialCategorySiteVO3;
import tarzan.material.infra.mapper.MtMaterialCategorySiteMapper;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.infra.mapper.MtModSiteMapper;

/**
 * 物料类别站点分配应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
@Service
public class MtMaterialCategorySiteServiceImpl extends BaseServiceImpl<MtMaterialCategorySite>
                implements MtMaterialCategorySiteService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtMaterialCategorySiteRepository mtMaterialCategorySiteRepository;

    @Autowired
    private MtMaterialCategoryService mtMaterialCategoryService;

    @Autowired
    private MtMaterialCategorySiteMapper mtMaterialCategorySiteMapper;

    @Autowired
    private MtModSiteMapper mtModSiteMapper;

    @Override
    public Page<MtMaterialCategorySiteVO> listMaterialCategorySiteForUi(Long tenantId, String materialCategoryId,
                                                                        PageRequest pageRequest) {

        List<MtGenType> genTypes = mtGenTypeRepository.getGenTypes(tenantId, "MODELING", "ORGANIZATION_REL_TYPE");

        Page<MtMaterialCategorySiteVO> page = PageHelper.doPage(pageRequest, () -> mtMaterialCategorySiteMapper
                        .selectMaterialCategorySiteByIdForUi(tenantId, materialCategoryId));

        page.forEach(vo -> {
            Optional<MtGenType> optional =
                            genTypes.stream().filter(t -> t.getTypeCode().equals(vo.getSiteType())).findFirst();
            if (optional.isPresent()) {
                vo.setSiteTypeDesc(optional.get().getDescription());
            }
        });


        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveMaterialCategorySiteForUi(Long tenantId, MtMaterialCategorySiteDTO dto) {

        mtMaterialCategoryService.verifyDefaultSetForUi(tenantId, dto.getMaterialCategoryId());

        // 新增或修改
        MtMaterialCategorySite mtMaterialCategorySite = new MtMaterialCategorySite();
        BeanUtils.copyProperties(dto, mtMaterialCategorySite);
        mtMaterialCategorySite.setTenantId(tenantId);
        // 唯一性校验
        Criteria criteria = new Criteria();
        List<WhereField> whereFields = new ArrayList<>();
        if (StringUtils.isNotEmpty(mtMaterialCategorySite.getMaterialCategorySiteId())) {
            whereFields.add(new WhereField(MtMaterialCategorySite.FIELD_MATERIAL_CATEGORY_SITE_ID,
                            Comparison.NOT_EQUAL));
        }
        whereFields.add(new WhereField(MtMaterialCategorySite.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtMaterialCategorySite.FIELD_MATERIAL_CATEGORY_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtMaterialCategorySite.FIELD_SITE_ID, Comparison.EQUAL));
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        if (mtMaterialCategorySiteMapper.selectOptional(mtMaterialCategorySite, criteria).size() > 0) {
            // 当前物料类别对应站点${1}已存在,请确认
            MtModSite site = new MtModSite();
            site.setTenantId(tenantId);
            site.setSiteId(mtMaterialCategorySite.getSiteId());
            site = mtModSiteMapper.selectOne(site);
            throw new MtException("MT_MATERIAL_0087", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0087", "MATERIAL", site.getSiteCode()));
        }
        if (StringUtils.isEmpty(mtMaterialCategorySite.getMaterialCategorySiteId())) {
            insertSelective(mtMaterialCategorySite);
        } else {
            updateByPrimaryKeySelective(mtMaterialCategorySite);
        }
        return mtMaterialCategorySite.getMaterialCategorySiteId();

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeMaterialCategorySiteForUi(Long tenantId, String materialCategorySiteId) {
        MtMaterialCategorySite mtMaterialCategorySite = new MtMaterialCategorySite();
        mtMaterialCategorySite.setTenantId(tenantId);
        mtMaterialCategorySite.setMaterialCategorySiteId(materialCategorySiteId);
        mtMaterialCategorySite = mtMaterialCategorySiteMapper.selectOne(mtMaterialCategorySite);

        // 验证物料类别是否在站点下存在PFEP属性
        if (mtMaterialCategorySite != null) {
            String exitFlag = mtMaterialCategorySiteRepository.materialCategorySitePfepExistValidate(tenantId,
                            mtMaterialCategorySite);
            if ("Y".equals(exitFlag)) {
                throw new MtException("MT_MATERIAL_0041", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0041", "MATERIAL", "【API:materialCategorySiteDelete】"));
            }
            delete(mtMaterialCategorySite);
        }
    }

    @Override
    public Page<MtMaterialCategorySiteVO2> materialCategorySiteNotExistForUi(Long tenantId,
                                                                             MtMaterialCategorySiteVO3 condition, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest,
                        () -> mtMaterialCategorySiteMapper.materialCategorySiteNotExistForLov(tenantId, condition));
    }

}
