package com.ruike.hme.infra.feign.fallback;

import com.ruike.hme.api.dto.HmeHzeroFileDTO;
import com.ruike.hme.api.dto.HmeHzeroPlatformUnitDTO;
import com.ruike.hme.infra.feign.HmeHzeroFileFeignClient;
import com.ruike.hme.infra.feign.HmeHzeroPlatformFeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @Classname HmeHzeroFileFeignClientFallback
 * @Description File调用失败回调
 * @Date 2020/7/22 10:40
 * @Author sanfeng.zhang
 */
@Component
public class HmeHzeroFileFeignClientFallback implements HmeHzeroFileFeignClient {

    @Override
    public ResponseEntity<List<HmeHzeroFileDTO>> getUnitsInfo(@PathVariable("organizationId") Long organizationId,
                                                              @PathVariable("attachmentUUID") String attachmentUUID){
        return new ResponseEntity<List<HmeHzeroFileDTO>>(HttpStatus.NO_CONTENT);
    }

}