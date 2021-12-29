package com.ruike.hme.infra.feign;

import com.ruike.hme.api.dto.HmeHzeroFileDTO;
import com.ruike.hme.api.dto.HmeHzeroPlatformUnitDTO;
import com.ruike.hme.infra.feign.fallback.HmeHzeroFileFeignClientFallback;
import com.ruike.hme.infra.feign.fallback.HmeHzeroPlatformFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


/**
 * file服务调用
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/14 20:23
 */
@FeignClient(value = "hzero-file", fallback = HmeHzeroFileFeignClientFallback.class)
public interface HmeHzeroFileFeignClient {


    /**
     * 获取文件列表
     *
     * @param organizationId 租户ID
     * @param attachmentUUID 附件UUID
     * @return : org.springframework.http.ResponseEntity<com.ruike.wms.api.dto.WmsUserInfoDTO>
     * @author sanfeng.zhang 2020/7/14 20:23
     */
    @GetMapping("/v1/{organizationId}/files/{attachmentUUID}/file")
    ResponseEntity<List<HmeHzeroFileDTO>> getUnitsInfo(@PathVariable("organizationId") Long organizationId,
                                                       @PathVariable("attachmentUUID") String attachmentUUID);
}