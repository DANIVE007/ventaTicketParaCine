package cl.inacap.eval3appmovil_iturra;

import android.os.Parcel;
import android.os.Parcelable;

public class Usuario implements Parcelable {

    private String nom;
    private String ema;
    private String tipo;
    private String password;

    public Usuario() {

    }

    public Usuario(String nom, String ema, String tipo, String password) {
        this.nom = nom;
        this.ema = ema;
        this.tipo = tipo;
        this.password = password;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEma() {
        return ema;
    }

    public void setEma(String ema) {
        this.ema = ema;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return nom;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nom);
        dest.writeString(this.ema);
        dest.writeString(this.tipo);
        dest.writeString(this.password);
    }

    public void readFromParcel(Parcel source) {
        this.nom = source.readString();
        this.ema = source.readString();
        this.tipo = source.readString();
        this.password = source.readString();
    }

    protected Usuario(Parcel in) {
        this.nom = in.readString();
        this.ema = in.readString();
        this.tipo = in.readString();
        this.password = in.readString();
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel source) {
            return new Usuario(source);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };
}