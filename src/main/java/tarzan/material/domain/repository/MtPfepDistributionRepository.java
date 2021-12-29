package tarzan.material.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.entity.MtPfepDistribution;
import tarzan.material.domain.vo.MtPfepDistributionVO;
import tarzan.material.domain.vo.MtPfepDistributionVO2;
import tarzan.material.domain.vo.MtPfepDistributionVO3;


/**
 * 物料配送属性资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
public interface MtPfepDistributionRepository
                extends BaseRepository<MtPfepDistribution>, AopProxy<MtPfepDistributionRepository> {

    /**
     * 物料配送PFEP属性新增&更新扩展表属性
     * 
     * @Author peng.yuan
     * @Date 2019/11/19 10:56
     * @param tenantId :
     * @param mtExtendVO10 :
     * @return void
     */
    void pfepDistributionAttrPropertyUpdate(Long tenantId, MtExtendVO10 mtExtendVO10);


    /**
     * materialDistributionPfepQuery-获取物料配送属性
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtPfepDistributionVO2> materialDistributionPfepQuery(Long tenantId, MtPfepDistributionVO3 dto);


    /**
     * 物料存储属性新增&更新
     * 
     * @Author peng.yuan
     * @Date 2020/2/4 15:29
     * @param tenantId :
     * @param vo :
     * @return java.lang.String
     */
    String materialPfepDistributionUpdate(Long tenantId, MtPfepDistributionVO vo);

}
