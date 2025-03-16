package com.example.capstoneproject;

public class DataValidation {

    public boolean validateEmptyData(String []data)
    {
        try
        {
        for (String value:data){
            if(value.isEmpty())
            {
                return false;
            }

        }
        }
        catch (Exception e)
        {

        }
        return true;
    }
}
