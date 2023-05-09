package com.example.carpool52;

import com.google.firebase.firestore.DocumentReference;

public class Matches {
    private int mid;
    private String description;
    private int status;
    private DocumentReference u1;
    private DocumentReference u2;
    private DocumentReference u3;
    private DocumentReference u4;

    public Matches() {}

    public Matches(int mid, String description, int status, DocumentReference u1, DocumentReference u2, DocumentReference u3, DocumentReference u4) {
        this.mid = mid;
        this.description = description;
        this.status = status;
        this.u1 = u1;
        this.u2 = u2;
        this.u3 = u3;
        this.u4 = u4;
    }

    public int getMid() {return mid;}
    public String getDescription() {return description;}
    public int getStatus() {return status;}
    public DocumentReference getU1() {return u1;}
    public DocumentReference getU2() {return u2;}
    public DocumentReference getU3() {return u3;}
    public DocumentReference getU4() {return u4;}

    public void setMid(int mid) {this.mid = mid;}
    public void setDescription(String description) {this.description = description;}
    public void setStatus(int status) {this.status = status;}
    public void setU1(DocumentReference u1) {this.u1 = u1;}
    public void setU2(DocumentReference u2) {this.u2 = u2;}
    public void setU3(DocumentReference u3) {this.u3 = u3;}
    public void setU4(DocumentReference u4) {this.u4 = u4;}
}
