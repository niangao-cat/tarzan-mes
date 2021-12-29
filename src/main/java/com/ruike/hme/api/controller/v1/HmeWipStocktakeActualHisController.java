package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeWipStocktakeActualHis;
import com.ruike.hme.domain.repository.HmeWipStocktakeActualHisRepository;
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
 * 在制盘点实际历史 管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-03-03 13:48:57
 */
@RestController("hmeWipStocktakeActualHisController.v1")
@RequestMapping("/v1/{organizationId}/hme-wip-stocktake-actual-hiss")
public class HmeWipStocktakeActualHisController extends BaseController {


}
