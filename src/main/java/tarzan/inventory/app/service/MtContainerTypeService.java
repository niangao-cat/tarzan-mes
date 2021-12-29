package tarzan.inventory.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.inventory.api.dto.MtContainerTypeDTO;
import tarzan.inventory.api.dto.MtContainerTypeDTO1;
import tarzan.inventory.api.dto.MtContainerTypeDTO2;

/**
 * 容器类型，定义一类容器和一类容器的控制属性，包括容器可装载的对象类型、最大容量、混装类型等应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:32
 */
public interface MtContainerTypeService {

    /**
     * UI查询容器类型列表
     *
     * @Author Xie.yiyang
     * @Date 2019/12/4 9:52
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<tarzan.inventory.api.dto.MtContainerTypeDTO1>
     */
    Page<MtContainerTypeDTO1> queryContainerTypeListForUi(Long tenantId, MtContainerTypeDTO dto,
                                                          PageRequest pageRequest);

    /**
     * UI查询容器类型详细信息
     *
     * @Author Xie.yiyang
     * @Date 2019/12/4 10:36
     * @param tenantId
     * @param containerTypeId
     * @return tarzan.inventory.api.dto.MtContainerTypeDTO1
     */
    MtContainerTypeDTO1 queryContainerTypeDetailForUi(Long tenantId, String containerTypeId);

    /**
     * UI保存容器类型
     *
     * @Author Xie.yiyang
     * @Date 2019/12/4 10:54
     * @param tenantId
     * @param dto
     * @return java.lang.String
     */
    String saveContainerTypeForUi(Long tenantId, MtContainerTypeDTO2 dto);
}
