package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeCosDegreeTestActualDTO;
import com.ruike.hme.app.service.HmeCosDegreeTestActualService;
import com.ruike.hme.domain.vo.HmeCosDegreeTestActualVO3;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeCosDegreeTestActual;
import com.ruike.hme.domain.repository.HmeCosDegreeTestActualRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hzero.mybatis.helper.SecurityTokenHelper;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 偏振度和发散角测试结果 管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-09-13 10:02:48
 */
@RestController("hmeCosDegreeTestActualController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-degree-test-actuals")
public class HmeCosDegreeTestActualController extends BaseController {

    @Autowired
    private HmeCosDegreeTestActualService hmeCosDegreeTestActualService;

    @ApiOperation(value = "偏振度&发散角良率计算JOB")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/job")
    public ResponseEntity<?> dopAndDivergenceComputeJob(@PathVariable("organizationId") Long tenantId) {
        hmeCosDegreeTestActualService.dopAndDivergenceComputeJob(tenantId);
        return Results.success();
    }

    @ApiOperation(value = "偏振度&发散角放行分页查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/page-query")
    public ResponseEntity<Page<HmeCosDegreeTestActualVO3>> cosDegreeTestActualPageQuery(@PathVariable("organizationId") Long tenantId,
                                                                                        HmeCosDegreeTestActualDTO dto, PageRequest pageRequest) {
        return Results.success(hmeCosDegreeTestActualService.cosDegreeTestActualPageQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "偏振度&发散角放行更新")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/update")
    public ResponseEntity<?> cosDegreeTestActualUpdate(@PathVariable("organizationId") Long tenantId,
                                                       @RequestBody HmeCosDegreeTestActualVO3 dto) {
        hmeCosDegreeTestActualService.cosDegreeTestActualUpdate(tenantId, dto);
        return Results.success();
    }

}
