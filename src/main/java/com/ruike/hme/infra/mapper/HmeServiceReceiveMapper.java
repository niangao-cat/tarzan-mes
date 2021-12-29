package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeServiceReceiveDTO;
import com.ruike.hme.domain.entity.HmeServiceReceive;
import com.ruike.hme.domain.vo.HmeServiceReceiveVO3;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 营销服务部接收拆箱登记表Mapper
 *
 * @author chaonan.hu@hand-china.com 2020-09-01 14:14:21
 */
public interface HmeServiceReceiveMapper extends BaseMapper<HmeServiceReceive> {

    /**
     * 站点下物料数据查询
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/1 15:44:39
     * @return java.util.List<com.ruike.hme.domain.vo.HmeServiceReceiveVO3>
     */
    List<HmeServiceReceiveVO3> materialLovQuery(@Param(value = "tenantId") Long tenantId,
                                                @Param(value = "dto") HmeServiceReceiveDTO dto,
                                                @Param(value = "itemTypeList") List<String> itemTypeList);

    /**
     * 根据SN查询SAP物料关系表中对应的物料
     *
     * @param tenantId 租户ID
     * @param snNum
     * @param siteId 站点ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/23 14:10:37
     * @return java.lang.String
     */
    String getMaterialIdBySnNum(@Param(value = "tenantId") Long tenantId,@Param(value = "snNum") String snNum,
                                @Param(value = "siteId") String siteId);

    /**
     * 根据租户+SN查询SN是否存在未返修完成的记录
     *
     * @param tenantId 租户ID
     * @param snNum SN
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/30 09:26:39
     * @return java.lang.Long
     */
    Long getTotalBySn(@Param(value = "tenantId") Long tenantId,@Param(value = "snNum") String snNum);
}
