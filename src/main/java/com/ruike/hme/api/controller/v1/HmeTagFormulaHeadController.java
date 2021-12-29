package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeWkcEquSwitchDTO3;
import com.ruike.hme.app.service.HmeTagFormulaHeadService;
import com.ruike.hme.app.service.HmeTagFormulaLineService;
import com.ruike.hme.domain.vo.HmeTagFormulaVO;
import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeTagFormulaHead;
import com.ruike.hme.domain.repository.HmeTagFormulaHeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import com.ruike.hme.domain.vo.HmeTagFormulaHeadVO;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.config.SwaggerApiConfig;

/**
 * 数据采集项公式头表 管理 API
 *
 * @author guiming.zhou@hand-china.com 2020-09-21 19:50:40
 */
@RestController("hmeTagFormulaHeadController.v1")
@RequestMapping("/v1/{organizationId}/hme-tag-formula-heads")
@Api(tags = SwaggerApiConfig.HME_TAG_FORMULA_HEAD)
public class HmeTagFormulaHeadController extends BaseController {

    @Autowired
    private HmeTagFormulaHeadRepository hmeTagFormulaHeadRepository;

    @Autowired
    private HmeTagFormulaHeadService hmeTagFormulaHeadService;

    @Autowired
    private HmeTagFormulaLineService hmeTagFormulaLineService;

    /**
     * 创建数据采集项公式头表
     *
     * @param tenantId
     * @param hmeTagFormulaHead
     * @author guiming.zhou@hand-china.com 2020/9/25 13:52
     * @return org.springframework.http.ResponseEntity<com.ruike.hme.domain.entity.HmeTagFormulaHead>
     */
    @ApiOperation(value = "创建数据采集项公式头表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/tag-formula/insert-head-list")
    public ResponseEntity<HmeTagFormulaHead> create(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody HmeTagFormulaHead hmeTagFormulaHead) {
        validObject(hmeTagFormulaHead);
        hmeTagFormulaHeadRepository.createOrUpdateHead(tenantId, hmeTagFormulaHead);
        return Results.success(hmeTagFormulaHead);
    }

    /**
     * 修改数据采集项公式头表
     *
     * @param tenantId
     * @param hmeTagFormulaHead
     * @author guiming.zhou@hand-china.com 2020/9/25 13:52
     * @return org.springframework.http.ResponseEntity<com.ruike.hme.domain.entity.HmeTagFormulaHead>
     */
    @ApiOperation(value = "修改数据采集项公式头表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/tag-formula/update-head-list")
    public ResponseEntity<HmeTagFormulaHead> update(@PathVariable("organizationId") Long tenantId,
                                                    @RequestBody HmeTagFormulaHead hmeTagFormulaHead) {
        hmeTagFormulaHeadRepository.createOrUpdateHead(tenantId, hmeTagFormulaHead);
        return Results.success(hmeTagFormulaHead);
    }

    /**
     * 查询公式头
     *
     * @param tenantId
     * @param vo
     * @param pageRequest
     * @author guiming.zhou@hand-china.com 2020/9/25 13:53
     * @return org.springframework.http.ResponseEntity<io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeTagFormulaHeadVO>>
     */
    @ApiOperation(value = "查询公式头")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/tag-formula/get-head-list")
    public ResponseEntity<Page<HmeTagFormulaHeadVO>> getTagHeadList(@PathVariable("organizationId") Long tenantId,
                                                                    HmeTagFormulaVO vo,
                                                                    @ApiIgnore PageRequest pageRequest) {
        return Results.success(hmeTagFormulaHeadService.getTagHeadList(tenantId, vo, pageRequest));
    }

    /**
     * 删除数据采集项公式头表
     *
     * @param tenantId
     * @param tagFormulaHeadId
     * @author guiming.zhou@hand-china.com 2020/9/25 13:53
     * @return org.springframework.http.ResponseEntity<?>
     */
    @ApiOperation(value = "删除数据采集项公式头表-自定义")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/tag-formula/delete-head-list/{tagFormulaHeadId}")
    public ResponseEntity<?> deleteHmeTagFormulaHead(@PathVariable(value = "organizationId") Long tenantId,
                                                     @PathVariable String tagFormulaHeadId) {
        hmeTagFormulaHeadService.deleteHmeTagFormulaHead(tenantId, tagFormulaHeadId);
        hmeTagFormulaLineService.deleteHmeTagFormulaLine(tenantId, tagFormulaHeadId);
        return Results.success();
    }

}
