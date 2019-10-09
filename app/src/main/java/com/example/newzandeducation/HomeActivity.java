package com.example.newzandeducation;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
//import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newzandeducation.API.APIClient;
import com.example.newzandeducation.API.APIInterface;
import com.example.newzandeducation.models.Article;
import com.example.newzandeducation.models.newz;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener
       // implements NavigationView.OnNavigationItemSelectedListener
{

/*Newz app Copy Paste*/
    public  static final String API_KEY="94666baed00240a48f6fc614fffbf373";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private Adapter adapter;
    private String TAG = HomeActivity.class.getSimpleName();
    private TextView topHeadLines;
    private SwipeRefreshLayout swipeRefreshLayout;

    /*Newz app Copy Paste*/

   /* private DrawerLayout drawerLayout;
    androidx.appcompat.widget.Toolbar toolbar;*/
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        /*toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("NewZEducation");
*/
       /* drawerLayout=findViewById(R.id.drawer_layout);
        NavigationView navigationView=findViewById(R.id.nav_view);
       navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_colse);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contailner, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.home);
        }*/
        /*Newz app Copy Paste*/
        swipeRefreshLayout=findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        topHeadLines=findViewById(R.id.topheadlines);
        recyclerView = findViewById(R.id.recyclerview);
        layoutManager=new LinearLayoutManager(HomeActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        /*Newz app Copy Paste*/

        onLoadingSwipeRefresh("");
       // LoadJson("");

    }
   //*Newz app Copy Paste*/
    public void LoadJson(final String keyword){
        topHeadLines.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(true);
        APIInterface apiInterface= APIClient.getAPIClient().create(APIInterface.class);

        String country=Utils.getCountry();
        String language=Utils.getLanguage();
        Call<newz> call;

        if(keyword.length()> 0){
            call=apiInterface.getnewzSearch(keyword,language,"publishedAt",API_KEY);
        }else{
            call=apiInterface.getnewz(country,API_KEY);
        }


        call.enqueue(new Callback<newz>() {
            @Override
            public void onResponse(Call<newz> call, Response<newz> response) {
                if (response.isSuccessful() && response.body().getArticle()!=null){
                    if(!articles.isEmpty()){
                        articles.clear();
                    }
                    articles=response.body().getArticle();
                    adapter=new Adapter(articles,HomeActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    initListener();

                    topHeadLines.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);

                }else{
                    topHeadLines.setVisibility(View.INVISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(HomeActivity.this,"No Result",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<newz> call, Throwable t) {
                topHeadLines.setVisibility(View.INVISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private  void initListener(){
        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Intent intent =new Intent(HomeActivity.this,NewsDetailActivity.class);
                Article article=articles.get(postion);
                intent.putExtra("url",article.getUrl());
                intent.putExtra("title",article.getUrl());
                intent.putExtra("img",article.getUrl());
                intent.putExtra("date",article.getUrl());
                intent.putExtra("source",article.getUrl());
                intent.putExtra("author",article.getUrl());
                startActivity(intent);
            }
        });
    }

    /*News app tool bar code start*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);
        SearchManager searchManager=(SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView=(SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem=menu.findItem(R.id.action_search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search Latest News...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length()>2){
                    onLoadingSwipeRefresh(query);
                   // LoadJson(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //onLoadingSwipeRefresh(newText);
                //LoadJson(newText);
                return false;
            }
        });

        searchMenuItem.getIcon().setVisible(false,false);

        return true;
    }

    @Override
    public void onRefresh() {
        LoadJson("");
    }
    private void onLoadingSwipeRefresh(final String keyword){
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                LoadJson(keyword);
            }
        });

        }
    }

    /*News app tool bar code End*/

    /*Newz app Copy Paste*/

  /* @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.homeFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contailner,new HomeFragment()).commit();
                break;
            case R.id.nationalFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contailner,new NationalFragment()).commit();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

