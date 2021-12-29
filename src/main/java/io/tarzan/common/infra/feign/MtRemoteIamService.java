package io.tarzan.common.infra.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.choerodon.core.domain.Page;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.vo.MtRoleVO;
import io.tarzan.common.infra.feign.failback.MtRemoteIamServiceImpl;

@FeignClient(value = "${hzero.service.iam:hzero-iam}", fallback = MtRemoteIamServiceImpl.class)
public interface MtRemoteIamService {
        /**
         * 获取当前用户拥有的所有角色
         *
         * @return
         */
        @GetMapping({"/hzero/v1/{organizationId}/roles/self/roles"})
        ResponseEntity<List<MtRoleVO>> selfRoles(@PathVariable("organizationId") Long tenantId);

        /**
         * 获取所有用户，最多只能查询400（size = 400）
         *
         * @author chuang.yang
         * @date 2019/10/23
         * @param tenantId
         * @param size
         */
        @GetMapping({"/hzero/v1/{organizationId}/users/paging"})
        ResponseEntity<Page<MtUserInfo>> userAllInfoRemoteGet(@PathVariable("organizationId") Long tenantId,
                                                              @RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size);

        /**
         * 获取单个用户信息
         *
         * @param tenantId
         * @param userId
         * @return
         */
        @GetMapping({"/hzero/v1/{organizationId}/users/{userId}/info"})
        ResponseEntity<MtUserInfo> userInfoRemoteGet(@PathVariable("organizationId") Long tenantId,
                                                     @PathVariable("userId") Long userId);
}
