package com.ruike.wms.app.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.ruike.wms.domain.vo.WmsMaterialLotAttrViewVO;
import com.ruike.wms.domain.vo.WmsMaterialLotPntVO;
import org.hzero.boot.platform.lov.dto.LovValueDTO;


import com.ruike.wms.api.dto.WmsDeliverySiteDTO;

/**
 * @Classname CommonApiService
 * @Description TODO
 * @Date 2019/11/21 10:56 上午
 * @Created by selino
 * @Author selino
 */
public interface WmsCommonApiService {
	/**
	 * @return java.util.List<org.hzero.boot.platform.lov.dto.LovValueDTO>
	 * @Description 查询值集lOV
	 * @Param [tenantId, lovCode]
	 * @date 2019/11/25 1:22 下午
	 * @auther selino
	 */
	List<LovValueDTO> queryLovValueList(Long tenantId, String lovCode,String value);

	/**
	 * 获取配置维护的参数值
	 *
	 * @param tenantId
	 * @param profileName
	 * @return java.util.List<org.hzero.boot.platform.lov.dto.LovValueDTO>
	 * @Description 获取配置维护的参数值
	 * @Date 2019/12/2 19:17
	 * @Created by {HuangYuBin}
	 */
	String getProfileName(Long tenantId, String profileName);

	/**
	 * 调用斑马ZR328打印机公共方法
	 *
	 * @param tenantId
	 * @param materialLotList
	 * @throws IOException
	 */
	String commonPrint(Long tenantId, List<WmsMaterialLotPntVO> materialLotList) throws IOException;

	/**
	 * 条码（物料批）扩展属性行转列方法
	 *
	 * @param tenantId
	 * @param mateiralLotIds
	 * @author chuang.yang
	 * @date 2020/4/22
	 */
	List<WmsMaterialLotAttrViewVO> queryMaterialLotAttrViewData(Long tenantId, List<String> mateiralLotIds);

	/**
	 * 条码（物料批）扩展属性行转列方法
	 *
	 * @param tenantId
	 * @param mateiralLotIds
	 * @author junhui.liu
	 * @date 2020/4/22
	 */
	Map<String, WmsMaterialLotAttrViewVO> queryMaterialLotAttrViewDataMap(Long tenantId, List<String> mateiralLotIds);


}
