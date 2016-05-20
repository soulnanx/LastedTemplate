package br.com.beta.superplayground.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.beta.superplayground.R;
import br.com.beta.superplayground.adapter.RepositoriesAdapter;
import br.com.beta.superplayground.entity.Repository;
import br.com.beta.superplayground.http.client.GithubClient;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExampleFragment extends Fragment {

    private View view;

    @BindView(value = R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(value = R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private List<Repository> repositories;
    private RepositoriesAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_repositories, container, false);
        init();
        return view;
    }

    private void init() {
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        setEvents();
        loadValues();
        setList();
    }

    private void setEvents() {
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener());
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                loadValues();
            }
        };
    }

    private void loadValues() {
        repositories = new ArrayList<>();
        callService();
    }

    private void setList() {
        adapter = new RepositoriesAdapter(repositories, onClickItemCallback());
        layoutManager = new LinearLayoutManager(ExampleFragment.this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private RepositoriesAdapter.CallbackClick onClickItemCallback() {
        return new RepositoriesAdapter.CallbackClick() {

            @Override
            public void onClick(Repository model) {

            }
        };
    }

    private void callService() {
        new GithubClient().getRepositories("soulnanx", new Callback<List<Repository>>() {
            @Override
            public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {

            }

            @Override
            public void onFailure(Call<List<Repository>> call, Throwable t) {

            }
        });
    }

    private View.OnClickListener onClickTryAgain() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callService();
            }
        };
    }

}