package com.nus.team4.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    /** 自增id */
    private Long id;

    /** 卡片id **/
    private Long cardId;

    /** 用户名 */
    private String username;

    /** MD5 加密密码 */
    private String password;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;
}
