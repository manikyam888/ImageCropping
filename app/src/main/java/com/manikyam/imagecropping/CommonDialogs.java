package com.manikyam.imagecropping;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by user on 7/21/2017.
 */

public class CommonDialogs {
    /**
     * This dialogue is to ask for permissions dynamically
     *
     * @param context
     * @return
     */
    public static Dialog permissionsAppDialog(final Context context) {
        final IOnPermissionsDenyListener iOnPermissionsDenyListener = (IOnPermissionsDenyListener) context;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle("Please Grant all the permissions to continue");
        // set positive button: Exit the app
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                iOnPermissionsDenyListener.onPermissionsDeny();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();
        return alertDialog;
    }
}
