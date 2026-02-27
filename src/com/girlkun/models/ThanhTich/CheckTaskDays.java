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
public class CheckTaskDays
{
    
    
    public static boolean[] CheckTaskDay(Player pl)
    {
        return new boolean[]
        {
            CheckTimeOnline(pl,0),
            CheckTimeOnline(pl,1),
            CheckTimeOnline(pl,2),
            CheckTimeOnline(pl,3),
            CheckTimeOnline(pl,4),
            CheckTimeOnline(pl,5),
            CheckDoneDTDN(pl),
            CheckDoneDKB(pl),
            CheckJoinNRSD(pl),
            CheckDoneNRSD(pl),
            CheckCauCa(pl)
        };
    }
     public static boolean[] CheckNapTheDay(Player pl)
    {
        return new boolean[]
        {
            CheckNapThe(pl,0),
            CheckNapThe(pl,1),
            CheckNapThe(pl,2),
            CheckNapThe(pl,3),
            CheckNapThe(pl,4),
            CheckNapThe(pl,5),
            CheckNapThe(pl,6)
        };
    }
    public static boolean CheckNapThe(Player pl,int type)
    {
        int napngay = pl.NapNgay;
        switch(type)
        {
            case 0 :
                return napngay >= 10000;
            case 1 :
                return napngay >= 20000;
            case 2 :
                return napngay >= 50000;
            case 3 :
                return napngay >= 100000;
            case 4 :
                return napngay >= 200000;
            case 5 :
                return napngay >= 500000;
            case 6 :
                return napngay >= 1000000;
        }
        return false;
    }
    public static boolean CheckTimeOnline(Player pl,int type)
    {
        int time = pl.TimeOnline;
        switch(type)
        {
            case 0 :
                return time >= 10;
            case 1 :
                return time >= 30;
            case 2 :
                return time >= 60;
            case 3 :
                return time >= 120;
            case 4 :
                return time >= 180;
            case 5 :
                return time >= 300;
        }
        return false;
    }
    public static String GetSTRTimeOnline(Player pl,int type)
    {
        return "";
    }
    public static boolean CheckDoneDTDN(Player pl)
    {
        return pl.DoneDTDN;
    }
    public static boolean CheckDoneDKB(Player pl)
    {
        return pl.DoneDKB;
    }
    public static boolean CheckJoinNRSD(Player pl)
    {
        return pl.JoinNRSD;
    }
    public static boolean CheckDoneNRSD(Player pl)
    {
        return pl.DoneNRSD;
    }
    public static boolean CheckCauCa(Player pl)
    {
        return pl.TickCauCa >= 10;
    }
     public static String CheckSTRCauCa(Player pl)
    {
        return "";
    }
}
