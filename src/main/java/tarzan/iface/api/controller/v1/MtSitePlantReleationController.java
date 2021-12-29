package tarzan.iface.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.iface.domain.repository.MtSitePlantReleationRepository;
import tarzan.iface.domain.vo.MtSitePlantReleationVO;
import tarzan.iface.domain.vo.MtSitePlantReleationVO1;

/**
 * ERP工厂与站点映射关系 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:40:02
 */
@RestController("mtSitePlantReleationController.v1")
@RequestMapping("/v1/{organizationId}/mt-site-plant-releation")
@Api(tags = "MtSitePlantReleation")
public class MtSitePlantReleationController extends BaseController {

    @Autowired
    private MtSitePlantReleationRepository repository;

    @ApiOperation("itemMaterialSiteIdBatchQuery")
    @PostMapping(value = {"/item/material-siteid/batch-query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtSitePlantReleationVO1>> itemMaterialSiteIdBatchQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtSitePlantReleationVO dto) {
        ResponseData<List<MtSitePlantReleationVO1>> responseData = new ResponseData<List<MtSitePlantReleationVO1>>();
        try {
            responseData.setRows(this.repository.itemMaterialSiteIdBatchQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
    
}
