/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.girlkun.models.map.gas;

import com.girlkun.models.player.Player;
import com.girlkun.network.io.Message;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class TopGasService {

    public static List<TopGas> Sort(List<TopGas> topsort) {
        Collections.sort(topsort, new Comparator<TopGas>() {
            @Override
            public int compare(TopGas topGas1, TopGas topGas2) {
                int levelComparison = Integer.compare(topGas2.Level, topGas1.Level);

                if (levelComparison != 0) {
                    return levelComparison;
                }

                return Long.compare(topGas1.TimeDone, topGas2.TimeDone);
            }
        });
        if (topsort.size() > 50) {
            return topsort.subList(0, 50);
        } else {
            return topsort;
        }
    }

    public static String GetTimeDone(long Time) {
        int phut = 0;
        int giay = 0;
        while (Time / 3600 > 0) {
            Time -= 3600;
            phut += 1;
        }
        while (Time / 60 > 0) {
            Time -= 60;
            giay += 1;
        }
        return "Thời Gian Hoàn Thành :" + phut + "p:" + giay + "s";
    }

    public static String GetLevelDone(int level) {
        return "Level Hoàn Thành :" + level;
    }

    public static void SendTop(List<TopGas> tops, Player player) {
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top Khí Gas");
            msg.writer().writeByte(tops.size());
            for (int i = 0; i < tops.size(); i++) {
                TopGas z = tops.get(i);
                msg.writer().writeInt(i);
                msg.writer().writeInt(z.clan.getLeader().id);
                msg.writer().writeShort(z.clan.getLeader().head);
                msg.writer().writeShort(-1);
                msg.writer().writeShort(z.clan.getLeader().body);
                msg.writer().writeShort(z.clan.getLeader().leg);
                msg.writer().writeUTF("Clan : " + z.Name);
                msg.writer().writeUTF(GetLevelDone(z.Level));
                msg.writer().writeUTF(GetTimeDone(z.TimeDone));
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            System.err.print("\nError at 172\n");
             e.printStackTrace();
        }
    }
}
