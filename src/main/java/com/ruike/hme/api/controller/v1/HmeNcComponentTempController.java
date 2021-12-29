package com.ruike.hme.api.controller.v1;

import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeNcComponentTemp;
import com.ruike.hme.domain.repository.HmeNcComponentTempRepository;
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

/**
 * 不良材料清单临时表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2020-07-01 20:13:11
 */
@RestController("hmeNcComponentTempController.v1")
@RequestMapping("/v1/{organizationId}/hme-nc-component-temps")
@Api(tags = SwaggerApiConfig.HME_NC_COMPONENT_TEMP)
public class HmeNcComponentTempController extends BaseController {

    @Autowired
    private HmeNcComponentTempRepository hmeNcComponentTempRepository;

    @ApiOperation(value = "不良材料清单临时表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeNcComponentTemp>> list(HmeNcComponentTemp hmeNcComponentTemp, @ApiIgnore @SortDefault(value = HmeNcComponentTemp.FIELD_NC_COMPONENT_TEMP_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeNcComponentTemp> list = hmeNcComponentTempRepository.pageAndSort(pageRequest, hmeNcComponentTemp);
        return Results.success(list);
    }

    @ApiOperation(value = "不良材料清单临时表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{ncComponentTempId}")
    public ResponseEntity<HmeNcComponentTemp> detail(@PathVariable Long ncComponentTempId) {
        HmeNcComponentTemp hmeNcComponentTemp = hmeNcComponentTempRepository.selectByPrimaryKey(ncComponentTempId);
        return Results.success(hmeNcComponentTemp);
    }

    @ApiOperation(value = "创建不良材料清单临时表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeNcComponentTemp> create(@RequestBody HmeNcComponentTemp hmeNcComponentTemp) {
        validObject(hmeNcComponentTemp);
        hmeNcComponentTempRepository.insertSelective(hmeNcComponentTemp);
        return Results.success(hmeNcComponentTemp);
    }

    @ApiOperation(value = "修改不良材料清单临时表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeNcComponentTemp> update(@RequestBody HmeNcComponentTemp hmeNcComponentTemp) {
        SecurityTokenHelper.validToken(hmeNcComponentTemp);
        hmeNcComponentTempRepository.updateByPrimaryKeySelective(hmeNcComponentTemp);
        return Results.success(hmeNcComponentTemp);
    }

    @ApiOperation(value = "删除不良材料清单临时表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeNcComponentTemp hmeNcComponentTemp) {
        SecurityTokenHelper.validToken(hmeNcComponentTemp);
        hmeNcComponentTempRepository.deleteByPrimaryKey(hmeNcComponentTemp);
        return Results.success();
    }

}
