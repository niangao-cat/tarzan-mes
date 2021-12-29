package com.ruike.hme.infra.feign;

import com.ruike.hme.api.dto.HmeHzeroIamUserDTO;
import com.ruike.hme.api.dto.TenantDTO;
import com.ruike.hme.infra.feign.fallback.HmeHzeroIamFeignClientFallback;
import io.choerodon.core.domain.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * iam服务调用
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/22 16:23
 */
@FeignClient(value = "hzero-iam", fallback = HmeHzeroIamFeignClientFallback.class)
public interface HmeHzeroIamFeignClient {

    /**
     * 根据用户名或邮箱或手机查询用户信息
     *
     * @param organizationId 租户ID
     * @param condition      名称/邮箱/手机
     * @param userType       用户类型 默认P
     * @return : org.springframework.http.ResponseEntity<com.ruike.wms.api.dto.WmsUserInfoDTO>
     * @author sanfeng.zhang 2020/7/14 20:23
     */
    @GetMapping("/hzero/v1/{organizationId}/users")
    ResponseEntity<HmeHzeroIamUserDTO> getUserInfo(@PathVariable("organizationId") Long organizationId,
                                                   @RequestParam("condition") String condition, @RequestParam("userType") String userType);

    /**
     * 查询租户
     *
     * @param organizationId 租户ID
     * @return : org.springframework.http.ResponseEntity<com.ruike.wms.api.dto.WmsUserInfoDTO>
     * @author sanfeng.zhang 2020/7/14 20:23
     */
    @GetMapping("/v1/{organizationId}/tenants/paging-tenant")
    Page<TenantDTO> selectTenantPage(@PathVariable("organizationId") Long organizationId);

}
