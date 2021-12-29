package com.ruike.hme.domain.repository;

import java.util.List;

import com.ruike.hme.api.dto.HmeToolHisDTO;
import com.ruike.hme.domain.entity.HmeTool;
import com.ruike.hme.domain.vo.HmeToolHisVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeToolHis;

/**
 * 工装基础数据历史表资源库
 *
 * @author li.zhang13@hand-china.com 2021-01-07 10:06:46
 */
public interface HmeToolHisRepository extends BaseRepository<HmeToolHis> {

    /**
     * @param tenantId
     * @param hmeToolHisDTO
     * @Description 根据工装ID查询工装修改历史记录
     * @Author li.zhang13@hand-china.com
     */
    Page<HmeToolHisVO> selectHmeToolHis(PageRequest pageRequest, String tenantId, HmeToolHisDTO hmeToolHisDTO);

    /**
     * 新增工装管理历史
     *
     * @param tenantId 租户ID
     * @param dtoList 工装管理历史保存参数
     * @author penglin.sui@hand-china.com 2021/1/11 14:16
     * @return java.util<com.ruike.hme.domain.entity.HmeToolHis>
     */
    List<HmeToolHis> insertHmeToolHis(Long tenantId, List<HmeTool> dtoList);
}
