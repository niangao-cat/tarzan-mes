package io.tarzan.common.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.vo.MtGenTypeVO;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import io.tarzan.common.domain.vo.MtGenTypeVO7;
import io.tarzan.common.domain.vo.MtGenTypeVO8;

/**
 * 类型资源库
 *
 * @author MrZ 2019-05-21 17:09:05
 */
public interface MtGenTypeRepository extends BaseRepository<MtGenType>, AopProxy<MtGenTypeRepository> {

    /**
     * groupLimitTypeQuery根据类型组获取类型组下类型
     * 
     * @param condition
     * @return
     */
    List<MtGenType> groupLimitTypeQuery(Long tenantId, MtGenTypeVO2 condition);

    /**
     * typeLimitTypeGroupQuery根据类型获取所属类型组
     * 
     * @param condition
     * @return
     */
    List<String> typeLimitTypeGroupQuery(Long tenantId, MtGenTypeVO condition);

    /**
     * groupLimitDefaultTypeGet根据类型组获取类型组下默认类型
     * 
     * @param condition
     * @return
     */
    List<MtGenType> groupLimitDefaultTypeGet(Long tenantId, MtGenTypeVO2 condition);


    /**
     * 获取单个类型属性,非API文档函数
     * 
     * @param module
     * @param group
     * @param code
     * @return
     */
    MtGenType getGenType(Long tenantId, String module, String group, String code);


    /**
     * 获取多个类型属性,非API文档函数
     * 
     * @param module
     * @param group
     * @return
     */
    List<MtGenType> getGenTypes(Long tenantId, String module, String group);

    void initDataToRedis();

    String genTypeBasicPropertyUpdate(Long tenantId, MtGenType dto);

    void removeGenType(Long tenantId, List<MtGenType> list);

    /**
     * 根据属性获取类型信息
     * @Author peng.yuan
     * @Date 2019/10/17 10:14
     * @param tenantId :
     * @param dto :
     * @return java.util.List<MtGenTypeVO8>
     */
    List<MtGenTypeVO8> propertyLimitGenTypePropertyQuery (Long tenantId, MtGenTypeVO7 dto);

}
