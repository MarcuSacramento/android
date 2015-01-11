package com.kollecionador.taxman.util;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.widget.EditText;

public class FieldSalario extends EditText {
	/**
	 * Indicativo de atualização do campo.
	 */
	private boolean ib_update;

	/**
	 * Construtor da classe.
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public FieldSalario(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		ComponenteInicializar();
	}

	/**
	 * Construtor da classe.
	 * 
	 * @param context
	 * @param attrs
	 */
	public FieldSalario(Context context, AttributeSet attrs) {
		super(context, attrs);

		ComponenteInicializar();
	}

	/**
	 * Construtor da classe.
	 * 
	 * @param context
	 */
	public FieldSalario(Context context) {
		super(context);

		ComponenteInicializar();
	}

	/**
	 * Inicializa o componente.
	 */
	private void ComponenteInicializar() {

		this.setKeyListener(io_key_listener);

		this.setText("R$ 0,00");

		this.setSelection(1);

		this.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				String ls_valor_original = s.toString();

				if (ib_update) {
					ib_update = false;
					return;
				}

				if (ls_valor_original.length() < 16) {

					StringBuffer ls_mascara = new StringBuffer();

					ls_mascara.append(ls_valor_original.replaceAll("[^0-9]*",
							""));

					Long ln_number = new Long(ls_mascara.toString());

					//
					// Faz o replace necessário para manipulação das casas
					// decimais.
					//
					ls_mascara.replace(0, ls_mascara.length(),
							ln_number.toString());

					//
					// Se conteudo menor do que 3.
					//
					if (ls_mascara.length() < 3) {
						//
						// Se o tamanho for de 1 posição.
						//
						if (ls_mascara.length() == 1) {
							//
							// Insere os caracteres em ordem.
							//
							ls_mascara.insert(0, "0").insert(0, ",")
									.insert(0, "0");
						}

						//
						// Se o tamanho for de 2 posições.
						//
						else if (ls_mascara.length() == 2) {
							//
							// Insere os caracteres em ordem.
							//
							ls_mascara.insert(0, ",").insert(0, "0");
						}
					}

					//
					// Se tiver um tamanho maior que 3.
					//
					else {
						//
						// Insere a virgula.
						//
						ls_mascara.insert(ls_mascara.length() - 2, ",");
					}

					//
					// Se tiver o tamanho de 6 posições.
					//
					if (ls_mascara.length() > 6) {
						//
						// Insere o ponto.
						//
						ls_mascara.insert(ls_mascara.length() - 6, '.');

						//
						// Se o tamanho for maior do que 10.
						//
						if (ls_mascara.length() > 10) {
							//
							// Insere o ponto.
							//
							ls_mascara.insert(ls_mascara.length() - 10, '.');

							//
							// Se for maior do que 14.
							//
							if (ls_mascara.length() > 14) {
								//
								// insere o ponto.
								//
								ls_mascara.insert(ls_mascara.length() - 14, '.');
							}
						}
					}
					ls_mascara.insert(0, "R$ ");

					//
					// Define que o campo esta atualizando.
					//
					ib_update = true;

					//
					// Roda o novo valor.
					//
					FieldSalario.this.setText(ls_mascara);

					//
					// Faz a seleção.
					//
					FieldSalario.this.setSelection(ls_mascara.length());
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}
		});
	}

	private final KeylistenerNumber io_key_listener = new KeylistenerNumber();

	private class KeylistenerNumber extends NumberKeyListener {

		public int getInputType() {
			return InputType.TYPE_CLASS_NUMBER
					| InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;

		}

		@Override
		protected char[] getAcceptedChars() {
			return new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8',
					'9' };

		}
	}

	public String getValue() {

		return this.getText().replace(0, 3, "").toString().replace(".", "")
				.replace(",", ".").replace("R$", "").replace(" ", "");

	}

}