package com.ruike.hme.api.controller.v1;

import com.ruike.hme.domain.repository.HmeTagCheckRepository;
import com.ruike.hme.domain.vo.HmeTagCheckVO;
import com.ruike.hme.domain.vo.HmeTagCheckVO2;
import com.ruike.hme.domain.vo.HmeTagCheckVO5;
import com.ruike.hme.domain.vo.HmeTagDaqAttrVO2;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据项展示报表
 *
 * @author sanfeng.zhang@hand-china.com 2021/9/1 11:36
 */
@RestController("hmeTagCheckController.v1")
@RequestMapping("/v1/{organizationId}/hme-tag-checks")
public class HmeTagCheckController {

    @Autowired
    private HmeTagCheckRepository hmeTagCheckRepository;

    @ApiOperation(value = "当前SN-弹框")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-sn-list")
    public ResponseEntity<List<HmeTagCheckVO2>> snListQuery(@PathVariable("organizationId") Long tenantId,
                                                            HmeTagCheckVO vo) {
        return Results.success(hmeTagCheckRepository.snListQuery(tenantId, vo));
    }

    @ApiOperation(value = "组件数据-弹框")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-component-list")
    public ResponseEntity<List<HmeTagCheckVO5>> componentListQuery(@PathVariable("organizationId") Long tenantId,
                                                                   HmeTagCheckVO vo) {
        vo.initParam();
        return Results.success(hmeTagCheckRepository.componentListQuery(tenantId, vo));
    }
}
