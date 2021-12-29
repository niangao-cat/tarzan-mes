package com.ruike.itf.domain.repository;

import com.ruike.itf.api.dto.ItfMaterialLotConfirmIfaceDTO;
import com.ruike.itf.domain.vo.ItfMaterialLotConfirmIfaceVO2;
import com.ruike.itf.domain.vo.ItfMaterialLotConfirmIfaceVO4;
import com.ruike.itf.domain.vo.ItfWcsResponseVO;
import org.hzero.boot.interfaces.sdk.dto.ResponsePayloadDTO;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.itf.domain.entity.ItfMaterialLotConfirmIface;

import java.util.List;
import java.util.Map;

/**
 * 立库入库复核接口表资源库
 *
 * @author chaonan.hu@hand-china.com 2021-07-13 19:40:25
 */
public interface ItfMaterialLotConfirmIfaceRepository extends BaseRepository<ItfMaterialLotConfirmIface>, AopProxy<ItfMaterialLotConfirmIfaceRepository> {

    /**
     * 根据物料批ID查询数据，并将数据插入到接口表中
     *
     * @param tenantId 租户ID
     * @param materialLotIdList 物料批ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/14 09:29:29
     * @return java.util.List<com.ruike.itf.domain.entity.ItfMaterialLotConfirmIface>
     */
    String insertMaterialLotConfirmIface(Long tenantId, List<String> materialLotIdList);

    /**
     * MES-WCS接口方法
     *
     * @param requestMap 传输数据
     * @param logName 接口方法日志输出名
     * @param itfPath 接口编码
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/14 01:51:30
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    ResponsePayloadDTO sendWcs(Object requestMap , String logName, String itfPath);

    /**
     * 批量将接口数据更新为同一状态同一信息
     *
     * @param tenantId 租户ID
     * @param ifaceIdList 接口ID集合
     * @param message 信息
     * @param userId 用户
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/14 04:04:20
     * @return void
     */
    void updateIfaceData(Long tenantId, List<String> ifaceIdList, String message, Long userId);

    /**
     * 校验返回报文
     *
     * @param tenantId 租户ID
     * @param responsePayload 返回报文
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/14 04:08:24
     * @return void
     */
    ItfWcsResponseVO validateResponsePayload(Long tenantId, ResponsePayloadDTO responsePayload,
                                             List<String> ifaceIdList, Long userId);

    /**
     * 根据正常返回的报文去更新接口数据
     * 
     * @param tenantId 租户ID
     * @param header 报文数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/14 04:37:12 
     * @return void
     */
    List<ItfMaterialLotConfirmIfaceVO4> updateIface(Long tenantId, List<ItfMaterialLotConfirmIfaceVO2> header, String batchId, Long userId,
                                                    List<String> ifaceIdList);

    /**
     * 
     * @description 根据物料批ID查询数据，并将数据插入到接口表中
     * 
     * @param itfMaterialLotConfirmIfaceList 要插入的主数据
     * @author yuchao.wang@hand-china.com
     * @date 2021/8/7 15:03
     * @return String 批次ID
     * 
     */
    String insertMaterialLotConfirmIfaceMain(Long tenantId, List<ItfMaterialLotConfirmIface> itfMaterialLotConfirmIfaceList);
}
