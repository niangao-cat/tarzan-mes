package tarzan.pull.api.controller.v1;

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
import tarzan.pull.domain.repository.MtPullOnhandSnapshotRepository;
import tarzan.pull.domain.vo.MtPullOnhandSnapshotVO;
import tarzan.pull.domain.vo.MtPullOnhandSnapshotVO2;
import tarzan.pull.domain.vo.MtPullOnhandSnapshotVO3;
import tarzan.pull.domain.vo.MtPullOnhandSnapshotVO4;
import tarzan.pull.domain.vo.MtPullOnhandSnapshotVO5;
import tarzan.pull.domain.vo.MtPullOnhandSnapshotVO6;
import tarzan.pull.domain.vo.MtPullOnhandSnapshotVO7;
import tarzan.pull.domain.vo.MtPullOnhandSnapshotVO8;

/**
 * 拉动线边库存快照 管理 API
 *
 * @author yiyang.xie 2020-02-04 15:53:01
 */
@RestController("mtPullOnhandSnapshotController.v1")
@RequestMapping("/v1/{organizationId}/mt-pull-onhand-snapshot")
@Api(tags = "MtPullOnhandSnapshot")
public class MtPullOnhandSnapshotController extends BaseController {

    @Autowired
    private MtPullOnhandSnapshotRepository repository;

    @ApiOperation(value = "onhandSnapShotCreate")
    @PostMapping(value = {"/SnapShot/create"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtPullOnhandSnapshotVO2>> onhandSnapShotCreate(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<MtPullOnhandSnapshotVO> dto) {
        ResponseData<List<MtPullOnhandSnapshotVO2>> result = new ResponseData<List<MtPullOnhandSnapshotVO2>>();
        try {
            result.setRows(repository.onhandSnapShotCreate(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "pullOnhandSnapshotQuery")
    @PostMapping(value = {"/snapshot/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtPullOnhandSnapshotVO6>> pullOnhandSnapshotQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtPullOnhandSnapshotVO5 dto) {
        ResponseData<List<MtPullOnhandSnapshotVO6>> result = new ResponseData<List<MtPullOnhandSnapshotVO6>>();
        try {
            result.setRows(repository.pullOnhandSnapshotQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "inventoryWaveReplenishmentQtyCalculate")
    @PostMapping(value = {"/replenishment-qty/calculate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtPullOnhandSnapshotVO4> inventoryWaveReplenishmentQtyCalculate(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtPullOnhandSnapshotVO3 dto) {
        ResponseData<MtPullOnhandSnapshotVO4> result = new ResponseData<MtPullOnhandSnapshotVO4>();
        try {
            result.setRows(repository.inventoryWaveReplenishmentQtyCalculate(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }


    @ApiOperation(value = "inventoryWaveReplenishmentPullProcess")
    @PostMapping(value = {"/replenishment/pull/process"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> inventoryWaveReplenishmentPullProcess(@PathVariable("organizationId") Long tenantId,
                                                                    @RequestBody MtPullOnhandSnapshotVO7 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            repository.inventoryWaveReplenishmentPullProcess(tenantId, dto);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "distributionActualQtyCalculate")
    @PostMapping(value = {"/distribution/actual-qty/calculate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Double> distributionActualQtyCalculate(@PathVariable("organizationId") Long tenantId,
                                                               @RequestBody MtPullOnhandSnapshotVO8 dto) {
        ResponseData<Double> result = new ResponseData<Double>();
        try {
            result.setRows(repository.distributionActualQtyCalculate(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

}
