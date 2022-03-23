package kg.programm.programmingapp

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationBarView
import kg.programm.programmingapp.databinding.ActivityMainBinding
import kg.programm.programmingapp.ui.main.MainPagerAdapter

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var sectionPagerAdapter: MainPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorWhite)

        viewPagerListener()
        binding.navBottomView.setOnItemSelectedListener(this)
        sectionPagerAdapter = MainPagerAdapter(this)
        binding.viewPager.adapter = sectionPagerAdapter
        binding.viewPager.isUserInputEnabled = false

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.viewPager.currentItem = when (item.itemId) {
            R.id.homeTab -> 0
            R.id.lectureTab -> 1
            else -> 2
        }
        return true
    }

    private fun viewPagerListener() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        binding.navBottomView.menu.findItem(R.id.homeTab).isChecked = true
                    }
                    1 -> {
                        binding.navBottomView.menu.findItem(R.id.lectureTab).isChecked = true
                    }
                    else -> {
                        binding.navBottomView.menu.findItem(R.id.profileTab).isChecked = true
                    }
                }
            }
        })
    }


}