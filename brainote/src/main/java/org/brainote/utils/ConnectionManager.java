/*
 * Copyright (C) 2014-2015 BraiNote
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.brainote.utils;

import android.content.Context;
import android.net.ConnectivityManager;


public class ConnectionManager {

    /**
     * Checks for available internet connection
     */
    public static boolean internetAvailable(Context ctx) {
        boolean result = false;
        ConnectivityManager conMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null) {
            boolean connected = conMgr.getActiveNetworkInfo().isConnected();
//			boolean wifi = conMgr.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
//			boolean allowMobileData = ctx.getSharedPreferences(Constants.PREFS_NAME, 
// ctx.MODE_MULTI_PROCESS).getBoolean(
//					"settings_allow_mobile_data", false);
//			result = connected && (wifi || allowMobileData);
            result = connected;
        }
        return result;
    }
}
