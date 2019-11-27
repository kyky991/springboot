package com.zing.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建商户响应对象
 *
 * @author Zing
 * @date 2019-11-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMerchantsResponse {

    /**
     * 商户 id: 创建失败则为 -1
     */
    private Long id;
}
