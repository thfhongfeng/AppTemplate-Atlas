package com.pine.mvp;

import com.pine.base.BaseUrlConstants;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public interface MvpUrlConstants extends BaseUrlConstants {
    String Query_HomeShopList = BASE_URL + "";
    String Query_HomeShopDetail = BASE_URL + "";
    String Query_HomeShopAndProductList = BASE_URL + "";
    // Test code begin
//    String Add_HomeShopPhoto = BASE_URL + "";
    String Add_HomeShopPhoto = "http://10.10.128.134:8011/" + "/mobile/bizFile/addBizFile.htm";
    // Test code end

    String Query_TravelNoteList = BASE_URL + "";
    String Query_TravelNoteDetail = BASE_URL + "";
    String Query_TravelNoteCommentList = BASE_URL + "";

    String H5_DefaultUrl = "https://www.baidu.com";
}
