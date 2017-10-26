package com.example.christopher.pneuvida;


public class Patient {
    private int _id;
    private String _name;
    private String _dob;
    private String _sex;
    private String _height;
    private String _weight;
    private String _meds;
    private String _allergies;
    private String _notes;

    public Patient() {

    }
    public Patient(String name) {
        this._name = name;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_dob(String _dob) {
        this._dob = _dob;
    }

    public void set_sex(String _sex) {
        this._sex = _sex;
    }

    public void set_height(String _height) {
        this._height = _height;
    }

    public void set_weight(String _weight) {
        this._weight = _weight;
    }

    public void set_meds(String _meds) {
        this._meds = _meds;
    }

    public void set_allergies(String _allergies) {
        this._allergies = _allergies;
    }

    public void set_notes(String _notes) {
        this._notes = _notes;
    }

    public int get_id() {
        return _id;
    }

    public String get_name() {
        return _name;
    }

    public String get_dob() {
        return _dob;
    }

    public String get_sex() {
        return _sex;
    }

    public String get_height() {
        return _height;
    }

    public String get_weight() {
        return _weight;
    }

    public String get_meds() {
        return _meds;
    }

    public String get_allergies() {
        return _allergies;
    }

    public String get_notes() {
        return _notes;
    }
}
