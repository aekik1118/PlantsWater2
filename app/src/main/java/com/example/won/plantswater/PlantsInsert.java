package com.example.won.plantswater;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PlantsInsert extends Activity implements View.OnClickListener {

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBULM = 1;
    private static final int CROP_FROM_IMAGE = 2;

    private Uri mImageCaptureUri;
    private ImageView iv_UserPhoto;
    private int id_view;
    private String absoultePath;

    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}; //권한 설정 변수
    private static final int MULTIPLE_PERMISSIONS = 101; //권한 동의 여부 문의 후 CallBack 함수에 쓰일 변수


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants_insert);

        //checkPermissions();


        iv_UserPhoto = (ImageView) this.findViewById(R.id.imageView2);
        Button btn_agreeJoin = (Button) this.findViewById(R.id.button);
        Button btn_add = (Button)this.findViewById(R.id.button1);
        final EditText et_name = (EditText)this.findViewById(R.id.editText);

        final RadioButton rbtn_harf = (RadioButton)this.findViewById(R.id.radioButton3);
        final RadioButton rbtn_one = (RadioButton)this.findViewById(R.id.radioButton2);
        final RadioButton rbtn_two = (RadioButton)this.findViewById(R.id.radioButton6);
        final RadioButton rbtn_tri = (RadioButton)this.findViewById(R.id.radioButton4);
        final RadioButton rbtn_week = (RadioButton)this.findViewById(R.id.radioButton5);
        rbtn_harf.setChecked(true);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pr=0;

                 if(et_name.getText().toString().length() == 0)
                 {
                     Toast.makeText(PlantsInsert.this, "이름을 입력해 주세요", Toast.LENGTH_SHORT).show();
                     return;
                 }
                 else if(rbtn_harf.isChecked())
                     pr = 12;
                 else if(rbtn_one.isChecked())
                     pr = 24;
                 else if(rbtn_two.isChecked())
                     pr = 48;
                 else if(rbtn_tri.isChecked())
                     pr = 72;
                 else
                     pr = 168;

                MainActivity.mDatabase.insertData(et_name.getText().toString(),null,pr);
                Toast.makeText(PlantsInsert.this, "식물 추가 완료", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PlantsInsert.this,MainActivity.class);
                startActivity(intent);
            }
        });
        btn_agreeJoin.setOnClickListener(this);
    }


    public void doTakePhotoAction() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
       // mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//sdk 24 이상, 누가(7.0)
            mImageCaptureUri = FileProvider.getUriForFile(PlantsInsert.this,"com.example.won.plantswater.provider",new File(Environment.getExternalStorageDirectory(), url));
        } else {//sdk 23 이하, 7.0 미만
            mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
        }

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);

    }

    public void doTakeAlbumAction() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        startActivityForResult(intent, PICK_FROM_ALBULM);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case PICK_FROM_ALBULM: {
                mImageCaptureUri = data.getData();
                Log.d("SmartWheel", mImageCaptureUri.getPath().toString());


            }

            case PICK_FROM_CAMERA: {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                startActivityForResult(intent, CROP_FROM_IMAGE);

                break;
            }

            case CROP_FROM_IMAGE: {

                if (resultCode != RESULT_OK) {
                    return;

                }
                final Bundle extras = data.getExtras();

                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/SmartWheel/" + System.currentTimeMillis() + ".jpg";

                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    iv_UserPhoto.setImageBitmap(photo);
                    storeCropImage(photo, filePath);
                    absoultePath = filePath;
                    break;
                }

                File f = new File(mImageCaptureUri.getPath());
                if (f.exists()) {
                    f.delete();
                }
            }
        }


    }
    @Override
    public void onClick(View v) {
        id_view = v.getId();
        if (v.getId() == R.id.button) {


            DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doTakePhotoAction();
                }
            };
            DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doTakeAlbumAction();
                }
            };

            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };

            new AlertDialog.Builder(this)
                    .setTitle("업로드할 이미지 선택")
                    .setPositiveButton("사진촬영", cameraListener)
                    .setNeutralButton("앨범선택", albumListener)
                    .setNegativeButton("취소", cancelListener)
                    .show();
        }
    }


private void storeCropImage(Bitmap bitmap, String filePath) {

    String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/smartWheel";
    File directory_SmartWheel = new File(dirPath);

    if(!directory_SmartWheel.exists())
        directory_SmartWheel.mkdir();

    File copyFile = new File(filePath);
    BufferedOutputStream out = null;

    try {

        copyFile.createNewFile();
        out = new BufferedOutputStream(new FileOutputStream(copyFile));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));

        out.flush();
        out.close();
    } catch (Exception e) {
        e.printStackTrace();
    }

    }

    private boolean checkPermissions() {
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);
            if (result != PackageManager.PERMISSION_GRANTED) { //사용자가 해당 권한을 가지고 있지 않을 경우 리스트에 해당 권한명 추가
                permissionList.add(pm);
            }
        }
        if (!permissionList.isEmpty()) { //권한이 추가되었으면 해당 리스트가 empty가 아니므로 request 즉 권한을 요청합니다.
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(this.permissions[0])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();
                            }
                        } else if (permissions[i].equals(this.permissions[1])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        } else if (permissions[i].equals(this.permissions[2])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        }
                    }
                } else {
                    showNoPermissionToastAndFinish();
                }
                return;
            }
        }
    }

    //권한 획득에 동의를 하지 않았을 경우 아래 Toast 메세지를 띄우며 해당 Activity를 종료시킵니다.
    private void showNoPermissionToastAndFinish() {
        Toast.makeText(this, "권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
        finish();
    }



}




