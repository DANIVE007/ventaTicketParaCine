package cl.inacap.eval3appmovil_iturra;

import android.os.Parcel;
import android.os.Parcelable;

public class Perfiles implements Parcelable {

    private String nombre;
    private String e_mail;
    private String perfil;
    private String password2;

    public Perfiles() {
    }

    public Perfiles(String nombre, String e_mail, String perfil, String password2) {
        this.nombre = nombre;
        this.e_mail = e_mail;
        this.perfil = perfil;
        this.password2 = password2;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nombre);
        dest.writeString(this.e_mail);
        dest.writeString(this.perfil);
        dest.writeString(this.password2);
    }

    public void readFromParcel(Parcel source) {
        this.nombre = source.readString();
        this.e_mail = source.readString();
        this.perfil = source.readString();
        this.password2 = source.readString();
    }

    protected Perfiles(Parcel in) {
        this.nombre = in.readString();
        this.e_mail = in.readString();
        this.perfil = in.readString();
        this.password2 = in.readString();
    }

    public static final Creator<Perfiles> CREATOR = new Creator<Perfiles>() {
        @Override
        public Perfiles createFromParcel(Parcel source) {
            return new Perfiles(source);
        }

        @Override
        public Perfiles[] newArray(int size) {
            return new Perfiles[size];
        }
    };
}
