package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author xiao tang
 * @ClassName MtWkcShiftVO10
 * @description
 */
public class MtWkcShiftVO10 implements Serializable {

	private static final long serialVersionUID = -4040358513235570677L;
	
	@ApiModelProperty(value = "班次时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date shiftDate;
	
	@ApiModelProperty(value = "班次编码")
    private String shiftCode;// 班次编码

	public Date getShiftDate() {
		return shiftDate == null ? null : (Date)shiftDate.clone();
	}

	public void setShiftDate(Date shiftDate) {
		this.shiftDate = shiftDate == null ? null : (Date)shiftDate.clone();
	}

	public String getShiftCode() {
		return shiftCode;
	}

	public void setShiftCode(String shiftCode) {
		this.shiftCode = shiftCode;
	}

}
