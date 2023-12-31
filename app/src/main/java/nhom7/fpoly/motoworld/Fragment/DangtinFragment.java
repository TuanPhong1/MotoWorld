package nhom7.fpoly.motoworld.Fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import nhom7.fpoly.motoworld.Adapter.HangXeSpinnerAdapter;
import nhom7.fpoly.motoworld.Dao.HangxeDao;
import nhom7.fpoly.motoworld.Dao.SanphamDao;
import nhom7.fpoly.motoworld.Dao.TkNguoiDungDao;
import nhom7.fpoly.motoworld.Model.Hangxe;
import nhom7.fpoly.motoworld.Model.Sanpham;
import nhom7.fpoly.motoworld.R;
import nhom7.fpoly.motoworld.databinding.FragmentDangtinBinding;

public class DangtinFragment extends Fragment {
    public static String imageUrl;

    private FragmentDangtinBinding binding;
    private View view;
    SanphamDao dao;
    HangxeDao hangxeDao;
    ArrayList<Hangxe> list;
    HangXeSpinnerAdapter hangXeSpinnerAdapter;
    final int REQUEST_CODE_CAMERA = 123;
    final int REQUEST_CODE_FOLDER = 456;
    int mahang;
    Sanpham item;
    Uri selectUri;
    int REQUEST_CODE = 2;
    public int PICK_IMAGE_REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDangtinBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        dao = new SanphamDao(getActivity());

        hangxeDao = new HangxeDao(getContext());
        list = new ArrayList<>();
        list = (ArrayList<Hangxe>) hangxeDao.getAll();
        hangXeSpinnerAdapter = new HangXeSpinnerAdapter(getContext(), list);
        binding.spnhangxe.setAdapter(hangXeSpinnerAdapter);

        binding.spnhangxe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mahang = list.get(i).getMahang();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.btnDangtin.setOnClickListener(view -> {
            String tensp = binding.edtTensp.getText().toString();
            String giaspStr = binding.edtGiasp.getText().toString();
            Integer giasp = Integer.parseInt(giaspStr);
            String loaixe = binding.edtLoaixe.getText().toString();
            String mausac = binding.edtMausac.getText().toString();
            String dongco = binding.edtDongco.getText().toString();
            String namsxStr = binding.edtNamsx.getText().toString();
            Integer namsx = Integer.parseInt(namsxStr);

            if (tensp.isEmpty() || loaixe.isEmpty() || mausac.isEmpty() || dongco.isEmpty()) {
                Toast.makeText(getActivity(), "Vui lòng nhập đúng trường dữ liệu!!!", Toast.LENGTH_SHORT).show();
            } else if (giasp < 0) {
                Toast.makeText(getActivity(), "Giá sản phẩm phải lớn hơn 0", Toast.LENGTH_SHORT).show();
            } else if (namsx < 1900 || namsx > 2040) {
                Toast.makeText(getActivity(), "Năm sản xuất không đúng", Toast.LENGTH_SHORT).show();
            }

            item = new Sanpham();
            item.setMahang(mahang);
            item.setTensp(tensp);
            item.setGia(giasp);
            item.setLoaixe(loaixe);
            item.setMauxe(mausac);
            item.setDongco(dongco);
            item.setNamsx(namsx);

            if (binding.chkTtdangtin.isChecked()) {
                item.setTrangthai(1);
            } else {
                item.setTrangthai(0);
            }

            String imagePath = getPathFromUri(selectUri);
            Log.d("TAG", "onClick: " + imagePath);
            item.setImage(imagePath);

            SharedPreferences pref = getActivity().getSharedPreferences("USER_FILE",Context.MODE_PRIVATE);
            String user = pref.getString("USERNAME","");
            String pass = pref.getString("PASSWORD","");

            TkNguoiDungDao tkDao = new TkNguoiDungDao(getActivity());
            int matk = tkDao.getMatkndFromTkNguoiDung(user,pass);
            Log.d("tk","tk=" +matk);

            long insert = dao.insert(item,matk);
            if (insert > 0) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                MuaHangFragment muaHangFragment = new MuaHangFragment();
                fragmentTransaction.replace(R.id.frmbottom, muaHangFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                Toast.makeText(getActivity(), "Thêm sản phẩn thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
            }
        });
        binding.imgAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
                    // Kiểm tra và yêu cầu quyền truy cập vào bộ nhớ ngoài
                    if (!Environment.isExternalStorageManager()) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                        startActivity(intent);
                        return;
                    }
                } else {
                    // Kiểm tra và yêu cầu quyền truy cập vào bộ nhớ ngoài
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Yêu cầu quyền truy cập vào bộ nhớ ngoài
                        ActivityCompat.requestPermissions(requireActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_CODE);
                        return;
                    }
                }
                // Mở trình chọn ảnh
                openImagePicker();
            }
        });

        return view;
    }

//    public void openCamera() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, REQUEST_CODE_CAMERA);
//    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp, mở picker ảnh
                openImagePicker();
            } else {
                // Quyền không được cấp, thông báo cho người dùng
                Toast.makeText(getContext(), "Bạn cần cấp quyền để chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getPathFromUri(Uri contentUri) {
        String filePath;
        Cursor cursor = getContext().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            filePath = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Nhận Uri của ảnh đã chọn
            selectUri = data.getData();
            Log.d("Image", "Image URI: " + selectUri.toString());
            DangtinFragment.imageUrl = selectUri.toString();
            // Tiến hành xử lý ảnh, ví dụ: hiển thị ảnh lên ImageView
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(selectUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (bitmap != null) {
                    binding.imgAnh.setImageBitmap(bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}