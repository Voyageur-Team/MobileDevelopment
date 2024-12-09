package com.voyageur.application.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.voyageur.application.R

class OnboardingAdapter(private var context: Context) : PagerAdapter() {
    private var images: IntArray = intArrayOf(
        R.drawable.onboarding1,
        R.drawable.onboarding2,
        R.drawable.onboarding3,
        R.drawable.onboarding4
    )

    private var headings: IntArray = intArrayOf(
        R.string.title_onboarding1,
        R.string.title_onboarding2,
        R.string.title_onboarding3,
        R.string.title_onboarding4
    )

    private var description: IntArray = intArrayOf(
        R.string.desc_onboarding1,
        R.string.desc_onboarding2,
        R.string.desc_onboarding3,
        R.string.desc_onboarding4
    )

    override fun getCount(): Int {
        return headings.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ConstraintLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.item_onboarding, container, false)

        val slidetitleimage = view.findViewById<ImageView>(R.id.titleImage)
        val slideHeading = view.findViewById<TextView>(R.id.texttitle)
        val slideDescription = view.findViewById<TextView>(R.id.textdescription)

        slidetitleimage.setImageResource(images[position])
        slideHeading.setText(headings[position])
        slideDescription.setText(description[position])

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}
