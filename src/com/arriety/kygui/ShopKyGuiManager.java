/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arriety.kygui;

import com.girlkun.database.GirlkunDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.json.simple.JSONValue;

/**
 *
 * @author Administrator
 */
public class ShopKyGuiManager {

    private static ShopKyGuiManager instance;

    public static ShopKyGuiManager gI() {
        if (instance == null) {
            instance = new ShopKyGuiManager();
        }
        return instance;
    }


    public String[] tabName = {"Trang bị", "Bông tai", "Linh Thú", "Linh tinh",""};

    public List<ItemKyGui> listItem = new ArrayList<>();

    public void clear() throws SQLException 
    {
        try (Connection con = GirlkunDB.getConnection()) {
            String insertQuery = "TRUNCATE shop_ky_gui";
            try (PreparedStatement ps = con.prepareStatement(insertQuery)) {
                ps.executeUpdate();
            } catch (SQLException e) 
            {
                 System.err.print("\nError at 4\n");
                e.printStackTrace();
            }
        }
    }

    private boolean isSave;
    public void save() throws InterruptedException 
    {
         if(isSave)
        {
            return;
        }
        isSave = true;
        try {
            clear();
        } catch (SQLException ex) 
        {
             System.err.print("\nError at 5\n");
            java.util.logging.Logger.getLogger(ShopKyGuiManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        try (Connection con = GirlkunDB.getConnection()) {
            String insertQuery = "INSERT INTO shop_ky_gui (id, player_id, tab, item_id, ruby, gem, quantity, itemOption, isUpTop, isBuy, createTime) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(insertQuery)) 
            {
                List<ItemKyGui> newit = new ArrayList<>(listItem);
                for (ItemKyGui item : newit) {
                    ps.setInt(1, item.id);
                    ps.setInt(2, item.player_sell);
                    ps.setInt(3, item.tab);
                    ps.setInt(4, item.itemId);
                    ps.setInt(5, item.goldSell);
                    ps.setInt(6, item.gemSell);
                    ps.setInt(7, item.quantity);
                    ps.setString(8, JSONValue.toJSONString(item.options).equals("null") ? "[]" : JSONValue.toJSONString(item.options));
                    ps.setInt(9, item.isUpTop);
                    ps.setInt(10, item.isBuy);
                    ps.setLong(11, item.createTime);
                    ps.executeUpdate();
                }
            }
        } 
        catch (Exception e)
        {
            System.err.print("\nError at 6\n");
            e.printStackTrace();
        }
        isSave = false;
    }
}
