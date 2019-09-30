<?xml version="1.0" encoding="utf-8"?>
<layout
	xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    tools:context="${packageName}.${className}Activity">

    <data>

        <variable
            name="view"
            type="${packageName}.${className}View"/>

        <variable
            name="viewModel"
            type="${packageName}.${className}ViewModel"/>

        <variable
            name="viewState"
            type="${packageName}.${className}ViewState"/>

    </data>

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

    </androidx.constraintlayout.widget.ConstraintLayout>

</layout>
