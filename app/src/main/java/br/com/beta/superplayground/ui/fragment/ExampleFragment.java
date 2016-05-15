package br.com.beta.superplayground.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import br.com.beta.superplayground.rest.GitHubService;
import br.com.beta.superplayground.rest.ServiceGenerator;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ExampleFragment extends Fragment implements SearchView.OnQueryTextListener {

    private View view;

    @Bind(value = R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(value = R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private List<Repository> repositories;
    private RepositoriesAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Repository> filteredRepositoryList;

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

        if (adapter != null) {
            adapter.clear();
        }

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
        GitHubService service = ServiceGenerator.generate(GitHubService.class);
        service.listAllRepositoryByUser("soulnanx", new Callback<List<Repository>>() {
            @Override
            public void success(List<Repository> modelListFromService, Response response) {
                swipeRefreshLayout.setRefreshing(false);
                repositories.addAll(modelListFromService);

                if (repositories.isEmpty()) {
                    showEmptyResult();
                } else if (!repositories.isEmpty()) {
                    loadMoreItems(modelListFromService);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                swipeRefreshLayout.setRefreshing(false);

                int message;
                if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                    message = R.string.internet_error;

                } else {
                    message = R.string.internal_error;
                }
                Snackbar.make(
                        view.findViewById(R.id.root),
                        getString(message) + " " + error.getMessage(),
                        Snackbar.LENGTH_LONG).setAction(R.string.try_again, onClickTryAgain()).show();
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

    private void loadMoreItems(List<Repository> modelListFromService) {
        for (Repository model : modelListFromService) {
            adapter.addData(model);
        }
    }

    private void showEmptyResult() {

    }

    private List<Repository> filter(List<Repository> modelList, String query) {
        query = query.toLowerCase();
        final List<Repository> modelFilteredList = new ArrayList<>();
        for (Repository model : modelList) {
            final String text = model.getFullName().toLowerCase();
            if (text.contains(query)) {
                filteredRepositoryList.add(model);
            }
        }

        return filteredRepositoryList;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_filter, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<Repository> filteredRepositoryList = filter(repositories, query);
        adapter.animateTo(filteredRepositoryList);
        recyclerView.scrollToPosition(0);
        return true;
    }


}