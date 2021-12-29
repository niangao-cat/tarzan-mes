package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeCosFunctionMaterialTime;
import com.ruike.hme.domain.repository.HmeCosFunctionMaterialTimeRepository;
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
 * COS投料性能时间记录表 管理 API
 *
 * @author penglin.sui@hand-china.com 2021-06-23 18:09:37
 */
@RestController("hmeCosFunctionMaterialTimeController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-function-material-times")
public class HmeCosFunctionMaterialTimeController extends BaseController {

}
