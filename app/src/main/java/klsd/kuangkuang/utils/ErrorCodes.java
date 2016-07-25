package klsd.kuangkuang.utils;


import java.util.HashMap;

import klsd.kuangkuang.R;

/**
 * Created by sunzhimeng on 16/2/23.
 */
public class ErrorCodes {
    public static HashMap<String,Integer> CODES = new HashMap<String, Integer>();
    static {
        CODES.put("2017", R.string.e2017);
        CODES.put("2038", R.string.e2038);
        CODES.put("2041", R.string.e2041);


    }
}
