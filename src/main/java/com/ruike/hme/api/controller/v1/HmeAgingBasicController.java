package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeAgingBasicDTO;
import com.ruike.hme.domain.vo.HmeAgingBasicVO;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.repository.AgingBasicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;

/**
 * 老化基础数据 管理 API
 *
 * @author junfeng.chen@hand-china.com 2021-03-02 11:56:36
 */
@RestController("agingBasicController.v1")
@RequestMapping("/v1/{organizationId}/aging-basics")
//@Api(tags = SwaggerApiConfig.HME_COS_INSPECT_PLATFORM)
@Slf4j
public class HmeAgingBasicController extends BaseController {

    @Autowired
    private AgingBasicRepository agingBasicRepository;

    @ApiOperation(value = "老化基础数据列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/list")
    public ResponseEntity<Page<HmeAgingBasicVO>> list(@PathVariable (value = "organizationId") Long tenantId,
                                                      HmeAgingBasicDTO dto, PageRequest pageRequest) {
        Page<HmeAgingBasicVO> list = agingBasicRepository.pageList(pageRequest, dto, tenantId);
        return Results.success(list);
    }


    @ApiOperation(value = "修改老化基础数据")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = {"/save"},produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> save(@PathVariable (value = "organizationId") Long tenantId,
                                  @RequestBody HmeAgingBasicVO vo) {
        agingBasicRepository.save(vo, tenantId);
        return Results.success(vo);
    }


}
