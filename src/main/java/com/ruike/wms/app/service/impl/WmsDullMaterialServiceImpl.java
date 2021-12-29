package com.ruike.wms.app.service.impl;

import com.ruike.wms.api.dto.WmsDullMaterialImportQueryRequestDTO;
import com.ruike.wms.api.dto.WmsDullMaterialImportQueryResponseDTO;
import com.ruike.wms.api.dto.WmsDullMaterialQueryRequestDTO;
import com.ruike.wms.api.dto.WmsDullMaterialQueryResponseDTO;
import com.ruike.wms.app.service.WmsDullMaterialService;
import com.ruike.wms.domain.entity.WmsDullMaterial;
import com.ruike.wms.domain.repository.WmsDullMaterialRepository;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsDullMaterialMapper;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;

import java.util.ArrayList;
import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * @Classname DullMaterialServiceImpl
 * @Description 呆滞物料报表
 * @Date 2019/10/29 17:55
 * @Author by {HuangYuBin}
 */
@Service
@Slf4j
public class WmsDullMaterialServiceImpl implements WmsDullMaterialService {
	@Autowired
	WmsDullMaterialMapper mapper;
	@Autowired
	WmsCommonServiceComponent commonServiceComponent;
	@Autowired
    MtMaterialLotRepository mtMaterialLotRepository;
	@Autowired
	WmsDullMaterialRepository repository;
	@Autowired
    CustomSequence customSequence;
	@Autowired
    MtExtendSettingsRepository mtExtendSettingsRepository;

	@Transactional(rollbackFor = Exception.class)
	@ProcessLovValue
	@Override
	public Page<WmsDullMaterialQueryResponseDTO> queryList(Long tenantId, PageRequest pageRequest, WmsDullMaterialQueryRequestDTO dto) {
		Page<WmsDullMaterialQueryResponseDTO> response = new Page<>();
		//超期呆滞，直接查询
		if (OVERDUE.equals(dto.getDullType())) {
			response = PageHelper.doPageAndSort(pageRequest, () -> mapper.queryOverDue(tenantId, dto));
		}
		//变更呆滞,先查询超期呆滞,排除超期呆滞
		if (CHANGE.equals(dto.getDullType())) {
			List<WmsDullMaterialQueryResponseDTO> dullMaterialOverDueResponse = mapper.queryOverDue(tenantId, dto);
			response = PageHelper.doPageAndSort(pageRequest, () -> mapper.queryChange(tenantId, dto, dullMaterialOverDueResponse));
		}
		//无异动呆滞，排除超期呆滞和变更呆滞
		if (NOMOVE.equals(dto.getDullType())) {
			List<WmsDullMaterialQueryResponseDTO> dullMaterialOverDueResponse = mapper.queryOverDue(tenantId, dto);
			List<WmsDullMaterialQueryResponseDTO> dullMaterialChangeResponse = mapper.queryChange(tenantId, dto, dullMaterialOverDueResponse);
			dullMaterialOverDueResponse.addAll(dullMaterialChangeResponse);
			response = PageHelper.doPageAndSort(pageRequest, () -> mapper.queryNoMove(tenantId, dto, dullMaterialOverDueResponse, dullMaterialChangeResponse));
		}
		//查询所有
		if (StringUtils.isEmpty(dto.getDullType())) {
			mapper.saveOverDueTemp(tenantId, dto);
			mapper.saveChangeTemp(tenantId, dto);
			mapper.saveQueryNoMoveTemp(tenantId, dto);
			response = PageHelper.doPageAndSort(pageRequest, () -> mapper.tempQuery(tenantId, dto));
		}
		mapper.clearTemp();
		return response;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@ProcessLovValue
	public List<WmsDullMaterialQueryResponseDTO> exportList(Long tenantId, WmsDullMaterialQueryRequestDTO dto) {
		List<WmsDullMaterialQueryResponseDTO> response = new Page<>();
		//超期呆滞，直接查询
		if (OVERDUE.equals(dto.getDullType())) {
			response = mapper.queryOverDue(tenantId, dto);
		}
		//变更呆滞,先查询超期呆滞,排除超期呆滞
		if (CHANGE.equals(dto.getDullType())) {
			List<WmsDullMaterialQueryResponseDTO> dullMaterialOverDueResponse = mapper.queryOverDue(tenantId, dto);
			response = mapper.queryChange(tenantId, dto, dullMaterialOverDueResponse);
		}
		//无异动呆滞，排除超期呆滞和变更呆滞
		if (NOMOVE.equals(dto.getDullType())) {
			List<WmsDullMaterialQueryResponseDTO> dullMaterialOverDueResponse = mapper.queryOverDue(tenantId, dto);
			List<WmsDullMaterialQueryResponseDTO> dullMaterialChangeResponse = mapper.queryChange(tenantId, dto, dullMaterialOverDueResponse);
			dullMaterialOverDueResponse.addAll(dullMaterialChangeResponse);
			response = mapper.queryNoMove(tenantId, dto, dullMaterialOverDueResponse, dullMaterialChangeResponse);
		}
		//查询所有
		if (StringUtils.isEmpty(dto.getDullType())) {
			mapper.saveOverDueTemp(tenantId, dto);
			mapper.saveChangeTemp(tenantId, dto);
			mapper.saveQueryNoMoveTemp(tenantId, dto);
			response = mapper.tempQuery(tenantId, dto);
		}
		mapper.clearTemp();
		return response;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public List<String> dellMaterialJob(Long tenantId) {
		// 筛选超期检验时间-当前时间>30天的条码
		// 若超期检验时间为空则筛选生产日期-当前时间>保质期的条码
		// 若生产日期为空则筛选条码创建时间-当前时间>保质期的条码
		// 更新条码质量状态
		// 查询到符合条件的条码ID
		MtMaterialLotVO2 mtMaterialLot2 = new MtMaterialLotVO2();

		List<String> materialLotIdList = mapper.getMaterialLotIdByJob();
		String eventId = commonServiceComponent.generateEvent(tenantId, "OVERDUE");
		//对查询到的id列表循环修改质量状态
		for (String id : materialLotIdList) {
            mtMaterialLot2.setMaterialLotId(id);
            mtMaterialLot2.setEventId(eventId);
            mtMaterialLot2.setQualityStatus("PENDING");
            mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLot2, WmsConstant.CONSTANT_N);
            commonServiceComponent.attrUpdate(tenantId, "mt_material_lot_attr", id, eventId);
        }
		return materialLotIdList;
	}

	@ProcessLovValue
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Page<WmsDullMaterialImportQueryResponseDTO> importQuery(Long tenantId, PageRequest pageRequest, WmsDullMaterialImportQueryRequestDTO dto) {
		WmsDullMaterialQueryRequestDTO dullMaterialQueryRequest = new WmsDullMaterialQueryRequestDTO();
		dullMaterialQueryRequest.setSiteId(dto.getSiteId());
		mapper.saveOverDueTemp(tenantId, dullMaterialQueryRequest);
		mapper.saveChangeTemp(tenantId, dullMaterialQueryRequest);
		mapper.saveQueryNoMoveTemp(tenantId, dullMaterialQueryRequest);
		Page<WmsDullMaterialImportQueryResponseDTO> response = new Page<>();
		response = PageHelper.doPageAndSort(pageRequest, () -> mapper.tempImportQuery(tenantId, dto));
		mapper.clearTemp();
		return response;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void importSave(Long tenantId, List<WmsDullMaterialImportQueryResponseDTO> dtoList) {
		List<WmsDullMaterial> dullMaterialList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(dtoList) && dtoList.size() > 0) {
			for (WmsDullMaterialImportQueryResponseDTO dto : dtoList) {
				WmsDullMaterial dullMaterial = new WmsDullMaterial();
				dullMaterial.setMaterialLotCode(dto.getMaterialLotCode());
				dullMaterial.setSiteId(dto.getSiteId());
				dullMaterial.setDullMaterialId(customSequence.getNextKey("wms_dull_material_s"));
				dullMaterial.setCid(Long.valueOf(customSequence.getNextKey("wms_dull_material_cid_s")));
				dullMaterialList.add(dullMaterial);
			}
			repository.batchInsertSelective(dullMaterialList);
		}
	}
}