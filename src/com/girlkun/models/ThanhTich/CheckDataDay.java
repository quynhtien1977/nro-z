/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.girlkun.models.ThanhTich;

import com.girlkun.database.GirlkunDB;
import com.girlkun.models.player.Player;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

/**
 *
 * @author Administrator
 */
public class CheckDataDay {

    private static String DataReset = "[0,0,0,0,0,0,0]";
    private static String DataOnline = "[0,0,0,0,0,0,0,0,0,0,0]";
    private static String DataNap = "[0,0,0,0,0,0,0]";

    public static void LoadDataDay(Player player, JSONArray dataArray) {
        player.TimeOnline = Integer.parseInt(String.valueOf(dataArray.get(0)));
        player.DoneDTDN = Integer.parseInt(String.valueOf(dataArray.get(1))) == 1;
        player.DoneDKB = Integer.parseInt(String.valueOf(dataArray.get(2))) == 1;
        player.JoinNRSD = Integer.parseInt(String.valueOf(dataArray.get(3))) == 1;
        player.DoneNRSD = Integer.parseInt(String.valueOf(dataArray.get(4))) == 1;
      //  player.TickCauCa = Integer.parseInt(String.valueOf(dataArray.get(5)));
   //     player.NapNgay = Integer.parseInt(String.valueOf(dataArray.get(6)));
    }
    private static final String ResetString = "update player set DataDay = \"[0,0,0,0,0,0,0]\",DataOnline = \"[0,0,0,0,0,0,0,0,0,0,0]\",DataNap = \"[0,0,0,0,0,0,0]\"";

    public static void ResetDataDay() throws SQLException {
        try (Connection con = GirlkunDB.getConnection()) {
            String insertQuery = ResetString;
            try (PreparedStatement ps = con.prepareStatement(insertQuery)) {
                ps.executeUpdate();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    System.err.print("\nError at 86\n");
                    Logger.getLogger(CheckDataDay.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException e) {
                System.err.print("\nError at 87\n");
                e.printStackTrace();
            }
        }
    }

    public static String SaveDataDay(Player player) {
        JSONArray dataArray = new JSONArray();
        dataArray.add(player.TimeOnline);
        dataArray.add(player.DoneDTDN ? 1 : 0);
        dataArray.add(player.DoneDKB ? 1 : 0);
        dataArray.add(player.JoinNRSD ? 1 : 0);
        dataArray.add(player.DoneNRSD ? 1 : 0);
//        dataArray.add(player.TickCauCa);
//        dataArray.add(player.NapNgay);
        return dataArray.toJSONString();
    }

//    public static String SaveRecieveOnline(Player player) {
//        JSONArray dataArray = new JSONArray();
//        for (int i = 0; i < OnlineHangNgay.isRecieve.length; i++) {
//            dataArray.add(OnlineHangNgay.isRecieve[i] ? 1 : 0);
//        }
//        return dataArray.toJSONString();
//    }
//
//    public static String SaveRecieveNap(Player player) {
//        JSONArray dataArray = new JSONArray();
//        for (int i = 0; i < QuaNapHangNgay.isRecieve.length; i++) {
//            dataArray.add(QuaNapHangNgay.isRecieve[i] ? 1 : 0);
//        }
//        return dataArray.toJSONString();
//    }
}
