package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeToolHisDTO;
import com.ruike.hme.domain.vo.HmeToolHisVO;
import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeToolHis;
import com.ruike.hme.domain.repository.HmeToolHisRepository;
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

/**
 * HmeToolHisController
 * 工装基础数据历史表 管理 API
 *
 * @author li.zhang13@hand-china.com 2021-01-07 10:06:46
 */
@RestController("hmeToolHisController.v1")
@RequestMapping("/v1/{organizationId}/hme-tool-his")
@Api(tags = SwaggerApiConfig.HME_TOOL_HIS)
public class HmeToolHisController extends BaseController {

    @Autowired
    private HmeToolHisRepository hmeToolHisRepository;

    /**
     * @Description: 根据工装id获取修改记录明细
     * @author: li.zhang13@hand-china.com
     * @param tenantId 租户ID
     * @param hmeToolHisDTO 查询条件（工装ID）
     * @return : HmeToolHisVO
     */
    @ApiOperation(value = "根据工装id获取修改记录明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeToolHisVO>> list(HmeToolHisDTO hmeToolHisDTO, @ApiIgnore @SortDefault(value = HmeToolHis.FIELD_TOOL_HIS_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest, @PathVariable("organizationId") String tenantId) {
        Page<HmeToolHisVO> list = hmeToolHisRepository.selectHmeToolHis(pageRequest, tenantId, hmeToolHisDTO);
        return Results.success(list);
    }
}
