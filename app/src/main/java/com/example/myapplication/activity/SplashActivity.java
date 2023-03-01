package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anchorfree.partner.api.ClientInfo;
import com.anchorfree.partner.api.data.Country;
import com.anchorfree.partner.api.response.AvailableCountries;
import com.anchorfree.reporting.TrackingConstants;
import com.anchorfree.sdk.SessionConfig;
import com.anchorfree.sdk.UnifiedSDK;
import com.anchorfree.sdk.rules.TrafficRule;
import com.anchorfree.vpnsdk.callbacks.Callback;
import com.anchorfree.vpnsdk.callbacks.CompletableCallback;
import com.anchorfree.vpnsdk.exceptions.VpnException;
import com.anchorfree.vpnsdk.transporthydra.HydraTransport;
import com.anchorfree.vpnsdk.vpnservice.VPNState;
import com.example.myapplication.R;
import com.example.myapplication.utils.SharePreferences;
import com.example.myapplication.utils.Utilss;
import com.example.myapplication.model.Country_Code;
import com.example.myapplication.model.NotificationBody;
import com.example.myapplication.viewModel.NotificationViewModel;
import com.northghost.caketube.OpenVpnTransportConfig;
import com.pesonal.adsdk.ADS_SplashActivity;
import com.pesonal.adsdk.AppManage;
import com.pesonal.adsdk.AppOpenManager;
import com.pesonal.adsdk.getDataListner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class SplashActivity extends ADS_SplashActivity {

    private static final String TAG = "SplashActivity";

    private ClientInfo clientInfo;
    ArrayList<Country_Code> list_country;
    private String selected = "";
    SharePreferences sp;
    List<Country> list;
    private AppOpenManager manager;
    public boolean vpn_connection = false;
    NotificationViewModel viewModel;


    @Override
    protected void onResume() {
        super.onResume();

        Log.e(TAG, "onResume: "+sp.getFCMToken() );
        NotificationBody body = new NotificationBody("Splash Screen", "Hello Splash Activity!");
        viewModel.sendNotification(body);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_splash);

        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        sp = new SharePreferences(this);

        getCountryList();

        if (sp.getSecureBox()) {
            Log.e("Check_Status", "Testing...");
            vpn_connection = true;
            loginVpnMethodSTAR();
        } else {
            Log.e("Check_Status", "Testing...sdgdfghf");
            ADSinit(SplashActivity.this, getCurrentVersionCode(), new getDataListner() {
                @Override
                public void onSuccess() {
                    Log.e("Check_Status", "" + AppManage.VPN_STATUS);
                    if (AppManage.VPN_STATUS == 0) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    } else if (AppManage.VPN_STATUS == 2) {
                        loginVpnMethod();

                    } else if (AppManage.VPN_STATUS == 1) {
                        startActivity(new Intent(getApplicationContext(), VPNActivity.class));
                        finish();
                    }
                }

                @Override
                public void onUpdate(String url) {
                    Log.e("my_log", "onUpdate: " + url);
                    showUpdateDialog(url);
                }

                @Override
                public void onRedirect(String url) {
                    Log.e("my_log", "onRedirect: " + url);
                    showRedirectDialog(url);
                }

                @Override
                public void onReload() {
                    startActivity(new Intent(SplashActivity.this, SplashActivity.class));
                    finish();
                }

                @Override
                public void onGetExtradata(JSONObject extraData) {
                    Log.e("my_log", "ongetExtradata: " + extraData.toString());
                }
            });
        }


    }

    public void getInit() {
        ADSinit(SplashActivity.this, getCurrentVersionCode(), new getDataListner() {
            @Override
            public void onSuccess() {
                Log.e("Check_Status", "" + AppManage.VPN_STATUS);

                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }


            @Override
            public void onUpdate(String url) {
                Log.e("my_log", "onUpdate: " + url);
                showUpdateDialog(url);
            }

            @Override
            public void onRedirect(String url) {
                Log.e("my_log", "onRedirect: " + url);
                showRedirectDialog(url);
            }

            @Override
            public void onReload() {
                startActivity(new Intent(SplashActivity.this, SplashActivity.class));
                finish();
            }

            @Override
            public void onGetExtradata(JSONObject extraData) {
                Log.e("my_log", "ongetExtradata: " + extraData.toString());
            }
        });
    }


    public void showRedirectDialog(final String url) {

        final Dialog dialog = new Dialog(SplashActivity.this);
        dialog.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.installnewappdialog, null);
        dialog.setContentView(view);
        TextView update = view.findViewById(R.id.update);
        TextView txt_title = view.findViewById(R.id.txt_title);
        TextView txt_decription = view.findViewById(R.id.txt_decription);

        update.setText("Install Now");
        txt_title.setText("Install our new app now and enjoy");
        txt_decription.setText("We have transferred our server, so install our new app by clicking the button below to enjoy the new features of app.");


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri marketUri = Uri.parse(url);
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    startActivity(marketIntent);
                } catch (ActivityNotFoundException ignored1) {
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.create();
        }

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }


    public void showUpdateDialog(final String url) {

        final Dialog dialog = new Dialog(SplashActivity.this);
        dialog.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.installnewappdialog, null);
        dialog.setContentView(view);
        TextView update = view.findViewById(R.id.update);
        TextView txt_title = view.findViewById(R.id.txt_title);
        TextView txt_decription = view.findViewById(R.id.txt_decription);

        update.setText("Update Now");
        txt_title.setText("Update our new app now and enjoy");
        txt_decription.setText("");
        txt_decription.setVisibility(View.GONE);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri marketUri = Uri.parse(url);
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    startActivity(marketIntent);
                } catch (ActivityNotFoundException ignored1) {
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.create();
        }

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    public int getCurrentVersionCode() {
        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    getPackageName(), 0);
            return info.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 0;

    }


    public void loginVpnMethodSTAR() {
        String backendurl = "https://d2isj403unfbyl.cloudfront.net";
        String key = "touchvpn";
        if (!backendurl.isEmpty() && !key.isEmpty()) {
            if (!backendurl.equals("") && !key.equals("")) {
                clientInfo = ClientInfo.newBuilder().addUrl(backendurl).carrierId(key).build();
                UnifiedSDK.clearInstances();
                Utilss.loginClientInfo(clientInfo);
            }
            getCountry();

        } else {
            getInit();
        }
    }

    public void loginVpnMethod() {
        String backendurl = "https://d2isj403unfbyl.cloudfront.net";
        String key = "touchvpn";
        if (!backendurl.isEmpty() && !key.isEmpty()) {
            if (!backendurl.equals("") && !key.equals("")) {
                clientInfo = ClientInfo.newBuilder().addUrl(backendurl).carrierId(key).build();
                UnifiedSDK.clearInstances();
                Utilss.loginClientInfo(clientInfo);
            }
            if (sp.getSecureBox()) {
                getCountry();
            } else {
                DailogVpn();
            }

        } else {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            Log.e("Error", "Login ...!");
        }
    }

    public void getCountryList() {
        try {
            String a = loadJSONFromAsset("country_short.json", SplashActivity.this);
            JSONObject obj = new JSONObject(a);
            JSONArray m_jArry = obj.getJSONArray("country");
            Utilss.list_Country = new ArrayList<>();
            for (int i = 0; i < m_jArry.length(); i++) {
                Country_Code counteyModal = new Country_Code();
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                String code = jo_inside.getString("Code");
                String name = jo_inside.getString("Name");
                counteyModal.setCode(code);
                counteyModal.setName(name);
                Utilss.list_Country.add(counteyModal);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(SplashActivity.this, "Something went wrong. please check again later", Toast.LENGTH_SHORT).show();
        }
    }

    public static String loadJSONFromAsset(String a, Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(a);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void DailogVpn() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.vpn_dialog, null);
        builder.setView(view);
        TextView turen_btn = view.findViewById(R.id.turen_btn);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        turen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.setSecureBox(true);
                turen_btn.setText("Connecting....");
                getCountry();
            }
        });


    }


    public void getCountry() {
        try {
            UnifiedSDK.getInstance().getBackend().countries(new com.anchorfree.vpnsdk.callbacks.Callback<AvailableCountries>() {
                @Override
                public void success(@NonNull AvailableCountries availableCountries) {
                    try {
                        Utilss.list = new ArrayList<>();
                        list_country = new ArrayList<>();
                        list = availableCountries.getCountries();
                        Utilss.list = availableCountries.getCountries();
                        if (Utilss.list_Country.size() > 0) {
                            for (Country country : Utilss.list) {
                                for (Country_Code code : Utilss.list_Country) {

                                    if (country.getCountry().equalsIgnoreCase(code.getCode())) {
                                        Log.e("Country_Size", "Service_List " + code.getCode());
                                        Country_Code new_code = new Country_Code();
                                        new_code.setCode(code.getCode());
                                        list_country.add(new_code);
                                    }
                                }


                            }
                            if (!list_country.isEmpty()) {
                                selected = getRandomString(list_country);
                            } else {
                                selected = "";
                            }
                        } else {

                            if (!list.isEmpty()) {
                                selected = getRandomStringValue(list);
                            } else {
                                selected = "";
                            }
                        }

                        connectToVpn(selected);
                    } catch (Exception e) {
                        Log.e("test", "Error " + e.getMessage());
                        connectToVpn("");
                    }

                }


                @Override
                public void failure(@NonNull VpnException e) {
                    Log.e("test", "Vpn Error " + e.getMessage());
                    connectToVpn("");
                }
            });
        } catch (Exception e) {

        }

    }

    private void connectToVpn(String country) {
        UnifiedSDK.getInstance().getBackend().isLoggedIn(new com.anchorfree.vpnsdk.callbacks.Callback<Boolean>() {
            @Override
            public void success(@NonNull Boolean aBoolean) {
                if (aBoolean) {
                    Log.e("Vpn_Error", "success: logged in");
                    List<String> fallbackOrder = new ArrayList<>();
                    fallbackOrder.add(HydraTransport.TRANSPORT_ID);
                    fallbackOrder.add(OpenVpnTransportConfig.tcp().getName());
                    fallbackOrder.add(OpenVpnTransportConfig.udp().getName());

                    List<String> bypassDomains = new LinkedList<>();
                    UnifiedSDK.getInstance().getVPN().start(new SessionConfig.Builder()
                            .withReason(TrackingConstants.GprReasons.M_UI)
                            .withTransportFallback(fallbackOrder)
                            .withTransport(HydraTransport.TRANSPORT_ID)
                            .withVirtualLocation(country)
                            .addDnsRule(TrafficRule.Builder.bypass().fromDomains(bypassDomains))
                            .build(), new CompletableCallback() {
                        @Override
                        public void complete() {
                            updateUi();
                            if (vpn_connection) {
                                getInit();
                            } else {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }


                        }

                        @Override
                        public void error(@NonNull VpnException e) {
                            //  connection_progress.setVisibility(View.GONE);
                            Log.e("Check_Exception", e.getMessage());
//                            Toast.makeText(SplashActivity.this, "Error while connecting to vpn", Toast.LENGTH_SHORT).show();
                            getInit();
                            updateUi();


                        }
                    });

                } else {

                    updateUi();

                }


            }

            @Override
            public void failure(@NonNull VpnException e) {
                Log.e("Connect_Vpn", e.getMessage());
                getInit();

            }
        });

    }

    private void updateUi() {


        UnifiedSDK.getVpnState(new Callback<VPNState>() {
            @Override
            public void success(@NonNull VPNState vpnState) {
                if (vpnState == VPNState.CONNECTED && sp.getVpnButtonStatus() == 1) {
                    //  llVpnState.setVisibility(View.VISIBLE);
                } else {
                    //  llVpnState.setVisibility(View.GONE);
                }
                if (sp.getVpnButtonStatus() == 1) {
                    //  connect_btn.setVisibility(vpnState == VPNState.CONNECTED ? View.VISIBLE : View.GONE);
                } else {
                    //  connect_btn.setVisibility(View.GONE);
                }

                switch (vpnState) {
                    case IDLE:
                        // tvConnectionStatus.setText("Status: " + getString(R.string.disconnected));
                        Log.e("Vpn_Status", " Disconnected");
                        //   connect_btn.setImageResource(R.drawable.off_btn);
                        break;
                    case CONNECTED:
                        // tvConnectionStatus.setText("Status: " + getString(R.string.connected));
                        Log.e("Vpn_Status", " Connected");
                        //   connect_btn.setImageResource(R.drawable.vpn);
                        break;
                    case CONNECTING_VPN:
                    case CONNECTING_CREDENTIALS:
                    case CONNECTING_PERMISSIONS:
                        Log.e("Vpn_Status", " Connecting");
                        //  connect_btn.setImageResource(R.drawable.off_btn);
                        break;
                    case PAUSED:
                        Log.e("Vpn_Status", " PAUSE");
                        // connect_btn.setImageResource(R.drawable.off_btn);
                        break;
                    case DISCONNECTING:
                        Log.e("Vpn_Status", " DISCONNECTING");
                        //   connect_btn.setImageResource(R.drawable.off_btn);

                }
            }

            @Override
            public void failure(@NonNull VpnException e) {

            }
        });


    }


    private String getRandomString(ArrayList<Country_Code> list) {
        int min = 0;
        int max = list.size();
        return list.get(new Random().nextInt(((max - min) + 1) + min)).getCode();
    }

    private String getRandomStringValue(List<Country> list) {
        int min = 0;
        int max = list.size();
        return list.get(new Random().nextInt(((max - min) + 1) + min)).getCountry();
    }


    public static void stopVpn() {
        UnifiedSDK.getVpnState(new com.anchorfree.vpnsdk.callbacks.Callback<VPNState>() {
            @Override
            public void success(@NonNull VPNState vpnState) {
                if (vpnState == VPNState.CONNECTED) {
                    UnifiedSDK.getInstance().getVPN().stop(TrackingConstants.GprReasons.M_UI, new CompletableCallback() {
                        @Override
                        public void complete() {
                        }

                        @Override
                        public void error(@NonNull VpnException e) {

                        }
                    });
                }
            }

            @Override
            public void failure(@NonNull VpnException e) {

            }
        });
    }


}