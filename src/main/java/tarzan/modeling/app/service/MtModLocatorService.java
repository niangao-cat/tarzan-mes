package tarzan.modeling.app.service;


import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.modeling.api.dto.MtModLocatorDTO5;
import tarzan.modeling.domain.vo.MtModLocatorVO3;
import tarzan.modeling.domain.vo.MtModLocatorVO5;
import tarzan.modeling.domain.vo.MtModLocatorVO6;

/**
 * 库位应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
public interface MtModLocatorService {

    /**
     * 获取库位列表
     *
     * @author xiao.tang02@hand-china.com 2019年8月15日上午9:51:39
     * @param tenantId
     * @param condition
     * @param pageRequest
     * @return
     * @return Page<MtModLocatorVO2>
     */
    Page<MtModLocatorVO6> listModLocatorForUi(Long tenantId, MtModLocatorVO5 condition, PageRequest pageRequest);

    /**
     * 新增&更新库位
     *
     * @author xiao.tang02@hand-china.com 2019年8月15日上午9:51:39
     * @param tenantId
     * @param dto
     * @return
     * @return Page<MtModLocatorVO2>
     */
    String saveModLocatorForUi(Long tenantId, MtModLocatorDTO5 dto);

    MtModLocatorVO3 detailModLocatorForUi(Long tenantId, String locatorId);

}
