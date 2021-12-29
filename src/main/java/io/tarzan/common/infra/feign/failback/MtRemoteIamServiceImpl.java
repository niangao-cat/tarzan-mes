package io.tarzan.common.infra.feign.failback;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import io.choerodon.core.domain.Page;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.vo.MtRoleVO;
import io.tarzan.common.infra.feign.MtRemoteIamService;
@Component
public class MtRemoteIamServiceImpl implements MtRemoteIamService {
    @Override
    public ResponseEntity<List<MtRoleVO>> selfRoles(Long tenantId) {
        return null;
    }

    @Override
    public ResponseEntity<Page<MtUserInfo>> userAllInfoRemoteGet(Long tenantId, Integer page, Integer size) {
        return null;
    }

    @Override
    public ResponseEntity<MtUserInfo> userInfoRemoteGet(Long tenantId, Long userId) {
        return null;
    }
}