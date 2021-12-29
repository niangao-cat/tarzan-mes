package com.ruike.wms.domain.repository;

import com.ruike.wms.domain.vo.*;
import org.apache.ibatis.annotations.Param;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO3;

import java.util.List;

/**
 * 备料执行 资源库
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/25 16:57
 */
public interface WmsPrepareExecuteRepository {
    /**
     * 通过配送单号查询配送单
     *
     * @param tenantId          租户
     * @param instructionDocNum 配送单号
     * @return com.ruike.wms.domain.vo.WmsDistDemandInsDocVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/24 08:27:20
     */
    WmsPrepareExecInsDocVO selectDistDocByNum(Long tenantId, String instructionDocNum);

    /**
     * 根据单据行ID查询推荐货位
     *
     * @param tenantId      租户
     * @param instructionId 单据行ID
     * @return tarzan.modeling.domain.entity.MtModLocator
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/10/22 02:45:37
     */
    MtModLocator getRecommendLocator(Long tenantId, String instructionId);

    /**
     * 通过单据ID查询配送单行
     *
     * @param tenantId         租户
     * @param instructionDocId 配送单ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDistDemandInsVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/24 09:41:54
     */
    List<WmsPrepareExecInsVO> selectInsListByDocId(Long tenantId, String instructionDocId);

    /**
     * 查询货位是否在线边仓
     *
     * @param tenantId  租户
     * @param locatorId 货位
     * @return tarzan.modeling.domain.entity.MtModLocator
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/25 04:05:04
     */
    MtModLocator selectLocatorOnWarehouse(Long tenantId
            , String locatorId);

    /**
     * 通过指令行ID查询指令实际
     *
     * @param tenantId      租户
     * @param instructionId 指令行ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsInstructionActualVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/27 02:56:17
     */
    List<WmsInstructionActualDetailVO> selectActualDetailByInstId(Long tenantId, String instructionId);

    /**
     * 通过指令ID查询指令实际
     *
     * @param tenantId         租户
     * @param instructionDocId 指令ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsInstructionActualVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/27 02:56:17
     */
    List<WmsInstructionActualDetailVO> selectActualDetailByDocId(Long tenantId, String instructionDocId);

    /**
     * 查询目标货位
     *
     * @param tenantId       租户
     * @param signFlag       标志
     * @param instructionDoc 指令
     * @param scan           扫描
     * @return java.util.List<tarzan.modeling.domain.entity.MtModLocator>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/27 08:07:14
     */
    WmsLocatorSiteVO selectTargetLocator(Long tenantId, String signFlag, WmsInstructionDocAttrVO instructionDoc, WmsInstructionActualDetailVO scan);

    /**
     * 查询货位的站点
     *
     * @param tenantId  租户
     * @param locatorId 标志
     * @return java.util.List<tarzan.modeling.domain.entity.MtModLocator>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/27 08:07:14
     */
    MtModLocatorOrgRelVO3 selectSiteByLocator(Long tenantId, String locatorId);

    /**
     * 通过站点查询推荐的备料货位
     *
     * @param tenantId        租户
     * @param materialId      物料
     * @param materialVersion 物料版本
     * @param siteId          站点ID
     * @param soNum           销售订单号
     * @param soLineNum       销售订单行号
     * @return tarzan.modeling.domain.entity.MtModLocator
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/25 04:05:04
     */
    MtModLocator selectDistLocatorBySiteId(Long tenantId
            , String materialId
            , String materialVersion
            , String siteId
            , String soNum
            , String soLineNum);
}
