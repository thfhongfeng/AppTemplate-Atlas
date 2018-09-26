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
     * Business a bundle remote command begin
     */
    String BUSINESS_goBusinessAHomeActivity = "goBusinessAHomeActivity";
    /**
     * Business a bundle remote command end
     */

    /**
     * Business b bundle remote command begin
     */
    String BUSINESS_goBusinessBHomeActivity = "goBusinessBHomeActivity";
    /**
     * Business b bundle remote command end
     */
}
