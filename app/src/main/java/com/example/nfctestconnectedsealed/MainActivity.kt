package com.example.nfctestconnectedsealed

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.nfctestconnectedsealed.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        private lateinit var scanningViewModel: ScanningViewModel

        const val factoyReset = "049156EA\n" +
                "7C129000\n" +
                "44000000\n" +
                "00000000\n" +
                "0202FFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFE7"

        const val getSystemeStatus = "049156EA\n" +
                "7C129000\n" +
                "44000000\n" +
                "00000000\n" +
                "02030000\n" +
                "00000000\n" +
                "00000000\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFF2"

        const val joinLora = "049156EA\n" +
                "7C129000\n" +
                "44000000\n" +
                "00000000\n" +
                "0103FFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFE7"

        const val setDevEUI = "049156EA\n" +
                "7C129000\n" +
                "44000000\n" +
                "00000000\n" +
                "01040080\n" +
                "E115000A\n" +
                "9829FFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFF31"

        const  val startMaintMode = "049156EA\n" +
                "7C129000\n" +
                "44000000\n" +
                "00000000\n" +
                "0101FFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFE5"

        const val startProdMode = "049156EA\n" +
                "7C129000\n" +
                "44000000\n" +
                "00000000\n" +
                "0201FFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFE6"

        const val stopMaintMode = "049156EA\n" +
                "7C129000\n" +
                "44000000\n" +
                "00000000\n" +
                "0102FFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFFF\n" +
                "FFFFFFE6"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)
        scanningViewModel = ScanningViewModel()
        readWriteVisibility()
        processNfcScript()
    }

    private fun processNfcScript() {
        binding.SetDevEui.setOnClickListener {
            binding.writeEditText.setText(setDevEUI)
        }
        binding.startMaintMode.setOnClickListener {
            binding.writeEditText.setText(startMaintMode)
        }
        binding.startProdMod.setOnClickListener {
            binding.writeEditText.setText(startProdMode)
        }
        binding.getSystemStatus.setOnClickListener {
            binding.writeEditText.setText(getSystemeStatus)
        }
        binding.factoryReset.setOnClickListener {
            binding.writeEditText.setText(factoyReset)
        }
        binding.joinLora.setOnClickListener {
            binding.writeEditText.setText(joinLora)
        }
        binding.stopMaintMode.setOnClickListener {
            binding.writeEditText.setText(stopMaintMode)
        }
    }

    private fun readWriteVisibility() {
        binding.readButton.setOnClickListener {
            if (!binding.readTagTextview.isVisible){
                binding.readTagTextview.visibility = View.VISIBLE
                binding.writeContainer.visibility = View.GONE
            }
        }

        binding.writeButton.setOnClickListener {
            if (!binding.writeContainer.isVisible){
                binding.readTagTextview.visibility = View.GONE
                binding.writeContainer.visibility = View.VISIBLE
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        scanningViewModel.onActivityNewIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        scanningViewModel.enableScanning(this)
        if (binding.readTagTextview.isVisible){
            binding.readTagTextview.text = scanningViewModel.mifareUltralightInfo.value?.data
        }
    }
}