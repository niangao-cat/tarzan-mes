package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeFreezeCosLoadRepresentationDTO;
import com.ruike.hme.api.dto.HmeFreezeDocLineSnQueryDTO;
import com.ruike.hme.domain.entity.HmeFreezeDocLine;
import com.ruike.hme.domain.vo.HmeFreezeDocJobVO;
import com.ruike.hme.domain.vo.HmeFreezeDocLineSnUnfreezeVO;
import com.ruike.hme.domain.vo.HmeFreezeDocLineVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 条码冻结单行Mapper
 *
 * @author yonghui.zhu@hand-china.com 2021-02-22 15:44:41
 */
public interface HmeFreezeDocLineMapper extends BaseMapper<HmeFreezeDocLine> {

    /**
     * 查询展示数据
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeFreezeDocLineVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/23 10:35:05
     */
    List<HmeFreezeDocLineVO> selectRepresentationList(@Param("tenantId") Long tenantId,
                                                      @Param("dto") HmeFreezeDocLineSnQueryDTO dto);

    /**
     * 批量查询job信息
     *
     * @param tenantId 租户
     * @param jobIds   作业ID列表
     * @return java.util.List<com.ruike.hme.domain.vo.HmeFreezeDocLineVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/23 01:58:28
     */
    List<HmeFreezeDocJobVO> selectJobInfoList(@Param("tenantId") Long tenantId,
                                              @Param("jobIds") Iterable<String> jobIds);

    /**
     * 批量查询job信息
     *
     * @param tenantId 租户
     * @param jobIds   作业ID列表
     * @return java.util.List<com.ruike.hme.domain.vo.HmeFreezeDocLineVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/23 01:58:28
     */
    List<HmeFreezeDocJobVO> selectJobInfoList2(@Param("tenantId") Long tenantId,
                                              @Param("jobIds") List<String> jobIds);

    /**
     * 批量插入
     *
     * @param lineList 行列表
     * @return int
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/24 11:07:48
     */
    int batchInsert(@Param("list") List<HmeFreezeDocLine> lineList);

    /**
     * 批量更新
     *
     * @param lineList 行列表
     * @return int
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/24 11:08:01
     */
    int batchUpdate(@Param("list") List<HmeFreezeDocLine> lineList);

    /**
     * sn解冻判定结果列表查询
     *
     * @param tenantId       租户
     * @param materialLotIds SN
     * @return java.util.List<com.ruike.hme.domain.vo.HmeFreezeDocLineSnUnfreezeVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/25 10:14:00
     */
    List<HmeFreezeDocLineSnUnfreezeVO> selectSnUnfreezeList(@Param("tenantId") Long tenantId,
                                                            @Param("materialLotIds") Iterable<String> materialLotIds);

    /**
     * 查询COS装载冻结信息
     *
     * @param tenantId      租户
     * @param materialLotId 物料批
     * @return java.util.List<com.ruike.hme.api.dto.HmeFreezeCosLoadRepresentationDTO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/17 02:21:01
     */
    List<HmeFreezeCosLoadRepresentationDTO> selectCostLoadList(@Param("tenantId") Long tenantId,
                                                               @Param("materialLotId") String materialLotId);
}
