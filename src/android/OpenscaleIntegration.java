package hu.bme.iit.cordova;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class OpenscaleIntegration extends CordovaPlugin {
    final String PERMISSION = "com.health.openscale.READ_WRITE_DATA";
    final int REQ_CODE = 0;
    final String usersContentUri = "content://com.health.openscale.provider/users";
    final String measurementsContentUri = "content://com.health.openscale.provider/measurements/";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("getUsers")) {
            this.getUsers(callbackContext);
            return true;
        } else if(action.equals("getUserMeasurements")) {
            this.getUserMeasurements(args.getInt(0), callbackContext);
            return true;
        }
        return false;
    }

    private void getUsers(CallbackContext callbackContext) {
        if(!cordova.hasPermission(PERMISSION)) {
            cordova.requestPermission(this, REQ_CODE, PERMISSION);
        }

        ContentResolver contentResolver = cordova.getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(Uri.parse(usersContentUri), null, null, null, null);

        if(cursor != null) {
            callbackContext.success(cursorToJson(cursor));
        } else {
            callbackContext.error("Cursor null, couldn't access ContentProvider");
        }
    }

    private void getUserMeasurements(int id, CallbackContext callbackContext) {
        if(!cordova.hasPermission(PERMISSION)) {
            cordova.requestPermission(this, REQ_CODE, PERMISSION);
        }

        ContentResolver contentResolver = cordova.getActivity().getContentResolver();
        Uri uri = ContentUris.withAppendedId(Uri.parse(measurementsContentUri), id);
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if(cursor != null) {
            callbackContext.success(cursorToJson(cursor));
        } else {
            callbackContext.error("Cursor null, couldn't access ContentProvider");
        }
    }

    protected JSONArray cursorToJson(Cursor cursor) {
        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        rowObject.put(cursor.getColumnName(i),
                                cursor.getString(i));
                    } catch (Exception e) {
                        Log.d("cursorToJson", e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();
        return resultSet;
    }

}
