package com.example.messengerr;

import android.content.Context;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messengerr.databinding.ItemDiscussionBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class DiscussionAdapter extends ListAdapter<DiscussionItem, DiscussionViewHolder> {

    Context context;

    protected DiscussionAdapter(Context context) {
        super(new DiffUtil.ItemCallback<DiscussionItem>() {
            @Override
            public boolean areItemsTheSame(@NonNull DiscussionItem oldItem, @NonNull DiscussionItem newItem) {
                return oldItem.user.equals(newItem.user);
            }

            @Override
            public boolean areContentsTheSame(@NonNull DiscussionItem oldItem, @NonNull DiscussionItem newItem) {
                return oldItem.equals(newItem);
            }
        });
        this.context = context;
    }

    @NonNull
    @Override
    public DiscussionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DiscussionViewHolder(
                ItemDiscussionBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                ), context);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscussionViewHolder holder, int position) {
        holder.bind(getItem(position));
    }
}

class DiscussionViewHolder extends RecyclerView.ViewHolder {


    ItemDiscussionBinding binding;
    Context context;

    private List<String> users = new ArrayList<>();

    public DiscussionViewHolder(ItemDiscussionBinding binding, Context context) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
        users.add("hello");
    }

    void bind(DiscussionItem item) {
        if (users.contains(item.user)) {
            String user = item.user;
            String msg = item.message;
            SpannableString spannableText = new SpannableString(user + ": " + msg);
            int end = user.length() + 1;
            int start = 0;
            ClickableSpan clickable = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View view) {

                }

                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(context.getColor(R.color.yellow));
                }

            };
            spannableText.setSpan(
                    clickable,
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            binding.tvMessage.setMovementMethod(LinkMovementMethod.getInstance());
            binding.tvMessage.setText(spannableText);
        } else {
            binding.tvMessage.setText(item.user + ": " + item.message);
        }
    }

}

class DiscussionItem {
    String user;
    String message;

    public DiscussionItem(String user, String message) {
        this.user = user;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscussionItem that = (DiscussionItem) o;
        return Objects.equals(user, that.user) && Objects.equals(message, that.message);
    }

}

