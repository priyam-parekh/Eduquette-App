package com.eduquette;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.eduquette.model.IndexDeck;
import com.eduquette.model.ReviewOrder;
import com.eduquette.model.ReviewType;

import java.util.List;

/**
 * Adapter class for the IndexDeck recycler view
 */
public class IndexDeckAdapter extends RecyclerView.Adapter<IndexDeckAdapter.IndexDeckViewHolder> {
    private static final String SHARED_PREFERENCES_NAME = "SharedPreference";

    // Instance variables for the index deck adapter
    private Context context;
    private List<IndexDeck> indexDeckList;

    /**
     * Constructor for the Index Deck adapter
     *
     * @param context
     * @param indexDeckList
     */
    public IndexDeckAdapter(Context context, List<IndexDeck> indexDeckList) {
        this.context = context;
        this.indexDeckList = indexDeckList;
    }

    /**
     * Overriden method that is called when the view holder is created
     *
     * @param viewGroup
     * @param position
     * @return
     */
    @Override
    public IndexDeckAdapter.IndexDeckViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                                      .inflate(R.layout.activity_index_deck, viewGroup, false);

        return new IndexDeckViewHolder(itemView);
    }

    /**
     * Overriden method that is called when the data is bound to the view holder
     *
     * @param indexDeckViewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(IndexDeckAdapter.IndexDeckViewHolder indexDeckViewHolder, int position) {
        IndexDeck indexDeck = indexDeckList.get(position);
        indexDeckViewHolder.subject.setText(indexDeck.getSubject());
        indexDeckViewHolder.count.setText(indexDeck.getCountDisplayString());

        Glide.with(context).load(indexDeck.getThumbnail()).into(indexDeckViewHolder.thumbnail);

        indexDeckViewHolder.overflow.setOnClickListener((holder) -> {
            showPopupMenu(indexDeckViewHolder.overflow, position);
        });
    }

    /**
     * Get the number of items in the view
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return indexDeckList.size();
    }

    /**
     * Method to show the popup menu
     *
     * @param view
     * @param position
     */
    private void showPopupMenu(View view, int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_index_deck, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // Save the currently selected index deck
                saveCurrentSubject(indexDeckList.get(position));

                Intent intent;

                if (menuItem.getItemId() == R.id.action_view_cards) {
                    // If the view cards action is called then start the IndexCard activity
                    intent = new Intent(context, IndexCardActivity.class);
                    view.getContext().startActivity(intent);
                    return true;
                } else if (menuItem.getItemId() == R.id.action_review_terms_in_order) {
                    // If the review terms in order is called then start the review card activity
                    intent = new Intent(context, ReviewIndexCardActivity.class);
                    intent.putExtra("ReviewType", ReviewType.TERM);
                    intent.putExtra("ReviewOrder", ReviewOrder.IN_ORDER);
                    view.getContext().startActivity(intent);
                    return true;
                } else if (menuItem.getItemId() == R.id.action_review_definitions_in_order) {
                    // If the review definitions in order is called then start the review card activity
                    intent = new Intent(context, ReviewIndexCardActivity.class);
                    intent.putExtra("ReviewType", ReviewType.DEFINITION);
                    intent.putExtra("ReviewOrder", ReviewOrder.IN_ORDER);
                    view.getContext().startActivity(intent);
                    return true;
                } else if (menuItem.getItemId() == R.id.action_review_terms_random_order) {
                    // If the review terms in random order is called then start the review card activity
                    intent = new Intent(context, ReviewIndexCardActivity.class);
                    intent.putExtra("ReviewType", ReviewType.TERM);
                    intent.putExtra("ReviewOrder", ReviewOrder.RANDOM_ORDER);
                    view.getContext().startActivity(intent);
                    return true;
                } else if (menuItem.getItemId() == R.id.action_review_definitions_random_order) {
                    // If the review definitions in random order is called then start the review card activity
                    intent = new Intent(context, ReviewIndexCardActivity.class);
                    intent.putExtra("ReviewType", ReviewType.DEFINITION);
                    intent.putExtra("ReviewOrder", ReviewOrder.RANDOM_ORDER);
                    view.getContext().startActivity(intent);
                    return true;
                }

                return false;

            }
        });
        popup.show();
    }

    /**
     * Saves the given index deck as the current subject
     *
     * @param indexDeck the index deck to be saved
     */
    private void saveCurrentSubject(IndexDeck indexDeck) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(IndexDeck.ID, new Gson().toJson(indexDeck));
        editor.apply();
    }

    /**
     * View Holder class for the Index Deck recycler view
     */
    public class IndexDeckViewHolder extends RecyclerView.ViewHolder {
        // Instance variable for all the components in the adapter
        public TextView subject, count;
        public ImageView thumbnail, overflow;

        /**
         * Constructor for the index deck view holder
         *
         * @param view
         */
        public IndexDeckViewHolder(View view) {
            super(view);

            // Initialize all the panel components
            subject = view.findViewById(R.id.subject);
            count = view.findViewById(R.id.count);
            thumbnail = view.findViewById(R.id.thumbnail);
            overflow = view.findViewById(R.id.overflow);

            // Configure the thumbnail button listener
            thumbnail.setOnClickListener(new ThumbnailClickListener());
        }

        private class ThumbnailClickListener implements View.OnClickListener {
            @Override
            public void onClick(View view) {
                saveCurrentSubject(indexDeckList.stream().filter((IndexDeck d) -> d.getSubject() == subject.getText()).findFirst().get());
                Intent intent = new Intent(view.getContext(), IndexCardActivity.class);
                view.getContext().startActivity(intent);
            }
        }
    }

}
