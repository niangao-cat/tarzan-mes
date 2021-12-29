package io.tarzan.common.infra.feign.failback;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.choerodon.core.domain.Page;
import io.tarzan.common.domain.vo.MtUserVO3;
import io.tarzan.common.infra.feign.MtUserService;

@Service
public class MtUserServiceImpl implements MtUserService {

    @Override
    public ResponseEntity<Page<MtUserVO3>> userByOrganization(Long tenantId, Long organizationId, Integer size) {
        return null;
    }
}
