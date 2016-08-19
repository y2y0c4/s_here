package apps.chan.com.shere;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{
    private WebView wv;
    private final Handler handler = new Handler();
    private final static String MURL = "http://52.78.119.127:8080/app";
    private final static String PURL = "http://52.78.119.127:8080/chanlee";

    private ImageView piv;
    private TextView ptv;
    private TextView jtv;
    private TextView lchk;

    private Boolean login_chk = false;


    private String id=null;
    private String pic=null;
    private String nick=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        wv = (WebView)findViewById(R.id.WebViewMain);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.addJavascriptInterface(new AndroidBridge(), "android");
        wv.loadUrl(MURL);
        wv.setWebViewClient(new WebViewClientClass());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private class AndroidBridge{
        @JavascriptInterface
        public void setAlert(final String ale){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.i("CHK...kke",ale);
                    String al = ale;
                    showMsg(al, 2000);
                }
            });
        }
        @JavascriptInterface
        public void setInfo(final String uid, final String upic, final String unick){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    id = uid;
                    pic = upic;
                    nick = unick;
                    Log.i("CHK",id+":"+pic+":"+nick);
                   // showMsg(id+":"+pic+":"+nick,2000);
                    //showMsg(id,2000 );
                    if(id == null){ //로그인안됬으면
                        ptv.setText("");
                        login_chk=false;
                        lchk.setText("로그인");
                        //piv.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.default_profile));

                    }else{
                        Log.i("id:::CHK:::::",id);
                        ptv.setText(nick);
                        login_chk=true;
                        lchk.setText("로그아웃");
                        jtv.setText("");
                        piv.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.default_profile));
                    }
                }
            });
        }
    }

    void showMsg(String msg, int option){
        Toast.makeText(this, msg, option).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        piv =(ImageView)findViewById(R.id.profile_pic);
        ptv = (TextView)findViewById(R.id.text_view_id);
        jtv = (TextView)findViewById(R.id.join_btn);
        //Typeface font = Typeface.createFromAsset(getAssets(), "fonts/SDMisaeng.ttf");
        //ptv.setTypeface(font);
        //jtv.setTypeface(font);
        lchk = (TextView)findViewById(R.id.login_chk);
        lchk.setOnClickListener(this);


        return true;
    }
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.login_chk:
                if(login_chk){//로그인 했으면
                    wv.loadUrl(MURL+"/member.do?method=logout");

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    break;
                }else{
                    wv.loadUrl(MURL+"/member.do?method=loginform");

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    break;
                }
            case R.id.join_btn:
                if(login_chk) {//로그인 했으면
                    break;
                }else{
                    wv.loadUrl(MURL+"/member.do?method=joinform");

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);

                    break;
                }
            default:
                break;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            wv.loadUrl(MURL+"/main.do");
        } else if (id == R.id.nav_board) {
            wv.loadUrl(MURL+"/board.do?method=list");
        } else if (id == R.id.nav_qlist) {
            wv.loadUrl(MURL+"/main.do?method=list");
        } else if (id == R.id.nav_rqustion) {
            wv.loadUrl(MURL+"/qget.do?method=list&pg=1");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
