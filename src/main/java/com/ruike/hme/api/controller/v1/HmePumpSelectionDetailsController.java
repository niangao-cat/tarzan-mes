package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmePumpSelectionDetails;
import com.ruike.hme.domain.repository.HmePumpSelectionDetailsRepository;
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
 * 泵浦源预筛选明细表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-08-30 10:59:48
 */
@RestController("hmePumpSelectionDetailsController.v1")
@RequestMapping("/v1/{organizationId}/hme-pump-selection-detailss")
public class HmePumpSelectionDetailsController extends BaseController {
    

}
