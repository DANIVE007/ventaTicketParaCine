package cl.inacap.eval3appmovil_iturra;

import android.os.Parcel;
import android.os.Parcelable;

public class Tickets implements Parcelable {
    private String rut;
    private String bol;
    private String pel;
    private String sal;

    public Tickets() {
    }

    public Tickets(String rut, String bol, String pel, String sal) {
        this.rut = rut;
        this.bol = bol;
        this.pel = pel;
        this.sal = sal;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getBol() {
        return bol;
    }

    public void setBol(String bol) {
        this.bol = bol;
    }

    public String getPel() {
        return pel;
    }

    public void setPel(String pel) {
        this.pel = pel;
    }

    public String getSal() {
        return sal;
    }

    public void setSal(String sal) {
        this.sal = sal;
    }

    @Override
    public String toString() {
        return bol;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.rut);
        dest.writeString(this.bol);
        dest.writeString(this.pel);
        dest.writeString(this.sal);
    }

    public void readFromParcel(Parcel source) {
        this.rut = source.readString();
        this.bol = source.readString();
        this.pel = source.readString();
        this.sal = source.readString();
    }

    protected Tickets(Parcel in) {
        this.rut = in.readString();
        this.bol = in.readString();
        this.pel = in.readString();
        this.sal = in.readString();
    }

    public static final Parcelable.Creator<Tickets> CREATOR = new Parcelable.Creator<Tickets>() {
        @Override
        public Tickets createFromParcel(Parcel source) {
            return new Tickets(source);
        }

        @Override
        public Tickets[] newArray(int size) {
            return new Tickets[size];
        }
    };
}