package tarzan.material.app.service.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.common.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.material.api.dto.MtMaterialCategorySetDTO;
import tarzan.material.app.service.MtMaterialCategorySetService;
import tarzan.material.domain.entity.MtMaterialCategorySet;
import tarzan.material.infra.mapper.MtMaterialCategorySetMapper;

/**
 * 物料类别集应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
@Service
public class MtMaterialCategorySetServiceImpl extends BaseServiceImpl<MtMaterialCategorySet>
                implements MtMaterialCategorySetService {
    private static String YES = "Y";
    @Autowired
    private MtMaterialCategorySetMapper mtMaterialCategorySetMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    public Page<MtMaterialCategorySet> listUi(Long tenantId, MtMaterialCategorySetDTO dto, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest,
                        () -> mtMaterialCategorySetMapper.selectList(tenantId, dto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String materialCategorySetUpdate(Long tenantId, MtMaterialCategorySet dto) {
        // 校验唯一性
        MtMaterialCategorySet mtMaterialCategorySet = new MtMaterialCategorySet();
        mtMaterialCategorySet.setTenantId(tenantId);
        mtMaterialCategorySet.setCategorySetCode(dto.getCategorySetCode());
        List<MtMaterialCategorySet> mtMaterialCategorySets = mtMaterialCategorySetMapper.select(mtMaterialCategorySet);
        if (mtMaterialCategorySets != null && mtMaterialCategorySets.size() > 0) {
            if (StringUtils.isEmpty(dto.getMaterialCategorySetId())) {
                throw new MtException("MT_MATERIAL_0083", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0083", "MATERIAL"));
            } else {
                mtMaterialCategorySet = mtMaterialCategorySets.get(0);
                if (!mtMaterialCategorySet.getMaterialCategorySetId().equals(dto.getMaterialCategorySetId())) {
                    throw new MtException("MT_MATERIAL_0083", mtErrorMessageRepository
                                    .getErrorMessageWithModule(tenantId, "MT_MATERIAL_0083", "MATERIAL"));
                }
            }
        }
        if(StringUtils.isNotEmpty(dto.getMaterialCategorySetId())){
            mtMaterialCategorySet = new MtMaterialCategorySet();
            mtMaterialCategorySet.setTenantId(tenantId);
            mtMaterialCategorySet.setMaterialCategorySetId(dto.getMaterialCategorySetId());
            mtMaterialCategorySet = mtMaterialCategorySetMapper.selectOne(mtMaterialCategorySet);
        }

        MtMaterialCategorySet query;
        boolean exists = false;
        // 只允许存在一个计划默认类别集
        if (YES.equals(dto.getDefaultScheduleFlag())) {
            exists = StringUtils.isEmpty(dto.getMaterialCategorySetId())
                    || (StringUtils.isNotEmpty(dto.getMaterialCategorySetId()) && !YES.equals(mtMaterialCategorySet.getDefaultScheduleFlag()));
            if (exists) {
                query = new MtMaterialCategorySet();
                query.setDefaultScheduleFlag(YES);
                query.setTenantId(tenantId);
                Criteria criteria = new Criteria(query);
                criteria.where(MtMaterialCategorySet.FIELD_DEFAULT_SCHEDULE_FLAG,
                        MtMaterialCategorySet.FIELD_TENANT_ID);
                List<MtMaterialCategorySet> all = mtMaterialCategorySetMapper.selectOptional(query, criteria);
                if (CollectionUtils.isNotEmpty(all)) {
                    throw new MtException("MT_MATERIAL_0056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0056", "MATERIAL"));
                }
            }
        }

        // 只允许存在一个采购默认类别集
        if (YES.equals(dto.getDefaultPurchaseFlag())) {
            exists = StringUtils.isEmpty(dto.getMaterialCategorySetId())
                    || (StringUtils.isNotEmpty(dto.getMaterialCategorySetId()) && !YES.equals(mtMaterialCategorySet.getDefaultPurchaseFlag()));
            if (exists) {
                query = new MtMaterialCategorySet();
                query.setDefaultPurchaseFlag(YES);
                query.setTenantId(tenantId);
                Criteria criteria = new Criteria(query);
                criteria.where(MtMaterialCategorySet.FIELD_DEFAULT_PURCHASE_FLAG, MtMaterialCategorySet.FIELD_TENANT_ID);
                List<MtMaterialCategorySet> all = mtMaterialCategorySetMapper.selectOptional(query, criteria);
                if (CollectionUtils.isNotEmpty(all)) {
                    throw new MtException("MT_MATERIAL_0058", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0058", "MATERIAL"));
                }
            }
        }

        // 只允许存在一个生产默认类别集
        if (YES.equals(dto.getDefaultManufacturingFlag())) {
            exists = StringUtils.isEmpty(dto.getMaterialCategorySetId())
                    || (StringUtils.isNotEmpty(dto.getMaterialCategorySetId()) && !YES.equals(mtMaterialCategorySet.getDefaultManufacturingFlag()));
            if (exists) {
                query = new MtMaterialCategorySet();
                query.setDefaultManufacturingFlag(YES);
                query.setTenantId(tenantId);
                Criteria criteria = new Criteria(query);
                criteria.where(MtMaterialCategorySet.FIELD_DEFAULT_MANUFACTURING_FLAG, MtMaterialCategorySet.FIELD_TENANT_ID);
                List<MtMaterialCategorySet> all = mtMaterialCategorySetMapper.selectOptional(query, criteria);
                if (CollectionUtils.isNotEmpty(all)) {
                    throw new MtException("MT_MATERIAL_0057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0057", "MATERIAL"));
                }
            }
        }

        int result = 0;
        dto.setTenantId(tenantId);
        if (StringUtils.isEmpty(dto.getMaterialCategorySetId())) {
            result = insertSelective(dto);
        } else {
            result = updateByPrimaryKeySelective(dto);
        }

        return String.valueOf(result);
    }

}
