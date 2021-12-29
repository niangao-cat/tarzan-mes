package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeProcessNcLineService;
import com.ruike.hme.domain.repository.HmeProcessNcDetailRepository;
import com.ruike.hme.domain.vo.HmeProcessNcLineVO;
import com.ruike.hme.infra.mapper.HmeProcessNcDetailMapper;
import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeProcessNcLine;
import com.ruike.hme.domain.repository.HmeProcessNcLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
import tarzan.config.SwaggerApiConfig;

/**
 * 工序不良行表 管理 API
 *
 * @author li.zhang13@hand-china.com 2021-01-21 09:36:44
 */
@RestController("hmeProcessNcLineController.v1")
@RequestMapping("/v1/{organizationId}/hme-process-nc-lines")
@Api(tags = SwaggerApiConfig.HME_PROCESS_NC_LINE)
public class HmeProcessNcLineController extends BaseController {

    @Autowired
    private HmeProcessNcLineRepository hmeProcessNcLineRepository;
    @Autowired
    private HmeProcessNcLineService hmeProcessNcLineService;
    @Autowired
    private HmeProcessNcDetailRepository hmeProcessNcDetailRepository;

    @ApiOperation(value = "工序不良行表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeProcessNcLineVO>> list(@PathVariable("organizationId") Long tenantId,
                                                         String headerId,
                                                         PageRequest pageRequest) {
        Page<HmeProcessNcLineVO> list = hmeProcessNcLineRepository.selectLine(tenantId,headerId,pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "创建工序不良行表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/insert")
    public ResponseEntity<HmeProcessNcLine> create(@PathVariable("organizationId") Long tenantId,
                                                     HmeProcessNcLine hmeProcessNcLine) {
        hmeProcessNcLineService.createLine(tenantId,hmeProcessNcLine);
        return Results.success(hmeProcessNcLine);
    }

    @ApiOperation(value = "修改工序不良行表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/update")
    public ResponseEntity<HmeProcessNcLine> update(@PathVariable("organizationId") Long tenantId,
                                                   HmeProcessNcLine hmeProcessNcLine) {
        hmeProcessNcLineService.updateLine(tenantId,hmeProcessNcLine);
        return Results.success(hmeProcessNcLine);
    }

    @ApiOperation(value = "删除工序不良行表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/delete")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> remove(@PathVariable("organizationId") Long tenantId,
                                    HmeProcessNcLine hmeProcessNcLine) {
        hmeProcessNcDetailRepository.deleteByLine(tenantId,hmeProcessNcLine);
        hmeProcessNcLineRepository.deleteByPrimaryKey(hmeProcessNcLine.getLineId());
        return Results.success();
    }

}
