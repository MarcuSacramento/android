package com.example.componentescustomizados;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class JogadorItem extends LinearLayout {

	TextView nome;
	TextView quantiaView;
	private View mValue;
	private ImageView mImage;

	public JogadorItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.Jogador, 0, 0);

		String nomeJogador = a.getString(R.styleable.Jogador_nome);
		String quantia = a.getString(R.styleable.Jogador_quantia);
		int cor = a.getColor(R.styleable.Jogador_cor,
				android.R.color.holo_blue_light);
		a.recycle();

		setOrientation(LinearLayout.HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.jogador_item, this, true);

		mImage = (ImageView) getChildAt(0);

		nome = (TextView) getChildAt(1);
		nome.setText(nomeJogador);

		mValue = getChildAt(2);
		mValue.setBackgroundColor(cor);

		quantiaView = (TextView) getChildAt(3);
		quantiaView.setText(quantia);

	}

	public JogadorItem(Context context) {
		this(context, null);
	}

	public void setValueColor(int color) {
		mValue.setBackgroundColor(color);
	}

	public void setImageVisible(boolean visible) {
		mImage.setVisibility(visible ? View.VISIBLE : View.GONE);
	}

}
