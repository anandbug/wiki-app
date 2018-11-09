package in.nishachar.wiki_app.home;

import android.view.View;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.nishachar.wiki_app.R;
import in.nishachar.wiki_app.model.Page;
import in.nishachar.wiki_app.shared.GlideApp;

class SearchItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private OnItemClick onItemClick;

    @BindView(R.id.searchTitle)
    AppCompatTextView searchTitle;

    @BindView(R.id.searchDescription)
    AppCompatTextView searchDescription;

    @BindView(R.id.searchImage)
    AppCompatImageView searchImage;

    SearchItemHolder(@NonNull View itemView, OnItemClick onItemClick) {
        super(itemView);
        this.onItemClick = onItemClick;

        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(this);
    }

    void bindData(Page page) {
        searchTitle.setText(page.getTitle());

        if (page.getTerms() != null)
            searchDescription.setText(page.getTerms().getDescription().get(0));

        GlideApp.with(itemView)
                .load(page.getThumbnail() != null ? page.getThumbnail().getSource() : null)
                .placeholder(R.drawable.ic_wikipedia)
                .transform(new CircleCrop())
                .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                .into(searchImage);
    }

    @Override
    public void onClick(View v) {
        if(onItemClick != null)
            onItemClick.itemClicked(getAdapterPosition());
    }
}
