package com.pine.router;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public interface RouterCommand {
    /**
     * Main bundle remote command begin
     */
    String MAIN_goMainHomeActivity = "goMainHomeActivity";
    /**
     * Main bundle remote command end
     */


    /**
     * Login bundle remote command begin
     */
    String LOGIN_goLoginActivity = "goLoginActivity";
    String LOGIN_autoLogin = "autoLogin";
    /**
     * Login bundle remote command end
     */

    /**
     * User bundle remote command begin
     */
    String USER_goUserCenterActivity = "goUserCenterActivity";
    /**
     * User bundle remote command end
     */

    /**
     * Business mvc bundle remote command begin
     */
    String BUSINESS_goBusinessMvcHomeActivity = "goBusinessMvcHomeActivity";
    /**
     * Business mvc bundle remote command end
     */

    /**
     * Business mvp bundle remote command begin
     */
    String BUSINESS_goBusinessMvpHomeActivity = "goBusinessMvpHomeActivity";
    /**
     * Business mvp bundle remote command end
     */

    /**
     * Business mvvm bundle remote command begin
     */
    String BUSINESS_goBusinessMvvmHomeActivity = "goBusinessMvvmHomeActivity";
    /**
     * Business mvvm bundle remote command end
     */
}
