package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeTagDaqAttrDTO;
import com.ruike.hme.app.service.HmeTagDaqAttrService;
import com.ruike.hme.domain.vo.HmeTagDaqAttrVO2;
import io.swagger.annotations.Api;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeTagDaqAttr;
import com.ruike.hme.domain.repository.HmeTagDaqAttrRepository;
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
 * 数据项数据采集扩展属性表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2020-07-21 09:52:44
 */
@RestController("hmeTagDaqAttrController.v1")
@RequestMapping("/v1/{organizationId}/hme-tag-daq-attrs")
@Api(tags = SwaggerApiConfig.HME_TAG_DAQ_ATTR)
public class HmeTagDaqAttrController extends BaseController {

    @Autowired
    private HmeTagDaqAttrService hmeTagDaqAttrService;

    @ApiOperation(value = "查询数据项数据采集扩展属性表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<HmeTagDaqAttrVO2> list(@PathVariable("organizationId") Long tenantId, String tagId) {
        return Results.success(hmeTagDaqAttrService.query(tenantId, tagId));
    }

    @ApiOperation(value = "创建或者更新数据项数据采集扩展属性表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/submit"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeTagDaqAttr> createOrUpdate(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody HmeTagDaqAttr hmeTagDaqAttr) {
        return Results.success(hmeTagDaqAttrService.createOrUpdate(tenantId, hmeTagDaqAttr));
    }

    @ApiOperation(value = "数据采集LOV")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/data/collection")
    public ResponseEntity<Page<LovValueDTO>> dataCollectionLovQuery(@PathVariable("organizationId") Long tenantId,
                                                                    HmeTagDaqAttrDTO dto, PageRequest pageRequest){
        return Results.success(hmeTagDaqAttrService.dataCollectionLovQuery(tenantId, dto, pageRequest));
    }
}
