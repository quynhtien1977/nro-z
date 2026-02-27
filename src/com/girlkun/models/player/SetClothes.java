package com.girlkun.models.player;

import com.girlkun.models.item.Item;


public class SetClothes {

    private Player player;

    public SetClothes(Player player) {
        this.player = player;
    }

    public byte songoku;
    public byte thienXinHang;
    public byte kirin;
    
    public boolean huydietClothers;
    public boolean thanlinhClothers;

    public byte ocTieu;
    public byte pikkoroDaimao;
    public byte picolo;

    public byte kakarot;
    public byte cadic;
    public byte nappa;

    public byte worldcup;
    public byte setDHD;
    public byte thienSuClothes = 0;
    public boolean godClothes;
    public int ctHaiTac = -1;

    public void setup() {
        setDefault();
        setupSKT();
        this.huydietClothers = true;
        this.thanlinhClothers = true;
        this.godClothes = true;
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id > 567 || item.template.id < 555) {
                    this.godClothes = false;
                    break;
                }
            } else {
                this.godClothes = false;
                break;
            }
        }
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id > 662 || item.template.id < 650) {
                    this.huydietClothers = false;
                    break;
                }
            } else {
                this.huydietClothers = false;
                break;
            }
        }
        Item ct = this.player.inventory.itemsBody.get(5);
        if (ct.isNotNullItem()) {
            switch (ct.template.id) {
                case 618:
                case 619:
                case 620:
                case 621:
                case 622:
                case 623:
                case 624:
                case 626:
                case 627:
                    this.ctHaiTac = ct.template.id;
                    break;

            }
        }
    }
    public boolean setHuyDiet() {
        for (int i = 0; i < 6; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id >= 650 && item.template.id <= 662) {
                    i++;
                } else if (i == 5) {
                    this.huydietClothers = true;
                    break;
                }
            } else {
                this.huydietClothers = false;
                break;
            }
        }
        return this.huydietClothers ? true : false;
    }
    public boolean setThanLinh() {
        for (int i = 0; i < 6; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id >= 555 && item.template.id <= 567) {
                    i++;
                } else if (i == 5) {
                    this.thanlinhClothers = true;
                    break;
                }
            } else {
                this.thanlinhClothers = false;
                break;
            }
        }
        return this.thanlinhClothers ? true : false;
    }
    public boolean IsSetThienSu() {
        int[][] DoThienSu = new int[][]{
            {1048, 1051, 1054, 1057, 1060},// td
            {1049, 1052, 1055, 1058, 1060},// namec
            {1050, 1053, 1056, 1059, 1060},// xayda    
        };
        int z = 0;
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id == DoThienSu[this.player.gender][i]) {
                    z++;
                }
                else {return false;}
            }
            else 
            {
                return false;
            }
            
        }
        return z == 5;
    }
    public boolean IsSetHuyDiet() {
        int[][] DoHuyDiet = new int[][]{
              {650, 651, 657, 658,656},// td
        {652, 653, 659, 660,656},// namec
        {654, 655, 661, 662,656},// xayda    
        };
        int z = 0;
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id == DoHuyDiet[this.player.gender][i]) {
                    z++;
                }
                else {return false;}
            }
            else 
            {
                return false;
            }
            
        }
        return z == 5;
    }
    public boolean IsSetThanlinh() {
        int[][] DoHuyDiet = new int[][]{
              {555, 556, 562, 563,561},// td
        {557, 558, 564, 565,561},// namec
        {559, 569, 566, 567,561},// xayda    
        };
        int z = 0;
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id == DoHuyDiet[this.player.gender][i]) {
                    z++;
                }
                else {return false;}
            }
            else 
            {
                return false;
            }
            
        }
        return z == 5;
    }
    private void setupSKT() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                for (Item.ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 129:
                        case 141:
                            isActSet = true;
                            songoku++;
                            break;
                        case 127:
                        case 139:
                            isActSet = true;
                            thienXinHang++;
                            break;
                        case 128:
                        case 140:
                            isActSet = true;
                            kirin++;
                            break;
                        case 131:
                        case 143:
                            isActSet = true;
                            ocTieu++;
                            break;
                        case 132:
                        case 144:
                            isActSet = true;
                            pikkoroDaimao++;
                            break;
                        case 130:
                        case 142:
                            isActSet = true;
                            picolo++;
                            break;
                        case 135:
                        case 138:
                            isActSet = true;
                            nappa++;
                            break;
                        case 133:
                        case 136:
                            isActSet = true;
                            kakarot++;
                            break;
                        case 134:
                        case 137:
                            isActSet = true;
                            cadic++;
                            break;
                        case 21:
                            if (io.param == 80) {
                                setDHD++;
                            }
                            break;
                    }

                    if (isActSet) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }

    private void setDefault() {
        this.songoku = 0;
        this.thienXinHang = 0;
        this.kirin = 0;
        this.ocTieu = 0;
        this.pikkoroDaimao = 0;
        this.picolo = 0;
        this.kakarot = 0;
        this.cadic = 0;
        this.nappa = 0;
        this.setDHD = 0;
        this.worldcup = 0;
        this.godClothes = false;
        this.ctHaiTac = -1;
    }

    public void dispose() {
        this.player = null;
    }
}
