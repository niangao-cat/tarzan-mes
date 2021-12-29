package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.ItfObjectTransactionResultQueryDTO;
import com.ruike.itf.domain.entity.ItfObjectTransactionIface;
import com.ruike.itf.domain.vo.ItfObjectTransactionIfaceVO;
import com.ruike.wms.domain.entity.WmsObjectTransaction;
import com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * 事务汇总接口表应用服务
 *
 * @author yonghui.zhu@hand-china.com 2020-08-11 09:44:09
 */
public interface ItfObjectTransactionIfaceService {

    /**
     * 处理汇总
     *
     * @param tenantId 租户
     * @param type     是否为实时发送
     * @return java.util.List<com.ruike.itf.domain.entity.ItfObjectTransactionIface>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/11 10:44:40
     */
    List<ItfObjectTransactionIface> processSummary(Long tenantId, String type, List<WmsObjectTransaction> detailList);

    /**
     * 实时接口，物料移动
     *
     * @param tenantId                        租户
     * @param wmsObjectTransactionResponseVOS
     * @return java.util.List<com.ruike.itf.domain.entity.ItfObjectTransactionIface>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/11 10:44:40
     */
    List<ItfObjectTransactionIface> sendSapMaterialMove(Long tenantId, List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS);

    /**
     * 实时报功，实时移动接口
     *
     * @param tenantId                        租户
     * @param reportList
     * @param workList
     * @author kejin.liu01@hand-china.com 2020/8/11 10:44:40
     */
    void sendSapProdMaterialMove(Long tenantId, List<WmsObjectTransactionResponseVO> reportList,List<WmsObjectTransactionResponseVO> workList);

    /**
     * 排序发送 实时接口，物料移动
     *
     * @param tenantId
     * @param wmsObjectTransactionResponseVOS
     * @param tranType                        排序类型
     * @return
     */
    List<ItfObjectTransactionIface> sendSapMaterialMoveSort(Long tenantId, List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS, List<String> tranType);


    /**
     * 接口表执行成功状态
     */
    String STATUS_SUCCESS = "S";

    /**
     * 接口表执行失败状态
     */
    String STATUS_ERROR = "E";

    /**
     * 接口表初始状态状态
     */
    String STATUS_NORMAL = "N";

    /**
     * 接口表执行中状态
     */
    String STATUS_PROCESS = "P";

    /**
     * HTTP请求成功
     */
    Integer HTTP_STATUS_OK = 200;

    /**
     * ESB接口成功状态码
     */
    String ESB_STATUS_SUCCESS = "S00001";

    /**
     * 物料移动回传ERP
     *
     * @param tenantId
     * @param list
     * @param type     是否为实时发送
     * @return
     * @author kejin.liu01@hand-china.com 2020/8/24 16:46
     */
    void materialMove(Long tenantId, String type, List<ItfObjectTransactionIface> list) throws Exception;

    /**
     * 生产报工回传ERP
     *
     * @param tenantId
     * @param wmsObjectTransactionResponseVOS
     * @return
     * @author kejin.liu01@hand-china.com 2020/8/24 16:46
     */
    void productionStatementInvoke(Long tenantId, String type,List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS);


    /**
     * @description 事务查询
     * @param tenantId
     * @param queryDTO
     * @param pageRequest
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2020/11/11
     * @time 16:48
     * @version 0.0.1
     * @return java.util.List<com.ruike.itf.domain.entity.ItfObjectTransactionIface>
     */
    Page<ItfObjectTransactionIfaceVO> list(Long tenantId, ItfObjectTransactionResultQueryDTO queryDTO, PageRequest pageRequest);

    /**
     * @description 更新数据
     * @param itfObjectTransactionIface
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2020/11/11
     * @time 19:29
     * @version 0.0.1
     * @return void
     */
    ItfObjectTransactionIface update(Long tenantId, ItfObjectTransactionIfaceVO itfObjectTransactionIface);


}
