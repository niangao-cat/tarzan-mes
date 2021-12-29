package com.ruike.itf.domain.repository;

import com.ruike.itf.api.dto.ItfLightTaskIfaceDTO;
import com.ruike.itf.domain.vo.*;
import org.hzero.boot.interfaces.sdk.dto.ResponsePayloadDTO;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.itf.domain.entity.ItfLightTaskIface;

import java.util.List;
import java.util.Map;

/**
 * 亮灯指令接口表资源库
 *
 * @author li.zhang13@hand-china.com 2021-08-09 11:12:14
 */
public interface ItfLightTaskIfaceRepository extends BaseRepository<ItfLightTaskIface> {

    /**
     * 对传入数据进行整理，并将数据插入或更新到接口表中
     *
     * @param tenantId 租户ID
     * @param dtoList 传入数据集合
     * @author li.zhang13@hand-china.com 2021-08-09 11:12:14
     */
    List<String> insertitfLightTaskIface(Long tenantId, List<ItfLightTaskIfaceDTO> dtoList);

    /**
     * 校验返回报文
     *
     * @param tenantId 租户ID
     * @param responsePayload 返回报文
     * @author li.zhang13@hand-china.com 2021-08-09 11:12:14
     * @return ItfWcsResponseVO2
     */
    ItfWcsResponseVO2 validateResponsePayload(Long tenantId, ResponsePayloadDTO responsePayload, List<String> ifaceIdList, Long userId);

    /**
     * 批量将接口数据更新为同一状态同一信息
     *
     * @param tenantId 租户ID
     * @param ifaceIdList 接口ID集合
     * @param message 信息
     * @param userId 用户
     * @author li.zhang13@hand-china.com 2021-08-09 11:12:14
     * @return void
     */
    void updateIfaceData(Long tenantId, List<String> ifaceIdList, String message, Long userId);

    /**
     * 根据正常返回的报文去更新接口数据
     *
     * @param tenantId 租户ID
     * @param header 报文数据
     * @author li.zhang13@hand-china.com 2021-08-09 11:12:14
     * @return ItfLightTaskIfaceVO
     */
    List<ItfLightTaskIfaceVO> updateIface(Long tenantId, List<ItfLightTaskIfaceVO3> header, Long userId, List<String> ifaceIdList, List<String> taskNumList);

    /**
     * MES-WCS接口方法
     *
     * @param requestMap 传输数据
     * @param logName 接口方法日志输出名
     * @param itfPath 接口编码
     * @author li.zhang13@hand-china.com 2021-08-09 11:12:14
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    ResponsePayloadDTO sendLight(Object requestMap, String logName, String itfPath);
}
