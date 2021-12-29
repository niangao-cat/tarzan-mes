package com.ruike.hme.api.controller.v1;

import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeAfterSaleQuotationLine;
import com.ruike.hme.domain.repository.HmeAfterSaleQuotationLineRepository;
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
 * 售后报价单行表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-09-26 11:07:30
 */
@RestController("hmeAfterSaleQuotationLineController.v1")
@RequestMapping("/v1/{organizationId}/hme-after-sale-quotation-lines")
public class HmeAfterSaleQuotationLineController extends BaseController {


}
