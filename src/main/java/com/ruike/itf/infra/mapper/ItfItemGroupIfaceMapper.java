package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfItemGroupIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.iface.domain.entity.MtBomComponentIface;

import java.util.List;

/**
 * 物料组接口表Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-07-17 10:12:42
 */
public interface ItfItemGroupIfaceMapper extends BaseMapper<ItfItemGroupIface> {

    /**
     * 批量物料组导入
     * @author jiangling.zheng@hand-china.com 2020-07-17 10:12:42
     * @param itemGroupIfaceList
     * @return
     */
    void batchInsertItemGroup(@Param(value = "itemGroupIfaceList") List<ItfItemGroupIface> itemGroupIfaceList);

    /**
     * 获取为导入数据
     * condition: status is 'N' or 'E'
     *
     * @param tenantId
     * @author jiangling.zheng@hand-china.com 2020/7/17 11:52
     * @return List<ItfItemGroupIface>
     */
    List<ItfItemGroupIface> getUnprocessedList(@Param(value = "tenantId") Long tenantId);
}
