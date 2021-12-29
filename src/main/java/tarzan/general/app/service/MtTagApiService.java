package tarzan.general.app.service;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.general.api.dto.MtTagApiDTO;

/**
 * API转化表应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
public interface MtTagApiService {

    /**
     * 获取API转换数据（前台）
     * 
     * @param tenantId
     * @param condition
     * @param pageRequest
     * @return
     */

    Page<MtTagApiDTO> listTahApiForUi(Long tenantId, MtTagApiDTO condition, PageRequest pageRequest);

    /**
     * 新增&更新API转换数据（前台）
     *
     * @param tenantId
     * @param condition
     * @return
     */
    MtTagApiDTO saveTahApiForUi(Long tenantId, MtTagApiDTO condition);

    /**
     * 删除API转换数据（前台）
     *
     * @param tenantId
     * @param condition
     * @return
     */
    Integer deleteTahApiForUi(Long tenantId, List<String> condition);
}
