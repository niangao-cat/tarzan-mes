package com.ruike.itf.infra.mapper;

import com.ruike.itf.api.dto.OperationCollectItfDTO;
import com.ruike.itf.api.dto.OperationCollectItfDTO1;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName ItfOperationCollectIfaceMapper
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/9/17 21:52
 * @Version 1.0
 **/
public interface ItfOperationCollectIfaceMapper  {

    List<OperationCollectItfDTO> queryResult(@Param("tenantId") Long tenantId, @Param("dto") OperationCollectItfDTO1 collect);


    List<OperationCollectItfDTO> queryResultLike(@Param("tenantId")Long tenantId, @Param("dto")OperationCollectItfDTO1 operationCollectItfDTO1);
}
