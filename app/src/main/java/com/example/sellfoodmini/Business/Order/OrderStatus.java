package com.example.sellfoodmini.Business.Order;

public enum OrderStatus {

    CHO_XU_LY("Chờ xử lý"),
    DANG_GIAO("Đang giao"),
    DA_GIAO("Đã giao"),
    DA_HUY("Đã hủy");
    private final String displayname;
    private OrderStatus(String displayname){
        this.displayname = displayname;
    }

    public String getStatus() {return this.displayname;}
}
