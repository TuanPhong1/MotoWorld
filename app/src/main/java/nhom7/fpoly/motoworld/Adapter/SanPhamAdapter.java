package nhom7.fpoly.motoworld.Adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;

import nhom7.fpoly.motoworld.Dao.HangxeDao;
import nhom7.fpoly.motoworld.Fragment.ChiTietSanPhamFragment;
import nhom7.fpoly.motoworld.Fragment.MuaHangFragment;
import nhom7.fpoly.motoworld.Model.Hangxe;
import nhom7.fpoly.motoworld.Model.Sanpham;
import nhom7.fpoly.motoworld.R;
import nhom7.fpoly.motoworld.databinding.ItemSanphamBinding;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Sanpham> list;
    private Activity activity;
    HangxeDao hangxeDao;
    private MuaHangFragment fragment;


    public void setSearchList(ArrayList<Sanpham> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    public SanPhamAdapter(Context context, ArrayList<Sanpham> list, Activity activity) {
        this.context = context;
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSanphamBinding binding = ItemSanphamBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        Sanpham sp = list.get(position);

        //Lấy dữ liệu ảnh lên recyclerview
        Uri imagesUri = Uri.parse(sp.getImage());
        Log.d("tag", "onBindViewHolder: " + imagesUri);
        holder.binding.listImage1.setImageURI(imagesUri);


//        String imgName = sp.getImage();
//        int resId = 0;
//        if (imgName != null && !imgName.isEmpty()) {
//            resId = ((Activity) context).getResources().getIdentifier(imgName, "drawable", ((Activity) context).getPackageName());
//        }

//        holder.binding.listImage1.setImageResource(resId);


        holder.binding.tvTensp.setText("TênSP:" + sp.getTensp());

        hangxeDao = new HangxeDao(context);
        Hangxe hangxe = hangxeDao.getID(String.valueOf(sp.getMahang()));
        holder.binding.tvHangsp.setText("Hãng:" + String.valueOf(hangxe.getTenhang()));

        holder.binding.tvGiasp.setText("Giá:" + String.valueOf(sp.getGia()));
        holder.binding.btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                editFragment(sp,holder.itemView.getContext());

            }
        });
        holder.binding.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.binding.cardviewsp.setOnClickListener(view -> {
            openFragment(sp,holder.itemView.getContext());
        });
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemSanphamBinding binding;

        public ViewHolder(@NonNull ItemSanphamBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void openFragment(final Sanpham sanPham, Context context) {
        if (context instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) context;
            // Tạo Bundle và truyền thông tin sản phẩm vào Bundle
            Bundle bundle = new Bundle();
            bundle.putSerializable("Chitietsanpham", sanPham);

            // Tạo Fragment và truyền Bundle vào Fragment
            ChiTietSanPhamFragment frg = new ChiTietSanPhamFragment();
            frg.setArguments(bundle);

            // Gửi sự kiện tới FragmentActivity để thay thế Fragment hiện tại bằng Fragment chỉnh sửa
            if (activity instanceof FragmentActivity) {
                fragmentActivity = (FragmentActivity) activity;
                FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frmbottom, frg);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
    }

}