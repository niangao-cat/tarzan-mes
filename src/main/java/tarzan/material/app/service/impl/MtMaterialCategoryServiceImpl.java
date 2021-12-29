package tarzan.material.app.service.impl;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.material.api.dto.MtMaterialCategoryDTO2;
import tarzan.material.app.service.MtMaterialCategoryService;
import tarzan.material.domain.entity.MtMaterialCategory;
import tarzan.material.domain.repository.MtMaterialCategorySetRepository;
import tarzan.material.domain.vo.MtMaterialCategoryVO2;
import tarzan.material.domain.vo.MtMaterialCategoryVO3;
import tarzan.material.infra.mapper.MtMaterialCategoryMapper;

/**
 * 物料类别应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
@Service
public class MtMaterialCategoryServiceImpl  extends BaseServiceImpl<MtMaterialCategory>
                                                    implements MtMaterialCategoryService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    
    @Autowired
    private MtMaterialCategorySetRepository mtMaterialCategorySetRepository;
    
    @Autowired
    private MtMaterialCategoryMapper mtMaterialCategoryMapper;
    
    @Override
    public Page<MtMaterialCategoryVO3> listMaterialCategoryForUi(Long tenantId, MtMaterialCategoryVO2 dto,
                                                                 PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest,()-> mtMaterialCategoryMapper.selectByConditionForUi(tenantId, dto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveMaterialCategoryForUi(Long tenantId, MtMaterialCategoryDTO2 dto) {
        
        //唯一性校验
        MtMaterialCategory oldCategory = new MtMaterialCategory();
        oldCategory.setTenantId(tenantId);
        oldCategory.setCategoryCode(dto.getCategoryCode());
        oldCategory = mtMaterialCategoryMapper.selectOne(oldCategory);
        if (oldCategory != null) {
            if (StringUtils.isEmpty(dto.getMaterialCategoryId()) 
                            || !dto.getMaterialCategoryId().equals(oldCategory.getMaterialCategoryId())) {
                throw new MtException("MT_MATERIAL_0081",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MATERIAL_0081",
                                                "MATERIAL", "categoryCode"));
            }
        }
        
        //执行保存
        MtMaterialCategory mtMaterialCategory = new MtMaterialCategory();
        BeanUtils.copyProperties(dto, mtMaterialCategory);
        mtMaterialCategory.setTenantId(tenantId);
        
        if (StringUtils.isNotEmpty(mtMaterialCategory.getMaterialCategoryId())) {
            updateByPrimaryKeySelective(mtMaterialCategory);
        } else {
            insertSelective(mtMaterialCategory);
        }
        
        return mtMaterialCategory.getMaterialCategoryId();
        
    }

    @Override
    public boolean verifyDefaultSetForUi(Long tenantId,String materialCategoryId) {
        
        MtMaterialCategory mtMaterialCategory = new MtMaterialCategory();
        mtMaterialCategory.setTenantId(tenantId);
        mtMaterialCategory.setMaterialCategoryId(materialCategoryId);
        mtMaterialCategory = mtMaterialCategoryMapper.selectOne(mtMaterialCategory);
        // 校验物料类别集是否为默认级别物料类别集
        if (!"Y".equals(mtMaterialCategorySetRepository.defaultCategorySetValidate(tenantId,
                        mtMaterialCategory.getMaterialCategorySetId()))) {
            throw new MtException("MT_MATERIAL_0039", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0039", "MATERIAL",""));
        }
        return true;
    }

}
