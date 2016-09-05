package ansteph.com.beecab.view.profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import ansteph.com.beecab.R;

public class ViewFullImage extends AppCompatActivity {
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_full_image);

        Intent intent = getIntent();
        int i = intent.getIntExtra(ImageListView.BITMAP_ID,0);

        imageView = (ImageView) findViewById(R.id.imageViewFull);
        imageView.setImageBitmap(GetAllImages.bitmaps[i]);
    }
}
