package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeEqTaskDocLineQueryDTO;
import com.ruike.hme.api.dto.HmeEqTaskDocQueryDTO;
import com.ruike.hme.domain.vo.HmeEqTaskDocAndLineExportVO;
import com.ruike.hme.domain.vo.HmeEqTaskHisVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeEqManageTaskDocLine;

import java.util.List;

/**
 * 设备管理任务单行表资源库
 *
 * @author jiangling.zheng@hand-china.com 2020-06-16 16:06:10
 */
public interface HmeEqManageTaskDocLineRepository extends BaseRepository<HmeEqManageTaskDocLine>, AopProxy<HmeEqManageTaskDocLineRepository> {

    /**
     * 任务单行查询
     *
     * @param tenantId
     * @param taskDocId
     * @return
     */
    List<HmeEqTaskDocLineQueryDTO> queryTaskDocLineList(Long tenantId, String taskDocId);

    /**
     * 编辑行
     *
     * @param tenantId
     * @param dto
     * @return com.ruike.hme.api.dto.HmeEqTaskDocLineQueryDTO
     * @author sanfeng.zhang@hand-china.com 2020/10/16 14:58
     */
    HmeEqTaskDocLineQueryDTO updateEqCheckDoc(Long tenantId, HmeEqTaskDocLineQueryDTO dto);

    /**
     * 任务行修改历史
     *
     * @param tenantId
     * @param taskDocLineId
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeEqTaskHisVO>
     * @author sanfeng.zhang@hand-china.com 2021/3/4 22:44
     */
    Page<HmeEqTaskHisVO> taskHistoryListQuery(Long tenantId, String taskDocLineId, PageRequest pageRequest);

    /**
     * 任务单和任务单行-导出
     * @param tenantId
     * @param dto
     * @return
     */
    List<HmeEqTaskDocAndLineExportVO> queryTaskDocListAndTaskDocLineList(Long tenantId, HmeEqTaskDocQueryDTO dto);
}
