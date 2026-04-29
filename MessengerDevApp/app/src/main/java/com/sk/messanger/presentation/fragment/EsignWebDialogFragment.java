package com.sk.messanger.presentation.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.activity.ComponentDialog;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sk.messanger.R;

import javax.annotation.Nullable;

/**
 * @author Sudhakar.R
 * @date 20-07-2021
 */
public class EsignWebDialogFragment extends DialogFragment {
    View rootView;
    WebView webView;
    ImageButton imgClose;

    String eSignURL = "", typeOfModule = "";

    public interface EsignStageCallBack {
        void onEsignStageCallBack(String typeOfModule,String eSignMemberID, String status);
    }
    EsignStageCallBack esignStageListener;
    public void setEsignStageCallBack(EsignStageCallBack esignStageListener) {
        this.esignStageListener = esignStageListener;
    }
    ProgressBar progressBar;
    ComponentDialog componentDialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@androidx.annotation.Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_esign_web, container, false);
        typeOfModule = getTag();
        if (getArguments() != null) {
            eSignURL = getArguments().getString("url");
        }

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
        componentDialog = (ComponentDialog) requireDialog();
        /*rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        Constants.currentFragmentName = "";
                        DashboardFragment dashboardFragment = new DashboardFragment();
                        replaceFragment(dashboardFragment);
                        return true;
                    }
                }
                return false;
            }
        });*/
        componentDialog.getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack(); // Navigate back in WebView history
                } else {
                    // No more history, disable callback and close the dialog
                    this.setEnabled(false);
                    esignStageListener.onEsignStageCallBack(typeOfModule,"", "close");
                    dismiss();
                }
            }
        });

    }

    private void initialize() {
        try {
            webView = (WebView) rootView.findViewById(R.id.web_view);
            imgClose = (ImageButton) rootView.findViewById(R.id.img_close);
            progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
            //progressBar.setVisibility(VISIBLE);
            /*WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);*/
            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            //settings.setBuiltInZoomControls(true);
            settings.setLoadWithOverviewMode(true);
            settings.setUseWideViewPort(true);
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            settings.setDatabaseEnabled(true);*/

            //webView.setWebViewClient(new MyWebViewClient());
            // COMBINE EVERYTHING HERE
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    Log.e("WebViewStatus", "Page Start Url ===> " + url);

                    // Show progress bar when loading starts
                    progressBar.setVisibility(View.VISIBLE);

                    if (url.equalsIgnoreCase(eSignURL)) {
                        // Your specific logic
                    }
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    Log.e("WebViewStatus", "Page Finished Url ===> " + url);

                    // Hide progress bar when loading ends
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    Log.e("WebViewStatus", "SSL Error Msg ===> " + error.toString());
                    // WARNING: handler.proceed() will get you banned from Play Store
                    // Use only for local testing
                    handler.proceed();
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    // Return false to let the WebView load the URL itself
                    return false;
                }
            });

            // KEEP WebChromeClient separate for progress % updates
            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    // Update the actual progress bar value
                    progressBar.setProgress(newProgress);
                    Log.e("progressBar", "ProgressBar Value ===> "+newProgress);
                }
            });

            webView.loadUrl(eSignURL);
            Log.e("onReceivedSslError", "Initial Url ===> "+eSignURL);
            imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    esignStageListener.onEsignStageCallBack(typeOfModule,"", "close");
                    dismiss();
                }
            });
        } catch (Exception e) {
            //////e.printStackTrace();
        }

    }




    /*public void replaceFragment(Fragment fragment) {
        MainActivity.mContainer.removeAllViews();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.right_to_left_in, R.anim.right_to_left_out);
        transaction.replace(R.id.frame_container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();

    }*/
    @Override
    public void onStart() {
        super.onStart();
        // Make it full screen for a better "activity-like" feel
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }
}