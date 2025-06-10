package dev.emirbaycan.mefab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.emirbaycan.mefab.R

public class FabTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.floating_button) // Or use test_overlay_layout if you make one
    }
}