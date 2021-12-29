package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeProcessNcDetailService;
import com.ruike.hme.domain.vo.HmeProcessNcDetailVO;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeProcessNcDetail;
import com.ruike.hme.domain.repository.HmeProcessNcDetailRepository;
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
 * 工序不良明细表 管理 API
 *
 * @author li.zhang13@hand-china.com 2021-01-21 09:36:44
 */
@RestController("hmeProcessNcDetailController.v1")
@RequestMapping("/v1/{organizationId}/hme-process-nc-details")
public class HmeProcessNcDetailController extends BaseController {

    @Autowired
    private HmeProcessNcDetailRepository hmeProcessNcDetailRepository;
    @Autowired
    private HmeProcessNcDetailService hmeProcessNcDetailService;

    @ApiOperation(value = "工序不良明细表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeProcessNcDetailVO>> list(@PathVariable("organizationId") Long tenantId,
                                                           String lineId,
                                                           PageRequest pageRequest) {
        Page<HmeProcessNcDetailVO> list = hmeProcessNcDetailRepository.selectDetail(tenantId,lineId,pageRequest);
        return Results.success(list);
    }


    @ApiOperation(value = "创建工序不良明细表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/insert")
    public ResponseEntity<HmeProcessNcDetail> create(@PathVariable("organizationId") Long tenantId,
                                                     HmeProcessNcDetail hmeProcessNcDetail) {
        hmeProcessNcDetailService.createDetail(tenantId,hmeProcessNcDetail);
        return Results.success(hmeProcessNcDetail);
    }

    @ApiOperation(value = "修改工序不良明细表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/update")
    public ResponseEntity<HmeProcessNcDetail> update(@PathVariable("organizationId") Long tenantId,
                                                     HmeProcessNcDetail hmeProcessNcDetail) {
        hmeProcessNcDetailService.updateDetail(tenantId,hmeProcessNcDetail);
        return Results.success(hmeProcessNcDetail);
    }

    @ApiOperation(value = "删除工序不良明细表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/delete")
    public ResponseEntity<?> remove(HmeProcessNcDetail hmeProcessNcDetail) {
        hmeProcessNcDetailRepository.deleteByPrimaryKey(hmeProcessNcDetail.getDetailId());
        return Results.success();
    }

}
