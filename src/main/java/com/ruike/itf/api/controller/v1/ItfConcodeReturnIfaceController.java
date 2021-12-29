package com.ruike.itf.api.controller.v1;


import com.ruike.itf.api.dto.ItfConcodeReturnIfaceDTO;
import com.ruike.itf.api.dto.ItfConcodeReturnIfaceDTO1;
import com.ruike.itf.domain.repository.ItfConcodeReturnIfaceRepository;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



/**
 * 成品出入库容器信息返回接口表 API
 *
 * @author taowen.wang@hand-china.com 2021/6/30 11:10
 */
@RestController("itfConcodeReturnIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-concode-return-iface")
@Slf4j
public class ItfConcodeReturnIfaceController extends BaseController {

    @Autowired
    private ItfConcodeReturnIfaceRepository itfConcodeReturnIfaceRepository;

    @ApiOperation(value = "成品出入库容器信息返回接口表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<ItfConcodeReturnIfaceDTO> itfConcodeReturnIfaceByContainerCode(@PathVariable Long organizationId,@RequestBody ItfConcodeReturnIfaceDTO1 itfConcodeReturnIfaceDTO1){
        long startTime = System.currentTimeMillis();
        log.info("<====【ItfReceiveMaterialProductionOrderController-insert】成品出入库容器信息返回接口列表开始时间: {},单位毫秒", startTime);
        ItfConcodeReturnIfaceDTO itfConcodeReturnIfaceDTO = itfConcodeReturnIfaceRepository.itfConcodeReturnIfaceByContainerCode(organizationId,itfConcodeReturnIfaceDTO1);
        log.info("<====【ItfReceiveMaterialProductionOrderController-insert】成品出入库容器信息返回接口列表结束时间: {},用时：{}单位毫秒", System.currentTimeMillis(), System.currentTimeMillis() - startTime);
        return Results.success(itfConcodeReturnIfaceDTO);
    }

}
