package com.github.nightdeveloper.smartdashboard.dto;

public class IdResult {

    private String _id;

    public IdResult() {
    }

    public IdResult(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "IdResult{" +
                "_id='" + _id + '\'' +
                '}';
    }
}
