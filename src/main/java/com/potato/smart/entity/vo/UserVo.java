package com.potato.smart.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class UserVo {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String username;

    private String password;

    private String email;

    private String code;

    private Date createTime;

    private Date updateTime;
}

