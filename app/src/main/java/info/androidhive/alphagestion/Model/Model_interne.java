package info.androidhive.alphagestion.Model;

/**
 * Created by Jean Philippe on 15/08/2016.
 */
public class Model_interne {

    private String refsrt,nomouvrier, date;

    public Model_interne(){}

    public Model_interne(String ref,String nom,String date){
        this.refsrt=ref;
        this.nomouvrier=nom;
        this.date=date;
    }

    public String getRefsrt() {
        return refsrt;
    }

    public void setRefsrt(String refsrt) {
        this.refsrt = refsrt;
    }

    public String getNomouvrier() {
        return nomouvrier;
    }

    public void setNomouvrier(String nomouvrier) {
        this.nomouvrier = nomouvrier;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
