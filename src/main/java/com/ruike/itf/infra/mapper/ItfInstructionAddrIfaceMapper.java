package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfInstructionAddrIface;
import com.ruike.itf.domain.vo.ItfInstructionAddrVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 送货单的文件地址表Mapper
 *
 * @author kejin.liu@hand-china.com 2020-10-27 15:24:39
 */
public interface ItfInstructionAddrIfaceMapper extends BaseMapper<ItfInstructionAddrIface> {

    /**
     * 根据送货单好查询送货单号和行号
     * @param tenantId
     * @param docNums
     * @return
     */
    List<ItfInstructionAddrVO> selectDocNumAndSrmLineNumByDocNum(@Param("tenantId") Long tenantId,@Param("docNums") List<String> docNums);
}
