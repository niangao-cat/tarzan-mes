package com.ruike.hme.domain.service;

import com.ruike.hme.api.dto.HmeFreezeDocQueryDTO;
import com.ruike.hme.api.dto.HmeFreezeDocSaveCommandDTO;
import com.ruike.hme.api.dto.HmeFreezeDocSnUnfreezeCommandDTO;
import com.ruike.hme.domain.entity.HmeFreezeDoc;
import com.ruike.hme.domain.entity.HmeFreezePrivilege;
import com.ruike.hme.domain.vo.HmeFreezeDocVO;

import java.util.List;

/**
 * <p>
 * 条码冻结单 领域服务
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/24 10:45
 */
public interface HmeFreezeDocDomainService {

    /**
     * 创建单据
     *
     * @param tenantId 租户
     * @param dto      数据
     * @return String
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/24 10:34:32
     */
    HmeFreezeDocVO docCreation(Long tenantId, HmeFreezeDocSaveCommandDTO dto);

    /**
     * 创建单据-新版
     *
     * @param tenantId 租户
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/3 04:19:32
     * @return com.ruike.hme.api.dto.HmeFreezeDocQueryDTO
     */
    void docCreationNew(Long tenantId, HmeFreezeDocQueryDTO dto);

    /**
     * 冻结单据
     *
     * @param tenantId 租户ID
     * @param freezeDocId 冻结单ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/3 05:56:05
     * @return void
     */
    HmeFreezeDocQueryDTO docFreeze(Long tenantId, String freezeDocId);


    /**
     * 单据解冻
     *
     * @param tenantId    租户
     * @param freezeDocId 单据ID
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/24 10:34:32
     */
    void docUnfrozen(Long tenantId, String freezeDocId);

    /**
     * 勾选数据解冻
     *
     * @param tenantId 租户
     * @param command  单据ID
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/24 10:34:32
     */
    void selectionUnfrozen(Long tenantId, HmeFreezeDocSnUnfreezeCommandDTO command);

    /**
     * 提交审核
     *
     * @param tenantId    租户
     * @param freezeDocId 单据ID
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/3 05:06:22
     */
    void apply(Long tenantId, String freezeDocId);

    /**
     * 判断用户是否有解冻权限
     *
     * @param doc       冻结单
     * @param privilege 权限
     * @return java.lang.Boolean
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/19 10:35:53
     */
    Boolean isUserUnfreezePrivileged(HmeFreezeDoc doc, HmeFreezePrivilege privilege);
}
