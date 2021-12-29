package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeCosSelectionCurrentHis;
import com.ruike.hme.domain.repository.HmeCosSelectionCurrentHisRepository;
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
 * COS筛选电流点维护历史表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-08-18 11:07:41
 */
@RestController("hmeCosSelectionCurrentHisController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-selection-current-hiss")
public class HmeCosSelectionCurrentHisController extends BaseController {

}
