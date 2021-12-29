package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeNameplatePrintRelHeaderDTO;
import com.ruike.hme.domain.entity.HmeNameplatePrintRelHeader;
import com.ruike.hme.domain.vo.HmeNameplatePrintRelHeaderAndLineVO;
import com.ruike.hme.domain.vo.HmeNameplatePrintRelHeaderVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 铭牌打印内部识别码对应关系头表Mapper
 *
 * @author wengang.qiang@hand-chian.com 2021-10-12 10:56:11
 */
public interface HmeNameplatePrintRelHeaderMapper extends BaseMapper<HmeNameplatePrintRelHeader> {

    /**
     * 铭牌打印内部识别码对应关系头表查询
     *
     * @param tenantId 租户id
     * @param dto      查询参数
     * @return
     */
    List<HmeNameplatePrintRelHeaderVO> queryPrintRelHeader(@Param(value = "tenantId") Long tenantId,
                                                           @Param(value = "dto") HmeNameplatePrintRelHeaderDTO dto);

    /**
     * 铭牌打印历史查询
     *
     * @param tenantId          租户id
     * @param nameplateHeaderId 头表id
     * @return
     */
    List<HmeNameplatePrintRelHeaderAndLineVO> queryPrintRelHeaderAndLine(@Param(value = "tenantId") Long tenantId,
                                                                         @Param(value = "nameplateHeaderId") String nameplateHeaderId);

    /**
     * 根据序列查询
     *
     * @param tenantId       租户id
     * @param nameplateOrder 序列
     * @return
     */
    String QueryOneByOrder(@Param(value = "tenantId") Long tenantId, @Param(value = "nameplateOrder") Integer nameplateOrder);

}
