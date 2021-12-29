package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.actual.domain.entity.MtDataRecord;
import tarzan.general.domain.vo.*;

/**
 * 数据收集实绩资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:01:00
 */
public interface MtDataRecordRepository extends BaseRepository<MtDataRecord>, AopProxy<MtDataRecordRepository> {
    /**
     * dataRecordGet-获取数据收集实绩属性
     *
     * @author benjamin
     * @date 2019-07-02 11:01
     * @param tenantId IRequest
     * @param dataRecordId 数据收集实绩Id
     * @return MtDataRecord
     */
    MtDataRecord dataRecordGet(Long tenantId, String dataRecordId);

    /**
     * dataRecordBatchGet-批量获取数据收集实绩属性
     *
     * @author benjamin
     * @date 2019-07-02 11:05
     * @param tenantId IRequest
     * @param dataRecordIdList 数据收集实绩Id集合
     * @return List
     */
    List<MtDataRecord> dataRecordBatchGet(Long tenantId, List<String> dataRecordIdList);

    /**
     * propertyLimitDataRecordQuery-根据属性获取数据收集实绩
     *
     * @author benjamin
     * @date 2019-07-02 15:36
     * @param tenantId IRequest
     * @param dto MtDataRecord
     * @return List
     */
    List<MtDataRecord> propertyLimitDataRecordQuery(Long tenantId, MtDataRecordVO5 dto);

    /**
     * dataRecordAndHisCreate-数据收集实绩创建
     *
     * @author benjamin
     * @date 2019-07-02 11:30
     * @param tenantId IRequest
     * @param createVO 创建VO
     * @return String
     */
    MtDataRecordVO6 dataRecordAndHisCreate(Long tenantId, MtDataRecordVO createVO);

    /**
     * dataRecordAndHisUpdate-数据收集实绩更新
     *
     * @author benjamin
     * @date 2019-07-02 15:16
     * @param tenantId IRequest
     * @param updateVO 更新VO
     */
    MtDataRecordVO6 dataRecordAndHisUpdate(Long tenantId, MtDataRecordVO2 updateVO, String fullUpdate);

    /**
     * allTypeDataRecordCreate-根据收集组类型创建数据收集实绩
     *
     * @author benjamin
     * @date 2019-07-02 16:34
     * @param tenantId IRequest
     * @param createVO 创建VO
     * @return MtDataRecordVO
     */
    MtDataRecordVO6 allTypeDataRecordCreate(Long tenantId, MtDataRecordVO createVO);

    /**
     * distributionGroupLimitTagGroupQuery-根据收集组及收集参数获取应分发组清单
     *
     * @author benjamin
     * @date 2019-07-02 20:24
     * @param tenantId IRequest
     * @param queryVO 查询VO
     * @return MtDataRecordVO4
     */
    MtDataRecordVO4 distributionGroupLimitTagGroupQuery(Long tenantId, MtDataRecordVO3 queryVO);

    /**
     * 数据采集实绩新增&更新扩展表属性
     *
     * @Author peng.yuan
     * @Date 2019/11/20 14:08
     * @param tenantId :
     * @param mtExtendVO10 :
     * @return void
     */
    void dataRecordAttrPropertyUpdate(Long tenantId, MtExtendVO10 mtExtendVO10);

    /**
     * 校验EO步骤实绩数据采集完整性
     *
     * @Author peng.yuan
     * @Date 2020/2/14 10:54
     * @param tenantId :
     * @param vo :
     * @return
     */
    void eoStepActualDataRecordCompleteValidate(Long tenantId, MtDataRecordVO7 vo);
}
