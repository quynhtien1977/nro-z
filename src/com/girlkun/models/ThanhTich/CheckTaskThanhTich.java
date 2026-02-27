/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.girlkun.models.ThanhTich;

import com.girlkun.models.player.Player;

/**
 *
 * @author Administrator
 */
public class CheckTaskThanhTich 
{
    public static boolean CheckSKH(Player pl,int type)
    {
        boolean flag1= pl.setClothes.nappa == 5 || pl.setClothes.kakarot == 5 ||pl.setClothes.cadic == 5;
        boolean flag2= pl.setClothes.kirin == 5 || pl.setClothes.songoku == 5 ||pl.setClothes.thienXinHang == 5;
        boolean flag3= pl.setClothes.picolo == 5 || pl.setClothes.ocTieu == 5 ||pl.setClothes.pikkoroDaimao == 5;
        if(type == 1)
        {
            return flag1 || flag2 || flag3;
        }
        if(type == 2)
        {
            return (flag1 || flag2 || flag3) && pl.setClothes.godClothes;
        }
        if(type == 3)
        {
             return (flag1 || flag2 || flag3) && pl.setClothes.IsSetHuyDiet();
        }
        return false;
    }
    public static boolean CheckKillBoss(Player pl,int type)
    {
        int param = Integer.MAX_VALUE;
        switch(type)
        {
            case 1 :
                param = 50;
                break;
            case 2 :
                param = 500;
                break;
            case 3 :
                param = 5000;
                break;
        }
        return pl.PointBoss >= param;
    }
    public static String SCheckKillBoss(Player pl,int type)
    {
        int param = 0;
        switch(type)
        {
            case 1 :
                param = 50;
                break;
            case 2 :
                param = 500;
                break;
            case 3 :
                param = 5000;
                break;
        }
        return " ("+pl.PointBoss + "/" + param + ")";
    }
    public static boolean CheckTask(Player pl,int type)
    {
        int param = Integer.MAX_VALUE;
        switch(type)
        {
            case 1 :
                param = 21 ;
                break;
            case 2 :
                param = 24;
                break;
        }
        return pl.playerTask.taskMain.id >= param;
    }
    public static String SCheckTask(Player pl,int type)
    {
        int param = 0;
        switch(type)
        {
            case 1 :
                param = 21 ;
                break;
            case 2 :
                param = 24;
                break;
        }
        return " ("+pl.playerTask.taskMain.id + "/" + param + ")";
    }
    public static boolean CheckTongNap(Player pl,int type)
    {
        int param = Integer.MAX_VALUE;
        switch(type)
        {
            case 1 :
                param = 50000;
                break;
            case 2 :
                param = 100000;
                break;
            case 3 :
                param = 500000;
                break;
        }
        return pl.getSession().TongNap >= param;
    }
    public static String SCheckTongNap(Player pl,int type)
    {
        int param = 0;
        switch(type)
        {
            case 1 :
                param = 50000;
                break;
            case 2 :
                param = 100000;
                break;
            case 3 :
                param = 500000;
                break;
        }
        return " ("+pl.getSession().TongNap + "/" + param + ")";
    }
    public static boolean CheckSMTN(Player pl,int type)
    {
        long param = Long.MAX_VALUE;
        switch(type)
        {
            case 1 :
                param = 120000000000l;
                break;
            case 2 :
                param = 150000000000l;
                break;
            case 3 :
                param = 180000000000l;
                break;
        }
        return pl.nPoint.power >= param && pl.pet.nPoint.power >= param;
    }
}
