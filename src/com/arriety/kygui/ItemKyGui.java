/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arriety.kygui;

import com.girlkun.models.item.Item.ItemOption;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MITCHIKEN ZALO 0358689793
 */
public class ItemKyGui {
    public int id;
    public short itemId;
    public int player_sell;
    public byte tab;
    public int goldSell;
    public int gemSell;
    public int quantity;
    public byte isUpTop;
    public List<ItemOption> options = new ArrayList<>();
    public int isBuy;
    public long createTime;
    public ItemKyGui(){
    }
    
    public ItemKyGui(int i,short id,int plId,byte t,int gold,int gem,int q,byte isUp,List<ItemOption> op,byte b,long createTime){
        this.id = i;
        itemId = id;
        player_sell=plId;
        tab = t;
        goldSell = gold;
        gemSell = gem;
        quantity =q;
        isUpTop = isUp;
        options = op;
        isBuy = b;
        this.createTime = createTime;
    }
}
