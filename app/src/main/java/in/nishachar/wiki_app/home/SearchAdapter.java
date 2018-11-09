package in.nishachar.wiki_app.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import in.nishachar.wiki_app.R;
import in.nishachar.wiki_app.model.Page;

class SearchAdapter extends RecyclerView.Adapter<SearchItemHolder> implements OnItemClick {
    private List<Page> pages = new ArrayList<>();
    private Context context;

    SearchAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SearchItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new SearchItemHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchItemHolder holder, int position) {
        holder.bindData(pages.get(position));
    }

    @Override
    public int getItemCount() {
        return pages.size();
    }

    void addPages(List<Page> pages) {
        this.pages = pages;
        notifyDataSetChanged();
    }

    @Override
    public void itemClicked(Integer position) {
        Page page = pages.get(position);
        if (context instanceof IPageCaller)
            ((IPageCaller) context).openPage(page);
        else
            Log.d("SearchAdapter", "calling activity must implement IPageCaller");
    }
}
