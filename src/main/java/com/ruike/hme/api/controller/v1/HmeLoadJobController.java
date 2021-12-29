package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeLoadJobDTO;
import com.ruike.hme.api.dto.HmeLoadJobDTO2;
import com.ruike.hme.app.service.HmeLoadJobService;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeLoadJob;
import com.ruike.hme.domain.repository.HmeLoadJobRepository;
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

import java.util.List;

/**
 * 装载信息作业记录表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-02-01 11:09:48
 */
@RestController("hmeLoadJobController.v1")
@RequestMapping("/v1/{organizationId}/hme-load-jobs")
public class HmeLoadJobController extends BaseController {

    @Autowired
    private HmeLoadJobService hmeLoadJobService;

    @ApiOperation(value = "分页查询")
    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeLoadJobDTO>> pageList(@PathVariable("organizationId") Long tenantId,
                                                        HmeLoadJobDTO2 dto, PageRequest pageRequest) {
        return Results.success(hmeLoadJobService.pageList(tenantId, dto, pageRequest));
    }
}
