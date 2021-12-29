package com.ruike.itf.api.controller.v1;

import com.alibaba.fastjson.JSONArray;
import com.ruike.itf.api.dto.ItfCommonReturnDTO;
import com.ruike.itf.api.dto.ItfMaterialSubstituteRelDTO;
import com.ruike.itf.app.service.ItfMaterialSubstituteRelIfaceService;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 物料全局替代关系表 管理 API
 *
 * @author yapeng.yao@hand-china.com 2020-08-18 14:40:53
 */
@RestController("itfMaterialSubstituteRelIfaceSiteController.v1")
@RequestMapping("/v1/itf_material_substitute_rel_ifaces")
@Slf4j
public class ItfMaterialSubstituteRelIfaceController extends BaseController {

    @Autowired
    private ItfMaterialSubstituteRelIfaceService itfMaterialSubstituteRelIfaceService;

    @ApiOperation(value = "物料全局替代关系同步接口")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping("/material-substitute-rel")
    public ResponseEntity<?> materialSubstituteRelSync(@RequestBody String dataStr) {
        log.info("<==== material-substitute-rel-sync Success requestPayload: {}", dataStr);
        List<ItfMaterialSubstituteRelDTO> itfMaterialSubstituteRelDTOList = JSONArray.parseArray(dataStr, ItfMaterialSubstituteRelDTO.class);
        List<ItfCommonReturnDTO> itfCommonReturnDTOList = itfMaterialSubstituteRelIfaceService.invoke(itfMaterialSubstituteRelDTOList);
        return Results.success(itfCommonReturnDTOList);
    }

}
