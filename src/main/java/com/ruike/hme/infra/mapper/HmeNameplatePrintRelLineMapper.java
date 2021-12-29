package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeNameplatePrintRelLine;
import com.ruike.hme.domain.vo.HmeNameplatePrintRelLineVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 铭牌打印内部识别码对应关系行表Mapper
 *
 * @author wengang.qiang@hand-chian.com 2021-10-12 10:56:13
 */
public interface HmeNameplatePrintRelLineMapper extends BaseMapper<HmeNameplatePrintRelLine> {

    /**
     * 铭牌打印内部识别码对应关系行表数据查询
     *
     * @param tenantId          租户id
     * @param nameplateHeaderId 头id
     * @return
     */
    List<HmeNameplatePrintRelLineVO> queryPrintRelLine(@Param(value = "tenantId") Long tenantId,
                                                       @Param(value = "nameplateHeaderId") String nameplateHeaderId);

}
