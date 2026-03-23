package com.girlkun.jdbc.daos;

import com.girlkun.database.GirlkunDB;
import com.girlkun.models.player.Player;
import com.girlkun.utils.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

public class SuperRankDAO {

    public static void loadSuperRank(Player player) {
        try (Connection con = GirlkunDB.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM super_rank WHERE player_id = ?")) {
            ps.setInt(1, (int) player.id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    player.superRank = rs.getInt("rank");
                    player.superRankTicket = rs.getInt("ticket");
                    player.superRankWins = rs.getInt("wins");
                    player.superRankLoses = rs.getInt("loses");
                    player.lastTimePKSuperRank = rs.getLong("last_time_pk");
                    player.lastTimeRewardSuperRank = rs.getLong("last_time_reward");
                    
                    String historyStr = rs.getString("history");
                    if (historyStr != null && !historyStr.isEmpty()) {
                        JSONArray arr = (JSONArray) JSONValue.parse(historyStr);
                        if (arr != null) {
                            player.superRankHistory.clear();
                            for (Object obj : arr) {
                                player.superRankHistory.add(String.valueOf(obj));
                            }
                        }
                    }
                } else {
                    // Create new record for this player
                    insertNewRank(player);
                }
            }
        } catch (Exception e) {
            Logger.logException(SuperRankDAO.class, e, "Error loading Super Rank for player: " + player.name);
        }
    }

    private static void insertNewRank(Player player) {
        Connection con = null;
        PreparedStatement psSelect = null;
        PreparedStatement psInsert = null;
        ResultSet rs = null;
        try {
            con = GirlkunDB.getConnection();
            con.setAutoCommit(false);
            
            psSelect = con.prepareStatement("SELECT MAX(rank) AS max_rank FROM super_rank FOR UPDATE");
            rs = psSelect.executeQuery();
            
            int maxRank = 0;
            if (rs.next()) {
                maxRank = rs.getInt("max_rank");
            }
            
            player.superRank = maxRank + 1;
            player.superRankTicket = 3;
            
            psInsert = con.prepareStatement("INSERT INTO super_rank (player_id, rank, ticket, wins, loses, last_time_pk, history, last_time_reward) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            psInsert.setInt(1, (int) player.id);
            psInsert.setInt(2, player.superRank);
            psInsert.setInt(3, player.superRankTicket);
            psInsert.setInt(4, 0);
            psInsert.setInt(5, 0);
            psInsert.setLong(6, 0);
            psInsert.setString(7, "[]");
            psInsert.setLong(8, 0);
            psInsert.executeUpdate();
            
            con.commit();
        } catch (Exception e) {
            if (con != null) {
                try { con.rollback(); } catch (Exception ex) {}
            }
            Logger.logException(SuperRankDAO.class, e, "Error inserting new Super Rank for player: " + player.name);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (psSelect != null) psSelect.close(); } catch (Exception e) {}
            try { if (psInsert != null) psInsert.close(); } catch (Exception e) {}
            try { if (con != null) { con.setAutoCommit(true); con.close(); } } catch (Exception e) {}
        }
    }

    public static void updateSuperRank(Player player) {
        try (Connection con = GirlkunDB.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE super_rank SET rank = ?, ticket = ?, wins = ?, loses = ?, last_time_pk = ?, history = ?, last_time_reward = ? WHERE player_id = ?")) {
            
            JSONArray arr = new JSONArray();
            arr.addAll(player.superRankHistory);
            
            ps.setInt(1, player.superRank);
            ps.setInt(2, player.superRankTicket);
            ps.setInt(3, player.superRankWins);
            ps.setInt(4, player.superRankLoses);
            ps.setLong(5, player.lastTimePKSuperRank);
            ps.setString(6, arr.toJSONString());
            ps.setLong(7, player.lastTimeRewardSuperRank);
            ps.setInt(8, (int) player.id);
            ps.executeUpdate();
        } catch (Exception e) {
            Logger.logException(SuperRankDAO.class, e, "Error updating Super Rank for player: " + player.name);
        }
    }

    public static java.util.List<com.girlkun.models.matches.pvp.SuperRankBuilder> getPlayerListInRank(int currentRank, int limit) {
        java.util.List<com.girlkun.models.matches.pvp.SuperRankBuilder> list = new java.util.ArrayList<>();
        try (Connection con = GirlkunDB.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT p.id, p.name, p.head, p.data_inventory, p.items_body, s.rank, s.wins, s.loses, s.last_time_pk " +
                     "FROM player p INNER JOIN super_rank s ON p.id = s.player_id ORDER BY s.rank ASC LIMIT ?")) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    com.girlkun.models.matches.pvp.SuperRankBuilder sb = new com.girlkun.models.matches.pvp.SuperRankBuilder();
                    sb.setId(rs.getInt("id"));
                    sb.setName(rs.getString("name"));
                    sb.setRank(rs.getInt("rank"));
                    sb.setWin(rs.getInt("wins"));
                    sb.setLose(rs.getInt("loses"));
                    sb.setLastPKTime(rs.getLong("last_time_pk"));
                    sb.setHead(rs.getShort("head"));
                    
                    // Decode body and leg
                    String itemsBody = rs.getString("items_body");
                    short body = -1, leg = -1;
                    if (itemsBody != null && !itemsBody.isEmpty()) {
                        JSONArray items = (JSONArray) JSONValue.parse(itemsBody);
                        if (items != null) {
                            if (items.size() > 1) { // Ao
                                JSONArray itemAo = (JSONArray) JSONValue.parse(items.get(1).toString());
                                body = Short.parseShort(itemAo.get(0).toString());
                            }
                            if (items.size() > 2) { // Quan
                                JSONArray itemQuan = (JSONArray) JSONValue.parse(items.get(2).toString());
                                leg = Short.parseShort(itemQuan.get(0).toString());
                            }
                        }
                    }
                    sb.setBody(body);
                    sb.setLeg(leg);
                    
                    sb.setInfo("Thắng/Thua: " + sb.getWin() + "/" + sb.getLose());
                    list.add(sb);
                }
            }
        } catch (Exception e) {
            Logger.logException(SuperRankDAO.class, e, "Error getPlayerListInRank");
        }
        return list;
    }

    public static java.util.List<com.girlkun.models.matches.pvp.SuperRankBuilder> getPlayerListInRankRange(int currentRank, int limit) {
        java.util.List<com.girlkun.models.matches.pvp.SuperRankBuilder> list = new java.util.ArrayList<>();
        int startRank = Math.max(1, currentRank - Math.max(5, (limit / 2)));
        
        try (Connection con = GirlkunDB.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT p.id, p.name, p.head, p.data_inventory, p.items_body, s.rank, s.wins, s.loses, s.last_time_pk " +
                     "FROM player p INNER JOIN super_rank s ON p.id = s.player_id WHERE s.rank >= ? ORDER BY s.rank ASC LIMIT ?")) {
            ps.setInt(1, startRank);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    com.girlkun.models.matches.pvp.SuperRankBuilder sb = new com.girlkun.models.matches.pvp.SuperRankBuilder();
                    sb.setId(rs.getInt("id"));
                    sb.setName(rs.getString("name"));
                    sb.setRank(rs.getInt("rank"));
                    sb.setWin(rs.getInt("wins"));
                    sb.setLose(rs.getInt("loses"));
                    sb.setLastPKTime(rs.getLong("last_time_pk"));
                    sb.setHead(rs.getShort("head"));
                    
                    // Decode body and leg
                    String itemsBody = rs.getString("items_body");
                    short body = -1, leg = -1;
                    if (itemsBody != null && !itemsBody.isEmpty()) {
                        JSONArray items = (JSONArray) JSONValue.parse(itemsBody);
                        if (items != null) {
                            if (items.size() > 1) { // Ao
                                JSONArray itemAo = (JSONArray) JSONValue.parse(items.get(1).toString());
                                if (Short.parseShort(itemAo.get(0).toString()) != -1) body = Short.parseShort(itemAo.get(0).toString());
                            }
                            if (items.size() > 2) { // Quan
                                JSONArray itemQuan = (JSONArray) JSONValue.parse(items.get(2).toString());
                                if (Short.parseShort(itemQuan.get(0).toString()) != -1) leg = Short.parseShort(itemQuan.get(0).toString());
                            }
                        }
                    }
                    sb.setBody(body);
                    sb.setLeg(leg);
                    
                    sb.setInfo("Thắng/Thua: " + sb.getWin() + "/" + sb.getLose());
                    list.add(sb);
                }
            }
        } catch (Exception e) {
            Logger.logException(SuperRankDAO.class, e, "Error getPlayerListInRankRange");
        }
        return list;
    }
}
