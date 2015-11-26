package embedded.kookmin.ac.kr.siwoncamera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    final int CAMERA_CODE = 1;
    final int GALLERY_CODE =2;

    Button camera;
    Button gallery;
    ImageView imageView;
    Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.img_view);
        camera = (Button) findViewById(R.id.bt_camera);
        gallery = (Button) findViewById(R.id.bt_gallary);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCamera = new Intent();
                intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentCamera, CAMERA_CODE);
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGallery = new Intent(Intent.ACTION_PICK);
                intentGallery.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intentGallery.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intentGallery.putExtra("return-data", true);
                startActivityForResult(intentGallery, GALLERY_CODE);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_CODE) {
            Uri camera_uri = data.getData();
            if (camera_uri != null) {
                try {
                    photo = MediaStore.Images.Media.getBitmap(getContentResolver(), camera_uri);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Camera_Uri is null", Toast.LENGTH_SHORT).show();
                }
                photo = rotateImage(photo);
                imageView.setImageBitmap(photo);
            }
        }

        if (requestCode == GALLERY_CODE) {
            Uri gallery_uri = data.getData();
            if (gallery_uri != null) {
                try {
                    photo = MediaStore.Images.Media.getBitmap(getContentResolver(), gallery_uri);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Gallery_Uri is null", Toast.LENGTH_SHORT).show();
                }
                photo = rotateImage(photo);
                imageView.setImageBitmap(photo);
            }
        }
    }

    public Bitmap rotateImage(Bitmap image) {

        if(image.getWidth() > image.getHeight()) {

            Matrix matrix = new Matrix();
            matrix.postRotate(90);

            return Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);

        } else {
            return image;
        }

    }

}
