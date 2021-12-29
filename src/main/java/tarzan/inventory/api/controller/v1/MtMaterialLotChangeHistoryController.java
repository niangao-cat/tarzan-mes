package tarzan.inventory.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
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
import tarzan.inventory.api.dto.MtMaterialLotChangeHistoryDTO;
import tarzan.inventory.domain.entity.MtMaterialLotChangeHistory;
import tarzan.inventory.domain.repository.MtMaterialLotChangeHistoryRepository;
import tarzan.inventory.domain.vo.MtMaterialCategoryHisVO;
import tarzan.inventory.domain.vo.MtMaterialCategoryHisVO1;

/**
 * 物料批变更历史，记录物料批拆分合并的变更情况 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:05:08
 */
@RestController("mtMaterialLotChangeHistoryController.v1")
@RequestMapping("/v1/{organizationId}/mt-material-lot-change-history")
@Api(tags = "MtMaterialLotChangeHistory")
public class MtMaterialLotChangeHistoryController extends BaseController {
    @Autowired
    private MtMaterialLotChangeHistoryRepository repository;

    @ApiOperation(value = "sourceMaterialLotQuery")
    @PostMapping(value = {"/source"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialCategoryHisVO>> sourceMaterialLotQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String materialLotId) {
        ResponseData<List<MtMaterialCategoryHisVO>> responseData = new ResponseData<List<MtMaterialCategoryHisVO>>();
        try {
            responseData.setRows(repository.sourceMaterialLotQuery(tenantId, materialLotId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotLimitChangeHistoryQuery")
    @PostMapping(value = {"/limit-material-lot"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialCategoryHisVO1>> materialLotLimitChangeHistoryQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody String materialLotId) {
        ResponseData<List<MtMaterialCategoryHisVO1>> responseData = new ResponseData<List<MtMaterialCategoryHisVO1>>();
        try {
            responseData.setRows(repository.materialLotLimitChangeHistoryQuery(tenantId, materialLotId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotChangeHistoryCreate")
    @PostMapping(value = {"/add"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialLotChangeHistoryCreate(@PathVariable("organizationId") Long tenantId,
                                                               @RequestBody MtMaterialLotChangeHistoryDTO dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            MtMaterialLotChangeHistory mtMaterialLotChangeHistory = new MtMaterialLotChangeHistory();
            BeanUtils.copyProperties(dto, mtMaterialLotChangeHistory);
            responseData.setRows(repository.materialLotChangeHistoryCreate(tenantId, mtMaterialLotChangeHistory));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
