package com.ruike.hme.domain.service;

import com.ruike.hme.api.dto.HmeFreezePrivilegeSaveCommandDTO;
import com.ruike.hme.domain.vo.HmeFreezePrivilegeVO;

/**
 * <p>
 * 条码冻结权限 领域服务
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/1 11:21
 */
public interface HmeFreezePrivilegeDomainService {

    /**
     * 保存
     *
     * @param dto 参数
     * @return com.ruike.hme.domain.vo.HmeFreezePrivilegeVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/1 09:39:12
     */
    HmeFreezePrivilegeVO save(HmeFreezePrivilegeSaveCommandDTO dto);
}
