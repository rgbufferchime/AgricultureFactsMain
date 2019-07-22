package com.bufferchime.agriculturefact;

import org.json.JSONObject;

/**
 * Created by kstanoev on 1/14/2015.
 */
public interface AsyncResult
{
    void onResult(JSONObject object);
}