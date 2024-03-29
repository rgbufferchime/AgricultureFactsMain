package com.bufferchime.agriculturefact;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bufferchime.agriculturefact.Notespack.CategoryNotes;

import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;

public class Pdfview extends AppCompatActivity implements DownloadFile.Listener {

    LinearLayout root;
    RemotePDFViewPager remotePDFViewPager;
    PDFPagerAdapter adapter;
   // public static String select= CategoryNotes.getSelection();

       String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Hello World");
        setContentView(R.layout.activity_pdfview);

        root = (LinearLayout) findViewById(R.id.remote_pdf_root);
        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
             address=extras.getString("selection");
        }

        final Context ctx = this;
        final DownloadFile.Listener listener = this;
        remotePDFViewPager = new RemotePDFViewPager(ctx, getUrlFromEditText(), listener);
        remotePDFViewPager.setId(R.id.pdfViewPager);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (adapter != null) {
            adapter.close();
        }
    }



    protected String getUrlFromEditText() {

        return ("https://drive.google.com/uc?id="+address+"&export=download");
    }

    public static void open(Context context) {
        Intent i = new Intent(context, Pdfview.class);
        context.startActivity(i);
    }


    public void updateLayout() {
        root.removeAllViewsInLayout();
        root.addView(remotePDFViewPager,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onSuccess(String url, String destinationPath) {
        adapter = new PDFPagerAdapter(this, FileUtil.extractFileNameFromURL(url));
        remotePDFViewPager.setAdapter(adapter);
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        updateLayout();

    }

    @Override
    public void onFailure(Exception e) {
        e.printStackTrace();

    }

    @Override
    public void onProgressUpdate(int progress, int total) {

    }
}
