package com.ruike.mdm.infra.mapper;

import com.ruike.mdm.api.dto.query.ModSiteQuery;
import com.ruike.mdm.api.dto.representation.ModSiteRept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 站点 mapper
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/12 15:11
 */
public interface ModSiteMapper {

    /**
     * 查询列表
     *
     * @param query 条件
     * @return java.util.List<com.ruike.mdm.api.dto.representation.ModSiteRept>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/12 03:16:06
     */
    List<ModSiteRept> listByCondition(@Param("qry") ModSiteQuery query);
}
