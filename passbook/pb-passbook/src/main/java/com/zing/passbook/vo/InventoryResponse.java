package com.zing.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 库存请求响应
 *
 * @author Zing
 * @date 2019-11-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 优惠券模板信息
     */
    private List<PassTemplateInfo> passTemplateInfos;

}
