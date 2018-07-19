package com.mygdx.game;

/**
 * Created by Mada on 7/3/2018.
 */

public class TeachingFirebaseItem {

    public String de;
    public String en;
    public String fr;
    public String img;
    public String kor;
    public String ro;

    public TeachingFirebaseItem(){
        super();
    }

    public TeachingFirebaseItem(String de, String en, String fr, String img, String kor, String ro){
        this.de = de;
        this.en = en;
        this.fr = fr;
        this.img = img;
        this.kor = kor;
        this.ro = ro;
    }

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getKor() {
        return kor;
    }

    public void setKor(String kor) {
        this.kor = kor;
    }

    public String getRo() {
        return ro;
    }

    public void setRo(String ro) {
        this.ro = ro;
    }

    public String getWordFromLanguage(String language) {
        String result ="";
        switch (language) {
            case "en" : {
                result = en;
                break;
            }
            case "de" : {
                result = de;
                break;
            }
            case "fr" : {
                result = fr;
                break;
            }
            case "kor" : {
                result = kor;
                break;
            }
            case "ro" : {
                result = ro;
                break;
            }
        }
        return result;
    }
}
