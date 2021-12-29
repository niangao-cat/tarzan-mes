package com.ruike.qms.api.controller.v1;

import com.ruike.qms.api.dto.QmsIqcAuditDTO;
import com.ruike.qms.api.dto.QmsIqcAuditQueryDTO;
import com.ruike.qms.domain.entity.QmsIqcDetails;
import com.ruike.qms.domain.repository.QmsIqcAuditRepository;
import com.ruike.qms.domain.vo.QmsIqcAuditQueryLineVO;
import com.ruike.qms.domain.vo.QmsIqcAuditQueryVO;
import com.ruike.qms.domain.vo.QmsIqcAuditQueryVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @program: tarzan-mes
 * @description: iqc审核控制层
 * @author: han.zhang
 * @create: 2020/05/19 11:50
 */
@RestController("qmsIqcAuditController.v1")
@RequestMapping("/v1/{organizationId}/iqc-audit")
@Api(tags = SwaggerApiConfig.QMS_IQC_AUDIT)
@Slf4j
public class QmsIqcAuditController extends BaseController {
    @Autowired
    private QmsIqcAuditRepository qmsIqcAuditRepository;

    /**
     * @Description 查询iqc头数据
     * @param tenantId 租户Id
     * @param qmsIqcCheckPlatformDTO 查询参数
     * @param pageRequest 页码请求
     * @return org.springframework.http.ResponseEntity<io.choerodon.core.domain.Page<com.ruike.qms.domain.vo.QmsIqcAuditQueryVO>>
     * @Date 2020-05-19 18:59
     * @Author han.zhang
     */
    @ApiOperation(value = "查询iqc头数据")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/list/head",produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<QmsIqcAuditQueryVO>> selectIqcHeader(@PathVariable("organizationId") Long tenantId, QmsIqcAuditQueryDTO qmsIqcCheckPlatformDTO
            , @ApiIgnore PageRequest pageRequest) {
        Page<QmsIqcAuditQueryVO> list = qmsIqcAuditRepository.selectIqcHeader(tenantId, qmsIqcCheckPlatformDTO,pageRequest);
        return Results.success(list);
    }

    /**
     * @Description 查询iqc行数据
     * @param tenantId 租户id
     * @param iqcHeaderId 头id
     * @param pageRequest 页码请求
     * @return org.springframework.http.ResponseEntity<io.choerodon.core.domain.Page<com.ruike.qms.domain.vo.QmsIqcAuditQueryLineVO>>
     * @Date 2020-05-19 19:35
     * @Author han.zhang
     */
    @ApiOperation(value = "查询iqc行数据")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/list/line/{iqcHeaderId}",produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<QmsIqcAuditQueryLineVO>> selectIqcLine(@PathVariable("organizationId") Long tenantId,@PathVariable("iqcHeaderId") String iqcHeaderId
            , @ApiIgnore PageRequest pageRequest) {
        Page<QmsIqcAuditQueryLineVO> list = qmsIqcAuditRepository.selectIqcLine(tenantId, iqcHeaderId,pageRequest);
        return Results.success(list);
    }

    /**
     * @Description 明细数据
     * @param tenantId 租户id
     * @param iqcLineId 行id
     * @param pageRequest 页码请求
     * @return org.springframework.http.ResponseEntity<io.choerodon.core.domain.Page<com.ruike.qms.domain.entity.QmsIqcDetails>>
     * @Date 2020-05-19 19:35
     * @Author han.zhang
     */
    @ApiOperation(value = "查询iqc明细数据")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/list/details/{iqcLineId}",produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<QmsIqcDetails>> selectIqcDetail(@PathVariable("organizationId") Long tenantId, @PathVariable("iqcLineId") String iqcLineId
            , @ApiIgnore PageRequest pageRequest) {
        Page<QmsIqcDetails> list = qmsIqcAuditRepository.selectIqcDetail(tenantId, iqcLineId,pageRequest);
        return Results.success(list);
    }

    /**
     * @Description 让步、挑选、退货
     * @param tenantId
     * @param iqcAuditDTO
     * @return org.springframework.http.ResponseEntity<?>
     * @Date 2020-05-20 14:09
     * @Author han.zhang
     */
    @ApiOperation(value = "让步、挑选、退货")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/audit",produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> audit(@PathVariable("organizationId") Long tenantId, @RequestBody QmsIqcAuditDTO iqcAuditDTO) {
        log.info("<====QmsIqcAuditController-audit:{},{}",tenantId, iqcAuditDTO);
        qmsIqcAuditRepository.audit(tenantId, iqcAuditDTO);
        return Results.success();
    }

    @ApiOperation(value = "IQC检验审核导出")
    @GetMapping(value = "/export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(QmsIqcAuditQueryVO2.class)
    public ResponseEntity<List<QmsIqcAuditQueryVO2>> exportIqcHeader(@PathVariable("organizationId") Long tenantId,
                                                                   ExportParam exportParam,
                                                                   HttpServletResponse response,
                                                                     QmsIqcAuditQueryDTO dto) {
        return Results.success(qmsIqcAuditRepository.exportIqcHeader(tenantId, dto));
    }
}