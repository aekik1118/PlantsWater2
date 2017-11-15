package com.example.won.plantswater;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class PlantsInsert extends Activity implements View.OnClickListener {

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBULM = 1;
    private static final int CROP_FROM_IMAGE = 2;

    private Uri mlmageCaptureUri;
    private ImageView iv_UserPhoto;
    private int id_view;
    private String absoultePath;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants_insert);


        iv_UserPhoto = (ImageView) this.findViewById(R.id.imageView2);
        Button btn_agreeJoin = (Button) this.findViewById(R.id.button);

        btn_agreeJoin.setOnClickListener(this);
    }

    public void doTakePhotoAction() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        mlmageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mlmageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);

    }

    public void doTakeAlbumAction() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBULM);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case PICK_FROM_ALBULM: {
                mlmageCaptureUri = data.getData();
                Log.d("SmartWheel", mlmageCaptureUri.getPath().toString());
            }

            case PICK_FROM_CAMERA: {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mlmageCaptureUri, "image/*");

                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
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

                File f = new File(mlmageCaptureUri.getPath());
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

}




