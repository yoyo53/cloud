package com.example.rentalService.data;

public class Rental {
    private String begin;
    private String end;
    private boolean active;

    public Rental(String begin, String end, boolean active) {
        this.begin = begin;
        this.end = end;
        this.active = active;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
}
