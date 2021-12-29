package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeEqTaskDocQueryDTO;
import com.ruike.hme.api.dto.HmeToolDTO;
import com.ruike.hme.api.dto.HmeToolInsertDto;
import com.ruike.hme.app.service.HmeToolService;
import com.ruike.hme.domain.vo.HmeToolVO;
import com.ruike.hme.domain.vo.HmeToolVO2;
import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeTool;
import com.ruike.hme.domain.repository.HmeToolRepository;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;
import tarzan.modeling.domain.entity.MtModWorkcell;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * HmeToolController
 * 工装基础数据表 管理 API
 *
 * @author li.zhang13@hand-china.com 2021-01-07 10:06:45
 */
@RestController("hmeToolController.v1")
@RequestMapping("/v1/{organizationId}/hme-tools")
@Api(tags = SwaggerApiConfig.HME_TOOL)
public class HmeToolController extends BaseController {

    @Autowired
    private HmeToolRepository hmeToolRepository;
    @Autowired
    private HmeToolService hmeToolService;

    /**
     * @Description: 工装基础数据表列表
     * @author: li.zhang13@hand-china.com
     * @param tenantId 租户ID
     * @param hmeTooldto 查询条件
     * @param pageRequest 分页条件
     * @return : HmeToolVO
     */
    @ApiOperation(value = "工装基础数据表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeToolVO>> list(HmeToolDTO hmeTooldto, @ApiIgnore @SortDefault(value = HmeTool.FIELD_TOOL_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest, @PathVariable("organizationId") Long tenantId) {
        Page<HmeToolVO> list = hmeToolRepository.selectHmeTOOLs(pageRequest, tenantId, hmeTooldto);
        return Results.success(list);
    }

    /**
     * @Description: 更新创建工装基础数据
     * @author: li.zhang13@hand-china.com
     * @param tenantId 租户ID
     * @param hmeToolInsertDtos 工装基本数据
     * @return : hmeToolInsertDtos
     */
    @ApiOperation(value = "更新创建工装基础数据表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<List<HmeToolInsertDto>> create(@PathVariable("organizationId") String tenantId, @RequestBody List<HmeToolInsertDto> hmeToolInsertDtos) {
        validObject(hmeToolInsertDtos);
        hmeToolService.insertorupdateSelective(tenantId,hmeToolInsertDtos);
        return Results.success(hmeToolInsertDtos);
    }

    /**
     * @Description 工位扫描
     * @param tenantId 租户ID
     * @param workcellCode 工位编码
     * @return org.springframework.http.ResponseEntity<tarzan.modeling.domain.entity.MtModWorkcell>
     * @Date 2021-01-12 19:49
     * @Author penglin.sui
     */
    @ApiOperation(value = "工位扫描")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/workcell-scan", produces = "application/json;charset=UTF-8")
    public ResponseEntity<MtModWorkcell> workcellScan(@PathVariable("organizationId") Long tenantId, String workcellCode) {
        return Results.success(hmeToolRepository.workcellScan(tenantId,workcellCode));
    }

    /**
     * @Description 根据工位获取工装信息
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param pageRequest
     * @return org.springframework.http.ResponseEntity<io.choerodon.core.domain.Page<java.util<com.ruike.hme.domain.entity.HmeTool>>>
     * @Date 2021-01-08 16:57
     * @Author penglin.sui
     */
    @ApiOperation(value = "根据工位获取工装信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/query/tool", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<HmeToolVO2>> hmeToolQuery(@PathVariable("organizationId") Long tenantId, String workcellId, @ApiIgnore PageRequest pageRequest) {
        return Results.success(hmeToolRepository.hmeToolQuery(tenantId,workcellId,pageRequest));
    }

    /**
     * @Description 工装管理保存
     * @param tenantId 租户ID
     * @param dtoList 工装管理保存参数
     * @return java.util<com.ruike.hme.domain.entity.HmeTool>
     * @Date 2021-01-08 17:47
     * @Author penglin.sui
     */
    @ApiOperation(value = "工装管理保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/save/tool", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<HmeTool>> hmeToolSave(@PathVariable("organizationId") Long tenantId,  @RequestBody List<HmeTool> dtoList) {
        validObject(dtoList);
        return Results.success(hmeToolRepository.hmeToolSave(tenantId,dtoList));
    }

    @ApiOperation(value = "工装基础数据表列表导出")
    @GetMapping(value = "/list/export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(HmeToolVO.class)
    public ResponseEntity<List<HmeToolVO>> listExport(@PathVariable("organizationId") Long tenantId,
                                                                 HmeToolDTO dto,
                                                                 HttpServletResponse response,
                                                                 ExportParam exportParam) {
        List<HmeToolVO> hmeToolVOList = hmeToolRepository.listExport(tenantId, dto);
        return Results.success(hmeToolVOList);
    }
}
