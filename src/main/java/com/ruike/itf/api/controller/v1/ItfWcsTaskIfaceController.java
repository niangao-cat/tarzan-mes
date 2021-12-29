package com.ruike.itf.api.controller.v1;


import com.ruike.itf.api.dto.ItfWcsTaskIfaceDTO1;
import com.ruike.itf.api.dto.ItfWcsTaskIfaceDTO2;
import com.ruike.itf.domain.repository.ItfWcsTaskIfaceRepository;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 出库任务状态回传接口  API
 *
 * @author taowen.wang@hand-china.com 2021/7/5 10:56
 */
@RestController("itfWcsTaskIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-wcs-task-iface")
@Slf4j
public class ItfWcsTaskIfaceController {
    @Autowired
    private ItfWcsTaskIfaceRepository itfWcsTaskIfaceRepository;

    @ApiOperation(value = "出库任务状态回传接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<ItfWcsTaskIfaceDTO2> itfConcodeReturnIfaceByContainerCode(@RequestBody ItfWcsTaskIfaceDTO1 itfWcsTaskIfaceDTO1){
        long startTime = System.currentTimeMillis();
        log.info("<====【ItfWcsTaskIfaceController-update】出库任务状态回传接口列表开始时间: {},单位毫秒", startTime);
        ItfWcsTaskIfaceDTO2 itfWcsTaskIfaceDTO2 = itfWcsTaskIfaceRepository.ItfWcsTaskIfaceUpdate(itfWcsTaskIfaceDTO1);
        log.info("<====【ItfWcsTaskIfaceController-update】出库任务状态回传接口列表结束时间: {},用时：{}单位毫秒", System.currentTimeMillis(), System.currentTimeMillis() - startTime);
        return Results.success(itfWcsTaskIfaceDTO2);
    }
}
