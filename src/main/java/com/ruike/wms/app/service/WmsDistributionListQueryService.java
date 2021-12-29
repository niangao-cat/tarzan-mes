package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsReplenishmentCreateDTO;
import com.ruike.wms.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface WmsDistributionListQueryService {

    /**
     * 查询配送单头
     *
     * @param tenantId
     * @param dto
     * @author yifan.xiong
     * @date 2020-9-2 16:20:42
     */
    Page<WmsDistributionListQueryVO> propertyDistributionDocQuery(Long tenantId, WmsDistributionListQueryVO dto, PageRequest pageRequest);

    /**
     * 查询配送单行
     *
     * @param tenantId
     * @param instructionDocId
     * @author yifan.xiong
     * @date 2020-9-2 18:32:21
     */
    Page<WmsDistributionListQueryVO1> propertyDistributionQuery(Long tenantId, String instructionDocId, PageRequest pageRequest);

    /**
     * 查询配送单明细
     *
     * @param tenantId
     * @param instructionId
     * @author yifan.xiong
     * @date 2020-9-2 18:32:21
     */
    Page<WmsDistributionListQueryVO2> propertyDistributionDtlQuery(Long tenantId, String instructionId, PageRequest pageRequest);

    /**
     * 配送单打印
     *
     * @param tenantId
     * @param instructionDocIds
     * @return
     * @Description 配送单打印
     * @Date 2020-9-9 15:34:30
     * @Created by yifan.xiong
     */
    void multiplePrint(Long tenantId, List<String> instructionDocIds, HttpServletResponse response);

    /**
     * 取消配送单
     *
     * @param tenantId          租户id
     * @param instructionDocIds
     * @author yifan.xiong 2020-9-10 17:35:23
     */
    void cancelDistribution(Long tenantId, List<String> instructionDocIds);

    /**
     * 关闭配送单
     *
     * @param tenantId          租户id
     * @param instructionDocIds
     * @author 2020-9-10 20:38:19
     */
    void closeDistribution(Long tenantId, List<String> instructionDocIds);

    /**
     * 补料单行获取
     *
     * @param tenantId          租户
     * @param instructionDocIds 勾选的配送单ID
     * @param pageRequest       分页参数
     * @return java.util.List<com.ruike.hme.domain.vo.HmeReplenishmentLineVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/10/26 05:18:45
     */
    List<WmsReplenishmentLineVO> replenishmentLineGet(Long tenantId, List<String> instructionDocIds, PageRequest pageRequest);

    /**
     * 补料单生成
     *
     * @param tenantId 租户
     * @param dto      生成数据
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/3 02:34:04
     */
    void replenishmentDocCreate(Long tenantId, WmsReplenishmentCreateDTO dto);

    /***
     * 配送单导出
     * @param tenantId
     * @param instructionDocId
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDistributionDocVO>
     * @author sanfeng.zhang@hand-china.com 2021/5/20
     */
    List<WmsDistributionDocVO> instructionDocExport(Long tenantId, String instructionDocId);
}
