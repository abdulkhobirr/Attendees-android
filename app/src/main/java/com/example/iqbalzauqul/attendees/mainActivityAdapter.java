    package com.example.iqbalzauqul.attendees;

    import android.content.Context;
    import android.content.Intent;
    import android.support.v7.widget.CardView;
    import android.support.v7.widget.RecyclerView;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;

    import com.bumptech.glide.Glide;

    import java.util.List;

    /**
     * Created by iqbalzauqul on 18/11/17.
     */

    public class mainActivityAdapter extends RecyclerView.Adapter<mainActivityAdapter.ViewHolder>  {

        Context context;
        List<listItem> itemList;

        public mainActivityAdapter(Context context, List<listItem> itemList) {
            this.context = context;
            this.itemList = itemList;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_kelas,null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(mainActivityAdapter.ViewHolder holder, final int position) {
            holder.lblKelas.setText(itemList.get(position).getLblKelas());
            holder.lblPengabsen.setText(itemList.get(position).getLblPengabsen());

            Glide.with(context)
                    .load(itemList.get(position).getImgKelas())
                    .into(holder.imgKelas);


            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DetailActivity.class);
                    intent.putExtra("lblKelas", itemList.get(position).getLblKelas());
                    intent.putExtra("lblPengabsen", itemList.get(position).getLblPengabsen());
                    view.getContext().startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView lblKelas, lblPengabsen;
            ImageView imgKelas;
            CardView cardView;

            public ViewHolder(View itemView) {
                super(itemView);
                lblKelas = itemView.findViewById(R.id.lbl_kelasID);
                lblPengabsen = itemView.findViewById(R.id.lbl_pengabsenID);
                imgKelas = itemView.findViewById(R.id.imageID);
                cardView = itemView.findViewById(R.id.cardViewID);

            }
        }
    }
