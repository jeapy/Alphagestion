package info.androidhive.alphagestion.Model;

/**
 * Created by Jean Philippe on 30/07/2016.
 */
public class Model_Bon {

    private String idbon,refbon,nomlivreur,numlivreur, date;
    public Model_Bon() {

    }
    public Model_Bon( String idbon,String refbon,String nomlivreur,String numlivreur,String date) {
        this.idbon = idbon;
        this.refbon =refbon;
        this.nomlivreur=nomlivreur;
        this.numlivreur =numlivreur;
        this.date=date;
    }

    public String getIdbon() {
        return idbon;
    }

    public void setIdbon(String idbon) {
        this.idbon = idbon;
    }

    public String getRefbon() {
        return refbon;
    }

    public void setRefbon(String refbon) {
        this.refbon = refbon;
    }

    public String getNomlivreur() {
        return nomlivreur;
    }

    public void setNomlivreur(String nomlivreur) {
        this.nomlivreur = nomlivreur;
    }

    public String getNumlivreur() {
        return numlivreur;
    }

    public void setNumlivreur(String numlivreur) {
        this.numlivreur = numlivreur;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



}
