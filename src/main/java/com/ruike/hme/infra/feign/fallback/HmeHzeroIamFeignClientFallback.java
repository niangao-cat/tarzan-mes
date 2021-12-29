package com.ruike.hme.infra.feign.fallback;

import com.ruike.hme.api.dto.HmeHzeroIamUserDTO;
import com.ruike.hme.api.dto.TenantDTO;
import com.ruike.hme.infra.feign.HmeHzeroIamFeignClient;
import io.choerodon.core.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Classname HmeHzeroIamFeignClientFallback
 * @Description iam调用失败回调
 * @Date 2020/7/22 10:40
 * @Author sanfeng.zhang
 */
@Component
public class HmeHzeroIamFeignClientFallback implements HmeHzeroIamFeignClient {


    @Override
    public ResponseEntity<HmeHzeroIamUserDTO> getUserInfo(@PathVariable("organizationId") Long organizationId,
                                                          @RequestParam("condition") String condition, @RequestParam("userType") String userType) {
        return new ResponseEntity<HmeHzeroIamUserDTO>(HttpStatus.NO_CONTENT);
    }

    @Override
    public Page<TenantDTO> selectTenantPage(Long organizationId) {
        return null;
    }

}
