package hu.bme.iit.cordova;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.database.Cursor;
import android.net.Uri;
import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class OpenscaleIntegration extends CordovaPlugin {
    final String usersContentUri = "content://com.health.openscale.provider/users";
    final String measurementsContentUri = "content://com.health.openscale.provider/measurements/$ID";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("coolMethod")) {
            String message = args.getString(0);
            this.coolMethod(message, callbackContext);
            return true;
        }
        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {

        ContentResolver contentResolver = cordova.getActivity().getContentResolver();
        if(!cordova.hasPermission("com.health.openscale.READ_WRITE_DATA"))
            cordova.requestPermission(this, 1, "com.health.openscale.READ_WRITE_DATA");
        final String[] projection = new String[]{"id", "username"};
        Uri uri = Uri.parse(usersContentUri);
        Cursor query = contentResolver.query(uri, null, null, null, null);
        String ret = "";
        for (String s : query.getColumnNames()) {
            ret = ret+" "+s;
        }
        callbackContext.success(ret);


        /*
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }*/
    }


}
