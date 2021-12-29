package com.ruike.hme.api.controller.v1;

import io.swagger.annotations.Api;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeEquipmentWkcRelHis;
import com.ruike.hme.domain.repository.HmeEquipmentWkcRelHisRepository;
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
 * 设备工位关系历史表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2020-06-23 15:53:04
 */
@RestController("hmeEquipmentWkcRelHisController.v1")
@RequestMapping("/v1/{organizationId}/hme-equipment-wkc-rel-his")
public class HmeEquipmentWkcRelHisController extends BaseController {

}
