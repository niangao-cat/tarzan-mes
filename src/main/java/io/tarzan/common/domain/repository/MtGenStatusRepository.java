package io.tarzan.common.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.vo.MtGenStatusVO;
import io.tarzan.common.domain.vo.MtGenStatusVO2;
import io.tarzan.common.domain.vo.MtGenStatusVO4;
import io.tarzan.common.domain.vo.MtGenStatusVO5;

/**
 * 状态资源库
 *
 * @author MrZ 2019-05-21 17:09:05
 */
public interface MtGenStatusRepository extends BaseRepository<MtGenStatus>, AopProxy<MtGenStatusRepository> {

    /**
     * groupLimitStatusQuery根据状态组获取状态组下状态
     * 
     * @param condition
     * @return List<MtGenStatus>
     */
    List<MtGenStatus> groupLimitStatusQuery(Long tenantId, MtGenStatusVO2 condition);

    /**
     * statusLimitStatusGroupQuery根据状态获取所属状态组
     * 
     * @param condition
     * @return List<String>
     */
    List<String> statusLimitStatusGroupQuery(Long tenantId, MtGenStatusVO condition);

    /**
     * groupLimitDefaultStatusGet 根据状态组获取状态组下默认状态
     * 
     * @param condition
     * @return List<MtGenStatus>
     */
    List<MtGenStatus> groupLimitDefaultStatusGet(Long tenantId, MtGenStatusVO2 condition);


    /**
     * 获取单个状态,非API文档函数
     * 
     * @param module
     * @param group
     * @param code
     * @return
     */
    MtGenStatus getGenStatus(Long tenantId, String module, String group, String code);


    /**
     * 获取多个状态,非API文档函数
     * 
     * @param module
     * @param group
     * @return
     */
    List<MtGenStatus> getGenStatuz(Long tenantId, String module, String group);

    void initDataToRedis();

    String genStatusBasicPropertyUpdate(Long tenantId, MtGenStatus dto);

    void removeGenStatus(Long tenantId, List<MtGenStatus> list);

    /**
     * 根据属性获取状态信息
     * @Author peng.yuan
     * @Date 2019/10/17 9:50
     * @param tenantId :
     * @param dto :
     * @return java.util.List<io.tarzan.common.domain.vo.MtGenStatusVO5>
     */
    List<MtGenStatusVO5> propertyLimitGenStatusPropertyQuery (Long tenantId, MtGenStatusVO4 dto);

}
