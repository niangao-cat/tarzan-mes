package tarzan.general.infra.repository.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.general.infra.mapper.MtUserOrganizationMapper;

/**
 * 用户组织关系表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:40
 */
@Component
public class MtUserOrganizationRepositoryImpl extends BaseRepositoryImpl<MtUserOrganization>
                implements MtUserOrganizationRepository {
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtUserOrganizationMapper mtUserOrganizationMapper;

    @Override
    public List<MtUserOrganization> userOrganizationPermissionQuery(Long tenantId, MtUserOrganization dto) {
        if (dto.getUserId() == null) {
            throw new MtException("MT_PERMISSION_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_PERMISSION_0002", "PERMISSION", "userId", "【API:userOrganizationPermissionQuery】"));
        }
        MtUserOrganization tmp = new MtUserOrganization();
        tmp.setTenantId(tenantId);
        tmp.setUserId(dto.getUserId());
        tmp.setEnableFlag("Y");
        tmp.setOrganizationType(dto.getOrganizationType());
        return mtUserOrganizationMapper.select(tmp);
    }

    @Override
    public MtUserOrganization userDefaultOrganizationGet(Long tenantId, MtUserOrganization dto) {
        if (dto.getUserId() == null) {
            throw new MtException("MT_PERMISSION_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_PERMISSION_0002", "PERMISSION", "userId", "【API:userDefaultOrganizationGet】"));
        }
        if (StringUtils.isEmpty(dto.getOrganizationType())) {
            throw new MtException("MT_PERMISSION_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_PERMISSION_0002",
                                            "PERMISSION", "organizationType", "【API:userDefaultOrganizationGet】"));
        }

        MtUserOrganization tmp = new MtUserOrganization();
        tmp.setTenantId(tenantId);
        tmp.setUserId(dto.getUserId());
        tmp.setEnableFlag("Y");
        tmp.setOrganizationType(dto.getOrganizationType());
        tmp.setDefaultOrganizationFlag("Y");
        List<MtUserOrganization> ls = mtUserOrganizationMapper.select(tmp);
        if (CollectionUtils.isEmpty(ls)) {
            return null;
        } else if (ls.size() > 1) {

            MtGenType t = this.mtGenTypeRepository.getGenType(tenantId, "PERMISSION", "USER_ORGANIZATION_TYPE",
                            dto.getOrganizationType());

            throw new MtException("MT_PERMISSION_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_PERMISSION_0001",
                                            "PERMISSION", t.getDescription(), "【API:userDefaultOrganizationGet】"));

        }
        return ls.get(0);
    }

    @Override
    public void userOrganizationPermissionValidate(Long tenantId, MtUserOrganization dto) {
        if (dto.getUserId() == null) {
            throw new MtException("MT_PERMISSION_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_PERMISSION_0002", "PERMISSION", "userId", "【API:userOrganizationPermissionValidate】"));
        }
        if (StringUtils.isEmpty(dto.getOrganizationType())) {
            throw new MtException("MT_PERMISSION_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_PERMISSION_0002",
                                            "PERMISSION", "organizationType",
                                            "【API:userOrganizationPermissionValidate】"));
        }
        if (StringUtils.isEmpty(dto.getOrganizationId())) {
            throw new MtException("MT_PERMISSION_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_PERMISSION_0002",
                                            "PERMISSION", "organizationId",
                                            "【API:userOrganizationPermissionValidate】"));
        }

        MtUserOrganization tmp = new MtUserOrganization();
        tmp.setTenantId(tenantId);
        tmp.setEnableFlag("Y");
        tmp.setUserId(dto.getUserId());
        tmp.setOrganizationType(dto.getOrganizationType());
        tmp.setOrganizationId(dto.getOrganizationId());

        List<MtUserOrganization> ls = mtUserOrganizationMapper.select(tmp);
        if (CollectionUtils.isEmpty(ls)) {
            throw new MtException("MT_PERMISSION_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_PERMISSION_0003",
                                            "PERMISSION", "organizationId",
                                            "【API:userOrganizationPermissionValidate】"));
        }
    }

}
