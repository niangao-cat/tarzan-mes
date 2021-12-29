package com.ruike.wms.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruike.wms.api.dto.WmsTransactionTypeDTO;
import com.ruike.wms.domain.entity.WmsTransactionType;

import io.choerodon.mybatis.common.BaseMapper;

/**
 * @Classname TransactionTypeMapper
 * @Description 事务类型维护
 * @Date 2019/10/18 10:00
 * @Author by {HuangYuBin}
 */
public interface WmsTransactionTypeMapper extends BaseMapper<WmsTransactionType> {

	/**
	 * 事务类型查询
	 *
	 * @param tenantId
	 * @param transactionTypeCode
	 * @param description
	 * @return
	 */
	List<WmsTransactionTypeDTO> queryList(@Param("tenantId") Long tenantId,
										  @Param("transactionTypeCode") String transactionTypeCode,
										  @Param("description") String description);
}
