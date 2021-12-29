package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeEoJobEquipment;
import com.ruike.hme.domain.repository.HmeEoJobEquipmentRepository;
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
 * SN进出站设备状态记录表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2020-06-28 16:52:11
 */
@RestController("hmeEoJobEquipmentController.v1")
@RequestMapping("/v1/{organizationId}/hme-eo-job-equipments")
public class HmeEoJobEquipmentController extends BaseController {
}
