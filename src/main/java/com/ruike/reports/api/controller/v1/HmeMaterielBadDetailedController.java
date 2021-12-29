package com.ruike.reports.api.controller.v1;

import com.ruike.reports.api.dto.HmeMaterielBadDetailedDTO;
import com.ruike.reports.domain.repository.HmeMaterielBadDetailedRepository;
import com.ruike.reports.domain.vo.HmeMaterielBadDetailedVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 材料不良明细报表 API 管理
 *
 * @author 35113 2021/02/02 12:43
 */
@RestController("hmeMaterielBadDetailedController.v1")
@RequestMapping("/v1/{organizationId}/hme-material-bad-detailed")
@Api(tags = "材料不良明细报表")
public class HmeMaterielBadDetailedController {

    @Autowired
    private HmeMaterielBadDetailedRepository hmeMaterielBadDetailedRepository;

    @ApiOperation(value = "材料不良明细报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    @ProcessLovValue
    public ResponseEntity<Page<HmeMaterielBadDetailedVO>> list(@PathVariable("organizationId") Long tenantId,
                                                               HmeMaterielBadDetailedDTO dto,
                                                               @ApiIgnore PageRequest pageRequest) {
        Page<HmeMaterielBadDetailedVO> list = hmeMaterielBadDetailedRepository.pageList(tenantId,dto,pageRequest);
        for(HmeMaterielBadDetailedVO vo : list){
            if(StringUtils.isBlank(vo.getFreezeFlag())){
                vo.setFreezeFlag("N");
            }
        }
        return Results.success(list);
    }

}
