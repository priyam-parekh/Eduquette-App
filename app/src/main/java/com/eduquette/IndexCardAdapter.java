package com.eduquette;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eduquette.model.IndexCard;

import java.util.List;

/**
 * Adapter class for the IndexCard recycler view
 */
public class IndexCardAdapter extends RecyclerView.Adapter<IndexCardAdapter.IndexCardViewHolder> {

    // Instance variables for the index card adapter
    private Context context;
    private List<IndexCard> indexCardList;

    /**
     * Constructor for the Index Card adapter
     *
     * @param context
     * @param indexCardList
     */
    public IndexCardAdapter(Context context, List<IndexCard> indexCardList) {
        this.context = context;
        this.indexCardList = indexCardList;
    }

    /**
     * Overriden method that is called when the view holder is created
     *
     * @param viewGroup
     * @param position
     * @return
     */
    @Override
    public IndexCardViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_index_card, viewGroup, false);

        return new IndexCardAdapter.IndexCardViewHolder(itemView);
    }

    /**
     * Overriden method that is called when the data is bound to the view holder
     *
     * @param indexCardViewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(IndexCardViewHolder indexCardViewHolder, int position) {
        IndexCard indexCard = indexCardList.get(position);
        indexCardViewHolder.id.setText(indexCard.getId().toString());
        indexCardViewHolder.term.setText(indexCard.getTerm());
        indexCardViewHolder.definition.setText(Html.fromHtml(indexCard.getDefinition(), Html.FROM_HTML_MODE_LEGACY));
    }

    /**
     * Get the number of items in the view
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return indexCardList.size();
    }

    /**
     * View Holder class for the Index Card recycler view
     */
    public class IndexCardViewHolder extends RecyclerView.ViewHolder {
        // Instance variables for all the components in the adapter
        public TextView id, term, definition;

        /**
         * Constructor for the index card view holder
         *
         * @param view
         */
        public IndexCardViewHolder(View view) {
            super(view);

            // Initialize all the panel components
            id = view.findViewById(R.id.tv_view_cards_id);
            term = view.findViewById(R.id.tv_view_cards_term);
            definition = view.findViewById(R.id.tv_view_cards_definition);
        }
    }

}
