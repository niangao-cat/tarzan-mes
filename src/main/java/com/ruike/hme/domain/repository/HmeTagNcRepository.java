package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeTagNcDTO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeTagNc;

import java.util.List;

/**
 * 数据项不良判定基础表资源库
 *
 * @author guiming.zhou@hand-china.com 2020-09-24 16:00:30
 */
public interface HmeTagNcRepository extends BaseRepository<HmeTagNc> {

    /**
     * 获取不良信息
     *
     * @param tenantId
     * @param hmeTagNc
     * @param pageRequest
     * @author guiming.zhou@hand-china.com 2020/9/25 14:06
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeTagNcDTO>
     */
    List<HmeTagNcDTO> getTagNcList(Long tenantId, HmeTagNc hmeTagNc, PageRequest pageRequest);
}
