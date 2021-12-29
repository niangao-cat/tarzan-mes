package tarzan.instruction.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.instruction.api.dto.MtInstructionDocDTO2;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.vo.MtInstructionDocVO3;
import tarzan.instruction.domain.vo.MtInstructionDocVO4;
import tarzan.instruction.domain.vo.MtInstructionDocVO5;

/**
 * 指令单据头表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:36:42
 */
public interface MtInstructionDocRepository
                extends BaseRepository<MtInstructionDoc>, AopProxy<MtInstructionDocRepository> {

    /**
     * propertyLimitInstructionDocQuery-根据指定属性查询指令单据
     *
     * @param tenantId
     * @param mtLogisticInstructionDoc MtLogisticInstructionDoc
     * @return List
     * @author benjamin
     * @date 2019-06-18 11:29
     *
     */
    List<String> propertyLimitInstructionDocQuery(Long tenantId, MtInstructionDocVO4 mtLogisticInstructionDoc);

    /**
     * instructionDocPropertyGet-根据指令单据id获取指令单据属性
     * 
     * @author benjamin
     * @date 2019-06-18 19:47
     * @param tenantId
     * @param instructionDocId 指令单据Id
     * @return MtLogisticInstructionDoc
     */
    MtInstructionDoc instructionDocPropertyGet(Long tenantId, String instructionDocId);

    /**
     * instructionDocPropertyBatchGet-根据批量单据Id获取单据属性
     * 
     * @author benjamin
     * @date 2019-06-21 10:17
     * @param tenantId
     * @param instructionDocIdList 指令单据Id列表
     * @return List
     */
    List<MtInstructionDoc> instructionDocPropertyBatchGet(Long tenantId, List<String> instructionDocIdList);

    /**
     * instructionDocNextNumGet-获取下一个指令单据编码
     * 
     * @author benjamin
     * @date 2019-06-18 14:46
     * @param tenantId
     * @param siteId 站点Id
     * @return String
     */
    String instructionDocNextNumGet(Long tenantId, String siteId);

    /**
     * instructionDocUpdate-指令单据更新
     * 
     * @author benjamin
     * @date 2019-06-19 09:54
     * @param tenantId
     * @param vo MtInstructionDocDTO2
     * @return instructionDocHisId
     */
    MtInstructionDocVO3 instructionDocUpdate(Long tenantId, MtInstructionDocDTO2 vo, String fullUpdate);

    /**
     * instructionDocRelease-指令单据下达
     * 
     * @author benjamin
     * @date 2019-06-21 13:06
     * @param tenantId
     * @param instructionDocId 指令单据Id
     */
    void instructionDocRelease(Long tenantId, String instructionDocId, String eventRequestId);

    /**
     * instructionDocReleaseVerify-指令单据下达验证
     * 
     * @author benjamin
     * @date 2019-06-21 13:02
     * @param tenantId
     * @param instructionDocId 指令单据Id
     */
    void instructionDocReleaseVerify(Long tenantId, String instructionDocId);

    /**
     * instructionDocCancel-指令单据取消
     * 
     * @author benjamin
     * @date 2019-06-24 10:30
     * @param tenantId
     * @param instructionDocId 指令单据Id
     */
    void instructionDocCancel(Long tenantId, String instructionDocId, String eventRequestId);

    /**
     * instructionDocCancelVerify-指令单据取消验证
     * 
     * @author benjamin
     * @date 2019-06-24 10:25
     * @param tenantId
     * @param instructionDocId 指令单据Id
     */
    void instructionDocCancelVerify(Long tenantId, String instructionDocId);

    /**
     * instructionDocComplete-指令单据完成
     * 
     * @author benjamin
     * @date 2019-06-24 10:52
     * @param tenantId
     * @param instructionDocId 指令单据Id
     */
    void instructionDocComplete(Long tenantId, String instructionDocId, String eventRequestId);

    /**
     * instructionDocCompleteVerify-指令单据完成验证
     * 
     * @author benjamin
     * @date 2019-06-24 10:41
     * @param tenantId
     * @param instructionDocId 指令单据Id
     */
    void instructionDocCompleteVerify(Long tenantId, String instructionDocId);

    /**
     * instructionDocCompletedCancel-指令单据完成取消
     * 
     * @author benjamin
     * @date 2019-06-24 11:02
     * @param tenantId
     * @param instructionDocId 指令单据Id
     */
    void instructionDocCompletedCancel(Long tenantId, String instructionDocId, String eventRequestId);

    /**
     * instructionDocCompletedCancelVerify-指令单据完成取消验证
     * 
     * @author benjamin
     * @date 2019-06-24 10:59
     * @param tenantId
     * @param instructionDocId 指令单据Id
     */
    void instructionDocCompletedCancelVerify(Long tenantId, String instructionDocId);


    MtInstructionDocVO5 instructionDocLimitInstructionAndActualQuery(Long tenantId, String instructionDocId);

}
