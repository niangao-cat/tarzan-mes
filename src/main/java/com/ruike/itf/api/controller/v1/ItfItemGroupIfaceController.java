package com.ruike.itf.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.domain.entity.ItfItemGroupIface;
import com.ruike.itf.domain.repository.ItfItemGroupIfaceRepository;
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
 * 物料组接口表 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-07-17 10:12:42
 */
@RestController("itfItemGroupIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-item-group-iface")
public class ItfItemGroupIfaceController extends BaseController {

    @Autowired
    private ItfItemGroupIfaceRepository repository;

    @ApiOperation(value = "物料组导入")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/import"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> remove(@PathVariable("organizationId") Long tenantId) {
        this.repository.itemGroupIfaceImport(tenantId);
        return Results.success();
    }

}
