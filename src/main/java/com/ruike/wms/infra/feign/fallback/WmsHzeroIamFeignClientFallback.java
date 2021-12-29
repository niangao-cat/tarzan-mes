package com.ruike.wms.infra.feign.fallback;

import com.ruike.wms.api.dto.WmsUserInfoDTO;
import com.ruike.wms.infra.feign.WmsHzeroIamFeignClient;
import com.ruike.wms.infra.value.UserVO2;
import com.ruike.wms.infra.value.UserVO4;
import io.choerodon.core.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @Classname HzeroIamFeignClientFallback
 * @Description Iam调用失败回调
 * @Date 2019/9/24 10:40
 * @Author by {HuangYuBin}
 */
@Component
public class WmsHzeroIamFeignClientFallback implements WmsHzeroIamFeignClient {

    @Override
    public ResponseEntity<WmsUserInfoDTO> getUserInfo(Long organizationId, Long userId) {
        return new ResponseEntity<WmsUserInfoDTO>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Page<WmsUserInfoDTO>> queryUser(Long organizationId, String realName) {
        return new ResponseEntity<Page<WmsUserInfoDTO>>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Page<WmsUserInfoDTO>> queryUserByLoginName(Long organizationId, String loginName) {
        return new ResponseEntity<Page<WmsUserInfoDTO>>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<UserVO2> createUser(UserVO2 userVo2) {
        return new ResponseEntity<UserVO2>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<UserVO4> queryUserInformationByLoginName(Long organizationId, String loginName) {
        return new ResponseEntity<UserVO4>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<UserVO4> updateUser(UserVO4 userVo4) {
        return new ResponseEntity<UserVO4>(HttpStatus.NO_CONTENT);
    }
}