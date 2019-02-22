package com.pine.router.command;

/**
 * Created by tanghongfeng on 2019/1/25
 */

public interface RouterLoginCommand {
    // Ui command begin
    String goLoginActivity = "goLoginActivity";
    // Ui command end

    // Data command begin
    // Data command end

    // Op command begin
    String autoLogin = "autoLogin";
    String logout = "logout";
    // Op command end
}
