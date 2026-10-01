package ma.lias.app.model;

import java.util.List;

public class RapportAnnuelData {

    private int year;
    private int prevYear;

    private int eventsCurrent;
    private int eventsPrev;

    private int pubCurrent;
    private int pubPrev;

    private int convCurrent;
    private int convPrev;

    private int membresActifs;

    private List<Evenement> evenements;

    // ✅ Getters & Setters

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getPrevYear() {
        return prevYear;
    }

    public void setPrevYear(int prevYear) {
        this.prevYear = prevYear;
    }

    public int getEventsCurrent() {
        return eventsCurrent;
    }

    public void setEventsCurrent(int eventsCurrent) {
        this.eventsCurrent = eventsCurrent;
    }

    public int getEventsPrev() {
        return eventsPrev;
    }

    public void setEventsPrev(int eventsPrev) {
        this.eventsPrev = eventsPrev;
    }

    public int getPubCurrent() {
        return pubCurrent;
    }

    public void setPubCurrent(int pubCurrent) {
        this.pubCurrent = pubCurrent;
    }

    public int getPubPrev() {
        return pubPrev;
    }

    public void setPubPrev(int pubPrev) {
        this.pubPrev = pubPrev;
    }

    public int getConvCurrent() {
        return convCurrent;
    }

    public void setConvCurrent(int convCurrent) {
        this.convCurrent = convCurrent;
    }

    public int getConvPrev() {
        return convPrev;
    }

    public void setConvPrev(int convPrev) {
        this.convPrev = convPrev;
    }

    public int getMembresActifs() {
        return membresActifs;
    }

    public void setMembresActifs(int membresActifs) {
        this.membresActifs = membresActifs;
    }

    public List<Evenement> getEvenements() {
        return evenements;
    }

    public void setEvenements(List<Evenement> evenements) {
        this.evenements = evenements;
    }
}