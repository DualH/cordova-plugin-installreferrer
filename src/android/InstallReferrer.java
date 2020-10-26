package cordova.plugin.installreferrer;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerClient.*;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;

import org.apache.cordova.*;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class InstallReferrer extends CordovaPlugin {
    public InstallReferrerClient referrerClient;

    private boolean clientInitialized = false;
    private static final String LOG_TAG = "InstallReferrer";

    private static final String ACTION_CONNECTION_START = "open";
    private static final String ACTION_CONNECTION_END = "close";
    private static final String ACTION_CONNECTION_IS_STARTED = "isOpen";
    private static final String ACTION_GET_PARAMS = "getParams";

    protected static void handleExceptionWithContext(Exception e, CallbackContext context){
        String msg = e.toString();
        LOG.e(LOG_TAG, msg);
        context.error(msg);
    }

    protected static void handleErrorWithContext(String e, CallbackContext context) {
        LOG.e(LOG_TAG, e);
        context.error(e);
    }

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (action.equals(ACTION_CONNECTION_START)) {
            Context context = this.cordova.getActivity().getApplicationContext();

            LOG.d(LOG_TAG, "Attempt initialize client referrer");

            if (clientInitialized == false) {
                try {
                    referrerClient = InstallReferrerClient.newBuilder(context).build();
                    referrerClient.startConnection(new InstallReferrerStateListener() {
                        @Override
                        public void onInstallReferrerSetupFinished(int responseCode) {
                            switch (responseCode) {
                                case InstallReferrerResponse.OK:
                                    clientInitialized = true;
                                    LOG.d(LOG_TAG, "Client initialized");
                                    callbackContext.success(1);
                                    break;
                                case InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                                    // API not available on the current Play Store app.
                                    clientInitialized = false;
                                    LOG.d(LOG_TAG, "Feature not supported");
                                    handleErrorWithContext("Feature not supported", callbackContext);
                                    break;
                                case InstallReferrerResponse.SERVICE_UNAVAILABLE:
                                    // Connection couldn't be established.
                                    clientInitialized = false;
                                    LOG.d(LOG_TAG, "Service Unavailable");
                                    handleErrorWithContext("Service Unavailable", callbackContext);
                                    break;
                            }
                        }

                        @Override
                        public void onInstallReferrerServiceDisconnected() {
                            // Try to restart the connection on the next request to
                            // Google Play by calling the startConnection() method.
                            clientInitialized = false;
                        }
                    });
                } catch (Exception e) {
                    handleExceptionWithContext(e, callbackContext);
                }
            } else {
                callbackContext.error("Connection already started");
            }

            
            return true;
        }

        else if (action.equals(ACTION_GET_PARAMS)) {
            if (clientInitialized != true) {
                LOG.d(LOG_TAG, "Client not initialized in getParams method");
                handleErrorWithContext("Client not initialized in getParams method", callbackContext);
            } else {
                try {
                    ReferrerDetails response = referrerClient.getInstallReferrer();
                    String referrerUrl = response.getInstallReferrer();
                    long referrerClickTime = response.getReferrerClickTimestampSeconds();
                    long appInstallTime = response.getInstallBeginTimestampSeconds();
                    boolean instantExperienceLaunched = response.getGooglePlayInstantParam();

                    JSONObject params = new JSONObject();
                    // params.put("response", response);
                    params.put("referrerUrl", referrerUrl);
                    params.put("referrerClickTime", referrerClickTime);
                    params.put("appInstallTime", appInstallTime);
                    params.put("instantExperienceLaunched", instantExperienceLaunched);

                    LOG.d(LOG_TAG, "Sending params");

                    referrerClient.endConnection();
                    clientInitialized = false;
                    callbackContext.success(params);
                } catch (Exception e) {
                    LOG.d(LOG_TAG, "Catch general");
                    handleExceptionWithContext(e, callbackContext);
                }
            }
        }

        else if (action.equals(ACTION_CONNECTION_IS_STARTED)) {
            callbackContext.success((clientInitialized) ? 1 : 0);
        }

        else if (action.equals(ACTION_CONNECTION_END)) {
            if (clientInitialized == true) {
                try {
                    referrerClient.endConnection();
                    clientInitialized = false;
                    callbackContext.success(1);
                } catch (Exception e) {
                    handleExceptionWithContext(e, callbackContext);
                }
            } else {
                callbackContext.success(1);
            }
        }

        return false;
    }
}
