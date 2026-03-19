package com.shunshousong.dto;

import javax.validation.constraints.*;
import lombok.Data;

public class UserDTOs {

    @Data
    public static class CreateUserDto {
        @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
        private String phone;

        @Email(message = "邮箱格式不正确")
        private String email;

        @Size(min = 6, message = "密码至少需要 6 位")
        private String password;

        @Size(min = 2, message = "昵称至少需要 2 位")
        @NotBlank(message = "昵称不能为空")
        private String nickname;

        private String avatar;
        private String realName;
        private String idCard;
        private String openid;
    }

    @Data
    public static class RegisterDto {
        @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
        @NotBlank(message = "手机号不能为空")
        private String phone;

        @Size(min = 6, message = "密码至少需要 6 位")
        @NotBlank(message = "密码不能为空")
        private String password;

        @Size(min = 2, message = "昵称至少需要 2 位")
        @NotBlank(message = "昵称不能为空")
        private String nickname;

        private String avatar;

        @AssertTrue(message = "请同意用户协议")
        private Boolean isAgreementAccepted;
    }

    @Data
    public static class LoginDto {
        @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
        @NotBlank(message = "手机号不能为空")
        private String phone;

        @NotBlank(message = "密码不能为空")
        private String password;
    }

    @Data
    public static class DepositDto {
        @NotNull(message = "金额不能为空")
        @Positive(message = "金额必须大于 0")
        private Double amount;
    }

    @Data
    public static class RatingDto {
        @NotNull(message = "评分不能为空")
        @Min(value = 1, message = "评分最小为 1")
        @Max(value = 5, message = "评分最大为 5")
        private Integer rating;
    }
}
