package com.ruike.qms.api.controller.v1;

import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.qms.domain.entity.QmsPqcHeaderHis;
import com.ruike.qms.domain.repository.QmsPqcHeaderHisRepository;
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
import tarzan.config.SwaggerApiConfig;

/**
 * 巡检单头历史表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2020-08-17 11:49:25
 */
@RestController("qmsPqcHeaderHisController.v1")
@RequestMapping("/v1/{organizationId}/qms-pqc-header-hiss")
@Api(tags = SwaggerApiConfig.QMS_PQC_HEADER_HIS)
public class QmsPqcHeaderHisController extends BaseController {

}
