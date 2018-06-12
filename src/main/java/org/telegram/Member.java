package org.telegram;

public class Member {

    private long id;
    private String name;
    private long gesamt;
    private long positiv;
    private long negativ;

    public Member(String name,String id, String gesamt, String positiv, String negativ ){
        super();
        this.id = Long.parseLong(id);
        this.gesamt = Long.parseLong(gesamt);
        this.positiv = Long.parseLong(positiv);
        this.negativ = Long.parseLong(negativ);
        this.name = name;

    }

    public long[] getId(){
        long[] res = {id,gesamt,positiv,negativ};
        return res;
    }

    public String getName(){
        return name;
    }


    @Override

    public String toString() {

        return  String.valueOf(id);

    }

}
