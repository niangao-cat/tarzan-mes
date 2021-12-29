package io.tarzan.common.infra.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.choerodon.core.domain.Page;
import io.tarzan.common.domain.vo.MtUserVO3;
import io.tarzan.common.infra.feign.failback.MtUserServiceImpl;

@FeignClient(value = "${hzero.service.iam:hzero-iam}", fallback = MtUserServiceImpl.class)
public interface MtUserService {
    /**
     *  新版本获取用户，最多只能查询400（size = 400）
     *
     * @author chuang.yang
     * @date 2019/10/23
     * @param tenantId
     * @param organizationId
     * @param size
     */
    @GetMapping({"/hzero/v1/{organizationId}/users/paging"})
    ResponseEntity<Page<MtUserVO3>> userByOrganization(@PathVariable("organizationId") Long tenantId,
                                                       @RequestParam(name = "organizationId") Long organizationId, @RequestParam(name = "size") Integer size);
}
