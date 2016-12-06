package info.androidhive.alphagestion.Model;

/**
 * Created by Jean Philippe on 14/06/2016.
 */
public class Model_Rqst {

    private String ref, date,id;

    public Model_Rqst() {
    }

    public Model_Rqst(String ref, String date) {
        this.ref = ref;
        this.date = date;
        this.id = id;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
