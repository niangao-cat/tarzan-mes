package tarzan.method.app.service.impl;

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

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.api.dto.MtExtendAttrDTO;
import io.tarzan.common.app.service.MtExtendSettingsService;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.api.dto.MtNcGroupDTO;
import tarzan.method.api.dto.MtNcGroupDTO2;
import tarzan.method.api.dto.MtNcGroupDTO4;
import tarzan.method.api.dto.MtNcSecondaryCodeDTO;
import tarzan.method.app.service.MtNcGroupService;
import tarzan.method.app.service.MtNcSecondaryCodeService;
import tarzan.method.app.service.MtNcValidOperService;
import tarzan.method.domain.entity.MtNcGroup;
import tarzan.method.domain.repository.MtNcGroupRepository;
import tarzan.method.infra.mapper.MtNcGroupMapper;

/**
 * 不良代码组应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:18:39
 */
@Service
public class MtNcGroupServiceImpl implements MtNcGroupService {

    private static final String MT_NC_GROUP_ATTR = "mt_nc_group_attr";

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtNcGroupRepository mtNcGroupRepo;

    @Autowired
    private MtNcSecondaryCodeService mtNcSecondaryCodeService;

    @Autowired
    private MtNcValidOperService mtNcValidOperService;

    @Autowired
    private MtExtendSettingsService mtExtendSettingsService;

    @Autowired
    private MtNcGroupMapper mtNcGroupMapper;

    @Override
    public Page<MtNcGroupDTO> queryNcGroupListForUi(Long tenantId, MtNcGroupDTO dto, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> mtNcGroupMapper.queryNcGroupForUi(tenantId, dto));
    }

    @Override
    public MtNcGroupDTO2 queryNcGroupDetailForUi(Long tenantId, String ncGroupId) {
        if (StringUtils.isEmpty(ncGroupId)) {
            return null;
        }
        MtNcGroupDTO2 result = new MtNcGroupDTO2();

        MtNcGroupDTO queryNcGroupDTO = new MtNcGroupDTO();
        queryNcGroupDTO.setNcGroupId(ncGroupId);
        List<MtNcGroupDTO> ncGroupList = mtNcGroupMapper.queryNcGroupForUi(tenantId, queryNcGroupDTO);
        if (CollectionUtils.isEmpty(ncGroupList)) {
            return null;
        }

        List<MtExtendAttrDTO> ncGroupAttrList = mtExtendSettingsService.attrQuery(tenantId,
                        ncGroupList.get(0).getNcGroupId(), MT_NC_GROUP_ATTR);
        ncGroupList.get(0).setNcGroupAttrList(ncGroupAttrList);

        result.setMtNcGroup(ncGroupList.get(0));
        result.setMtNcSecondaryCodeList(mtNcSecondaryCodeService.querySecondaryCodeListForUi(tenantId,
                        ncGroupList.get(0).getNcGroupId(), "NC_GROUP"));
        result.setMtNcValidOperList(mtNcValidOperService.queryNcValidOperListForUi(tenantId,
                        ncGroupList.get(0).getNcGroupId(), "NC_GROUP"));
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveNcGroupForUi(Long tenantId, MtNcGroupDTO4 dto) {
        MtNcGroup ncGroup = new MtNcGroup();
        BeanUtils.copyProperties(dto.getMtNcGroup(), ncGroup);

        MtNcGroup vaildNc = new MtNcGroup();
        vaildNc.setTenantId(tenantId);
        vaildNc.setNcGroupId(ncGroup.getNcGroupId());
        vaildNc.setSiteId(ncGroup.getSiteId());
        vaildNc.setNcGroupCode(ncGroup.getNcGroupCode());

        Criteria criteria = new Criteria(vaildNc);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtNcGroup.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtNcGroup.FIELD_SITE_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtNcGroup.FIELD_NC_GROUP_CODE, Comparison.EQUAL));
        if (StringUtils.isNotEmpty(vaildNc.getNcGroupId())) {
            whereFields.add(new WhereField(MtNcGroup.FIELD_NC_GROUP_ID, Comparison.NOT_EQUAL));
        }
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        if (mtNcGroupMapper.selectOptional(vaildNc, criteria).size() > 0) {
            throw new MtException("MT_NC_CODE_0005",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_NC_CODE_0005", "NC_CODE"));
        }

        List<MtNcSecondaryCodeDTO> secondaryCodeList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dto.getMtNcSecondaryCodeList())) {
            secondaryCodeList.addAll(dto.getMtNcSecondaryCodeList());
        }
        if (StringUtils.isNotEmpty(ncGroup.getNcGroupId())) {
            secondaryCodeList.addAll(mtNcSecondaryCodeService.querySecondaryCodeListForUi(tenantId,
                            ncGroup.getNcGroupId(), "NC_GROUP"));
        }

        if ("Y".equals(ncGroup.getSecondaryReqdForClose())) {
            if (CollectionUtils.isEmpty(secondaryCodeList)
                            || secondaryCodeList.stream().noneMatch(c -> "Y".equals(c.getRequiredFlag()))) {
                throw new MtException("MT_NC_CODE_0006",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_NC_CODE_0006", "NC_CODE"));
            }
        }

        // 保存不良代码组
        ncGroup.setTenantId(tenantId);
        if (StringUtils.isEmpty(ncGroup.getNcGroupId())) {
            mtNcGroupRepo.insertSelective(ncGroup);
        } else {
            mtNcGroupRepo.updateByPrimaryKeySelective(ncGroup);
        }

        mtNcSecondaryCodeService.saveSecondaryCodeListForUi(tenantId, ncGroup.getNcGroupId(), "NC_GROUP",
                        dto.getMtNcSecondaryCodeList());
        mtNcValidOperService.saveNcValidOperListForUi(tenantId, ncGroup.getNcGroupId(), "NC_GROUP",
                        dto.getMtNcValidOperList());

        if (CollectionUtils.isNotEmpty(dto.getMtNcGroup().getNcGroupAttrList())) {
            mtExtendSettingsService.attrSave(tenantId, MT_NC_GROUP_ATTR, ncGroup.getNcGroupId(), null,
                            dto.getMtNcGroup().getNcGroupAttrList());
        }

        return ncGroup.getNcGroupId();
    }
}
