package tarzan.method.infra.repository.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.domain.entity.MtDispositionGroupMember;
import tarzan.method.domain.repository.MtDispositionGroupMemberRepository;
import tarzan.method.infra.mapper.MtDispositionGroupMemberMapper;

/**
 * 处置组分配 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:47
 */
@Component
public class MtDispositionGroupMemberRepositoryImpl extends BaseRepositoryImpl<MtDispositionGroupMember>
                implements MtDispositionGroupMemberRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtDispositionGroupMemberMapper mtDispositionGroupMemberMapper;

    @Override
    public List<MtDispositionGroupMember> dispositionGroupMemberQuery(Long tenantId, String dispositionGroupId) {

        if (StringUtils.isEmpty(dispositionGroupId)) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "dispositionGroupId", "【API:dispositionGroupMemberQuery】"));

        }
        MtDispositionGroupMember member = new MtDispositionGroupMember();
        member.setTenantId(tenantId);
        member.setDispositionGroupId(dispositionGroupId);
        return mtDispositionGroupMemberMapper.select(member);
    }

    @Override
    public List<MtDispositionGroupMember> dispositionFunctionLimitDispositionGroupQuery(Long tenantId,
                    String dispositionFunctionId) {
        if (StringUtils.isEmpty(dispositionFunctionId)) {
            throw new MtException("MT_NC_CODE_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_NC_CODE_0001", "NC_CODE",
                                            "dispositionFunctionId",
                                            "【API:dispositionFunctionLimitDispositionGroupQuery】"));
        }

        MtDispositionGroupMember member = new MtDispositionGroupMember();
        member.setTenantId(tenantId);
        member.setDispositionFunctionId(dispositionFunctionId);
        return mtDispositionGroupMemberMapper.select(member);
    }
}
