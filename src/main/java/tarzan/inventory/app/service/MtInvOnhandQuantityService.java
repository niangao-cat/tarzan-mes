package tarzan.inventory.app.service;

import java.util.List;

import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;
import tarzan.inventory.api.dto.MtInvOnhandQuantityDTO;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO4;

/**
 * 库存量应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:53
 */
public interface MtInvOnhandQuantityService {

    /**
     * 查询库存信息
     * 
     * @author benjamin
     * @date 2019-08-15 15:54
     * @param tenantId 租户Id
     * @param dto MtInvOnhandQuantityDTO
     * @param pageRequest PageRequest
     * @return page
     */
    List<MtInvOnhandQuantityVO4> queryInventoryOnhandQuantityForUi(Long tenantId, MtInvOnhandQuantityDTO dto,
                                                                   PageRequest pageRequest);

}
