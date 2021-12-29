package tarzan.method.infra.repository.impl;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.domain.entity.MtDispositionFunction;
import tarzan.method.domain.entity.MtDispositionGroup;
import tarzan.method.domain.entity.MtDispositionGroupMember;
import tarzan.method.domain.repository.MtDispositionFunctionRepository;
import tarzan.method.domain.repository.MtDispositionGroupMemberRepository;
import tarzan.method.domain.repository.MtDispositionGroupRepository;
import tarzan.method.infra.mapper.MtDispositionGroupMapper;

/**
 * 处置组 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:47
 */
@Component
public class MtDispositionGroupRepositoryImpl extends BaseRepositoryImpl<MtDispositionGroup>
                implements MtDispositionGroupRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtDispositionGroupMemberRepository mtDispositionGroupMemberRepo;

    @Autowired
    private MtDispositionFunctionRepository mtDispositionFunctionRepo;

    @Autowired
    private MtDispositionGroupMapper mtDispositionGroupMapper;

    @Override
    public MtDispositionGroup dispositionGroupPropertyGet(Long tenantId, String dispositionGroupId) {

        if (StringUtils.isEmpty(dispositionGroupId)) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "dispositionGroupId", "【API:dispositionGroupPropertyGet】"));

        }
        MtDispositionGroup group = new MtDispositionGroup();
        group.setTenantId(tenantId);
        group.setDispositionGroupId(dispositionGroupId);
        return mtDispositionGroupMapper.selectOne(group);
    }

    @Override
    public List<String> dispositionGroupRouterQuery(Long tenantId, String dispositionGroupId) {
        if (StringUtils.isEmpty(dispositionGroupId)) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "dispositionGroupId", "【API:dispositionGroupRouterQuery】"));

        }
        List<MtDispositionGroupMember> memberList =
                        mtDispositionGroupMemberRepo.dispositionGroupMemberQuery(tenantId, dispositionGroupId);
        List<MtDispositionFunction> functionList = new ArrayList<>();
        for (MtDispositionGroupMember t : memberList) {
            functionList.add(mtDispositionFunctionRepo.dispositionFunctionPropertyGet(tenantId,
                            t.getDispositionFunctionId()));
        }

        if (CollectionUtils.isEmpty(functionList)) {
            return Collections.emptyList();
        }

        return functionList.stream().filter(t -> "NC_ROUTER".equals(t.getFunctionType()))
                        .map(MtDispositionFunction::getRouterId).collect(toList());
    }
}
