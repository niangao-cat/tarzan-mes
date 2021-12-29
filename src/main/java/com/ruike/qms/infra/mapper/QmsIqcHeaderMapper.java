package com.ruike.qms.infra.mapper;

import com.ruike.qms.domain.entity.QmsIqcHeader;
import com.ruike.qms.domain.entity.QmsSampleScheme;
import com.ruike.qms.domain.entity.QmsSampleSizeCodeLetter;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.math.BigDecimal;
import java.util.List;

/**
 * 质检单头表 Mapper
 *
 * @author tong.li05@hand-china.com 2020-04-28 19:39:00
 */
public interface QmsIqcHeaderMapper extends BaseMapper<QmsIqcHeader> {
    /**
     * 根据INSPECTION_LEVELS与质检单头上的数量查询QmsSampleSizeCodeLetter
     *
     * @param quantity 质检单头数量
     * @return : com.ruike.qms.domain.entity.QmsSampleSizeCodeLetter
     * @author tong.li 2020/4/30 10:57
     */
    List<QmsSampleSizeCodeLetter> querySampleSizeCodeLetter(@Param("quantity") BigDecimal quantity);

    /**
     * @param sampleStandard 1抽样标准
     * @param quantity       2 质检单头数量
     * @param aql            3 AQL值
     * @return : com.ruike.qms.domain.entity.QmsSampleScheme
     * @Description:
     * @author: tong.li
     * @date 2020/4/30 11:27
     * @version 1.0
     */
    QmsSampleScheme querySampleScheme(@Param("sampleStandard") String sampleStandard, @Param("quantity") BigDecimal quantity, @Param("aql") String aql);

}
