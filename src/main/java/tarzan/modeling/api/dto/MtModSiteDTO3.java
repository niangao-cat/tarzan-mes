package tarzan.modeling.api.dto;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO;
import tarzan.modeling.domain.vo.MtModSiteManufacturingVO2;
import tarzan.modeling.domain.vo.MtModSiteScheduleVO2;
import tarzan.modeling.domain.vo.MtModSiteVO4;

import java.io.Serializable;
import java.util.List;

/**
 * @author yuan.yuan@hand-chin.com 2019/8/9
 */
public class MtModSiteDTO3 implements Serializable {
    private static final long serialVersionUID = -1771157560969962381L;

    @ApiModelProperty("站点基础属性")
    private MtModSiteVO4 site;

    @ApiModelProperty("站点计划属性")
    private MtModSiteScheduleVO2 siteSchedule;

    @ApiModelProperty("站点生产属性")
    private MtModSiteManufacturingVO2 siteManufacturing;

    @ApiModelProperty("站点扩展属性")
    private List<MtExtendAttrDTO> siteAttrs;


    public MtModSiteVO4 getSite() {
        return site;
    }

    public void setSite(MtModSiteVO4 site) {
        this.site = site;
    }

    public MtModSiteScheduleVO2 getSiteSchedule() {
        return siteSchedule;
    }

    public void setSiteSchedule(MtModSiteScheduleVO2 siteSchedule) {
        this.siteSchedule = siteSchedule;
    }

    public MtModSiteManufacturingVO2 getSiteManufacturing() {
        return siteManufacturing;
    }

    public void setSiteManufacturing(MtModSiteManufacturingVO2 siteManufacturing) {
        this.siteManufacturing = siteManufacturing;
    }

    public List<MtExtendAttrDTO> getSiteAttrs() {
        return siteAttrs;
    }

    public void setSiteAttrs(List<MtExtendAttrDTO> siteAttrs) {
        this.siteAttrs = siteAttrs;
    }

}
