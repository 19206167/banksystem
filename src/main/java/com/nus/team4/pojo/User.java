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

    /** 用户名 */
    private String username;

    /** MD5 加密密码 */
    private String password;

    /** email **/
    private String email;

    /** 用户姓名 **/
    private String name;

    /** 电话号码 **/
    private String phone;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;
}
