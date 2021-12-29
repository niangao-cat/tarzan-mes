package com.ruike.hme.infra.mapper;

import java.util.List;

import com.ruike.hme.domain.entity.HmeExcGroupWkcAssign;
import io.choerodon.mybatis.common.BaseMapper;

import org.apache.ibatis.annotations.Param;

/**
 * 异常收集组分配WKC关系表Mapper
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:23
 */
public interface HmeExcGroupWkcAssignMapper extends BaseMapper<HmeExcGroupWkcAssign> {

    /**
     * 异常组ID查询异常组WKC
     *
     * @param tenantId         租户ID
     * @param exceptionGroupId 异常组ID
     * @return java.util.List<com.ruike.hme.domain.entity.HmeExcGroupWkcAssign>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/7/15 11:41:23
     */
    List<HmeExcGroupWkcAssign> selectByExceptionGroupId(@Param(value = "tenantId") Long tenantId,
                                                        @Param(value = "exceptionGroupId") String exceptionGroupId);

    /**
     * 查询异常组下最大的序号
     *
     * @param tenantId         租户id
     * @param exceptionGroupId 异常组id
     * @return java.lang.Long
     * @author sanfeng.zhang@hand-china.com 2020/12/9 10:43
     */
    Long queryMaxSerialNumber(@Param("tenantId") Long tenantId, @Param(value = "exceptionGroupId") String exceptionGroupId);
}
