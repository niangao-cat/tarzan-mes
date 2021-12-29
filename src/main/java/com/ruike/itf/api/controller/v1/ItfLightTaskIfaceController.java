package com.ruike.itf.api.controller.v1;

import com.ruike.itf.api.dto.ItfLightTaskIfaceDTO;
import com.ruike.itf.api.dto.ItfMaterialLotConfirmIfaceDTO;
import com.ruike.itf.app.service.ItfLightTaskIfaceService;
import com.ruike.itf.domain.vo.ItfLightTaskIfaceVO;
import com.ruike.itf.domain.vo.ItfMaterialLotConfirmIfaceVO4;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 亮灯指令接口表 管理 API
 *
 * @author li.zhang13@hand-china.com 2021/08/09 10:27
 */
@RestController("itfLightTaskIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-light-task-ifaces")
public class ItfLightTaskIfaceController extends BaseController {

    @Autowired
    private ItfLightTaskIfaceService itfLightTaskIfaceService;

    @ApiOperation(value = "亮灯指令接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<List<ItfLightTaskIfaceVO>> itfLightTaskIface(@PathVariable(value = "organizationId") Long tenantId,
                                                                                        @RequestBody List<ItfLightTaskIfaceDTO> dtoList){
        List<ItfLightTaskIfaceVO> resultList = itfLightTaskIfaceService.itfLightTaskIface(tenantId, dtoList);
        return Results.success(resultList);
    }

}
