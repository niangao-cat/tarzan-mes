package com.ruike.mdm.domain.repository;

import com.ruike.mdm.api.dto.query.ModSiteQuery;
import com.ruike.mdm.api.dto.representation.ModSiteRept;

import java.util.List;

/**
 * <p>
 * 站点资源库
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/12 15:19
 */
public interface ModSiteRepository {

    /**
     * 查询列表
     *
     * @param query 条件
     * @return java.util.List<com.ruike.mdm.api.dto.representation.ModSiteRept>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/12 03:16:06
     */
    List<ModSiteRept> list(ModSiteQuery query);
}
