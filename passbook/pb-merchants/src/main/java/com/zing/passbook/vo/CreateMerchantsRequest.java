package com.zing.passbook.vo;

import com.zing.passbook.constant.ResultCode;
import com.zing.passbook.entity.Merchants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

/**
 * 创建商户请求对象
 *
 * @author Zing
 * @date 2019-11-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMerchantsRequest {

    /**
     * 商户名称
     */
    private String name;

    /**
     * 商户 logo
     */
    private String logoUrl;

    /**
     * 商户营业执照
     */
    private String businessLicenseUrl;

    /**
     * 商户联系电话
     */
    private String phone;

    /**
     * 商户地址
     */
    private String address;

    /**
     * <h2>验证请求的有效性</h2>
     *
     * @return {@link ResultCode}
     */
    public ResultCode validate() {
        if (null == this.name) {
            return ResultCode.DUPLICATE_NAME;
        }

        if (null == this.logoUrl) {
            return ResultCode.EMPTY_LOGO;
        }

        if (null == this.businessLicenseUrl) {
            return ResultCode.EMPTY_BUSINESS_LICENSE;
        }

        if (null == this.address) {
            return ResultCode.EMPTY_ADDRESS;
        }

        if (null == this.phone) {
            return ResultCode.ERROR_PHONE;
        }

        return ResultCode.SUCCESS;
    }

    /**
     * <h2>将请求对象转换为商户对象</h2>
     *
     * @return {@link Merchants}
     */
    public Merchants toMerchants() {
        Merchants merchants = new Merchants();
        BeanUtils.copyProperties(merchants, this);
        return merchants;
    }
}
