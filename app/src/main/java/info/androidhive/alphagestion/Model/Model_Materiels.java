package info.androidhive.alphagestion.Model;

/**
 * Created by Jean Philippe on 16/06/2016.
 */
public class Model_Materiels {
    private String libelle, image,ref;


    public Model_Materiels() {
    }

    public Model_Materiels( String image,String lib,String ref) {
        this.libelle = ref;
        this.image =image;
        this.libelle=lib;
    }

    public String getLibelle() {
        return libelle;
    }
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }


    public String getRef() {
        return ref;
    }
    public void setRef(String ref) {
        this.ref = ref;
    }



    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

}
