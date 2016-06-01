package com.example.dell.assignment;

public class MyConstants {
    public static final String TAG="dell.assignment.TAG";
    public static final String SEARCH_KEY="dell.assignment.search";
    public static final String REMOVE_KEY="dell.assignment.remove";
    public static final String STORE_KEY="dell.assignment.remove";
    public static final String MODE_KEY="dell.assignment.mode";
    public static final String ID_KEY="dell.assignment.id";

    public static int getColor(int id)
    {
        switch (id)
        {
            case 0: return R.color.chrome;
            case 1: return R.color.black;
            case 2: return R.color.cyan;
            case 3: return R.color.green;
            case 4: return R.color.pink;
            case 5: return R.color.violet;
            case 6: return R.color.red;
            default: return R.color.black;
        }
    }
}
