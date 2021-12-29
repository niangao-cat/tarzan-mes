package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeNcDowngradeDTO;
import com.ruike.hme.api.dto.HmeNcDowngradeDTO2;
import com.ruike.hme.api.dto.HmeNcDowngradeDTO3;
import com.ruike.hme.app.service.HmeNcDowngradeService;
import com.ruike.hme.domain.vo.HmeNcDowngradeVO;
import com.ruike.hme.domain.vo.HmeNcDowngradeVO2;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeNcDowngrade;
import com.ruike.hme.domain.repository.HmeNcDowngradeRepository;
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
 * 产品降级关系维护 管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-05-18 11:35:47
 */
@RestController("hmeNcDowngradeController.v1")
@RequestMapping("/v1/{organizationId}/hme-nc-downgrades")
public class HmeNcDowngradeController extends BaseController {

    @Autowired
    private HmeNcDowngradeService hmeNcDowngradeService;

    @ApiOperation(value = "新建或者更新产品降级关系维护")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeNcDowngrade> createOrUpdate(@PathVariable("organizationId") Long tenantId,
                                                         @RequestBody HmeNcDowngradeDTO dto) {
        return Results.success(hmeNcDowngradeService.createOrUpdate(tenantId, dto));
    }

    @ApiOperation(value = "分页查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeNcDowngradeVO>> pageQuery(@PathVariable("organizationId") Long tenantId,
                                                               HmeNcDowngradeDTO2 dto, PageRequest pageRequest) {
        Page<HmeNcDowngradeVO> resultPage = hmeNcDowngradeService.pageQuery(tenantId, dto, pageRequest);
        return Results.success(resultPage);
    }

    @ApiOperation(value = "历史数据分页查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/his")
    public ResponseEntity<Page<HmeNcDowngradeVO2>> hisPageQuery(@PathVariable("organizationId") Long tenantId,
                                                                HmeNcDowngradeDTO3 dto, PageRequest pageRequest) {
        Page<HmeNcDowngradeVO2> resultPage = hmeNcDowngradeService.hisPageQuery(tenantId, dto, pageRequest);
        return Results.success(resultPage);
    }

}
