package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.entity.HmeExcWkcRecord;
import com.ruike.hme.domain.vo.HmeAreaWorkshopProdLineVO;
import com.ruike.hme.domain.vo.HmeExcRecordCreateVO;
import com.ruike.hme.domain.vo.HmeExceptionHandlePlatFormVO;
import com.ruike.hme.domain.vo.HmeExceptionRecordQueryVO;

import java.util.List;
import java.util.Map;

/**
 * 异常处理平台
 *
 * @author Deng xu 2020/6/22 14:27
 */
public interface HmeExceptionHandlePlatformRepository {

    /**
     * 异常处理平台-主界面查询
     *
     * @param tenantId     租户ID
     * @param workcellCode 工位ID
     * @param siteId       用户默认站点ID
     * @return : java.util.List<com.ruike.hme.domain.vo.HmeExceptionHandlePlatFormVO>
     */
    List<HmeExceptionHandlePlatFormVO> listExceptionForUi(Long tenantId, String workcellCode, String siteId);

    /**
     * 异常处理平台-扫描设备编码后校验并返回设备ID
     *
     * @param tenantId 租户ID
     * @param createVO 创建VO（包含设备编码）
     * @return : com.ruike.hme.domain.vo.HmeExcRecordCreateVO
     */
    HmeExcRecordCreateVO equipmentVerification(Long tenantId, HmeExcRecordCreateVO createVO);

    /**
     * 异常处理平台-扫描物料条码后校验是否存在并返回物料信息
     *
     * @param tenantId 租户ID
     * @param createVO 创建VO（包含物料条码）
     * @return : com.ruike.hme.domain.vo.HmeExcRecordCreateVO
     */
    HmeExcRecordCreateVO materialLotVerification(Long tenantId, HmeExcRecordCreateVO createVO);

    /**
     * 异常处理平台-创建异常记录
     *
     * @param tenantId 租户ID
     * @param createVO 创建VO
     * @return : com.ruike.hme.domain.entity.HmeExcWkcRecord
     */
    HmeExcWkcRecord createExceptionRecord(Long tenantId, HmeExcRecordCreateVO createVO);

    /**
     * 异常处理平台-异常历史查看
     *
     * @param tenantId             租户ID
     * @param exceptionWkcRecordId 异常记录ID
     * @return : com.ruike.hme.domain.vo.HmeExceptionQueryVO
     */
    HmeExceptionRecordQueryVO queryHistoryForUi(Long tenantId, String exceptionWkcRecordId);

    /**
     * 异常处理平台-异常关闭
     *
     * @param tenantId      租户id
     * @param createVO      创建VO
     * @author sanfeng.zhang@hand-china.com 2020/8/3 11:51
     * @return com.ruike.hme.domain.entity.HmeExcWkcRecord
     */
    HmeExcWkcRecord closeExceptionRecord(Long tenantId, HmeExcRecordCreateVO createVO);

    /**
     * <strong>Title : areaCJProdLineByUserId</strong><br/>
     * <strong>Description : 异常处理平台-根据用户查询有效的区域、车间、产线 </strong><br/>
     * <strong>Create on : 2021/2/25 2:18 下午</strong><br/>
     *
     * @param tenantId
     * @param userId
     * @return java.lang.Object
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    HmeAreaWorkshopProdLineVO areaCJProdLineByUserId(Long tenantId, Long userId);

    /**
     * <strong>Title : listExceptionNotLoginForUi</strong><br/>
     * <strong>Description : 异常处理平台-主界面查询-不登陆工位查询 </strong><br/>
     * <strong>Create on : 2021/2/25 4:42 下午</strong><br/>
     *
     * @param tenantId
     * @param siteId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeExceptionHandlePlatFormVO>
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    List<HmeExceptionHandlePlatFormVO> listExceptionNotLoginForUi(Long tenantId, String siteId);

    /**
     * <strong>Title : listExceptionNotLoginHistoryForUi</strong><br/>
     * <strong>Description : 异常处理平台-异常清单查询历史-不登陆工位查询-限制当前用户 </strong><br/>
     * <strong>Create on : 2021/3/4 4:26 下午</strong><br/>
     *
     * @param tenantId
     * @param exceptionWkcRecordId
     * @return com.ruike.hme.domain.vo.HmeExceptionRecordQueryVO
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    HmeExceptionRecordQueryVO listExceptionNotLoginHistoryForUi(Long tenantId, String exceptionWkcRecordId);
}
