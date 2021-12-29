package tarzan.method.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.method.api.dto.MtOperationDTO3;
import tarzan.method.api.dto.MtOperationDTO4;
import tarzan.method.api.dto.MtOperationDTO5;

/**
 * 工序应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:19:27
 */
public interface MtOperationService {

    /**
     * UI查询工艺列表
     *
     * @author benjamin
     * @date 2019/9/9 10:52 AM
     * @param tenantId 租户Id
     * @param dto MtOperationDTO3
     * @param pageRequest PageRequest
     * @return list
     */
    Page<MtOperationDTO4> queryOperationListForUi(Long tenantId, MtOperationDTO3 dto, PageRequest pageRequest);

    /**
     * UI查询工艺详细信息
     *
     * @author benjamin
     * @date 2019/9/9 11:00 AM
     * @param tenantId 租户Id
     * @param operationId 工艺Id
     * @return MtOperationDTO4
     */
    MtOperationDTO4 queryOperationDetailForUi(Long tenantId, String operationId);

    /**
     * UI保存工艺信息
     *
     * @author benjamin
     * @date 2019/9/9 4:21 PM
     * @param tenantId 租户Id
     * @param dto MtOperationDTO5
     * @return String
     */
    String saveOperationForUi(Long tenantId, MtOperationDTO5 dto);
}
