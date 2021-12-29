package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeProcessNcHeaderDTO;
import com.ruike.hme.app.service.HmeProcessNcHeaderService;
import com.ruike.hme.domain.repository.HmeProcessNcDetailRepository;
import com.ruike.hme.domain.repository.HmeProcessNcLineRepository;
import com.ruike.hme.domain.vo.HmeProcessNcHeaderVO;
import com.ruike.hme.domain.vo.HmeProcessNcVO;
import io.swagger.annotations.Api;
import io.tarzan.common.domain.entity.MtGenType;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeProcessNcHeader;
import com.ruike.hme.domain.repository.HmeProcessNcHeaderRepository;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
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
import springfox.documentation.service.Tag;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 工序不良头表 管理 API
 *
 * @author li.zhang13@hand-china.com 2021-01-21 09:36:44
 */
@RestController("hmeProcessNcHeaderController.v1")
@RequestMapping("/v1/{organizationId}/hme-process-nc-headers")
@Api(tags = SwaggerApiConfig.HME_PROCESS_NC_HEADER)
public class HmeProcessNcHeaderController extends BaseController {

    @Autowired
    private HmeProcessNcHeaderRepository hmeProcessNcHeaderRepository;
    @Autowired
    private HmeProcessNcHeaderService hmeProcessNcHeaderService;
    @Autowired
    private HmeProcessNcLineRepository hmeProcessNcLineRepository;
    @Autowired
    private HmeProcessNcDetailRepository hmeProcessNcDetailRepository;

    @ApiOperation(value = "工序不良头表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeProcessNcHeaderVO>> list(@PathVariable("organizationId") Long tenantId,HmeProcessNcHeaderDTO hmeProcessNcHeaderDTO,  PageRequest pageRequest) {
        Page<HmeProcessNcHeaderVO> list = hmeProcessNcHeaderRepository.selectProcessHeader(tenantId, pageRequest, hmeProcessNcHeaderDTO);
        return Results.success(list);
    }

    @ApiOperation(value = "创建工序不良头表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/insert")
    public ResponseEntity<HmeProcessNcHeader> create(@PathVariable("organizationId") Long tenantId,
                                                     HmeProcessNcHeader hmeProcessNcHeader) {
        hmeProcessNcHeaderService.createHeader(tenantId,hmeProcessNcHeader);
        return Results.success(hmeProcessNcHeader);
    }

    @ApiOperation(value = "修改工序不良头表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/update")
    public ResponseEntity<HmeProcessNcHeader> update(@PathVariable("organizationId") Long tenantId,HmeProcessNcHeader hmeProcessNcHeader) {
        hmeProcessNcHeaderService.updateHeader(tenantId,hmeProcessNcHeader);
        return Results.success(hmeProcessNcHeader);
    }

    @ApiOperation(value = "删除工序不良头表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/delete")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> remove(@PathVariable("organizationId") Long tenantId,
                                    HmeProcessNcHeader hmeProcessNcHeader) {
        hmeProcessNcLineRepository.deleteLineByHeader(tenantId,hmeProcessNcHeader.getHeaderId());
        hmeProcessNcDetailRepository.deleteDetailByHeader(tenantId,hmeProcessNcHeader.getHeaderId());
        hmeProcessNcHeaderRepository.deleteByPrimaryKey(hmeProcessNcHeader.getHeaderId());
        return Results.success();
    }

    @ApiOperation(value = "工序不良判定标准维护-导出")
    @GetMapping(value = "/export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(HmeProcessNcVO.class)
    public ResponseEntity<List<HmeProcessNcVO>> processNcExport(@PathVariable("organizationId") Long tenantId,
                                                               ExportParam exportParam,
                                                               HttpServletResponse response,
                                                               HmeProcessNcHeaderDTO dto) {
        return Results.success(hmeProcessNcHeaderRepository.processNcExport(tenantId, dto));
    }

}
