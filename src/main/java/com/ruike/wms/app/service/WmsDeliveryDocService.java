package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.domain.vo.WmsDeliveryDocVO;
import com.ruike.wms.domain.vo.WmsMaterialLotLineVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.core.base.AopProxy;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Classname DeliveryDocService
 * @Description 送货单查询Service
 * @Date 2019/9/19 19:44
 * @Author by {HuangYuBin}
 */
public interface WmsDeliveryDocService extends AopProxy<WmsDeliveryDocService> {

	/**
	 * 查询指令单据列表
	 *
	 * @param tenantId
	 * @param dto
	 * @param pageRequest
	 * @return io.choerodon.core.domain.Page<com.ruike.wms.api.controller.dto.InstructionDocResponseDTO>
	 * @Description 查询指令单据列表
	 * @Date 2019/9/24 11:40
	 * @Created by {HuangYuBin}
	 */
	Page<WmsInstructionDocResponseDTO> instructionDocQuery(Long tenantId, WmsInstructionDocRequestDTO dto, PageRequest pageRequest);

	/**
	 * 查询指令列表
	 *
	 * @param tenantId
	 * @param instructionDocId
	 * @param pageRequest
	 * @return io.choerodon.core.domain.Page<com.ruike.wms.api.controller.dto.InstructionDTO>
	 * @Description TODO
	 * @Date 2019/9/24 20:21
	 * @Created by {HuangYuBin}
	 */
	Page<WmsInstructionDTO> instructionQuery(Long tenantId, String instructionDocId, PageRequest pageRequest);

	/**
	 * 送货单行（指令）明细查询
	 *
	 * @param tenantId
	 * @param dto
	 * @param pageRequest
	 * @return io.choerodon.core.domain.Page<com.ruike.wms.api.controller.dto.InstructionDetailResponseDTO>
	 * @Description 送货单行（指令）明细查询
	 * @Date 2019/9/21 10:46
	 * @Created by {HuangYuBin}
	 */
	Page<WmsInstructionDetailResponseDTO> instructionDetailQuery(Long tenantId, WmsInstructionDetailRequestDTO dto, PageRequest pageRequest);

	/**
     * 获得可制单数量
     *
     * @param tenantId 租户id
     * @param dto      参数
     * @return java.util.List<com.ruike.wms.api.dto.WmsAvailQuantityReturnDTO>
     * @author han.zhang 2020-04-28 21:43
     */
    List<WmsAvailQuantityReturnDTO> getAvailQuantity(Long tenantId, WmsAvailQuantityGetDTO dto);


	/**
	 * 送货单打印
	 *
	 * @param tenantId
	 * @param instructionDocIds
	 * @return
	 * @Description 送货单打印
	 * @Date 2020-9-7 14:30:36
	 * @Created by yifan.xiong
	 */
	public void multiplePrint(Long tenantId, List<String> instructionDocIds, HttpServletResponse response);

	/**
	 * 根据行Id查询明细数据
	 *
	 * @param tenantId 租户ID
	 * @param instructionId 行ID
	 * @param pageRequest 分页信息
	 * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/10 14:03:07
	 * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsDeliveryDocVO>
	 */
	Page<WmsDeliveryDocVO> detailQuery(Long tenantId, String instructionId, PageRequest pageRequest);

	/**
	 * 查询送货单物料批信息
	 *
	 * @param tenantId				租户id
	 * @param instructionDocNum		送货单号
	 * @param pageRequest			分页参数
	 * @author sanfeng.zhang@hand-china.com 2020/10/12 15:53 
	 * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsMaterialLotLineVO>
	 */
	Page<WmsMaterialLotLineVO> instructionMaterialLotQuery(Long tenantId, String instructionDocNum, PageRequest pageRequest);
}