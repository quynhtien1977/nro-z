package com.girlkun.server;

import com.arriety.kygui.ShopKyGuiManager;
import com.girlkun.services.ClanService;
import com.girlkun.services.Service;
import com.girlkun.utils.Logger;

public class Maintenance extends Thread {

    public static boolean isRuning = false;
    public boolean canUseCode;
    public static boolean isBaoTri = false;
    private static Maintenance i;

    private int min;

    private Maintenance() {

    }

    public static Maintenance gI() {
        if (i == null) {
            i = new Maintenance();
        }
        return i;
    }

    public void start(int min) {
        if (!isRuning) {
            isRuning = true;
            this.min = min;
            this.start();
        }
    }

    @Override
    public void run() {
        while (this.min > 0) 
        {
            isBaoTri = true;
            this.min--;
            Service.gI().sendThongBaoAllPlayer("Hệ thống sẽ bảo trì sau " + min
                    + " giây nữa, vui lòng thoát game để tránh mất vật phẩm");
            Logger.log(Logger.RED_BACKGROUND, "\nHệ thống tiến hành bảo trì sau " + min + "s");
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        } ServerManager.gI().close(100);
    }

}
