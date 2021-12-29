package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeFreezePrivilegeQueryDTO;
import com.ruike.hme.domain.entity.HmeFreezePrivilege;
import com.ruike.hme.domain.vo.HmeFreezePrivilegeVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 条码冻结权限Mapper
 *
 * @author yonghui.zhu@hand-china.com 2021-02-26 17:41:20
 */
public interface HmeFreezePrivilegeMapper extends BaseMapper<HmeFreezePrivilege> {

    /**
     * 查询展示列表
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeFreezePrivilegeVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/1 08:59:27
     */
    List<HmeFreezePrivilegeVO> selectRepresentationList(@Param("tenantId") Long tenantId,
                                                        @Param("dto") HmeFreezePrivilegeQueryDTO dto);

    /**
     * 获取用户名Id
     *
     * @param realName
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-03-24 15:46
     * @return Long
     */
    Long getUserId(String realName);

    /**
     * 导入唯一性校验
     *
     * @param tenantId
     * @param userId
     * @param detailObjectId
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-03-24 16:37
     * @return int
     */
    int selectExist(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("detailObjectId") String detailObjectId);
}
