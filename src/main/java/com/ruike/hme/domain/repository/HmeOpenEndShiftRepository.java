package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeOpenEndShiftDTO;
import com.ruike.hme.api.dto.HmeOpenEndShiftDTO2;
import com.ruike.hme.api.dto.HmeShiftDTO;
import com.ruike.hme.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.Date;
import java.util.List;

/**
 * 班组工作平台-开班结班管理-资源库
 *
 * @author chaonan.hu@hand-china.com 2020-07-07 09:49:32
 */
public interface HmeOpenEndShiftRepository {

    /***
     * @Description 工段下拉框数据查询
     * @param tenantId 租户Id
     * @return java.util.List<com.ruike.hme.domain.vo.HmeOpenEndShiftVO>
     * @auther chaonan.hu
     * @date 2020/7/7
     */
    List<HmeOpenEndShiftVO> lineWorkellDataQuery(Long tenantId);

    /***
     * @Description 班次下拉框数据查询
     * @param tenantId 租户Id
     * @param dto
     * @return java.util.List<com.ruike.hme.domain.vo.HmeOpenEndShiftVO2>
     * @auther chaonan.hu
     * @date 2020/7/7
    */
    List<HmeOpenEndShiftVO2> shiftQuery(Long tenantId, HmeOpenEndShiftDTO dto);

    /**
     * 根据工段查询当前班次和日期
     *
     * @param tenantId 租户ID
     * @param lineWorkcellId 工段ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/29 15:35:38
     * @return com.ruike.hme.domain.vo.HmeOpenEndShiftVO4
     */
    HmeOpenEndShiftVO4 shiftDateAndCodeQuery(Long tenantId, String lineWorkcellId);

    /***
     * @Description 查询开结班的计划和实际时间
     * @param tenantId 租户Id
     * @param dto
     * @return com.ruike.hme.domain.vo.HmeOpenEndShiftVO3
     * @auther chaonan.hu
     * @date 2020/7/7
    */
    HmeOpenEndShiftVO3 dateTimeQuery(Long tenantId, HmeOpenEndShiftDTO2 dto);

    /***
     * @Description 加载开班实际时间
     * @param tenantId 租户Id
     * @param dto
     * @return java.util.Date
     * @auther chaonan.hu
     * @date 2020/7/7
    */
    Date openShiftActualDate(Long tenantId, HmeOpenEndShiftDTO2 dto);

    /***
     * @Description 加载结班实际时间
     * @param tenantId 租户Id
     * @param dto
     * @return java.util.Date
     * @auther chaonan.hu
     * @date 2020/7/7
     */
    Date endShiftActualDate(Long tenantId, HmeOpenEndShiftDTO2 dto);

    /**
     * 班组信息
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/28 16:01:12
     * @return com.ruike.hme.domain.vo.HmeShiftVO
     */
    HmeShiftVO shiftDataQuery(Long tenantId, HmeShiftDTO dto);

    /**
     * 完工统计
     * 
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/29 09:32:06 
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeShiftVO5>
     */
    Page<HmeShiftVO5> completeStatistical(Long tenantId, HmeShiftDTO dto, PageRequest pageRequest);

    /**
     * 产品节拍
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/30 10:17:46
     * @return com.ruike.hme.domain.vo.HmeShiftVO7
     */
    HmeShiftVO7 productBeat(Long tenantId, HmeShiftDTO dto);

    /**
     * 交接注意事项查询
     *
     * @param tenantId 租户ID
     * @param wkcShiftId
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/31 16:51:49
     * @return com.ruike.hme.domain.vo.HmeShiftVO8
     */
    HmeShiftVO8 handoverMattersQuery(Long tenantId, String wkcShiftId);

    /**
     * 本月安全日历
     *
     * @param tenantId 租户ID
     * @param siteId 站点ID
     * @param lineWorkcellId 工段ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/1 11:34:16
     * @return java.util.List<com.ruike.hme.domain.vo.HmeShiftVO9>
     */
    List<HmeShiftVO9> monthSecurityCalendar(Long tenantId, String siteId, String lineWorkcellId);

    /**
     * 工艺质量
     * 
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/3 09:33:18
     * @return com.ruike.hme.domain.vo.HmeShiftVO7
     */
    HmeShiftVO7 operationQuality(Long tenantId, HmeShiftDTO dto);

    /**
     * 设备管理
     * 
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/3 10:38:06 
     * @return com.ruike.hme.domain.vo.HmeShiftVO7
     */
    HmeShiftVO7 equipmentManage(Long tenantId, HmeShiftDTO dto);

    /**
     * 其他异常
     * 
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/3 14:25:56
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeShiftVO11>
     */
    Page<HmeShiftVO11> otherException(Long tenantId, HmeShiftDTO dto, PageRequest pageRequest);
}
