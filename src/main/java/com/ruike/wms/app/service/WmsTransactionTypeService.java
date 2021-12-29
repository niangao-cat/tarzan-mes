package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsTransactionTypeDTO;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * @Classname TransactionTypeService
 * @Description 事务类型维护
 * @Date 2019/10/18 10:00
 * @Author by {HuangYuBin}
 */
public interface WmsTransactionTypeService {
	String SQLSTATE = "23000";

	/**
	 * 批量保存事务类型
	 *
	 * @param tenantId
	 * @param dtoList
	 * @return void
	 * @Description 保存事务类型
	 * @Date 2019/10/18 10:14
	 * @Created by {HuangYuBin}
	 */
	void batchSave(Long tenantId, List<WmsTransactionTypeDTO> dtoList);

	/**
	 * 批量保存事务类型
	 *
	 * @param tenantId 租户
	 * @param dto      事务数据
	 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/30 09:28:05
	 */
	void insertOrUpdate(Long tenantId, WmsTransactionTypeDTO dto);

	/**
	 * 事务类型 查询
	 *
	 * @param tenantId
	 * @param pageRequest
	 * @param transactionTypeCode
	 * @param description
	 * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.entity.TransactionType>
	 * @Description 事务类型 查询
	 * @Date 2019/10/18 11:02
	 * @Created by {HuangYuBin}
	 */
	Page<WmsTransactionTypeDTO> queryList(Long tenantId, PageRequest pageRequest, String transactionTypeCode, String description);
}
