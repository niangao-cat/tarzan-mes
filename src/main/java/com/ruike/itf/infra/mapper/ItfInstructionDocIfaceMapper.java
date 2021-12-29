package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfInstructionDocIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 指令单据头表Mapper
 *
 * @author kejin.liu01@hand-china.com 2020-08-11 11:24:54
 */
public interface ItfInstructionDocIfaceMapper extends BaseMapper<ItfInstructionDocIface> {

    String selectMySql(@Param("sql") String sql);

}
