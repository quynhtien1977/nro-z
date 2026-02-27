/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.girlkun.models.Effect;

import com.girlkun.models.player.Player;
import com.girlkun.network.io.Message;
import com.girlkun.services.Service;
import java.io.IOException;

/**
 *
 * @author Administrator
 */
public class EffectService {

    public static void AddEffecttoChar(Player pl, int idEff, int layer, int loop, int loopCount, int isStand) {
        Message msg = null;
        try {
            msg = new Message(-128);
            msg.writer().writeByte(0); // 0 là add effect
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeShort((short) idEff); // id effect
            msg.writer().writeByte((byte) layer); // 0 hiện sau nv - 1 hiện trước nv
            msg.writer().writeByte((byte) loop); // để là -1 sẽ lặp vô thời hạn
            msg.writer().writeShort((short) loopCount); // thời gian lặp
            msg.writer().writeByte((byte) isStand);
            Service.gI().sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {
            System.err.print("\nError at 80\n");
            e.printStackTrace();
        }
    }

    public static void RemoveEffecttoChar(Player pl, short id) {
        Message msg = null;
        try {
            msg = new Message(-128);
            msg.writer().writeByte(1);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeShort(id);
            Service.gI().sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {
            System.err.print("\nError at 81\n");
            e.printStackTrace();
        }
    }

    public static void RemoveAllEff(Player pl) {
        Message msg = null;
        try {
            msg = new Message(-128);
            msg.writer().writeByte(2);
            msg.writer().writeInt((int) pl.id);
            Service.gI().sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {
            System.err.print("\nError at 82\n");
            e.printStackTrace();
        }
    }
     public static void RemoveEff(Player pl,int ideff) {
       for(EffectChar ef : pl.ListEffect)
       {
           if(ef.id == ideff)
           {
               pl.ListEffect.remove(ef);
               break;
           }
       }
    }
    public static void SendEffPettoMap(Player pl) {
        if (pl != null && pl.pet != null) {
            try {
                Message msg = null;
                msg = new Message(-128);
                msg.writer().writeByte(4);
                msg.writer().writeInt(pl.pet.idAura);
                msg.writer().writeInt(pl.pet.idVongChan);
                msg.writer().writeInt(pl.pet.idVongChan2);
                pl.sendMessage(msg);
                msg.cleanup();
            } catch (IOException e) {
                System.err.print("\nError at 83\n");
                e.printStackTrace();
            }
        }
    }

    public static void SendEffChartoMap(Player pl, Player plrecieve) {
        try {
            Message msg = null;
            msg = new Message(-128);
            msg.writer().writeByte(3);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeInt((int) pl.ListEffect.size());
            for (int i = 0; i < pl.ListEffect.size(); i++) {
                msg.writer().writeShort((short) pl.ListEffect.get(i).id); // id effect
                msg.writer().writeByte((byte) pl.ListEffect.get(i).layer); // 0 hiện sau nv - 1 hiện trước nv
                msg.writer().writeByte((byte) pl.ListEffect.get(i).loop); // để là -1 sẽ lặp vô thời hạn
                msg.writer().writeShort((short) pl.ListEffect.get(i).loopCount); // thời gian lặp
                msg.writer().writeByte((byte) pl.ListEffect.get(i).isStand);
            }
            msg.writer().writeInt(pl.ListDanhHieu.size());
            for (int i = 0; i < pl.ListDanhHieu.size(); i++) {
                msg.writer().writeInt(DanhHieu.GetImgDanhHieu(pl.ListDanhHieu.get(i)));
            }
            msg.writer().writeInt(pl.idAura);
            msg.writer().writeInt(pl.idVongChan);
            msg.writer().writeInt(pl.idVongChan2);
            plrecieve.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
            System.err.print("\nError at 84\n");
            e.printStackTrace();
        }
    }

    public static void SendEffChartoMap(Player pl) {
        try {
            Message msg = null;
            msg = new Message(-128);
            msg.writer().writeByte(3);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeInt((int) pl.ListEffect.size());
            for (int i = 0; i < pl.ListEffect.size(); i++) {
                msg.writer().writeShort((short) pl.ListEffect.get(i).id); // id effect
                msg.writer().writeByte((byte) pl.ListEffect.get(i).layer); // 0 hiện sau nv - 1 hiện trước nv
                msg.writer().writeByte((byte) pl.ListEffect.get(i).loop); // để là -1 sẽ lặp vô thời hạn
                msg.writer().writeShort((short) pl.ListEffect.get(i).loopCount); // thời gian lặp
                msg.writer().writeByte((byte) pl.ListEffect.get(i).isStand);
            }
            msg.writer().writeInt(pl.ListDanhHieu.size());
            for (int i = 0; i < pl.ListDanhHieu.size(); i++) {
                msg.writer().writeInt(DanhHieu.GetImgDanhHieu(pl.ListDanhHieu.get(i)));
            }
            msg.writer().writeInt(pl.idAura);
            msg.writer().writeInt(pl.idVongChan);
            msg.writer().writeInt(pl.idVongChan2);
            Service.gI().sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();
        } catch (IOException e) {
            System.err.print("\nError at 85\n");
            e.printStackTrace();
        }
    }

}
