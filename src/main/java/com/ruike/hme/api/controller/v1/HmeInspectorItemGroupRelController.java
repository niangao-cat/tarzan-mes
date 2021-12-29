package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeInspectorItemGroupRelDTO;
import com.ruike.hme.app.service.HmeInspectorItemGroupRelService;
import com.ruike.hme.domain.vo.HmeInspectorItemGroupRelVO;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeInspectorItemGroupRel;
import com.ruike.hme.domain.repository.HmeInspectorItemGroupRelRepository;
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

import java.util.List;

/**
 * 检验员与物料组关系表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-03-29 13:44:29
 */
@RestController("hmeInspectorItemGroupRelController.v1")
@RequestMapping("/v1/{organizationId}/hme-inspector-item-group-rels")
public class HmeInspectorItemGroupRelController extends BaseController {

    @Autowired
    private HmeInspectorItemGroupRelService hmeInspectorItemGroupRelService;

    @ApiOperation(value = "分页查询检验员与物料组关系")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeInspectorItemGroupRelVO>> relPageQuery(@PathVariable("organizationId") Long tenantId,
                                                                         HmeInspectorItemGroupRelDTO dto, PageRequest pageRequest) {
        Page<HmeInspectorItemGroupRelVO> resultPage = hmeInspectorItemGroupRelService.relPageQuery(tenantId, dto, pageRequest);
        return Results.success(resultPage);
    }

    @ApiOperation(value = "新建或者更新检验员与物料组关系")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<List<HmeInspectorItemGroupRelVO>> relCreateOrUpdate(@PathVariable("organizationId") Long tenantId,
                                                                              @RequestBody List<HmeInspectorItemGroupRelVO> dtoList) {
        List<HmeInspectorItemGroupRelVO> resultList = hmeInspectorItemGroupRelService.relCreateOrUpdate(tenantId, dtoList);
        return Results.success(resultList);
    }

}
