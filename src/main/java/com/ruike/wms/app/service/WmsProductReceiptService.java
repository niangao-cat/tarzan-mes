package com.ruike.wms.app.service;

import com.ruike.wms.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2020/9/10 10:31
 */
public interface WmsProductReceiptService {

    /**
     * 入库单-头查询
     *
     * @param tenantId          租户id
     * @param reqVO             参数
     * @param pageRequest       分页参数
     * @author sanfeng.zhang@hand-china.com 2020/9/10 10:42
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsReceiptDocVO>
     */
    Page<WmsReceiptDocVO> receiptDocQuery(Long tenantId, WmsReceiptDocReqVO reqVO , PageRequest pageRequest);


    /**
     * 入库单-行查询
     *
     * @param tenantId          租户id
     * @param docVO             参数
     * @param pageRequest       分页参数
     * @author sanfeng.zhang@hand-china.com 2020/9/10 15:57
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsReceiptLineVO>
     */
    Page<WmsReceiptLineVO> receiptDocLineQuery(Long tenantId, WmsReceiptDocVO docVO , PageRequest pageRequest);

    /**
     * 入库单-行明细
     *
     * @param tenantId              租户id
     * @param docVO                 参数
     * @param pageRequest           分页参数
     * @author sanfeng.zhang@hand-china.com 2020/9/10 17:10
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsReceiptDetailVO>
     */
    Page<WmsReceiptDetailVO> receiptDocLineDetail(Long tenantId, WmsReceiptDetailReqVO docVO , PageRequest pageRequest);

    /**
     * 入库单打印
     *
     * @param tenantId
     * @param instructionDocIds
     * @return
     * @Description 入库单
     * @Date 2020-9-22 14:23:07
     * @Created by yifan.xiong
     */
    void multiplePrint(Long tenantId, List<String> instructionDocIds, HttpServletResponse response);

    /**
     * 入库单撤回
     * @param tenantId
     * @param instructionDocIds
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/5/25
     */
    void retractReceiptDoc(Long tenantId, List<String> instructionDocIds);
}
