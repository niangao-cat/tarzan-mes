package tarzan.general.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.general.api.dto.MtTagDTO;
import tarzan.general.api.dto.MtTagDTO1;
import tarzan.general.api.dto.MtTagDTO2;

/**
 * 数据收集项表应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
public interface MtTagService {
    /**
     * UI 查询TAG数据
     *
     * @author benjamin
     * @date 2019-07-03 17:24
     * @param tenantId
     * @param mtTagDTO MtTagDTO
     * @return List
     */
    Page<MtTagDTO1> tagQueryForUi(Long tenantId, MtTagDTO mtTagDTO, PageRequest pageRequest);

    /**
     * UI查询数据收集组详细信息
     *
     * @author benjamin
     * @date 2019/9/17 11:36 AM
     * @param tenantId 租户Id
     * @param tagId 数据收集组Id
     * @return String
     */
    MtTagDTO1 queryTagDetailForUi(Long tenantId, String tagId);

    /**
     * UI 保存TAG数据
     *
     * @author benjamin
     * @date 2019-07-03 17:24
     * @param tenantId Long
     * @param mtTagDTO MtTagDTO
     * @return String
     */
    String tagSaveForUi(Long tenantId, MtTagDTO mtTagDTO);

    /**
     * UI 保存TAG数据
     *
     * @author benjamin
     * @date 2019-07-03 17:24
     * @param tenantId Long
     * @param mtTagDTO2 MtTagDTO
     * @return String
     */
    String tagCopyForUi(Long tenantId, MtTagDTO2 mtTagDTO2);

}
