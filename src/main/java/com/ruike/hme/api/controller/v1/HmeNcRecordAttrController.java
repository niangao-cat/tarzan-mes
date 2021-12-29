package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeNcRecordAttr;
import com.ruike.hme.domain.repository.HmeNcRecordAttrRepository;
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
 * 不良记录扩展属性表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2020-07-23 10:53:45
 */
@RestController("hmeNcRecordAttrController.v1")
@RequestMapping("/v1/{organizationId}/hme-nc-record-attrs")
public class HmeNcRecordAttrController extends BaseController {

}
