package com.pine.login;

import com.pine.base.BaseUrlConstants;

/**
 * Created by tanghongfeng on 2018/9/10.
 */

public interface LoginUrlConstants extends BaseUrlConstants {
//    String Login = BASE_URL + "";
//    String Logout = BASE_URL + "";
//    String Verify_Code_Image = BASE_URL + "";

    // Test code begin
    String Login = "http://10.10.96.154:8011/" + "/mobile/login.htm";
    String Logout = "http://10.10.96.154:8011/" + "/mobile/logout.htm";
    String Verify_Code_Image = "http://10.10.96.154:8011/" + "/picCode.htm";
    // Test code end
}
