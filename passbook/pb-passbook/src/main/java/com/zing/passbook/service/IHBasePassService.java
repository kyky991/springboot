package com.zing.passbook.service;

import com.zing.passbook.vo.PassTemplate;

/**
 * Pass HBase 服务
 *
 * @author Zing
 * @date 2019-11-27
 */
public interface IHBasePassService {

    /**
     * <h2>将 PassTemplate 写入 HBase</h2>
     *
     * @param passTemplate {@link PassTemplate}
     * @return true/false
     */
    boolean dropPassTemplateToHBase(PassTemplate passTemplate);

}
