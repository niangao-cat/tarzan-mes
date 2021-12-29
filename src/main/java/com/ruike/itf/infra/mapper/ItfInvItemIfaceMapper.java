package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfInvItemIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物料接口表Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
public interface ItfInvItemIfaceMapper extends BaseMapper<ItfInvItemIface> {

    /**
     * 批量物料导入
     * @author jiangling.zheng@hand-china.com 2020-07-09 15:28
     * @param tableName
     * @param itemIfaceList
     * @return
     */
    void batchInsertItemIface(@Param(value = "tableName") String tableName,
                         @Param(value = "itemIfaceList") List<ItfInvItemIface> itemIfaceList);

    /**
     * 批量物料导入
     * @author jiangling.zheng@hand-china.com 2020-07-09 15:28
     * @param tableName
     * @param itemIfaceList
     * @return
     */
    void batchInsertItem(@Param(value = "tableName") String tableName,
                         @Param(value = "itemIfaceList") List<ItfInvItemIface> itemIfaceList);
}
