package com.example.laboratory_2

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import java.math.RoundingMode
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager>(R.id.viewPager)

        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(Task1Fragment(), "Завдання 1")

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }
}

class Task1Fragment : androidx.fragment.app.Fragment(R.layout.fragment_task1) {

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val inputFields = arrayOf(
            view.findViewById<EditText>(R.id.inputCoal),
            view.findViewById<EditText>(R.id.inputOil),
            view.findViewById<EditText>(R.id.inputGas)
        )

        val calculateButton = view.findViewById<Button>(R.id.calculateButton)
        val resultsTextView = view.findViewById<TextView>(R.id.resultsTextView)

        calculateButton.setOnClickListener {
            try {
                val coalAmount = inputFields[0].text.toString().toDouble()
                val oilAmount = inputFields[1].text.toString().toDouble()
                val gasAmount = inputFields[2].text.toString().toDouble()

                val Ar_coal = 25.20 / 100
                val a_vin_coal = 0.80
                val eta_zu_coal = 0.985
                val G_vin_coal = 0.015
                val Qri_coal = 20.47

                val Ar_oil = 0.15 / 100
                val a_vin_oil = 1.00
                val eta_zu_oil = 0.985
                val G_vin_oil = 0.00
                val Qri_oil = 40.40

                val k_coal = (Ar_coal * a_vin_coal * (1 - eta_zu_coal) * (1 - G_vin_coal)) / Qri_coal
                val k_oil = (Ar_oil * a_vin_oil * (1 - eta_zu_oil) * (1 - G_vin_oil)) / Qri_oil
                val k_gas = 0.0

                val E_coal = k_coal * Qri_coal * coalAmount
                val E_oil = k_oil * Qri_oil * oilAmount
                val E_gas = k_gas * gasAmount

                val df = DecimalFormat("#.##")
                df.roundingMode = RoundingMode.HALF_UP

                resultsTextView.text = """
                    Результати розрахунків:

                    Вугілля:
                    Показник емісії: ${df.format(k_coal * 1_000_000)} г/ГДж
                    Валовий викид: ${df.format(E_coal)} т

                    Мазут:
                    Показник емісії: ${df.format(k_oil * 1_000_000)} г/ГДж
                    Валовий викид: ${df.format(E_oil)} т

                    Природний газ:
                    Показник емісії: ${df.format(k_gas * 1_000_000)} г/ГДж
                    Валовий викид: ${df.format(E_gas)} т
                """.trimIndent()

            } catch (e: Exception) {
                resultsTextView.text = "Помилка вхідних даних. Перевірте введені значення."
            }
        }
    }
}

class ViewPagerAdapter(fm: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fm) {
    private val fragmentList = mutableListOf<androidx.fragment.app.Fragment>()
    private val fragmentTitleList = mutableListOf<String>()

    override fun getItem(position: Int): androidx.fragment.app.Fragment = fragmentList[position]

    override fun getCount(): Int = fragmentList.size

    override fun getPageTitle(position: Int): CharSequence? = fragmentTitleList[position]

    fun addFragment(fragment: androidx.fragment.app.Fragment, title: String) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }
}
