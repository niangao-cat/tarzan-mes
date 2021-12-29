package com.ruike.qms.infra.mapper;

import com.ruike.qms.api.dto.QmsIqcExamineBoardDTO;
import com.ruike.qms.api.dto.QmsIqcCalSumDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * ICQ检验看板Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-05-06 10:37:10
 */
public interface QmsIqcExamineBoardMapper {


    /**
     * ICQ检验看板查询
     * @author jiangling.zheng@hand-china.com 2020-05-06 10:37:10
     * @param tenantId
     * @return
     */
    List<QmsIqcExamineBoardDTO> selectIqcExamineBoard(@Param("tenantId") Long tenantId);

    /**
     * ICQ检验看板30天物料量查询
     * @author jiangling.zheng@hand-china.com 2020-05-06 10:37:10
     * @param tenantId
     * @return
     */
    List<QmsIqcCalSumDTO> selectIqcDays(@Param("tenantId") Long tenantId);

    /**
     * ICQ检验看板12个月物料量查询
     * @author jiangling.zheng@hand-china.com 2020-05-06 10:37:10
     * @param tenantId
     * @return
     */
    List<QmsIqcCalSumDTO> selectIqcMonths(@Param("tenantId") Long tenantId);
}
