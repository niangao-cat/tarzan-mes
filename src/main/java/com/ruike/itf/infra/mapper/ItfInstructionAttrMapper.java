package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfInstructionAttr;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 指令拓展表Mapper
 *
 * @author kejin.liu01@hand-china.com 2020-08-12 12:36:21
 */
public interface ItfInstructionAttrMapper extends BaseMapper<ItfInstructionAttr> {

    void deleteByDocId(@Param("instructionDocId") String instructionDocId);


    List<String> selectByDocId(@Param("instructionId")String instructionDocId);


    void deleteByIds(@Param("attrIds")List<String> attrId);
}
