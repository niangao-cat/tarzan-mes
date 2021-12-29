package io.tarzan.common.infra.repository.impl;

import io.choerodon.mybatis.helper.LanguageHelper;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.entity.MtExtendTableDesc;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendTableDescRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.vo.MtExtendColumnRpcVO;
import io.tarzan.common.domain.vo.MtExtendRpcVO;
import io.tarzan.common.domain.vo.MtRoleVO;
import io.tarzan.common.infra.feign.MtRemoteIamService;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import io.tarzan.common.infra.mapper.MtExtendTableDescMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.core.redis.RedisHelper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 扩展说明表 资源库实现
 *
 * @author MrZ 2019-05-21 17:09:05
 */
@Component
public class MtExtendTableDescRepositoryImpl extends BaseRepositoryImpl<MtExtendTableDesc>
		implements MtExtendTableDescRepository {

	@Autowired
	private RedisHelper redisHelper;

	@Autowired
	private ProfileClient profileClient;

	@Autowired
	private MtRemoteIamService remoteIamService;

	@Autowired
	private MtExtendTableDescMapper mtExtendTableDescMapper;

	@Autowired
	private MtExtendSettingsMapper mtExtendSettingsMapper;

	@Autowired
	private MtErrorMessageRepository mtErrorMessageRepository;

	@Override
	public void initDataToRedis() {
		List<MtExtendTableDesc> extTables = mtExtendTableDescMapper.selectAllExtTab();
		if (CollectionUtils.isNotEmpty(extTables)) {
			MtExtendTableDesc.initCache(extTables, redisHelper);
		}
	}

	@Override
	public MtExtendTableDesc attrTableNameLimitGet(Long tenantId, String attrTable) {
		MtExtendTableDesc result = null;
		if (MtExtendTableDesc.existExtendTableDescCache(tenantId, attrTable, LanguageHelper.language(), redisHelper)) {
			result = MtExtendTableDesc.getExtendTableDescCache(tenantId, attrTable, LanguageHelper.language(),
					redisHelper);
		} else {
			MtExtendTableDesc mtExtendTableDesc = new MtExtendTableDesc();
			mtExtendTableDesc.setTenantId(tenantId);
			mtExtendTableDesc.setAttrTable(attrTable);
			result = mtExtendTableDescMapper.selectOne(mtExtendTableDesc);
		}
		return result;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public String extTabBasicPropertyUpdate(Long tenantId, MtExtendTableDesc dto) {
		String extendTableDescId = dto.getExtendTableDescId();
		dto.setTenantId(tenantId);

		if (null == extendTableDescId) {
			if (StringUtils.isEmpty(dto.getAttrTable())) {
				throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
						"MT_GENERAL_0001", "GENERAL", "attrTable", "【API:extTabBasicPropertyUpdate】"));
			}
			if (StringUtils.isEmpty(dto.getAttrTableDesc())) {
				throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
						"MT_GENERAL_0001", "GENERAL", "attrTableDesc", "【API:extTabBasicPropertyUpdate】"));
			}
			if (StringUtils.isEmpty(dto.getMainTable())) {
				throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
						"MT_GENERAL_0001", "GENERAL", "mainTable", "【API:extTabBasicPropertyUpdate】"));
			}
			if (StringUtils.isEmpty(dto.getMainTableKey())) {
				throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
						"MT_GENERAL_0001", "GENERAL", "mainTableKey", "【API:extTabBasicPropertyUpdate】"));
			}

			MtExtendTableDesc mtExtendTableDesc = new MtExtendTableDesc();
			mtExtendTableDesc.setTenantId(tenantId);
			mtExtendTableDesc.setAttrTable(dto.getAttrTable());
			mtExtendTableDesc = this.mtExtendTableDescMapper.selectOne(mtExtendTableDesc);
			if (null != mtExtendTableDesc) {
				throw new MtException("MT_GENERAL_0039", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
						"MT_GENERAL_0039", "GENERAL", "【API:extTabBasicPropertyUpdate】"));
			}

			self().insertSelective(dto);
			extendTableDescId = dto.getExtendTableDescId();
			mtExtendTableDesc = new MtExtendTableDesc();
			mtExtendTableDesc.setTenantId(tenantId);
			mtExtendTableDesc.setExtendTableDescId(extendTableDescId);
			mtExtendTableDesc = this.mtExtendTableDescMapper.selectOne(mtExtendTableDesc);

			if (dto.get_tls() == null || dto.get_tls().get("attrTableDesc") == null) {
				for (Language language : LanguageHelper.languages()) {
					mtExtendTableDesc.setLang(language.getCode());
					MtExtendTableDesc.refreshCache(tenantId, language.getCode(), mtExtendTableDesc, redisHelper);
				}
			} else {
				Map<String, String> map = dto.get_tls().get("attrTableDesc");
				for (Map.Entry<String, String> entry : map.entrySet()) {
					mtExtendTableDesc.setLang(entry.getKey());
					mtExtendTableDesc.setAttrTableDesc(entry.getValue());
					MtExtendTableDesc.refreshCache(tenantId, entry.getKey(), mtExtendTableDesc, redisHelper);
				}
			}
		} else {
			if (null != dto.getAttrTable() && "".equals(dto.getAttrTable())) {
				throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
						"MT_GENERAL_0001", "GENERAL", "attrTable", "【API:extTabBasicPropertyUpdate】"));
			}
			if (null != dto.getAttrTableDesc() && "".equals(dto.getAttrTableDesc())) {
				throw new MtException("MT_GENERAL_0001",
						mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0001",
								"GENERAL", "GENERAL", "attrTableDesc",
								"【API:extTabBasicPropertyUpdate】"));
			}
			if (null != dto.getMainTable() && "".equals(dto.getMainTable())) {
				throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
						"MT_GENERAL_0001", "GENERAL", "mainTable", "【API:extTabBasicPropertyUpdate】"));
			}
			if (null != dto.getMainTableKey() && "".equals(dto.getMainTableKey())) {
				throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
						"MT_GENERAL_0001", "GENERAL", "mainTableKey", "【API:extTabBasicPropertyUpdate】"));
			}


			MtExtendTableDesc oldExtendTableDesc = new MtExtendTableDesc();
			oldExtendTableDesc.setTenantId(tenantId);
			oldExtendTableDesc.setExtendTableDescId(extendTableDescId);
			oldExtendTableDesc = this.mtExtendTableDescMapper.selectOne(oldExtendTableDesc);
			if (null == oldExtendTableDesc) {
				throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
						"MT_GENERAL_0037", "GENERAL", "extendTableDescId", "【API:extTabBasicPropertyUpdate】"));
			}

			MtExtendTableDesc mtExtendTableDesc = new MtExtendTableDesc();
			mtExtendTableDesc.setTenantId(tenantId);
			mtExtendTableDesc.setAttrTable(dto.getAttrTable());
			mtExtendTableDesc = this.mtExtendTableDescMapper.selectOne(mtExtendTableDesc);
			if (null != mtExtendTableDesc && !mtExtendTableDesc.getExtendTableDescId().equals(extendTableDescId)) {
				throw new MtException("MT_GENERAL_0039", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
						"MT_GENERAL_0039", "GENERAL", "【API:extTabBasicPropertyUpdate】"));
			}


			// 获取initialFlag为Y的原始数据(当修改时需判断)
			if (MtBaseConstants.YES.equals(dto.getInitialFlag())) {
				// 校验当前角色是否可以修改initialFlag字段
				String allowUpdateRole =
						profileClient.getProfileValueByOptions(tenantId, MtUserClient.getCurrentUserId(),
								MtUserClient.getCurrentRoleId(), "MT.GEN_INITIALIZE_UPDATE_ROLE");


				ResponseEntity<List<MtRoleVO>> listMemberRoles = remoteIamService.selfRoles(tenantId);
				List<MtRoleVO> roleList =
						listMemberRoles == null ? new ArrayList<MtRoleVO>() : listMemberRoles.getBody();

				List<String> roles = roleList == null ? new ArrayList<String>()
						: roleList.stream().map(MtRoleVO::getCode).collect(Collectors.toList());

				if (StringUtils.isEmpty(allowUpdateRole) || !roles.contains(allowUpdateRole)) {
					throw new MtException("MT_GENERAL_0007",
							mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0007",
									"GENERAL", allowUpdateRole, "【API:getGenTypeBasicPropertyUpdate】"));
				}
			}

			self().updateByPrimaryKeySelective(dto);

			mtExtendTableDesc = new MtExtendTableDesc();
			mtExtendTableDesc.setTenantId(tenantId);
			mtExtendTableDesc.setExtendTableDescId(extendTableDescId);
			mtExtendTableDesc = this.mtExtendTableDescMapper.selectOne(mtExtendTableDesc);

			if (dto.get_tls() == null || dto.get_tls().get("attrTableDesc") == null) {
				mtExtendTableDesc.setLang(LanguageHelper.language());
				MtExtendTableDesc.refreshCache(tenantId, LanguageHelper.language(), mtExtendTableDesc, redisHelper);
			} else {
				Map<String, String> map = dto.get_tls().get("attrTableDesc");
				for (Map.Entry<String, String> entry : map.entrySet()) {
					mtExtendTableDesc.setLang(entry.getKey());
					mtExtendTableDesc.setAttrTableDesc(entry.getValue());
					MtExtendTableDesc.refreshCache(tenantId, entry.getKey(), mtExtendTableDesc, redisHelper);
				}
			}
		}
		return extendTableDescId;
	}

	@Override
	public MtExtendRpcVO tableLimitAttrNameQuery(Long tenantId, String tableName) {
		String apiName = "【API: tableLimitAttrNameQuery】";
		if (StringUtils.isEmpty(tableName)) {
			throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
					"MT_GENERAL_0001", "GENERAL", "tableName", apiName));
		}

		MtExtendTableDesc table = new MtExtendTableDesc();
		table.setTenantId(tenantId);
		table.setAttrTable(tableName);
		table = mtExtendTableDescMapper.selectOne(table);
		if (null == table) {
			throw new MtException("MT_GENERAL_0065",
					mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0065", "GENERAL", tableName, apiName));
		}

		MtExtendRpcVO tableVO = new MtExtendRpcVO();
		tableVO.setExtendTableDescId(table.getExtendTableDescId());
		tableVO.setAttrTable(table.getAttrTable());
		tableVO.setAttrTableDesc(table.getAttrTableDesc());
		tableVO.setEnableFlag(table.getEnableFlag());
		tableVO.setHisTable(table.getHisTable());
		tableVO.setHisAttrTable(table.getHisAttrTable());
		tableVO.setHisTableKey(table.getHisTableKey());
		tableVO.setInitialFlag(table.getInitialFlag());
		tableVO.setMainTable(table.getMainTable());
		tableVO.setMainTableKey(table.getMainTableKey());

		MtExtendSettings column = new MtExtendSettings();
		column.setTenantId(tenantId);
		column.setExtendTableDescId(table.getExtendTableDescId());
		List<MtExtendSettings> columnList = mtExtendSettingsMapper.select(column);
		if (CollectionUtils.isNotEmpty(columnList)) {
			List<MtExtendColumnRpcVO> columnRpcVOList = new ArrayList<>(columnList.size());
			MtExtendColumnRpcVO columnVO;
			for (MtExtendSettings col : columnList) {
				columnVO = new MtExtendColumnRpcVO();
				columnVO.setAttrName(col.getAttrName());
				columnVO.setAttrMeaning(col.getAttrMeaning());
				columnVO.setEnableFlag(col.getEnableFlag());
				columnVO.setExtendId(col.getExtendId());
				columnVO.setExtendTableDescId(col.getExtendTableDescId());
				columnVO.setTlFlag(col.getTlFlag());
				columnVO.setSequence(col.getSequence());
				columnRpcVOList.add(columnVO);
			}
			tableVO.setExtendColumnList(columnRpcVOList);
		} else {
			tableVO.setExtendColumnList(Lists.newArrayList());
		}

		return tableVO;
	}
}