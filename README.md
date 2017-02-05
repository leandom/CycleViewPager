# CycleViewPager
A ViewPager that can scroll automatically.
![image](https://github.com/leandom/CycleViewPager/blob/master/image/demo.gif)
# Gradle Dependency
add the library to your module build.gradle
```
dependencies {
    compile 'com.leandom:cycleviewpager:1.0.0@aar'
}
```

# Simple Usage

step1: Config in xml
```
<FrameLayout
    android:layout_width="match_parent"
    android:layout_height="140dp">

    <com.leandom.banner.CycleViewPager
        android:id="@+id/banner"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:autoScrollInterval="4000"
        app:pageSwitchDuration="800" />

    <com.leandom.banner.ViewPagerIndicator
        android:id="@+id/viewpager_indicator"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom"
        android:background="#30ffffff"
        android:gravity="center"
        android:paddingBottom="10dp"
        android:paddingLeft="10dp"
        android:paddingRight="10dp"
        android:paddingTop="10dp" />
</FrameLayout>
``` 

step2: Use in java code

```java
// Implement the CyclePagerAdapter interface
public class SimpleBannerAdapter extends CyclePagerAdapter {

    private static final int[] drawableIds = new int[]{R.drawable.desert, R.drawable.koala,
            R.drawable.jellyfish, R.drawable.hydrangeas};
    private Context mContext;

    public SimpleBannerAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getRealCount() {
        return drawableIds.length;
    }

    @Override
    public View getViewAtRealPosition(final int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.banner_item, container, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        imageView.setImageResource(drawableIds[position % drawableIds.length]);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "click at " + position, Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }
}

// on Activity
public class SimpleExampleActivity extends AppCompatActivity {

    private CycleViewPager mCyclellViewPager;
    private ViewPagerIndicator mIndicator;
    private SimpleBannerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_example);
        mCyclellViewPager = (CycleViewPager) findViewById(R.id.banner);
        mIndicator = (ViewPagerIndicator) findViewById(R.id.viewpager_indicator);
        mPagerAdapter = new SimpleBannerAdapter(this);
        mCyclellViewPager.setAdapter(mPagerAdapter);
        mIndicator.bindToViewPager(mCyclellViewPager);

    }
    
    @Override
    protected void onStart() {
        super.onStart();
        mCyclellViewPager.startAutoScroll();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCyclellViewPager.stopAutoScroll();
    }

}

```