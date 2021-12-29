package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtNcRecord;
import tarzan.actual.domain.vo.MtNcRecordVO1;
import tarzan.actual.domain.vo.MtNcRecordVO2;
import tarzan.actual.domain.vo.MtNcRecordVO3;
import tarzan.actual.domain.vo.MtNcRecordVO4;
import tarzan.actual.domain.vo.MtNcRecordVO5;
import tarzan.actual.domain.vo.MtNcRecordVO6;
import tarzan.actual.domain.vo.MtNcRecordVO7;
import tarzan.actual.domain.vo.MtNcRecordVO8;

/**
 * 不良代码记录资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:59:55
 */
public interface MtNcRecordRepository extends BaseRepository<MtNcRecord>, AopProxy<MtNcRecordRepository> {

    /**
     * ncEventCreate-不良事件创建
     *
     * @param tenantId
     * @param dto
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/4/2
     */
    String ncEventCreate(Long tenantId, MtNcRecordVO6 dto);

    /**
     * ncRecordPropertyGet-获取不良记录基本属性
     *
     * @param tenantId
     * @param ncRecordId
     * @return hmes.nc_record.dto.MtNcRecord
     * @author chuang.yang
     * @date 2019/4/2
     */
    MtNcRecord ncRecordPropertyGet(Long tenantId, String ncRecordId);

    /**
     * ncRecordSourceEoStepActualGet-根据不良记录获取来源步骤实绩
     *
     * @param tenantId
     * @param ncRecordId
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/4/2
     */
    String ncRecordSourceEoStepActualGet(Long tenantId, String ncRecordId);

    /**
     * ncDispositionGroupGet-获取不良记录处置组
     *
     * @param tenantId
     * @param ncRecordId
     * @return
     * @author sen.luo
     * @date 2019/4/1
     */
    MtNcRecordVO5 ncDispositionGroupGet(Long tenantId, String ncRecordId);

    /**
     * eoNcRecordAllClosedValidate-根据执行作业验证是否存在未关闭不良记
     *
     * @param tenantId
     * @param eoId
     * @author guichuan.li
     * @date 2019/4/2
     */
    String eoNcRecordAllClosedValidate(Long tenantId, String eoId);

    /**
     * eoStepNcRecordAllClosedValidate-根据执行作业步骤验证是否存在
     *
     * @param tenantId
     * @param eoStepActualId
     * @author guichuan.li
     * @date 2019/4/2
     */
    String eoStepNcRecordAllClosedValidate(Long tenantId, String eoStepActualId);

    /**
     * eoIncidentAndCodeLimitNcRecordGet-根据执行作业事故与不良代码获取不良记录
     *
     * @Author lxs
     * @Date 2019/4/2
     * @Return hmes.nc_record.dto.MtNcRecord
     */
    MtNcRecord eoIncidentAndCodeLimitNcRecordGet(Long tenantId, MtNcRecord dto);

    /**
     * eoLimitNcRecordQuery -根据执行作业查询不良记录清单
     *
     * @Author lxs
     * @Date 2019/4/2
     * @Return java.util.List<hmes.nc_record.dto.MtNcRecord>
     */
    List<MtNcRecord> eoLimitNcRecordQuery(Long tenantId, String eoId);

    /**
     * incidentLimitNcRecordQuery-根据事故查询不良记录清单
     *
     * @param tenantId
     * @param ncIncidentId
     * @author guichuan.li
     * @date 2019/4/2
     */
    List<MtNcRecord> incidentLimitNcRecordQuery(Long tenantId, String ncIncidentId);

    /**
     * ncRecordSecondaryCodeClosedValidate-验证不良代码所需次级代码是否全部已关闭
     *
     * @param tenantId
     * @param ncRecordId
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/4/2
     */
    void ncRecordSecondaryCodeClosedValidate(Long tenantId, String ncRecordId);

    /**
     * propertyLimitNcRecordQuery-根据属性查询不良记录清单
     *
     * @param tenantId
     * @param dto
     * @return
     * @author sen.luo
     * @date 2019/4/1
     */
    List<MtNcRecord> propertyLimitNcRecordQuery(Long tenantId, MtNcRecord dto);

    /**
     * eoMaxNcLimitValidate -验证执行作业是否达到不良代码最大记录数
     * 
     * @Author lxs
     * @Date 2019/4/2
     * @Return java.lang.Boolean
     */
    String eoMaxNcLimitValidate(Long tenantId, MtNcRecordVO1 dto);

    /**
     * ncRecordAndHisUpdate-更新不良记录及历史
     *
     * @author chuang.yang
     * @date 2019/4/2
     * @param tenantId
     * @param dto
     * @return void
     */
    MtNcRecordVO8 ncRecordAndHisUpdate(Long tenantId, MtNcRecordVO7 dto);

    /**
     * ncRecordAndIncidentClose-不良记录与事故关闭
     * 
     * @Author lxs
     * @Date 2019/4/2
     * @Return java.lang.String
     */
    String ncRecordAndIncidentClose(Long tenantId, MtNcRecordVO2 dto);

    /**
     * ncRecordAndHisCreate-创建不良记录及历史
     * 
     * @Author lxs
     * @Date 2019/4/2
     * @Return java.lang.String
     */
    MtNcRecordVO8 ncRecordAndHisCreate(Long tenantId, MtNcRecordVO3 dto);


    /**
     * ncRecordCloseVerify-验证不良代码是否可以关闭
     *
     * @param tenantId
     * @param ncRecordId
     * @author guichuan.li
     * @date 2019/4/2
     */
    String ncRecordCloseVerify(Long tenantId, String ncRecordId);

    /**
     * ncRecordCloseProcess-不良关闭
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/2
     */
    void ncRecordCloseProcess(Long tenantId, MtNcRecordVO2 dto);

    /**
     * ncIncidentCloseVerify-验证不良事故是否允许关闭
     * 
     * @author sen.luo
     * @date 2019/4/1
     * @param tenantId
     * @param ncIncidentId
     * @return
     */
    String ncIncidentCloseVerify(Long tenantId, String ncIncidentId);

    /**
     * ncRecordClose-不良记录关闭
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/2
     */
    String ncRecordClose(Long tenantId, MtNcRecordVO2 dto);

    /**
     * ncRecordConfirm-不良记录复核
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/2
     */
    void ncRecordConfirm(Long tenantId, MtNcRecordVO2 dto);

    /**
     * ncRecordCancel-不良记录取消
     * 
     * @Author lxs
     * @Date 2019/4/2
     * @Return void
     */
    void ncRecordCancel(Long tenantId, MtNcRecordVO2 dto);

    /**
     * parentNcCodeLimitNcRecordGet -根据主不良记录查询不良记录清单
     * 
     * @Author lxs
     * @Date 2019/4/2
     * @Return java.util.List<hmes.nc_record.dto.MtNcRecord>
     */
    List<MtNcRecord> parentNcCodeLimitNcRecordGet(Long tenantId, String parentNcRecordId);

    /**
     * ncRecordCreateProcess-不良记录登记处置.
     *
     * @author chuang.yang
     * @date 2019/4/2
     * @param tenantId
     * @param dto
     * @return java.util.List<java.lang.String>
     */
    List<String> ncRecordCreateProcess(Long tenantId, MtNcRecordVO4 dto);

    /**
     * eoStepActualLimitNcRecordQuery-根据eo步骤实绩不良记录清单
     *
     */
    List<MtNcRecord> eoStepActualLimitNcRecordQuery(Long tenantId, String eoStepActualId);
}
