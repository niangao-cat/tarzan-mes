package io.tarzan.common.infra.feign.failback;

import io.tarzan.common.domain.vo.MtRoleVO;
import io.tarzan.common.infra.feign.MtMemberRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MtMemberRoleServiceImpl implements MtMemberRoleService {
    @Override
    public ResponseEntity<List<MtRoleVO>> listSelfRoles() {
        return null;
    }
}
