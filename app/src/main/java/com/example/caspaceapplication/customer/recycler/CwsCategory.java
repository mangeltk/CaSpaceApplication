package com.example.caspaceapplication.customer.recycler;

import java.util.List;

public class CwsCategory {

    private String nameCategory;
    private List<CoworkingSpaces> cws;

    public CwsCategory(String nameCategory, List<CoworkingSpaces> cws)
    {
        this.nameCategory = nameCategory;
        this.cws = cws;
    }

    public String getNameCategory()
    {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory)
    {
        this.nameCategory = nameCategory;
    }

    public List<CoworkingSpaces> getCws()
    {
        return cws;
    }

    public void setCws(List<CoworkingSpaces> cws)
    {
        this.cws = cws;
    }
}
