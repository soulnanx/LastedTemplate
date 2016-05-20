package br.com.beta.superplayground.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.beta.superplayground.R;
import br.com.beta.superplayground.entity.Repository;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by renan on 09/05/16.
 */
public class RepositoriesAdapter extends RecyclerView.Adapter<RepositoriesAdapter.ViewHolder> {
    private Context context;
    private final List<Repository> repositories;
    private final CallbackClick clickCallback;

    public RepositoriesAdapter(List<Repository> repositories, CallbackClick clickSegments) {
        super();
        this.repositories = repositories;
        this.clickCallback = clickSegments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repository, parent, false);
        return new ViewHolder(v);
    }


    public void addData(Repository repository){
        repositories.add(repository);
        notifyDataSetChanged();
    }

    public void clear(){
        repositories.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Repository repository = repositories.get(position);

        if (repository != null){
            holder.name.setText(repository.getName());
            holder.fullName.setText(repository.getFullName());
        }

    }

    public void animateTo(List<Repository> repositories) {
        applyAndAnimateRemovals(repositories);
        applyAndAnimateAdditions(repositories);
        applyAndAnimateMovedItems(repositories);
    }

    private void applyAndAnimateAdditions(List<Repository> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Repository model = newModels.get(i);
            if (!repositories.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Repository> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Repository model = newModels.get(toPosition);
            final int fromPosition = repositories.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public void addItem(int position, Repository model) {
        repositories.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Repository model = repositories.remove(fromPosition);
        repositories.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    private void applyAndAnimateRemovals(List<Repository> newModels) {
        for (int i = repositories.size() - 1; i >= 0; i--) {
            final Repository model = repositories.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    public Repository removeItem(int position) {
        final Repository model = repositories.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(value = R.id.item_repository_title)
        TextView name;

        @BindView(value = R.id.item_repository_full_name)
        TextView fullName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickCallback.onClick(repositories.get(getAdapterPosition()));
        }
    }

    public interface CallbackClick {
        void onClick(Repository repository);
    }
}
