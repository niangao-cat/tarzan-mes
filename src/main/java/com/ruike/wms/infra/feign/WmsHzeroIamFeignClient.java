package com.ruike.wms.infra.feign;

import com.ruike.wms.api.dto.WmsUserInfoDTO;
import com.ruike.wms.infra.feign.fallback.WmsHzeroIamFeignClientFallback;
import com.ruike.wms.infra.value.UserVO2;
import com.ruike.wms.infra.value.UserVO4;
import io.choerodon.core.domain.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname HzeroIamFeignClient
 * @Description Iam服务调用
 * @Date 2019/9/24 10:36
 * @Author by {HuangYuBin}
 */
@FeignClient(value = "hzero-iam", fallback = WmsHzeroIamFeignClientFallback.class)
public interface WmsHzeroIamFeignClient {

    /**
     * 根据ID获取用户信息
     *
     * @param organizationId
     * @param userId
     * @return org.springframework.http.ResponseEntity<com.ruike.wms.api.controller.dto.UserInfoDTO>
     * @Description 根据ID获取用户信息
     * @Date 2019/9/24 11:42
     * @Created by {HuangYuBin}
     */
    @GetMapping("/hzero/v1/{organizationId}/users/{userId}/info")
    ResponseEntity<WmsUserInfoDTO> getUserInfo(@PathVariable("organizationId") Long organizationId,
                                               @PathVariable("userId") Long userId);

    /**
     * 根据真实姓名模糊查询用户信息
     *
     * @param organizationId
     * @param realName
     * @return org.springframework.http.ResponseEntity<com.ruike.wms.api.controller.dto.UserInfoDTO>
     * @Description 根据真实姓名模糊查询用户信息
     * @Date 2019/10/14 10:19
     * @Created by {HuangYuBin}
     */
    @GetMapping("/hzero/v1/{organizationId}/users/paging")
    ResponseEntity<Page<WmsUserInfoDTO>> queryUser(@PathVariable("organizationId") Long organizationId,
                                                   @RequestParam("realName") String realName);

    /**
     * 根据登录名查询用户信息
     *
     * @param organizationId
     * @param loginName
     * @return org.springframework.http.ResponseEntity<io.choerodon.core.domain.Page < com.ruike.wms.api.controller.dto.UserInfoDTO>>
     * @Description 根据登录名查询用户信息
     * @date 2019/12/23 9:00
     * @author wei.zheng
     */
    @GetMapping("/hzero/v1/{organizationId}/users/paging")
    ResponseEntity<Page<WmsUserInfoDTO>> queryUserByLoginName(@PathVariable("organizationId") Long organizationId,
                                                              @RequestParam("loginName") String loginName);

    /**
     * 创建用户
     *
     * @param userVO2
     * @return org.springframework.http.ResponseEntity<io.choerodon.core.domain.Page < com.ruike.wms.api.controller.dto.UserInfoDTO>>
     * @Description 创建用户
     * @date 2019/12/23 9:04
     * @author wei.zheng
     */
    @PostMapping("/hzero/v1/users/internal")
    ResponseEntity<UserVO2> createUser(@RequestBody UserVO2 userVO2);

    /**
     * 根据登录名查询用户信息
     *
     * @param organizationId
     * @param loginName
     * @return org.springframework.http.ResponseEntity<io.choerodon.core.domain.Page < com.ruike.wms.api.controller.dto.UserInfoDTO>>
     * @Description 根据登录名查询用户信息
     * @date 2019/12/23 9:00
     * @author junhui.liu
     */
    @GetMapping("/hzero/v1/{organizationId}/users/paging")
    ResponseEntity<UserVO4> queryUserInformationByLoginName(@PathVariable("organizationId") Long organizationId,
                                                            @RequestParam("loginName") String loginName);

    /**
     * 更新用户
     *
     * @param userVO2 用户数据
     * @return UserVO4
     */
    @PostMapping("/hzero/v1/users")
    ResponseEntity<UserVO4> updateUser(@RequestBody UserVO4 userVO2);
}