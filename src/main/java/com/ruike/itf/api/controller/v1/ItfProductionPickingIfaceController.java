package com.ruike.itf.api.controller.v1;

import com.ruike.itf.api.dto.ItfLightTaskIfaceDTO;
import com.ruike.itf.api.dto.ItfProductionPickingIfaceDTO;
import com.ruike.itf.app.service.ItfProductionPickingIfaceService;
import com.ruike.itf.domain.vo.ItfLightTaskIfaceVO;
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
 * 生产领料执行数据回传接口
 *
 * @author li.zhang13@hand-china.com 2021/08/11 10:42
 */
@RestController("itfProductionPickingIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-production-picking-ifaces")
public class ItfProductionPickingIfaceController extends BaseController {

    @Autowired
    private ItfProductionPickingIfaceService itfProductionPickingIfaceService;

    @ApiOperation(value = "生产领料执行数据回传接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<Void> itfProductionPickingIface(@PathVariable(value = "organizationId") Long tenantId,
                                                                       @RequestBody List<ItfProductionPickingIfaceDTO> dtoList){
        itfProductionPickingIfaceService.itfProductionPickingIface(tenantId, dtoList);
        return Results.success();
    }
}
