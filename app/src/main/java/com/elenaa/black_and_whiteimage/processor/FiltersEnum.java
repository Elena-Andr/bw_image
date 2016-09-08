package com.elenaa.black_and_whiteimage.processor;

// Enum of available filters
public enum FiltersEnum {
    None(1),
    Otsu_Algorithm(2),
    Adaptive_binarization(3),
    Sepia(4);

    private int mId;

    FiltersEnum(int id){
        mId = id;
    }

    int getId(){
        return mId;
    }

    public static FiltersEnum getFilterById(int id){
        switch (id){
            case 2:
                return Otsu_Algorithm;
            case 3:
                return Adaptive_binarization;
            case 4:
                return Sepia;
            default:
                return None;
        }
    }
}