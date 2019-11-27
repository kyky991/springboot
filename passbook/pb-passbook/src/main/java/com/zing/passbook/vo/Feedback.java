package com.zing.passbook.vo;

import com.zing.passbook.constant.FeedbackType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

/**
 * 用户评论
 *
 * @author Zing
 * @date 2019-11-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 评论类型
     */
    private String type;

    /**
     * PassTemplate RowKey, 如果是 app 类型的评论, 则没有
     */
    private String templateId;

    /**
     * 评论内容
     */
    private String comment;

    public boolean validate() {
        FeedbackType feedbackType = Arrays.stream(FeedbackType.values())
                .filter(t -> this.type.equals(t.getCode()))
                .findFirst()
                .orElse(null);

        return !(null == feedbackType || null == comment);
    }

}
