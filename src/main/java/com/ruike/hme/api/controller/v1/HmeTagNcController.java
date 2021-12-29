package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeTagNcDTO;
import com.ruike.hme.app.service.HmeTagNcService;
import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeTagNc;
import com.ruike.hme.domain.repository.HmeTagNcRepository;
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
import tarzan.config.SwaggerApiConfig;

import java.util.List;

/**
 * 数据项不良判定基础表 管理 API
 *
 * @author guiming.zhou@hand-china.com 2020-09-24 16:00:30
 */
@RestController("hmeTagNcController.v1")
@RequestMapping("/v1/{organizationId}/hme-tag-ncs")
@Api(tags = SwaggerApiConfig.HME_TAG_NC)
public class HmeTagNcController extends BaseController {

    @Autowired
    private HmeTagNcRepository hmeTagNcRepository;

    @Autowired
    private HmeTagNcService hmeTagNcService;

    @ApiOperation(value = "创建数据项不良判定基础表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/hme-tag-nc/create")
    public ResponseEntity<List<HmeTagNc>> create(@PathVariable("organizationId") Long tenantId,
                                           @RequestBody List<HmeTagNc> hmeTagNcs) {
        validList(hmeTagNcs);
        hmeTagNcRepository.batchInsertSelective(hmeTagNcs);
        return Results.success(hmeTagNcs);
    }

    @ApiOperation(value = "修改数据项不良判定基础表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/hme-tag-nc/update")
    public ResponseEntity<List<HmeTagNc>> update(@PathVariable("organizationId") Long tenantId,
                                           @RequestBody List<HmeTagNc> hmeTagNcs) {
        //SecurityTokenHelper.validToken(hmeTagNc);
        hmeTagNcRepository.batchUpdateByPrimaryKeySelective(hmeTagNcs);
        return Results.success(hmeTagNcs);
    }

    @ApiOperation(value = "删除数据项不良判定基础表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@PathVariable("organizationId") Long tenantId,
                                    @RequestBody HmeTagNc hmeTagNc) {
        SecurityTokenHelper.validToken(hmeTagNc);
        hmeTagNcRepository.deleteByPrimaryKey(hmeTagNc);
        return Results.success();
    }

    @ApiOperation(value = "查询公式头")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/hme-tag-nc/get-tag-nc-list")
    public ResponseEntity<Page<HmeTagNcDTO>> getTagNcList(@PathVariable("organizationId") Long tenantId,
                                                          HmeTagNc hmeTagNc,
                                                          @ApiIgnore PageRequest pageRequest) {
        return Results.success(hmeTagNcService.getTagNcList(tenantId, hmeTagNc, pageRequest));
    }

    @ApiOperation(value = "删除数据--自定义")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping("/hme-tag-nc/delete/{tagNcId}")
    public ResponseEntity<?> deleteHmeTagNc(@PathVariable(value = "organizationId") Long tenantId,
                                            @PathVariable String tagNcId) {
        hmeTagNcRepository.deleteByPrimaryKey(tagNcId);
        return Results.success();
    }

}
