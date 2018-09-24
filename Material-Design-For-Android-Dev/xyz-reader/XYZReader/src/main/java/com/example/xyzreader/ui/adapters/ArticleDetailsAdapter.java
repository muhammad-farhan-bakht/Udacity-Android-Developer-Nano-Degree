package com.example.xyzreader.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xyzreader.R;

import java.util.List;
import java.util.regex.Pattern;

public class ArticleDetailsAdapter extends RecyclerView.Adapter<ArticleDetailsAdapter.ArticleDetailsAdapterViewHolder> {
    private Context ctx;
    private List<String> body;

    public ArticleDetailsAdapter(Context ctx, List<String> body) {
        this.ctx = ctx;
        this.body = body;
    }

    @NonNull
    @Override
    public ArticleDetailsAdapter.ArticleDetailsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = R.layout.list_item_body_text;
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(layout, parent, false);

        return new ArticleDetailsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleDetailsAdapter.ArticleDetailsAdapterViewHolder holder, int position) {
        String paragraph = body.get(position);
        holder.bind(paragraph);
    }

    @Override
    public int getItemCount() {
        return body.size();
    }

    class ArticleDetailsAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView paragraphView;

        ArticleDetailsAdapterViewHolder(View itemView) {
            super(itemView);
            paragraphView = itemView.findViewById(R.id.tv_paragraph);
        }

        void bind(String paragraph) {
            String text = Pattern.compile("\r\n").matcher(paragraph).replaceAll(" ");
            paragraphView.setText(Html.fromHtml(text));
            paragraphView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}