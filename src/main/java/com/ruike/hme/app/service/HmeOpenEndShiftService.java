package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeOpenEndShiftDTO;
import com.ruike.hme.api.dto.HmeOpenEndShiftDTO2;
import com.ruike.hme.api.dto.HmeOpenEndShiftEndCancelCommandDTO;
import com.ruike.hme.domain.vo.HmeOpenEndShiftVO;
import com.ruike.hme.domain.vo.HmeOpenEndShiftVO2;
import com.ruike.hme.domain.vo.HmeOpenEndShiftVO3;
import com.ruike.hme.domain.vo.HmeOpenEndShiftVO4;

import java.util.Date;
import java.util.List;

/**
 * 班组工作平台-开班结班管理应用服务
 *
 * @author chaonan.hu@hand-china.com 2020-07-07 09:46:16
 */
public interface HmeOpenEndShiftService {

    /**
     * 工段下拉框数据查询
     *
     * @param tenantId 租户Id
     * @return java.util.List<com.ruike.hme.domain.vo.HmeOpenEndShiftVO>
     */
    List<HmeOpenEndShiftVO> lineWorkellDataQuery(Long tenantId);

    /**
     * 班次下拉框数据查询
     *
     * @param tenantId 租户Id
     * @param dto      查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeOpenEndShiftVO2>
     */
    List<HmeOpenEndShiftVO2> shiftQuery(Long tenantId, HmeOpenEndShiftDTO dto);

    /**
     * 根据工段查询当前班次和日期
     *
     * @param tenantId       租户ID
     * @param lineWorkcellId 工段ID
     * @return com.ruike.hme.domain.vo.HmeOpenEndShiftVO4
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/29 15:35:38
     */
    HmeOpenEndShiftVO4 shiftDateAndCodeQuery(Long tenantId, String lineWorkcellId);

    /**
     * 查询开结班的计划和实际时间
     *
     * @param tenantId 租户Id
     * @param dto      查询条件
     * @return com.ruike.hme.domain.vo.HmeOpenEndShiftVO3
     */
    HmeOpenEndShiftVO3 dateTimeQuery(Long tenantId, HmeOpenEndShiftDTO2 dto);

    /**
     * 加载开班实际时间
     *
     * @param tenantId 租户Id
     * @param dto      查询条件
     * @return java.util.Date
     */
    Date openShiftActualDate(Long tenantId, HmeOpenEndShiftDTO2 dto);

    /**
     * 加载结班实际时间
     *
     * @param tenantId 租户Id
     * @param dto      查询条件
     * @return java.util.Date
     */
    Date endShiftActualDate(Long tenantId, HmeOpenEndShiftDTO2 dto);

    /**
     * 接班撤销
     *
     * @param tenantId 租户Id
     * @param command  命令
     */
    void shiftEndCancel(Long tenantId, HmeOpenEndShiftEndCancelCommandDTO command);
}
