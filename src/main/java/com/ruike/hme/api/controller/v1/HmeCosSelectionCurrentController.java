package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeCosSelectionCurrentDTO;
import com.ruike.hme.app.service.HmeCosSelectionCurrentService;
import com.ruike.hme.domain.vo.HmeCosSelectionCurrentVO;
import com.ruike.hme.domain.vo.HmeCosSelectionCurrentVO2;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeCosSelectionCurrent;
import com.ruike.hme.domain.repository.HmeCosSelectionCurrentRepository;
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
 * COS筛选电流点维护表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-08-18 11:07:41
 */
@RestController("hmeCosSelectionCurrentController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-selection-currents")
public class HmeCosSelectionCurrentController extends BaseController {

    @Autowired
    private HmeCosSelectionCurrentService hmeCosSelectionCurrentService;

    @ApiOperation(value = "分页查询COS筛选电流点维护表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeCosSelectionCurrentVO>> cosSelectionCurrentPageQuery(@PathVariable("organizationId") Long tenantId, HmeCosSelectionCurrentDTO dto,
                                                               PageRequest pageRequest) {
        Page<HmeCosSelectionCurrentVO> resultPage = hmeCosSelectionCurrentService.cosSelectionCurrentPageQuery(tenantId, dto, pageRequest);
        return Results.success(resultPage);
    }

    @ApiOperation(value = "创建或更新COS筛选电流点维护表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<?> cosSelectionCurrentCreateOrUpdate(@PathVariable("organizationId") Long tenantId,
                                                               @RequestBody HmeCosSelectionCurrentVO dto) {
        hmeCosSelectionCurrentService.cosSelectionCurrentCreateOrUpdate(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "分页查询COS筛选电流点维护历史")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/his-query/{cosId}")
    public ResponseEntity<Page<HmeCosSelectionCurrentVO2>> cosSelectionCurrentHisPageQuery(@PathVariable("organizationId") Long tenantId,
                                                                                           @PathVariable("cosId") String cosId,
                                                                                           PageRequest pageRequest) {
        Page<HmeCosSelectionCurrentVO2> resultPage = hmeCosSelectionCurrentService.cosSelectionCurrentHisPageQuery(tenantId, cosId, pageRequest);
        return Results.success(resultPage);
    }
}
