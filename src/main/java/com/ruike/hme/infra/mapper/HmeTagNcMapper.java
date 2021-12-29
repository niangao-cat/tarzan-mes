package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeTagNcDTO;
import com.ruike.hme.domain.entity.HmeTagNc;
import com.ruike.hme.domain.vo.HmeTagFormulaHeadVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 数据项不良判定基础表Mapper
 *
 * @author guiming.zhou@hand-china.com 2020-09-24 16:00:30
 */
public interface HmeTagNcMapper extends BaseMapper<HmeTagNc> {
    /**
     * 获取行信息
     *
     * @param tenantId
     * @param hmeTagNc
     * @author guiming.zhou@hand-china.com 2020/9/25 14:06
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeTagNcDTO>
     */
    List<HmeTagNcDTO> getTagNcList(@Param("tenantId") Long tenantId, @Param("vo") HmeTagNc hmeTagNc);
}
